/*  1:   */ package org.springframework.jdbc.core.metadata;
/*  2:   */ 
/*  3:   */ public class CallParameterMetaData
/*  4:   */ {
/*  5:   */   private String parameterName;
/*  6:   */   private int parameterType;
/*  7:   */   private int sqlType;
/*  8:   */   private String typeName;
/*  9:   */   private boolean nullable;
/* 10:   */   
/* 11:   */   public CallParameterMetaData(String columnName, int columnType, int sqlType, String typeName, boolean nullable)
/* 12:   */   {
/* 13:36 */     this.parameterName = columnName;
/* 14:37 */     this.parameterType = columnType;
/* 15:38 */     this.sqlType = sqlType;
/* 16:39 */     this.typeName = typeName;
/* 17:40 */     this.nullable = nullable;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getParameterName()
/* 21:   */   {
/* 22:48 */     return this.parameterName;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public int getParameterType()
/* 26:   */   {
/* 27:55 */     return this.parameterType;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public int getSqlType()
/* 31:   */   {
/* 32:62 */     return this.sqlType;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String getTypeName()
/* 36:   */   {
/* 37:69 */     return this.typeName;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public boolean isNullable()
/* 41:   */   {
/* 42:76 */     return this.nullable;
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.CallParameterMetaData
 * JD-Core Version:    0.7.0.1
 */