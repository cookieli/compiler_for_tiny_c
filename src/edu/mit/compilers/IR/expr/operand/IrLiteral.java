package edu.mit.compilers.IR.expr.operand;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.IrType.Type;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.UnaryExpression;
import edu.mit.compilers.trees.ParseTreeNode;

public class IrLiteral extends IrOperand{
	
	private IrType type;
	private String value;
	private boolean isPositive = true;
	public boolean isPositive() {
		return isPositive;
	}
	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}
	public IrLiteral(ParseTreeNode node, String filename) {
		super(node.getToken().getLine(), node.getToken().getColumn(), filename);
		//System.out.println(node.getName());
		value = node.getName();
		value = this.getValue();
		type = new IrType(node);
			
	}
	
	public IrLiteral() {
		
	}
	
	public IrLiteral(int v) {
		value = Integer.toString(v);
		type = new IrType(Type.INT);
	}
	
	public static IrLiteral getLiteral(int v) {
		return new IrLiteral(v);
	}
	
	public IrLiteral(IrLiteral l) {
		super(l.getLineNumber(), l.getColumnNumber(), l.getFilename());
		value = l.value;
		setPositive(l.isPositive);
		type = new IrType(l.type.getType());
	}
	public IrLiteral(String value, UnaryExpression expr, IrType type) {
		super(expr.getLineNumber(), expr.getColumnNumber(), expr.getFilename());
		this.value = value;
		this.type = type;
	}
	public BigInteger getIntValue() {
		BigInteger ret;
		if(value.length() > 2 &&value.charAt(0) == '0' && value.charAt(1)=='x') {
			//System.out.println("value: "+ value);
			ret = new BigInteger(value.substring(2), 16);
		}
		else if(value.length() == 3 && value.charAt(0) == '\'' && value.charAt(2) == '\'')
			ret = new BigInteger(value.substring(1, 2).getBytes());
		else
			ret =  new BigInteger(value);
		if(isPositive())
			return ret;
		return ret.negate();
	}
	public IrType getType() {
		return type;
	}
	@Override
	public String toString() {
		return getIntValue().toString();
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(type.toString() + " ");
		sb.append("Literal(");
		if(getType().equals(new IrType(Type.INT)))
			sb.append(getIntValue().toString());
		else
			sb.append(value);
		sb.append(")");
		return sb.toString();
	}
	
	public String getValue() {
		if(value.equals("true"))
			return "1";
		if(value.equals("false"))
			return "0";
		return value;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}
	
	public static IrLiteral getTrueLiteral() {
		IrLiteral l = new IrLiteral();
		l.type = IrType.BoolType;
		l.value = "1";
		return l;
	}
	
	public static IrLiteral getFalseLiteral() {
		IrLiteral l = new IrLiteral();
		l.type = IrType.BoolType;
		l.value = "0";
		return l;
	}
	@Override
	public List<IrOperand> operandList() {
		// TODO Auto-generated method stub
		List<IrOperand> lst = new ArrayList<>();
		lst.add(this);
		return lst;
	}
	
	public static void main(String args[]) {
		BigInteger b = new BigInteger("-2229223372036854775807");
		//BigInteger c = new BigInteger(, 16);
		//System.out.println(b.compareTo(c));
		int a = -0xFFFF;
		System.out.println(a);
		String s = "'b'";
		System.out.println(new BigInteger(s.substring(1, s.length()-1).getBytes()));
	}
	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		IrLiteral literal = new IrLiteral(this);
		return literal;
	}

}
