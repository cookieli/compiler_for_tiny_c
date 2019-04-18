package edu.mit.compilers.trees;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.IrType.Type;
import edu.mit.compilers.IR.IR_decl_Node.IrDeclaration;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLenExpr;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.ImportTable;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class SemanticCheckerNode implements IrNodeVistor{
	
	public final String[] mustBeIntSymbol = {"+", "-","*","/", "+=", "-="};
	public final String[] comOp = {"<", ">", ">=", "<=", "==", "!="};
	public final String[] condOp = {"&&", "||"};
	public EnvStack envs;
	public boolean hasError;
	public StringBuilder errorMessage;
	public SemanticCheckerNode() {
		envs = new EnvStack();
		hasError = false;
		errorMessage = new StringBuilder();
	}
	@Override
	public boolean visit(IrProgram p) {
		// TODO Auto-generated method stub
		envs.pushVariables(p.globalVariableTable);
		envs.pushMethods(p.globalMethodTable);
		checkReDeclariationErr(envs.peekVariables(), envs.peekMethod(), p.importIR);
		checkMainMethodFormat(envs.peekMethod(), p);
		for(MethodDecl m: p.globalMethodTable) {
			m.accept(this);
		}
		if(hasError)
			System.out.println(errorMessage.toString());
		
		envs.popVariables();
		return hasError;
	}
	
	@Override
	public boolean visit(MethodDecl m) {
		// TODO Auto-generated method stub
		envs.pushVariables(m.localVars);
		checkReDeclariationErr(envs.peekVariables(), null, null);
		for(IrStatement s: m.getStatements()) {
			//System.out.println(s.getName());
			s.accept(this);
		}
		envs.popVariables();
		return hasError;
	}
	
	@Override
	public boolean visit(IrAssignment assign) {
		// TODO Auto-generated method stub
		//checkUsedBeforeDecledErr(envs.peekVariables(), assign);
		checkTypeMatchForAssignment(assign, envs.peekVariables(), envs.peekMethod());
		return hasError;
	}
	
	
	
	public void checkMainMethodFormat(MethodTable m, IrProgram p) {
		if(! m.lastMethodIsMain()) {
			hasError = true;
			errorMessage.append(ErrorReport.NotHaveMainErr(p));
		} else {
			MethodDecl mainMethod = m.getLastMethod();
			if(!(mainMethod.getMethodType().getType() == IrType.Type.VOID)) {
				hasError = true;
				errorMessage.append(ErrorReport.MainHasWrongType(mainMethod));
			}
			if(mainMethod.hasParameter()) {
				hasError = true;
				errorMessage.append(ErrorReport.MainHasParaErr(mainMethod));
			}
		}
	}
	
	public boolean checkUsedBeforeDecledErr(VariableTable v, MethodTable m, IrAssignment assign)  {
		IrLocation loc = assign.getLhs();
		boolean lhsUsedBeforeDecled = checkLocationUsedBeforeDecled(loc, v);
		IrExpression expr = assign.getRhs();
		boolean rhsUsedBeforeDecled = false;;
		if(expr instanceof BinaryExpression) {
			rhsUsedBeforeDecled = checkBinaryExprOperandUsedBeforeDecled((BinaryExpression) expr, v, m);
		}
		else if(expr instanceof IrOperand)
			rhsUsedBeforeDecled = checkUsedBeforeDecled(expr, v, m);
		return lhsUsedBeforeDecled || rhsUsedBeforeDecled;
	}
	
	public boolean checkUsedBeforeDecled(IrExpression expr, VariableTable v, MethodTable m) {
		//System.out.println(expr.getName());
		if (expr instanceof IrLocation)
			return checkLocationUsedBeforeDecled((IrLocation)expr, v);
		if(expr instanceof IrFuncInvocation)
			return checkFuncInvokeUsedBeforeDecled((IrFuncInvocation)expr, m);
		if(expr instanceof IrLenExpr)
			return checkLenExprErr((IrLenExpr) expr, v);
		if(expr instanceof IrLiteral)
			return false;
		else
			
			throw new IllegalArgumentException("the expr isn't IrLocation or IrFuncInvocation "+ expr.getName());
	}
	
	public boolean checkBinaryExprOperandUsedBeforeDecled(BinaryExpression expr, VariableTable v, MethodTable m) {
		boolean hasErr =false;
		for(IrExpression e: expr.operandList()) {
			if(checkUsedBeforeDecled(e, v, m))  hasErr = true;
		}
		return hasErr;
	}
	
	public void checkTypeMatchForAssignment(IrAssignment assign, VariableTable v, MethodTable m) {
		IrLocation loc = assign.getLhs();
		if(checkUsedBeforeDecledErr(v, m, assign))  return;
		String symbol = assign.getSymbol();
		IrType leftType = v.getVariableType(loc.getId());
		if(Arrays.asList(mustBeIntSymbol).contains(symbol)){
			if(!leftType.equals(new IrType(IrType.Type.INT))) {
				hasError = true;
				errorMessage.append(ErrorReport.AssignmentLhsSouldBeErr(loc, new IrType(IrType.Type.INT), leftType));
			} 
		}
		IrExpression expr = assign.getRhs();
		if(expr == null) return;
		//if(expr instanceof IrLenExpr)
		//	if(checkLenExprErr((IrLenExpr) expr, v))  return;
		IrType rightType = getIrExpressionType(expr, v, m);
		if(! leftType.equals(rightType)) {
			hasError = true;
			errorMessage.append(ErrorReport.AssignmentTypeUnmatched(leftType, rightType, loc));
		}
	}
	public IrType getIrExpressionType(IrExpression expr, VariableTable v, MethodTable m) {
		if(expr instanceof BinaryExpression)
			return getBinaryExpressionType((BinaryExpression)expr, v, m);
		else
			return getIrOperandType(expr, v, m);
		
	}
	public IrType getIrOperandType(IrExpression expr, VariableTable v, MethodTable m) {
		IrType res;
		if(expr instanceof IrLocation)
			res = v.getVariableType(((IrLocation)expr).getId());
		else if(expr instanceof IrLiteral)
			res = ((IrLiteral)expr).getType();
		else if(expr instanceof IrFuncInvocation)
			res = m.getMethodType(((IrFuncInvocation)expr).getId());
		else if(expr instanceof IrLenExpr)
			res = new IrType(IrType.Type.INT);
		else
			res = new IrType();
		return res;
	}
	
	public IrType getBinaryExpressionType(BinaryExpression expr, VariableTable v, MethodTable m) {
		if(Arrays.asList(mustBeIntSymbol).contains(expr.getSymbol())) {
			return getArithmeticExpressionType(expr, v, m);
		} else if(Arrays.asList(comOp).contains(expr.getSymbol())) {
			return getComOpExprType(expr, v, m);
		} else if(Arrays.asList(condOp).contains(expr.getSymbol()))
			return getCondExprType(expr, v, m);
		
		return new IrType(IrType.Type.UNSPECIFIED);
	}
	
	public IrType getComOpExprType(BinaryExpression expr, VariableTable v, MethodTable m) {
		return getBinaryExprType(expr, v, m, new IrType(IrType.Type.BOOL), new IrType(Type.INT));
		
	}
	
	public IrType getBinaryExprType(BinaryExpression expr, VariableTable v, MethodTable m, IrType resultType, IrType operandType) {
		IrType wrongType = new IrType(IrType.Type.UNSPECIFIED);
		IrExpression lhs = expr.getlhs();
		IrExpression rhs = expr.getrhs();
		IrType leftType = getIrExpressionType(lhs, v, m);
		IrType rightType = getIrExpressionType(rhs, v, m);
		if(!leftType.equals(operandType)) {
			hasError = true;
			errorMessage.append(ErrorReport.typeNotMatchForBinary(lhs, leftType, operandType));
			return wrongType;
		}
		if(!rightType.equals(operandType)) {
			hasError = true;
			errorMessage.append(ErrorReport.typeNotMatchForBinary(rhs, rightType, operandType));
			return wrongType;
		}
		return resultType;
	}
	
	public IrType getArithmeticExpressionType(BinaryExpression expr, VariableTable v, MethodTable m) {
		return getBinaryExprType(expr, v, m, new IrType(IrType.Type.INT), new IrType(IrType.Type.INT));
	}
	
	public IrType getCondExprType(BinaryExpression expr, VariableTable v, MethodTable m) {
		return getBinaryExprType(expr, v, m, new IrType(IrType.Type.BOOL), new IrType(IrType.Type.BOOL));
	}
	
	public boolean checkLenExprErr(IrLenExpr len, VariableTable v) {
		IrLocation opr = len.getOperand();
		if(!v.containsVariable(opr.getId())) {
			hasError = true;
			errorMessage.append(ErrorReport.UsedBeforeDecledError(opr));
			return true;
		} else if(!v.get(opr.getId()).isArray()) {
			hasError = true;
			errorMessage.append(ErrorReport.LenOprNotArray(opr));
			return true;
		}
		return false;
	}
	public boolean checkLocationUsedBeforeDecled(IrLocation loc, VariableTable v) {
		
		if(!v.containsVariable(loc.getId())) {
			errorMessage.append(ErrorReport.UsedBeforeDecledError(loc));
			hasError = true;
			return true;
		}
		return false;
	}
	
	public boolean checkFuncInvokeUsedBeforeDecled(IrFuncInvocation func, MethodTable m) {
		if(!m.containsMethod(func.getId())) {
			hasError = true;
			errorMessage.append(ErrorReport.UsedBeforeDecledError(func));
			return true;
		}
		return false;
	}
	
	public void checkReDeclariationErr(VariableTable v, MethodTable m, ImportTable i) {
		int size = 0;
		if(v != null) size += v.toArray().length;
		if(m != null) size += m.toArray().length;
		if(i != null) size += i.toArray().length;
		IrDeclaration[] decls = new IrDeclaration[size];
		int offset = 0;
		if(v != null) {
			System.arraycopy(v.toArray(), 0, decls, offset, v.toArray().length);
			offset += v.toArray().length;
		}
		if(m != null) {
			System.arraycopy(m.toArray(), 0, decls, offset, m.toArray().length);
			offset += m.toArray().length;
		}
		if( i != null)
			System.arraycopy(i.toArray(), 0, decls, offset, i.toArray().length);
		checkReDeclariationErr(decls);
	}
	
	
	public void checkReDeclariationErr(IrDeclaration[] decls) {
		Arrays.sort(decls);
		for(int i = 1; i < decls.length; i++) {
			if(decls[i].equals(decls[i-1])) {
				if(hasError == false)  hasError = true;
				errorMessage.append(ErrorReport.redeclariationError(decls[i], decls[i-1]));
			}
		}
	}
	
	
}
