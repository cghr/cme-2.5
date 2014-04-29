/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ import java.sql.DatabaseMetaData;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ 
/*  7:   */ public class Db2CallMetaDataProvider
/*  8:   */   extends GenericCallMetaDataProvider
/*  9:   */ {
/* 10:   */   public Db2CallMetaDataProvider(DatabaseMetaData databaseMetaData)
/* 11:   */     throws SQLException
/* 12:   */   {
/* 13:33 */     super(databaseMetaData);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void initializeWithMetaData(DatabaseMetaData databaseMetaData)
/* 17:   */     throws SQLException
/* 18:   */   {
/* 19:   */     try
/* 20:   */     {
/* 21:40 */       setSupportsCatalogsInProcedureCalls(databaseMetaData.supportsCatalogsInProcedureCalls());
/* 22:   */     }
/* 23:   */     catch (SQLException se)
/* 24:   */     {
/* 25:43 */       logger.debug("Error retrieving 'DatabaseMetaData.supportsCatalogsInProcedureCalls' - " + se.getMessage());
/* 26:   */     }
/* 27:   */     try
/* 28:   */     {
/* 29:46 */       setSupportsSchemasInProcedureCalls(databaseMetaData.supportsSchemasInProcedureCalls());
/* 30:   */     }
/* 31:   */     catch (SQLException se)
/* 32:   */     {
/* 33:49 */       logger.debug("Error retrieving 'DatabaseMetaData.supportsSchemasInProcedureCalls' - " + se.getMessage());
/* 34:   */     }
/* 35:   */     try
/* 36:   */     {
/* 37:52 */       setStoresUpperCaseIdentifiers(databaseMetaData.storesUpperCaseIdentifiers());
/* 38:   */     }
/* 39:   */     catch (SQLException se)
/* 40:   */     {
/* 41:55 */       logger.debug("Error retrieving 'DatabaseMetaData.storesUpperCaseIdentifiers' - " + se.getMessage());
/* 42:   */     }
/* 43:   */     try
/* 44:   */     {
/* 45:58 */       setStoresLowerCaseIdentifiers(databaseMetaData.storesLowerCaseIdentifiers());
/* 46:   */     }
/* 47:   */     catch (SQLException se)
/* 48:   */     {
/* 49:61 */       logger.debug("Error retrieving 'DatabaseMetaData.storesLowerCaseIdentifiers' - " + se.getMessage());
/* 50:   */     }
/* 51:   */   }
/* 52:   */   
/* 53:   */   public String metaDataSchemaNameToUse(String schemaName)
/* 54:   */   {
/* 55:67 */     if (schemaName != null) {
/* 56:68 */       return super.metaDataSchemaNameToUse(schemaName);
/* 57:   */     }
/* 58:71 */     String userName = getUserName();
/* 59:72 */     return userName != null ? userName.toUpperCase() : null;
/* 60:   */   }
/* 61:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.Db2CallMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */