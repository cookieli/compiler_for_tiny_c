package edu.mit.compilers.IR.expr;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;

public abstract class IrExpression extends IrNode {
	
	public IrExpression() {
		super();
	}
	public IrExpression(int line, int column, String filename) {
		// TODO Auto-generated constructor stub
		super(line, column, filename);
	}

}
