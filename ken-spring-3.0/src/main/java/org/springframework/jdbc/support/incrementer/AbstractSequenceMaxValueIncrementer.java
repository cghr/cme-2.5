/*  1:   */ package org.springframework.jdbc.support.incrementer;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.sql.ResultSet;
/*  5:   */ import java.sql.SQLException;
/*  6:   */ import java.sql.Statement;
/*  7:   */ import javax.sql.DataSource;
/*  8:   */ import org.springframework.dao.DataAccessException;
/*  9:   */ import org.springframework.dao.DataAccessResourceFailureException;
/* 10:   */ import org.springframework.jdbc.datasource.DataSourceUtils;
/* 11:   */ import org.springframework.jdbc.support.JdbcUtils;
/* 12:   */ 
/* 13:   */ public abstract class AbstractSequenceMaxValueIncrementer
/* 14:   */   extends AbstractDataFieldMaxValueIncrementer
/* 15:   */ {
/* 16:   */   public AbstractSequenceMaxValueIncrementer() {}
/* 17:   */   
/* 18:   */   public AbstractSequenceMaxValueIncrementer(DataSource dataSource, String incrementerName)
/* 19:   */   {
/* 20:55 */     super(dataSource, incrementerName);
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected long getNextKey()
/* 24:   */     throws DataAccessException
/* 25:   */   {
/* 26:64 */     Connection con = DataSourceUtils.getConnection(getDataSource());
/* 27:65 */     Statement stmt = null;
/* 28:66 */     ResultSet rs = null;
/* 29:   */     try
/* 30:   */     {
/* 31:68 */       stmt = con.createStatement();
/* 32:69 */       DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
/* 33:70 */       rs = stmt.executeQuery(getSequenceQuery());
/* 34:71 */       if (rs.next()) {
/* 35:72 */         return rs.getLong(1);
/* 36:   */       }
/* 37:75 */       throw new DataAccessResourceFailureException("Sequence query did not return a result");
/* 38:   */     }
/* 39:   */     catch (SQLException ex)
/* 40:   */     {
/* 41:79 */       throw new DataAccessResourceFailureException("Could not obtain sequence value", ex);
/* 42:   */     }
/* 43:   */     finally
/* 44:   */     {
/* 45:82 */       JdbcUtils.closeResultSet(rs);
/* 46:83 */       JdbcUtils.closeStatement(stmt);
/* 47:84 */       DataSourceUtils.releaseConnection(con, getDataSource());
/* 48:   */     }
/* 49:   */   }
/* 50:   */   
/* 51:   */   protected abstract String getSequenceQuery();
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */