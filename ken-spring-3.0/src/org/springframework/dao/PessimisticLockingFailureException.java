/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class PessimisticLockingFailureException
/*  4:   */   extends ConcurrencyFailureException
/*  5:   */ {
/*  6:   */   public PessimisticLockingFailureException(String msg)
/*  7:   */   {
/*  8:40 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public PessimisticLockingFailureException(String msg, Throwable cause)
/* 12:   */   {
/* 13:49 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.PessimisticLockingFailureException
 * JD-Core Version:    0.7.0.1
 */