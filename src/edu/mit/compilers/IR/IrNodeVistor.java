package edu.mit.compilers.IR;

import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.statement.IrAssignment;

public interface IrNodeVistor {
	public boolean visit(IrProgram p);
	
	public boolean visit(MethodDecl m);
	
	public boolean visit(IrAssignment assign);
}
