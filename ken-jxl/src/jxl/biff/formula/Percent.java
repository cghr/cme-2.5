/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ class Percent
/*  4:   */   extends UnaryOperator
/*  5:   */   implements ParsedThing
/*  6:   */ {
/*  7:   */   public String getSymbol()
/*  8:   */   {
/*  9:36 */     return "%";
/* 10:   */   }
/* 11:   */   
/* 12:   */   public void getString(StringBuffer buf)
/* 13:   */   {
/* 14:41 */     ParseItem[] operands = getOperands();
/* 15:42 */     operands[0].getString(buf);
/* 16:43 */     buf.append(getSymbol());
/* 17:   */   }
/* 18:   */   
/* 19:   */   void handleImportedCellReferences()
/* 20:   */   {
/* 21:53 */     ParseItem[] operands = getOperands();
/* 22:54 */     operands[0].handleImportedCellReferences();
/* 23:   */   }
/* 24:   */   
/* 25:   */   Token getToken()
/* 26:   */   {
/* 27:64 */     return Token.PERCENT;
/* 28:   */   }
/* 29:   */   
/* 30:   */   int getPrecedence()
/* 31:   */   {
/* 32:75 */     return 5;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Percent
 * JD-Core Version:    0.7.0.1
 */