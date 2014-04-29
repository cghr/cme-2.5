/*  1:   */ package org.springframework.context;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.FatalBeanException;
/*  4:   */ 
/*  5:   */ public class ApplicationContextException
/*  6:   */   extends FatalBeanException
/*  7:   */ {
/*  8:   */   public ApplicationContextException(String msg)
/*  9:   */   {
/* 10:34 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public ApplicationContextException(String msg, Throwable cause)
/* 14:   */   {
/* 15:44 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.ApplicationContextException
 * JD-Core Version:    0.7.0.1
 */