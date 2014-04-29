/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.CellType;
/*  4:   */ import jxl.biff.FormattingRecords;
/*  5:   */ 
/*  6:   */ public class BlankCell
/*  7:   */   extends CellValue
/*  8:   */ {
/*  9:   */   BlankCell(Record t, FormattingRecords fr, SheetImpl si)
/* 10:   */   {
/* 11:40 */     super(t, fr, si);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public String getContents()
/* 15:   */   {
/* 16:50 */     return "";
/* 17:   */   }
/* 18:   */   
/* 19:   */   public CellType getType()
/* 20:   */   {
/* 21:60 */     return CellType.EMPTY;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.BlankCell
 * JD-Core Version:    0.7.0.1
 */