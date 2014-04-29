/*   1:    */ package com.kentropy.cme.qa.neochecks;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbConnection;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.ResultSetMetaData;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.sql.Statement;
/*  10:    */ import java.util.Hashtable;
/*  11:    */ import java.util.Vector;
/*  12:    */ import net.xoetrope.xui.data.XModel;
/*  13:    */ 
/*  14:    */ public class FlowChecks
/*  15:    */   implements QualityCheck
/*  16:    */ {
/*  17: 26 */   Connection c = null;
/*  18: 28 */   public static Hashtable flowCache = new Hashtable();
/*  19: 29 */   public Vector errors = new Vector();
/*  20: 30 */   public Vector fields = new Vector();
/*  21:    */   
/*  22:    */   public boolean validate(ResultSet rs, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/*  23:    */   {
/*  24: 33 */     return true;
/*  25:    */   }
/*  26:    */   
/*  27: 35 */   static Hashtable fieldCache = new Hashtable();
/*  28:    */   
/*  29:    */   public synchronized Vector getFlowDefn(String domain)
/*  30:    */     throws Exception
/*  31:    */   {
/*  32: 38 */     String key = domain;
/*  33: 39 */     if (fieldCache.get(key) != null) {
/*  34: 40 */       return (Vector)fieldCache.get(key);
/*  35:    */     }
/*  36: 41 */     if (this.c == null) {
/*  37: 42 */       this.c = DbConnection.getConnection();
/*  38:    */     }
/*  39: 44 */     boolean isDiscrete = false;
/*  40:    */     
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44:    */ 
/*  45: 50 */     String range = "SELECT a.q_no,a.FIELD FROM data_dict_has_domain a LEFT JOIN section b ON a.FIELD=b.field_name AND a.domain=b.domain WHERE a.domain='" + domain + "' ORDER BY b.sequence";
/*  46: 51 */     Statement smt = this.c.createStatement();
/*  47: 52 */     ResultSet rsRange = smt.executeQuery(range);
/*  48: 53 */     String error = null;
/*  49: 54 */     Vector v = new Vector();
/*  50: 56 */     while (rsRange.next())
/*  51:    */     {
/*  52: 57 */       Hashtable ht = new Hashtable();
/*  53: 58 */       for (int i = 0; i < rsRange.getMetaData().getColumnCount(); i++)
/*  54:    */       {
/*  55: 60 */         String fld = rsRange.getMetaData().getColumnName(i + 1);
/*  56: 61 */         ht.put(fld, rsRange.getString(fld));
/*  57:    */       }
/*  58: 63 */       v.add(ht);
/*  59:    */     }
/*  60: 66 */     fieldCache.put(key, v);
/*  61:    */     
/*  62: 68 */     return v;
/*  63:    */   }
/*  64:    */   
/*  65: 71 */   public StringBuffer path = new StringBuffer();
/*  66: 72 */   public Vector path1 = new Vector();
/*  67:    */   
/*  68:    */   public boolean validate(Hashtable values, Hashtable flags, String name, String value, StringBuffer errorMsg, String domain)
/*  69:    */   {
/*  70: 76 */     boolean isValid = true;
/*  71:    */     try
/*  72:    */     {
/*  73: 79 */       this.c = DbConnection.getConnection();
/*  74:    */       
/*  75:    */ 
/*  76: 82 */       Vector flowDef = getFlowDefn(domain);
/*  77: 83 */       String nextQ = null;
/*  78: 85 */       for (int i = 0; i < flowDef.size(); i++)
/*  79:    */       {
/*  80: 88 */         Hashtable questionRs = (Hashtable)flowDef.get(i);
/*  81: 89 */         String qno = (String)questionRs.get("q_no");
/*  82:    */         
/*  83: 91 */         String field = (String)questionRs.get("field");
/*  84: 92 */         this.fields.add(field);
/*  85: 93 */         System.out.println("FIELD:" + field);
/*  86: 94 */         String value1 = "";
/*  87:    */         try
/*  88:    */         {
/*  89: 96 */           value1 = values.get(field).toString();
/*  90:    */         }
/*  91:    */         catch (NullPointerException localNullPointerException) {}
/*  92:100 */         StringBuffer err1 = new StringBuffer();
/*  93:101 */         String tempNextQ = nextQ;
/*  94:102 */         boolean skip = false;
/*  95:103 */         String condition = "";
/*  96:105 */         if ((nextQ == null) || (nextQ.equals(qno)))
/*  97:    */         {
/*  98:107 */           String[] res = getNextQ(domain, qno, value1, this.path1, "before");
/*  99:109 */           if (res != null)
/* 100:    */           {
/* 101:112 */             tempNextQ = res[0];
/* 102:113 */             condition = res[1];
/* 103:114 */             skip = true;
/* 104:    */           }
/* 105:    */         }
/* 106:123 */         if (((nextQ == null) || (nextQ.equals(qno))) && (!skip))
/* 107:    */         {
/* 108:125 */           GeneralChecks gc = new GeneralChecks();
/* 109:126 */           gc.validate(values, flags, field, value1, err1, domain);
/* 110:    */           
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:132 */           this.errors.add(err1.toString());
/* 116:133 */           if (!err1.toString().startsWith("V")) {
/* 117:134 */             isValid = false;
/* 118:    */           }
/* 119:139 */           nextQ = null;
/* 120:140 */           this.path = new StringBuffer();
/* 121:    */         }
/* 122:    */         else
/* 123:    */         {
/* 124:147 */           GeneralChecks gc = new GeneralChecks();
/* 125:148 */           gc.validate(values, flags, field, value1, err1, domain);
/* 126:151 */           if ((this.disc_flags != null) && (!this.disc_flags.equals("")) && 
/* 127:152 */             (err1.toString().startsWith(this.disc_flags)))
/* 128:    */           {
/* 129:154 */             this.errors.add("D-" + this.path1 + " " + field + "  " + 
/* 130:155 */               err1.toString().substring(0, 1) + " " + nextQ);
/* 131:156 */             flags.put(field, "D-" + this.path1 + " " + field + "  " + 
/* 132:157 */               err1.toString().substring(0, 1) + " " + nextQ);
/* 133:    */           }
/* 134:159 */           else if ((err1.toString().startsWith("M")) || 
/* 135:160 */             (err1.toString().startsWith("I")) || 
/* 136:161 */             (err1.toString().startsWith("O")) || 
/* 137:162 */             (err1.toString().startsWith("W")))
/* 138:    */           {
/* 139:164 */             this.errors.add("N");
/* 140:165 */             flags.put(field, "N");
/* 141:    */           }
/* 142:    */           else
/* 143:    */           {
/* 144:179 */             this.errors.add(err1.toString());
/* 145:180 */             isValid = false;
/* 146:    */           }
/* 147:    */         }
/* 148:190 */         if (skip)
/* 149:    */         {
/* 150:192 */           nextQ = tempNextQ;
/* 151:    */         }
/* 152:    */         else
/* 153:    */         {
/* 154:196 */           String[] res = getNextQ(domain, qno, value1, this.path1, "after");
/* 155:198 */           if (res != null)
/* 156:    */           {
/* 157:201 */             nextQ = res[0];
/* 158:202 */             condition = res[1];
/* 159:    */             
/* 160:    */ 
/* 161:205 */             this.path.append(field + " " + condition + " ");
/* 162:206 */             this.path1.add(qno + " " + condition);
/* 163:    */           }
/* 164:    */         }
/* 165:    */       }
/* 166:    */     }
/* 167:    */     catch (Exception e)
/* 168:    */     {
/* 169:218 */       e.printStackTrace();
/* 170:    */     }
/* 171:220 */     return isValid;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public boolean validate1(Hashtable values, Hashtable flags, String name, String value, StringBuffer errorMsg, String domain)
/* 175:    */   {
/* 176:225 */     boolean isValid = true;
/* 177:    */     try
/* 178:    */     {
/* 179:228 */       this.c = DbConnection.getConnection();
/* 180:    */       
/* 181:230 */       String sql = "SELECT q_no,field FROM data_dict_has_domain WHERE domain='" + 
/* 182:231 */         domain + "' ORDER BY CAST(q_no AS UNSIGNED),q_no ASC";
/* 183:232 */       Statement s = this.c.createStatement();
/* 184:233 */       ResultSet questionRs = s.executeQuery(sql);
/* 185:234 */       String nextQ = null;
/* 186:235 */       StringBuffer path = new StringBuffer();
/* 187:236 */       Vector path1 = new Vector();
/* 188:237 */       while (questionRs.next())
/* 189:    */       {
/* 190:240 */         String qno = questionRs.getString("q_no");
/* 191:    */         
/* 192:242 */         String field = questionRs.getString("field");
/* 193:243 */         this.fields.add(field);
/* 194:244 */         String value1 = (String)values.get(field);
/* 195:245 */         StringBuffer err1 = new StringBuffer();
/* 196:246 */         String tempNextQ = nextQ;
/* 197:247 */         boolean skip = false;
/* 198:248 */         String condition = "";
/* 199:250 */         if ((nextQ == null) || (nextQ.equals(qno)))
/* 200:    */         {
/* 201:252 */           String[] res = getNextQ(domain, qno, value1, path1, "before");
/* 202:254 */           if (res != null)
/* 203:    */           {
/* 204:257 */             tempNextQ = res[0];
/* 205:258 */             condition = res[1];
/* 206:259 */             skip = true;
/* 207:    */           }
/* 208:    */         }
/* 209:268 */         if (((nextQ == null) || (nextQ.equals(qno))) && (!skip))
/* 210:    */         {
/* 211:270 */           GeneralChecks gc = new GeneralChecks();
/* 212:271 */           gc.validate(values, flags, field, value1, err1, domain);
/* 213:272 */           if (err1.toString().startsWith("V"))
/* 214:    */           {
/* 215:273 */             CrossChecks c = new CrossChecks();
/* 216:274 */             c.validate(values, flags, field, value1, err1, domain);
/* 217:    */           }
/* 218:277 */           this.errors.add(err1.toString());
/* 219:278 */           if (!err1.toString().startsWith("V")) {
/* 220:279 */             isValid = false;
/* 221:    */           }
/* 222:284 */           nextQ = null;
/* 223:285 */           path = new StringBuffer();
/* 224:    */         }
/* 225:    */         else
/* 226:    */         {
/* 227:292 */           GeneralChecks gc = new GeneralChecks();
/* 228:293 */           gc.validate(values, flags, field, value1, err1, domain);
/* 229:294 */           if (err1.toString().startsWith("V"))
/* 230:    */           {
/* 231:295 */             CrossChecks c = new CrossChecks();
/* 232:296 */             c.validate(values, flags, field, value1, err1, domain);
/* 233:    */           }
/* 234:299 */           if ((this.disc_flags != null) && (!this.disc_flags.equals("")) && 
/* 235:300 */             (err1.toString().startsWith(this.disc_flags)))
/* 236:    */           {
/* 237:302 */             this.errors.add("D-" + path1 + " " + field + "  " + 
/* 238:303 */               err1.toString().substring(0, 1) + " " + nextQ);
/* 239:304 */             flags.put(field, "D-" + path1 + " " + field + "  " + 
/* 240:305 */               err1.toString().substring(0, 1) + " " + nextQ);
/* 241:    */           }
/* 242:307 */           else if ((err1.toString().startsWith("M")) || 
/* 243:308 */             (err1.toString().startsWith("I")) || 
/* 244:309 */             (err1.toString().startsWith("O")) || 
/* 245:310 */             (err1.toString().startsWith("W")))
/* 246:    */           {
/* 247:312 */             this.errors.add("N");
/* 248:313 */             flags.put(field, "N");
/* 249:    */           }
/* 250:    */           else
/* 251:    */           {
/* 252:327 */             this.errors.add(err1.toString());
/* 253:328 */             isValid = false;
/* 254:    */           }
/* 255:    */         }
/* 256:338 */         if (skip)
/* 257:    */         {
/* 258:340 */           nextQ = tempNextQ;
/* 259:    */         }
/* 260:    */         else
/* 261:    */         {
/* 262:344 */           String[] res = getNextQ(domain, qno, value1, path1, "after");
/* 263:346 */           if (res != null)
/* 264:    */           {
/* 265:349 */             nextQ = res[0];
/* 266:350 */             condition = res[1];
/* 267:    */             
/* 268:    */ 
/* 269:353 */             path.append(field + " " + condition + " ");
/* 270:354 */             path1.add(qno + " " + condition);
/* 271:    */           }
/* 272:    */         }
/* 273:    */       }
/* 274:    */     }
/* 275:    */     catch (Exception e)
/* 276:    */     {
/* 277:366 */       e.printStackTrace();
/* 278:    */     }
/* 279:368 */     return isValid;
/* 280:    */   }
/* 281:    */   
/* 282:    */   private boolean skipCurrent(String domain, String qno, String value, Vector path)
/* 283:    */     throws SQLException
/* 284:    */   {
/* 285:372 */     String sql = "SELECT next_q_no,disc_flags FROM skip_conditions WHERE q_no='" + 
/* 286:373 */       qno + 
/* 287:374 */       "'and domain='" + 
/* 288:375 */       domain + 
/* 289:376 */       "' and `condition`='" + 
/* 290:377 */       value + "' and (path='" + path + "' or path is null) and when1='before'";
/* 291:378 */     ResultSet rs = this.c.createStatement().executeQuery(sql);
/* 292:379 */     if (rs.next()) {
/* 293:380 */       return true;
/* 294:    */     }
/* 295:382 */     return false;
/* 296:    */   }
/* 297:    */   
/* 298:387 */   public String disc_flags = "V";
/* 299:    */   
/* 300:    */   public synchronized Vector getSkip(String domain, String qno, String when, String path)
/* 301:    */     throws SQLException
/* 302:    */   {
/* 303:404 */     String key = domain + "-" + qno + "-" + when + "-" + path;
/* 304:405 */     if (flowCache.get(key) != null) {
/* 305:406 */       return (Vector)flowCache.get(key);
/* 306:    */     }
/* 307:407 */     String sql = "SELECT next_q_no,disc_flags,when1,`condition` FROM skip_conditions WHERE q_no='" + 
/* 308:408 */       qno + 
/* 309:409 */       "'and domain='" + 
/* 310:410 */       domain + "' and when1='" + when + "' and ('" + path + "' LIKE CONCAT('%',path,'%') or path is null) ";
/* 311:411 */     Statement s = this.c.createStatement();
/* 312:412 */     ResultSet rs2 = s.executeQuery(sql);
/* 313:413 */     String nextQ = null;
/* 314:414 */     Vector v = new Vector();
/* 315:415 */     String[] flds = { "next_q_no", "disc_flags", "when1", "condition" };
/* 316:416 */     System.out.println(sql);
/* 317:417 */     while (rs2.next())
/* 318:    */     {
/* 319:418 */       Hashtable ht = new Hashtable();
/* 320:419 */       for (int i = 0; i < flds.length; i++) {
/* 321:421 */         ht.put(flds[i], rs2.getString(flds[i]));
/* 322:    */       }
/* 323:423 */       v.add(ht);
/* 324:    */     }
/* 325:425 */     flowCache.put(key, v);
/* 326:426 */     return v;
/* 327:    */   }
/* 328:    */   
/* 329:    */   private String[] getNextQ(String domain, String qno, String value, Vector path, String when)
/* 330:    */   {
/* 331:    */     try
/* 332:    */     {
/* 333:432 */       String path1 = "";
/* 334:433 */       for (int i = 0; i < path.size(); i++) {
/* 335:434 */         path1 = 
/* 336:435 */           path1 + (i == 0 ? "" : ",") + path.get(i).toString().trim();
/* 337:    */       }
/* 338:437 */       String[] res = (String[])null;
/* 339:438 */       path1 = path1;
/* 340:    */       
/* 341:    */ 
/* 342:    */ 
/* 343:    */ 
/* 344:    */ 
/* 345:    */ 
/* 346:445 */       Vector flowChecks = getSkip(domain, qno, when, path1);
/* 347:446 */       String nextQ = null;
/* 348:447 */       for (int i = 0; i < flowChecks.size(); i++)
/* 349:    */       {
/* 350:448 */         Hashtable ht = (Hashtable)flowChecks.get(i);
/* 351:449 */         this.disc_flags = ((String)ht.get("disc_flags"));
/* 352:450 */         if (when.equals("before"))
/* 353:    */         {
/* 354:452 */           res = new String[2];
/* 355:453 */           res[0] = ((String)ht.get("next_q_no"));
/* 356:454 */           res[1] = value;
/* 357:455 */           return res;
/* 358:    */         }
/* 359:457 */         String condition = (String)ht.get("condition");
/* 360:464 */         if (condition.indexOf("-") != -1)
/* 361:    */         {
/* 362:466 */           String[] test = condition.split("-");
/* 363:467 */           System.out.println(" Comparision " + value + " " + " " + test[0] + " " + value.compareTo(test[1]) + " " + test[1]);
/* 364:    */           try
/* 365:    */           {
/* 366:470 */             if ((Integer.parseInt(value) < Integer.parseInt(test[0])) || (Integer.parseInt(value) > Integer.parseInt(test[1]))) {
/* 367:    */               continue;
/* 368:    */             }
/* 369:472 */             res = new String[2];
/* 370:473 */             res[0] = ((String)ht.get("next_q_no"));
/* 371:474 */             res[1] = condition;
/* 372:    */             
/* 373:    */ 
/* 374:477 */             return res;
/* 375:    */           }
/* 376:    */           catch (Exception e)
/* 377:    */           {
/* 378:480 */             e.printStackTrace();
/* 379:    */           }
/* 380:    */         }
/* 381:487 */         else if (condition.equals(value))
/* 382:    */         {
/* 383:489 */           res = new String[2];
/* 384:490 */           res[0] = ((String)ht.get("next_q_no"));
/* 385:491 */           res[1] = condition;
/* 386:    */           
/* 387:493 */           return res;
/* 388:    */         }
/* 389:    */       }
/* 390:    */     }
/* 391:    */     catch (Exception e)
/* 392:    */     {
/* 393:507 */       e.printStackTrace();
/* 394:    */     }
/* 395:509 */     return null;
/* 396:    */   }
/* 397:    */   
/* 398:    */   private String[] getNextQ1(String domain, String qno, String value, Vector path, String when)
/* 399:    */   {
/* 400:    */     try
/* 401:    */     {
/* 402:515 */       String path1 = "";
/* 403:516 */       for (int i = 0; i < path.size(); i++) {
/* 404:517 */         path1 = 
/* 405:518 */           path1 + (i == 0 ? "" : ",") + path.get(i).toString().trim();
/* 406:    */       }
/* 407:520 */       String[] res = (String[])null;
/* 408:521 */       path1 = path1;
/* 409:    */       
/* 410:    */ 
/* 411:    */ 
/* 412:    */ 
/* 413:    */ 
/* 414:    */ 
/* 415:528 */       String sql = "SELECT next_q_no,disc_flags,when1,`condition` FROM skip_conditions WHERE q_no='" + 
/* 416:529 */         qno + 
/* 417:530 */         "'and domain='" + 
/* 418:531 */         domain + "' and when1='" + when + "' and ('" + path + "' LIKE CONCAT('%',path,'%') or path is null) ";
/* 419:532 */       System.out.println(sql);
/* 420:533 */       Statement s = this.c.createStatement();
/* 421:534 */       ResultSet rs2 = s.executeQuery(sql);
/* 422:535 */       String nextQ = null;
/* 423:536 */       while (rs2.next())
/* 424:    */       {
/* 425:537 */         this.disc_flags = rs2.getString("disc_flags");
/* 426:538 */         if (when.equals("before"))
/* 427:    */         {
/* 428:540 */           res = new String[2];
/* 429:541 */           res[0] = rs2.getString("next_q_no");
/* 430:542 */           res[1] = value;
/* 431:543 */           return res;
/* 432:    */         }
/* 433:545 */         String condition = rs2.getString("condition");
/* 434:546 */         if (qno.equals("06")) {
/* 435:548 */           System.out.println(" Condition " + condition + " " + condition.indexOf("-"));
/* 436:    */         }
/* 437:552 */         if (condition.indexOf("-") != -1)
/* 438:    */         {
/* 439:554 */           String[] test = condition.split("-");
/* 440:555 */           System.out.println(" Comparision " + value + " " + " " + test[0] + " " + value.compareTo(test[1]) + " " + test[1]);
/* 441:558 */           if ((Integer.parseInt(value) >= Integer.parseInt(test[0])) && (Integer.parseInt(value) <= Integer.parseInt(test[1])))
/* 442:    */           {
/* 443:560 */             res = new String[2];
/* 444:561 */             res[0] = rs2.getString("next_q_no");
/* 445:562 */             res[1] = condition;
/* 446:    */             
/* 447:    */ 
/* 448:565 */             return res;
/* 449:    */           }
/* 450:    */         }
/* 451:571 */         else if (condition.equals(value))
/* 452:    */         {
/* 453:573 */           res = new String[2];
/* 454:574 */           res[0] = rs2.getString("next_q_no");
/* 455:575 */           res[1] = condition;
/* 456:    */           
/* 457:577 */           return res;
/* 458:    */         }
/* 459:    */       }
/* 460:    */     }
/* 461:    */     catch (Exception e)
/* 462:    */     {
/* 463:591 */       e.printStackTrace();
/* 464:    */     }
/* 465:593 */     return null;
/* 466:    */   }
/* 467:    */   
/* 468:    */   public static void main(String[] args) {}
/* 469:    */   
/* 470:    */   public boolean validate(XModel valueM, XModel qaM, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 471:    */   {
/* 472:624 */     return false;
/* 473:    */   }
/* 474:    */   
/* 475:    */   public boolean validate(Hashtable rs, Hashtable flags, ResultSet checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 476:    */   {
/* 477:630 */     return false;
/* 478:    */   }
/* 479:    */   
/* 480:    */   public boolean validate(Hashtable rs, Hashtable flags, Hashtable checkDef, String name, String value, StringBuffer errorMsg, String domain)
/* 481:    */   {
/* 482:637 */     return false;
/* 483:    */   }
/* 484:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.FlowChecks
 * JD-Core Version:    0.7.0.1
 */