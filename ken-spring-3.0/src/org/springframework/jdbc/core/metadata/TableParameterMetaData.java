/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ public class TableParameterMetaData
/*  4:   */ {
/*  5:   */   private final String parameterName;
/*  6:   */   private final int sqlType;
/*  7:   */   private final boolean nullable;
/*  8:   */   
/*  9:   */   public TableParameterMetaData(String columnName, int sqlType, boolean nullable)
/* 10:   */   {
/* 11:38 */     this.parameterName = columnName;
/* 12:39 */     this.sqlType = sqlType;
/* 13:40 */     this.nullable = nullable;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getParameterName()
/* 17:   */   {
/* 18:48 */     return this.parameterName;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public int getSqlType()
/* 22:   */   {
/* 23:55 */     return this.sqlType;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean isNullable()
/* 27:   */   {
/* 28:62 */     return this.nullable;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.TableParameterMetaData
 * JD-Core Version:    0.7.0.1
 */