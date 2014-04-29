/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class DeadlockLoserDataAccessException
/*  4:   */   extends PessimisticLockingFailureException
/*  5:   */ {
/*  6:   */   public DeadlockLoserDataAccessException(String msg, Throwable cause)
/*  7:   */   {
/*  8:33 */     super(msg, cause);
/*  9:   */   }
/* 10:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.DeadlockLoserDataAccessException
 * JD-Core Version:    0.7.0.1
 */