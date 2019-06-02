package edu.mit.compilers.CFG;

import java.util.ArrayList;

public class NoOpCFG extends CFGNode{
	public NoOpCFG() {
		this.statements = null;
		this.inComingDegree = 0;
		this.pointTo = new ArrayList<>();
		
	}
}
