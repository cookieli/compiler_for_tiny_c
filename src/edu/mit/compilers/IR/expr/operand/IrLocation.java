package edu.mit.compilers.IR.expr.operand;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.trees.AstCreator;
import edu.mit.compilers.trees.ParseTreeNode;

public class IrLocation extends IrOperand{
	
	public String location;
	public IrExpression sizeExpr = null;
	public boolean isArray = false;
	public IrLocation() {
		super();
	}
	
	public IrLocation(String l) {
		super();
		location = l;
	}
	
	public void setSizeExpr(IrExpression sizeExpr) {
		this.sizeExpr = sizeExpr;
	}
	
	public IrLocation(Token t, String filename) {
		super(t.getLine(), t.getColumn(), filename);
		location = t.getText();
	}
	
	public IrLocation(ParseTreeNode n, String filename) {
		this(n.getFirstChild().getToken(), filename);
		sizeExpr = AstCreator.getSpecificExpr(n.getLastChild());
		isArray = true;
	}
	public IrLocation(IrLocation l) {
		super(l.getLineNumber(),l.getColumnNumber(), l.getFilename());
		location = new StringBuilder(l.location).toString();
		if(l.sizeExpr != null)
			sizeExpr = (IrExpression) l.sizeExpr.copy();
		isArray = l.isArray;
		
	}
	
	public boolean locationIsArray() {
		return isArray;
	}
	
	public IrExpression getSizeExpr() {
		return sizeExpr;
	}
	
	public String getId() {
		return location;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if(isArray)
			return location + "["+sizeExpr.getName()+"]";
		return location;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IrOperand> operandList() {
		// TODO Auto-generated method stub
		List<IrOperand> lst = new ArrayList<>();
		lst.add(this);
		return lst;
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		//super.copy();
		
		return new IrLocation(this);
	}

}
