/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ import jxl.common.Logger;
/*  4:   */ 
/*  5:   */ class Minus
/*  6:   */   extends StringOperator
/*  7:   */ {
/*  8:32 */   private static Logger logger = Logger.getLogger(StringOperator.class);
/*  9:   */   
/* 10:   */   Operator getBinaryOperator()
/* 11:   */   {
/* 12:47 */     return new Subtract();
/* 13:   */   }
/* 14:   */   
/* 15:   */   Operator getUnaryOperator()
/* 16:   */   {
/* 17:55 */     return new UnaryMinus();
/* 18:   */   }
/* 19:   */   
/* 20:   */   void handleImportedCellReferences() {}
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Minus
 * JD-Core Version:    0.7.0.1
 */