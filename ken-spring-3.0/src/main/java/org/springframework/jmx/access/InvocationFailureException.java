/*  1:   */ package org.springframework.jmx.access;
/*  2:   */ 
/*  3:   */ import org.springframework.jmx.JmxException;
/*  4:   */ 
/*  5:   */ public class InvocationFailureException
/*  6:   */   extends JmxException
/*  7:   */ {
/*  8:   */   public InvocationFailureException(String msg)
/*  9:   */   {
/* 10:37 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public InvocationFailureException(String msg, Throwable cause)
/* 14:   */   {
/* 15:47 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.access.InvocationFailureException
 * JD-Core Version:    0.7.0.1
 */