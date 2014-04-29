/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import jxl.BooleanCell;
/*   4:    */ import jxl.format.CellFormat;
/*   5:    */ import jxl.write.biff.BooleanRecord;
/*   6:    */ 
/*   7:    */ public class Boolean
/*   8:    */   extends BooleanRecord
/*   9:    */   implements WritableCell, BooleanCell
/*  10:    */ {
/*  11:    */   public Boolean(int c, int r, boolean val)
/*  12:    */   {
/*  13: 42 */     super(c, r, val);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public Boolean(int c, int r, boolean val, CellFormat st)
/*  17:    */   {
/*  18: 57 */     super(c, r, val, st);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Boolean(BooleanCell nc)
/*  22:    */   {
/*  23: 68 */     super(nc);
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected Boolean(int col, int row, Boolean b)
/*  27:    */   {
/*  28: 80 */     super(col, row, b);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setValue(boolean val)
/*  32:    */   {
/*  33: 89 */     super.setValue(val);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public WritableCell copyTo(int col, int row)
/*  37:    */   {
/*  38:101 */     return new Boolean(col, row, this);
/*  39:    */   }
/*  40:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.Boolean
 * JD-Core Version:    0.7.0.1
 */