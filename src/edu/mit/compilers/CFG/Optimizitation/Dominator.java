package edu.mit.compilers.CFG.Optimizitation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import edu.mit.compilers.CFG.CFG;
import edu.mit.compilers.CFG.CFGNode;

public class Dominator extends DataFlow {

	public Dominator(CFG cfg) {
		super(cfg);
	}

	public void initialize() {
		CFGNode entry = methodCFG.entry;
		entry.dominators = new HashSet<>();
		entry.dominators.add(entry);
		nodeSet.remove(entry);
		for (CFGNode n : nodeSet) {
			n.dominators = new HashSet<>();
			n.dominators.addAll(nodeSet);
			n.dominators.add(entry);
		}
	}

	private HashSet<CFGNode> preDominator(CFGNode n) {
		if (n.parents == null || n.parents.size() == 0)
			return null;
		HashSet<CFGNode> ret = new HashSet<>();
		ret.addAll(n.parents.get(0).dominators);
		for (int i = 1; i < n.parents.size(); i++) {
			ret.retainAll(n.parents.get(i).dominators);
		}
		return ret;
	}

	private void dominatorOf(CFGNode n) {
		HashSet<CFGNode> domi = new HashSet<>();
		domi.add(n);
		HashSet<CFGNode> sets = preDominator(n);
		if (sets != null)
			domi.addAll(sets);
		n.dominators = domi;
	}

	private boolean equal(HashSet<CFGNode> a, HashSet<CFGNode> b) {
		for (CFGNode n : a) {
			if (!b.contains(n))
				return false;
		}
		return a.size() == b.size();
	}

	private LoopSet defineLoop(CFGNode header) {
		LoopSet loop = new LoopSet(header);

		if (header.parents == null || header.parents.size() == 0) {
			throw new IllegalArgumentException("this while node doesn't have parents");
		}
		for (CFGNode pre : header.parents) {
			if (pre.dominators.contains(header)) {
				loop.addEnd(pre);
			}
		}
		return loop;
	}

	private List<LoopSet> defineLoop() {

		List<LoopSet> lst = new ArrayList<>();
		HashSet<CFGNode> headers = new HashSet<>();
		for (CFGNode n : methodCFG.nodes) {
			if (n.isWhileNode()) {
				headers.add(n);
			}
		}

		if (headers.size() == 0)
			return null;
		for (CFGNode n : headers) {
			lst.add(defineLoop(n));
		}
		return lst;
	}

	private void insert(CFGNode n, Stack<CFGNode> stack, HashSet<CFGNode> loop) {
		if (!loop.contains(n)) {
			loop.add(n);
			stack.push(n);
		}
	}

	public void constructLoop(LoopSet loopSet) {
		HashSet<CFGNode> loop = new HashSet<>();
		Stack<CFGNode> stack = new Stack<>();
		loop.add(loopSet.header);
		for (CFGNode n : loopSet.ends) {
			insert(n, stack, loop);
		}
		while (!stack.isEmpty()) {
			CFGNode n = stack.pop();
			for (CFGNode pre : n.parents) {
				insert(pre, stack, loop);
			}
		}
		loopSet.addLoop(loop);
	}

	public List<LoopSet> constructLoop() {
		List<LoopSet> lst = defineLoop();
		if (lst != null) {
			for (LoopSet l : lst) {
				constructLoop(l);
			}
		}
		return lst;
	}

	public void addPreHeader(LoopSet loop) {
		CFGNode header = loop.header;
		CFGNode preHeader = new CFGNode();
		// System.out.println(preHeader.getName());
		List<CFGNode> lst = new ArrayList<>();
		lst.addAll(header.parents);
		preHeader.addSuccessor(header);
		int count = 0;
		for (CFGNode n : lst) {
			if (!loop.contains(n)) {
				// System.out.println("add pre header " + count++);
				// System.out.println(n.getStats());
				header.removeParent(n);
				n.removeSucc(header);
				n.addSuccessor(preHeader);
				// System.out.println(n.getName());

			}
		}
		methodCFG.nodes.add(preHeader);
	}

	public void findAlldominators() {
		initialize();
		List<CFGNode> lst;

		while (!nodeSet.isEmpty()) {
			lst = new ArrayList<>();
			lst.addAll(nodeSet);
			CFGNode node = lst.get(0);
			nodeSet.remove(node);
			HashSet<CFGNode> domiCp = new HashSet<>();
			domiCp.addAll(node.dominators);
			System.out.println("this node: ");
			System.out.println(node.getStats());
			System.out.println("before dominate");
			System.out.println(domiCp.size());
			for (CFGNode n : domiCp) {
				System.out.println(n.getStats());
			}
			dominatorOf(node);
			System.out.println("after dominate");
			System.out.println(node.dominators.size());
			for (CFGNode n : node.dominators) {
				System.out.println(n.getStats());
			}
			if (!equal(node.dominators, domiCp)) {
				if (node.getSuccessor() != null) {
					for (CFGNode n : node.getSuccessor()) {
						nodeSet.add(n);
					}
				}
			}

		}
	}

}
