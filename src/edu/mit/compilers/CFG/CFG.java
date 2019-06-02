package edu.mit.compilers.CFG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CFG {

	public String method;
	public List<CFGNode> nodes;
	public CFGNode entry;
	public CFGNode end;

	public CFG() {
		nodes = new ArrayList<>();
	}

	public CFG(int size) {
		this();
		entry = new EntryNode(size);
		nodes.add(entry);
		end = entry;
	}

	public CFG(int size, String method) {
		this(size);
		this.method = method;
	}

	public void addCFGNode(CFGNode node) {
		end.addSuccessor(node);
		nodes.add(node);
		end = node;
	}

	public void end() {
		addCFGNode(new ExitNode());

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
