package edu.mit.compilers.IR.LowLevelIR;

import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class IrQuadForFuncInvoke extends IrQuad{
	public IrQuadForFuncInvoke(FuncInvokeStatement func,VariableTable v, MethodTable m) {
		super("call", func.getFunc(), null, null);
		this.v = v;
		this.m = m;
	}
	
	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
		
	}
	
	public IrOperand getFunc() {
		return this.getOp1();
	}
}
