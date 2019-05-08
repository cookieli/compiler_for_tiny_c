package edu.mit.compilers.SymbolTables;

import java.util.Iterator;

import edu.mit.compilers.IR.IrType;
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
	
	public boolean containsMethod(String m) {
		if(table.containsKey(m))     return true;
		else if(getParent() != null) return getParent().containsMethod(m);
		else                         return false;
	}
	
	public boolean canBeInvoked(String cur, String invoked) {
		if(table.containsKey(cur) && table.containsKey(invoked)) {
			if(idList.indexOf(table.get(cur)) >= idList.indexOf(table.get(invoked)))
				return true;
		}
		return false;
	}
	
	public boolean lastMethodIsMain() {
		if(idList.size() == 0) {
			return false;
		}
		MethodDecl method = idList.get(idList.size()-1);
		//System.out.println("method: "+method);
		if(method == null)   return false;
		if(method.getId().equals("main")) {
			return true;
		}
		return false;
	}
	
	public IrType getMethodType(String m) {
		if(!containsMethod(m))
			throw new IllegalArgumentException("we can't have method");
		return table.get(m).getMethodType();
	}
	
	public MethodDecl getLastMethod() {
		return idList.get(idList.size() - 1);
	}
	
}
