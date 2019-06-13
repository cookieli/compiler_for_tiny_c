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
		addCFGPair(pair.get(0), pair.get(1));
	}

	public void end() {
		addCFGNode(new ExitNode());

	}

	public static List<CFGNode> destruct(IrIfBlockQuad block) {
		CFGNode noOp = new NoOpCFG();
		List<CFGNode> truePair = destruct(block.getTrueBlock());
		List<CFGNode> falsePair = null;
		if (block.getFalseBlock() != null) {
			falsePair = destruct(block.getFalseBlock());
		}
		CFGNode trueBegin = truePair.get(0);
		truePair.get(1).addSuccessor(noOp);
		CFGNode falseBegin = noOp;
		if (falsePair != null) {
			falseBegin = falsePair.get(0);
			falsePair.get(1).addSuccessor(noOp);
		}
		CFGNode beginNode = shortcircuit(block.getCondQuad(), trueBegin, falseBegin);
		List<CFGNode> pair = new ArrayList<>();
		pair.add(beginNode);
		pair.add(noOp);
		return pair;
	}

	private static CFGNode shortcircuit(CondQuad quad, CFGNode trueStart, CFGNode falseStart) {
		if (quad.getSymbol().isEmpty()) {
			return shortcircuit((IrQuadWithLocation) quad.getCondStack().get(0), trueStart, falseStart);
		}

		List<String> symbol = quad.getSymbol();
		List<LowLevelIR> condStack = quad.getCondStack();
		int stackCursor = condStack.size() -1;
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
			}else {
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
		CFGNode beginNode = new CFGNode(loc);
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
		return null;
	}

	public static List<CFGNode> destruct(IrBlock block) {
		List<CFGNode> pair = null;
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

					if (node instanceof NoOpCFG && succ.isMergeNode()) {
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
