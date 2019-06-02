package edu.mit.compilers.utils;

public class Register {
	public String name;
	public boolean isUsed = false;
	public Register(String name) {
		this.name = name;
	}
	public boolean isUsed() {
		return isUsed;
	}
	public void alloc() {
		isUsed = true;
	}
	
	public void free() {
		isUsed = false;
	}
	public String getName() {
		return name;
	}
	
	public static final Register rax = new Register("%rax");
	public static final Register r10 = new Register("%r10");
	public static final Register r11 = new Register("%r11");
	
	public static final Register[] tempForAssign = {rax, r10, r11};
}
