/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ import java.sql.DatabaseMetaData;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ 
/*  7:   */ public class DerbyTableMetaDataProvider
/*  8:   */   extends GenericTableMetaDataProvider
/*  9:   */ {
/* 10:32 */   private boolean supportsGeneratedKeysOverride = false;
/* 11:   */   
/* 12:   */   public DerbyTableMetaDataProvider(DatabaseMetaData databaseMetaData)
/* 13:   */     throws SQLException
/* 14:   */   {
/* 15:35 */     super(databaseMetaData);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void initializeWithMetaData(DatabaseMetaData databaseMetaData)
/* 19:   */     throws SQLException
/* 20:   */   {
/* 21:40 */     super.initializeWithMetaData(databaseMetaData);
/* 22:41 */     if (!databaseMetaData.supportsGetGeneratedKeys())
/* 23:   */     {
/* 24:42 */       logger.warn("Overriding supportsGetGeneratedKeys from DatabaseMetaData to 'true'; it was reported as 'false' by " + 
/* 25:43 */         databaseMetaData.getDriverName() + " " + databaseMetaData.getDriverVersion());
/* 26:44 */       this.supportsGeneratedKeysOverride = true;
/* 27:   */     }
/* 28:   */   }
/* 29:   */   
/* 30:   */   public boolean isGetGeneratedKeysSupported()
/* 31:   */   {
/* 32:50 */     boolean derbysAnswer = super.isGetGeneratedKeysSupported();
/* 33:51 */     if (!derbysAnswer) {
/* 34:52 */       return this.supportsGeneratedKeysOverride;
/* 35:   */     }
/* 36:54 */     return derbysAnswer;
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.DerbyTableMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */