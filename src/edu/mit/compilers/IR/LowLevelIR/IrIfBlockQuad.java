package edu.mit.compilers.IR.LowLevelIR;

import java.util.Stack;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;

public class IrIfBlockQuad extends LowLevelIR {
	
	//public IrQuad cond;
	public Stack<LowLevelIR> condStack;
	
	public Stack<String> symbolStack;
	
	public IrBlock trueBlock,
	               falseBlock;
	
	public IrIfBlockQuad(IrQuad cond, IrBlock trueBlock, IrBlock falseBlock) {
		//this.cond = cond;
		condStack = new Stack<>();
		symbolStack = new Stack<>();
		condStack.add(cond);
		this.trueBlock = trueBlock;
		this.falseBlock = falseBlock;
	}
	
	public IrIfBlockQuad(IrQuad cond, IrBlock trueBlock) {
		this(cond, trueBlock, null);
	}
	
	public void addCond(LowLevelIR cond) {
		condStack.add(cond);
	}
	
	
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("=======ifBlock==========\n");
		for(LowLevelIR q: condStack) {
			sb.append(q.getName());
		}
		sb.append("-------trueBlock---------\n");
		sb.append(trueBlock.getName());
		sb.append("-------trueBlock---------\n");
		sb.append("\n");
		if(falseBlock != null) {
			sb.append("--------falseBLock--------\n");
			sb.append(falseBlock.getName());
			sb.append("--------falseBlock---------\n");
		}
		sb.append("=======ifBlock==========\n");
		return sb.toString();
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
	}

	public Stack<LowLevelIR> getCondStack() {
		return condStack;
	}

	public void setCondStack(Stack<LowLevelIR> condStack) {
		this.condStack = condStack;
	}

	public Stack<String> getSymbolStack() {
		return symbolStack;
	}

	public void setSymbolStack(Stack<String> symbolStack) {
		this.symbolStack = symbolStack;
	}

	public IrBlock getTrueBlock() {
		return trueBlock;
	}

	public void setTrueBlock(IrBlock trueBlock) {
		this.trueBlock = trueBlock;
	}

	public IrBlock getFalseBlock() {
		return falseBlock;
	}

	public void setFalseBlock(IrBlock falseBlock) {
		this.falseBlock = falseBlock;
	}


}
