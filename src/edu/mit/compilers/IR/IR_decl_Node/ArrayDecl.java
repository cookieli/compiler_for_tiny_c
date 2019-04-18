package edu.mit.compilers.IR.IR_decl_Node;

import antlr.Token;
import edu.mit.compilers.IR.IrType;

public class ArrayDecl extends Variable_decl{
	public int arraySize;
	
	public ArrayDecl(Token type, Token id, int size, String fileName) {
		super(type, id, fileName);
		this.arraySize = size;
		this.isArray = true;
		if(this.type.typeIs(IrType.Type.INT))
			this.type = new IrType(IrType.Type.INT_ARRAY);
		else if(this.type.typeIs(IrType.Type.BOOL))
			this.type = new IrType(IrType.Type.BOOL_ARRAY);
	}
	@Override
	public String getName() {
		return this.getType()+ " "+ this.id +"["+ arraySize + "]";
	}
	
	public int getSize() {
		return arraySize;
	}
	
	
	
}
