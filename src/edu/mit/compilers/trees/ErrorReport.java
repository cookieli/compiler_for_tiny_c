package edu.mit.compilers.trees;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.IR_decl_Node.Import_decl;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.TernaryExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLenExpr;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.statement.LoopStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;


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
	
	public static String ArraySubscriptNotInt(IrLocation i) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(i));
		sb.append("error: " + i.getName()+" subscript isn't int type");
		sb.append("\n");
		return sb.toString();
	}
	public static String methodReturnTypeNotMatch(Return_Assignment ret, IrType retType, MethodDecl m) {
		StringBuilder sb = new StringBuilder();
		if(ret.getExpr() == null)
			sb.append(getErrLocation(ret));
		else 
			sb.append(getErrLocation(ret.getExpr()));
		sb.append("error: ");
		sb.append("the method real type is " + m.getName());
		sb.append("  ");
		sb.append("but the return type is ");
		if(ret.getExpr() == null)
			sb.append("void\n");
		else
			sb.append(retType.toString() + "\n");
		
		return sb.toString();
	}
	
	public static String loopStatementNotInLoop(LoopStatement loop) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(loop));
		sb.append("error: ");
		sb.append(loop.getName() + " not in loop");
		sb.append("\n");
		return sb.toString();
	}
	public static String notHaveSamePara(IrFuncInvocation func, MethodDecl m) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(func));
		sb.append("error: the func invoke "+ func.getName() + " has improper parameter size");
		sb.append(" the correct method decl is ");
		sb.append(m.getName());
		sb.append("\n");
		return sb.toString();
	}
	
	public static String notHaveSamePara(IrFuncInvocation func, MethodDecl m, int i, IrType rightType, IrType wrongType) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(func));
		sb.append("error: ");
		sb.append("the func invoke " + func.getName() + " " +  i+"th"+ " type " + wrongType.toString());
		sb.append(" not equals " + rightType.toString());
		sb.append(" the correct method decl is ");
		sb.append(m.getName());
		sb.append("\n");
		
		return sb.toString();
	}
	
	public static String AssignmentLhsSouldBeErr(IrLocation loc, IrType shouldBe, IrType reallyIs) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(loc));
		sb.append("the assignment should be " + shouldBe.toString());
		sb.append(" ");
		sb.append(" but the type really is  "+ reallyIs.toString());
		sb.append("\n");
		return sb.toString();
	}
	
	
	public static String LenOprNotArray(IrLocation loc) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(loc));
		sb.append("error: ");
		sb.append("len oprand "+ loc.getId() + " isn't array");
		sb.append("\n");
		return sb.toString();
	}
	
	public static String typeNotMatchForExpr(IrExpression expr, IrType type, IrType shouldBe) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(expr));
		sb.append("error: ");
		sb.append(expr.getName() + "'s type " + type.toString());
		sb.append("  can't match expression operand should be type: " + shouldBe.toString());
		sb.append("\n");
		return sb.toString();
	}
	
	public static String typeNotMatchForTernary(TernaryExpression expr, IrType lhsType, IrType rhsType) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(expr));
		sb.append("error: ");
		sb.append(expr.getName() + " " + "first expr type is " + lhsType.toString());
		sb.append(".  it's second expr type is "+ rhsType.toString());
		sb.append(".  they are not match");
		sb.append("\n");
		return sb.toString();
	}
	
	public static String typeNotMatchForArithmetic(IrExpression expr, IrType type) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(expr));
		sb.append("error: ");
		sb.append(expr.getName()+ "'s type "+type.toString());
		sb.append(" can't match arithmetic type int");
		sb.append("\n");
		return sb.toString();
	}
	public static String MainHasParaErr(MethodDecl m) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(m));
		sb.append("error: ");
		sb.append("the main method can't have parameters");
		sb.append("\n");
		return sb.toString();
	}
	public static String NotHaveMainErr(IrProgram p) {
		StringBuilder sb = new StringBuilder();
		sb.append(p.getFilename());
		sb.append(":");
		sb.append("error: ");
		sb.append("the program doesn't have main method\n");
		sb.append("\n");
		return sb.toString();
	}
	
	public static String MainHasWrongType(MethodDecl m) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(m));
		sb.append("error: ");
		sb.append("the main type isn't void: it's type is  ");
		sb.append(m.getType());
		sb.append("\n");
		return sb.toString();
	}
	
	public static String UsedBeforeDecledError(IrExpression ir) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(ir));
		sb.append("error: ");
		if(ir instanceof IrLocation)
			sb.append("the variable ");
		else if(ir instanceof IrFuncInvocation)
			sb.append("the function ");
		sb.append(ir.getName());
		sb.append(" used before declared");
		sb.append("\n");
		return sb.toString();
	}
	
	public static String AssignmentTypeUnmatched(IrType lhs, IrType rhs, IrLocation loc) {
		StringBuilder sb = new StringBuilder();
		sb.append(getErrLocation(loc));
		sb.append("error: ");
		sb.append("the assignment rhs type ");
		sb.append(rhs.toString());
		sb.append(" can't match to");
		sb.append(" lhs ");
		sb.append(lhs.toString());
		sb.append("\n");
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
