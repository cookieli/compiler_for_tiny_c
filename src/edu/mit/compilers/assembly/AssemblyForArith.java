package edu.mit.compilers.assembly;

import java.util.regex.Matcher;

import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.ReturnQuadWithLoc;
import edu.mit.compilers.utils.ImmOperandForm;
import edu.mit.compilers.utils.MemOperandForm;
import edu.mit.compilers.utils.OperandForm;
import edu.mit.compilers.utils.RegOperandForm;
import edu.mit.compilers.utils.Util;
import edu.mit.compilers.utils.X86_64Register;
import edu.mit.compilers.utils.X86_64Register.Register;

public class AssemblyForArith {
	public static final String[] arithOp = { "add", "sub", "imul", "div", "mod" };
	public static final String spilter = ",";
	public static final String whiteSpace = " ";
	public static final String rax = "%rax";
	public static final String al = "%al";
	public static final int jmpStart = 1;

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
		return jmpLabel + (jmpCursor - 1);
	}

	public static String getAssemblyForArithReg(IrQuadWithLocation quad) {
		StringBuilder code = new StringBuilder();
		OperandForm op1 = quad.getOp1();
		OperandForm op2 = quad.getOp2();
		OperandForm dst = quad.getDest();
		if (!(op1 instanceof RegOperandForm) && !(op2 instanceof RegOperandForm) && !(dst instanceof RegOperandForm)) {
			return null;
		}
		if (op1.equals(dst)) {
			code.append(new AssemblyForm(quad.getSymbol(), op2.toString(), op1.toString()).toString());
		} else {
			if (quad.getSymbol().equals("addq") || quad.getSymbol().equals("imulq")) {
				if (op2.equals(dst)) {
					code.append(new AssemblyForm(quad.getSymbol(), op1.toString(), op2.toString()).toString());
				}
			} else {
				String rax = X86_64Register.rax.getName_64bit();
				String mov1 = new AssemblyForm("movq", op1.toString(), rax).toString();
				String mov2 = new AssemblyForm("movq", rax, quad.getDest().toString()).toString();
				String arith = new AssemblyForm(quad.getSymbol(), op2.toString(), rax).toString();
				code.append(mov1);
				code.append(arith);
				code.append(mov2);
			}
		}
		return code.toString();
	}

	public static String getAssemblyForArith(IrQuadWithLocation quad) {
		String codeStr = getAssemblyForArithReg(quad);
		if (codeStr != null)
			return codeStr;
		StringBuilder code = new StringBuilder();
		String rax = X86_64Register.rax.getName_64bit();
		String r10 = X86_64Register.r10.getName_64bit();
		String mov1 = new AssemblyForm("movq", quad.getOp1().toString(), rax).toString();
		String mov2 = new AssemblyForm("movq", quad.getOp2().toString(), r10).toString();
		code.append(mov1);
		code.append(mov2);
		String retReg = rax;
		if (quad.getSymbol().equals("divq")) {
			code.append(new AssemblyForm("cqto").toString());// cqto
			code.append(new AssemblyForm("idivq", r10).toString());// idivq
		} else if (quad.getSymbol().equals("modq")) {
			code.append(new AssemblyForm("cltd"));
			code.append(new AssemblyForm("idivq", r10).toString());
			retReg = X86_64Register.rdx.getName_64bit();
		} else {
			// arith = quad.getSymbol()+ " "+ "%r10"+", "+ "%rax\n";
			code.append(new AssemblyForm(quad.getSymbol(), r10, rax).toString());
		}
		// String mov3 = "movq"+" "+retReg + ", "+ quad.getDest()+"\n";
		code.append(new AssemblyForm("movq", retReg, quad.getDest().toString()));
		return code.toString();
	}

	public static String getAssemblyForCmp(IrQuadWithLocation quad) {
		StringBuilder code = new StringBuilder();
		String symbol = quad.getSymbol();
		String comOp = null;
		String jmpOpr = null;
		Matcher m = Util.ComPattern.matcher(symbol);
		if (m.matches()) {
			symbol = "cmp" + m.group(2);
			comOp = m.group(1);
		} else {
			throw new IllegalArgumentException("not match for compare");
		}

		if (quad.getOp1() instanceof MemOperandForm && quad.getOp2() instanceof MemOperandForm) {
			code.append(cmpTwoOprAreMem(symbol, (MemOperandForm) quad.getOp1(), (MemOperandForm) quad.getOp2()));
		} else if (quad.getOp1() instanceof MemOperandForm && quad.getOp2() instanceof ImmOperandForm) {
			code.append(cmpOneOprIsMem(symbol, (MemOperandForm) quad.getOp1(), (ImmOperandForm) quad.getOp2()));
		} else if (quad.getOp1() instanceof ImmOperandForm && quad.getOp2() instanceof MemOperandForm) {
			code.append(cmpOneOprIsMem(symbol, (MemOperandForm) quad.getOp2(), (ImmOperandForm) quad.getOp1()));
			comOp = reverseCmp(comOp);
		} else if (quad.getOp1() instanceof ImmOperandForm && quad.getOp2() instanceof ImmOperandForm) {
			code.append(cmpTwoOprAreImm(symbol, (ImmOperandForm) quad.getOp1(), (ImmOperandForm) quad.getOp2()));
		} else if (quad.getOp1() instanceof RegOperandForm && quad.getOp2() instanceof ImmOperandForm) {
			code.append(cmpOneOprIsMem(symbol, (RegOperandForm) quad.getOp1(), (ImmOperandForm) quad.getOp2()));
		} else if (quad.getOp1() instanceof ImmOperandForm && quad.getOp2() instanceof RegOperandForm) {
			code.append(cmpOneOprIsMem(symbol, (RegOperandForm) quad.getOp2(), (ImmOperandForm) quad.getOp1()));
			comOp = reverseCmp(comOp);
		} else {
			code.append(new AssemblyForm(symbol, quad.getOp1().toString(), quad.getOp2().toString()).toString());
		}

		jmpOpr = setJmpPrepare(comOp);
		code.append(jmpOpr);

		return code.toString();
	}

	private static String setJmpPrepare(String comOp) {
		String jmpOpr = null;
		if (comOp.equals(">"))
			jmpOpr = getJmpOpr("jle");
		else if (comOp.equals("<"))
			jmpOpr = getJmpOpr("jge");
		else if (comOp.equals(">="))
			jmpOpr = getJmpOpr("jl");
		else if (comOp.equals("<="))
			jmpOpr = getJmpOpr("jg");
		else if (comOp.equals("!="))
			jmpOpr = getJmpOpr("je");
		else if (comOp.equals("=="))
			jmpOpr = getJmpOpr("jne");
		return jmpOpr;
	}

	public static String setJmpLabel(String label) {
		// System.out.println("the label is " + label + "\n");
		return label + "\n";
	}

	private static String reverseCmp(String comOp) {
		String jmpOpr = null;
		if (comOp.equals(">"))
			jmpOpr = "<=";
		else if (comOp.equals("<"))
			jmpOpr = ">=";
		else if (comOp.equals(">="))
			jmpOpr = "<";
		else if (comOp.equals("<="))
			jmpOpr = ">";
		else if (comOp.equals("!="))
			jmpOpr = "==";
		else if (comOp.equals("=="))
			jmpOpr = "!=";
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
		if (symbol.endsWith("b")) {
			move = "movb";
			firstReg = X86_64Register.rax.getName_8bit();
			secondReg = X86_64Register.r10.getName_8bit();
		}
		sb.append(new AssemblyForm(move, first.toString(), firstReg).toString());
		sb.append(new AssemblyForm(move, second.toString(), secondReg).toString());

		sb.append(new AssemblyForm(symbol, secondReg, firstReg).toString());

		return sb.toString();
	}

	private static String cmpTwoOprAreImm(String symbol, ImmOperandForm first, ImmOperandForm second) {
		StringBuilder sb = new StringBuilder();
		String firstReg = X86_64Register.rax.getName_64bit();
		String secondReg = X86_64Register.r10.getName_64bit();
		String move = "movq";
		if (symbol.endsWith("b")) {
			move = "movb";
			firstReg = X86_64Register.rax.getName_8bit();
			secondReg = X86_64Register.r10.getName_8bit();
		}
		sb.append(new AssemblyForm(move, first.toString(), firstReg).toString());
		sb.append(new AssemblyForm(move, second.toString(), secondReg).toString());

		sb.append(new AssemblyForm(symbol, secondReg, firstReg).toString());
		return sb.toString();
	}

	private static String cmpOneOprIsMem(String symbol, OperandForm first, ImmOperandForm second) {
		StringBuilder sb = new StringBuilder();
		String firstReg;
		if (first instanceof MemOperandForm) {
			firstReg = X86_64Register.rax.getName_64bit();
			String move = "movq";
			if (symbol.endsWith("b")) {
				move = "movb";
				firstReg = X86_64Register.rax.getName_8bit();
			}

			sb.append(new AssemblyForm(move, first.toString(), firstReg).toString());
		} else {
			firstReg = first.toString();
		}
		sb.append(new AssemblyForm(symbol, second.toString(), firstReg).toString());
		return sb.toString();
	}

	private static void resembleMemOperandForm(MemOperandForm op, StringBuilder code) {
		if (!Util.isInteger(op.getImm()) && op.getLoc() != null) {
			MemOperandForm mem = new MemOperandForm(op.getImm(), op.getBase(), null, op.getScale());
			String reg = X86_64Register.getNxtTempForAssign64bit();
			code.append(new AssemblyForm("leaq", mem.toString(), reg).toString());
			op.setImm(0);
			op.setBase(reg);
		}
	}

	public static String getAssemblyForMov(IrQuadWithLocation quad) {
		StringBuilder code = new StringBuilder();
		// code.append(quad.getName());
		String symbol = quad.getSymbol();
		// System.err.println("the error is:\n"+quad.getName());
		OperandForm op1 = quad.getOp1();
		OperandForm op2 = quad.getOp2();
		boolean is_64bit = op2.is64bit();

		OperandForm memOp2;
		if (op2 instanceof RegOperandForm)
			memOp2 = (RegOperandForm) op2;
		else {
			memOp2 = (MemOperandForm) op2;
			changeMemOperandFormLocToReg((MemOperandForm) memOp2, code);
		}
		if (op1 instanceof MemOperandForm) {
			MemOperandForm memOp1 = (MemOperandForm) op1;

			changeMemOperandFormLocToReg(memOp1, code);
			String reg;
			if (is_64bit)
				reg = X86_64Register.getNxtTempForAssign64bit();
			else
				reg = X86_64Register.getNxtTempForAssing8bit();
			code.append(new AssemblyForm(symbol, memOp1.toString(), reg));
			code.append(new AssemblyForm(symbol, reg, memOp2.toString()));

		} else {
			if (op1 == null)
				throw new IllegalArgumentException("op1 is null" + quad.getName());
			if (memOp2 == null)
				throw new IllegalArgumentException("op2 is null" + quad.getName());
			code.append(new AssemblyForm(symbol, op1.toString(), memOp2.toString()));
		}

		X86_64Register.freeAllRegisterTempForAssign();
		return code.toString();
	}

	public static String setParaAssemblyMov(Register reg, MemOperandForm op2) {
		String symbol = "movb";
		boolean is64bit = op2.is64bit();
		if (is64bit)
			symbol = "movq";
		RegOperandForm op1 = new RegOperandForm(reg);
		op1.set64bit(is64bit);
		return new AssemblyForm(symbol, op1.toString(), op2.toString()).toString();
	}

	public static String setAssemblyMov(MemOperandForm paraOnStack, MemOperandForm op2) {
		StringBuilder sb = new StringBuilder();
		String symbol = "movq";
		RegOperandForm reg = new RegOperandForm(X86_64Register.rax);
		reg.set64bit(true);
		sb.append(new AssemblyForm(symbol, paraOnStack.toString(), reg.toString()).toString());
		// sb.append("\n");
		if (!op2.is64bit()) {
			reg.set64bit(false);
			symbol = "movb";
		}
		sb.append(new AssemblyForm(symbol, reg.toString(), op2.toString()).toString());
		// sb.append("\n");
		return sb.toString();
	}

	private static void changeMemOperandFormLocToReg(MemOperandForm op, StringBuilder code) {
		resembleMemOperandForm(op, code);
		if (op.getLoc() != null) {
			String loc = op.getLoc();
			String reg = X86_64Register.getNxtTempForAssign64bit();
			code.append(new AssemblyForm("movq", loc, reg).toString());
			op.setLoc(reg);

		}
	}

	public static String getAssemblyForReturn(ReturnQuadWithLoc quad) {
		X86_64Register.freeAllRegisterTempForAssign();
		if (quad.getRetLoc() == null)
			return "\n";
		RegOperandForm op1 = new RegOperandForm(X86_64Register.getNxtTempForAssign());
		op1.set64bit(quad.isIs64bit());
		OperandForm op2 = quad.getRetLoc();
		String symbol = "movb";
		if (quad.isIs64bit())
			symbol = "movq";
		IrQuadWithLocation opr = new IrQuadWithLocation(symbol, op2, op1);
		return getAssemblyForMov(opr);
	}

	public static String getAssemBlyForFuncInvoke(IrQuadWithLocForFuncInvoke func) {
		StringBuilder sb = new StringBuilder();
		int paraNum = func.getParaNum();
		int moreParaNum = paraNum - X86_64Register.paraPassReg.length;
		int allocNum = moreParaNum % 2;
		if (moreParaNum > 0 && allocNum > 0)
			sb.append(new AssemblyForm("subq", allocNum * 8, X86_64Register.rsp.getName_64bit()).toString());
		for (int i = paraNum - 1; i >= 0; i--) {
			String paraReg = X86_64Register.paraPassReg[0].getName_64bit();
			OperandForm oprand = func.getParameter(i);
			if (i < X86_64Register.paraPassReg.length) {
				paraReg = X86_64Register.paraPassReg[i].getName_64bit();
			}
			if (oprand instanceof ImmOperandForm)
				sb.append(new AssemblyForm("movq", oprand.toString(), paraReg).toString());
			if (oprand instanceof MemOperandForm) {
				MemOperandForm memOp = (MemOperandForm) oprand;
				if (memOp.isString()) {
					// throw new IllegalArgumentException(memOp.toString());
					sb.append(new AssemblyForm("leaq", memOp.toString(), paraReg).toString());
				} else if (memOp.getScale() == 1)
					sb.append(new AssemblyForm("movsbq", memOp.toString(), paraReg).toString());
				else
					sb.append(new AssemblyForm("movq", memOp.toString(), paraReg).toString());
			}
			if (i >= X86_64Register.paraPassReg.length) {
				sb.append(new AssemblyForm("pushq", paraReg).toString());
			}
		}
		sb.append(SetRaxZero());
		sb.append(new AssemblyForm("call", func.getFuncName()));

		sb.append(afterFuncInvokeOpr(func));
		if (moreParaNum > 0)
			sb.append(new AssemblyForm("addq", (moreParaNum + allocNum) * 8, X86_64Register.rsp.getName_64bit())
					.toString());
		return sb.toString();
	}

	private static String afterFuncInvokeOpr(IrQuadWithLocForFuncInvoke func) {
		X86_64Register.freeAllRegisterTempForAssign();
		StringBuilder sb = new StringBuilder();
		if (func.getHasRetValue()) {
			RegOperandForm op1 = new RegOperandForm(X86_64Register.getNxtTempForAssign());
			String symbol = "movb";
			X86_64Register.rax.alloc();
			OperandForm op2 = func.getRetLoc();
			if (func.isIs64bit()) {
				op1.set64bit(true);
				symbol = "movq";
			}
			IrQuadWithLocation opr = new IrQuadWithLocation(symbol, op1, op2);
			sb.append(getAssemblyForMov(opr));

		} else
			sb.append(SetRaxZero());
		return sb.toString();
	}

	public static boolean oprIsImm(String opr) {

		return opr.startsWith("$");
	}

	public static boolean oprIsReg(String opr) {
		return opr.startsWith("%");
	}

	public static boolean oprIsMem(String opr) {
		if (opr == null)
			throw new IllegalArgumentException("it's null");
		return !oprIsImm(opr) && !oprIsReg(opr);
	}

	public static String getAssembly(IrQuadWithLocation quad) {
		if (quad.getSymbol().contains("mov"))
			return getAssemblyForMov(quad);
		else {
			Matcher m = Util.ComPattern.matcher(quad.getSymbol());
			if (m.matches()) {
				return getAssemblyForCmp(quad);
			}

			return getAssemblyForArith(quad);
		}
	}

	public static String SetRaxZero() {
		return new AssemblyForm("movq", 0, X86_64Register.rax.getName_64bit()).toString();
	}

	public static String getAssemblyImm(int i) {
		return "$" + i;
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
