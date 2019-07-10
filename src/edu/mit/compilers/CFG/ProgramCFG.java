package edu.mit.compilers.CFG;

import java.util.LinkedHashMap;

import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class ProgramCFG {
	public VariableTable globlVtb;
	public MethodTable globlMtb;
	public LinkedHashMap<String, CFG> cfgs;
	public IrProgram program;
	
	public ProgramCFG(IrProgram p, VariableTable globlVtb,  MethodTable globlMtb, LinkedHashMap<String, CFG> cfgs) {
		this.globlVtb = globlVtb;
		this.globlMtb = globlMtb;
		this.cfgs = cfgs;
		this.program = p;
	}
}
