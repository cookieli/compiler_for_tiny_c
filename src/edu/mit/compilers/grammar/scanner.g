header {
package edu.mit.compilers.grammar;
}

options
{
  mangleLiteralPrefix = "TK_";
  language = "Java";
}

{@SuppressWarnings("unchecked")}
class DecafScanner extends Lexer;
options
{
  k = 2;
}

tokens 
{
  BOOL = "bool";
  BREAK = "break";
  IMPORT = "import";
  CONTINUE = "continue";
  ELSE ="else";
  FOR = "for";
  FALSE= "false";
  TRUE= "true";
  WHILE = "while";
  IF = "if";
  INT = "int";
  RETURN = "return";
  LEN = "len";
  VOID = "void";
}

// Selectively turns on debug tracing mode.
// You can insert arbitrary Java code into your parser/lexer this way.
{
  /** Whether to display debug information. */
  private boolean trace = false;

  public void setTrace(boolean shouldTrace) {
    trace = shouldTrace;
  }
  @Override
  public void traceIn(String rname) throws CharStreamException {
    if (trace) {
      super.traceIn(rname);
    }
  }
  @Override
  public void traceOut(String rname) throws CharStreamException {
    if (trace) {
      super.traceOut(rname);
    }
  }
}

LCURLY options { paraphrase = "{"; } : "{";
RCURLY options { paraphrase = "}"; } : "}";
LBRACKET options { paraphrase = "["; } :"[";
RBRACKET options { paraphrase = "]"; } : "]";
LPAREN options { paraphrase = "("; } : "(";
RPAREN options { paraphrase = ")"; } : ")";
SEMICOLON options { paraphrase = ";";} : ";";
COLON options {paraphrase = ":";} : ":";
COMMA options {paraphrase = ",";} : ",";
ID options { paraphrase = "an identifier"; } : 
        (ALPHA | '_')(ALPHA|'_' | DIGIT)*;

// Note that here, the {} syntax allows you to literally command the lexer
// to skip mark this token as skipped, or to advance to the next line
// by directly adding Java commands.
WS_ : ('\t'|' ' | '\n' {newline();})+ {_ttype = Token.SKIP; };
SL_COMMENT : "//" (~'\n')* '\n' {_ttype = Token.SKIP; newline (); };
BL_COMMENT : "/*"(ESC | ALPHA | DIGIT | WS_ |BL_COMMENT | ~('*'))*("*/"){_ttype = Token.SKIP; } ;
CHAR_LITERAL :  '\''CHAR'\'' ;
INT_LITERAL : HEX_LITERAL | DECIMAL_LITERAL;
PLUS: '+';
MINUS: '-';
MUL_OP: "*"|"/"|"%";
REL_OP: "<"|"<="|">="|">";
EQUAL_OP: "!="|"==";
COND_OP: "&&"|"||";
NOT: "!";
EQUALS: "=";
QUESTION: "?";
INCREMENT: "++";
DECREMENT: "--";
COMBOUND_ASSIGN_OP: "+=" | "-=" | "*=" | "/=";


STRING : '"' (CHAR)* '"';

protected
ESC :  '\\' ('"'| '\''| '\\' |'t'|'n');
protected
ALPHA: 'a'..'z' |'A'..'Z';
protected
DIGIT: '0'..'9';
protected
CHAR :  (ESC|~('"'|'\''|'\\'|'\n'|'\t'));
protected
HEXDIGIT: DIGIT | 'A'..'F' | 'a'..'f';
protected
HEX_LITERAL: "0x"(HEXDIGIT)+;
protected
DECIMAL_LITERAL: (DIGIT)+;
