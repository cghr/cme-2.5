/*  1:   */ package org.springframework.jdbc.core;
/*  2:   */ 
/*  3:   */ public class SqlParameterValue
/*  4:   */   extends SqlParameter
/*  5:   */ {
/*  6:   */   private final Object value;
/*  7:   */   
/*  8:   */   public SqlParameterValue(int sqlType, Object value)
/*  9:   */   {
/* 10:48 */     super(sqlType);
/* 11:49 */     this.value = value;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public SqlParameterValue(int sqlType, String typeName, Object value)
/* 15:   */   {
/* 16:59 */     super(sqlType, typeName);
/* 17:60 */     this.value = value;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public SqlParameterValue(int sqlType, int scale, Object value)
/* 21:   */   {
/* 22:71 */     super(sqlType, scale);
/* 23:72 */     this.value = value;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public SqlParameterValue(SqlParameter declaredParam, Object value)
/* 27:   */   {
/* 28:81 */     super(declaredParam);
/* 29:82 */     this.value = value;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object getValue()
/* 33:   */   {
/* 34:90 */     return this.value;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SqlParameterValue
 * JD-Core Version:    0.7.0.1
 */