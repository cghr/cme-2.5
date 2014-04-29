/*   1:    */ package org.springframework.jdbc.core.simple;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import java.util.Set;
/*   9:    */ import javax.sql.DataSource;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  13:    */ import org.springframework.jdbc.core.CallableStatementCreator;
/*  14:    */ import org.springframework.jdbc.core.CallableStatementCreatorFactory;
/*  15:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  16:    */ import org.springframework.jdbc.core.RowMapper;
/*  17:    */ import org.springframework.jdbc.core.SqlParameter;
/*  18:    */ import org.springframework.jdbc.core.metadata.CallMetaDataContext;
/*  19:    */ import org.springframework.jdbc.core.namedparam.SqlParameterSource;
/*  20:    */ import org.springframework.util.Assert;
/*  21:    */ import org.springframework.util.StringUtils;
/*  22:    */ 
/*  23:    */ public abstract class AbstractJdbcCall
/*  24:    */ {
/*  25: 51 */   protected final Log logger = LogFactory.getLog(getClass());
/*  26:    */   private final JdbcTemplate jdbcTemplate;
/*  27: 57 */   private final List<SqlParameter> declaredParameters = new ArrayList();
/*  28: 60 */   private final Map<String, RowMapper> declaredRowMappers = new LinkedHashMap();
/*  29: 67 */   private boolean compiled = false;
/*  30:    */   private String callString;
/*  31: 73 */   private CallMetaDataContext callMetaDataContext = new CallMetaDataContext();
/*  32:    */   private CallableStatementCreatorFactory callableStatementFactory;
/*  33:    */   
/*  34:    */   protected AbstractJdbcCall(DataSource dataSource)
/*  35:    */   {
/*  36: 87 */     this.jdbcTemplate = new JdbcTemplate(dataSource);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected AbstractJdbcCall(JdbcTemplate jdbcTemplate)
/*  40:    */   {
/*  41: 95 */     this.jdbcTemplate = jdbcTemplate;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public JdbcTemplate getJdbcTemplate()
/*  45:    */   {
/*  46:103 */     return this.jdbcTemplate;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected CallableStatementCreatorFactory getCallableStatementFactory()
/*  50:    */   {
/*  51:110 */     return this.callableStatementFactory;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setProcedureName(String procedureName)
/*  55:    */   {
/*  56:118 */     this.callMetaDataContext.setProcedureName(procedureName);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String getProcedureName()
/*  60:    */   {
/*  61:125 */     return this.callMetaDataContext.getProcedureName();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setInParameterNames(Set<String> inParameterNames)
/*  65:    */   {
/*  66:132 */     this.callMetaDataContext.setLimitedInParameterNames(inParameterNames);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Set<String> getInParameterNames()
/*  70:    */   {
/*  71:139 */     return this.callMetaDataContext.getLimitedInParameterNames();
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setCatalogName(String catalogName)
/*  75:    */   {
/*  76:146 */     this.callMetaDataContext.setCatalogName(catalogName);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getCatalogName()
/*  80:    */   {
/*  81:153 */     return this.callMetaDataContext.getCatalogName();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setSchemaName(String schemaName)
/*  85:    */   {
/*  86:160 */     this.callMetaDataContext.setSchemaName(schemaName);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public String getSchemaName()
/*  90:    */   {
/*  91:167 */     return this.callMetaDataContext.getSchemaName();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setFunction(boolean function)
/*  95:    */   {
/*  96:174 */     this.callMetaDataContext.setFunction(function);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public boolean isFunction()
/* 100:    */   {
/* 101:181 */     return this.callMetaDataContext.isFunction();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setReturnValueRequired(boolean b)
/* 105:    */   {
/* 106:188 */     this.callMetaDataContext.setReturnValueRequired(b);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public boolean isReturnValueRequired()
/* 110:    */   {
/* 111:195 */     return this.callMetaDataContext.isReturnValueRequired();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void addDeclaredParameter(SqlParameter parameter)
/* 115:    */   {
/* 116:207 */     Assert.notNull(parameter, "The supplied parameter must not be null");
/* 117:208 */     if (!StringUtils.hasText(parameter.getName())) {
/* 118:209 */       throw new InvalidDataAccessApiUsageException(
/* 119:210 */         "You must specify a parameter name when declaring parameters for \"" + getProcedureName() + "\"");
/* 120:    */     }
/* 121:212 */     this.declaredParameters.add(parameter);
/* 122:213 */     if (this.logger.isDebugEnabled()) {
/* 123:214 */       this.logger.debug("Added declared parameter for [" + getProcedureName() + "]: " + parameter.getName());
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void addDeclaredRowMapper(String parameterName, RowMapper rowMapper)
/* 128:    */   {
/* 129:224 */     this.declaredRowMappers.put(parameterName, rowMapper);
/* 130:225 */     if (this.logger.isDebugEnabled()) {
/* 131:226 */       this.logger.debug("Added row mapper for [" + getProcedureName() + "]: " + parameterName);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   @Deprecated
/* 136:    */   public void addDeclaredRowMapper(String parameterName, ParameterizedRowMapper rowMapper)
/* 137:    */   {
/* 138:236 */     addDeclaredRowMapper(parameterName, rowMapper);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public String getCallString()
/* 142:    */   {
/* 143:243 */     return this.callString;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setAccessCallParameterMetaData(boolean accessCallParameterMetaData)
/* 147:    */   {
/* 148:250 */     this.callMetaDataContext.setAccessCallParameterMetaData(accessCallParameterMetaData);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public final synchronized void compile()
/* 152:    */     throws InvalidDataAccessApiUsageException
/* 153:    */   {
/* 154:266 */     if (!isCompiled())
/* 155:    */     {
/* 156:267 */       if (getProcedureName() == null) {
/* 157:268 */         throw new InvalidDataAccessApiUsageException("Procedure or Function name is required");
/* 158:    */       }
/* 159:    */       try
/* 160:    */       {
/* 161:272 */         this.jdbcTemplate.afterPropertiesSet();
/* 162:    */       }
/* 163:    */       catch (IllegalArgumentException ex)
/* 164:    */       {
/* 165:275 */         throw new InvalidDataAccessApiUsageException(ex.getMessage());
/* 166:    */       }
/* 167:278 */       compileInternal();
/* 168:279 */       this.compiled = true;
/* 169:281 */       if (this.logger.isDebugEnabled()) {
/* 170:282 */         this.logger.debug("SqlCall for " + (isFunction() ? "function" : "procedure") + " [" + getProcedureName() + "] compiled");
/* 171:    */       }
/* 172:    */     }
/* 173:    */   }
/* 174:    */   
/* 175:    */   protected void compileInternal()
/* 176:    */   {
/* 177:292 */     this.callMetaDataContext.initializeMetaData(getJdbcTemplate().getDataSource());
/* 178:295 */     for (Map.Entry<String, RowMapper> entry : this.declaredRowMappers.entrySet())
/* 179:    */     {
/* 180:296 */       SqlParameter resultSetParameter = 
/* 181:297 */         this.callMetaDataContext.createReturnResultSetParameter((String)entry.getKey(), (RowMapper)entry.getValue());
/* 182:298 */       this.declaredParameters.add(resultSetParameter);
/* 183:    */     }
/* 184:300 */     this.callMetaDataContext.processParameters(this.declaredParameters);
/* 185:    */     
/* 186:302 */     this.callString = this.callMetaDataContext.createCallString();
/* 187:303 */     if (this.logger.isDebugEnabled()) {
/* 188:304 */       this.logger.debug("Compiled stored procedure. Call string is [" + this.callString + "]");
/* 189:    */     }
/* 190:307 */     this.callableStatementFactory = 
/* 191:308 */       new CallableStatementCreatorFactory(getCallString(), this.callMetaDataContext.getCallParameters());
/* 192:309 */     this.callableStatementFactory.setNativeJdbcExtractor(getJdbcTemplate().getNativeJdbcExtractor());
/* 193:    */     
/* 194:311 */     onCompileInternal();
/* 195:    */   }
/* 196:    */   
/* 197:    */   protected void onCompileInternal() {}
/* 198:    */   
/* 199:    */   public boolean isCompiled()
/* 200:    */   {
/* 201:326 */     return this.compiled;
/* 202:    */   }
/* 203:    */   
/* 204:    */   protected void checkCompiled()
/* 205:    */   {
/* 206:335 */     if (!isCompiled())
/* 207:    */     {
/* 208:336 */       this.logger.debug("JdbcCall call not compiled before execution - invoking compile");
/* 209:337 */       compile();
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   protected Map<String, Object> doExecute(SqlParameterSource parameterSource)
/* 214:    */   {
/* 215:352 */     checkCompiled();
/* 216:353 */     Map<String, Object> params = matchInParameterValuesWithCallParameters(parameterSource);
/* 217:354 */     return executeCallInternal(params);
/* 218:    */   }
/* 219:    */   
/* 220:    */   protected Map<String, Object> doExecute(Object[] args)
/* 221:    */   {
/* 222:363 */     checkCompiled();
/* 223:364 */     Map<String, ?> params = matchInParameterValuesWithCallParameters(args);
/* 224:365 */     return executeCallInternal(params);
/* 225:    */   }
/* 226:    */   
/* 227:    */   protected Map<String, Object> doExecute(Map<String, ?> args)
/* 228:    */   {
/* 229:374 */     checkCompiled();
/* 230:375 */     Map<String, ?> params = matchInParameterValuesWithCallParameters(args);
/* 231:376 */     return executeCallInternal(params);
/* 232:    */   }
/* 233:    */   
/* 234:    */   private Map<String, Object> executeCallInternal(Map<String, ?> params)
/* 235:    */   {
/* 236:383 */     CallableStatementCreator csc = getCallableStatementFactory().newCallableStatementCreator(params);
/* 237:384 */     if (this.logger.isDebugEnabled())
/* 238:    */     {
/* 239:385 */       this.logger.debug("The following parameters are used for call " + getCallString() + " with: " + params);
/* 240:386 */       int i = 1;
/* 241:387 */       for (SqlParameter p : getCallParameters()) {
/* 242:388 */         this.logger.debug(i++ + ": " + p.getName() + " SQL Type " + p.getSqlType() + " Type Name " + p.getTypeName() + " " + p.getClass().getName());
/* 243:    */       }
/* 244:    */     }
/* 245:391 */     return getJdbcTemplate().call(csc, getCallParameters());
/* 246:    */   }
/* 247:    */   
/* 248:    */   protected String getScalarOutParameterName()
/* 249:    */   {
/* 250:399 */     return this.callMetaDataContext.getScalarOutParameterName();
/* 251:    */   }
/* 252:    */   
/* 253:    */   protected Map<String, Object> matchInParameterValuesWithCallParameters(SqlParameterSource parameterSource)
/* 254:    */   {
/* 255:409 */     return this.callMetaDataContext.matchInParameterValuesWithCallParameters(parameterSource);
/* 256:    */   }
/* 257:    */   
/* 258:    */   private Map<String, ?> matchInParameterValuesWithCallParameters(Object[] args)
/* 259:    */   {
/* 260:419 */     return this.callMetaDataContext.matchInParameterValuesWithCallParameters(args);
/* 261:    */   }
/* 262:    */   
/* 263:    */   protected Map<String, ?> matchInParameterValuesWithCallParameters(Map<String, ?> args)
/* 264:    */   {
/* 265:429 */     return this.callMetaDataContext.matchInParameterValuesWithCallParameters(args);
/* 266:    */   }
/* 267:    */   
/* 268:    */   protected List<SqlParameter> getCallParameters()
/* 269:    */   {
/* 270:437 */     return this.callMetaDataContext.getCallParameters();
/* 271:    */   }
/* 272:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.AbstractJdbcCall
 * JD-Core Version:    0.7.0.1
 */