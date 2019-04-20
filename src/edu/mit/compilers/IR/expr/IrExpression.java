package edu.mit.compilers.IR.expr;

import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLenExpr;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;

public abstract class IrExpression extends IrNode {
	
	public IrExpression() {
		super();
	}
	public IrExpression(int line, int column, String filename) {
		// TODO Auto-generated constructor stub
		super(line, column, filename);
	}
	
	public static boolean isOperand(IrExpression expr) {
		if(expr instanceof IrLocation || expr instanceof IrLiteral || expr instanceof IrLenExpr || expr instanceof IrFuncInvocation)
			return true;
		return false;
	}
	
	public static boolean isBinaryExpr(IrExpression expr) {
		return expr instanceof BinaryExpression;
	}
	public abstract List<IrOperand> operandList();

}
