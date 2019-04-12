package edu.mit.compilers.IR;

import antlr.Token;
import edu.mit.compilers.grammar.DecafParserTokenTypes;
import edu.mit.compilers.trees.ParseTreeNode;

public class IrType {
	public enum Type{
		INT,
		BOOL,
		VOID,
		UNSPECIFIED
	}
	
	private Type type = Type.UNSPECIFIED;
	public IrType() {}
	public IrType(Token t) {
		switch(t.getType()) {
		case DecafParserTokenTypes.BOOL:
			type = Type.BOOL;
			break;
		case DecafParserTokenTypes.INT:
			type = Type.INT;
			break;
		case DecafParserTokenTypes.VOID:
			type = Type.VOID;
			break;
		}
	}
	
	public IrType(ParseTreeNode node) {
		if(node.isBoolLiteral()) type = type.BOOL;
		else if(node.isNumLiteral()) type = type.INT;
		else
			throw new IllegalArgumentException("this node isn't literal node");
	}
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o == this)
			return true;
		if(!(o instanceof IrType))
			return false;
		IrType i = (IrType)o;
		if(this.type == i.type)
			return true;
		return false;
		
	}
	@Override
	public String toString() {
		return type.name();
	}
}
