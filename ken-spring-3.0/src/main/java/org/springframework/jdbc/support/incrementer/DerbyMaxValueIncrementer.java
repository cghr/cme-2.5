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
/*  13:    */ public class DerbyMaxValueIncrementer
/*  14:    */   extends AbstractColumnMaxValueIncrementer
/*  15:    */ {
/*  16:    */   private static final String DEFAULT_DUMMY_NAME = "dummy";
/*  17: 72 */   private String dummyName = "dummy";
/*  18:    */   private long[] valueCache;
/*  19: 78 */   private int nextValueIndex = -1;
/*  20:    */   
/*  21:    */   public DerbyMaxValueIncrementer() {}
/*  22:    */   
/*  23:    */   public DerbyMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName)
/*  24:    */   {
/*  25: 97 */     super(dataSource, incrementerName, columnName);
/*  26: 98 */     this.dummyName = "dummy";
/*  27:    */   }
/*  28:    */   
/*  29:    */   public DerbyMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName, String dummyName)
/*  30:    */   {
/*  31:109 */     super(dataSource, incrementerName, columnName);
/*  32:110 */     this.dummyName = dummyName;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setDummyName(String dummyName)
/*  36:    */   {
/*  37:118 */     this.dummyName = dummyName;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String getDummyName()
/*  41:    */   {
/*  42:125 */     return this.dummyName;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected synchronized long getNextKey()
/*  46:    */     throws DataAccessException
/*  47:    */   {
/*  48:131 */     if ((this.nextValueIndex < 0) || (this.nextValueIndex >= getCacheSize()))
/*  49:    */     {
/*  50:137 */       Connection con = DataSourceUtils.getConnection(getDataSource());
/*  51:138 */       Statement stmt = null;
/*  52:    */       try
/*  53:    */       {
/*  54:140 */         stmt = con.createStatement();
/*  55:141 */         DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
/*  56:142 */         this.valueCache = new long[getCacheSize()];
/*  57:143 */         this.nextValueIndex = 0;
/*  58:144 */         for (int i = 0; i < getCacheSize(); i++)
/*  59:    */         {
/*  60:145 */           stmt.executeUpdate("insert into " + getIncrementerName() + " (" + getDummyName() + ") values(null)");
/*  61:146 */           ResultSet rs = stmt.executeQuery("select IDENTITY_VAL_LOCAL() from " + getIncrementerName());
/*  62:    */           try
/*  63:    */           {
/*  64:148 */             if (!rs.next()) {
/*  65:149 */               throw new DataAccessResourceFailureException("IDENTITY_VAL_LOCAL() failed after executing an update");
/*  66:    */             }
/*  67:151 */             this.valueCache[i] = rs.getLong(1);
/*  68:    */           }
/*  69:    */           finally
/*  70:    */           {
/*  71:154 */             JdbcUtils.closeResultSet(rs);
/*  72:    */           }
/*  73:    */         }
/*  74:157 */         long maxValue = this.valueCache[(this.valueCache.length - 1)];
/*  75:158 */         stmt.executeUpdate("delete from " + getIncrementerName() + " where " + getColumnName() + " < " + maxValue);
/*  76:    */       }
/*  77:    */       catch (SQLException ex)
/*  78:    */       {
/*  79:161 */         throw new DataAccessResourceFailureException("Could not obtain IDENTITY value", ex);
/*  80:    */       }
/*  81:    */       finally
/*  82:    */       {
/*  83:164 */         JdbcUtils.closeStatement(stmt);
/*  84:165 */         DataSourceUtils.releaseConnection(con, getDataSource());
/*  85:    */       }
/*  86:    */     }
/*  87:168 */     return this.valueCache[(this.nextValueIndex++)];
/*  88:    */   }
/*  89:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.DerbyMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */