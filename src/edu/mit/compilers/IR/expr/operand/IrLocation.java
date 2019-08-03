package edu.mit.compilers.IR.expr.operand;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.CFG.Optimizitation.AvailableExpression;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.trees.AstCreator;
import edu.mit.compilers.trees.ParseTreeNode;

public class IrLocation extends IrOperand {

	public String location;
	public IrExpression sizeExpr = null;
	public boolean isArray = false;
	public int naming;

	public int getNaming() {
		return naming;
	}

	@Override
	public int hashCode() {

		return location.hashCode() + naming;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof IrLocation))
			return false;
		IrLocation loc = (IrLocation) o;
		if (sizeExpr == null) {
			if (loc.location.equals(this.location) && loc.naming == this.naming)
				return true;
		} else {
			if (loc.location.equals(this.location) && loc.naming == this.naming && sizeExpr.equals(loc.sizeExpr))
				return true;
		}
		return false;

	}
	
	public boolean isTempVariable() {
		return location.startsWith("$") || location.startsWith(AvailableExpression.tempName);
	}

	public void setNaming(int naming) {

		this.naming = naming;
	}

	public IrLocation() {
		super();
	}

	public IrLocation(String l) {
		super();
		location = l;
	}

	public void setSizeExpr(IrExpression sizeExpr) {
		this.sizeExpr = sizeExpr;
		if (isArray == false)
			isArray = true;
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
		super(l.getLineNumber(), l.getColumnNumber(), l.getFilename());
		location = new StringBuilder(l.location).toString();
		if (l.sizeExpr != null)
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
		String sym = "";
		if (IrOperand.inDataFlow) {
			sym = "(" + naming + ")";
		}
		if (isArray)
			return location + sym + "[" + sizeExpr.getName() + "]";
		return location + sym;
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
		// super.copy();

		return new IrLocation(this);
	}

}
