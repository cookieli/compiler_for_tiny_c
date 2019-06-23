package edu.mit.compilers.IR.statement.codeBlock;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.SymbolTables.VariableTable;

public class IrBlock extends IrStatement {
	public List<IrStatement> statements;
	public VariableTable localVars;
	
	public IrBlock() {
		statements = new ArrayList<>();
		localVars = new VariableTable();
	}
	public IrBlock(VariableTable v) {
		this();
		localVars.addParent(v);
	}
	
	public boolean haveNoStatements() {
		return statements.isEmpty();
	}
	
	public VariableTable getLocalVar() {
		return localVars;
	}
	
	public IrBlock(IrBlock b) {
		localVars = b.localVars.copy();
		statements = new ArrayList<>();
		for(IrStatement s: b.statements) {
			statements.add((IrStatement) s.copy());
		}
		
		for(IrStatement s: statements) {
			s.setLocalVarTableParent(this.localVars);
		}
	}
	
	public void addLocalVarParent(VariableTable parent) {
		localVars.addParent(parent);
	}
	
	public List<IrStatement> getStatements(){
		return statements;
	}
	
	public void addLocalVariable(List<Variable_decl> lst) {
		for(Variable_decl v: lst) {
			addLocalVariable(v);
		}
	}
	
	public void addLocalVariable(Variable_decl var) {
		localVars.put(var);
	}
	
	public void addIrStatement(IrStatement s) {
		statements.add(s);
	}
	
	@Override
	public String getName() {
		StringBuilder sb = new StringBuilder();
		sb.append("variables: ");
		for(Variable_decl v: localVars) {

				sb.append(v.getName());
				sb.append(",");
			
		}
		sb.append("\n");
		sb.append("statements: \n");
		for(IrStatement s: statements) {
			
			sb.append(s.getName());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	@Override
	public void accept(IrNodeVistor vistor) {
		vistor.visit(this);
	}
	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new IrBlock(this);
	}
	
	@Override
	public void setLocalVarTableParent(VariableTable v) {
		addLocalVarParent(v);
	}
}
