package edu.mit.compilers.CFG.Optimizitation;

public class VirtualExpr {
	public VirtualValue op1;
	public VirtualValue op2;
	public String symbol;
	
	
	public VirtualExpr(VirtualValue op1, VirtualValue op2, String symbol) {
		this.op1 = op1;
		this.op2 = op2;
		this.symbol = symbol;
	}
	
	@Override
	public String toString() {
		return op1.toString() + " "+symbol+" " + op2.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o == this)
			return true;
		if(!(o instanceof VirtualExpr))
			return false;
		VirtualExpr v = (VirtualExpr)o;
		return v.op1.equals(this.op1) && v.op2.equals(this.op2) && symbol.equals(v.symbol);
	}
	
	@Override
	public int hashCode() {
		return op1.hashCode() + op2.hashCode() + symbol.hashCode();
	}
}
