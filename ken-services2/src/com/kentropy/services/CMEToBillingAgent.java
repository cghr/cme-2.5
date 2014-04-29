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
/*  18:    */ import java.util.Date;
/*  19:    */ import java.util.Properties;
/*  20:    */ import javax.mail.Message.RecipientType;
/*  21:    */ import javax.mail.MessagingException;
/*  22:    */ import javax.mail.Session;
/*  23:    */ import javax.mail.Transport;
/*  24:    */ import javax.mail.internet.AddressException;
/*  25:    */ import javax.mail.internet.InternetAddress;
/*  26:    */ import javax.mail.internet.MimeMessage;
/*  27:    */ import net.xoetrope.data.XDataSource;
/*  28:    */ import net.xoetrope.xml.XmlElement;
/*  29:    */ import net.xoetrope.xml.XmlSource;
/*  30:    */ import net.xoetrope.xui.data.XBaseModel;
/*  31:    */ import net.xoetrope.xui.data.XModel;
/*  32:    */ 
/*  33:    */ public class CMEToBillingAgent
/*  34:    */   implements Runnable
/*  35:    */ {
/*  36: 42 */   public String billingURL = "http://192.168.1.102:8080/cmebilling/";
/*  37: 44 */   public String cmeURL = "http://192.168.1.102:8080/cme/";
/*  38: 46 */   String lastSyncTime = "2013-05-01";
/*  39: 47 */   public String path = ".";
/*  40:    */   private XModel tasksM;
/*  41:    */   String smtpHost;
/*  42:    */   String supportEmail;
/*  43:    */   private String smtpUserName;
/*  44:    */   private String smtpPassword;
/*  45:    */   Properties props;
/*  46:    */   private String lastSyncTime1;
/*  47:    */   
/*  48:    */   public static void main(String[] args)
/*  49:    */     throws Exception
/*  50:    */   {
/*  51: 59 */     Properties p = new Properties();
/*  52: 60 */     p.load(new FileReader("E:/workspace3/testservices/cme-billing-agent.properties"));
/*  53:    */     
/*  54:    */ 
/*  55:    */ 
/*  56: 64 */     new CMEToBillingAgent(p).run();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public CMEToBillingAgent(Properties p)
/*  60:    */   {
/*  61: 77 */     this.cmeURL = p.getProperty("cmeURL");
/*  62: 78 */     this.billingURL = p.getProperty("billingURL");
/*  63: 79 */     this.smtpHost = p.getProperty("smtpHost");
/*  64: 80 */     this.supportEmail = p.getProperty("supportEmail");
/*  65: 81 */     this.smtpUserName = p.getProperty("smtpUsername");
/*  66: 82 */     this.smtpPassword = p.getProperty("smtpPassword");
/*  67: 83 */     this.props = p;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void deliver(String msg, String agent)
/*  71:    */     throws IOException
/*  72:    */   {
/*  73: 90 */     File file = new File(msg);
/*  74: 91 */     Path src = FileSystems.getDefault().getPath("cme", new String[] { "out", msg });
/*  75: 92 */     Path dest = FileSystems.getDefault().getPath(agent, new String[] { "in", msg });
/*  76: 93 */     Files.copy(src, dest, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void processImports()
/*  80:    */     throws Exception
/*  81:    */   {
/*  82:104 */     File dir = new File("physician/in");
/*  83:105 */     File[] files = dir.listFiles();
/*  84:106 */     for (int i = 0; i < files.length; i++)
/*  85:    */     {
/*  86:108 */       process(files[i]);
/*  87:109 */       files[i].renameTo(new File("physician/processed/" + files[i].getName()));
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   private void process(File file)
/*  92:    */     throws Exception
/*  93:    */   {
/*  94:120 */     FileReader sr = new FileReader(file);
/*  95:    */     
/*  96:122 */     XmlElement xe = XmlSource.read(sr);
/*  97:123 */     sr.close();
/*  98:124 */     System.out.println(xe);
/*  99:125 */     XDataSource xd = new XDataSource();
/* 100:126 */     XModel container = new XBaseModel();
/* 101:127 */     xd.loadTable(xe, container);
/* 102:128 */     StringWriter sw = new StringWriter();
/* 103:129 */     System.out.println(" Model in " + file);
/* 104:130 */     XDataSource.outputModel(sw, container);
/* 105:131 */     System.out.println(sw);
/* 106:132 */     for (int i = 0; i < container.getNumChildren(); i++)
/* 107:    */     {
/* 108:134 */       XModel xm = container.get(i);
/* 109:135 */       if (xm.getId().equals("tasks")) {
/* 110:137 */         this.tasksM = xm;
/* 111:    */       } else {
/* 112:141 */         processData(xm);
/* 113:    */       }
/* 114:    */     }
/* 115:145 */     processTasks(this.tasksM);
/* 116:    */   }
/* 117:    */   
/* 118:    */   private void processData(XModel xm)
/* 119:    */     throws Exception
/* 120:    */   {
/* 121:156 */     System.out.println(xm.getId() + " " + xm.get("@matchfield"));
/* 122:157 */     if (xm.getNumChildren() > 0) {
/* 123:158 */       new IntegrationService().write(this.billingURL, xm.getId(), xm.get("@matchfield").toString(), xm);
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   private void processTasks(XModel xm)
/* 128:    */     throws Exception
/* 129:    */   {
/* 130:166 */     processData(xm);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setRefreshProcess(XModel xm, String pidFld)
/* 134:    */   {
/* 135:171 */     for (int i = 0; i < xm.getNumChildren(); i++) {
/* 136:174 */       for (int j = 0; j < xm.get(i).getNumChildren(); j++) {
/* 137:176 */         xm.get(i).get(j).set("@refreshprocess", xm.get(i).get(j).get(pidFld + "/@value"));
/* 138:    */       }
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void getLastSyncTime()
/* 143:    */   {
/* 144:215 */     System.out.println(" Process >>>>>");
/* 145:    */     try
/* 146:    */     {
/* 147:217 */       FileInputStream fr = new FileInputStream("cme-billing-lastsynctime");
/* 148:218 */       Properties p = new Properties();
/* 149:219 */       p.load(fr);
/* 150:    */       
/* 151:    */ 
/* 152:    */ 
/* 153:    */ 
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:227 */       this.lastSyncTime = p.getProperty("lastSyncTime-cme");
/* 158:228 */       this.lastSyncTime1 = p.getProperty("lastSyncTime-billing");
/* 159:    */     }
/* 160:    */     catch (Exception e)
/* 161:    */     {
/* 162:232 */       e.printStackTrace();
/* 163:233 */       this.lastSyncTime = "2012-10-01 00:00:00";
/* 164:234 */       this.lastSyncTime1 = "2012-10-01 00:00:00";
/* 165:    */     }
/* 166:236 */     if (this.lastSyncTime == null) {
/* 167:237 */       this.lastSyncTime = "2012-10-01 00:00:00";
/* 168:    */     }
/* 169:238 */     if (this.lastSyncTime1 == null) {
/* 170:239 */       this.lastSyncTime1 = "2012-10-01 00:00:00";
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   public void sendMessage(String to, String subject, String text)
/* 175:    */     throws AddressException, MessagingException
/* 176:    */   {
/* 177:252 */     Session session = Session.getDefaultInstance(this.props);
/* 178:    */     
/* 179:254 */     MimeMessage message = new MimeMessage(session);
/* 180:    */     
/* 181:    */ 
/* 182:257 */     message.setFrom(new InternetAddress(this.supportEmail));
/* 183:    */     
/* 184:    */ 
/* 185:260 */     message.addRecipient(Message.RecipientType.TO, 
/* 186:261 */       new InternetAddress(this.supportEmail));
/* 187:262 */     message.setSubject(subject);
/* 188:263 */     message.setText(text);
/* 189:264 */     Transport tr = session.getTransport("smtp");
/* 190:265 */     tr.connect(this.smtpHost, this.smtpUserName, this.smtpPassword);
/* 191:266 */     message.saveChanges();
/* 192:267 */     tr.sendMessage(message, message.getAllRecipients());
/* 193:268 */     tr.close();
/* 194:    */   }
/* 195:    */   
/* 196:    */   public String getTime(String serverURL)
/* 197:    */     throws Exception
/* 198:    */   {
/* 199:274 */     XModel xm = new IntegrationService().readTable(serverURL, " accounts  ", " NOW() time1", " true limit 1 ");
/* 200:    */     
/* 201:276 */     return (String)xm.get(0).get(0).get(0).get();
/* 202:    */   }
/* 203:    */   
/* 204:278 */   public String curSyncTime = "";
/* 205:279 */   public String curSyncTime1 = "";
/* 206:    */   public static final double DUMMY_OFFSET = -1000000.0D;
/* 207:281 */   public double timeDiff = -1000000.0D;
/* 208:282 */   double offset1 = -1000000.0D;
/* 209:283 */   double offset2 = -1000000.0D;
/* 210:284 */   private String sntpServer = "0.uk.pool.ntp.org";
/* 211:    */   
/* 212:    */   public void getCurSyncTime()
/* 213:    */     throws Exception
/* 214:    */   {
/* 215:289 */     this.curSyncTime = getTime(this.cmeURL);
/* 216:290 */     this.curSyncTime1 = getTime(this.billingURL);
/* 217:291 */     SntpClient sntpc = new SntpClient();
/* 218:    */     
/* 219:293 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 220:294 */     Date dt1 = sdf.parse(this.curSyncTime);
/* 221:295 */     Date dt2 = sdf.parse(this.curSyncTime1);
/* 222:    */     
/* 223:297 */     NtpMessage msg = sntpc.getMessage(this.sntpServer);
/* 224:298 */     double dt1offset = sntpc.getClockOffset(msg, dt1.getTime()) * 1000.0D;
/* 225:299 */     double dt2offset = sntpc.getClockOffset(msg, dt2.getTime()) * 1000.0D;
/* 226:    */     
/* 227:301 */     System.out.println(" DT1 " + this.offset1 + " " + dt1offset + " " + this.curSyncTime);
/* 228:302 */     System.out.println(" DT2 " + this.offset2 + " " + dt2offset + " " + this.curSyncTime1);
/* 229:303 */     long td = dt1.getTime() - dt2.getTime();
/* 230:304 */     if (this.offset1 != -1000000.0D)
/* 231:    */     {
/* 232:306 */       if (Math.abs(this.offset1 - dt1offset) > 1000.0D) {
/* 233:307 */         throw new Exception(" Date have changed on CME Server");
/* 234:    */       }
/* 235:    */     }
/* 236:    */     else {
/* 237:311 */       this.offset1 = dt1offset;
/* 238:    */     }
/* 239:312 */     if (this.offset2 != -1000000.0D)
/* 240:    */     {
/* 241:314 */       if (Math.abs(this.offset2 - dt2offset) > 1000.0D) {
/* 242:315 */         throw new Exception(" Date have changed on Physician Server");
/* 243:    */       }
/* 244:    */     }
/* 245:    */     else {
/* 246:319 */       this.offset2 = dt2offset;
/* 247:    */     }
/* 248:321 */     if (this.timeDiff != -1000000.0D)
/* 249:    */     {
/* 250:323 */       if (this.timeDiff != td) {
/* 251:324 */         throw new Exception(" Dates have changed");
/* 252:    */       }
/* 253:    */     }
/* 254:    */     else {
/* 255:329 */       this.timeDiff = td;
/* 256:    */     }
/* 257:    */   }
/* 258:    */   
/* 259:    */   public void sync()
/* 260:    */     throws Exception
/* 261:    */   {
/* 262:339 */     System.out.println(" Process >>>>>");
/* 263:340 */     getLastSyncTime();
/* 264:    */     
/* 265:342 */     getCurSyncTime();
/* 266:    */     
/* 267:    */ 
/* 268:345 */     IntegrationService is = new IntegrationService();
/* 269:346 */     XModel container2 = new XBaseModel();
/* 270:347 */     container2.set("@src", this.cmeURL);
/* 271:348 */     container2.set("@dest", this.billingURL);
/* 272:349 */     String datetable = "billables";
/* 273:350 */     String dateFld = "date_of_billable";
/* 274:    */     
/* 275:    */ 
/* 276:    */ 
/* 277:354 */     XModel xm = new IntegrationService().readTable(this.cmeURL, "billables", datetable, "bid", "bid", "<k1> = '<k2>%'", dateFld, this.lastSyncTime, this.curSyncTime, null, new XBaseModel());
/* 278:    */     
/* 279:356 */     xm.set("@matchfield", "bid");
/* 280:357 */     xm.setId("billables");
/* 281:358 */     container2.append(xm);
/* 282:    */     
/* 283:360 */     XModel p1 = new XBaseModel();
/* 284:    */     
/* 285:    */ 
/* 286:    */ 
/* 287:364 */     String dt = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
/* 288:    */     
/* 289:366 */     String msg = "cme-billing-" + dt + ".xml";
/* 290:367 */     File f = new File("data/" + msg);
/* 291:    */     
/* 292:    */ 
/* 293:370 */     FileWriter fw = new FileWriter(f, false);
/* 294:371 */     XDataSource.outputModel(fw, container2);
/* 295:372 */     fw.close();
/* 296:    */     
/* 297:    */ 
/* 298:    */ 
/* 299:376 */     is.writeTaskAndData(this.billingURL, container2);
/* 300:    */     
/* 301:    */ 
/* 302:379 */     FileWriter fw1 = new FileWriter("cme-billing-lastsynctime");
/* 303:380 */     Properties p = new Properties();
/* 304:381 */     p.setProperty("lastSyncTime-cme", this.curSyncTime);
/* 305:382 */     p.setProperty("lastSyncTime-billing", this.curSyncTime1);
/* 306:383 */     p.store(fw1, new Date().toString());
/* 307:384 */     fw1.close();
/* 308:385 */     fw1 = new FileWriter("cme-billing.log", true);
/* 309:386 */     fw1.write("Last sync times : CME:" + this.lastSyncTime + " QA:" + this.lastSyncTime1 + "\r\n");
/* 310:387 */     fw1.close();
/* 311:    */   }
/* 312:    */   
/* 313:    */   public void run()
/* 314:    */   {
/* 315:    */     try
/* 316:    */     {
/* 317:406 */       System.out.println(" Process >>>>>");
/* 318:407 */       sync();
/* 319:    */     }
/* 320:    */     catch (Exception e)
/* 321:    */     {
/* 322:411 */       e.printStackTrace();
/* 323:412 */       StringWriter s = new StringWriter();
/* 324:413 */       e.printStackTrace(new PrintWriter(s));
/* 325:    */       try
/* 326:    */       {
/* 327:415 */         sendMessage(this.supportEmail, " Exception has occured ", s.toString());
/* 328:    */       }
/* 329:    */       catch (AddressException e1)
/* 330:    */       {
/* 331:418 */         e1.printStackTrace();
/* 332:    */       }
/* 333:    */       catch (MessagingException e1)
/* 334:    */       {
/* 335:421 */         e1.printStackTrace();
/* 336:    */       }
/* 337:    */     }
/* 338:    */   }
/* 339:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.CMEToBillingAgent
 * JD-Core Version:    0.7.0.1
 */