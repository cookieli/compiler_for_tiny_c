package edu.mit.compilers.CFG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import edu.mit.compilers.IR.LowLevelIR.CondQuad;
import edu.mit.compilers.IR.LowLevelIR.IrIfBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.IrWhileBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;

public class CFG {

	public String method;
	public CFGNode entry;
	public CFGNode end;

	public CFG() {
	}

	public CFG(int size) {
		this();
		entry = new EntryNode(size);
		end = entry;
	}

	public CFG(int size, String method) {
		this(size);
		this.method = method;
	}

	public void addCFGNode(CFGNode node) {
		end.addSuccessor(node);
		end = node;
	}

	private void addCFGPair(CFGNode begin, CFGNode end) {
		this.end.addSuccessor(begin);
		this.end = end;
	}

	public void addCFGPair(List<CFGNode> pair) {
		if (pair == null)
			return;
		addCFGPair(pair.get(0), pair.get(1));
	}

	public void end() {
		addCFGNode(new ExitNode());

	}

	private static CFGNode getFlowBeginNode(List<CFGNode> flow, CFGNode succ) {
		if (flow == null)
			return succ;
		else {
			flow.get(flow.size() - 1).addSuccessor(succ);
			return flow.get(0);
		}
	}

	public static List<CFGNode> destruct(IrIfBlockQuad block) {
		CFGNode noOp = new NoOpCFG();

		List<CFGNode> truePair = destruct(block.getTrueBlock());// destruct(block.getTrueBlock());
		List<CFGNode> falsePair = destruct(block.getFalseBlock());
		CFGNode trueBegin = getFlowBeginNode(truePair, noOp);
		CFGNode falseBegin = getFlowBeginNode(falsePair, noOp);
		if (trueBegin == falseBegin)
			return null;
		CFGNode beginNode = shortcircuit(block.getCondQuad(), trueBegin, falseBegin);
		List<CFGNode> pair = new ArrayList<>();
		pair.add(beginNode);
		pair.add(noOp);
		return pair;
	}

	private static List<CFGNode> destructLst(List<IrStatement> quadLst) {
		if (quadLst == null)
			return null;
		CFGNode reallyBeginNode = null;
		CFGNode beginLstEndNode = null;

		if (!quadLst.isEmpty()) {
			List<CFGNode> pair = destruct((LowLevelIR) quadLst.get(0));
			reallyBeginNode = pair.get(0);
			beginLstEndNode = pair.get(1);
			for (int i = 1; i < quadLst.size(); i++) {
				pair = destruct((LowLevelIR) quadLst.get(i));
				beginLstEndNode.addSuccessor(pair.get(0));
				beginLstEndNode = pair.get(1);
			}
		} else {
			return null;
		}
		List<CFGNode> pair = new ArrayList<>();
		pair.add(reallyBeginNode);
		pair.add(beginLstEndNode);
		return pair;

	}

	public static List<CFGNode> destruct(IrWhileBlockQuad block) {
		CFGNode falseNode = new NoOpCFG();
		List<CFGNode> truePair = null;
		if (block.getBlock() != null) {
			truePair = destruct(block.getBlock());
		} else {
			CFGNode noOp = new NoOpCFG();
			truePair = new ArrayList<>();
			truePair.add(noOp);
			truePair.add(noOp);
		}
		List<CFGNode> prePair = destructLst(block.getPreQuad());
		CFGNode beginNode = shortcircuit(block.getCond(), truePair.get(0), falseNode);
		beginNode.setWhileNode(true);
		List<CFGNode> pair = new ArrayList<>();
		if (prePair != null) {
			prePair.get(0).setWhileNode(true);
			prePair.get(1).addSuccessor(beginNode);
			truePair.get(1).addSuccessor(prePair.get(0));
			pair.add(prePair.get(0));
			pair.add(falseNode);
		} else {
			truePair.get(1).addSuccessor(beginNode);
			pair.add(beginNode);
			pair.add(falseNode);
		}

		return pair;
	}

	private static CFGNode shortcircuit(CondQuad quad, CFGNode trueStart, CFGNode falseStart) {
		if (quad.getSymbol().isEmpty()) {
			return shortcircuit((IrQuadWithLocation) quad.getCondStack().get(0), trueStart, falseStart);
		}
		if (quad.getSymbol().size() == 1 && quad.getSymbol().get(0).equals("!")) {
			if (quad.getCondStack().get(0) instanceof IrQuadWithLocation)
				return shortcircuit((IrQuadWithLocation) quad.getCondStack().get(0), falseStart, trueStart);
			return shortcircuit((CondQuad) quad.getCondStack().get(0), falseStart, trueStart);
		}

		List<String> symbol = quad.getSymbol();
		List<LowLevelIR> condStack = quad.getCondStack();
		int stackCursor = condStack.size() - 1;
		CFGNode beginNode = null;
		LowLevelIR op1 = condStack.get(stackCursor--);
		if (op1 instanceof IrQuadWithLocation) {
			beginNode = shortcircuit((IrQuadWithLocation) op1, trueStart, falseStart);
		} else {
			beginNode = shortcircuit((CondQuad) op1, trueStart, falseStart);
		}
		for (int i = symbol.size() - 1; i >= 0; i--) {
			String sym = symbol.get(i);
			if (sym.equals("&&")) {
				trueStart = beginNode;
			} else {
				falseStart = beginNode;
			}
			op1 = condStack.get(stackCursor--);
			if (op1 instanceof IrQuadWithLocation) {
				beginNode = shortcircuit((IrQuadWithLocation) op1, trueStart, falseStart);
			} else {
				beginNode = shortcircuit((CondQuad) op1, trueStart, falseStart);
			}

		}
		return beginNode;
	}

	private static CFGNode shortcircuit(CFGNode beginNode, CFGNode trueStart, CFGNode falseStart) {
		beginNode.addSuccessor(trueStart);
		beginNode.addSuccessor(falseStart);
		return beginNode;
	}

	private static CFGNode shortcircuit(IrQuadWithLocation loc, CFGNode trueStart, CFGNode falseStart) {

		CFGNode beginNode = new CFGNode();
		beginNode.addLowLevelIr(loc);
		return shortcircuit(beginNode, trueStart, falseStart);
	}

	private static List<CFGNode> destructOnelineQuad(LowLevelIR quad) {
		CFGNode newNode = new CFGNode(quad);
		List<CFGNode> list = new ArrayList<>();
		list.add(newNode);
		list.add(newNode);
		return list;
	}

	public static List<CFGNode> destruct(LowLevelIR ir) {
		if (ir instanceof IrQuadWithLocForFuncInvoke || ir instanceof IrQuadWithLocation)
			return destructOnelineQuad(ir);
		else if (ir instanceof IrIfBlockQuad)
			return destruct((IrIfBlockQuad) ir);
		else if (ir instanceof IrWhileBlockQuad)
			return destruct((IrWhileBlockQuad) ir);
		return null;
	}

	public static List<CFGNode> destruct(IrBlock block) {
		List<CFGNode> pair = null;
		if (block == null || block.haveNoStatements()) {
			return null;
		}
		List<IrStatement> lowLevelIRs = block.getStatements();
		CFGNode beginNode, endNode;

		LowLevelIR firstQuad = (LowLevelIR) lowLevelIRs.get(0);
		pair = destruct(firstQuad);
		int memSize = block.getLocalVar().getMemSize();
		if (memSize > 0) {
			beginNode = new stackAllocNode(memSize);
			beginNode.addSuccessor(pair.get(0));
			endNode = pair.get(1);
		} else {
			beginNode = pair.get(0);
			endNode = pair.get(1);
		}
		for (int i = 1; i < lowLevelIRs.size(); i++) {
			pair = destruct((LowLevelIR) lowLevelIRs.get(i));
			if(pair == null) {
				throw new IllegalArgumentException(lowLevelIRs.get(i).getName());
			}
			endNode.addSuccessor(pair.get(0));
			endNode = pair.get(1);
		}
		if (memSize > 0) {
			CFGNode temp = new stackFreeNode(memSize);
			endNode.addSuccessor(temp);
			endNode = temp;

		}
		pair = new ArrayList<>();
		pair.add(beginNode);
		pair.add(endNode);
		return pair;
	}

	public void compressCFG() {
		Queue<CFGNode> queue = new LinkedList<>();
		CFGNode node;
		entry.setVisited();
		queue.add(entry);
		while (!queue.isEmpty()) {
			node = queue.remove();
			List<CFGNode> successors = node.getSuccessor();
			if (successors != null) {
				if (successors.size() == 1) {
					CFGNode succ = successors.get(0);
					if (!succ.isVisited()) {
						succ.setVisited();
						if (succ.getIncomingDegree() == 1) {
							node.combineNode(succ);
							queue.add(node);
						} else {
							queue.add(succ);
						}
					}

					if (node.statements == null && succ.isMergeNode()) {
						if (succ.getParents().get(0).equals(node))
							succ.deleteParent();
						if (succ.getParents().contains(node)) {
							succ.removeParent(node);
						}
						// System.out.println("count: "+ node.getParents().size());
						for (CFGNode n : node.getParents()) {
							n.deletePointTo();
							n.addSuccessor(succ);
						}
					}
				} else {
					for (CFGNode n : successors) {
						if (!n.isVisited()) {
							n.setVisited();
							queue.add(n);
						}
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return entry.getName();
	}

	public String accept(AssemblyFromCFGVistor vistor) {
		vistor.visit(this);
		return vistor.getAssembly();
	}

	public String getFuncTitile() {
		StringBuilder sb = new StringBuilder();
		sb.append(".globl ");
		sb.append(method);
		sb.append("\n");
		sb.append(".type ");
		sb.append(method + ", ");
		sb.append("@function\n");
		sb.append(method + ":" + "\n");
		return sb.toString();

	}

}
