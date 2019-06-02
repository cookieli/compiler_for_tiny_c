package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.mit.compilers.IR.IR_decl_Node.Import_decl;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.SymbolTables.ImportTable;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.RODataArea;
import edu.mit.compilers.SymbolTables.VariableTable;

public class IrProgram extends IrNode{
	public ImportTable importIR;
	public VariableTable globalVariableTable;
	public MethodTable globalMethodTable;
	public RODataArea roData=null;
	public RODataArea getRoData() {
		return roData;
	}

	private void setRoData() {
		this.roData = new RODataArea();
	}
	
	public void addReadOnlyData(String var) {
		if(roData == null)
			setRoData();
		roData.addData(var);
	}

	//public String fileName;
	public IrProgram(String name) {
		importIR = new ImportTable();
		globalVariableTable = new VariableTable(null);
		globalMethodTable = new MethodTable();
		filename = name;
	}
	
	public IrProgram(IrProgram p) {
		this.setFilename(p.getFilename());
		importIR = p.importIR.copy();
		globalVariableTable = p.globalVariableTable.copy();
		globalMethodTable = new MethodTable();
		for(MethodDecl method: p.globalMethodTable) {
			MethodDecl newMethod = (MethodDecl) method.copy();
			newMethod.addLocalVarParent(globalVariableTable);
			globalMethodTable.put(newMethod);
		}
	}
	public VariableTable getGlobalVariableTable() {return globalVariableTable; }
	
	public MethodTable getGlobalMethodTable() {return globalMethodTable; }
	
	public void addImportIR(List<Import_decl> lst) {
		if(! lst.isEmpty()) {
			importIR = new ImportTable();
		}
		for(Import_decl i: lst) {
			addImportIR(i);
		}
	}
	public ImportTable getImportTable() {
		return importIR;
	}
	public void addImportIR(Import_decl ir) {
		if(importIR == null)
			importIR = new ImportTable();
		importIR.put(ir);
	}
	public void addGlobalVariable(List<Variable_decl> vas) {
		for(Variable_decl va: vas) {
			addGlobalVariable(va);
		}
	}
	public void addGlobalMethod(MethodDecl method) {
		if(globalMethodTable == null)
			globalMethodTable = new MethodTable();
		globalMethodTable.put(method);
	}
	public void addGlobalVariable(Variable_decl va) {
		AddGlobalVariable(va.getId(), va);
	}
	public void AddGlobalVariable(String name, Variable_decl va) {
		if(globalVariableTable == null)
			globalVariableTable = new VariableTable(null);
		globalVariableTable.put(name, va);
	}
	public String importNodesToString() {
		if(importIR == null)
			return "no import Nodes";
		StringBuilder sb = new StringBuilder();
		for(Import_decl i: importIR) {
			sb.append(i.getName());
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public String allVariableToString() {
		if(globalVariableTable == null)
			return "no variable defined";
		StringBuilder sb = new StringBuilder();
		for(Variable_decl v: globalVariableTable) {
			sb.append(v.getName());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String allMethodToString() {
		if(globalMethodTable == null)
			return "no methods defined";
		StringBuilder sb = new StringBuilder();
		for(MethodDecl m: globalMethodTable) {
			sb.append(m.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	@Override
	public String toString() {
		return "import Nodes:\n"+ importNodesToString() 
		+ "\n"+"all variables: \n"+ allVariableToString()
		+ "\n"+"all methods: \n" + allMethodToString();
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "program";
	}
	
	
	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		if(vistor.visit(this)) {
			System.out.println();
		};
		
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return new IrProgram(this);
	}
	
}
