/*   1:    */ package org.springframework.jdbc.support.nativejdbc;
/*   2:    */ 
/*   3:    */ import java.sql.CallableStatement;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.DatabaseMetaData;
/*   6:    */ import java.sql.PreparedStatement;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.sql.Statement;
/*  10:    */ import org.springframework.jdbc.datasource.DataSourceUtils;
/*  11:    */ 
/*  12:    */ public abstract class NativeJdbcExtractorAdapter
/*  13:    */   implements NativeJdbcExtractor
/*  14:    */ {
/*  15:    */   public boolean isNativeConnectionNecessaryForNativeStatements()
/*  16:    */   {
/*  17: 65 */     return false;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public boolean isNativeConnectionNecessaryForNativePreparedStatements()
/*  21:    */   {
/*  22: 72 */     return false;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public boolean isNativeConnectionNecessaryForNativeCallableStatements()
/*  26:    */   {
/*  27: 79 */     return false;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Connection getNativeConnection(Connection con)
/*  31:    */     throws SQLException
/*  32:    */   {
/*  33: 95 */     if (con == null) {
/*  34: 96 */       return null;
/*  35:    */     }
/*  36: 98 */     Connection targetCon = DataSourceUtils.getTargetConnection(con);
/*  37: 99 */     Connection nativeCon = doGetNativeConnection(targetCon);
/*  38:100 */     if (nativeCon == targetCon)
/*  39:    */     {
/*  40:104 */       DatabaseMetaData metaData = targetCon.getMetaData();
/*  41:107 */       if (metaData != null)
/*  42:    */       {
/*  43:108 */         Connection metaCon = metaData.getConnection();
/*  44:109 */         if ((metaCon != null) && (metaCon != targetCon)) {
/*  45:112 */           nativeCon = doGetNativeConnection(metaCon);
/*  46:    */         }
/*  47:    */       }
/*  48:    */     }
/*  49:116 */     return nativeCon;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected Connection doGetNativeConnection(Connection con)
/*  53:    */     throws SQLException
/*  54:    */   {
/*  55:123 */     return con;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Connection getNativeConnectionFromStatement(Statement stmt)
/*  59:    */     throws SQLException
/*  60:    */   {
/*  61:132 */     if (stmt == null) {
/*  62:133 */       return null;
/*  63:    */     }
/*  64:135 */     return getNativeConnection(stmt.getConnection());
/*  65:    */   }
/*  66:    */   
/*  67:    */   public Statement getNativeStatement(Statement stmt)
/*  68:    */     throws SQLException
/*  69:    */   {
/*  70:142 */     return stmt;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public PreparedStatement getNativePreparedStatement(PreparedStatement ps)
/*  74:    */     throws SQLException
/*  75:    */   {
/*  76:149 */     return ps;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public CallableStatement getNativeCallableStatement(CallableStatement cs)
/*  80:    */     throws SQLException
/*  81:    */   {
/*  82:156 */     return cs;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public ResultSet getNativeResultSet(ResultSet rs)
/*  86:    */     throws SQLException
/*  87:    */   {
/*  88:163 */     return rs;
/*  89:    */   }
/*  90:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter
 * JD-Core Version:    0.7.0.1
 */