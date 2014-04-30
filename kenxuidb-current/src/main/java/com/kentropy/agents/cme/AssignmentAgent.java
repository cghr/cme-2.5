/*   1:    */ package com.kentropy.agents.cme;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.db.XTaskModel;
/*   5:    */ import com.kentropy.process.Agent;
/*   6:    */ import com.kentropy.process.CMEStateMachine;
/*   7:    */ import com.kentropy.process.OfflineWorkListHandler;
/*   8:    */ import com.kentropy.process.Process;
/*   9:    */ import com.kentropy.process.StateMachine;
/*  10:    */ import com.kentropy.transfer.Client;
/*  11:    */ import java.io.InputStream;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import java.text.SimpleDateFormat;
/*  14:    */ import java.util.Calendar;
/*  15:    */ import java.util.Date;
/*  16:    */ import java.util.GregorianCalendar;
/*  17:    */ import java.util.Properties;
/*  18:    */ import java.util.Vector;
/*  19:    */ import net.xoetrope.xui.data.XBaseModel;
/*  20:    */ import net.xoetrope.xui.data.XModel;
/*  21:    */ import org.apache.log4j.Logger;
/*  22:    */ 
/*  23:    */ public class AssignmentAgent
/*  24:    */   implements Agent
/*  25:    */ {
/*  26: 26 */   CMEStateMachine sm = null;
/*  27: 28 */   Process p = null;
/*  28: 29 */   Logger logger = Logger.getLogger(getClass().getName());
/*  29: 31 */   public String ext = "png";
/*  30:    */   String[] resources;
/*  31:    */   String[] resources1;
/*  32: 34 */   int count = 0;
/*  33:    */   
/*  34:    */   public void sendImage()
/*  35:    */     throws Exception
/*  36:    */   {
/*  37: 39 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/*  38: 40 */     String firstpage = this.p.pid + "_0_blank." + this.ext;
/*  39: 41 */     String secondpage = this.p.pid + "_1_blank." + this.ext;
/*  40: 42 */     String codcrop = this.p.pid + "_cod." + this.ext;
/*  41:    */     
/*  42: 44 */     Client cl = new Client();
/*  43: 45 */     cl.run(imagepath + firstpage, firstpage, this.sm.assignedfirst + "," + 
/*  44: 46 */       this.sm.assignedsecond);
/*  45: 47 */     cl.run(imagepath + secondpage, secondpage, this.sm.assignedfirst + "," + 
/*  46: 48 */       this.sm.assignedsecond);
/*  47: 49 */     cl.run(imagepath + codcrop, codcrop, this.sm.assignedfirst + "," + 
/*  48: 50 */       this.sm.assignedsecond);
/*  49:    */     
/*  50: 52 */     XModel dataModel = new XBaseModel();
/*  51: 53 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", "/va/" + this.p.pid);
/*  52: 54 */     String domain = dataModel.get("type/@value").toString();
/*  53: 55 */     this.logger.info("Domain: " + domain);
/*  54: 56 */     if (domain.toLowerCase().equals("maternal"))
/*  55:    */     {
/*  56: 57 */       String maternalImage = (String)dataModel.get("report/maternal_image/@value");
/*  57: 58 */       this.logger.info("Maternal Image:" + maternalImage);
/*  58: 59 */       cl.run(imagepath + maternalImage, maternalImage, this.sm.assignedfirst + "," + 
/*  59: 60 */         this.sm.assignedsecond);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void updateWorkload(String physician)
/*  64:    */   {
/*  65: 66 */     String sql = "update physician_workload set workload=workload+1 where physician='" + 
/*  66: 67 */       physician + "'";
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void stateChange(Process p)
/*  70:    */   {
/*  71: 72 */     this.logger.info("Inside AssignmentAgent.stateChange() " + p.states.getCurrentState());
/*  72: 73 */     this.p = p;
/*  73: 74 */     if (p.states.getCurrentState().equals("assignment"))
/*  74:    */     {
/*  75: 75 */       this.sm = ((CMEStateMachine)p.states);
/*  76: 77 */       if (assign()) {
/*  77: 78 */         Process.transition(p.pid);
/*  78:    */       }
/*  79:    */     }
/*  80: 80 */     if (p.states.getCurrentState().equals("reassignment"))
/*  81:    */     {
/*  82: 81 */       this.sm = ((CMEStateMachine)p.states);
/*  83:    */       
/*  84: 83 */       reassign1();
/*  85: 84 */       if (!this.sm.reassignCoding) {
/*  86: 85 */         Process.transition(p.pid);
/*  87:    */       }
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void stateChange1(Process p)
/*  92:    */   {
/*  93: 91 */     this.logger.info("Inside AssignmentAgent.stateChange()");
/*  94: 92 */     this.p = p;
/*  95: 93 */     if (p.states.getCurrentState().equals("assignment"))
/*  96:    */     {
/*  97: 94 */       this.sm = ((CMEStateMachine)p.states);
/*  98:    */       
/*  99: 96 */       this.logger.info(" Doing assignment for Process " + p.pid);
/* 100:    */       
/* 101: 98 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 102:    */       
/* 103:100 */       Vector v = findPhysicians(p.pid);
/* 104:101 */       if (v.size() < 2) {
/* 105:102 */         return;
/* 106:    */       }
/* 107:    */       try
/* 108:    */       {
/* 109:106 */         Calendar cal = new GregorianCalendar();
/* 110:107 */         cal.add(5, 10);
/* 111:108 */         String dueDate = sdf.format(cal.getTime());
/* 112:109 */         String assignDate = sdf.format(new Date());
/* 113:110 */         String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 114:111 */         synchronized (this)
/* 115:    */         {
/* 116:113 */           XTaskModel rootM = XTaskModel.getRoot(v.get(0).toString(), 
/* 117:114 */             "cme", "6");
/* 118:115 */           XTaskModel cmeM = (XTaskModel)rootM.get("task0");
/* 119:116 */           cmeM.area = "1";
/* 120:117 */           cmeM.household = "1";
/* 121:118 */           cmeM.house = "1";
/* 122:119 */           cmeM.assignedTo = v.get(0).toString();
/* 123:120 */           cmeM.set("@assignedto", v.get(0).toString());
/* 124:121 */           XTaskModel codM1 = (XTaskModel)cmeM.get("task0-" + p.pid);
/* 125:122 */           codM1.assignedTo = v.get(0).toString();
/* 126:123 */           codM1.set("@assignedto", v.get(0).toString());
/* 127:124 */           codM1.set("@dateassigned", assignDate);
/* 128:125 */           codM1.set("@duedate", dueDate);
/* 129:126 */           cmeM.save();
/* 130:127 */           codM1.save();
/* 131:128 */           this.sm.assignedfirst = v.get(0).toString();
/* 132:129 */           updateWorkload(this.sm.assignedfirst);
/* 133:130 */           Vector keys = new Vector();
/* 134:131 */           keys.add("key1");
/* 135:132 */           keys.add("value1");
/* 136:    */           
/* 137:134 */           TestXUIDB.getInstance().sendServerLogs("admin", 
/* 138:135 */             v.get(0).toString(), bookmark, "999999");
/* 139:136 */           bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 140:137 */           rootM = XTaskModel.getRoot(v.get(1).toString(), "cme", "6");
/* 141:138 */           cmeM = (XTaskModel)rootM.get("task0");
/* 142:139 */           cmeM.set("@assignedto", v.get(1).toString());
/* 143:140 */           cmeM.area = "1";
/* 144:141 */           cmeM.household = "1";
/* 145:142 */           cmeM.house = "1";
/* 146:143 */           cmeM.assignedTo = v.get(1).toString();
/* 147:144 */           cmeM.save();
/* 148:145 */           codM1 = (XTaskModel)cmeM.get("task0-" + p.pid);
/* 149:146 */           codM1.assignedTo = v.get(1).toString();
/* 150:147 */           codM1.set("@assignedto", v.get(1).toString());
/* 151:    */           
/* 152:149 */           codM1.set("@dateassigned", assignDate);
/* 153:150 */           codM1.set("@duedate", dueDate);
/* 154:    */           
/* 155:152 */           codM1.save();
/* 156:153 */           this.sm.assignedsecond = v.get(1).toString();
/* 157:    */           
/* 158:155 */           TestXUIDB.getInstance().sendServerLogs("admin", 
/* 159:156 */             v.get(1).toString(), bookmark, "999999");
/* 160:157 */           bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 161:158 */           TestXUIDB.getInstance().createChangeLog("keyvalue", 
/* 162:159 */             "key1 like '/va/" + p.pid + "%'", keys);
/* 163:160 */           TestXUIDB.getInstance().sendServerLogs("admin", 
/* 164:161 */             v.get(0).toString() + "," + v.get(1).toString(), 
/* 165:162 */             bookmark, "999999");
/* 166:163 */           sendImage();
/* 167:    */           
/* 168:165 */           this.sm.assignedsecond = v.get(1).toString();
/* 169:    */         }
/* 170:    */       }
/* 171:    */       catch (Exception e)
/* 172:    */       {
/* 173:170 */         e.printStackTrace();
/* 174:171 */         TestXUIDB.getInstance().logAgent(p.pid, getClass().getName(), this.sm.currentState, "Error:" + e.getMessage());
/* 175:    */         
/* 176:173 */         Process.transition(p.pid);
/* 177:    */       }
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   public void getResources()
/* 182:    */   {
/* 183:180 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/* 184:181 */     String[] resourcesC = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext };
/* 185:182 */     String[] resourcesC1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext };
/* 186:183 */     XModel dataModel = new XBaseModel();
/* 187:184 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", "/va/" + this.p.pid);
/* 188:185 */     String domain = dataModel.get("type/@value").toString();
/* 189:187 */     if (domain.toLowerCase().equals("maternal"))
/* 190:    */     {
/* 191:188 */       String maternalImage = (String)dataModel.get("report/maternal_image/@value");
/* 192:189 */       String[] resourcesM = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext, imagepath + maternalImage };
/* 193:190 */       String[] resourcesM1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext, maternalImage };
/* 194:191 */       this.resources = resourcesM;
/* 195:192 */       this.resources1 = resourcesM1;
/* 196:    */     }
/* 197:    */     else
/* 198:    */     {
/* 199:196 */       this.resources = resourcesC;
/* 200:197 */       this.resources1 = resourcesC1;
/* 201:    */     }
/* 202:    */   }
/* 203:    */   
/* 204:    */   public boolean assign()
/* 205:    */   {
/* 206:203 */     OfflineWorkListHandler ofwh = new OfflineWorkListHandler();
/* 207:    */     
/* 208:205 */     this.logger.info(" Doing assignment for Process " + this.p.pid);
/* 209:    */     
/* 210:207 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 211:    */     
/* 212:209 */     Vector v = findPhysicians(this.p.pid);
/* 213:210 */     if (v.size() < 2) {
/* 214:211 */       return false;
/* 215:    */     }
/* 216:    */     try
/* 217:    */     {
/* 218:215 */       Calendar cal = new GregorianCalendar();
/* 219:216 */       int codingtime = 10;
/* 220:217 */       String codingTime1 = TestXUIDB.getInstance().getProperty("codingtime");
/* 221:218 */       if (codingTime1 != null) {
/* 222:    */         try
/* 223:    */         {
/* 224:221 */           codingtime = Integer.parseInt(codingTime1);
/* 225:    */         }
/* 226:    */         catch (Exception e)
/* 227:    */         {
/* 228:225 */           e.printStackTrace();
/* 229:226 */           codingtime = 10;
/* 230:    */         }
/* 231:    */       }
/* 232:229 */       cal.add(5, codingtime);
/* 233:230 */       String dueDate = sdf.format(cal.getTime());
/* 234:231 */       String assignDate = sdf.format(new Date());
/* 235:232 */       String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 236:234 */       synchronized (this)
/* 237:    */       {
/* 238:236 */         getResources();
/* 239:237 */         String parentPath = "area:1/house:1/household:1";
/* 240:238 */         String[] dataPath = { "/va/" + this.p.pid };
/* 241:239 */         ofwh.assign("task0", v.get(0).toString(), "cme", "6", parentPath, null, null, null, null, null);
/* 242:240 */         ofwh.assign("task0/task0-" + this.p.pid, v.get(0).toString(), "cme", "6", parentPath, assignDate, dueDate, dataPath, this.resources, this.resources1);
/* 243:241 */         this.sm.assignedfirst = v.get(0).toString();
/* 244:242 */         ofwh.assign("task0", v.get(1).toString(), "cme", "6", parentPath, null, null, null, null, null);
/* 245:243 */         ofwh.assign("task0/task0-" + this.p.pid, v.get(1).toString(), "cme", "6", parentPath, assignDate, dueDate, dataPath, this.resources, this.resources1);
/* 246:244 */         this.sm.assignedsecond = v.get(1).toString();
/* 247:245 */         return true;
/* 248:    */       }
/* 249:251 */       //return false;
/* 250:    */     }
/* 251:    */     catch (Exception e)
/* 252:    */     {
/* 253:248 */       e.printStackTrace();
/* 254:    */     }
				  return false;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public boolean reassign1()
/* 258:    */   {
/* 259:256 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.getCurrentState(), "starting to reassign list=" + this.sm.codingUnassigned + " reassignCoding=" + this.sm.reassignCoding);
/* 260:258 */     if ((this.sm.codingUnassigned.trim().equals("")) || (this.sm.codingUnassigned.trim().equals(":")))
/* 261:    */     {
/* 262:260 */       TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.getCurrentState(), "Nothing to assign list=" + this.sm.codingUnassigned + " reassignCoding=" + this.sm.reassignCoding);
/* 263:261 */       this.sm.codingUnassigned = " ";
/* 264:262 */       this.sm.reassignCoding = false;
/* 265:263 */       return false;
/* 266:    */     }
/* 267:265 */     String[] list = this.sm.codingUnassigned.split(":");
/* 268:    */     
/* 269:267 */     OfflineWorkListHandler ofwh = new OfflineWorkListHandler();
/* 270:    */     
/* 271:269 */     this.logger.info(" Doing assignment for Process " + this.p.pid);
/* 272:    */     
/* 273:271 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 274:272 */     String ignore = "";
/* 275:273 */     XModel xm = new XBaseModel();
/* 276:274 */     TestXUIDB.getInstance().getData("tasks", "distinct assignedto", "task like '%task0/task0' and member='" + this.p.pid + "'", xm);
/* 277:276 */     for (int i = 0; i < xm.getNumChildren(); i++) {
/* 278:278 */       ignore = ignore + (i > 0 ? "," : "") + "'" + xm.get(i).get(0).get() + "'";
/* 279:    */     }
/* 280:281 */     Vector v = findPhysicians(this.p.pid, ignore);
/* 281:282 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, "Found   " + v.size());
/* 282:283 */     if (v.size() < 1) {
/* 283:284 */       return false;
/* 284:    */     }
/* 285:    */     try
/* 286:    */     {
/* 287:289 */       Calendar cal = new GregorianCalendar();
/* 288:290 */       int codingtime = 10;
/* 289:291 */       String codingTime1 = TestXUIDB.getInstance().getProperty("codingtime");
/* 290:292 */       if (codingTime1 != null) {
/* 291:    */         try
/* 292:    */         {
/* 293:295 */           codingtime = Integer.parseInt(codingTime1);
/* 294:    */         }
/* 295:    */         catch (Exception e)
/* 296:    */         {
/* 297:299 */           e.printStackTrace();
/* 298:300 */           codingtime = 10;
/* 299:    */         }
/* 300:    */       }
/* 301:303 */       cal.add(5, codingtime);
/* 302:304 */       String dueDate = sdf.format(cal.getTime());
/* 303:305 */       String assignDate = sdf.format(new Date());
/* 304:306 */       String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 305:308 */       synchronized (this)
/* 306:    */       {
/* 307:310 */         getResources();
/* 308:311 */         String pending = "";
/* 309:312 */         for (int i = 0; i < list.length; i++)
/* 310:    */         {
/* 311:314 */           TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, "Processing reassigment " + list[i]);
/* 312:315 */           if (i < v.size())
/* 313:    */           {
/* 314:317 */             String parentPath = "area:1/house:1/household:1";
/* 315:318 */             String[] dataPath = { "/va/" + this.p.pid };
/* 316:319 */             ofwh.assign("task0", v.get(i).toString(), "cme", "6", parentPath, null, null, null, null, null);
/* 317:320 */             ofwh.assign("task0/task0-" + this.p.pid, v.get(i).toString(), "cme", "6", parentPath, assignDate, dueDate, dataPath, this.resources, this.resources1);
/* 318:322 */             if (list[i].trim().equals(this.sm.assignedfirst.trim()))
/* 319:    */             {
/* 320:324 */               TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, "assigned first change " + this.sm.assignedfirst + " -- " + v.get(i));
/* 321:325 */               this.sm.assignedfirst = v.get(i).toString();
/* 322:    */             }
/* 323:327 */             else if (list[i].equals(this.sm.assignedsecond))
/* 324:    */             {
/* 325:329 */               TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, "assigned second change " + this.sm.assignedsecond + " -- " + v.get(i));
/* 326:330 */               this.sm.assignedsecond = v.get(i).toString();
/* 327:    */             }
/* 328:    */           }
/* 329:    */           else
/* 330:    */           {
/* 331:334 */             pending = pending + (pending.trim().length() != 0 ? ":" : new StringBuilder().append(list[i]).toString());
/* 332:    */           }
/* 333:    */         }
/* 334:338 */         this.sm.codingUnassigned = pending;
/* 335:339 */         if (pending.trim().length() == 0)
/* 336:    */         {
/* 337:341 */           this.sm.codingUnassigned = " ";
/* 338:342 */           this.sm.reassignCoding = false;
/* 339:    */         }
/* 340:345 */         return true;
/* 341:    */       }
/* 342:350 */       //return false;
/* 343:    */     }
/* 344:    */     catch (Exception e)
/* 345:    */     {
/* 346:348 */       e.printStackTrace();
/* 347:    */     }
                  return false;
/* 348:    */   }
/* 349:    */   
/* 350:    */   public void reaassign()
/* 351:    */   {
/* 352:355 */     this.logger.info(" Doing assignment for Process " + this.p.pid);
/* 353:    */     
/* 354:357 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 355:    */     
/* 356:359 */     Vector v = findPhysicians(this.p.pid);
/* 357:360 */     if (v.size() < 2) {
/* 358:361 */       return;
/* 359:    */     }
/* 360:    */     try
/* 361:    */     {
/* 362:365 */       Calendar cal = new GregorianCalendar();
/* 363:366 */       cal.add(5, 10);
/* 364:367 */       String dueDate = sdf.format(cal.getTime());
/* 365:368 */       String assignDate = sdf.format(new Date());
/* 366:369 */       String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 367:370 */       synchronized (this)
/* 368:    */       {
/* 369:372 */         XTaskModel rootM = XTaskModel.getRoot(v.get(0).toString(), 
/* 370:373 */           "cme", "6");
/* 371:374 */         XTaskModel cmeM = (XTaskModel)rootM.get("task0");
/* 372:375 */         cmeM.area = "1";
/* 373:376 */         cmeM.household = "1";
/* 374:377 */         cmeM.house = "1";
/* 375:378 */         cmeM.assignedTo = v.get(0).toString();
/* 376:379 */         cmeM.set("@assignedto", v.get(0).toString());
/* 377:380 */         XTaskModel codM1 = (XTaskModel)cmeM.get("task0-" + this.p.pid);
/* 378:381 */         codM1.assignedTo = v.get(0).toString();
/* 379:382 */         codM1.set("@assignedto", v.get(0).toString());
/* 380:383 */         codM1.set("@dateassigned", assignDate);
/* 381:384 */         codM1.set("@duedate", dueDate);
/* 382:    */         
/* 383:386 */         cmeM.save();
/* 384:387 */         codM1.save();
/* 385:388 */         this.sm.assignedfirst = v.get(0).toString();
/* 386:389 */         updateWorkload(this.sm.assignedfirst);
/* 387:390 */         Vector keys = new Vector();
/* 388:391 */         keys.add("key1");
/* 389:392 */         keys.add("value1");
/* 390:    */         
/* 391:394 */         TestXUIDB.getInstance().sendServerLogs("admin", 
/* 392:395 */           v.get(0).toString(), bookmark, "999999");
/* 393:396 */         bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 394:397 */         rootM = XTaskModel.getRoot(v.get(1).toString(), "cme", "6");
/* 395:398 */         cmeM = (XTaskModel)rootM.get("task0");
/* 396:399 */         cmeM.set("@assignedto", v.get(1).toString());
/* 397:400 */         cmeM.area = "1";
/* 398:401 */         cmeM.household = "1";
/* 399:402 */         cmeM.house = "1";
/* 400:403 */         cmeM.assignedTo = v.get(1).toString();
/* 401:404 */         cmeM.save();
/* 402:405 */         codM1 = (XTaskModel)cmeM.get("task0-" + this.p.pid);
/* 403:406 */         codM1.assignedTo = v.get(1).toString();
/* 404:407 */         codM1.set("@assignedto", v.get(1).toString());
/* 405:    */         
/* 406:409 */         codM1.set("@dateassigned", assignDate);
/* 407:410 */         codM1.set("@duedate", dueDate);
/* 408:    */         
/* 409:412 */         codM1.save();
/* 410:413 */         this.sm.assignedsecond = v.get(1).toString();
/* 411:    */         
/* 412:415 */         TestXUIDB.getInstance().sendServerLogs("admin", 
/* 413:416 */           v.get(1).toString(), bookmark, "999999");
/* 414:417 */         bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 415:418 */         TestXUIDB.getInstance().createChangeLog("keyvalue", 
/* 416:419 */           "key1 like '/va/" + this.p.pid + "%'", keys);
/* 417:420 */         TestXUIDB.getInstance().sendServerLogs("admin", 
/* 418:421 */           v.get(0).toString() + "," + v.get(1).toString(), 
/* 419:422 */           bookmark, "999999");
/* 420:423 */         sendImage();
/* 421:    */         
/* 422:425 */         this.sm.assignedsecond = v.get(1).toString();
/* 423:    */       }
/* 424:    */     }
/* 425:    */     catch (Exception e)
/* 426:    */     {
/* 427:430 */       e.printStackTrace();
/* 428:    */     }
/* 429:    */   }
/* 430:    */   
/* 431:    */   public void batchExecute() {}
/* 432:    */   
/* 433:    */   public Vector findPhysicians(String vaId)
/* 434:    */   {
/* 435:440 */     XModel workloadModel = new XBaseModel();
/* 436:    */     
/* 437:442 */     String language = TestXUIDB.getInstance().getValue("keyvalue", 
/* 438:443 */       "/va/" + vaId + "/gi/language");
/* 439:    */     
/* 440:445 */     TestXUIDB.getInstance().getPhysiciansWithLessWorkload(language, "a.coder=1 and a.status!='stopped'", workloadModel);
/* 441:    */     
/* 442:447 */     Vector ind = new Vector();
/* 443:448 */     this.logger.info("Going to call assign");
/* 444:449 */     if (workloadModel.getNumChildren() >= 2) {
/* 445:451 */       for (int i = 0; i < 2; i++)
/* 446:    */       {
/* 447:452 */         String phy = ((XModel)workloadModel.get(i).get("id")).get().toString();
/* 448:453 */         this.logger.info("physician::" + phy);
/* 449:454 */         ind.add(phy);
/* 450:    */       }
/* 451:    */     }
/* 452:459 */     return ind;
/* 453:    */   }
/* 454:    */   
/* 455:    */   public Vector findPhysicians(String vaId, String ignore)
/* 456:    */   {
/* 457:463 */     XModel workloadModel = new XBaseModel();
/* 458:    */     
/* 459:465 */     String language = TestXUIDB.getInstance().getValue("keyvalue", 
/* 460:466 */       "/va/" + vaId + "/gi/language");
/* 461:    */     
/* 462:468 */     TestXUIDB.getInstance().getPhysiciansWithLessWorkload(language, "a.coder=1 and a.status='active' and a.id not in(" + ignore + ")", workloadModel);
/* 463:    */     
/* 464:470 */     Vector ind = new Vector();
/* 465:471 */     this.logger.info("Going to call assign");
/* 466:472 */     if (workloadModel.getNumChildren() >= 1) {
/* 467:474 */       for (int i = 0; i < workloadModel.getNumChildren(); i++)
/* 468:    */       {
/* 469:475 */         String phy = ((XModel)workloadModel.get(i).get("id")).get().toString();
/* 470:476 */         this.logger.info("physician::" + phy);
/* 471:477 */         ind.add(phy);
/* 472:    */       }
/* 473:    */     }
/* 474:482 */     return ind;
/* 475:    */   }
/* 476:    */   
/* 477:    */   public static void main(String[] args)
/* 478:    */     throws Exception
/* 479:    */   {
/* 480:488 */     System.out.println("246".split(":").length);
/* 481:489 */     System.in.read();
/* 482:490 */     Properties p = System.getProperties();
/* 483:491 */     p.list(System.out);
/* 484:    */   }
/* 485:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.AssignmentAgent
 * JD-Core Version:    0.7.0.1
 */