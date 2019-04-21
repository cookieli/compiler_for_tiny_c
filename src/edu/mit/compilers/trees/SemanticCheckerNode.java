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
import edu.mit.compilers.IR.expr.TernaryExpression;
import edu.mit.compilers.IR.expr.UnaryExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLenExpr;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.LoopStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrForBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrWhileBlock;
import edu.mit.compilers.SymbolTables.ImportTable;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class SemanticCheckerNode implements IrNodeVistor{
	
	public final String[] mustBeIntSymbol = {"+", "-","*","/", "+=", "-="};
	public final String[] comOp = {"<", ">", ">=", "<=", "==", "!="};
	public final String[] condOp = {"&&", "||"};
	public EnvStack envs;
	public boolean hasError;
	public ImportTable importIr = null;
	public boolean inLoop = false;
	public MethodDecl currentMethod = null;
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
		if(p.getImportTable() != null) {
			importIr = p.getImportTable();
		}
		for(MethodDecl m: p.globalMethodTable) {
			currentMethod = m;
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
	public boolean visit(FuncInvokeStatement func) {
		checkFuncInvokeUsedBeforeDecled(func.getFunc(), envs.peekVariables(), envs.peekMethod());
		return hasError;
	}
	
	@Override
	public boolean visit(IfBlock if_code) {
		if(!checkUsedBeforeDecledForExprList(if_code.getBoolExpr(), envs.peekVariables(), envs.peekMethod()))
			typeCheckForExpr(if_code.getBoolExpr(), envs.peekVariables(), envs.peekMethod(), new IrType(Type.BOOL));
		if_code.getTrueBlock().accept(this);
		if(if_code.getFalseBlock() != null)
			if_code.getFalseBlock().accept(this);
		return hasError;
	}
	@Override
	public boolean visit(Return_Assignment r) {
		if(r.getExpr() == null && !(currentMethod.getMethodType().equals(new IrType(Type.VOID)))) {
			hasError = true;
			errorMessage.append(ErrorReport.methodReturnTypeNotMatch(r, null, currentMethod));
		}
		else if(r.getExpr() != null) {
			IrType retType = getIrExpressionType(r.getExpr(), envs.peekVariables(), envs.peekMethod());
			if(!retType.equals(currentMethod.getMethodType())) {
				hasError = true;
				errorMessage.append(ErrorReport.methodReturnTypeNotMatch(r, retType, currentMethod));
			}
		}
		return hasError;
	}
	
	@Override
	public boolean visit(IrBlock block) {
		envs.pushVariables(block.localVars);
		inLoop = true;
		checkReDeclariationErr(envs.peekVariables(), null, null);
		for(IrStatement s: block.getStatements()) {
			s.accept(this);
		}
		envs.popVariables();
		return hasError;
	}
	@Override
	public boolean visit(IrWhileBlock whileBlock) {
		if(!checkUsedBeforeDecledForExprList(whileBlock.getBoolExpr(), envs.peekVariables(), envs.peekMethod()))
			typeCheckForExpr(whileBlock.getBoolExpr(), envs.peekVariables(), envs.peekMethod(), new IrType(Type.BOOL));
		boolean tempLoop = inLoop;
		inLoop = true;
		whileBlock.getCodeBlock().accept(this);
		inLoop = tempLoop;
		return hasError;
	}
	@Override
	public boolean visit(IrForBlock forBlock) {
		checkUsedBeforeDecledErr(envs.peekVariables(), envs.peekMethod(), forBlock.getInitialAssign());
		if(!checkUsedBeforeDecledForExprList(forBlock.getBoolExpr(), envs.peekVariables(), envs.peekMethod()))
			typeCheckForExpr(forBlock.getBoolExpr(), envs.peekVariables(), envs.peekMethod(), new IrType(Type.BOOL));
		checkUsedBeforeDecledErr(envs.peekVariables(), envs.peekMethod(), forBlock.getStepFunction());
		boolean tempLoop = inLoop;
		inLoop = true;
		forBlock.getBlock().accept(this);
		inLoop = tempLoop;
		return hasError;
	}
	@Override
	public boolean visit(LoopStatement l) {
		if(inLoop != true) {
			hasError = true;
			errorMessage.append(ErrorReport.loopStatementNotInLoop(l));
		}
		return hasError;
	}
	
	
	@Override
	public boolean visit(IrAssignment assign) {
		// TODO Auto-generated method stub
		//checkUsedBeforeDecledErr(envs.peekVariables(), assign);
		checkTypeMatchForAssignment(assign, envs.peekVariables(), envs.peekMethod());
		return hasError;
	}
	
	public void typeCheckForExpr(IrExpression expr, VariableTable v, MethodTable m, IrType shouldBe) {
		IrType type = getIrExpressionType(expr, v, m);
		if(!type.equals(shouldBe)) {
			hasError = true;
			errorMessage.append(ErrorReport.typeNotMatchForExpr(expr, type, shouldBe));
		}
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
		boolean lhsUsedBeforeDecled = checkUsedBeforeDecledForExprList(loc, v, m);
		IrExpression expr = assign.getRhs();
		boolean rhsUsedBeforeDecled = false;;
		if(expr != null)
			rhsUsedBeforeDecled = checkUsedBeforeDecledForExprList(expr, v, m);
		return lhsUsedBeforeDecled || rhsUsedBeforeDecled;
	}
	
	public boolean checkUsedBeforeDecledForExprList(IrExpression expr, VariableTable v, MethodTable m) {
		boolean hasErr = false;
		for(IrOperand o: expr.operandList()) {
			if(checkUsedBeforeDecled(o, v, m))
				hasErr = true;
		}
		return hasErr;
	}
	public boolean checkUsedBeforeDecled(IrExpression expr, VariableTable v, MethodTable m) {
		//System.out.println(expr.getName());
		if (expr instanceof IrLocation)
			return checkLocationUsedBeforeDecled((IrLocation)expr, v, m);
		if(expr instanceof IrFuncInvocation)
			return checkFuncInvokeUsedBeforeDecled((IrFuncInvocation)expr, v, m);
		if(expr instanceof IrLenExpr)
			return checkLenExprErr((IrLenExpr) expr, v);
		if(expr instanceof IrLiteral)
			return false;
		else
			
			throw new IllegalArgumentException("the expr isn't IrLocation or IrFuncInvocation "+ expr.getName());
	}
	
	
	public void checkTypeMatchForAssignment(IrAssignment assign, VariableTable v, MethodTable m) {
		IrLocation loc = assign.getLhs();
		if(checkUsedBeforeDecledErr(v, m, assign))  return;
		String symbol = assign.getSymbol();
		IrType leftType = getIrExpressionType(loc, v, m);
		if(Arrays.asList(mustBeIntSymbol).contains(symbol)){
			if(!leftType.equals(new IrType(IrType.Type.INT))) {
				hasError = true;
				errorMessage.append(ErrorReport.AssignmentLhsSouldBeErr(loc, new IrType(IrType.Type.INT), leftType));
			} 
		}
		IrExpression expr = assign.getRhs();
		if(expr == null) return;
		IrType rightType = getIrExpressionType(expr, v, m);
		if(! leftType.equals(rightType)) {
			hasError = true;
			errorMessage.append(ErrorReport.AssignmentTypeUnmatched(leftType, rightType, loc));
		}
	}
	public IrType getIrExpressionType(IrExpression expr, VariableTable v, MethodTable m) {
		if(expr instanceof BinaryExpression)
			return getBinaryExpressionType((BinaryExpression)expr, v, m);
		else if(expr instanceof UnaryExpression)
			return getIrUnaryExpressionType((UnaryExpression) expr, v, m);
		else if(expr instanceof TernaryExpression)
			return getTernaryExprType((TernaryExpression) expr, v, m);
		else
			return getIrOperandType(expr, v, m);
		
	}
	
	private IrType typeTransformation(IrType type) {
		IrType boolArrayType = new IrType(IrType.Type.BOOL_ARRAY);
		IrType intArrayType = new IrType(IrType.Type.INT_ARRAY);
		IrType boolType = new IrType(IrType.Type.BOOL);
		IrType intType = new IrType(IrType.Type.INT);
		if(type.equals(boolArrayType))
			return boolType;
		else if(type.equals(intArrayType))
			return intType;
		else 
			return type;
	}
	public IrType getIrOperandType(IrExpression expr, VariableTable v, MethodTable m) {
		IrType res;
		if(expr instanceof IrLocation) {
			res = v.getVariableType(((IrLocation)expr).getId());
			res = typeTransformation(res);
		}
		else if(expr instanceof IrLiteral)
			res = ((IrLiteral)expr).getType();
		else if(expr instanceof IrFuncInvocation) {
			if(importIr.contains(((IrFuncInvocation)expr).getId()))
				res = new IrType(Type.NOTKNOWN);
			else
				res = m.getMethodType(((IrFuncInvocation)expr).getId());
		}
		else if(expr instanceof IrLenExpr)
			res = new IrType(IrType.Type.INT);
		else
			res = new IrType();
		return res;
	}
	
	private IrType getIrUnaryExprType(UnaryExpression expr, VariableTable v, MethodTable m, IrType shouldBe) {
		IrExpression child = expr.getIrExpression();
		IrType childType = getIrExpressionType(child, v, m);
		if(!childType.equals(shouldBe)) {
			hasError = true;
			errorMessage.append(ErrorReport.typeNotMatchForExpr(expr, childType, shouldBe));
			return new IrType(IrType.Type.UNSPECIFIED);
		} else
			return shouldBe;
	}
	
	public IrType getIrUnaryExpressionType(UnaryExpression expr, VariableTable v, MethodTable m) {
		if(expr.getSymbol().equals("!")){
			return getIrUnaryExprType(expr, v, m, new IrType(Type.BOOL));
		} else if(expr.getSymbol().equals("-"))
			return getIrUnaryExprType(expr, v, m, new IrType(Type.INT));
		return new IrType(IrType.Type.UNSPECIFIED);
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
			errorMessage.append(ErrorReport.typeNotMatchForExpr(lhs, leftType, operandType));
			return wrongType;
		}
		if(!rightType.equals(operandType)) {
			hasError = true;
			errorMessage.append(ErrorReport.typeNotMatchForExpr(rhs, rightType, operandType));
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
	
	public IrType getTernaryExprType(TernaryExpression expr, VariableTable v, MethodTable m) {
		IrExpression cond = expr.getCondExpr();
		IrType condShouldBe = new IrType(Type.BOOL);
		IrType condType = getIrExpressionType(cond, v, m);
		if(!condType.equals(condShouldBe)) {
			hasError = true;
			errorMessage.append(ErrorReport.typeNotMatchForExpr(cond, condType, condShouldBe));
			return new IrType(Type.UNSPECIFIED);
		}
		IrExpression firstExpr = expr.getFirstExpr();
		IrExpression secondExpr = expr.getSecondExpr();
		IrType firstType = getIrExpressionType(firstExpr, v, m);
		IrType secondType = getIrExpressionType(secondExpr, v, m);
		if(firstType != null && secondType != null && (!firstType.equals(secondType))) {
			hasError = true;
			errorMessage.append(ErrorReport.typeNotMatchForTernary(expr, firstType, secondType));
			return new IrType(Type.UNSPECIFIED);
		}
		return firstType;
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
	public boolean checkLocationUsedBeforeDecled(IrLocation loc, VariableTable v, MethodTable m) {
		
		if(!v.containsVariable(loc.getId())) {
			errorMessage.append(ErrorReport.UsedBeforeDecledError(loc));
			hasError = true;
			return true;
		} else if(loc.locationIsArray()) {
			return checkArrayHasWrongFormat(loc, v, m);
		}
		return false;
	}
	
	public boolean checkArrayHasWrongFormat(IrLocation loc, VariableTable v, MethodTable m) {
		if(loc.locationIsArray()) {
			IrExpression size = loc.getSizeExpr();
			if(checkUsedBeforeDecledForExprList(size, v, m))   return true;
			IrType type = getIrExpressionType(size, v, m);
			if(!type.equals(new IrType(Type.INT))) {
				hasError = true;
				errorMessage.append(ErrorReport.ArraySubscriptNotInt(loc));
				return true;
			} 
			return false;
		} else
			throw new IllegalArgumentException("the location isn't array ");
	}
	
	public boolean checkFuncInvokeUsedBeforeDecled(IrFuncInvocation func, VariableTable v, MethodTable m) {
		if(importIr.contains(func.getId()))
			return false;
		if(!m.containsMethod(func.getId())) {
			hasError = true;
			errorMessage.append(ErrorReport.UsedBeforeDecledError(func));
			return true;
		} else {
			MethodDecl method = m.get(func.getId());
			if(method.getParameterSize() != func.getFuncArgNum()) {
				hasError = true;
				errorMessage.append(ErrorReport.notHaveSamePara(func, method));
				return true;
			} else {
				boolean hasErr = false;
				for(int i = 0; i < func.getFuncArgNum(); i++) {
					if(checkUsedBeforeDecledForExprList(func.getFunctionArg(i), v, m)) continue;
					IrType realType = getIrExpressionType(func.getFunctionArg(i), v, m);
					IrType shouldBeType = method.getParameterType(i);
					if(!realType.equals(shouldBeType)){
						hasErr = true;
						errorMessage.append(ErrorReport.notHaveSamePara(func, method, i, shouldBeType, realType));
					}
				}
				return hasErr;
			}
				
		}
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
