package edu.mit.compilers.utils;

import edu.mit.compilers.utils.X86_64Register.Register;

public class RegOperandForm  extends OperandForm {
	public Register reg;
	
	public boolean is_64bit = false;
	
	public RegOperandForm(Register reg) {
		this.reg = reg;
	}
	
	public void set64bit(boolean boo) {
		this.is_64bit = boo;
	}
	
	@Override
	public String toString() {
		if(is_64bit)
			return reg.getName_64bit();
		else
			return reg.getName_8bit();
	}
}
