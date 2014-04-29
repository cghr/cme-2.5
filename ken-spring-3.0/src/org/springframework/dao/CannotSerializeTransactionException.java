/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class CannotSerializeTransactionException
/*  4:   */   extends PessimisticLockingFailureException
/*  5:   */ {
/*  6:   */   public CannotSerializeTransactionException(String msg)
/*  7:   */   {
/*  8:32 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public CannotSerializeTransactionException(String msg, Throwable cause)
/* 12:   */   {
/* 13:41 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.CannotSerializeTransactionException
 * JD-Core Version:    0.7.0.1
 */