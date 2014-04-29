/*   1:    */ package org.springframework.jdbc.core.simple;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.Map;
/*   7:    */ import javax.sql.DataSource;
/*   8:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   9:    */ import org.springframework.jdbc.core.RowMapper;
/*  10:    */ import org.springframework.jdbc.core.SqlParameter;
/*  11:    */ import org.springframework.jdbc.core.namedparam.SqlParameterSource;
/*  12:    */ 
/*  13:    */ public class SimpleJdbcCall
/*  14:    */   extends AbstractJdbcCall
/*  15:    */   implements SimpleJdbcCallOperations
/*  16:    */ {
/*  17:    */   public SimpleJdbcCall(DataSource dataSource)
/*  18:    */   {
/*  19: 69 */     super(dataSource);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public SimpleJdbcCall(JdbcTemplate jdbcTemplate)
/*  23:    */   {
/*  24: 78 */     super(jdbcTemplate);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public SimpleJdbcCall withProcedureName(String procedureName)
/*  28:    */   {
/*  29: 83 */     setProcedureName(procedureName);
/*  30: 84 */     setFunction(false);
/*  31: 85 */     return this;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public SimpleJdbcCall withFunctionName(String functionName)
/*  35:    */   {
/*  36: 89 */     setProcedureName(functionName);
/*  37: 90 */     setFunction(true);
/*  38: 91 */     return this;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public SimpleJdbcCall withSchemaName(String schemaName)
/*  42:    */   {
/*  43: 95 */     setSchemaName(schemaName);
/*  44: 96 */     return this;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public SimpleJdbcCall withCatalogName(String catalogName)
/*  48:    */   {
/*  49:100 */     setCatalogName(catalogName);
/*  50:101 */     return this;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public SimpleJdbcCall withReturnValue()
/*  54:    */   {
/*  55:105 */     setReturnValueRequired(true);
/*  56:106 */     return this;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public SimpleJdbcCall declareParameters(SqlParameter... sqlParameters)
/*  60:    */   {
/*  61:110 */     for (SqlParameter sqlParameter : sqlParameters) {
/*  62:111 */       if (sqlParameter != null) {
/*  63:112 */         addDeclaredParameter(sqlParameter);
/*  64:    */       }
/*  65:    */     }
/*  66:115 */     return this;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public SimpleJdbcCall useInParameterNames(String... inParameterNames)
/*  70:    */   {
/*  71:119 */     setInParameterNames(new HashSet((Collection)Arrays.asList(inParameterNames)));
/*  72:120 */     return this;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public SimpleJdbcCall returningResultSet(String parameterName, RowMapper rowMapper)
/*  76:    */   {
/*  77:124 */     addDeclaredRowMapper(parameterName, rowMapper);
/*  78:125 */     return this;
/*  79:    */   }
/*  80:    */   
/*  81:    */   @Deprecated
/*  82:    */   public SimpleJdbcCall returningResultSet(String parameterName, ParameterizedRowMapper rowMapper)
/*  83:    */   {
/*  84:133 */     addDeclaredRowMapper(parameterName, rowMapper);
/*  85:134 */     return this;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public SimpleJdbcCall withoutProcedureColumnMetaDataAccess()
/*  89:    */   {
/*  90:138 */     setAccessCallParameterMetaData(false);
/*  91:139 */     return this;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public <T> T executeFunction(Class<T> returnType, Object... args)
/*  95:    */   {
/*  96:144 */     return doExecute(args).get(getScalarOutParameterName());
/*  97:    */   }
/*  98:    */   
/*  99:    */   public <T> T executeFunction(Class<T> returnType, Map<String, ?> args)
/* 100:    */   {
/* 101:149 */     return doExecute(args).get(getScalarOutParameterName());
/* 102:    */   }
/* 103:    */   
/* 104:    */   public <T> T executeFunction(Class<T> returnType, SqlParameterSource args)
/* 105:    */   {
/* 106:154 */     return doExecute(args).get(getScalarOutParameterName());
/* 107:    */   }
/* 108:    */   
/* 109:    */   public <T> T executeObject(Class<T> returnType, Object... args)
/* 110:    */   {
/* 111:159 */     return doExecute(args).get(getScalarOutParameterName());
/* 112:    */   }
/* 113:    */   
/* 114:    */   public <T> T executeObject(Class<T> returnType, Map<String, ?> args)
/* 115:    */   {
/* 116:164 */     return doExecute(args).get(getScalarOutParameterName());
/* 117:    */   }
/* 118:    */   
/* 119:    */   public <T> T executeObject(Class<T> returnType, SqlParameterSource args)
/* 120:    */   {
/* 121:169 */     return doExecute(args).get(getScalarOutParameterName());
/* 122:    */   }
/* 123:    */   
/* 124:    */   public Map<String, Object> execute(Object... args)
/* 125:    */   {
/* 126:173 */     return doExecute(args);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public Map<String, Object> execute(Map<String, ?> args)
/* 130:    */   {
/* 131:177 */     return doExecute(args);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public Map<String, Object> execute(SqlParameterSource parameterSource)
/* 135:    */   {
/* 136:181 */     return doExecute(parameterSource);
/* 137:    */   }
/* 138:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.SimpleJdbcCall
 * JD-Core Version:    0.7.0.1
 */