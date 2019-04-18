package edu.mit.compilers.IR.IR_decl_Node;

import antlr.Token;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;

public class Variable_decl extends IrDeclaration{
	public IrType type;
	public boolean isArray = false;
	
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
	@Override
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type.toString();
	}
	
	public boolean isArray() {
		return isArray;
	}
	
	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
