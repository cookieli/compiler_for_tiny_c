package edu.mit.compilers.IR.statement;

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
	
	public IrLocation getLhs(){
		return lhs;
	}
	
	public IrExpression getRhs() {
		return rhs;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return lhs.getName()+ " "+ symbol +" " +rhs.getName();
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
	}
}
