/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class DataIntegrityViolationException
/*  4:   */   extends NonTransientDataAccessException
/*  5:   */ {
/*  6:   */   public DataIntegrityViolationException(String msg)
/*  7:   */   {
/*  8:34 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public DataIntegrityViolationException(String msg, Throwable cause)
/* 12:   */   {
/* 13:43 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.DataIntegrityViolationException
 * JD-Core Version:    0.7.0.1
 */