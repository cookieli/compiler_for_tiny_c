package edu.mit.compilers.CFG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.Quad.LowLevelIR;

public class CFGNode extends IrNode{
	public List<LowLevelIR> statements;
	public List<CFGNode> pointTo;
	public int inComingDegree;
	public boolean isVisited = false;
	
	public CFGNode() {
		statements = new ArrayList<>();
		pointTo = new LinkedList<>();
		inComingDegree = 0;
	}
	
	public CFGNode(LowLevelIR ir) {
		this();
		addLowLevelIr(ir);
	}
	
	public int getIncomingDegree() {
		return inComingDegree;
	}
	
	public void addSuccessor(CFGNode node) {
		pointTo.add(node);
		node.addPredecessor();
	}
	
	public void addLowLevelIr(LowLevelIR ir) {
		statements.add(ir);
	}
	
	private void addPredecessor() {
		inComingDegree ++;
	}
	
	public void setVisited() {
		isVisited = true;
	}
	
	public boolean isVisited() {
		return isVisited;
	}
	
	public List<CFGNode> getSuccessor(){
		return pointTo;
	}
	
	public void combineNode(CFGNode node) {
		this.statements.addAll(node.statements);
		this.pointTo = node.pointTo;
		node.pointTo = null;
		
	}
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("=======cfgNode======\n");
		for(LowLevelIR s: statements) {
			sb.append(s.getName());
		}
		sb.append("=======cfgNode======\n");
		if(pointTo != null) {
			for(CFGNode node: pointTo) {
				sb.append(node.getName());
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
		
	}
	
	public void accept(AssemblyFromCFGVistor vistor) {
		vistor.visit(this);
	}
}
