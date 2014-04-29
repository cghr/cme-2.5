/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public abstract class UncategorizedDataAccessException
/*  4:   */   extends NonTransientDataAccessException
/*  5:   */ {
/*  6:   */   public UncategorizedDataAccessException(String msg, Throwable cause)
/*  7:   */   {
/*  8:34 */     super(msg, cause);
/*  9:   */   }
/* 10:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.UncategorizedDataAccessException
 * JD-Core Version:    0.7.0.1
 */