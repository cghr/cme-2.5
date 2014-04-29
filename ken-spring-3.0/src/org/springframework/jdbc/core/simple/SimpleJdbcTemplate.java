/*   1:    */ package org.springframework.jdbc.core.simple;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.sql.DataSource;
/*   6:    */ import org.springframework.dao.DataAccessException;
/*   7:    */ import org.springframework.jdbc.core.BatchUpdateUtils;
/*   8:    */ import org.springframework.jdbc.core.JdbcOperations;
/*   9:    */ import org.springframework.jdbc.core.RowMapper;
/*  10:    */ import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
/*  11:    */ import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
/*  12:    */ import org.springframework.jdbc.core.namedparam.SqlParameterSource;
/*  13:    */ import org.springframework.util.ObjectUtils;
/*  14:    */ 
/*  15:    */ @Deprecated
/*  16:    */ public class SimpleJdbcTemplate
/*  17:    */   implements SimpleJdbcOperations
/*  18:    */ {
/*  19:    */   private final NamedParameterJdbcOperations namedParameterJdbcOperations;
/*  20:    */   
/*  21:    */   public SimpleJdbcTemplate(DataSource dataSource)
/*  22:    */   {
/*  23: 70 */     this.namedParameterJdbcOperations = new NamedParameterJdbcTemplate(dataSource);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public SimpleJdbcTemplate(JdbcOperations classicJdbcTemplate)
/*  27:    */   {
/*  28: 78 */     this.namedParameterJdbcOperations = new NamedParameterJdbcTemplate(classicJdbcTemplate);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public SimpleJdbcTemplate(NamedParameterJdbcOperations namedParameterJdbcTemplate)
/*  32:    */   {
/*  33: 86 */     this.namedParameterJdbcOperations = namedParameterJdbcTemplate;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public JdbcOperations getJdbcOperations()
/*  37:    */   {
/*  38: 95 */     return this.namedParameterJdbcOperations.getJdbcOperations();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public NamedParameterJdbcOperations getNamedParameterJdbcOperations()
/*  42:    */   {
/*  43:103 */     return this.namedParameterJdbcOperations;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int queryForInt(String sql, Map<String, ?> args)
/*  47:    */     throws DataAccessException
/*  48:    */   {
/*  49:108 */     return getNamedParameterJdbcOperations().queryForInt(sql, args);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int queryForInt(String sql, SqlParameterSource args)
/*  53:    */     throws DataAccessException
/*  54:    */   {
/*  55:112 */     return getNamedParameterJdbcOperations().queryForInt(sql, args);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int queryForInt(String sql, Object... args)
/*  59:    */     throws DataAccessException
/*  60:    */   {
/*  61:116 */     return ObjectUtils.isEmpty(args) ? 
/*  62:117 */       getJdbcOperations().queryForInt(sql) : 
/*  63:118 */       getJdbcOperations().queryForInt(sql, getArguments(args));
/*  64:    */   }
/*  65:    */   
/*  66:    */   public long queryForLong(String sql, Map<String, ?> args)
/*  67:    */     throws DataAccessException
/*  68:    */   {
/*  69:122 */     return getNamedParameterJdbcOperations().queryForLong(sql, args);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public long queryForLong(String sql, SqlParameterSource args)
/*  73:    */     throws DataAccessException
/*  74:    */   {
/*  75:126 */     return getNamedParameterJdbcOperations().queryForLong(sql, args);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public long queryForLong(String sql, Object... args)
/*  79:    */     throws DataAccessException
/*  80:    */   {
/*  81:130 */     return ObjectUtils.isEmpty(args) ? 
/*  82:131 */       getJdbcOperations().queryForLong(sql) : 
/*  83:132 */       getJdbcOperations().queryForLong(sql, getArguments(args));
/*  84:    */   }
/*  85:    */   
/*  86:    */   public <T> T queryForObject(String sql, Class<T> requiredType, Map<String, ?> args)
/*  87:    */     throws DataAccessException
/*  88:    */   {
/*  89:136 */     return getNamedParameterJdbcOperations().queryForObject(sql, args, requiredType);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public <T> T queryForObject(String sql, Class<T> requiredType, SqlParameterSource args)
/*  93:    */     throws DataAccessException
/*  94:    */   {
/*  95:141 */     return getNamedParameterJdbcOperations().queryForObject(sql, args, requiredType);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public <T> T queryForObject(String sql, Class<T> requiredType, Object... args)
/*  99:    */     throws DataAccessException
/* 100:    */   {
/* 101:145 */     return ObjectUtils.isEmpty(args) ? 
/* 102:146 */       getJdbcOperations().queryForObject(sql, requiredType) : 
/* 103:147 */       getJdbcOperations().queryForObject(sql, getArguments(args), requiredType);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public <T> T queryForObject(String sql, RowMapper<T> rm, Map<String, ?> args)
/* 107:    */     throws DataAccessException
/* 108:    */   {
/* 109:151 */     return getNamedParameterJdbcOperations().queryForObject(sql, args, rm);
/* 110:    */   }
/* 111:    */   
/* 112:    */   @Deprecated
/* 113:    */   public <T> T queryForObject(String sql, ParameterizedRowMapper<T> rm, Map<String, ?> args)
/* 114:    */     throws DataAccessException
/* 115:    */   {
/* 116:156 */     return queryForObject(sql, rm, args);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public <T> T queryForObject(String sql, RowMapper<T> rm, SqlParameterSource args)
/* 120:    */     throws DataAccessException
/* 121:    */   {
/* 122:161 */     return getNamedParameterJdbcOperations().queryForObject(sql, args, rm);
/* 123:    */   }
/* 124:    */   
/* 125:    */   @Deprecated
/* 126:    */   public <T> T queryForObject(String sql, ParameterizedRowMapper<T> rm, SqlParameterSource args)
/* 127:    */     throws DataAccessException
/* 128:    */   {
/* 129:167 */     return queryForObject(sql, rm, args);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public <T> T queryForObject(String sql, RowMapper<T> rm, Object... args)
/* 133:    */     throws DataAccessException
/* 134:    */   {
/* 135:171 */     return ObjectUtils.isEmpty(args) ? 
/* 136:172 */       getJdbcOperations().queryForObject(sql, rm) : 
/* 137:173 */       getJdbcOperations().queryForObject(sql, getArguments(args), rm);
/* 138:    */   }
/* 139:    */   
/* 140:    */   @Deprecated
/* 141:    */   public <T> T queryForObject(String sql, ParameterizedRowMapper<T> rm, Object... args)
/* 142:    */     throws DataAccessException
/* 143:    */   {
/* 144:178 */     return queryForObject(sql, rm, args);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public <T> List<T> query(String sql, RowMapper<T> rm, Map<String, ?> args)
/* 148:    */     throws DataAccessException
/* 149:    */   {
/* 150:182 */     return getNamedParameterJdbcOperations().query(sql, args, rm);
/* 151:    */   }
/* 152:    */   
/* 153:    */   @Deprecated
/* 154:    */   public <T> List<T> query(String sql, ParameterizedRowMapper<T> rm, Map<String, ?> args)
/* 155:    */     throws DataAccessException
/* 156:    */   {
/* 157:187 */     return query(sql, rm, args);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public <T> List<T> query(String sql, RowMapper<T> rm, SqlParameterSource args)
/* 161:    */     throws DataAccessException
/* 162:    */   {
/* 163:192 */     return getNamedParameterJdbcOperations().query(sql, args, rm);
/* 164:    */   }
/* 165:    */   
/* 166:    */   @Deprecated
/* 167:    */   public <T> List<T> query(String sql, ParameterizedRowMapper<T> rm, SqlParameterSource args)
/* 168:    */     throws DataAccessException
/* 169:    */   {
/* 170:198 */     return query(sql, rm, args);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public <T> List<T> query(String sql, RowMapper<T> rm, Object... args)
/* 174:    */     throws DataAccessException
/* 175:    */   {
/* 176:202 */     return ObjectUtils.isEmpty(args) ? 
/* 177:203 */       getJdbcOperations().query(sql, rm) : 
/* 178:204 */       getJdbcOperations().query(sql, getArguments(args), rm);
/* 179:    */   }
/* 180:    */   
/* 181:    */   @Deprecated
/* 182:    */   public <T> List<T> query(String sql, ParameterizedRowMapper<T> rm, Object... args)
/* 183:    */     throws DataAccessException
/* 184:    */   {
/* 185:209 */     return query(sql, rm, args);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public Map<String, Object> queryForMap(String sql, Map<String, ?> args)
/* 189:    */     throws DataAccessException
/* 190:    */   {
/* 191:213 */     return getNamedParameterJdbcOperations().queryForMap(sql, args);
/* 192:    */   }
/* 193:    */   
/* 194:    */   public Map<String, Object> queryForMap(String sql, SqlParameterSource args)
/* 195:    */     throws DataAccessException
/* 196:    */   {
/* 197:218 */     return getNamedParameterJdbcOperations().queryForMap(sql, args);
/* 198:    */   }
/* 199:    */   
/* 200:    */   public Map<String, Object> queryForMap(String sql, Object... args)
/* 201:    */     throws DataAccessException
/* 202:    */   {
/* 203:222 */     return ObjectUtils.isEmpty(args) ? 
/* 204:223 */       getJdbcOperations().queryForMap(sql) : 
/* 205:224 */       getJdbcOperations().queryForMap(sql, getArguments(args));
/* 206:    */   }
/* 207:    */   
/* 208:    */   public List<Map<String, Object>> queryForList(String sql, Map<String, ?> args)
/* 209:    */     throws DataAccessException
/* 210:    */   {
/* 211:228 */     return getNamedParameterJdbcOperations().queryForList(sql, args);
/* 212:    */   }
/* 213:    */   
/* 214:    */   public List<Map<String, Object>> queryForList(String sql, SqlParameterSource args)
/* 215:    */     throws DataAccessException
/* 216:    */   {
/* 217:233 */     return getNamedParameterJdbcOperations().queryForList(sql, args);
/* 218:    */   }
/* 219:    */   
/* 220:    */   public List<Map<String, Object>> queryForList(String sql, Object... args)
/* 221:    */     throws DataAccessException
/* 222:    */   {
/* 223:237 */     return ObjectUtils.isEmpty(args) ? 
/* 224:238 */       getJdbcOperations().queryForList(sql) : 
/* 225:239 */       getJdbcOperations().queryForList(sql, getArguments(args));
/* 226:    */   }
/* 227:    */   
/* 228:    */   public int update(String sql, Map<String, ?> args)
/* 229:    */     throws DataAccessException
/* 230:    */   {
/* 231:243 */     return getNamedParameterJdbcOperations().update(sql, args);
/* 232:    */   }
/* 233:    */   
/* 234:    */   public int update(String sql, SqlParameterSource args)
/* 235:    */     throws DataAccessException
/* 236:    */   {
/* 237:247 */     return getNamedParameterJdbcOperations().update(sql, args);
/* 238:    */   }
/* 239:    */   
/* 240:    */   public int update(String sql, Object... args)
/* 241:    */     throws DataAccessException
/* 242:    */   {
/* 243:251 */     return ObjectUtils.isEmpty(args) ? 
/* 244:252 */       getJdbcOperations().update(sql) : 
/* 245:253 */       getJdbcOperations().update(sql, getArguments(args));
/* 246:    */   }
/* 247:    */   
/* 248:    */   public int[] batchUpdate(String sql, List<Object[]> batchArgs)
/* 249:    */   {
/* 250:257 */     return batchUpdate(sql, batchArgs, new int[0]);
/* 251:    */   }
/* 252:    */   
/* 253:    */   public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes)
/* 254:    */   {
/* 255:261 */     return BatchUpdateUtils.executeBatchUpdate(sql, batchArgs, argTypes, getJdbcOperations());
/* 256:    */   }
/* 257:    */   
/* 258:    */   public int[] batchUpdate(String sql, Map<String, ?>[] batchValues)
/* 259:    */   {
/* 260:265 */     return getNamedParameterJdbcOperations().batchUpdate(sql, batchValues);
/* 261:    */   }
/* 262:    */   
/* 263:    */   public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs)
/* 264:    */   {
/* 265:269 */     return getNamedParameterJdbcOperations().batchUpdate(sql, batchArgs);
/* 266:    */   }
/* 267:    */   
/* 268:    */   private Object[] getArguments(Object[] varArgs)
/* 269:    */   {
/* 270:278 */     if ((varArgs.length == 1) && ((varArgs[0] instanceof Object[]))) {
/* 271:279 */       return (Object[])varArgs[0];
/* 272:    */     }
/* 273:282 */     return varArgs;
/* 274:    */   }
/* 275:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.SimpleJdbcTemplate
 * JD-Core Version:    0.7.0.1
 */