package edu.mit.compilers.SymbolTables;

import java.util.HashMap;
import java.util.Iterator;

import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.IR_decl_Node.ArrayDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.utils.Util;

public class VariableTable extends SymbolTable<VariableTable, Variable_decl> {

	
	public int numbering = 0;
	public int currentSlotPosition = 0;
	public int wholeStackFrame = 0;
	public HashMap<String, Integer> variableSlot;

	public VariableTable() {
		super();
		variableSlot = new HashMap<>();
	}

	public VariableTable(VariableTable parent) {
		super(parent);
		if(parent != null && numbering != parent.numbering + 1)
			numbering = parent.numbering + 1;
		for(Variable_decl v: idList) {
			if(v.numbering != numbering)
				v.setNumbering(numbering);
		}
		variableSlot = new HashMap<>();
		computeWholeStackFrame();

	}
	

	@Override
	public void addParent(VariableTable parent) {
		this.parent = parent;
		if(numbering != parent.numbering+1)
			numbering = parent.numbering + 1;
		for(Variable_decl v: idList) {
			if(v.numbering != numbering)
				v.setNumbering(numbering);
		}
		computeWholeStackFrame();
	}

	public boolean containsVariable(String var) {
		if (table.containsKey(var))
			return true;
		else if (getParent() != null)
			return getParent().containsVariable(var);
		else
			return false;
	}

	public IrType getVariableType(String var) {
		if (!containsVariable(var)) {
			System.out.println("vtb contains: ?");
			System.out.println(this.toString());
			throw new IllegalArgumentException("the whole scope doesn't have var " + var);
			
		}
		return get(var).type;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Variable_decl v: idList) {
			sb.append(v.getName());
			sb.append(" ");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public Iterator<Variable_decl> iterator() {
		// TODO Auto-generated method stub
		return idList.iterator();
	}

	public Variable_decl[] toArray() {
		Variable_decl[] array = new Variable_decl[idList.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = idList.get(i);
		}
		return array;
	}
	
	@Override
	public void put(String id, Variable_decl v) {
		v.setNumbering(numbering);
		super.put(id, v);
	}

	@Override
	public void put(Variable_decl v) {
		// TODO Auto-generated method stub
		put(v.getId(), v);
		variableSlot.put(v.getId(), currentSlotPosition);
		if (v.type().equals(IrType.IntType)) {
			currentSlotPosition += 8;
		}
		else if (v.type().equals(IrType.BoolType)) {
			currentSlotPosition += 1;
		}
		else if (v.type().equals(IrType.IntArray)) {
			System.out.println(v.getName());
			ArrayDecl arr = (ArrayDecl) v;
			currentSlotPosition += 8 * arr.arraySize.getIntValue().intValue() + Util.ArrayHeaderSize;
		}
		else if (v.type().equals(IrType.boolArray)) {
			ArrayDecl arr = (ArrayDecl) v;
			currentSlotPosition += 1 * arr.arraySize.getIntValue().intValue() + Util.ArrayHeaderSize;
		}

		computeWholeStackFrame();
	}

	private void computeWholeStackFrame() {
		if (this.parent != null)
			wholeStackFrame = this.parent.wholeStackFrame + currentSlotPosition;
		else
			wholeStackFrame = currentSlotPosition;
	}

	public String getMemLocation(String id) {
		computeWholeStackFrame();
		int location = 0;
		if (variableSlot.containsKey(id) && this.parent != null) {
			location = this.variableSlot.get(id) - wholeStackFrame;
			return Integer.toString(location);
		} else {
			if (this.parent != null)
				return parent.getMemLocation(id);
			else
				return id;
		}

	}

	public boolean isGloblVariable(String id) {
		if (table.containsKey(id) && this.getParent() == null)
			// throw new IllegalArgumentException(id + " is global variable ");
			return true;
		else if (!table.containsKey(id) && this.getParent() != null)
			return this.getParent().isGloblVariable(id);
		return false;
	}
	
	public int getNumbering(String id) {
		Variable_decl decl = get(id);
		if(decl == null)
			return - 1;
		return decl.getNumbering();
	}

	public int getMemSize() {
		computeWholeStackFrame();
		int ret;
		if (this.parent != null) {
			ret = wholeStackFrame - this.parent.wholeStackFrame;
			// throw new IllegalArgumentException("alloc size is " + ret);
		} else
			ret = wholeStackFrame;
		if (ret <= 0)
			return 0;
		int remainder = ret % 16;
		if (remainder != 0)
			ret += 16 - remainder;
		return ret;
	}

	public VariableTable copy() {
		if (this.getParent() != null) {
			VariableTable v = new VariableTable();
			for (Variable_decl decl : this) {
				v.put((Variable_decl) decl.copy());
			}
			v.numbering = this.numbering;
			return v;
		} else {
			VariableTable v = new VariableTable();
			for(Variable_decl decl: this) {
				v.put(decl.getId(), (Variable_decl) decl.copy());
			}
			v.numbering = this.numbering;
			return v;
		}
	}

}
