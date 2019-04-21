package edu.mit.compilers.IR.statement;

import antlr.Token;
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

}
