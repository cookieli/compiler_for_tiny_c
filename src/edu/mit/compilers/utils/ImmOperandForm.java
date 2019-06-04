package edu.mit.compilers.utils;

public class ImmOperandForm extends OperandForm{
	public Integer  imm= null;
	public ImmOperandForm(int imm, int step) {
		this.imm = imm;
		this.scale = step;
	}
	
	public int getImm() {
		return imm;
	}
	
	@Override
	public String toString() {
		return "$"+imm;
	}
}
