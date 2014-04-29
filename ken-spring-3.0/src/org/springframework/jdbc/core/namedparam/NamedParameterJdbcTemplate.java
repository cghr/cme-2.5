/*   1:    */ package org.springframework.jdbc.core.namedparam;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashMap;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import javax.sql.DataSource;
/*   8:    */ import org.springframework.dao.DataAccessException;
/*   9:    */ import org.springframework.dao.support.DataAccessUtils;
/*  10:    */ import org.springframework.jdbc.core.ColumnMapRowMapper;
/*  11:    */ import org.springframework.jdbc.core.JdbcOperations;
/*  12:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  13:    */ import org.springframework.jdbc.core.PreparedStatementCallback;
/*  14:    */ import org.springframework.jdbc.core.PreparedStatementCreator;
/*  15:    */ import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
/*  16:    */ import org.springframework.jdbc.core.ResultSetExtractor;
/*  17:    */ import org.springframework.jdbc.core.RowCallbackHandler;
/*  18:    */ import org.springframework.jdbc.core.RowMapper;
/*  19:    */ import org.springframework.jdbc.core.SingleColumnRowMapper;
/*  20:    */ import org.springframework.jdbc.core.SqlParameter;
/*  21:    */ import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
/*  22:    */ import org.springframework.jdbc.support.KeyHolder;
/*  23:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  24:    */ import org.springframework.util.Assert;
/*  25:    */ 
/*  26:    */ public class NamedParameterJdbcTemplate
/*  27:    */   implements NamedParameterJdbcOperations
/*  28:    */ {
/*  29:    */   public static final int DEFAULT_CACHE_LIMIT = 256;
/*  30:    */   private final JdbcOperations classicJdbcTemplate;
/*  31: 70 */   private volatile int cacheLimit = 256;
/*  32: 74 */   private final Map<String, ParsedSql> parsedSqlCache = new LinkedHashMap(256, 0.75F, true)
/*  33:    */   {
/*  34:    */     protected boolean removeEldestEntry(Map.Entry<String, ParsedSql> eldest)
/*  35:    */     {
/*  36: 77 */       return size() > NamedParameterJdbcTemplate.this.getCacheLimit();
/*  37:    */     }
/*  38:    */   };
/*  39:    */   
/*  40:    */   public NamedParameterJdbcTemplate(DataSource dataSource)
/*  41:    */   {
/*  42: 88 */     Assert.notNull(dataSource, "DataSource must not be null");
/*  43: 89 */     this.classicJdbcTemplate = new JdbcTemplate(dataSource);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public NamedParameterJdbcTemplate(JdbcOperations classicJdbcTemplate)
/*  47:    */   {
/*  48: 98 */     Assert.notNull(classicJdbcTemplate, "JdbcTemplate must not be null");
/*  49: 99 */     this.classicJdbcTemplate = classicJdbcTemplate;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public JdbcOperations getJdbcOperations()
/*  53:    */   {
/*  54:108 */     return this.classicJdbcTemplate;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setCacheLimit(int cacheLimit)
/*  58:    */   {
/*  59:116 */     this.cacheLimit = cacheLimit;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int getCacheLimit()
/*  63:    */   {
/*  64:123 */     return this.cacheLimit;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public <T> T execute(String sql, SqlParameterSource paramSource, PreparedStatementCallback<T> action)
/*  68:    */     throws DataAccessException
/*  69:    */   {
/*  70:130 */     return getJdbcOperations().execute(getPreparedStatementCreator(sql, paramSource), action);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public <T> T execute(String sql, Map<String, ?> paramMap, PreparedStatementCallback<T> action)
/*  74:    */     throws DataAccessException
/*  75:    */   {
/*  76:136 */     return execute(sql, new MapSqlParameterSource(paramMap), action);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public <T> T query(String sql, SqlParameterSource paramSource, ResultSetExtractor<T> rse)
/*  80:    */     throws DataAccessException
/*  81:    */   {
/*  82:142 */     return getJdbcOperations().query(getPreparedStatementCreator(sql, paramSource), rse);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public <T> T query(String sql, Map<String, ?> paramMap, ResultSetExtractor<T> rse)
/*  86:    */     throws DataAccessException
/*  87:    */   {
/*  88:148 */     return query(sql, new MapSqlParameterSource(paramMap), rse);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void query(String sql, SqlParameterSource paramSource, RowCallbackHandler rch)
/*  92:    */     throws DataAccessException
/*  93:    */   {
/*  94:154 */     getJdbcOperations().query(getPreparedStatementCreator(sql, paramSource), rch);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void query(String sql, Map<String, ?> paramMap, RowCallbackHandler rch)
/*  98:    */     throws DataAccessException
/*  99:    */   {
/* 100:160 */     query(sql, new MapSqlParameterSource(paramMap), rch);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper)
/* 104:    */     throws DataAccessException
/* 105:    */   {
/* 106:166 */     return getJdbcOperations().query(getPreparedStatementCreator(sql, paramSource), rowMapper);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
/* 110:    */     throws DataAccessException
/* 111:    */   {
/* 112:172 */     return query(sql, new MapSqlParameterSource(paramMap), rowMapper);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public <T> T queryForObject(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper)
/* 116:    */     throws DataAccessException
/* 117:    */   {
/* 118:178 */     List<T> results = getJdbcOperations().query(getPreparedStatementCreator(sql, paramSource), rowMapper);
/* 119:179 */     return DataAccessUtils.requiredSingleResult(results);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public <T> T queryForObject(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
/* 123:    */     throws DataAccessException
/* 124:    */   {
/* 125:185 */     return queryForObject(sql, new MapSqlParameterSource(paramMap), rowMapper);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public <T> T queryForObject(String sql, SqlParameterSource paramSource, Class<T> requiredType)
/* 129:    */     throws DataAccessException
/* 130:    */   {
/* 131:191 */     return queryForObject(sql, paramSource, new SingleColumnRowMapper(requiredType));
/* 132:    */   }
/* 133:    */   
/* 134:    */   public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType)
/* 135:    */     throws DataAccessException
/* 136:    */   {
/* 137:197 */     return queryForObject(sql, paramMap, new SingleColumnRowMapper(requiredType));
/* 138:    */   }
/* 139:    */   
/* 140:    */   public Map<String, Object> queryForMap(String sql, SqlParameterSource paramSource)
/* 141:    */     throws DataAccessException
/* 142:    */   {
/* 143:201 */     return (Map)queryForObject(sql, paramSource, new ColumnMapRowMapper());
/* 144:    */   }
/* 145:    */   
/* 146:    */   public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap)
/* 147:    */     throws DataAccessException
/* 148:    */   {
/* 149:205 */     return (Map)queryForObject(sql, paramMap, new ColumnMapRowMapper());
/* 150:    */   }
/* 151:    */   
/* 152:    */   public long queryForLong(String sql, SqlParameterSource paramSource)
/* 153:    */     throws DataAccessException
/* 154:    */   {
/* 155:209 */     Number number = (Number)queryForObject(sql, paramSource, Long.class);
/* 156:210 */     return number != null ? number.longValue() : 0L;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public long queryForLong(String sql, Map<String, ?> paramMap)
/* 160:    */     throws DataAccessException
/* 161:    */   {
/* 162:214 */     return queryForLong(sql, new MapSqlParameterSource(paramMap));
/* 163:    */   }
/* 164:    */   
/* 165:    */   public int queryForInt(String sql, SqlParameterSource paramSource)
/* 166:    */     throws DataAccessException
/* 167:    */   {
/* 168:218 */     Number number = (Number)queryForObject(sql, paramSource, Integer.class);
/* 169:219 */     return number != null ? number.intValue() : 0;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public int queryForInt(String sql, Map<String, ?> paramMap)
/* 173:    */     throws DataAccessException
/* 174:    */   {
/* 175:223 */     return queryForInt(sql, new MapSqlParameterSource(paramMap));
/* 176:    */   }
/* 177:    */   
/* 178:    */   public <T> List<T> queryForList(String sql, SqlParameterSource paramSource, Class<T> elementType)
/* 179:    */     throws DataAccessException
/* 180:    */   {
/* 181:229 */     return query(sql, paramSource, new SingleColumnRowMapper(elementType));
/* 182:    */   }
/* 183:    */   
/* 184:    */   public <T> List<T> queryForList(String sql, Map<String, ?> paramMap, Class<T> elementType)
/* 185:    */     throws DataAccessException
/* 186:    */   {
/* 187:235 */     return queryForList(sql, new MapSqlParameterSource(paramMap), elementType);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public List<Map<String, Object>> queryForList(String sql, SqlParameterSource paramSource)
/* 191:    */     throws DataAccessException
/* 192:    */   {
/* 193:241 */     return query(sql, paramSource, new ColumnMapRowMapper());
/* 194:    */   }
/* 195:    */   
/* 196:    */   public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap)
/* 197:    */     throws DataAccessException
/* 198:    */   {
/* 199:247 */     return queryForList(sql, new MapSqlParameterSource(paramMap));
/* 200:    */   }
/* 201:    */   
/* 202:    */   public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource)
/* 203:    */     throws DataAccessException
/* 204:    */   {
/* 205:251 */     return (SqlRowSet)getJdbcOperations().query(
/* 206:252 */       getPreparedStatementCreator(sql, paramSource), new SqlRowSetResultSetExtractor());
/* 207:    */   }
/* 208:    */   
/* 209:    */   public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap)
/* 210:    */     throws DataAccessException
/* 211:    */   {
/* 212:256 */     return queryForRowSet(sql, new MapSqlParameterSource(paramMap));
/* 213:    */   }
/* 214:    */   
/* 215:    */   public int update(String sql, SqlParameterSource paramSource)
/* 216:    */     throws DataAccessException
/* 217:    */   {
/* 218:260 */     return getJdbcOperations().update(getPreparedStatementCreator(sql, paramSource));
/* 219:    */   }
/* 220:    */   
/* 221:    */   public int update(String sql, Map<String, ?> paramMap)
/* 222:    */     throws DataAccessException
/* 223:    */   {
/* 224:264 */     return update(sql, new MapSqlParameterSource(paramMap));
/* 225:    */   }
/* 226:    */   
/* 227:    */   public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder)
/* 228:    */     throws DataAccessException
/* 229:    */   {
/* 230:270 */     return update(sql, paramSource, generatedKeyHolder, null);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder, String[] keyColumnNames)
/* 234:    */     throws DataAccessException
/* 235:    */   {
/* 236:277 */     ParsedSql parsedSql = getParsedSql(sql);
/* 237:278 */     String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
/* 238:279 */     Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
/* 239:280 */     List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
/* 240:281 */     PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
/* 241:282 */     if (keyColumnNames != null) {
/* 242:283 */       pscf.setGeneratedKeysColumnNames(keyColumnNames);
/* 243:    */     } else {
/* 244:286 */       pscf.setReturnGeneratedKeys(true);
/* 245:    */     }
/* 246:288 */     return getJdbcOperations().update(pscf.newPreparedStatementCreator(params), generatedKeyHolder);
/* 247:    */   }
/* 248:    */   
/* 249:    */   public int[] batchUpdate(String sql, Map<String, ?>[] batchValues)
/* 250:    */   {
/* 251:292 */     SqlParameterSource[] batchArgs = new SqlParameterSource[batchValues.length];
/* 252:293 */     int i = 0;
/* 253:294 */     for (Map<String, ?> values : batchValues)
/* 254:    */     {
/* 255:295 */       batchArgs[i] = new MapSqlParameterSource(values);
/* 256:296 */       i++;
/* 257:    */     }
/* 258:298 */     return batchUpdate(sql, batchArgs);
/* 259:    */   }
/* 260:    */   
/* 261:    */   public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs)
/* 262:    */   {
/* 263:302 */     ParsedSql parsedSql = getParsedSql(sql);
/* 264:303 */     return NamedParameterBatchUpdateUtils.executeBatchUpdateWithNamedParameters(parsedSql, batchArgs, getJdbcOperations());
/* 265:    */   }
/* 266:    */   
/* 267:    */   protected PreparedStatementCreator getPreparedStatementCreator(String sql, SqlParameterSource paramSource)
/* 268:    */   {
/* 269:314 */     ParsedSql parsedSql = getParsedSql(sql);
/* 270:315 */     String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
/* 271:316 */     Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
/* 272:317 */     List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
/* 273:318 */     PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
/* 274:319 */     return pscf.newPreparedStatementCreator(params);
/* 275:    */   }
/* 276:    */   
/* 277:    */   protected ParsedSql getParsedSql(String sql)
/* 278:    */   {
/* 279:330 */     if (getCacheLimit() <= 0) {
/* 280:331 */       return NamedParameterUtils.parseSqlStatement(sql);
/* 281:    */     }
/* 282:333 */     synchronized (this.parsedSqlCache)
/* 283:    */     {
/* 284:334 */       ParsedSql parsedSql = (ParsedSql)this.parsedSqlCache.get(sql);
/* 285:335 */       if (parsedSql == null)
/* 286:    */       {
/* 287:336 */         parsedSql = NamedParameterUtils.parseSqlStatement(sql);
/* 288:337 */         this.parsedSqlCache.put(sql, parsedSql);
/* 289:    */       }
/* 290:339 */       return parsedSql;
/* 291:    */     }
/* 292:    */   }
/* 293:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
 * JD-Core Version:    0.7.0.1
 */