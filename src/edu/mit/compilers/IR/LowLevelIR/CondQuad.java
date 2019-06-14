package edu.mit.compilers.IR.LowLevelIR;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.UnaryExpression;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class CondQuad extends LowLevelIR {
	public List<LowLevelIR> condStack;
	public List<String> symbol;

	public CondQuad() {
		condStack = new ArrayList<>();
		symbol = new ArrayList<>();
	}

	public CondQuad(IrQuad quad) {
		this();
		condStack.add(quad);
	}

	public CondQuad(IrExpression expr, VariableTable vtb, MethodTable mtb) {
		this();
		addCondQuad(expr, vtb, mtb);
	}

	public List<LowLevelIR> getCondStack() {
		return condStack;
	}

	public void setCondStack(List<LowLevelIR> condStack) {
		this.condStack = condStack;
	}

	public List<String> getSymbol() {
		return symbol;
	}

	public void setSymbol(Stack<String> symbol) {
		this.symbol = symbol;
	}

	public void addCondQuad(IrExpression expr, VariableTable vtb, MethodTable mtb) {
		if (expr instanceof BinaryExpression && ((BinaryExpression) expr).contactWithAndOr()) {
			BinaryExpression binary = (BinaryExpression) expr;
			symbol.add(binary.getSymbol());
			condStack.add(generateCondLowIr(binary.getlhs(), vtb, mtb));
			condStack.add(generateCondLowIr(binary.getrhs(), vtb, mtb));
			// condStack.push(generateCondLowIr(binary, vtb, mtb));
		} else if (expr instanceof UnaryExpression) {
			symbol.add(((UnaryExpression) expr).getSymbol());
			condStack.add(generateCondLowIr(((UnaryExpression) expr).getIrExpression(), vtb, mtb));
		} else {
			condStack.add(generateCondLowIr(expr, vtb, mtb));
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
		else if (expr instanceof UnaryExpression) {
			ir = setCondQuad((UnaryExpression) expr, vtb, mtb);
		} else
			ir = null;
		return ir;
	}

	private CondQuad setCondQuad(UnaryExpression unary, VariableTable vtb, MethodTable mtb) {
		CondQuad quad = new CondQuad();
		quad.symbol.add(unary.getSymbol());
		quad.condStack.add(generateCondLowIr(unary.getIrExpression(), vtb, mtb));
		return quad;
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
			quad.condStack.add(lhs);
			quad.condStack.add(rhs);
			quad.symbol.add(binaryExpr.getSymbol());
			return quad;
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		if (symbol.isEmpty()) {
			sb.append(condStack.get(0).getName());
		} else if (symbol.size() == 1 && symbol.get(0).equals("!")) {
			sb.append("!" + condStack.get(0).getName());
		} else {
			int size = symbol.size();
			int stackCursor = 0;
			for (int i = 0; i < size; i++) {
				String sym = symbol.get(i);
				if (!sym.equals("!")) {
					LowLevelIR op1 = condStack.get(stackCursor++);
					LowLevelIR op2 = condStack.get(stackCursor++);
					sb.append(op1.getName());
					sb.append(sym + "\n");
					sb.append(op2.getName());
				} else {
					LowLevelIR op1 = condStack.get(stackCursor++);
					sb.append(sym + "\n");
					sb.append(op1.getName());
				}
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
