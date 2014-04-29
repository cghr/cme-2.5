/*   1:    */ package org.springframework.jdbc.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.math.BigDecimal;
/*   6:    */ import java.sql.Blob;
/*   7:    */ import java.sql.Clob;
/*   8:    */ import java.sql.Connection;
/*   9:    */ import java.sql.DatabaseMetaData;
/*  10:    */ import java.sql.ResultSet;
/*  11:    */ import java.sql.ResultSetMetaData;
/*  12:    */ import java.sql.SQLException;
/*  13:    */ import java.sql.Statement;
/*  14:    */ import java.sql.Time;
/*  15:    */ import java.sql.Timestamp;
/*  16:    */ import javax.sql.DataSource;
/*  17:    */ import org.apache.commons.logging.Log;
/*  18:    */ import org.apache.commons.logging.LogFactory;
/*  19:    */ import org.springframework.jdbc.CannotGetJdbcConnectionException;
/*  20:    */ import org.springframework.jdbc.datasource.DataSourceUtils;
/*  21:    */ 
/*  22:    */ public abstract class JdbcUtils
/*  23:    */ {
/*  24:    */   public static final int TYPE_UNKNOWN = -2147483648;
/*  25: 56 */   private static final Log logger = LogFactory.getLog(JdbcUtils.class);
/*  26:    */   
/*  27:    */   public static void closeConnection(Connection con)
/*  28:    */   {
/*  29: 65 */     if (con != null) {
/*  30:    */       try
/*  31:    */       {
/*  32: 67 */         con.close();
/*  33:    */       }
/*  34:    */       catch (SQLException ex)
/*  35:    */       {
/*  36: 70 */         logger.debug("Could not close JDBC Connection", ex);
/*  37:    */       }
/*  38:    */       catch (Throwable ex)
/*  39:    */       {
/*  40: 74 */         logger.debug("Unexpected exception on closing JDBC Connection", ex);
/*  41:    */       }
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static void closeStatement(Statement stmt)
/*  46:    */   {
/*  47: 85 */     if (stmt != null) {
/*  48:    */       try
/*  49:    */       {
/*  50: 87 */         stmt.close();
/*  51:    */       }
/*  52:    */       catch (SQLException ex)
/*  53:    */       {
/*  54: 90 */         logger.trace("Could not close JDBC Statement", ex);
/*  55:    */       }
/*  56:    */       catch (Throwable ex)
/*  57:    */       {
/*  58: 94 */         logger.trace("Unexpected exception on closing JDBC Statement", ex);
/*  59:    */       }
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static void closeResultSet(ResultSet rs)
/*  64:    */   {
/*  65:105 */     if (rs != null) {
/*  66:    */       try
/*  67:    */       {
/*  68:107 */         rs.close();
/*  69:    */       }
/*  70:    */       catch (SQLException ex)
/*  71:    */       {
/*  72:110 */         logger.trace("Could not close JDBC ResultSet", ex);
/*  73:    */       }
/*  74:    */       catch (Throwable ex)
/*  75:    */       {
/*  76:114 */         logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
/*  77:    */       }
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static Object getResultSetValue(ResultSet rs, int index, Class requiredType)
/*  82:    */     throws SQLException
/*  83:    */   {
/*  84:133 */     if (requiredType == null) {
/*  85:134 */       return getResultSetValue(rs, index);
/*  86:    */     }
/*  87:137 */     Object value = null;
/*  88:138 */     boolean wasNullCheck = false;
/*  89:141 */     if (String.class.equals(requiredType))
/*  90:    */     {
/*  91:142 */       value = rs.getString(index);
/*  92:    */     }
/*  93:144 */     else if ((Boolean.TYPE.equals(requiredType)) || (Boolean.class.equals(requiredType)))
/*  94:    */     {
/*  95:145 */       value = Boolean.valueOf(rs.getBoolean(index));
/*  96:146 */       wasNullCheck = true;
/*  97:    */     }
/*  98:148 */     else if ((Byte.TYPE.equals(requiredType)) || (Byte.class.equals(requiredType)))
/*  99:    */     {
/* 100:149 */       value = Byte.valueOf(rs.getByte(index));
/* 101:150 */       wasNullCheck = true;
/* 102:    */     }
/* 103:152 */     else if ((Short.TYPE.equals(requiredType)) || (Short.class.equals(requiredType)))
/* 104:    */     {
/* 105:153 */       value = Short.valueOf(rs.getShort(index));
/* 106:154 */       wasNullCheck = true;
/* 107:    */     }
/* 108:156 */     else if ((Integer.TYPE.equals(requiredType)) || (Integer.class.equals(requiredType)))
/* 109:    */     {
/* 110:157 */       value = Integer.valueOf(rs.getInt(index));
/* 111:158 */       wasNullCheck = true;
/* 112:    */     }
/* 113:160 */     else if ((Long.TYPE.equals(requiredType)) || (Long.class.equals(requiredType)))
/* 114:    */     {
/* 115:161 */       value = Long.valueOf(rs.getLong(index));
/* 116:162 */       wasNullCheck = true;
/* 117:    */     }
/* 118:164 */     else if ((Float.TYPE.equals(requiredType)) || (Float.class.equals(requiredType)))
/* 119:    */     {
/* 120:165 */       value = Float.valueOf(rs.getFloat(index));
/* 121:166 */       wasNullCheck = true;
/* 122:    */     }
/* 123:168 */     else if ((Double.TYPE.equals(requiredType)) || (Double.class.equals(requiredType)) || 
/* 124:169 */       (Number.class.equals(requiredType)))
/* 125:    */     {
/* 126:170 */       value = Double.valueOf(rs.getDouble(index));
/* 127:171 */       wasNullCheck = true;
/* 128:    */     }
/* 129:173 */     else if ([B.class.equals(requiredType))
/* 130:    */     {
/* 131:174 */       value = rs.getBytes(index);
/* 132:    */     }
/* 133:176 */     else if (java.sql.Date.class.equals(requiredType))
/* 134:    */     {
/* 135:177 */       value = rs.getDate(index);
/* 136:    */     }
/* 137:179 */     else if (Time.class.equals(requiredType))
/* 138:    */     {
/* 139:180 */       value = rs.getTime(index);
/* 140:    */     }
/* 141:182 */     else if ((Timestamp.class.equals(requiredType)) || (java.util.Date.class.equals(requiredType)))
/* 142:    */     {
/* 143:183 */       value = rs.getTimestamp(index);
/* 144:    */     }
/* 145:185 */     else if (BigDecimal.class.equals(requiredType))
/* 146:    */     {
/* 147:186 */       value = rs.getBigDecimal(index);
/* 148:    */     }
/* 149:188 */     else if (Blob.class.equals(requiredType))
/* 150:    */     {
/* 151:189 */       value = rs.getBlob(index);
/* 152:    */     }
/* 153:191 */     else if (Clob.class.equals(requiredType))
/* 154:    */     {
/* 155:192 */       value = rs.getClob(index);
/* 156:    */     }
/* 157:    */     else
/* 158:    */     {
/* 159:196 */       value = getResultSetValue(rs, index);
/* 160:    */     }
/* 161:201 */     if ((wasNullCheck) && (value != null) && (rs.wasNull())) {
/* 162:202 */       value = null;
/* 163:    */     }
/* 164:204 */     return value;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public static Object getResultSetValue(ResultSet rs, int index)
/* 168:    */     throws SQLException
/* 169:    */   {
/* 170:226 */     Object obj = rs.getObject(index);
/* 171:227 */     String className = null;
/* 172:228 */     if (obj != null) {
/* 173:229 */       className = obj.getClass().getName();
/* 174:    */     }
/* 175:231 */     if ((obj instanceof Blob))
/* 176:    */     {
/* 177:232 */       obj = rs.getBytes(index);
/* 178:    */     }
/* 179:234 */     else if ((obj instanceof Clob))
/* 180:    */     {
/* 181:235 */       obj = rs.getString(index);
/* 182:    */     }
/* 183:237 */     else if ((className != null) && (
/* 184:238 */       ("oracle.sql.TIMESTAMP".equals(className)) || 
/* 185:239 */       ("oracle.sql.TIMESTAMPTZ".equals(className))))
/* 186:    */     {
/* 187:240 */       obj = rs.getTimestamp(index);
/* 188:    */     }
/* 189:242 */     else if ((className != null) && (className.startsWith("oracle.sql.DATE")))
/* 190:    */     {
/* 191:243 */       String metaDataClassName = rs.getMetaData().getColumnClassName(index);
/* 192:244 */       if (("java.sql.Timestamp".equals(metaDataClassName)) || 
/* 193:245 */         ("oracle.sql.TIMESTAMP".equals(metaDataClassName))) {
/* 194:246 */         obj = rs.getTimestamp(index);
/* 195:    */       } else {
/* 196:249 */         obj = rs.getDate(index);
/* 197:    */       }
/* 198:    */     }
/* 199:252 */     else if ((obj != null) && ((obj instanceof java.sql.Date)) && 
/* 200:253 */       ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))))
/* 201:    */     {
/* 202:254 */       obj = rs.getTimestamp(index);
/* 203:    */     }
/* 204:257 */     return obj;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public static Object extractDatabaseMetaData(DataSource dataSource, DatabaseMetaDataCallback action)
/* 208:    */     throws MetaDataAccessException
/* 209:    */   {
/* 210:278 */     Connection con = null;
/* 211:    */     try
/* 212:    */     {
/* 213:280 */       con = DataSourceUtils.getConnection(dataSource);
/* 214:281 */       if (con == null) {
/* 215:283 */         throw new MetaDataAccessException("Connection returned by DataSource [" + dataSource + "] was null");
/* 216:    */       }
/* 217:285 */       DatabaseMetaData metaData = con.getMetaData();
/* 218:286 */       if (metaData == null) {
/* 219:288 */         throw new MetaDataAccessException("DatabaseMetaData returned by Connection [" + con + "] was null");
/* 220:    */       }
/* 221:290 */       return action.processMetaData(metaData);
/* 222:    */     }
/* 223:    */     catch (CannotGetJdbcConnectionException ex)
/* 224:    */     {
/* 225:293 */       throw new MetaDataAccessException("Could not get Connection for extracting meta data", ex);
/* 226:    */     }
/* 227:    */     catch (SQLException ex)
/* 228:    */     {
/* 229:296 */       throw new MetaDataAccessException("Error while extracting DatabaseMetaData", ex);
/* 230:    */     }
/* 231:    */     catch (AbstractMethodError err)
/* 232:    */     {
/* 233:299 */       throw new MetaDataAccessException(
/* 234:300 */         "JDBC DatabaseMetaData method not implemented by JDBC driver - upgrade your driver", err);
/* 235:    */     }
/* 236:    */     finally
/* 237:    */     {
/* 238:303 */       DataSourceUtils.releaseConnection(con, dataSource);
/* 239:    */     }
/* 240:    */   }
/* 241:    */   
/* 242:    */   public static Object extractDatabaseMetaData(DataSource dataSource, String metaDataMethodName)
/* 243:    */     throws MetaDataAccessException
/* 244:    */   {
/* 245:320 */     extractDatabaseMetaData(dataSource, 
/* 246:321 */       new DatabaseMetaDataCallback()
/* 247:    */       {
/* 248:    */         public Object processMetaData(DatabaseMetaData dbmd)
/* 249:    */           throws SQLException, MetaDataAccessException
/* 250:    */         {
/* 251:    */           try
/* 252:    */           {
/* 253:324 */             Method method = DatabaseMetaData.class.getMethod(JdbcUtils.this, null);
/* 254:325 */             return method.invoke(dbmd, null);
/* 255:    */           }
/* 256:    */           catch (NoSuchMethodException ex)
/* 257:    */           {
/* 258:328 */             throw new MetaDataAccessException("No method named '" + JdbcUtils.this + 
/* 259:329 */               "' found on DatabaseMetaData instance [" + dbmd + "]", ex);
/* 260:    */           }
/* 261:    */           catch (IllegalAccessException ex)
/* 262:    */           {
/* 263:332 */             throw new MetaDataAccessException(
/* 264:333 */               "Could not access DatabaseMetaData method '" + JdbcUtils.this + "'", ex);
/* 265:    */           }
/* 266:    */           catch (InvocationTargetException ex)
/* 267:    */           {
/* 268:336 */             if ((ex.getTargetException() instanceof SQLException)) {
/* 269:337 */               throw ((SQLException)ex.getTargetException());
/* 270:    */             }
/* 271:339 */             throw new MetaDataAccessException(
/* 272:340 */               "Invocation of DatabaseMetaData method '" + JdbcUtils.this + "' failed", ex);
/* 273:    */           }
/* 274:    */         }
/* 275:    */       });
/* 276:    */   }
/* 277:    */   
/* 278:    */   public static boolean supportsBatchUpdates(Connection con)
/* 279:    */   {
/* 280:    */     try
/* 281:    */     {
/* 282:359 */       DatabaseMetaData dbmd = con.getMetaData();
/* 283:360 */       if (dbmd != null)
/* 284:    */       {
/* 285:361 */         if (dbmd.supportsBatchUpdates())
/* 286:    */         {
/* 287:362 */           logger.debug("JDBC driver supports batch updates");
/* 288:363 */           return true;
/* 289:    */         }
/* 290:366 */         logger.debug("JDBC driver does not support batch updates");
/* 291:    */       }
/* 292:    */     }
/* 293:    */     catch (SQLException ex)
/* 294:    */     {
/* 295:371 */       logger.debug("JDBC driver 'supportsBatchUpdates' method threw exception", ex);
/* 296:    */     }
/* 297:    */     catch (AbstractMethodError err)
/* 298:    */     {
/* 299:374 */       logger.debug("JDBC driver does not support JDBC 2.0 'supportsBatchUpdates' method", err);
/* 300:    */     }
/* 301:376 */     return false;
/* 302:    */   }
/* 303:    */   
/* 304:    */   public static String commonDatabaseName(String source)
/* 305:    */   {
/* 306:385 */     String name = source;
/* 307:386 */     if ((source != null) && (source.startsWith("DB2"))) {
/* 308:387 */       name = "DB2";
/* 309:389 */     } else if (("Sybase SQL Server".equals(source)) || 
/* 310:390 */       ("Adaptive Server Enterprise".equals(source)) || 
/* 311:391 */       ("ASE".equals(source)) || 
/* 312:392 */       ("sql server".equalsIgnoreCase(source))) {
/* 313:393 */       name = "Sybase";
/* 314:    */     }
/* 315:395 */     return name;
/* 316:    */   }
/* 317:    */   
/* 318:    */   public static boolean isNumeric(int sqlType)
/* 319:    */   {
/* 320:407 */     return (-7 == sqlType) || (-5 == sqlType) || (3 == sqlType) || (8 == sqlType) || (6 == sqlType) || (4 == sqlType) || (2 == sqlType) || (7 == sqlType) || (5 == sqlType) || (-6 == sqlType);
/* 321:    */   }
/* 322:    */   
/* 323:    */   public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex)
/* 324:    */     throws SQLException
/* 325:    */   {
/* 326:423 */     String name = resultSetMetaData.getColumnLabel(columnIndex);
/* 327:424 */     if ((name == null) || (name.length() < 1)) {
/* 328:425 */       name = resultSetMetaData.getColumnName(columnIndex);
/* 329:    */     }
/* 330:427 */     return name;
/* 331:    */   }
/* 332:    */   
/* 333:    */   public static String convertUnderscoreNameToPropertyName(String name)
/* 334:    */   {
/* 335:437 */     StringBuilder result = new StringBuilder();
/* 336:438 */     boolean nextIsUpper = false;
/* 337:439 */     if ((name != null) && (name.length() > 0))
/* 338:    */     {
/* 339:440 */       if ((name.length() > 1) && (name.substring(1, 2).equals("_"))) {
/* 340:441 */         result.append(name.substring(0, 1).toUpperCase());
/* 341:    */       } else {
/* 342:444 */         result.append(name.substring(0, 1).toLowerCase());
/* 343:    */       }
/* 344:446 */       for (int i = 1; i < name.length(); i++)
/* 345:    */       {
/* 346:447 */         String s = name.substring(i, i + 1);
/* 347:448 */         if (s.equals("_"))
/* 348:    */         {
/* 349:449 */           nextIsUpper = true;
/* 350:    */         }
/* 351:452 */         else if (nextIsUpper)
/* 352:    */         {
/* 353:453 */           result.append(s.toUpperCase());
/* 354:454 */           nextIsUpper = false;
/* 355:    */         }
/* 356:    */         else
/* 357:    */         {
/* 358:457 */           result.append(s.toLowerCase());
/* 359:    */         }
/* 360:    */       }
/* 361:    */     }
/* 362:462 */     return result.toString();
/* 363:    */   }
/* 364:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.JdbcUtils
 * JD-Core Version:    0.7.0.1
 */