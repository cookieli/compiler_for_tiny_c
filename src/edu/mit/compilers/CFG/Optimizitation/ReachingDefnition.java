package edu.mit.compilers.CFG.Optimizitation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.mit.compilers.CFG.CFG;
import edu.mit.compilers.CFG.CFGNode;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;

public class ReachingDefnition extends DataFlow {

	public HashMap<IrQuad, Integer> defWithInts;// = new HashMap<>();
	public HashMap<Integer, IrQuad> IntWithDefs;// = new HashMap<>();
	public int cursor = 0;

	public ReachingDefnition(CFG cfg) {
		super(cfg);
		defWithInts = new HashMap<>();
		IntWithDefs = new HashMap<>();

		for (CFGNode node : cfg.nodes) {
			visit(node);
		}
		// nodeSet.addAll(cfg.nodes);
		// IrOperand.inDataFlow = true;
	}

	@Override
	public void visit(CFGNode node) {
		node.resetBitSet();
		if (node.statements != null)
			for (IrStatement s : node.statements) {
				if (s instanceof IrQuad) {
					if (((IrQuad) s).isValueAssign()) {
						defWithInts.put((IrQuad) s, cursor);
						IntWithDefs.put(cursor, (IrQuad) s);
						node.addDefs((IrQuad) s);
						cursor++;
					}
				}
			}
	}

	public BitSet Gen(CFGNode node) {
		BitSet gen = new BitSet(cursor);
		if (node.statements != null)
			for (IrStatement s : node.statements) {
				if (s instanceof IrQuad && ((IrQuad) s).isValueAssign()) {
					IrQuad quad = (IrQuad) s;
					if (defWithInts.containsKey(quad)) {
						gen.set(defWithInts.get(quad));
					} else {
						throw new IllegalArgumentException("not found quad" + quad.getName());
					}
				}
			}
		return gen;
	}

	public BitSet Kill(CFGNode node) {
		BitSet kill = new BitSet(cursor);
		int count = 0;
		if (node.statements != null)
			for (IrStatement s : node.statements) {
				if (s instanceof IrQuad && ((IrQuad) s).isValueAssign()) {
					IrQuad quad = (IrQuad) s;
					if (defWithInts.containsKey(quad)) {
						count = defWithInts.get(quad);
					} else {
						throw new IllegalArgumentException("not found quad" + quad.getName());
					}
					for (int i = 0; i < count; i++) {
						IrQuad def = IntWithDefs.get(i);
						if (IrQuad.hasSameDst(quad, def)) {
							kill.set(i);
						}
					}
				}
			}
		return kill;
	}

	public void initialize() {
		for (CFGNode n : nodeSet) {
			n.out = new BitSet(cursor);
		}
		methodCFG.entry.in = new BitSet(cursor);
		methodCFG.entry.out = Gen(methodCFG.entry);
		System.out.println("entry out " + methodCFG.entry.out);
		nodeSet.remove(methodCFG.entry);
	}

	public void setInAndOutForNode(CFGNode node) {
		node.in = new BitSet(cursor);
		for (CFGNode pre : node.parents) {
			if (pre.out == null) {
				System.out.println(nodeSet.contains(pre));
				System.out.println(methodCFG.nodes.contains(pre));
				System.out.println(node == pre);
				System.out.println(node.getStats());
				throw new IllegalArgumentException(pre.getName());
			}
			node.in.or(pre.out);
		}
		System.out.println("in " + node.in);
		BitSet kill;
		if (node.kill == null) {
			node.kill = Kill(node);
		}
		kill = node.kill;
		System.out.println("kill " + kill);
		BitSet InMinusKill = new BitSet(kill.size());
		InMinusKill.or(node.in);
		for (int j = 0; j < kill.size(); j++) {
			if (node.in.get(j) && node.in.get(j) == kill.get(j)) {
				InMinusKill.set(j, false);
			}
		}
		BitSet gen = new BitSet();
		if (node.gen == null) {
			node.gen = Gen(node);
		}
		gen.or(node.gen);
		System.out.println("gen " + gen);
		gen.or(InMinusKill);
		node.out = gen;
		System.out.println("out " + node.out);

	}

	public void reachDef() {
		initialize();
		List<CFGNode> lst;

		while (!nodeSet.isEmpty()) {
			lst = new ArrayList<>();
			lst.addAll(nodeSet);
			CFGNode node = lst.get(0);
			System.out.println(node.getStats());
			nodeSet.remove(node);
			BitSet outCopy = deepCopy(node.out);
			System.out.println("out " + node.out);
			System.out.println("out copy " + outCopy);
			setInAndOutForNode(node);
			if (!outCopy.equals(node.out)) {
				if (node.getSuccessor() != null) {
					for (CFGNode n : node.getSuccessor()) {
						nodeSet.add(n);
					}
				}
			}

		}

	}

	private void setNodeDefs(CFGNode node) {
		if (node.defs != null) {
			for (int i = 0; i < node.in.length(); i++) {
				if (node.in.get(i) && node.out.get(i)) {
					IrQuad quad = IntWithDefs.get(i);
					IrLocation dst = (IrLocation) quad.getDest();
					if (dst == null)
						dst = (IrLocation) quad.getOp2();
					if (node.defs.containsKey(dst))
						node.defs.get(dst).add(i);
				}
			}
		}
	}

	public void setNodesDefs() {
		for (int i = 0; i < methodCFG.nodes.size(); i++) {
			setNodeDefs(methodCFG.nodes.get(i));
		}
	}

	public void CPvisit() {
		for (int i = 1; i < methodCFG.nodes.size(); i++) {
			CPvisit(methodCFG.nodes.get(i));
		}
	}

	public IrOperand CP(IrLocation loc, HashMap<IrLocation, List<Integer>> defs) {
		System.out.println(loc.getId());
		if (!(loc.getNaming() == 0) && !loc.isTempVariable()) {
			if (!defs.containsKey(loc)) {
				//throw new IllegalArgumentException(loc.getId());
				return null;
			}
			if (defs.get(loc).size() == 1) {
				IrQuad def = IntWithDefs.get(defs.get(loc).get(0));
				if (def.isMovAssign()) {
					if (def.getOp1() instanceof IrLocation) {
						IrLocation op = (IrLocation) def.getOp1();
						if (op.sizeExpr == null && !op.isTempVariable()) {
							//throw new IllegalArgumentException(op.getId());
							return op;
						}
					} else if (def.getOp1() instanceof IrLiteral) {
						return def.getOp1();
					}
				} 
			} 
		}
		
		return null;
	}
	
	private void resetIrQuad(IrQuad quad, HashMap<IrLocation, List<Integer>> defs) {
		if (quad instanceof IrQuadForFuncInvoke) {
			IrFuncInvocation func = (IrFuncInvocation) ((IrQuadForFuncInvoke) quad).getFunc();
			for (int i = 0; i < func.funcArgs.size(); i++) {
				if (func.funcArgs.get(i) instanceof IrLocation) {
					IrLocation loc = (IrLocation) func.funcArgs.get(i);
					IrOperand opr = CP(loc, defs);
					if (opr != null) {
						func.funcArgs.set(i, opr);
					}
				}
			}
		} else {
			if (quad.getOp1() instanceof IrLocation) {
				IrLocation op1 = (IrLocation) quad.getOp1();
				IrOperand opr = CP(op1, defs);
				if (opr != null) {
					quad.setOp1(opr);
				}
			}
			if (quad.getDest() != null || !quad.isValueAssign()) {
				if (quad.getOp2() instanceof IrLocation) {
					IrLocation op2 = (IrLocation) quad.getOp2();
					IrOperand opr = CP(op2, defs);
					if (opr != null) {
						quad.setOp2(opr);
					}
				}
			}
		}
	}
	
	private void resetReturnAssign(Return_Assignment s, HashMap<IrLocation, List<Integer>> defs) {
		IrExpression expr = s.getExpr();
		if(expr instanceof IrLocation) {
			IrOperand opr = CP((IrLocation) expr, defs);
			if(opr != null) {
				s.setReturnExpr(opr);
			}
		}
	}

	private void CPvisit(CFGNode node) {
		if (node.statements != null && node.defs != null)
			for (IrStatement s : node.statements) {
				if (s instanceof IrQuad) {
					IrQuad quad = (IrQuad) s;
					resetIrQuad(quad, node.defs);
				} else if (s instanceof Return_Assignment) {
					resetReturnAssign((Return_Assignment) s, node.defs);
				}
			}
	}

	public static void main(String[] args) {

		BitSet bits = new BitSet(8);
		bits.set(0, false);
		bits.set(1, false);
		bits.set(2, false);
		bits.set(3, false);
		bits.set(6);

		System.out.println(bits);
		byte[] array = bits.toByteArray();
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + ",");
		}
		System.out.println(bits.length());

	}

}
