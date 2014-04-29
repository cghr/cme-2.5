/*   1:    */ package org.springframework.jdbc.support.nativejdbc;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import org.springframework.util.ReflectionUtils;
/*   7:    */ 
/*   8:    */ public class WebSphereNativeJdbcExtractor
/*   9:    */   extends NativeJdbcExtractorAdapter
/*  10:    */ {
/*  11:    */   private static final String JDBC_ADAPTER_CONNECTION_NAME_5 = "com.ibm.ws.rsadapter.jdbc.WSJdbcConnection";
/*  12:    */   private static final String JDBC_ADAPTER_UTIL_NAME_5 = "com.ibm.ws.rsadapter.jdbc.WSJdbcUtil";
/*  13:    */   private Class webSphere5ConnectionClass;
/*  14:    */   private Method webSphere5NativeConnectionMethod;
/*  15:    */   
/*  16:    */   public WebSphereNativeJdbcExtractor()
/*  17:    */   {
/*  18:    */     try
/*  19:    */     {
/*  20: 61 */       this.webSphere5ConnectionClass = getClass().getClassLoader().loadClass("com.ibm.ws.rsadapter.jdbc.WSJdbcConnection");
/*  21: 62 */       Class jdbcAdapterUtilClass = getClass().getClassLoader().loadClass("com.ibm.ws.rsadapter.jdbc.WSJdbcUtil");
/*  22: 63 */       this.webSphere5NativeConnectionMethod = 
/*  23: 64 */         jdbcAdapterUtilClass.getMethod("getNativeConnection", new Class[] { this.webSphere5ConnectionClass });
/*  24:    */     }
/*  25:    */     catch (Exception ex)
/*  26:    */     {
/*  27: 67 */       throw new IllegalStateException(
/*  28: 68 */         "Could not initialize WebSphereNativeJdbcExtractor because WebSphere API classes are not available: " + ex);
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public boolean isNativeConnectionNecessaryForNativeStatements()
/*  33:    */   {
/*  34: 78 */     return true;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public boolean isNativeConnectionNecessaryForNativePreparedStatements()
/*  38:    */   {
/*  39: 86 */     return true;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean isNativeConnectionNecessaryForNativeCallableStatements()
/*  43:    */   {
/*  44: 94 */     return true;
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected Connection doGetNativeConnection(Connection con)
/*  48:    */     throws SQLException
/*  49:    */   {
/*  50:102 */     if (this.webSphere5ConnectionClass.isAssignableFrom(con.getClass())) {
/*  51:103 */       return (Connection)ReflectionUtils.invokeJdbcMethod(
/*  52:104 */         this.webSphere5NativeConnectionMethod, null, new Object[] { con });
/*  53:    */     }
/*  54:106 */     return con;
/*  55:    */   }
/*  56:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.WebSphereNativeJdbcExtractor
 * JD-Core Version:    0.7.0.1
 */