package edu.mit.compilers.IR.expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrOperand;

public class BinaryExpression extends IrExpression {
	
	
	public IrExpression lhs;
	public IrExpression rhs;
	public String symbol;
	
	public BinaryExpression(IrExpression lhs, IrExpression rhs, String symbol) {
		super(lhs.getLineNumber(), lhs.getColumnNumber(), lhs.getFilename());
		this.lhs = lhs;
		this.rhs = rhs;
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public IrExpression getlhs() {
		return lhs;
	}
	
	public IrExpression getrhs() {
		return rhs;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		String left = lhs == null ? "null":lhs.getName();
		String right = rhs == null ? "null": rhs.getName();
		
		return left + " " + symbol + " "+ right;
	}
	
	
	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}
	
	public List<IrOperand> operandList(){
		List<IrOperand> list = new ArrayList<>();
		list.addAll(lhs.operandList());
		list.addAll(rhs.operandList());
		return list;
	}
	
}
