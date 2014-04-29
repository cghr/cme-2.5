/*  1:   */ package org.springframework.web.bind;
/*  2:   */ 
/*  3:   */ import org.springframework.web.util.NestedServletException;
/*  4:   */ 
/*  5:   */ public class ServletRequestBindingException
/*  6:   */   extends NestedServletException
/*  7:   */ {
/*  8:   */   public ServletRequestBindingException(String msg)
/*  9:   */   {
/* 10:39 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public ServletRequestBindingException(String msg, Throwable cause)
/* 14:   */   {
/* 15:48 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.ServletRequestBindingException
 * JD-Core Version:    0.7.0.1
 */