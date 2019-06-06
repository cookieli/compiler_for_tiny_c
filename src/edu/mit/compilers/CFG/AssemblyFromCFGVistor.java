package edu.mit.compilers.CFG;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;
import edu.mit.compilers.assembly.AssemblyForArith;

public class AssemblyFromCFGVistor {
	StringBuilder sb;
	public static final String tab = "\t";
	
	
	public AssemblyFromCFGVistor() {
		sb = new StringBuilder();
	}
	
	public static void assemblyFile(String code, String file) throws FileNotFoundException {
		String fileName = file;
		String newFileName = fileName.replaceAll(".dcf", ".s");
		//System.out.println(fileName);
		try {
			PrintWriter out = new PrintWriter(newFileName);
			out.println(code);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new FileNotFoundException("not create" + newFileName);
			//e.printStackTrace();
		}
	}
	
	public static String assemblyForWholeCFG(IrProgram p) {
		StringBuilder sb = new StringBuilder();
		sb.append(".file "+ "\""+p.getFilename()+ "\""+"\n");
		sb.append(".text\n");
		if(p.globalVariableTable != null) {
			for(Variable_decl v: p.getGlobalVariableTable().idList()) {
				sb.append(v.getGloblAddr());
			}
		}
		if(p.getRoData() != null) {
			sb.append(p.getRoData().toString());
		}
		sb.append(".text\n");
		LinkedHashMap<String, CFG> maps = cfgNodeVistor.cfgForProgram(p);
		for(String key: maps.keySet()) {
			sb.append(maps.get(key).accept(new AssemblyFromCFGVistor()));
		}
		return sb.toString();
	}
	
	public void visit(CFGNode n) {
		for(LowLevelIR ir: n.statements) {
			if(ir instanceof IrQuadWithLocation)
				sb.append(AssemblyForArith.getAssembly(((IrQuadWithLocation) ir)));
			else if(ir instanceof IrQuadWithLocForFuncInvoke) {
				sb.append(AssemblyForArith.getAssemBlyForFuncInvoke((IrQuadWithLocForFuncInvoke) ir));
				sb.append(AssemblyForArith.SetRaxZero());
			} else
				sb.append(ir.getName());
			
		}
	}
	
	public void visit(CFG graph) {
		Stack<CFGNode> branch  = new Stack<>();
		CFGNode node = graph.entry;
		sb.append(graph.getFuncTitile());
		while(node != null) {
			node.accept(this);
			if(node.getSuccessor() != null) {
				if(node.getSuccessor().size() == 1)
					node = node.getSuccessor().get(0);
			} else {
				node = null;
			}
		}
	}
	
	public String getAssembly() {
		return sb.toString();
	}

}
