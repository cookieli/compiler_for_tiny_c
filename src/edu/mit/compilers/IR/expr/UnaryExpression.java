package edu.mit.compilers.IR.expr;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrOperand;

public class UnaryExpression extends IrExpression{
	List<String> sym;
	IrExpression expr;
	
	public UnaryExpression() {
		super();
	}
	
	public UnaryExpression(String symbol, IrExpression expr) {
		super(expr.getLineNumber(), expr.getColumnNumber(), expr.getFilename());
		this.sym = new ArrayList<>();
		this.sym.add(symbol);
		this.expr = expr;
	}
	
	
	public UnaryExpression(List<String> lst, IrExpression expr) {
		super(expr.getLineNumber(), expr.getColumnNumber(), expr.getFilename());
		sym = lst;
		this.expr = expr;
	}
	
	
	public UnaryExpression(UnaryExpression unary) {
		//symbol = new StringBuilder(unary.getSymbol()).toString();
		this.sym = new ArrayList<>();
		for(int i = 0; i < unary.sym.size(); i++) {
			this.sym.add(new StringBuilder(unary.sym.get(i)).toString());
		}
		expr = (IrExpression) unary.getIrExpression().copy();
	}
	
	public String getSymbol() {
		return sym.get(0);
	}
	public IrExpression getIrExpression() {
		return expr;
	}
	
	public void setExpr(IrExpression expr) {
		this.expr = expr;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if(expr == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < sym.size(); i++){
			sb.append(sym.get(i) + " ");
		}
		sb.append(expr.getName());
		return sb.toString();
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}
	
	public RealUnaryExpression convertToRealUnary() {
		String symbol;
		if(sym.size() %2 == 0)
			symbol = null;
		else
			symbol = sym.get(0);
		return new RealUnaryExpression(symbol, this.expr);
	}
	
	public boolean isBool() {
		return sym.get(0).equals("!");
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
