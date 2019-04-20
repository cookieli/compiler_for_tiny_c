package edu.mit.compilers.IR.IR_decl_Node;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.VariableTable;

public class MethodDecl extends Variable_decl{
	
	public List<IrStatement> statements;
	public VariableTable localVars;
	public List<String> paraList;
	
	public MethodDecl(Token type, Token id, String fileName) {
		this(type, id, fileName, null);
	}
	public MethodDecl(Token type, Token id, String fileName,VariableTable parent) {
		super(type, id, fileName);
		// TODO Auto-generated constructor stub
		
		paraList = new ArrayList<>();
		localVars = new VariableTable(parent);
		statements = new ArrayList<>();
	}
	
	public void addLocalVarParent(VariableTable parent) {
		localVars.addParent(parent);
	}
	public List<IrStatement> getStatements(){
		return statements;
	}
	public void addParameter(Variable_decl var) {
		paraList.add(var.getId());
		localVars.put(var);
	}
	
	public void addParameter(List<Variable_decl> lst) {
		for(Variable_decl v: lst)
			addParameter(v);
	}
	public void addIrStatement(IrStatement s) {
		statements.add(s);
	}
	
	public boolean isParameter(String e) {
		return paraList.contains(e);
	}
	
	public boolean isParameter(Variable_decl v) {
		return isParameter(v.getId());
	}
	public void addLocalVariable(List<Variable_decl> lst) {
		for(Variable_decl v: lst) {
			addLocalVariable(v);
		}
	}
	public void addLocalVariable(Variable_decl var) {
		localVars.put(var);
	}
	//in future we need a function body, but before that we need same basic things
	@Override
	public String getName() {
		StringBuilder sb = new StringBuilder();
		sb.append(type.toString() + " " + id + "(");
		for(String para: paraList) {
			if(isParameter(para)) {
				sb.append(localVars.get(para).getName());
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append("\n");
		sb.append("variables: ");
		for(Variable_decl v: localVars) {
			if(!isParameter(v)) {
				sb.append(v.getName());
				sb.append(",");
			}
		}
		sb.append("\n");
		sb.append("statements: \n");
		for(IrStatement s: statements) {
			
			sb.append(s.getName());
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	public IrType getMethodType() {
		return this.type;
	}
	
	public boolean hasParameter() {
		return !paraList.isEmpty();
	}
	
	public IrType getParameterType(int i) {
		if (i >= paraList.size())
			throw new IllegalArgumentException("the int "+ i+ " is out of parameter list");
		return getParameterType(paraList.get(i));
		
	}
	public int getParameterSize() {
		return paraList.size();
	}
	
	public IrType getParameterType(String para) {
		return localVars.getVariableType(para);
	}
	
	@Override
	public void accept(IrNodeVistor vistor) {
		vistor.visit(this);
	}
	
}
