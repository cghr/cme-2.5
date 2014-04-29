/*   1:    */ package org.springframework.jdbc.core.namedparam;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.LinkedList;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  12:    */ import org.springframework.jdbc.core.SqlParameter;
/*  13:    */ import org.springframework.jdbc.core.SqlParameterValue;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ 
/*  16:    */ public abstract class NamedParameterUtils
/*  17:    */ {
/*  18: 48 */   private static final char[] PARAMETER_SEPARATORS = { '"', '\'', ':', '&', ',', ';', '(', ')', '|', '=', '+', '-', '*', '%', '/', '\\', '<', '>', '^' };
/*  19: 54 */   private static final String[] START_SKIP = { "'", "\"", "--", "/*" };
/*  20: 60 */   private static final String[] STOP_SKIP = { "'", "\"", "\n", "*/" };
/*  21:    */   
/*  22:    */   public static ParsedSql parseSqlStatement(String sql)
/*  23:    */   {
/*  24: 74 */     Assert.notNull(sql, "SQL must not be null");
/*  25:    */     
/*  26: 76 */     Set<String> namedParameters = new HashSet();
/*  27: 77 */     String sqlToUse = sql;
/*  28: 78 */     List<ParameterHolder> parameterList = new ArrayList();
/*  29:    */     
/*  30: 80 */     char[] statement = sql.toCharArray();
/*  31: 81 */     int namedParameterCount = 0;
/*  32: 82 */     int unnamedParameterCount = 0;
/*  33: 83 */     int totalParameterCount = 0;
/*  34:    */     
/*  35: 85 */     int escapes = 0;
/*  36: 86 */     int i = 0;
/*  37:    */     int j;
/*  38: 87 */     while (i < statement.length)
/*  39:    */     {
/*  40: 88 */       int skipToPosition = skipCommentsAndQuotes(statement, i);
/*  41: 89 */       if (i != skipToPosition)
/*  42:    */       {
/*  43: 90 */         if (skipToPosition >= statement.length) {
/*  44:    */           break;
/*  45:    */         }
/*  46: 93 */         i = skipToPosition;
/*  47:    */       }
/*  48: 95 */       char c = statement[i];
/*  49: 96 */       if ((c == ':') || (c == '&'))
/*  50:    */       {
/*  51: 97 */         int j = i + 1;
/*  52: 98 */         if ((j < statement.length) && (statement[j] == ':') && (c == ':'))
/*  53:    */         {
/*  54:100 */           i += 2;
/*  55:101 */           continue;
/*  56:    */         }
/*  57:103 */         String parameter = null;
/*  58:104 */         if ((j < statement.length) && (c == ':') && (statement[j] == '{'))
/*  59:    */         {
/*  60:106 */           while ((j < statement.length) && ('}' != statement[j]))
/*  61:    */           {
/*  62:107 */             j++;
/*  63:108 */             if ((':' == statement[j]) || ('{' == statement[j])) {
/*  64:109 */               throw new InvalidDataAccessApiUsageException("Parameter name contains invalid character '" + statement[j] + "' at position " + i + " in statement " + sql);
/*  65:    */             }
/*  66:    */           }
/*  67:112 */           if (j >= statement.length) {
/*  68:113 */             throw new InvalidDataAccessApiUsageException("Non-terminated named parameter declaration at position " + i + " in statement " + sql);
/*  69:    */           }
/*  70:115 */           if (j - i > 3)
/*  71:    */           {
/*  72:116 */             parameter = sql.substring(i + 2, j);
/*  73:117 */             namedParameterCount = addNewNamedParameter(namedParameters, namedParameterCount, parameter);
/*  74:118 */             totalParameterCount = addNamedParameter(parameterList, totalParameterCount, escapes, i, j + 1, parameter);
/*  75:    */           }
/*  76:120 */           j++;
/*  77:    */         }
/*  78:    */         else
/*  79:    */         {
/*  80:123 */           while ((j < statement.length) && (!isParameterSeparator(statement[j]))) {
/*  81:124 */             j++;
/*  82:    */           }
/*  83:126 */           if (j - i > 1)
/*  84:    */           {
/*  85:127 */             parameter = sql.substring(i + 1, j);
/*  86:128 */             namedParameterCount = addNewNamedParameter(namedParameters, namedParameterCount, parameter);
/*  87:129 */             totalParameterCount = addNamedParameter(parameterList, totalParameterCount, escapes, i, j, parameter);
/*  88:    */           }
/*  89:    */         }
/*  90:132 */         i = j - 1;
/*  91:    */       }
/*  92:    */       else
/*  93:    */       {
/*  94:135 */         if (c == '\\')
/*  95:    */         {
/*  96:136 */           j = i + 1;
/*  97:137 */           if ((j < statement.length) && (statement[j] == ':'))
/*  98:    */           {
/*  99:139 */             sqlToUse = sqlToUse.substring(0, i - escapes) + sqlToUse.substring(i - escapes + 1);
/* 100:140 */             escapes++;
/* 101:141 */             i += 2;
/* 102:142 */             continue;
/* 103:    */           }
/* 104:    */         }
/* 105:145 */         if (c == '?')
/* 106:    */         {
/* 107:146 */           unnamedParameterCount++;
/* 108:147 */           totalParameterCount++;
/* 109:    */         }
/* 110:    */       }
/* 111:150 */       i++;
/* 112:    */     }
/* 113:152 */     ParsedSql parsedSql = new ParsedSql(sqlToUse);
/* 114:153 */     for (ParameterHolder ph : parameterList) {
/* 115:154 */       parsedSql.addNamedParameter(ph.getParameterName(), ph.getStartIndex(), ph.getEndIndex());
/* 116:    */     }
/* 117:156 */     parsedSql.setNamedParameterCount(namedParameterCount);
/* 118:157 */     parsedSql.setUnnamedParameterCount(unnamedParameterCount);
/* 119:158 */     parsedSql.setTotalParameterCount(totalParameterCount);
/* 120:159 */     return parsedSql;
/* 121:    */   }
/* 122:    */   
/* 123:    */   private static int addNamedParameter(List<ParameterHolder> parameterList, int totalParameterCount, int escapes, int i, int j, String parameter)
/* 124:    */   {
/* 125:164 */     parameterList.add(new ParameterHolder(parameter, i - escapes, j - escapes));
/* 126:165 */     totalParameterCount++;
/* 127:166 */     return totalParameterCount;
/* 128:    */   }
/* 129:    */   
/* 130:    */   private static int addNewNamedParameter(Set<String> namedParameters, int namedParameterCount, String parameter)
/* 131:    */   {
/* 132:170 */     if (!namedParameters.contains(parameter))
/* 133:    */     {
/* 134:171 */       namedParameters.add(parameter);
/* 135:172 */       namedParameterCount++;
/* 136:    */     }
/* 137:174 */     return namedParameterCount;
/* 138:    */   }
/* 139:    */   
/* 140:    */   private static int skipCommentsAndQuotes(char[] statement, int position)
/* 141:    */   {
/* 142:184 */     for (int i = 0; i < START_SKIP.length; i++) {
/* 143:185 */       if (statement[position] == START_SKIP[i].charAt(0))
/* 144:    */       {
/* 145:186 */         boolean match = true;
/* 146:187 */         for (int j = 1; j < START_SKIP[i].length(); j++) {
/* 147:188 */           if (statement[(position + j)] != START_SKIP[i].charAt(j))
/* 148:    */           {
/* 149:189 */             match = false;
/* 150:190 */             break;
/* 151:    */           }
/* 152:    */         }
/* 153:193 */         if (match)
/* 154:    */         {
/* 155:194 */           int offset = START_SKIP[i].length();
/* 156:195 */           for (int m = position + offset; m < statement.length; m++) {
/* 157:196 */             if (statement[m] == STOP_SKIP[i].charAt(0))
/* 158:    */             {
/* 159:197 */               boolean endMatch = true;
/* 160:198 */               int endPos = m;
/* 161:199 */               for (int n = 1; n < STOP_SKIP[i].length(); n++)
/* 162:    */               {
/* 163:200 */                 if (m + n >= statement.length) {
/* 164:202 */                   return statement.length;
/* 165:    */                 }
/* 166:204 */                 if (statement[(m + n)] != STOP_SKIP[i].charAt(n))
/* 167:    */                 {
/* 168:205 */                   endMatch = false;
/* 169:206 */                   break;
/* 170:    */                 }
/* 171:208 */                 endPos = m + n;
/* 172:    */               }
/* 173:210 */               if (endMatch) {
/* 174:212 */                 return endPos + 1;
/* 175:    */               }
/* 176:    */             }
/* 177:    */           }
/* 178:217 */           return statement.length;
/* 179:    */         }
/* 180:    */       }
/* 181:    */     }
/* 182:222 */     return position;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public static String substituteNamedParameters(ParsedSql parsedSql, SqlParameterSource paramSource)
/* 186:    */   {
/* 187:243 */     String originalSql = parsedSql.getOriginalSql();
/* 188:244 */     StringBuilder actualSql = new StringBuilder();
/* 189:245 */     List paramNames = parsedSql.getParameterNames();
/* 190:246 */     int lastIndex = 0;
/* 191:247 */     for (int i = 0; i < paramNames.size(); i++)
/* 192:    */     {
/* 193:248 */       String paramName = (String)paramNames.get(i);
/* 194:249 */       int[] indexes = parsedSql.getParameterIndexes(i);
/* 195:250 */       int startIndex = indexes[0];
/* 196:251 */       int endIndex = indexes[1];
/* 197:252 */       actualSql.append(originalSql.substring(lastIndex, startIndex));
/* 198:253 */       if ((paramSource != null) && (paramSource.hasValue(paramName)))
/* 199:    */       {
/* 200:254 */         Object value = paramSource.getValue(paramName);
/* 201:255 */         if ((value instanceof Collection))
/* 202:    */         {
/* 203:256 */           Iterator entryIter = ((Collection)value).iterator();
/* 204:257 */           int k = 0;
/* 205:258 */           while (entryIter.hasNext())
/* 206:    */           {
/* 207:259 */             if (k > 0) {
/* 208:260 */               actualSql.append(", ");
/* 209:    */             }
/* 210:262 */             k++;
/* 211:263 */             Object entryItem = entryIter.next();
/* 212:264 */             if ((entryItem instanceof Object[]))
/* 213:    */             {
/* 214:265 */               Object[] expressionList = (Object[])entryItem;
/* 215:266 */               actualSql.append("(");
/* 216:267 */               for (int m = 0; m < expressionList.length; m++)
/* 217:    */               {
/* 218:268 */                 if (m > 0) {
/* 219:269 */                   actualSql.append(", ");
/* 220:    */                 }
/* 221:271 */                 actualSql.append("?");
/* 222:    */               }
/* 223:273 */               actualSql.append(")");
/* 224:    */             }
/* 225:    */             else
/* 226:    */             {
/* 227:276 */               actualSql.append("?");
/* 228:    */             }
/* 229:    */           }
/* 230:    */         }
/* 231:    */         else
/* 232:    */         {
/* 233:281 */           actualSql.append("?");
/* 234:    */         }
/* 235:    */       }
/* 236:    */       else
/* 237:    */       {
/* 238:285 */         actualSql.append("?");
/* 239:    */       }
/* 240:287 */       lastIndex = endIndex;
/* 241:    */     }
/* 242:289 */     actualSql.append(originalSql.substring(lastIndex, originalSql.length()));
/* 243:290 */     return actualSql.toString();
/* 244:    */   }
/* 245:    */   
/* 246:    */   public static Object[] buildValueArray(ParsedSql parsedSql, SqlParameterSource paramSource, List<SqlParameter> declaredParams)
/* 247:    */   {
/* 248:305 */     Object[] paramArray = new Object[parsedSql.getTotalParameterCount()];
/* 249:306 */     if ((parsedSql.getNamedParameterCount() > 0) && (parsedSql.getUnnamedParameterCount() > 0)) {
/* 250:307 */       throw new InvalidDataAccessApiUsageException(
/* 251:308 */         "You can't mix named and traditional ? placeholders. You have " + 
/* 252:309 */         parsedSql.getNamedParameterCount() + " named parameter(s) and " + 
/* 253:310 */         parsedSql.getUnnamedParameterCount() + " traditonal placeholder(s) in [" + 
/* 254:311 */         parsedSql.getOriginalSql() + "]");
/* 255:    */     }
/* 256:313 */     List<String> paramNames = parsedSql.getParameterNames();
/* 257:314 */     for (int i = 0; i < paramNames.size(); i++)
/* 258:    */     {
/* 259:315 */       String paramName = (String)paramNames.get(i);
/* 260:    */       try
/* 261:    */       {
/* 262:317 */         Object value = paramSource.getValue(paramName);
/* 263:318 */         SqlParameter param = findParameter(declaredParams, paramName, i);
/* 264:319 */         paramArray[i] = (param != null ? new SqlParameterValue(param, value) : value);
/* 265:    */       }
/* 266:    */       catch (IllegalArgumentException ex)
/* 267:    */       {
/* 268:322 */         throw new InvalidDataAccessApiUsageException(
/* 269:323 */           "No value supplied for the SQL parameter '" + paramName + "': " + ex.getMessage());
/* 270:    */       }
/* 271:    */     }
/* 272:326 */     return paramArray;
/* 273:    */   }
/* 274:    */   
/* 275:    */   private static SqlParameter findParameter(List<SqlParameter> declaredParams, String paramName, int paramIndex)
/* 276:    */   {
/* 277:337 */     if (declaredParams != null)
/* 278:    */     {
/* 279:339 */       for (SqlParameter declaredParam : declaredParams) {
/* 280:340 */         if (paramName.equals(declaredParam.getName())) {
/* 281:341 */           return declaredParam;
/* 282:    */         }
/* 283:    */       }
/* 284:345 */       if (paramIndex < declaredParams.size())
/* 285:    */       {
/* 286:346 */         SqlParameter declaredParam = (SqlParameter)declaredParams.get(paramIndex);
/* 287:348 */         if (declaredParam.getName() == null) {
/* 288:349 */           return declaredParam;
/* 289:    */         }
/* 290:    */       }
/* 291:    */     }
/* 292:353 */     return null;
/* 293:    */   }
/* 294:    */   
/* 295:    */   private static boolean isParameterSeparator(char c)
/* 296:    */   {
/* 297:361 */     if (Character.isWhitespace(c)) {
/* 298:362 */       return true;
/* 299:    */     }
/* 300:364 */     for (char separator : PARAMETER_SEPARATORS) {
/* 301:365 */       if (c == separator) {
/* 302:366 */         return true;
/* 303:    */       }
/* 304:    */     }
/* 305:369 */     return false;
/* 306:    */   }
/* 307:    */   
/* 308:    */   public static int[] buildSqlTypeArray(ParsedSql parsedSql, SqlParameterSource paramSource)
/* 309:    */   {
/* 310:381 */     int[] sqlTypes = new int[parsedSql.getTotalParameterCount()];
/* 311:382 */     List<String> paramNames = parsedSql.getParameterNames();
/* 312:383 */     for (int i = 0; i < paramNames.size(); i++)
/* 313:    */     {
/* 314:384 */       String paramName = (String)paramNames.get(i);
/* 315:385 */       sqlTypes[i] = paramSource.getSqlType(paramName);
/* 316:    */     }
/* 317:387 */     return sqlTypes;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public static List<SqlParameter> buildSqlParameterList(ParsedSql parsedSql, SqlParameterSource paramSource)
/* 321:    */   {
/* 322:399 */     List<String> paramNames = parsedSql.getParameterNames();
/* 323:400 */     List<SqlParameter> params = new LinkedList();
/* 324:401 */     for (String paramName : paramNames)
/* 325:    */     {
/* 326:402 */       SqlParameter param = new SqlParameter(
/* 327:403 */         paramName, 
/* 328:404 */         paramSource.getSqlType(paramName), 
/* 329:405 */         paramSource.getTypeName(paramName));
/* 330:406 */       params.add(param);
/* 331:    */     }
/* 332:408 */     return params;
/* 333:    */   }
/* 334:    */   
/* 335:    */   public static String parseSqlStatementIntoString(String sql)
/* 336:    */   {
/* 337:425 */     ParsedSql parsedSql = parseSqlStatement(sql);
/* 338:426 */     return substituteNamedParameters(parsedSql, null);
/* 339:    */   }
/* 340:    */   
/* 341:    */   public static String substituteNamedParameters(String sql, SqlParameterSource paramSource)
/* 342:    */   {
/* 343:440 */     ParsedSql parsedSql = parseSqlStatement(sql);
/* 344:441 */     return substituteNamedParameters(parsedSql, paramSource);
/* 345:    */   }
/* 346:    */   
/* 347:    */   public static Object[] buildValueArray(String sql, Map<String, ?> paramMap)
/* 348:    */   {
/* 349:453 */     ParsedSql parsedSql = parseSqlStatement(sql);
/* 350:454 */     return buildValueArray(parsedSql, new MapSqlParameterSource(paramMap), null);
/* 351:    */   }
/* 352:    */   
/* 353:    */   private static class ParameterHolder
/* 354:    */   {
/* 355:    */     private String parameterName;
/* 356:    */     private int startIndex;
/* 357:    */     private int endIndex;
/* 358:    */     
/* 359:    */     public ParameterHolder(String parameterName, int startIndex, int endIndex)
/* 360:    */     {
/* 361:464 */       this.parameterName = parameterName;
/* 362:465 */       this.startIndex = startIndex;
/* 363:466 */       this.endIndex = endIndex;
/* 364:    */     }
/* 365:    */     
/* 366:    */     public String getParameterName()
/* 367:    */     {
/* 368:470 */       return this.parameterName;
/* 369:    */     }
/* 370:    */     
/* 371:    */     public int getStartIndex()
/* 372:    */     {
/* 373:474 */       return this.startIndex;
/* 374:    */     }
/* 375:    */     
/* 376:    */     public int getEndIndex()
/* 377:    */     {
/* 378:478 */       return this.endIndex;
/* 379:    */     }
/* 380:    */   }
/* 381:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.NamedParameterUtils
 * JD-Core Version:    0.7.0.1
 */