/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ import java.sql.DatabaseMetaData;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import org.springframework.jdbc.core.ColumnMapRowMapper;
/*  6:   */ import org.springframework.jdbc.core.SqlOutParameter;
/*  7:   */ import org.springframework.jdbc.core.SqlParameter;
/*  8:   */ 
/*  9:   */ public class OracleCallMetaDataProvider
/* 10:   */   extends GenericCallMetaDataProvider
/* 11:   */ {
/* 12:   */   private static final String REF_CURSOR_NAME = "REF CURSOR";
/* 13:   */   
/* 14:   */   public OracleCallMetaDataProvider(DatabaseMetaData databaseMetaData)
/* 15:   */     throws SQLException
/* 16:   */   {
/* 17:40 */     super(databaseMetaData);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public boolean isReturnResultSetSupported()
/* 21:   */   {
/* 22:46 */     return false;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isRefCursorSupported()
/* 26:   */   {
/* 27:51 */     return true;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public int getRefCursorSqlType()
/* 31:   */   {
/* 32:56 */     return -10;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String metaDataCatalogNameToUse(String catalogName)
/* 36:   */   {
/* 37:62 */     return catalogName == null ? "" : catalogNameToUse(catalogName);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public String metaDataSchemaNameToUse(String schemaName)
/* 41:   */   {
/* 42:68 */     return schemaName == null ? getUserName() : super.metaDataSchemaNameToUse(schemaName);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public SqlParameter createDefaultOutParameter(String parameterName, CallParameterMetaData meta)
/* 46:   */   {
/* 47:73 */     if ((meta.getSqlType() == 1111) && ("REF CURSOR".equals(meta.getTypeName()))) {
/* 48:74 */       return new SqlOutParameter(parameterName, getRefCursorSqlType(), new ColumnMapRowMapper());
/* 49:   */     }
/* 50:77 */     return super.createDefaultOutParameter(parameterName, meta);
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.OracleCallMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */