/*  1:   */ package org.springframework.remoting;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ 
/*  5:   */ public class RemoteAccessException
/*  6:   */   extends NestedRuntimeException
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = -4906825139312227864L;
/*  9:   */   
/* 10:   */   public RemoteAccessException(String msg)
/* 11:   */   {
/* 12:60 */     super(msg);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public RemoteAccessException(String msg, Throwable cause)
/* 16:   */   {
/* 17:70 */     super(msg, cause);
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.RemoteAccessException
 * JD-Core Version:    0.7.0.1
 */