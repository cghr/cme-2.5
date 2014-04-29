/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ public class FatalBeanException
/*  4:   */   extends BeansException
/*  5:   */ {
/*  6:   */   public FatalBeanException(String msg)
/*  7:   */   {
/*  8:32 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public FatalBeanException(String msg, Throwable cause)
/* 12:   */   {
/* 13:42 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.FatalBeanException
 * JD-Core Version:    0.7.0.1
 */