package edu.mit.compilers.utils;

import edu.mit.compilers.utils.X86_64Register.Register;

public class RegOperandForm  extends OperandForm {
	public Register reg;
	
	public boolean is_64bit = false;
	
	@Override
	public boolean is64bit() {
		return is_64bit;
	}
	
	public RegOperandForm(Register reg) {
		this.reg = reg;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reg == null) ? 0 : reg.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegOperandForm other = (RegOperandForm) obj;
		if (reg == null) {
			if (other.reg != null)
				return false;
		} else if (!reg.equals(other.reg))
			return false;
		return true;
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
