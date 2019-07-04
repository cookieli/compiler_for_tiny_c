package edu.mit.compilers.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
	public static final String[] boolBinaryOp = {"&&", "||"};
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
	
	public static void main(String[] args) throws FileNotFoundException {
//		String text = ">=b";
//		Matcher match = ComPattern.matcher(text);
//		if(match.matches()) {
//			System.out.println(match.group(1));
//			System.out.println(match.group(2));
//		}else {
//			System.out.println("not match");
//		}
		
		PrintStream o = new PrintStream(new File("A.txt")); 
		  
        // Store current System.out before assigning a new value 
        PrintStream console = System.out; 
  
        // Assign o to output stream 
        System.setOut(o); 
        System.out.println("This will be written to the text file"); 
  
        // Use stored value for output stream 
        System.setOut(console); 
        System.out.println("This will be written on the console!");
	}
	
}
