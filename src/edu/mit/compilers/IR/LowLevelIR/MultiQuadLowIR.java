package edu.mit.compilers.IR.LowLevelIR;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrQuadVistor;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.MultiStatementExpr;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class MultiQuadLowIR  extends LowLevelIR{
	
	public List<IrStatement> quadLst;
	
	
	public MultiQuadLowIR(MultiStatementExpr expr, VariableTable v, MethodTable m, IrQuadVistor vistor) {
		List<IrStatement> temp = vistor.currentList;
		
		vistor.currentList = new ArrayList<>();
		for(IrStatement s: expr.getPreStatement()) {
			if(s instanceof IrAssignment) {
				//quadLst.add(new IrQuadForAssign((IrAssignment) s, v,m ));
				((IrAssignment)s).accept(vistor);
			} else if(s instanceof IfBlock) {
				//throw new IllegalArgumentException(s.getName());
				((IfBlock)s).accept(vistor);
			}
		}
		
		IrExpression last = expr.getExpr();
		if(last instanceof BinaryExpression) {
			vistor.currentList.add(new IrQuad((BinaryExpression) last, v, m));
		} else {
			vistor.currentList.add( new IrQuad(">", (IrOperand) last, IrLiteral.getLiteral(0), null, v, m));
		}
		
		quadLst = vistor.currentList;
		vistor.currentList = temp;
		
	}
	
	public List<IrStatement> getQuadLst(){
		return quadLst;
	}
	
	public void setQuadLst(List<IrStatement> lst) {
		this.quadLst = lst;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("MultiLowLevelIr: ");
		for(IrStatement ir: quadLst) {
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
