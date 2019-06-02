package edu.mit.compilers.utils;


public class X86_64Register {
	
	public static final Register rax = new Register("%rax", "%al");
	public static final Register r10 = new Register("%r10", "%r10b");
	public static final Register r11 = new Register("%r11", "%r11b");
	
	public static final Register[] tempForAssign = {rax, r10, r11};
	private static int tempForAssignCursor = 0;
	
	public static Register getNxtTempForAssign() {
		if(tempForAssignCursor >= tempForAssign.length)
			throw new IllegalArgumentException("we have use all register for assign");
		Register r = tempForAssign[tempForAssignCursor];
		r.alloc();
		tempForAssignCursor++;
		return r;
	}
	public static String getNxtTempForAssign64bit() {
		return getNxtTempForAssign().getName_64bit();
	}
	
	public static String getNxtTempForAssing8bit() {
		return getNxtTempForAssign().getName_8bit();
	}
	
	public void freeAllRegisterTempForAssign() {
		for(int i = 0; i <= tempForAssignCursor; i++) {
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
		
		public void alloc() {
			isUsed = true;
		}
		
		public boolean isUsed() {
			return isUsed;
		}
		
		public void free() {
			isUsed = true;
		}
		
		public String getName_64bit() {
			return name_64bit;
		}
		
		public String getName_8bit() {
			return name_8bit;
		}
		
	}
	
}
