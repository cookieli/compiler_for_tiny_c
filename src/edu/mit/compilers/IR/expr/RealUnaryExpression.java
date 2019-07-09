package edu.mit.compilers.IR.expr;

import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrOperand;

public class RealUnaryExpression extends UnaryExpression{
	
	public String symbol;
	
	public IrExpression expr;
	
	public RealUnaryExpression(String symbol, IrExpression expr) {
		this.symbol = symbol;
		this.expr = expr;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if(this.symbol == null)
			return expr.getName();
		if(expr == null)
			throw new IllegalArgumentException();
		return symbol + " " + expr.getName();
	}
	@Override
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public IrExpression getIrExpression() {
		return expr;
	}
	
	@Override
	public void setExpr(IrExpression expr) {
		this.expr = expr;
	}

	@Override
	public List<IrOperand> operandList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

}
