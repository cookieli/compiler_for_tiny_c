package edu.mit.compilers.IR.IR_decl_Node;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
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
	
	public Variable_decl(String id, IrType type) {
		this.id = id;
		this.type = new IrType(type.getType());
	}
	
	public Variable_decl(Variable_decl v) {
		super(v.getLineNumber(), v.getColumnNumber(), v.getFilename());
		this.type = new IrType(v.type.getType());
		this.isArray = v.isArray;
		this.id = v.getId();
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
	public IrType type() {
		return type;
	}
	
	public boolean isArray() {
		return isArray;
	}
	
	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		Variable_decl v = new Variable_decl(this);
		return v;
	}
	
	
	
}
