package edu.mit.compilers.IR.expr.operand;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.trees.ParseTreeNode;

public class IrLiteral extends IrExpression{
	
	IrType type;
	public IrLiteral(ParseTreeNode node) {
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

}
