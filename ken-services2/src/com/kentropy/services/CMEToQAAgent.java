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
/*  33:    */ public class CMEToQAAgent
/*  34:    */   implements Runnable
/*  35:    */ {
/*  36: 42 */   public String qaURL = "http://192.168.1.102:8080/qa/";
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
/*  52: 60 */     p.load(new FileReader("E:/workspace3/testservices/cme-qa-agent.properties"));
/*  53:    */     
/*  54:    */ 
/*  55:    */ 
/*  56: 64 */     new CMEToQAAgent(p).run();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public CMEToQAAgent(Properties p)
/*  60:    */   {
/*  61: 77 */     this.cmeURL = p.getProperty("cmeURL");
/*  62: 78 */     this.qaURL = p.getProperty("qaURL");
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
/* 123:158 */       new IntegrationService().write(this.qaURL, xm.getId(), xm.get("@matchfield").toString(), xm);
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
/* 147:217 */       FileInputStream fr = new FileInputStream("cme-qa-lastsynctime");
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
/* 158:228 */       this.lastSyncTime1 = p.getProperty("lastSyncTime-qa");
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
/* 216:290 */     this.curSyncTime1 = getTime(this.qaURL);
/* 217:291 */     SntpClient sntpc = new SntpClient();
/* 218:    */     
/* 219:293 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 220:294 */     Date dt1 = sdf.parse(this.curSyncTime);
/* 221:295 */     Date dt2 = sdf.parse(this.curSyncTime1);
/* 222:296 */     long td = dt1.getTime() - dt2.getTime();
/* 223:323 */     if (this.timeDiff != -1000000.0D)
/* 224:    */     {
/* 225:325 */       if (Math.abs(this.timeDiff - td) > 1000.0D) {
/* 226:326 */         throw new Exception(" Dates have changed");
/* 227:    */       }
/* 228:    */     }
/* 229:    */     else {
/* 230:331 */       this.timeDiff = td;
/* 231:    */     }
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void sync()
/* 235:    */     throws Exception
/* 236:    */   {
/* 237:341 */     System.out.println(" Process >>>>>");
/* 238:342 */     getLastSyncTime();
/* 239:    */     
/* 240:344 */     getCurSyncTime();
/* 241:    */     
/* 242:    */ 
/* 243:    */ 
/* 244:348 */     XModel container1 = new XBaseModel();
/* 245:    */     
/* 246:    */ 
/* 247:351 */     IntegrationService is = new IntegrationService();
/* 248:    */     
/* 249:    */ 
/* 250:354 */     XModel container2 = new XBaseModel();
/* 251:355 */     container2.set("@src", this.cmeURL);
/* 252:356 */     container2.set("@dest", this.qaURL);
/* 253:    */     
/* 254:    */ 
/* 255:    */ 
/* 256:    */ 
/* 257:    */ 
/* 258:    */ 
/* 259:363 */     is.readCMEReport(this.cmeURL, this.lastSyncTime, this.curSyncTime, container2, null);
/* 260:    */     
/* 261:    */ 
/* 262:    */ 
/* 263:    */ 
/* 264:    */ 
/* 265:369 */     String dt = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
/* 266:    */     
/* 267:371 */     String msg = "cme-qa-" + dt + ".xml";
/* 268:372 */     File f = new File("data/" + msg);
/* 269:    */     
/* 270:    */ 
/* 271:375 */     FileWriter fw = new FileWriter(f, false);
/* 272:376 */     XDataSource.outputModel(fw, container2);
/* 273:377 */     fw.close();
/* 274:    */     
/* 275:    */ 
/* 276:    */ 
/* 277:    */ 
/* 278:    */ 
/* 279:383 */     is.writeTaskAndData(this.qaURL, container2);
/* 280:    */     
/* 281:    */ 
/* 282:    */ 
/* 283:    */ 
/* 284:    */ 
/* 285:    */ 
/* 286:    */ 
/* 287:    */ 
/* 288:    */ 
/* 289:    */ 
/* 290:    */ 
/* 291:    */ 
/* 292:    */ 
/* 293:397 */     FileWriter fw1 = new FileWriter("cme-qa-lastsynctime");
/* 294:398 */     Properties p = new Properties();
/* 295:399 */     p.setProperty("lastSyncTime-cme", this.curSyncTime);
/* 296:400 */     p.setProperty("lastSyncTime-qa", this.curSyncTime1);
/* 297:401 */     p.store(fw1, new Date().toString());
/* 298:402 */     fw1.close();
/* 299:403 */     fw1 = new FileWriter("cme-qa-logs", true);
/* 300:404 */     fw1.write("Last sync times : CME:" + this.lastSyncTime + "-" + this.curSyncTime + " QA:" + this.lastSyncTime1 + "-" + this.curSyncTime1 + "\r\n");
/* 301:405 */     fw1.close();
/* 302:406 */     this.lastSyncTime = this.curSyncTime;
/* 303:407 */     this.lastSyncTime1 = this.curSyncTime1;
/* 304:    */   }
/* 305:    */   
/* 306:    */   public void run()
/* 307:    */   {
/* 308:    */     try
/* 309:    */     {
/* 310:426 */       System.out.println(" Process >>>>>");
/* 311:427 */       sync();
/* 312:    */     }
/* 313:    */     catch (Exception e)
/* 314:    */     {
/* 315:431 */       e.printStackTrace();
/* 316:432 */       StringWriter s = new StringWriter();
/* 317:433 */       e.printStackTrace(new PrintWriter(s));
/* 318:    */       try
/* 319:    */       {
/* 320:435 */         sendMessage(this.supportEmail, " Exception has occured ", s.toString());
/* 321:    */       }
/* 322:    */       catch (AddressException e1)
/* 323:    */       {
/* 324:438 */         e1.printStackTrace();
/* 325:    */       }
/* 326:    */       catch (MessagingException e1)
/* 327:    */       {
/* 328:441 */         e1.printStackTrace();
/* 329:    */       }
/* 330:    */     }
/* 331:    */   }
/* 332:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.CMEToQAAgent
 * JD-Core Version:    0.7.0.1
 */