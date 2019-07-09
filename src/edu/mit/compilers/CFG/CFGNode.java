package edu.mit.compilers.CFG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;
import edu.mit.compilers.IR.LowLevelIR.ReturnQuadWithLoc;

public class CFGNode extends IrNode {
	public List<LowLevelIR> statements;
	public List<CFGNode> pointTo;
	public List<CFGNode> parents;
	public int inComingDegree;
	public boolean isVisited = false;
	public boolean isAssemblyVisited = false;
	
	private boolean forAfterBlock = false;
	private int nameVisited = 0;
	
	private boolean isWhileNode = false;
	private boolean isLoopEnd   = false;
	
	//private CFGNode afterNode = null;
	
	public void setForAfterBlock(boolean boo) {
		this.forAfterBlock = boo;
	}
	
	public boolean lastStatementIsReturn() {
		if(statements == null || statements.isEmpty())
			return false;
		else {
			for(LowLevelIR ir: statements) {
			//return statements.get(statements.size() -1) instanceof ReturnQuadWithLoc;
				if(ir instanceof ReturnQuadWithLoc)
					return true;
			}
			return false;
		}
	}
	
	public boolean isForAfterBlock() {
		return forAfterBlock;
	}
	
	public void setLoopEnd(boolean boo) {
		this.isLoopEnd = boo;
	}
	
	public boolean istLoopEnd() {
		return isLoopEnd;
	}
	
	
	public boolean isTransitionNodeForEliminate() {
		boolean ret = false;
		if(this.statements == null && (parents != null && !parents.isEmpty())) {
			ret = true;
			for(CFGNode parent: parents) {
				//System.out.println(parent.getName());
				if(parent.pointTo == null ) {
					throw new IllegalArgumentException( parent.getStats() + " " + this.getSuccessor().get(0).getStats());
				}
				if(!(parent.pointTo.size() == 1 && parent.pointTo.get(0).equals(this))) {
					ret = false;
				}
			}
		}
		return ret;
	}
	
	public int visitCount = 1;
	
	public void setWhileNode(boolean isWhile) {
		this.isWhileNode = isWhile;
	}
	
	public boolean isWhileNode() {
		return isWhileNode;
	}
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
		pointTo = null;
		//throw new IllegalArgumentException("point to == null");
	}

	public void deleteParent() {
		inComingDegree = 0;
		parents = new LinkedList<>();
	}

	public int getIncomingDegree() {
		return inComingDegree;
	}

	public void addSuccessor(CFGNode node) {
		if(pointTo == null)
			pointTo = new LinkedList<>();
		pointTo.add(node);
		node.addParent(this);
	}
	
	//TODO: warning : only use for insert exit node.
	public void insertNode(CFGNode node) {
		node.pointTo = this.pointTo;
		this.pointTo = new ArrayList<>();
		this.pointTo.add(node);
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
	public void addLowLevelIrFromHead(LowLevelIR ir) {
		statements.add(0, ir);
	}

	private void addPredecessor() {
		inComingDegree++;
	}

	private void addParent(CFGNode node) {
		if(parents == null)
			parents = new LinkedList<>();
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

	public List<CFGNode> getSuccessor() {
		return pointTo;
	}

	public void combineNode(CFGNode node) {
		if (this.statements == null)
			this.statements = node.statements;
		else
			this.statements.addAll(node.statements);
		this.pointTo = node.pointTo;
		if(node.pointTo != null)
		for(CFGNode n: node.pointTo) {
			int index = n.parents.indexOf(node);
			if(index == -1) {
				for(CFGNode c: n.parents) {
					System.out.println(c.getStats());
				}
				throw new IllegalArgumentException("pointTo " + n.getStats() + "node "+node.getStats());
			}
			n.parents.set(index, this);
		}
		node.pointTo = null;
		node.parents = null;
		node.inComingDegree = 0;
		this.isWhileNode = this.isWhileNode || node.isWhileNode();
		this.isLoopEnd = this.isLoopEnd || node.isLoopEnd;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		try {
			if (this.nameVisited == 1) {
				this.nameVisited = 0;
				return this.getStats();
			} else
				this.nameVisited += 1;
			
			sb.append("=======cfgNode======\n");
			sb.append(this.getStats());
			sb.append("=======cfgNode======\n");
			if (pointTo != null) {

				for (CFGNode node : pointTo) {
					sb.append(node.getName());
				}
			}
			return sb.toString();
		} catch (StackOverflowError e) {
			return sb.toString();
		}
	}

	public String getStats() {
		StringBuilder sb = new StringBuilder();
		// sb.append("=======cfgNode======\n");
		if (statements == null) {
			sb.append("noOp\n");
		} else
			for (LowLevelIR s : statements) {
				sb.append(s.getName());
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
		// System.out.println(getOneRectangle("", "\n"));
		String a = null;
		System.out.println(a);
	}
}
