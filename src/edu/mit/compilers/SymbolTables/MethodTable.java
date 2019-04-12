package edu.mit.compilers.SymbolTables;

import java.util.Iterator;

import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;

public class MethodTable extends SymbolTable<MethodTable, MethodDecl>{

	@Override
	public Iterator<MethodDecl> iterator() {
		// TODO Auto-generated method stub
		return idList.iterator();
	}

	@Override
	public void put(MethodDecl v) {
		// TODO Auto-generated method stub
		put(v.getId(), v);
	}
	
	public MethodDecl[] toArray() {
		MethodDecl[] res = new MethodDecl[idList.size()];
		for(int i = 0; i < res.length; i++) {
			res[i] = idList.get(i);
		}
		return res;
	}
}
