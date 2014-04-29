/*  1:   */ package org.springframework.jdbc.core.namedparam;
/*  2:   */ 
/*  3:   */ import java.sql.PreparedStatement;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import org.springframework.jdbc.core.BatchPreparedStatementSetter;
/*  6:   */ import org.springframework.jdbc.core.BatchUpdateUtils;
/*  7:   */ import org.springframework.jdbc.core.JdbcOperations;
/*  8:   */ 
/*  9:   */ public class NamedParameterBatchUpdateUtils
/* 10:   */   extends BatchUpdateUtils
/* 11:   */ {
/* 12:   */   public static int[] executeBatchUpdateWithNamedParameters(ParsedSql parsedSql, final SqlParameterSource[] batchArgs, JdbcOperations jdbcOperations)
/* 13:   */   {
/* 14:20 */     if (batchArgs.length <= 0) {
/* 15:21 */       return new int[1];
/* 16:   */     }
/* 17:23 */     String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, batchArgs[0]);
/* 18:24 */     jdbcOperations.batchUpdate(
/* 19:25 */       sqlToUse, 
/* 20:26 */       new BatchPreparedStatementSetter()
/* 21:   */       {
/* 22:   */         public void setValues(PreparedStatement ps, int i)
/* 23:   */           throws SQLException
/* 24:   */         {
/* 25:29 */           Object[] values = NamedParameterUtils.buildValueArray(NamedParameterBatchUpdateUtils.this, batchArgs[i], null);
/* 26:30 */           int[] columnTypes = NamedParameterUtils.buildSqlTypeArray(NamedParameterBatchUpdateUtils.this, batchArgs[i]);
/* 27:31 */           NamedParameterBatchUpdateUtils.setStatementParameters(values, ps, columnTypes);
/* 28:   */         }
/* 29:   */         
/* 30:   */         public int getBatchSize()
/* 31:   */         {
/* 32:35 */           return batchArgs.length;
/* 33:   */         }
/* 34:   */       });
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.NamedParameterBatchUpdateUtils
 * JD-Core Version:    0.7.0.1
 */