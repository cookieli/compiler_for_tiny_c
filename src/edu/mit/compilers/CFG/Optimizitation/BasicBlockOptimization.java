package edu.mit.compilers.CFG.Optimizitation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.mit.compilers.CFG.CFG;
import edu.mit.compilers.CFG.CFGNode;
import edu.mit.compilers.CFG.ProgramCFG;
import edu.mit.compilers.IR.LowLevelIR.EntryPoint;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.stackAllocIR;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.utils.Util;

public class BasicBlockOptimization extends Optimization {

	public HashMap<String, VirtualValue> varToVal;
	public HashMap<VirtualExpr, VirtualValue> exprToVal;
	public HashMap<VirtualExpr, IrLocation> exprTotemp;
	public HashMap<String, IrLocation> tmpToVar;
	public HashMap<String, Set<String>> varToTmp;
	public HashSet<String> neededSet;
	public static String tempName = "$$temp";
	public static int currentSubScript = 1;
	public int allocSize = 0;
	public VariableTable vtb;
	public MethodTable mtb;
	public List<IrStatement> currentLst;

	public BasicBlockOptimization() {
	}

	public static boolean isTempVariable(String id) {
		return id.startsWith("$$");
	}
	
	public void prepareForCSE() {
		varToVal = new HashMap<>();
		exprToVal = new HashMap<>();
		exprTotemp = new HashMap<>();
	}
	
	public void prepareForCp() {
		tmpToVar = new HashMap<>();
		varToTmp = new HashMap<>();
	}
	
	public void prepareForDCE() {
		neededSet = new HashSet<>();

	}

	public static void optimizeForCFG(ProgramCFG pCFG) {
		BasicBlockOptimization b = new BasicBlockOptimization();
		b.mtb = pCFG.globlMtb;
		for (String key : pCFG.cfgs.keySet()) {
			b.visit(pCFG.cfgs.get(key));
		}
		for (String key : pCFG.cfgs.keySet()) {
			b.CPvisit(pCFG.cfgs.get(key));
		}
		for (String key : pCFG.cfgs.keySet()) {
			b.DCEvisit(pCFG.cfgs.get(key));
		}
	}

	public IrLocation getExprCorrespondsVar(IrLocation dst) {
		return Util.getExprCorrespondVar(dst, vtb, mtb, tempName, currentSubScript++);
	}

	public void addStatement(IrStatement s) {
		if (currentLst != null)
			currentLst.add(s);
	}

	public void visit(CFG graph) {
		// int count =0;
		for (CFGNode node : graph.nodes) {
			// System.out.println(count++);
			visit(node);
		}
	}

	public void DCEvisit(CFG graph) {
		for (CFGNode node : graph.nodes) {
			DCEvisit(node);
		}
	}

	public void DCEvisit(CFGNode node) {
		this.prepareForDCE();
		List<IrStatement> lst = new ArrayList<>();
		currentLst = node.statements;
		if (node.statements != null) {
			for (int i = node.statements.size() - 1; i >= 0; i--) {
				IrStatement s = node.statements.get(i);
				if (s instanceof IrQuad && ((IrQuad) s).isValueAssign())
					DCEvisit((IrQuad) s);
			}
		}
		// node.statements = currentLst;
		currentLst = null;
	}

	public void DCEvisit(IrQuad assign) {
		if (assign.isValueAssign()) {
			if (assign.getDest() != null) {
				IrLocation dst = (IrLocation) assign.getDest();
				if (isTempVariable(dst.getId()) && !neededSet.contains(dst.getId())) {
					currentLst.remove(assign);
					return;
				}
				if (assign.getOp1() instanceof IrLocation) {
					neededSet.add(((IrLocation) assign.getOp1()).getId());
				}
				if (assign.getOp2() instanceof IrLocation) {
					neededSet.add(((IrLocation) assign.getOp2()).getId());
				}

			} else {
				IrLocation dst = (IrLocation) assign.getOp2();
				if (isTempVariable(dst.getId()) && !neededSet.contains(dst.getId())) {
					currentLst.remove(assign);
					return;
				}

			}
			if (assign.getOp1() instanceof IrLocation) {
				neededSet.add(((IrLocation) assign.getOp1()).getId());
			}
		}
		// addStatement(assign);
	}

	public void CPvisit(CFG graph) {
		for (CFGNode node : graph.nodes) {
			BasicBlockOptimization b= new BasicBlockOptimization();
			b.mtb = this.mtb;
			b.CPvisit(node);
		}
	}

	public void CPvisit(CFGNode node) {
		this.prepareForCp();
		if (node.statements == null)
			return;
		List<IrStatement> lst = new ArrayList<>();
		currentLst = lst;
		if (node.statements != null) {
			for (IrStatement s : node.statements) {
				if (s instanceof IrQuad && ((IrQuad) s).isValueAssign())
					CPvisit((IrQuad) s);
				else {
					addStatement(s);
				}
			}
			node.statements = currentLst;
			currentLst = null;
		}
		// currentLst = null;
	}

	public void CPvisit(IrQuad assign) {
		IrLocation dst = (IrLocation) assign.getDest();
		if (dst != null) {
			if (dst.isArray) {
				addStatement(assign);
				return;
			}
			if (varToTmp.containsKey(dst.getId())) {
				for (String id : varToTmp.get(dst.getId())) {
					tmpToVar.put(id, new IrLocation(id));
				}
				varToTmp.get(dst.getId()).clear();
				addStatement(assign);
				return;
			}
		}
		if (assign.getDest() == null && assign.isMovAssign()) {

			IrLocation loc2 = (IrLocation) assign.getOp2();
			if (loc2 == null || loc2.isArray) {
				addStatement(assign);
				return;
			}

			if (varToTmp.containsKey(loc2.getId())) {
				for (String id : varToTmp.get(loc2.getId())) {
					tmpToVar.put(id, new IrLocation(id));
				}
				varToTmp.get(loc2.getId()).clear();
				addStatement(assign);
				return;
			}
			if (assign.getOp1() instanceof IrLocation) {
				IrLocation loc1 = (IrLocation) assign.getOp1();
				if (loc1.isArray) {
					addStatement(assign);
					return;
				}
				if(isTempVariable(loc2.getId())) {
					tmpToVar.put(loc2.getId(), loc1);
					if (!varToTmp.containsKey(loc1.getId())) {
						varToTmp.put(loc1.getId(), new HashSet<>());
					}
					varToTmp.get(loc1.getId()).add(loc2.getId());
				}
				
				if (tmpToVar.containsKey(loc1.getId())) {
					IrQuad quad = new IrQuad(assign.symbol, tmpToVar.get(loc1.getId()), loc2, null);
					addStatement(quad);
					return;
				}
			}

			addStatement(assign);
		} else {
			addStatement(assign);
		}
	}

	public void visit(CFGNode node) {
		this.prepareForCSE();
		List<IrStatement> lst = new ArrayList<>();
		EntryPoint entry = null;
		stackAllocIR alloc = null;
		currentLst = lst;
		this.vtb = node.getVtb();
		if (node.statements != null) {
			for (IrStatement s : node.statements) {
				if (s instanceof IrQuad && ((IrQuad) s).isValueAssign())
					visit((IrQuad) s);
				else {
					if (s instanceof EntryPoint) {
						entry = (EntryPoint) s;
					}
					if (s instanceof stackAllocIR) {
						alloc = (stackAllocIR) s;
					}

					addStatement(s);
				}
			}
		} else {
			currentLst = null;
			node.setVtb(this.vtb);
		}
		if (entry != null)
			entry.resetStackSize(this.vtb.getMemSize());
		if (alloc != null)
			alloc.resetAllocSize(this.vtb.getMemSize());
		node.statements = currentLst;
		currentLst = null;
		node.setVtb(this.vtb);
		this.vtb = null;
	}

	public void visit(IrQuad assign) {
		if (!(assign.getOp1() instanceof IrLocation) || !(assign.getOp2() instanceof IrLocation)) {
			IrLocation dst;
			if (assign.getDest() != null) {
				dst = (IrLocation) assign.getDest();
			} else {
				dst = (IrLocation) assign.getOp2();
			}
			varToVal.put(dst.getId(), VirtualValue.generate());
			addStatement(assign);
			return;
		}

		IrLocation op1 = (IrLocation) assign.getOp1();
		IrLocation op2 = (IrLocation) assign.getOp2();
		IrLocation dst = (IrLocation) assign.getDest();
		if (op1.isArray || op2.isArray) {
			addStatement(assign);
			return;
		}
		IrQuad newAssign = null;
		IrLocation temp = null;
		if (dst != null) {
			temp = getExprCorrespondsVar(dst);
			newAssign = new IrQuad("movq", dst, temp, null);
		}
		if (!varToVal.containsKey(op1.getId()))
			varToVal.put(op1.getId(), VirtualValue.generate());
		if (dst == null) {
			varToVal.put(op2.getId(), varToVal.get(op1.getId()));
			addStatement(assign);
		} else {

			if (!varToVal.containsKey(op2.getId()))
				varToVal.put(op2.getId(), VirtualValue.generate());
			VirtualExpr expr = new VirtualExpr(varToVal.get(op1.getId()), varToVal.get(op2.getId()),
					assign.getSymbol());
			if (!exprToVal.containsKey(expr)) {
				varToVal.put(dst.getId(), VirtualValue.generate());
				exprToVal.put(expr, varToVal.get(dst.getId()));
				exprTotemp.put(expr, temp);
				addStatement(assign);
			} else {
				varToVal.put(dst.getId(), exprToVal.get(expr));
				IrLocation loc = exprTotemp.get(expr);
				addStatement(new IrQuad("movq", loc, dst, null));
			}
			addStatement(newAssign);
		}

	}
}
