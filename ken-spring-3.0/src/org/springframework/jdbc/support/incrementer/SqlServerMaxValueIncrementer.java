/*   1:    */ package org.springframework.jdbc.support.incrementer;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.ResultSet;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.sql.Statement;
/*   7:    */ import javax.sql.DataSource;
/*   8:    */ import org.springframework.dao.DataAccessException;
/*   9:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*  10:    */ import org.springframework.jdbc.datasource.DataSourceUtils;
/*  11:    */ import org.springframework.jdbc.support.JdbcUtils;
/*  12:    */ 
/*  13:    */ public class SqlServerMaxValueIncrementer
/*  14:    */   extends AbstractColumnMaxValueIncrementer
/*  15:    */ {
/*  16:    */   private long[] valueCache;
/*  17: 53 */   private int nextValueIndex = -1;
/*  18:    */   
/*  19:    */   public SqlServerMaxValueIncrementer() {}
/*  20:    */   
/*  21:    */   public SqlServerMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName)
/*  22:    */   {
/*  23: 72 */     super(dataSource, incrementerName, columnName);
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected synchronized long getNextKey()
/*  27:    */     throws DataAccessException
/*  28:    */   {
/*  29: 78 */     if ((this.nextValueIndex < 0) || (this.nextValueIndex >= getCacheSize()))
/*  30:    */     {
/*  31: 84 */       Connection con = DataSourceUtils.getConnection(getDataSource());
/*  32: 85 */       Statement stmt = null;
/*  33:    */       try
/*  34:    */       {
/*  35: 87 */         stmt = con.createStatement();
/*  36: 88 */         DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
/*  37: 89 */         this.valueCache = new long[getCacheSize()];
/*  38: 90 */         this.nextValueIndex = 0;
/*  39: 91 */         for (int i = 0; i < getCacheSize(); i++)
/*  40:    */         {
/*  41: 92 */           stmt.executeUpdate("insert into " + getIncrementerName() + " default values");
/*  42: 93 */           ResultSet rs = stmt.executeQuery("select @@identity");
/*  43:    */           try
/*  44:    */           {
/*  45: 95 */             if (!rs.next()) {
/*  46: 96 */               throw new DataAccessResourceFailureException("@@identity failed after executing an update");
/*  47:    */             }
/*  48: 98 */             this.valueCache[i] = rs.getLong(1);
/*  49:    */           }
/*  50:    */           finally
/*  51:    */           {
/*  52:101 */             JdbcUtils.closeResultSet(rs);
/*  53:    */           }
/*  54:    */         }
/*  55:104 */         long maxValue = this.valueCache[(this.valueCache.length - 1)];
/*  56:105 */         stmt.executeUpdate("delete from " + getIncrementerName() + " where " + getColumnName() + " < " + maxValue);
/*  57:    */       }
/*  58:    */       catch (SQLException ex)
/*  59:    */       {
/*  60:108 */         throw new DataAccessResourceFailureException("Could not increment identity", ex);
/*  61:    */       }
/*  62:    */       finally
/*  63:    */       {
/*  64:111 */         JdbcUtils.closeStatement(stmt);
/*  65:112 */         DataSourceUtils.releaseConnection(con, getDataSource());
/*  66:    */       }
/*  67:    */     }
/*  68:115 */     return this.valueCache[(this.nextValueIndex++)];
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.SqlServerMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */