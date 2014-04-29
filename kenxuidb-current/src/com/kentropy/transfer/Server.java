/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import java.io.DataInputStream;
/*   5:    */ import java.io.DataOutputStream;
/*   6:    */ import java.io.File;
/*   7:    */ import java.io.FileInputStream;
/*   8:    */ import java.io.FileWriter;
/*   9:    */ import java.io.InputStream;
/*  10:    */ import java.io.OutputStream;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.net.ServerSocket;
/*  13:    */ import java.net.Socket;
/*  14:    */ import java.text.SimpleDateFormat;
/*  15:    */ import java.util.Date;
/*  16:    */ import java.util.Hashtable;
/*  17:    */ import java.util.StringTokenizer;
/*  18:    */ import java.util.Vector;
/*  19:    */ import java.util.zip.ZipEntry;
/*  20:    */ import java.util.zip.ZipOutputStream;
/*  21:    */ import org.apache.log4j.Logger;
/*  22:    */ 
/*  23:    */ public class Server
/*  24:    */   implements Runnable
/*  25:    */ {
/*  26: 26 */   Logger logger = Logger.getLogger(getClass().getName());
/*  27: 27 */   public Socket sock = null;
/*  28: 28 */   public DataOutputStream dout = null;
/*  29: 29 */   int count = 0;
/*  30: 30 */   public String state = "headers";
/*  31: 31 */   public String currentLogId = "";
/*  32: 32 */   public StringBuffer log = new StringBuffer();
/*  33: 33 */   public String headers = "";
/*  34: 34 */   public Hashtable headers1 = new Hashtable();
/*  35: 35 */   String path = ".";
/*  36: 37 */   MsgChannel mc = null;
/*  37:    */   
/*  38:    */   public void deliver() {}
/*  39:    */   
/*  40:    */   public void startLog()
/*  41:    */     throws Exception
/*  42:    */   {
/*  43: 46 */     this.logger.info("Start ");
/*  44: 47 */     if (this.state.equals("Started"))
/*  45:    */     {
/*  46: 49 */       this.dout.writeBytes("Error: Two starts in one message\n");
/*  47: 50 */       return;
/*  48:    */     }
/*  49: 53 */     this.state = "Started";
/*  50: 54 */     this.count += 1;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void endLog()
/*  54:    */     throws Exception
/*  55:    */   {
/*  56: 59 */     if (!this.state.equals("Started"))
/*  57:    */     {
/*  58: 61 */       this.dout.writeBytes("Error: End without start\n");
/*  59: 62 */       return;
/*  60:    */     }
/*  61: 65 */     this.state = "ended";
/*  62: 67 */     if (!saveLog()) {
/*  63: 69 */       this.dout.writeBytes("Error: while saving " + this.currentLogId + "\n");
/*  64:    */     }
/*  65: 70 */     this.currentLogId = "";
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean saveLog()
/*  69:    */   {
/*  70: 75 */     return true;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void handleHeader(String header)
/*  74:    */   {
/*  75: 79 */     this.logger.info("Header " + header);
/*  76: 80 */     if (!header.trim().equals(""))
/*  77:    */     {
/*  78: 82 */       this.headers += header;
/*  79: 83 */       StringTokenizer st = new StringTokenizer(header, ":");
/*  80: 84 */       String key = st.nextToken();
/*  81: 85 */       String value = st.nextToken();
/*  82: 86 */       this.headers1.put(key, value);
/*  83:    */     }
/*  84:    */     else
/*  85:    */     {
/*  86: 90 */       this.logger.info("Headers complete ");
/*  87: 91 */       this.state = "";
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void handleLine(String line)
/*  92:    */     throws Exception
/*  93:    */   {
/*  94: 96 */     if (line.startsWith("<l "))
/*  95:    */     {
/*  96: 98 */       startLog();
/*  97: 99 */       this.log.append(line + "\n");
/*  98:    */     }
/*  99:101 */     else if (line.startsWith("</l"))
/* 100:    */     {
/* 101:103 */       this.log.append(line + "\n");
/* 102:104 */       endLog();
/* 103:    */     }
/* 104:    */     else
/* 105:    */     {
/* 106:107 */       this.log.append(line + "\n");
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void run2()
/* 111:    */   {
/* 112:    */     try
/* 113:    */     {
/* 114:115 */       DataInputStream in = new DataInputStream(this.sock.getInputStream());
/* 115:116 */       ZipOutputStream zout = new ZipOutputStream(this.sock.getOutputStream());
/* 116:117 */       this.dout = new DataOutputStream(zout);
/* 117:118 */       String line = in.readLine();int count = 0;
/* 118:120 */       while ((line != null) && (this.state.equals("headers")))
/* 119:    */       {
/* 120:122 */         if (this.state.equals("headers")) {
/* 121:124 */           handleHeader(line);
/* 122:    */         }
/* 123:126 */         if (!this.state.equals("headers")) {
/* 124:    */           break;
/* 125:    */         }
/* 126:129 */         line = in.readLine();
/* 127:130 */         count++;
/* 128:    */       }
/* 129:133 */       Vector v = TestXUIDB.getInstance().getMessages((String)this.headers1.get("participant"));
/* 130:    */       
/* 131:135 */       this.logger.info(" Size " + v.size());
/* 132:136 */       for (int i = 0; i < v.size(); i++)
/* 133:    */       {
/* 134:138 */         ZipEntry ze = new ZipEntry(v.get(i).toString());
/* 135:    */         
/* 136:140 */         zout.putNextEntry(ze);
/* 137:141 */         FileInputStream fr = new FileInputStream(v.get(i).toString());
/* 138:142 */         DataInputStream fin = new DataInputStream(fr);
/* 139:143 */         String fline = fin.readLine();
/* 140:144 */         while (fline != null)
/* 141:    */         {
/* 142:146 */           this.dout.writeBytes(fline + "\n");
/* 143:147 */           fline = fin.readLine();
/* 144:    */         }
/* 145:149 */         fin.close();
/* 146:150 */         zout.closeEntry();
/* 147:151 */         TestXUIDB.getInstance().updateMessageStatus(v.get(i).toString(), (String)this.headers1.get("participant"));
/* 148:    */       }
/* 149:154 */       ZipEntry ze = new ZipEntry("Complete");
/* 150:155 */       zout.putNextEntry(ze);
/* 151:156 */       this.dout.writeBytes("Complete\n");
/* 152:157 */       zout.closeEntry();
/* 153:    */       
/* 154:159 */       this.logger.info("dELIVERED");
/* 155:    */       
/* 156:161 */       String status = in.readLine();
/* 157:162 */       this.logger.info(status);
/* 158:163 */       this.sock.close();
/* 159:    */     }
/* 160:    */     catch (Exception e)
/* 161:    */     {
/* 162:167 */       e.printStackTrace();
/* 163:    */     }
/* 164:    */   }
/* 165:    */   
/* 166:    */   public Server(InputStream in, OutputStream out)
/* 167:    */     throws Exception
/* 168:    */   {
/* 169:174 */     this.mc = new MsgChannel(in, out, 0);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public Server(Socket sock1)
/* 173:    */     throws Exception
/* 174:    */   {
/* 175:178 */     this.sock = sock1;
/* 176:179 */     this.mc = new MsgChannel(this.sock, 0);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void run()
/* 180:    */   {
/* 181:    */     try
/* 182:    */     {
/* 183:186 */       String line = this.mc.readLine();int count = 0;
/* 184:188 */       while ((line != null) && (this.state.equals("headers")))
/* 185:    */       {
/* 186:190 */         if (this.state.equals("headers")) {
/* 187:192 */           handleHeader(line);
/* 188:    */         }
/* 189:194 */         if (!this.state.equals("headers")) {
/* 190:    */           break;
/* 191:    */         }
/* 192:197 */         line = this.mc.readLine();
/* 193:198 */         count++;
/* 194:    */       }
/* 195:201 */       System.out.println("OP >> " + this.headers1.get("op"));
/* 196:202 */       if ((this.headers1.get("op") != null) && (this.headers1.get("op").equals("acknowledge")))
/* 197:    */       {
/* 198:204 */         updateAckStatus(this.headers1.get("downloads").toString());
/* 199:205 */         return;
/* 200:    */       }
/* 201:207 */       Vector v = TestXUIDB.getInstance().getMessages((String)this.headers1.get("participant"));
/* 202:    */       
/* 203:209 */       this.logger.info(" Size " + v.size());
/* 204:210 */       for (int i = 0; i < v.size(); i++)
/* 205:    */       {
/* 206:212 */         this.mc.putNextEntry(v.get(i).toString());
/* 207:213 */         FileInputStream fr = new FileInputStream(this.path + "/" + v.get(i).toString());
/* 208:214 */         DataInputStream fin = new DataInputStream(fr);
/* 209:215 */         String fline = fin.readLine();
/* 210:216 */         while (fline != null)
/* 211:    */         {
/* 212:218 */           this.mc.writeBytes(fline + "\n");
/* 213:219 */           fline = fin.readLine();
/* 214:    */         }
/* 215:221 */         fin.close();
/* 216:222 */         this.mc.closeEntry();
/* 217:    */       }
/* 218:225 */       ZipEntry ze = new ZipEntry("Complete");
/* 219:226 */       this.mc.putNextEntry("Complete");
/* 220:227 */       this.mc.writeBytes("Complete\n");
/* 221:228 */       this.mc.closeEntry();
/* 222:229 */       this.mc.flush();
/* 223:    */       
/* 224:231 */       this.logger.info("dELIVERED");
/* 225:233 */       for (int i = 0; i < v.size(); i++) {
/* 226:235 */         TestXUIDB.getInstance().updateMessageStatus(v.get(i).toString(), (String)this.headers1.get("participant"));
/* 227:    */       }
/* 228:238 */       this.logger.info("after importing");
/* 229:239 */       this.mc.close();
/* 230:    */     }
/* 231:    */     catch (Exception e)
/* 232:    */     {
/* 233:243 */       e.printStackTrace();
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   private void updateAckStatus(String downloads)
/* 238:    */     throws Exception
/* 239:    */   {
/* 240:248 */     String[] tt = downloads.split(",");
/* 241:249 */     for (int i = 0; i < tt.length; i++) {
/* 242:251 */       TestXUIDB.getInstance().updateAckStatus(tt[i], (String)this.headers1.get("participant"));
/* 243:    */     }
/* 244:    */   }
/* 245:    */   
/* 246:    */   public static void checkSetup()
/* 247:    */     throws Exception
/* 248:    */   {
/* 249:258 */     TestXUIDB.getInstance().checkDBServer();
/* 250:259 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
/* 251:260 */     String name = "tt" + sdf.format(new Date());
/* 252:261 */     FileWriter fw = new FileWriter(name);
/* 253:262 */     fw.write("test");
/* 254:263 */     fw.close();
/* 255:264 */     File file = new File(name);
/* 256:265 */     file.delete();
/* 257:    */   }
/* 258:    */   
/* 259:    */   public static void main(String[] args)
/* 260:    */   {
/* 261:    */     try
/* 262:    */     {
/* 263:270 */       checkSetup();
/* 264:271 */       ServerSocket ss = new ServerSocket(8087);
/* 265:    */       for (;;)
/* 266:    */       {
/* 267:274 */         Socket s = ss.accept();
/* 268:275 */         Server srv = new Server(s);
/* 269:276 */         srv.sock = s;
/* 270:277 */         new Thread(srv).run();
/* 271:    */       }
/* 272:    */     }
/* 273:    */     catch (Exception e)
/* 274:    */     {
/* 275:282 */       System.out.println("Exiting......");
/* 276:283 */       e.printStackTrace();
/* 277:284 */       System.exit(0);
/* 278:    */     }
/* 279:    */   }
/* 280:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.Server
 * JD-Core Version:    0.7.0.1
 */