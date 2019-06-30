package edu.mit.compilers.utils;

public class OperandForm {
	public int scale;
	public int getScale() {
		return scale;
	}
	public boolean is64bit() {
		return getScale() == 8;
	}
	
	public void setScale(int scale) {
		this.scale = scale;
	}
	public OperandForm() {
		
	}
}
