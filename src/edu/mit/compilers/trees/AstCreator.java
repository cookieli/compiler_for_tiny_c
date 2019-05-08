package edu.mit.compilers.trees;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IrType;
import edu.mit.compilers.IR.IR_decl_Node.ArrayDecl;
import edu.mit.compilers.IR.IR_decl_Node.Import_decl;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.TernaryExpression;
import edu.mit.compilers.IR.expr.UnaryExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLenExpr;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.LoopStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrForBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrWhileBlock;
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
				if(id.isArrayForm()) {
					ParseTreeNode idNode = id.getFirstChild();
					//int size = Integer.parseInt(idNode.getRightSibling().getName());
					IrLiteral size ;
					size = new IrLiteral(idNode.getRightSibling(), ParseTreeNode.fileName);
					lst.add(new ArrayDecl(type, idNode.getToken(), size, ParseTreeNode.fileName));
				} else 
					lst.add(new Variable_decl(type,id.getToken(), ParseTreeNode.fileName));
				id = id.getRightSibling();
			}
			return lst;
		} else {
			throw new IllegalArgumentException("this node is not Variable_decl Node");
		}
	}
	
	public static List<Variable_decl> parseParameterForMethod(ParseTreeNode node){
		List<Variable_decl> lst = new ArrayList<>();
		if(node.isFuncArg()) {
			ParseTreeNode typeNode = node.getFirstChild();
			while(typeNode != null) {
				ParseTreeNode idNode = typeNode.getRightSibling();
				lst.add(new Variable_decl(typeNode.getToken(), idNode.getToken(), ParseTreeNode.fileName));
				typeNode = idNode.getRightSibling();
			}
			return lst;
		} else
			throw new IllegalArgumentException("this node is function_arg node");
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
				expr = getSpecificExpr(node);
			}
			return new IrAssignment(loc, expr, symbol);
		} else
			throw new IllegalArgumentException("the node isn't assignment");
		
	}
	
	private static IrAssignment parseAssignment(ParseTreeNode locNode, ParseTreeNode symbolNode) {
		IrLocation loc = parseLocation(locNode);
		String symbol = symbolNode.getName();
		IrExpression expr = null;
		if(!(symbolNode.getName().equals("++") || symbolNode.getName().equals("--"))) {
			expr = getSpecificExpr(symbolNode.getRightSibling());
		}
		return new IrAssignment(loc, expr, symbol);
	}
	
	public static IrLocation parseLocation(ParseTreeNode node) {
		if(node == null)
			System.out.println("the node is null");
		if(node.isLocation()) {
			if(node.isArrayMember())
				return new IrLocation(node, ParseTreeNode.fileName);
			else
				return new IrLocation(node.token, ParseTreeNode.fileName);
		}
		throw new IllegalArgumentException("the node isn't location node");
	}
	
	public static IrFuncInvocation parseFuncInvoke(ParseTreeNode node) {
		if(node.isFuncInvoke()) {
			ParseTreeNode funcNode = node.getFirstChild();
			IrFuncInvocation funcInvoke = new IrFuncInvocation(funcNode.getToken(), ParseTreeNode.fileName);
			funcInvoke.addFuncName(funcNode.getName());
			ParseTreeNode paraNode = funcNode.getRightSibling();
			if(paraNode != null &&paraNode.isFuncInvokeArg())
				paraNode = paraNode.getFirstChild();
			while(paraNode != null) {
				funcInvoke.addFuncArg(getSpecificExpr(paraNode));
				paraNode = paraNode.getRightSibling();
			}
			return funcInvoke;
		}
		throw new IllegalArgumentException("the node isn't funcInvoke node");
	}
	
	public static IrLenExpr parseLenExpr(ParseTreeNode node) {
		if(node.isLenExpr()) {
			Token opr = node.getFirstChild().getRightSibling().getToken();
			IrLenExpr len = new IrLenExpr(opr, ParseTreeNode.fileName);
			return len;
			
		}
		throw new IllegalArgumentException("the len expr can't work");
	}
	
	public static MethodDecl parseMethodDecl(ParseTreeNode node, VariableTable localVariableParent) {
		if(node.isMethodNode()) {
			Token type = node.getFirstChild().getToken();
			ParseTreeNode id = node.getFirstChild().getRightSibling();
			MethodDecl method = new MethodDecl(type, id.getToken(), ParseTreeNode.fileName);
			method.addLocalVarParent(localVariableParent);
			ParseTreeNode n = id.getRightSibling();
			if(n.isFuncArg()) {
				addFunctionArgForMethod(n, method);
			     n = n.getRightSibling();
			}
			addFunctionBodyForMethod(n, method);
			return method;
		}
		return null;
	}
	
	
	public static void addFunctionArgForMethod(ParseTreeNode n, MethodDecl method) {
		if(n.isFuncArg()) {
			method.addParameter(parseParameterForMethod(n));
		} else
			throw new IllegalArgumentException("the node isn't func arg def node");
	}
	
	public static void addFunctionBodyForMethod(ParseTreeNode n, MethodDecl method) {
		
		if(n.isVariableNode()) {//it means the method have nothing except for just a variable declariation
			method.addLocalVariable(parseVariableDecl(n));
		} else if(n.isFuncBody()) {
			ParseTreeNode node = n.getFirstChild();
			while(node != null && node.isVariableNode()) {
				method.addLocalVariable(parseVariableDecl(node));
				node = node.getRightSibling();
			}while(node != null && node.isIrStatement()) {
				
				method.addIrStatement(parseIrStatement(node, method.getVariableTable()));
				node = node.getRightSibling();
			}
		} else if(n.isIrStatement()) {
			method.addIrStatement(parseIrStatement(n, method.getVariableTable()));
		}
	}
	
	public static void  addFunctionBodyForBlock(ParseTreeNode n, IrBlock method, VariableTable parent) {
		if(n.isVariableNode()) {//it means the method have nothing except for just a variable declariation
			method.addLocalVariable(parseVariableDecl(n));
		} else if(n.isFuncBody()) {
			ParseTreeNode node = n.getFirstChild();
			while(node != null && node.isVariableNode()) {
				method.addLocalVariable(parseVariableDecl(node));
				node = node.getRightSibling();
			}while(node != null && node.isIrStatement()) {
				method.addIrStatement(parseIrStatement(node, parent));
				node = node.getRightSibling();
			}
		} else if(n.isIrStatement()) {
			method.addIrStatement(parseIrStatement(n, method.localVars));
		}
	}
	public static IrExpression getSpecificExpr(ParseTreeNode n) {
		if(n.isLiteral())
			return new IrLiteral(n, ParseTreeNode.fileName);
		else if(n.isLocation()) {
			return parseLocation(n);
		}
		else if(n.isExpr()) {
			return parseIrExpression(n);
		}else if(n.isFuncInvoke()) {
			return parseFuncInvoke(n);
		} else if(n.isLenExpr())
			return parseLenExpr(n);
		else if(n.isStringLiteral()) {
			return new IrLiteral(n, ParseTreeNode.fileName);
		}
		return null;
	}
	
	public static IrExpression parseIrExpression(ParseTreeNode n) {
		if(n.isBinaryExpr())
			return parseBinaryExpr(n);
		else if(n.isUnaryExpr()) {
			UnaryExpression temp =  parseUnaryExpr(n);
			if(temp.getSymbol().equals("-")) {
				if(temp.getIrExpression() instanceof IrLiteral) {
					IrLiteral literal = (IrLiteral) temp.getIrExpression();
					if(literal.getType().equals(new IrType(IrType.Type.INT))) {
						IrLiteral newLiteral = new IrLiteral(literal.getValue(), temp, literal.getType());
						newLiteral.setPositive(!literal.isPositive());
						return newLiteral;
					}
				}
			}
			return temp;
		} else if(n.isTernaryExpr()) {
			return parseTernaryExpression(n);
		}else
			return null;
	}
	
	
	
	public static BinaryExpression parseBinaryExpr(ParseTreeNode n) {
		if(n.isBinaryExpr()) {
			ParseTreeNode firstOperand = n.getFirstChild();
			ParseTreeNode symbolNode = firstOperand.getRightSibling();
			ParseTreeNode secondOperand = symbolNode.getRightSibling();
			BinaryExpression expr = new BinaryExpression(getSpecificExpr(firstOperand), getSpecificExpr(secondOperand), symbolNode.getName());
			symbolNode = secondOperand.getRightSibling();
			while(symbolNode != null) {
				secondOperand = symbolNode.getRightSibling();
				expr = new BinaryExpression(expr, getSpecificExpr(secondOperand), symbolNode.getName());
				symbolNode = secondOperand.getRightSibling();
			}
			return expr;
		}
		throw new IllegalArgumentException("the tree node isn't binary expression");
	}
	
	public static UnaryExpression parseUnaryExpr(ParseTreeNode n) {
		if(n.isUnaryExpr()) {
			String symbol = n.getFirstChild().getName();
			IrExpression expr = getSpecificExpr(n.getLastChild());
			return new UnaryExpression(symbol, expr);
		}
		throw new IllegalArgumentException("the tree node isn't unary expression");
	}
	
	public static TernaryExpression parseTernaryExpression(ParseTreeNode n) {
		if(n.isTernaryExpr()) {
			ParseTreeNode condNode = n.getFirstChild();
			ParseTreeNode firstExprNode = condNode.getRightSibling().getRightSibling();
			ParseTreeNode secondExprNode = firstExprNode.getRightSibling().getRightSibling();
			return new TernaryExpression(getSpecificExpr(condNode), getSpecificExpr(firstExprNode),getSpecificExpr(secondExprNode));
		} else
			throw new IllegalArgumentException("this node isn't ternaryExpr Node");
	}
	
	public static IfBlock parseIfBlock(ParseTreeNode n, VariableTable parent) {
		if(n.isIfBlock()) {
			ParseTreeNode boolNode = n.getFirstChild().getRightSibling();
			ParseTreeNode trueNode = boolNode.getRightSibling();
			ParseTreeNode falseNode = null;
			IrBlock falseBlock = null;
			if(trueNode.getRightSibling() != null) {
				falseNode = trueNode.getRightSibling().getRightSibling();
				falseBlock = parseIrBlock(falseNode, parent);
			}
			IrExpression boolExpr = getSpecificExpr(boolNode);
			IrBlock trueBlock = parseIrBlock(trueNode, parent);
		
			return new IfBlock(boolExpr, trueBlock, falseBlock);
		}
		throw new IllegalArgumentException("this node isn't block");
	}
	
	public static IrWhileBlock parseWhileBlock(ParseTreeNode n, VariableTable parent) {
		if(n.isWhileBlock()) {
			ParseTreeNode boolNode = n.getFirstChild().getRightSibling();
			ParseTreeNode bodyNode = boolNode.getRightSibling();
			IrExpression boolExpr = getSpecificExpr(boolNode);
			IrBlock body = parseIrBlock(bodyNode, parent);
			return new IrWhileBlock(boolExpr, body);
		}
		throw new IllegalArgumentException("this node isn't while block");
	}
	
	public static IrForBlock parseForBlock(ParseTreeNode n, VariableTable parent) {
		if(n.isForBlock()) {
			ParseTreeNode locNode = n.getFirstChild().getRightSibling();
			ParseTreeNode symbolNode = locNode.getRightSibling();
			IrAssignment initialAssign = parseAssignment(locNode, symbolNode);
			ParseTreeNode exprNode = symbolNode.getRightSibling().getRightSibling();
			IrExpression boolExpr = getSpecificExpr(exprNode);
			ParseTreeNode stepFuncLocNode = exprNode.getRightSibling();
			ParseTreeNode stepFuncSymbolNode = stepFuncLocNode.getRightSibling();
			IrAssignment stepFunction = parseAssignment(stepFuncLocNode, stepFuncSymbolNode);
			ParseTreeNode blockNode;
			if(stepFuncSymbolNode.getName().equals("++") || stepFuncSymbolNode.getName().equals("--")) {
				blockNode = stepFuncSymbolNode.getRightSibling();
			} else {
				blockNode = stepFuncSymbolNode.getRightSibling().getRightSibling();
			}
			IrBlock block = parseIrBlock(blockNode, parent);
			return new IrForBlock(initialAssign, boolExpr, stepFunction, block);
		}else
			throw new IllegalArgumentException("this node isn't For block node");
	}
	
	public static FuncInvokeStatement parseFuncInvocStatement(ParseTreeNode n) {
		if(n.isFuncInvoke()) {
			IrFuncInvocation func = parseFuncInvoke(n);
			return new FuncInvokeStatement(func);
		} else
			throw new IllegalArgumentException("it is not func invokeStatement");
	}
	
	public static IrBlock parseIrBlock(ParseTreeNode n, VariableTable parent) {
		IrBlock block = new IrBlock(parent);
		addFunctionBodyForBlock(n, block, block.localVars);
		return block;
	}
	
	public static Return_Assignment parseIrReturnAssignment(ParseTreeNode n) {
		if(n.isReturnAssignment()) {
			if(n.getName().equals("return"))
				return new Return_Assignment(n.getToken(), ParseTreeNode.fileName);
			else {
				IrExpression expr = getSpecificExpr(n.getLastChild());
				return new Return_Assignment(expr);
			}
		}else
			throw new IllegalArgumentException("this isn't return assignment");
	}
	
	public static LoopStatement parseLoopStatement(ParseTreeNode n) {
		if(n.isLoopStatement()) {
			return new LoopStatement(n, ParseTreeNode.fileName);
		}else
			throw new IllegalArgumentException("it isn't continue or break");
	}
	
	public static IrStatement parseIrStatement(ParseTreeNode n, VariableTable parent) {
		if(n.isAssignment())
			return parseAssignment(n);
		else if (n.isIfBlock())
			return parseIfBlock(n, parent);
		else if(n.isWhileBlock()) {
			return parseWhileBlock(n, parent);
		} else if(n.isForBlock())
			return parseForBlock(n, parent);
		else if(n.isFuncInvoke())
			return parseFuncInvocStatement(n);
		else if(n.isReturnAssignment())
			return parseIrReturnAssignment(n);
		else if(n.isLoopStatement())
			return parseLoopStatement(n);
		else
			throw new IllegalArgumentException("the node isn't parseTreeNode");
	}
	
}
