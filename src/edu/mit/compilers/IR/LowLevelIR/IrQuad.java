package edu.mit.compilers.IR.LowLevelIR;


import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.trees.SemanticCheckerNode;

public class IrQuad extends LowLevelIR{
	public static String[] operand = {"mov", "add", "sub", "mul", "div", "mod"};
	
	public String symbol;
	public IrOperand op1;
	public IrOperand op2;
	public IrOperand dest;
	
	
	public SemanticCheckerNode semantics;
	public VariableTable v;
	public MethodTable m;
	
	
	public IrQuad() {
		
	}
	
	public IrQuad(VariableTable v, MethodTable m) {
		this.v = v;
		this.m = m;
	}
	
	public IrQuad(String symbol, IrOperand op1, IrOperand op2, IrOperand dest) {
		this.symbol = symbol;
		this.op1 = op1;
		this.op2 = op2;
		this.dest = dest;
	}
	
	public IrQuad(BinaryExpression expr, VariableTable v, MethodTable m) {
		this(v, m);
		if(!(expr.getlhs() instanceof IrOperand) || !(expr.getrhs() instanceof IrOperand))
			throw new IllegalArgumentException("not binary expr we want " + expr.getName());
		this.symbol = expr.getSymbol();
		this.op1 = (IrOperand) expr.getlhs();
		this.op2 = (IrOperand) expr.getrhs();
		this.dest = null;
		semantics = new SemanticCheckerNode();
		
		setOperandType();
			
	}
	
	public IrQuad(String symbol, IrOperand op1, IrOperand op2, IrOperand dest, VariableTable v, MethodTable m) {
		this(symbol, op1, op2, dest);
		this.v = v;
		this.m = m;
		semantics = new SemanticCheckerNode();
		setOperandType();
	}
	
	public void setOperandType() {
		if(semantics.getIrOperandType(op1, v, m).equals(IrType.IntType))
			this.symbol += "q";
		if(semantics.getIrOperandType(op1, v, m).equals(IrType.BoolType))
			this.symbol += "b";
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(symbol+ " ");
		if(op1 != null)
			sb.append(op1.getName() + " ");
		if(op2 != null)
			sb.append(op2.getName() + " ");
		if(dest != null)
			sb.append(dest.getName());
		sb.append("\n");
		return sb.toString();
	}
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public IrOperand getOp1() {
		return op1;
	}

	public void setOp1(IrOperand op1) {
		this.op1 = op1;
	}

	public IrOperand getOp2() {
		return op2;
	}

	public void setOp2(IrOperand op2) {
		this.op2 = op2;
	}

	public IrOperand getDest() {
		return dest;
	}

	public void setDest(IrOperand dest) {
		this.dest = dest;
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
		
	}

}
