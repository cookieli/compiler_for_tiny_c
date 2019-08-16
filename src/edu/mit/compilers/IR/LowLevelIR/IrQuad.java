package edu.mit.compilers.IR.LowLevelIR;


import java.util.HashSet;

import edu.mit.compilers.CFG.Optimizitation.BinaryQuadExpr;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.trees.SemanticCheckerNode;
import edu.mit.compilers.utils.Util;

public class IrQuad extends LowLevelIR{
	public static String[] operand = {"mov", "add", "sub", "mul", "div", "mod"};
	
	
	public HashSet<IrLocation> use;
	
	public HashSet<IrLocation> subsequentAlive;
	
	public void setSubsequentAlive(HashSet<IrLocation> alive) {
		subsequentAlive = new HashSet<>();
		subsequentAlive.addAll(alive);
	}
	
	public void addUse(IrLocation loc) {
		if(use == null) {
			use = new HashSet<>();
		}
		use.add(loc);
	}
	
	public void addAllUse(HashSet <IrLocation> set) {
		if(use == null) {
			use = new HashSet<>();
		}
		use.addAll(set);
	}
	
	public void removeUse(IrLocation loc) {
		if(use == null) {
			return ;
		}
		use.remove(loc);
	}
	public String symbol;
	public IrOperand op1;
	public IrOperand op2;
	public IrOperand dest;
	
	public BinaryQuadExpr binary = null;
	
	
	public SemanticCheckerNode semantics;
	public VariableTable v;
	public MethodTable m;
	
	public boolean isValueAssign() {
		for(int i = 0; i < operand.length; i++) {
			if(symbol.contains(operand[i]))
				return true;
		}
		return false;
	}
	
	public boolean isCmpQuad() {
		for(int i = 0; i < Util.comOp.length; i++) {
			if(symbol.contains(Util.comOp[i])) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isMovAssign() {
		return symbol.contains("mov");
	}
	
	public BinaryQuadExpr getBinaryQuad() {
		if(binary == null)
			binary =  new BinaryQuadExpr(op1, op2, symbol);
		return binary;
	}
	
	public static boolean hasSameDst(IrQuad a, IrQuad b) {
		IrLocation dstA = (IrLocation) a.getDest();
		IrLocation dstB = (IrLocation)b.getDest();
		if(dstA == null)
			dstA = (IrLocation) a.getOp2();
		if(dstB == null)
			dstB = (IrLocation) b.getOp2();
		if(dstA.getId().equals(dstB.getId()) && dstA.getNaming() == dstB.getNaming())
			return true;
		return false;
	}
	
	
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
		if(use != null) {
			sb.append("   use( ");
			for(IrLocation loc: use) {
				sb.append(loc.getName() + ",");
			}
			sb.append(")");
		}
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
	
	public IrOperand getRealDst() {
		if(this.isValueAssign()) {
			if( dest != null)
				return dest;
			return op2;
		}
		return null;
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
