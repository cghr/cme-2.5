/*   1:    */ package com.kentropy.scheduler.jobs;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.process.Process;
/*   5:    */ import com.kentropy.scheduler.Job;
/*   6:    */ import com.kentropy.scheduler.Scheduler;
/*   7:    */ import com.kentropy.security.client.UserAuthentication;
/*   8:    */ import java.io.File;
/*   9:    */ import java.io.PrintStream;
/*  10:    */ import java.sql.SQLException;
/*  11:    */ import java.text.SimpleDateFormat;
/*  12:    */ import java.util.Date;
/*  13:    */ import java.util.Timer;
/*  14:    */ import java.util.TimerTask;
/*  15:    */ import net.xoetrope.xui.data.XBaseModel;
/*  16:    */ import net.xoetrope.xui.data.XModel;
/*  17:    */ 
/*  18:    */ public class CMEJobs
/*  19:    */   implements Job
/*  20:    */ {
/*  21: 21 */   int task = 0;
/*  22: 23 */   String name = "";
/*  23: 25 */   String message = "";
/*  24:    */   public static final int OUTBOUND = 2;
/*  25:    */   private static final int ASSIGNMENT = 4;
/*  26:    */   private static final int PHYSICIAN_PROCESS = 7;
/*  27:    */   private static final int REFRESH_PHYSICIAN = 8;
/*  28:    */   private static final int CODING = 9;
/*  29:    */   private static final int ADJUDICATION = 10;
/*  30:    */   private static final int RECON = 11;
/*  31:    */   private static final int GC = 99;
/*  32:    */   
/*  33:    */   static
/*  34:    */   {
/*  35: 37 */     Scheduler.add("Communication", "Outbound Messages", new CMEJobs(2), "15,min");
/*  36: 38 */     Scheduler.add("Process Refresh", "Assignment", new CMEJobs(4), "daily");
/*  37: 39 */     Scheduler.add("Physician Process", "Create Physician Process", new CMEJobs(7), "never");
/*  38: 40 */     Scheduler.add("Physician Process ", "Refresh Physician Process", new CMEJobs(8), "daily");
/*  39: 41 */     Scheduler.add("Process Refresh", "Refresh Coding Processes ", new CMEJobs(9), "weekly");
/*  40: 42 */     Scheduler.add("Process Refresh", "Refresh Adjudication Process", new CMEJobs(10), "weekly");
/*  41: 43 */     Scheduler.add("Process Refresh", "Refresh Reconciliation Process", new CMEJobs(11), "weekly");
/*  42: 44 */     Scheduler.add("System", "Run GC", new CMEJobs(99), "15,min");
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void assignAll()
/*  46:    */   {
/*  47: 49 */     XModel xm = TestXUIDB.getInstance().getDataM1("cme_records2", null);
/*  48: 50 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  49:    */     {
/*  50: 51 */       XModel row = xm.get(i);
/*  51: 52 */       String uniqno = ((XModel)row.get("uniqno")).get().toString();
/*  52: 53 */       System.out.println("Uniqno::" + uniqno);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean isPhysicianAvailable(String physician)
/*  57:    */   {
/*  58: 58 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  59: 59 */     String date = sdf.format(new Date());
/*  60:    */     
/*  61: 61 */     XModel xm = new XBaseModel();
/*  62: 62 */     TestXUIDB.getInstance().getData("physician_away", "*", "physician='" + physician + "' AND away_date='" + date + "'", xm);
/*  63:    */     
/*  64: 64 */     return xm.getNumChildren() <= 0;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean imageExists(String uniqno, String domain)
/*  68:    */     throws SQLException
/*  69:    */   {
/*  70: 70 */     String absolutePath = TestXUIDB.getInstance().getImagePath();
/*  71: 71 */     System.out.println("imagePath::" + absolutePath);
/*  72: 72 */     System.out.println("imagePath1::" + absolutePath + uniqno + 
/*  73: 73 */       "_0_blank.png");
/*  74: 74 */     boolean imageExists = (new File(absolutePath + "/" + uniqno + "_0_blank.png").exists()) && 
/*  75: 75 */       (new File(absolutePath + "/" + uniqno + "_1_blank.png").exists()) && 
/*  76: 76 */       (new File(absolutePath + "/" + uniqno + "_cod.png").exists());
/*  77: 77 */     System.out.println("ImageExists:" + imageExists);
/*  78: 78 */     return imageExists;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static void runProcessTransitions(String[] args)
/*  82:    */   {
/*  83:    */     try
/*  84:    */     {
/*  85: 83 */       XModel xm = new XBaseModel();
/*  86: 84 */       TestXUIDB.getInstance().getData("process", "*", "status!='Completed", xm);
/*  87: 85 */       for (int i = 0; i < xm.getNumChildren(); i++)
/*  88:    */       {
/*  89: 86 */         XModel row = xm.get(i);
/*  90: 87 */         String pid = ((XModel)row.get("pid")).get().toString();
/*  91: 88 */         Process.createProcess(pid);
/*  92: 89 */         Process.processTransitions();
/*  93:    */       }
/*  94:    */     }
/*  95:    */     catch (Exception e)
/*  96:    */     {
/*  97: 92 */       e.printStackTrace();
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   public static void sendOutboundMessages(String[] args)
/* 102:    */   {
/* 103:    */     try
/* 104:    */     {
/* 105: 98 */       System.out.println("In CMEController.sendOutboundMessages");
/* 106: 99 */       String username = TestXUIDB.getInstance().getProperty("username");
/* 107:100 */       String password = TestXUIDB.getInstance().getProperty("password");
/* 108:101 */       UserAuthentication userAuthentication = new UserAuthentication();
/* 109:102 */       if (userAuthentication.authenticate1(username, password))
/* 110:    */       {
/* 111:103 */         TestXUIDB.getInstance().sendOutboundLogs("admin");
/* 112:104 */         TestXUIDB.getInstance().sendOutBoundResources();
/* 113:    */       }
/* 114:    */     }
/* 115:    */     catch (Exception e)
/* 116:    */     {
/* 117:108 */       e.printStackTrace();
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static void assignTrainingRecords(String[] args)
/* 122:    */   {
/* 123:    */     try
/* 124:    */     {
/* 125:116 */       XModel xm = new XBaseModel();
/* 126:117 */       TestXUIDB.getInstance().getData("adult", "uniqno", null, xm);
/* 127:118 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 128:    */       {
/* 129:119 */         XModel row = xm.get(i);
/* 130:120 */         String uniqno = ((XModel)row.get("uniqno")).get().toString();
/* 131:121 */         Process process = Process.createProcess("2012-05/" + uniqno, "com.kentropy.process.CMEStateMachine2");
/* 132:122 */         Process.processTransitions();
/* 133:    */       }
/* 134:    */     }
/* 135:    */     catch (Exception e)
/* 136:    */     {
/* 137:125 */       e.printStackTrace();
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public boolean checkRecord(String uniqno, StringBuffer strBuf)
/* 142:    */   {
/* 143:131 */     String keyvaluePath = "/va/" + uniqno + "/";
/* 144:132 */     String domain = TestXUIDB.getInstance().getValue("keyvalue", keyvaluePath + "type");
/* 145:133 */     if (domain == null)
/* 146:    */     {
/* 147:134 */       System.out.println("QA ERROR: Path '" + keyvaluePath + "type' not found");
/* 148:135 */       return false;
/* 149:    */     }
/* 150:137 */     System.out.println("CME QA Domain: " + domain);
/* 151:    */     
/* 152:139 */     XModel dataModel = new XBaseModel();
/* 153:140 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", keyvaluePath);
/* 154:    */     
/* 155:142 */     XModel mappingModel = new XBaseModel();
/* 156:143 */     TestXUIDB.getInstance().getData("cme_mapping", "*", "domain='" + domain + "'", mappingModel);
/* 157:144 */     for (int i = 0; i < mappingModel.getNumChildren(); i++)
/* 158:    */     {
/* 159:145 */       XModel row = mappingModel.get(i);
/* 160:146 */       String path = (String)((XModel)row.get("path")).get();
/* 161:147 */       String required = (String)((XModel)row.get("required")).get();
/* 162:148 */       if (required.equals("yes"))
/* 163:    */       {
/* 164:150 */         String value = TestXUIDB.getInstance().getValue("keyvalue", keyvaluePath + path);
/* 165:151 */         if ((value == null) || (value.trim().equals("")))
/* 166:    */         {
/* 167:152 */           System.out.println("CME QA ERROR: Path '" + keyvaluePath + path + "' not found");
/* 168:153 */           strBuf.append("Missing Path: \"" + keyvaluePath + path + "\"");
/* 169:154 */           return false;
/* 170:    */         }
/* 171:156 */         System.out.println("CME QA INFO: Path '" + keyvaluePath + path + "' found");
/* 172:    */       }
/* 173:    */     }
/* 174:159 */     System.out.println("CME QA: OK");
/* 175:160 */     return true;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public CMEJobs(int task1)
/* 179:    */   {
/* 180:165 */     this.task = task1;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void execute()
/* 184:    */     throws Exception
/* 185:    */   {
/* 186:171 */     String[] args = new String[3];
/* 187:173 */     switch (this.task)
/* 188:    */     {
/* 189:    */     case 2: 
/* 190:176 */       sendOutboundMessages(args);
/* 191:177 */       this.message = "Sent Outbound messages";
/* 192:178 */       break;
/* 193:    */     case 4: 
/* 194:180 */       Process.refreshProcessStatus(" stateMachine like '%assign%' and stateMachineClass like '%CME%'");
/* 195:181 */       this.message = "Refreshed pending Assignments ";
/* 196:    */       
/* 197:183 */       break;
/* 198:    */     case 7: 
/* 199:185 */       XModel xm = new XBaseModel();
/* 200:186 */       TestXUIDB.getInstance().getData("physician", "id", " id  NOT IN (SELECT pid FROM `process` WHERE stateMachineClass='com.kentropy.process.PhysicianStateMachine') AND STATUS='active'", xm);
/* 201:187 */       int count = 0;
/* 202:188 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 203:    */       {
/* 204:190 */         count = 1;
/* 205:191 */         String pid = (String)((XModel)xm.get(i).get("id")).get();
/* 206:192 */         Process.createProcess(pid, "com.kentropy.process.PhysicianStateMachine");
/* 207:    */       }
/* 208:195 */       this.message = (" Created Processes for " + xm.getNumChildren() + " physicians");
/* 209:    */       
/* 210:197 */       break;
/* 211:    */     case 8: 
/* 212:199 */       Process.refreshProcessStatus(" stateMachineClass like '%Physician%'");
/* 213:200 */       this.message = "Physician status refreshed";
/* 214:    */       
/* 215:202 */       break;
/* 216:    */     case 9: 
/* 217:204 */       Process.refreshProcessStatus(" stateMachine  like '%coding%' and stateMachineClass like '%CME%'");
/* 218:    */       
/* 219:206 */       this.message = "Coding Processes refreshed ";
/* 220:207 */       break;
/* 221:    */     case 10: 
/* 222:209 */       Process.refreshProcessStatus(" stateMachine  like '%adjudication%' and stateMachineClass like '%CME%'");
/* 223:210 */       this.message = "Adjudication Processes refreshed ";
/* 224:    */       
/* 225:212 */       break;
/* 226:    */     case 11: 
/* 227:214 */       Process.refreshProcessStatus(" stateMachine  like '%recon%' and stateMachineClass like '%CME%'");
/* 228:215 */       this.message = "Reconciliation Processes refreshed ";
/* 229:    */       
/* 230:217 */       break;
/* 231:    */     case 99: 
/* 232:219 */       System.gc();
/* 233:220 */       this.message = " GC invoked";
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   public static void main(String[] args)
/* 238:    */   {
/* 239:228 */     Timer timer = new Timer();
/* 240:229 */     timer.schedule(new TimerTask()
/* 241:    */     {
/* 242:    */       public void run()
/* 243:    */       {
/* 244:233 */         new Scheduler().runJobs("15,min");
/* 245:    */       }
/* 246:236 */     }, 10L, 1000L);
/* 247:237 */     timer.schedule(new TimerTask()
/* 248:    */     {
/* 249:    */       public void run()
/* 250:    */       {
/* 251:241 */         new Scheduler().runJobs("daily");
/* 252:    */       }
/* 253:244 */     }, 10L, 86400000L);
/* 254:    */     try
/* 255:    */     {
/* 256:247 */       Thread.sleep(10000L);
/* 257:    */     }
/* 258:    */     catch (InterruptedException e)
/* 259:    */     {
/* 260:250 */       e.printStackTrace();
/* 261:    */     }
/* 262:252 */     timer.cancel();
/* 263:    */   }
/* 264:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.scheduler.jobs.CMEJobs
 * JD-Core Version:    0.7.0.1
 */