package edu.mit.compilers.IR.LowLevelIR;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;

public class IrWhileBlockQuad extends LowLevelIR {
	
	public List<IrStatement> preQuad = null;
	public CondQuad cond;
	public IrBlock block;
	
	public List<IrStatement> afterBlockQuad = null;
	
	public List<IrStatement> getPreQuad(){
		if(preQuad == null)
			preQuad = new ArrayList<>();
		return preQuad;
	}
	
	public List<IrStatement> getAfterBlockQuad(){
		if(afterBlockQuad == null)
			afterBlockQuad = new ArrayList<>();
		return afterBlockQuad;
	}
	
	public void setAfterBlockQuad(List<IrStatement> lst) {
		afterBlockQuad = lst;
	}
	
	public void setPreQuad(List<IrStatement> lst) {
		this.preQuad = lst;
	}
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
		if(preQuad != null) {
			for(IrStatement s: preQuad)
				sb.append(s.getName() + "\n");
		}
		sb.append(cond.getName());
		if(block != null)
			sb.append(block.getName());
		if(afterBlockQuad != null) {
			for(IrStatement s: afterBlockQuad) {
				sb.append(s.getName() + "\n");
			}
		}
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
