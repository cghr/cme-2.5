/*  1:   */ package org.springframework.expression.spel.standard;
/*  2:   */ 
/*  3:   */  enum TokenKind
/*  4:   */ {
/*  5:24 */   LITERAL_INT,  LITERAL_LONG,  LITERAL_HEXINT,  LITERAL_HEXLONG,  LITERAL_STRING,  LITERAL_REAL,  LITERAL_REAL_FLOAT,  LPAREN("("),  RPAREN(")"),  COMMA(","),  IDENTIFIER,  COLON(":"),  HASH("#"),  RSQUARE("]"),  LSQUARE("["),  LCURLY("{"),  RCURLY("}"),  DOT("."),  PLUS("+"),  STAR("*"),  MINUS("-"),  SELECT_FIRST("^["),  SELECT_LAST("$["),  QMARK("?"),  PROJECT("!["),  DIV("/"),  GE(">="),  GT(">"),  LE("<="),  LT("<"),  EQ("=="),  NE("!="),  MOD("%"),  NOT("!"),  ASSIGN("="),  INSTANCEOF("instanceof"),  MATCHES("matches"),  BETWEEN("between"),  SELECT("?["),  POWER("^"),  ELVIS("?:"),  SAFE_NAVI("?."),  BEAN_REF("@");
/*  6:   */   
/*  7:   */   char[] tokenChars;
/*  8:   */   private boolean hasPayload;
/*  9:   */   
/* 10:   */   private TokenKind(String tokenString)
/* 11:   */   {
/* 12:39 */     this.tokenChars = tokenString.toCharArray();
/* 13:40 */     this.hasPayload = (this.tokenChars.length == 0);
/* 14:   */   }
/* 15:   */   
/* 16:   */   private TokenKind()
/* 17:   */   {
/* 18:44 */     this("");
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String toString()
/* 22:   */   {
/* 23:48 */     return name() + (this.tokenChars.length != 0 ? "(" + new String(this.tokenChars) + ")" : "");
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean hasPayload()
/* 27:   */   {
/* 28:52 */     return this.hasPayload;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public int getLength()
/* 32:   */   {
/* 33:56 */     return this.tokenChars.length;
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.standard.TokenKind
 * JD-Core Version:    0.7.0.1
 */