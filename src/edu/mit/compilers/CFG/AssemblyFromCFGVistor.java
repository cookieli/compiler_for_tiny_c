package edu.mit.compilers.CFG;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.LowLevelIR.CondQuad;
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
		// System.out.println(fileName);
		try {
			PrintWriter out = new PrintWriter(newFileName);
			out.println(code);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new FileNotFoundException("not create" + newFileName);
			// e.printStackTrace();
		}
	}

	public static String assemblyForWholeCFG(IrProgram p) {
		StringBuilder sb = new StringBuilder();
		sb.append(".file " + "\"" + p.getFilename() + "\"" + "\n");
		sb.append(".text\n");
		if (p.globalVariableTable != null) {
			for (Variable_decl v : p.getGlobalVariableTable().idList()) {
				sb.append(v.getGloblAddr());
			}
		}
		if (p.getRoData() != null) {
			sb.append(p.getRoData().toString());
		}
		sb.append(".text\n");
		LinkedHashMap<String, CFG> maps = cfgNodeVistor.cfgForProgram(p);
		for (String key : maps.keySet()) {
			sb.append(maps.get(key).accept(new AssemblyFromCFGVistor()));
		}
		return sb.toString();
	}

	public void visit(CFGNode n) {
		if (n.getLabel() != null) {
			sb.append(n.getLabel() + ":\n");
		}
		if (n.statements != null)
			for (LowLevelIR ir : n.statements) {
				if (ir instanceof IrQuadWithLocation)
					sb.append(AssemblyForArith.getAssembly(((IrQuadWithLocation) ir)));
				else if (ir instanceof IrQuadWithLocForFuncInvoke) {
					sb.append(AssemblyForArith.getAssemBlyForFuncInvoke((IrQuadWithLocForFuncInvoke) ir));
					sb.append(AssemblyForArith.SetRaxZero());
				} else
					sb.append(ir.getName());

			}
	}

	private String getJmpOpr(String label) {
		return "jmp" + " " + label + "\n";
	}

	private void setJmpOpr(String label) {
		sb.append(getJmpOpr(label));
	}

	public void visit(CFG graph) {
		Stack<CFGNode> branch = new Stack<>();
		CFGNode node = graph.entry;
		CFGNode before = null;
		sb.append(graph.getFuncTitile());
		int count = 1;
		while (node != null) {
			if(!node.isAssemblyVisited())         
				node.accept(this);
			else if( node.getLabel() != null) {       
				setJmpOpr(node.getLabel());
				if(!branch.isEmpty()) {
					node = branch.pop();
					continue;
				}
				return;
			}else {
				throw new IllegalArgumentException("the node is visited and doesn't have label");
			}
			if (node.getSuccessor() != null) {
				before = node;
				if (node.getSuccessor().size() == 1) {
					node = node.getSuccessor().get(0);
					if (node.isMergeNode() && count < node.getIncomingDegree()) {
						if (node.getLabel() == null)
							node.setLabel(AssemblyForArith.getNxtJmpLabel());
						setJmpOpr(node.getLabel());
						try {
							node = branch.pop();
						} catch (EmptyStackException e) {
							System.out.println(
									"the count num is " + count + " node parent are " + node.getIncomingDegree());
							System.exit(-1);
						}
						if (!before.isAssemblyVisited()) {
							count++;
						}
					} else if (count == node.getIncomingDegree() && !before.isAssemblyVisited()) {
						count = 1;
					} else if(before.isAssemblyVisited()) {
						if(!branch.isEmpty())  node = branch.pop();
					}

				} else {
					if (node.getSuccessor().get(1).getLabel() == null) {
						node.getSuccessor().get(1).setLabel(AssemblyForArith.getNxtJmpLabel());
						branch.push(node.getSuccessor().get(1));
					}
					if(!before.isAssemblyVisited())  setJmpLabel(node.getSuccessor().get(1).getLabel());
					node = node.getSuccessor().get(0);
					if(node.isMergeNode() && node.getLabel() == null) node.setLabel(AssemblyForArith.getNxtJmpLabel());
				}
				before.setAssemblyVisited();
			} else {
				if(!branch.isEmpty() && node.isMergeNode()) {
					node.setAssemblyVisited();
					node = branch.pop();
					count++;
				}
				else                    node = null;
			}
		}

	}

	private void setJmpLabel(String label) {
		sb.append(AssemblyForArith.setJmpLabel(label));
	}

	public String getAssembly() {
		return sb.toString();
	}

}
