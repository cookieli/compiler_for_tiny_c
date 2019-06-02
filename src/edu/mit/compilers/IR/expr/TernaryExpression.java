package edu.mit.compilers.IR.expr;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrOperand;

public class TernaryExpression extends IrExpression{
	
	public IrExpression cond;
	public IrExpression firstExpr;
	public IrExpression secondExpr;
	
	public TernaryExpression(IrExpression cond, IrExpression firstExpr, IrExpression secondExpr) {
		this.cond = cond;
		this.firstExpr = firstExpr;
		this.secondExpr = secondExpr;
	}
	
	public TernaryExpression(TernaryExpression t) {
		this((IrExpression)t.getCondExpr().copy(), (IrExpression)t.firstExpr.copy(), (IrExpression)t.getSecondExpr().copy());
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return cond.getName() + " ?:"+ firstExpr.getName() + ": "+ secondExpr.getName();
	}

	@Override
	public List<IrOperand> operandList() {
		// TODO Auto-generated method stub
		List<IrOperand> lst = new ArrayList<>();
		lst.addAll(cond.operandList());
		lst.addAll(firstExpr.operandList());
		lst.addAll(secondExpr.operandList());
		return lst;
	}
	
	public IrExpression getCondExpr() {
		return cond;
	}
	
	public IrExpression getFirstExpr() {
		return firstExpr;
	}
	
	public IrExpression getSecondExpr() {
		return secondExpr;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new TernaryExpression(this);
	}

}
