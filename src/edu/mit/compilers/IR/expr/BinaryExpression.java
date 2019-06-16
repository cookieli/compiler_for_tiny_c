package edu.mit.compilers.IR.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.utils.Util;

public class BinaryExpression extends IrExpression {
	
	
	public IrExpression lhs;
	public void setLhs(IrExpression lhs) {
		this.lhs = lhs;
	}

	public void setRhs(IrExpression rhs) {
		this.rhs = rhs;
	}

	public IrExpression rhs;
	public String symbol;
	
	public BinaryExpression(IrExpression lhs, IrExpression rhs, String symbol) {
		super(lhs.getLineNumber(), lhs.getColumnNumber(), lhs.getFilename());
		this.lhs = lhs;
		this.rhs = rhs;
		this.symbol = symbol;
	}
	
	public BinaryExpression(BinaryExpression binary) {
		super(binary.getLineNumber(), binary.getColumnNumber(), binary.getFilename());
		this.lhs = (IrExpression) binary.getlhs().copy();
		this.rhs = (IrExpression) binary.getrhs().copy();
		this.symbol = new StringBuilder(binary.getSymbol()).toString();
	}
	
	public boolean isCmpExpr() {
		return Arrays.asList(Util.comOp).contains(symbol);
	}
	
	public boolean isBoolExpr() {
		return Arrays.asList(Util.boolBinaryOp).contains(symbol);
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
	
	public boolean contactWithAndOr(){
		return symbol.equals("&&") || symbol.equals("||");
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
	
	public List<String> symbolList(){
		List<String> list = new ArrayList<>();
		if(lhs instanceof BinaryExpression) {
			list.addAll(((BinaryExpression) lhs).symbolList());
		}
		list.add(this.symbol);
		if(rhs instanceof BinaryExpression)
			list.addAll(((BinaryExpression) rhs).symbolList());
		return list;
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new BinaryExpression(this);
	}
	
}
