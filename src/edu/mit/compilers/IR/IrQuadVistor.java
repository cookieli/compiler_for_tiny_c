package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForAssign;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.LoopStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrForBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrWhileBlock;
import edu.mit.compilers.trees.EnvStack;
//TODO: add store location for mehod
public class IrQuadVistor implements IrNodeVistor{
	public EnvStack env;
	public MethodDecl currentMethod;
	public IrQuadVistor() {
		env = new EnvStack();
	}
	public static IrProgram newProgram(IrProgram program) {
	    //IrProgram p = (IrProgram) program.copy();
		IrQuadVistor vistor = new IrQuadVistor();
		program.accept(vistor);
		return program;
	}

	@Override
	public boolean visit(IrProgram p) {
		// TODO Auto-generated method stub
		env.pushMethods(p.globalMethodTable);
		env.pushVariables(p.globalVariableTable);
		for(MethodDecl m: env.peekMethod()) {
			m.accept(this);
		}
		env.popMethod();
		env.popVariables();
		
		return false;
	}

	@Override
	public boolean visit(MethodDecl m) {
		// TODO Auto-generated method stub
		currentMethod = m;
		
		List<IrStatement> stats = currentMethod.statements;
		currentMethod.statements = new ArrayList<>();
		env.pushVariables(m.localVars);
		for(IrStatement s: stats) {
			s.accept(this);
		}
		env.popVariables();
		return false;
	}

	@Override
	public boolean visit(IrAssignment assign) {
		// TODO Auto-generated method stub
		currentMethod.addIrStatement(new IrQuadForAssign(assign, env.peekVariables(), env.peekMethod()));
		return false;
	}

	@Override
	public boolean visit(IrBlock block) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IfBlock ifCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IrWhileBlock whileBlock) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IrForBlock forBlock) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(FuncInvokeStatement func) {
		// TODO Auto-generated method stub
		currentMethod.addIrStatement(new IrQuadForFuncInvoke(func, env.peekVariables(), env.peekMethod()));
		return false;
	}

	@Override
	public boolean visit(Return_Assignment r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(LoopStatement l) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean visit(IrQuad quad) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean visit(IrQuadForAssign quad) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean visit(IrQuadWithLocation quad) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean visit(IrQuadForFuncInvoke quad) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean visit(IrQuadWithLocForFuncInvoke quad) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
