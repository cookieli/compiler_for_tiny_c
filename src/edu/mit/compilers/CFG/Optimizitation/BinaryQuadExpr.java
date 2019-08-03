package edu.mit.compilers.CFG.Optimizitation;

import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;

public class BinaryQuadExpr {
	
	public IrOperand op1;
	public IrOperand op2;
	public String symbol;
	
	public BinaryQuadExpr(IrOperand op1, IrOperand op2, String symbol) {
		this.op1 = op1;
		this.op2 = op2;
		this.symbol = symbol;
	}
	
	
	@Override
	public String toString() {
		return symbol +" " + op1.getName() +" " + op2.getName()+"\n";
	}
	
	@Override
	public int hashCode() {
		return op1.hashCode() + op2.hashCode() + symbol.hashCode();
	}
	
	public boolean contains(IrLocation loc) {
		return loc.equals(op1) || loc.equals(op2);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o == this)
			return true;
		if(!(o instanceof BinaryQuadExpr))
			return false;
		BinaryQuadExpr bQ = (BinaryQuadExpr) o;
		if(op1.equals(bQ.op1) && op2.equals(bQ.op2) && symbol.equals(bQ.symbol))
			return true;
		return false;
	}
	
	
}
