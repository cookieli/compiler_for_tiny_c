package edu.mit.compilers.CFG;

import java.util.HashMap;
import java.util.LinkedHashMap;

import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.LowLevelIR.IrIfBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForAssign;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForLoopStatement;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.IrWhileBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.ReturnQuadWithLoc;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.LoopStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrForBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrWhileBlock;

public class cfgNodeVistor implements IrNodeVistor{
	public LinkedHashMap<String, CFG> cfgs;
	public CFG currentCFG;
	public IrWhileBlockQuad currentLoop = null;
	public IrProgram p;
	public cfgNodeVistor() {
		cfgs = new LinkedHashMap<>();
	}
	
	public static LinkedHashMap<String, CFG> cfgForProgram(IrProgram p){
		cfgNodeVistor vistor = new cfgNodeVistor();
		p.accept(vistor);
		return vistor.cfgs;
	}
	
	@Override
	public boolean visit(IrProgram p) {
		// TODO Auto-generated method stub
		this.p = p;
		for(MethodDecl m: p.globalMethodTable) {
			m.accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(MethodDecl m) {
		// TODO Auto-generated method stub
		CFG cfg = new CFG(m.getMethodStackSize(), m.getId());
		cfg.setParaLst(m.getParaOpr());
		currentCFG = cfg;
		currentCFG.setHasReturnValue(m.hasReturnValue());
		for(IrStatement s: m.getStatements()) {
			s.accept(this);
		}
		currentCFG.end();
		currentCFG.compressCFG();
		currentCFG.checkMethodOutOfCtrl(p);
		cfgs.put(m.getId(), cfg);
		return false;
	}

	@Override
	public boolean visit(IrAssignment assign) {
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
		
		currentCFG.addCFGPair(CFG.destruct(quad));
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
		currentCFG.addCFGPair(CFG.destruct(quad));;
		return false;
	}

	@Override
	public void visit(IrIfBlockQuad irIfBlockQuad) {
		// TODO Auto-generated method stub
		currentCFG.addCFGPair(CFG.destruct(irIfBlockQuad));
		
	}
	
	public boolean visit(IrQuadForLoopStatement quad) {
		currentCFG.addCFGPair(CFG.destruct(quad));
		return false;
	}
	
	
	public boolean visit(ReturnQuadWithLoc loc) {
		currentCFG.addCFGPair(CFG.destruct(loc));
		return false;
	}
	
	
	@Override
	public boolean visit(IrBlock block) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void visit(IrWhileBlockQuad whileQuad) {
		// TODO Auto-generated method stub
		currentLoop = whileQuad;
		currentCFG.addCFGPair(CFG.destruct(whileQuad));
	}

}
