package edu.mit.compilers.CFG.Optimizitation.RegAllocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.mit.compilers.CFG.CFGNode;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.utils.X86_64Register;

public class SingleValueWeb {
	public IrLocation loc;
	public int count;
	
	public int regNumbering = -1;
	
	public boolean useMem = false;
	
	public boolean isUseMem() {
		return useMem;
	}


	public void setUseMem(boolean useMem) {
		this.useMem = useMem;
	}


	public int getRegNumbering() {
		return regNumbering;
	}


	public void setRegNumbering(int regNumbering) {
		this.regNumbering = regNumbering;
	}


	public Map<CFGNode, Set<Integer>> posMap;
	
	public SingleValueWeb(IrLocation loc, int count) {
		this.loc = loc;
		this.count = count;
		posMap = new HashMap<>();
	}
	
	
	public void addPos(HashSet<IrQuadPos> posis) {
		for(IrQuadPos pos : posis) {
			if(!posMap.containsKey(pos.getNode())) {
				posMap.put(pos.getNode(), new HashSet<>());
			}
			posMap.get(pos.getNode()).add(pos.getCursor());
		}
	}
	
	public void setStatReg() {
		for(CFGNode n: posMap.keySet()) {
			for(int e: posMap.get(n)) {
				IrStatement s = n.statements.get(e);
				if(s.locRegs == null) {
					s.locRegs = new HashMap<>();
				}
				if(regNumbering != -1)
					s.locRegs.put(loc, X86_64Register.usedForValue[regNumbering]);
			}
		}
	}
	
	private static boolean subAliveInference(IrQuad quad, IrLocation loc1, IrLocation loc2) {
		//throw new IllegalArgumentException(quad.getName()+ "   " + loc1.getName() + "  " + loc2.getName());
		//return (quad.use.contains(loc1) && quad.subsequentAlive.contains(loc2)) || (quad.use.contains(loc2) && quad.subsequentAlive.contains(loc1));
		IrLocation dst = (IrLocation) quad.getRealDst();
		if(dst != null) {
			if(dst.equals(loc1) && quad.subsequentAlive.contains(loc2) || dst.equals(loc2) && quad.subsequentAlive.contains(loc1))
				return true;
		}
		return false;
	}
	
	public static boolean inference(SingleValueWeb web1, SingleValueWeb web2) {
		if(web1.loc.equals(web2.loc)) {
			//throw new IllegalArgumentException("two same variable don't need to check inference");
			return false;
		}
		
		for(CFGNode n1: web1.posMap.keySet()) {
			if(web2.posMap.containsKey(n1)) {
				for(Integer i: web1.posMap.get(n1)) {
					if(web2.posMap.get(n1).contains(i)) {
						IrQuad quad = (IrQuad) n1.statements.get(i);
						if(quad.use.contains(web1.loc) && quad.use.contains(web2.loc)) {
							return true;
						} else if(subAliveInference(quad, web1.loc, web2.loc)) {
							return true;
						}
					}
				}
			}
			
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(loc.getName() + " " + count+"\n");
		if(regNumbering != -1)
			sb.append(X86_64Register.usedForValue[regNumbering].getName_64bit() + "\n");
		for(CFGNode n: posMap.keySet()) {
			for(Integer i: posMap.get(n)) {
				sb.append(n.statements.get(i).getName());
			}
		}
		return sb.toString();
	}
	
}
