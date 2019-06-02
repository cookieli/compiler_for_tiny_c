package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.Quad.IrQuad;
import edu.mit.compilers.IR.Quad.IrQuadForAssign;
import edu.mit.compilers.IR.Quad.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.Quad.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.Quad.IrQuadWithLocation;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
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
import edu.mit.compilers.utils.Util;
import edu.mit.compilers.utils.X86_64Register;

public class IrResolveNameToLocationVistor implements IrNodeVistor {
	public EnvStack env;
	public MethodDecl currentMethod = null;
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

		env.pushVariables(m.localVars);
		for (IrStatement s : statements) {
			s.accept(this);
		}
		env.popVariables();
		return false;
	}

	@Override
	public boolean visit(IrAssignment assign) {
		// TODO Auto-generated method stub
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

	private String getStringForOperand(IrOperand opr, VariableTable v, MethodTable m) {
		if (opr instanceof IrLiteral) {
			IrLiteral literal = (IrLiteral) opr;
			if (literal.getType().equals(IrType.IntType))
				return "$" + literal.getIntValue().toString();
			else
				return "$" + literal.getValue();
		} else if (opr instanceof IrLocation) {
			IrLocation loc = (IrLocation) opr;
			if(!loc.locationIsArray()) {
				//return v.getMemLocation(loc.getId());
				int imm = v.getMemLocation(loc.getId());
				return setMemLocation(imm, Util.stackBaseReg, null, null);
			}else {
				int step = 8;
				if(semantics.getIrOperandType(opr, v, m).equals(IrType.BoolType))
					step = 1;
				int imm = v.getMemLocation(loc.getId());
				IrExpression size = loc.getSizeExpr();
				if(size instanceof IrLiteral)
					return setMemLocation(imm+ ((IrLiteral)size).getIntValue().intValue()*step,Util.stackBaseReg, null, null);
				else if(size instanceof IrLocation) {
					int locOfSize = v.getMemLocation(((IrLocation) size).getId());
					String sizeLoc = setMemLocation(locOfSize, Util.stackBaseReg, null, null);
					IrQuadWithLocation quad = new IrQuadWithLocation("movq", sizeLoc, X86_64Register.getNxtTempForAssign64bit());
					currentMethod.addIrStatement(quad);
					return setMemLocation(imm, Util.stackBaseReg, sizeLoc, Integer.toString(step));
				}
				return null;
			}
		} else
			return null;

	}
	
	private String setMemLocation(Integer imm, String base, String index, String scale) {
		StringBuilder sb = new StringBuilder();
		sb.append(imm.toString());
		sb.append("(");
		sb.append(base);
		if(index != null) {
			sb.append(","+index);
			if(scale != null)
				sb.append(","+ scale);
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public boolean visit(IrQuadForAssign quad) {
		VariableTable vtb = env.peekVariables();
		MethodTable mtb = env.peekMethod();
		String symbol = quad.getSymbol();
		String op1 = null;
		String op2 = null;
		String dest = null;
		if (quad.getOp1() != null) {
			op1 = getStringForOperand(quad.getOp1(), vtb, mtb);
		}
		if (quad.getOp2() != null)
			op2 = getStringForOperand(quad.getOp2(), vtb, mtb);
		if (quad.getDest() != null)
			dest = getStringForOperand(quad.getDest(), vtb, mtb);
		

		currentMethod.addIrStatement(new IrQuadWithLocation(symbol, op1, op2, dest));
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
		//int i = 0;
		String paraLoc = null;
		int pushNum = func.getFuncArgNum() - paraPassReg.length;
		int allocSize = pushNum % 2;
		if(allocSize > 0)
		funcQuad.addOprBeforeCall(IrQuadWithLocation.reallocStack(allocSize* 8));
		
		for(int i = func.getFuncArgNum()-1 ; i >=0; i--) {
			IrExpression e = func.getFunctionArg(i);
			if(i > 5) {
				paraLoc = paraPassReg[0];
			} else
				paraLoc = paraPassReg[i];
			if (e instanceof IrLiteral && ((IrLiteral) e).getType().equals(IrType.stringType)) {
				funcQuad.addOprBeforeCall(getStrLiteralLeaqForProgram((IrLiteral) e, paraLoc));
			}
			else if(e instanceof IrOperand) {
				String src = getStringForOperand((IrOperand) e, env.peekVariables(), env.peekMethod());
				funcQuad.addOprBeforeCall(IrQuadWithLocation.movToReg((IrOperand) e, src, paraLoc, env.peekVariables(), env.peekMethod()));
			}
			if(i > 5) {
				//funcQuad.addOprBeforeCall(IrQuadWithLocation.reallocStack(8));
				funcQuad.addOprBeforeCall(IrQuadWithLocation.push(paraLoc));
			}
			
			
		}
		funcQuad.addOprBeforeCall(IrQuadWithLocation.setRaxZero());
		int deallocSize = func.getFuncArgNum() - 6;
		if(deallocSize + allocSize > 0) {
			funcQuad.addOprAfterCall(IrQuadWithLocation.deallocStack((deallocSize + allocSize)*8));
		}
		
		//funcQuad.addOprBeforeCall(AssemblyForArith.SetRaxZero());
		
		currentMethod.addIrStatement(funcQuad);
		return false;
	}
	
	public IrQuadWithLocation getStrLiteralLeaqForProgram(IrLiteral l, String reg) {
		program.addReadOnlyData(l.getValue());
		String loc = program.getRoData().getLastLabel() + "(" + rip +")";
		return IrQuadWithLocation.setLeaqOpr(loc, reg);
	}

	@Override
	public boolean visit(IrQuadWithLocForFuncInvoke quad) {
		// TODO Auto-generated method stub
		return false;
	}

}
