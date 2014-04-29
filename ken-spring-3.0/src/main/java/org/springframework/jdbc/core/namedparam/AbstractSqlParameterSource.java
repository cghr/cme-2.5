/*  1:   */ package org.springframework.jdbc.core.namedparam;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public abstract class AbstractSqlParameterSource
/*  8:   */   implements SqlParameterSource
/*  9:   */ {
/* 10:33 */   private final Map<String, Integer> sqlTypes = new HashMap();
/* 11:35 */   private final Map<String, String> typeNames = new HashMap();
/* 12:   */   
/* 13:   */   public void registerSqlType(String paramName, int sqlType)
/* 14:   */   {
/* 15:44 */     Assert.notNull(paramName, "Parameter name must not be null");
/* 16:45 */     this.sqlTypes.put(paramName, Integer.valueOf(sqlType));
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void registerTypeName(String paramName, String typeName)
/* 20:   */   {
/* 21:54 */     Assert.notNull(paramName, "Parameter name must not be null");
/* 22:55 */     this.typeNames.put(paramName, typeName);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public int getSqlType(String paramName)
/* 26:   */   {
/* 27:65 */     Assert.notNull(paramName, "Parameter name must not be null");
/* 28:66 */     Integer sqlType = (Integer)this.sqlTypes.get(paramName);
/* 29:67 */     if (sqlType != null) {
/* 30:68 */       return sqlType.intValue();
/* 31:   */     }
/* 32:70 */     return -2147483648;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String getTypeName(String paramName)
/* 36:   */   {
/* 37:80 */     Assert.notNull(paramName, "Parameter name must not be null");
/* 38:81 */     return (String)this.typeNames.get(paramName);
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource
 * JD-Core Version:    0.7.0.1
 */