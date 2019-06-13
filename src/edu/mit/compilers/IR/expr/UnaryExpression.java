package edu.mit.compilers.IR.expr;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrOperand;

public class UnaryExpression extends IrExpression{
	String symbol;
	IrExpression expr;
	
	public UnaryExpression() {
		super();
	}
	
	public UnaryExpression(String symbol, IrExpression expr) {
		super(expr.getLineNumber(), expr.getColumnNumber(), expr.getFilename());
		this.symbol = symbol;
		this.expr = expr;
	}
	
	public UnaryExpression(UnaryExpression unary) {
		symbol = new StringBuilder(unary.getSymbol()).toString();
		expr = (IrExpression) unary.getIrExpression().copy();
	}
	
	public String getSymbol() {
		return symbol;
	}
	public IrExpression getIrExpression() {
		return expr;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if(expr == null) return null;
		return symbol + " "+ expr.getName();
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isBool() {
		return symbol.equals("!");
	}

	@Override
	public List<IrOperand> operandList() {
		// TODO Auto-generated method stub
		List<IrOperand> lst = new ArrayList<>();
		lst.addAll(expr.operandList());
		return lst;
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new UnaryExpression(this);
	}

}
