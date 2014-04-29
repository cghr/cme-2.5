/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.resource.Base64FileEncoder;
/*   5:    */ import java.io.DataInputStream;
/*   6:    */ import java.io.DataOutputStream;
/*   7:    */ import java.io.File;
/*   8:    */ import java.io.FileInputStream;
/*   9:    */ import java.io.FileWriter;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.InputStream;
/*  12:    */ import java.io.OutputStream;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.net.ServerSocket;
/*  15:    */ import java.net.Socket;
/*  16:    */ import java.text.SimpleDateFormat;
/*  17:    */ import java.util.Calendar;
/*  18:    */ import java.util.Date;
/*  19:    */ import java.util.Hashtable;
/*  20:    */ import java.util.StringTokenizer;
/*  21:    */ import java.util.TimeZone;
/*  22:    */ import java.util.zip.ZipEntry;
/*  23:    */ import javax.servlet.ServletContext;
/*  24:    */ import javax.servlet.http.HttpServletRequest;
/*  25:    */ import javax.servlet.http.HttpServletResponse;
/*  26:    */ import javax.servlet.http.HttpSession;
/*  27:    */ import net.xoetrope.xui.data.XBaseModel;
/*  28:    */ import net.xoetrope.xui.data.XModel;
/*  29:    */ import org.apache.log4j.Logger;
/*  30:    */ import org.springframework.web.servlet.ModelAndView;
/*  31:    */ import org.springframework.web.servlet.mvc.Controller;
/*  32:    */ 
/*  33:    */ public class RepServer
/*  34:    */   implements Runnable, Controller
/*  35:    */ {
/*  36: 36 */   Logger logger = Logger.getLogger(getClass().getName());
/*  37: 37 */   public Socket sock = null;
/*  38: 38 */   public DataOutputStream dout = null;
/*  39: 39 */   int count = 0;
/*  40: 40 */   public String state = "headers";
/*  41: 41 */   public String currentLogId = "";
/*  42: 42 */   public StringBuffer log = new StringBuffer();
/*  43: 43 */   public String headers = "";
/*  44: 44 */   public Hashtable headers1 = new Hashtable();
/*  45: 45 */   String path = "c:/test/repotest";
/*  46: 47 */   MsgChannel mc = null;
/*  47:    */   
/*  48:    */   public void deliver() {}
/*  49:    */   
/*  50:    */   public void startLog()
/*  51:    */     throws Exception
/*  52:    */   {
/*  53: 56 */     this.logger.info("Start ");
/*  54: 57 */     if (this.state.equals("Started"))
/*  55:    */     {
/*  56: 59 */       this.dout.writeBytes("Error: Two starts in one message\n");
/*  57: 60 */       return;
/*  58:    */     }
/*  59: 63 */     this.state = "Started";
/*  60: 64 */     this.count += 1;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void endLog()
/*  64:    */     throws Exception
/*  65:    */   {
/*  66: 69 */     if (!this.state.equals("Started"))
/*  67:    */     {
/*  68: 71 */       this.dout.writeBytes("Error: End without start\n");
/*  69: 72 */       return;
/*  70:    */     }
/*  71: 75 */     this.state = "ended";
/*  72: 77 */     if (!saveLog()) {
/*  73: 79 */       this.dout.writeBytes("Error: while saving " + this.currentLogId + "\n");
/*  74:    */     }
/*  75: 80 */     this.currentLogId = "";
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean saveLog()
/*  79:    */   {
/*  80: 85 */     return true;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void handleHeader(String header)
/*  84:    */   {
/*  85: 89 */     this.logger.info("Header " + header);
/*  86: 90 */     if (!header.trim().equals(""))
/*  87:    */     {
/*  88: 92 */       this.headers += header;
/*  89: 93 */       StringTokenizer st = new StringTokenizer(header, ":");
/*  90: 94 */       String key = st.nextToken();
/*  91: 95 */       String value = st.nextToken();
/*  92: 96 */       this.headers1.put(key, value);
/*  93:    */     }
/*  94:    */     else
/*  95:    */     {
/*  96:100 */       this.logger.info("Headers complete ");
/*  97:101 */       this.state = "";
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void handleLine(String line)
/* 102:    */     throws Exception
/* 103:    */   {
/* 104:106 */     if (line.startsWith("<l "))
/* 105:    */     {
/* 106:108 */       startLog();
/* 107:109 */       this.log.append(line + "\n");
/* 108:    */     }
/* 109:111 */     else if (line.startsWith("</l"))
/* 110:    */     {
/* 111:113 */       this.log.append(line + "\n");
/* 112:114 */       endLog();
/* 113:    */     }
/* 114:    */     else
/* 115:    */     {
/* 116:117 */       this.log.append(line + "\n");
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   public RepServer(InputStream in, OutputStream out)
/* 121:    */     throws Exception
/* 122:    */   {
/* 123:124 */     this.mc = new MsgChannel(in, out, 0);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public RepServer(Socket sock1)
/* 127:    */     throws Exception
/* 128:    */   {
/* 129:128 */     this.sock = sock1;
/* 130:129 */     this.mc = new MsgChannel(this.sock, 0);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public RepServer() {}
/* 134:    */   
/* 135:    */   public void run()
/* 136:    */   {
/* 137:    */     try
/* 138:    */     {
/* 139:140 */       String line = this.mc.readLine();int count = 0;
/* 140:142 */       while ((line != null) && (this.state.equals("headers")))
/* 141:    */       {
/* 142:144 */         if (this.state.equals("headers")) {
/* 143:146 */           handleHeader(line);
/* 144:    */         }
/* 145:148 */         if (!this.state.equals("headers")) {
/* 146:    */           break;
/* 147:    */         }
/* 148:151 */         line = this.mc.readLine();
/* 149:152 */         count++;
/* 150:    */       }
/* 151:155 */       Date syncdate = new SimpleDateFormat("yyyy-MM-dd").parse((String)this.headers1.get("lastsynctime"));
/* 152:156 */       Calendar cal = Calendar.getInstance();
/* 153:157 */       cal.setTime(syncdate);
/* 154:158 */       cal.add(5, 1);
/* 155:159 */       syncdate = cal.getTime();
/* 156:    */       
/* 157:161 */       long sync = syncdate.getTime();
/* 158:162 */       String repoPath = this.path + "/repo";
/* 159:163 */       String encodedRepoPath = this.path + "/repoencoded";
/* 160:164 */       String[] pathsToCheck = { "lib", "app_lib", "pages", "resources", "sql" };
/* 161:165 */       String participant = (String)this.headers1.get("participant");
/* 162:166 */       for (int i = 0; i < pathsToCheck.length; i++)
/* 163:    */       {
/* 164:168 */         File dir = new File(repoPath + "/" + pathsToCheck[i]);
/* 165:169 */         this.logger.info(" Dir " + dir.getAbsolutePath());
/* 166:171 */         if (dir.exists())
/* 167:    */         {
/* 168:173 */           this.logger.info(" Dir found ");
/* 169:174 */           File[] files = dir.listFiles();
/* 170:175 */           for (int j = 0; j < files.length; j++)
/* 171:    */           {
/* 172:177 */             String lastSyncTime1 = "";
/* 173:178 */             XModel xm = new XBaseModel();
/* 174:    */             
/* 175:180 */             TestXUIDB.getInstance().getData("reposync_status", "max(date)", "recepient ='" + participant + "' and message='Sent " + pathsToCheck[i] + "/" + files[j].getName() + "' and status='success'", xm);
/* 176:    */             
/* 177:182 */             lastSyncTime1 = (String)xm.get(0).get(0).get();
/* 178:    */             
/* 179:184 */             log("Started " + pathsToCheck[i] + "/" + files[j].getName(), "success");
/* 180:185 */             File encoded = new File(encodedRepoPath + "/" + pathsToCheck[i] + "/" + files[j].getName() + ".txt");
/* 181:186 */             this.logger.info(" Encoded file " + encoded.getAbsolutePath());
/* 182:187 */             if ((!encoded.exists()) || (encoded.lastModified() < files[j].lastModified()))
/* 183:    */             {
/* 184:188 */               this.logger.info(" Not there ");
/* 185:189 */               Base64FileEncoder.encodeFile(files[j].getAbsolutePath(), encoded.getAbsolutePath());
/* 186:    */             }
/* 187:191 */             long mod = files[j].lastModified();
/* 188:192 */             this.logger.info(mod + " --- " + sync + (mod - sync));
/* 189:193 */             if (mod > sync)
/* 190:    */             {
/* 191:195 */               this.mc.putNextEntry(pathsToCheck[i] + "/" + files[j].getName() + ".txt");
/* 192:196 */               FileInputStream fr = new FileInputStream(encoded);
/* 193:197 */               DataInputStream fin = new DataInputStream(fr);
/* 194:198 */               String fline = fin.readLine();
/* 195:199 */               while (fline != null)
/* 196:    */               {
/* 197:201 */                 this.mc.writeBytes(fline + "\n");
/* 198:202 */                 fline = fin.readLine();
/* 199:    */               }
/* 200:204 */               fin.close();
/* 201:205 */               this.mc.closeEntry();
/* 202:206 */               this.logger.info("dELIVERED");
/* 203:207 */               log("Sent " + pathsToCheck[i] + "/" + files[j].getName(), "success");
/* 204:    */             }
/* 205:    */           }
/* 206:    */         }
/* 207:    */       }
/* 208:216 */       ZipEntry ze = new ZipEntry("Complete");
/* 209:217 */       this.mc.putNextEntry("Complete");
/* 210:218 */       this.mc.writeBytes("Complete\n");
/* 211:219 */       this.mc.closeEntry();
/* 212:    */       
/* 213:221 */       String status = this.mc.readLine();
/* 214:222 */       this.logger.info(status);
/* 215:223 */       this.mc.close();
/* 216:    */     }
/* 217:    */     catch (Exception e)
/* 218:    */     {
/* 219:227 */       log(e.getMessage(), "failed");
/* 220:228 */       e.printStackTrace();
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   public void log(String message, String status)
/* 225:    */   {
/* 226:234 */     XModel dataM = new XBaseModel();
/* 227:235 */     String participant = (String)this.headers1.get("participant");
/* 228:236 */     dataM.set("recepient", participant);
/* 229:237 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 230:238 */     String date = sdf.format(new Date());
/* 231:239 */     dataM.set("date", date);
/* 232:    */     
/* 233:241 */     dataM.set("message", message);
/* 234:242 */     dataM.set("status", status);
/* 235:    */     try
/* 236:    */     {
/* 237:244 */       TestXUIDB.getInstance().saveDataM2("reposync_status", "false ", dataM);
/* 238:    */     }
/* 239:    */     catch (Exception e)
/* 240:    */     {
/* 241:247 */       e.printStackTrace();
/* 242:    */     }
/* 243:    */   }
/* 244:    */   
/* 245:    */   public static void checkSetup()
/* 246:    */     throws Exception
/* 247:    */   {
/* 248:252 */     TestXUIDB.getInstance().checkDBServer();
/* 249:253 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
/* 250:254 */     String name = "tt" + sdf.format(new Date());
/* 251:255 */     FileWriter fw = new FileWriter(name);
/* 252:256 */     fw.write("test");
/* 253:257 */     fw.close();
/* 254:258 */     File file = new File(name);
/* 255:259 */     file.delete();
/* 256:    */   }
/* 257:    */   
/* 258:    */   public static void main(String[] args)
/* 259:    */     throws IOException
/* 260:    */   {
/* 261:265 */     System.out.println(" " + TimeZone.getDefault().inDaylightTime(new Date()));
/* 262:    */     try
/* 263:    */     {
/* 264:267 */       ServerSocket ss = new ServerSocket(8087);
/* 265:    */       for (;;)
/* 266:    */       {
/* 267:270 */         Socket s = ss.accept();
/* 268:271 */         RepServer srv = new RepServer(s);
/* 269:272 */         srv.sock = s;
/* 270:273 */         new Thread(srv).run();
/* 271:    */       }
/* 272:    */     }
/* 273:    */     catch (Exception e)
/* 274:    */     {
/* 275:278 */       System.out.println("Exiting......");
/* 276:279 */       e.printStackTrace();
/* 277:280 */       System.exit(0);
/* 278:    */     }
/* 279:    */   }
/* 280:    */   
/* 281:    */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 282:    */     throws Exception
/* 283:    */   {
/* 284:287 */     RepServer rep = new RepServer(request.getInputStream(), response.getOutputStream());
/* 285:288 */     rep.path = request.getSession().getServletContext().getRealPath("/");
/* 286:289 */     rep.run();
/* 287:290 */     return null;
/* 288:    */   }
/* 289:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.RepServer
 * JD-Core Version:    0.7.0.1
 */