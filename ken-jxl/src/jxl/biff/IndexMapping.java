/*  1:   */ package jxl.biff;
/*  2:   */ 
/*  3:   */ import jxl.common.Logger;
/*  4:   */ 
/*  5:   */ public final class IndexMapping
/*  6:   */ {
/*  7:34 */   private static Logger logger = Logger.getLogger(IndexMapping.class);
/*  8:   */   private int[] newIndices;
/*  9:   */   
/* 10:   */   public IndexMapping(int size)
/* 11:   */   {
/* 12:48 */     this.newIndices = new int[size];
/* 13:   */   }
/* 14:   */   
/* 15:   */   public void setMapping(int oldIndex, int newIndex)
/* 16:   */   {
/* 17:58 */     this.newIndices[oldIndex] = newIndex;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public int getNewIndex(int oldIndex)
/* 21:   */   {
/* 22:68 */     return this.newIndices[oldIndex];
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.IndexMapping
 * JD-Core Version:    0.7.0.1
 */