package edu.mit.compilers.IR.statement;

import java.util.Map;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.utils.X86_64Register;

public abstract class IrStatement extends IrNode{
	
	public Map<IrLocation, X86_64Register.Register> locRegs = null;
	
	public IrStatement() {}
	public IrStatement(int line, int column, String filename) {
		// TODO Auto-generated constructor stub
		super(line, column, filename);
	}
	
	public void setLocalVarTableParent(VariableTable v) {
		
	}
	
}
