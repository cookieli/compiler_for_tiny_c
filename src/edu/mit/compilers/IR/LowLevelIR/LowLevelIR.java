package edu.mit.compilers.IR.LowLevelIR;

import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.assembly.AssemblyForArith;

public abstract class LowLevelIR extends IrStatement{
	
	
	public LowLevelIR () {
		
	}

	public LowLevelIR(int line, int column, String filename) {
		// TODO Auto-generated constructor stub
		super(line, column, filename);
	}
	

}
