package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.CFG.CFG;
import edu.mit.compilers.CFG.CFGNode;
import edu.mit.compilers.CFG.ProgramCFG;
import edu.mit.compilers.IR.IR_decl_Node.ArrayDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.LowLevelIR.EntryPoint;
import edu.mit.compilers.IR.LowLevelIR.ExitPoint;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;
import edu.mit.compilers.IR.LowLevelIR.stackAllocIR;
import edu.mit.compilers.IR.LowLevelIR.stackFreeIR;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.utils.OperandForm;

public class ResolveNameForCFG extends IrResolveNameToLocationVistor {

	public VariableTable globlVtb;
	public MethodTable globlMtb;
	

	public static void visit(ProgramCFG p) {
		ResolveNameForCFG vistor = new ResolveNameForCFG();
		vistor.globlVtb = p.globlVtb;
		vistor.globlMtb = p.globlMtb;
		vistor.program = p.program;
		vistor.env.pushMethods(vistor.globlMtb);
		for (String key : p.cfgs.keySet()) {
			vistor.visit(p.cfgs.get(key));
		}
		vistor.env.popMethod();
	}

	public void visit(CFG cfg) {
		
		List<OperandForm> lst = new ArrayList<>();
		env.pushVariables(cfg.getWholeMethodVtb());
		for(String id: cfg.paraNameLst) {
			lst.add(getParaOperandForm(id, env.peekVariables(), env.peekMethod()));
		}
		cfg.setParaLst(lst);
		CFGNode first = cfg.nodes.get(0);
		if(cfg.isMain()) {
			
			visit(first, true);
		} else {
			visit(first, false);
		}
		for(int i = 1;  i < cfg.nodes.size(); i++) {
			visit(cfg.nodes.get(i), false);
		}
		env.popVariables();
	}

	private List<LowLevelIR> setArraySizeForAllVar(VariableTable vtb, MethodTable mtb, boolean isGlobl) {
		List<LowLevelIR> statements = new ArrayList<>();
		for (Variable_decl v : vtb) {
			if (v instanceof ArrayDecl) {
				statements.add(setArraySize((ArrayDecl) v, vtb, mtb, isGlobl));
			}
		}
		return statements;
	}

	public void visit(CFGNode node, boolean isMainFirstNode) {
		List<IrStatement> stats = new ArrayList<>();
		List<IrStatement> tempList = currentList;
		currentList = stats;
		env.pushVariables(node.getVtb());
		System.out.println(env.peekVariables());
		if(node.statements == null)
			return ;
		for (int i = 0; i < node.statements.size(); i++) {
			node.statements.get(i).accept(this);
			if (node.statements.get(i) instanceof EntryPoint) {
				currentList.add(node.statements.get(i));
				if (isMainFirstNode)
					currentList.addAll(setArraySizeForAllVar(globlVtb, globlMtb, true));
				if(node.getVtb() == null) {
					//System.out.println(node.getStats());
					throw new IllegalArgumentException("vtb is null");
				}
				currentList.addAll(setArraySizeForAllVar(node.getVtb(), globlMtb, false));
			} 
			if(node.statements.get(i) instanceof stackAllocIR) {
				currentList.add(node.statements.get(i));
				currentList.addAll(setArraySizeForAllVar(node.getVtb(), globlMtb, false));
			}
			if(node.statements.get(i) instanceof stackFreeIR) {
				currentList.add(node.statements.get(i));
			}
			if(node.statements.get(i) instanceof ExitPoint) {
				currentList.add(node.statements.get(i));
			}
			
		}
		node.statements = currentList;
		currentList = tempList;
		env.popVariables();
	}

}
