package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.mit.compilers.IR.IR_decl_Node.ArrayDecl;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.LowLevelIR.IrIfBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForAssign;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLenExpr;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.LoopStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrForBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrWhileBlock;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.assembly.AssemblyForArith;
import edu.mit.compilers.trees.EnvStack;
import edu.mit.compilers.trees.SemanticCheckerNode;
import edu.mit.compilers.utils.ImmOperandForm;
import edu.mit.compilers.utils.MemOperandForm;
import edu.mit.compilers.utils.OperandForm;
import edu.mit.compilers.utils.Util;
import edu.mit.compilers.utils.X86_64Register;

public class IrResolveNameToLocationVistor implements IrNodeVistor {
	public EnvStack env;
	public MethodDecl currentMethod = null;
	public IrBlock currentBlock = null;
	public IrProgram program;
	public SemanticCheckerNode semantics;

	public static final String[] paraPassReg = { "%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9" };
	public static final String[] paraPassReg_32bit = {"edi", "%esi", "%edx", "%ecx", "%r8d", "%r9d"};
	public static final String[] paraPassReg_16bit = {"%di", "%si", "%dx", "%cx", "%r8w", "%r9w"};
	public static final String[] paraPassReg_8bit = {"%dil", "%sil", "%dl", "%cl", "%r8b", "%r9b"};
	public static final String rip = "%rip";
	public static final String rax = "%rax";
	public IrResolveNameToLocationVistor() {
		env = new EnvStack();
		semantics = new SemanticCheckerNode();
	}

	public static IrProgram newProgram(IrProgram p) {
		IrResolveNameToLocationVistor vistor = new IrResolveNameToLocationVistor();
		p.accept(vistor);
		return p;
	}
	
	public void addIrStatement(IrStatement s) {
		if(currentBlock == null)
			currentMethod.addIrStatement(s);
		else
			currentBlock.addIrStatement(s);
	}

	@Override
	public boolean visit(IrProgram p) {
		// TODO Auto-generated method stub
		program = p;
		env.pushVariables(p.globalVariableTable);
		env.pushMethods(p.globalMethodTable);
		for (MethodDecl m : p.globalMethodTable) {
			m.accept(this);
		}
		env.popVariables();
		env.popMethod();
		return false;
	}

	@Override
	public boolean visit(MethodDecl m) {
		// TODO Auto-generated method stub
		currentMethod = m;
		List<IrStatement> statements = currentMethod.statements;
		currentMethod.statements = new ArrayList<>();
		if(Util.isMainMethod(m)) {
			setArraySizeForAllVar(env.peekVariables(), env.peekMethod(), true);
		}
		env.pushVariables(m.localVars);
		setArraySizeForAllVar(env.peekVariables(), env.peekMethod(), false);
		
		
		
		for (IrStatement s : statements) {
			s.accept(this);
		}
		env.popVariables();
		return false;
	}
	
	private void setArraySizeForAllVar(VariableTable vtb, MethodTable mtb, boolean isGlobl) {
		for(Variable_decl v: vtb) {
			if(v instanceof ArrayDecl)
				addIrStatement(setArraySize((ArrayDecl) v, vtb, mtb, isGlobl));
		}
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
	
	public String setRealLocationForArray(String imm) {
		if(Util.isInteger(imm))
			return Integer.toString(Integer.parseInt(imm) + Util.ArrayHeaderSize);
		else
			return imm + "+" + Util.ArrayHeaderSize;
	}
	
	
	private OperandForm arrayStartMemLoc(ArrayDecl arr, VariableTable vtb, MethodTable mtb, boolean isGlobl) {
		String imm = vtb.getMemLocation(arr.getId());
		String base = X86_64Register.rbp.getName_64bit();
		int step = 8;
		if(isGlobl)  base = X86_64Register.rip.getName_64bit();
		//if(arr.type.equals(IrType.boolArray))    step = 1;
		return new MemOperandForm(imm, base, null, step);
	}
	private IrQuadWithLocation setArraySize(ArrayDecl arr, VariableTable vtb, MethodTable mtb, boolean isGlobl) {
		OperandForm operand = arrayStartMemLoc(arr, vtb, mtb, isGlobl);
		int size = arr.getArraySize();
		return new IrQuadWithLocation("movq", new ImmOperandForm(size, 8), operand);
	}
	

	
	public OperandForm  getOperandForm(IrOperand opr, VariableTable vtb, MethodTable mtb) {
		if(opr == null)
			return null;
		int step = 1;
		String base = X86_64Register.rbp.getName_64bit();
		String imm;
		if(opr instanceof IrLiteral) {
			IrLiteral literal = (IrLiteral) opr;
			if(literal.getType().equals(IrType.IntType)) {
				return new ImmOperandForm(literal.getIntValue().intValue(), 8);
			} else
				return new ImmOperandForm(Integer.parseInt(literal.getValue()), 1);
		} else if(opr instanceof IrLocation) {
			IrLocation loc = (IrLocation) opr;
			
			if(semantics.getIrOperandType(loc, vtb, mtb).equals(IrType.IntType))
				step = 8;
			
			if(vtb.isGloblVariable(loc.getId()))
				base = X86_64Register.rip.getName_64bit();
			String locForMem = null;
			imm = vtb.getMemLocation(loc.getId());
			if(loc.locationIsArray()) {
				imm = setRealLocationForArray(imm);
				IrExpression sizeExpr = loc.getSizeExpr();
				if(sizeExpr instanceof IrLiteral) {
					IrLiteral sizeL = (IrLiteral) sizeExpr;
					if(Util.isInteger(imm))
						imm = Integer.toString(sizeL.getIntValue().intValue()*step+ Integer.parseInt(imm));
					else {
						if(sizeL.getIntValue().intValue() != 0)
						imm = Integer.toString(sizeL.getIntValue().intValue()*step) + "+"+imm;
					}
					return new MemOperandForm(imm,base,null, step);
				} else {
					IrLocation sizeLoc = (IrLocation) sizeExpr;
					locForMem = getMemLocForNotArrayLocation(sizeLoc, vtb, mtb);
					return new MemOperandForm(imm, base, locForMem, step);
				}
			} else
				return new MemOperandForm(imm, base, null, step);
		} else if(opr instanceof IrLenExpr) {
			step = 8;
			IrLenExpr lenExpr = (IrLenExpr) opr;
			imm = vtb.getMemLocation(lenExpr.getOperand().getId());
			if(vtb.isGloblVariable(lenExpr.getOperand().getId()))
				base = X86_64Register.rip.getName_64bit();
			return new MemOperandForm(imm, base, null, step);
		}
		return null;
	}
	
	private String getMemLocForNotArrayLocation(IrLocation loc, VariableTable vtb, MethodTable mtb) {
		String base = X86_64Register.rbp.getName_64bit();
		if(vtb.isGloblVariable(loc.getId()))
			base = X86_64Register.rip.getName_64bit();
		String imm = vtb.getMemLocation(loc.getId());
		return new MemOperandForm(imm, base).toString();
	}

	@Override
	public boolean visit(IrQuadForAssign quad) {
		VariableTable vtb = env.peekVariables();
		MethodTable mtb = env.peekMethod();
		String symbol = quad.getSymbol();
		OperandForm op1 = null;
		OperandForm op2 = null;
		OperandForm dest = null;
		if (quad.getOp1() != null) {
			op1 = getOperandForm(quad.getOp1(), vtb, mtb);
		}
		if (quad.getOp2() != null)
			op2 = getOperandForm(quad.getOp2(), vtb, mtb);
		if (quad.getDest() != null)
			dest = getOperandForm(quad.getDest(), vtb, mtb);
		

		addIrStatement(new IrQuadWithLocation(symbol, op1, op2, dest));
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
		IrFuncInvocation func = (IrFuncInvocation) quad.getFunc();
		IrQuadWithLocForFuncInvoke funcQuad = new IrQuadWithLocForFuncInvoke(func.getId());
		if(func.isPLT())
			funcQuad.setFuncName(func.getId() + "@PLT");
		for(IrExpression e: func.funcArgs) {
			if(e instanceof IrLiteral && ((IrLiteral) e).getType().equals(IrType.stringType)) {
				funcQuad.addParameter(setStringLiteralForFunc((IrLiteral) e));
			} else
				funcQuad.addParameter(getOperandForm((IrOperand) e, env.peekVariables(), env.peekMethod()));
		}
		
		addIrStatement(funcQuad);
		return false;
	}
	
	public MemOperandForm setStringLiteralForFunc(IrLiteral l) {
		program.addReadOnlyData(l.getValue());
		MemOperandForm mem = new MemOperandForm(program.getRoData().getLastLabel(), X86_64Register.rip.name_64bit);
		mem.setMemReadOnlyString();
		mem.setScale(8);
		return mem;
	}
	
	
	

	@Override
	public boolean visit(IrQuadWithLocForFuncInvoke quad) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public IrQuadWithLocation resetQuad(IrQuad quad, VariableTable vtb, MethodTable mtb) {
		IrOperand op1 = quad.getOp1();
		IrOperand op2 = quad.getOp2();
		IrOperand dst = quad.getDest();
		OperandForm opForm1, opForm2, dstForm;
		opForm1 = getOperandForm(op1, vtb, mtb);
		opForm2 = getOperandForm(op2, vtb, mtb);
		dstForm = getOperandForm(dst, vtb, mtb);
		
		return new IrQuadWithLocation(quad.getSymbol(), opForm1, opForm2, dstForm);
	}

	@Override
	public void visit(IrIfBlockQuad irIfBlockQuad) {
		// TODO Auto-generated method stub
		irIfBlockQuad.setCondQuad(resetQuad((IrQuad) irIfBlockQuad.getCondQuad(), env.peekVariables(), env.peekMethod()));
		irIfBlockQuad.getTrueBlock().accept(this);
		if(irIfBlockQuad.getFalseBlock() != null)
			irIfBlockQuad.getFalseBlock().accept(this);
		addIrStatement(irIfBlockQuad);
		
	}
	@Override
	public boolean visit(IrBlock block) {
		// TODO Auto-generated method stub
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
		return false;
	}

}
