package edu.mit.compilers.utils;

public class X86_64Register {

	public static final Register rax = new Register("%rax", "%al");
	public static final Register r10 = new Register("%r10", "%r10b");
	public static final Register r11 = new Register("%r11", "%r11b");
	public static final Register rbp = new Register("%rbp"), rsp = new Register("%rsp");

	public static final Register rip = new Register("%rip");

	public static final Register rdx = new Register("%rdx");

	public static final Register rdi = new Register("%rdi"), rsi = new Register("%rsi"), rcx = new Register("%rcx"),
			r8 = new Register("%r8"), r9 = new Register("%r9"), r12 = new Register("%r12", "%r12b");

	public static final Register[] tempForAssign = { rax, r10, r11 }, paraPassReg = { rdi, rsi, rdx, rcx, r8, r9 };
	private static int tempForAssignCursor = 0;

	public static Register getNxtTempForAssign() {
		if (tempForAssignCursor >= tempForAssign.length)
			throw new IllegalArgumentException("we have use all register for assign");
		Register r = tempForAssign[tempForAssignCursor];
		while (r.isUsed()) {
			tempForAssignCursor++;
			if (tempForAssignCursor >= tempForAssign.length)
				throw new IllegalArgumentException("we have use all register for assign");
			r = tempForAssign[tempForAssignCursor];
		}
		r.alloc();

		return r;
	}

	public static String getNxtTempForAssign64bit() {
		return getNxtTempForAssign().getName_64bit();
	}

	public static String getNxtTempForAssing8bit() {
		return getNxtTempForAssign().getName_8bit();
	}

	public static void freeAllRegisterTempForAssign() {
		for (int i = 0; i < tempForAssign.length; i++) {
			tempForAssign[i].free();
		}
		tempForAssignCursor = 0;
	}

	public static class Register {
		public String name_64bit;
		public String name_8bit;

		public boolean isUsed = false;

		public Register(String name_64bit, String name_8bit) {
			this.name_64bit = name_64bit;
			this.name_8bit = name_8bit;
		}

		public Register(String name_64bit) {
			this(name_64bit, null);
		}

		public void alloc() {
			isUsed = true;
		}

		public boolean isUsed() {
			return isUsed;
		}

		public void free() {
			isUsed = false;
		}

		public String getName_64bit() {
			return name_64bit;
		}

		public String getName_8bit() {
			return name_8bit;
		}

	}

}
