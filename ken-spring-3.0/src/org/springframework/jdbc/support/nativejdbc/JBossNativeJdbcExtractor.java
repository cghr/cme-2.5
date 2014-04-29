/*   1:    */ package org.springframework.jdbc.support.nativejdbc;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.sql.CallableStatement;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.sql.Statement;
/*  10:    */ import org.springframework.util.ReflectionUtils;
/*  11:    */ 
/*  12:    */ public class JBossNativeJdbcExtractor
/*  13:    */   extends NativeJdbcExtractorAdapter
/*  14:    */ {
/*  15:    */   private static final String WRAPPED_CONNECTION_NAME = "org.jboss.resource.adapter.jdbc.WrappedConnection";
/*  16:    */   private static final String WRAPPED_STATEMENT_NAME = "org.jboss.resource.adapter.jdbc.WrappedStatement";
/*  17:    */   private static final String WRAPPED_RESULT_SET_NAME = "org.jboss.resource.adapter.jdbc.WrappedResultSet";
/*  18:    */   private Class wrappedConnectionClass;
/*  19:    */   private Class wrappedStatementClass;
/*  20:    */   private Class wrappedResultSetClass;
/*  21:    */   private Method getUnderlyingConnectionMethod;
/*  22:    */   private Method getUnderlyingStatementMethod;
/*  23:    */   private Method getUnderlyingResultSetMethod;
/*  24:    */   
/*  25:    */   public JBossNativeJdbcExtractor()
/*  26:    */   {
/*  27:    */     try
/*  28:    */     {
/*  29: 76 */       this.wrappedConnectionClass = getClass().getClassLoader().loadClass("org.jboss.resource.adapter.jdbc.WrappedConnection");
/*  30: 77 */       this.wrappedStatementClass = getClass().getClassLoader().loadClass("org.jboss.resource.adapter.jdbc.WrappedStatement");
/*  31: 78 */       this.wrappedResultSetClass = getClass().getClassLoader().loadClass("org.jboss.resource.adapter.jdbc.WrappedResultSet");
/*  32: 79 */       this.getUnderlyingConnectionMethod = 
/*  33: 80 */         this.wrappedConnectionClass.getMethod("getUnderlyingConnection", null);
/*  34: 81 */       this.getUnderlyingStatementMethod = 
/*  35: 82 */         this.wrappedStatementClass.getMethod("getUnderlyingStatement", null);
/*  36: 83 */       this.getUnderlyingResultSetMethod = 
/*  37: 84 */         this.wrappedResultSetClass.getMethod("getUnderlyingResultSet", null);
/*  38:    */     }
/*  39:    */     catch (Exception ex)
/*  40:    */     {
/*  41: 87 */       throw new IllegalStateException(
/*  42: 88 */         "Could not initialize JBossNativeJdbcExtractor because JBoss API classes are not available: " + ex);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected Connection doGetNativeConnection(Connection con)
/*  47:    */     throws SQLException
/*  48:    */   {
/*  49: 98 */     if (this.wrappedConnectionClass.isAssignableFrom(con.getClass())) {
/*  50: 99 */       return (Connection)ReflectionUtils.invokeJdbcMethod(this.getUnderlyingConnectionMethod, con);
/*  51:    */     }
/*  52:101 */     return con;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Statement getNativeStatement(Statement stmt)
/*  56:    */     throws SQLException
/*  57:    */   {
/*  58:109 */     if (this.wrappedStatementClass.isAssignableFrom(stmt.getClass())) {
/*  59:110 */       return (Statement)ReflectionUtils.invokeJdbcMethod(this.getUnderlyingStatementMethod, stmt);
/*  60:    */     }
/*  61:112 */     return stmt;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public PreparedStatement getNativePreparedStatement(PreparedStatement ps)
/*  65:    */     throws SQLException
/*  66:    */   {
/*  67:120 */     return (PreparedStatement)getNativeStatement(ps);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public CallableStatement getNativeCallableStatement(CallableStatement cs)
/*  71:    */     throws SQLException
/*  72:    */   {
/*  73:128 */     return (CallableStatement)getNativeStatement(cs);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public ResultSet getNativeResultSet(ResultSet rs)
/*  77:    */     throws SQLException
/*  78:    */   {
/*  79:136 */     if (this.wrappedResultSetClass.isAssignableFrom(rs.getClass())) {
/*  80:137 */       return (ResultSet)ReflectionUtils.invokeJdbcMethod(this.getUnderlyingResultSetMethod, rs);
/*  81:    */     }
/*  82:139 */     return rs;
/*  83:    */   }
/*  84:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.JBossNativeJdbcExtractor
 * JD-Core Version:    0.7.0.1
 */