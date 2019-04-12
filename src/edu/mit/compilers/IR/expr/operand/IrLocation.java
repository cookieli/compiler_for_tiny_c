package edu.mit.compilers.IR.expr.operand;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;

public class IrLocation extends IrExpression{
	
	public String location;
	
	public IrLocation() {
		super();
	}
	
	public IrLocation(String l) {
		super();
		location = l;
	}
	
	public IrLocation(Token t, String filename) {
		super(t.getLine(), t.getColumn(), filename);
		location = t.getText();
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

}
