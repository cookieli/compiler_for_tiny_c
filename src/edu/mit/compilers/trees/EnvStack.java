package edu.mit.compilers.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.mit.compilers.SymbolTables.VariableTable;

/*
 * when we check some node,we need hold current environment,
 * think it as a stack when program holds
 * 
 * */
public class EnvStack {
	public Stack<VariableTable> variableScope;
	
	public EnvStack() {
		variableScope = new Stack<>();
	}
	
	public void pushVariables(VariableTable v) {
		variableScope.push(v);
	}
	
	public VariableTable popVariables() {
		return variableScope.pop();
	}
	
	public VariableTable peekVariables() {
		return variableScope.peek();
	}
}
