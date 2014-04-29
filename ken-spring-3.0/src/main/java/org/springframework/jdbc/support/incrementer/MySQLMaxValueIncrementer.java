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
/*  13:    */ public class MySQLMaxValueIncrementer
/*  14:    */   extends AbstractColumnMaxValueIncrementer
/*  15:    */ {
/*  16:    */   private static final String VALUE_SQL = "select last_insert_id()";
/*  17: 62 */   private long nextId = 0L;
/*  18: 65 */   private long maxId = 0L;
/*  19:    */   
/*  20:    */   public MySQLMaxValueIncrementer() {}
/*  21:    */   
/*  22:    */   public MySQLMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName)
/*  23:    */   {
/*  24: 84 */     super(dataSource, incrementerName, columnName);
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected synchronized long getNextKey()
/*  28:    */     throws DataAccessException
/*  29:    */   {
/*  30: 90 */     if (this.maxId == this.nextId)
/*  31:    */     {
/*  32: 96 */       Connection con = DataSourceUtils.getConnection(getDataSource());
/*  33: 97 */       Statement stmt = null;
/*  34:    */       try
/*  35:    */       {
/*  36: 99 */         stmt = con.createStatement();
/*  37:100 */         DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
/*  38:    */         
/*  39:102 */         String columnName = getColumnName();
/*  40:103 */         stmt.executeUpdate("update " + getIncrementerName() + " set " + columnName + 
/*  41:104 */           " = last_insert_id(" + columnName + " + " + getCacheSize() + ")");
/*  42:    */         
/*  43:106 */         ResultSet rs = stmt.executeQuery("select last_insert_id()");
/*  44:    */         try
/*  45:    */         {
/*  46:108 */           if (!rs.next()) {
/*  47:109 */             throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
/*  48:    */           }
/*  49:111 */           this.maxId = rs.getLong(1);
/*  50:    */         }
/*  51:    */         finally
/*  52:    */         {
/*  53:114 */           JdbcUtils.closeResultSet(rs);
/*  54:    */         }
/*  55:116 */         this.nextId = (this.maxId - getCacheSize() + 1L);
/*  56:    */       }
/*  57:    */       catch (SQLException ex)
/*  58:    */       {
/*  59:119 */         throw new DataAccessResourceFailureException("Could not obtain last_insert_id()", ex);
/*  60:    */       }
/*  61:    */       finally
/*  62:    */       {
/*  63:122 */         JdbcUtils.closeStatement(stmt);
/*  64:123 */         DataSourceUtils.releaseConnection(con, getDataSource());
/*  65:    */       }
/*  66:    */     }
/*  67:    */     else
/*  68:    */     {
/*  69:127 */       this.nextId += 1L;
/*  70:    */     }
/*  71:129 */     return this.nextId;
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */