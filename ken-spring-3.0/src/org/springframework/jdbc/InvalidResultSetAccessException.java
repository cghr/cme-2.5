/*  1:   */ package org.springframework.jdbc;
/*  2:   */ 
/*  3:   */ import java.sql.SQLException;
/*  4:   */ import org.springframework.dao.InvalidDataAccessResourceUsageException;
/*  5:   */ 
/*  6:   */ public class InvalidResultSetAccessException
/*  7:   */   extends InvalidDataAccessResourceUsageException
/*  8:   */ {
/*  9:   */   private String sql;
/* 10:   */   
/* 11:   */   public InvalidResultSetAccessException(String task, String sql, SQLException ex)
/* 12:   */   {
/* 13:47 */     super(task + "; invalid ResultSet access for SQL [" + sql + "]", ex);
/* 14:48 */     this.sql = sql;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public InvalidResultSetAccessException(SQLException ex)
/* 18:   */   {
/* 19:56 */     super(ex.getMessage(), ex);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public SQLException getSQLException()
/* 23:   */   {
/* 24:64 */     return (SQLException)getCause();
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getSql()
/* 28:   */   {
/* 29:72 */     return this.sql;
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.InvalidResultSetAccessException
 * JD-Core Version:    0.7.0.1
 */