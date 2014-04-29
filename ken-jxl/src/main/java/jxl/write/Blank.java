/*  1:   */ package jxl.write;
/*  2:   */ 
/*  3:   */ import jxl.Cell;
/*  4:   */ import jxl.format.CellFormat;
/*  5:   */ import jxl.write.biff.BlankRecord;
/*  6:   */ 
/*  7:   */ public class Blank
/*  8:   */   extends BlankRecord
/*  9:   */   implements WritableCell
/* 10:   */ {
/* 11:   */   public Blank(int c, int r)
/* 12:   */   {
/* 13:42 */     super(c, r);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Blank(int c, int r, CellFormat st)
/* 17:   */   {
/* 18:56 */     super(c, r, st);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Blank(Cell lc)
/* 22:   */   {
/* 23:67 */     super(lc);
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected Blank(int col, int row, Blank b)
/* 27:   */   {
/* 28:80 */     super(col, row, b);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public WritableCell copyTo(int col, int row)
/* 32:   */   {
/* 33:92 */     return new Blank(col, row, this);
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.Blank
 * JD-Core Version:    0.7.0.1
 */