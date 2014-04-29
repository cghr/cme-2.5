/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.DriverManager;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.Properties;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ import org.springframework.util.ClassUtils;
/*  10:    */ 
/*  11:    */ public class DriverManagerDataSource
/*  12:    */   extends AbstractDriverBasedDataSource
/*  13:    */ {
/*  14:    */   public DriverManagerDataSource() {}
/*  15:    */   
/*  16:    */   public DriverManagerDataSource(String url)
/*  17:    */   {
/*  18: 81 */     setUrl(url);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public DriverManagerDataSource(String url, String username, String password)
/*  22:    */   {
/*  23: 93 */     setUrl(url);
/*  24: 94 */     setUsername(username);
/*  25: 95 */     setPassword(password);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public DriverManagerDataSource(String url, Properties conProps)
/*  29:    */   {
/*  30:106 */     setUrl(url);
/*  31:107 */     setConnectionProperties(conProps);
/*  32:    */   }
/*  33:    */   
/*  34:    */   @Deprecated
/*  35:    */   public DriverManagerDataSource(String driverClassName, String url, String username, String password)
/*  36:    */   {
/*  37:124 */     setDriverClassName(driverClassName);
/*  38:125 */     setUrl(url);
/*  39:126 */     setUsername(username);
/*  40:127 */     setPassword(password);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setDriverClassName(String driverClassName)
/*  44:    */   {
/*  45:144 */     Assert.hasText(driverClassName, "Property 'driverClassName' must not be empty");
/*  46:145 */     String driverClassNameToUse = driverClassName.trim();
/*  47:    */     try
/*  48:    */     {
/*  49:147 */       Class.forName(driverClassNameToUse, true, ClassUtils.getDefaultClassLoader());
/*  50:    */     }
/*  51:    */     catch (ClassNotFoundException ex)
/*  52:    */     {
/*  53:150 */       throw new IllegalStateException("Could not load JDBC driver class [" + driverClassNameToUse + "]", ex);
/*  54:    */     }
/*  55:152 */     if (this.logger.isInfoEnabled()) {
/*  56:153 */       this.logger.info("Loaded JDBC driver: " + driverClassNameToUse);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected Connection getConnectionFromDriver(Properties props)
/*  61:    */     throws SQLException
/*  62:    */   {
/*  63:160 */     String url = getUrl();
/*  64:161 */     if (this.logger.isDebugEnabled()) {
/*  65:162 */       this.logger.debug("Creating new JDBC DriverManager Connection to [" + url + "]");
/*  66:    */     }
/*  67:164 */     return getConnectionFromDriverManager(url, props);
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected Connection getConnectionFromDriverManager(String url, Properties props)
/*  71:    */     throws SQLException
/*  72:    */   {
/*  73:173 */     return DriverManager.getConnection(url, props);
/*  74:    */   }
/*  75:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.DriverManagerDataSource
 * JD-Core Version:    0.7.0.1
 */