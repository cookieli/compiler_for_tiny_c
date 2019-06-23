package edu.mit.compilers.IR.statement.codeBlock;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.VariableTable;

public class IrWhileBlock extends IrStatement{
	
	public List<IrStatement> preTempStat;
	public IrExpression boolExpr;
	public IrBlock codeBlock;
	
	
	public List<IrStatement> getPreTempStat(){
		if(preTempStat == null)
			preTempStat = new ArrayList<>();
		return preTempStat;
	}
	
	public void setPreTempStat(List<IrStatement> lst) {
		this.preTempStat = lst;
	}
	
	
	public IrWhileBlock(IrExpression expr, IrBlock block) {
		boolExpr = expr;
		codeBlock = block;
	}
	
	public IrWhileBlock(IrWhileBlock whileBLock) {
		this.boolExpr = (IrExpression) whileBLock.getBoolExpr().copy();
		this.codeBlock = (IrBlock) whileBLock.getCodeBlock().copy();
	}

	public IrExpression getBoolExpr() {
		return boolExpr;
	}

	public void setBoolExpr(IrExpression boolExpr) {
		this.boolExpr = boolExpr;
	}

	public IrBlock getCodeBlock() {
		return codeBlock;
	}

	public void setCodeBlock(IrBlock codeBlock) {
		this.codeBlock = codeBlock;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		if(preTempStat != null) {
			for(IrStatement s: preTempStat) {
				sb.append(s.getName());
				sb.append("\n");
			}
		}
		sb.append("while "+ boolExpr.getName());
		sb.append("\n");
		sb.append(codeBlock.getName());
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new IrWhileBlock(this);
	}
	
	@Override
	public void setLocalVarTableParent(VariableTable v) {
		codeBlock.setLocalVarTableParent(v);
	}

}
