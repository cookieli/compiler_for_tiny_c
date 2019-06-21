package edu.mit.compilers.IR;

import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.LowLevelIR.IrIfBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForAssign;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.IrWhileBlockQuad;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.LoopStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrForBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrWhileBlock;

public interface IrNodeVistor {
	public boolean visit(IrProgram p);
	
	public boolean visit(MethodDecl m);
	
	public boolean visit(IrAssignment assign);
	
	public boolean visit(IrBlock block);
	
	public boolean visit(IfBlock ifCode);
	
	public boolean visit(IrWhileBlock whileBlock);
	
	public boolean visit(IrForBlock forBlock);
	
	public boolean visit(FuncInvokeStatement func);
	
	public boolean visit(Return_Assignment r);
	
	public boolean visit(LoopStatement l);
	
	public boolean visit(IrQuad quad);
	
	public boolean visit(IrQuadForAssign quad);
	
	public boolean visit(IrQuadWithLocation quad);
	
	public boolean visit(IrQuadForFuncInvoke quad);
	
	
	
	public boolean visit(IrQuadWithLocForFuncInvoke quad);

	public void visit(IrIfBlockQuad irIfBlockQuad);
	
	public void visit(IrWhileBlockQuad whileQuad);
	
	
	
	
}
