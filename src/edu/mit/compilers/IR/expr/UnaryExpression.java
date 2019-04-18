package edu.mit.compilers.IR.expr;

import antlr.Token;
import edu.mit.compilers.IR.IrNodeVistor;

public class UnaryExpression extends IrExpression{
	String symbol;
	IrExpression expr;
	
	public UnaryExpression() {
		super();
	}
	
	public UnaryExpression(String symbol, IrExpression expr) {
		super(expr.getLineNumber(), expr.getColumnNumber(), expr.getFilename());
		this.symbol = symbol;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return expr.getName();
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

}
