package edu.mit.compilers.CFG.Optimizitation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import edu.mit.compilers.CFG.CFG;
import edu.mit.compilers.CFG.CFGNode;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.LowLevelIR.EntryPoint;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.stackAllocIR;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.trees.SemanticCheckerNode;
import edu.mit.compilers.utils.Util;

public class AvailableExpression extends DataFlow {

	public HashMap<Integer, BinaryQuadExpr> IntsWithExpr;
	public HashMap<BinaryQuadExpr, Integer> ExprWithInts;
	public HashMap<Integer, IrLocation> IntsWithValues;
	
	
	public EntryPoint entry = null;

	public int count = 0;

	public final static String tempName = "$$$T";

	public int subScript = 0;

	public static SemanticCheckerNode semantics = new SemanticCheckerNode();

	public AvailableExpression(CFG cfg) {
		super(cfg);
		IntsWithExpr = new HashMap<>();
		ExprWithInts = new HashMap<>();
		IntsWithValues = new HashMap<>();

		for (CFGNode node : cfg.nodes) {
			visit(node);
		}
	}

	@Override
	public void visit(CFGNode n) {
		if (n.statements != null) {
			for (IrStatement s : n.statements) {
				if (s instanceof IrQuad) {
					if (((IrQuad) s).isValueAssign() && ((IrQuad) s).getDest() != null) {
						BinaryQuadExpr binary = ((IrQuad) s).getBinaryQuad();
						if (!ExprWithInts.containsKey(binary)) {
							System.out.println("binary: " + binary.toString());
							System.out.println("count: " + count);
							IntsWithExpr.put(count, binary);
							ExprWithInts.put(binary, count);
							count++;
						}
					}
				}
			}
		}
	}

	public BitSet Gen(CFGNode node) {
		BitSet gen = new BitSet(count);
		if (node.statements != null) {
			for (IrStatement s : node.statements) {
				if (s instanceof IrQuad && ((IrQuad) s).getDest() != null) {
					IrQuad quad = (IrQuad) s;
					if (ExprWithInts.containsKey(quad.getBinaryQuad())) {
						gen.set(ExprWithInts.get(quad.getBinaryQuad()));
					}
				}
			}
		}
		return gen;
	}

	public BitSet Kill(CFGNode node) {
		BitSet kill = new BitSet(count);
		if (node.statements != null) {
			for (IrStatement s : node.statements) {
				if (s instanceof IrQuad && ((IrQuad) s).isValueAssign()) {
					IrQuad quad = (IrQuad) s;
					IrLocation dst = (IrLocation) quad.getDest();
					if (dst == null)
						dst = (IrLocation) quad.getOp2();
					for (int i = 0; i < count; i++) {
						if (IntsWithExpr.get(i).contains(dst)) {
							kill.set(i);
						}
					}
				}
			}
		}

		return kill;
	}

	public void setNodeInAndOut(CFGNode n) {
		setNodeIn(n);
		setNodeOut(n);
		System.out.println("in: " + n.in);
		System.out.println("gen: " + n.gen);
		System.out.println("kill " + n.kill);
		System.out.println("out: " + n.out);
	}

	private void setNodeIn(CFGNode node) {
		BitSet in = new BitSet(count);
		in.set(0, count);
		for (CFGNode pre : node.parents) {
			in.and(pre.out);
		}
		node.in = in;
	}

	public BitSet inMinusKill(CFGNode node) {
		BitSet inMinusKill = new BitSet(count);
		inMinusKill.or(node.in);
		for (int i = 0; i < node.kill.size(); i++) {
			if (node.kill.get(i) && inMinusKill.get(i) == node.kill.get(i)) {
				inMinusKill.set(i, false);
			}
		}
		return inMinusKill;
	}

	private void setNodeOut(CFGNode node) {
		if (node.kill == null) {
			node.kill = Kill(node);
		}
		if (node.gen == null) {
			node.gen = Gen(node);
		}
		BitSet imk = inMinusKill(node);
		BitSet gen = new BitSet();
		gen.or(node.gen);
		gen.or(imk);
		node.out = gen;
	}

	public void AvaiExpr() {
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
			node.in = new BitSet(count);
			node.in.set(0, count);
			setNodeInAndOut(node);
			if (!outCopy.equals(node.out)) {
				if (node.getSuccessor() != null) {
					for (CFGNode n : node.getSuccessor()) {
						nodeSet.add(n);
					}
				}
			}

		}

	}

	public void initialize() {
		for (CFGNode n : nodeSet) {
			n.out = new BitSet(count);
			n.out.set(0, count);
		}
		methodCFG.entry.in = new BitSet(count);
		methodCFG.entry.out = Gen(methodCFG.entry);
		System.out.println("entry: " + methodCFG.entry.out.toString());
		nodeSet.remove(methodCFG.entry);
	}

	private IrLocation getExprCorrespondVar(IrOperand loc, VariableTable src) {
		IrType type = semantics.getIrOperandType(loc, src, null);
		String id = tempName + subScript;
		Variable_decl var = new Variable_decl(id, type);
		methodCFG.wholeMethodVtb.put(var);
		IrLocation ret = new IrLocation(id);
		subScript++;
		return ret;
	}

	public void addNewIrQuad(IrQuad q, List<IrStatement> lst, VariableTable vtb, int num) {
		IrLocation loc = getExprCorrespondVar(q.getDest(), vtb);
		addNewIrQuad(loc, q, lst, vtb);
		IntsWithValues.put(num, loc);
	}

	private void addNewIrQuad(IrLocation dst, IrQuad q, List<IrStatement> lst, VariableTable vtb) {
		IrQuad quad = new IrQuad("mov", q.getDest(), dst, null, vtb, null);
		lst.add(quad);
	}

	public void cse() {
		for (CFGNode node : methodCFG.nodes) {
			cse(node);
		}
		entry.resetStackSize(methodCFG.wholeMethodVtb.getMemSize());
		
	}

	public void cse(CFGNode n) {
		if (n.statements != null) {
			List<IrStatement> lst = new ArrayList<>();
			for (IrStatement s : n.statements) {
				lst.add(s);
				if (s instanceof IrQuad) {
					if (((IrQuad) s).isValueAssign() && ((IrQuad) s).getDest() != null) {
						BinaryQuadExpr binary = ((IrQuad) s).getBinaryQuad();
						if (ExprWithInts.containsKey(binary)) {
							int num = ExprWithInts.get(binary);
							if (n.out.get(num)) {
								if (!n.in.get(num)) {
									addNewIrQuad((IrQuad) s, lst, n.getVtb(), num);
								} else if (n.kill.get(num)) {
									IrLocation loc = IntsWithValues.get(num);
									addNewIrQuad(loc, (IrQuad) s, lst, n.getVtb());
								} else {
									lst.remove(lst.size() - 1);
									lst.add(new IrQuad("mov", IntsWithValues.get(num), ((IrQuad) s).getDest(), null,
											n.getVtb(), null));
								}
							}
						} else {
							throw new IllegalArgumentException("error");
						}
					}
				}  else if( s instanceof EntryPoint) {
					entry = (EntryPoint) s;
				}
			}
			n.statements = lst;
		}
	}

}
