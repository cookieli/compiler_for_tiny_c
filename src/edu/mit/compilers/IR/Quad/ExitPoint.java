package edu.mit.compilers.IR.Quad;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.statement.IrStatement;

public class ExitPoint extends LowLevelIR{
	
	public static final String leaveOp = "leave";
	public static final String retOp  ="ret";
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		
		return leaveOp + "\n" +retOp + "\n";
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
	public static final ExitPoint exit = new ExitPoint();

}
