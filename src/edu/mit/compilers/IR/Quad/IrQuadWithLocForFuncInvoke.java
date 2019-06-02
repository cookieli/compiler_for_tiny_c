package edu.mit.compilers.IR.Quad;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;

public class IrQuadWithLocForFuncInvoke extends LowLevelIR{
	
	public List<IrQuadWithLocation> beforeCallOpr = null;
	
	public List<IrQuadWithLocation> afterCallOpr = null;
	public String opr = "call";
	public String funcName;
	
	public IrQuadWithLocForFuncInvoke(String funcName) {
		this.funcName = funcName;
		
	}
	
	public void addOprBeforeCall(IrQuadWithLocation quad) {
		if(beforeCallOpr == null)
			beforeCallOpr = new ArrayList<>();
		beforeCallOpr.add(quad);
	}
	
	public void addOprAfterCall(IrQuadWithLocation quad) {
		if(afterCallOpr == null)
			afterCallOpr = new ArrayList<>();
		afterCallOpr.add(quad);
	}
	
	
	public void setFuncName(String name) {
		this.funcName = name;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		if(beforeCallOpr != null) 
			for(IrQuadWithLocation quad: beforeCallOpr) {
				sb.append(quad.getName());
			}
		sb.append(opr + " " + funcName);
		sb.append("\n");
		if(afterCallOpr != null) {
			for(IrQuadWithLocation quad: afterCallOpr) {
				sb.append(quad.getName());
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
