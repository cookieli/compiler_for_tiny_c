package edu.mit.compilers.IR.expr.operand;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;

public class IrFuncInvocation extends IrOperand {
	public String funcName;
	public List<IrExpression> funcArgs;
	
	public IrFuncInvocation() {
		funcName = null;
		funcArgs = new ArrayList<>();
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
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(funcName);
		sb.append("(");
		for(IrExpression e: funcArgs) {
			sb.append(e.getName());
			sb.append(",");
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

}
