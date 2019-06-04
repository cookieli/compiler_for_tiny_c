package edu.mit.compilers.CFG;


import edu.mit.compilers.IR.LowLevelIR.EntryPoint;

public class EntryNode extends CFGNode{
	
	public EntryNode(int size) {
		super();
		this.statements.add(new EntryPoint(size));
	}
}
