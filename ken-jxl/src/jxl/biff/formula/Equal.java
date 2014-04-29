/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ class Equal
/*  4:   */   extends BinaryOperator
/*  5:   */   implements ParsedThing
/*  6:   */ {
/*  7:   */   public String getSymbol()
/*  8:   */   {
/*  9:41 */     return "=";
/* 10:   */   }
/* 11:   */   
/* 12:   */   Token getToken()
/* 13:   */   {
/* 14:52 */     return Token.EQUAL;
/* 15:   */   }
/* 16:   */   
/* 17:   */   int getPrecedence()
/* 18:   */   {
/* 19:63 */     return 5;
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Equal
 * JD-Core Version:    0.7.0.1
 */