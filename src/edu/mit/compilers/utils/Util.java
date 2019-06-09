package edu.mit.compilers.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;

public class Util {
	public static final String stackBaseReg = "%rbp";
	public static final String rax = "%rax";
	public static final String[] comOp = {"<", ">", ">=", "<=", "!=", "=="};
	public static final int ArrayHeaderSize = 8;
	public static final Pattern ComPattern = Pattern.compile("(\\>=|\\<=|\\>|\\<|\\!=|\\==)(q|b)");
	
	public static boolean isInteger(String s) {
		return s.matches("-?(0|[1-9]\\d*)");
	}
	
	public static boolean isMainMethod(MethodDecl method) {
		return method.getId().equals("main");
	}
	
	
	private static boolean isNaiveBoolean(IrExpression expr) {
		boolean ret = isLiteralOrIrLocation(expr);
		if(expr instanceof BinaryExpression) {
			BinaryExpression binary = (BinaryExpression) expr;
			if(Arrays.asList(comOp).contains(binary.getSymbol())) {
				if(isLiteralOrIrLocation(binary.getlhs()) && isLiteralOrIrLocation(binary.getrhs()))
					return true;
			}
		}
		return ret;
	}
	
	private static boolean isLiteralOrIrLocation(IrExpression expr) {
		return expr instanceof IrLocation || expr instanceof IrLiteral;
	}
	
	public static void main(String[] args) {
		String text = ">=q";
		Matcher match = ComPattern.matcher(text);
		if(match.matches()) {
			System.out.println(match.group(1));
			System.out.println(match.group(2));
		}else {
			System.out.println("not match");
		}
	}
	
}
