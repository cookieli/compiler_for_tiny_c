package edu.mit.compilers.CFG.Optimizitation;

public class VirtualValue {
	public static int count = 0;
	
	public int num;
	
	private VirtualValue(int num) {
		this.num = num;
	}
	
	public static VirtualValue generate() {
		return new VirtualValue(count++);
	}
	
	
	@Override
	public boolean equals(Object v) {
		if(v == null)
			return false;
		if(this == v)
			return true;
		if(!(v instanceof VirtualValue))
			return false;
		VirtualValue vv = (VirtualValue) v;
		return this.num == vv.num;
	}
	
	@Override
	public int hashCode() {
		return num;
	}
	
	@Override
	public String toString() {
		return "val"+ num;
	}
}
