package edu.mit.compilers.IR.IR_decl_Node;

import java.math.BigInteger;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.expr.operand.IrLiteral;

public class ArrayDecl extends Variable_decl{
	public IrLiteral arraySize;
	public ArrayDecl(Token type, Token id, IrLiteral size, String fileName) {
		super(type, id, fileName);
		this.arraySize = size;
		this.isArray = true;
		if(this.type.typeIs(IrType.Type.INT))
			this.type = new IrType(IrType.Type.INT_ARRAY);
		else if(this.type.typeIs(IrType.Type.BOOL))
			this.type = new IrType(IrType.Type.BOOL_ARRAY);
	}
	
	public ArrayDecl(ArrayDecl arr) {
		super(arr);
		this.arraySize = (IrLiteral) arr.arraySize.copy();
	}
	@Override
	public String getName() {
		return  this.id +"["+ arraySize + "]"+ " "+this.getType();
	}
	
	public String getSize() {
		return arraySize.toString();
	}
	
	@Override
	public IrNode copy() {
		ArrayDecl arr = new ArrayDecl(this);
		return arr;
		
	}
	
	
	
}
