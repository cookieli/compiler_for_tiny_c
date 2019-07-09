package edu.mit.compilers.IR.expr;

import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrStatement;

public class MultiStatementExpr extends IrExpression{
	
	public List<IrStatement> preStatement;
	
	public IrExpression expr;
	
	public MultiStatementExpr(IrExpression loc, List<IrStatement> lst) {
		this.expr = loc;
		this.preStatement = lst;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("MultiStatement:\n");
		if(preStatement != null) {
			for(IrStatement s: preStatement)
				sb.append(s.getName());
		}
		sb.append(expr.getName());
		sb.append("\n");
		sb.append("Multi end\n");
		return sb.toString();
	}

	public List<IrStatement> getPreStatement() {
		return preStatement;
	}

	public void setPreStatement(List<IrStatement> preStatement) {
		this.preStatement = preStatement;
	}

	public IrExpression getExpr() {
		return expr;
	}

	public void setExpr(IrLocation expr) {
		this.expr = expr;
	}

	@Override
	public List<IrOperand> operandList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

}
