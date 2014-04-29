/*  1:   */ package org.springframework.expression.spel.standard;
/*  2:   */ 
/*  3:   */ class Token
/*  4:   */ {
/*  5:   */   TokenKind kind;
/*  6:   */   String data;
/*  7:   */   int startpos;
/*  8:   */   int endpos;
/*  9:   */   
/* 10:   */   Token(TokenKind tokenKind, int startpos, int endpos)
/* 11:   */   {
/* 12:38 */     this.kind = tokenKind;
/* 13:39 */     this.startpos = startpos;
/* 14:40 */     this.endpos = endpos;
/* 15:   */   }
/* 16:   */   
/* 17:   */   Token(TokenKind tokenKind, char[] tokenData, int pos, int endpos)
/* 18:   */   {
/* 19:44 */     this(tokenKind, pos, endpos);
/* 20:45 */     this.data = new String(tokenData);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public TokenKind getKind()
/* 24:   */   {
/* 25:50 */     return this.kind;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:54 */     StringBuilder s = new StringBuilder();
/* 31:55 */     s.append("[").append(this.kind.toString());
/* 32:56 */     if (this.kind.hasPayload()) {
/* 33:57 */       s.append(":").append(this.data);
/* 34:   */     }
/* 35:59 */     s.append("]");
/* 36:60 */     s.append("(").append(this.startpos).append(",").append(this.endpos).append(")");
/* 37:61 */     return s.toString();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public boolean isIdentifier()
/* 41:   */   {
/* 42:65 */     return this.kind == TokenKind.IDENTIFIER;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public boolean isNumericRelationalOperator()
/* 46:   */   {
/* 47:69 */     return (this.kind == TokenKind.GT) || (this.kind == TokenKind.GE) || (this.kind == TokenKind.LT) || (this.kind == TokenKind.LE) || (this.kind == TokenKind.EQ) || (this.kind == TokenKind.NE);
/* 48:   */   }
/* 49:   */   
/* 50:   */   public String stringValue()
/* 51:   */   {
/* 52:73 */     return this.data;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public Token asInstanceOfToken()
/* 56:   */   {
/* 57:77 */     return new Token(TokenKind.INSTANCEOF, this.startpos, this.endpos);
/* 58:   */   }
/* 59:   */   
/* 60:   */   public Token asMatchesToken()
/* 61:   */   {
/* 62:81 */     return new Token(TokenKind.MATCHES, this.startpos, this.endpos);
/* 63:   */   }
/* 64:   */   
/* 65:   */   public Token asBetweenToken()
/* 66:   */   {
/* 67:85 */     return new Token(TokenKind.BETWEEN, this.startpos, this.endpos);
/* 68:   */   }
/* 69:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.standard.Token
 * JD-Core Version:    0.7.0.1
 */