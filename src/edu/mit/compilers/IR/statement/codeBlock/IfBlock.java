package edu.mit.compilers.IR.statement.codeBlock;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.VariableTable;

public class IfBlock extends IrStatement{
	
	public IrExpression boolExpr;
	
	public IrBlock trueBlock;
	
	public IrBlock falseBlock;
	
	public IfBlock(IrExpression boolExpr, VariableTable parent) {
		this.boolExpr = boolExpr;
		this.trueBlock = new IrBlock(parent);
		this.falseBlock = new IrBlock(parent);
	}
	
	public void addTrueStatement(IrStatement assign) {
		this.trueBlock.addIrStatement(assign);
	}
	public void addFalseStatement(IrStatement assign) {
		this.falseBlock.addIrStatement(assign);
	}
	
	public IfBlock(IrExpression boolExpr, IrBlock trueBlock, IrBlock falseBlock) {
		this.boolExpr = boolExpr;
		this.trueBlock = trueBlock;
		this.falseBlock = falseBlock;
	}
	public IfBlock(IfBlock ifBlock) {
		this.boolExpr = (IrExpression) ifBlock.getBoolExpr().copy();
		this.trueBlock = (IrBlock) ifBlock.trueBlock.copy();
		if(ifBlock.falseBlock != null)
			this.falseBlock = (IrBlock) ifBlock.falseBlock.copy();
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("if "+ boolExpr.getName());
		sb.append("\n");
		sb.append(trueBlock.getName());
		sb.append("\n");
		if(falseBlock !=null) {
			sb.append("else :\n");
			sb.append(falseBlock.getName());
			sb.append("\n");
		}
		return sb.toString();
		
	}
	
	public IrExpression getBoolExpr() {
		return boolExpr;
	}
	
	public void setBoolExpr(IrExpression boolExpr) {
		this.boolExpr = boolExpr;
	}
	
	public IrBlock getTrueBlock() {
		return trueBlock;
	}
	
	public IrBlock getFalseBlock() {
		return falseBlock;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
		
	}
	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new IfBlock(this);
	}
	@Override
	public void setLocalVarTableParent(VariableTable v) {
		trueBlock.setLocalVarTableParent(v);
		if(falseBlock != null)
			falseBlock.setLocalVarTableParent(v);
	}
	

}
