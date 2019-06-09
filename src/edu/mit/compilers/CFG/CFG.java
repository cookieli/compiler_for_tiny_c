package edu.mit.compilers.CFG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
	
	public static List<CFGNode> destruct(IrIfBlockQuad block){
		List<CFGNode> truePair =destruct(block.getTrueBlock());
		List<CFGNode> falsePair = null;
		if(block.getFalseBlock() != null)
			falsePair = destruct(block.getFalseBlock());
		CFGNode beginNode = new CFGNode(block.getCondQuad());
		CFGNode noOp = new NoOpCFG();
		truePair.get(1).addSuccessor(noOp);
		beginNode.addSuccessor(truePair.get(0));
		if(falsePair != null) {
			falsePair.get(1).addSuccessor(noOp);
			beginNode.addSuccessor(falsePair.get(0));
		}
		else
			beginNode.addSuccessor(noOp);
		List<CFGNode> lst = new ArrayList<>();
		lst.add(beginNode);
		lst.add(noOp);
		return lst;
	}
	
	private static List<CFGNode> destructOnelineQuad(LowLevelIR quad){
		CFGNode newNode = new CFGNode(quad);
		List<CFGNode> list = new ArrayList<>();
		list.add(newNode);
		list.add(newNode);
		return list;
	}
	
	
	public static List<CFGNode> destruct(LowLevelIR ir){
		if(ir instanceof IrQuadWithLocForFuncInvoke || ir instanceof IrQuadWithLocation)
			return destructOnelineQuad(ir);
		else if(ir instanceof IrIfBlockQuad)
			return destruct((IrIfBlockQuad) ir);
		return null;
	}
	
	public static List<CFGNode> destruct(IrBlock block){
		List<CFGNode> pair = null;
		List<IrStatement> lowLevelIRs = block.getStatements();
		LowLevelIR firstQuad = (LowLevelIR) lowLevelIRs.get(0);
		pair = destruct(firstQuad);
		CFGNode beginNode = pair.get(0);
		CFGNode endNode = pair.get(1);
		CFGNode cursor = null;
		for(int i = 1; i < lowLevelIRs.size(); i++) {
			pair = destruct((LowLevelIR) lowLevelIRs.get(i));
			endNode.addSuccessor(pair.get(0));
			endNode = pair.get(1);
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
				} else {
					for (CFGNode n : successors) {
						if(!n.isVisited())
							queue.add(n);
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
		sb.append(method+", ");
		sb.append("@function\n");
		sb.append(method+":"+"\n");
		return sb.toString();
		
	}
	
	

}
