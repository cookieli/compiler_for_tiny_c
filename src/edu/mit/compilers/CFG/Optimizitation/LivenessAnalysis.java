package edu.mit.compilers.CFG.Optimizitation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import edu.mit.compilers.CFG.CFG;
import edu.mit.compilers.CFG.CFGNode;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.SymbolTables.VariableTable;

public class LivenessAnalysis extends DataFlow {
	public HashMap<Integer, IrLocation> intsWithVars;
	public HashMap<IrLocation, Integer> varsWithInts;
	public List<Integer> varsIsGlobl;
	public int count = 0;

	public boolean locationIsTemp(IrLocation loc) {
		return loc.getId().contains(AvailableExpression.tempName);
	}

	public LivenessAnalysis(CFG cfg) {
		super(cfg);
		intsWithVars = new HashMap<>();
		varsWithInts = new HashMap<>();
		varsIsGlobl = new ArrayList<>();
		for (CFGNode n : cfg.nodes) {
			visit(n);
		}
	}

	public void saveDef(IrQuad quad, VariableTable vtb) {
		IrOperand op1 = quad.getOp1();
		IrOperand op2 = quad.getOp2();
		IrOperand dst = quad.getDest();
		saveDef(op1, vtb);
		saveDef(op2, vtb);
		saveDef(dst, vtb);
	}

	public void saveDef(IrOperand op, VariableTable vtb) {
		if (op == null)
			return;
		if (op instanceof IrLocation && ((IrLocation) op).getSizeExpr() == null) {
			IrLocation loc = (IrLocation) op;
			if (!varsWithInts.containsKey(loc)) {
				varsWithInts.put(loc, count);
				intsWithVars.put(count, loc);
				if (vtb.isGloblVariable(loc.getId())) {
					varsIsGlobl.add(count);
				}
				count++;
			}
		}
	}

	@Override
	public void visit(CFGNode n) {
		n.resetBitSet();
		if (n.statements != null) {
			for (IrStatement s : n.statements) {
				if (s instanceof IrQuad && ((IrQuad) s).isValueAssign()) {
					saveDef((IrQuad) s, n.getVtb());
				}
			}
		}
	}

	private void setUseForStat(IrStatement s, BitSet use, BitSet def, BitSet mustInUse) {
		// BitSet mustInUse = new BitSet(count);
		if (s instanceof IrQuad && ((IrQuad) s).isValueAssign()) {
			IrQuad quad = (IrQuad) s;
			IrOperand op1 = quad.getOp1();
			IrOperand op2 = quad.getOp2();
			IrOperand dst = quad.getDest();
			if (dst == null) {
				if (varsWithInts.containsKey(op2)) {
					use.set(varsWithInts.get(op2), false);
					def.set(varsWithInts.get(op2));
				}
			} else {
				if (varsWithInts.containsKey(dst)) {
					use.set(varsWithInts.get(dst), false);
					def.set(varsWithInts.get(dst));
				}
				if (op2 instanceof IrLocation) {
					IrLocation loc2 = (IrLocation) op2;
					if (varsWithInts.containsKey(loc2)) {
						use.set(varsWithInts.get(loc2));
					}
				}
			}

			if (op1 instanceof IrLocation) {
				IrLocation loc = (IrLocation) op1;
				if (varsWithInts.containsKey(loc)) {
					use.set(varsWithInts.get(loc));
				}
			}
		} else if (s instanceof IrQuadForFuncInvoke) {
			IrFuncInvocation invoke = (IrFuncInvocation) ((IrQuadForFuncInvoke) s).getFunc();
			for (IrExpression e : invoke.funcArgs) {
				if (e instanceof IrLocation) {
					IrLocation loc = (IrLocation) e;
					if (varsWithInts.containsKey(loc)) {

						mustInUse.set(varsWithInts.get(loc));

					}

				}
			}
		} else if (s instanceof Return_Assignment) {
			Return_Assignment ret = (Return_Assignment) s;
			if (ret.getExpr() instanceof IrLocation) {
				IrLocation loc = (IrLocation) ret.getExpr();
				if (varsWithInts.containsKey(loc)) {
					mustInUse.set(varsWithInts.get(loc));
				}
			}
		} else if (s instanceof IrQuad) { // it's compare
			IrQuad quad = (IrQuad) s;
			IrOperand op1 = quad.getOp1();
			IrOperand op2 = quad.getOp2();
			if (op1 instanceof IrLocation) {
				IrLocation loc = (IrLocation) op1;
				if (varsWithInts.containsKey(loc)) {
					mustInUse.set(varsWithInts.get(loc));
				}
			}
			if (op2 instanceof IrLocation) {
				IrLocation loc2 = (IrLocation) op2;
				if (varsWithInts.containsKey(loc2)) {
					mustInUse.set(varsWithInts.get(loc2));
				}
			}

		}
		// use.or(mustInUse);
	}

	public BitSet[] UseAndDef(CFGNode n) {
		BitSet use = new BitSet(count);
		BitSet def = new BitSet(count);
		if (n.statements != null) {
			BitSet mustInUse = new BitSet(count);
			for (int i = n.statements.size() - 1; i >= 0; i--) {
				setUseForStat(n.statements.get(i), use, def, mustInUse);
			}
			// use.or(mustInUse);
			n.mustInUse = mustInUse;
		}
		BitSet[] sets = { use, def };
		return sets;
	}

	public BitSet outKillsDef(CFGNode n) {
		BitSet ret = new BitSet();
		ret.or(n.out);

		for (int i = 0; i < n.def.size(); i++) {
			if (n.def.get(i) && ret.get(i) == n.def.get(i)) {
				ret.set(i, false);
			}
		}
		return ret;
	}

	public void setNodeIn(CFGNode n) {
		if (n.use == null || n.def == null) {
			BitSet[] sets = UseAndDef(n);
			n.use = sets[0];
			n.def = sets[1];
		}
		BitSet okd = outKillsDef(n);
		BitSet use = new BitSet();
		use.or(n.use);
		use.or(okd);
		n.in = use;

	}

	public void setNodeOut(CFGNode n) {
		BitSet out = new BitSet(count);
		for (CFGNode s : n.getSuccessor()) {
			out.or(s.in);
		}
		n.out = out;
	}

	public void initialize() {
		for (CFGNode n : nodeSet) {
			n.in = new BitSet(count);
		}
		CFGNode exit = methodCFG.end;
		exit.out = new BitSet(count);
		for (int i = 0; i < varsIsGlobl.size(); i++) {
			exit.out.set(varsIsGlobl.get(i));
		}

		setNodeIn(exit);
		System.out.println("exit out: " + exit.out);
		System.out.println("exit in: " + exit.in);
		nodeSet.remove(exit);
	}

	public void setNodeInAndOut(CFGNode n) {
		setNodeOut(n);
		setNodeIn(n);
		System.out.println("def " + n.def);
		System.out.println("use: " + n.use);
		System.out.println("out: " + n.out);
		System.out.println("in: " + n.in);

	}

	public void liveness() {
		initialize();
		List<CFGNode> lst;
		for (int i = 0; i < count; i++) {
			System.out.print(intsWithVars.get(i).getId() + " ");
		}
		System.out.println();

		while (!nodeSet.isEmpty()) {
			lst = new ArrayList<>();
			lst.addAll(nodeSet);
			CFGNode node = lst.get(0);
			System.out.println(node.getStats());
			nodeSet.remove(node);
			BitSet inCopy = deepCopy(node.in);
			System.out.println("in " + node.in);
			System.out.println("in copy " + inCopy);
			node.out = new BitSet(count);
			node.out.set(0, count);
			setNodeInAndOut(node);
			if (!inCopy.equals(node.in)) {
				if (node.getParents() != null) {
					for (CFGNode n : node.getParents()) {
						nodeSet.add(n);
					}
				}
			}

		}

	}

	public void dce() {
		for (CFGNode n : methodCFG.nodes) {
			dce(n);
		}
	}

	public void dce(CFGNode n) {
		if (n.statements != null) {
			List<IrStatement> lst = new ArrayList<>();
			for (IrStatement s : n.statements) {
				lst.add(s);
				if (s instanceof IrQuad && ((IrQuad) s).isValueAssign()) {
					IrQuad quad = (IrQuad) s;
					IrLocation dst;
					if (quad.getDest() != null) {
						dst = (IrLocation) quad.getDest();
					} else {
						dst = (IrLocation) quad.getOp2();
					}
					if (varsWithInts.containsKey(dst)) {
						if (!n.out.get(varsWithInts.get(dst)) && !n.mustInUse.get(varsWithInts.get(dst))) {
							if (locationIsTemp(dst))
								lst.remove(lst.size() - 1);
							if(n.getVtb().isGloblVariable(dst.getId())) {
								lst.remove(lst.size() - 1);
							}
						}
					}
				}
			}
			n.statements = lst;
		}
	}

}
