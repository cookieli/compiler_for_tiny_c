package edu.mit.compilers.assembly;

public class AssemblyForm {
	public String opr;
	public String op1;
	public String op2;
	
	public static final String whitSpace = " ";
	public static final String comma = ",";
	
	public AssemblyForm(String opr, String op1, String op2) {
		this.opr = opr;
		this.op1 = op1;
		this.op2 = op2;
	}
	
	public AssemblyForm(String opr, int op1, String op2) {
		this.opr = opr;
		this.op1 = "$"+op1;
		this.op2 = op2;
	}
	
	public AssemblyForm(String opr) {
		this(opr, null, null);
	}
	
	public AssemblyForm(String opr, String op1) {
		this(opr, op1, null);
	}
	
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(opr);
		if(op1 != null)
			sb.append(whitSpace + op1);
		if(op2 != null)
			sb.append(comma + op2);
		sb.append("\n");
		return sb.toString();
	}
}
