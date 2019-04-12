package edu.mit.compilers.trees;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IR_decl_Node.Import_decl;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.grammar.DecafParserTokenTypes;

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
			
			while(node != null && node.isMethodNode()) {
				program.addGlobalMethod(parseMethodDecl(node, program.globalVariableTable));
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
			program.addGlobalMethod(parseMethodDecl(tree, program.globalVariableTable));
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
	
	public static IrAssignment parseAssignment(ParseTreeNode node) {
		IrLocation loc;
		String symbol;
		IrExpression expr = null;
		if(node.isAssignment()) {
			loc = parseLocation(node.getFirstChild());
			node = node.getFirstChild().getRightSibling();
			symbol = node.getName();
			if(node.getRightSibling() != null) {
				node = node.getRightSibling();
				if(node.isLiteral()) {
					expr = new IrLiteral(node);
					//return new IrAssignment(loc, expr, symbol);
				} else if(node.isLocation())
					expr = parseLocation(node);
			}
			return new IrAssignment(loc, expr, symbol);
		} else
			throw new IllegalArgumentException("the node isn't assignment");
		
	}
	
	public static IrLocation parseLocation(ParseTreeNode node) {
		if(node.isLocation())
			return new IrLocation(node.token, ParseTreeNode.fileName);
		throw new IllegalArgumentException("the node isn't location node");
	}
	
	public static MethodDecl parseMethodDecl(ParseTreeNode node, VariableTable localVariableParent) {
		if(node.isMethodNode()) {
			Token type = node.getFirstChild().getToken();
			ParseTreeNode id = node.getFirstChild().getRightSibling();
			MethodDecl method = new MethodDecl(type, id.getToken(), ParseTreeNode.fileName);
			method.addLocalVarParent(localVariableParent);
			ParseTreeNode n = id.getRightSibling();
			addFunctionBodyForMethod(n, method);
			return method;
		}
		return null;
	}
	public static void addFunctionBodyForMethod(ParseTreeNode n, MethodDecl method) {
		if(n.isVariableNode()) {
			method.addLocalVariable(parseVariableDecl(n));
		} else if(n.isFuncBody()) {
			ParseTreeNode node = n.getFirstChild();
			while(node != null && node.isVariableNode()) {
				method.addLocalVariable(parseVariableDecl(node));
				node = node.getRightSibling();
			}while(node != null && node.isAssignment()) {
				method.addIrStatement(parseAssignment(node));
				node = node.getRightSibling();
			}
		} else if(n.isAssignment()) {
			method.addIrStatement(parseAssignment(n));
		}
	}
	
	
}
