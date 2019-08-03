package edu.mit.compilers.IR.LowLevelIR;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;



public class EntryPoint extends LowLevelIR{
	
	public static final String pushOp = "pushq %rbp";
	public static final String movOp = "movq %rsp, %rbp";
	public String stackAlloc;
	public StackZeroIR zeroStack;
	public int stackSize;
	public EntryPoint(int stackSize) {
		this.stackSize = stackSize;
		stackAlloc = "subq " +"$"+ this.stackSize+", "+ "%rsp";
		zeroStack = new StackZeroIR(this.stackSize);
	}
	
	public void resetStackSize(int size) {
		this.stackSize = size;
		stackAlloc = "subq " +"$"+ this.stackSize+", "+ "%rsp";
		zeroStack = new StackZeroIR(this.stackSize);
	}
	
	
	
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(pushOp +"\n");
		sb.append(movOp + "\n");
		sb.append(stackAlloc+"\n");
		sb.append(zeroStack.getName());
		return sb.toString();
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
