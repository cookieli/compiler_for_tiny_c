header {
package edu.mit.compilers.grammar;
import java.io.*;
import edu.mit.compilers.trees.ParseTreeNode;
}

options
{
  mangleLiteralPrefix = "TK_";
  language = "Java";
}

class DecafParser extends Parser;
options
{
  importVocab = DecafScanner;
  k = 3;
  buildAST = true;
}

// Java glue code that makes error reporting easier.
// You can insert arbitrary Java code into your parser/lexer this way.
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
  private boolean trace = true;

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
}

program: (import_decl SEMICOLON)* (variable_declaration SEMICOLON)* (func_def)* EOF;

import_decl: IMPORT ID;
variable_declaration: type (location_def (COMMA location_def)*) ;

location_def: array_form|ID;
array_form: ID LBRACKET num_literal RBRACKET;


func_def: (VOID | type) ID LPAREN (func_def_arg)? RPAREN LCURLY (function_body)? RCURLY;
func_def_arg: (type ID (COMMA type ID)*);

type: INT|BOOL;
function_body: (variable_declaration SEMICOLON)* (statement)* ;
statement: (assignment SEMICOLON)
    | if_block | for_block | while_block| (func_invoc SEMICOLON)
    | (BREAK SEMICOLON) | (CONTINUE SEMICOLON)| (return_assignment SEMICOLON);
assignment: location ((EQUALS expr)| (COMBOUND_ASSIGN_OP expr)| INCREMENT | DECREMENT);
return_assignment: RETURN expr;
operand: (location |literal | func_invoc | (LEN LPAREN ID RPAREN));
location: ID | array_member;
literal: num_literal | bool_value;
num_literal: INT_LITERAL | CHAR_LITERAL;
bool_value: TRUE | FALSE;

array_member: ID LBRACKET expr RBRACKET;
func_invoc: ID LPAREN (func_arg)? RPAREN;
func_arg: (expr|STRING) (COMMA (expr|STRING))*;

plus_or_minus: PLUS | MINUS;
expr: expr7;

expr0: operand | LPAREN expr RPAREN;
expr1: (MINUS| NOT)*expr0;
expr2: expr1 (MUL_OP expr1)*;
expr3: expr2 (plus_or_minus expr2)* ;

expr4: expr3 (cmp_op expr3)*;
expr5: expr4 (COND_AND expr4)*;
expr6: expr5 (COND_OR expr5)*;

expr7: expr6 (QUESTION expr7 COLON expr7)*;

cmp_op: REL_OP | EQUAL_OP;
if_block: (IF LPAREN expr RPAREN LCURLY function_body RCURLY)
        (ELSE  IF LPAREN expr RPAREN LCURLY function_body RCURLY)*
        (ELSE  LCURLY function_body  RCURLY)?;

for_block: FOR LPAREN
        (location (EQUALS expr(COMMA location EQUALS expr)*))? SEMICOLON
        (expr (COMMA expr)*)? SEMICOLON
        (location ((COMBOUND_ASSIGN_OP expr) | INCREMENT |DECREMENT)) 
        RPAREN LCURLY
        function_body
        RCURLY;

while_block: WHILE LPAREN expr RPAREN
        LCURLY function_body RCURLY;
