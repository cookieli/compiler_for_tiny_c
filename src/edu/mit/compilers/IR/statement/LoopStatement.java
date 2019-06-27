package edu.mit.compilers.IR.statement;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.trees.ParseTreeNode;

public class LoopStatement extends IrStatement{
	
	public boolean isContinue = false;
	
	public boolean isContinue() {
		return isContinue;
	}
	
	public LoopStatement(ParseTreeNode n, String fileName) {
		super(n.getToken().getLine(), n.getToken().getColumn(), fileName);
		if(n.getName().equals("continue"))
			isContinue = true;
		
	}
	
	public LoopStatement(LoopStatement l) {
		super(l.getLineNumber(), l.getColumnNumber(), l.getFilename());
		isContinue = l.isContinue;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if(isContinue) return "continue";
		return "break";
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new LoopStatement(this);
	}

}
