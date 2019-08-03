package edu.mit.compilers.IR.expr.operand;

import edu.mit.compilers.IR.expr.IrExpression;

public abstract class IrOperand extends IrExpression{
	public static boolean inDataFlow = false;;
	public IrOperand() {
		super();
	}
	
	public IrOperand(int lineNum, int columnNum, String fileName) {
		super(lineNum, columnNum, fileName);
	}
}
