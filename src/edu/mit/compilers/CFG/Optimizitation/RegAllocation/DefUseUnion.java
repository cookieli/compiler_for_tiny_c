package edu.mit.compilers.CFG.Optimizitation.RegAllocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.mit.compilers.IR.expr.operand.IrLocation;

public class DefUseUnion {
	
	
	public IrLocation loc;
	public HashMap<IrQuadPos, IrQuadPos > id;
	public HashMap<IrQuadPos, Integer> sz;
	
	public HashMap<IrQuadPos, HashSet<IrQuadPos>> rootGroup;
	public int count;
	
	public DefUseUnion(IrLocation loc) {
		this.loc = loc;
		id = new HashMap<>();
		sz = new HashMap<>();
		rootGroup = new HashMap<>();
		count = 0;
		
	}
	
	public void makeSet(IrQuadPos pos) {
		if(id.containsKey(pos))
			return;
		id.put(pos, pos);
		sz.put(pos, 1);
		rootGroup.put(pos, new HashSet<>());
		rootGroup.get(pos).add(pos);
		count++;
	}
	
	
	public IrQuadPos find(IrQuadPos code) {
		while(id.get(code) != code) {
			code = id.get(code);
		}
		return code;
	}
	
	private void unionSet(IrQuadPos rootA, IrQuadPos rootB) {
		rootGroup.get(rootA).addAll(rootGroup.get(rootB));
		rootGroup.remove(rootB);
	}
	
	public  void union(IrQuadPos uA, IrQuadPos uB) {
		IrQuadPos rootA = find(uA);
		IrQuadPos rootB = find(uB);
		if(!rootA.equals(rootB)) {
			if(sz.get(rootA) < sz.get(rootB)) {
				id.put(rootA, rootB);
				sz.put(rootB, sz.get(rootA) + sz.get(rootB));
				unionSet(rootB, rootA);
			} else {
				id.put(rootB, rootA);
				sz.put(rootA, sz.get(rootA) + sz.get(rootB));
				unionSet(rootA,rootB);
			}
		}
		count--;
	}
	
	
	public boolean connected(IrQuadPos codeA, IrQuadPos codeB) {
		return find(codeA) == find(codeB);
	}
	
	public List<SingleValueWeb> getWeb(){
		List<SingleValueWeb> lst = new ArrayList<>();
		int cnt = 0;
		for(IrQuadPos pos : rootGroup.keySet()) {
			SingleValueWeb web = new SingleValueWeb(loc, cnt);
			web.addPos(rootGroup.get(pos));
			lst.add(web);
			cnt++;
		}
		return lst;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(loc.getName() + " " + count + "\n");
		int cnt = 0;
		for(IrQuadPos pos: rootGroup.keySet()) {
			sb.append(cnt+"\n");
			for(IrQuadPos p: rootGroup.get(pos)) {
				sb.append(p.toString());
			}
			cnt ++;
		}
		return sb.toString();
		
	}
	
	
	
}
