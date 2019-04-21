package edu.mit.compilers.IR.statement;

import edu.mit.compilers.IR.IrNode;

public abstract class IrStatement extends IrNode{
	
	public IrStatement() {}
	public IrStatement(int line, int column, String filename) {
		// TODO Auto-generated constructor stub
		super(line, column, filename);
	}
	
}
