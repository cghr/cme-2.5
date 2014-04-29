/*  1:   */ package jxl.common;
/*  2:   */ 
/*  3:   */ public final class Assert
/*  4:   */ {
/*  5:   */   public static void verify(boolean condition)
/*  6:   */   {
/*  7:35 */     if (!condition) {
/*  8:37 */       throw new AssertionFailed();
/*  9:   */     }
/* 10:   */   }
/* 11:   */   
/* 12:   */   public static void verify(boolean condition, String message)
/* 13:   */   {
/* 14:49 */     if (!condition) {
/* 15:51 */       throw new AssertionFailed(message);
/* 16:   */     }
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.common.Assert
 * JD-Core Version:    0.7.0.1
 */