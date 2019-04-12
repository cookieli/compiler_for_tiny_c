package edu.mit.compilers.IR.IR_decl_Node;

import edu.mit.compilers.IR.IrNode;

public abstract class IrDeclaration extends IrNode implements Comparable<IrDeclaration>{
	String id;
	
	public IrDeclaration() {
		super();
	}
	
	public IrDeclaration(int lineNumber, int columnNumber, String filename) {
		super(lineNumber, columnNumber, filename);
	}
	public IrDeclaration(String name) {
		id = name;
	}
	
	@Override
	public String getName() {
		return id;
	}
	public String getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj == this)
			return true;
		if(!(obj instanceof IrDeclaration))
			return false;
		IrDeclaration i = (IrDeclaration)obj;
		if(i.getId().equals(id))
			return true;
		return false;
	}
	
	@Override
	public int compareTo(IrDeclaration o) {
		// TODO Auto-generated method stub
		if(o.getId().equals(this.getId()))
			return ((Integer)this.getLineNumber()).compareTo(o.getLineNumber());
		return getId().compareTo(o.getId());
	}
	
}
