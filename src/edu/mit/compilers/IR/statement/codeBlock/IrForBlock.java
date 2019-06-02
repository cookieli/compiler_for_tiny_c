package edu.mit.compilers.IR.statement.codeBlock;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.VariableTable;

public class IrForBlock extends IrStatement{
	
	IrAssignment initialAssign;
	IrExpression boolExpr;
	IrAssignment stepFunction;
	IrBlock block;
	
	public IrForBlock(IrAssignment initialAssign, IrExpression boolExpr, IrAssignment stepFunction, IrBlock block) {
		this.initialAssign = initialAssign;
		this.boolExpr = boolExpr;
		this.stepFunction = stepFunction;
		this.block = block;
	}
	
	public IrForBlock(IrForBlock forBlock) {
		initialAssign = (IrAssignment) forBlock.getInitialAssign().copy();
		boolExpr = (IrExpression)forBlock.getBoolExpr().copy();
		stepFunction = (IrAssignment)forBlock.getStepFunction().copy();
		block = (IrBlock) forBlock.copy();
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("for "+ initialAssign.getName() + " "+ boolExpr.getName()+ " "+ stepFunction.getName());
		sb.append("\n");
		sb.append(block.getName());
		return sb.toString();
		
	}
	
	public IrAssignment getInitialAssign() {
		return initialAssign;
	}
	
	public IrExpression getBoolExpr() {
		return boolExpr;
	}
	
	public IrAssignment getStepFunction() {
		return stepFunction;
	}
	
	public IrBlock getBlock() {
		return block;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);
		
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new IrForBlock(this);
	}
	
	@Override
	public void setLocalVarTableParent(VariableTable v) {
		block.setLocalVarTableParent(v);
	}
	

}
