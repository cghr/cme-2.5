/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.io.PrintWriter;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.logging.Logger;
/*   7:    */ import javax.sql.DataSource;
/*   8:    */ import org.springframework.beans.factory.InitializingBean;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ 
/*  11:    */ public class DelegatingDataSource
/*  12:    */   implements DataSource, InitializingBean
/*  13:    */ {
/*  14:    */   private DataSource targetDataSource;
/*  15:    */   
/*  16:    */   public DelegatingDataSource() {}
/*  17:    */   
/*  18:    */   public DelegatingDataSource(DataSource targetDataSource)
/*  19:    */   {
/*  20: 57 */     setTargetDataSource(targetDataSource);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setTargetDataSource(DataSource targetDataSource)
/*  24:    */   {
/*  25: 65 */     Assert.notNull(targetDataSource, "'targetDataSource' must not be null");
/*  26: 66 */     this.targetDataSource = targetDataSource;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public DataSource getTargetDataSource()
/*  30:    */   {
/*  31: 73 */     return this.targetDataSource;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void afterPropertiesSet()
/*  35:    */   {
/*  36: 77 */     if (getTargetDataSource() == null) {
/*  37: 78 */       throw new IllegalArgumentException("Property 'targetDataSource' is required");
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Connection getConnection()
/*  42:    */     throws SQLException
/*  43:    */   {
/*  44: 84 */     return getTargetDataSource().getConnection();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Connection getConnection(String username, String password)
/*  48:    */     throws SQLException
/*  49:    */   {
/*  50: 88 */     return getTargetDataSource().getConnection(username, password);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public PrintWriter getLogWriter()
/*  54:    */     throws SQLException
/*  55:    */   {
/*  56: 92 */     return getTargetDataSource().getLogWriter();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setLogWriter(PrintWriter out)
/*  60:    */     throws SQLException
/*  61:    */   {
/*  62: 96 */     getTargetDataSource().setLogWriter(out);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getLoginTimeout()
/*  66:    */     throws SQLException
/*  67:    */   {
/*  68:100 */     return getTargetDataSource().getLoginTimeout();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setLoginTimeout(int seconds)
/*  72:    */     throws SQLException
/*  73:    */   {
/*  74:104 */     getTargetDataSource().setLoginTimeout(seconds);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public <T> T unwrap(Class<T> iface)
/*  78:    */     throws SQLException
/*  79:    */   {
/*  80:114 */     return getTargetDataSource().unwrap(iface);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public boolean isWrapperFor(Class<?> iface)
/*  84:    */     throws SQLException
/*  85:    */   {
/*  86:118 */     return getTargetDataSource().isWrapperFor(iface);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public Logger getParentLogger()
/*  90:    */   {
/*  91:127 */     return Logger.getLogger("global");
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.DelegatingDataSource
 * JD-Core Version:    0.7.0.1
 */