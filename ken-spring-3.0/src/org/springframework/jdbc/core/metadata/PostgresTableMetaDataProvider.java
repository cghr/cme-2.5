/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ import java.sql.DatabaseMetaData;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ 
/*  7:   */ public class PostgresTableMetaDataProvider
/*  8:   */   extends GenericTableMetaDataProvider
/*  9:   */ {
/* 10:   */   public PostgresTableMetaDataProvider(DatabaseMetaData databaseMetaData)
/* 11:   */     throws SQLException
/* 12:   */   {
/* 13:16 */     super(databaseMetaData);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean isGetGeneratedKeysSimulated()
/* 17:   */   {
/* 18:22 */     if (getDatabaseVersion().compareTo("8.2.0") >= 0) {
/* 19:23 */       return true;
/* 20:   */     }
/* 21:26 */     logger.warn("PostgreSQL does not support getGeneratedKeys or INSERT ... RETURNING in version " + getDatabaseVersion());
/* 22:27 */     return false;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getSimpleQueryForGetGeneratedKey(String tableName, String keyColumnName)
/* 26:   */   {
/* 27:34 */     return "RETURNING " + keyColumnName;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.PostgresTableMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */