/*  1:   */ package org.springframework.jdbc.support.nativejdbc;
/*  2:   */ 
/*  3:   */ public class OracleJdbc4NativeJdbcExtractor
/*  4:   */   extends Jdbc4NativeJdbcExtractor
/*  5:   */ {
/*  6:   */   public OracleJdbc4NativeJdbcExtractor()
/*  7:   */   {
/*  8:   */     try
/*  9:   */     {
/* 10:48 */       setConnectionType(getClass().getClassLoader().loadClass("oracle.jdbc.OracleConnection"));
/* 11:49 */       setStatementType(getClass().getClassLoader().loadClass("oracle.jdbc.OracleStatement"));
/* 12:50 */       setPreparedStatementType(getClass().getClassLoader().loadClass("oracle.jdbc.OraclePreparedStatement"));
/* 13:51 */       setCallableStatementType(getClass().getClassLoader().loadClass("oracle.jdbc.OracleCallableStatement"));
/* 14:52 */       setResultSetType(getClass().getClassLoader().loadClass("oracle.jdbc.OracleResultSet"));
/* 15:   */     }
/* 16:   */     catch (Exception ex)
/* 17:   */     {
/* 18:55 */       throw new IllegalStateException(
/* 19:56 */         "Could not initialize OracleJdbc4NativeJdbcExtractor because Oracle API classes are not available: " + ex);
/* 20:   */     }
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.OracleJdbc4NativeJdbcExtractor
 * JD-Core Version:    0.7.0.1
 */