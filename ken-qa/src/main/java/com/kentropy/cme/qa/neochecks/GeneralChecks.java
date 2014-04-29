/*   1:    */ package com.kentropy.cme.qa.neochecks;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.qa.Handler1;
/*   4:    */ import com.kentropy.util.DbConnection;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.sql.Connection;
/*   8:    */ import java.sql.ResultSet;
/*   9:    */ import java.sql.ResultSetMetaData;
/*  10:    */ import java.sql.SQLException;
/*  11:    */ import java.sql.Statement;
/*  12:    */ import java.text.ParseException;
/*  13:    */ import java.text.SimpleDateFormat;
/*  14:    */ import java.util.Hashtable;
/*  15:    */ import java.util.Vector;
/*  16:    */ 
/*  17:    */ public class GeneralChecks
/*  18:    */   implements QualityCheck
/*  19:    */ {
/*  20: 27 */   Connection c = null;
/*  21: 29 */   ResultSet fieldClabelSet = null;
/*  22: 30 */   Statement stmt1 = null;
/*  23:    */   
/*  24:    */   public String[] split(String string, String token)
/*  25:    */   {
/*  26: 43 */     String[] str = (String[])null;
/*  27: 44 */     if (token == null)
/*  28:    */     {
/*  29: 45 */       char[] c = string.toCharArray();
/*  30: 46 */       str = new String[c.length];
/*  31: 47 */       for (int i = 0; i < c.length; i++) {
/*  32: 48 */         str[i] = new String(c[i]);
/*  33:    */       }
/*  34:    */     }
/*  35:    */     else
/*  36:    */     {
/*  37: 52 */       str = string.split(token);
/*  38:    */     }
/*  39: 54 */     return str;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String join(String[] string, String token)
/*  43:    */   {
/*  44: 68 */     String str = "";
/*  45: 69 */     if (token != null) {
/*  46: 71 */       for (int i = 0; i < string.length; i++) {
/*  47: 72 */         if (string[i].trim().length() > 0) {
/*  48: 73 */           str = str + (str.trim().length() == 0 ? "" : token) + string[i];
/*  49:    */         }
/*  50:    */       }
/*  51:    */     }
/*  52: 78 */     return str;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public boolean validate(ResultSet rs, ResultSet checkDef, String field, String value, StringBuffer errorMsg, String domain)
/*  56:    */   {
/*  57:    */     try
/*  58:    */     {
/*  59: 86 */       ResultSet fieldRs = getFieldDefn1(field, value, errorMsg, domain);
/*  60:    */       
/*  61: 88 */       String clabel = fieldRs.getString("clabel");
/*  62: 90 */       if (clabel.equals("def"))
/*  63:    */       {
/*  64: 91 */         errorMsg.append("V");
/*  65:    */         
/*  66: 93 */         return true;
/*  67:    */       }
/*  68: 96 */       if (isMissing(value))
/*  69:    */       {
/*  70: 97 */         errorMsg.append("M- Missing Value");
/*  71:    */         
/*  72: 99 */         return false;
/*  73:    */       }
/*  74:102 */       String sql = "select range from clabel where name='" + clabel + 
/*  75:103 */         "' and value='Unknown'";
/*  76:104 */       this.c = DbConnection.getConnection();
/*  77:105 */       Statement stmt = this.c.createStatement();
/*  78:106 */       ResultSet rs1 = stmt.executeQuery(sql);
/*  79:107 */       while (rs1.next()) {
/*  80:109 */         if (value.equals(rs1.getString("range")))
/*  81:    */         {
/*  82:110 */           errorMsg.append("U");
/*  83:111 */           return false;
/*  84:    */         }
/*  85:    */       }
/*  86:114 */       String[] values = (String[])null;
/*  87:115 */       if (fieldRs.getString("type").equals("multiple"))
/*  88:    */       {
/*  89:116 */         values = split(value, value.contains(",") ? "," : null);
/*  90:117 */         new Handler1(null, null).updateField(domain, 
/*  91:118 */           rs.getString("uniqno"), field, join(values, ","));
/*  92:    */       }
/*  93:    */       else
/*  94:    */       {
/*  95:123 */         values = new String[1];
/*  96:124 */         values[0] = value;
/*  97:    */       }
/*  98:126 */       if (this.c == null) {
/*  99:127 */         this.c = DbConnection.getConnection();
/* 100:    */       }
/* 101:129 */       boolean ret = true;
/* 102:130 */       StringBuffer errMsg1 = new StringBuffer();
/* 103:131 */       for (int i = 0; i < values.length; i++)
/* 104:    */       {
/* 105:133 */         if (isOutOfRange(field, values[i], errMsg1, domain))
/* 106:    */         {
/* 107:134 */           System.out.println(errMsg1);
/* 108:135 */           ret = false;
/* 109:    */         }
/* 110:138 */         errorMsg.append("\r\n");
/* 111:    */       }
/* 112:141 */       if (ret)
/* 113:    */       {
/* 114:142 */         errorMsg.setLength(0);
/* 115:143 */         errorMsg.append("V");
/* 116:    */       }
/* 117:    */       else
/* 118:    */       {
/* 119:145 */         errorMsg.setLength(0);
/* 120:146 */         errorMsg.append("I-Invalid\r\n");
/* 121:    */       }
/* 122:151 */       return ret;
/* 123:    */     }
/* 124:    */     catch (Exception e)
/* 125:    */     {
/* 126:153 */       e.printStackTrace();
/* 127:    */     }
/* 128:155 */     return false;
/* 129:    */   }
/* 130:    */   
/* 131:158 */   static Hashtable rangesCache = new Hashtable();
/* 132:159 */   static Hashtable fieldCache = new Hashtable();
/* 133:    */   
/* 134:    */   public String getUnknown(Vector ranges)
/* 135:    */   {
/* 136:163 */     for (int i = 0; i < ranges.size(); i++)
/* 137:    */     {
/* 138:165 */       Hashtable ht = (Hashtable)ranges.get(i);
/* 139:166 */       if (ht.get("value").equals("Unknown")) {
/* 140:167 */         return (String)ht.get("range");
/* 141:    */       }
/* 142:    */     }
/* 143:169 */     return null;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean validate(Hashtable rs, Hashtable flags, String field, String value, StringBuffer errorMsg, String domain)
/* 147:    */   {
/* 148:    */     try
/* 149:    */     {
/* 150:175 */       Hashtable fieldRs = getFieldDefn(field, value, errorMsg, domain);
/* 151:    */       
/* 152:177 */       String clabel = (String)fieldRs.get("clabel");
/* 153:178 */       String ignore = (String)fieldRs.get("ignore1");
/* 154:179 */       String ignoreStatus = ignore.equals("yes") ? "S-Skip" : "M-Missing";
/* 155:181 */       if (clabel.equals("def"))
/* 156:    */       {
/* 157:182 */         errorMsg.append("V");
/* 158:183 */         flags.put(field, "V");
/* 159:    */         
/* 160:185 */         return true;
/* 161:    */       }
/* 162:188 */       if (isMissing(value))
/* 163:    */       {
/* 164:189 */         errorMsg.append(ignoreStatus);
/* 165:190 */         flags.put(field, ignoreStatus);
/* 166:    */         
/* 167:192 */         return false;
/* 168:    */       }
/* 169:194 */       Vector ranges = getRange(domain, field);
/* 170:195 */       Hashtable ht = (Hashtable)ranges.get(0);
/* 171:196 */       if (ht.get("range_type").equals("discrete")) {
/* 172:197 */         rs.put(field, value.trim());
/* 173:    */       }
/* 174:212 */       String unknown = getUnknown(ranges);
/* 175:213 */       if (unknown != null) {
/* 176:215 */         if (value.equals(unknown))
/* 177:    */         {
/* 178:216 */           errorMsg.append("U");
/* 179:217 */           flags.put(field, "U");
/* 180:218 */           return false;
/* 181:    */         }
/* 182:    */       }
/* 183:221 */       String[] values = (String[])null;
/* 184:222 */       if (fieldRs.get("type").equals("multiple"))
/* 185:    */       {
/* 186:223 */         values = split(value, value.contains(",") ? "," : null);
/* 187:224 */         rs.put(field, join(values, ","));
/* 188:    */       }
/* 189:    */       else
/* 190:    */       {
/* 191:226 */         values = new String[1];
/* 192:227 */         values[0] = value;
/* 193:    */       }
/* 194:229 */       boolean ret = true;
/* 195:230 */       StringBuffer errMsg1 = new StringBuffer();
/* 196:231 */       for (int i = 0; i < values.length; i++)
/* 197:    */       {
/* 198:233 */         errorMsg.setLength(0);
/* 199:234 */         if (!checkRange(field, values[i], ranges, "hard"))
/* 200:    */         {
/* 201:236 */           errorMsg.append("I-Invalid\r\n");
/* 202:237 */           flags.put(field, "I-Invalid");
/* 203:    */         }
/* 204:240 */         else if (!checkRange(field, values[i], ranges, "soft"))
/* 205:    */         {
/* 206:242 */           flags.put(field, ht.get("soft_error"));
/* 207:243 */           errorMsg.append(ht.get("soft_error") + "\r\n");
/* 208:244 */           System.out.println(ht.get("soft_error"));
/* 209:    */         }
/* 210:    */         else
/* 211:    */         {
/* 212:247 */           flags.put(field, "V");
/* 213:248 */           errorMsg.append("V\r\n");
/* 214:    */         }
/* 215:255 */         errorMsg.append("\r\n");
/* 216:    */       }
/* 217:270 */       return ret;
/* 218:    */     }
/* 219:    */     catch (Exception e)
/* 220:    */     {
/* 221:272 */       e.printStackTrace();
/* 222:    */     }
/* 223:274 */     return false;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public boolean validate1(Hashtable rs, Hashtable flags, String field, String value, StringBuffer errorMsg, String domain)
/* 227:    */   {
/* 228:    */     try
/* 229:    */     {
/* 230:280 */       ResultSet fieldRs = getFieldDefn1(field, value, errorMsg, domain);
/* 231:    */       
/* 232:282 */       String clabel = fieldRs.getString("clabel");
/* 233:284 */       if (clabel.equals("def"))
/* 234:    */       {
/* 235:285 */         errorMsg.append("V");
/* 236:286 */         flags.put(field, "V");
/* 237:    */         
/* 238:288 */         return true;
/* 239:    */       }
/* 240:291 */       if (isMissing(value))
/* 241:    */       {
/* 242:292 */         errorMsg.append("M- Missing Value");
/* 243:293 */         flags.put(field, "M- Missing Value");
/* 244:    */         
/* 245:295 */         return false;
/* 246:    */       }
/* 247:298 */       String sql = "select `range` from clabel where name='" + clabel + 
/* 248:299 */         "' and value='Unknown'";
/* 249:300 */       this.c = DbConnection.getConnection();
/* 250:301 */       Statement stmt = this.c.createStatement();
/* 251:302 */       ResultSet rs1 = stmt.executeQuery(sql);
/* 252:303 */       while (rs1.next()) {
/* 253:305 */         if (value.equals(rs1.getString("range")))
/* 254:    */         {
/* 255:306 */           errorMsg.append("U");
/* 256:307 */           flags.put(field, "U");
/* 257:308 */           return false;
/* 258:    */         }
/* 259:    */       }
/* 260:311 */       String[] values = (String[])null;
/* 261:312 */       if (fieldRs.getString("type").equals("multiple"))
/* 262:    */       {
/* 263:313 */         values = split(value, value.contains(",") ? "," : null);
/* 264:314 */         rs.put(field, join(values, ","));
/* 265:    */       }
/* 266:    */       else
/* 267:    */       {
/* 268:316 */         values = new String[1];
/* 269:317 */         values[0] = value;
/* 270:    */       }
/* 271:319 */       boolean ret = true;
/* 272:320 */       StringBuffer errMsg1 = new StringBuffer();
/* 273:321 */       for (int i = 0; i < values.length; i++)
/* 274:    */       {
/* 275:323 */         Vector ranges = getRange(domain, field);
/* 276:324 */         errorMsg.setLength(0);
/* 277:325 */         if (!checkRange(field, value, ranges, "hard"))
/* 278:    */         {
/* 279:327 */           errorMsg.append("I-Invalid\r\n");
/* 280:328 */           flags.put(field, "I-Invalid");
/* 281:    */         }
/* 282:331 */         else if (!checkRange(field, value, ranges, "soft"))
/* 283:    */         {
/* 284:333 */           flags.put(field, "W-Warning");
/* 285:334 */           errorMsg.append("W-Warning\r\n");
/* 286:    */         }
/* 287:    */         else
/* 288:    */         {
/* 289:337 */           flags.put(field, "V");
/* 290:338 */           errorMsg.append("V\r\n");
/* 291:    */         }
/* 292:345 */         errorMsg.append("\r\n");
/* 293:    */       }
/* 294:360 */       return ret;
/* 295:    */     }
/* 296:    */     catch (Exception e)
/* 297:    */     {
/* 298:362 */       e.printStackTrace();
/* 299:    */     }
/* 300:364 */     return false;
/* 301:    */   }
/* 302:    */   
/* 303:    */   private String validateChildDeathMonth(ResultSet rs)
/* 304:    */   {
/* 305:    */     try
/* 306:    */     {
/* 307:377 */       String monthStr = rs.getString("death_age_month");
/* 308:378 */       String yearStr = rs.getString("death_age_year");
/* 309:    */       
/* 310:380 */       int month = Integer.parseInt(monthStr.trim() == "" ? "0" : monthStr);
/* 311:381 */       int year = Integer.parseInt(yearStr.trim() == "" ? "0" : yearStr);
/* 312:384 */       if ((month == 0) && (year == 0)) {
/* 313:385 */         return "M - Missing";
/* 314:    */       }
/* 315:386 */       if ((month == 0) && (year > 0)) {
/* 316:387 */         return "N - Not Applicable";
/* 317:    */       }
/* 318:388 */       if ((month > 0) && (year == 0)) {
/* 319:389 */         return "V - Valid";
/* 320:    */       }
/* 321:390 */       if ((month > 12) && (year > 0)) {
/* 322:391 */         return "N - Not Applicable";
/* 323:    */       }
/* 324:    */     }
/* 325:    */     catch (SQLException e)
/* 326:    */     {
/* 327:395 */       e.printStackTrace();
/* 328:    */     }
/* 329:398 */     return null;
/* 330:    */   }
/* 331:    */   
/* 332:    */   private String validateChildDeathYear(ResultSet rs)
/* 333:    */   {
/* 334:    */     try
/* 335:    */     {
/* 336:411 */       String monthStr = rs.getString("death_age_month");
/* 337:412 */       String yearStr = rs.getString("death_age_year");
/* 338:    */       
/* 339:414 */       int month = Integer.parseInt(monthStr.trim() == "" ? "0" : monthStr);
/* 340:415 */       int year = Integer.parseInt(yearStr.trim() == "" ? "0" : yearStr);
/* 341:416 */       if ((month == 0) && (year == 0)) {
/* 342:417 */         return "M - Missing";
/* 343:    */       }
/* 344:418 */       if ((month == 0) && (year > 0)) {
/* 345:419 */         return "V - Valid";
/* 346:    */       }
/* 347:420 */       if ((month > 0) && (year == 0)) {
/* 348:421 */         return "N - Not Applicable";
/* 349:    */       }
/* 350:422 */       if ((month > 12) && (year > 0)) {
/* 351:423 */         return "V - Valid";
/* 352:    */       }
/* 353:    */     }
/* 354:    */     catch (SQLException e)
/* 355:    */     {
/* 356:427 */       e.printStackTrace();
/* 357:    */     }
/* 358:430 */     return null;
/* 359:    */   }
/* 360:    */   
/* 361:    */   public boolean isNotValid(String value)
/* 362:    */     throws Exception
/* 363:    */   {
/* 364:    */     do
/* 365:    */     {
/* 366:447 */       if ((value.equals(this.fieldClabelSet.getString("range"))) && 
/* 367:448 */         (this.fieldClabelSet.getString("range_type").equals(
/* 368:449 */         "discrete")))
/* 369:    */       {
/* 370:450 */         this.stmt1.close();
/* 371:    */         
/* 372:452 */         return false;
/* 373:    */       }
/* 374:445 */     } while (
/* 375:    */     
/* 376:    */ 
/* 377:    */ 
/* 378:    */ 
/* 379:    */ 
/* 380:    */ 
/* 381:    */ 
/* 382:    */ 
/* 383:    */ 
/* 384:455 */       this.fieldClabelSet.next());
/* 385:457 */     this.stmt1.close();
/* 386:    */     
/* 387:459 */     return true;
/* 388:    */   }
/* 389:    */   
/* 390:    */   public boolean isMissing(String value)
/* 391:    */   {
/* 392:471 */     if ((value == null) || (value.trim().equals(""))) {
/* 393:473 */       return true;
/* 394:    */     }
/* 395:475 */     return false;
/* 396:    */   }
/* 397:    */   
/* 398:    */   public Hashtable getFieldDefn(String field, String value, StringBuffer errorMsg, String domain)
/* 399:    */     throws Exception
/* 400:    */   {
/* 401:497 */     String key = domain + "-" + field;
/* 402:498 */     if (fieldCache.get(key) != null) {
/* 403:499 */       return (Hashtable)fieldCache.get(key);
/* 404:    */     }
/* 405:500 */     if (this.c == null) {
/* 406:501 */       this.c = DbConnection.getConnection();
/* 407:    */     }
/* 408:503 */     boolean isDiscrete = false;
/* 409:504 */     String range = "SELECT * FROM data_dict_has_domain where field='" + 
/* 410:505 */       field + "' and domain='" + domain + "'";
/* 411:506 */     Statement smt = this.c.createStatement();
/* 412:507 */     ResultSet rsRange = smt.executeQuery(range);
/* 413:508 */     String error = null;
/* 414:509 */     Hashtable ht = new Hashtable();
/* 415:510 */     if (rsRange.next())
/* 416:    */     {
/* 417:512 */       for (int i = 0; i < rsRange.getMetaData().getColumnCount(); i++)
/* 418:    */       {
/* 419:514 */         String fld = rsRange.getMetaData().getColumnName(i + 1);
/* 420:515 */         ht.put(fld, rsRange.getString(fld));
/* 421:    */       }
/* 422:517 */       fieldCache.put(key, ht);
/* 423:518 */       return ht;
/* 424:    */     }
/* 425:521 */     return null;
/* 426:    */   }
/* 427:    */   
/* 428:    */   public ResultSet getFieldDefn1(String field, String value, StringBuffer errorMsg, String domain)
/* 429:    */     throws Exception
/* 430:    */   {
/* 431:528 */     if (this.c == null) {
/* 432:529 */       this.c = DbConnection.getConnection();
/* 433:    */     }
/* 434:531 */     boolean isDiscrete = false;
/* 435:532 */     String range = "SELECT * FROM data_dict_has_domain where field='" + 
/* 436:533 */       field + "' and domain='" + domain + "'";
/* 437:534 */     Statement smt = this.c.createStatement();
/* 438:535 */     ResultSet rsRange = smt.executeQuery(range);
/* 439:536 */     String error = null;
/* 440:537 */     if (rsRange.next()) {
/* 441:538 */       return rsRange;
/* 442:    */     }
/* 443:540 */     return null;
/* 444:    */   }
/* 445:    */   
/* 446:    */   public boolean validateString(String range, String value, StringBuffer errorMsg)
/* 447:    */   {
/* 448:545 */     if (range == null) {
/* 449:546 */       return true;
/* 450:    */     }
/* 451:548 */     String[] bounds = range.split("-");
/* 452:549 */     int min = Integer.parseInt(bounds[0]);
/* 453:550 */     int max = bounds.length > 1 ? Integer.parseInt(bounds[1]) : min;
/* 454:551 */     if ((value.length() >= min) && (value.length() <= max)) {
/* 455:553 */       return true;
/* 456:    */     }
/* 457:556 */     return false;
/* 458:    */   }
/* 459:    */   
/* 460:    */   public boolean validateRegex(String regex, String value, StringBuffer errorMsg)
/* 461:    */   {
/* 462:561 */     if (regex == null) {
/* 463:562 */       return true;
/* 464:    */     }
/* 465:564 */     if (!value.matches(regex)) {
/* 466:567 */       return false;
/* 467:    */     }
/* 468:569 */     return true;
/* 469:    */   }
/* 470:    */   
/* 471:    */   public boolean validateDate(String dateFormat, String value, StringBuffer errorMsg)
/* 472:    */   {
/* 473:574 */     if (dateFormat == null) {
/* 474:575 */       return true;
/* 475:    */     }
/* 476:577 */     String[] date = dateFormat.split(",");
/* 477:578 */     for (int i = 0; i < date.length; i++)
/* 478:    */     {
/* 479:580 */       SimpleDateFormat sdf = new SimpleDateFormat(date[i]);
/* 480:581 */       sdf.setLenient(false);
/* 481:    */       try
/* 482:    */       {
/* 483:583 */         sdf.parse(value);
/* 484:    */         
/* 485:585 */         return true;
/* 486:    */       }
/* 487:    */       catch (ParseException e)
/* 488:    */       {
/* 489:588 */         if (i == date.length - 1) {
/* 490:589 */           return false;
/* 491:    */         }
/* 492:    */       }
/* 493:    */     }
/* 494:594 */     return false;
/* 495:    */   }
/* 496:    */   
/* 497:    */   public boolean validateInt(String range, String value, StringBuffer errorMsg)
/* 498:    */   {
/* 499:598 */     System.out.println("Validating Int");
/* 500:599 */     System.out.println("Range: " + range);
/* 501:600 */     System.out.println("Value: " + value);
/* 502:602 */     if (range == null) {
/* 503:603 */       return true;
/* 504:    */     }
/* 505:    */     try
/* 506:    */     {
/* 507:607 */       String[] rag = range.split("-");
/* 508:608 */       int min = Integer.parseInt(rag[0]);
/* 509:    */       
/* 510:610 */       int max = rag.length > 1 ? Integer.parseInt(rag[1]) : min;
/* 511:611 */       int h = Integer.parseInt(value);
/* 512:612 */       if ((h >= min) && (h <= max)) {
/* 513:613 */         return true;
/* 514:    */       }
/* 515:615 */       return false;
/* 516:    */     }
/* 517:    */     catch (NumberFormatException ne) {}
/* 518:618 */     return false;
/* 519:    */   }
/* 520:    */   
/* 521:    */   public Vector getRange(String domain, String field)
/* 522:    */     throws SQLException
/* 523:    */   {
/* 524:640 */     if (rangesCache.get(domain + "-" + field) != null) {
/* 525:641 */       return (Vector)rangesCache.get(domain + "-" + field);
/* 526:    */     }
/* 527:642 */     if (this.c == null) {
/* 528:643 */       this.c = DbConnection.getConnection();
/* 529:    */     }
/* 530:645 */     String rangeSQL = "SELECT b.* FROM data_dict_has_domain a LEFT JOIN clabel b ON a.clabel=b.NAME WHERE FIELD='" + 
/* 531:646 */       field + "' and domain='" + domain + "'";
/* 532:647 */     Statement smt = this.c.createStatement();
/* 533:648 */     ResultSet rsRange = smt.executeQuery(rangeSQL);
/* 534:649 */     System.out.println(rangeSQL);
/* 535:650 */     Vector v = new Vector();
/* 536:651 */     while (rsRange.next())
/* 537:    */     {
/* 538:652 */       Hashtable ht = new Hashtable();
/* 539:653 */       ht.put("range_type", rsRange.getString("range_type"));
/* 540:654 */       if (rsRange.getString("range") != null) {
/* 541:655 */         ht.put("range", rsRange.getString("range"));
/* 542:    */       }
/* 543:657 */       if (rsRange.getString("soft_range") != null)
/* 544:    */       {
/* 545:658 */         ht.put("soft_range", rsRange.getString("soft_range"));
/* 546:659 */         ht.put("soft_error", rsRange.getString("soft_error"));
/* 547:    */       }
/* 548:661 */       if (rsRange.getString("value") != null) {
/* 549:662 */         ht.put("value", rsRange.getString("value"));
/* 550:    */       }
/* 551:664 */       v.add(ht);
/* 552:    */     }
/* 553:666 */     rangesCache.put(domain + "-" + field, v);
/* 554:    */     
/* 555:668 */     return v;
/* 556:    */   }
/* 557:    */   
/* 558:    */   public boolean checkRange(String field, String value, Vector ranges, String type)
/* 559:    */     throws Exception
/* 560:    */   {
/* 561:679 */     value = value.trim();
/* 562:    */     
/* 563:681 */     boolean isDiscrete = false;
/* 564:683 */     for (int i = 0; i < ranges.size(); i++)
/* 565:    */     {
/* 566:685 */       Hashtable ht = (Hashtable)ranges.get(i);
/* 567:686 */       String range = (String)(type.equals("hard") ? ht.get("range") : ht.get("soft_range"));
/* 568:687 */       if (range != null)
/* 569:    */       {
/* 570:689 */         String rangeType = ht.get("range_type").toString();
/* 571:690 */         if (ht.get("range_type").equals("string")) {
/* 572:691 */           return validateString(range, value, new StringBuffer());
/* 573:    */         }
/* 574:694 */         if (rangeType.equals("regex")) {
/* 575:695 */           return validateRegex(range, value, new StringBuffer());
/* 576:    */         }
/* 577:699 */         if (rangeType.equals("date")) {
/* 578:700 */           return validateDate(range, value, new StringBuffer());
/* 579:    */         }
/* 580:705 */         if ((rangeType.equals("int")) || (rangeType.equals("discrete")))
/* 581:    */         {
/* 582:706 */           if (rangeType.equals("discrete")) {
/* 583:707 */             isDiscrete = true;
/* 584:    */           }
/* 585:709 */           if ((!validateInt(range, value, new StringBuffer())) && (rangeType.equals("hard"))) {
/* 586:711 */             return false;
/* 587:    */           }
/* 588:    */         }
/* 589:    */         else
/* 590:    */         {
/* 591:718 */           if (field.equals("hys")) {
/* 592:719 */             System.out.println("HYS in hardCheck true");
/* 593:    */           }
/* 594:723 */           return false;
/* 595:    */         }
/* 596:    */       }
/* 597:    */     }
/* 598:731 */     return true;
/* 599:    */   }
/* 600:    */   
/* 601:    */   public boolean isOutOfRange(String field, String value, StringBuffer errorMsg, String domain)
/* 602:    */     throws Exception
/* 603:    */   {
/* 604:737 */     value = value.trim();
/* 605:738 */     if (this.c == null) {
/* 606:739 */       this.c = DbConnection.getConnection();
/* 607:    */     }
/* 608:741 */     String rangeSQL = "SELECT b.* FROM data_dict_has_domain a LEFT JOIN clabel b ON a.clabel=b.NAME WHERE FIELD='" + 
/* 609:742 */       field + "' and domain='" + domain + "'";
/* 610:743 */     Statement smt = this.c.createStatement();
/* 611:744 */     ResultSet rsRange = smt.executeQuery(rangeSQL);
/* 612:745 */     boolean isDiscrete = false;
/* 613:    */     
/* 614:    */ 
/* 615:    */ 
/* 616:749 */     String error = null;
/* 617:750 */     while (rsRange.next())
/* 618:    */     {
/* 619:752 */       String check = rsRange.getString("name");
/* 620:753 */       if (!rsRange.getString("range_type").equals("string")) {
/* 621:759 */         if (!rsRange.getString("range_type").equals("regex")) {
/* 622:765 */           if (!rsRange.getString("range_type").equals("date")) {
/* 623:773 */             if ((rsRange.getString("range_type").equals("int")) || 
/* 624:774 */               (rsRange.getString("range_type").equals(
/* 625:775 */               "discrete")))
/* 626:    */             {
/* 627:776 */               if (rsRange.getString("range_type").equals(
/* 628:777 */                 "discrete")) {
/* 629:778 */                 isDiscrete = true;
/* 630:    */               }
/* 631:    */             }
/* 632:    */             else
/* 633:    */             {
/* 634:791 */               if (field.equals("hys"))
/* 635:    */               {
/* 636:792 */                 System.out.println("HYS in hardCheck true");
/* 637:793 */                 System.in.read();
/* 638:    */               }
/* 639:795 */               errorMsg.append("V");
/* 640:796 */               return false;
/* 641:    */             }
/* 642:    */           }
/* 643:    */         }
/* 644:    */       }
/* 645:    */     }
/* 646:801 */     errorMsg.append(isDiscrete ? "I-Invalid Choice" : "O-Out of Range");
/* 647:    */     
/* 648:803 */     smt.close();
/* 649:804 */     return true;
/* 650:    */   }
/* 651:    */   
/* 652:    */   public static void main(String[] args)
/* 653:    */   {
/* 654:812 */     GeneralChecks gen = new GeneralChecks();
/* 655:813 */     StringBuffer strBuf = new StringBuffer();
/* 656:    */     
/* 657:815 */     System.out.println("Error: " + strBuf.toString());
/* 658:    */   }
/* 659:    */   
/* 660:    */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 661:    */   {
/* 662:832 */     return false;
/* 663:    */   }
/* 664:    */   
/* 665:    */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 666:    */   {
/* 667:838 */     return false;
/* 668:    */   }
/* 669:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.GeneralChecks
 * JD-Core Version:    0.7.0.1
 */