/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class DataAccessResourceFailureException
/*  4:   */   extends NonTransientDataAccessResourceException
/*  5:   */ {
/*  6:   */   public DataAccessResourceFailureException(String msg)
/*  7:   */   {
/*  8:33 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public DataAccessResourceFailureException(String msg, Throwable cause)
/* 12:   */   {
/* 13:42 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.DataAccessResourceFailureException
 * JD-Core Version:    0.7.0.1
 */