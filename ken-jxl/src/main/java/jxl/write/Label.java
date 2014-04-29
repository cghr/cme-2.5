/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import jxl.LabelCell;
/*   4:    */ import jxl.format.CellFormat;
/*   5:    */ import jxl.write.biff.LabelRecord;
/*   6:    */ 
/*   7:    */ public class Label
/*   8:    */   extends LabelRecord
/*   9:    */   implements WritableCell, LabelCell
/*  10:    */ {
/*  11:    */   public Label(int c, int r, String cont)
/*  12:    */   {
/*  13: 41 */     super(c, r, cont);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public Label(int c, int r, String cont, CellFormat st)
/*  17:    */   {
/*  18: 56 */     super(c, r, cont, st);
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected Label(int col, int row, Label l)
/*  22:    */   {
/*  23: 68 */     super(col, row, l);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Label(LabelCell lc)
/*  27:    */   {
/*  28: 79 */     super(lc);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setString(String s)
/*  32:    */   {
/*  33: 89 */     super.setString(s);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public WritableCell copyTo(int col, int row)
/*  37:    */   {
/*  38:101 */     return new Label(col, row, this);
/*  39:    */   }
/*  40:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.Label
 * JD-Core Version:    0.7.0.1
 */