/*  1:   */ package org.springframework.core;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ 
/*  5:   */ public class NestedIOException
/*  6:   */   extends IOException
/*  7:   */ {
/*  8:   */   static
/*  9:   */   {
/* 10:43 */     NestedExceptionUtils.class.getName();
/* 11:   */   }
/* 12:   */   
/* 13:   */   public NestedIOException(String msg)
/* 14:   */   {
/* 15:52 */     super(msg);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public NestedIOException(String msg, Throwable cause)
/* 19:   */   {
/* 20:62 */     super(msg);
/* 21:63 */     initCause(cause);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String getMessage()
/* 25:   */   {
/* 26:73 */     return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.NestedIOException
 * JD-Core Version:    0.7.0.1
 */