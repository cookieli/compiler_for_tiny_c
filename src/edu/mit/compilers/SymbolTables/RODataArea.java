package edu.mit.compilers.SymbolTables;

import java.util.LinkedList;
import java.util.List;

public class RODataArea {
	public List<RoData> area;
	public static int count = 0;
	public RODataArea() {
		area = new LinkedList<>();
	}
	
	public void addData(String value) {
		area.add(new RoData(value));
	}
	public class RoData {
		public static final String lc = ".LC";
		public String label;
		public String value;
		
		public RoData(String value) {
			this.value = value;
			this.label = lc + count;
			count++;
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.label);
			sb.append(":\n");
			sb.append(".string ");
			sb.append(this.value );
			sb.append("\n");
			return sb.toString();
		}
		
	}
	
	public String getLastLabel() {
		return area.get(count -1).label;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(".section" + " "+ ".rodata");
		sb.append("\n");
		for(RoData r: area) {
			sb.append(r.toString());
		}
		return sb.toString();
	}
	
	
	
}
