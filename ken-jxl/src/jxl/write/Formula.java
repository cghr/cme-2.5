/*  1:   */ package jxl.write;
/*  2:   */ 
/*  3:   */ import jxl.format.CellFormat;
/*  4:   */ import jxl.write.biff.FormulaRecord;
/*  5:   */ 
/*  6:   */ public class Formula
/*  7:   */   extends FormulaRecord
/*  8:   */   implements WritableCell
/*  9:   */ {
/* 10:   */   public Formula(int c, int r, String form)
/* 11:   */   {
/* 12:39 */     super(c, r, form);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public Formula(int c, int r, String form, CellFormat st)
/* 16:   */   {
/* 17:52 */     super(c, r, form, st);
/* 18:   */   }
/* 19:   */   
/* 20:   */   protected Formula(int c, int r, Formula f)
/* 21:   */   {
/* 22:64 */     super(c, r, f);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public WritableCell copyTo(int col, int row)
/* 26:   */   {
/* 27:75 */     return new Formula(col, row, this);
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.Formula
 * JD-Core Version:    0.7.0.1
 */