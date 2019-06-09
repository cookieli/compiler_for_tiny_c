package edu.mit.compilers.IR.LowLevelIR;

import java.util.Stack;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;

public class IrIfBlockQuad extends LowLevelIR {
	
	//public IrQuad cond;
	public LowLevelIR condQuad;// in IrQuadVistor, it means IrQuad, in IrQuadResolveNameToLocation it means IrQuadWIthLocation;
	
	public IrBlock trueBlock,
	               falseBlock;
	
	public IrIfBlockQuad(IrQuad cond, IrBlock trueBlock, IrBlock falseBlock) {
		//this.cond = cond;
		condQuad = cond;
		this.trueBlock = trueBlock;
		this.falseBlock = falseBlock;
	}
	
	public IrIfBlockQuad(IrQuad cond, IrBlock trueBlock) {
		this(cond, trueBlock, null);
	}
	
	
	
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("=======ifBlock==========\n");
		sb.append(condQuad.getName());
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

	

	public LowLevelIR getCondQuad() {
		return condQuad;
	}

	public void setCondQuad(LowLevelIR condQuad) {
		this.condQuad = condQuad;
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
