/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class CannotAcquireLockException
/*  4:   */   extends PessimisticLockingFailureException
/*  5:   */ {
/*  6:   */   public CannotAcquireLockException(String msg)
/*  7:   */   {
/*  8:32 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public CannotAcquireLockException(String msg, Throwable cause)
/* 12:   */   {
/* 13:41 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.CannotAcquireLockException
 * JD-Core Version:    0.7.0.1
 */