package edu.mit.compilers.CFG;

import edu.mit.compilers.IR.LowLevelIR.stackFreeIR;

public class stackFreeNode extends CFGNode{
	
	public stackFreeNode(int size) {
		super();
		this.statements.add(new stackFreeIR(size));
	}
}
