/*  1:   */ package org.springframework.jdbc.core;
/*  2:   */ 
/*  3:   */ import java.sql.PreparedStatement;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import java.util.List;
/*  6:   */ 
/*  7:   */ public abstract class BatchUpdateUtils
/*  8:   */ {
/*  9:   */   public static int[] executeBatchUpdate(String sql, List<Object[]> batchValues, final int[] columnTypes, JdbcOperations jdbcOperations)
/* 10:   */   {
/* 11:32 */     jdbcOperations.batchUpdate(
/* 12:33 */       sql, 
/* 13:34 */       new BatchPreparedStatementSetter()
/* 14:   */       {
/* 15:   */         public void setValues(PreparedStatement ps, int i)
/* 16:   */           throws SQLException
/* 17:   */         {
/* 18:37 */           Object[] values = (Object[])BatchUpdateUtils.this.get(i);
/* 19:38 */           BatchUpdateUtils.setStatementParameters(values, ps, columnTypes);
/* 20:   */         }
/* 21:   */         
/* 22:   */         public int getBatchSize()
/* 23:   */         {
/* 24:42 */           return BatchUpdateUtils.this.size();
/* 25:   */         }
/* 26:   */       });
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected static void setStatementParameters(Object[] values, PreparedStatement ps, int[] columnTypes)
/* 30:   */     throws SQLException
/* 31:   */   {
/* 32:48 */     int colIndex = 0;
/* 33:49 */     Object[] arrayOfObject = values;int j = values.length;
/* 34:49 */     for (int i = 0; i < j; i++)
/* 35:   */     {
/* 36:49 */       Object value = arrayOfObject[i];
/* 37:50 */       colIndex++;
/* 38:51 */       if ((value instanceof SqlParameterValue))
/* 39:   */       {
/* 40:52 */         SqlParameterValue paramValue = (SqlParameterValue)value;
/* 41:53 */         StatementCreatorUtils.setParameterValue(ps, colIndex, paramValue, paramValue.getValue());
/* 42:   */       }
/* 43:   */       else
/* 44:   */       {
/* 45:   */         int colType;
/* 46:   */         int colType;
/* 47:57 */         if ((columnTypes == null) || (columnTypes.length < colIndex)) {
/* 48:58 */           colType = -2147483648;
/* 49:   */         } else {
/* 50:61 */           colType = columnTypes[(colIndex - 1)];
/* 51:   */         }
/* 52:63 */         StatementCreatorUtils.setParameterValue(ps, colIndex, colType, value);
/* 53:   */       }
/* 54:   */     }
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.BatchUpdateUtils
 * JD-Core Version:    0.7.0.1
 */