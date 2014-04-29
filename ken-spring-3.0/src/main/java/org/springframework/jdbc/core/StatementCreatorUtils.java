/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ import java.io.StringWriter;
/*   4:    */ import java.math.BigDecimal;
/*   5:    */ import java.math.BigInteger;
/*   6:    */ import java.sql.Blob;
/*   7:    */ import java.sql.Clob;
/*   8:    */ import java.sql.Connection;
/*   9:    */ import java.sql.DatabaseMetaData;
/*  10:    */ import java.sql.PreparedStatement;
/*  11:    */ import java.sql.SQLException;
/*  12:    */ import java.sql.Time;
/*  13:    */ import java.sql.Timestamp;
/*  14:    */ import java.util.Arrays;
/*  15:    */ import java.util.Calendar;
/*  16:    */ import java.util.Collection;
/*  17:    */ import java.util.HashMap;
/*  18:    */ import java.util.Map;
/*  19:    */ import org.apache.commons.logging.Log;
/*  20:    */ import org.apache.commons.logging.LogFactory;
/*  21:    */ import org.springframework.jdbc.support.SqlValue;
/*  22:    */ 
/*  23:    */ public abstract class StatementCreatorUtils
/*  24:    */ {
/*  25: 61 */   private static final Log logger = LogFactory.getLog(StatementCreatorUtils.class);
/*  26: 63 */   private static Map<Class, Integer> javaTypeToSqlTypeMap = new HashMap(32);
/*  27:    */   
/*  28:    */   static
/*  29:    */   {
/*  30: 70 */     javaTypeToSqlTypeMap.put(Byte.TYPE, Integer.valueOf(-6));
/*  31: 71 */     javaTypeToSqlTypeMap.put(Byte.class, Integer.valueOf(-6));
/*  32: 72 */     javaTypeToSqlTypeMap.put(Short.TYPE, Integer.valueOf(5));
/*  33: 73 */     javaTypeToSqlTypeMap.put(Short.class, Integer.valueOf(5));
/*  34: 74 */     javaTypeToSqlTypeMap.put(Integer.TYPE, Integer.valueOf(4));
/*  35: 75 */     javaTypeToSqlTypeMap.put(Integer.class, Integer.valueOf(4));
/*  36: 76 */     javaTypeToSqlTypeMap.put(Long.TYPE, Integer.valueOf(-5));
/*  37: 77 */     javaTypeToSqlTypeMap.put(Long.class, Integer.valueOf(-5));
/*  38: 78 */     javaTypeToSqlTypeMap.put(BigInteger.class, Integer.valueOf(-5));
/*  39: 79 */     javaTypeToSqlTypeMap.put(Float.TYPE, Integer.valueOf(6));
/*  40: 80 */     javaTypeToSqlTypeMap.put(Float.class, Integer.valueOf(6));
/*  41: 81 */     javaTypeToSqlTypeMap.put(Double.TYPE, Integer.valueOf(8));
/*  42: 82 */     javaTypeToSqlTypeMap.put(Double.class, Integer.valueOf(8));
/*  43: 83 */     javaTypeToSqlTypeMap.put(BigDecimal.class, Integer.valueOf(3));
/*  44: 84 */     javaTypeToSqlTypeMap.put(java.sql.Date.class, Integer.valueOf(91));
/*  45: 85 */     javaTypeToSqlTypeMap.put(Time.class, Integer.valueOf(92));
/*  46: 86 */     javaTypeToSqlTypeMap.put(Timestamp.class, Integer.valueOf(93));
/*  47: 87 */     javaTypeToSqlTypeMap.put(Blob.class, Integer.valueOf(2004));
/*  48: 88 */     javaTypeToSqlTypeMap.put(Clob.class, Integer.valueOf(2005));
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static int javaTypeToSqlParameterType(Class javaType)
/*  52:    */   {
/*  53: 98 */     Integer sqlType = (Integer)javaTypeToSqlTypeMap.get(javaType);
/*  54: 99 */     if (sqlType != null) {
/*  55:100 */       return sqlType.intValue();
/*  56:    */     }
/*  57:102 */     if (Number.class.isAssignableFrom(javaType)) {
/*  58:103 */       return 2;
/*  59:    */     }
/*  60:105 */     if (isStringValue(javaType)) {
/*  61:106 */       return 12;
/*  62:    */     }
/*  63:108 */     if ((isDateValue(javaType)) || (Calendar.class.isAssignableFrom(javaType))) {
/*  64:109 */       return 93;
/*  65:    */     }
/*  66:111 */     return -2147483648;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static void setParameterValue(PreparedStatement ps, int paramIndex, SqlParameter param, Object inValue)
/*  70:    */     throws SQLException
/*  71:    */   {
/*  72:127 */     setParameterValueInternal(ps, paramIndex, param.getSqlType(), param.getTypeName(), param.getScale(), inValue);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static void setParameterValue(PreparedStatement ps, int paramIndex, int sqlType, Object inValue)
/*  76:    */     throws SQLException
/*  77:    */   {
/*  78:144 */     setParameterValueInternal(ps, paramIndex, sqlType, null, null, inValue);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static void setParameterValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName, Object inValue)
/*  82:    */     throws SQLException
/*  83:    */   {
/*  84:163 */     setParameterValueInternal(ps, paramIndex, sqlType, typeName, null, inValue);
/*  85:    */   }
/*  86:    */   
/*  87:    */   private static void setParameterValueInternal(PreparedStatement ps, int paramIndex, int sqlType, String typeName, Integer scale, Object inValue)
/*  88:    */     throws SQLException
/*  89:    */   {
/*  90:184 */     String typeNameToUse = typeName;
/*  91:185 */     int sqlTypeToUse = sqlType;
/*  92:186 */     Object inValueToUse = inValue;
/*  93:189 */     if ((inValue instanceof SqlParameterValue))
/*  94:    */     {
/*  95:190 */       SqlParameterValue parameterValue = (SqlParameterValue)inValue;
/*  96:191 */       if (logger.isDebugEnabled()) {
/*  97:192 */         logger.debug("Overriding typeinfo with runtime info from SqlParameterValue: column index " + paramIndex + 
/*  98:193 */           ", SQL type " + parameterValue.getSqlType() + 
/*  99:194 */           ", Type name " + parameterValue.getTypeName());
/* 100:    */       }
/* 101:196 */       if (parameterValue.getSqlType() != -2147483648) {
/* 102:197 */         sqlTypeToUse = parameterValue.getSqlType();
/* 103:    */       }
/* 104:199 */       if (parameterValue.getTypeName() != null) {
/* 105:200 */         typeNameToUse = parameterValue.getTypeName();
/* 106:    */       }
/* 107:202 */       inValueToUse = parameterValue.getValue();
/* 108:    */     }
/* 109:205 */     if (logger.isTraceEnabled()) {
/* 110:206 */       logger.trace("Setting SQL statement parameter value: column index " + paramIndex + 
/* 111:207 */         ", parameter value [" + inValueToUse + 
/* 112:208 */         "], value class [" + (inValueToUse != null ? inValueToUse.getClass().getName() : "null") + 
/* 113:209 */         "], SQL type " + (sqlTypeToUse == -2147483648 ? "unknown" : Integer.toString(sqlTypeToUse)));
/* 114:    */     }
/* 115:212 */     if (inValueToUse == null) {
/* 116:213 */       setNull(ps, paramIndex, sqlTypeToUse, typeNameToUse);
/* 117:    */     } else {
/* 118:216 */       setValue(ps, paramIndex, sqlTypeToUse, typeNameToUse, scale, inValueToUse);
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   private static void setNull(PreparedStatement ps, int paramIndex, int sqlType, String typeName)
/* 123:    */     throws SQLException
/* 124:    */   {
/* 125:227 */     if (sqlType == -2147483648)
/* 126:    */     {
/* 127:228 */       boolean useSetObject = false;
/* 128:229 */       sqlType = 0;
/* 129:    */       try
/* 130:    */       {
/* 131:231 */         DatabaseMetaData dbmd = ps.getConnection().getMetaData();
/* 132:232 */         String databaseProductName = dbmd.getDatabaseProductName();
/* 133:233 */         String jdbcDriverName = dbmd.getDriverName();
/* 134:234 */         if ((databaseProductName.startsWith("Informix")) || 
/* 135:235 */           (jdbcDriverName.startsWith("Microsoft SQL Server"))) {
/* 136:236 */           useSetObject = true;
/* 137:238 */         } else if ((databaseProductName.startsWith("DB2")) || 
/* 138:239 */           (jdbcDriverName.startsWith("jConnect")) || 
/* 139:240 */           (jdbcDriverName.startsWith("SQLServer")) || 
/* 140:241 */           (jdbcDriverName.startsWith("Apache Derby"))) {
/* 141:242 */           sqlType = 12;
/* 142:    */         }
/* 143:    */       }
/* 144:    */       catch (Throwable ex)
/* 145:    */       {
/* 146:246 */         logger.debug("Could not check database or driver name", ex);
/* 147:    */       }
/* 148:248 */       if (useSetObject) {
/* 149:249 */         ps.setObject(paramIndex, null);
/* 150:    */       } else {
/* 151:252 */         ps.setNull(paramIndex, sqlType);
/* 152:    */       }
/* 153:    */     }
/* 154:255 */     else if (typeName != null)
/* 155:    */     {
/* 156:256 */       ps.setNull(paramIndex, sqlType, typeName);
/* 157:    */     }
/* 158:    */     else
/* 159:    */     {
/* 160:259 */       ps.setNull(paramIndex, sqlType);
/* 161:    */     }
/* 162:    */   }
/* 163:    */   
/* 164:    */   private static void setValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName, Integer scale, Object inValue)
/* 165:    */     throws SQLException
/* 166:    */   {
/* 167:266 */     if ((inValue instanceof SqlTypeValue)) {
/* 168:267 */       ((SqlTypeValue)inValue).setTypeValue(ps, paramIndex, sqlType, typeName);
/* 169:269 */     } else if ((inValue instanceof SqlValue)) {
/* 170:270 */       ((SqlValue)inValue).setValue(ps, paramIndex);
/* 171:272 */     } else if ((sqlType == 12) || (sqlType == -1) || (
/* 172:273 */       (sqlType == 2005) && (isStringValue(inValue.getClass())))) {
/* 173:274 */       ps.setString(paramIndex, inValue.toString());
/* 174:276 */     } else if ((sqlType == 3) || (sqlType == 2))
/* 175:    */     {
/* 176:277 */       if ((inValue instanceof BigDecimal)) {
/* 177:278 */         ps.setBigDecimal(paramIndex, (BigDecimal)inValue);
/* 178:280 */       } else if (scale != null) {
/* 179:281 */         ps.setObject(paramIndex, inValue, sqlType, scale.intValue());
/* 180:    */       } else {
/* 181:284 */         ps.setObject(paramIndex, inValue, sqlType);
/* 182:    */       }
/* 183:    */     }
/* 184:287 */     else if (sqlType == 91)
/* 185:    */     {
/* 186:288 */       if ((inValue instanceof java.util.Date))
/* 187:    */       {
/* 188:289 */         if ((inValue instanceof java.sql.Date)) {
/* 189:290 */           ps.setDate(paramIndex, (java.sql.Date)inValue);
/* 190:    */         } else {
/* 191:293 */           ps.setDate(paramIndex, new java.sql.Date(((java.util.Date)inValue).getTime()));
/* 192:    */         }
/* 193:    */       }
/* 194:296 */       else if ((inValue instanceof Calendar))
/* 195:    */       {
/* 196:297 */         Calendar cal = (Calendar)inValue;
/* 197:298 */         ps.setDate(paramIndex, new java.sql.Date(cal.getTime().getTime()), cal);
/* 198:    */       }
/* 199:    */       else
/* 200:    */       {
/* 201:301 */         ps.setObject(paramIndex, inValue, 91);
/* 202:    */       }
/* 203:    */     }
/* 204:304 */     else if (sqlType == 92)
/* 205:    */     {
/* 206:305 */       if ((inValue instanceof java.util.Date))
/* 207:    */       {
/* 208:306 */         if ((inValue instanceof Time)) {
/* 209:307 */           ps.setTime(paramIndex, (Time)inValue);
/* 210:    */         } else {
/* 211:310 */           ps.setTime(paramIndex, new Time(((java.util.Date)inValue).getTime()));
/* 212:    */         }
/* 213:    */       }
/* 214:313 */       else if ((inValue instanceof Calendar))
/* 215:    */       {
/* 216:314 */         Calendar cal = (Calendar)inValue;
/* 217:315 */         ps.setTime(paramIndex, new Time(cal.getTime().getTime()), cal);
/* 218:    */       }
/* 219:    */       else
/* 220:    */       {
/* 221:318 */         ps.setObject(paramIndex, inValue, 92);
/* 222:    */       }
/* 223:    */     }
/* 224:321 */     else if (sqlType == 93)
/* 225:    */     {
/* 226:322 */       if ((inValue instanceof java.util.Date))
/* 227:    */       {
/* 228:323 */         if ((inValue instanceof Timestamp)) {
/* 229:324 */           ps.setTimestamp(paramIndex, (Timestamp)inValue);
/* 230:    */         } else {
/* 231:327 */           ps.setTimestamp(paramIndex, new Timestamp(((java.util.Date)inValue).getTime()));
/* 232:    */         }
/* 233:    */       }
/* 234:330 */       else if ((inValue instanceof Calendar))
/* 235:    */       {
/* 236:331 */         Calendar cal = (Calendar)inValue;
/* 237:332 */         ps.setTimestamp(paramIndex, new Timestamp(cal.getTime().getTime()), cal);
/* 238:    */       }
/* 239:    */       else
/* 240:    */       {
/* 241:335 */         ps.setObject(paramIndex, inValue, 93);
/* 242:    */       }
/* 243:    */     }
/* 244:338 */     else if (sqlType == -2147483648)
/* 245:    */     {
/* 246:339 */       if (isStringValue(inValue.getClass()))
/* 247:    */       {
/* 248:340 */         ps.setString(paramIndex, inValue.toString());
/* 249:    */       }
/* 250:342 */       else if (isDateValue(inValue.getClass()))
/* 251:    */       {
/* 252:343 */         ps.setTimestamp(paramIndex, new Timestamp(((java.util.Date)inValue).getTime()));
/* 253:    */       }
/* 254:345 */       else if ((inValue instanceof Calendar))
/* 255:    */       {
/* 256:346 */         Calendar cal = (Calendar)inValue;
/* 257:347 */         ps.setTimestamp(paramIndex, new Timestamp(cal.getTime().getTime()), cal);
/* 258:    */       }
/* 259:    */       else
/* 260:    */       {
/* 261:351 */         ps.setObject(paramIndex, inValue);
/* 262:    */       }
/* 263:    */     }
/* 264:    */     else {
/* 265:356 */       ps.setObject(paramIndex, inValue, sqlType);
/* 266:    */     }
/* 267:    */   }
/* 268:    */   
/* 269:    */   private static boolean isStringValue(Class inValueType)
/* 270:    */   {
/* 271:366 */     return (CharSequence.class.isAssignableFrom(inValueType)) || (StringWriter.class.isAssignableFrom(inValueType));
/* 272:    */   }
/* 273:    */   
/* 274:    */   private static boolean isDateValue(Class inValueType)
/* 275:    */   {
/* 276:377 */     return (java.util.Date.class.isAssignableFrom(inValueType)) && (!java.sql.Date.class.isAssignableFrom(inValueType)) && (!Time.class.isAssignableFrom(inValueType)) && (!Timestamp.class.isAssignableFrom(inValueType));
/* 277:    */   }
/* 278:    */   
/* 279:    */   public static void cleanupParameters(Object[] paramValues)
/* 280:    */   {
/* 281:388 */     if (paramValues != null) {
/* 282:389 */       cleanupParameters((Collection)Arrays.asList(paramValues));
/* 283:    */     }
/* 284:    */   }
/* 285:    */   
/* 286:    */   public static void cleanupParameters(Collection paramValues)
/* 287:    */   {
/* 288:401 */     if (paramValues != null) {
/* 289:402 */       for (Object inValue : paramValues) {
/* 290:403 */         if ((inValue instanceof DisposableSqlTypeValue)) {
/* 291:404 */           ((DisposableSqlTypeValue)inValue).cleanup();
/* 292:406 */         } else if ((inValue instanceof SqlValue)) {
/* 293:407 */           ((SqlValue)inValue).cleanup();
/* 294:    */         }
/* 295:    */       }
/* 296:    */     }
/* 297:    */   }
/* 298:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.StatementCreatorUtils
 * JD-Core Version:    0.7.0.1
 */