/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ import java.sql.DatabaseMetaData;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ 
/*  6:   */ public class HsqlTableMetaDataProvider
/*  7:   */   extends GenericTableMetaDataProvider
/*  8:   */ {
/*  9:   */   public HsqlTableMetaDataProvider(DatabaseMetaData databaseMetaData)
/* 10:   */     throws SQLException
/* 11:   */   {
/* 12:32 */     super(databaseMetaData);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public boolean isGetGeneratedKeysSimulated()
/* 16:   */   {
/* 17:38 */     return true;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getSimpleQueryForGetGeneratedKey(String tableName, String keyColumnName)
/* 21:   */   {
/* 22:44 */     return "select max(identity()) from " + tableName;
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.HsqlTableMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */