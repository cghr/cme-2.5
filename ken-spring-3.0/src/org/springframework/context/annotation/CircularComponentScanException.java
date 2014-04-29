/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ class CircularComponentScanException
/*  4:   */   extends IllegalStateException
/*  5:   */ {
/*  6:   */   public CircularComponentScanException(String message, Exception cause)
/*  7:   */   {
/*  8:29 */     super(message, cause);
/*  9:   */   }
/* 10:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.CircularComponentScanException
 * JD-Core Version:    0.7.0.1
 */