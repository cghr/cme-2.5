/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ import jxl.common.Assert;
/*   5:    */ 
/*   6:    */ abstract class StringOperator
/*   7:    */   extends Operator
/*   8:    */ {
/*   9:    */   public void getOperands(Stack s)
/*  10:    */   {
/*  11: 48 */     Assert.verify(false);
/*  12:    */   }
/*  13:    */   
/*  14:    */   int getPrecedence()
/*  15:    */   {
/*  16: 58 */     Assert.verify(false);
/*  17: 59 */     return 0;
/*  18:    */   }
/*  19:    */   
/*  20:    */   byte[] getBytes()
/*  21:    */   {
/*  22: 69 */     Assert.verify(false);
/*  23: 70 */     return null;
/*  24:    */   }
/*  25:    */   
/*  26:    */   void getString(StringBuffer buf)
/*  27:    */   {
/*  28: 78 */     Assert.verify(false);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/*  32:    */   {
/*  33: 89 */     Assert.verify(false);
/*  34:    */   }
/*  35:    */   
/*  36:    */   void columnInserted(int sheetIndex, int col, boolean currentSheet)
/*  37:    */   {
/*  38:103 */     Assert.verify(false);
/*  39:    */   }
/*  40:    */   
/*  41:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/*  42:    */   {
/*  43:118 */     Assert.verify(false);
/*  44:    */   }
/*  45:    */   
/*  46:    */   void rowInserted(int sheetIndex, int row, boolean currentSheet)
/*  47:    */   {
/*  48:133 */     Assert.verify(false);
/*  49:    */   }
/*  50:    */   
/*  51:    */   void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/*  52:    */   {
/*  53:148 */     Assert.verify(false);
/*  54:    */   }
/*  55:    */   
/*  56:    */   abstract Operator getBinaryOperator();
/*  57:    */   
/*  58:    */   abstract Operator getUnaryOperator();
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.StringOperator
 * JD-Core Version:    0.7.0.1
 */