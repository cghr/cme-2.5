/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class InvalidDataAccessApiUsageException
/*  4:   */   extends NonTransientDataAccessException
/*  5:   */ {
/*  6:   */   public InvalidDataAccessApiUsageException(String msg)
/*  7:   */   {
/*  8:35 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public InvalidDataAccessApiUsageException(String msg, Throwable cause)
/* 12:   */   {
/* 13:44 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.InvalidDataAccessApiUsageException
 * JD-Core Version:    0.7.0.1
 */