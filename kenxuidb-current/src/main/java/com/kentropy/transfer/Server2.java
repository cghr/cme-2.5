/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.db.XUIDB;
/*   5:    */ import com.kentropy.resource.Base64FileDecoder;
/*   6:    */ import java.io.DataInputStream;
/*   7:    */ import java.io.DataOutputStream;
/*   8:    */ import java.io.File;
/*   9:    */ import java.io.FileInputStream;
/*  10:    */ import java.io.FileOutputStream;
/*  11:    */ import java.io.FileWriter;
/*  12:    */ import java.io.IOException;
/*  13:    */ import java.io.InputStream;
/*  14:    */ import java.io.OutputStream;
/*  15:    */ import java.io.PrintStream;
/*  16:    */ import java.net.ServerSocket;
/*  17:    */ import java.net.Socket;
/*  18:    */ import java.text.SimpleDateFormat;
/*  19:    */ import java.util.Date;
/*  20:    */ import java.util.Hashtable;
/*  21:    */ import java.util.StringTokenizer;
/*  22:    */ import java.util.zip.ZipEntry;
/*  23:    */ import java.util.zip.ZipInputStream;
/*  24:    */ import net.xoetrope.xui.data.XBaseModel;
/*  25:    */ import net.xoetrope.xui.data.XModel;
/*  26:    */ import org.apache.log4j.Logger;
/*  27:    */ 
/*  28:    */ public class Server2
/*  29:    */   implements Runnable
/*  30:    */ {
/*  31: 31 */   public Socket sock = null;
/*  32: 32 */   public DataOutputStream dout = null;
/*  33: 33 */   int count = 0;
/*  34: 34 */   public String state = "headers";
/*  35: 35 */   public String currentLogId = "";
/*  36: 36 */   public StringBuffer log = new StringBuffer();
/*  37: 37 */   public String headers = "";
/*  38: 38 */   public Hashtable headers1 = new Hashtable();
/*  39: 39 */   public String path = ".";
/*  40: 40 */   Logger logger = Logger.getLogger(getClass().getName());
/*  41: 42 */   DataOutputStream dout1 = null;
/*  42: 44 */   MsgChannel mc = null;
/*  43: 46 */   boolean isLog = false;
/*  44:    */   
/*  45:    */   public void deliver() {}
/*  46:    */   
/*  47:    */   public void startLog()
/*  48:    */     throws Exception
/*  49:    */   {
/*  50: 55 */     this.logger.info("Start ");
/*  51: 57 */     if (this.state.equals("Started"))
/*  52:    */     {
/*  53: 59 */       this.dout.writeBytes("Error: Two starts in one message\n");
/*  54: 60 */       return;
/*  55:    */     }
/*  56: 63 */     this.state = "Started";
/*  57: 64 */     this.count += 1;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void endLog()
/*  61:    */     throws Exception
/*  62:    */   {
/*  63: 70 */     if (!this.state.equals("Started"))
/*  64:    */     {
/*  65: 72 */       this.dout.writeBytes("Error: End without start\n");
/*  66: 73 */       return;
/*  67:    */     }
/*  68: 76 */     this.state = "ended";
/*  69: 78 */     if (!saveLog()) {
/*  70: 80 */       this.dout.writeBytes("Error: while saving " + this.currentLogId + "\n");
/*  71:    */     }
/*  72: 81 */     this.currentLogId = "";
/*  73:    */   }
/*  74:    */   
/*  75:    */   public boolean saveLog()
/*  76:    */   {
/*  77: 86 */     return true;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void handleHeader(String header)
/*  81:    */     throws Exception
/*  82:    */   {
/*  83: 92 */     this.logger.info("Header " + header);
/*  84: 93 */     if (!header.trim().equals(""))
/*  85:    */     {
/*  86: 95 */       this.headers += header;
/*  87: 96 */       StringTokenizer st = new StringTokenizer(header, ":");
/*  88: 97 */       String key = st.nextToken();
/*  89: 98 */       String value = st.nextToken();
/*  90: 99 */       this.headers1.put(key, value);
/*  91:    */     }
/*  92:    */     else
/*  93:    */     {
/*  94:103 */       this.state = "";
/*  95:104 */       this.logger.info(" >> msg type" + this.headers1.get("msg-type"));
/*  96:    */       
/*  97:106 */       this.isLog = ((this.headers1.get("msg-type") != null) && (this.headers1.get("msg-type").equals("changelogs")));
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void handleLine(String line)
/* 102:    */     throws Exception
/* 103:    */   {
/* 104:113 */     if (line.startsWith("<l "))
/* 105:    */     {
/* 106:115 */       startLog();
/* 107:    */       
/* 108:117 */       this.dout1.writeBytes(line + "\n");
/* 109:    */     }
/* 110:119 */     else if (line.startsWith("</l"))
/* 111:    */     {
/* 112:121 */       this.dout1.writeBytes(line + "\n");
/* 113:122 */       endLog();
/* 114:    */     }
/* 115:    */     else
/* 116:    */     {
/* 117:126 */       this.dout1.writeBytes(line + "\n");
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public Server2(InputStream in, OutputStream out)
/* 122:    */     throws Exception
/* 123:    */   {
/* 124:133 */     this.mc = new MsgChannel(in, out, 1);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public Server2(Socket sock1)
/* 128:    */     throws Exception
/* 129:    */   {
/* 130:137 */     this.sock = sock1;
/* 131:138 */     this.mc = new MsgChannel(this.sock, 1);
/* 132:    */   }
/* 133:    */   
/* 134:    */   /**
/* 135:    */    * @deprecated
/* 136:    */    */
/* 137:    */   public void run2()
/* 138:    */   {
/* 139:    */     try
/* 140:    */     {
/* 141:146 */       ZipInputStream zin = new ZipInputStream(this.sock.getInputStream());
/* 142:147 */       ZipEntry ze = zin.getNextEntry();
/* 143:148 */       DataInputStream in = new DataInputStream(zin);
/* 144:149 */       this.dout = new DataOutputStream(this.sock.getOutputStream());
/* 145:150 */       String line = in.readLine();int count = 0;
/* 146:152 */       while (line != null)
/* 147:    */       {
/* 148:154 */         if (this.state.equals("headers")) {
/* 149:156 */           handleHeader(line);
/* 150:    */         } else {
/* 151:160 */           handleLine(line);
/* 152:    */         }
/* 153:162 */         line = in.readLine();
/* 154:163 */         count++;
/* 155:    */       }
/* 156:166 */       if (this.isLog) {
/* 157:167 */         this.log.append("</logs>");
/* 158:    */       }
/* 159:170 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SS");
/* 160:    */       
/* 161:172 */       String fname = "";
/* 162:173 */       if (this.isLog) {
/* 163:175 */         fname = this.headers1.get("participant") + "-" + sdf.format(new Date()) + "-received.xml";
/* 164:    */       } else {
/* 165:179 */         fname = ze.getName() + ".txt";
/* 166:    */       }
/* 167:181 */       FileWriter fw = new FileWriter(this.path + "/" + fname);
/* 168:    */       
/* 169:183 */       fw.write(this.log.toString());
/* 170:    */       
/* 171:185 */       fw.close();
/* 172:    */       
/* 173:187 */       this.logger.info("Line count " + count);
/* 174:188 */       this.logger.info("Line count " + count);
/* 175:189 */       String recepients = (String)this.headers1.get("recepients");
/* 176:190 */       this.logger.info("recepients " + recepients);
/* 177:191 */       String op = (String)this.headers1.get("op");
/* 178:192 */       StringTokenizer st = new StringTokenizer(recepients, ",");
/* 179:    */       
/* 180:194 */       this.logger.info("rec count " + st.countTokens());
/* 181:195 */       if (st.countTokens() > 1)
/* 182:    */       {
/* 183:197 */         while (st.hasMoreTokens()) {
/* 184:199 */           TestXUIDB.getInstance().deliverMessage(fname, st.nextToken());
/* 185:    */         }
/* 186:    */       }
/* 187:    */       else
/* 188:    */       {
/* 189:204 */         this.logger.info("Inside else ");
/* 190:205 */         TestXUIDB.getInstance().deliverMessage(fname, recepients);
/* 191:    */       }
/* 192:207 */       this.logger.info("dELIVERED");
/* 193:    */       
/* 194:209 */       this.dout.writeBytes("Success:\n");
/* 195:210 */       if ((op != null) && (op.equals("import"))) {
/* 196:212 */         if (this.isLog) {
/* 197:214 */           TestXUIDB.getInstance().importChangeLogs(this.log.toString());
/* 198:216 */         } else if (this.headers1.get("msg-type").equals("resource")) {
/* 199:218 */           Base64FileDecoder.decodeFile("./" + fname, "./" + ze.getName());
/* 200:    */         }
/* 201:    */       }
/* 202:    */     }
/* 203:    */     catch (Exception e)
/* 204:    */     {
/* 205:226 */       e.printStackTrace();
/* 206:    */     }
/* 207:    */   }
/* 208:    */   
/* 209:    */   public void run()
/* 210:    */   {
/* 211:232 */     String fname1 = "";
/* 212:    */     try
/* 213:    */     {
/* 214:235 */       this.mc.getNextEntry();
/* 215:236 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SS");
/* 216:237 */       fname1 = this.mc.getName();
/* 217:    */       
/* 218:239 */       String line = this.mc.readLine();int count = 0;
/* 219:    */       
/* 220:241 */       boolean first = true;
/* 221:242 */       while (line != null)
/* 222:    */       {
/* 223:244 */         if (this.state.equals("headers"))
/* 224:    */         {
/* 225:246 */           handleHeader(line);
/* 226:    */         }
/* 227:    */         else
/* 228:    */         {
/* 229:250 */           if (first)
/* 230:    */           {
/* 231:252 */             if ((fname1.endsWith(".xml")) || (fname1.equals("test")))
/* 232:    */             {
/* 233:254 */               fname1 = this.headers1.get("participant") + "-" + sdf.format(new Date()) + "-received.xml";
/* 234:255 */               this.isLog = true;
/* 235:    */             }
/* 236:    */             else
/* 237:    */             {
/* 238:259 */               fname1 = this.mc.getName() + ".txt";
/* 239:    */             }
/* 240:260 */             this.logger.info(" File name : " + fname1);
/* 241:261 */             this.dout1 = new DataOutputStream(new FileOutputStream(this.path + "/" + fname1));
/* 242:262 */             if (this.isLog) {
/* 243:263 */               this.dout1.writeBytes("<logs>");
/* 244:    */             }
/* 245:264 */             first = false;
/* 246:    */           }
/* 247:266 */           handleLine(line);
/* 248:    */         }
/* 249:269 */         line = this.mc.readLine();
/* 250:270 */         count++;
/* 251:    */       }
/* 252:273 */       if (this.isLog) {
/* 253:275 */         this.dout1.writeBytes("</logs>");
/* 254:    */       }
/* 255:278 */       String fname = "";
/* 256:    */       
/* 257:280 */       fname = fname1;
/* 258:    */       
/* 259:282 */       this.logger.info("Line count " + count);
/* 260:283 */       String recepients = (String)this.headers1.get("recepients");
/* 261:284 */       this.logger.info("Rec " + recepients);
/* 262:285 */       String op = (String)this.headers1.get("op");
/* 263:286 */       this.logger.info("Rec " + recepients);
/* 264:287 */       StringTokenizer st = new StringTokenizer(recepients, ",");
/* 265:    */       
/* 266:289 */       this.logger.info("st " + st.countTokens());
/* 267:291 */       if (st.countTokens() > 1)
/* 268:    */       {
/* 269:293 */         while (st.hasMoreTokens())
/* 270:    */         {
/* 271:295 */           String rec = st.nextToken();
/* 272:    */           
/* 273:297 */           TestXUIDB.getInstance().deliverMessage(fname, rec);
/* 274:298 */           TestXUIDB.getInstance().distributeMessage(fname, rec);
/* 275:    */         }
/* 276:    */       }
/* 277:    */       else
/* 278:    */       {
/* 279:304 */         this.logger.info("Inside");
/* 280:305 */         XUIDB tt = new TestXUIDB();
/* 281:306 */         this.logger.info("Inside 1");
/* 282:    */         
/* 283:308 */         tt.deliverMessage(fname, recepients);
/* 284:309 */         tt.distributeMessage(fname, recepients);
/* 285:    */       }
/* 286:312 */       this.logger.info("dELIVERED");
/* 287:    */       
/* 288:314 */       this.mc.writeBytes("Success:\n");
/* 289:315 */       if ((op != null) && (op.equals("import"))) {
/* 290:317 */         if (this.isLog)
/* 291:    */         {
/* 292:319 */           TestXUIDB.getInstance().importChangeLogs1(this.path + "/" + fname);
/* 293:320 */           XModel xm = new XBaseModel();
/* 294:321 */           xm.set("importstatus", "Completed");
/* 295:    */           
/* 296:323 */           TestXUIDB.getInstance().saveDataM2("mqueue", "message='" + fname + "'", xm);
/* 297:    */         }
/* 298:325 */         else if (this.headers1.get("msg-type").equals("resource"))
/* 299:    */         {
/* 300:327 */           Base64FileDecoder.decodeFile(this.path + "/" + fname, TestXUIDB.getInstance().getImagePath() + "/" + this.mc.getName());
/* 301:    */         }
/* 302:    */       }
/* 303:    */     }
/* 304:    */     catch (Exception e)
/* 305:    */     {
/* 306:335 */       if (fname1 != null) {
/* 307:337 */         if (this.dout1 != null) {
/* 308:    */           try
/* 309:    */           {
/* 310:340 */             this.dout1.close();
/* 311:    */           }
/* 312:    */           catch (IOException e1)
/* 313:    */           {
/* 314:343 */             e1.printStackTrace();
/* 315:344 */             File f = new File(fname1);
/* 316:345 */             f.renameTo(new File(fname1 + ".err"));
/* 317:    */           }
/* 318:    */         }
/* 319:    */       }
/* 320:349 */       e.printStackTrace();
/* 321:    */     }
/* 322:    */   }
/* 323:    */   
/* 324:    */   public static void checkSetup()
/* 325:    */     throws Exception
/* 326:    */   {
/* 327:356 */     TestXUIDB.getInstance().checkDBServer();
/* 328:357 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
/* 329:358 */     String name = "tt" + sdf.format(new Date());
/* 330:359 */     FileWriter fw = new FileWriter(name);
/* 331:360 */     fw.close();
/* 332:361 */     File file = new File(name);
/* 333:362 */     file.delete();
/* 334:    */   }
/* 335:    */   
/* 336:    */   public static void main(String[] args)
/* 337:    */   {
/* 338:    */     try
/* 339:    */     {
/* 340:    */       
/* 341:370 */       if (args.length > 1) {
/* 342:372 */         if (args[0].equals("import")) {
/* 343:374 */           if (args[1] != null)
/* 344:    */           {
/* 345:376 */             StringBuffer logs = new StringBuffer();
/* 346:377 */             DataInputStream din = new DataInputStream(new FileInputStream(args[1]));
/* 347:378 */             String line = "";
/* 348:379 */             while (line != null)
/* 349:    */             {
/* 350:381 */               line = din.readLine();
/* 351:383 */               if (line != null) {
/* 352:385 */                 logs.append(line + "\r\n");
/* 353:    */               }
/* 354:    */             }
/* 355:388 */             TestXUIDB.getInstance().importChangeLogs(logs.toString());
/* 356:389 */             return;
/* 357:    */           }
/* 358:    */         }
/* 359:    */       }
/* 360:393 */       ServerSocket ss = new ServerSocket(8086);
/* 361:    */       for (;;)
/* 362:    */       {
/* 363:396 */         Socket s = ss.accept();
/* 364:397 */         Server2 srv = new Server2(s);
/* 365:398 */         srv.sock = s;
/* 366:399 */         new Thread(srv).run();
/* 367:    */       }
/* 368:410 */       return;
/* 369:    */     }
/* 370:    */     catch (Exception e)
/* 371:    */     {
/* 372:405 */       System.out.println("Exiting......");
/* 373:    */       
/* 374:407 */       e.printStackTrace();
/* 375:408 */       System.exit(0);
/* 376:    */     }
/* 377:    */   }
/* 378:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.Server2
 * JD-Core Version:    0.7.0.1
 */