/*   1:    */ package com.kentropy.agents.resource.physician;
/*   2:    */ 
/*   3:    */ import com.kentropy.cmetraining.grading.Grading;
/*   4:    */ import com.kentropy.db.TestXUIDB;
/*   5:    */ import com.kentropy.process.Agent;
/*   6:    */ import com.kentropy.process.OfflineWorkListHandler;
/*   7:    */ import com.kentropy.process.PhysicianStateMachine3;
/*   8:    */ import com.kentropy.process.Process;
/*   9:    */ import com.kentropy.process.notification.ProcessNotification;
/*  10:    */ import java.io.PrintStream;
/*  11:    */ import java.text.ParseException;
/*  12:    */ import java.text.SimpleDateFormat;
/*  13:    */ import java.util.Calendar;
/*  14:    */ import java.util.Date;
/*  15:    */ import java.util.GregorianCalendar;
/*  16:    */ import net.xoetrope.xui.data.XBaseModel;
/*  17:    */ import net.xoetrope.xui.data.XModel;
/*  18:    */ 
/*  19:    */ public class PhysicianMonitoringAgent3
/*  20:    */   implements Agent
/*  21:    */ {
/*  22: 22 */   PhysicianStateMachine3 sm = null;
/*  23: 23 */   Process p = null;
/*  24: 24 */   String resultTable = "training_results";
/*  25: 25 */   String p1ResultCol = "p1_result";
/*  26: 26 */   String p2ResultCol = "p2_result";
/*  27:    */   
/*  28:    */   public void setPhysicianStatus(String phy, String status)
/*  29:    */     throws Exception
/*  30:    */   {
/*  31: 30 */     XModel dataM = new XBaseModel();
/*  32: 31 */     ((XModel)dataM.get("status")).set(status);
/*  33:    */     
/*  34: 33 */     TestXUIDB.getInstance().saveDataM1("physician", "id='" + phy + "'", dataM);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int checkCodingResult()
/*  38:    */     throws Exception
/*  39:    */   {
/*  40: 39 */     XModel xm = new XBaseModel();
/*  41:    */     
/*  42: 41 */     System.out.println("physician ='" + this.p.pid + "'");
/*  43: 42 */     Grading grading = new Grading();
/*  44: 43 */     grading.performGradingPhase1(this.p.pid);
/*  45: 44 */     TestXUIDB.getInstance().getData(this.resultTable, this.p1ResultCol, " physician ='" + this.p.pid + "'", xm);
/*  46: 45 */     if (xm.getNumChildren() > 0)
/*  47:    */     {
/*  48: 47 */       String p1Result1 = xm.get(0).get(0).get().toString();
/*  49: 49 */       if (p1Result1 != null)
/*  50:    */       {
/*  51: 51 */         if (p1Result1.equals("pass")) {
/*  52: 53 */           return 1;
/*  53:    */         }
/*  54: 56 */         Date eddt = new SimpleDateFormat("yyyy-MM-dd").parse(this.sm.getCurStep().eddate);
/*  55: 57 */         if (new Date().before(eddt)) {
/*  56: 59 */           return -1;
/*  57:    */         }
/*  58: 62 */         return 0;
/*  59:    */       }
/*  60:    */     }
/*  61: 66 */     return -1;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int checkReconcResult()
/*  65:    */     throws Exception
/*  66:    */   {
/*  67: 72 */     XModel xm = new XBaseModel();
/*  68:    */     
/*  69: 74 */     System.out.println("  physician ='" + this.p.pid + "'");
/*  70: 75 */     Grading grading = new Grading();
/*  71: 76 */     grading.performGradingPhase2(this.p.pid);
/*  72: 77 */     TestXUIDB.getInstance().getData(this.resultTable, this.p2ResultCol, " physician ='" + this.p.pid + "' ", xm);
/*  73: 79 */     if (xm.getNumChildren() > 0)
/*  74:    */     {
/*  75: 81 */       String p2Result1 = xm.get(0).get(0).get().toString();
/*  76: 83 */       if (p2Result1 != null)
/*  77:    */       {
/*  78: 85 */         if (p2Result1.equals("pass")) {
/*  79: 87 */           return 1;
/*  80:    */         }
/*  81: 90 */         Date eddt = new SimpleDateFormat("yyyy-mm-dd").parse(this.sm.getCurStep().eddate);
/*  82: 91 */         if (new Date().before(eddt)) {
/*  83: 93 */           return -1;
/*  84:    */         }
/*  85: 96 */         return 0;
/*  86:    */       }
/*  87:    */     }
/*  88:100 */     return -1;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void processResult(int result)
/*  92:    */     throws Exception
/*  93:    */   {
/*  94:106 */     String step = "step" + this.sm.currentStep;
/*  95:107 */     if (result == 1)
/*  96:    */     {
/*  97:109 */       XModel xm = new XBaseModel();
/*  98:    */       
/*  99:111 */       new ProcessNotification().queue(this.p.pid, "physician", step + "-pass", step + "-pass", "init");
/* 100:    */       
/* 101:113 */       this.sm.getCurStep().currentState = "complete";
/* 102:    */     }
/* 103:115 */     else if (result == 0)
/* 104:    */     {
/* 105:117 */       new ProcessNotification().queue(this.p.pid, "physician", step + "-fail", step + "-fail", "init");
/* 106:    */       
/* 107:119 */       this.sm.getCurStep().currentState = "terminated";
/* 108:120 */       this.sm.currentState = "terminated";
/* 109:121 */       log("Cleaning up data for " + this.p.pid + " RESULT=" + result);
/* 110:    */       
/* 111:123 */       new Grading().cleanUpOldData(this.p.pid);
/* 112:124 */       log("Finished Cleaning up data for " + this.p.pid + " RESULT=" + result);
/* 113:    */     }
/* 114:    */     else
/* 115:    */     {
/* 116:128 */       Calendar cal = Calendar.getInstance();
/* 117:129 */       Date dt1 = cal.getTime();
/* 118:130 */       Date eddt = new SimpleDateFormat("yyyy-MM-dd").parse(this.sm.getCurStep().eddate);
/* 119:131 */       cal.add(5, 5);
/* 120:132 */       if (dt1.after(eddt))
/* 121:    */       {
/* 122:134 */         new ProcessNotification().queue(this.p.pid, "physician", step + "-fail", step + "-fail", "init");
/* 123:135 */         this.sm.getCurStep().currentState = "terminated";
/* 124:136 */         this.sm.currentState = "terminated";
/* 125:    */         
/* 126:138 */         log("Cleaning up data for " + this.p.pid + " RESULT=" + result + " Date " + dt1 + " after " + eddt);
/* 127:    */         
/* 128:140 */         new Grading().cleanUpOldData(this.p.pid);
/* 129:141 */         log("Finished Cleaning up data for " + this.p.pid + " RESULT=" + result + " Date " + dt1 + " after " + eddt);
/* 130:    */       }
/* 131:    */       else
/* 132:    */       {
/* 133:144 */         if (cal.getTime().equals(eddt)) {
/* 134:145 */           new ProcessNotification().queue(this.p.pid, "physician", step + "-reminder", step + "-reminder", "init");
/* 135:    */         }
/* 136:146 */         this.sm.getCurStep().currentState = "inprocess";
/* 137:    */       }
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public int checkQuizResult()
/* 142:    */     throws ParseException
/* 143:    */   {
/* 144:154 */     XModel xm = new XBaseModel();
/* 145:155 */     TestXUIDB.getInstance().getData("quiz_result", "count(*) attempts, max(result) score", "physician='" + this.p.pid + "'", xm);
/* 146:156 */     int attempts = Integer.parseInt(xm.get(0).get("attempts/@value").toString());
/* 147:157 */     Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(this.sm.eddate);
/* 148:158 */     Date today = new Date();
/* 149:159 */     if (attempts == 0)
/* 150:    */     {
/* 151:161 */       if (dt.before(today)) {
/* 152:163 */         return 0;
/* 153:    */       }
/* 154:166 */       return -1;
/* 155:    */     }
/* 156:169 */     int score = Integer.parseInt(xm.get(0).get("score/@value").toString());
/* 157:170 */     if (score >= 20) {
/* 158:172 */       return 1;
/* 159:    */     }
/* 160:175 */     if ((dt.before(today)) || (attempts == 3)) {
/* 161:177 */       return 0;
/* 162:    */     }
/* 163:180 */     return -1;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public String getCurrentPhysicianBatch(String phy)
/* 167:    */     throws Exception
/* 168:    */   {
/* 169:186 */     XModel xm = new XBaseModel();
/* 170:    */     
/* 171:188 */     TestXUIDB.getInstance().getData("physician", "current_tr_batch", "id='" + phy + "'", xm);
/* 172:189 */     if (xm.getNumChildren() > 0) {
/* 173:190 */       return xm.get(0).get(0).get().toString();
/* 174:    */     }
/* 175:192 */     return null;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void stateChange(Process p)
/* 179:    */   {
/* 180:198 */     this.p = p;
/* 181:199 */     this.sm = ((PhysicianStateMachine3)p.states);
/* 182:    */     
/* 183:201 */     String dt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
/* 184:202 */     if (this.sm.currentStep == -1)
/* 185:    */     {
/* 186:204 */       this.sm.currentState = "inprocess";
/* 187:205 */       Process.transition(p.pid);
/* 188:    */     }
/* 189:207 */     else if (this.sm.currentStep == 0)
/* 190:    */     {
/* 191:209 */       if (this.sm.getCurStep().currentState.equals("started"))
/* 192:    */       {
/* 193:211 */         this.sm.getCurStep().currentState = "inprocess";
/* 194:    */       }
/* 195:213 */       else if (this.sm.getCurStep().currentState.equals("inprocess"))
/* 196:    */       {
/* 197:215 */         System.out.println(" Inside IN process ");
/* 198:216 */         log(" Inside IN process ");
/* 199:    */         try
/* 200:    */         {
/* 201:219 */           int result = checkQuizResult();
/* 202:220 */           System.out.println(" Result  " + result);
/* 203:221 */           log(" Result  " + result);
/* 204:    */           
/* 205:223 */           processResult(result);
/* 206:224 */           if (this.sm.getCurStep().currentState != "inprocess") {
/* 207:225 */             Process.transition(p.pid);
/* 208:    */           }
/* 209:    */         }
/* 210:    */         catch (Exception e)
/* 211:    */         {
/* 212:228 */           e.printStackTrace();
/* 213:    */         }
/* 214:    */       }
/* 215:    */     }
/* 216:236 */     if (this.sm.currentStep == 1)
/* 217:    */     {
/* 218:238 */       if (this.sm.getCurStep().currentState.equals("started")) {
/* 219:    */         try
/* 220:    */         {
/* 221:241 */           boolean flg = assignP1();
/* 222:242 */           if (flg)
/* 223:    */           {
/* 224:244 */             this.sm.getCurStep().currentState = "inprocess";
/* 225:245 */             Process.transition(p.pid);return;
/* 226:    */           }
/* 227:248 */           return;
/* 228:    */         }
/* 229:    */         catch (Exception e)
/* 230:    */         {
/* 231:252 */           e.printStackTrace();
/* 232:253 */           return;
/* 233:    */         }
/* 234:    */       }
/* 235:257 */       if ((this.sm.getCurStep().currentState.equals("inprocess")) || (this.sm.getCurStep().currentState.equals("delayed"))) {
/* 236:    */         try
/* 237:    */         {
/* 238:261 */           int result = checkCodingResult();
/* 239:262 */           System.out.println(" Result  " + result);
/* 240:    */           
/* 241:    */ 
/* 242:    */ 
/* 243:266 */           processResult(result);
/* 244:268 */           if (this.sm.getCurStep().currentState == "inprocess") {
/* 245:    */             return;
/* 246:    */           }
/* 247:269 */           Process.transition(p.pid);
/* 248:    */         }
/* 249:    */         catch (Exception e)
/* 250:    */         {
/* 251:273 */           e.printStackTrace();
/* 252:    */         }
/* 253:    */       }
/* 254:    */     }
/* 255:279 */     else if (this.sm.currentStep == 2)
/* 256:    */     {
/* 257:281 */       if (this.sm.getCurStep().currentState.equals("started")) {
/* 258:    */         try
/* 259:    */         {
/* 260:284 */           boolean flg = assignP21();
/* 261:285 */           if (flg)
/* 262:    */           {
/* 263:287 */             this.sm.getCurStep().currentState = "inprocess";
/* 264:288 */             Process.transition(p.pid);return;
/* 265:    */           }
/* 266:291 */           return;
/* 267:    */         }
/* 268:    */         catch (Exception e)
/* 269:    */         {
/* 270:295 */           e.printStackTrace();
/* 271:296 */           return;
/* 272:    */         }
/* 273:    */       }
/* 274:300 */       if ((this.sm.getCurStep().currentState.equals("inprocess")) || (this.sm.getCurStep().currentState.equals("delayed"))) {
/* 275:    */         try
/* 276:    */         {
/* 277:304 */           int result = checkReconcResult();
/* 278:    */           
/* 279:306 */           processResult(result);
/* 280:307 */           System.out.println(" Result  " + result);
/* 281:    */         }
/* 282:    */         catch (Exception e)
/* 283:    */         {
/* 284:310 */           e.printStackTrace();
/* 285:    */         }
/* 286:    */       }
/* 287:    */     }
/* 288:    */   }
/* 289:    */   
/* 290:    */   public boolean assignP1(String uniqno)
/* 291:    */   {
/* 292:318 */     OfflineWorkListHandler ofwh = new OfflineWorkListHandler();
/* 293:    */     
/* 294:320 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 295:    */     try
/* 296:    */     {
/* 297:323 */       Calendar cal = new GregorianCalendar();
/* 298:324 */       int codingtime = 10;
/* 299:325 */       String codingTime1 = TestXUIDB.getInstance().getProperty("codingtime");
/* 300:326 */       if (codingTime1 != null) {
/* 301:    */         try
/* 302:    */         {
/* 303:329 */           codingtime = Integer.parseInt(codingTime1);
/* 304:    */         }
/* 305:    */         catch (Exception e)
/* 306:    */         {
/* 307:333 */           e.printStackTrace();
/* 308:334 */           codingtime = 10;
/* 309:    */         }
/* 310:    */       }
/* 311:337 */       cal.add(5, codingtime);
/* 312:338 */       String dueDate = sdf.format(cal.getTime());
/* 313:339 */       String assignDate = sdf.format(new Date());
/* 314:340 */       dueDate = this.sm.getCurStep().eddate;
/* 315:342 */       synchronized (this)
/* 316:    */       {
/* 317:344 */         String parentPath = "area:1/house:1/household:1";
/* 318:345 */         String[] dataPath = { "/va/" + uniqno };
/* 319:346 */         ofwh.assign("task0", this.p.pid, "cme", "6", parentPath, null, null, null, null, null);
/* 320:347 */         ofwh.assign("task0/task0-" + uniqno, this.p.pid, "cme", "6", parentPath, assignDate, dueDate, dataPath, null, null);
/* 321:    */         
/* 322:349 */         return true;
/* 323:    */       }
/* 324:353 */       return false;
/* 325:    */     }
/* 326:    */     catch (Exception e)
/* 327:    */     {
/* 328:352 */       e.printStackTrace();
/* 329:    */     }
/* 330:    */   }
/* 331:    */   
/* 332:    */   private boolean assignP1()
/* 333:    */     throws Exception
/* 334:    */   {
/* 335:359 */     String curbatch = getCurrentPhysicianBatch(this.p.pid);
/* 336:360 */     if (curbatch == null) {
/* 337:361 */       throw new Exception("batch not found");
/* 338:    */     }
/* 339:364 */     XModel xm = new XBaseModel();
/* 340:365 */     TestXUIDB.getInstance().getData("adult", "uniqno", "true ORDER BY RAND() LIMIT 50 ", xm);
/* 341:366 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 342:    */     {
/* 343:368 */       String uniqno = (String)xm.get(i).get(0).get();
/* 344:369 */       System.out.println(" UniqNO =" + uniqno);
/* 345:370 */       assignP1(uniqno);
/* 346:    */     }
/* 347:372 */     return true;
/* 348:    */   }
/* 349:    */   
/* 350:    */   private boolean assignP21()
/* 351:    */     throws Exception
/* 352:    */   {
/* 353:378 */     String curbatch = getCurrentPhysicianBatch(this.p.pid);
/* 354:379 */     if (curbatch == null) {
/* 355:380 */       throw new Exception("batch not found");
/* 356:    */     }
/* 357:382 */     XModel xm = new XBaseModel();
/* 358:383 */     TestXUIDB.getInstance().getData("cme_report a left join physician b on a.physician=b.id ", "uniqno,icd", " physician ='" + this.p.pid + "' and current_tr_batch='" + curbatch + "'", xm);
/* 359:    */     
/* 360:385 */     int count = 20;
/* 361:386 */     int p2ReportCount = 0;
/* 362:387 */     System.out.println(" Count " + count + " " + this.sm.getCurrentState());
/* 363:388 */     for (int i = 0; (i < xm.getNumChildren()) && (p2ReportCount < 20); i++)
/* 364:    */     {
/* 365:390 */       String uniqno = xm.get(i).get(0).get().toString();
/* 366:391 */       String icd = xm.get(i).get(1).get().toString();
/* 367:392 */       if (findReconciliation(uniqno, icd)) {
/* 368:394 */         p2ReportCount++;
/* 369:    */       }
/* 370:    */     }
/* 371:399 */     return true;
/* 372:    */   }
/* 373:    */   
/* 374:    */   public void log(String message)
/* 375:    */   {
/* 376:404 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.getCurrentState(), message);
/* 377:    */   }
/* 378:    */   
/* 379:    */   public boolean findReconciliation(String uniqno, String icd)
/* 380:    */     throws Exception
/* 381:    */   {
/* 382:409 */     XModel xm = new XBaseModel();
/* 383:    */     
/* 384:411 */     TestXUIDB.getInstance().getData("cme_report", "icd,physician", " uniqno ='" + uniqno + "' and  physician !='" + this.p.pid + "' and icd!='" + icd + "' and uniqno NOT in (select distinct member from tasks where assignedto='" + this.p.pid + "' and task like '%task1')", xm);
/* 385:412 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 386:    */     {
/* 387:414 */       String first = icd;
/* 388:    */       
/* 389:416 */       String second = xm.get(i).get(0).get().toString();
/* 390:417 */       String phy2 = xm.get(i).get(1).get().toString();
/* 391:    */       
/* 392:419 */       log(" icd=" + phy2 + " phy2=" + phy2);
/* 393:421 */       if (!TestXUIDB.getInstance().checkEquivalence(first, second))
/* 394:    */       {
/* 395:423 */         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 396:424 */         String parentPath = "area:1/house:1/household:1";
/* 397:    */         
/* 398:426 */         OfflineWorkListHandler owfh = new OfflineWorkListHandler();
/* 399:427 */         Calendar cal = new GregorianCalendar();
/* 400:428 */         int recontime = 5;
/* 401:429 */         String reconTime1 = TestXUIDB.getInstance().getProperty("recontime");
/* 402:430 */         if (reconTime1 != null) {
/* 403:    */           try
/* 404:    */           {
/* 405:433 */             recontime = Integer.parseInt(reconTime1);
/* 406:    */           }
/* 407:    */           catch (Exception e)
/* 408:    */           {
/* 409:437 */             e.printStackTrace();
/* 410:438 */             recontime = 5;
/* 411:    */           }
/* 412:    */         }
/* 413:441 */         cal.add(5, recontime);
/* 414:442 */         String dueDate = sdf.format(cal.getTime());
/* 415:443 */         String assignDate = sdf.format(new Date());
/* 416:444 */         dueDate = this.sm.getCurStep().eddate;
/* 417:    */         
/* 418:446 */         String[] dataPath = { "/va/" + uniqno, "/cme/" + uniqno + "/Coding/" + this.p.pid, "/cme/" + uniqno + "/Coding/" + phy2, "/cme/" + uniqno + "/Coding/Comments/" + this.p.pid, "/cme/" + uniqno + "/Coding/Comments/" + phy2 };
/* 419:    */         
/* 420:448 */         owfh.assign("task0", this.p.pid, "cme", "6", parentPath, null, null, null, null, null);
/* 421:    */         
/* 422:450 */         owfh.assign("task0/task1-" + uniqno, this.p.pid, "cme", "6", parentPath, assignDate, dueDate, dataPath, null, null);
/* 423:451 */         String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 424:452 */         TestXUIDB.getInstance().createKeyValueChangeLog("keyvalue", "/cme/" + uniqno + "/stage", "Reconciliation");
/* 425:453 */         TestXUIDB.getInstance().createKeyValueChangeLog("keyvalue", "/cme/" + uniqno + "/phy2", phy2);
/* 426:    */         
/* 427:455 */         String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 428:456 */         TestXUIDB.getInstance().addToChangeLogOutboundQueue(this.p.pid, frombookmark, tobookmark);
/* 429:457 */         return true;
/* 430:    */       }
/* 431:    */     }
/* 432:462 */     return false;
/* 433:    */   }
/* 434:    */   
/* 435:    */   public void batchExecute() {}
/* 436:    */   
/* 437:    */   public static void main(String[] args)
/* 438:    */   {
/* 439:    */     try
/* 440:    */     {
/* 441:473 */       Process p = Process.createProcess("808", PhysicianStateMachine3.createInstance("808", "2012-11-10"));
/* 442:474 */       Process.refreshProcessStatus("pid='808'");
/* 443:475 */       p = TestXUIDB.getInstance().getProcess("808");
/* 444:476 */       PhysicianStateMachine3 psm = (PhysicianStateMachine3)p.states;
/* 445:477 */       psm.currentState = "inprocess";
/* 446:478 */       TestXUIDB.getInstance().saveProcess(p);
/* 447:479 */       Process.refreshProcessStatus("pid='808'");
/* 448:    */       
/* 449:481 */       Process.refreshProcessStatus("pid='808'");
/* 450:482 */       Process.refreshProcessStatus("pid='808'");
/* 451:483 */       p = TestXUIDB.getInstance().getProcess("808");
/* 452:    */     }
/* 453:    */     catch (Exception e)
/* 454:    */     {
/* 455:487 */       e.printStackTrace();
/* 456:    */     }
/* 457:    */   }
/* 458:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.resource.physician.PhysicianMonitoringAgent3
 * JD-Core Version:    0.7.0.1
 */