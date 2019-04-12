package edu.mit.compilers.trees;

import java.util.Stack;

import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;

/*
 * when we check some node,we need hold current environment,
 * think it as a stack when program holds
 * 
 * */
public class EnvStack {
	public Stack<VariableTable> variableScope;
	public Stack<MethodTable> methodScope;
	
	public EnvStack() {
		variableScope = new Stack<>();
		methodScope = new Stack<>();
	}
	
	public void pushVariables(VariableTable v) {
		variableScope.push(v);
	}
	
	public void pushMethods(MethodTable m) {
		methodScope.push(m);
	}
	
	
	public MethodTable popMethod() {
		return methodScope.pop();
	}
	public VariableTable popVariables() {
		return variableScope.pop();
	}
	
	public VariableTable peekVariables() {
		return variableScope.peek();
	}
	
	public MethodTable peekMethod() {
		return methodScope.peek();
	}
}
