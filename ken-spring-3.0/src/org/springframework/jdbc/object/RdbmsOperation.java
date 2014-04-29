/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.LinkedList;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import javax.sql.DataSource;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  13:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  14:    */ import org.springframework.jdbc.core.SqlParameter;
/*  15:    */ 
/*  16:    */ public abstract class RdbmsOperation
/*  17:    */   implements InitializingBean
/*  18:    */ {
/*  19: 62 */   protected final Log logger = LogFactory.getLog(getClass());
/*  20: 65 */   private JdbcTemplate jdbcTemplate = new JdbcTemplate();
/*  21: 67 */   private int resultSetType = 1003;
/*  22: 69 */   private boolean updatableResults = false;
/*  23: 71 */   private boolean returnGeneratedKeys = false;
/*  24: 73 */   private String[] generatedKeysColumnNames = null;
/*  25:    */   private String sql;
/*  26: 77 */   private final List<SqlParameter> declaredParameters = new LinkedList();
/*  27:    */   private boolean compiled;
/*  28:    */   
/*  29:    */   public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
/*  30:    */   {
/*  31: 94 */     if (jdbcTemplate == null) {
/*  32: 95 */       throw new IllegalArgumentException("jdbcTemplate must not be null");
/*  33:    */     }
/*  34: 97 */     this.jdbcTemplate = jdbcTemplate;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public JdbcTemplate getJdbcTemplate()
/*  38:    */   {
/*  39:104 */     return this.jdbcTemplate;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setDataSource(DataSource dataSource)
/*  43:    */   {
/*  44:112 */     this.jdbcTemplate.setDataSource(dataSource);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setFetchSize(int fetchSize)
/*  48:    */   {
/*  49:124 */     this.jdbcTemplate.setFetchSize(fetchSize);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setMaxRows(int maxRows)
/*  53:    */   {
/*  54:135 */     this.jdbcTemplate.setMaxRows(maxRows);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setQueryTimeout(int queryTimeout)
/*  58:    */   {
/*  59:146 */     this.jdbcTemplate.setQueryTimeout(queryTimeout);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setResultSetType(int resultSetType)
/*  63:    */   {
/*  64:158 */     this.resultSetType = resultSetType;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int getResultSetType()
/*  68:    */   {
/*  69:165 */     return this.resultSetType;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setUpdatableResults(boolean updatableResults)
/*  73:    */   {
/*  74:174 */     if (isCompiled()) {
/*  75:175 */       throw new InvalidDataAccessApiUsageException(
/*  76:176 */         "The updateableResults flag must be set before the operation is compiled");
/*  77:    */     }
/*  78:178 */     this.updatableResults = updatableResults;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean isUpdatableResults()
/*  82:    */   {
/*  83:185 */     return this.updatableResults;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setReturnGeneratedKeys(boolean returnGeneratedKeys)
/*  87:    */   {
/*  88:194 */     if (isCompiled()) {
/*  89:195 */       throw new InvalidDataAccessApiUsageException(
/*  90:196 */         "The returnGeneratedKeys flag must be set before the operation is compiled");
/*  91:    */     }
/*  92:198 */     this.returnGeneratedKeys = returnGeneratedKeys;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean isReturnGeneratedKeys()
/*  96:    */   {
/*  97:206 */     return this.returnGeneratedKeys;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setGeneratedKeysColumnNames(String[] names)
/* 101:    */   {
/* 102:214 */     if (isCompiled()) {
/* 103:215 */       throw new InvalidDataAccessApiUsageException(
/* 104:216 */         "The column names for the generated keys must be set before the operation is compiled");
/* 105:    */     }
/* 106:218 */     this.generatedKeysColumnNames = names;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public String[] getGeneratedKeysColumnNames()
/* 110:    */   {
/* 111:225 */     return this.generatedKeysColumnNames;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setSql(String sql)
/* 115:    */   {
/* 116:232 */     this.sql = sql;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public String getSql()
/* 120:    */   {
/* 121:241 */     return this.sql;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setTypes(int[] types)
/* 125:    */     throws InvalidDataAccessApiUsageException
/* 126:    */   {
/* 127:254 */     if (isCompiled()) {
/* 128:255 */       throw new InvalidDataAccessApiUsageException("Cannot add parameters once query is compiled");
/* 129:    */     }
/* 130:257 */     if (types != null) {
/* 131:258 */       for (int type : types) {
/* 132:259 */         declareParameter(new SqlParameter(type));
/* 133:    */       }
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void declareParameter(SqlParameter param)
/* 138:    */     throws InvalidDataAccessApiUsageException
/* 139:    */   {
/* 140:277 */     if (isCompiled()) {
/* 141:278 */       throw new InvalidDataAccessApiUsageException("Cannot add parameters once the query is compiled");
/* 142:    */     }
/* 143:280 */     this.declaredParameters.add(param);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setParameters(SqlParameter[] parameters)
/* 147:    */   {
/* 148:291 */     if (isCompiled()) {
/* 149:292 */       throw new InvalidDataAccessApiUsageException("Cannot add parameters once the query is compiled");
/* 150:    */     }
/* 151:294 */     for (int i = 0; i < parameters.length; i++) {
/* 152:295 */       if (parameters[i] != null) {
/* 153:296 */         this.declaredParameters.add(parameters[i]);
/* 154:    */       } else {
/* 155:299 */         throw new InvalidDataAccessApiUsageException("Cannot add parameter at index " + i + " from " + 
/* 156:300 */           Arrays.asList(parameters) + " since it is 'null'");
/* 157:    */       }
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   protected List<SqlParameter> getDeclaredParameters()
/* 162:    */   {
/* 163:309 */     return this.declaredParameters;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void afterPropertiesSet()
/* 167:    */   {
/* 168:317 */     compile();
/* 169:    */   }
/* 170:    */   
/* 171:    */   public final void compile()
/* 172:    */     throws InvalidDataAccessApiUsageException
/* 173:    */   {
/* 174:327 */     if (!isCompiled())
/* 175:    */     {
/* 176:328 */       if (getSql() == null) {
/* 177:329 */         throw new InvalidDataAccessApiUsageException("Property 'sql' is required");
/* 178:    */       }
/* 179:    */       try
/* 180:    */       {
/* 181:333 */         this.jdbcTemplate.afterPropertiesSet();
/* 182:    */       }
/* 183:    */       catch (IllegalArgumentException ex)
/* 184:    */       {
/* 185:336 */         throw new InvalidDataAccessApiUsageException(ex.getMessage());
/* 186:    */       }
/* 187:339 */       compileInternal();
/* 188:340 */       this.compiled = true;
/* 189:342 */       if (this.logger.isDebugEnabled()) {
/* 190:343 */         this.logger.debug("RdbmsOperation with SQL [" + getSql() + "] compiled");
/* 191:    */       }
/* 192:    */     }
/* 193:    */   }
/* 194:    */   
/* 195:    */   public boolean isCompiled()
/* 196:    */   {
/* 197:355 */     return this.compiled;
/* 198:    */   }
/* 199:    */   
/* 200:    */   protected void checkCompiled()
/* 201:    */   {
/* 202:365 */     if (!isCompiled())
/* 203:    */     {
/* 204:366 */       this.logger.debug("SQL operation not compiled before execution - invoking compile");
/* 205:367 */       compile();
/* 206:    */     }
/* 207:    */   }
/* 208:    */   
/* 209:    */   protected void validateParameters(Object[] parameters)
/* 210:    */     throws InvalidDataAccessApiUsageException
/* 211:    */   {
/* 212:379 */     checkCompiled();
/* 213:380 */     int declaredInParameters = 0;
/* 214:381 */     for (SqlParameter param : this.declaredParameters) {
/* 215:382 */       if (param.isInputValueProvided())
/* 216:    */       {
/* 217:383 */         if ((!supportsLobParameters()) && (
/* 218:384 */           (param.getSqlType() == 2004) || (param.getSqlType() == 2005))) {
/* 219:385 */           throw new InvalidDataAccessApiUsageException(
/* 220:386 */             "BLOB or CLOB parameters are not allowed for this kind of operation");
/* 221:    */         }
/* 222:388 */         declaredInParameters++;
/* 223:    */       }
/* 224:    */     }
/* 225:391 */     validateParameterCount(parameters != null ? parameters.length : 0, declaredInParameters);
/* 226:    */   }
/* 227:    */   
/* 228:    */   protected void validateNamedParameters(Map<String, ?> parameters)
/* 229:    */     throws InvalidDataAccessApiUsageException
/* 230:    */   {
/* 231:402 */     checkCompiled();
/* 232:403 */     Map paramsToUse = parameters != null ? parameters : Collections.emptyMap();
/* 233:404 */     int declaredInParameters = 0;
/* 234:405 */     for (SqlParameter param : this.declaredParameters) {
/* 235:406 */       if (param.isInputValueProvided())
/* 236:    */       {
/* 237:407 */         if ((!supportsLobParameters()) && (
/* 238:408 */           (param.getSqlType() == 2004) || (param.getSqlType() == 2005))) {
/* 239:409 */           throw new InvalidDataAccessApiUsageException(
/* 240:410 */             "BLOB or CLOB parameters are not allowed for this kind of operation");
/* 241:    */         }
/* 242:412 */         if ((param.getName() != null) && (!paramsToUse.containsKey(param.getName()))) {
/* 243:413 */           throw new InvalidDataAccessApiUsageException("The parameter named '" + param.getName() + 
/* 244:414 */             "' was not among the parameters supplied: " + paramsToUse.keySet());
/* 245:    */         }
/* 246:416 */         declaredInParameters++;
/* 247:    */       }
/* 248:    */     }
/* 249:419 */     validateParameterCount(paramsToUse.size(), declaredInParameters);
/* 250:    */   }
/* 251:    */   
/* 252:    */   private void validateParameterCount(int suppliedParamCount, int declaredInParamCount)
/* 253:    */   {
/* 254:428 */     if (suppliedParamCount < declaredInParamCount) {
/* 255:429 */       throw new InvalidDataAccessApiUsageException(suppliedParamCount + " parameters were supplied, but " + 
/* 256:430 */         declaredInParamCount + " in parameters were declared in class [" + getClass().getName() + "]");
/* 257:    */     }
/* 258:432 */     if ((suppliedParamCount > this.declaredParameters.size()) && (!allowsUnusedParameters())) {
/* 259:433 */       throw new InvalidDataAccessApiUsageException(suppliedParamCount + " parameters were supplied, but " + 
/* 260:434 */         declaredInParamCount + " parameters were declared in class [" + getClass().getName() + "]");
/* 261:    */     }
/* 262:    */   }
/* 263:    */   
/* 264:    */   protected abstract void compileInternal()
/* 265:    */     throws InvalidDataAccessApiUsageException;
/* 266:    */   
/* 267:    */   protected boolean supportsLobParameters()
/* 268:    */   {
/* 269:453 */     return true;
/* 270:    */   }
/* 271:    */   
/* 272:    */   protected boolean allowsUnusedParameters()
/* 273:    */   {
/* 274:463 */     return false;
/* 275:    */   }
/* 276:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.RdbmsOperation
 * JD-Core Version:    0.7.0.1
 */