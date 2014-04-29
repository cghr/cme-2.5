/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import java.io.BufferedReader;
/*   4:    */ import java.io.BufferedWriter;
/*   5:    */ import java.io.DataOutputStream;
/*   6:    */ import java.io.EOFException;
/*   7:    */ import java.io.File;
/*   8:    */ import java.io.FileInputStream;
/*   9:    */ import java.io.FileOutputStream;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.InputStream;
/*  12:    */ import java.io.InputStreamReader;
/*  13:    */ import java.io.OutputStream;
/*  14:    */ import java.io.OutputStreamWriter;
/*  15:    */ import java.net.Socket;
/*  16:    */ import java.net.URL;
/*  17:    */ import java.net.URLConnection;
/*  18:    */ import java.net.UnknownHostException;
/*  19:    */ import java.text.SimpleDateFormat;
/*  20:    */ import java.util.Date;
/*  21:    */ import java.util.zip.ZipEntry;
/*  22:    */ import java.util.zip.ZipInputStream;
/*  23:    */ import java.util.zip.ZipOutputStream;
/*  24:    */ import org.apache.log4j.Logger;
/*  25:    */ 
/*  26:    */ public class MsgChannel
/*  27:    */ {
/*  28:    */   Socket sock;
/*  29:    */   BufferedWriter dout;
/*  30:    */   BufferedReader in;
/*  31:    */   String name;
/*  32: 32 */   String path = ".";
/*  33: 33 */   Logger logger = Logger.getLogger(getClass().getName());
/*  34: 34 */   String local = null;
/*  35: 36 */   public StringBuffer headers = new StringBuffer("");
/*  36:    */   ZipOutputStream zout;
/*  37:    */   ZipInputStream zin;
/*  38: 39 */   public boolean zip = false;
/*  39: 40 */   URL url = null;
/*  40: 41 */   URLConnection con = null;
/*  41: 42 */   FileOutputStream fout = null;
/*  42: 43 */   String fname = "";
/*  43:    */   public String sessionID;
/*  44:    */   FileOutputStream fout10;
/*  45:    */   
/*  46:    */   public static void main(String[] args)
/*  47:    */     throws Exception
/*  48:    */   {
/*  49: 50 */     Logger logger = Logger.getLogger(MsgChannel.class);
/*  50: 51 */     MsgChannel mc = new MsgChannel();
/*  51: 52 */     StringBuffer sbuf = new StringBuffer("participant:10\n");
/*  52: 53 */     sbuf.append("team:10\n");
/*  53: 54 */     sbuf.append("op:download\n");
/*  54:    */     
/*  55: 56 */     sbuf.append("\n");
/*  56: 57 */     logger.info("Headers Complete");
/*  57: 58 */     mc.uploadFileToServer(new File("d:\\tmp\\test2.zip"));
/*  58:    */   }
/*  59:    */   
/*  60:    */   public MsgChannel() {}
/*  61:    */   
/*  62:    */   public MsgChannel(int mode)
/*  63:    */   {
/*  64:    */     try
/*  65:    */     {
/*  66: 69 */       this.local = "true";
/*  67: 70 */       if (mode == 0)
/*  68:    */       {
/*  69: 72 */         this.zout = new ZipOutputStream(new FileOutputStream("d:\\tmp\\test5.zip"));
/*  70: 73 */         this.dout = getWriter(this.zout);
/*  71:    */       }
/*  72:    */       else
/*  73:    */       {
/*  74: 77 */         this.zin = new ZipInputStream(new FileInputStream("d:\\tmp\\test6.zip"));
/*  75:    */         
/*  76: 79 */         this.in = new BufferedReader(new InputStreamReader(this.zin, "UTF8"));
/*  77:    */       }
/*  78:    */     }
/*  79:    */     catch (Exception e)
/*  80:    */     {
/*  81: 85 */       e.printStackTrace();
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public BufferedWriter getWriter(OutputStream out1)
/*  86:    */     throws Exception
/*  87:    */   {
/*  88: 91 */     return new BufferedWriter(new OutputStreamWriter(out1, "UTF8"));
/*  89:    */   }
/*  90:    */   
/*  91:    */   public MsgChannel(InputStream in1, OutputStream out, int mode)
/*  92:    */     throws Exception, IOException
/*  93:    */   {
/*  94: 97 */     if (mode == 0)
/*  95:    */     {
/*  96: 99 */       this.zout = new ZipOutputStream(out);
/*  97:    */       
/*  98:101 */       this.dout = getWriter(this.zout);
/*  99:102 */       this.in = new BufferedReader(new InputStreamReader(in1, "UTF8"));
/* 100:    */     }
/* 101:    */     else
/* 102:    */     {
/* 103:106 */       this.zin = new ZipInputStream(in1);
/* 104:    */       
/* 105:108 */       this.in = new BufferedReader(new InputStreamReader(this.zin));
/* 106:    */       
/* 107:110 */       this.dout = getWriter(out);
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   public MsgChannel(Socket sock1, int mode)
/* 112:    */     throws UnknownHostException, IOException, Exception
/* 113:    */   {
/* 114:117 */     this.sock = sock1;
/* 115:118 */     this.logger.info("Connected");
/* 116:119 */     if (mode == 0)
/* 117:    */     {
/* 118:121 */       this.zout = new ZipOutputStream(this.sock.getOutputStream());
/* 119:122 */       this.dout = getWriter(this.zout);
/* 120:    */       
/* 121:124 */       this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
/* 122:    */     }
/* 123:    */     else
/* 124:    */     {
/* 125:128 */       this.zin = new ZipInputStream(this.sock.getInputStream());
/* 126:    */       
/* 127:130 */       this.in = new BufferedReader(new InputStreamReader(this.zin));
/* 128:131 */       this.dout = getWriter(this.sock.getOutputStream());
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   public MsgChannel(String server, int port, int mode)
/* 133:    */     throws UnknownHostException, IOException, Exception
/* 134:    */   {
/* 135:137 */     this.sock = new Socket(server, port);
/* 136:138 */     this.logger.info("Connected");
/* 137:140 */     if (mode == 0)
/* 138:    */     {
/* 139:142 */       this.zout = new ZipOutputStream(this.sock.getOutputStream());
/* 140:143 */       this.dout = getWriter(this.zout);
/* 141:    */       
/* 142:145 */       this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream(), "UTF8"));
/* 143:    */     }
/* 144:    */     else
/* 145:    */     {
/* 146:149 */       this.zin = new ZipInputStream(this.sock.getInputStream());
/* 147:    */       
/* 148:151 */       this.in = new BufferedReader(new InputStreamReader(this.zin));
/* 149:152 */       this.dout = getWriter(this.sock.getOutputStream());
/* 150:    */     }
/* 151:    */   }
/* 152:    */   
/* 153:    */   public MsgChannel(URL url, int mode, String path)
/* 154:    */     throws UnknownHostException, IOException, Exception
/* 155:    */   {
/* 156:159 */     this.url = url;
/* 157:160 */     this.path = path;
/* 158:162 */     if (mode == 0)
/* 159:    */     {
/* 160:164 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SS");
/* 161:    */       
/* 162:166 */       this.fname = "";
/* 163:    */       
/* 164:168 */       this.fname = (sdf.format(new Date()) + "-sent.zip");
/* 165:169 */       FileOutputStream fout = new FileOutputStream(path + "/" + this.fname);
/* 166:    */       
/* 167:171 */       this.zout = new ZipOutputStream(fout);
/* 168:172 */       this.dout = getWriter(this.zout);
/* 169:    */     }
/* 170:    */     else
/* 171:    */     {
/* 172:176 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SS");
/* 173:    */       
/* 174:178 */       this.fname = "";
/* 175:    */       
/* 176:180 */       this.fname = (sdf.format(new Date()) + "-received.zip");
/* 177:    */     }
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void putNextEntry(String name)
/* 181:    */     throws IOException
/* 182:    */   {
/* 183:187 */     this.name = name;
/* 184:    */     
/* 185:189 */     this.zout.putNextEntry(new ZipEntry(name));
/* 186:    */   }
/* 187:    */   
/* 188:    */   public void closeEntry()
/* 189:    */     throws IOException
/* 190:    */   {
/* 191:194 */     this.zout.closeEntry();
/* 192:    */   }
/* 193:    */   
/* 194:    */   public void getNextEntry()
/* 195:    */     throws IOException
/* 196:    */   {
/* 197:200 */     ZipEntry ze = this.zin.getNextEntry();
/* 198:201 */     this.name = ze.getName();
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void writeBytes(String msg)
/* 202:    */     throws IOException
/* 203:    */   {
/* 204:206 */     if (this.dout != null)
/* 205:    */     {
/* 206:208 */       this.dout.write(msg);
/* 207:209 */       this.dout.flush();
/* 208:    */     }
/* 209:    */   }
/* 210:    */   
/* 211:    */   public String readLine()
/* 212:    */     throws Exception
/* 213:    */   {
/* 214:216 */     return this.in.readLine();
/* 215:    */   }
/* 216:    */   
/* 217:    */   public String getName()
/* 218:    */   {
/* 219:220 */     return this.name;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public void complete()
/* 223:    */     throws Exception
/* 224:    */   {
/* 225:226 */     if (this.url != null)
/* 226:    */     {
/* 227:228 */       this.zout.close();
/* 228:229 */       uploadFileToURL(this.url, new File(this.path + "/" + this.fname));
/* 229:    */     }
/* 230:231 */     if (this.local != null)
/* 231:    */     {
/* 232:233 */       this.dout.close();
/* 233:234 */       uploadFileToServer(new File(this.path + "/" + this.fname));
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   public String start()
/* 238:    */     throws Exception
/* 239:    */   {
/* 240:241 */     if (this.url != null)
/* 241:    */     {
/* 242:243 */       downloadFileFromURL(this.url, new File(this.path + "/" + this.fname), this.headers);
/* 243:    */       
/* 244:245 */       FileInputStream fin = new FileInputStream(new File(this.path + "/" + this.fname));
/* 245:246 */       this.zin = new ZipInputStream(fin);
/* 246:    */       
/* 247:248 */       this.in = new BufferedReader(new InputStreamReader(this.zin));
/* 248:249 */       return this.path + "/" + this.fname;
/* 249:    */     }
/* 250:252 */     writeBytes(this.headers.toString());
/* 251:253 */     return null;
/* 252:    */   }
/* 253:    */   
/* 254:    */   public void restart(String filename)
/* 255:    */     throws Exception
/* 256:    */   {
/* 257:259 */     if (this.url != null)
/* 258:    */     {
/* 259:261 */       FileInputStream fin = new FileInputStream(new File(filename));
/* 260:262 */       this.zin = new ZipInputStream(fin);
/* 261:    */       
/* 262:264 */       this.in = new BufferedReader(new InputStreamReader(this.zin));
/* 263:    */     }
/* 264:    */     else
/* 265:    */     {
/* 266:268 */       writeBytes(this.headers.toString());
/* 267:    */     }
/* 268:    */   }
/* 269:    */   
/* 270:    */   public void flush()
/* 271:    */     throws IOException
/* 272:    */   {
/* 273:275 */     this.dout.close();
/* 274:    */   }
/* 275:    */   
/* 276:    */   public void close()
/* 277:    */     throws IOException
/* 278:    */   {
/* 279:281 */     this.dout.close();
/* 280:    */   }
/* 281:    */   
/* 282:    */   public void downloadFileFromURL(URL url, File file, StringBuffer test)
/* 283:    */     throws Exception
/* 284:    */   {
/* 285:287 */     FileOutputStream fout1 = new FileOutputStream(file);
/* 286:    */     
/* 287:289 */     this.url = url;
/* 288:290 */     this.con = this.url.openConnection();
/* 289:291 */     this.con.setRequestProperty("Cookie", this.sessionID);
/* 290:    */     
/* 291:293 */     this.con.setRequestProperty("Content-Type", 
/* 292:294 */       "text/plain");
/* 293:    */     
/* 294:296 */     this.con.setRequestProperty("Content-Length", 
/* 295:297 */       "20000");
/* 296:298 */     this.con.setRequestProperty("Connection", "Keep-Alive");
/* 297:299 */     this.con.setDoOutput(true);
/* 298:300 */     new DataOutputStream(this.con.getOutputStream()).writeBytes(test.toString());
/* 299:    */     
/* 300:302 */     this.logger.info("Test1::");
/* 301:    */     
/* 302:304 */     InputStream in = this.con.getInputStream();
/* 303:305 */     byte[] b = new byte[2048];
/* 304:306 */     int read = in.read(b);
/* 305:308 */     while (read > 0)
/* 306:    */     {
/* 307:310 */       this.logger.info(Integer.valueOf(read));
/* 308:    */       
/* 309:312 */       fout1.write(b, 0, read);
/* 310:    */       try
/* 311:    */       {
/* 312:314 */         read = in.read(b);
/* 313:    */       }
/* 314:    */       catch (EOFException e)
/* 315:    */       {
/* 316:318 */         this.logger.info(e.getMessage());
/* 317:319 */         break;
/* 318:    */       }
/* 319:    */     }
/* 320:324 */     fout1.close();
/* 321:    */   }
/* 322:    */   
/* 323:    */   public void uploadFileToURL(URL url, File file)
/* 324:    */     throws Exception
/* 325:    */   {
/* 326:330 */     FileInputStream fin = new FileInputStream(file);
/* 327:331 */     byte[] b = new byte[2048];
/* 328:332 */     int read = fin.read(b);
/* 329:333 */     this.url = url;
/* 330:334 */     this.con = this.url.openConnection();
/* 331:    */     
/* 332:336 */     this.con.setDoOutput(true);
/* 333:337 */     this.con.setDoInput(true);
/* 334:    */     
/* 335:339 */     this.con.setUseCaches(false);
/* 336:    */     
/* 337:341 */     this.con.setRequestProperty("Cookie", this.sessionID);
/* 338:342 */     this.con.setRequestProperty("Content-Type", 
/* 339:343 */       "application/binary");
/* 340:    */     
/* 341:345 */     this.con.setRequestProperty("Content-Length", 
/* 342:346 */       "20000");
/* 343:347 */     this.con.setRequestProperty("Connection", "Keep-Alive");
/* 344:348 */     this.logger.info(Integer.valueOf(read));
/* 345:349 */     while (read > 0)
/* 346:    */     {
/* 347:351 */       this.logger.info(Integer.valueOf(read));
/* 348:    */       
/* 349:353 */       this.con.getOutputStream().write(b, 0, read);
/* 350:354 */       read = fin.read(b);
/* 351:    */     }
/* 352:357 */     this.in = new BufferedReader(new InputStreamReader(this.con.getInputStream()));
/* 353:    */   }
/* 354:    */   
/* 355:    */   public void uploadFileToServer(File file)
/* 356:    */     throws Exception
/* 357:    */   {
/* 358:363 */     FileInputStream fin = new FileInputStream(file);
/* 359:364 */     this.fout10 = new FileOutputStream("d:\\tmp\\test11.txt");
/* 360:365 */     Server2 server2 = new Server2(fin, this.fout10);
/* 361:366 */     server2.run();
/* 362:    */     
/* 363:368 */     fin.close();
/* 364:    */     
/* 365:370 */     this.in = new BufferedReader(new InputStreamReader(new FileInputStream("d:\\tmp\\test11.txt")));
/* 366:    */   }
/* 367:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.MsgChannel
 * JD-Core Version:    0.7.0.1
 */