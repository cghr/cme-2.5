/*  1:   */ package jxl.common;
/*  2:   */ 
/*  3:   */ public class AssertionFailed
/*  4:   */   extends RuntimeException
/*  5:   */ {
/*  6:   */   public AssertionFailed()
/*  7:   */   {
/*  8:35 */     printStackTrace();
/*  9:   */   }
/* 10:   */   
/* 11:   */   public AssertionFailed(String s)
/* 12:   */   {
/* 13:46 */     super(s);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.common.AssertionFailed
 * JD-Core Version:    0.7.0.1
 */