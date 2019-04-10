package edu.mit.compilers.trees;

import edu.mit.compilers.IR.IrNode;


public class ErrorReport {
	public static String getErrLocation(IrNode v) {
		StringBuilder sb = new StringBuilder();
		sb.append(v.getFilename());
		sb.append(":");
		sb.append(v.getLineNumber());
		sb.append(":");
		sb.append(v.getColumnNumber());
		sb.append(":");
		return sb.toString();
	}
	public static String redeclariationError(IrNode v, IrNode prev) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(v));
		sb.append("error: ");
		sb.append("redeclariation of variable: ");
		sb.append(v.getName());
		sb.append("\n");
		sb.append("note: ");
		sb.append("previous declariation of variable: ");
		sb.append(getErrLocation(prev));
		sb.append(prev.getName());
		sb.append("\n");
		return sb.toString();
	}
}
