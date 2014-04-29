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
/*  19:    */ 
/*  20:    */ public class Server3
/*  21:    */   implements Runnable
/*  22:    */ {
/*  23: 23 */   public Socket sock = null;
/*  24: 24 */   public DataOutputStream dout = null;
/*  25: 25 */   int count = 0;
/*  26: 26 */   public String state = "headers";
/*  27: 27 */   public String currentLogId = "";
/*  28: 28 */   public StringBuffer log = new StringBuffer();
/*  29: 29 */   public String headers = "";
/*  30: 30 */   public Hashtable headers1 = new Hashtable();
/*  31: 31 */   public String path = ".";
/*  32: 33 */   MsgChannel mc = null;
/*  33: 35 */   boolean isLog = false;
/*  34: 37 */   public static Vector sessions = new Vector();
/*  35:    */   
/*  36:    */   public void deliver() {}
/*  37:    */   
/*  38:    */   public void startLog()
/*  39:    */     throws Exception
/*  40:    */   {
/*  41: 46 */     System.out.println("Start ");
/*  42: 47 */     if (this.state.equals("Started"))
/*  43:    */     {
/*  44: 49 */       this.dout.writeBytes("Error: Two starts in one message\n");
/*  45: 50 */       return;
/*  46:    */     }
/*  47: 53 */     this.state = "Started";
/*  48: 54 */     this.count += 1;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void endLog()
/*  52:    */     throws Exception
/*  53:    */   {
/*  54: 59 */     if (!this.state.equals("Started"))
/*  55:    */     {
/*  56: 61 */       this.dout.writeBytes("Error: End without start\n");
/*  57: 62 */       return;
/*  58:    */     }
/*  59: 65 */     this.state = "ended";
/*  60: 67 */     if (!saveLog()) {
/*  61: 69 */       this.dout.writeBytes("Error: while saving " + this.currentLogId + "\n");
/*  62:    */     }
/*  63: 70 */     this.currentLogId = "";
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean saveLog()
/*  67:    */   {
/*  68: 75 */     return true;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void handleHeader(String header)
/*  72:    */   {
/*  73: 79 */     System.out.println("Header " + header);
/*  74: 80 */     if (!header.trim().equals(""))
/*  75:    */     {
/*  76: 82 */       this.headers += header;
/*  77: 83 */       StringTokenizer st = new StringTokenizer(header, ":");
/*  78: 84 */       String key = st.nextToken();
/*  79: 85 */       String value = st.nextToken();
/*  80: 86 */       this.headers1.put(key, value);
/*  81:    */     }
/*  82:    */     else
/*  83:    */     {
/*  84: 90 */       this.state = "";
/*  85: 91 */       System.out.println(" >> msg type" + this.headers1.get("msg-type"));
/*  86:    */       
/*  87: 93 */       this.isLog = ((this.headers1.get("msg-type") != null) && (this.headers1.get("msg-type").equals("changelogs")));
/*  88: 94 */       if (this.isLog) {
/*  89: 95 */         this.log.append("<logs>");
/*  90:    */       }
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void handleLine(String line)
/*  95:    */     throws Exception
/*  96:    */   {
/*  97:100 */     if (line.startsWith("<l "))
/*  98:    */     {
/*  99:102 */       startLog();
/* 100:103 */       this.log.append(line + "\n");
/* 101:    */     }
/* 102:105 */     else if (line.startsWith("</l"))
/* 103:    */     {
/* 104:107 */       this.log.append(line + "\n");
/* 105:108 */       endLog();
/* 106:    */     }
/* 107:    */     else
/* 108:    */     {
/* 109:111 */       this.log.append(line + "\n");
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public Server3(InputStream in, OutputStream out)
/* 114:    */     throws Exception
/* 115:    */   {
/* 116:118 */     this.mc = new MsgChannel(in, out, 1);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public Server3(Socket sock1)
/* 120:    */     throws Exception
/* 121:    */   {
/* 122:122 */     this.sock = sock1;
/* 123:123 */     this.mc = new MsgChannel(this.sock, 1);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void run()
/* 127:    */   {
/* 128:127 */     String op = null;
/* 129:128 */     String user = null;
/* 130:129 */     String password = null;
/* 131:    */     try
/* 132:    */     {
/* 133:132 */       DataInputStream in = new DataInputStream(this.sock.getInputStream());
/* 134:133 */       this.dout = new DataOutputStream(this.sock.getOutputStream());
/* 135:134 */       String line = in.readLine();int count = 0;
/* 136:136 */       while (line != null)
/* 137:    */       {
/* 138:138 */         if (this.state.equals("headers")) {
/* 139:140 */           handleHeader(line);
/* 140:    */         } else {
/* 141:144 */           handleLine(line);
/* 142:    */         }
/* 143:146 */         line = in.readLine();
/* 144:147 */         count++;
/* 145:    */       }
/* 146:150 */       System.out.println("Line count " + count);
/* 147:    */       
/* 148:152 */       op = (String)this.headers1.get("op");
/* 149:153 */       user = (String)this.headers1.get("user");
/* 150:154 */       password = (String)this.headers1.get("password");
/* 151:    */       
/* 152:156 */       sessions.add(user + "-" + password);
/* 153:157 */       Thread.currentThread();Thread.sleep(10000L);
/* 154:    */     }
/* 155:    */     catch (Exception e)
/* 156:    */     {
/* 157:161 */       sessions.remove(user + "-" + password);
/* 158:162 */       e.printStackTrace();
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   public static void checkSetup()
/* 163:    */     throws Exception
/* 164:    */   {
/* 165:168 */     TestXUIDB.getInstance().checkDBServer();
/* 166:169 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
/* 167:170 */     String name = "tt" + sdf.format(new Date());
/* 168:171 */     FileWriter fw = new FileWriter(name);
/* 169:172 */     fw.close();
/* 170:173 */     File file = new File(name);
/* 171:174 */     file.delete();
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static void main(String[] args)
/* 175:    */   {
/* 176:    */     try
/* 177:    */     {
/* 178:    */       
/* 179:182 */       if (args.length > 1) {
/* 180:184 */         if (args[0].equals("import")) {
/* 181:186 */           if (args[1] != null)
/* 182:    */           {
/* 183:188 */             StringBuffer logs = new StringBuffer();
/* 184:189 */             DataInputStream din = new DataInputStream(new FileInputStream(args[1]));
/* 185:190 */             String line = "";
/* 186:191 */             while (line != null)
/* 187:    */             {
/* 188:193 */               line = din.readLine();
/* 189:195 */               if (line != null) {
/* 190:197 */                 logs.append(line + "\r\n");
/* 191:    */               }
/* 192:    */             }
/* 193:200 */             TestXUIDB.getInstance().importChangeLogs(logs.toString());
/* 194:201 */             return;
/* 195:    */           }
/* 196:    */         }
/* 197:    */       }
/* 198:205 */       ServerSocket ss = new ServerSocket(8086);
/* 199:    */       for (;;)
/* 200:    */       {
/* 201:208 */         Socket s = ss.accept();
/* 202:209 */         Server3 srv = new Server3(s);
/* 203:210 */         srv.sock = s;
/* 204:211 */         new Thread(srv).run();
/* 205:    */       }
/* 206:221 */       return;
/* 207:    */     }
/* 208:    */     catch (Exception e)
/* 209:    */     {
/* 210:217 */       System.out.println("Exiting......");
/* 211:218 */       e.printStackTrace();
/* 212:219 */       System.exit(0);
/* 213:    */     }
/* 214:    */   }
/* 215:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.Server3
 * JD-Core Version:    0.7.0.1
 */