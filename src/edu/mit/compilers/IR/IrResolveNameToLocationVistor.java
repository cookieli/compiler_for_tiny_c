package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.mit.compilers.IR.IR_decl_Node.ArrayDecl;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
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
import edu.mit.compilers.IR.LowLevelIR.MultiQuadLowIR;
import edu.mit.compilers.IR.LowLevelIR.ReturnQuadWithLoc;
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
import edu.mit.compilers.utils.RegOperandForm;
import edu.mit.compilers.utils.Util;
import edu.mit.compilers.utils.X86_64Register;

public class IrResolveNameToLocationVistor implements IrNodeVistor {
	public EnvStack env;
	public MethodDecl currentMethod = null;
	public IrBlock currentBlock = null;
	public List<IrStatement> currentList = null;
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
		if(currentList != null) {
			currentList.add(s);
			return;
		}
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
		List<OperandForm> lst = new ArrayList<>();
		
		List<IrStatement> statements = currentMethod.statements;
		currentMethod.statements = new ArrayList<>();
		if(Util.isMainMethod(m)) {
			setArraySizeForAllVar(env.peekVariables(), env.peekMethod(), true);
		}
		env.pushVariables(m.localVars);
		setArraySizeForAllVar(env.peekVariables(), env.peekMethod(), false);
		
		for(String id: m.paraList) {
			lst.add(getParaOperandForm(id, env.peekVariables(), env.peekMethod()));
		}
		m.setParaOpr(lst);
		
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
		IrOperand ret = (IrOperand) r.getExpr();
		ReturnQuadWithLoc quad = new ReturnQuadWithLoc(getOperandForm(ret, env.peekVariables(), env.peekMethod()));
		quad.setIs64bit(r.isIs64bit());
		addIrStatement(quad);
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
		addIrStatement(resetQuad(quad, env.peekVariables(), env.peekMethod()));
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
	public IrQuadWithLocation setArraySize(ArrayDecl arr, VariableTable vtb, MethodTable mtb, boolean isGlobl) {
		OperandForm operand = arrayStartMemLoc(arr, vtb, mtb, isGlobl);
		int size = arr.getArraySize();
		return new IrQuadWithLocation("movq", new ImmOperandForm(size, 8), operand);
	}
	
	
	protected OperandForm getParaOperandForm(String id, VariableTable vtb, MethodTable mtb) {
		int step = 1;
		Variable_decl decl = vtb.get(id);
		if(decl.getIrType().equals(IrType.IntType))
			step = 8;
		String imm = vtb.getMemLocation(id);
		String base = X86_64Register.rbp.getName_64bit();
		
		return new MemOperandForm(imm,base, null, step);
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
		if (quad.getOp2() != null)
			op2 = getOperandForm(quad.getOp2(), vtb, mtb);
		if (quad.getOp1() != null) {
			if(quad.getOp1() instanceof IrFuncInvocation) {
				setOperandFormForFunc((IrFuncInvocation) quad.getOp1(), op2, quad.is_64bit, vtb, mtb);
				return false;
			}
			else
				op1 = getOperandForm(quad.getOp1(), vtb, mtb);
		}
		
		if (quad.getDest() != null)
			dest = getOperandForm(quad.getDest(), vtb, mtb);
		
		
		addIrStatement(new IrQuadWithLocation(symbol, op1, op2, dest));
		return false;

	}
	
	private void setOperandFormForFunc(IrFuncInvocation func, OperandForm ret, boolean is64bit, VariableTable v, MethodTable m) {
		func.setHasRetValue(true);
		func.setMemOperandForm(ret);
		func.setIs64bit(is64bit);
		IrQuadForFuncInvoke funcQuad = new IrQuadForFuncInvoke(func, v, m);
		funcQuad.accept(this);
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
		funcQuad.setHasRetValue(func.getHasRetValue());
		funcQuad.setIs64bit(func.isIs64bit());
		funcQuad.setRetLoc(func.getRetLoc());
		if(func.isPLT())
			funcQuad.setFuncName(func.getId() + "@PLT");
		for(IrExpression e: func.funcArgs) {
			if(e instanceof IrLiteral && ((IrLiteral) e).getType().equals(IrType.stringType)) {
				funcQuad.addParameter(setStringLiteralForFunc((IrLiteral) e, program));
			} else
				funcQuad.addParameter(getOperandForm((IrOperand) e, env.peekVariables(), env.peekMethod()));
		}
		
		addIrStatement(funcQuad);
		return false;
	}
	
	
	public boolean visit(IrQuadForLoopStatement quad) {
		addIrStatement(quad);
		return false;
	}
	
	public static MemOperandForm setStringLiteralForFunc(IrLiteral l, IrProgram program) {
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
	
	
	public MultiQuadLowIR resetMultiQuad(MultiQuadLowIR quad, VariableTable vtb, MethodTable mtb) {
		
		List<IrStatement> temp = currentList;
		currentList = new ArrayList<>();
		for(IrStatement ir: quad.getQuadLst()) {
			if(ir instanceof IrQuad) {
				//lst.add(resetQuad((IrQuad) ir, vtb,  mtb));
				System.out.println("now " + ir.getName());
				if(ir instanceof IrQuadForAssign) {
					//addIrStatement(ir);
					((IrQuadForAssign)ir).accept(this);
				} else {
					currentList.add(resetQuad((IrQuad) ir, vtb,  mtb));
				}
				
			} else if(ir instanceof IrIfBlockQuad){
				((IrIfBlockQuad)ir).accept(this);;
			}
		}
		quad.setQuadLst(currentList);
		currentList = temp;
		return quad;
		
	}
	
	public void resetCondQuad(CondQuad cond, VariableTable vtb, MethodTable mtb) {
		List<LowLevelIR> condStack = cond.getCondStack();
		if(condStack.size() == 1) {
			LowLevelIR quad =  condStack.remove(0);
			if(quad instanceof IrQuad)
				condStack.add(resetQuad((IrQuad)quad, vtb, mtb));
			else if(quad instanceof MultiQuadLowIR) {
				condStack.add(resetMultiQuad((MultiQuadLowIR) quad, vtb, mtb));
			}else if(quad instanceof IrQuadWithLocation) {
				throw new ClassCastException(quad.getName());
			}else if(quad instanceof CondQuad){
				//condStack.add(resetCondQuad((CondQuad) quad, vtb, mtb));
				condStack.add(giveLocationToLowIr(quad, vtb, mtb));
			}
		} else {
			for(int i = 0; i < condStack.size(); i++) {
				condStack.set(i, giveLocationToLowIr(condStack.get(i), vtb, mtb));
			}
		}
	}
	
	private LowLevelIR giveLocationToLowIr(LowLevelIR ir, VariableTable vtb, MethodTable mtb) {
		if(ir instanceof IrQuad) {
			return resetQuad((IrQuad) ir, vtb, mtb);
		} else if(ir instanceof MultiQuadLowIR) {
			return resetMultiQuad((MultiQuadLowIR) ir, vtb, mtb);
		}else  if(ir instanceof CondQuad) {
			List<LowLevelIR> condStack = ((CondQuad)ir).getCondStack();
			if(condStack.size() == 1) {
				LowLevelIR quad =  condStack.remove(0);
				if(quad instanceof IrQuad)
					condStack.add(resetQuad((IrQuad) quad, vtb, mtb));
				else if(quad instanceof MultiQuadLowIR)
					condStack.add(resetMultiQuad((MultiQuadLowIR) quad, vtb, mtb));
				else if(quad instanceof CondQuad) {
					condStack.add(giveLocationToLowIr(quad, vtb, mtb));
				}
			} else {
				for(int i = 0; i < condStack.size(); i++) {
					LowLevelIR member = condStack.get(i);
					condStack.set(i, giveLocationToLowIr(member, vtb, mtb));
				}
			}
			return ir;
		} else {
			return null;
		}
	}
	
	
	
	

	@Override
	public void visit(IrIfBlockQuad irIfBlockQuad) {
		// TODO Auto-generated method stub
		//irIfBlockQuad.setCondQuad(resetQuad((IrQuad) irIfBlockQuad.getCondQuad(), env.peekVariables(), env.peekMethod()));
		List<IrStatement> temp = currentList;
		currentList = null;
		CondQuad condQuad = irIfBlockQuad.getCondQuad();
		//throw new IllegalArgumentException("int reslove name" + condQuad.getName());
		resetCondQuad(condQuad, env.peekVariables(), env.peekMethod());
		irIfBlockQuad.getTrueBlock().accept(this);
		if(irIfBlockQuad.getFalseBlock() != null)
			irIfBlockQuad.getFalseBlock().accept(this);
		currentList = temp;
		addIrStatement(irIfBlockQuad);
		
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
	public void visit(IrWhileBlockQuad whileQuad) {
		// TODO Auto-generated method stub
		CondQuad condQuad = whileQuad.getCond();
		resetCondQuad(condQuad, env.peekVariables(), env.peekMethod());
		currentList = new ArrayList<IrStatement>();
		for(IrStatement s: whileQuad.getPreQuad()) {
			s.accept(this);
		}
		whileQuad.setPreQuad(currentList);
		currentList = null;
		if(whileQuad.getBlock() != null)
			whileQuad.getBlock().accept(this);
		
		if(whileQuad.getAfterBlockQuad() != null) {
			currentList = new ArrayList<>();
			for(IrStatement s: whileQuad.getAfterBlockQuad()) {
				s.accept(this);
			}
			whileQuad.setAfterBlockQuad(currentList);
			whileQuad.getBlock().addIrStatement(currentList);
			currentList = null;
		}
		addIrStatement(whileQuad);
		
	}

}
