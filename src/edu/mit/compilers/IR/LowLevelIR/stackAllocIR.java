package edu.mit.compilers.IR.LowLevelIR;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;

public class stackAllocIR extends LowLevelIR {
	
	public String stackAlloc;
	public StackZeroIR zeroStack;
	
	public stackAllocIR(int size) {
		stackAlloc = "subq " +"$"+ size+", "+ "%rsp";
		zeroStack = new StackZeroIR(size, true);
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return stackAlloc+"\n"+ zeroStack.getName();
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

}
