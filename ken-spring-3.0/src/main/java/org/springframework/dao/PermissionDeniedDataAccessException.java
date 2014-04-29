/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class PermissionDeniedDataAccessException
/*  4:   */   extends NonTransientDataAccessException
/*  5:   */ {
/*  6:   */   public PermissionDeniedDataAccessException(String msg, Throwable cause)
/*  7:   */   {
/*  8:35 */     super(msg, cause);
/*  9:   */   }
/* 10:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.PermissionDeniedDataAccessException
 * JD-Core Version:    0.7.0.1
 */