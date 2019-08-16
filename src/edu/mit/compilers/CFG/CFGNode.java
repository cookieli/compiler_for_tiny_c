package edu.mit.compilers.CFG;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;
import edu.mit.compilers.IR.LowLevelIR.ReturnQuadWithLoc;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.SymbolTables.VariableTable;

public class CFGNode extends IrNode {
	public List<IrStatement> statements;
	public List<CFGNode> pointTo;
	public List<CFGNode> parents;
	public int inComingDegree;
	public boolean isVisited = false;
	public boolean isAssemblyVisited = false;
	
	public int hash = -1;
	public static int hashCount = 0;

	private boolean forAfterBlock = false;
	private int nameVisited = 0;

	private boolean isWhileNode = false;
	private boolean isLoopEnd = false;

	private VariableTable vtb;

	public BitSet in;
	public BitSet out;

	public BitSet gen = null;
	public BitSet kill = null;
	
	public BitSet use = null;
	
	public BitSet def = null;
	
	public BitSet mustInUse = null;
	
	public HashSet<CFGNode> dominators;
	
	public void resetBitSet() {
		this.in = null;
		this.out = null;
		this.gen = null;
		this.kill = null;
		this.use = null;
		this.def = null;
	}
	public HashMap<IrLocation, List<Integer>> defs = null;

	public void addDefs(IrQuad quad) {

		if (quad.getOp1() instanceof IrLocation) {
			IrLocation op1 = (IrLocation) quad.getOp1();
			if (!(op1.getNaming() == 0)&& !op1.isTempVariable()) {
				if (defs == null)
					defs = new HashMap<>();
				if (!defs.containsKey(op1)) {
					defs.put(op1, new ArrayList<>());
				}
			}
		}

		if (quad.getDest() != null) {
			if (quad.getOp2() instanceof IrLocation) {
				IrLocation op2 = (IrLocation) quad.getOp2();
				if (!(op2.getNaming() == 0)&& !op2.isTempVariable()) {
					if (defs == null)
						defs = new HashMap<>();
					if (!defs.containsKey(op2)) {
						defs.put(op2, new ArrayList<>());
					}
				}
			}
		}

	}

	// private CFGNode afterNode = null;

	public VariableTable getVtb() {
		return vtb;
	}

	public void setVtb(VariableTable vtb) {
		this.vtb = vtb;
	}

	public void setForAfterBlock(boolean boo) {
		this.forAfterBlock = boo;
	}

	public boolean lastStatementIsReturn() {
		if (statements == null || statements.isEmpty())
			return false;
		else {
			for (IrStatement ir : statements) {
				// return statements.get(statements.size() -1) instanceof ReturnQuadWithLoc;
				if (ir instanceof Return_Assignment)
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
		if (this.statements == null && (parents != null && !parents.isEmpty())) {
			ret = true;
			for (CFGNode parent : parents) {
				// System.out.println(parent.getName());
				if (parent.pointTo == null) {
					throw new IllegalArgumentException(parent.getStats() + " " + this.getSuccessor().get(0).getStats());
				}
				if (!(parent.pointTo.size() == 1 && parent.pointTo.get(0).equals(this))) {
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
		// throw new IllegalArgumentException("point to == null");
	}

	public void deleteParent() {
		inComingDegree = 0;
		parents = new LinkedList<>();
	}

	public int getIncomingDegree() {
		return inComingDegree;
	}

	public void addSuccessor(CFGNode node) {
		if (pointTo == null)
			pointTo = new LinkedList<>();
		pointTo.add(node);
		node.addParent(this);
	}
	
	

	// TODO: warning : only use for insert exit node.
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
		if (parents == null)
			parents = new LinkedList<>();
		parents.add(node);
		addPredecessor();
	}

	public void removeParent(CFGNode node) {
		parents.remove(node);
		inComingDegree--;
	}
	
	public void removeSucc(CFGNode n) {
		pointTo.remove(n);
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
		if (node.pointTo != null)
			for (CFGNode n : node.pointTo) {
				int index = n.parents.indexOf(node);
				if (index == -1) {
					for (CFGNode c : n.parents) {
						System.out.println(c.getStats());
					}
					throw new IllegalArgumentException("pointTo " + n.getStats() + "node " + node.getStats());
				}
				n.parents.set(index, this);
			}
		node.pointTo = null;
		node.parents = null;
		node.inComingDegree = 0;
		this.isWhileNode = this.isWhileNode || node.isWhileNode();
		this.isLoopEnd = this.isLoopEnd || node.isLoopEnd;
		if (this.vtb == null && node.vtb != null) {
			this.vtb = node.vtb;
		}
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
		if (statements == null || statements.size() == 0) {
			sb.append("noOp\n");
		} else
			for (IrStatement s : statements) {
				sb.append(s.getName());
			}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		if(hash == -1) {
			hash = hashCount;
			hashCount ++;
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		return false;
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
