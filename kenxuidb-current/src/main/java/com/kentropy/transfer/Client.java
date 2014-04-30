/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.resource.Base64FileDecoder;
/*   5:    */ import com.kentropy.resource.Base64FileEncoder;
/*   6:    */ import com.kentropy.security.KenJAASAuthenticator;
/*   7:    */ import com.kentropy.security.KenPrincipal;
/*   8:    */ import com.kentropy.security.TestJAAS;
/*   9:    */ import java.io.BufferedReader;
/*  10:    */ import java.io.DataInputStream;
/*  11:    */ import java.io.DataOutputStream;
/*  12:    */ import java.io.File;
/*  13:    */ import java.io.FileOutputStream;
/*  14:    */ import java.io.FileReader;
/*  15:    */ import java.io.FileWriter;
/*  16:    */ import java.io.IOException;
/*  17:    */ import java.io.PrintStream;
/*  18:    */ import java.net.Socket;
/*  19:    */ import java.net.URL;
/*  20:    */ import java.text.SimpleDateFormat;
/*  21:    */ import java.util.Date;
/*  22:    */ import java.util.Enumeration;
/*  23:    */ import java.util.Hashtable;
/*  24:    */ import java.util.Properties;
/*  25:    */ import java.util.Set;
/*  26:    */ import java.util.Vector;
/*  27:    */ import java.util.zip.ZipEntry;
/*  28:    */ import java.util.zip.ZipInputStream;
/*  29:    */ import org.apache.log4j.Logger;
/*  30:    */ 
/*  31:    */ public class Client
/*  32:    */ {
/*  33: 33 */   Logger logger = Logger.getLogger(getClass().getName());
/*  34: 34 */   public String participant = "12";
/*  35: 35 */   public String server = "localhost";
/*  36: 36 */   public int sendPort = 8086;
/*  37: 37 */   public int receivePort = 8087;
/*  38: 38 */   public String sendURL = null;
/*  39: 39 */   public String receiveURL = null;
/*  40: 40 */   public String repoURL = null;
/*  41: 41 */   public String operation = "import";
/*  42: 42 */   public String msgType = "changelogs";
/*  43: 43 */   public String type = "socket";
/*  44: 44 */   public String path = ".";
/*  45: 45 */   public String mailboxPath = "./mbox";
/*  46: 47 */   public String md5 = "enable";
/*  47: 48 */   public String authUrl = "";
/*  48: 50 */   public static Hashtable dests = new Hashtable();
/*  49:    */   public static TestJAAS tj;
/*  50: 52 */   public String sessionID = "";
/*  51: 54 */   public Vector messages = new Vector();
/*  52: 55 */   String zipName = "";
/*  53: 57 */   MsgChannel mc = null;
/*  54:    */   
/*  55:    */   static
/*  56:    */   {
/*  57:    */     try
/*  58:    */     {
/*  59: 63 */       Logger logger = Logger.getLogger(Client.class);
/*  60:    */       
/*  61: 65 */       Properties p = (Properties)TestXUIDB.getInstance().getBean("test");
/*  62: 66 */       logger.info(p.getProperty("default"));
/*  63:    */       
/*  64: 68 */       Set keys = p.keySet();
/*  65: 69 */       Enumeration e = p.propertyNames();
/*  66: 70 */       logger.info(" Elements " + e.hasMoreElements());
/*  67: 72 */       while (e.hasMoreElements())
/*  68:    */       {
/*  69: 74 */         String key = e.nextElement().toString();
/*  70: 75 */         logger.info(key);
/*  71: 76 */         dests.put(key, TestXUIDB.getInstance().getBean(p.getProperty(key)));
/*  72:    */       }
/*  73:    */     }
/*  74:    */     catch (Exception e)
/*  75:    */     {
/*  76: 82 */       e.printStackTrace();
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Client()
/*  81:    */   {
/*  82: 88 */     init("default");
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void init(String dest)
/*  86:    */   {
/*  87: 93 */     Properties p = (Properties)dests.get(dest);
/*  88: 94 */     this.logger.info(dest);
/*  89: 95 */     this.type = p.getProperty("type");
/*  90: 96 */     if (this.type.equals("socket"))
/*  91:    */     {
/*  92: 98 */       this.server = p.getProperty("server");
/*  93: 99 */       this.sendPort = Integer.parseInt(p.getProperty("sendPort"));
/*  94:100 */       this.receivePort = Integer.parseInt(p.getProperty("receivePort"));
/*  95:    */     }
/*  96:102 */     else if (this.type.equals("URL"))
/*  97:    */     {
/*  98:104 */       this.receiveURL = p.getProperty("receiveUrl");
/*  99:105 */       this.sendURL = p.getProperty("sendUrl");
/* 100:106 */       this.repoURL = p.getProperty("repoUrl");
/* 101:107 */       this.path = p.getProperty("path");
/* 102:108 */       this.authUrl = p.getProperty("authUrl");
/* 103:109 */       this.md5 = p.getProperty("md5");
/* 104:110 */       this.logger.info(">>> Path " + this.path + " >> " + this.receiveURL + " >>" + this.sendURL);
/* 105:    */     }
/* 106:112 */     else if (this.type.equals("local"))
/* 107:    */     {
/* 108:114 */       this.mailboxPath = p.getProperty("mailboxPath");
/* 109:115 */       this.logger.info(">>> Mail box Path " + this.mailboxPath);
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void authenticate()
/* 114:    */     throws Exception
/* 115:    */   {
/* 116:122 */     System.out.println("Before  Client Authentication");
/* 117:123 */     KenJAASAuthenticator.run("Test2Login", 3);
/* 118:    */     
/* 119:125 */     this.sessionID = ((KenPrincipal)TestXUIDB.getInstance().getBean("ken-principal")).getSessionID();
/* 120:126 */     System.out.println("After  Client Authentication " + this.sessionID);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void call(URL url)
/* 124:    */   {
/* 125:    */     try
/* 126:    */     {
/* 127:133 */       File f = File.createTempFile("tt", "url");
/* 128:134 */       authenticate();
/* 129:135 */       MsgChannel mc = new MsgChannel();
/* 130:136 */       mc.sessionID = this.sessionID;
/* 131:137 */       mc.downloadFileFromURL(url, f, new StringBuffer());
/* 132:    */     }
/* 133:    */     catch (IOException e)
/* 134:    */     {
/* 135:141 */       e.printStackTrace();
/* 136:    */     }
/* 137:    */     catch (Exception e)
/* 138:    */     {
/* 139:144 */       e.printStackTrace();
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static Client getInstance(String dest)
/* 144:    */   {
/* 145:150 */     Client client = new Client();
/* 146:151 */     client.init(dest);
/* 147:152 */     return client;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public MsgChannel getMsgChannel(int mode)
/* 151:    */     throws Exception
/* 152:    */   {
/* 153:158 */     MsgChannel mc = null;
/* 154:159 */     if (this.type.equals("socket"))
/* 155:    */     {
/* 156:161 */       if (mode == 0)
/* 157:    */       {
/* 158:163 */         mc = new MsgChannel(this.server, this.sendPort, mode);
/* 159:164 */         mc.path = this.path;
/* 160:    */         
/* 161:166 */         return mc;
/* 162:    */       }
/* 163:169 */       mc = new MsgChannel(this.server, this.receivePort, mode);
/* 164:170 */       mc.path = this.path;
/* 165:171 */       return mc;
/* 166:    */     }
/* 167:174 */     if (this.type.equals("URL"))
/* 168:    */     {
/* 169:176 */       if (mode == 0)
/* 170:    */       {
/* 171:178 */         mc = new MsgChannel(new URL(this.sendURL), mode, this.path);
/* 172:    */         
/* 173:180 */         return mc;
/* 174:    */       }
/* 175:183 */       if (mode == 2)
/* 176:    */       {
/* 177:185 */         mc = new MsgChannel(new URL(this.repoURL), mode, this.path);
/* 178:186 */         mc.path = this.path;
/* 179:187 */         return mc;
/* 180:    */       }
/* 181:190 */       mc = new MsgChannel(new URL(this.receiveURL), mode, this.path);
/* 182:191 */       mc.path = this.path;
/* 183:192 */       return mc;
/* 184:    */     }
/* 185:195 */     if (this.type.equals("local"))
/* 186:    */     {
/* 187:197 */       mc = new MsgChannel(mode);
/* 188:    */       
/* 189:199 */       return mc;
/* 190:    */     }
/* 191:202 */     throw new Exception("Not Configured");
/* 192:    */   }
/* 193:    */   
/* 194:    */   
/* 195:    */    @Deprecated
/* 196:    */    
/* 197:    */   public void run2()
/* 198:    */     throws Exception
/* 199:    */   {
/* 200:209 */     this.logger.info("Connecting " + this.server);
/* 201:210 */     Socket sock = new Socket(this.server, this.receivePort);
/* 202:211 */     this.logger.info("Connected " + this.server);
/* 203:    */     
/* 204:213 */     ZipInputStream zin = new ZipInputStream(sock.getInputStream());
/* 205:    */     
/* 206:215 */     DataOutputStream dout = new DataOutputStream(sock.getOutputStream());
/* 207:216 */     DataInputStream in = new DataInputStream(zin);
/* 208:    */     
/* 209:218 */     dout.writeBytes("participant:" + this.participant + "\n");
/* 210:219 */     dout.writeBytes("team:12\n");
/* 211:220 */     dout.writeBytes("op:" + this.operation + "\n");
/* 212:    */     
/* 213:222 */     dout.writeBytes("\n");
/* 214:223 */     this.logger.info("Headers Complete");
/* 215:    */     try
/* 216:    */     {
/* 217:225 */       ZipEntry ze = zin.getNextEntry();
/* 218:226 */       this.logger.info(ze.getName());
/* 219:    */       
/* 220:228 */       String line = in.readLine();
/* 221:229 */       StringBuffer log = new StringBuffer();
/* 222:230 */       int count = 0;
/* 223:231 */       String acks = null;
/* 224:232 */       while ((ze != null) && (!ze.getName().equals("Complete")))
/* 225:    */       {
/* 226:234 */         count = 0;
/* 227:235 */         while (line != null)
/* 228:    */         {
/* 229:237 */           log.append(line);
/* 230:    */           
/* 231:239 */           line = in.readLine();
/* 232:240 */           count++;
/* 233:    */         }
/* 234:243 */         this.logger.info("Lines " + count + " complete");
/* 235:244 */         FileWriter fw = new FileWriter(ze.getName());
/* 236:    */         
/* 237:246 */         fw.write(log.toString());
/* 238:    */         
/* 239:248 */         fw.close();
/* 240:249 */         acks = acks + "," + ze.getName();
/* 241:    */         
/* 242:251 */         this.logger.info("After file");
/* 243:252 */         if (ze.getName().endsWith(".xml"))
/* 244:    */         {
/* 245:254 */           TestXUIDB.getInstance().importChangeLogs(log.toString());
/* 246:    */         }
/* 247:    */         else
/* 248:    */         {
/* 249:258 */           FileWriter fw1 = new FileWriter(ze.getName());
/* 250:    */           
/* 251:260 */           fw1.write(log.toString());
/* 252:    */           
/* 253:262 */           fw1.close();
/* 254:    */           
/* 255:264 */           String fname = ze.getName().substring(ze.getName().lastIndexOf(".txt"));
/* 256:    */           
/* 257:266 */           Base64FileDecoder.decodeFile(ze.getName(), fname);
/* 258:    */         }
/* 259:269 */         this.logger.info("After importing");
/* 260:270 */         log = new StringBuffer();
/* 261:271 */         ze = zin.getNextEntry();
/* 262:272 */         line = "";
/* 263:    */         
/* 264:274 */         this.logger.info(" Next " + ze.getName());
/* 265:275 */         if (ze.getName().equals("Complete")) {
/* 266:    */           break;
/* 267:    */         }
/* 268:    */       }
/* 269:279 */       dout.writeBytes("Success:\n");
/* 270:    */     }
/* 271:    */     catch (Exception e)
/* 272:    */     {
/* 273:283 */       e.printStackTrace();
/* 274:284 */       dout.writeBytes("Failure:Could not import messages");
/* 275:    */     }
/* 276:    */   }
/* 277:    */   
/* 278:    */   public void sendAck(String acks)
/* 279:    */     throws Exception
/* 280:    */   {
/* 281:291 */     this.logger.info("Connecting " + this.server);
/* 282:    */     
/* 283:293 */     this.logger.info("Connected " + this.server);
/* 284:    */     
/* 285:295 */     authenticate();
/* 286:    */     
/* 287:297 */     MsgChannel mc1 = getMsgChannel(1);
/* 288:298 */     mc1.sessionID = this.sessionID;
/* 289:299 */     mc1.headers.append("participant:" + this.participant + "\n");
/* 290:300 */     mc1.headers.append("team:12\n");
/* 291:301 */     mc1.headers.append("op:acknowledge\n");
/* 292:302 */     mc1.headers.append("downloads:" + acks + "\n");
/* 293:    */     
/* 294:304 */     mc1.headers.append("\n");
/* 295:305 */     System.out.println(" sent acks:" + acks);
/* 296:306 */     mc1.start();
/* 297:    */   }
/* 298:    */   
/* 299:    */   public void run()
/* 300:    */     throws Exception
/* 301:    */   {
/* 302:312 */     this.logger.info("Connecting " + this.server);
/* 303:    */     
/* 304:314 */     this.logger.info("Connected " + this.server);
/* 305:    */     
/* 306:316 */     authenticate();
/* 307:    */     
/* 308:318 */     this.mc = getMsgChannel(1);
/* 309:319 */     this.mc.sessionID = this.sessionID;
/* 310:320 */     this.mc.headers.append("participant:" + this.participant + "\n");
/* 311:321 */     this.mc.headers.append("team:12\n");
/* 312:322 */     this.mc.headers.append("op:" + this.operation + "\n");
/* 313:    */     
/* 314:324 */     this.mc.headers.append("\n");
/* 315:    */     
/* 316:326 */     this.logger.info("Headers Complete");
/* 317:327 */     String acks = null;
/* 318:328 */     int imports = 0;
/* 319:329 */     String zipName = this.mc.start();
/* 320:330 */     if (zipName != null) {
/* 321:332 */       TestXUIDB.getInstance().logImport(zipName, "processing", "");
/* 322:    */     }
/* 323:    */     try
/* 324:    */     {
/* 325:336 */       this.mc.getNextEntry();
/* 326:337 */       this.logger.info(this.mc.getName());
/* 327:    */       
/* 328:339 */       String line = this.mc.readLine();
/* 329:340 */       StringBuffer log = new StringBuffer();
/* 330:341 */       int count = 0;
/* 331:343 */       while ((this.mc != null) && (!this.mc.getName().equals("Complete")))
/* 332:    */       {
/* 333:345 */         if (acks == null) {
/* 334:347 */           acks = this.mc.getName();
/* 335:    */         } else {
/* 336:350 */           acks = acks + "," + this.mc.getName();
/* 337:    */         }
/* 338:351 */         count = 0;
/* 339:352 */         while (line != null)
/* 340:    */         {
/* 341:354 */           log.append(line);
/* 342:    */           
/* 343:356 */           line = this.mc.readLine();
/* 344:357 */           count++;
/* 345:    */         }
/* 346:360 */         this.logger.info("Lines " + count + " complete");
/* 347:    */         
/* 348:362 */         this.logger.info("After file");
/* 349:363 */         if (this.mc.getName().endsWith(".xml"))
/* 350:    */         {
/* 351:365 */           this.logger.info("After file" + this.path + "/" + this.mc.getName());
/* 352:366 */           FileWriter fw1 = new FileWriter(this.path + "/" + this.mc.getName());
/* 353:    */           
/* 354:368 */           fw1.write(log.toString());
/* 355:    */           
/* 356:370 */           fw1.close();
/* 357:    */           
/* 358:372 */           TestXUIDB.getInstance().importChangeLogs1(this.path + "/" + this.mc.getName());
/* 359:373 */           this.messages.add("imported changelog");
/* 360:374 */           imports++;
/* 361:375 */           Logger logger = Logger.getLogger(getClass());
/* 362:376 */           logger.info(" Before Deleting " + this.path + "/" + this.mc.getName());
/* 363:    */           
/* 364:378 */           File f = new File(this.path + "/" + this.mc.getName());
/* 365:379 */           if (f.exists())
/* 366:    */           {
/* 367:381 */             logger.info(" File found ");
/* 368:382 */             f.delete();
/* 369:383 */             logger.info(" After Delete");
/* 370:    */           }
/* 371:    */         }
/* 372:    */         else
/* 373:    */         {
/* 374:389 */           this.logger.info("After file" + this.path + "/" + this.mc.getName());
/* 375:390 */           FileWriter fw1 = new FileWriter(this.path + "/" + this.mc.getName());
/* 376:    */           
/* 377:392 */           fw1.write(log.toString());
/* 378:    */           
/* 379:394 */           fw1.close();
/* 380:    */           
/* 381:396 */           String fname = "./images/" + this.mc.getName().substring(0, this.mc.getName().lastIndexOf(".txt"));
/* 382:397 */           this.logger.info("After file --" + fname);
/* 383:398 */           Base64FileDecoder.decodeFile(this.path + "/" + this.mc.getName(), fname);
/* 384:399 */           this.messages.add("imported resource " + fname);
/* 385:400 */           imports++;
/* 386:401 */           File f = new File(this.path + "/" + this.mc.getName());
/* 387:402 */           if (f.exists()) {
/* 388:403 */             f.delete();
/* 389:    */           }
/* 390:    */         }
/* 391:408 */         this.logger.info("After importing");
/* 392:409 */         log = new StringBuffer();
/* 393:410 */         this.mc.getNextEntry();
/* 394:411 */         line = "";
/* 395:    */         
/* 396:413 */         this.logger.info(" Next " + this.mc.getName());
/* 397:414 */         if (this.mc.getName().equals("Complete")) {
/* 398:    */           break;
/* 399:    */         }
/* 400:    */       }
/* 401:417 */       sendAck(acks);
/* 402:418 */       this.mc.writeBytes("Success:\n");
/* 403:420 */       if (zipName != null)
/* 404:    */       {
/* 405:422 */         TestXUIDB.getInstance().logImport(zipName, "complete", "");
/* 406:423 */         File f = new File(zipName);
/* 407:424 */         Logger logger = Logger.getLogger(getClass());
/* 408:425 */         logger.info(" Before Deleting " + zipName);
/* 409:426 */         if (f.exists())
/* 410:    */         {
/* 411:428 */           logger.info("File exists");
/* 412:429 */           f.delete();
/* 413:430 */           logger.info("File deleted");
/* 414:    */         }
/* 415:    */       }
/* 416:433 */       this.messages.add("Total imports " + imports);
/* 417:    */     }
/* 418:    */     catch (Exception e)
/* 419:    */     {
/* 420:437 */       e.printStackTrace();
/* 421:438 */       this.mc.writeBytes("Failure:Could not import messages");
/* 422:439 */       this.messages.add("Failure:Could not import messages " + e.getMessage());
/* 423:    */     }
/* 424:    */   }
/* 425:    */   
/* 426:    */   public void reRun(String zipName)
/* 427:    */     throws Exception
/* 428:    */   {
/* 429:445 */     this.logger.info("Connecting " + this.server);
/* 430:    */     
/* 431:447 */     this.logger.info("Connected " + this.server);
/* 432:    */     
/* 433:449 */     this.mc = getMsgChannel(1);
/* 434:450 */     this.mc.sessionID = this.sessionID;
/* 435:451 */     this.mc.headers.append("participant:" + this.participant + "\n");
/* 436:452 */     this.mc.headers.append("team:12\n");
/* 437:453 */     this.mc.headers.append("op:" + this.operation + "\n");
/* 438:    */     
/* 439:455 */     this.mc.headers.append("\n");
/* 440:    */     
/* 441:457 */     this.logger.info("Headers Complete");
/* 442:458 */     int imports = 0;
/* 443:459 */     this.mc.restart(zipName);
/* 444:460 */     if (zipName != null) {
/* 445:462 */       TestXUIDB.getInstance().logImport(zipName, "processing", "");
/* 446:    */     }
/* 447:    */     try
/* 448:    */     {
/* 449:466 */       this.mc.getNextEntry();
/* 450:467 */       this.logger.info(this.mc.getName());
/* 451:    */       
/* 452:469 */       String line = this.mc.readLine();
/* 453:470 */       StringBuffer log = new StringBuffer();
/* 454:471 */       int count = 0;
/* 455:473 */       while ((this.mc != null) && (!this.mc.getName().equals("Complete")))
/* 456:    */       {
/* 457:475 */         count = 0;
/* 458:476 */         while (line != null)
/* 459:    */         {
/* 460:478 */           log.append(line);
/* 461:    */           
/* 462:480 */           line = this.mc.readLine();
/* 463:481 */           count++;
/* 464:    */         }
/* 465:484 */         this.logger.info("Lines " + count + " complete");
/* 466:485 */         FileWriter fw = new FileWriter(this.mc.getName());
/* 467:    */         
/* 468:487 */         fw.write(log.toString());
/* 469:    */         
/* 470:489 */         fw.close();
/* 471:    */         
/* 472:491 */         this.logger.info("After file");
/* 473:492 */         if (this.mc.getName().endsWith(".xml"))
/* 474:    */         {
/* 475:494 */           TestXUIDB.getInstance().importChangeLogs1(this.mc.getName());
/* 476:495 */           this.messages.add("imported changelog");
/* 477:496 */           imports++;
/* 478:    */         }
/* 479:    */         else
/* 480:    */         {
/* 481:500 */           this.logger.info("After file" + this.path + "/" + this.mc.getName());
/* 482:501 */           FileWriter fw1 = new FileWriter(this.path + "/" + this.mc.getName());
/* 483:    */           
/* 484:503 */           fw1.write(log.toString());
/* 485:    */           
/* 486:505 */           fw1.close();
/* 487:    */           
/* 488:507 */           String fname = "./images/" + this.mc.getName().substring(0, this.mc.getName().lastIndexOf(".txt"));
/* 489:508 */           this.logger.info("After file --" + fname);
/* 490:509 */           Base64FileDecoder.decodeFile(this.path + "/" + this.mc.getName(), fname);
/* 491:510 */           this.messages.add("imported resource " + fname);
/* 492:511 */           imports++;
/* 493:    */         }
/* 494:514 */         this.logger.info("After importing");
/* 495:515 */         log = new StringBuffer();
/* 496:516 */         this.mc.getNextEntry();
/* 497:517 */         line = "";
/* 498:    */         
/* 499:519 */         this.logger.info(" Next " + this.mc.getName());
/* 500:520 */         if (this.mc.getName().equals("Complete")) {
/* 501:    */           break;
/* 502:    */         }
/* 503:    */       }
/* 504:523 */       this.mc.writeBytes("Success:\n");
/* 505:524 */       this.messages.add("Total imports " + imports);
/* 506:525 */       if (zipName != null) {
/* 507:526 */         TestXUIDB.getInstance().logImport(zipName, "complete", "");
/* 508:    */       }
/* 509:527 */       File f = new File(zipName);
/* 510:528 */       if (f.exists()) {
/* 511:529 */         f.delete();
/* 512:    */       }
/* 513:    */     }
/* 514:    */     catch (Exception e)
/* 515:    */     {
/* 516:534 */       e.printStackTrace();
/* 517:535 */       this.mc.writeBytes("Failure:Could not import messages");
/* 518:    */     }
/* 519:    */   }
/* 520:    */   
/* 521:    */   public int runRepoSync()
/* 522:    */     throws Exception
/* 523:    */   {
/* 524:539 */     this.logger.info("Connecting " + this.server);
/* 525:540 */     authenticate();
/* 526:    */     
/* 527:542 */     String path1 = ".";
/* 528:    */     
/* 529:544 */     this.logger.info("Connected " + this.server);
/* 530:545 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 531:546 */     String dt1 = "1990-01-01";
/* 532:547 */     if (new File(path1 + "/synclog.txt").exists())
/* 533:    */     {
/* 534:549 */       BufferedReader fr = new BufferedReader(new FileReader(path1 + "/synclog.txt"));
/* 535:550 */       dt1 = fr.readLine();
/* 536:551 */       fr.close();
/* 537:    */     }
/* 538:553 */     this.logger.info(" DT " + dt1);
/* 539:    */     
/* 540:555 */     this.mc = getMsgChannel(2);
/* 541:556 */     this.mc.sessionID = this.sessionID;
/* 542:    */     
/* 543:558 */     this.mc.headers.append("participant:" + this.participant + "\n");
/* 544:559 */     this.mc.headers.append("team:12\n");
/* 545:560 */     this.mc.headers.append("op:" + this.operation + "\n");
/* 546:561 */     this.mc.headers.append("lastsynctime:" + dt1.trim() + "\n");
/* 547:    */     
/* 548:563 */     this.mc.headers.append("\n");
/* 549:    */     
/* 550:565 */     this.logger.info("Headers Complete");
/* 551:566 */     int imports = 0;
/* 552:567 */     this.mc.start();
/* 553:    */     try
/* 554:    */     {
/* 555:570 */       this.mc.getNextEntry();
/* 556:571 */       this.logger.info(this.mc.getName());
/* 557:    */       
/* 558:573 */       String line = this.mc.readLine();
/* 559:574 */       StringBuffer log = new StringBuffer();
/* 560:575 */       int count = 0;
/* 561:577 */       while ((this.mc != null) && (!this.mc.getName().equals("Complete")))
/* 562:    */       {
/* 563:579 */         count = 0;
/* 564:580 */         while (line != null)
/* 565:    */         {
/* 566:582 */           log.append(line);
/* 567:    */           
/* 568:584 */           line = this.mc.readLine();
/* 569:585 */           count++;
/* 570:    */         }
/* 571:588 */         this.logger.info("Lines " + count + " complete");
/* 572:    */         
/* 573:590 */         this.logger.info("After file");
/* 574:    */         
/* 575:592 */         this.logger.info("After file" + this.path + "/" + this.mc.getName());
/* 576:593 */         String tmpfile = this.path + "/" + this.mc.getName();
/* 577:594 */         String testpath1 = tmpfile.substring(0, tmpfile.lastIndexOf("/"));
/* 578:595 */         File testdir = new File(testpath1);
/* 579:596 */         if (!testdir.exists())
/* 580:    */         {
/* 581:598 */           this.logger.info("Creating dir " + testdir);
/* 582:599 */           testdir.mkdirs();
/* 583:    */         }
/* 584:602 */         FileWriter fw1 = new FileWriter(tmpfile);
/* 585:    */         
/* 586:604 */         fw1.write(log.toString());
/* 587:    */         
/* 588:606 */         fw1.close();
/* 589:    */         
/* 590:608 */         String fname = path1 + "/" + this.mc.getName().substring(0, this.mc.getName().lastIndexOf(".txt"));
/* 591:609 */         testpath1 = fname.substring(0, fname.lastIndexOf("/"));
/* 592:610 */         testdir = new File(testpath1);
/* 593:611 */         if (!testdir.exists())
/* 594:    */         {
/* 595:613 */           this.logger.info("Creating dir " + testdir);
/* 596:614 */           testdir.mkdirs();
/* 597:    */         }
/* 598:616 */         this.logger.info("After file --" + fname);
/* 599:617 */         Base64FileDecoder.decodeFile(tmpfile, fname);
/* 600:618 */         this.logger.info("Deleteing --" + tmpfile);
/* 601:619 */         new File(tmpfile).delete();
/* 602:620 */         this.messages.add("imported resource " + fname);
/* 603:621 */         imports++;
/* 604:    */         
/* 605:623 */         this.logger.info("After importing");
/* 606:624 */         log = new StringBuffer();
/* 607:625 */         this.mc.getNextEntry();
/* 608:626 */         line = "";
/* 609:    */         
/* 610:628 */         this.logger.info(" Next " + this.mc.getName());
/* 611:629 */         if (this.mc.getName().equals("Complete")) {
/* 612:    */           break;
/* 613:    */         }
/* 614:    */       }
/* 615:632 */       if (imports > 0)
/* 616:    */       {
/* 617:634 */         FileWriter fw = new FileWriter(path1 + "/synclog.txt");
/* 618:    */         
/* 619:636 */         fw.write(sdf.format(new Date()));
/* 620:637 */         fw.close();
/* 621:    */       }
/* 622:640 */       this.mc.writeBytes("Success:\n");
/* 623:641 */       this.messages.add("Total imports " + imports);
/* 624:642 */       return imports;
/* 625:    */     }
/* 626:    */     catch (Exception e)
/* 627:    */     {
/* 628:646 */       e.printStackTrace();
/* 629:647 */       this.mc.writeBytes("Failure:Could not import messages");
/* 630:    */     }
/* 631:649 */     return imports;
/* 632:    */   }
/* 633:    */   
/* 634:    */   public synchronized void run(Vector logs, String recepients)
/* 635:    */     throws Exception
/* 636:    */   {
/* 637:655 */     this.logger.info(">>Type  " + this.type);
/* 638:656 */     if ((this.type != null) && (this.type.equals("local")))
/* 639:    */     {
/* 640:658 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SS");
/* 641:659 */       String fname1 = "admin-" + sdf.format(new Date()) + "-received.xml";
/* 642:660 */       this.logger.info(">>Filename   " + this.mailboxPath + "/" + fname1);
/* 643:661 */       DataOutputStream dout = new DataOutputStream(new FileOutputStream(this.mailboxPath + "/" + fname1));
/* 644:662 */       dout.writeBytes("<logs>\n");
/* 645:663 */       for (int i = 0; i < logs.size(); i++) {
/* 646:665 */         dout.writeBytes(logs.get(i) + "\n");
/* 647:    */       }
/* 648:668 */       dout.writeBytes("</logs>\n");
/* 649:669 */       dout.close();
/* 650:    */       
/* 651:671 */       TestXUIDB.getInstance().deliverMessage(fname1, recepients);
/* 652:672 */       return;
/* 653:    */     }
/* 654:674 */     authenticate();
/* 655:675 */     MsgChannel mc = getMsgChannel(0);
/* 656:676 */     mc.sessionID = this.sessionID;
/* 657:    */     
/* 658:678 */     mc.putNextEntry("test");
/* 659:    */     
/* 660:680 */     mc.writeBytes("participant:" + this.participant + "\n");
/* 661:681 */     mc.writeBytes("team:12\n");
/* 662:682 */     mc.writeBytes("recepients:" + recepients + "\n");
/* 663:683 */     mc.writeBytes("op:" + this.operation + "\n");
/* 664:684 */     mc.writeBytes("msg-type:" + this.msgType + "\n");
/* 665:685 */     mc.writeBytes("\n");
/* 666:686 */     this.logger.info("Headers complete");
/* 667:687 */     for (int i = 0; i < logs.size(); i++) {
/* 668:689 */       mc.writeBytes(logs.get(i) + "\n");
/* 669:    */     }
/* 670:692 */     this.logger.info("Completed0");
/* 671:    */     
/* 672:694 */     mc.closeEntry();mc.complete();
/* 673:    */     
/* 674:696 */     String ret = mc.readLine();
/* 675:697 */     this.logger.info("Completed " + ret);
/* 676:698 */     if ((ret == null) || (!ret.startsWith("Success"))) {
/* 677:700 */       throw new Exception("Failed to complete upload .Server returned " + ret);
/* 678:    */     }
/* 679:    */     try
/* 680:    */     {
/* 681:705 */       mc.close();
/* 682:    */     }
/* 683:    */     catch (Exception e)
/* 684:    */     {
/* 685:709 */       e.printStackTrace();
/* 686:    */     }
/* 687:    */   }
/* 688:    */   
/* 689:    */   public void run(String resource, String name, String recepients)
/* 690:    */     throws Exception
/* 691:    */   {
/* 692:716 */     if ((this.type != null) && (this.type.equals("local")))
/* 693:    */     {
/* 694:718 */       if (!new File(this.mailboxPath + "/" + name + ".txt").exists()) {
/* 695:720 */         Base64FileEncoder.encodeFile(resource, this.mailboxPath + "/" + name + ".txt");
/* 696:    */       }
/* 697:722 */       TestXUIDB.getInstance().deliverMessage(name + ".txt", recepients);
/* 698:    */       
/* 699:724 */       return;
/* 700:    */     }
/* 701:727 */     authenticate();
/* 702:728 */     Base64FileEncoder.encodeFile(resource, resource + ".txt");
/* 703:729 */     MsgChannel mc = getMsgChannel(0);
/* 704:730 */     mc.sessionID = this.sessionID;
/* 705:731 */     mc.zip = false;
/* 706:    */     
/* 707:733 */     mc.putNextEntry(name);
/* 708:    */     
/* 709:735 */     mc.writeBytes("participant:" + this.participant + "\n");
/* 710:736 */     mc.writeBytes("team:12\n");
/* 711:737 */     mc.writeBytes("recepients:" + recepients + "\n");
/* 712:738 */     mc.writeBytes("op:" + this.operation + "\n");
/* 713:739 */     mc.writeBytes("msg-type:resource\n");
/* 714:740 */     mc.writeBytes("\n");
/* 715:741 */     this.logger.info("Headers complete");
/* 716:    */     
/* 717:743 */     BufferedReader fr = new BufferedReader(new FileReader(resource + ".txt"));
/* 718:744 */     String line = fr.readLine();
/* 719:746 */     while (line != null)
/* 720:    */     {
/* 721:748 */       mc.writeBytes(line + "\n");
/* 722:749 */       line = fr.readLine();
/* 723:    */     }
/* 724:752 */     this.logger.info("Completed0");
/* 725:    */     
/* 726:754 */     mc.closeEntry();
/* 727:755 */     mc.complete();
/* 728:    */     
/* 729:757 */     mc.close();
/* 730:    */   }
/* 731:    */   
/* 732:    */   
/* 733:    */    @Deprecated
/* 734:    */    
/* 735:    */   public void run1(String resource, String name, String recepients)
/* 736:    */     throws Exception
/* 737:    */   {
/* 738:764 */     MsgChannel mc = new MsgChannel(this.server, this.sendPort, 0);
/* 739:    */     
/* 740:766 */     mc.putNextEntry(name);
/* 741:767 */     Base64FileEncoder.encodeFile(resource, resource + ".txt");
/* 742:    */     
/* 743:769 */     mc.writeBytes("participant:" + this.participant + "\n");
/* 744:770 */     mc.writeBytes("team:12\n");
/* 745:771 */     mc.writeBytes("recepients:" + recepients + "\n");
/* 746:772 */     mc.writeBytes("op:" + this.operation + "\n");
/* 747:773 */     mc.writeBytes("msg-type:resource\n");
/* 748:774 */     mc.writeBytes("\n");
/* 749:775 */     this.logger.info("Headers complete");
/* 750:    */     
/* 751:777 */     BufferedReader fr = new BufferedReader(new FileReader(resource + ".txt"));
/* 752:778 */     String line = fr.readLine();
/* 753:780 */     while (line != null)
/* 754:    */     {
/* 755:782 */       mc.writeBytes(line + "\n");
/* 756:783 */       line = fr.readLine();
/* 757:    */     }
/* 758:786 */     this.logger.info("Completed0");
/* 759:    */     
/* 760:788 */     mc.closeEntry();
/* 761:    */     
/* 762:790 */     mc.close();
/* 763:    */   }
/* 764:    */   
/* 765:    */   public static void main(String[] args)
/* 766:    */     throws Exception
/* 767:    */   {
/* 768:796 */     String test = "sunset.jpg.txt";
/* 769:    */     
/* 770:798 */     Client cl = new Client();
/* 771:799 */     cl.path = "d:/tmp";
/* 772:800 */     cl.run("D:/test/Sunset.jpg", "sunset.jpg", "12");
/* 773:801 */     cl.path = "d:/tmp";
/* 774:802 */     cl.participant = "381";
/* 775:    */   }
/* 776:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.Client
 * JD-Core Version:    0.7.0.1
 */