package edu.mit.compilers.IR.statement;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;

public class Return_Assignment extends IrStatement{
	
	public IrExpression expr;
	
	public Return_Assignment(IrExpression expr) {
		this.expr = expr;
	}
	public Return_Assignment(Token ret, String filename) {
		super(ret.getLine(), ret.getColumn(), filename);
		this.expr = null;
	}
	
	public Return_Assignment(Return_Assignment ret) {
		super(ret.getLineNumber(), ret.getColumnNumber(), ret.getFilename());
		expr = (IrExpression) ret.getExpr().copy();
	}
	
	public IrExpression getExpr() {
		return expr;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if(expr == null) return "return\n";
		return "return "+expr.getName();
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
	}
	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new Return_Assignment(this);
	}

}
