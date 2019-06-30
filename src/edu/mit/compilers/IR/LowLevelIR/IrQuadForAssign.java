package edu.mit.compilers.IR.LowLevelIR;


import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.trees.SemanticCheckerNode;

public class IrQuadForAssign extends IrQuad{
	
	public boolean is_64bit = false;
	private IrQuadForAssign(IrAssignment assign) {
		
		IrExpression binary = assign.getRhs();
		if(!(binary instanceof BinaryExpression)) {
			symbol = "mov";
			op1 = (IrOperand) binary;
			//op2 = null;
			op2 = assign.getLhs();
			dest = null;
		} else {
			BinaryExpression expr = (BinaryExpression) binary;
			if(expr.getSymbol().equals("+"))    symbol = "add";
			if(expr.getSymbol().equals("-"))    symbol = "sub";
			if(expr.getSymbol().equals("*"))    symbol = "imul";
			if(expr.getSymbol().equals("/"))    symbol = "div";
			if(expr.getSymbol().equals("%"))    symbol = "mod";
			this.op1 = (IrOperand) expr.getlhs();
			this.op2 = (IrOperand) expr.getrhs();
			this.dest = assign.getLhs();
		}
	}
	
	public IrQuadForAssign(IrAssignment assign, VariableTable v, MethodTable m) {
		this(assign);
		this.v = v;
		this.m = m;
		semantics = new SemanticCheckerNode();
		if(semantics.getIrOperandType(assign.getLhs(), v, m).equals(IrType.IntType)) {
			is_64bit = true;
			this.symbol += "q";
		}
		if(semantics.getIrOperandType(assign.getLhs(), v, m).equals(IrType.BoolType)) {
			is_64bit = false;
			this.symbol += "b";
		}
	}
	
	
	@Override
	public void accept(IrNodeVistor vistor) {
		vistor.visit(this);
	}
	
}
