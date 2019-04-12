package edu.mit.compilers.trees;

import java.util.Arrays;

import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.IR_decl_Node.IrDeclaration;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.ImportTable;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class SemanticCheckerNode implements IrNodeVistor{
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
		checkUsedBeforeDecledErr(envs.peekVariables(), assign);
		checkTypeMatchForAssignment(assign);
		return hasError;
	}
	
	public void checkUsedBeforeDecledErr(VariableTable v, IrAssignment assign) {
		IrLocation loc = assign.getLhs();
		checkLocationUsedBeforeDecled(loc, v);
		IrExpression expr = assign.getRhs();
		if(expr instanceof IrLocation) {
			checkLocationUsedBeforeDecled((IrLocation) expr, v);
		}
		
	}
	public void checkTypeMatchForAssignment(IrAssignment assign) {
		IrLocation loc = assign.getLhs();
		IrType leftType = envs.peekVariables().getVariableType(loc.getName());
		IrExpression expr = assign.getRhs();
		IrType rightType = getIrExpressionType(expr);
		if(! leftType.equals(rightType)) {
			hasError = true;
			errorMessage.append(ErrorReport.AssignmentTypeUnmatched(leftType, rightType, loc));
		}
	}
	
	public IrType getIrExpressionType(IrExpression expr) {
		IrType res;
		if(expr instanceof IrLocation)
			res = envs.peekVariables().getVariableType(((IrLocation)expr).getName());
		else if(expr instanceof IrLiteral)
			res = ((IrLiteral)expr).getType();
		else
			res = new IrType();
		return res;
	}
	
	public void checkLocationUsedBeforeDecled(IrLocation loc, VariableTable v) {
		if(!v.containsVariable(loc.getName())) {
			errorMessage.append(ErrorReport.UsedBeforeDecledError(loc));
			hasError = true;
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
