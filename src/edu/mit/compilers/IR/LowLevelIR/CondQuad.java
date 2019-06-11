package edu.mit.compilers.IR.LowLevelIR;

import java.util.Stack;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class CondQuad extends LowLevelIR {
	public Stack<LowLevelIR> condStack;
	public Stack<String> symbol;

	public CondQuad() {
		condStack = new Stack<>();
		symbol = new Stack<>();
	}

	public CondQuad(IrQuad quad) {
		this();
		condStack.add(quad);
	}

	public CondQuad(IrExpression expr, VariableTable vtb, MethodTable mtb) {
		this();
		addCondQuad(expr, vtb, mtb);
	}

	public Stack<LowLevelIR> getCondStack() {
		return condStack;
	}

	public void setCondStack(Stack<LowLevelIR> condStack) {
		this.condStack = condStack;
	}

	public Stack<String> getSymbol() {
		return symbol;
	}

	public void setSymbol(Stack<String> symbol) {
		this.symbol = symbol;
	}

	public void addCondQuad(IrExpression expr, VariableTable vtb, MethodTable mtb) {
		if (expr instanceof BinaryExpression && ((BinaryExpression) expr).contactWithAndOr()) {
			BinaryExpression binary = (BinaryExpression) expr;
			symbol.push(binary.getSymbol());
			condStack.push(generateCondLowIr(binary.getlhs(), vtb, mtb));
			condStack.push(generateCondLowIr(binary.getrhs(), vtb, mtb));
			// condStack.push(generateCondLowIr(binary, vtb, mtb));
		} else {
			condStack.push(generateCondLowIr(expr, vtb, mtb));
		}
	}

	private IrQuad generateCmpQuadForBool(IrOperand opr, VariableTable vtb, MethodTable mtb) {
		return new IrQuad(">", opr, IrLiteral.getLiteral(0), null, vtb, mtb);
	}

	private LowLevelIR generateCondLowIr(IrExpression expr, VariableTable vtb, MethodTable mtb) {
		LowLevelIR ir;
		if (expr instanceof IrOperand)
			ir = generateCmpQuadForBool((IrOperand) expr, vtb, mtb);
		else if (expr instanceof BinaryExpression)
			ir = setCondQuad((BinaryExpression) expr, vtb, mtb);
		else
			ir = null;
		return ir;
	}

	public LowLevelIR setCondQuad(BinaryExpression binaryExpr, VariableTable vtb, MethodTable mtb) {
		if (!binaryExpr.contactWithAndOr()) {
			return new IrQuad(binaryExpr, vtb, mtb);
		} else {
			CondQuad quad = new CondQuad();
			LowLevelIR lhs, rhs;
			IrExpression binaryLhs = binaryExpr.getlhs();
			IrExpression binaryRhs = binaryExpr.getrhs();
			lhs = generateCondLowIr(binaryLhs, vtb, mtb);
			rhs = generateCondLowIr(binaryRhs, vtb, mtb);
			quad.condStack.push(lhs);
			quad.condStack.push(rhs);
			quad.symbol.push(binaryExpr.getSymbol());
			return quad;
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		if (symbol.isEmpty()) {
			sb.append(condStack.peek().getName());
		} else {
			int size = symbol.size();
			Stack<LowLevelIR> tempStack = new Stack<>();
			for (int i = size - 1; i >= 0; i--) {
				String sym = symbol.get(i);
				LowLevelIR op1 = condStack.pop();
				LowLevelIR op2 = condStack.pop();
				tempStack.push(op1);
				tempStack.push(op2);
				sb.append(op1.getName());
				sb.append(sym + "\n");
				sb.append(op2.getName());
			}
			
			while(!tempStack.isEmpty()) {
				condStack.push(tempStack.pop());
			}
			
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
