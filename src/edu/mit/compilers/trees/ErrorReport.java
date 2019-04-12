package edu.mit.compilers.trees;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.IR_decl_Node.Import_decl;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.expr.operand.IrLocation;


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
	
	public static String UsedBeforeDecledError(IrLocation ir) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(ir));
		sb.append("error: ");
		sb.append("the variable ");
		sb.append(ir.getName());
		sb.append(" used before declared");
		sb.append("\n");
		return sb.toString();
	}
	
	public static String AssignmentTypeUnmatched(IrType lhs, IrType rhs, IrLocation loc) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(loc));
		sb.append("error: ");
		sb.append("the assignment rhs type");
		sb.append(rhs.toString());
		sb.append("can't match to");
		sb.append("lhs ");
		sb.append(lhs.toString());
		return sb.toString();
	}
	public static String redeclariationError(IrNode v, IrNode prev) {
		String err1, err2;
		if(v instanceof MethodDecl) {
			err1 = "redeclariation of method: ";
		} else if(v instanceof Variable_decl) {
			err1 = "redeclariation of variable: ";
		} else if(v instanceof Import_decl) {
			err1 = "redeclariation of import: ";
		
		} else {
			throw new IllegalArgumentException("this error can't be defined as redeclariation error");
		}
		if(prev instanceof MethodDecl) {
			err2 = "previous declariation of method: ";
		} else if(prev instanceof Variable_decl) {
			err2 = "previous declariation of variable: ";
		} else if(prev instanceof Import_decl) {
			err2 = "previous declariation of import: ";
		
		} else {
			throw new IllegalArgumentException("this error can't be defined as redeclariation error");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(v));
		sb.append("error: ");
		sb.append(err1);
		sb.append(v.getName());
		sb.append("\n");
		sb.append("note: ");
		sb.append(err2);
		sb.append(getErrLocation(prev));
		sb.append(prev.getName());
		sb.append("\n");
		return sb.toString();
	}
}
