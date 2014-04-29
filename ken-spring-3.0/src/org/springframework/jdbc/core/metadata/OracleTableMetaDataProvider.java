/*   1:    */ package org.springframework.jdbc.core.metadata;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.sql.CallableStatement;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.DatabaseMetaData;
/*   7:    */ import java.sql.SQLException;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  10:    */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*  11:    */ import org.springframework.util.ReflectionUtils;
/*  12:    */ 
/*  13:    */ public class OracleTableMetaDataProvider
/*  14:    */   extends GenericTableMetaDataProvider
/*  15:    */ {
/*  16:    */   private final boolean includeSynonyms;
/*  17:    */   private String defaultSchema;
/*  18:    */   
/*  19:    */   public OracleTableMetaDataProvider(DatabaseMetaData databaseMetaData)
/*  20:    */     throws SQLException
/*  21:    */   {
/*  22: 50 */     this(databaseMetaData, false);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public OracleTableMetaDataProvider(DatabaseMetaData databaseMetaData, boolean includeSynonyms)
/*  26:    */     throws SQLException
/*  27:    */   {
/*  28: 54 */     super(databaseMetaData);
/*  29: 55 */     this.includeSynonyms = includeSynonyms;
/*  30: 56 */     lookupDefaultSchema(databaseMetaData);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected String getDefaultSchema()
/*  34:    */   {
/*  35: 61 */     if (this.defaultSchema != null) {
/*  36: 62 */       return this.defaultSchema;
/*  37:    */     }
/*  38: 64 */     return super.getDefaultSchema();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void initializeWithTableColumnMetaData(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String tableName)
/*  42:    */     throws SQLException
/*  43:    */   {
/*  44: 71 */     if (!this.includeSynonyms)
/*  45:    */     {
/*  46: 72 */       logger.debug("Defaulting to no synonyms in table metadata lookup");
/*  47: 73 */       super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
/*  48: 74 */       return;
/*  49:    */     }
/*  50: 77 */     Connection con = databaseMetaData.getConnection();
/*  51: 78 */     NativeJdbcExtractor nativeJdbcExtractor = getNativeJdbcExtractor();
/*  52: 79 */     if (nativeJdbcExtractor != null) {
/*  53: 80 */       con = nativeJdbcExtractor.getNativeConnection(con);
/*  54:    */     }
/*  55:    */     boolean isOracleCon;
/*  56:    */     try
/*  57:    */     {
/*  58: 84 */       Class<?> oracleConClass = getClass().getClassLoader().loadClass("oracle.jdbc.OracleConnection");
/*  59: 85 */       isOracleCon = oracleConClass.isInstance(con);
/*  60:    */     }
/*  61:    */     catch (ClassNotFoundException ex)
/*  62:    */     {
/*  63:    */       boolean isOracleCon;
/*  64: 88 */       if (logger.isInfoEnabled()) {
/*  65: 89 */         logger.info("Couldn't find Oracle JDBC API: " + ex);
/*  66:    */       }
/*  67: 91 */       isOracleCon = false;
/*  68:    */     }
/*  69: 94 */     if (!isOracleCon)
/*  70:    */     {
/*  71: 95 */       logger.warn("Unable to include synonyms in table metadata lookup. Connection used for DatabaseMetaData is not recognized as an Oracle connection: " + 
/*  72: 96 */         con);
/*  73: 97 */       super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
/*  74: 98 */       return;
/*  75:    */     }
/*  76:101 */     logger.debug("Including synonyms in table metadata lookup");
/*  77:    */     try
/*  78:    */     {
/*  79:106 */       Method getIncludeSynonyms = con.getClass().getMethod("getIncludeSynonyms", null);
/*  80:107 */       ReflectionUtils.makeAccessible(getIncludeSynonyms);
/*  81:108 */       Boolean originalValueForIncludeSynonyms = (Boolean)getIncludeSynonyms.invoke(con, new Object[0]);
/*  82:    */       
/*  83:110 */       Method setIncludeSynonyms = con.getClass().getMethod("setIncludeSynonyms", new Class[] { Boolean.TYPE });
/*  84:111 */       ReflectionUtils.makeAccessible(setIncludeSynonyms);
/*  85:112 */       setIncludeSynonyms.invoke(con, new Object[] { Boolean.TRUE });
/*  86:    */     }
/*  87:    */     catch (Exception ex)
/*  88:    */     {
/*  89:115 */       throw new InvalidDataAccessApiUsageException("Couldn't prepare Oracle Connection", ex);
/*  90:    */     }
/*  91:    */     Boolean originalValueForIncludeSynonyms;
/*  92:    */     Method setIncludeSynonyms;
/*  93:118 */     super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
/*  94:    */     try
/*  95:    */     {
/*  96:121 */       setIncludeSynonyms.invoke(con, new Object[] { originalValueForIncludeSynonyms });
/*  97:    */     }
/*  98:    */     catch (Exception ex)
/*  99:    */     {
/* 100:124 */       throw new InvalidDataAccessApiUsageException("Couldn't reset Oracle Connection", ex);
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   private void lookupDefaultSchema(DatabaseMetaData databaseMetaData)
/* 105:    */   {
/* 106:    */     try
/* 107:    */     {
/* 108:135 */       CallableStatement cstmt = null;
/* 109:    */       try
/* 110:    */       {
/* 111:137 */         cstmt = databaseMetaData.getConnection().prepareCall("{? = call sys_context('USERENV', 'CURRENT_SCHEMA')}");
/* 112:138 */         cstmt.registerOutParameter(1, 12);
/* 113:139 */         cstmt.execute();
/* 114:140 */         this.defaultSchema = cstmt.getString(1);
/* 115:    */       }
/* 116:    */       finally
/* 117:    */       {
/* 118:143 */         if (cstmt != null) {
/* 119:144 */           cstmt.close();
/* 120:    */         }
/* 121:    */       }
/* 122:    */     }
/* 123:    */     catch (Exception localException) {}
/* 124:    */   }
/* 125:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.OracleTableMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */