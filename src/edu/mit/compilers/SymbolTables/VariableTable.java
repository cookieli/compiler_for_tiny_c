package edu.mit.compilers.SymbolTables;

import java.util.Iterator;

import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;

public class VariableTable extends SymbolTable<VariableTable, Variable_decl>{
	
	public VariableTable() {
		super();
	}
	public VariableTable(VariableTable parent) {
		super(parent);
	}
	@Override
	public Iterator<Variable_decl> iterator() {
		// TODO Auto-generated method stub
		return idList.iterator();
	}
	public Variable_decl[] toArrray() {
		Variable_decl[] array = new Variable_decl[idList.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = idList.get(i);
		}
		return array;
	}
	@Override
	public void put(Variable_decl v) {
		// TODO Auto-generated method stub
		super.put(v.getId(), v);
	}
	
	
}
