/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ class Plus
/*  4:   */   extends StringOperator
/*  5:   */ {
/*  6:   */   Operator getBinaryOperator()
/*  7:   */   {
/*  8:43 */     return new Add();
/*  9:   */   }
/* 10:   */   
/* 11:   */   Operator getUnaryOperator()
/* 12:   */   {
/* 13:51 */     return new UnaryPlus();
/* 14:   */   }
/* 15:   */   
/* 16:   */   void handleImportedCellReferences() {}
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Plus
 * JD-Core Version:    0.7.0.1
 */