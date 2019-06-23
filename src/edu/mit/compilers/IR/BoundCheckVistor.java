package edu.mit.compilers.IR;

import edu.mit.compilers.IR.IR_decl_Node.ArrayDecl;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrForBlock;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.trees.ErrorReport;

public class BoundCheckVistor extends IrWithTemp{
	
	
	public static String ErrMsgForBoundCheck(IrLocation loc) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		sb.append(ErrorReport.getErrLocation(loc));
		sb.append("out of array bound error for " + loc.getId() );
		sb.append("\\n");
		sb.append("\"");
		return sb.toString();
	}
	
	
	
	public static IrProgram newProgram(IrProgram p) {
		BoundCheckVistor vistor = new BoundCheckVistor();
		p.accept(vistor);
		return p;
	}
	
	
	@Override
	public boolean visit(IrAssignment assign) {
		IrLocation lhs = assign.getLhs();
		IrExpression rhs = assign.getRhs();
		if(lhs.locationIsArray()) {
			boundCheckArrLocation(lhs, envs.peekVariables(), envs.peekMethod());
		}
		if(rhs instanceof IrLocation && ((IrLocation) rhs).locationIsArray()) {
			boundCheckArrLocation((IrLocation) rhs, envs.peekVariables(), envs.peekMethod());
		}
		addIrStatement(assign);
		return false;
	}
	
	
	@Override
	public boolean visit(IrForBlock forBlock) {
		
		return false;
	}
	
	public void boundCheckArrLocation(IrLocation loc, VariableTable v, MethodTable m) {
		ifCodeForBoundCheck(loc, v, m).accept(this);
	}
	
	private IfBlock ifCodeForBoundCheck(IrLocation loc, VariableTable v, MethodTable m) {
		IrExpression sizeExpr = loc.getSizeExpr();
		IrLiteral arrayLen = ((ArrayDecl)v.get(loc.getId())).getSizeLiteral();
		if(sizeExpr instanceof IrLiteral) {
			IrLiteral sizeLiteral = (IrLiteral) sizeExpr;
			if(sizeLiteral.getIntValue().intValue() >= arrayLen.getIntValue().intValue()) {
				System.out.println(ErrMsgForBoundCheck(loc));
				System.exit(-1);
			}
		}
		
		BinaryExpression binary = new BinaryExpression(sizeExpr, arrayLen, ">=");
		IfBlock ifCode = new IfBlock(binary, v);
		ifCode.addTrueStatement(FuncInvokeStatement.getPrintStatement(ErrMsgForBoundCheck(loc)));
		ifCode.addTrueStatement(FuncInvokeStatement.getExitStatement(-1));
		return ifCode;
	}
}
