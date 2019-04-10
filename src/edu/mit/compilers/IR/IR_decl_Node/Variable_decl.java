package edu.mit.compilers.IR.IR_decl_Node;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;

public class Variable_decl extends IrDeclaration{
	public IrType type;
	
	public Variable_decl(Token type, Token id, String fileName) {
		super(id.getLine(), id.getColumn(),fileName);
		this.type = new IrType(type);
		this.id = id.getText();
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return id+ " "+ type.toString();
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type.toString();
	}
	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o == this)
			return true;
		if(! (o instanceof Variable_decl))
			return false;
		Variable_decl v = (Variable_decl)o;
		if(v.id.equals(id))
			return true;
		return false;
		
	}
	
}
