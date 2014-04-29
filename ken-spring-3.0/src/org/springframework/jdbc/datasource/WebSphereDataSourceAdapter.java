/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.springframework.util.ReflectionUtils;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ 
/*  11:    */ public class WebSphereDataSourceAdapter
/*  12:    */   extends IsolationLevelDataSourceAdapter
/*  13:    */ {
/*  14: 71 */   protected final Log logger = LogFactory.getLog(getClass());
/*  15:    */   private Class wsDataSourceClass;
/*  16:    */   private Method newJdbcConnSpecMethod;
/*  17:    */   private Method wsDataSourceGetConnectionMethod;
/*  18:    */   private Method setTransactionIsolationMethod;
/*  19:    */   private Method setReadOnlyMethod;
/*  20:    */   private Method setUserNameMethod;
/*  21:    */   private Method setPasswordMethod;
/*  22:    */   
/*  23:    */   public WebSphereDataSourceAdapter()
/*  24:    */   {
/*  25:    */     try
/*  26:    */     {
/*  27: 94 */       this.wsDataSourceClass = getClass().getClassLoader().loadClass("com.ibm.websphere.rsadapter.WSDataSource");
/*  28: 95 */       Class jdbcConnSpecClass = getClass().getClassLoader().loadClass("com.ibm.websphere.rsadapter.JDBCConnectionSpec");
/*  29: 96 */       Class wsrraFactoryClass = getClass().getClassLoader().loadClass("com.ibm.websphere.rsadapter.WSRRAFactory");
/*  30: 97 */       this.newJdbcConnSpecMethod = wsrraFactoryClass.getMethod("createJDBCConnectionSpec", null);
/*  31: 98 */       this.wsDataSourceGetConnectionMethod = 
/*  32: 99 */         this.wsDataSourceClass.getMethod("getConnection", new Class[] { jdbcConnSpecClass });
/*  33:100 */       this.setTransactionIsolationMethod = 
/*  34:101 */         jdbcConnSpecClass.getMethod("setTransactionIsolation", new Class[] { Integer.TYPE });
/*  35:102 */       this.setReadOnlyMethod = jdbcConnSpecClass.getMethod("setReadOnly", new Class[] { Boolean.class });
/*  36:103 */       this.setUserNameMethod = jdbcConnSpecClass.getMethod("setUserName", new Class[] { String.class });
/*  37:104 */       this.setPasswordMethod = jdbcConnSpecClass.getMethod("setPassword", new Class[] { String.class });
/*  38:    */     }
/*  39:    */     catch (Exception ex)
/*  40:    */     {
/*  41:107 */       throw new IllegalStateException(
/*  42:108 */         "Could not initialize WebSphereDataSourceAdapter because WebSphere API classes are not available: " + ex);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void afterPropertiesSet()
/*  47:    */   {
/*  48:118 */     super.afterPropertiesSet();
/*  49:120 */     if (!this.wsDataSourceClass.isInstance(getTargetDataSource())) {
/*  50:121 */       throw new IllegalStateException(
/*  51:122 */         "Specified 'targetDataSource' is not a WebSphere WSDataSource: " + getTargetDataSource());
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected Connection doGetConnection(String username, String password)
/*  56:    */     throws SQLException
/*  57:    */   {
/*  58:136 */     Object connSpec = createConnectionSpec(
/*  59:137 */       getCurrentIsolationLevel(), getCurrentReadOnlyFlag(), username, password);
/*  60:138 */     if (this.logger.isDebugEnabled()) {
/*  61:139 */       this.logger.debug("Obtaining JDBC Connection from WebSphere DataSource [" + 
/*  62:140 */         getTargetDataSource() + "], using ConnectionSpec [" + connSpec + "]");
/*  63:    */     }
/*  64:143 */     return (Connection)ReflectionUtils.invokeJdbcMethod(
/*  65:144 */       this.wsDataSourceGetConnectionMethod, getTargetDataSource(), new Object[] { connSpec });
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected Object createConnectionSpec(Integer isolationLevel, Boolean readOnlyFlag, String username, String password)
/*  69:    */     throws SQLException
/*  70:    */   {
/*  71:163 */     Object connSpec = ReflectionUtils.invokeJdbcMethod(this.newJdbcConnSpecMethod, null);
/*  72:164 */     if (isolationLevel != null) {
/*  73:165 */       ReflectionUtils.invokeJdbcMethod(this.setTransactionIsolationMethod, connSpec, new Object[] { isolationLevel });
/*  74:    */     }
/*  75:167 */     if (readOnlyFlag != null) {
/*  76:168 */       ReflectionUtils.invokeJdbcMethod(this.setReadOnlyMethod, connSpec, new Object[] { readOnlyFlag });
/*  77:    */     }
/*  78:172 */     if (StringUtils.hasLength(username))
/*  79:    */     {
/*  80:173 */       ReflectionUtils.invokeJdbcMethod(this.setUserNameMethod, connSpec, new Object[] { username });
/*  81:174 */       ReflectionUtils.invokeJdbcMethod(this.setPasswordMethod, connSpec, new Object[] { password });
/*  82:    */     }
/*  83:176 */     return connSpec;
/*  84:    */   }
/*  85:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.WebSphereDataSourceAdapter
 * JD-Core Version:    0.7.0.1
 */