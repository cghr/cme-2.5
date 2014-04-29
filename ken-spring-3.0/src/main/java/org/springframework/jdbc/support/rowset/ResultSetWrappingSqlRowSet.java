/*   1:    */ package org.springframework.jdbc.support.rowset;
/*   2:    */ 
/*   3:    */ import java.math.BigDecimal;
/*   4:    */ import java.sql.Date;
/*   5:    */ import java.sql.ResultSet;
/*   6:    */ import java.sql.ResultSetMetaData;
/*   7:    */ import java.sql.SQLException;
/*   8:    */ import java.sql.Time;
/*   9:    */ import java.sql.Timestamp;
/*  10:    */ import java.util.Calendar;
/*  11:    */ import java.util.Collections;
/*  12:    */ import java.util.HashMap;
/*  13:    */ import java.util.Map;
/*  14:    */ import org.springframework.jdbc.InvalidResultSetAccessException;
/*  15:    */ 
/*  16:    */ public class ResultSetWrappingSqlRowSet
/*  17:    */   implements SqlRowSet
/*  18:    */ {
/*  19:    */   private static final long serialVersionUID = -4688694393146734764L;
/*  20:    */   private final ResultSet resultSet;
/*  21:    */   private final SqlRowSetMetaData rowSetMetaData;
/*  22:    */   private final Map<String, Integer> columnLabelMap;
/*  23:    */   
/*  24:    */   public ResultSetWrappingSqlRowSet(ResultSet resultSet)
/*  25:    */     throws InvalidResultSetAccessException
/*  26:    */   {
/*  27: 89 */     this.resultSet = resultSet;
/*  28:    */     try
/*  29:    */     {
/*  30: 91 */       this.rowSetMetaData = new ResultSetWrappingSqlRowSetMetaData(resultSet.getMetaData());
/*  31:    */     }
/*  32:    */     catch (SQLException se)
/*  33:    */     {
/*  34: 94 */       throw new InvalidResultSetAccessException(se);
/*  35:    */     }
/*  36:    */     try
/*  37:    */     {
/*  38: 97 */       ResultSetMetaData rsmd = resultSet.getMetaData();
/*  39: 98 */       if (rsmd != null)
/*  40:    */       {
/*  41: 99 */         int columnCount = rsmd.getColumnCount();
/*  42:100 */         this.columnLabelMap = new HashMap(columnCount);
/*  43:101 */         for (int i = 1; i <= columnCount; i++) {
/*  44:102 */           this.columnLabelMap.put(rsmd.getColumnLabel(i), Integer.valueOf(i));
/*  45:    */         }
/*  46:    */       }
/*  47:    */       else
/*  48:    */       {
/*  49:106 */         this.columnLabelMap = Collections.emptyMap();
/*  50:    */       }
/*  51:    */     }
/*  52:    */     catch (SQLException se)
/*  53:    */     {
/*  54:110 */       throw new InvalidResultSetAccessException(se);
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public final ResultSet getResultSet()
/*  59:    */   {
/*  60:122 */     return this.resultSet;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public final SqlRowSetMetaData getMetaData()
/*  64:    */   {
/*  65:129 */     return this.rowSetMetaData;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int findColumn(String columnLabel)
/*  69:    */     throws InvalidResultSetAccessException
/*  70:    */   {
/*  71:136 */     Integer columnIndex = (Integer)this.columnLabelMap.get(columnLabel);
/*  72:137 */     if (columnIndex != null) {
/*  73:138 */       return columnIndex.intValue();
/*  74:    */     }
/*  75:    */     try
/*  76:    */     {
/*  77:142 */       return this.resultSet.findColumn(columnLabel);
/*  78:    */     }
/*  79:    */     catch (SQLException se)
/*  80:    */     {
/*  81:145 */       throw new InvalidResultSetAccessException(se);
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public BigDecimal getBigDecimal(int columnIndex)
/*  86:    */     throws InvalidResultSetAccessException
/*  87:    */   {
/*  88:    */     try
/*  89:    */     {
/*  90:158 */       return this.resultSet.getBigDecimal(columnIndex);
/*  91:    */     }
/*  92:    */     catch (SQLException se)
/*  93:    */     {
/*  94:161 */       throw new InvalidResultSetAccessException(se);
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public BigDecimal getBigDecimal(String columnLabel)
/*  99:    */     throws InvalidResultSetAccessException
/* 100:    */   {
/* 101:169 */     return getBigDecimal(findColumn(columnLabel));
/* 102:    */   }
/* 103:    */   
/* 104:    */   public boolean getBoolean(int columnIndex)
/* 105:    */     throws InvalidResultSetAccessException
/* 106:    */   {
/* 107:    */     try
/* 108:    */     {
/* 109:177 */       return this.resultSet.getBoolean(columnIndex);
/* 110:    */     }
/* 111:    */     catch (SQLException se)
/* 112:    */     {
/* 113:180 */       throw new InvalidResultSetAccessException(se);
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean getBoolean(String columnLabel)
/* 118:    */     throws InvalidResultSetAccessException
/* 119:    */   {
/* 120:188 */     return getBoolean(findColumn(columnLabel));
/* 121:    */   }
/* 122:    */   
/* 123:    */   public byte getByte(int columnIndex)
/* 124:    */     throws InvalidResultSetAccessException
/* 125:    */   {
/* 126:    */     try
/* 127:    */     {
/* 128:196 */       return this.resultSet.getByte(columnIndex);
/* 129:    */     }
/* 130:    */     catch (SQLException se)
/* 131:    */     {
/* 132:199 */       throw new InvalidResultSetAccessException(se);
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   public byte getByte(String columnLabel)
/* 137:    */     throws InvalidResultSetAccessException
/* 138:    */   {
/* 139:207 */     return getByte(findColumn(columnLabel));
/* 140:    */   }
/* 141:    */   
/* 142:    */   public Date getDate(int columnIndex, Calendar cal)
/* 143:    */     throws InvalidResultSetAccessException
/* 144:    */   {
/* 145:    */     try
/* 146:    */     {
/* 147:215 */       return this.resultSet.getDate(columnIndex, cal);
/* 148:    */     }
/* 149:    */     catch (SQLException se)
/* 150:    */     {
/* 151:218 */       throw new InvalidResultSetAccessException(se);
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public Date getDate(int columnIndex)
/* 156:    */     throws InvalidResultSetAccessException
/* 157:    */   {
/* 158:    */     try
/* 159:    */     {
/* 160:227 */       return this.resultSet.getDate(columnIndex);
/* 161:    */     }
/* 162:    */     catch (SQLException se)
/* 163:    */     {
/* 164:230 */       throw new InvalidResultSetAccessException(se);
/* 165:    */     }
/* 166:    */   }
/* 167:    */   
/* 168:    */   public Date getDate(String columnLabel, Calendar cal)
/* 169:    */     throws InvalidResultSetAccessException
/* 170:    */   {
/* 171:237 */     return getDate(findColumn(columnLabel), cal);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public Date getDate(String columnLabel)
/* 175:    */     throws InvalidResultSetAccessException
/* 176:    */   {
/* 177:244 */     return getDate(findColumn(columnLabel));
/* 178:    */   }
/* 179:    */   
/* 180:    */   public double getDouble(int columnIndex)
/* 181:    */     throws InvalidResultSetAccessException
/* 182:    */   {
/* 183:    */     try
/* 184:    */     {
/* 185:252 */       return this.resultSet.getDouble(columnIndex);
/* 186:    */     }
/* 187:    */     catch (SQLException se)
/* 188:    */     {
/* 189:255 */       throw new InvalidResultSetAccessException(se);
/* 190:    */     }
/* 191:    */   }
/* 192:    */   
/* 193:    */   public double getDouble(String columnLabel)
/* 194:    */     throws InvalidResultSetAccessException
/* 195:    */   {
/* 196:263 */     return getDouble(findColumn(columnLabel));
/* 197:    */   }
/* 198:    */   
/* 199:    */   public float getFloat(int columnIndex)
/* 200:    */     throws InvalidResultSetAccessException
/* 201:    */   {
/* 202:    */     try
/* 203:    */     {
/* 204:271 */       return this.resultSet.getFloat(columnIndex);
/* 205:    */     }
/* 206:    */     catch (SQLException se)
/* 207:    */     {
/* 208:274 */       throw new InvalidResultSetAccessException(se);
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   public float getFloat(String columnLabel)
/* 213:    */     throws InvalidResultSetAccessException
/* 214:    */   {
/* 215:282 */     return getFloat(findColumn(columnLabel));
/* 216:    */   }
/* 217:    */   
/* 218:    */   public int getInt(int columnIndex)
/* 219:    */     throws InvalidResultSetAccessException
/* 220:    */   {
/* 221:    */     try
/* 222:    */     {
/* 223:289 */       return this.resultSet.getInt(columnIndex);
/* 224:    */     }
/* 225:    */     catch (SQLException se)
/* 226:    */     {
/* 227:292 */       throw new InvalidResultSetAccessException(se);
/* 228:    */     }
/* 229:    */   }
/* 230:    */   
/* 231:    */   public int getInt(String columnLabel)
/* 232:    */     throws InvalidResultSetAccessException
/* 233:    */   {
/* 234:300 */     return getInt(findColumn(columnLabel));
/* 235:    */   }
/* 236:    */   
/* 237:    */   public long getLong(int columnIndex)
/* 238:    */     throws InvalidResultSetAccessException
/* 239:    */   {
/* 240:    */     try
/* 241:    */     {
/* 242:308 */       return this.resultSet.getLong(columnIndex);
/* 243:    */     }
/* 244:    */     catch (SQLException se)
/* 245:    */     {
/* 246:311 */       throw new InvalidResultSetAccessException(se);
/* 247:    */     }
/* 248:    */   }
/* 249:    */   
/* 250:    */   public long getLong(String columnLabel)
/* 251:    */     throws InvalidResultSetAccessException
/* 252:    */   {
/* 253:319 */     return getLong(findColumn(columnLabel));
/* 254:    */   }
/* 255:    */   
/* 256:    */   public Object getObject(int i, Map<String, Class<?>> map)
/* 257:    */     throws InvalidResultSetAccessException
/* 258:    */   {
/* 259:    */     try
/* 260:    */     {
/* 261:327 */       return this.resultSet.getObject(i, map);
/* 262:    */     }
/* 263:    */     catch (SQLException se)
/* 264:    */     {
/* 265:330 */       throw new InvalidResultSetAccessException(se);
/* 266:    */     }
/* 267:    */   }
/* 268:    */   
/* 269:    */   public Object getObject(int columnIndex)
/* 270:    */     throws InvalidResultSetAccessException
/* 271:    */   {
/* 272:    */     try
/* 273:    */     {
/* 274:339 */       return this.resultSet.getObject(columnIndex);
/* 275:    */     }
/* 276:    */     catch (SQLException se)
/* 277:    */     {
/* 278:342 */       throw new InvalidResultSetAccessException(se);
/* 279:    */     }
/* 280:    */   }
/* 281:    */   
/* 282:    */   public Object getObject(String columnLabel, Map<String, Class<?>> map)
/* 283:    */     throws InvalidResultSetAccessException
/* 284:    */   {
/* 285:350 */     return getObject(findColumn(columnLabel), map);
/* 286:    */   }
/* 287:    */   
/* 288:    */   public Object getObject(String columnLabel)
/* 289:    */     throws InvalidResultSetAccessException
/* 290:    */   {
/* 291:357 */     return getObject(findColumn(columnLabel));
/* 292:    */   }
/* 293:    */   
/* 294:    */   public short getShort(int columnIndex)
/* 295:    */     throws InvalidResultSetAccessException
/* 296:    */   {
/* 297:    */     try
/* 298:    */     {
/* 299:365 */       return this.resultSet.getShort(columnIndex);
/* 300:    */     }
/* 301:    */     catch (SQLException se)
/* 302:    */     {
/* 303:368 */       throw new InvalidResultSetAccessException(se);
/* 304:    */     }
/* 305:    */   }
/* 306:    */   
/* 307:    */   public short getShort(String columnLabel)
/* 308:    */     throws InvalidResultSetAccessException
/* 309:    */   {
/* 310:376 */     return getShort(findColumn(columnLabel));
/* 311:    */   }
/* 312:    */   
/* 313:    */   public String getString(int columnIndex)
/* 314:    */     throws InvalidResultSetAccessException
/* 315:    */   {
/* 316:    */     try
/* 317:    */     {
/* 318:384 */       return this.resultSet.getString(columnIndex);
/* 319:    */     }
/* 320:    */     catch (SQLException se)
/* 321:    */     {
/* 322:387 */       throw new InvalidResultSetAccessException(se);
/* 323:    */     }
/* 324:    */   }
/* 325:    */   
/* 326:    */   public String getString(String columnLabel)
/* 327:    */     throws InvalidResultSetAccessException
/* 328:    */   {
/* 329:395 */     return getString(findColumn(columnLabel));
/* 330:    */   }
/* 331:    */   
/* 332:    */   public Time getTime(int columnIndex, Calendar cal)
/* 333:    */     throws InvalidResultSetAccessException
/* 334:    */   {
/* 335:    */     try
/* 336:    */     {
/* 337:403 */       return this.resultSet.getTime(columnIndex, cal);
/* 338:    */     }
/* 339:    */     catch (SQLException se)
/* 340:    */     {
/* 341:406 */       throw new InvalidResultSetAccessException(se);
/* 342:    */     }
/* 343:    */   }
/* 344:    */   
/* 345:    */   public Time getTime(int columnIndex)
/* 346:    */     throws InvalidResultSetAccessException
/* 347:    */   {
/* 348:    */     try
/* 349:    */     {
/* 350:415 */       return this.resultSet.getTime(columnIndex);
/* 351:    */     }
/* 352:    */     catch (SQLException se)
/* 353:    */     {
/* 354:418 */       throw new InvalidResultSetAccessException(se);
/* 355:    */     }
/* 356:    */   }
/* 357:    */   
/* 358:    */   public Time getTime(String columnLabel, Calendar cal)
/* 359:    */     throws InvalidResultSetAccessException
/* 360:    */   {
/* 361:426 */     return getTime(findColumn(columnLabel), cal);
/* 362:    */   }
/* 363:    */   
/* 364:    */   public Time getTime(String columnLabel)
/* 365:    */     throws InvalidResultSetAccessException
/* 366:    */   {
/* 367:433 */     return getTime(findColumn(columnLabel));
/* 368:    */   }
/* 369:    */   
/* 370:    */   public Timestamp getTimestamp(int columnIndex, Calendar cal)
/* 371:    */     throws InvalidResultSetAccessException
/* 372:    */   {
/* 373:    */     try
/* 374:    */     {
/* 375:441 */       return this.resultSet.getTimestamp(columnIndex, cal);
/* 376:    */     }
/* 377:    */     catch (SQLException se)
/* 378:    */     {
/* 379:444 */       throw new InvalidResultSetAccessException(se);
/* 380:    */     }
/* 381:    */   }
/* 382:    */   
/* 383:    */   public Timestamp getTimestamp(int columnIndex)
/* 384:    */     throws InvalidResultSetAccessException
/* 385:    */   {
/* 386:    */     try
/* 387:    */     {
/* 388:453 */       return this.resultSet.getTimestamp(columnIndex);
/* 389:    */     }
/* 390:    */     catch (SQLException se)
/* 391:    */     {
/* 392:456 */       throw new InvalidResultSetAccessException(se);
/* 393:    */     }
/* 394:    */   }
/* 395:    */   
/* 396:    */   public Timestamp getTimestamp(String columnLabel, Calendar cal)
/* 397:    */     throws InvalidResultSetAccessException
/* 398:    */   {
/* 399:464 */     return getTimestamp(findColumn(columnLabel), cal);
/* 400:    */   }
/* 401:    */   
/* 402:    */   public Timestamp getTimestamp(String columnLabel)
/* 403:    */     throws InvalidResultSetAccessException
/* 404:    */   {
/* 405:471 */     return getTimestamp(findColumn(columnLabel));
/* 406:    */   }
/* 407:    */   
/* 408:    */   public boolean absolute(int row)
/* 409:    */     throws InvalidResultSetAccessException
/* 410:    */   {
/* 411:    */     try
/* 412:    */     {
/* 413:482 */       return this.resultSet.absolute(row);
/* 414:    */     }
/* 415:    */     catch (SQLException se)
/* 416:    */     {
/* 417:485 */       throw new InvalidResultSetAccessException(se);
/* 418:    */     }
/* 419:    */   }
/* 420:    */   
/* 421:    */   public void afterLast()
/* 422:    */     throws InvalidResultSetAccessException
/* 423:    */   {
/* 424:    */     try
/* 425:    */     {
/* 426:494 */       this.resultSet.afterLast();
/* 427:    */     }
/* 428:    */     catch (SQLException se)
/* 429:    */     {
/* 430:497 */       throw new InvalidResultSetAccessException(se);
/* 431:    */     }
/* 432:    */   }
/* 433:    */   
/* 434:    */   public void beforeFirst()
/* 435:    */     throws InvalidResultSetAccessException
/* 436:    */   {
/* 437:    */     try
/* 438:    */     {
/* 439:506 */       this.resultSet.beforeFirst();
/* 440:    */     }
/* 441:    */     catch (SQLException se)
/* 442:    */     {
/* 443:509 */       throw new InvalidResultSetAccessException(se);
/* 444:    */     }
/* 445:    */   }
/* 446:    */   
/* 447:    */   public boolean first()
/* 448:    */     throws InvalidResultSetAccessException
/* 449:    */   {
/* 450:    */     try
/* 451:    */     {
/* 452:518 */       return this.resultSet.first();
/* 453:    */     }
/* 454:    */     catch (SQLException se)
/* 455:    */     {
/* 456:521 */       throw new InvalidResultSetAccessException(se);
/* 457:    */     }
/* 458:    */   }
/* 459:    */   
/* 460:    */   public int getRow()
/* 461:    */     throws InvalidResultSetAccessException
/* 462:    */   {
/* 463:    */     try
/* 464:    */     {
/* 465:530 */       return this.resultSet.getRow();
/* 466:    */     }
/* 467:    */     catch (SQLException se)
/* 468:    */     {
/* 469:533 */       throw new InvalidResultSetAccessException(se);
/* 470:    */     }
/* 471:    */   }
/* 472:    */   
/* 473:    */   public boolean isAfterLast()
/* 474:    */     throws InvalidResultSetAccessException
/* 475:    */   {
/* 476:    */     try
/* 477:    */     {
/* 478:542 */       return this.resultSet.isAfterLast();
/* 479:    */     }
/* 480:    */     catch (SQLException se)
/* 481:    */     {
/* 482:545 */       throw new InvalidResultSetAccessException(se);
/* 483:    */     }
/* 484:    */   }
/* 485:    */   
/* 486:    */   public boolean isBeforeFirst()
/* 487:    */     throws InvalidResultSetAccessException
/* 488:    */   {
/* 489:    */     try
/* 490:    */     {
/* 491:554 */       return this.resultSet.isBeforeFirst();
/* 492:    */     }
/* 493:    */     catch (SQLException se)
/* 494:    */     {
/* 495:557 */       throw new InvalidResultSetAccessException(se);
/* 496:    */     }
/* 497:    */   }
/* 498:    */   
/* 499:    */   public boolean isFirst()
/* 500:    */     throws InvalidResultSetAccessException
/* 501:    */   {
/* 502:    */     try
/* 503:    */     {
/* 504:566 */       return this.resultSet.isFirst();
/* 505:    */     }
/* 506:    */     catch (SQLException se)
/* 507:    */     {
/* 508:569 */       throw new InvalidResultSetAccessException(se);
/* 509:    */     }
/* 510:    */   }
/* 511:    */   
/* 512:    */   public boolean isLast()
/* 513:    */     throws InvalidResultSetAccessException
/* 514:    */   {
/* 515:    */     try
/* 516:    */     {
/* 517:578 */       return this.resultSet.isLast();
/* 518:    */     }
/* 519:    */     catch (SQLException se)
/* 520:    */     {
/* 521:581 */       throw new InvalidResultSetAccessException(se);
/* 522:    */     }
/* 523:    */   }
/* 524:    */   
/* 525:    */   public boolean last()
/* 526:    */     throws InvalidResultSetAccessException
/* 527:    */   {
/* 528:    */     try
/* 529:    */     {
/* 530:590 */       return this.resultSet.last();
/* 531:    */     }
/* 532:    */     catch (SQLException se)
/* 533:    */     {
/* 534:593 */       throw new InvalidResultSetAccessException(se);
/* 535:    */     }
/* 536:    */   }
/* 537:    */   
/* 538:    */   public boolean next()
/* 539:    */     throws InvalidResultSetAccessException
/* 540:    */   {
/* 541:    */     try
/* 542:    */     {
/* 543:602 */       return this.resultSet.next();
/* 544:    */     }
/* 545:    */     catch (SQLException se)
/* 546:    */     {
/* 547:605 */       throw new InvalidResultSetAccessException(se);
/* 548:    */     }
/* 549:    */   }
/* 550:    */   
/* 551:    */   public boolean previous()
/* 552:    */     throws InvalidResultSetAccessException
/* 553:    */   {
/* 554:    */     try
/* 555:    */     {
/* 556:614 */       return this.resultSet.previous();
/* 557:    */     }
/* 558:    */     catch (SQLException se)
/* 559:    */     {
/* 560:617 */       throw new InvalidResultSetAccessException(se);
/* 561:    */     }
/* 562:    */   }
/* 563:    */   
/* 564:    */   public boolean relative(int rows)
/* 565:    */     throws InvalidResultSetAccessException
/* 566:    */   {
/* 567:    */     try
/* 568:    */     {
/* 569:626 */       return this.resultSet.relative(rows);
/* 570:    */     }
/* 571:    */     catch (SQLException se)
/* 572:    */     {
/* 573:629 */       throw new InvalidResultSetAccessException(se);
/* 574:    */     }
/* 575:    */   }
/* 576:    */   
/* 577:    */   public boolean wasNull()
/* 578:    */     throws InvalidResultSetAccessException
/* 579:    */   {
/* 580:    */     try
/* 581:    */     {
/* 582:638 */       return this.resultSet.wasNull();
/* 583:    */     }
/* 584:    */     catch (SQLException se)
/* 585:    */     {
/* 586:641 */       throw new InvalidResultSetAccessException(se);
/* 587:    */     }
/* 588:    */   }
/* 589:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet
 * JD-Core Version:    0.7.0.1
 */