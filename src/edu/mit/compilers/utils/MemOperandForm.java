package edu.mit.compilers.utils;

import edu.mit.compilers.IR.expr.operand.IrLocation;

public class MemOperandForm extends OperandForm{
	public String imm = null;
	public String base = null;
	public String loc = null;
	public boolean isString = false;
	
	public MemOperandForm(int imm, String base, String loc, int scale) {
		setImm(imm);
		setBase(base);
		setLoc(loc);
		setScale(scale);
	}
	
	public MemOperandForm(String imm, String base, String loc, int scale) {
		this.imm = imm;
		setBase(base);
		setLoc(loc);
		setScale(scale);
	}
	
	public MemOperandForm(String label, String base) {
		setLabel(label);
		setBase(base);
	}
	
	public MemOperandForm(int imm, String base) {
		setImm(imm);
		setBase(base);
	}
	
	public String getImm() {
		return imm;
	}

	public void setImm(int imm) {
		if(imm == 0)
			this.imm = null;
		this.imm = Integer.toString(imm);
	}
	
	public void setLabel(String label) {
		this.imm = label;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public void setMemReadOnlyString() {
		isString = true;
	}
	
	public boolean isString() {
		return isString;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(imm != null ) {
			if(Util.isInteger(imm) && Integer.parseInt(imm) == 0)
				;
			else
				sb.append(imm);
		}
		if(base != null) {
			sb.append("(");
			sb.append(base);
			if(loc != null) {
				sb.append(",");
				sb.append(loc);
				if(scale != 0) {
					sb.append(",");
					sb.append(scale);
				}
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	
	
}
