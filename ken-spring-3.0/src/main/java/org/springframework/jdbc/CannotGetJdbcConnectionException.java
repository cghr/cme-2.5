/*  1:   */ package org.springframework.jdbc;
/*  2:   */ 
/*  3:   */ import java.sql.SQLException;
/*  4:   */ import org.springframework.dao.DataAccessResourceFailureException;
/*  5:   */ 
/*  6:   */ public class CannotGetJdbcConnectionException
/*  7:   */   extends DataAccessResourceFailureException
/*  8:   */ {
/*  9:   */   public CannotGetJdbcConnectionException(String msg, SQLException ex)
/* 10:   */   {
/* 11:36 */     super(msg, ex);
/* 12:   */   }
/* 13:   */   
/* 14:   */   @Deprecated
/* 15:   */   public CannotGetJdbcConnectionException(String msg, ClassNotFoundException ex)
/* 16:   */   {
/* 17:48 */     super(msg, ex);
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.CannotGetJdbcConnectionException
 * JD-Core Version:    0.7.0.1
 */