package edu.mit.compilers.IR.LowLevelIR;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;

public class IrWhileBlockQuad extends LowLevelIR {
	public CondQuad cond;
	public IrBlock block;
	
	public IrWhileBlockQuad(CondQuad cond, IrBlock block) {
		this.cond = cond;
		this.block = block;
	}
	public CondQuad getCond() {
		return cond;
	}

	public void setCond(CondQuad cond) {
		this.cond = cond;
	}

	public IrBlock getBlock() {
		return block;
	}

	public void setBlock(IrBlock block) {
		this.block = block;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(cond.getName());
		if(block != null)
			sb.append(block.getName());
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

}
