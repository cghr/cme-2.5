/*   1:    */ package org.springframework.jdbc.support.nativejdbc;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import org.springframework.util.ReflectionUtils;
/*   7:    */ 
/*   8:    */ public class WebLogicNativeJdbcExtractor
/*   9:    */   extends NativeJdbcExtractorAdapter
/*  10:    */ {
/*  11:    */   private static final String JDBC_EXTENSION_NAME = "weblogic.jdbc.extensions.WLConnection";
/*  12:    */   private final Class jdbcExtensionClass;
/*  13:    */   private final Method getVendorConnectionMethod;
/*  14:    */   
/*  15:    */   public WebLogicNativeJdbcExtractor()
/*  16:    */   {
/*  17:    */     try
/*  18:    */     {
/*  19: 60 */       this.jdbcExtensionClass = getClass().getClassLoader().loadClass("weblogic.jdbc.extensions.WLConnection");
/*  20: 61 */       this.getVendorConnectionMethod = this.jdbcExtensionClass.getMethod("getVendorConnection", null);
/*  21:    */     }
/*  22:    */     catch (Exception ex)
/*  23:    */     {
/*  24: 64 */       throw new IllegalStateException(
/*  25: 65 */         "Could not initialize WebLogicNativeJdbcExtractor because WebLogic API classes are not available: " + ex);
/*  26:    */     }
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isNativeConnectionNecessaryForNativeStatements()
/*  30:    */   {
/*  31: 75 */     return true;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean isNativeConnectionNecessaryForNativePreparedStatements()
/*  35:    */   {
/*  36: 83 */     return true;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public boolean isNativeConnectionNecessaryForNativeCallableStatements()
/*  40:    */   {
/*  41: 91 */     return true;
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected Connection doGetNativeConnection(Connection con)
/*  45:    */     throws SQLException
/*  46:    */   {
/*  47: 99 */     if (this.jdbcExtensionClass.isAssignableFrom(con.getClass())) {
/*  48:100 */       return (Connection)ReflectionUtils.invokeJdbcMethod(this.getVendorConnectionMethod, con);
/*  49:    */     }
/*  50:102 */     return con;
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.WebLogicNativeJdbcExtractor
 * JD-Core Version:    0.7.0.1
 */