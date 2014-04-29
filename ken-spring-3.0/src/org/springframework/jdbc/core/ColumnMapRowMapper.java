/*  1:   */ package org.springframework.jdbc.core;
/*  2:   */ 
/*  3:   */ import java.sql.ResultSet;
/*  4:   */ import java.sql.ResultSetMetaData;
/*  5:   */ import java.sql.SQLException;
/*  6:   */ import java.util.Map;
/*  7:   */ import org.springframework.jdbc.support.JdbcUtils;
/*  8:   */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*  9:   */ 
/* 10:   */ public class ColumnMapRowMapper
/* 11:   */   implements RowMapper<Map<String, Object>>
/* 12:   */ {
/* 13:   */   public Map<String, Object> mapRow(ResultSet rs, int rowNum)
/* 14:   */     throws SQLException
/* 15:   */   {
/* 16:51 */     ResultSetMetaData rsmd = rs.getMetaData();
/* 17:52 */     int columnCount = rsmd.getColumnCount();
/* 18:53 */     Map<String, Object> mapOfColValues = createColumnMap(columnCount);
/* 19:54 */     for (int i = 1; i <= columnCount; i++)
/* 20:   */     {
/* 21:55 */       String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
/* 22:56 */       Object obj = getColumnValue(rs, i);
/* 23:57 */       mapOfColValues.put(key, obj);
/* 24:   */     }
/* 25:59 */     return mapOfColValues;
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected Map<String, Object> createColumnMap(int columnCount)
/* 29:   */   {
/* 30:72 */     return new LinkedCaseInsensitiveMap(columnCount);
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected String getColumnKey(String columnName)
/* 34:   */   {
/* 35:82 */     return columnName;
/* 36:   */   }
/* 37:   */   
/* 38:   */   protected Object getColumnValue(ResultSet rs, int index)
/* 39:   */     throws SQLException
/* 40:   */   {
/* 41:96 */     return JdbcUtils.getResultSetValue(rs, index);
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.ColumnMapRowMapper
 * JD-Core Version:    0.7.0.1
 */