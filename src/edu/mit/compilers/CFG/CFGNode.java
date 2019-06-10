package edu.mit.compilers.CFG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;

public class CFGNode extends IrNode{
	public List<LowLevelIR> statements;
	public List<CFGNode> pointTo;
	public List<CFGNode> parents;
	public int inComingDegree;
	public boolean isVisited = false;
	public boolean isAssemblyVisited = false;
	public String label = null;
	public CFGNode() {
		statements = new ArrayList<>();
		pointTo = new LinkedList<>();
		parents = new LinkedList<>();
		inComingDegree = 0;
	}
	
	public CFGNode(LowLevelIR ir) {
		this();
		addLowLevelIr(ir);
	}
	
	public void deletePointTo() {
		pointTo = new LinkedList<>();
	}
	
	public void deleteParent() {
		inComingDegree = 0;
		parents = new LinkedList<>();
	}
	
	public int getIncomingDegree() {
		return inComingDegree;
	}
	
	public void addSuccessor(CFGNode node) {
		pointTo.add(node);
		node.addParent(this);
	}
	
	public List<CFGNode> getParents() {
		return parents;
	}

	public void setParents(List<CFGNode> parents) {
		this.parents = parents;
	}

	public void addLowLevelIr(LowLevelIR ir) {
		statements.add(ir);
	}
	
	private void addPredecessor() {
		inComingDegree ++;
	}
	
	private void addParent(CFGNode node) {
		parents.add(node);
		addPredecessor();
	}
	public void removeParent(CFGNode node) {
		parents.remove(node);
		inComingDegree--;
	}
	
	public void setVisited() {
		isVisited = true;
	}
	
	public boolean isVisited() {
		return isVisited;
	}
	
	public boolean isMergeNode() {
		return inComingDegree > 1;
	}
	
	public boolean isAssemblyVisited() {
		return isAssemblyVisited;
	}
	
	public void setAssemblyVisited() {
		isAssemblyVisited = true;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<CFGNode> getSuccessor(){
		return pointTo;
	}
	
	public void combineNode(CFGNode node) {
		if(this.statements == null)
			this.statements = node.statements;
		else
			this.statements.addAll(node.statements);
		this.pointTo = node.pointTo;
		node.pointTo = null;
		node.parents = null;
		node.inComingDegree = 0;
		
	}
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("=======cfgNode======\n");
		if(statements == null) {
			sb.append("noOp\n");
		} else
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
	
	public String getStruct() {
		StringBuilder sb = new StringBuilder();
		
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
	
	public static void main(String[] args) {
		//System.out.println(getOneRectangle("", "\n"));
	}
}
