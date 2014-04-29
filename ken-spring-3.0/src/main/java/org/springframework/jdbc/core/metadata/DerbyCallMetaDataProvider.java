/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ import java.sql.DatabaseMetaData;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ 
/*  6:   */ public class DerbyCallMetaDataProvider
/*  7:   */   extends GenericCallMetaDataProvider
/*  8:   */ {
/*  9:   */   public DerbyCallMetaDataProvider(DatabaseMetaData databaseMetaData)
/* 10:   */     throws SQLException
/* 11:   */   {
/* 12:33 */     super(databaseMetaData);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public String metaDataSchemaNameToUse(String schemaName)
/* 16:   */   {
/* 17:38 */     if (schemaName != null) {
/* 18:39 */       return super.metaDataSchemaNameToUse(schemaName);
/* 19:   */     }
/* 20:42 */     String userName = getUserName();
/* 21:43 */     return userName != null ? userName.toUpperCase() : null;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.DerbyCallMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */