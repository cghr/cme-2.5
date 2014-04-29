/*  1:   */ package jxl;
/*  2:   */ 
/*  3:   */ import jxl.biff.BaseCellFeatures;
/*  4:   */ 
/*  5:   */ public class CellFeatures
/*  6:   */   extends BaseCellFeatures
/*  7:   */ {
/*  8:   */   public CellFeatures() {}
/*  9:   */   
/* 10:   */   protected CellFeatures(CellFeatures cf)
/* 11:   */   {
/* 12:44 */     super(cf);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public String getComment()
/* 16:   */   {
/* 17:55 */     return super.getComment();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getDataValidationList()
/* 21:   */   {
/* 22:65 */     return super.getDataValidationList();
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Range getSharedDataValidationRange()
/* 26:   */   {
/* 27:78 */     return super.getSharedDataValidationRange();
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.CellFeatures
 * JD-Core Version:    0.7.0.1
 */