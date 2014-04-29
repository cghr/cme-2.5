/*  1:   */ package org.springframework.jdbc;
/*  2:   */ 
/*  3:   */ import java.sql.SQLException;
/*  4:   */ import org.springframework.dao.InvalidDataAccessResourceUsageException;
/*  5:   */ 
/*  6:   */ public class BadSqlGrammarException
/*  7:   */   extends InvalidDataAccessResourceUsageException
/*  8:   */ {
/*  9:   */   private String sql;
/* 10:   */   
/* 11:   */   public BadSqlGrammarException(String task, String sql, SQLException ex)
/* 12:   */   {
/* 13:46 */     super(task + "; bad SQL grammar [" + sql + "]", ex);
/* 14:47 */     this.sql = sql;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public SQLException getSQLException()
/* 18:   */   {
/* 19:55 */     return (SQLException)getCause();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String getSql()
/* 23:   */   {
/* 24:62 */     return this.sql;
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.BadSqlGrammarException
 * JD-Core Version:    0.7.0.1
 */