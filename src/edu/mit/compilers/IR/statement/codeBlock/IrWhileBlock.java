package edu.mit.compilers.IR.statement.codeBlock;

import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.statement.IrStatement;

public class IrWhileBlock extends IrStatement{
	public IrExpression boolExpr;
	
	public IrBlock codeBlock;
	
	public IrWhileBlock(IrExpression expr, IrBlock block) {
		boolExpr = expr;
		codeBlock = block;
	}

	public IrExpression getBoolExpr() {
		return boolExpr;
	}

	public void setBoolExpr(IrExpression boolExpr) {
		this.boolExpr = boolExpr;
	}

	public IrBlock getCodeBlock() {
		return codeBlock;
	}

	public void setCodeBlock(IrBlock codeBlock) {
		this.codeBlock = codeBlock;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("while "+ boolExpr.getName());
		sb.append("\n");
		sb.append(codeBlock.getName());
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
	}

}
