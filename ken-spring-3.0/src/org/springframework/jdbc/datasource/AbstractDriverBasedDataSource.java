/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.Properties;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ 
/*   8:    */ public abstract class AbstractDriverBasedDataSource
/*   9:    */   extends AbstractDataSource
/*  10:    */ {
/*  11:    */   private String url;
/*  12:    */   private String username;
/*  13:    */   private String password;
/*  14:    */   private Properties connectionProperties;
/*  15:    */   
/*  16:    */   public void setUrl(String url)
/*  17:    */   {
/*  18: 50 */     Assert.hasText(url, "Property 'url' must not be empty");
/*  19: 51 */     this.url = url.trim();
/*  20:    */   }
/*  21:    */   
/*  22:    */   public String getUrl()
/*  23:    */   {
/*  24: 58 */     return this.url;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setUsername(String username)
/*  28:    */   {
/*  29: 66 */     this.username = username;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getUsername()
/*  33:    */   {
/*  34: 73 */     return this.username;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setPassword(String password)
/*  38:    */   {
/*  39: 81 */     this.password = password;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getPassword()
/*  43:    */   {
/*  44: 88 */     return this.password;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setConnectionProperties(Properties connectionProperties)
/*  48:    */   {
/*  49:100 */     this.connectionProperties = connectionProperties;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Properties getConnectionProperties()
/*  53:    */   {
/*  54:107 */     return this.connectionProperties;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public Connection getConnection()
/*  58:    */     throws SQLException
/*  59:    */   {
/*  60:119 */     return getConnectionFromDriver(getUsername(), getPassword());
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Connection getConnection(String username, String password)
/*  64:    */     throws SQLException
/*  65:    */   {
/*  66:128 */     return getConnectionFromDriver(username, password);
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected Connection getConnectionFromDriver(String username, String password)
/*  70:    */     throws SQLException
/*  71:    */   {
/*  72:142 */     Properties props = new Properties(getConnectionProperties());
/*  73:143 */     if (username != null) {
/*  74:144 */       props.setProperty("user", username);
/*  75:    */     }
/*  76:146 */     if (password != null) {
/*  77:147 */       props.setProperty("password", password);
/*  78:    */     }
/*  79:149 */     return getConnectionFromDriver(props);
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected abstract Connection getConnectionFromDriver(Properties paramProperties)
/*  83:    */     throws SQLException;
/*  84:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.AbstractDriverBasedDataSource
 * JD-Core Version:    0.7.0.1
 */