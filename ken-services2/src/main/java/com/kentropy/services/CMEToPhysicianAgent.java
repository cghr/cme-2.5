/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileReader;
/*   6:    */ import java.io.FileWriter;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.io.PrintWriter;
/*  10:    */ import java.io.StringWriter;
/*  11:    */ import java.nio.file.CopyOption;
/*  12:    */ import java.nio.file.FileSystem;
/*  13:    */ import java.nio.file.FileSystems;
/*  14:    */ import java.nio.file.Files;
/*  15:    */ import java.nio.file.Path;
/*  16:    */ import java.nio.file.StandardCopyOption;
/*  17:    */ import java.text.SimpleDateFormat;
/*  18:    */ import java.util.Calendar;
/*  19:    */ import java.util.Date;
/*  20:    */ import java.util.Properties;
/*  21:    */ import java.util.Vector;
/*  22:    */ import javax.mail.Message.RecipientType;
/*  23:    */ import javax.mail.MessagingException;
/*  24:    */ import javax.mail.Session;
/*  25:    */ import javax.mail.Transport;
/*  26:    */ import javax.mail.internet.AddressException;
/*  27:    */ import javax.mail.internet.InternetAddress;
/*  28:    */ import javax.mail.internet.MimeMessage;
/*  29:    */ import net.xoetrope.data.XDataSource;
/*  30:    */ import net.xoetrope.xml.XmlElement;
/*  31:    */ import net.xoetrope.xml.XmlSource;
/*  32:    */ import net.xoetrope.xui.data.XBaseModel;
/*  33:    */ import net.xoetrope.xui.data.XModel;
/*  34:    */ 
/*  35:    */ public class CMEToPhysicianAgent
/*  36:    */   implements Runnable
/*  37:    */ {
/*  38: 43 */   public String phyURL = "http://192.168.1.102:8080/cme2-test/";
/*  39: 45 */   public String cmeURL = "http://192.168.1.102:8080/cme/";
/*  40: 47 */   String lastSyncTime = "2013-05-01";
/*  41: 48 */   public String path = ".";
/*  42:    */   private XModel tasksM;
/*  43:    */   String smtpHost;
/*  44:    */   String supportEmail;
/*  45:    */   private String smtpUserName;
/*  46:    */   private String smtpPassword;
/*  47:    */   Properties props;
/*  48:    */   private String lastSyncTime1;
/*  49:    */   
/*  50:    */   public static void main(String[] args)
/*  51:    */     throws Exception
/*  52:    */   {
/*  53: 59 */     Properties p = new Properties();
/*  54: 60 */     p.load(new FileReader("cme-phy-agent.properties"));
/*  55:    */     
/*  56:    */ 
/*  57:    */ 
/*  58: 64 */     new CMEToPhysicianAgent(p).run();
/*  59:    */   }
/*  60:    */   
/*  61:    */   public CMEToPhysicianAgent(Properties p)
/*  62:    */   {
/*  63: 77 */     this.cmeURL = p.getProperty("cmeURL");
/*  64: 78 */     this.phyURL = p.getProperty("phyURL");
/*  65: 79 */     this.smtpHost = p.getProperty("smtpHost");
/*  66: 80 */     this.supportEmail = p.getProperty("supportEmail");
/*  67: 81 */     this.smtpUserName = p.getProperty("smtpUsername");
/*  68: 82 */     this.smtpPassword = p.getProperty("smtpPassword");
/*  69: 83 */     this.props = p;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void deliver(String msg, String agent)
/*  73:    */     throws IOException
/*  74:    */   {
/*  75: 90 */     File file = new File(msg);
/*  76: 91 */     Path src = FileSystems.getDefault().getPath("cme", new String[] { "out", msg });
/*  77: 92 */     Path dest = FileSystems.getDefault().getPath(agent, new String[] { "in", msg });
/*  78: 93 */     Files.copy(src, dest, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void processImports()
/*  82:    */     throws Exception
/*  83:    */   {
/*  84:104 */     File dir = new File("physician/in");
/*  85:105 */     File[] files = dir.listFiles();
/*  86:106 */     for (int i = 0; i < files.length; i++)
/*  87:    */     {
/*  88:108 */       process(files[i]);
/*  89:109 */       files[i].renameTo(new File("physician/processed/" + files[i].getName()));
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   private void process(File file)
/*  94:    */     throws Exception
/*  95:    */   {
/*  96:120 */     FileReader sr = new FileReader(file);
/*  97:    */     
/*  98:122 */     XmlElement xe = XmlSource.read(sr);
/*  99:123 */     sr.close();
/* 100:124 */     System.out.println(xe);
/* 101:125 */     XDataSource xd = new XDataSource();
/* 102:126 */     XModel container = new XBaseModel();
/* 103:127 */     xd.loadTable(xe, container);
/* 104:128 */     StringWriter sw = new StringWriter();
/* 105:129 */     System.out.println(" Model in " + file);
/* 106:130 */     XDataSource.outputModel(sw, container);
/* 107:131 */     System.out.println(sw);
/* 108:132 */     for (int i = 0; i < container.getNumChildren(); i++)
/* 109:    */     {
/* 110:134 */       XModel xm = container.get(i);
/* 111:135 */       if (xm.getId().equals("tasks")) {
/* 112:137 */         this.tasksM = xm;
/* 113:    */       } else {
/* 114:141 */         processData(xm);
/* 115:    */       }
/* 116:    */     }
/* 117:145 */     processTasks(this.tasksM);
/* 118:    */   }
/* 119:    */   
/* 120:    */   private void processData(XModel xm)
/* 121:    */     throws Exception
/* 122:    */   {
/* 123:156 */     System.out.println(xm.getId() + " " + xm.get("@matchfield"));
/* 124:157 */     if (xm.getNumChildren() > 0) {
/* 125:158 */       new IntegrationService().write(this.phyURL, xm.getId(), xm.get("@matchfield").toString(), xm);
/* 126:    */     }
/* 127:    */   }
/* 128:    */   
/* 129:    */   private void processTasks(XModel xm)
/* 130:    */     throws Exception
/* 131:    */   {
/* 132:166 */     processData(xm);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setRefreshProcess(XModel xm, String pidFld)
/* 136:    */   {
/* 137:171 */     for (int i = 0; i < xm.getNumChildren(); i++) {
/* 138:174 */       for (int j = 0; j < xm.get(i).getNumChildren(); j++) {
/* 139:176 */         xm.get(i).get(j).set("@refreshprocess", xm.get(i).get(j).get(pidFld + "/@value"));
/* 140:    */       }
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   public void getLastSyncTime()
/* 145:    */   {
/* 146:215 */     System.out.println(" Process >>>>>");
/* 147:    */     try
/* 148:    */     {
/* 149:217 */       FileInputStream fr = new FileInputStream("cme-phy-lastsynctime");
/* 150:218 */       Properties p = new Properties();
/* 151:219 */       p.load(fr);
/* 152:    */       
/* 153:    */ 
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:    */ 
/* 158:    */ 
/* 159:227 */       this.lastSyncTime = p.getProperty("lastSyncTime-cme");
/* 160:228 */       this.lastSyncTime1 = p.getProperty("lastSyncTime-phy");
/* 161:229 */       Calendar cal = Calendar.getInstance();
/* 162:230 */       cal.add(5, -1);
/* 163:231 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 164:232 */       this.yesterday = sdf.format(cal.getTime());
/* 165:    */     }
/* 166:    */     catch (Exception e)
/* 167:    */     {
/* 168:237 */       e.printStackTrace();
/* 169:238 */       this.lastSyncTime = "2012-10-01 00:00:00";
/* 170:239 */       this.lastSyncTime1 = "2012-10-01 00:00:00";
/* 171:240 */       this.yesterday = this.lastSyncTime1;
/* 172:    */     }
/* 173:243 */     if (this.lastSyncTime == null) {
/* 174:244 */       this.lastSyncTime = "2012-10-01 00:00:00";
/* 175:    */     }
/* 176:245 */     if (this.lastSyncTime1 == null)
/* 177:    */     {
/* 178:247 */       this.lastSyncTime1 = "2012-10-01 00:00:00";
/* 179:248 */       this.yesterday = this.lastSyncTime1;
/* 180:    */     }
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void sendMessage(String to, String subject, String text)
/* 184:    */     throws AddressException, MessagingException
/* 185:    */   {
/* 186:262 */     Session session = Session.getDefaultInstance(this.props);
/* 187:    */     
/* 188:264 */     MimeMessage message = new MimeMessage(session);
/* 189:    */     
/* 190:    */ 
/* 191:267 */     message.setFrom(new InternetAddress(this.supportEmail));
/* 192:    */     
/* 193:    */ 
/* 194:270 */     message.addRecipient(Message.RecipientType.TO, 
/* 195:271 */       new InternetAddress(this.supportEmail));
/* 196:272 */     message.setSubject(subject);
/* 197:273 */     message.setText(text);
/* 198:274 */     Transport tr = session.getTransport("smtp");
/* 199:275 */     tr.connect(this.smtpHost, this.smtpUserName, this.smtpPassword);
/* 200:276 */     message.saveChanges();
/* 201:277 */     tr.sendMessage(message, message.getAllRecipients());
/* 202:278 */     tr.close();
/* 203:    */   }
/* 204:    */   
/* 205:    */   public String getTime(String serverURL)
/* 206:    */     throws Exception
/* 207:    */   {
/* 208:284 */     XModel xm = new IntegrationService().readTable(serverURL, " accounts  ", " NOW() time1", " true limit 1 ");
/* 209:    */     
/* 210:286 */     return (String)xm.get(0).get(0).get(0).get();
/* 211:    */   }
/* 212:    */   
/* 213:288 */   public String curSyncTime = "";
/* 214:289 */   public String curSyncTime1 = "";
/* 215:    */   public static final double DUMMY_OFFSET = -1000000.0D;
/* 216:291 */   public double timeDiff = -1000000.0D;
/* 217:292 */   double offset1 = -1000000.0D;
/* 218:293 */   double offset2 = -1000000.0D;
/* 219:294 */   private String sntpServer = "0.uk.pool.ntp.org";
/* 220:    */   String yesterday;
/* 221:    */   
/* 222:    */   public void getCurSyncTime()
/* 223:    */     throws Exception
/* 224:    */   {
/* 225:299 */     this.curSyncTime = getTime(this.cmeURL);
/* 226:300 */     this.curSyncTime1 = getTime(this.phyURL);
/* 227:301 */     SntpClient sntpc = new SntpClient();
/* 228:    */     
/* 229:303 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 230:304 */     Date dt1 = sdf.parse(this.curSyncTime);
/* 231:305 */     Date dt2 = sdf.parse(this.curSyncTime1);
/* 232:306 */     long td = dt1.getTime() - dt2.getTime();
/* 233:333 */     if (this.timeDiff != -1000000.0D)
/* 234:    */     {
/* 235:335 */       if (Math.abs(this.timeDiff - td) > 1000.0D) {
/* 236:336 */         throw new Exception(" Dates have changed");
/* 237:    */       }
/* 238:    */     }
/* 239:    */     else {
/* 240:341 */       this.timeDiff = td;
/* 241:    */     }
/* 242:    */   }
/* 243:    */   
/* 244:    */   public void sync()
/* 245:    */     throws Exception
/* 246:    */   {
/* 247:351 */     System.out.println(" Process >>>>>");
/* 248:352 */     getLastSyncTime();
/* 249:    */     
/* 250:354 */     getCurSyncTime();
/* 251:    */     
/* 252:    */ 
/* 253:    */ 
/* 254:358 */     XModel container1 = new XBaseModel();
/* 255:359 */     container1.set("@src", this.cmeURL);
/* 256:360 */     container1.set("@dest", this.phyURL);
/* 257:361 */     IntegrationService is = new IntegrationService();
/* 258:362 */     is.readTaskAndDataAssigned(this.cmeURL, this.lastSyncTime, this.curSyncTime, container1);
/* 259:363 */     is.readBillable(this.cmeURL, this.lastSyncTime, this.curSyncTime, container1, null);
/* 260:364 */     is.readCMEReport(this.cmeURL, this.lastSyncTime, this.curSyncTime, container1, null);
/* 261:365 */     is.readCancellations(this.cmeURL, this.lastSyncTime, this.curSyncTime, container1);
/* 262:    */     
/* 263:367 */     XModel container2 = new XBaseModel();
/* 264:368 */     container2.set("@src", this.phyURL);
/* 265:369 */     container2.set("@dest", this.cmeURL);
/* 266:370 */     XModel container3 = new XBaseModel();
/* 267:371 */     container3.set("@src", this.cmeURL);
/* 268:372 */     container3.set("@dest", this.phyURL);
/* 269:    */     
/* 270:374 */     Vector<String> processes = new Vector();
/* 271:375 */     is.readTaskAndDataCompleted(this.phyURL, this.lastSyncTime1, this.curSyncTime1, container2, processes);
/* 272:376 */     is.readNewPhy(this.phyURL, this.lastSyncTime1, this.curSyncTime1, container2);
/* 273:377 */     is.readUserlog(this.phyURL, this.lastSyncTime1, this.curSyncTime1, container2);
/* 274:378 */     is.readAccts(this.phyURL, this.lastSyncTime1, this.curSyncTime1, container2);
/* 275:379 */     processes = new Vector();
/* 276:380 */     is.readProcessesNew(this.cmeURL, this.yesterday, this.curSyncTime1, container3, processes);
/* 277:381 */     is.readProcessesCompleted(this.phyURL, this.yesterday, this.curSyncTime1, container3, processes);
/* 278:    */     
/* 279:    */ 
/* 280:384 */     XModel p1 = new IntegrationService().readProcesses(this.cmeURL, processes);
/* 281:385 */     p1.set("@matchfield", "pid");
/* 282:386 */     p1.setId("process");
/* 283:387 */     container3.append(p1);
/* 284:    */     
/* 285:    */ 
/* 286:390 */     String dt = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
/* 287:    */     
/* 288:392 */     String msg = "cme-phy-" + dt + ".xml";
/* 289:393 */     File f = new File("data/" + msg);
/* 290:    */     
/* 291:    */ 
/* 292:396 */     FileWriter fw = new FileWriter(f, false);
/* 293:397 */     XDataSource.outputModel(fw, container1);
/* 294:398 */     fw.close();
/* 295:    */     
/* 296:400 */     msg = "phy-cme-" + dt + ".xml";
/* 297:401 */     f = new File("data/" + msg);
/* 298:    */     
/* 299:    */ 
/* 300:404 */     fw = new FileWriter(f, false);
/* 301:405 */     XDataSource.outputModel(fw, container2);
/* 302:    */     
/* 303:    */ 
/* 304:    */ 
/* 305:409 */     is.writeTaskAndData(this.phyURL, container1);
/* 306:410 */     is.writeTaskAndData(this.cmeURL, container2);
/* 307:411 */     System.out.println("Container 3");
/* 308:412 */     System.out.println("-----------------------------------------------");
/* 309:413 */     XDataSource.outputModel(fw, container3);
/* 310:414 */     fw.close();
/* 311:415 */     System.out.println("-----------------------------------------------");
/* 312:    */     
/* 313:417 */     Thread.currentThread();Thread.sleep(5000L);
/* 314:    */     
/* 315:419 */     is.writeTaskAndData(this.phyURL, container3);
/* 316:    */     
/* 317:    */ 
/* 318:    */ 
/* 319:423 */     FileWriter fw1 = new FileWriter("cme-phy-lastsynctime");
/* 320:424 */     Properties p = new Properties();
/* 321:425 */     p.setProperty("lastSyncTime-cme", this.curSyncTime);
/* 322:426 */     p.setProperty("lastSyncTime-phy", this.curSyncTime1);
/* 323:427 */     p.store(fw1, new Date().toString());
/* 324:428 */     fw1.close();
/* 325:429 */     fw1 = new FileWriter("cme-phy-logs", true);
/* 326:430 */     fw1.write("Last sync times : CME:" + this.lastSyncTime + "-" + this.curSyncTime + " Physician:" + this.lastSyncTime1 + "-" + this.curSyncTime1 + "\r\n");
/* 327:431 */     fw1.close();
/* 328:432 */     this.lastSyncTime = this.curSyncTime;
/* 329:433 */     this.lastSyncTime1 = this.curSyncTime1;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public void run()
/* 333:    */   {
/* 334:    */     try
/* 335:    */     {
/* 336:452 */       System.out.println(" Process >>>>>");
/* 337:453 */       sync();
/* 338:    */     }
/* 339:    */     catch (Exception e)
/* 340:    */     {
/* 341:457 */       e.printStackTrace();
/* 342:458 */       StringWriter s = new StringWriter();
/* 343:459 */       e.printStackTrace(new PrintWriter(s));
/* 344:    */       try
/* 345:    */       {
/* 346:461 */         sendMessage(this.supportEmail, " Exception has occured ", s.toString());
/* 347:    */       }
/* 348:    */       catch (AddressException e1)
/* 349:    */       {
/* 350:464 */         e1.printStackTrace();
/* 351:    */       }
/* 352:    */       catch (MessagingException e1)
/* 353:    */       {
/* 354:467 */         e1.printStackTrace();
/* 355:    */       }
/* 356:    */     }
/* 357:    */   }
/* 358:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.CMEToPhysicianAgent
 * JD-Core Version:    0.7.0.1
 */