/*   1:    */ package org.springframework.jdbc.core.metadata;
/*   2:    */ 
/*   3:    */ import java.sql.DatabaseMetaData;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.List;
/*   7:    */ import javax.sql.DataSource;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*  11:    */ import org.springframework.jdbc.support.DatabaseMetaDataCallback;
/*  12:    */ import org.springframework.jdbc.support.JdbcUtils;
/*  13:    */ import org.springframework.jdbc.support.MetaDataAccessException;
/*  14:    */ 
/*  15:    */ public class CallMetaDataProviderFactory
/*  16:    */ {
/*  17: 43 */   private static final Log logger = LogFactory.getLog(CallMetaDataProviderFactory.class);
/*  18: 53 */   public static final List<String> supportedDatabaseProductsForProcedures = Arrays.asList(new String[] {"Apache Derby", "DB2", "MySQL", "Microsoft SQL Server", "Oracle", "PostgreSQL", "Sybase" });
/*  19: 60 */   public static final List<String> supportedDatabaseProductsForFunctions = Arrays.asList(new String[] {"MySQL", "Microsoft SQL Server", "Oracle", "PostgreSQL" });
/*  20:    */   
/*  21:    */   public static CallMetaDataProvider createMetaDataProvider(DataSource dataSource, CallMetaDataContext context)
/*  22:    */   {
/*  23:    */     try
/*  24:    */     {
/*  25: 71 */       (CallMetaDataProvider)JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback()
/*  26:    */       {
/*  27:    */         public Object processMetaData(DatabaseMetaData databaseMetaData)
/*  28:    */           throws SQLException, MetaDataAccessException
/*  29:    */         {
/*  30: 73 */           String databaseProductName = JdbcUtils.commonDatabaseName(databaseMetaData.getDatabaseProductName());
/*  31: 74 */           boolean accessProcedureColumnMetaData = CallMetaDataProviderFactory.this.isAccessCallParameterMetaData();
/*  32: 75 */           if (CallMetaDataProviderFactory.this.isFunction())
/*  33:    */           {
/*  34: 76 */             if (!CallMetaDataProviderFactory.supportedDatabaseProductsForFunctions.contains(databaseProductName))
/*  35:    */             {
/*  36: 77 */               if (CallMetaDataProviderFactory.logger.isWarnEnabled()) {
/*  37: 78 */                 CallMetaDataProviderFactory.logger.warn(databaseProductName + " is not one of the databases fully supported for function calls " + 
/*  38: 79 */                   "-- supported are: " + CallMetaDataProviderFactory.supportedDatabaseProductsForFunctions);
/*  39:    */               }
/*  40: 81 */               if (accessProcedureColumnMetaData)
/*  41:    */               {
/*  42: 82 */                 CallMetaDataProviderFactory.logger.warn("Metadata processing disabled - you must specify all parameters explicitly");
/*  43: 83 */                 accessProcedureColumnMetaData = false;
/*  44:    */               }
/*  45:    */             }
/*  46:    */           }
/*  47: 88 */           else if (!CallMetaDataProviderFactory.supportedDatabaseProductsForProcedures.contains(databaseProductName))
/*  48:    */           {
/*  49: 89 */             if (CallMetaDataProviderFactory.logger.isWarnEnabled()) {
/*  50: 90 */               CallMetaDataProviderFactory.logger.warn(databaseProductName + " is not one of the databases fully supported for procedure calls " + 
/*  51: 91 */                 "-- supported are: " + CallMetaDataProviderFactory.supportedDatabaseProductsForProcedures);
/*  52:    */             }
/*  53: 93 */             if (accessProcedureColumnMetaData)
/*  54:    */             {
/*  55: 94 */               CallMetaDataProviderFactory.logger.warn("Metadata processing disabled - you must specify all parameters explicitly");
/*  56: 95 */               accessProcedureColumnMetaData = false;
/*  57:    */             }
/*  58:    */           }
/*  59:    */           CallMetaDataProvider provider;
/*  60:    */           CallMetaDataProvider provider;
/*  61:101 */           if ("Oracle".equals(databaseProductName))
/*  62:    */           {
/*  63:102 */             provider = new OracleCallMetaDataProvider(databaseMetaData);
/*  64:    */           }
/*  65:    */           else
/*  66:    */           {
/*  67:    */             CallMetaDataProvider provider;
/*  68:104 */             if ("DB2".equals(databaseProductName))
/*  69:    */             {
/*  70:105 */               provider = new Db2CallMetaDataProvider(databaseMetaData);
/*  71:    */             }
/*  72:    */             else
/*  73:    */             {
/*  74:    */               CallMetaDataProvider provider;
/*  75:107 */               if ("Apache Derby".equals(databaseProductName))
/*  76:    */               {
/*  77:108 */                 provider = new DerbyCallMetaDataProvider(databaseMetaData);
/*  78:    */               }
/*  79:    */               else
/*  80:    */               {
/*  81:    */                 CallMetaDataProvider provider;
/*  82:110 */                 if ("PostgreSQL".equals(databaseProductName))
/*  83:    */                 {
/*  84:111 */                   provider = new PostgresCallMetaDataProvider(databaseMetaData);
/*  85:    */                 }
/*  86:    */                 else
/*  87:    */                 {
/*  88:    */                   CallMetaDataProvider provider;
/*  89:113 */                   if ("Sybase".equals(databaseProductName))
/*  90:    */                   {
/*  91:114 */                     provider = new SybaseCallMetaDataProvider(databaseMetaData);
/*  92:    */                   }
/*  93:    */                   else
/*  94:    */                   {
/*  95:    */                     CallMetaDataProvider provider;
/*  96:116 */                     if ("Microsoft SQL Server".equals(databaseProductName)) {
/*  97:117 */                       provider = new SqlServerCallMetaDataProvider(databaseMetaData);
/*  98:    */                     } else {
/*  99:120 */                       provider = new GenericCallMetaDataProvider(databaseMetaData);
/* 100:    */                     }
/* 101:    */                   }
/* 102:    */                 }
/* 103:    */               }
/* 104:    */             }
/* 105:    */           }
/* 106:122 */           if (CallMetaDataProviderFactory.logger.isDebugEnabled()) {
/* 107:123 */             CallMetaDataProviderFactory.logger.debug("Using " + provider.getClass().getName());
/* 108:    */           }
/* 109:125 */           provider.initializeWithMetaData(databaseMetaData);
/* 110:126 */           if (accessProcedureColumnMetaData) {
/* 111:127 */             provider.initializeWithProcedureColumnMetaData(
/* 112:128 */               databaseMetaData, CallMetaDataProviderFactory.this.getCatalogName(), CallMetaDataProviderFactory.this.getSchemaName(), CallMetaDataProviderFactory.this.getProcedureName());
/* 113:    */           }
/* 114:130 */           return provider;
/* 115:    */         }
/* 116:    */       });
/* 117:    */     }
/* 118:    */     catch (MetaDataAccessException ex)
/* 119:    */     {
/* 120:135 */       throw new DataAccessResourceFailureException("Error retreiving database metadata", ex);
/* 121:    */     }
/* 122:    */   }
/* 123:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.CallMetaDataProviderFactory
 * JD-Core Version:    0.7.0.1
 */