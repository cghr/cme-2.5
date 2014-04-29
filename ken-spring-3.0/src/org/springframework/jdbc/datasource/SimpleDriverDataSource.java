/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.Driver;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.Properties;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.springframework.beans.BeanUtils;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ 
/*  11:    */ public class SimpleDriverDataSource
/*  12:    */   extends AbstractDriverBasedDataSource
/*  13:    */ {
/*  14:    */   private Driver driver;
/*  15:    */   
/*  16:    */   public SimpleDriverDataSource() {}
/*  17:    */   
/*  18:    */   public SimpleDriverDataSource(Driver driver, String url)
/*  19:    */   {
/*  20: 71 */     setDriver(driver);
/*  21: 72 */     setUrl(url);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public SimpleDriverDataSource(Driver driver, String url, String username, String password)
/*  25:    */   {
/*  26: 84 */     setDriver(driver);
/*  27: 85 */     setUrl(url);
/*  28: 86 */     setUsername(username);
/*  29: 87 */     setPassword(password);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public SimpleDriverDataSource(Driver driver, String url, Properties conProps)
/*  33:    */   {
/*  34: 98 */     setDriver(driver);
/*  35: 99 */     setUrl(url);
/*  36:100 */     setConnectionProperties(conProps);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setDriverClass(Class<? extends Driver> driverClass)
/*  40:    */   {
/*  41:111 */     this.driver = ((Driver)BeanUtils.instantiateClass(driverClass));
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setDriver(Driver driver)
/*  45:    */   {
/*  46:121 */     this.driver = driver;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Driver getDriver()
/*  50:    */   {
/*  51:128 */     return this.driver;
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected Connection getConnectionFromDriver(Properties props)
/*  55:    */     throws SQLException
/*  56:    */   {
/*  57:134 */     Driver driver = getDriver();
/*  58:135 */     String url = getUrl();
/*  59:136 */     Assert.notNull(driver, "Driver must not be null");
/*  60:137 */     if (this.logger.isDebugEnabled()) {
/*  61:138 */       this.logger.debug("Creating new JDBC Driver Connection to [" + url + "]");
/*  62:    */     }
/*  63:140 */     return driver.connect(url, props);
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.SimpleDriverDataSource
 * JD-Core Version:    0.7.0.1
 */