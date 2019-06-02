package edu.mit.compilers.CFG;

import java.util.ArrayList;

import edu.mit.compilers.IR.Quad.ExitPoint;

public class ExitNode extends CFGNode{
	public ExitNode() {
		this.statements = new ArrayList<>();
		this.inComingDegree = 0;
		this.pointTo = null;
		this.statements.add(ExitPoint.exit);
	}
	
	//public static final ExitNode exit = new ExitNode();
}
