package edu.mit.compilers.IR.statement;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrLocation;

public class IrAssignment extends IrStatement{
	private final IrLocation lhs;
	private final IrExpression rhs;
	private final String symbol;
	public IrAssignment() {
		lhs = null;
		rhs = null;
		symbol = null;
	}
	
	public IrAssignment(IrLocation lhs, IrExpression rhs, String symbol) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.symbol = symbol;
	}
	
	public IrAssignment(IrAssignment assign) {
		lhs = (IrLocation) assign.getLhs().copy();
		rhs = (IrExpression)assign.getRhs().copy();
		symbol = new StringBuilder(assign.symbol).toString();
		
	}
	
	public IrLocation getLhs(){
		return lhs;
	}
	
	public IrExpression getRhs() {
		return rhs;
	}
	
	public String getSymbol() {
		return symbol;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if(rhs != null)
			return lhs.getName()+ " "+ symbol +" " +rhs.getName();
		else
			return lhs.getName() + " "+ symbol+" ";
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new IrAssignment(this);
	}
}
