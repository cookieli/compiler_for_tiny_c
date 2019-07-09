package edu.mit.compilers.IR.IR_decl_Node;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;

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
	
	private IrLocation getCorrespondLocation() {
		return new IrLocation(this.id);
	}
	
	public IrStatement getInitialAssign() {
		IrLocation loc = getCorrespondLocation();
		if(type.equals(IrType.IntType))
			return new IrAssignment(loc, IrLiteral.getLiteral(0), "=");
		else
			return new IrAssignment(loc, IrLiteral.getFalseLiteral(), "=");
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
	
	public IrType getIrType() {
		return type;
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
	
	public String getGloblAddr() {
		StringBuilder sb = new StringBuilder();
		sb.append(".comm");
		sb.append("\t");
		sb.append(getId());
		sb.append(",");
		if(type.equals(IrType.IntType))    sb.append("8, 8\n");
		else                               sb.append("1, 1\n");
		return sb.toString();
		
	}
	
	
	
}
