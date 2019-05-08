package edu.mit.compilers.IR;

import antlr.Token;
import edu.mit.compilers.grammar.DecafParserTokenTypes;
import edu.mit.compilers.trees.ParseTreeNode;

public class IrType {
	public enum Type{
		INT,
		BOOL,
		VOID,
		INT_ARRAY,
		BOOL_ARRAY,
		STRING_LITERAL,
		UNSPECIFIED,
		NOTKNOWN
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
	public IrType(Type t) {
		type = t;
	}
	
	public IrType(ParseTreeNode node) {
		if(node.isBoolLiteral()) type = Type.BOOL;
		else if(node.isNumLiteral()) type = Type.INT;
		else if(node.isStringLiteral()) type = Type.STRING_LITERAL;
		else
			throw new IllegalArgumentException("this node isn't literal node");
	}
	public boolean isNotKnownType() {
		return type ==Type.NOTKNOWN;
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
		if(this.type == Type.NOTKNOWN || i.type == Type.NOTKNOWN)
			return true;
		return false;
		
	}
	
	public boolean typeIs(Type t) {
		return type == t;
	}
	@Override
	public String toString() {
		return type.name();
	}
	public Type getType() {
		return type;
	}
}
