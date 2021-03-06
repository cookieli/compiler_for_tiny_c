package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.LowLevelIR.CondQuad;
import edu.mit.compilers.IR.LowLevelIR.IrIfBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForAssign;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForLoopStatement;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.IrWhileBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
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
//TODO: add store location for method
public class IrQuadVistor implements IrNodeVistor{
	public EnvStack env;
	public MethodDecl currentMethod;
	
	public IrBlock currentBlock = null;
	
	public List<IrStatement> currentList= null;
	
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
	
	private void addIrStatement(IrStatement s) {
		if(currentList != null) {
			currentList.add(s);
			return;
		}
		if(currentBlock ==null)
			currentMethod.addIrStatement(s);
		else
			currentBlock.addIrStatement(s);
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
		addIrStatement(new IrQuadForAssign(assign, env.peekVariables(), env.peekMethod()));
		
		return false;
	}

	@Override
	public boolean visit(IrBlock block) {
		// TODO Auto-generated method stub
		List<IrStatement> tempList = currentList;
		currentList = null;
		IrBlock tempBlock = currentBlock;
		currentBlock = block;
		List<IrStatement> stats = currentBlock.statements;
		currentBlock.statements = new ArrayList<>();
		env.pushVariables(currentBlock.localVars);
		for(IrStatement s: stats) {
			s.accept(this);
		}
		
		env.popVariables();
		currentBlock = tempBlock;
		currentList = tempList;
		return false;
	}

	@Override
	public boolean visit(IfBlock ifCode) {
		// TODO Auto-generated method stub
		IrExpression expr = ifCode.getBoolExpr();
		IrBlock trueBlock = ifCode.getTrueBlock();
		IrBlock falseBlock = ifCode.getFalseBlock();
		CondQuad cond = new CondQuad(expr, env.peekVariables(), env.peekMethod(), this);
		List<IrStatement> tempLst = currentList;
		currentList = null;
		trueBlock.accept(this);
		if(falseBlock != null)  falseBlock.accept(this);
		IrIfBlockQuad ifBlock = new IrIfBlockQuad(cond, trueBlock, falseBlock);
		currentList = tempLst;
		addIrStatement(ifBlock);
		return false;
	}

	@Override
	public boolean visit(IrWhileBlock whileBlock) {
		
		IrExpression expr = whileBlock.getBoolExpr();
		IrBlock block = whileBlock.getCodeBlock();
		CondQuad cond = new CondQuad(expr, env.peekVariables(), env.peekMethod(), this);
		block.accept(this);
		//whileBlock.setBoolExpr(cond);
		IrWhileBlockQuad whileQuad = new IrWhileBlockQuad(cond, block);
		currentList = whileQuad.getPreQuad();
		for(IrStatement s: whileBlock.getPreTempStat()) {
			s.accept(this);
		}
		currentList = null;
		addIrStatement(whileQuad);
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public boolean visit(IrForBlock forBlock) {
		// TODO Auto-generated method stub
		IrExpression expr = forBlock.getBoolExpr();
		IrBlock block = forBlock.getBlock();
		CondQuad cond = new CondQuad(expr, env.peekVariables(), env.peekMethod(), this);
		block.accept(this);
		IrWhileBlockQuad forQuad = new IrWhileBlockQuad(cond, block);
		currentList = forQuad.getPreQuad();
		for(IrStatement s: forBlock.getPreTempStat()) {
			s.accept(this);
		}
		currentList = forQuad.getAfterBlockQuad();
		for(IrStatement s: forBlock.getAfterBlockStat()) {
			s.accept(this);
		}
		block.addIrStatement(currentList);
		currentList = null;
		addIrStatement(forQuad);
		return false;
	}

	@Override
	public boolean visit(FuncInvokeStatement func) {
		// TODO Auto-generated method stub
		addIrStatement(new IrQuadForFuncInvoke(func, env.peekVariables(), env.peekMethod()));
		return false;
	}

	@Override
	public boolean visit(Return_Assignment r) {
		// TODO Auto-generated method stub
		addIrStatement(r);
		return false;
	}

	@Override
	public boolean visit(LoopStatement l) {
		// TODO Auto-generated method stub
		//if(!l.isContinue())
		addIrStatement(new IrQuadForLoopStatement(l.getName()));
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
	@Override
	public void visit(IrIfBlockQuad irIfBlockQuad) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(IrWhileBlockQuad whileQuad) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
