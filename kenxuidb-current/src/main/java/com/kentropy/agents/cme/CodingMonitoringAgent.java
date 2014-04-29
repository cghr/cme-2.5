/*   1:    */ package com.kentropy.agents.cme;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.process.Agent;
/*   5:    */ import com.kentropy.process.CMEStateMachine;
/*   6:    */ import com.kentropy.process.Process;
/*   7:    */ import com.kentropy.process.StateMachine;
/*   8:    */ import java.text.SimpleDateFormat;
/*   9:    */ import java.util.Calendar;
/*  10:    */ import java.util.Date;
/*  11:    */ import net.xoetrope.xui.data.XBaseModel;
/*  12:    */ import net.xoetrope.xui.data.XModel;
/*  13:    */ import org.apache.log4j.Logger;
/*  14:    */ 
/*  15:    */ public class CodingMonitoringAgent
/*  16:    */   implements Agent
/*  17:    */ {
/*  18: 18 */   Logger logger = Logger.getLogger(getClass().getName());
/*  19: 19 */   CMEStateMachine sm = null;
/*  20: 21 */   Process p = null;
/*  21: 23 */   public String ext = "png";
/*  22:    */   String[] resources;
/*  23:    */   String[] resources1;
/*  24:    */   
/*  25:    */   public void log(String message)
/*  26:    */   {
/*  27: 29 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, message);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void refreshStatus()
/*  31:    */   {
/*  32: 34 */     String pre = "taskdefinitions/cme_taskdefinition/task0/";
/*  33: 35 */     String completed = " ";
/*  34: 36 */     String unassigned = " ";
/*  35: 37 */     XModel xm = new XBaseModel();
/*  36: 38 */     String task = "";
/*  37: 39 */     if ((this.sm.currentState.equals("coding")) || (this.sm.currentState.equals("coding1"))) {
/*  38: 41 */       task = pre + "task0";
/*  39: 43 */     } else if ((this.sm.currentState.equals("reconc")) || (this.sm.currentState.equals("reconc1"))) {
/*  40: 45 */       task = pre + "task1";
/*  41: 47 */     } else if (this.sm.currentState.equals("adjudication")) {
/*  42: 49 */       task = pre + "task2";
/*  43:    */     } else {
/*  44: 53 */       return;
/*  45:    */     }
/*  46: 54 */     TestXUIDB.getInstance().getData("tasks", "assignedto,status", " status IN (1,5) and task ='" + task + "' and member='" + this.p.pid + "'", xm);
/*  47:    */     
/*  48: 56 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, "Children " + xm.getNumChildren());
/*  49: 57 */     this.logger.info("Children " + xm.getNumChildren());
/*  50: 58 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  51:    */     {
/*  52: 60 */       String status1 = (String)xm.get(i).get(1).get();
/*  53: 61 */       this.logger.info(" Status=" + status1);
/*  54: 62 */       log(" Status=" + status1);
/*  55: 64 */       if (status1.equals("1"))
/*  56:    */       {
/*  57: 66 */         log("compl:" + i + " " + xm.get(i).get(0).get());
/*  58: 67 */         if (this.sm.getCurrentState().equals("adjudication"))
/*  59:    */         {
/*  60: 69 */           if (xm.get(i).get(0).get().equals(this.sm.adjudicator))
/*  61:    */           {
/*  62: 71 */             completed = completed + (completed.equals(" ") ? "" : ":") + xm.get(i).get(0).get();
/*  63: 72 */             completed = completed.trim();
/*  64:    */           }
/*  65:    */         }
/*  66: 76 */         else if ((xm.get(i).get(0).get().equals(this.sm.assignedfirst)) || (xm.get(i).get(0).get().equals(this.sm.assignedsecond)))
/*  67:    */         {
/*  68: 78 */           completed = completed + (completed.equals(" ") ? "" : ":") + xm.get(i).get(0).get();
/*  69: 79 */           completed = completed.trim();
/*  70:    */         }
/*  71:    */       }
/*  72: 83 */       else if (status1.equals("5"))
/*  73:    */       {
/*  74: 85 */         if ((xm.get(i).get(0).get().equals(this.sm.assignedfirst)) || (xm.get(i).get(0).get().equals(this.sm.assignedsecond)))
/*  75:    */         {
/*  76: 87 */           unassigned = unassigned + (unassigned.equals(" ") ? "" : ":") + xm.get(i).get(0).get();
/*  77: 88 */           unassigned = unassigned.trim();
/*  78:    */         }
/*  79:    */       }
/*  80:    */     }
/*  81: 95 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, "Completed " + task + " " + completed);
/*  82: 96 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, "Unassigned " + task + " " + unassigned);
/*  83: 98 */     if ((this.sm.currentState.equals("coding")) || (this.sm.currentState.equals("coding1")))
/*  84:    */     {
/*  85:100 */       this.sm.codingCompleted = completed;
/*  86:101 */       this.sm.adjCompleted = " ";
/*  87:102 */       this.sm.codingUnassigned = unassigned;
/*  88:103 */       if (unassigned.trim().length() > 0) {
/*  89:104 */         this.sm.reassignCoding = true;
/*  90:    */       }
/*  91:    */     }
/*  92:107 */     else if ((this.sm.currentState.equals("reconc")) || (this.sm.currentState.equals("reconc1")))
/*  93:    */     {
/*  94:109 */       this.sm.reconcCompleted = completed;
/*  95:    */     }
/*  96:111 */     if (this.sm.currentState.equals("adjudication"))
/*  97:    */     {
/*  98:113 */       this.sm.adjCompleted = completed;
/*  99:114 */       this.sm.adjUnassigned = unassigned;
/* 100:115 */       if (unassigned.trim().length() > 0) {
/* 101:116 */         this.sm.reassignAdj = true;
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void unassignAdjudication()
/* 107:    */   {
/* 108:122 */     this.sm = ((CMEStateMachine)this.p.states);
/* 109:123 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 110:124 */     XModel xm = new XBaseModel();
/* 111:125 */     String check = "'" + this.sm.adjudicator + "'";
/* 112:126 */     TestXUIDB.getInstance().getData("tasks", "status,dueDate,assignedto,task", "task like '%task0/task2' and member='" + this.p.pid + "' and ( status is null) and assignedto IN (" + check + ")", xm);
/* 113:127 */     String late = "";
/* 114:128 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 115:    */     {
/* 116:130 */       String status = (String)xm.get(i).get(0).get();
/* 117:132 */       if (status == null) {
/* 118:    */         try
/* 119:    */         {
/* 120:135 */           Date dueDate = sdf.parse(xm.get(i).get(1).get().toString());
/* 121:136 */           Calendar cal1 = Calendar.getInstance();
/* 122:137 */           String delay2 = TestXUIDB.getInstance().getProperty("reassignDelay");
/* 123:138 */           int reassignDelay = 14;
/* 124:139 */           if (delay2 != null) {
/* 125:    */             try
/* 126:    */             {
/* 127:142 */               reassignDelay = Integer.parseInt(delay2);
/* 128:    */             }
/* 129:    */             catch (Exception e)
/* 130:    */             {
/* 131:146 */               e.printStackTrace();
/* 132:147 */               reassignDelay = 14;
/* 133:    */             }
/* 134:    */           }
/* 135:152 */           cal1.add(5, 0 - reassignDelay);
/* 136:153 */           this.logger.info("Checking time " + cal1.getTime() + " Due date " + dueDate);
/* 137:154 */           if (dueDate.before(cal1.getTime()))
/* 138:    */           {
/* 139:156 */             String assignedto = xm.get(i).get(2).get().toString();
/* 140:157 */             String task = xm.get(i).get(3).get().toString();
/* 141:158 */             late = late + (late.trim().equals("") ? "" : ":") + assignedto;
/* 142:    */             
/* 143:160 */             XModel xm1 = new XBaseModel();
/* 144:161 */             ((XModel)xm1.get("status")).set("5");
/* 145:162 */             ((XModel)xm1.get("endtime")).set("5");
/* 146:163 */             String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 147:164 */             TestXUIDB.getInstance().saveDataM1("tasks", " task like '%task0/task2' and member='" + this.p.pid + "' and assignedto='" + assignedto + "'", xm1);
/* 148:165 */             String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 149:166 */             TestXUIDB.getInstance().addToChangeLogOutboundQueue(assignedto, frombookmark, tobookmark);
/* 150:    */           }
/* 151:    */         }
/* 152:    */         catch (Exception e)
/* 153:    */         {
/* 154:172 */           e.printStackTrace();
/* 155:    */         }
/* 156:    */       }
/* 157:    */     }
/* 158:176 */     if (late.trim().length() > 0)
/* 159:    */     {
/* 160:178 */       this.sm.adjUnassigned = late;
/* 161:179 */       TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.getCurrentState(), " Unassigned adjudication " + late);
/* 162:    */       
/* 163:181 */       Process.transition(this.p.pid);
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void stateChange(Process p)
/* 168:    */   {
/* 169:187 */     this.logger.info("Inside AssignmentAgent.stateChange()");
/* 170:188 */     this.p = p;
/* 171:189 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 172:190 */     this.sm = ((CMEStateMachine)p.states);
/* 173:191 */     refreshStatus();
/* 174:192 */     if ((p.states.getCurrentState().equals("coding")) || (p.states.getCurrentState().equals("coding1")))
/* 175:    */     {
/* 176:193 */       this.sm = ((CMEStateMachine)p.states);
/* 177:    */       
/* 178:195 */       XModel xm = new XBaseModel();
/* 179:196 */       String check = "'" + this.sm.assignedfirst + "','" + this.sm.assignedsecond + "'";
/* 180:197 */       TestXUIDB.getInstance().getData("tasks", "status,dueDate,assignedto,task", "task like '%task0/task0' and member='" + p.pid + "' and ( status is null) and assignedto IN (" + check + ")", xm);
/* 181:198 */       String late = "";
/* 182:199 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 183:    */       {
/* 184:201 */         String status = (String)xm.get(i).get(0).get();
/* 185:203 */         if (status == null) {
/* 186:    */           try
/* 187:    */           {
/* 188:206 */             Date dueDate = sdf.parse(xm.get(i).get(1).get().toString());
/* 189:207 */             Calendar cal1 = Calendar.getInstance();
/* 190:208 */             String delay2 = TestXUIDB.getInstance().getProperty("reassignDelay");
/* 191:209 */             int reassignDelay = 14;
/* 192:210 */             if (delay2 != null) {
/* 193:    */               try
/* 194:    */               {
/* 195:213 */                 reassignDelay = Integer.parseInt(delay2);
/* 196:    */               }
/* 197:    */               catch (Exception e)
/* 198:    */               {
/* 199:217 */                 e.printStackTrace();
/* 200:218 */                 reassignDelay = 14;
/* 201:    */               }
/* 202:    */             }
/* 203:223 */             cal1.add(5, 0 - reassignDelay);
/* 204:224 */             this.logger.info("Checking time " + cal1.getTime() + " Due date " + dueDate);
/* 205:225 */             if (dueDate.before(cal1.getTime()))
/* 206:    */             {
/* 207:227 */               String assignedto = xm.get(i).get(2).get().toString();
/* 208:228 */               String task = xm.get(i).get(3).get().toString();
/* 209:229 */               late = late + (late.trim().equals("") ? "" : ":") + assignedto;
/* 210:    */               
/* 211:231 */               XModel xm1 = new XBaseModel();
/* 212:232 */               ((XModel)xm1.get("status")).set("5");
/* 213:233 */               String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 214:234 */               TestXUIDB.getInstance().saveDataM1("tasks", " task like '%task0/task0' and member='" + p.pid + "' and assignedto='" + assignedto + "'", xm1);
/* 215:235 */               String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 216:236 */               TestXUIDB.getInstance().addToChangeLogOutboundQueue(assignedto, frombookmark, tobookmark);
/* 217:237 */               this.sm.reassignCoding = true;
/* 218:    */             }
/* 219:    */           }
/* 220:    */           catch (Exception e)
/* 221:    */           {
/* 222:243 */             e.printStackTrace();
/* 223:    */           }
/* 224:    */         }
/* 225:    */       }
/* 226:247 */       if (late.trim().length() > 0)
/* 227:    */       {
/* 228:249 */         this.sm.codingUnassigned = late;
/* 229:    */         
/* 230:251 */         Process.transition(p.pid);
/* 231:    */       }
/* 232:    */     }
/* 233:254 */     if (p.states.getCurrentState().equals("reassign_coding")) {
/* 234:255 */       this.sm = ((CMEStateMachine)p.states);
/* 235:    */     }
/* 236:258 */     if (p.states.getCurrentState().equals("adjudication"))
/* 237:    */     {
/* 238:259 */       this.sm = ((CMEStateMachine)p.states);
/* 239:260 */       unassignAdjudication();
/* 240:    */     }
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void getResources()
/* 244:    */   {
/* 245:266 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/* 246:267 */     String[] resourcesC = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext };
/* 247:268 */     String[] resourcesC1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext };
/* 248:269 */     XModel dataModel = new XBaseModel();
/* 249:270 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", "/va/" + this.p.pid);
/* 250:271 */     String domain = dataModel.get("type/@value").toString();
/* 251:273 */     if (domain.toLowerCase().equals("maternal"))
/* 252:    */     {
/* 253:274 */       String maternalImage = (String)dataModel.get("report/maternal_image/@value");
/* 254:275 */       String[] resourcesM = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext, imagepath + maternalImage };
/* 255:276 */       String[] resourcesM1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext, maternalImage };
/* 256:277 */       this.resources = resourcesM;
/* 257:278 */       this.resources1 = resourcesM1;
/* 258:    */     }
/* 259:    */     else
/* 260:    */     {
/* 261:282 */       this.resources = resourcesC;
/* 262:283 */       this.resources1 = resourcesC1;
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   public void batchExecute() {}
/* 267:    */   
/* 268:    */   public static void main(String[] args)
/* 269:    */     throws Exception
/* 270:    */   {
/* 271:294 */     Process.createProcess("01300088_01_02");
/* 272:295 */     Process.processTransitions();
/* 273:    */   }
/* 274:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.CodingMonitoringAgent
 * JD-Core Version:    0.7.0.1
 */