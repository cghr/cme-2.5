/*   1:    */ package org.springframework.jdbc.support.nativejdbc;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.lang.reflect.Modifier;
/*   5:    */ import java.sql.CallableStatement;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.PreparedStatement;
/*   8:    */ import java.sql.ResultSet;
/*   9:    */ import java.sql.SQLException;
/*  10:    */ import java.sql.Statement;
/*  11:    */ import org.springframework.util.ReflectionUtils;
/*  12:    */ 
/*  13:    */ public class CommonsDbcpNativeJdbcExtractor
/*  14:    */   extends NativeJdbcExtractorAdapter
/*  15:    */ {
/*  16:    */   private static final String GET_INNERMOST_DELEGATE_METHOD_NAME = "getInnermostDelegate";
/*  17:    */   
/*  18:    */   private static Object getInnermostDelegate(Object obj)
/*  19:    */     throws SQLException
/*  20:    */   {
/*  21: 62 */     if (obj == null) {
/*  22: 63 */       return null;
/*  23:    */     }
/*  24:    */     try
/*  25:    */     {
/*  26: 66 */       Class classToAnalyze = obj.getClass();
/*  27: 67 */       while (!Modifier.isPublic(classToAnalyze.getModifiers()))
/*  28:    */       {
/*  29: 68 */         classToAnalyze = classToAnalyze.getSuperclass();
/*  30: 69 */         if (classToAnalyze == null) {
/*  31: 71 */           return obj;
/*  32:    */         }
/*  33:    */       }
/*  34: 74 */       Method getInnermostDelegate = classToAnalyze.getMethod("getInnermostDelegate", null);
/*  35: 75 */       Object delegate = ReflectionUtils.invokeJdbcMethod(getInnermostDelegate, obj);
/*  36: 76 */       return delegate != null ? delegate : obj;
/*  37:    */     }
/*  38:    */     catch (NoSuchMethodException localNoSuchMethodException)
/*  39:    */     {
/*  40: 79 */       return obj;
/*  41:    */     }
/*  42:    */     catch (SecurityException ex)
/*  43:    */     {
/*  44: 82 */       throw new IllegalStateException("Commons DBCP getInnermostDelegate method is not accessible: " + ex);
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected Connection doGetNativeConnection(Connection con)
/*  49:    */     throws SQLException
/*  50:    */   {
/*  51: 89 */     return (Connection)getInnermostDelegate(con);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Statement getNativeStatement(Statement stmt)
/*  55:    */     throws SQLException
/*  56:    */   {
/*  57: 94 */     return (Statement)getInnermostDelegate(stmt);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public PreparedStatement getNativePreparedStatement(PreparedStatement ps)
/*  61:    */     throws SQLException
/*  62:    */   {
/*  63: 99 */     return (PreparedStatement)getNativeStatement(ps);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public CallableStatement getNativeCallableStatement(CallableStatement cs)
/*  67:    */     throws SQLException
/*  68:    */   {
/*  69:104 */     return (CallableStatement)getNativeStatement(cs);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public ResultSet getNativeResultSet(ResultSet rs)
/*  73:    */     throws SQLException
/*  74:    */   {
/*  75:109 */     return (ResultSet)getInnermostDelegate(rs);
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor
 * JD-Core Version:    0.7.0.1
 */