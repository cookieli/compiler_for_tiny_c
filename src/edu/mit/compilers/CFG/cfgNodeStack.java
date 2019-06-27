package edu.mit.compilers.CFG;

import java.util.ArrayList;
import java.util.List;

public class cfgNodeStack {
	private List<CFGNode> lst;
	
	public cfgNodeStack() {
		lst =  new ArrayList<>();
	}
	
	public boolean isEmpty() {
		return lst.isEmpty();
	}
	
	public void push(CFGNode node) {
		lst.add(node);
	}
	
	public CFGNode pop() {
		return lst.remove(lst.size() - 1);
	}
	
	public CFGNode peek() {
		return lst.get(lst.size() - 1);
	}
	
	public CFGNode getMostClosestLoopEndNode() {
		for(int i = lst.size()- 1; i>=0; i--) {
			if(lst.get(i).istLoopEnd()) {
				return lst.get(i);
			}
		}
		return null;
	}
	
	
}
