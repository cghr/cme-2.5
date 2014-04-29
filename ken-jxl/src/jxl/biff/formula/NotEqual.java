/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ class NotEqual
/*  4:   */   extends BinaryOperator
/*  5:   */   implements ParsedThing
/*  6:   */ {
/*  7:   */   public String getSymbol()
/*  8:   */   {
/*  9:36 */     return "<>";
/* 10:   */   }
/* 11:   */   
/* 12:   */   Token getToken()
/* 13:   */   {
/* 14:47 */     return Token.NOT_EQUAL;
/* 15:   */   }
/* 16:   */   
/* 17:   */   int getPrecedence()
/* 18:   */   {
/* 19:58 */     return 5;
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.NotEqual
 * JD-Core Version:    0.7.0.1
 */