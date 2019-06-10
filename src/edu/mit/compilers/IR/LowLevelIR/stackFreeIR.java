package edu.mit.compilers.IR.LowLevelIR;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;

public class stackFreeIR extends LowLevelIR{
	
	public String stackFree;
	
	public stackFreeIR(int size) {
		stackFree = "addq " +"$"+ size+", "+ "%rsp";
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return stackFree +"\n";
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
