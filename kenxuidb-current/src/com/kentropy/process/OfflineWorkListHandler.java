/*   1:    */ package com.kentropy.process;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.db.XTaskModel;
/*   5:    */ import com.kentropy.model.KenList;
/*   6:    */ import com.kentropy.security.client.UserAuthentication;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.util.Vector;
/*   9:    */ import net.xoetrope.xui.data.XBaseModel;
/*  10:    */ import net.xoetrope.xui.data.XModel;
/*  11:    */ 
/*  12:    */ public class OfflineWorkListHandler
/*  13:    */ {
/*  14:    */   String[] dataPath;
/*  15:    */   String parentPath;
/*  16:    */   String[] resources;
/*  17:    */   String[] resources1;
/*  18: 18 */   String ext = "png";
/*  19:    */   
/*  20:    */   public XModel getAssignments(String task, String domain, String surveyType)
/*  21:    */   {
/*  22: 22 */     XModel xm = new XBaseModel();
/*  23: 23 */     TestXUIDB.getInstance().getTaskData(" task = " + task + " member=" + "0", xm);
/*  24: 24 */     return xm;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void getDataPath(String task, String id, String[] assigned)
/*  28:    */   {
/*  29: 29 */     this.dataPath = new String[] { "/va/" + id };
/*  30: 30 */     if (assigned != null) {
/*  31: 32 */       if (task.endsWith("task0")) {
/*  32: 34 */         this.dataPath = new String[] { "/va/" + id };
/*  33: 36 */       } else if (task.endsWith("task1")) {
/*  34: 38 */         this.dataPath = new String[] { "/va/" + id, "/cme/" + id + "/stage", "/cme/" + id + "/%Coding%/" + assigned[0] + "/%", "/cme/" + id + "/%Coding%/" + assigned[1] + "/%" };
/*  35: 40 */       } else if (task.endsWith("task2")) {
/*  36: 42 */         this.dataPath = new String[] { "/va/" + id, "/cme/" + id + "/stage", "/cme/" + id + "/%Coding%/" + assigned[0] + "/%", "/cme/" + id + "/%Coding%/" + assigned[1] + "/%", "/cme/" + id + "/%Reconciliation%/" + assigned[0] + "/%", "/cme/" + id + "/%Reconciliation%/" + assigned[1] + "/%" };
/*  37:    */       }
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void getParentPath(String task, String id)
/*  42:    */   {
/*  43: 49 */     this.parentPath = "area:1/house:1/household:1";
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void getResources(String task, String id)
/*  47:    */   {
/*  48: 54 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/*  49: 55 */     String[] resourcesC = { imagepath + id + "_0_blank." + this.ext, imagepath + id + "_1_blank." + this.ext, imagepath + id + "_cod." + this.ext };
/*  50: 56 */     String[] resourcesC1 = { id + "_0_blank." + this.ext, id + "_1_blank." + this.ext, id + "_cod." + this.ext };
/*  51: 57 */     XModel dataModel = new XBaseModel();
/*  52: 58 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", "/va/" + id);
/*  53: 59 */     String domain = dataModel.get("type/@value").toString();
/*  54: 61 */     if (domain.toLowerCase().equals("maternal"))
/*  55:    */     {
/*  56: 62 */       String maternalImage = (String)dataModel.get("report/maternal_image/@value");
/*  57: 63 */       String[] resourcesM = { imagepath + id + "_0_blank." + this.ext, imagepath + id + "_1_blank." + this.ext, imagepath + id + "_cod." + this.ext, imagepath + maternalImage };
/*  58: 64 */       String[] resourcesM1 = { id + "_0_blank." + this.ext, id + "_1_blank." + this.ext, id + "_cod." + this.ext, maternalImage };
/*  59: 65 */       this.resources = resourcesM;
/*  60: 66 */       this.resources1 = resourcesM1;
/*  61:    */     }
/*  62:    */     else
/*  63:    */     {
/*  64: 70 */       this.resources = resourcesC;
/*  65: 71 */       this.resources1 = resourcesC1;
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String[] getTaskAssignments(String pid)
/*  70:    */   {
/*  71:    */     try
/*  72:    */     {
/*  73: 78 */       System.out.println(" Pid " + pid);
/*  74: 79 */       Process p = TestXUIDB.getInstance().getProcess(pid);
/*  75: 80 */       CMEStateMachine sm = (CMEStateMachine)p.states;
/*  76: 81 */       String[] s = new String[2];
/*  77: 82 */       if (sm.currentState.equals("adjudication"))
/*  78:    */       {
/*  79: 84 */         s = new String[3];
/*  80: 85 */         s[2] = sm.adjudicator;
/*  81:    */       }
/*  82: 88 */       s[0] = sm.assignedfirst;
/*  83: 89 */       s[1] = sm.assignedsecond;
/*  84: 90 */       return s;
/*  85:    */     }
/*  86:    */     catch (Exception e)
/*  87:    */     {
/*  88: 94 */       e.printStackTrace();
/*  89:    */     }
/*  90: 96 */     return null;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public synchronized void sendTopLevelCMETasks()
/*  94:    */     throws Exception
/*  95:    */   {
/*  96:102 */     XModel xm = new XBaseModel();
/*  97:103 */     TestXUIDB.getInstance().getData("physician", "concat(id,'') phyId", " status !='stopped'", xm);
/*  98:105 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  99:    */     {
/* 100:107 */       String participant = xm.get(i).get("phyId/@value").toString();
/* 101:108 */       sendTopLevelCMETask(participant);
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   public synchronized void sendTopLevelCMETask(String participant)
/* 106:    */     throws Exception
/* 107:    */   {
/* 108:115 */     Vector keyF = new Vector();
/* 109:116 */     keyF.add("assignedto");
/* 110:117 */     keyF.add("member");
/* 111:118 */     keyF.add("task");
/* 112:119 */     keyF.add("survey_type");
/* 113:120 */     String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 114:121 */     TestXUIDB.getInstance().createChangeLog("tasks", "survey_type='6' and task like '%definition/task0' and assignedto='" + participant + "'", keyF);
/* 115:122 */     String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 116:123 */     TestXUIDB.getInstance().addToChangeLogOutboundQueue(participant, frombookmark, tobookmark);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public synchronized void sendAllCMETasksStage(String taskType)
/* 120:    */     throws Exception
/* 121:    */   {
/* 122:128 */     XModel xm = new XBaseModel();
/* 123:129 */     TestXUIDB.getInstance().getData("tasks", "task,member,assignedto", "status is null and task like '%task0/" + taskType + "'", xm);
/* 124:131 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 125:    */     {
/* 126:133 */       XModel rowM = xm.get(i);
/* 127:134 */       Vector keyF = new Vector();
/* 128:135 */       keyF.add("key1");
/* 129:136 */       String member = rowM.get("member/@value").toString();
/* 130:137 */       String assignedto = rowM.get("assignedto/@value").toString();
/* 131:138 */       String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 132:139 */       TestXUIDB.getInstance().createChangeLog("keyvalue", "key1 like '%/cme/" + member + "/stage%'", keyF);
/* 133:140 */       String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 134:    */       
/* 135:142 */       TestXUIDB.getInstance().addToChangeLogOutboundQueue(assignedto, frombookmark, tobookmark);
/* 136:    */     }
/* 137:    */   }
/* 138:    */   
/* 139:    */   public synchronized void sendAllPendingCMETasks(boolean deleteOp, String where)
/* 140:    */     throws Exception
/* 141:    */   {
/* 142:148 */     XModel xm = new XBaseModel();
/* 143:149 */     TestXUIDB.getInstance().getData("tasks", "task,member,assignedto", "status is null and task like '%task0/task%' and " + where, xm);
/* 144:151 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 145:    */     {
/* 146:153 */       XModel rowM = xm.get(i);
/* 147:154 */       String[] taskpath = rowM.get("task/@value").toString().split("/");
/* 148:155 */       String task = taskpath[(taskpath.length - 1)];
/* 149:156 */       String member = rowM.get("member/@value").toString();
/* 150:157 */       String assignedto = rowM.get("assignedto/@value").toString();
/* 151:158 */       resendCMETask(task, member, assignedto, deleteOp);
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public synchronized void sendAllPendingCMETasks(boolean deleteOp)
/* 156:    */     throws Exception
/* 157:    */   {
/* 158:165 */     XModel xm = new XBaseModel();
/* 159:166 */     TestXUIDB.getInstance().getData("tasks", "task,member,assignedto", "status is null and task like '%task0/task%'", xm);
/* 160:168 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 161:    */     {
/* 162:170 */       XModel rowM = xm.get(i);
/* 163:171 */       String[] taskpath = rowM.get("task/@value").toString().split("/");
/* 164:172 */       String task = taskpath[(taskpath.length - 1)];
/* 165:173 */       String member = rowM.get("member/@value").toString();
/* 166:174 */       String assignedto = rowM.get("assignedto/@value").toString();
/* 167:175 */       resendCMETask(task, member, assignedto, deleteOp);
/* 168:    */     }
/* 169:    */   }
/* 170:    */   
/* 171:    */   public synchronized void resendCMETask(String task, String member, String participant, boolean deleteOp)
/* 172:    */     throws Exception
/* 173:    */   {
/* 174:182 */     Vector keys = new Vector();
/* 175:183 */     keys.add("key1");
/* 176:184 */     keys.add("value1");
/* 177:    */     
/* 178:186 */     String surveyType = "6";
/* 179:187 */     String domain = "cme";
/* 180:188 */     String[] assigned = getTaskAssignments(member);
/* 181:    */     
/* 182:190 */     String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 183:192 */     if (deleteOp) {
/* 184:193 */       TestXUIDB.getInstance().createDeleteLog("tasks", "survey_type='" + surveyType + "' and task like '%" + task + "' and member='" + member + "' and assignedto='" + participant + "'");
/* 185:    */     }
/* 186:194 */     Vector keyF = new Vector();
/* 187:195 */     keyF.add("assignedto");
/* 188:196 */     keyF.add("member");
/* 189:197 */     keyF.add("task");
/* 190:198 */     keyF.add("survey_type");
/* 191:199 */     TestXUIDB.getInstance().createChangeLog("tasks", "survey_type='" + surveyType + "' and task like '%" + task + "' and member='" + member + "' and assignedto='" + participant + "'", keyF);
/* 192:200 */     getResources(task, member);
/* 193:201 */     getDataPath(task, member, assigned);
/* 194:202 */     getParentPath(task, member);
/* 195:204 */     if (this.dataPath != null)
/* 196:    */     {
/* 197:206 */       for (int i = 0; i < this.dataPath.length; i++)
/* 198:    */       {
/* 199:208 */         if (deleteOp) {
/* 200:209 */           TestXUIDB.getInstance().createDeleteLog("keyvalue", 
/* 201:210 */             "key1 like '" + this.dataPath[i] + "%'");
/* 202:    */         }
/* 203:211 */         TestXUIDB.getInstance().createChangeLog("keyvalue", 
/* 204:212 */           "key1 like '" + this.dataPath[i] + "%'", keys);
/* 205:    */       }
/* 206:215 */       String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 207:216 */       TestXUIDB.getInstance().addToChangeLogOutboundQueue(participant, frombookmark, tobookmark);
/* 208:    */     }
/* 209:219 */     if (this.resources != null) {
/* 210:221 */       for (int i = 0; i < this.resources.length; i++) {
/* 211:223 */         sendResource(this.resources[i], this.resources1[i], participant);
/* 212:    */       }
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   public synchronized void resend(String task, String participant, String domain, String surveyType, String[] assigned)
/* 217:    */     throws Exception
/* 218:    */   {
/* 219:230 */     Vector keys = new Vector();
/* 220:231 */     keys.add("key1");
/* 221:232 */     keys.add("value1");
/* 222:233 */     String[] taskParams = task.split("-");
/* 223:    */     
/* 224:235 */     String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 225:    */     
/* 226:237 */     TestXUIDB.getInstance().createDeleteLog("tasks", "survey_type='" + surveyType + "' and task like '%" + taskParams[0] + "' and member='" + taskParams[1] + "' and assignedto='" + participant + "'");
/* 227:238 */     Vector keyF = new Vector();
/* 228:239 */     keyF.add("assignedto");
/* 229:240 */     keyF.add("member");
/* 230:241 */     keyF.add("task");
/* 231:242 */     keyF.add("survey_type");
/* 232:243 */     TestXUIDB.getInstance().createChangeLog("tasks", "survey_type='" + surveyType + "' and task like '%" + taskParams[0] + "' and member='" + taskParams[1] + "' and assignedto='" + participant + "'", keyF);
/* 233:244 */     getResources(taskParams[0], taskParams[1]);
/* 234:245 */     getDataPath(taskParams[0], taskParams[1], assigned);
/* 235:246 */     getParentPath(taskParams[0], taskParams[1]);
/* 236:248 */     if (this.dataPath != null)
/* 237:    */     {
/* 238:250 */       for (int i = 0; i < this.dataPath.length; i++)
/* 239:    */       {
/* 240:252 */         TestXUIDB.getInstance().createDeleteLog("keyvalue", 
/* 241:253 */           "key1 like '" + this.dataPath[i] + "%'");
/* 242:254 */         TestXUIDB.getInstance().createChangeLog("keyvalue", 
/* 243:255 */           "key1 like '" + this.dataPath[i] + "%'", keys);
/* 244:    */       }
/* 245:258 */       String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 246:259 */       TestXUIDB.getInstance().addToChangeLogOutboundQueue(participant, frombookmark, tobookmark);
/* 247:    */     }
/* 248:262 */     if (this.resources != null) {
/* 249:264 */       for (int i = 0; i < this.resources.length; i++) {
/* 250:266 */         sendResource(this.resources[i], this.resources1[i], participant);
/* 251:    */       }
/* 252:    */     }
/* 253:    */   }
/* 254:    */   
/* 255:    */   public synchronized OfflineWorkListHandler assign(String task, String participant, String domain, String surveyType, String parentPath, String dateAssigned, String dueDate, String[] dataPath, String[] resources, String[] resources1)
/* 256:    */     throws Exception
/* 257:    */   {
/* 258:274 */     Vector keys = new Vector();
/* 259:275 */     keys.add("key1");
/* 260:276 */     keys.add("value1");
/* 261:    */     
/* 262:278 */     KenList taskL = new KenList(parentPath);
/* 263:279 */     KenList eL = taskL.right(":");
/* 264:    */     
/* 265:281 */     String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 266:282 */     XTaskModel rootM = XTaskModel.getRoot(participant, 
/* 267:283 */       domain, surveyType);
/* 268:284 */     XTaskModel taskM = (XTaskModel)rootM.get(task);
/* 269:285 */     for (int i = 0; i < eL.size(); i++) {
/* 270:287 */       System.out.println(eL.get(i));
/* 271:    */     }
/* 272:289 */     taskM.area = eL.get(0).toString();
/* 273:290 */     taskM.household = eL.get(2).toString();
/* 274:291 */     taskM.house = eL.get(1).toString();
/* 275:292 */     taskM.assignedTo = participant;
/* 276:293 */     taskM.set("@assignedto", participant);
/* 277:294 */     taskM.set("@dateassigned", dateAssigned);
/* 278:295 */     taskM.set("@duedate", dueDate);
/* 279:296 */     taskM.save();
/* 280:298 */     if (dataPath != null)
/* 281:    */     {
/* 282:300 */       for (int i = 0; i < dataPath.length; i++) {
/* 283:302 */         TestXUIDB.getInstance().createChangeLog("keyvalue", 
/* 284:303 */           "key1 like '" + dataPath[i] + "%'", keys);
/* 285:    */       }
/* 286:306 */       String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 287:307 */       TestXUIDB.getInstance().addToChangeLogOutboundQueue(participant, frombookmark, tobookmark);
/* 288:    */     }
/* 289:310 */     if (resources != null) {
/* 290:312 */       for (int i = 0; i < resources.length; i++) {
/* 291:314 */         sendResource(resources[i], resources1[i], participant);
/* 292:    */       }
/* 293:    */     }
/* 294:317 */     return this;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public void sendResource(String fromPath, String toPath, String recepient)
/* 298:    */     throws Exception
/* 299:    */   {
/* 300:323 */     TestXUIDB.getInstance().addToResourceOutboundQueue(recepient, fromPath, toPath, "admin");
/* 301:    */   }
/* 302:    */   
/* 303:    */   public static void main(String[] args)
/* 304:    */     throws Exception
/* 305:    */   {
/* 306:329 */     UserAuthentication ua = new UserAuthentication();
/* 307:    */     
/* 308:331 */     String[] resources = { "resources/appContext.xml" };
/* 309:332 */     String[] resources1 = { "appContext1.xml" };
/* 310:333 */     String[] d = { "/cme/03300118_01_01" };
/* 311:    */     
/* 312:335 */     TestXUIDB.getInstance().sendOutboundLogs("admin");
/* 313:336 */     TestXUIDB.getInstance().sendOutBoundResources();
/* 314:    */   }
/* 315:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.process.OfflineWorkListHandler
 * JD-Core Version:    0.7.0.1
 */