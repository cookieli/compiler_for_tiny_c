package edu.mit.compilers.SymbolTables;

import java.util.HashMap;
import java.util.Iterator;

import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.IR_decl_Node.ArrayDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.utils.Util;

public class VariableTable extends SymbolTable<VariableTable, Variable_decl>{
	
	public int currentSlotPosition = 0;
	public HashMap<String, Integer> variableSlot;
	public VariableTable() {
		super();
		variableSlot = new HashMap<>();
	}
	public VariableTable(VariableTable parent) {
		super(parent);
		variableSlot = new HashMap<>();
	}
	
	public boolean containsVariable(String var) {
		if(table.containsKey(var))   return true;
		else if(getParent() != null) return getParent().containsVariable(var);
		else                         return false;
	}
	
	public IrType getVariableType(String var) {
		if(!containsVariable(var)) 
			throw new IllegalArgumentException("the whole scope doesn't have var "+ var);
		return get(var).type;
	}
	@Override
	public Iterator<Variable_decl> iterator() {
		// TODO Auto-generated method stub
		return idList.iterator();
	}
	public Variable_decl[] toArray() {
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
		variableSlot.put(v.getId(), currentSlotPosition);
		if(v.type().equals(IrType.IntType)) {
			currentSlotPosition += 8;
		}
		if(v.type().equals(IrType.BoolType)) {
			currentSlotPosition += 1;
		}
		if(v.type().equals(IrType.IntArray)) {
			ArrayDecl arr = (ArrayDecl)v;
			currentSlotPosition += 8 * arr.arraySize.getIntValue().intValue()+ Util.ArrayHeaderSize;
		}
		if(v.type().equals(IrType.boolArray)) {
			ArrayDecl arr = (ArrayDecl)v;
			currentSlotPosition += 1 * arr.arraySize.getIntValue().intValue() + Util.ArrayHeaderSize;
		}
	}
	
	public String getMemLocation(String id) {
		int location = 0;
		if(variableSlot.containsKey(id) && this.parent != null) {
			location = this.variableSlot.get(id) - currentSlotPosition;
			return Integer.toString(location);
		}
		else {
			if(this.parent != null)
				return parent.getMemLocation(id);
			else
				return id;
		}
		
	}
	
	public boolean isGloblVariable(String id) {
		if(variableSlot.containsKey(id) && this.getParent() == null) 
			//throw new IllegalArgumentException(id + " is global variable ");
			return true;
		else if(!variableSlot.containsKey(id) && this.getParent() != null)
			return this.getParent().isGloblVariable(id);
		return false;
	}
	
	public int getMemSize() {
		
		 int ret = currentSlotPosition; 
		 int remainder = currentSlotPosition % 16;
		  if(remainder != 0) 
		  { ret += 16 - remainder; }
		  return ret;
	}
	
	public VariableTable copy() {
		VariableTable v = new VariableTable();
		for(Variable_decl decl: this) {
			v.put((Variable_decl) decl.copy());
		}
//		v.currentSlotPosition = this.currentSlotPosition;
//		v.variableSlot = new HashMap<>();
//		for(String key: this.variableSlot.keySet()) {
//			v.variableSlot.put(key, this.variableSlot.get(key));
//		}
		return v;
	}
	
	
	
	
	
	
}
