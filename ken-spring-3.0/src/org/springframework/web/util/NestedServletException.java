/*  1:   */ package org.springframework.web.util;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletException;
/*  4:   */ import org.springframework.core.NestedExceptionUtils;
/*  5:   */ 
/*  6:   */ public class NestedServletException
/*  7:   */   extends ServletException
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = -5292377985529381145L;
/* 10:   */   
/* 11:   */   static
/* 12:   */   {
/* 13:51 */     NestedExceptionUtils.class.getName();
/* 14:   */   }
/* 15:   */   
/* 16:   */   public NestedServletException(String msg)
/* 17:   */   {
/* 18:60 */     super(msg);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public NestedServletException(String msg, Throwable cause)
/* 22:   */   {
/* 23:70 */     super(msg, cause);
/* 24:73 */     if ((getCause() == null) && (cause != null)) {
/* 25:74 */       initCause(cause);
/* 26:   */     }
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String getMessage()
/* 30:   */   {
/* 31:85 */     return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.NestedServletException
 * JD-Core Version:    0.7.0.1
 */