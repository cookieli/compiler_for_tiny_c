package edu.mit.compilers.utils;

public class OperandForm {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + scale;
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
		OperandForm other = (OperandForm) obj;
		if (scale != other.scale)
			return false;
		return true;
	}
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
