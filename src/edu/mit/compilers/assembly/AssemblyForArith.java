package edu.mit.compilers.assembly;

import edu.mit.compilers.IR.Quad.IrQuadWithLocation;

public class AssemblyForArith {
	public static final String[] arithOp = {"add", "sub"};
	public static final String spilter = ",";
	public static final String whiteSpace= " ";
	public static final String rax = "%rax";
	public static final String al = "%al";
	public IrQuadWithLocation quad;
	public StringBuilder code;
	
	public static String getAssemblyForArith(IrQuadWithLocation quad) {
		StringBuilder code = new StringBuilder();
		String mov1 = "movq " + quad.getOp1() + ", "+ "%rax\n";
		String mov2 = "movq " + quad.getOp2() + ", "+ "%r10\n";
		code.append(mov1);
		code.append(mov2);
		String arith;
		String retReg = "%rax";
		if(quad.getSymbol().equals("divq")) {
			code.append("cqto\n");
			code.append("idivq"+ whiteSpace + "%r10\n");
		} else if(quad.getSymbol().equals("modq")){
			code.append("cltd\n");
			code.append("idivq" + whiteSpace + "%r10\n");
			retReg = "%rdx";
		}else {
			arith = quad.getSymbol()+ " "+ "%r10"+", "+ "%rax\n";
			code.append(arith);
		}
		String mov3 = "movq"+" "+retReg + ", "+ quad.getDest()+"\n";
		code.append(mov3);
		return code.toString();
	}
	
	
	public static String getAssemblyForMov(IrQuadWithLocation quad) {
		StringBuilder code = new StringBuilder();
		//code.append(quad.getName());
		String symbol = quad.getSymbol();
		//System.err.println("the error is:\n"+quad.getName());
		String op1 = quad.getOp1();
		String op2 = quad.getOp2();
		
		if(AssemblyForArith.oprIsMem(op1) && AssemblyForArith.oprIsMem(op2)) {
			String reg;
			if(symbol.endsWith("q")) {
				reg = rax;
			} else {
				reg = al;
			}
			IrQuadWithLocation loc1 = new IrQuadWithLocation(symbol, op1, reg);
			IrQuadWithLocation loc2 = new IrQuadWithLocation(symbol, reg, op2);
			code.append(loc1.getName());
			code.append(loc2.getName());
		}
		else 
			code.append(quad.getName());
		return code.toString();
	}
	
	public static boolean oprIsImm(String opr) {
		
		return opr.startsWith("$");
	}
	
	public static boolean oprIsReg(String opr) {
		return opr.startsWith("%");
	}
	
	public static boolean oprIsMem(String opr) {
		if(opr == null)
			throw new IllegalArgumentException("it's null");
		return !oprIsImm(opr) && ! oprIsReg(opr);
	}
	
	public static String getAssembly(IrQuadWithLocation quad) {
		if(quad.getSymbol().contains("mov"))
			return getAssemblyForMov(quad);
		else
			return getAssemblyForArith(quad);
	}
	
	public static String SetRaxZero() {
		return "movq" + whiteSpace + getAssemblyImm(0) + spilter + "%rax"+"\n";
	}
	
	public static String getAssemblyImm(int i) {
		return "$"+i;
	}
	
	public static String getAsmReg(String reg) {
		return "%" + reg;
	}
	
	
	
	
}
