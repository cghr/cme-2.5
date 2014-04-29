/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ import java.sql.DatabaseMetaData;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import org.springframework.jdbc.core.ColumnMapRowMapper;
/*  6:   */ import org.springframework.jdbc.core.SqlOutParameter;
/*  7:   */ import org.springframework.jdbc.core.SqlParameter;
/*  8:   */ 
/*  9:   */ public class PostgresCallMetaDataProvider
/* 10:   */   extends GenericCallMetaDataProvider
/* 11:   */ {
/* 12:   */   private static final String RETURN_VALUE_NAME = "returnValue";
/* 13:   */   
/* 14:   */   public PostgresCallMetaDataProvider(DatabaseMetaData databaseMetaData)
/* 15:   */     throws SQLException
/* 16:   */   {
/* 17:24 */     super(databaseMetaData);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public boolean isReturnResultSetSupported()
/* 21:   */   {
/* 22:30 */     return false;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isRefCursorSupported()
/* 26:   */   {
/* 27:35 */     return true;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public int getRefCursorSqlType()
/* 31:   */   {
/* 32:40 */     return 1111;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String metaDataSchemaNameToUse(String schemaName)
/* 36:   */   {
/* 37:46 */     return schemaName == null ? "public" : super.metaDataSchemaNameToUse(schemaName);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public SqlParameter createDefaultOutParameter(String parameterName, CallParameterMetaData meta)
/* 41:   */   {
/* 42:51 */     if ((meta.getSqlType() == 1111) && ("refcursor".equals(meta.getTypeName()))) {
/* 43:52 */       return new SqlOutParameter(parameterName, getRefCursorSqlType(), new ColumnMapRowMapper());
/* 44:   */     }
/* 45:55 */     return super.createDefaultOutParameter(parameterName, meta);
/* 46:   */   }
/* 47:   */   
/* 48:   */   public boolean byPassReturnParameter(String parameterName)
/* 49:   */   {
/* 50:62 */     return ("returnValue".equals(parameterName)) || (super.byPassReturnParameter(parameterName));
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.PostgresCallMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */