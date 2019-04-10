package edu.mit.compilers.trees;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IR_decl_Node.Import_decl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;

public class AstCreator {
	public static IrProgram parseProgram(ParseTreeNode tree,String inFile) {
		IrProgram program = new IrProgram(inFile);
		if(tree.getName().equals("program")) {
			ParseTreeNode node = tree.getFirstChild();
			while(node != null && node.isImportNode()) {
				program.addImportIR(parseImportDecl(node));
				node = node.rightSibling;
			}
			while(node != null && node.isVariableNode()) {
				program.addGlobalVariable(parseVariableDecl(node));
				node = node.rightSibling;
			}
			
		} else if(tree.getName().equals("import_decl")) {
			//System.out.println(tree.getTokenText());
			System.out.println("the program only has one import_decl");
			program.addImportIR(parseImportDecl(tree));
			
		} else if(tree.getName().equals("variable_declaration")) {
			//System.out.println(tree.getTokenText());
			program.addGlobalVariable(parseVariableDecl(tree));
		} else if(tree.getName().equals("func_def")) {
			
		}
		return program;
	}
	public static Import_decl parseImportDecl(ParseTreeNode node) {
		if(node.isImportNode()) {
			return (new Import_decl(node.getLastChild().getToken(), ParseTreeNode.fileName));
		}else 
			throw new IllegalArgumentException("the argument is not importNode");
	}
	public static List<Variable_decl> parseVariableDecl(ParseTreeNode node) {
		List<Variable_decl> lst = new ArrayList<>();
		if(node.isVariableNode()) {
			Token type = node.getFirstChild().getToken();
			ParseTreeNode id = node.getFirstChild().getRightSibling();
			while(id != null) {
				lst.add(new Variable_decl(type,id.getToken(), ParseTreeNode.fileName));
				id = id.getRightSibling();
			}
			return lst;
		} else {
			throw new IllegalArgumentException("this node is not Variable_decl Node");
		}
	}
}
