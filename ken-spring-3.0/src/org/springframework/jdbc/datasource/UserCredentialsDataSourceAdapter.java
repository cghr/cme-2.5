/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import javax.sql.DataSource;
/*   6:    */ import org.springframework.core.NamedThreadLocal;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ 
/*  10:    */ public class UserCredentialsDataSourceAdapter
/*  11:    */   extends DelegatingDataSource
/*  12:    */ {
/*  13:    */   private String username;
/*  14:    */   private String password;
/*  15: 69 */   private final ThreadLocal<JdbcUserCredentials> threadBoundCredentials = new NamedThreadLocal("Current JDBC user credentials");
/*  16:    */   
/*  17:    */   public void setUsername(String username)
/*  18:    */   {
/*  19: 81 */     this.username = username;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setPassword(String password)
/*  23:    */   {
/*  24: 93 */     this.password = password;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setCredentialsForCurrentThread(String username, String password)
/*  28:    */   {
/*  29:108 */     this.threadBoundCredentials.set(new JdbcUserCredentials(username, password, null));
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void removeCredentialsFromCurrentThread()
/*  33:    */   {
/*  34:117 */     this.threadBoundCredentials.remove();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Connection getConnection()
/*  38:    */     throws SQLException
/*  39:    */   {
/*  40:130 */     JdbcUserCredentials threadCredentials = (JdbcUserCredentials)this.threadBoundCredentials.get();
/*  41:131 */     if (threadCredentials != null) {
/*  42:132 */       return doGetConnection(threadCredentials.username, threadCredentials.password);
/*  43:    */     }
/*  44:135 */     return doGetConnection(this.username, this.password);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Connection getConnection(String username, String password)
/*  48:    */     throws SQLException
/*  49:    */   {
/*  50:145 */     return doGetConnection(username, password);
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected Connection doGetConnection(String username, String password)
/*  54:    */     throws SQLException
/*  55:    */   {
/*  56:160 */     Assert.state(getTargetDataSource() != null, "'targetDataSource' is required");
/*  57:161 */     if (StringUtils.hasLength(username)) {
/*  58:162 */       return getTargetDataSource().getConnection(username, password);
/*  59:    */     }
/*  60:165 */     return getTargetDataSource().getConnection();
/*  61:    */   }
/*  62:    */   
/*  63:    */   private static class JdbcUserCredentials
/*  64:    */   {
/*  65:    */     public final String username;
/*  66:    */     public final String password;
/*  67:    */     
/*  68:    */     private JdbcUserCredentials(String username, String password)
/*  69:    */     {
/*  70:180 */       this.username = username;
/*  71:181 */       this.password = password;
/*  72:    */     }
/*  73:    */     
/*  74:    */     public String toString()
/*  75:    */     {
/*  76:186 */       return "JdbcUserCredentials[username='" + this.username + "',password='" + this.password + "']";
/*  77:    */     }
/*  78:    */   }
/*  79:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter
 * JD-Core Version:    0.7.0.1
 */