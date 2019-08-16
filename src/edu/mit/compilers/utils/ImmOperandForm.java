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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((imm == null) ? 0 : imm.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmOperandForm other = (ImmOperandForm) obj;
		if (imm == null) {
			if (other.imm != null)
				return false;
		} else if (!imm.equals(other.imm))
			return false;
		return true;
	}
}
