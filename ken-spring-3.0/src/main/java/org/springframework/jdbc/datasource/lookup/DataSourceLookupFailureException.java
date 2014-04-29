/*  1:   */ package org.springframework.jdbc.datasource.lookup;
/*  2:   */ 
/*  3:   */ import org.springframework.dao.NonTransientDataAccessException;
/*  4:   */ 
/*  5:   */ public class DataSourceLookupFailureException
/*  6:   */   extends NonTransientDataAccessException
/*  7:   */ {
/*  8:   */   public DataSourceLookupFailureException(String msg)
/*  9:   */   {
/* 10:35 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public DataSourceLookupFailureException(String msg, Throwable cause)
/* 14:   */   {
/* 15:45 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException
 * JD-Core Version:    0.7.0.1
 */