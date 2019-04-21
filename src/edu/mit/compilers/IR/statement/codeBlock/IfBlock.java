package edu.mit.compilers.IR.statement.codeBlock;

import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.statement.IrStatement;

public class IfBlock extends IrStatement{
	
	public IrExpression boolExpr;
	
	public IrBlock trueBlock;
	
	public IrBlock falseBlock;
	
	public IfBlock(IrExpression boolExpr, IrBlock trueBlock, IrBlock falseBlock) {
		this.boolExpr = boolExpr;
		this.trueBlock = trueBlock;
		this.falseBlock = falseBlock;
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

}
