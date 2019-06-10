package edu.mit.compilers.CFG;

import edu.mit.compilers.IR.LowLevelIR.stackAllocIR;

public class stackAllocNode extends CFGNode{
	
	public stackAllocNode(int size) {
		super();
		this.statements.add(new stackAllocIR(size));
	}
}
