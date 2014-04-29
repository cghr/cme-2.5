/*  1:   */ package org.springframework.jdbc.core;
/*  2:   */ 
/*  3:   */ import java.sql.ResultSet;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import java.util.List;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class RowMapperResultSetExtractor<T>
/* 10:   */   implements ResultSetExtractor<List<T>>
/* 11:   */ {
/* 12:   */   private final RowMapper<T> rowMapper;
/* 13:   */   private final int rowsExpected;
/* 14:   */   
/* 15:   */   public RowMapperResultSetExtractor(RowMapper<T> rowMapper)
/* 16:   */   {
/* 17:72 */     this(rowMapper, 0);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public RowMapperResultSetExtractor(RowMapper<T> rowMapper, int rowsExpected)
/* 21:   */   {
/* 22:82 */     Assert.notNull(rowMapper, "RowMapper is required");
/* 23:83 */     this.rowMapper = rowMapper;
/* 24:84 */     this.rowsExpected = rowsExpected;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public List<T> extractData(ResultSet rs)
/* 28:   */     throws SQLException
/* 29:   */   {
/* 30:89 */     List<T> results = this.rowsExpected > 0 ? new ArrayList(this.rowsExpected) : new ArrayList();
/* 31:90 */     int rowNum = 0;
/* 32:91 */     while (rs.next()) {
/* 33:92 */       results.add(this.rowMapper.mapRow(rs, rowNum++));
/* 34:   */     }
/* 35:94 */     return results;
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.RowMapperResultSetExtractor
 * JD-Core Version:    0.7.0.1
 */