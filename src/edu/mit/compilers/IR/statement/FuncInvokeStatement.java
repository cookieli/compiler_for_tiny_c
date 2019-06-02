package edu.mit.compilers.IR.statement;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;

public class FuncInvokeStatement extends IrStatement{
	
	public IrFuncInvocation func;
	
	public FuncInvokeStatement(IrFuncInvocation func) {
		this.func = func;
	}
	
	public FuncInvokeStatement(FuncInvokeStatement func) {
		this.func = (IrFuncInvocation) func.getFunc().copy();
	}
	
	public IrFuncInvocation getFunc() {
		return func;
	}
	
	public String getFuncName() {
		return getFunc().getId();
	}
	
	public void setPLT(boolean plt) {
		func.setPLT(plt);
	}
	
	public boolean getPLT() {
		return func.isPLT();
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

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new FuncInvokeStatement(this);
	}

}
