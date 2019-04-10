package edu.mit.compilers.IR.IR_decl_Node;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;

public class Import_decl extends IrDeclaration{
	
	
	
	public Import_decl(Token id, String filename) {
		super(id.getLine(), id.getColumn(), filename);
		this.id = id.getText();
	}
	public Import_decl(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}
}
