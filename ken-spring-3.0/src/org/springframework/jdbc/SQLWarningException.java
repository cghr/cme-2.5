/*  1:   */ package org.springframework.jdbc;
/*  2:   */ 
/*  3:   */ import java.sql.SQLWarning;
/*  4:   */ import org.springframework.dao.UncategorizedDataAccessException;
/*  5:   */ 
/*  6:   */ public class SQLWarningException
/*  7:   */   extends UncategorizedDataAccessException
/*  8:   */ {
/*  9:   */   public SQLWarningException(String msg, SQLWarning ex)
/* 10:   */   {
/* 11:43 */     super(msg, ex);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public SQLWarning SQLWarning()
/* 15:   */   {
/* 16:50 */     return (SQLWarning)getCause();
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.SQLWarningException
 * JD-Core Version:    0.7.0.1
 */