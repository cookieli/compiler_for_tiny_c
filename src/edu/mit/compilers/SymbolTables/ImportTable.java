package edu.mit.compilers.SymbolTables;

import java.util.ArrayList;
import java.util.Iterator;

import edu.mit.compilers.IR.IR_decl_Node.Import_decl;

public class ImportTable extends SymbolTable<ImportTable, Import_decl>{
	
	public ImportTable() {
		super();
	}
	@Override
	public Iterator<Import_decl> iterator() {
		// TODO Auto-generated method stub
		return idList.iterator();
	}
	
	public Import_decl[] toArray() {
		Import_decl[] arr = new Import_decl[idList.size()];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = idList.get(i);
		}
		return arr;
	}
	@Override
	public void put(Import_decl v) {
		// TODO Auto-generated method stub
		super.put(v.getName(), v);
	}

}
