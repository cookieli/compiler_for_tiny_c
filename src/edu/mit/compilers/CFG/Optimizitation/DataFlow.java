package edu.mit.compilers.CFG.Optimizitation;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;

import edu.mit.compilers.CFG.CFG;
import edu.mit.compilers.CFG.CFGNode;
import edu.mit.compilers.CFG.ProgramCFG;
import edu.mit.compilers.CFG.Optimizitation.RegAllocation.RegisterAllocation;
import edu.mit.compilers.CFG.Optimizitation.RegAllocation.SingleValueWeb;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForAssign;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.utils.X86_64Register;

public abstract class DataFlow extends Optimization {
	public HashSet<CFGNode> nodeSet;
	public CFG methodCFG;

	public static void loopOptimize(ProgramCFG pCFG) {
		for (String id : pCFG.cfgs.keySet()) {
			Dominator domi = new Dominator(pCFG.cfgs.get(id));
			domi.findAlldominators();
			List<LoopSet> lst = domi.constructLoop();
			if (lst != null) {
				for (LoopSet l : lst) {
					System.out.println(l);
					domi.addPreHeader(l);
				}
			}

		}
	}
	
	public static void setUse(ProgramCFG pCFG) {
		for(String id: pCFG.cfgs.keySet()) {
			LivenessAnalysis la = new LivenessAnalysis(pCFG.cfgs.get(id));
			la.liveness();
			la.addUse();
			
			RegisterAllocation regAlloc = new RegisterAllocation(pCFG.cfgs.get(id));
			regAlloc.union();
			//regAlloc.printInfVecs();
			regAlloc.coloring(X86_64Register.usedForValue.length);
			
			
		}
	}
	
	
	
	

	public static void setCse(ProgramCFG pCFG) {
		for (String id : pCFG.cfgs.keySet()) {
			// new DataFlow(pCFG.cfgs.get(id));
			// System.out.println("available expression");
			AvailableExpression avai = new AvailableExpression(pCFG.cfgs.get(id));
			avai.AvaiExpr();
			avai.cse();
			LivenessAnalysis la = new LivenessAnalysis(pCFG.cfgs.get(id));
			la.liveness();
			la.dce();
		}
	}

	public static void setDce(ProgramCFG pCFG) {
		for (String id : pCFG.cfgs.keySet()) {
			ReachingDefnition rd = new ReachingDefnition(pCFG.cfgs.get(id));
			rd.reachDef();
			rd.setNodesDefs();
			rd.CPvisit();

			LivenessAnalysis la2 = new LivenessAnalysis(pCFG.cfgs.get(id));
			la2.liveness();
			la2.dce();
		}
	}

	public static void setDataFlow(ProgramCFG pCFG) {
		for (String id : pCFG.cfgs.keySet()) {
			// new DataFlow(pCFG.cfgs.get(id));

			// System.out.println("available expression");
			AvailableExpression avai = new AvailableExpression(pCFG.cfgs.get(id));
			avai.AvaiExpr();
			avai.cse();
			LivenessAnalysis la = new LivenessAnalysis(pCFG.cfgs.get(id));
			la.liveness();
			la.dce();

			ReachingDefnition rd = new ReachingDefnition(pCFG.cfgs.get(id));
			rd.reachDef();
			rd.setNodesDefs();
			rd.CPvisit();

			LivenessAnalysis la2 = new LivenessAnalysis(pCFG.cfgs.get(id));
			la2.liveness();
			la2.dce();
		}
	}

	public DataFlow() {

	}

	public DataFlow(CFG cfg) {
		methodCFG = cfg;
		nodeSet = new HashSet<>();
		for (CFGNode node : cfg.nodes) {
			renaming(node);
		}
		nodeSet.addAll(cfg.nodes);
		IrOperand.inDataFlow = true;
	}

	public void renaming(CFGNode node) {
		if (node.statements != null)
			for (IrStatement s : node.statements) {
				if (s instanceof IrQuad) {
					setRenaming((IrQuad) s, node.getVtb());
				} else if (s instanceof Return_Assignment) {
					IrExpression opr = ((Return_Assignment) s).getExpr();
					if (opr instanceof IrOperand) {
						setOperandRenaming((IrOperand) opr, node.getVtb());
					}
				}
			}
	}

	public void visit(CFGNode node) {

	}

	public void setRenaming(IrQuad quad, VariableTable vtb) {
		IrOperand op1 = quad.getOp1();
		IrOperand op2 = quad.getOp2();
		IrOperand dst = quad.getDest();
		setOperandRenaming(op1, vtb);
		setOperandRenaming(op2, vtb);
		setOperandRenaming(dst, vtb);

	}

	public BitSet deepCopy(BitSet bits) {
		BitSet ret = new BitSet(bits.size());
		for (int i = 0; i < bits.size(); i++) {
			ret.set(i, bits.get(i));
		}

		return ret;
	}

	public void setOperandRenaming(IrOperand opr, VariableTable vtb) {
		if (opr == null)
			return;
		if (opr instanceof IrLocation) {
			IrLocation loc = (IrLocation) opr;
			// System.out.println(loc.getId() + " "+vtb.getNumbering(loc.getId()));
			loc.setNaming(vtb.getNumbering(loc.getId()));
			if (loc.sizeExpr != null && loc.sizeExpr instanceof IrLocation) {
				setOperandRenaming((IrLocation) loc.sizeExpr, vtb);
			}
		} else if (opr instanceof IrFuncInvocation) {
			IrFuncInvocation func = (IrFuncInvocation) opr;
			for (IrExpression e : func.funcArgs) {
				if (e instanceof IrOperand) {
					setOperandRenaming((IrOperand) e, vtb);
				}
			}
		}
	}

}
