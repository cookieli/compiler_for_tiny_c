package edu.mit.compilers.IR.statement;

import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;

public class FuncInvokeStatement extends IrStatement{
	
	public IrFuncInvocation func;
	
	public FuncInvokeStatement(IrFuncInvocation func) {
		this.func = func;
	}
	
	public IrFuncInvocation getFunc() {
		return func;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return func.getName();
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
	}

}
