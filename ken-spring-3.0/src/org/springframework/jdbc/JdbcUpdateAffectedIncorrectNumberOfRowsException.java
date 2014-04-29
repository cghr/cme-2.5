/*  1:   */ package org.springframework.jdbc;
/*  2:   */ 
/*  3:   */ import org.springframework.dao.IncorrectUpdateSemanticsDataAccessException;
/*  4:   */ 
/*  5:   */ public class JdbcUpdateAffectedIncorrectNumberOfRowsException
/*  6:   */   extends IncorrectUpdateSemanticsDataAccessException
/*  7:   */ {
/*  8:   */   private int expected;
/*  9:   */   private int actual;
/* 10:   */   
/* 11:   */   public JdbcUpdateAffectedIncorrectNumberOfRowsException(String sql, int expected, int actual)
/* 12:   */   {
/* 13:45 */     super("SQL update '" + sql + "' affected " + actual + " rows, not " + expected + " as expected");
/* 14:46 */     this.expected = expected;
/* 15:47 */     this.actual = actual;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public int getExpectedRowsAffected()
/* 19:   */   {
/* 20:55 */     return this.expected;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int getActualRowsAffected()
/* 24:   */   {
/* 25:62 */     return this.actual;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean wasDataUpdated()
/* 29:   */   {
/* 30:67 */     return getActualRowsAffected() > 0;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException
 * JD-Core Version:    0.7.0.1
 */