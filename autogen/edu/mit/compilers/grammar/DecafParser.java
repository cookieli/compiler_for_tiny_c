// $ANTLR 2.7.7 (2006-11-01): "parser.g" -> "DecafParser.java"$

package edu.mit.compilers.grammar;
import java.io.*;
import edu.mit.compilers.trees.ParseTreeNode;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

public class DecafParser extends antlr.LLkParser       implements DecafParserTokenTypes
 {

  // Do our own reporting of errors so the parser can return a non-zero status
  // if any errors are detected.
  /** Reports if any errors were reported during parse. */
  private boolean error;

  @Override
  public void reportError (RecognitionException ex) {
    // Print the error via some kind of error reporting mechanism.
    error = true;
    System.out.println(ex.getMessage());
  }
  @Override
  public void reportError (String s) {
    // Print the error via some kind of error reporting mechanism.
    error = true;
  }
  public boolean getError () {
        return error;
  }

  // Selectively turns on debug mode.

  /** Whether to display debug information. */
  private boolean trace = false;

  public void setTrace(boolean shouldTrace) {
    trace = shouldTrace;
  }

  private ParseTreeNode parseTree = ParseTreeNode.root();
  public ParseTreeNode getParseTree(){ return parseTree.getFirstChild();}
  @Override
  public void traceIn(String rname) throws TokenStreamException {
     parseTree = parseTree.addChild(rname);
    if (trace) {
      super.traceIn(rname);
    }
  }
  @Override
  public void traceOut(String rname) throws TokenStreamException {
      parseTree = parseTree.getParent();
    if (trace) {
      super.traceOut(rname);
    }
  }

  @Override
  public void match(int t)throws MismatchedTokenException,TokenStreamException{
      if(LT(1).getText() != null){
         // System.out.println("match: "+LT(1).getText());
          parseTree.addChild(LT(1));
      }
     super.match(t);
  }

protected DecafParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public DecafParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected DecafParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public DecafParser(TokenStream lexer) {
  this(lexer,3);
}

public DecafParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void program() throws RecognitionException, TokenStreamException {
		
		traceIn("program");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST program_AST = null;
			
			try {      // for error handling
				{
				_loop3:
				do {
					if ((LA(1)==IMPORT)) {
						import_decl();
						astFactory.addASTChild(currentAST, returnAST);
						AST tmp1_AST = null;
						tmp1_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp1_AST);
						match(SEMICOLON);
					}
					else {
						break _loop3;
					}
					
				} while (true);
				}
				{
				_loop5:
				do {
					if ((LA(1)==BOOL||LA(1)==INT) && (LA(2)==ID) && (LA(3)==LBRACKET||LA(3)==SEMICOLON||LA(3)==COMMA)) {
						variable_declaration();
						astFactory.addASTChild(currentAST, returnAST);
						AST tmp2_AST = null;
						tmp2_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp2_AST);
						match(SEMICOLON);
					}
					else {
						break _loop5;
					}
					
				} while (true);
				}
				{
				_loop7:
				do {
					if ((LA(1)==BOOL||LA(1)==INT||LA(1)==VOID)) {
						func_def();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop7;
					}
					
				} while (true);
				}
				AST tmp3_AST = null;
				tmp3_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp3_AST);
				match(Token.EOF_TYPE);
				program_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			}
			returnAST = program_AST;
		} finally { // debugging
			traceOut("program");
		}
	}
	
	public final void import_decl() throws RecognitionException, TokenStreamException {
		
		traceIn("import_decl");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST import_decl_AST = null;
			
			try {      // for error handling
				AST tmp4_AST = null;
				tmp4_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp4_AST);
				match(IMPORT);
				AST tmp5_AST = null;
				tmp5_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp5_AST);
				match(ID);
				import_decl_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			}
			returnAST = import_decl_AST;
		} finally { // debugging
			traceOut("import_decl");
		}
	}
	
	public final void variable_declaration() throws RecognitionException, TokenStreamException {
		
		traceIn("variable_declaration");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST variable_declaration_AST = null;
			
			try {      // for error handling
				type();
				astFactory.addASTChild(currentAST, returnAST);
				{
				location_def();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop12:
				do {
					if ((LA(1)==COMMA)) {
						AST tmp6_AST = null;
						tmp6_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp6_AST);
						match(COMMA);
						location_def();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop12;
					}
					
				} while (true);
				}
				}
				variable_declaration_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			}
			returnAST = variable_declaration_AST;
		} finally { // debugging
			traceOut("variable_declaration");
		}
	}
	
	public final void func_def() throws RecognitionException, TokenStreamException {
		
		traceIn("func_def");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST func_def_AST = null;
			
			try {      // for error handling
				{
				switch ( LA(1)) {
				case VOID:
				{
					AST tmp7_AST = null;
					tmp7_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp7_AST);
					match(VOID);
					break;
				}
				case BOOL:
				case INT:
				{
					type();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				AST tmp8_AST = null;
				tmp8_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp8_AST);
				match(ID);
				AST tmp9_AST = null;
				tmp9_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp9_AST);
				match(LPAREN);
				{
				switch ( LA(1)) {
				case BOOL:
				case INT:
				{
					func_def_arg();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				AST tmp10_AST = null;
				tmp10_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp10_AST);
				match(RPAREN);
				AST tmp11_AST = null;
				tmp11_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp11_AST);
				match(LCURLY);
				{
				if ((_tokenSet_2.member(LA(1))) && (_tokenSet_3.member(LA(2))) && (_tokenSet_4.member(LA(3)))) {
					function_body();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((LA(1)==RCURLY) && (_tokenSet_5.member(LA(2))) && (LA(3)==EOF||LA(3)==ID)) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				AST tmp12_AST = null;
				tmp12_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp12_AST);
				match(RCURLY);
				func_def_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			}
			returnAST = func_def_AST;
		} finally { // debugging
			traceOut("func_def");
		}
	}
	
	public final void type() throws RecognitionException, TokenStreamException {
		
		traceIn("type");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST type_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case INT:
				{
					AST tmp13_AST = null;
					tmp13_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp13_AST);
					match(INT);
					type_AST = (AST)currentAST.root;
					break;
				}
				case BOOL:
				{
					AST tmp14_AST = null;
					tmp14_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp14_AST);
					match(BOOL);
					type_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			}
			returnAST = type_AST;
		} finally { // debugging
			traceOut("type");
		}
	}
	
	public final void location_def() throws RecognitionException, TokenStreamException {
		
		traceIn("location_def");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST location_def_AST = null;
			
			try {      // for error handling
				if ((LA(1)==ID) && (LA(2)==LBRACKET)) {
					array_form();
					astFactory.addASTChild(currentAST, returnAST);
					location_def_AST = (AST)currentAST.root;
				}
				else if ((LA(1)==ID) && (LA(2)==SEMICOLON||LA(2)==COMMA)) {
					AST tmp15_AST = null;
					tmp15_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp15_AST);
					match(ID);
					location_def_AST = (AST)currentAST.root;
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_7);
			}
			returnAST = location_def_AST;
		} finally { // debugging
			traceOut("location_def");
		}
	}
	
	public final void array_form() throws RecognitionException, TokenStreamException {
		
		traceIn("array_form");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST array_form_AST = null;
			
			try {      // for error handling
				AST tmp16_AST = null;
				tmp16_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp16_AST);
				match(ID);
				AST tmp17_AST = null;
				tmp17_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp17_AST);
				match(LBRACKET);
				num_literal();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp18_AST = null;
				tmp18_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp18_AST);
				match(RBRACKET);
				array_form_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_7);
			}
			returnAST = array_form_AST;
		} finally { // debugging
			traceOut("array_form");
		}
	}
	
	public final void num_literal() throws RecognitionException, TokenStreamException {
		
		traceIn("num_literal");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST num_literal_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case INT_LITERAL:
				{
					AST tmp19_AST = null;
					tmp19_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp19_AST);
					match(INT_LITERAL);
					num_literal_AST = (AST)currentAST.root;
					break;
				}
				case CHAR_LITERAL:
				{
					AST tmp20_AST = null;
					tmp20_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp20_AST);
					match(CHAR_LITERAL);
					num_literal_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			}
			returnAST = num_literal_AST;
		} finally { // debugging
			traceOut("num_literal");
		}
	}
	
	public final void func_def_arg() throws RecognitionException, TokenStreamException {
		
		traceIn("func_def_arg");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST func_def_arg_AST = null;
			
			try {      // for error handling
				{
				type();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp21_AST = null;
				tmp21_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp21_AST);
				match(ID);
				{
				_loop22:
				do {
					if ((LA(1)==COMMA)) {
						AST tmp22_AST = null;
						tmp22_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp22_AST);
						match(COMMA);
						type();
						astFactory.addASTChild(currentAST, returnAST);
						AST tmp23_AST = null;
						tmp23_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp23_AST);
						match(ID);
					}
					else {
						break _loop22;
					}
					
				} while (true);
				}
				}
				func_def_arg_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_9);
			}
			returnAST = func_def_arg_AST;
		} finally { // debugging
			traceOut("func_def_arg");
		}
	}
	
	public final void function_body() throws RecognitionException, TokenStreamException {
		
		traceIn("function_body");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST function_body_AST = null;
			
			try {      // for error handling
				{
				_loop26:
				do {
					if ((LA(1)==BOOL||LA(1)==INT)) {
						variable_declaration();
						astFactory.addASTChild(currentAST, returnAST);
						AST tmp24_AST = null;
						tmp24_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp24_AST);
						match(SEMICOLON);
					}
					else {
						break _loop26;
					}
					
				} while (true);
				}
				{
				_loop28:
				do {
					if ((_tokenSet_10.member(LA(1)))) {
						statement();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop28;
					}
					
				} while (true);
				}
				function_body_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			}
			returnAST = function_body_AST;
		} finally { // debugging
			traceOut("function_body");
		}
	}
	
	public final void statement() throws RecognitionException, TokenStreamException {
		
		traceIn("statement");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST statement_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case IF:
				{
					if_block();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
					break;
				}
				case FOR:
				{
					for_block();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
					break;
				}
				case WHILE:
				{
					while_block();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
					break;
				}
				case BREAK:
				{
					{
					AST tmp25_AST = null;
					tmp25_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp25_AST);
					match(BREAK);
					AST tmp26_AST = null;
					tmp26_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp26_AST);
					match(SEMICOLON);
					}
					statement_AST = (AST)currentAST.root;
					break;
				}
				case CONTINUE:
				{
					{
					AST tmp27_AST = null;
					tmp27_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp27_AST);
					match(CONTINUE);
					AST tmp28_AST = null;
					tmp28_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp28_AST);
					match(SEMICOLON);
					}
					statement_AST = (AST)currentAST.root;
					break;
				}
				case RETURN:
				{
					{
					return_assignment();
					astFactory.addASTChild(currentAST, returnAST);
					AST tmp29_AST = null;
					tmp29_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp29_AST);
					match(SEMICOLON);
					}
					statement_AST = (AST)currentAST.root;
					break;
				}
				default:
					if ((LA(1)==ID) && (_tokenSet_12.member(LA(2)))) {
						{
						assignment();
						astFactory.addASTChild(currentAST, returnAST);
						AST tmp30_AST = null;
						tmp30_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp30_AST);
						match(SEMICOLON);
						}
						statement_AST = (AST)currentAST.root;
					}
					else if ((LA(1)==ID) && (LA(2)==LPAREN)) {
						{
						func_invoc();
						astFactory.addASTChild(currentAST, returnAST);
						AST tmp31_AST = null;
						tmp31_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp31_AST);
						match(SEMICOLON);
						}
						statement_AST = (AST)currentAST.root;
					}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			}
			returnAST = statement_AST;
		} finally { // debugging
			traceOut("statement");
		}
	}
	
	public final void assignment() throws RecognitionException, TokenStreamException {
		
		traceIn("assignment");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST assignment_AST = null;
			
			try {      // for error handling
				location();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case EQUALS:
				{
					{
					AST tmp32_AST = null;
					tmp32_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp32_AST);
					match(EQUALS);
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case COMBOUND_ASSIGN_OP:
				{
					{
					AST tmp33_AST = null;
					tmp33_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp33_AST);
					match(COMBOUND_ASSIGN_OP);
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case INCREMENT:
				{
					AST tmp34_AST = null;
					tmp34_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp34_AST);
					match(INCREMENT);
					break;
				}
				case DECREMENT:
				{
					AST tmp35_AST = null;
					tmp35_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp35_AST);
					match(DECREMENT);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				assignment_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			}
			returnAST = assignment_AST;
		} finally { // debugging
			traceOut("assignment");
		}
	}
	
	public final void if_block() throws RecognitionException, TokenStreamException {
		
		traceIn("if_block");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST if_block_AST = null;
			
			try {      // for error handling
				{
				AST tmp36_AST = null;
				tmp36_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp36_AST);
				match(IF);
				AST tmp37_AST = null;
				tmp37_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp37_AST);
				match(LPAREN);
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp38_AST = null;
				tmp38_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp38_AST);
				match(RPAREN);
				AST tmp39_AST = null;
				tmp39_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp39_AST);
				match(LCURLY);
				function_body();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp40_AST = null;
				tmp40_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp40_AST);
				match(RCURLY);
				}
				{
				switch ( LA(1)) {
				case ELSE:
				{
					AST tmp41_AST = null;
					tmp41_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp41_AST);
					match(ELSE);
					AST tmp42_AST = null;
					tmp42_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp42_AST);
					match(LCURLY);
					function_body();
					astFactory.addASTChild(currentAST, returnAST);
					AST tmp43_AST = null;
					tmp43_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp43_AST);
					match(RCURLY);
					break;
				}
				case BREAK:
				case CONTINUE:
				case FOR:
				case WHILE:
				case IF:
				case RETURN:
				case RCURLY:
				case ID:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if_block_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			}
			returnAST = if_block_AST;
		} finally { // debugging
			traceOut("if_block");
		}
	}
	
	public final void for_block() throws RecognitionException, TokenStreamException {
		
		traceIn("for_block");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST for_block_AST = null;
			
			try {      // for error handling
				AST tmp44_AST = null;
				tmp44_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp44_AST);
				match(FOR);
				AST tmp45_AST = null;
				tmp45_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp45_AST);
				match(LPAREN);
				{
				location();
				astFactory.addASTChild(currentAST, returnAST);
				{
				AST tmp46_AST = null;
				tmp46_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp46_AST);
				match(EQUALS);
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				}
				}
				AST tmp47_AST = null;
				tmp47_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp47_AST);
				match(SEMICOLON);
				{
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				}
				AST tmp48_AST = null;
				tmp48_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp48_AST);
				match(SEMICOLON);
				{
				location();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COMBOUND_ASSIGN_OP:
				{
					{
					AST tmp49_AST = null;
					tmp49_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp49_AST);
					match(COMBOUND_ASSIGN_OP);
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case INCREMENT:
				{
					AST tmp50_AST = null;
					tmp50_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp50_AST);
					match(INCREMENT);
					break;
				}
				case DECREMENT:
				{
					AST tmp51_AST = null;
					tmp51_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp51_AST);
					match(DECREMENT);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				}
				AST tmp52_AST = null;
				tmp52_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp52_AST);
				match(RPAREN);
				AST tmp53_AST = null;
				tmp53_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp53_AST);
				match(LCURLY);
				function_body();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp54_AST = null;
				tmp54_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp54_AST);
				match(RCURLY);
				for_block_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			}
			returnAST = for_block_AST;
		} finally { // debugging
			traceOut("for_block");
		}
	}
	
	public final void while_block() throws RecognitionException, TokenStreamException {
		
		traceIn("while_block");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST while_block_AST = null;
			
			try {      // for error handling
				AST tmp55_AST = null;
				tmp55_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp55_AST);
				match(WHILE);
				AST tmp56_AST = null;
				tmp56_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp56_AST);
				match(LPAREN);
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp57_AST = null;
				tmp57_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp57_AST);
				match(RPAREN);
				AST tmp58_AST = null;
				tmp58_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp58_AST);
				match(LCURLY);
				function_body();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp59_AST = null;
				tmp59_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp59_AST);
				match(RCURLY);
				while_block_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			}
			returnAST = while_block_AST;
		} finally { // debugging
			traceOut("while_block");
		}
	}
	
	public final void func_invoc() throws RecognitionException, TokenStreamException {
		
		traceIn("func_invoc");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST func_invoc_AST = null;
			
			try {      // for error handling
				AST tmp60_AST = null;
				tmp60_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp60_AST);
				match(ID);
				AST tmp61_AST = null;
				tmp61_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp61_AST);
				match(LPAREN);
				{
				switch ( LA(1)) {
				case FALSE:
				case TRUE:
				case LEN:
				case LPAREN:
				case ID:
				case CHAR_LITERAL:
				case INT_LITERAL:
				case MINUS:
				case NOT:
				case STRING:
				{
					func_arg();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				AST tmp62_AST = null;
				tmp62_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp62_AST);
				match(RPAREN);
				func_invoc_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			}
			returnAST = func_invoc_AST;
		} finally { // debugging
			traceOut("func_invoc");
		}
	}
	
	public final void return_assignment() throws RecognitionException, TokenStreamException {
		
		traceIn("return_assignment");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST return_assignment_AST = null;
			
			try {      // for error handling
				AST tmp63_AST = null;
				tmp63_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp63_AST);
				match(RETURN);
				{
				switch ( LA(1)) {
				case FALSE:
				case TRUE:
				case LEN:
				case LPAREN:
				case ID:
				case CHAR_LITERAL:
				case INT_LITERAL:
				case MINUS:
				case NOT:
				{
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMICOLON:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				return_assignment_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			}
			returnAST = return_assignment_AST;
		} finally { // debugging
			traceOut("return_assignment");
		}
	}
	
	public final void location() throws RecognitionException, TokenStreamException {
		
		traceIn("location");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST location_AST = null;
			
			try {      // for error handling
				if ((LA(1)==ID) && (_tokenSet_14.member(LA(2)))) {
					AST tmp64_AST = null;
					tmp64_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp64_AST);
					match(ID);
					location_AST = (AST)currentAST.root;
				}
				else if ((LA(1)==ID) && (LA(2)==LBRACKET)) {
					array_member();
					astFactory.addASTChild(currentAST, returnAST);
					location_AST = (AST)currentAST.root;
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			}
			returnAST = location_AST;
		} finally { // debugging
			traceOut("location");
		}
	}
	
	public final void expr() throws RecognitionException, TokenStreamException {
		
		traceIn("expr");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr_AST = null;
			
			try {      // for error handling
				expr7();
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_15);
			}
			returnAST = expr_AST;
		} finally { // debugging
			traceOut("expr");
		}
	}
	
	public final void operand() throws RecognitionException, TokenStreamException {
		
		traceIn("operand");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST operand_AST = null;
			
			try {      // for error handling
				{
				switch ( LA(1)) {
				case FALSE:
				case TRUE:
				case CHAR_LITERAL:
				case INT_LITERAL:
				{
					literal();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case LEN:
				{
					{
					AST tmp65_AST = null;
					tmp65_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp65_AST);
					match(LEN);
					AST tmp66_AST = null;
					tmp66_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp66_AST);
					match(LPAREN);
					AST tmp67_AST = null;
					tmp67_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp67_AST);
					match(ID);
					AST tmp68_AST = null;
					tmp68_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp68_AST);
					match(RPAREN);
					}
					break;
				}
				default:
					if ((LA(1)==ID) && (_tokenSet_16.member(LA(2)))) {
						location();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else if ((LA(1)==ID) && (LA(2)==LPAREN)) {
						func_invoc();
						astFactory.addASTChild(currentAST, returnAST);
					}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				operand_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			}
			returnAST = operand_AST;
		} finally { // debugging
			traceOut("operand");
		}
	}
	
	public final void literal() throws RecognitionException, TokenStreamException {
		
		traceIn("literal");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST literal_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case CHAR_LITERAL:
				case INT_LITERAL:
				{
					num_literal();
					astFactory.addASTChild(currentAST, returnAST);
					literal_AST = (AST)currentAST.root;
					break;
				}
				case FALSE:
				case TRUE:
				{
					bool_value();
					astFactory.addASTChild(currentAST, returnAST);
					literal_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			}
			returnAST = literal_AST;
		} finally { // debugging
			traceOut("literal");
		}
	}
	
	public final void array_member() throws RecognitionException, TokenStreamException {
		
		traceIn("array_member");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST array_member_AST = null;
			
			try {      // for error handling
				AST tmp69_AST = null;
				tmp69_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp69_AST);
				match(ID);
				AST tmp70_AST = null;
				tmp70_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp70_AST);
				match(LBRACKET);
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp71_AST = null;
				tmp71_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp71_AST);
				match(RBRACKET);
				array_member_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			}
			returnAST = array_member_AST;
		} finally { // debugging
			traceOut("array_member");
		}
	}
	
	public final void bool_value() throws RecognitionException, TokenStreamException {
		
		traceIn("bool_value");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST bool_value_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case TRUE:
				{
					AST tmp72_AST = null;
					tmp72_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp72_AST);
					match(TRUE);
					bool_value_AST = (AST)currentAST.root;
					break;
				}
				case FALSE:
				{
					AST tmp73_AST = null;
					tmp73_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp73_AST);
					match(FALSE);
					bool_value_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			}
			returnAST = bool_value_AST;
		} finally { // debugging
			traceOut("bool_value");
		}
	}
	
	public final void func_arg() throws RecognitionException, TokenStreamException {
		
		traceIn("func_arg");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST func_arg_AST = null;
			
			try {      // for error handling
				{
				switch ( LA(1)) {
				case FALSE:
				case TRUE:
				case LEN:
				case LPAREN:
				case ID:
				case CHAR_LITERAL:
				case INT_LITERAL:
				case MINUS:
				case NOT:
				{
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case STRING:
				{
					AST tmp74_AST = null;
					tmp74_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp74_AST);
					match(STRING);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				_loop55:
				do {
					if ((LA(1)==COMMA)) {
						AST tmp75_AST = null;
						tmp75_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp75_AST);
						match(COMMA);
						{
						switch ( LA(1)) {
						case FALSE:
						case TRUE:
						case LEN:
						case LPAREN:
						case ID:
						case CHAR_LITERAL:
						case INT_LITERAL:
						case MINUS:
						case NOT:
						{
							expr();
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case STRING:
						{
							AST tmp76_AST = null;
							tmp76_AST = astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp76_AST);
							match(STRING);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
					}
					else {
						break _loop55;
					}
					
				} while (true);
				}
				func_arg_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_9);
			}
			returnAST = func_arg_AST;
		} finally { // debugging
			traceOut("func_arg");
		}
	}
	
	public final void plus_or_minus() throws RecognitionException, TokenStreamException {
		
		traceIn("plus_or_minus");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST plus_or_minus_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case PLUS:
				{
					AST tmp77_AST = null;
					tmp77_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp77_AST);
					match(PLUS);
					plus_or_minus_AST = (AST)currentAST.root;
					break;
				}
				case MINUS:
				{
					AST tmp78_AST = null;
					tmp78_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp78_AST);
					match(MINUS);
					plus_or_minus_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_17);
			}
			returnAST = plus_or_minus_AST;
		} finally { // debugging
			traceOut("plus_or_minus");
		}
	}
	
	public final void expr7() throws RecognitionException, TokenStreamException {
		
		traceIn("expr7");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr7_AST = null;
			
			try {      // for error handling
				expr6();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop79:
				do {
					if ((LA(1)==QUESTION) && (_tokenSet_17.member(LA(2))) && (_tokenSet_18.member(LA(3)))) {
						AST tmp79_AST = null;
						tmp79_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp79_AST);
						match(QUESTION);
						expr7();
						astFactory.addASTChild(currentAST, returnAST);
						AST tmp80_AST = null;
						tmp80_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp80_AST);
						match(COLON);
						expr7();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop79;
					}
					
				} while (true);
				}
				expr7_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			}
			returnAST = expr7_AST;
		} finally { // debugging
			traceOut("expr7");
		}
	}
	
	public final void expr0() throws RecognitionException, TokenStreamException {
		
		traceIn("expr0");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr0_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case FALSE:
				case TRUE:
				case LEN:
				case ID:
				case CHAR_LITERAL:
				case INT_LITERAL:
				{
					operand();
					astFactory.addASTChild(currentAST, returnAST);
					expr0_AST = (AST)currentAST.root;
					break;
				}
				case LPAREN:
				{
					AST tmp81_AST = null;
					tmp81_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp81_AST);
					match(LPAREN);
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					AST tmp82_AST = null;
					tmp82_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp82_AST);
					match(RPAREN);
					expr0_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			}
			returnAST = expr0_AST;
		} finally { // debugging
			traceOut("expr0");
		}
	}
	
	public final void expr1() throws RecognitionException, TokenStreamException {
		
		traceIn("expr1");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr1_AST = null;
			
			try {      // for error handling
				{
				_loop61:
				do {
					switch ( LA(1)) {
					case MINUS:
					{
						AST tmp83_AST = null;
						tmp83_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp83_AST);
						match(MINUS);
						break;
					}
					case NOT:
					{
						AST tmp84_AST = null;
						tmp84_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp84_AST);
						match(NOT);
						break;
					}
					default:
					{
						break _loop61;
					}
					}
				} while (true);
				}
				expr0();
				astFactory.addASTChild(currentAST, returnAST);
				expr1_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			}
			returnAST = expr1_AST;
		} finally { // debugging
			traceOut("expr1");
		}
	}
	
	public final void expr2() throws RecognitionException, TokenStreamException {
		
		traceIn("expr2");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr2_AST = null;
			
			try {      // for error handling
				expr1();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop64:
				do {
					if ((LA(1)==MUL_OP)) {
						AST tmp85_AST = null;
						tmp85_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp85_AST);
						match(MUL_OP);
						expr1();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop64;
					}
					
				} while (true);
				}
				expr2_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_20);
			}
			returnAST = expr2_AST;
		} finally { // debugging
			traceOut("expr2");
		}
	}
	
	public final void expr3() throws RecognitionException, TokenStreamException {
		
		traceIn("expr3");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr3_AST = null;
			
			try {      // for error handling
				expr2();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop67:
				do {
					if ((LA(1)==PLUS||LA(1)==MINUS)) {
						plus_or_minus();
						astFactory.addASTChild(currentAST, returnAST);
						expr2();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop67;
					}
					
				} while (true);
				}
				expr3_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_21);
			}
			returnAST = expr3_AST;
		} finally { // debugging
			traceOut("expr3");
		}
	}
	
	public final void expr4() throws RecognitionException, TokenStreamException {
		
		traceIn("expr4");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr4_AST = null;
			
			try {      // for error handling
				expr3();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop70:
				do {
					if ((LA(1)==REL_OP||LA(1)==EQUAL_OP)) {
						cmp_op();
						astFactory.addASTChild(currentAST, returnAST);
						expr3();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop70;
					}
					
				} while (true);
				}
				expr4_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_22);
			}
			returnAST = expr4_AST;
		} finally { // debugging
			traceOut("expr4");
		}
	}
	
	public final void cmp_op() throws RecognitionException, TokenStreamException {
		
		traceIn("cmp_op");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST cmp_op_AST = null;
			
			try {      // for error handling
				switch ( LA(1)) {
				case REL_OP:
				{
					AST tmp86_AST = null;
					tmp86_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp86_AST);
					match(REL_OP);
					cmp_op_AST = (AST)currentAST.root;
					break;
				}
				case EQUAL_OP:
				{
					AST tmp87_AST = null;
					tmp87_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp87_AST);
					match(EQUAL_OP);
					cmp_op_AST = (AST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_17);
			}
			returnAST = cmp_op_AST;
		} finally { // debugging
			traceOut("cmp_op");
		}
	}
	
	public final void expr5() throws RecognitionException, TokenStreamException {
		
		traceIn("expr5");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr5_AST = null;
			
			try {      // for error handling
				expr4();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop73:
				do {
					if ((LA(1)==COND_AND)) {
						AST tmp88_AST = null;
						tmp88_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp88_AST);
						match(COND_AND);
						expr4();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop73;
					}
					
				} while (true);
				}
				expr5_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_23);
			}
			returnAST = expr5_AST;
		} finally { // debugging
			traceOut("expr5");
		}
	}
	
	public final void expr6() throws RecognitionException, TokenStreamException {
		
		traceIn("expr6");
		try { // debugging
			returnAST = null;
			ASTPair currentAST = new ASTPair();
			AST expr6_AST = null;
			
			try {      // for error handling
				expr5();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop76:
				do {
					if ((LA(1)==COND_OR)) {
						AST tmp89_AST = null;
						tmp89_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp89_AST);
						match(COND_OR);
						expr5();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop76;
					}
					
				} while (true);
				}
				expr6_AST = (AST)currentAST.root;
			}
			catch (RecognitionException ex) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			}
			returnAST = expr6_AST;
		} finally { // debugging
			traceOut("expr6");
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"bool\"",
		"\"break\"",
		"\"import\"",
		"\"continue\"",
		"\"else\"",
		"\"for\"",
		"\"false\"",
		"\"true\"",
		"\"while\"",
		"\"if\"",
		"\"int\"",
		"\"return\"",
		"\"len\"",
		"\"void\"",
		"{",
		"}",
		"[",
		"]",
		"(",
		")",
		";",
		":",
		",",
		"an identifier",
		"WS_",
		"SL_COMMENT",
		"BL_COMMENT",
		"CHAR_LITERAL",
		"INT_LITERAL",
		"PLUS",
		"MINUS",
		"MUL_OP",
		"REL_OP",
		"EQUAL_OP",
		"COND_AND",
		"COND_OR",
		"NOT",
		"EQUALS",
		"QUESTION",
		"INCREMENT",
		"DECREMENT",
		"COMBOUND_ASSIGN_OP",
		"STRING",
		"ESC",
		"ALPHA",
		"DIGIT",
		"CHAR",
		"HEXDIGIT",
		"HEX_LITERAL",
		"DECIMAL_LITERAL"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 16777216L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 134804144L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 64894964812818L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 76963898834594L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 147474L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 134217728L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 83886080L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 5489096130560L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 8388608L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 134263456L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 524288L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 63771675459584L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 134787744L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 69260770541568L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 94371840L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 5489097179136L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 1123272428544L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 6595095366656L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 4398174437376L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 5454736392192L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 5428966588416L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 5222808158208L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 4947930251264L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	
	}
