package edu.mit.compilers.IR.expr.operand;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.expr.IrExpression;

public class IrLenExpr extends IrOperand{
	
	
	private final IrType type = new IrType(IrType.Type.INT);
	
	public IrLocation operand;
	
	public IrLenExpr(Token opr, String filename) {
		operand = new IrLocation(opr, filename);
		
	}
	
	public IrLenExpr(IrLenExpr expr) {
		operand = (IrLocation) expr.operand.copy();
	}
	
	public IrLocation getOperand() {
		return operand;
	}
	public IrType getType() {
		return type;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "len(" + operand.getId() +")";
	}
	
	@Override
	public int hashCode() {
		return operand.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o == this)
			return true;
		if(!(o instanceof IrLenExpr)) {
			return false;
		}
		IrLenExpr len = (IrLenExpr)o;
		if(len.operand.equals(this.operand))
			return true;
		return false;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IrOperand> operandList() {
		// TODO Auto-generated method stub
		List<IrOperand> lst = new ArrayList<>();
		lst.add(this);
		return lst;
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new IrLenExpr(this);
	}

}
