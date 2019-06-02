package edu.mit.compilers.utils;

import edu.mit.compilers.IR.expr.operand.IrLocation;

public class OperandForm {
	public int imm = Integer.MAX_VALUE;
	public IrLocation base = null;
	public IrLocation loc = null;
	public int scale = 0;
	
	public OperandForm(int imm, IrLocation base, IrLocation loc, int scale) {
		setImm(imm);
		setBase(base);
		setLoc(loc);
		setScale(scale);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(imm != Integer.MAX_VALUE) {
			sb.append(imm);
		}
		if(base != null) {
			sb.append("(");
			sb.append(base.getId());
			if(loc != null) {
				sb.append(",");
				sb.append(loc.getId());
				if(scale != 0) {
					sb.append(",");
					sb.append(scale);
				}
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	public int getImm() {
		return imm;
	}
	public void setImm(int imm) {
		this.imm = imm;
	}
	public IrLocation getBase() {
		return base;
	}
	public void setBase(IrLocation base) {
		this.base = base;
	}
	public IrLocation getLoc() {
		return loc;
	}
	public void setLoc(IrLocation loc) {
		this.loc = loc;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	
}
