/*   1:    */ package org.springframework.jdbc.core.metadata;
/*   2:    */ 
/*   3:    */ import java.sql.DatabaseMetaData;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import javax.sql.DataSource;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*   9:    */ import org.springframework.jdbc.support.DatabaseMetaDataCallback;
/*  10:    */ import org.springframework.jdbc.support.JdbcUtils;
/*  11:    */ import org.springframework.jdbc.support.MetaDataAccessException;
/*  12:    */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*  13:    */ 
/*  14:    */ public class TableMetaDataProviderFactory
/*  15:    */ {
/*  16: 40 */   private static final Log logger = LogFactory.getLog(TableMetaDataProviderFactory.class);
/*  17:    */   
/*  18:    */   public static TableMetaDataProvider createMetaDataProvider(DataSource dataSource, TableMetaDataContext context)
/*  19:    */   {
/*  20: 50 */     return createMetaDataProvider(dataSource, context, null);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static TableMetaDataProvider createMetaDataProvider(DataSource dataSource, TableMetaDataContext context, final NativeJdbcExtractor nativeJdbcExtractor)
/*  24:    */   {
/*  25:    */     try
/*  26:    */     {
/*  27: 63 */       (TableMetaDataProvider)JdbcUtils.extractDatabaseMetaData(dataSource, 
/*  28: 64 */         new DatabaseMetaDataCallback()
/*  29:    */         {
/*  30:    */           public Object processMetaData(DatabaseMetaData databaseMetaData)
/*  31:    */             throws SQLException
/*  32:    */           {
/*  33: 66 */             String databaseProductName = 
/*  34: 67 */               JdbcUtils.commonDatabaseName(databaseMetaData.getDatabaseProductName());
/*  35: 68 */             boolean accessTableColumnMetaData = TableMetaDataProviderFactory.this.isAccessTableColumnMetaData();
/*  36:    */             TableMetaDataProvider provider;
/*  37:    */             TableMetaDataProvider provider;
/*  38: 70 */             if ("Oracle".equals(databaseProductName))
/*  39:    */             {
/*  40: 71 */               provider = new OracleTableMetaDataProvider(databaseMetaData, 
/*  41: 72 */                 TableMetaDataProviderFactory.this.isOverrideIncludeSynonymsDefault());
/*  42:    */             }
/*  43:    */             else
/*  44:    */             {
/*  45:    */               TableMetaDataProvider provider;
/*  46: 74 */               if ("HSQL Database Engine".equals(databaseProductName))
/*  47:    */               {
/*  48: 75 */                 provider = new HsqlTableMetaDataProvider(databaseMetaData);
/*  49:    */               }
/*  50:    */               else
/*  51:    */               {
/*  52:    */                 TableMetaDataProvider provider;
/*  53: 77 */                 if ("PostgreSQL".equals(databaseProductName))
/*  54:    */                 {
/*  55: 78 */                   provider = new PostgresTableMetaDataProvider(databaseMetaData);
/*  56:    */                 }
/*  57:    */                 else
/*  58:    */                 {
/*  59:    */                   TableMetaDataProvider provider;
/*  60: 80 */                   if ("Apache Derby".equals(databaseProductName)) {
/*  61: 81 */                     provider = new DerbyTableMetaDataProvider(databaseMetaData);
/*  62:    */                   } else {
/*  63: 84 */                     provider = new GenericTableMetaDataProvider(databaseMetaData);
/*  64:    */                   }
/*  65:    */                 }
/*  66:    */               }
/*  67:    */             }
/*  68: 86 */             if (nativeJdbcExtractor != null) {
/*  69: 87 */               provider.setNativeJdbcExtractor(nativeJdbcExtractor);
/*  70:    */             }
/*  71: 89 */             if (TableMetaDataProviderFactory.logger.isDebugEnabled()) {
/*  72: 90 */               TableMetaDataProviderFactory.logger.debug("Using " + provider.getClass().getSimpleName());
/*  73:    */             }
/*  74: 92 */             provider.initializeWithMetaData(databaseMetaData);
/*  75: 93 */             if (accessTableColumnMetaData) {
/*  76: 94 */               provider.initializeWithTableColumnMetaData(databaseMetaData, TableMetaDataProviderFactory.this.getCatalogName(), 
/*  77: 95 */                 TableMetaDataProviderFactory.this.getSchemaName(), TableMetaDataProviderFactory.this.getTableName());
/*  78:    */             }
/*  79: 97 */             return provider;
/*  80:    */           }
/*  81:    */         });
/*  82:    */     }
/*  83:    */     catch (MetaDataAccessException ex)
/*  84:    */     {
/*  85:102 */       throw new DataAccessResourceFailureException("Error retrieving database metadata", ex);
/*  86:    */     }
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.TableMetaDataProviderFactory
 * JD-Core Version:    0.7.0.1
 */