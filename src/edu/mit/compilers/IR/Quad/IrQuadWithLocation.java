package edu.mit.compilers.IR.Quad;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.assembly.AssemblyForArith;
import edu.mit.compilers.trees.SemanticCheckerNode;

public class IrQuadWithLocation extends LowLevelIR{
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getOp1() {
		return op1;
	}

	public void setOp1(String op1) {
		this.op1 = op1;
	}

	public String getOp2() {
		return op2;
	}

	public void setOp2(String op2) {
		this.op2 = op2;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}
	
	
	public String symbol;
	public String op1;
	public String op2;
	public String dest;
	
	public static SemanticCheckerNode semantics = new SemanticCheckerNode();
	
	public IrQuadWithLocation(String symbol, String op1, String op2, String dest){
		this.symbol = symbol;
		this.op1 = op1;
		this.op2 = op2;
		this.dest = dest;
	}
	
	public IrQuadWithLocation(String symbol, String op1, String op2) {
		this(symbol, op1, op2, null);
	}
	
	public IrQuadWithLocation(String symbol, String op1) {
		this(symbol, op1, null, null);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		
		StringBuilder sb = new StringBuilder();
		sb.append(symbol+ " ");
		if(op1 != null)
			sb.append(op1);
		if(op2 != null)
			sb.append(","+ op2 );
		if(dest != null)
			sb.append(","+dest);
		sb.append("\n");
		return sb.toString();
	}
	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		//vistor.visit(this);
		vistor.visit(this);
		
	}
	
	public static IrQuadWithLocation setRegisterZero(String reg) {
		return new IrQuadWithLocation("movq", AssemblyForArith.getAssemblyImm(0), AssemblyForArith.getAsmReg(reg));
	}
	
	public static IrQuadWithLocation setRaxZero() {
		return setRegisterZero("rax");
	}
	
	public static boolean isReg(String opr) {
		return opr.startsWith("%");
	}
	
	public static IrQuadWithLocation movToReg(IrOperand o, String src, String dst, VariableTable v, MethodTable m) {
		if(!isReg(dst))
			throw new IllegalArgumentException("not register");
		if(o instanceof IrLiteral)
			return new IrQuadWithLocation("movq", src, dst);
		if(semantics.getIrOperandType(o, v, m).equals(IrType.IntType)) {
			return new IrQuadWithLocation("movq", src, dst);
		} else if(semantics.getIrOperandType(o,v,m).equals(IrType.BoolType))
			return new IrQuadWithLocation("movzbq", src, dst);
		return null;
	}
	
	public static IrQuadWithLocation setMovOpr(String src, String dst, VariableTable v) {
		String opr = "mov";
		opr +="q";
		return new IrQuadWithLocation(opr, src, dst);
	}
	
	public static IrQuadWithLocation setLeaqOpr(String src, String dst) {
		return new IrQuadWithLocation("leaq", src, dst);
	}
	
	public static IrQuadWithLocation reallocStack(int size) {
		return new IrQuadWithLocation("subq", "$"+Integer.toString(size),"%rsp");
	}
	public static IrQuadWithLocation deallocStack(int size) {
		return new IrQuadWithLocation("addq", "$"+Integer.toString(size), "%rsp");
	}
	
	public static IrQuadWithLocation push(String reg) {
		return new IrQuadWithLocation("pushq", reg);
	}
	
	
}
