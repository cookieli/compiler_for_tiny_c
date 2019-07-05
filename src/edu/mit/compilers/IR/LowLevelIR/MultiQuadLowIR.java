package edu.mit.compilers.IR.LowLevelIR;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.MultiStatementExpr;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class MultiQuadLowIR  extends LowLevelIR{
	
	public List<LowLevelIR> quadLst;
	
	
	public MultiQuadLowIR(MultiStatementExpr expr, VariableTable v, MethodTable m) {
		quadLst = new ArrayList<>();
		for(IrStatement s: expr.getPreStatement()) {
			if(s instanceof IrAssignment) {
				quadLst.add(new IrQuadForAssign((IrAssignment) s, v,m ));
			}
		}
		
		IrExpression last = expr.getExpr();
		if(last instanceof BinaryExpression) {
			quadLst.add(new IrQuad((BinaryExpression) last, v, m));
		} else {
			quadLst.add( new IrQuad(">", (IrOperand) last, IrLiteral.getLiteral(0), null, v, m));
		}
	}
	
	public List<LowLevelIR> getQuadLst(){
		return quadLst;
	}
	
	public void setQuadLst(List<LowLevelIR> lst) {
		this.quadLst = lst;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("MultiLowLevelIr: ");
		for(LowLevelIR ir: quadLst) {
			sb.append(ir.getName());
		}
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
		
	}

}
