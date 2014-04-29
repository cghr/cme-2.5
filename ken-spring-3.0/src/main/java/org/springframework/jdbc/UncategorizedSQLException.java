/*  1:   */ package org.springframework.jdbc;
/*  2:   */ 
/*  3:   */ import java.sql.SQLException;
/*  4:   */ import org.springframework.dao.UncategorizedDataAccessException;
/*  5:   */ 
/*  6:   */ public class UncategorizedSQLException
/*  7:   */   extends UncategorizedDataAccessException
/*  8:   */ {
/*  9:   */   private final String sql;
/* 10:   */   
/* 11:   */   public UncategorizedSQLException(String task, String sql, SQLException ex)
/* 12:   */   {
/* 13:44 */     super(task + "; uncategorized SQLException for SQL [" + sql + "]; SQL state [" + ex.getSQLState() + "]; error code [" + ex.getErrorCode() + "]; " + ex.getMessage(), ex);
/* 14:45 */     this.sql = sql;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public SQLException getSQLException()
/* 18:   */   {
/* 19:53 */     return (SQLException)getCause();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String getSql()
/* 23:   */   {
/* 24:60 */     return this.sql;
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.UncategorizedSQLException
 * JD-Core Version:    0.7.0.1
 */