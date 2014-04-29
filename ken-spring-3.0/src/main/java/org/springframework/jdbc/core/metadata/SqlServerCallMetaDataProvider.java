/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ import java.sql.DatabaseMetaData;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ 
/*  6:   */ public class SqlServerCallMetaDataProvider
/*  7:   */   extends GenericCallMetaDataProvider
/*  8:   */ {
/*  9:   */   private static final String REMOVABLE_COLUMN_PREFIX = "@";
/* 10:   */   private static final String RETURN_VALUE_NAME = "@RETURN_VALUE";
/* 11:   */   
/* 12:   */   public SqlServerCallMetaDataProvider(DatabaseMetaData databaseMetaData)
/* 13:   */     throws SQLException
/* 14:   */   {
/* 15:37 */     super(databaseMetaData);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String parameterNameToUse(String parameterName)
/* 19:   */   {
/* 20:43 */     if (parameterName == null) {
/* 21:44 */       return null;
/* 22:   */     }
/* 23:46 */     if ((parameterName.length() > 1) && (parameterName.startsWith("@"))) {
/* 24:47 */       return super.parameterNameToUse(parameterName.substring(1));
/* 25:   */     }
/* 26:50 */     return super.parameterNameToUse(parameterName);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public boolean byPassReturnParameter(String parameterName)
/* 30:   */   {
/* 31:56 */     return ("@RETURN_VALUE".equals(parameterName)) || (super.byPassReturnParameter(parameterName));
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.SqlServerCallMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */