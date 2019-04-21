package edu.mit.compilers.trees;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import antlr.Token;
import edu.mit.compilers.grammar.DecafParserTokenTypes;

public class ParseTreeNode {
	public static String fileName = null;
	
	
	public Token token;
	public String NodeName;
	public ParseTreeNode firstChild;
	public ParseTreeNode lastChild;
	public ParseTreeNode leftSibling;
	public ParseTreeNode rightSibling;

	public ParseTreeNode parent;
	
	public static final String filter[] = { "[" , "]", ";", "(", ")" ,"{","}", ","};
	public ParseTreeNode() {
		this("unknown");
	}
	
	public static void setFileName(String name) {
		if(fileName == null) 
			fileName = name;
		else
			System.out.println("the tree file name have been set");
	}
	public ParseTreeNode(String name) {
		this(name, null);
	}

	public ParseTreeNode(String name, ParseTreeNode p) {
		NodeName = name;
		parent = p;
		firstChild = null;
		lastChild = null;
		leftSibling = null;
		rightSibling = null;
		token = null;
	}

	public String getFileName() {
		return fileName;
	}


	public ParseTreeNode(Token token, ParseTreeNode p) {
		this(token.getText(), p);
		this.token = token;
	}
	
	public boolean isImportNode() {
		return NodeName.equals("import_decl");
	}
	
	public boolean isVariableNode() {
		return NodeName.equals("variable_declaration") || NodeName.equals("array_form");
	}
	
	public boolean isArrayForm() {
		return NodeName.equals("array_form");
	}
	public boolean isMethodNode() {
		return NodeName.equals("func_def");
	}
	public boolean isAssignment() {
		return NodeName.equals("assignment");
	}
	
	public boolean isIfBlock() {
		return NodeName.equals("if_block");
	}
	
	public boolean isWhileBlock() {
		return NodeName.equals("while_block");
	}
	
	public boolean isForBlock() {
		return NodeName.equals("for_block");
	}
	
	public boolean isReturnAssignment() {
		return NodeName.contains("return");
	}
	
	public boolean isLoopStatement() {
		return NodeName.equals("continue") || NodeName.equals("break");
	}
	
	public boolean isIrStatement() {
		return isAssignment() || isIfBlock() || isWhileBlock() || isForBlock() || isFuncInvoke() || isReturnAssignment() || isLoopStatement();
	}
	
	public boolean isLiteral() {
		if(getToken() == null) return false;
		return isBoolLiteral() || isNumLiteral();
	}
	
	public boolean isBoolLiteral() {
		return getToken().getType() == DecafParserTokenTypes.FALSE || getToken().getType() == DecafParserTokenTypes.TRUE;
	}
	
	public boolean isFuncInvoke() {
		return NodeName.equals("func_invoc");
	}
	
	public boolean isNumLiteral() {
		return isIntLiteral()|| isCharLiteral();
	}
	private boolean isIntLiteral() {
		return getToken().getType() == DecafParserTokenTypes.INT_LITERAL;        
	}
	private boolean isCharLiteral() {
		return getToken().getType() == DecafParserTokenTypes.CHAR_LITERAL;
	}
	public boolean isFuncBody() {
		return NodeName.equals("function_body");
	}
	public boolean isFuncArg() {
		return NodeName.equals("func_def_arg");
	}
	
	public boolean isFuncInvokeArg() {
		return NodeName.equals("func_arg");
	}
	public boolean isLocation() {
		if(!isArrayMember()) {
			if(getToken() == null)  return false;
			return getToken().getType() == DecafParserTokenTypes.ID;
		}
		return true;
	}
	
	public boolean isLenExpr() {
		if(NodeName.equals("operand"))
			if(this.getFirstChild() != null && (this.getFirstChild().getToken().getType() == DecafParserTokenTypes.LEN))
				if(this.getFirstChild().getRightSibling() != null)
					return true;
		return false;
				
	}
	
	public boolean isArrayMember() {
		return NodeName.equals("array_member");
	}
	
	public boolean isUnaryExpr() {
		return NodeName.equals("expr1");
	}
	
	public boolean isTernaryExpr() {
		return NodeName.equals("expr7");
	}
	
	public boolean isBinaryExpr() {
		return isExpr() && (!isUnaryExpr()) && (!isTernaryExpr());
	}
	
	public boolean isExpr() {
		return NodeName.contains("expr");
	}
	public String getName() {
		return this.NodeName;
	}
	
	public Token getToken() {
		return token;
	}
	public String getTokenText() {
		if(token == null) 
			return "the token is null";
		else
			return token.getText();
	}
	public ParseTreeNode getParent() {
		return parent;
	}

	public ParseTreeNode getFirstChild() {
		return firstChild;
	}

	public ParseTreeNode getLastChild() {
		return lastChild;
	}

	public ParseTreeNode getleftSibling() {
		return leftSibling;
	}

	public ParseTreeNode getRightSibling() {
		return rightSibling;
	}

	public boolean isRoot() {
		return parent == null;
	}

	/*
	 * there are two different add child happens first when trainIn, the traceIn
	 * parameter is string the production, we need to flow to the new child,and add
	 * name for the new child second when match, this time we have token,we need add
	 */
	public ParseTreeNode addChild(String name) {
		ParseTreeNode newNode = new ParseTreeNode(name, this);
		return addChild(newNode);
	}

	public ParseTreeNode addChild(Token token) {
		ParseTreeNode newNode = new ParseTreeNode(token, this);
		return addChild(newNode);
	}

	public ParseTreeNode addChild(ParseTreeNode node) {
		if (firstChild == null) {
			firstChild = node;
		} else {
			node.leftSibling = lastChild;
			lastChild.rightSibling = node;
		}
		lastChild = node;
		return lastChild;
	}

	private boolean isFirstChild() {
		return leftSibling == null;
	}

	private boolean isLastChild() {
		return rightSibling == null;
	}

	public boolean deleteTreeNode(String[] notUseful, ParseTreeNode node) {
		if (Arrays.asList(notUseful).contains(node.NodeName)) {
			deleteTreeNode(node);
			return true;
		}
		return false;
	}
	
	public static void filterChild(String[] notUseful, ParseTreeNode node) {
		ParseTreeNode n = node.getFirstChild();
		while(n != null) {
			ParseTreeNode right = n.getRightSibling();
			node.deleteTreeNode(notUseful, n);
			n = right;
		}
	}
	public void deleteTreeNode(ParseTreeNode child) {
		if (child.isRoot())
			return;
		if (child.isFirstChild() && child.isLastChild()) {
			child.parent.firstChild = null;
			child.parent.lastChild = null;
		} else if (child.isFirstChild()) {
			child.parent.firstChild = child.rightSibling;
			child.rightSibling.leftSibling = null;
			child.parent = null;
		} else if (child.isLastChild()) {
			child.parent.lastChild = child.leftSibling;
			child.leftSibling.rightSibling = null;
			child.parent = null;
		} else {
			ParseTreeNode left = child.leftSibling;
			ParseTreeNode right = child.rightSibling;
			left.rightSibling = right;
			right.leftSibling = left;
			child.leftSibling = null;
			child.rightSibling = null;
			child.parent = null;
		}
	}

	/*
	 * this happens to match we don't need to flow to the child
	 */
	public void addNode(Token token) {
		addChild(token);
	}

	public static void setSiblingWithLeft(ParseTreeNode left, ParseTreeNode node) {
		left.rightSibling = node;
		node.leftSibling = left;
	}

	public static void setSiblingWithRight(ParseTreeNode right, ParseTreeNode node) {
		right.leftSibling = node;
		node.rightSibling = right;
	}

	/*
	 * parameter: the node we need to compress return value: the node we have
	 * already been compressed;
	 * 
	 */
	public static ParseTreeNode compressTreeNode(String[] filter, ParseTreeNode treeNode) {
		//String[] filter = { ",", ";", "(", ")" ,"{","}"};
		ParseTreeNode node = treeNode;
		if (node == null)
			return null;
		filterChild(filter, node);
		ParseTreeNode parent = node.parent;
		while (node.getFirstChild() != null && (node.getFirstChild() == node.getLastChild()) && (!node.isFuncInvoke())) {
			ParseTreeNode childNode = node.getFirstChild();
			childNode.parent = node.parent;
			if (node.getleftSibling() == null && node.getRightSibling() == null) {
				// it is parent's only node
				if (!node.isRoot()) {
					node.parent.firstChild = childNode;
					node.parent.lastChild = childNode;
				}
			} else if (node.getleftSibling() == null) {
				// it is parent's first node
				ParseTreeNode rightSibling = node.getRightSibling();
				setSiblingWithRight(rightSibling, childNode);
				node.parent.firstChild = childNode;
			} else if (node.getRightSibling() == null) {
				// it is parent's last node
				ParseTreeNode leftSibling = node.getleftSibling();
				setSiblingWithLeft(leftSibling, childNode);
				node.parent.lastChild = childNode;
			} else {
				ParseTreeNode leftSibling = node.getleftSibling();
				ParseTreeNode rightSibling = node.getRightSibling();
				setSiblingWithLeft(leftSibling, childNode);
				setSiblingWithRight(rightSibling, childNode);

			}
			node = childNode;
			filterChild(filter, node);
		}
		return node;
	}

	public static ParseTreeNode compressTree(ParseTreeNode tree) {
		//String[] filter = { "[" , "]", ";", "(", ")" ,"{","}"};
		//filterChild(filter, tree);
		ParseTreeNode root = compressTreeNode(filter, tree);
		Queue<ParseTreeNode> q = new LinkedList<>();
		q.add(root);
		while (!q.isEmpty()) {
			ParseTreeNode n = q.poll();
			//filterChild(filter, n);
			n = compressTreeNode(filter, n);
			filterChildNodeToQueue(filter, n, q);
		}
		return root;
	}

	private static void filterChildNodeToQueue(String[] filter, ParseTreeNode n, Queue<ParseTreeNode> q) {
		if (n.getFirstChild() != null) {
			ParseTreeNode child = n.getFirstChild();
			if (!n.deleteTreeNode(filter, child)) {
				q.add(child);
				ParseTreeNode rightSibling = child.getRightSibling();
				while (rightSibling != null) {
					ParseTreeNode right = rightSibling.getRightSibling();
					if (!n.deleteTreeNode(filter, rightSibling)) 
						q.add(rightSibling);
					rightSibling = right;
				}
			}
		}
	}

	private static void addNodeChildToQueue(ParseTreeNode n, Queue<ParseTreeNode> q) {
		if (n.getFirstChild() != null) {
			ParseTreeNode child = n.getFirstChild();
			q.add(child);
			while (child.getRightSibling() != null) {
				child = child.getRightSibling();
				q.add(child);
			}
		}
	}

	public void printTree() {
		Queue<ParseTreeNode> q = new LinkedList<>();
		q.add(this);
		q.add(new ParseTreeNode("\n"));
		while(!q.isEmpty()) {
			ParseTreeNode n = q.poll();
			if(n.NodeName == "\n") {
				System.out.println();
				if(!q.isEmpty())
					q.add(new ParseTreeNode("\n"));
			} else {
				System.out.print(n.NodeName+ " ");
				if(n.getFirstChild() != null) {
					ParseTreeNode child = n.getFirstChild();
					q.add(child);
					while(child.getRightSibling() != null) {
						child = child.getRightSibling();
						q.add(child);
					}
				}
			}
		}
	}
	public static ParseTreeNode root() {
		return new ParseTreeNode("_PROGRAM_START");
	}

	public static void main(String[] args) {
		ParseTreeNode a = new ParseTreeNode("func_def");
		ParseTreeNode b = a.addChild("void");
		ParseTreeNode c = a.addChild("main");
		a.addChild("(");
		a.addChild(")");
		a.addChild("{");
		ParseTreeNode e = a.addChild("statement");
		a.addChild("}");
		ParseTreeNode assign = e.addChild("assignment");
		e.addChild(";");
		assign.addChild("a");
		assign.addChild("=");
		assign.addChild("b");
		a.printTree();
		System.out.println("-------------------");
		ParseTreeNode tree = ParseTreeNode.compressTree(a);
		tree.printTree();
		
	}

}
