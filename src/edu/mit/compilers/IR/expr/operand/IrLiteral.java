package edu.mit.compilers.IR.expr.operand;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.trees.ParseTreeNode;

public class IrLiteral extends IrOperand{
	
	IrType type;
	public IrLiteral(ParseTreeNode node, String filename) {
		super(node.getToken().getLine(), node.getToken().getColumn(), filename);
		type = new IrType(node);
			
	}
	public IrType getType() {
		return type;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return type.toString() + " "+ "Literal";
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

}
