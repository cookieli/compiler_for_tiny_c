package edu.mit.compilers;

import java.io.*;
import java.util.HashMap;

import antlr.CharStreamException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStreamException;
import edu.mit.compilers.CFG.AssemblyFromCFGVistor;
import edu.mit.compilers.CFG.CFG;
import edu.mit.compilers.CFG.cfgNodeVistor;
import edu.mit.compilers.IR.BoundCheckVistor;
import edu.mit.compilers.IR.IrProgram;
import edu.mit.compilers.IR.IrQuadVistor;
import edu.mit.compilers.IR.IrResolveNameToLocationVistor;
import edu.mit.compilers.IR.IrWithTemp;
import edu.mit.compilers.grammar.*;
import edu.mit.compilers.tools.CLI;
import edu.mit.compilers.tools.CLI.Action;
import edu.mit.compilers.trees.AstCreator;
import edu.mit.compilers.trees.ParseTreeNode;
import edu.mit.compilers.trees.SemanticCheckerNode;

class Main {
	public static void main(String[] args) {
		try {
			CLI.parse(args, new String[0]);
			InputStream inputStream = args.length == 0 ? System.in : new java.io.FileInputStream(CLI.infile);
			PrintStream outputStream = CLI.outfile == null ? System.out
					: new java.io.PrintStream(new java.io.FileOutputStream(CLI.outfile));
			if (CLI.target == Action.SCAN) {
				scan(inputStream, outputStream);
			} else if (CLI.target == Action.PARSE || CLI.target == Action.DEFAULT) {
				parse(inputStream);
			} else if(CLI.target == Action.INTER) {
				DecafParser parser = parse(inputStream);
				ParseTreeNode tree = parser.getParseTree();
				tree = ParseTreeNode.compressTree(tree);
				tree.setFileName(CLI.infile);
				if(CLI.debug) {
					System.out.println("--------tree-------");
					tree.printTree();
					System.out.println("--------tree----------");
				}
				IrProgram p = AstCreator.parseProgram(tree, CLI.infile);
				if(CLI.debug) {
					System.out.println("-----ir-------");
					System.out.println(p);
					System.out.println("-------ir-------");
				}
				SemanticCheckerNode checker = new SemanticCheckerNode();
				//p.accept(checker);
				if(checker.visit(p))
					System.exit(-1);
				System.out.println("------temp-------");
				IrProgram newP = IrWithTemp.newProgram(p);
				System.out.println(newP);
				System.out.println("------temp-------");
				System.out.println("-----boundCheck-----");
				newP =BoundCheckVistor.newProgram(newP);
				System.out.println(newP);
				System.out.println("-----boundCheck-----");
				IrProgram assemP = IrQuadVistor.newProgram(newP);
				System.out.println(assemP);
				assemP = IrResolveNameToLocationVistor.newProgram(assemP);
				System.out.println(assemP);
				HashMap<String, CFG> maps = cfgNodeVistor.cfgForProgram(assemP);
				for(String s: maps.keySet()) {
					System.out.println(maps.get(s));
				}
				String code = AssemblyFromCFGVistor.assemblyForWholeCFG(assemP);
				System.out.println(code);
				AssemblyFromCFGVistor.assemblyFile(code, p.getFilename());
			}
		} catch (Exception e) {
			// print the error:
			System.err.println(CLI.infile + " " + e);
			// parser.reportError(e.printStackTrace(););
			e.printStackTrace();
		}
	}

	public static void scan(InputStream inputStream, PrintStream outputStream) {
		DecafScanner scanner = new DecafScanner(new DataInputStream(inputStream));
		scanner.setTrace(CLI.debug);
		Token token;
		boolean done = false;
		while (!done) {
			try {
				for (token = scanner.nextToken(); token.getType() != DecafParserTokenTypes.EOF; token = scanner
						.nextToken()) {
					String type = "";
					String text = token.getText();
					switch (token.getType()) {
					// TODO: add strings for the other types here...
					case DecafScannerTokenTypes.ID:
						type = " IDENTIFIER";
						break;
					case DecafScannerTokenTypes.CHAR_LITERAL:
						type = " CHARLITERAL";
						break;
					case DecafScannerTokenTypes.INT_LITERAL:
						type = " INTLITERAL";
						break;
					case DecafScannerTokenTypes.STRING:
						type = " STRINGLITERAL";
						break;
					case DecafScannerTokenTypes.TRUE:
						type = " BOOLEANLITERAL";
						break;
					case DecafScannerTokenTypes.FALSE:
						type = " BOOLEANLITERAL";
						break;
					}
					outputStream.println(token.getLine() + type + " " + text);
				}
				done = true;
			} catch (Exception e) {
				// print the error:
				System.err.println(CLI.infile + " " + e);
				  try { 
					  scanner.consume(); 
					  } catch (CharStreamException e1) {
						  // TODOAuto-generated catch block 
						  e1.printStackTrace(); }
				 
			}
		}
		// return null;

	}

	public static DecafParser parse(InputStream inputStream) {
		DecafScanner scanner = new DecafScanner(new DataInputStream(inputStream));
		DecafParser parser = new DecafParser(scanner);
		try {
			//parser.setTrace(CLI.debug);
			parser.program();
			if (parser.getError()) {
				System.exit(1);
			}
		} catch (RecognitionException | TokenStreamException e) {
			// TODO Auto-generated catch block
			if (e instanceof RecognitionException)
				parser.reportError((RecognitionException) e);
			System.exit(-1);
		}
		return parser;
	}
}
