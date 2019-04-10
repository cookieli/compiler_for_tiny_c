package edu.mit.compilers.IR;

import antlr.Token;
import edu.mit.compilers.grammar.DecafParserTokenTypes;

public class IrType {
	public enum Type{
		INT,
		BOOL,
		VOID,
		UNSPECIFIED
	}
	
	private Type type = Type.UNSPECIFIED;
	
	public IrType(Token t) {
		switch(t.getType()) {
		case DecafParserTokenTypes.BOOL:
			type = Type.BOOL;
			break;
		case DecafParserTokenTypes.INT:
			type = Type.INT;
			break;
		case DecafParserTokenTypes.VOID:
			type = Type.BOOL;
			break;
		}
	}
	@Override
	public String toString() {
		return type.name();
	}
}
