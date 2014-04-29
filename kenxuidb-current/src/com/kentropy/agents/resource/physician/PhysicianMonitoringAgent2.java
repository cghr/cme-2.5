/*   1:    */ package com.kentropy.agents.resource.physician;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.process.Agent;
/*   5:    */ import com.kentropy.process.OfflineWorkListHandler;
/*   6:    */ import com.kentropy.process.PhysicianStateMachine2;
/*   7:    */ import com.kentropy.process.Process;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.text.SimpleDateFormat;
/*  10:    */ import java.util.Calendar;
/*  11:    */ import java.util.Date;
/*  12:    */ import java.util.GregorianCalendar;
/*  13:    */ import net.xoetrope.xui.data.XBaseModel;
/*  14:    */ import net.xoetrope.xui.data.XModel;
/*  15:    */ 
/*  16:    */ public class PhysicianMonitoringAgent2
/*  17:    */   implements Agent
/*  18:    */ {
/*  19: 19 */   PhysicianStateMachine2 sm = null;
/*  20: 20 */   Process p = null;
/*  21: 21 */   String resultTable = "training_results";
/*  22: 22 */   String p1ResultCol = "p1_result";
/*  23: 23 */   String p2ResultCol = "p2_result";
/*  24:    */   
/*  25:    */   public void setPhysicianStatus(String phy, String status)
/*  26:    */     throws Exception
/*  27:    */   {
/*  28: 27 */     XModel dataM = new XBaseModel();
/*  29: 28 */     ((XModel)dataM.get("status")).set(status);
/*  30:    */     
/*  31: 30 */     TestXUIDB.getInstance().saveDataM1("physician", "id='" + phy + "'", dataM);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String getCurrentPhysicianBatch(String phy)
/*  35:    */     throws Exception
/*  36:    */   {
/*  37: 36 */     XModel xm = new XBaseModel();
/*  38:    */     
/*  39: 38 */     TestXUIDB.getInstance().getData("physician", "current_tr_batch", "id='" + phy + "'", xm);
/*  40: 39 */     if (xm.getNumChildren() > 0) {
/*  41: 40 */       return xm.get(0).get(0).get().toString();
/*  42:    */     }
/*  43: 42 */     return null;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void stateChange(Process p)
/*  47:    */   {
/*  48: 47 */     this.p = p;
/*  49: 48 */     this.sm = ((PhysicianStateMachine2)p.states);
/*  50:    */     
/*  51: 50 */     String dt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
/*  52: 51 */     if (this.sm.getCurrentState().equals("phase1_assignment")) {
/*  53:    */       try
/*  54:    */       {
/*  55: 54 */         assignP1();
/*  56: 55 */         if (this.sm.p1AssignmentCompleted) {
/*  57: 56 */           Process.transition(p.pid);
/*  58:    */         } else {
/*  59: 58 */           return;
/*  60:    */         }
/*  61:    */       }
/*  62:    */       catch (Exception e)
/*  63:    */       {
/*  64: 62 */         e.printStackTrace();
/*  65: 63 */         return;
/*  66:    */       }
/*  67:    */     }
/*  68: 66 */     if (this.sm.getCurrentState().equals("phase1"))
/*  69:    */     {
/*  70: 68 */       XModel xm = new XBaseModel();
/*  71:    */       
/*  72: 70 */       System.out.println("physician ='" + p.pid + "'");
/*  73: 71 */       TestXUIDB.getInstance().getData(this.resultTable, this.p1ResultCol, " physician ='" + p.pid + "'", xm);
/*  74: 72 */       if (xm.getNumChildren() > 0)
/*  75:    */       {
/*  76: 74 */         String p1Result1 = xm.get(0).get(0).get().toString();
/*  77: 75 */         if ((p1Result1 != null) && (p1Result1.equals("pass")))
/*  78:    */         {
/*  79: 77 */           this.sm.p1Selected = true;
/*  80:    */           
/*  81: 79 */           Process.transition(p.pid);
/*  82:    */         }
/*  83:    */         else
/*  84:    */         {
/*  85: 83 */           this.sm.p1Selected = false;
/*  86:    */         }
/*  87:    */       }
/*  88:    */       else
/*  89:    */       {
/*  90: 89 */         this.sm.p1Selected = false;
/*  91:    */       }
/*  92:    */     }
/*  93: 93 */     else if (this.sm.getCurrentState().equals("phase2assignment"))
/*  94:    */     {
/*  95:    */       try
/*  96:    */       {
/*  97: 97 */         assignP2();
/*  98: 98 */         Process.transition(p.pid);
/*  99:    */       }
/* 100:    */       catch (Exception e)
/* 101:    */       {
/* 102:102 */         e.printStackTrace();
/* 103:    */       }
/* 104:    */     }
/* 105:106 */     else if (this.sm.getCurrentState().equals("phase2assignment1"))
/* 106:    */     {
/* 107:    */       try
/* 108:    */       {
/* 109:110 */         assignP21();
/* 110:111 */         Process.transition(p.pid);
/* 111:    */       }
/* 112:    */       catch (Exception e)
/* 113:    */       {
/* 114:115 */         e.printStackTrace();
/* 115:    */       }
/* 116:    */     }
/* 117:119 */     else if (this.sm.getCurrentState().equals("phase2"))
/* 118:    */     {
/* 119:121 */       XModel xm = new XBaseModel();
/* 120:122 */       System.out.println("  physician ='" + p.pid + "'");
/* 121:123 */       TestXUIDB.getInstance().getData(this.resultTable, this.p2ResultCol, " physician ='" + p.pid + "' and " + this.p2ResultCol + " is null ", xm);
/* 122:124 */       if (xm.getNumChildren() > 0)
/* 123:    */       {
/* 124:126 */         String p2Result1 = (String)xm.get(0).get(0).get();
/* 125:127 */         if ((p2Result1 != null) && (p2Result1.equals("pass")))
/* 126:    */         {
/* 127:129 */           this.sm.p2Selected = true;
/* 128:    */           
/* 129:131 */           Process.transition(p.pid);
/* 130:    */         }
/* 131:    */         else
/* 132:    */         {
/* 133:135 */           this.sm.p2Selected = false;
/* 134:    */         }
/* 135:    */       }
/* 136:    */       else
/* 137:    */       {
/* 138:141 */         this.sm.p2Selected = false;
/* 139:    */       }
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   public boolean assignP1(String uniqno)
/* 144:    */   {
/* 145:148 */     OfflineWorkListHandler ofwh = new OfflineWorkListHandler();
/* 146:    */     
/* 147:150 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 148:    */     try
/* 149:    */     {
/* 150:153 */       Calendar cal = new GregorianCalendar();
/* 151:154 */       int codingtime = 10;
/* 152:155 */       String codingTime1 = TestXUIDB.getInstance().getProperty("codingtime");
/* 153:156 */       if (codingTime1 != null) {
/* 154:    */         try
/* 155:    */         {
/* 156:159 */           codingtime = Integer.parseInt(codingTime1);
/* 157:    */         }
/* 158:    */         catch (Exception e)
/* 159:    */         {
/* 160:163 */           e.printStackTrace();
/* 161:164 */           codingtime = 10;
/* 162:    */         }
/* 163:    */       }
/* 164:167 */       cal.add(5, codingtime);
/* 165:168 */       String dueDate = sdf.format(cal.getTime());
/* 166:169 */       String assignDate = sdf.format(new Date());
/* 167:171 */       synchronized (this)
/* 168:    */       {
/* 169:173 */         String parentPath = "area:1/house:1/household:1";
/* 170:174 */         String[] dataPath = { "/va/" + uniqno };
/* 171:175 */         ofwh.assign("task0", this.p.pid, "cme", "6", parentPath, null, null, null, null, null);
/* 172:176 */         ofwh.assign("task0/task0-" + uniqno, this.p.pid, "cme", "6", parentPath, assignDate, dueDate, dataPath, null, null);
/* 173:    */         
/* 174:178 */         return true;
/* 175:    */       }
/* 176:182 */       return false;
/* 177:    */     }
/* 178:    */     catch (Exception e)
/* 179:    */     {
/* 180:181 */       e.printStackTrace();
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   private void assignP1()
/* 185:    */     throws Exception
/* 186:    */   {
/* 187:188 */     String curbatch = getCurrentPhysicianBatch(this.p.pid);
/* 188:189 */     if (curbatch == null) {
/* 189:190 */       throw new Exception("batch not found");
/* 190:    */     }
/* 191:192 */     String sql = "SELECT uniqno FROM adult ORDER BY RAND() LIMIT 50";
/* 192:193 */     XModel xm = new XBaseModel();
/* 193:194 */     TestXUIDB.getInstance().getData(sql, "uniqno", "", xm);
/* 194:195 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 195:    */     {
/* 196:197 */       String uniqno = (String)xm.get(i).get(0).get();
/* 197:198 */       assignP1(uniqno);
/* 198:    */     }
/* 199:200 */     this.sm.p1AssignmentCompleted = true;
/* 200:    */   }
/* 201:    */   
/* 202:    */   private void assignP2()
/* 203:    */     throws Exception
/* 204:    */   {
/* 205:206 */     String curbatch = getCurrentPhysicianBatch(this.p.pid);
/* 206:207 */     if (curbatch == null) {
/* 207:208 */       throw new Exception("batch not found");
/* 208:    */     }
/* 209:210 */     XModel xm = new XBaseModel();
/* 210:211 */     TestXUIDB.getInstance().getData("cme_report a left join physician b on a.physician=b.id  ", "uniqno,icd", " physician ='" + this.p.pid + "' and current_tr_batch='" + curbatch + "'", xm);
/* 211:    */     
/* 212:213 */     int count = !this.sm.p2AssignmentCompleted ? 3 : 23;
/* 213:214 */     System.out.println(" Count " + count + " " + this.sm.getCurrentState());
/* 214:215 */     for (int i = 0; (i < xm.getNumChildren()) && (this.sm.p2ReportCount < count); i++)
/* 215:    */     {
/* 216:217 */       String uniqno = xm.get(i).get(0).get().toString();
/* 217:218 */       String icd = xm.get(i).get(1).get().toString();
/* 218:219 */       if (findReconciliation(uniqno, icd)) {
/* 219:221 */         this.sm.p2ReportCount += 1;
/* 220:    */       }
/* 221:    */     }
/* 222:226 */     if (!this.sm.p2AssignmentCompleted) {
/* 223:227 */       this.sm.p2AssignmentCompleted = true;
/* 224:229 */     } else if (!this.sm.p2Assignment1Completed) {
/* 225:230 */       this.sm.p2Assignment1Completed = true;
/* 226:    */     }
/* 227:    */   }
/* 228:    */   
/* 229:    */   private void assignP21()
/* 230:    */     throws Exception
/* 231:    */   {
/* 232:236 */     String curbatch = getCurrentPhysicianBatch(this.p.pid);
/* 233:237 */     if (curbatch == null) {
/* 234:238 */       throw new Exception("batch not found");
/* 235:    */     }
/* 236:240 */     XModel xm = new XBaseModel();
/* 237:241 */     TestXUIDB.getInstance().getData("cme_report a left join physician b on a.physician=b.id ", "uniqno,icd", " physician ='" + this.p.pid + "' and current_tr_batch='" + curbatch + "'", xm);
/* 238:    */     
/* 239:243 */     int count = 20;
/* 240:244 */     System.out.println(" Count " + count + " " + this.sm.getCurrentState());
/* 241:245 */     for (int i = 0; (i < xm.getNumChildren()) && (this.sm.p2ReportCount < 20); i++)
/* 242:    */     {
/* 243:247 */       String uniqno = xm.get(i).get(0).get().toString();
/* 244:248 */       String icd = xm.get(i).get(1).get().toString();
/* 245:249 */       if (findReconciliation(uniqno, icd)) {
/* 246:251 */         this.sm.p2ReportCount += 1;
/* 247:    */       }
/* 248:    */     }
/* 249:256 */     if (this.sm.p2ReportCount >= 20) {
/* 250:257 */       this.sm.p2Assignment1Completed = true;
/* 251:    */     }
/* 252:    */   }
/* 253:    */   
/* 254:    */   public void log(String message)
/* 255:    */   {
/* 256:262 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.getCurrentState(), message);
/* 257:    */   }
/* 258:    */   
/* 259:    */   public boolean findReconciliation(String uniqno, String icd)
/* 260:    */     throws Exception
/* 261:    */   {
/* 262:267 */     XModel xm = new XBaseModel();
/* 263:    */     
/* 264:269 */     TestXUIDB.getInstance().getData("cme_report", "icd,physician", " uniqno ='" + uniqno + "' and  physician !='" + this.p.pid + "' and icd!='" + icd + "' and uniqno NOT in (select distinct member from tasks where assignedto='" + this.p.pid + "' and task like '%task1')", xm);
/* 265:270 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 266:    */     {
/* 267:272 */       String first = icd;
/* 268:    */       
/* 269:274 */       String second = xm.get(i).get(0).get().toString();
/* 270:275 */       String phy2 = xm.get(i).get(1).get().toString();
/* 271:    */       
/* 272:277 */       log(" icd=" + phy2 + " phy2=" + phy2);
/* 273:279 */       if (!TestXUIDB.getInstance().checkEquivalence(first, second))
/* 274:    */       {
/* 275:281 */         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 276:282 */         String parentPath = "area:1/house:1/household:1";
/* 277:    */         
/* 278:284 */         OfflineWorkListHandler owfh = new OfflineWorkListHandler();
/* 279:285 */         Calendar cal = new GregorianCalendar();
/* 280:286 */         int recontime = 5;
/* 281:287 */         String reconTime1 = TestXUIDB.getInstance().getProperty("recontime");
/* 282:288 */         if (reconTime1 != null) {
/* 283:    */           try
/* 284:    */           {
/* 285:291 */             recontime = Integer.parseInt(reconTime1);
/* 286:    */           }
/* 287:    */           catch (Exception e)
/* 288:    */           {
/* 289:295 */             e.printStackTrace();
/* 290:296 */             recontime = 5;
/* 291:    */           }
/* 292:    */         }
/* 293:299 */         cal.add(5, recontime);
/* 294:300 */         String dueDate = sdf.format(cal.getTime());
/* 295:301 */         String assignDate = sdf.format(new Date());
/* 296:    */         
/* 297:303 */         String[] dataPath = { "/va/" + uniqno, "/cme/" + uniqno + "/Coding/" + this.p.pid, "/cme/" + uniqno + "/Coding/" + phy2, "/cme/" + uniqno + "/Coding/Comments/" + this.p.pid, "/cme/" + uniqno + "/Coding/Comments/" + phy2 };
/* 298:    */         
/* 299:305 */         owfh.assign("task0", this.p.pid, "cme", "6", parentPath, null, null, null, null, null);
/* 300:    */         
/* 301:307 */         owfh.assign("task0/task1-" + uniqno, this.p.pid, "cme", "6", parentPath, assignDate, dueDate, dataPath, null, null);
/* 302:308 */         String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 303:309 */         TestXUIDB.getInstance().createKeyValueChangeLog("keyvalue", "/cme/" + uniqno + "/stage", "Reconciliation");
/* 304:310 */         TestXUIDB.getInstance().createKeyValueChangeLog("keyvalue", "/cme/" + uniqno + "/phy2", phy2);
/* 305:    */         
/* 306:312 */         String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 307:313 */         TestXUIDB.getInstance().addToChangeLogOutboundQueue(this.p.pid, frombookmark, tobookmark);
/* 308:314 */         return true;
/* 309:    */       }
/* 310:    */     }
/* 311:319 */     return false;
/* 312:    */   }
/* 313:    */   
/* 314:    */   public void batchExecute() {}
/* 315:    */   
/* 316:    */   public static void main(String[] args)
/* 317:    */   {
/* 318:    */     try
/* 319:    */     {
/* 320:330 */       Process p = Process.createProcess("1", "com.kentropy.process.PhysicianStateMachine");
/* 321:    */       
/* 322:332 */       Process.processTransitions();
/* 323:333 */       Process.processTransitions();
/* 324:    */     }
/* 325:    */     catch (Exception e)
/* 326:    */     {
/* 327:336 */       e.printStackTrace();
/* 328:    */     }
/* 329:    */   }
/* 330:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.resource.physician.PhysicianMonitoringAgent2
 * JD-Core Version:    0.7.0.1
 */