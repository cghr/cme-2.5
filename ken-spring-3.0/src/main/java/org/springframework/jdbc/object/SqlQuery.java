/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.sql.DataSource;
/*   6:    */ import org.springframework.dao.DataAccessException;
/*   7:    */ import org.springframework.dao.support.DataAccessUtils;
/*   8:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   9:    */ import org.springframework.jdbc.core.RowMapper;
/*  10:    */ import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
/*  11:    */ import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
/*  12:    */ import org.springframework.jdbc.core.namedparam.ParsedSql;
/*  13:    */ 
/*  14:    */ public abstract class SqlQuery<T>
/*  15:    */   extends SqlOperation
/*  16:    */ {
/*  17: 58 */   private int rowsExpected = 0;
/*  18:    */   
/*  19:    */   public SqlQuery() {}
/*  20:    */   
/*  21:    */   public SqlQuery(DataSource ds, String sql)
/*  22:    */   {
/*  23: 76 */     setDataSource(ds);
/*  24: 77 */     setSql(sql);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setRowsExpected(int rowsExpected)
/*  28:    */   {
/*  29: 87 */     this.rowsExpected = rowsExpected;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getRowsExpected()
/*  33:    */   {
/*  34: 94 */     return this.rowsExpected;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public List<T> execute(Object[] params, Map context)
/*  38:    */     throws DataAccessException
/*  39:    */   {
/*  40:110 */     validateParameters(params);
/*  41:111 */     RowMapper<T> rowMapper = newRowMapper(params, context);
/*  42:112 */     return getJdbcTemplate().query(newPreparedStatementCreator(params), rowMapper);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public List<T> execute(Object... params)
/*  46:    */     throws DataAccessException
/*  47:    */   {
/*  48:122 */     return execute(params, null);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public List<T> execute(Map context)
/*  52:    */     throws DataAccessException
/*  53:    */   {
/*  54:130 */     return execute(null, context);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public List<T> execute()
/*  58:    */     throws DataAccessException
/*  59:    */   {
/*  60:137 */     return execute(null);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public List<T> execute(int p1, Map context)
/*  64:    */     throws DataAccessException
/*  65:    */   {
/*  66:146 */     return execute(new Object[] { Integer.valueOf(p1) }, context);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public List<T> execute(int p1)
/*  70:    */     throws DataAccessException
/*  71:    */   {
/*  72:154 */     return execute(p1, null);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public List<T> execute(int p1, int p2, Map context)
/*  76:    */     throws DataAccessException
/*  77:    */   {
/*  78:164 */     return execute(new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }, context);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public List<T> execute(int p1, int p2)
/*  82:    */     throws DataAccessException
/*  83:    */   {
/*  84:173 */     return execute(p1, p2, null);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public List<T> execute(long p1, Map context)
/*  88:    */     throws DataAccessException
/*  89:    */   {
/*  90:182 */     return execute(new Object[] { Long.valueOf(p1) }, context);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public List<T> execute(long p1)
/*  94:    */     throws DataAccessException
/*  95:    */   {
/*  96:190 */     return execute(p1, null);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public List<T> execute(String p1, Map context)
/* 100:    */     throws DataAccessException
/* 101:    */   {
/* 102:199 */     return execute(new Object[] { p1 }, context);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public List<T> execute(String p1)
/* 106:    */     throws DataAccessException
/* 107:    */   {
/* 108:207 */     return execute(p1, null);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public List<T> executeByNamedParam(Map<String, ?> paramMap, Map context)
/* 112:    */     throws DataAccessException
/* 113:    */   {
/* 114:223 */     validateNamedParameters(paramMap);
/* 115:224 */     ParsedSql parsedSql = getParsedSql();
/* 116:225 */     MapSqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
/* 117:226 */     String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
/* 118:227 */     Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, getDeclaredParameters());
/* 119:228 */     RowMapper<T> rowMapper = newRowMapper(params, context);
/* 120:229 */     return getJdbcTemplate().query(newPreparedStatementCreator(sqlToUse, params), rowMapper);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public List<T> executeByNamedParam(Map<String, ?> paramMap)
/* 124:    */     throws DataAccessException
/* 125:    */   {
/* 126:239 */     return executeByNamedParam(paramMap, null);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public T findObject(Object[] params, Map context)
/* 130:    */     throws DataAccessException
/* 131:    */   {
/* 132:252 */     List<T> results = execute(params, context);
/* 133:253 */     return DataAccessUtils.singleResult(results);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public T findObject(Object... params)
/* 137:    */     throws DataAccessException
/* 138:    */   {
/* 139:260 */     return findObject(params, null);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public T findObject(int p1, Map context)
/* 143:    */     throws DataAccessException
/* 144:    */   {
/* 145:268 */     return findObject(new Object[] { Integer.valueOf(p1) }, context);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public T findObject(int p1)
/* 149:    */     throws DataAccessException
/* 150:    */   {
/* 151:275 */     return findObject(p1, null);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public T findObject(int p1, int p2, Map context)
/* 155:    */     throws DataAccessException
/* 156:    */   {
/* 157:283 */     return findObject(new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }, context);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public T findObject(int p1, int p2)
/* 161:    */     throws DataAccessException
/* 162:    */   {
/* 163:290 */     return findObject(p1, p2, null);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public T findObject(long p1, Map context)
/* 167:    */     throws DataAccessException
/* 168:    */   {
/* 169:298 */     return findObject(new Object[] { Long.valueOf(p1) }, context);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public T findObject(long p1)
/* 173:    */     throws DataAccessException
/* 174:    */   {
/* 175:305 */     return findObject(p1, null);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public T findObject(String p1, Map context)
/* 179:    */     throws DataAccessException
/* 180:    */   {
/* 181:313 */     return findObject(new Object[] { p1 }, context);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public T findObject(String p1)
/* 185:    */     throws DataAccessException
/* 186:    */   {
/* 187:320 */     return findObject(p1, null);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public T findObjectByNamedParam(Map<String, ?> paramMap, Map context)
/* 191:    */     throws DataAccessException
/* 192:    */   {
/* 193:335 */     List<T> results = executeByNamedParam(paramMap, context);
/* 194:336 */     return DataAccessUtils.singleResult(results);
/* 195:    */   }
/* 196:    */   
/* 197:    */   public T findObjectByNamedParam(Map<String, ?> paramMap)
/* 198:    */     throws DataAccessException
/* 199:    */   {
/* 200:346 */     return findObjectByNamedParam(paramMap, null);
/* 201:    */   }
/* 202:    */   
/* 203:    */   protected abstract RowMapper<T> newRowMapper(Object[] paramArrayOfObject, Map paramMap);
/* 204:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.SqlQuery
 * JD-Core Version:    0.7.0.1
 */