package edu.mit.compilers.CFG;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.LowLevelIR.CondQuad;
import edu.mit.compilers.IR.LowLevelIR.EntryPoint;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForLoopStatement;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.LowLevelIR;
import edu.mit.compilers.IR.LowLevelIR.ReturnQuadWithLoc;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.assembly.AssemblyForArith;
import edu.mit.compilers.utils.MemOperandForm;
import edu.mit.compilers.utils.OperandForm;
import edu.mit.compilers.utils.X86_64Register;

public class AssemblyFromCFGVistor {
	StringBuilder sb;
	public static final String tab = "\t";
	public String currenWhileLabel = null;
	
	
	
	public static CFG currentCFG = null; 

	public cfgNodeStack branch;
	public Stack<String> loopStack = null;
	
	public List<OperandForm> currMethodPara = null;

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
	
	public static String assemblyForWholeCFG(ProgramCFG pro) {
		LinkedHashMap<String, CFG> maps  = pro.cfgs;
		IrProgram p =pro.program;
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
		
		for (String key : maps.keySet()) {
			currentCFG = maps.get(key);
			System.out.println(currentCFG);
			sb.append(maps.get(key).accept(new AssemblyFromCFGVistor()));
		}
		return sb.toString();
	}

	public static String assemblyForWholeCFG(IrProgram p) {
		LinkedHashMap<String, CFG> maps = cfgNodeVistor.cfgForProgram(p);
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
		
		for (String key : maps.keySet()) {
			currentCFG = maps.get(key);
			System.out.println(currentCFG);
			sb.append(maps.get(key).accept(new AssemblyFromCFGVistor()));
		}
		return sb.toString();
	}

	public void visit(CFGNode n) {
		if (n.getLabel() != null) {
			// sb.append(" label");
			sb.append(n.getLabel() + ":\n");
		}
		if (n.statements != null) {
			for (IrStatement ir : n.statements) {
				if (ir instanceof IrQuadWithLocation)
					sb.append(AssemblyForArith.getAssembly(((IrQuadWithLocation) ir)));
				else if (ir instanceof IrQuadWithLocForFuncInvoke) {
					IrQuadWithLocForFuncInvoke func = (IrQuadWithLocForFuncInvoke) ir;
					sb.append(AssemblyForArith.getAssemBlyForFuncInvoke((IrQuadWithLocForFuncInvoke) ir));
				} else if (ir instanceof IrQuadForLoopStatement) {
					IrQuadForLoopStatement loop = (IrQuadForLoopStatement) ir;
					sb.append("nop\n");
					if (loop.isBreak())
						setJmpOpr(branch.getMostClosestLoopEndNode().getLabel());
					else
						setJmpOpr(loopStack.peek());
				} else if (ir instanceof ReturnQuadWithLoc) {
					sb.append(AssemblyForArith.getAssemblyForReturn((ReturnQuadWithLoc) ir));
					sb.append(getJmpOpr(currentCFG.getEndLabel()));
				} else {
					sb.append(ir.getName());
					if(ir instanceof EntryPoint && currMethodPara != null) {
						sb.append(setParaAsm(currMethodPara));
					}
				}

			}
		} else {
			sb.append("nop\n");
		}
	}

	private String getJmpOpr(String label) {
		return "jmp" + " " + label + "\n";
	}

	private void setJmpOpr(String label) {
		sb.append(getJmpOpr(label));
	}

	private String setParaAsm(List<OperandForm> lst) {
		StringBuilder code = new StringBuilder();
		int len = lst.size();
		if (len > X86_64Register.paraRegNum) {
			for (int i = 0; i < X86_64Register.paraRegNum; i++) {
				code.append(AssemblyForArith.setParaAssemblyMov(X86_64Register.paraPassReg[i], (MemOperandForm) lst.get(i)));
			}
			for(int i = X86_64Register.paraRegNum; i < len; i++) {
				MemOperandForm op1 = getParaOnStack(i- X86_64Register.paraRegNum + 1);
				code.append(AssemblyForArith.setAssemblyMov(op1, (MemOperandForm) lst.get(i)));
			}
		} else {
			for(int i = 0; i < len; i++) {
				code.append(AssemblyForArith.setParaAssemblyMov(X86_64Register.paraPassReg[i], (MemOperandForm) lst.get(i)));
			}
		}
		return code.toString();
	}
	
	private MemOperandForm getParaOnStack(int i) {
		String imm = Integer.toString(i*8+8);
		String base = X86_64Register.rbp.getName_64bit();
		int step = 8;
		return new MemOperandForm(imm, base, null, step);
	}

	public void visit(CFG graph) {

		branch = new cfgNodeStack();
		CFGNode node = graph.entry;
		CFGNode before = null;
		currMethodPara = graph.getParaLst();
		sb.append(graph.getFuncTitile());
		
		while (node != null) {
			if (node.isWhileNode()) {
				if (loopStack == null)
					loopStack = new Stack<>();
				if (node.getLabel() == null)
					node.setLabel(AssemblyForArith.getNxtJmpLabel());
				loopStack.push(node.getLabel());
			}
			if (!node.isAssemblyVisited()) {
				node.accept(this);
			}else if (node.getLabel() != null) {
				setJmpOpr(node.getLabel());

				if (branch.isEmpty())
					return;
				else {
					node = branch.pop();
					continue;
					// throw new IllegalArgumentException("branch "+ node.getStats());
				}
			} else {
				throw new IllegalArgumentException("the node is visited and doesn't have label " + node.isMergeNode()
						+ " " + node.getIncomingDegree() + " \n" + node.getStats());
			}
			if (node.getSuccessor() != null ) {
				before = node;
				if (node.getSuccessor().size() == 1) {
					node = node.getSuccessor().get(0);
					if (node.isMergeNode() && node.visitCount < node.getIncomingDegree()) {
						if (node.getLabel() == null)
							node.setLabel(AssemblyForArith.getNxtJmpLabel());

						if (!before.isAssemblyVisited()) {
							node.visitCount++;
						}

						if (!node.isWhileNode()) {
							if (!branch.isEmpty()) {
								setJmpOpr(node.getLabel());
								node = branch.pop();
							}
						}

					} else if (node.visitCount == node.getIncomingDegree() && node.isMergeNode()) {
						if (node.isWhileNode()) {
							// node = branch.pop();
							// throw new IllegalArgumentException(node.getStats());
							if (!branch.isEmpty()) {
								setJmpOpr(node.getLabel());
								node = branch.pop();
							}
							if (!loopStack.isEmpty()) {
								loopStack.pop();
							}
						}
					}
				} else {
					if(node.getSuccessor().size() == 0) {
						throw new IllegalArgumentException("no succ  " + node.getName());
					}
					if (node.getSuccessor().get(1).getLabel() == null) {
						node.getSuccessor().get(1).setLabel(AssemblyForArith.getNxtJmpLabel());

					}
					branch.push(node.getSuccessor().get(1));
					if (!before.isAssemblyVisited())
						setJmpLabel(node.getSuccessor().get(1).getLabel());
					node = node.getSuccessor().get(0);
					if (node.isMergeNode() && node.getLabel() == null)
						node.setLabel(AssemblyForArith.getNxtJmpLabel());
				}
				before.setAssemblyVisited();
			} else {
				if (!branch.isEmpty()) {
					node.setAssemblyVisited();
					node = branch.pop();
					if (!node.isAssemblyVisited() && node.isMergeNode())
						node.visitCount++;
				} else
					node = null;
			}
		}

	}

	private void setJmpLabel(String label) {

		sb.append(AssemblyForArith.setJmpLabel(label));
	}

	public String getAssembly() {
		return sb.toString();
	}
	
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("jmp .L1\n");
		sb.append("jmp .L2\n");
		System.out.println(sb.lastIndexOf("jmp") + " " + sb.lastIndexOf("\n") + " " +sb.capacity());
		System.out.println(sb.lastIndexOf("\n") - sb.lastIndexOf("jmp"));
	}

}
