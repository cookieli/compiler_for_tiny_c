package edu.mit.compilers.assembly;

import java.util.regex.Matcher;

import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.utils.ImmOperandForm;
import edu.mit.compilers.utils.MemOperandForm;
import edu.mit.compilers.utils.OperandForm;
import edu.mit.compilers.utils.Util;
import edu.mit.compilers.utils.X86_64Register;

public class AssemblyForArith {
	public static final String[] arithOp = {"add", "sub", "imul", "div", "mod"};
	public static final String spilter = ",";
	public static final String whiteSpace= " ";
	public static final String rax = "%rax";
	public static final String al = "%al";
	public static final int   jmpStart = 1;
	
	
	public static int jmpCursor = jmpStart;
	public static String jmpLabel = ".L";
	public IrQuadWithLocation quad;
	public StringBuilder code;
	
	
	public static String getNxtJmpLabel() {
		String ret = jmpLabel + jmpCursor;
		jmpCursor++;
		return ret;
	}
	
	public static String getCurrJmpLabel() {
		return jmpLabel + (jmpCursor-1);
	}
	
	public static String getAssemblyForArith(IrQuadWithLocation quad) {
		StringBuilder code = new StringBuilder();
		String rax = X86_64Register.rax.getName_64bit();
		String r10 = X86_64Register.r10.getName_64bit();
		String mov1 = new AssemblyForm("movq", quad.getOp1().toString(), rax).toString();
		String mov2 = new AssemblyForm("movq", quad.getOp2().toString(), r10).toString();
		code.append(mov1);
		code.append(mov2);
		String retReg =rax;
		if(quad.getSymbol().equals("divq")) {
			code.append(new AssemblyForm("cqto").toString());//cqto
			code.append(new AssemblyForm("idivq", r10).toString());// idivq
		} else if(quad.getSymbol().equals("modq")){
			code.append(new AssemblyForm("cltd"));
			code.append(new AssemblyForm("idivq", r10).toString());
			retReg = X86_64Register.rdx.getName_64bit();
		}else {
			//arith = quad.getSymbol()+ " "+ "%r10"+", "+ "%rax\n";
			code.append(new AssemblyForm(quad.getSymbol(), r10, rax).toString());
		}
		//String mov3 = "movq"+" "+retReg + ", "+ quad.getDest()+"\n";
		code.append(new AssemblyForm("movq", retReg, quad.getDest().toString()));
		return code.toString();
	}
	
	public static String getAssemblyForCmp(IrQuadWithLocation quad) {
		StringBuilder code = new StringBuilder();
		String symbol = quad.getSymbol();
		String comOp = null;
		String jmpOpr = null;
		Matcher m = Util.ComPattern.matcher(symbol);
		if(m.matches()) {
			symbol = "cmp" + m.group(2);
			comOp = m.group(1);
		} else {
			throw new IllegalArgumentException("not match for compare");
		}
		
		if(quad.getOp1() instanceof MemOperandForm && quad.getOp2() instanceof MemOperandForm) {
			code.append(cmpTwoOprAreMem(symbol, (MemOperandForm)quad.getOp1(), (MemOperandForm)quad.getOp2()));
		}else if(quad.getOp1() instanceof MemOperandForm && quad.getOp2() instanceof ImmOperandForm) {
			code.append(cmpOneOprIsMem(symbol, (MemOperandForm)quad.getOp1(), (ImmOperandForm)quad.getOp2()));
		} else if(quad.getOp1() instanceof ImmOperandForm && quad.getOp2() instanceof MemOperandForm) {
			code.append(cmpOneOprIsMem(symbol, (MemOperandForm)quad.getOp2(), (ImmOperandForm)quad.getOp1()));
			comOp = reverseCmp(comOp);
		}
		
		jmpOpr = setJmpPrepare(comOp);
		code.append(jmpOpr);
		
		return code.toString();
	}
	
	
	private static String setJmpPrepare(String comOp) {
		String jmpOpr = null;
		if(comOp.equals(">"))        jmpOpr = getJmpOpr("jle");
		else if(comOp.equals("<"))   jmpOpr = getJmpOpr("jge");
		else if(comOp.equals(">="))  jmpOpr = getJmpOpr("jl");
		else if(comOp.equals("<="))  jmpOpr = getJmpOpr("jg");
		else if(comOp.equals("!="))  jmpOpr = getJmpOpr("je");
		else if(comOp.equals("=="))  jmpOpr=getJmpOpr("jne");
		return jmpOpr;
	}
	
	public static String setJmpLabel(String label) {
		return label + "\n";
	}
	
	
	private static String reverseCmp(String comOp) {
		String jmpOpr = null;
		if(comOp.equals(">"))        jmpOpr = "<=";
		else if(comOp.equals("<"))   jmpOpr = ">=";
		else if(comOp.equals(">="))  jmpOpr = "<";
		else if(comOp.equals("<="))  jmpOpr = ">";
		else if(comOp.equals("!="))  jmpOpr = "==";
		else if(comOp.equals("=="))  jmpOpr="!=";
		return jmpOpr;
	}
	
	private static String getJmpOpr(String jmp) {
		return jmp + whiteSpace;
	}
	
	private static String cmpTwoOprAreMem(String symbol, MemOperandForm first, MemOperandForm second) {
		StringBuilder sb = new StringBuilder();
		String firstReg = X86_64Register.rax.getName_64bit();
		String secondReg = X86_64Register.r10.getName_64bit();
		String move = "movq";
		if(symbol.endsWith("b")) {
			move = "movb";
			firstReg = X86_64Register.rax.getName_8bit();
			secondReg = X86_64Register.r10.getName_8bit();
		}
		sb.append(new AssemblyForm(move, first.toString(), firstReg).toString());
		sb.append(new AssemblyForm(move, second.toString(), secondReg).toString());
		
		sb.append(new AssemblyForm(symbol, secondReg, firstReg).toString());
		
		return sb.toString();
	}
	
	private static String cmpOneOprIsMem(String symbol, MemOperandForm first, ImmOperandForm second) {
		StringBuilder  sb = new StringBuilder();
		String firstReg = X86_64Register.rax.getName_64bit();
		String  move = "movq";
		if(symbol.endsWith("b")) {
			move = "movb";
			firstReg = X86_64Register.rax.getName_8bit();
		}
		
		sb.append(new AssemblyForm(move, first.toString(), firstReg).toString());
		sb.append(new AssemblyForm(symbol, second.toString(), firstReg).toString());
		return sb.toString();
	}
	
	
	
	
	private static void resembleMemOperandForm(MemOperandForm op ,StringBuilder code) {
		if(!Util.isInteger(op.getImm()) && op.getLoc() !=null) {
			MemOperandForm mem = new MemOperandForm(op.getImm(), op.getBase(), null, op.getScale());
			String reg = X86_64Register.getNxtTempForAssign64bit();
			code.append(new AssemblyForm("leaq", mem.toString(), reg).toString());
			op.setImm(0);
			op.setBase(reg);
		}
	}
	
	
	public static String getAssemblyForMov(IrQuadWithLocation quad) {
		StringBuilder code = new StringBuilder();
		//code.append(quad.getName());
		String symbol = quad.getSymbol();
		//System.err.println("the error is:\n"+quad.getName());
		OperandForm op1 = quad.getOp1();
		OperandForm op2 = quad.getOp2();
		boolean is_64bit = op1.getScale() == 8;
		
		if(is_64bit)   symbol = "movq";
		else          symbol = "movb";
		
		MemOperandForm memOp2 = (MemOperandForm) op2;
		changeMemOperandFormLocToReg(memOp2, code);
		if(op1 instanceof MemOperandForm) {
			MemOperandForm memOp1 = (MemOperandForm) op1;
			
			changeMemOperandFormLocToReg(memOp1, code);
			String reg;
			if(is_64bit) reg = X86_64Register.getNxtTempForAssign64bit();
			else         reg = X86_64Register.getNxtTempForAssing8bit();
			code.append(new AssemblyForm(symbol, memOp1.toString(), reg));
			code.append(new AssemblyForm(symbol, reg, memOp2.toString()));
			
		} else {
			code.append(new AssemblyForm(symbol, op1.toString(), memOp2.toString()));
		}
		
		X86_64Register.freeAllRegisterTempForAssign();
		return code.toString();
	}
	
	private static void changeMemOperandFormLocToReg(MemOperandForm op, StringBuilder code) {
		resembleMemOperandForm(op, code);
		if(op.getLoc() != null) {
			String loc = op.getLoc();
			String reg = X86_64Register.getNxtTempForAssign64bit();
			code.append(new AssemblyForm("movq", loc, reg).toString());
			op.setLoc(reg);
			
		}
	}
	
	public static String getAssemBlyForFuncInvoke(IrQuadWithLocForFuncInvoke func) {
		StringBuilder sb = new StringBuilder();
		int paraNum = func.getParaNum();
		int moreParaNum = paraNum - X86_64Register.paraPassReg.length;
		int allocNum = moreParaNum % 2;
		if(moreParaNum > 0 && allocNum > 0)
			sb.append(new AssemblyForm("subq", allocNum*8, X86_64Register.rsp.getName_64bit()).toString());
		for(int i = paraNum -1; i >=0; i--) {
			String paraReg = X86_64Register.paraPassReg[0].getName_64bit();
			OperandForm oprand = func.getParameter(i);
			if(i < X86_64Register.paraPassReg.length) {
				paraReg = X86_64Register.paraPassReg[i].getName_64bit();
			}
			if(oprand instanceof ImmOperandForm)  sb.append(new AssemblyForm("movq", oprand.toString(), paraReg).toString());
			if(oprand instanceof MemOperandForm) {
				MemOperandForm memOp = (MemOperandForm) oprand;
				if(memOp.isString())            sb.append(new AssemblyForm("leaq", memOp.toString(), paraReg).toString());
				else if(memOp.getScale() == 1)  sb.append(new AssemblyForm("movsbq", memOp.toString(), paraReg).toString());
				else                            sb.append(new AssemblyForm("movq", memOp.toString(), paraReg).toString());
			}
			if(i >= X86_64Register.paraPassReg.length) {
				sb.append(new AssemblyForm("pushq", paraReg).toString());
			}
		}
		sb.append(SetRaxZero());
		sb.append(new AssemblyForm("call", func.getFuncName()));
		if(moreParaNum > 0)
			sb.append(new AssemblyForm("addq", (moreParaNum + allocNum)*8, X86_64Register.rsp.getName_64bit()).toString());
		return sb.toString();
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
		else {
			Matcher m = Util.ComPattern.matcher(quad.getSymbol());
			if(m.matches()) {
				return getAssemblyForCmp(quad);
			}
			
			return getAssemblyForArith(quad);
		}
	}
	
	public static String SetRaxZero() {
		return new AssemblyForm("movq", 0, X86_64Register.rax.getName_64bit()).toString();
	}
	
	public static String getAssemblyImm(int i) {
		return "$"+i;
	}
	
	public static String getAsmReg(String reg) {
		return "%" + reg;
	}
	
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		String a = null;
		sb.append(a);
		System.out.println(sb.toString());
		
	}
	
	
	
	
}
