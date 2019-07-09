package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IR_decl_Node.ArrayDecl;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.MultiStatementExpr;
import edu.mit.compilers.IR.expr.RealUnaryExpression;
import edu.mit.compilers.IR.expr.UnaryExpression;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrForBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrWhileBlock;
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
			//throw new IllegalArgumentException(" " + lhs.getId());
			boundCheckArrLocation(lhs, envs.peekVariables(), envs.peekMethod());
		}
		if(rhs instanceof IrLocation && ((IrLocation) rhs).locationIsArray()) {
			boundCheckArrLocation((IrLocation) rhs, envs.peekVariables(), envs.peekMethod());
		}
		addIrStatement(assign);
		return false;
	}
	
	public boolean visit(IfBlock ifCode) {
		ifCode.setBoolExpr(handleBoolExpr(ifCode.getBoolExpr(), envs.peekVariables(), envs.peekMethod()));
		ifCode.getTrueBlock().accept(this);
		if (ifCode.getFalseBlock() != null) {

			ifCode.getFalseBlock().accept(this);
		}

		addIrStatement(ifCode);
		
		return false;
	}
	
	private IrExpression handleBoolExpr(IrExpression expr, VariableTable v, MethodTable m) {
		if(expr instanceof MultiStatementExpr) {
			return handleMultiStatementExpr((MultiStatementExpr) expr, v ,m);
		} else if(expr instanceof BinaryExpression) {
			BinaryExpression binary = (BinaryExpression) expr;
			binary.setLhs(handleBoolExpr(binary.getlhs(), v, m));
			binary.setRhs(handleBoolExpr(binary.getrhs(), v, m));
			return binary;
		} else if(expr instanceof UnaryExpression) {
			return this.setUnaryExpr((UnaryExpression) expr, v, m);
		}
		return expr;
	}
	@Override
	public RealUnaryExpression setUnaryExpr(UnaryExpression expr, VariableTable vtb, MethodTable mtb) {
		RealUnaryExpression unary;
		if(!(expr instanceof RealUnaryExpression))
			unary = ((UnaryExpression) expr).convertToRealUnary();
		else
			unary  = (RealUnaryExpression) expr;
		
		unary.setExpr(handleBoolExpr(unary.getIrExpression(), vtb, mtb));
		//System.out.println(unary.getName());
		return unary;
	}
	
	private IrExpression handleMultiStatementExpr(MultiStatementExpr expr, VariableTable v, MethodTable m) {
		List<IrStatement> temp = currentList;
		currentList = new ArrayList<>();
		boundCheckForMultiStatementExpr(expr);
		expr.setPreStatement(currentList);
		currentList = temp;
		return expr;
	}
	
	
	@Override
	public boolean visit(IrForBlock forBlock) {
		forBlock.getBlock().accept(this);
		forBlock.setBoolExpression(handleBoolExpr(forBlock.getBoolExpr(), envs.peekVariables(), envs.peekMethod()));
		currentList = new ArrayList<>();
		for(IrStatement s: forBlock.getPreTempStat()) {
			s.accept(this);
		}
		forBlock.setPreTempStat(currentList);
		currentList = new ArrayList<>();
		for(IrStatement s: forBlock.getAfterBlockStat()) {
			s.accept(this);
		}
		forBlock.setAfterBlockStat(currentList);
		currentList = null;
		addIrStatement(forBlock);
		return false;
	}
	
	
	public boolean visit(IrWhileBlock whileBLock){
	    whileBLock.setBoolExpr(handleBoolExpr(whileBLock.getBoolExpr(), envs.peekVariables(), envs.peekMethod()));
		whileBLock.getCodeBlock().accept(this);
		currentList = new ArrayList<>();
		for(IrStatement s: whileBLock.getPreTempStat()) {
			s.accept(this);
		}
		whileBLock.setPreTempStat(currentList);
		currentList = null;
		addIrStatement(whileBLock);
		return false;
	}
	
	
	public void boundCheckArrLocation(IrLocation loc, VariableTable v, MethodTable m) {
		//ifCodeForBoundCheck(loc, v, m).accept(this);
		addIrStatement(ifCodeForBoundCheck(loc, v, m));
	}
	
	public void boundCheckForMultiStatementExpr(MultiStatementExpr expr) {
		for(IrStatement s: expr.getPreStatement()) {
			s.accept(this);
		}
	}
	
	
	
	private IfBlock ifCodeForBoundCheck(IrLocation loc, VariableTable v, MethodTable m) {
		IrExpression sizeExpr = loc.getSizeExpr();
		if(v.get(loc.getId()) == null)
			throw new IllegalArgumentException(loc.getId());
		IrLiteral arrayLen = ((ArrayDecl)v.get(loc.getId())).getSizeLiteral();
		
		BinaryExpression binary = new BinaryExpression(sizeExpr, arrayLen, ">=");
		BinaryExpression binary2 = new BinaryExpression(sizeExpr, IrLiteral.getLiteral(0), "<");
		BinaryExpression binary_last = new BinaryExpression(binary, binary2, "||");
		IfBlock ifCode = new IfBlock(binary_last, v);
		ifCode.addTrueStatement(FuncInvokeStatement.getPrintStatement(ErrMsgForBoundCheck(loc)));
		ifCode.addTrueStatement(FuncInvokeStatement.getExitStatement(-1));
		return ifCode;
	}
}
