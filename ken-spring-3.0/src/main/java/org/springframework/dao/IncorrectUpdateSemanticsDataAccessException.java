/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class IncorrectUpdateSemanticsDataAccessException
/*  4:   */   extends InvalidDataAccessResourceUsageException
/*  5:   */ {
/*  6:   */   public IncorrectUpdateSemanticsDataAccessException(String msg)
/*  7:   */   {
/*  8:34 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public IncorrectUpdateSemanticsDataAccessException(String msg, Throwable cause)
/* 12:   */   {
/* 13:43 */     super(msg, cause);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean wasDataUpdated()
/* 17:   */   {
/* 18:53 */     return true;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.IncorrectUpdateSemanticsDataAccessException
 * JD-Core Version:    0.7.0.1
 */