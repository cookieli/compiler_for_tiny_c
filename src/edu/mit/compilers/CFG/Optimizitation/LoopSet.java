package edu.mit.compilers.CFG.Optimizitation;

import java.util.HashSet;

import edu.mit.compilers.CFG.CFGNode;

public class LoopSet {
	public CFGNode  header;
	public HashSet<CFGNode> loopSets;
	public HashSet<CFGNode> ends;
	
	public LoopSet(CFGNode n) {
		this.header = n;
		this.loopSets = new HashSet<>();
		this.ends = new HashSet<>();
		
		loopSets.add(n);
		//ends.add(n);
	}
	
	public void addEnd(CFGNode n) {
		ends.add(n);
		loopSets.add(n);
	}
	
	public void addLoop(HashSet<CFGNode> loop) {
		loopSets.addAll(loop);
	}
	
	
	public boolean contains(CFGNode n) {
		return loopSets.contains(n);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--header---\n");
		sb.append(header.getStats());
		sb.append("---header---\n");
		for(CFGNode n: loopSets) {
			if(!n.equals(header) && !ends.contains(n))
				sb.append(n.getStats());
		}
		sb.append("-----end------\n");
		for(CFGNode n: ends) {
			sb.append(n.getStats());
		}
		sb.append("----end-----\n");
		return sb.toString();
	}
	
}
