/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import jxl.NumberCell;
/*   4:    */ import jxl.format.CellFormat;
/*   5:    */ import jxl.write.biff.NumberRecord;
/*   6:    */ 
/*   7:    */ public class Number
/*   8:    */   extends NumberRecord
/*   9:    */   implements WritableCell, NumberCell
/*  10:    */ {
/*  11:    */   public Number(int c, int r, double val)
/*  12:    */   {
/*  13: 42 */     super(c, r, val);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public Number(int c, int r, double val, CellFormat st)
/*  17:    */   {
/*  18: 58 */     super(c, r, val, st);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Number(NumberCell nc)
/*  22:    */   {
/*  23: 69 */     super(nc);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setValue(double val)
/*  27:    */   {
/*  28: 79 */     super.setValue(val);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected Number(int col, int row, Number n)
/*  32:    */   {
/*  33: 91 */     super(col, row, n);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public WritableCell copyTo(int col, int row)
/*  37:    */   {
/*  38:103 */     return new Number(col, row, this);
/*  39:    */   }
/*  40:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.Number
 * JD-Core Version:    0.7.0.1
 */