/*   1:    */ package org.springframework.jdbc.core.namedparam;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.springframework.jdbc.core.SqlParameterValue;
/*   6:    */ 
/*   7:    */ public class SqlParameterSourceUtils
/*   8:    */ {
/*   9:    */   public static SqlParameterSource[] createBatch(Map[] valueMaps)
/*  10:    */   {
/*  11: 39 */     MapSqlParameterSource[] batch = new MapSqlParameterSource[valueMaps.length];
/*  12: 40 */     for (int i = 0; i < valueMaps.length; i++)
/*  13:    */     {
/*  14: 41 */       Map valueMap = valueMaps[i];
/*  15: 42 */       batch[i] = new MapSqlParameterSource(valueMap);
/*  16:    */     }
/*  17: 44 */     return batch;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static SqlParameterSource[] createBatch(Object[] beans)
/*  21:    */   {
/*  22: 54 */     BeanPropertySqlParameterSource[] batch = new BeanPropertySqlParameterSource[beans.length];
/*  23: 55 */     for (int i = 0; i < beans.length; i++)
/*  24:    */     {
/*  25: 56 */       Object bean = beans[i];
/*  26: 57 */       batch[i] = new BeanPropertySqlParameterSource(bean);
/*  27:    */     }
/*  28: 59 */     return batch;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static Object getTypedValue(SqlParameterSource source, String parameterName)
/*  32:    */   {
/*  33: 69 */     int sqlType = source.getSqlType(parameterName);
/*  34: 70 */     if (sqlType != -2147483648)
/*  35:    */     {
/*  36: 71 */       if (source.getTypeName(parameterName) != null) {
/*  37: 72 */         return new SqlParameterValue(sqlType, source.getTypeName(parameterName), source.getValue(parameterName));
/*  38:    */       }
/*  39: 75 */       return new SqlParameterValue(sqlType, source.getValue(parameterName));
/*  40:    */     }
/*  41: 79 */     return source.getValue(parameterName);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static Map extractCaseInsensitiveParameterNames(SqlParameterSource parameterSource)
/*  45:    */   {
/*  46: 89 */     Map caseInsensitiveParameterNames = new HashMap();
/*  47:    */     int i;
/*  48: 90 */     if ((parameterSource instanceof BeanPropertySqlParameterSource))
/*  49:    */     {
/*  50: 91 */       String[] propertyNames = ((BeanPropertySqlParameterSource)parameterSource).getReadablePropertyNames();
/*  51: 92 */       for (i = 0; i < propertyNames.length; i++)
/*  52:    */       {
/*  53: 93 */         String name = propertyNames[i];
/*  54: 94 */         caseInsensitiveParameterNames.put(name.toLowerCase(), name);
/*  55:    */       }
/*  56:    */     }
/*  57: 97 */     else if ((parameterSource instanceof MapSqlParameterSource))
/*  58:    */     {
/*  59: 98 */       for (String name : ((MapSqlParameterSource)parameterSource).getValues().keySet()) {
/*  60: 99 */         caseInsensitiveParameterNames.put(name.toLowerCase(), name);
/*  61:    */       }
/*  62:    */     }
/*  63:102 */     return caseInsensitiveParameterNames;
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
 * JD-Core Version:    0.7.0.1
 */