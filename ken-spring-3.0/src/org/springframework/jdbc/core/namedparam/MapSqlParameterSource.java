/*   1:    */ package org.springframework.jdbc.core.namedparam;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import org.springframework.jdbc.core.SqlParameterValue;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ 
/*  10:    */ public class MapSqlParameterSource
/*  11:    */   extends AbstractSqlParameterSource
/*  12:    */ {
/*  13: 46 */   private final Map<String, Object> values = new HashMap();
/*  14:    */   
/*  15:    */   public MapSqlParameterSource() {}
/*  16:    */   
/*  17:    */   public MapSqlParameterSource(String paramName, Object value)
/*  18:    */   {
/*  19: 65 */     addValue(paramName, value);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public MapSqlParameterSource(Map<String, ?> values)
/*  23:    */   {
/*  24: 73 */     addValues(values);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public MapSqlParameterSource addValue(String paramName, Object value)
/*  28:    */   {
/*  29: 85 */     Assert.notNull(paramName, "Parameter name must not be null");
/*  30: 86 */     this.values.put(paramName, value);
/*  31: 87 */     if ((value instanceof SqlParameterValue)) {
/*  32: 88 */       registerSqlType(paramName, ((SqlParameterValue)value).getSqlType());
/*  33:    */     }
/*  34: 90 */     return this;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public MapSqlParameterSource addValue(String paramName, Object value, int sqlType)
/*  38:    */   {
/*  39:102 */     Assert.notNull(paramName, "Parameter name must not be null");
/*  40:103 */     this.values.put(paramName, value);
/*  41:104 */     registerSqlType(paramName, sqlType);
/*  42:105 */     return this;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public MapSqlParameterSource addValue(String paramName, Object value, int sqlType, String typeName)
/*  46:    */   {
/*  47:118 */     Assert.notNull(paramName, "Parameter name must not be null");
/*  48:119 */     this.values.put(paramName, value);
/*  49:120 */     registerSqlType(paramName, sqlType);
/*  50:121 */     registerTypeName(paramName, typeName);
/*  51:122 */     return this;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public MapSqlParameterSource addValues(Map<String, ?> values)
/*  55:    */   {
/*  56:132 */     if (values != null) {
/*  57:133 */       for (Map.Entry<String, ?> entry : values.entrySet())
/*  58:    */       {
/*  59:134 */         this.values.put((String)entry.getKey(), entry.getValue());
/*  60:135 */         if ((entry.getValue() instanceof SqlParameterValue))
/*  61:    */         {
/*  62:136 */           SqlParameterValue value = (SqlParameterValue)entry.getValue();
/*  63:137 */           registerSqlType((String)entry.getKey(), value.getSqlType());
/*  64:    */         }
/*  65:    */       }
/*  66:    */     }
/*  67:141 */     return this;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Map<String, Object> getValues()
/*  71:    */   {
/*  72:148 */     return Collections.unmodifiableMap(this.values);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public boolean hasValue(String paramName)
/*  76:    */   {
/*  77:153 */     return this.values.containsKey(paramName);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Object getValue(String paramName)
/*  81:    */   {
/*  82:157 */     if (!hasValue(paramName)) {
/*  83:158 */       throw new IllegalArgumentException("No value registered for key '" + paramName + "'");
/*  84:    */     }
/*  85:160 */     return this.values.get(paramName);
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.MapSqlParameterSource
 * JD-Core Version:    0.7.0.1
 */