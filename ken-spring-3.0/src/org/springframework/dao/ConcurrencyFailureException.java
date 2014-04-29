/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class ConcurrencyFailureException
/*  4:   */   extends TransientDataAccessException
/*  5:   */ {
/*  6:   */   public ConcurrencyFailureException(String msg)
/*  7:   */   {
/*  8:39 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public ConcurrencyFailureException(String msg, Throwable cause)
/* 12:   */   {
/* 13:48 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.ConcurrencyFailureException
 * JD-Core Version:    0.7.0.1
 */