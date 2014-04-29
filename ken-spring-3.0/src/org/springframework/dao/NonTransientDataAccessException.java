/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public abstract class NonTransientDataAccessException
/*  4:   */   extends DataAccessException
/*  5:   */ {
/*  6:   */   public NonTransientDataAccessException(String msg)
/*  7:   */   {
/*  8:35 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public NonTransientDataAccessException(String msg, Throwable cause)
/* 12:   */   {
/* 13:45 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.NonTransientDataAccessException
 * JD-Core Version:    0.7.0.1
 */