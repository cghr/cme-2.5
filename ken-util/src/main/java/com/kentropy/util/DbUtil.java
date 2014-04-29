/*   1:    */ package com.kentropy.util;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.sql.ResultSet;
/*   5:    */ import java.sql.ResultSetMetaData;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import java.util.Set;
/*  13:    */ import net.xoetrope.xui.data.XBaseModel;
/*  14:    */ import net.xoetrope.xui.data.XModel;
/*  15:    */ import org.apache.log4j.Logger;
/*  16:    */ import org.json.simple.JSONObject;
/*  17:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  18:    */ import org.springframework.jdbc.core.RowMapper;
/*  19:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  20:    */ import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
/*  21:    */ 
/*  22:    */ public class DbUtil
/*  23:    */ {
/*  24: 26 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  25: 28 */   String sql = "";
/*  26: 29 */   String field = "";
/*  27:    */   String value;
/*  28: 31 */   SqlRowSet rs = null;
/*  29: 32 */   Logger log = Logger.getLogger(DbUtil.class);
/*  30:    */   
/*  31:    */   public void saveData(String table, String where, XModel xm)
/*  32:    */   {
/*  33: 36 */     StringBuffer fields = new StringBuffer();
/*  34: 37 */     int children = xm.getNumChildren();
/*  35: 38 */     if (children <= 0)
/*  36:    */     {
/*  37: 40 */       this.log.info("No data in Xmodel");
/*  38: 41 */       return;
/*  39:    */     }
/*  40: 44 */     this.sql = ("SELECT COUNT(*) FROM " + table + " WHERE " + where);
/*  41: 45 */     int count = this.jt.queryForInt(this.sql);
/*  42:    */     
/*  43: 47 */     Object[] values = new Object[children];
/*  44: 48 */     StringBuffer wildCards = new StringBuffer();
/*  45: 49 */     StringBuffer updts = new StringBuffer();
/*  46: 50 */     updts.append("SET ");
/*  47: 51 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  48:    */     {
/*  49: 53 */       this.field = xm.get(i).getId();
/*  50: 54 */       this.value = ((String)((XModel)xm.get(this.field)).get());
/*  51:    */       
/*  52: 56 */       fields.append(this.field + ",");
/*  53: 57 */       wildCards.append("?,");
/*  54: 58 */       values[i] = this.value;
/*  55: 59 */       updts.append(this.field + "=?,");
/*  56:    */     }
/*  57: 62 */     fields.deleteCharAt(fields.length() - 1);
/*  58: 63 */     wildCards.deleteCharAt(wildCards.length() - 1);
/*  59: 64 */     updts.deleteCharAt(updts.length() - 1);
/*  60: 66 */     if ((where == null) || (count == 0)) {
/*  61: 67 */       this.sql = ("INSERT INTO " + table + " (" + fields + ") values(" + wildCards + ")");
/*  62:    */     } else {
/*  63: 69 */       this.sql = ("UPDATE " + table + " " + updts + " WHERE " + where);
/*  64:    */     }
/*  65: 71 */     this.log.debug("Query:: " + this.sql);
/*  66: 72 */     int updates = this.jt.update(this.sql, values);
/*  67: 73 */     this.log.debug("No of Updates::" + updates);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void saveDataFromMap(String table, String where, Map map)
/*  71:    */   {
/*  72: 79 */     StringBuffer fields = new StringBuffer();
/*  73: 80 */     int children = map.size();
/*  74: 82 */     if (children <= 0)
/*  75:    */     {
/*  76: 84 */       this.log.info("No data in Map");
/*  77: 85 */       return;
/*  78:    */     }
/*  79: 88 */     this.sql = ("SELECT COUNT(*) FROM " + table + " WHERE " + where);
/*  80: 89 */     int count = this.jt.queryForInt(this.sql);
/*  81:    */     
/*  82: 91 */     Object[] values = new Object[children];
/*  83: 92 */     StringBuffer wildCards = new StringBuffer();
/*  84: 93 */     StringBuffer updts = new StringBuffer();
/*  85: 94 */     updts.append("SET ");
/*  86:    */     
/*  87: 96 */     Set keys = map.keySet();
/*  88: 97 */     Iterator itr = keys.iterator();
/*  89:    */     
/*  90: 99 */     int i = 0;
/*  91:100 */     while (itr.hasNext())
/*  92:    */     {
/*  93:102 */       this.field = ((String)itr.next());
/*  94:103 */       this.value = ((String)map.get(this.field));
/*  95:    */       
/*  96:105 */       fields.append(this.field + ",");
/*  97:106 */       wildCards.append("?,");
/*  98:107 */       values[i] = this.value;
/*  99:108 */       updts.append(this.field + "=?,");
/* 100:109 */       i++;
/* 101:    */     }
/* 102:114 */     fields.deleteCharAt(fields.length() - 1);
/* 103:115 */     wildCards.deleteCharAt(wildCards.length() - 1);
/* 104:116 */     updts.deleteCharAt(updts.length() - 1);
/* 105:118 */     if ((where == null) || (count == 0)) {
/* 106:119 */       this.sql = ("INSERT INTO " + table + " (" + fields + ") values(" + wildCards + ")");
/* 107:    */     } else {
/* 108:121 */       this.sql = ("UPDATE " + table + " " + updts + " WHERE " + where);
/* 109:    */     }
/* 110:123 */     this.log.debug("Query:: " + this.sql);
/* 111:124 */     int updates = this.jt.update(this.sql, values);
/* 112:125 */     this.log.debug("No of Updates::" + updates);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void saveKeyValue(String table, String key, String value)
/* 116:    */   {
/* 117:136 */     if ((key == null) || (key == "")) {
/* 118:137 */       return;
/* 119:    */     }
/* 120:138 */     this.log.debug("==> Key=" + key + "; Value=" + value);
/* 121:139 */     this.sql = ("SELECT value1 FROM " + table + " WHERE key1=?");
/* 122:140 */     this.rs = this.jt.queryForRowSet(this.sql, new Object[] { key });
/* 123:141 */     String execSql = "";
/* 124:142 */     int updates = 0;
/* 125:143 */     if (this.rs.next())
/* 126:    */     {
/* 127:145 */       execSql = "UPDATE " + table + " SET value1=? WHERE key1=?";
/* 128:146 */       this.log.debug("Query:: " + execSql);
/* 129:147 */       updates = this.jt.update(execSql, new Object[] { value, key });
/* 130:    */     }
/* 131:    */     else
/* 132:    */     {
/* 133:152 */       execSql = "INSERT INTO " + table + " values(?,?)";
/* 134:153 */       this.log.debug("Query:: " + execSql);
/* 135:154 */       updates = this.jt.update(execSql, new Object[] { key, value });
/* 136:    */     }
/* 137:158 */     this.log.debug("No of Updates::" + updates);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public String getKeyValue(String table, String path)
/* 141:    */   {
/* 142:164 */     return (String)this.jt.queryForObject("SELECT value1 FROM " + table + " WHERE key1=?", new Object[] { path }, String.class);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public XModel getData(String table, String cols, String where)
/* 146:    */   {
/* 147:170 */     XModel xm = new XBaseModel();
/* 148:171 */     String sql = "";
/* 149:172 */     if (where != null)
/* 150:    */     {
/* 151:174 */       sql = "SELECT " + cols + " from " + table + " where " + where;
/* 152:175 */       this.log.debug("Get Data: " + sql);
/* 153:    */     }
/* 154:    */     else
/* 155:    */     {
/* 156:178 */       sql = "SELECT " + cols + " from " + table;
/* 157:    */     }
/* 158:179 */     this.rs = this.jt.queryForRowSet(sql);
/* 159:180 */     SqlRowSetMetaData rsmd = this.rs.getMetaData();
/* 160:181 */     int columnCount = rsmd.getColumnCount();
/* 161:    */     int i;
/* 162:182 */     for (; this.rs.next(); i <= columnCount)
/* 163:    */     {
/* 164:184 */       XModel row = new XBaseModel();
/* 165:185 */       i = 1; continue;
/* 166:    */       
/* 167:187 */       row.set(rsmd.getColumnName(i), this.rs.getString(i));
/* 168:    */       
/* 169:189 */       xm.append(row);i++;
/* 170:    */     }
/* 171:197 */     return xm;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String getRowAsNameValuePair(String path)
/* 175:    */   {
/* 176:202 */     String[] tmp = path.split("/");
/* 177:203 */     String[] constraint = tmp[2].split(":");
/* 178:204 */     StringBuffer json = new StringBuffer();
/* 179:205 */     json.append("[");
/* 180:206 */     this.log.debug(tmp[2].replace(":", "="));
/* 181:    */     
/* 182:208 */     List<Map> list = getDataAsListofMaps(tmp[0], tmp[1], constraint[0] + "='" + constraint[1] + "'");
/* 183:221 */     if (list.size() > 0)
/* 184:    */     {
/* 185:223 */       Map row = (Map)list.get(0);
/* 186:224 */       Iterator itr = row.keySet().iterator();
/* 187:226 */       while (itr.hasNext())
/* 188:    */       {
/* 189:228 */         String key = (String)itr.next();
/* 190:229 */         String value = (String)row.get(key);
/* 191:    */         
/* 192:231 */         json.append("{name:\"" + JSONObject.escape(key) + "\",");
/* 193:232 */         json.append("value:\"" + JSONObject.escape(value) + "\"},");
/* 194:    */       }
/* 195:    */     }
/* 196:243 */     json.deleteCharAt(json.length() - 1);
/* 197:244 */     json.append("]");
/* 198:245 */     if (json.length() == 1) {
/* 199:246 */       return "[]";
/* 200:    */     }
/* 201:250 */     return json.toString();
/* 202:    */   }
/* 203:    */   
/* 204:    */   @Deprecated
/* 205:    */   public boolean deleteData(String table, String where)
/* 206:    */   {
/* 207:256 */     this.sql = ("DELETE FROM " + table + " WHERE " + where);
/* 208:257 */     this.log.debug("Delete Data :: " + this.sql);
/* 209:258 */     int updates = this.jt.update(this.sql);
/* 210:259 */     this.log.debug("NO.of Rows Deleted :: " + updates);
/* 211:260 */     return true;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public boolean deleteData(String table, String where, Object[] object)
/* 215:    */   {
/* 216:267 */     this.sql = ("DELETE FROM " + table + " WHERE " + where);
/* 217:268 */     this.log.debug("Delete Data :: " + this.sql);
/* 218:269 */     int updates = this.jt.update(this.sql, object);
/* 219:270 */     this.log.debug("NO.of Rows Deleted :: " + updates);
/* 220:271 */     return true;
/* 221:    */   }
/* 222:    */   
/* 223:    */   @Deprecated
/* 224:    */   public String uniqueResult(String table, String column, String where)
/* 225:    */   {
/* 226:281 */     if (where == null) {
/* 227:282 */       this.sql = ("SELECT " + column + " FROM " + table);
/* 228:    */     } else {
/* 229:284 */       this.sql = ("SELECT " + column + " FROM " + table + " WHERE " + where);
/* 230:    */     }
/* 231:286 */     this.log.debug("Unique Result==>" + this.sql);
/* 232:287 */     this.rs = this.jt.queryForRowSet(this.sql);
/* 233:289 */     if (this.rs.next()) {
/* 234:290 */       return this.rs.getString(1);
/* 235:    */     }
/* 236:292 */     return "";
/* 237:    */   }
/* 238:    */   
/* 239:    */   public String uniqueResult(String table, String column, String where, Object[] object)
/* 240:    */   {
/* 241:298 */     if (where == null) {
/* 242:299 */       this.sql = ("SELECT " + column + " FROM " + table);
/* 243:    */     } else {
/* 244:301 */       this.sql = ("SELECT " + column + " FROM " + table + " WHERE " + where);
/* 245:    */     }
/* 246:303 */     this.log.debug("Unique Result==>" + this.sql);
/* 247:304 */     this.rs = this.jt.queryForRowSet(this.sql, object);
/* 248:306 */     if (this.rs.next()) {
/* 249:307 */       return this.rs.getString(1);
/* 250:    */     }
/* 251:309 */     return "";
/* 252:    */   }
/* 253:    */   
/* 254:    */   public String uniqueResultCached(String table, String column, String where)
/* 255:    */   {
/* 256:314 */     this.sql = ("SELECT SQL_CACHE " + column + " FROM " + table + " WHERE " + where);
/* 257:315 */     this.log.debug("Unique Result==>" + this.sql);
/* 258:316 */     this.rs = this.jt.queryForRowSet(this.sql);
/* 259:318 */     if (this.rs.next()) {
/* 260:319 */       return this.rs.getString(1);
/* 261:    */     }
/* 262:321 */     return "";
/* 263:    */   }
/* 264:    */   
/* 265:    */   public String uniqueResultCached(String sql)
/* 266:    */   {
/* 267:328 */     this.log.debug("Unique Result==>" + sql);
/* 268:329 */     this.rs = this.jt.queryForRowSet(sql);
/* 269:331 */     if (this.rs.next()) {
/* 270:332 */       return this.rs.getString(1);
/* 271:    */     }
/* 272:334 */     return "";
/* 273:    */   }
/* 274:    */   
/* 275:    */   @Deprecated
/* 276:    */   public List<Map> getDataAsListofMaps(String table, String cols, String where)
/* 277:    */   {
/* 278:342 */     String sql = "";
/* 279:343 */     if (where != null)
/* 280:    */     {
/* 281:345 */       sql = "SELECT " + cols + " from " + table + " where " + where;
/* 282:346 */       this.log.debug("Get Data As List : " + sql);
/* 283:    */     }
/* 284:    */     else
/* 285:    */     {
/* 286:349 */       sql = "SELECT " + cols + " from " + table;
/* 287:    */     }
/* 288:350 */     this.log.debug(sql);
/* 289:351 */     List<Map> result = new ArrayList();
/* 290:352 */     result = this.jt.query(sql, new RowMapper()
/* 291:    */     {
/* 292:    */       public Object mapRow(ResultSet rs, int arg)
/* 293:    */         throws SQLException
/* 294:    */       {
/* 295:355 */         ResultSetMetaData rsmd = rs.getMetaData();
/* 296:    */         
/* 297:357 */         Map<String, String> map = new HashMap();
/* 298:358 */         for (int i = 1; i <= rsmd.getColumnCount(); i++)
/* 299:    */         {
/* 300:360 */           String key = rsmd.getColumnLabel(i);
/* 301:361 */           String value = rs.getString(key);
/* 302:    */           
/* 303:363 */           map.put(key, value);
/* 304:    */         }
/* 305:370 */         return map;
/* 306:    */       }
/* 307:377 */     });
/* 308:378 */     return result;
/* 309:    */   }
/* 310:    */   
/* 311:    */   public List<Map> getDataAsListofMaps(String table, String cols, String where, Object[] object)
/* 312:    */   {
/* 313:385 */     String sql = "";
/* 314:386 */     if (where != null)
/* 315:    */     {
/* 316:388 */       sql = "SELECT " + cols + " from " + table + " where " + where;
/* 317:389 */       this.log.debug("Get Data As List : " + sql);
/* 318:    */     }
/* 319:    */     else
/* 320:    */     {
/* 321:392 */       sql = "SELECT " + cols + " from " + table;
/* 322:    */     }
/* 323:393 */     this.log.debug(sql);
/* 324:394 */     List<Map> result = new ArrayList();
/* 325:395 */     result = this.jt.query(sql, object, new RowMapper()
/* 326:    */     {
/* 327:    */       public Object mapRow(ResultSet rs, int arg)
/* 328:    */         throws SQLException
/* 329:    */       {
/* 330:398 */         ResultSetMetaData rsmd = rs.getMetaData();
/* 331:    */         
/* 332:400 */         Map<String, String> map = new HashMap();
/* 333:401 */         for (int i = 1; i <= rsmd.getColumnCount(); i++)
/* 334:    */         {
/* 335:403 */           String key = rsmd.getColumnLabel(i);
/* 336:404 */           String value = rs.getString(key);
/* 337:    */           
/* 338:406 */           map.put(key, value);
/* 339:    */         }
/* 340:413 */         return map;
/* 341:    */       }
/* 342:420 */     });
/* 343:421 */     return result;
/* 344:    */   }
/* 345:    */   
/* 346:    */   public List<Map> getDataAsListofMaps(String sql, Object[] args)
/* 347:    */   {
/* 348:428 */     List<Map> result = new ArrayList();
/* 349:429 */     result = this.jt.query(sql, args, new RowMapper()
/* 350:    */     {
/* 351:    */       public Object mapRow(ResultSet rs, int arg)
/* 352:    */         throws SQLException
/* 353:    */       {
/* 354:432 */         ResultSetMetaData rsmd = rs.getMetaData();
/* 355:    */         
/* 356:434 */         Map<String, String> map = new HashMap();
/* 357:435 */         for (int i = 1; i <= rsmd.getColumnCount(); i++)
/* 358:    */         {
/* 359:437 */           String key = rsmd.getColumnLabel(i);
/* 360:438 */           String value = rs.getString(key);
/* 361:    */           
/* 362:440 */           map.put(key, value);
/* 363:    */         }
/* 364:447 */         return map;
/* 365:    */       }
/* 366:454 */     });
/* 367:455 */     return result;
/* 368:    */   }
/* 369:    */   
/* 370:    */   public Map getRowAsMap(String table, String cols, String where, Object[] object)
/* 371:    */   {
/* 372:460 */     String sql = "";
/* 373:461 */     if (where != null)
/* 374:    */     {
/* 375:463 */       sql = "SELECT " + cols + " from " + table + " where " + where;
/* 376:464 */       this.log.debug("Get Data As List : " + sql);
/* 377:    */     }
/* 378:    */     else
/* 379:    */     {
/* 380:467 */       sql = "SELECT " + cols + " from " + table;
/* 381:    */     }
/* 382:468 */     this.log.debug(sql);
/* 383:469 */     List<Map> result = new ArrayList();
/* 384:    */     
/* 385:471 */     result = this.jt.query(sql, object, new RowMapper()
/* 386:    */     {
/* 387:    */       public Object mapRow(ResultSet rs, int arg)
/* 388:    */         throws SQLException
/* 389:    */       {
/* 390:474 */         ResultSetMetaData rsmd = rs.getMetaData();
/* 391:    */         
/* 392:476 */         Map<String, String> map = new HashMap();
/* 393:477 */         for (int i = 1; i <= rsmd.getColumnCount(); i++)
/* 394:    */         {
/* 395:479 */           String key = rsmd.getColumnLabel(i);
/* 396:480 */           String value = rs.getString(key);
/* 397:    */           
/* 398:482 */           map.put(key, value);
/* 399:    */         }
/* 400:489 */         return map;
/* 401:    */       }
/* 402:496 */     });
/* 403:497 */     return (Map)result.get(0);
/* 404:    */   }
/* 405:    */   
/* 406:    */   public List<StringBuffer[]> getDataAsListofStringBufferArrays(String table, String cols, String where, Object[] args)
/* 407:    */   {
/* 408:502 */     String sql = "";
/* 409:503 */     if (where != null)
/* 410:    */     {
/* 411:505 */       sql = "SELECT " + cols + " from " + table + " where " + where;
/* 412:506 */       this.log.debug("Get Data As List : " + sql);
/* 413:    */     }
/* 414:    */     else
/* 415:    */     {
/* 416:509 */       sql = "SELECT " + cols + " from " + table;
/* 417:    */     }
/* 418:510 */     this.log.debug(sql);
/* 419:511 */     List<StringBuffer[]> result = new ArrayList();
/* 420:    */     
/* 421:513 */     result = this.jt.query(sql, args, new RowMapper()
/* 422:    */     {
/* 423:    */       public Object mapRow(ResultSet rs, int arg)
/* 424:    */         throws SQLException
/* 425:    */       {
/* 426:516 */         ResultSetMetaData rsmd = rs.getMetaData();
/* 427:    */         
/* 428:518 */         StringBuffer[] row = new StringBuffer[rsmd.getColumnCount()];
/* 429:519 */         for (int i = 1; i <= rsmd.getColumnCount(); i++)
/* 430:    */         {
/* 431:521 */           String key = rsmd.getColumnLabel(i);
/* 432:522 */           String value = rs.getString(key);
/* 433:523 */           if (value == null) {
/* 434:524 */             value = "";
/* 435:    */           }
/* 436:527 */           row[(i - 1)] = new StringBuffer(value);
/* 437:    */         }
/* 438:534 */         return row;
/* 439:    */       }
/* 440:542 */     });
/* 441:543 */     return result;
/* 442:    */   }
/* 443:    */   
/* 444:    */   public StringBuffer getDataAsJSArray(String sql)
/* 445:    */   {
/* 446:548 */     StringBuffer js = new StringBuffer();
/* 447:    */     
/* 448:550 */     SqlRowSet rs = this.jt.queryForRowSet(sql);
/* 449:551 */     SqlRowSetMetaData rsmd = rs.getMetaData();
/* 450:    */     
/* 451:553 */     int cols = rsmd.getColumnCount();
/* 452:554 */     int rows = 0;
/* 453:    */     
/* 454:556 */     js.append("[");
/* 455:558 */     for (int i = 1; i <= cols; i++) {
/* 456:560 */       js.append("'" + rsmd.getColumnLabel(i) + "',");
/* 457:    */     }
/* 458:563 */     js.deleteCharAt(js.length() - 1);
/* 459:564 */     js.append("],");
/* 460:567 */     while (rs.next())
/* 461:    */     {
/* 462:570 */       js.append("[");
/* 463:572 */       for (int i = 1; i <= cols; i++) {
/* 464:575 */         if (i == 1) {
/* 465:576 */           js.append("'" + rs.getString(i) + "',");
/* 466:    */         } else {
/* 467:578 */           js.append(rs.getString(i) + ",");
/* 468:    */         }
/* 469:    */       }
/* 470:582 */       js.deleteCharAt(js.length() - 1);
/* 471:    */       
/* 472:    */ 
/* 473:    */ 
/* 474:586 */       js.append("],");
/* 475:587 */       rows++;
/* 476:    */     }
/* 477:590 */     if (rows > 0) {
/* 478:591 */       js.deleteCharAt(js.length() - 1);
/* 479:    */     }
/* 480:597 */     return js;
/* 481:    */   }
/* 482:    */   
/* 483:    */   public StringBuffer getDataAsJSArray(String sql, Object[] args)
/* 484:    */   {
/* 485:603 */     StringBuffer js = new StringBuffer();
/* 486:    */     
/* 487:605 */     SqlRowSet rs = this.jt.queryForRowSet(sql, args);
/* 488:606 */     SqlRowSetMetaData rsmd = rs.getMetaData();
/* 489:    */     
/* 490:608 */     int cols = rsmd.getColumnCount();
/* 491:609 */     int rows = 0;
/* 492:    */     
/* 493:611 */     js.append("[");
/* 494:613 */     for (int i = 1; i <= cols; i++) {
/* 495:615 */       js.append("'" + rsmd.getColumnLabel(i) + "',");
/* 496:    */     }
/* 497:618 */     js.deleteCharAt(js.length() - 1);
/* 498:619 */     js.append("],");
/* 499:622 */     while (rs.next())
/* 500:    */     {
/* 501:625 */       js.append("[");
/* 502:627 */       for (int i = 1; i <= cols; i++) {
/* 503:630 */         if (i == 1) {
/* 504:631 */           js.append("'" + rs.getString(i) + "',");
/* 505:    */         } else {
/* 506:633 */           js.append(rs.getString(i) + ",");
/* 507:    */         }
/* 508:    */       }
/* 509:637 */       js.deleteCharAt(js.length() - 1);
/* 510:    */       
/* 511:    */ 
/* 512:    */ 
/* 513:641 */       js.append("],");
/* 514:642 */       rows++;
/* 515:    */     }
/* 516:645 */     if (rows > 0) {
/* 517:646 */       js.deleteCharAt(js.length() - 1);
/* 518:    */     }
/* 519:652 */     return js;
/* 520:    */   }
/* 521:    */   
/* 522:    */   public StringBuffer getDataAsJSArrayWithAllStrings(String sql, Object[] args)
/* 523:    */   {
/* 524:657 */     StringBuffer js = new StringBuffer();
/* 525:    */     
/* 526:659 */     SqlRowSet rs = this.jt.queryForRowSet(sql, args);
/* 527:660 */     SqlRowSetMetaData rsmd = rs.getMetaData();
/* 528:    */     
/* 529:662 */     int cols = rsmd.getColumnCount();
/* 530:663 */     int rows = 0;
/* 531:    */     
/* 532:665 */     js.append("[");
/* 533:667 */     for (int i = 1; i <= cols; i++) {
/* 534:669 */       js.append("'" + rsmd.getColumnLabel(i) + "',");
/* 535:    */     }
/* 536:672 */     js.deleteCharAt(js.length() - 1);
/* 537:673 */     js.append("],");
/* 538:676 */     while (rs.next())
/* 539:    */     {
/* 540:679 */       js.append("[");
/* 541:681 */       for (int i = 1; i <= cols; i++) {
/* 542:685 */         js.append("'" + rs.getString(i) + "',");
/* 543:    */       }
/* 544:690 */       js.deleteCharAt(js.length() - 1);
/* 545:    */       
/* 546:    */ 
/* 547:    */ 
/* 548:694 */       js.append("],");
/* 549:695 */       rows++;
/* 550:    */     }
/* 551:698 */     if (rows > 0) {
/* 552:699 */       js.deleteCharAt(js.length() - 1);
/* 553:    */     }
/* 554:705 */     return js;
/* 555:    */   }
/* 556:    */   
/* 557:    */   public StringBuffer getDataAsJSArrayTranspose(String sql, Object[] args)
/* 558:    */   {
/* 559:712 */     StringBuffer js = new StringBuffer();
/* 560:    */     
/* 561:714 */     SqlRowSet rs = this.jt.queryForRowSet(sql, args);
/* 562:715 */     SqlRowSetMetaData rsmd = rs.getMetaData();
/* 563:    */     
/* 564:717 */     int cols = rsmd.getColumnCount();
/* 565:718 */     int rows = 0;
/* 566:    */     
/* 567:720 */     js.append("['Property','Value'],");
/* 568:723 */     while (rs.next())
/* 569:    */     {
/* 570:728 */       for (int i = 1; i <= cols; i++) {
/* 571:729 */         js.append("['" + rsmd.getColumnLabel(i) + "','" + rs.getString(i) + "'],");
/* 572:    */       }
/* 573:733 */       js.deleteCharAt(js.length() - 1);
/* 574:    */       
/* 575:735 */       rows++;
/* 576:    */     }
/* 577:743 */     return js;
/* 578:    */   }
/* 579:    */   
/* 580:    */   public StringBuffer getRowAsJSON(String table, String cols, String where, Object[] object)
/* 581:    */   {
/* 582:751 */     DbUtil db = new DbUtil();
/* 583:    */     
/* 584:753 */     Map row = (Map)db.getDataAsListofMaps(table, cols, where, object).get(0);
/* 585:754 */     Set keys = row.keySet();
/* 586:755 */     Iterator itr = keys.iterator();
/* 587:756 */     StringBuffer result = new StringBuffer();
/* 588:757 */     result.append("{");
/* 589:758 */     while (itr.hasNext())
/* 590:    */     {
/* 591:760 */       String key = (String)itr.next();
/* 592:761 */       result.append("\"" + key + "\":\"" + JSONObject.escape((String)row.get(key)) + "\",");
/* 593:    */     }
/* 594:767 */     result.deleteCharAt(result.length() - 1);
/* 595:768 */     result.append("}");
/* 596:    */     
/* 597:770 */     return result;
/* 598:    */   }
/* 599:    */   
/* 600:    */   public StringBuffer getDataAsJSON(String table, String cols, String where, Object[] object)
/* 601:    */   {
/* 602:777 */     DbUtil db = new DbUtil();
/* 603:778 */     List list = db.getDataAsListofMaps(table, cols, where, object);
/* 604:779 */     StringBuffer result = new StringBuffer();
/* 605:780 */     result.append("[");
/* 606:781 */     for (int i = 0; i < list.size(); i++)
/* 607:    */     {
/* 608:783 */       Map row = (Map)db.getDataAsListofMaps(table, cols, where, object).get(i);
/* 609:784 */       Set keys = row.keySet();
/* 610:785 */       Iterator itr = keys.iterator();
/* 611:    */       
/* 612:787 */       result.append("{");
/* 613:788 */       while (itr.hasNext())
/* 614:    */       {
/* 615:790 */         String key = (String)itr.next();
/* 616:791 */         result.append("\"" + key + "\":\"" + JSONObject.escape((String)row.get(key)) + "\",");
/* 617:    */       }
/* 618:797 */       result.deleteCharAt(result.length() - 1);
/* 619:798 */       result.append("},");
/* 620:    */     }
/* 621:800 */     result.deleteCharAt(result.length() - 1);
/* 622:801 */     result.append("]");
/* 623:    */     
/* 624:803 */     return result;
/* 625:    */   }
/* 626:    */   
/* 627:    */   public StringBuffer getDataAsJSON(String sql, Object[] object)
/* 628:    */   {
/* 629:810 */     DbUtil db = new DbUtil();
/* 630:811 */     List list = db.getDataAsListofMaps(sql, object);
/* 631:    */     
/* 632:813 */     StringBuffer result = new StringBuffer();
/* 633:814 */     result.append("[");
/* 634:815 */     for (int i = 0; i < list.size(); i++)
/* 635:    */     {
/* 636:817 */       Map row = (Map)list.get(i);
/* 637:818 */       Set keys = row.keySet();
/* 638:819 */       Iterator itr = keys.iterator();
/* 639:    */       
/* 640:821 */       result.append("{");
/* 641:822 */       while (itr.hasNext())
/* 642:    */       {
/* 643:824 */         String key = (String)itr.next();
/* 644:825 */         result.append("\"" + key + "\":\"" + JSONObject.escape((String)row.get(key)) + "\",");
/* 645:    */       }
/* 646:831 */       result.deleteCharAt(result.length() - 1);
/* 647:832 */       result.append("},");
/* 648:    */     }
/* 649:834 */     if (list.size() > 0) {
/* 650:835 */       result.deleteCharAt(result.length() - 1);
/* 651:    */     }
/* 652:837 */     result.append("]");
/* 653:    */     
/* 654:839 */     return result;
/* 655:    */   }
/* 656:    */   
/* 657:    */   public void execSQl(String sql)
/* 658:    */   {
/* 659:847 */     this.jt.execute(sql);
/* 660:    */   }
/* 661:    */   
/* 662:    */   public StringBuffer getColsAsCommaSeparated(String sql)
/* 663:    */   {
/* 664:854 */     SqlRowSet rs = this.jt.queryForRowSet(sql);
/* 665:855 */     SqlRowSetMetaData rsmd = rs.getMetaData();
/* 666:856 */     StringBuffer result = new StringBuffer();
/* 667:858 */     for (int i = 1; i <= rsmd.getColumnCount(); i++) {
/* 668:859 */       result.append(rsmd.getColumnLabel(i) + ",");
/* 669:    */     }
/* 670:861 */     result.deleteCharAt(result.length() - 1);
/* 671:    */     
/* 672:    */ 
/* 673:    */ 
/* 674:865 */     return result;
/* 675:    */   }
/* 676:    */   
/* 677:    */   public static void main(String[] args)
/* 678:    */   {
/* 679:870 */     DbUtil db = new DbUtil();
/* 680:    */     
/* 681:872 */     System.out.println(db.getColsAsCommaSeparated("select  * from patients"));
/* 682:    */   }
/* 683:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.DbUtil
 * JD-Core Version:    0.7.0.1
 */