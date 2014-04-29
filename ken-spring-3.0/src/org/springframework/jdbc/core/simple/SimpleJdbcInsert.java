/*   1:    */ package org.springframework.jdbc.core.simple;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.sql.DataSource;
/*   6:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   7:    */ import org.springframework.jdbc.core.namedparam.SqlParameterSource;
/*   8:    */ import org.springframework.jdbc.support.KeyHolder;
/*   9:    */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*  10:    */ 
/*  11:    */ public class SimpleJdbcInsert
/*  12:    */   extends AbstractJdbcInsert
/*  13:    */   implements SimpleJdbcInsertOperations
/*  14:    */ {
/*  15:    */   public SimpleJdbcInsert(DataSource dataSource)
/*  16:    */   {
/*  17: 60 */     super(dataSource);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public SimpleJdbcInsert(JdbcTemplate jdbcTemplate)
/*  21:    */   {
/*  22: 69 */     super(jdbcTemplate);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public SimpleJdbcInsert withTableName(String tableName)
/*  26:    */   {
/*  27: 74 */     setTableName(tableName);
/*  28: 75 */     return this;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public SimpleJdbcInsert withSchemaName(String schemaName)
/*  32:    */   {
/*  33: 79 */     setSchemaName(schemaName);
/*  34: 80 */     return this;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public SimpleJdbcInsert withCatalogName(String catalogName)
/*  38:    */   {
/*  39: 84 */     setCatalogName(catalogName);
/*  40: 85 */     return this;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public SimpleJdbcInsert usingColumns(String... columnNames)
/*  44:    */   {
/*  45: 89 */     setColumnNames(Arrays.asList(columnNames));
/*  46: 90 */     return this;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public SimpleJdbcInsert usingGeneratedKeyColumns(String... columnNames)
/*  50:    */   {
/*  51: 94 */     setGeneratedKeyNames(columnNames);
/*  52: 95 */     return this;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public SimpleJdbcInsertOperations withoutTableColumnMetaDataAccess()
/*  56:    */   {
/*  57: 99 */     setAccessTableColumnMetaData(false);
/*  58:100 */     return this;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public SimpleJdbcInsertOperations includeSynonymsForTableColumnMetaData()
/*  62:    */   {
/*  63:104 */     setOverrideIncludeSynonymsDefault(true);
/*  64:105 */     return this;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public SimpleJdbcInsertOperations useNativeJdbcExtractorForMetaData(NativeJdbcExtractor nativeJdbcExtractor)
/*  68:    */   {
/*  69:109 */     setNativeJdbcExtractor(nativeJdbcExtractor);
/*  70:110 */     return this;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int execute(Map<String, Object> args)
/*  74:    */   {
/*  75:114 */     return doExecute(args);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public int execute(SqlParameterSource parameterSource)
/*  79:    */   {
/*  80:118 */     return doExecute(parameterSource);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Number executeAndReturnKey(Map<String, Object> args)
/*  84:    */   {
/*  85:122 */     return doExecuteAndReturnKey(args);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public Number executeAndReturnKey(SqlParameterSource parameterSource)
/*  89:    */   {
/*  90:126 */     return doExecuteAndReturnKey(parameterSource);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public KeyHolder executeAndReturnKeyHolder(Map<String, Object> args)
/*  94:    */   {
/*  95:130 */     return doExecuteAndReturnKeyHolder(args);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public KeyHolder executeAndReturnKeyHolder(SqlParameterSource parameterSource)
/*  99:    */   {
/* 100:134 */     return doExecuteAndReturnKeyHolder(parameterSource);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public int[] executeBatch(Map<String, Object>[] batch)
/* 104:    */   {
/* 105:138 */     return doExecuteBatch(batch);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public int[] executeBatch(SqlParameterSource[] batch)
/* 109:    */   {
/* 110:142 */     return doExecuteBatch(batch);
/* 111:    */   }
/* 112:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.SimpleJdbcInsert
 * JD-Core Version:    0.7.0.1
 */