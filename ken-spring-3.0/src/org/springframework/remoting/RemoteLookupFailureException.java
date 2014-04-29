/*  1:   */ package org.springframework.remoting;
/*  2:   */ 
/*  3:   */ public class RemoteLookupFailureException
/*  4:   */   extends RemoteAccessException
/*  5:   */ {
/*  6:   */   public RemoteLookupFailureException(String msg)
/*  7:   */   {
/*  8:33 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public RemoteLookupFailureException(String msg, Throwable cause)
/* 12:   */   {
/* 13:42 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.RemoteLookupFailureException
 * JD-Core Version:    0.7.0.1
 */