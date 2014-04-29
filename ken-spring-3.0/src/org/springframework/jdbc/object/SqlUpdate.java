/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import javax.sql.DataSource;
/*   5:    */ import org.springframework.dao.DataAccessException;
/*   6:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*   7:    */ import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
/*   8:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   9:    */ import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
/*  10:    */ import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
/*  11:    */ import org.springframework.jdbc.core.namedparam.ParsedSql;
/*  12:    */ import org.springframework.jdbc.support.KeyHolder;
/*  13:    */ 
/*  14:    */ public class SqlUpdate
/*  15:    */   extends SqlOperation
/*  16:    */ {
/*  17: 56 */   private int maxRowsAffected = 0;
/*  18: 62 */   private int requiredRowsAffected = 0;
/*  19:    */   
/*  20:    */   public SqlUpdate() {}
/*  21:    */   
/*  22:    */   public SqlUpdate(DataSource ds, String sql)
/*  23:    */   {
/*  24: 80 */     setDataSource(ds);
/*  25: 81 */     setSql(sql);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public SqlUpdate(DataSource ds, String sql, int[] types)
/*  29:    */   {
/*  30: 94 */     setDataSource(ds);
/*  31: 95 */     setSql(sql);
/*  32: 96 */     setTypes(types);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public SqlUpdate(DataSource ds, String sql, int[] types, int maxRowsAffected)
/*  36:    */   {
/*  37:112 */     setDataSource(ds);
/*  38:113 */     setSql(sql);
/*  39:114 */     setTypes(types);
/*  40:115 */     this.maxRowsAffected = maxRowsAffected;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setMaxRowsAffected(int maxRowsAffected)
/*  44:    */   {
/*  45:126 */     this.maxRowsAffected = maxRowsAffected;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setRequiredRowsAffected(int requiredRowsAffected)
/*  49:    */   {
/*  50:138 */     this.requiredRowsAffected = requiredRowsAffected;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected void checkRowsAffected(int rowsAffected)
/*  54:    */     throws JdbcUpdateAffectedIncorrectNumberOfRowsException
/*  55:    */   {
/*  56:151 */     if ((this.maxRowsAffected > 0) && (rowsAffected > this.maxRowsAffected)) {
/*  57:152 */       throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(getSql(), this.maxRowsAffected, rowsAffected);
/*  58:    */     }
/*  59:154 */     if ((this.requiredRowsAffected > 0) && (rowsAffected != this.requiredRowsAffected)) {
/*  60:155 */       throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(getSql(), this.requiredRowsAffected, rowsAffected);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int update(Object... params)
/*  65:    */     throws DataAccessException
/*  66:    */   {
/*  67:167 */     validateParameters(params);
/*  68:168 */     int rowsAffected = getJdbcTemplate().update(newPreparedStatementCreator(params));
/*  69:169 */     checkRowsAffected(rowsAffected);
/*  70:170 */     return rowsAffected;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int update(Object[] params, KeyHolder generatedKeyHolder)
/*  74:    */     throws DataAccessException
/*  75:    */   {
/*  76:181 */     if ((!isReturnGeneratedKeys()) && (getGeneratedKeysColumnNames() == null)) {
/*  77:182 */       throw new InvalidDataAccessApiUsageException(
/*  78:183 */         "The update method taking a KeyHolder should only be used when generated keys have been configured by calling either 'setReturnGeneratedKeys' or 'setGeneratedKeysColumnNames'.");
/*  79:    */     }
/*  80:187 */     validateParameters(params);
/*  81:188 */     int rowsAffected = getJdbcTemplate().update(newPreparedStatementCreator(params), generatedKeyHolder);
/*  82:189 */     checkRowsAffected(rowsAffected);
/*  83:190 */     return rowsAffected;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int update()
/*  87:    */     throws DataAccessException
/*  88:    */   {
/*  89:197 */     return update(null);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public int update(int p1)
/*  93:    */     throws DataAccessException
/*  94:    */   {
/*  95:204 */     return update(new Object[] { Integer.valueOf(p1) });
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int update(int p1, int p2)
/*  99:    */     throws DataAccessException
/* 100:    */   {
/* 101:211 */     return update(new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) });
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int update(long p1)
/* 105:    */     throws DataAccessException
/* 106:    */   {
/* 107:218 */     return update(new Object[] { Long.valueOf(p1) });
/* 108:    */   }
/* 109:    */   
/* 110:    */   public int update(long p1, long p2)
/* 111:    */     throws DataAccessException
/* 112:    */   {
/* 113:225 */     return update(new Object[] { Long.valueOf(p1), Long.valueOf(p2) });
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int update(String p)
/* 117:    */     throws DataAccessException
/* 118:    */   {
/* 119:232 */     return update(new Object[] { p });
/* 120:    */   }
/* 121:    */   
/* 122:    */   public int update(String p1, String p2)
/* 123:    */     throws DataAccessException
/* 124:    */   {
/* 125:239 */     return update(new Object[] { p1, p2 });
/* 126:    */   }
/* 127:    */   
/* 128:    */   public int updateByNamedParam(Map<String, ?> paramMap)
/* 129:    */     throws DataAccessException
/* 130:    */   {
/* 131:250 */     validateNamedParameters(paramMap);
/* 132:251 */     ParsedSql parsedSql = getParsedSql();
/* 133:252 */     MapSqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
/* 134:253 */     String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
/* 135:254 */     Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, getDeclaredParameters());
/* 136:255 */     int rowsAffected = getJdbcTemplate().update(newPreparedStatementCreator(sqlToUse, params));
/* 137:256 */     checkRowsAffected(rowsAffected);
/* 138:257 */     return rowsAffected;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public int updateByNamedParam(Map<String, ?> paramMap, KeyHolder generatedKeyHolder)
/* 142:    */     throws DataAccessException
/* 143:    */   {
/* 144:269 */     validateNamedParameters(paramMap);
/* 145:270 */     ParsedSql parsedSql = getParsedSql();
/* 146:271 */     MapSqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
/* 147:272 */     String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
/* 148:273 */     Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, getDeclaredParameters());
/* 149:274 */     int rowsAffected = getJdbcTemplate().update(newPreparedStatementCreator(sqlToUse, params), generatedKeyHolder);
/* 150:275 */     checkRowsAffected(rowsAffected);
/* 151:276 */     return rowsAffected;
/* 152:    */   }
/* 153:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.SqlUpdate
 * JD-Core Version:    0.7.0.1
 */