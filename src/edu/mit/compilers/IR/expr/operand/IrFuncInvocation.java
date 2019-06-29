package edu.mit.compilers.IR.expr.operand;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;

public class IrFuncInvocation extends IrOperand {
	public String funcName;
	public List<IrExpression> funcArgs;
	public boolean isPLT = false;
	
	public static IrFuncInvocation getPrintFunction(String msg) {
		IrFuncInvocation func = new IrFuncInvocation();
		func.addFuncName("printf");
		func.setPLT(true);
		func.addFuncArg(IrLiteral.getStringLiteral(msg));
		return func;
	}
	
	public static IrFuncInvocation getExitFunction(int num) {
		IrFuncInvocation func = new IrFuncInvocation();
		func.addFuncName("exit");
		func.setPLT(true);
		func.addFuncArg(IrLiteral.getLiteral(num));
		return func;
	}
	
	public boolean isPLT() {
		return isPLT;
	}

	public void setPLT(boolean isPLT) {
		this.isPLT = isPLT;
	}

	public IrFuncInvocation() {
		funcName = null;
		funcArgs = new ArrayList<>();
	}
	
	public IrFuncInvocation(IrFuncInvocation func) {
		funcName = new StringBuilder(func.funcName).toString();
		funcArgs = new ArrayList<>();
		for(IrExpression e: func.funcArgs) {
			funcArgs.add((IrExpression) e.copy());
		}
	}
	
	public IrFuncInvocation(Token t, String fileName) {
		super(t.getLine(), t.getColumn(),fileName);
		funcArgs = new ArrayList<>();
	}
	
	public void addFuncName(String name) {
		funcName = name;
	}
	
	public void addFuncArg(IrExpression expr) {
		funcArgs.add(expr);
	}
	public String getId() {
		return funcName;
	}
	public int getFuncArgNum() {
		return funcArgs.size();
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(funcName);
		sb.append("(");
		for(IrExpression e: funcArgs) {
			if(e == null) continue;
			sb.append(e.getName());
			sb.append(",");
		}
		sb.append(")");
		return sb.toString();
	}
	
	public IrExpression getFunctionArg(int i) {
		if (i > funcArgs.size())
			throw new IllegalArgumentException("it is beyond funcArg's size");
		return funcArgs.get(i);
	}
	
	public List<IrExpression> getFuncArgs(){
		return funcArgs;
	}
	
	public void setFuncArgs(List<IrExpression> lst) {
		funcArgs = lst;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IrOperand> operandList() {
		// TODO Auto-generated method stub
		List<IrOperand> lst = new ArrayList<>();
		lst.add(this);
		//lst.addAll(funcArgs);
		return lst;
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new IrFuncInvocation(this);
	}

}
