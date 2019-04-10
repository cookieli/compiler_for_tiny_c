package edu.mit.compilers.trees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IR_decl_Node.Import_decl;
import edu.mit.compilers.IR.IR_decl_Node.IrDeclaration;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.SymbolTables.ImportTable;
import edu.mit.compilers.SymbolTables.VariableTable;

public class SemanticCheckerNode implements IrNodeVistor{
	public EnvStack envs;
	public boolean hasError;
	public StringBuilder errorMessage;
	public SemanticCheckerNode() {
		envs = new EnvStack();
		hasError = false;
		errorMessage = new StringBuilder();
	}
	@Override
	public boolean visit(IrProgram p) {
		// TODO Auto-generated method stub
		envs.pushVariables(p.globalVariableTable);
		//checkVariableList(envs.peekVariables());
		checkReDeclariationErr(envs.peekVariables().toArrray());
		checkReDeclariationErr(p.importIR.toArray());
		if(hasError)
			System.out.println(errorMessage.toString());
		
		envs.popVariables();
		return hasError;
	}
	
	
	public void checkReDeclariationErr(IrDeclaration[] decls) {
		Arrays.sort(decls);
		for(int i = 1; i < decls.length; i++) {
			if(decls[i].equals(decls[i-1])) {
				if(hasError == false)  hasError = true;
				errorMessage.append(ErrorReport.redeclariationError(decls[i], decls[i-1]));
			}
		}
	}
	
}
