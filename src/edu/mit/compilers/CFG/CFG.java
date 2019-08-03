package edu.mit.compilers.CFG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.LowLevelIR.CondQuad;
import edu.mit.compilers.IR.LowLevelIR.IrIfBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForAssign;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForLoopStatement;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.IrWhileBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;
import edu.mit.compilers.IR.LowLevelIR.MultiQuadLowIR;
import edu.mit.compilers.IR.LowLevelIR.ReturnQuadWithLoc;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.assembly.AssemblyForArith;
import edu.mit.compilers.utils.OperandForm;

public class CFG {

	public String method;
	
	public List<String> paraNameLst;

	public List<OperandForm> paraLst;
	
	public static VariableTable currentVtb;
	
	public VariableTable wholeMethodVtb;
	
	
	public void setVariableTable(VariableTable vtb) {
		currentVtb = vtb;
	}
	
	public VariableTable getWholeMethodVtb() {
		return wholeMethodVtb;
	}

	public void setWholeMethodVtb(VariableTable wholeMethodVtb) {
		this.wholeMethodVtb = wholeMethodVtb;
	}

	public boolean isMain() {
		return method.equals("main");
	}

	public List<String> getParaNameLst() {
		return paraNameLst;
	}

	public void setParaNameLst(List<String> paraNameLst) {
		this.paraNameLst = paraNameLst;
	}

	public List<OperandForm> getParaLst() {
		return paraLst;
	}

	public void setParaLst(List<OperandForm> paraLst) {
		this.paraLst = paraLst;
	}

	public CFGNode entry;
	public CFGNode end;

	public List<CFGNode> nodes;

	public boolean hasReturnValue;

	public boolean isHasReturnValue() {
		return hasReturnValue;
	}

	public void setHasReturnValue(boolean hasReturnValue) {
		this.hasReturnValue = hasReturnValue;
	}

	public static IrWhileBlockQuad currentLoop = null;

	public CFG() {
	}

	public CFG(int size) {
		this();
		entry = new EntryNode(size);
		nodes = new ArrayList<>();
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
		end.setLabel(AssemblyForArith.getNxtJmpLabel());
	}

	public String getEndLabel() {
		return end.getLabel();
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
		if (trueBegin == falseBegin) {
			// throw new IllegalArgumentException(block.getName());
			return null;
		}
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
		IrWhileBlockQuad tempLoop = currentLoop;
		currentLoop = block;
		CFGNode falseNode = new NoOpCFG();
		List<CFGNode> truePair = null;
		if (block.getBlock() != null) {
			truePair = destruct(block.getBlock());
		}
		if (truePair == null) {
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
		pair.get(1).setLoopEnd(true);
		currentLoop = tempLoop;
		return pair;
	}

	private static CFGNode shortcircuit(CondQuad quad, CFGNode trueStart, CFGNode falseStart) {
		if (quad.getSymbol().isEmpty()) {
			if (quad.getCondStack().isEmpty())
				throw new IllegalArgumentException("empty ");
			if (quad.getCondStack().get(0) instanceof IrQuad)
				return shortcircuit((IrQuad) quad.getCondStack().get(0), trueStart, falseStart);
			else
				return shortcircuit((MultiQuadLowIR) quad.getCondStack().get(0), trueStart, falseStart);
		}
		if (quad.getSymbol().size() == 1 && quad.getSymbol().get(0).equals("!")) {
			if (quad.getCondStack().get(0) instanceof IrQuad)
				return shortcircuit((IrQuad) quad.getCondStack().get(0), falseStart, trueStart);
			else if (quad.getCondStack().get(0) instanceof MultiQuadLowIR)
				return shortcircuit((MultiQuadLowIR) quad.getCondStack().get(0), falseStart, trueStart);
			return shortcircuit((CondQuad) quad.getCondStack().get(0), falseStart, trueStart);
		}

		List<String> symbol = quad.getSymbol();
		List<LowLevelIR> condStack = quad.getCondStack();
		int stackCursor = condStack.size() - 1;
		CFGNode beginNode = null;
		LowLevelIR op1 = condStack.get(stackCursor--);
		if (op1 instanceof IrQuad) {
			beginNode = shortcircuit((IrQuad) op1, trueStart, falseStart);
		} else if (op1 instanceof MultiQuadLowIR) {
			beginNode = shortcircuit((MultiQuadLowIR) op1, trueStart, falseStart);
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
			if (op1 instanceof IrQuad) {
				beginNode = shortcircuit((IrQuad) op1, trueStart, falseStart);
			} else if (op1 instanceof MultiQuadLowIR) {
				beginNode = shortcircuit((MultiQuadLowIR) op1, trueStart, falseStart);
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

	private static CFGNode shortcircuit(MultiQuadLowIR quad, CFGNode trueStart, CFGNode falseStart) {
		CFGNode beginNode = new CFGNode();
		beginNode.setVtb(currentVtb);
		CFGNode endNode = beginNode;
		for (IrStatement n : quad.getQuadLst()) {
			List<CFGNode> pair = destruct((LowLevelIR) n);
			endNode.addSuccessor(pair.get(0));
			endNode = pair.get(1);
		}
		shortcircuit(endNode, trueStart, falseStart);
		return beginNode;
	}

	private static CFGNode shortcircuit(IrQuad loc, CFGNode trueStart, CFGNode falseStart) {

		CFGNode beginNode = new CFGNode();
		beginNode.addLowLevelIr(loc);
		beginNode.setVtb(currentVtb);
		return shortcircuit(beginNode, trueStart, falseStart);
	}

	private static List<CFGNode> destructContinue(LowLevelIR quad) {
		CFGNode newNode = new CFGNode();
		CFGNode beginNode = null;
		CFGNode endNode = null;
		List<CFGNode> lst = new ArrayList<>();
		List<CFGNode> pair;
		if (currentLoop != null && currentLoop.getAfterBlockQuad() != null) {
			for (IrStatement ir : currentLoop.getAfterBlockQuad()) {
				pair = destruct((LowLevelIR) ir);
				if (pair != null) {
					if (beginNode == null) {
						beginNode = pair.get(0);
						endNode = pair.get(1);
					} else {
						endNode.addSuccessor(pair.get(0));
						endNode = pair.get(1);
					}
				}
			}
		}
		newNode.addLowLevelIr(quad);
		if (endNode != null) {
			endNode.addSuccessor(newNode);
			lst.add(beginNode);
			lst.add(newNode);
		} else {
			lst.add(newNode);
			lst.add(newNode);
		}
		return lst;
	}

	private static List<CFGNode> destructOnelineQuad(LowLevelIR quad) {

		if (quad instanceof IrQuadForLoopStatement && !((IrQuadForLoopStatement) quad).isBreak())
			return destructContinue(quad);
		CFGNode newNode = new CFGNode(quad);
		if(currentVtb == null)
			throw new IllegalArgumentException("table is null");
		newNode.setVtb(currentVtb);
		List<CFGNode> pair = new ArrayList<>();
		pair.add(newNode);
		pair.add(newNode);
		return pair;
	}

	private static boolean oneLineQuad(LowLevelIR ir) {
		return ir instanceof IrQuadWithLocation || ir instanceof IrQuadWithLocForFuncInvoke
				|| ir instanceof IrQuadForLoopStatement || ir instanceof IrQuadForAssign
				|| ir instanceof IrQuadForFuncInvoke || ir instanceof Return_Assignment || ir instanceof IrQuad;
	}

	public static List<CFGNode> destruct(LowLevelIR ir) {
		if (oneLineQuad(ir))
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
		VariableTable tempVtb = currentVtb;
		currentVtb = block.getLocalVar();
		List<IrStatement> lowLevelIRs = block.getStatements();
		CFGNode beginNode, endNode;

		int cursor = 0;
		while (pair == null && cursor < lowLevelIRs.size()) {
			LowLevelIR firstQuad = (LowLevelIR) lowLevelIRs.get(cursor++);
			pair = destruct(firstQuad);
		}

		if (pair == null)
			// throw new IllegalArgumentException(block.getName());
			return null;

		int memSize = block.getLocalVar().getMemSize();
		if (memSize > 0) {
			beginNode = new stackAllocNode(memSize);
			beginNode.addSuccessor(pair.get(0));
			endNode = pair.get(1);
		} else {
			beginNode = pair.get(0);
			endNode = pair.get(1);
		}
		for (int i = cursor; i < lowLevelIRs.size(); i++) {
			pair = destruct((LowLevelIR) lowLevelIRs.get(i));
			if (pair == null) {
				// throw new IllegalArgumentException(lowLevelIRs.get(i).getName());
				continue;
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
		currentVtb = tempVtb;
		return pair;
	}

	public void compressCFG() {
		Queue<CFGNode> queue = new LinkedList<>();
		CFGNode node;
		entry.setVisited();
		queue.add(entry);
		nodes.add(entry);
		while (!queue.isEmpty()) {
			node = queue.remove();
			// nodes.remove(node);
			List<CFGNode> successors = node.getSuccessor();
			if (successors != null) {
				if (successors.size() == 1) {
					CFGNode succ = successors.get(0);
					if (!succ.isVisited()) {
						succ.setVisited();
						if (succ.getIncomingDegree() == 1 && (succ != end || node.statements == null)) {
							nodes.remove(node);
							node.combineNode(succ);
							if (succ == end) {
								node.label = succ.label;
								end = node;
							}
							queue.add(node);
							nodes.add(node);
							continue;
						} else {
							queue.add(succ);
							// nodes.add(node);
							nodes.add(succ);
						}
					}

					if (node.isTransitionNodeForEliminate() && succ.isMergeNode() && !succ.isWhileNode()) {

						if (succ.getParents().contains(node)) {
							succ.removeParent(node);
						}
						for (CFGNode n : node.getParents()) {
							n.deletePointTo();
							n.addSuccessor(succ);
						}
						nodes.remove(node);
					}

				} else {
					for (CFGNode n : successors) {
						if (!n.isVisited()) {
							n.setVisited();
							queue.add(n);
							nodes.add(n);
						}
					}
				}
			}
		}
	}

	public boolean methodHasReturnValue() {
		for (CFGNode n : end.parents) {
			if (!n.lastStatementIsReturn()) {
				// throw new IllegalArgumentException(n.getName());
				return false;
			}
		}
		return true;
	}

	public void checkMethodOutOfCtrl(IrProgram p) {
		if (this.hasReturnValue && !methodHasReturnValue()) {
			// System.out.println("Sorry, this method out of Control " + method);
			// System.exit(-2);
			CFGNode newNode = new CFGNode();
			newNode.addLowLevelIr(
					IrQuadWithLocForFuncInvoke.getPrintFunc("Sorry, this method out of Control " + method + "\\n", p));
			newNode.addLowLevelIr(IrQuadWithLocForFuncInvoke.getExitFunc(-2));
			// entry.deletePointTo();
			entry = new EntryNode(0);
			entry.addSuccessor(newNode);
			newNode.deletePointTo();
			// this.end();

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
