/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import java.io.DataInputStream;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileNotFoundException;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.net.MalformedURLException;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Properties;
/*  11:    */ import java.util.Vector;
/*  12:    */ import net.xoetrope.xui.data.XBaseModel;
/*  13:    */ import net.xoetrope.xui.data.XModel;
/*  14:    */ 
/*  15:    */ public class TestConsole
/*  16:    */ {
/*  17: 17 */   Properties props = new Properties();
/*  18:    */   
/*  19:    */   public TestConsole(String file)
/*  20:    */   {
/*  21:    */     try
/*  22:    */     {
/*  23: 21 */       loadProperties(file);
/*  24:    */     }
/*  25:    */     catch (FileNotFoundException e)
/*  26:    */     {
/*  27: 24 */       e.printStackTrace();
/*  28:    */     }
/*  29:    */     catch (IOException e)
/*  30:    */     {
/*  31: 27 */       e.printStackTrace();
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void loadProperties(String file)
/*  36:    */     throws FileNotFoundException, IOException
/*  37:    */   {
/*  38: 32 */     this.props = new Properties();
/*  39: 33 */     FileInputStream fin = new FileInputStream(file);
/*  40: 34 */     this.props.load(fin);
/*  41: 35 */     fin.close();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String parseCommand(String cmd)
/*  45:    */   {
/*  46: 40 */     for (Map.Entry e : this.props.entrySet())
/*  47:    */     {
/*  48: 42 */       String key = (String)e.getKey();
/*  49: 43 */       cmd = cmd.replaceAll("#" + key, e.getValue().toString());
/*  50:    */     }
/*  51: 47 */     return cmd;
/*  52:    */   }
/*  53:    */   
/*  54: 49 */   TransactionClient cl = new TransactionClient();
/*  55:    */   
/*  56:    */   public void execRefreshProcess(String[] tt)
/*  57:    */   {
/*  58: 52 */     String[] processes = tt[2].split(",");
/*  59: 53 */     XModel xmlProcessWrite = new XBaseModel();
/*  60: 54 */     for (int k = 0; k < processes.length; k++) {
/*  61:    */       try
/*  62:    */       {
/*  63: 57 */         XModel xm1 = this.cl.getRefreshProcessesTr(" pid ='" + processes[k] + "'");
/*  64: 58 */         xmlProcessWrite.append(xm1);
/*  65:    */       }
/*  66:    */       catch (Exception e)
/*  67:    */       {
/*  68: 61 */         e.printStackTrace();
/*  69:    */       }
/*  70:    */     }
/*  71: 66 */     TransactionClient cl1 = new TransactionClient();
/*  72: 67 */     cl1.SERVER_URL = tt[1];
/*  73:    */     try
/*  74:    */     {
/*  75: 69 */       cl1.execute(xmlProcessWrite);
/*  76:    */     }
/*  77:    */     catch (MalformedURLException e)
/*  78:    */     {
/*  79: 72 */       e.printStackTrace();
/*  80:    */     }
/*  81:    */     catch (IOException e)
/*  82:    */     {
/*  83: 75 */       e.printStackTrace();
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void execRefreshProcess1(String[] tt)
/*  88:    */   {
/*  89:    */     try
/*  90:    */     {
/*  91: 82 */       XModel xm = new IntegrationService().readTable(tt[1], "process_errors", "pid", "errorstatus='error'");
/*  92:    */       
/*  93: 84 */       XModel xmlProcessWrite = new XBaseModel();
/*  94: 85 */       for (int i = 0; i < xm.getNumChildren(); i++)
/*  95:    */       {
/*  96: 87 */         for (int j = 0; j < xm.get(i).getNumChildren(); j++) {
/*  97:    */           try
/*  98:    */           {
/*  99: 90 */             XModel xm1 = new TransactionClient().getRefreshProcessesTr(" pid ='" + xm.get(i).get(j).get(0).get() + "'");
/* 100: 91 */             xmlProcessWrite.append(xm1);
/* 101:    */           }
/* 102:    */           catch (Exception e)
/* 103:    */           {
/* 104: 94 */             e.printStackTrace();
/* 105:    */           }
/* 106:    */         }
/* 107: 98 */         TransactionClient cl1 = new TransactionClient();
/* 108: 99 */         cl1.SERVER_URL = tt[1];
/* 109:100 */         cl1.execute(xmlProcessWrite);
/* 110:    */       }
/* 111:    */     }
/* 112:    */     catch (Exception e1)
/* 113:    */     {
/* 114:104 */       e1.printStackTrace();
/* 115:    */     }
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void execute(String cmd)
/* 119:    */   {
/* 120:113 */     cmd = parseCommand(cmd);
/* 121:114 */     System.out.println(cmd);
/* 122:115 */     String[] tt = cmd.split(" ");
/* 123:116 */     if (tt[0].equals("loadProps")) {
/* 124:118 */       execLoadProps(tt);
/* 125:121 */     } else if (tt[0].equals("replicate")) {
/* 126:123 */       execReplicate(tt);
/* 127:126 */     } else if (tt[0].equals("refreshProcesses")) {
/* 128:128 */       execRefreshProcess(tt);
/* 129:131 */     } else if (tt[0].equals("refreshProcesses1")) {
/* 130:133 */       execRefreshProcess1(tt);
/* 131:    */     } else {
/* 132:137 */       System.out.println(" Command not recognized");
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   private void execReplicate(String[] tt)
/* 137:    */   {
/* 138:    */     try
/* 139:    */     {
/* 140:146 */       String fromTime = tt[4].replaceAll("_", " ");
/* 141:147 */       String toTime = tt[5].replaceAll("_", " ");
/* 142:148 */       if (tt[1].equals("cmereport"))
/* 143:    */       {
/* 144:150 */         XModel container = new XBaseModel();
/* 145:151 */         container.set("@src", tt[2]);
/* 146:152 */         container.set("@dest", tt[3]);
/* 147:153 */         IntegrationService is = new IntegrationService();
/* 148:154 */         is.readCMEReport(tt[2], fromTime, toTime, container, new Vector());
/* 149:155 */         is.writeTaskAndData(tt[3], container);
/* 150:    */       }
/* 151:160 */       else if (tt[1].equals("billable"))
/* 152:    */       {
/* 153:162 */         XModel container = new XBaseModel();
/* 154:163 */         container.set("@src", tt[2]);
/* 155:164 */         container.set("@dest", tt[3]);
/* 156:165 */         IntegrationService is = new IntegrationService();
/* 157:166 */         is.readBillable(tt[2], fromTime, toTime, container, new Vector());
/* 158:167 */         is.writeTaskAndData(tt[3], container);
/* 159:    */       }
/* 160:172 */       else if (tt[1].equals("cmerecords"))
/* 161:    */       {
/* 162:174 */         XModel container = new XBaseModel();
/* 163:175 */         container.set("@src", tt[2]);
/* 164:176 */         container.set("@dest", tt[3]);
/* 165:177 */         IntegrationService is = new IntegrationService();
/* 166:178 */         is.readCMERecords(tt[2], fromTime, toTime, container);
/* 167:179 */         is.writeTaskAndData(tt[3], container);
/* 168:    */       }
/* 169:184 */       else if (tt[1].equals("report"))
/* 170:    */       {
/* 171:186 */         XModel container = new XBaseModel();
/* 172:187 */         container.set("@src", tt[2]);
/* 173:188 */         container.set("@dest", tt[3]);
/* 174:189 */         IntegrationService is = new IntegrationService();
/* 175:190 */         is.readReports(tt[2], fromTime, toTime, container);
/* 176:191 */         is.writeTaskAndData(tt[3], container);
/* 177:    */       }
/* 178:196 */       else if (tt[1].equals("cancellation"))
/* 179:    */       {
/* 180:198 */         XModel container = new XBaseModel();
/* 181:199 */         container.set("@src", tt[2]);
/* 182:200 */         container.set("@dest", tt[3]);
/* 183:201 */         IntegrationService is = new IntegrationService();
/* 184:202 */         is.readCancellations(tt[2], fromTime, toTime, container);
/* 185:203 */         is.writeTaskAndData(tt[3], container);
/* 186:    */       }
/* 187:209 */       else if (tt[1].equals("taskscompleted"))
/* 188:    */       {
/* 189:211 */         XModel container = new XBaseModel();
/* 190:212 */         container.set("@src", tt[2]);
/* 191:213 */         container.set("@dest", tt[3]);
/* 192:214 */         IntegrationService is = new IntegrationService();
/* 193:215 */         is.readTaskAndDataCompleted(tt[2], fromTime, toTime, container, new Vector());
/* 194:216 */         is.writeTaskAndData(tt[3], container);
/* 195:    */       }
/* 196:221 */       else if (tt[1].equals("tasksassigned"))
/* 197:    */       {
/* 198:223 */         XModel container = new XBaseModel();
/* 199:224 */         container.set("@src", tt[2]);
/* 200:225 */         container.set("@dest", tt[3]);
/* 201:226 */         IntegrationService is = new IntegrationService();
/* 202:227 */         is.readTasksAssigned(tt[2], fromTime, toTime, container);
/* 203:228 */         is.writeTaskAndData(tt[3], container);
/* 204:    */       }
/* 205:233 */       else if (tt[1].equals("vadata"))
/* 206:    */       {
/* 207:235 */         XModel container = new XBaseModel();
/* 208:236 */         container.set("@src", tt[2]);
/* 209:237 */         container.set("@dest", tt[3]);
/* 210:238 */         IntegrationService is = new IntegrationService();
/* 211:239 */         is.readVAKeyValues(tt[2], fromTime, toTime, container);
/* 212:240 */         is.writeTaskAndData(tt[3], container);
/* 213:    */       }
/* 214:    */     }
/* 215:    */     catch (Exception e)
/* 216:    */     {
/* 217:248 */       e.printStackTrace();
/* 218:    */     }
/* 219:    */   }
/* 220:    */   
/* 221:    */   private void execLoadProps(String[] tt)
/* 222:    */   {
/* 223:    */     try
/* 224:    */     {
/* 225:256 */       loadProperties(tt[1]);
/* 226:    */     }
/* 227:    */     catch (FileNotFoundException e)
/* 228:    */     {
/* 229:259 */       e.printStackTrace();
/* 230:    */     }
/* 231:    */     catch (IOException e)
/* 232:    */     {
/* 233:262 */       e.printStackTrace();
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   public void run()
/* 238:    */   {
/* 239:    */     try
/* 240:    */     {
/* 241:    */       for (;;)
/* 242:    */       {
/* 243:272 */         DataInputStream din = new DataInputStream(System.in);
/* 244:    */         
/* 245:274 */         String cmd = din.readLine();
/* 246:275 */         execute(cmd);
/* 247:    */       }
/* 248:    */     }
/* 249:    */     catch (IOException e)
/* 250:    */     {
/* 251:278 */       e.printStackTrace();
/* 252:    */     }
/* 253:    */   }
/* 254:    */   
/* 255:    */   public static void main(String[] args)
/* 256:    */   {
/* 257:287 */     new TestConsole("cme-phy-agent.properties").run();
/* 258:    */   }
/* 259:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.TestConsole
 * JD-Core Version:    0.7.0.1
 */