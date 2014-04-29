/*   1:    */ package org.springframework.jdbc.support.nativejdbc;
/*   2:    */ 
/*   3:    */ import java.sql.CallableStatement;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.SQLException;
/*   8:    */ import java.sql.Statement;
/*   9:    */ 
/*  10:    */ public class Jdbc4NativeJdbcExtractor
/*  11:    */   extends NativeJdbcExtractorAdapter
/*  12:    */ {
/*  13: 49 */   private Class<? extends Connection> connectionType = Connection.class;
/*  14: 51 */   private Class<? extends Statement> statementType = Statement.class;
/*  15: 53 */   private Class<? extends PreparedStatement> preparedStatementType = PreparedStatement.class;
/*  16: 55 */   private Class<? extends CallableStatement> callableStatementType = CallableStatement.class;
/*  17: 57 */   private Class<? extends ResultSet> resultSetType = ResultSet.class;
/*  18:    */   
/*  19:    */   public void setConnectionType(Class<? extends Connection> connectionType)
/*  20:    */   {
/*  21: 64 */     this.connectionType = connectionType;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setStatementType(Class<? extends Statement> statementType)
/*  25:    */   {
/*  26: 71 */     this.statementType = statementType;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setPreparedStatementType(Class<? extends PreparedStatement> preparedStatementType)
/*  30:    */   {
/*  31: 78 */     this.preparedStatementType = preparedStatementType;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setCallableStatementType(Class<? extends CallableStatement> callableStatementType)
/*  35:    */   {
/*  36: 85 */     this.callableStatementType = callableStatementType;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setResultSetType(Class<? extends ResultSet> resultSetType)
/*  40:    */   {
/*  41: 92 */     this.resultSetType = resultSetType;
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected Connection doGetNativeConnection(Connection con)
/*  45:    */     throws SQLException
/*  46:    */   {
/*  47: 98 */     return (Connection)con.unwrap(this.connectionType);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Statement getNativeStatement(Statement stmt)
/*  51:    */     throws SQLException
/*  52:    */   {
/*  53:103 */     return (Statement)stmt.unwrap(this.statementType);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public PreparedStatement getNativePreparedStatement(PreparedStatement ps)
/*  57:    */     throws SQLException
/*  58:    */   {
/*  59:108 */     return (PreparedStatement)ps.unwrap(this.preparedStatementType);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public CallableStatement getNativeCallableStatement(CallableStatement cs)
/*  63:    */     throws SQLException
/*  64:    */   {
/*  65:113 */     return (CallableStatement)cs.unwrap(this.callableStatementType);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public ResultSet getNativeResultSet(ResultSet rs)
/*  69:    */     throws SQLException
/*  70:    */   {
/*  71:118 */     return (ResultSet)rs.unwrap(this.resultSetType);
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.Jdbc4NativeJdbcExtractor
 * JD-Core Version:    0.7.0.1
 */