/*   1:    */ package com.kentropy.agents.cme;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.AgentTransformationSource;
/*   4:    */ import com.kentropy.cme.qa.transformation.Transformation;
/*   5:    */ import com.kentropy.db.TestXUIDB;
/*   6:    */ import com.kentropy.db.XTaskModel;
/*   7:    */ import com.kentropy.process.Agent;
/*   8:    */ import com.kentropy.process.CMEStateMachine;
/*   9:    */ import com.kentropy.process.OfflineWorkListHandler;
/*  10:    */ import com.kentropy.process.Process;
/*  11:    */ import com.kentropy.process.StateMachine;
/*  12:    */ import java.text.SimpleDateFormat;
/*  13:    */ import java.util.Calendar;
/*  14:    */ import java.util.Date;
/*  15:    */ import java.util.GregorianCalendar;
/*  16:    */ import java.util.Hashtable;
/*  17:    */ import java.util.Vector;
/*  18:    */ import net.xoetrope.xui.data.XBaseModel;
/*  19:    */ import net.xoetrope.xui.data.XModel;
/*  20:    */ import org.apache.log4j.Logger;
/*  21:    */ 
/*  22:    */ public class MatchingAgent
/*  23:    */   implements Agent, AgentTransformationSource
/*  24:    */ {
/*  25: 25 */   Process p = null;
/*  26: 26 */   CMEStateMachine sm = null;
/*  27: 27 */   Logger logger = Logger.getLogger(getClass().getName());
/*  28:    */   String[] resources;
/*  29:    */   String[] resources1;
/*  30: 30 */   String ext = "png";
/*  31:    */   
/*  32:    */   public void batchExecute() {}
/*  33:    */   
/*  34:    */   public void getResources()
/*  35:    */   {
/*  36: 33 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/*  37: 34 */     String[] resourcesC = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext };
/*  38: 35 */     String[] resourcesC1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext };
/*  39: 36 */     XModel dataModel = new XBaseModel();
/*  40: 37 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", "/va/" + this.p.pid);
/*  41: 38 */     String domain = dataModel.get("type/@value").toString();
/*  42: 40 */     if (domain.toLowerCase().equals("maternal"))
/*  43:    */     {
/*  44: 41 */       String maternalImage = (String)dataModel.get("report/maternal_image/@value");
/*  45: 42 */       String[] resourcesM = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext, imagepath + maternalImage };
/*  46: 43 */       String[] resourcesM1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext, maternalImage };
/*  47: 44 */       this.resources = resourcesM;
/*  48: 45 */       this.resources1 = resourcesM1;
/*  49:    */     }
/*  50:    */     else
/*  51:    */     {
/*  52: 49 */       this.resources = resourcesC;
/*  53: 50 */       this.resources1 = resourcesC1;
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void stateChange(Process p)
/*  58:    */   {
/*  59: 55 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  60: 56 */     this.p = p;
/*  61: 57 */     if (p.states.getCurrentState().equals("matching"))
/*  62:    */     {
/*  63: 59 */       this.logger.info(" Doing matching for Process " + p.pid);
/*  64:    */       
/*  65: 61 */       this.sm = ((CMEStateMachine)p.states);
/*  66: 62 */       if (this.sm.reconcCompleted.trim().length() > 0)
/*  67:    */       {
/*  68: 64 */         this.logger.info(" At least one person has complete recon.Aborting " + p.pid);
/*  69: 65 */         log(" At least one person has complete recon.Aborting ");
/*  70: 66 */         Process.transition(p.pid);
/*  71: 67 */         return;
/*  72:    */       }
/*  73: 69 */       this.logger.info(" Doing matching for Process " + p.pid);
/*  74:    */       try
/*  75:    */       {
/*  76: 72 */         XModel xm = new XBaseModel();
/*  77: 73 */         String path = "/cme/" + p.pid + "/Coding";
/*  78: 74 */         TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", path);
/*  79: 75 */         Calendar cal = new GregorianCalendar();
/*  80: 76 */         int recontime = 5;
/*  81: 77 */         String reconTime1 = TestXUIDB.getInstance().getProperty("recontime");
/*  82: 78 */         if (reconTime1 != null) {
/*  83:    */           try
/*  84:    */           {
/*  85: 81 */             recontime = Integer.parseInt(reconTime1);
/*  86:    */           }
/*  87:    */           catch (Exception e)
/*  88:    */           {
/*  89: 85 */             e.printStackTrace();
/*  90: 86 */             recontime = 5;
/*  91:    */           }
/*  92:    */         }
/*  93: 89 */         cal.add(5, recontime);
/*  94: 90 */         String dueDate = sdf.format(cal.getTime());
/*  95: 91 */         String assignDate = sdf.format(new Date());
/*  96:    */         try
/*  97:    */         {
/*  98: 93 */           XModel firstM = (XModel)xm.get(this.sm.assignedfirst + "/cancellation/reason");
/*  99: 94 */           XModel secondM = (XModel)xm.get(this.sm.assignedsecond + "/cancellation/reason");
/* 100: 95 */           String firstCancel = (String)firstM.get();
/* 101: 96 */           String secondCancel = (String)secondM.get();
/* 102: 98 */           if ((firstCancel != null) || (secondCancel != null))
/* 103:    */           {
/* 104:100 */             transform("cancellation");
/* 105:101 */             this.sm.cancelled = true;
/* 106:    */             
/* 107:103 */             Process.transition(p.pid);
/* 108:104 */             return;
/* 109:    */           }
/* 110:107 */           this.sm.cancelled = false;
/* 111:    */         }
/* 112:    */         catch (Exception e1)
/* 113:    */         {
/* 114:111 */           e1.printStackTrace();
/* 115:    */         }
/* 116:114 */         String first = (String)((XModel)xm.get(this.sm.assignedfirst + "/icd")).get();
/* 117:115 */         this.logger.info(" PATH " + this.sm.assignedsecond + "/icd");
/* 118:116 */         String second = (String)((XModel)xm.get(this.sm.assignedsecond + "/icd")).get();
/* 119:117 */         this.logger.info(first + " " + second);
/* 120:119 */         if ((first.equals(second)) || (TestXUIDB.getInstance().checkEquivalence(first, second)))
/* 121:    */         {
/* 122:121 */           transform("coderpayment");
/* 123:122 */           this.sm.matchingResult = true;
/* 124:123 */           Process.transition(p.pid);
/* 125:124 */           return;
/* 126:    */         }
/* 127:127 */         TestXUIDB.getInstance().saveKeyValue("keyvalue", "/cme/" + p.pid + "/stage", "Reconciliation");
/* 128:    */         
/* 129:129 */         String parentPath = "area:1/house:1/household:1";
/* 130:    */         
/* 131:131 */         OfflineWorkListHandler owfh = new OfflineWorkListHandler();
/* 132:132 */         getResources();
/* 133:133 */         String[] dataPath = { "/va/" + p.pid, "/cme/" + p.pid };
/* 134:134 */         String[] dataPath1 = { "/va/" + p.pid, "/cme/" + p.pid + "%/" + this.sm.assignedfirst + "/", "/cme/" + p.pid + "%/" + this.sm.assignedsecond + "/" };
/* 135:135 */         owfh.assign("task0", this.sm.assignedfirst, "cme", "6", parentPath, null, null, null, null, null);
/* 136:136 */         owfh.assign("task0/task1-" + p.pid, this.sm.assignedfirst, "cme", "6", parentPath, assignDate, dueDate, dataPath, this.resources, this.resources1);
/* 137:137 */         owfh.assign("task0", this.sm.assignedsecond, "cme", "6", parentPath, null, null, null, null, null);
/* 138:138 */         owfh.assign("task0/task1-" + p.pid, this.sm.assignedsecond, "cme", "6", parentPath, assignDate, dueDate, dataPath, this.resources, this.resources1);
/* 139:    */         
/* 140:140 */         Process.transition(p.pid);
/* 141:    */       }
/* 142:    */       catch (Exception e)
/* 143:    */       {
/* 144:144 */         e.printStackTrace();
/* 145:145 */         TestXUIDB.getInstance().logAgent(p.pid, getClass().getName(), this.sm.currentState, "Error:" + e.getMessage());
/* 146:    */       }
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void log(String message)
/* 151:    */   {
/* 152:151 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, message);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public Hashtable getHashtable()
/* 156:    */   {
/* 157:156 */     Hashtable ht = new Hashtable();
/* 158:157 */     ht.put("uniqno", this.p.pid);
/* 159:158 */     ht.put("phys1", this.sm.assignedfirst);
/* 160:159 */     ht.put("phys2", this.sm.assignedsecond);
/* 161:160 */     return ht;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public boolean transform(String type)
/* 165:    */     throws Exception
/* 166:    */   {
/* 167:163 */     XModel xm = new XBaseModel();
/* 168:164 */     TestXUIDB.getInstance()
/* 169:165 */       .getData("transformations", "*", "table1='" + type + "'", xm);
/* 170:166 */     boolean flag = false;
/* 171:167 */     Hashtable ht = getHashtable();
/* 172:    */     
/* 173:169 */     log(" Transformation " + xm.getNumChildren());
/* 174:171 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 175:    */     {
/* 176:172 */       XModel row = xm.get(i);
/* 177:173 */       String field = ((XModel)row.get("field1")).get().toString();
/* 178:174 */       String transformationClass = ((XModel)row.get("transformation_class")).get().toString();
/* 179:175 */       String outTab = ((XModel)row.get("output_table")).get().toString();
/* 180:176 */       String outputFld = ((XModel)row.get("output_field")).get().toString();
/* 181:177 */       this.logger.info("Transformation::" + ((XModel)row.get("transformation_class")).get());
/* 182:178 */       log(" class " + transformationClass);
/* 183:179 */       Transformation transformation = 
/* 184:    */       
/* 185:    */ 
/* 186:182 */         (Transformation)Class.forName(transformationClass).newInstance();
/* 187:    */       
/* 188:184 */       String retVal = (String)transformation.transform(ht, field, ht.get(field).toString(), null, 
/* 189:185 */         "keyvalue");
/* 190:186 */       flag = true;
/* 191:    */     }
/* 192:188 */     return flag;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void stateChange1(Process p)
/* 196:    */   {
/* 197:193 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 198:194 */     if (p.states.getCurrentState().equals("matching"))
/* 199:    */     {
/* 200:196 */       this.logger.info(" Doing matching for Process " + p.pid);
/* 201:    */       
/* 202:198 */       this.sm = ((CMEStateMachine)p.states);
/* 203:199 */       this.logger.info(" Doing matching for Process " + p.pid);
/* 204:    */       try
/* 205:    */       {
/* 206:202 */         XModel xm = new XBaseModel();
/* 207:203 */         String path = "/cme/" + p.pid + "/Coding";
/* 208:204 */         TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", path);
/* 209:205 */         Calendar cal = new GregorianCalendar();
/* 210:206 */         cal.add(5, 10);
/* 211:207 */         String dueDate = sdf.format(cal.getTime());
/* 212:208 */         String assignDate = sdf.format(new Date());
/* 213:    */         try
/* 214:    */         {
/* 215:210 */           XModel firstM = (XModel)xm.get(this.sm.assignedfirst + "/cancellation/reason");
/* 216:211 */           XModel secondM = (XModel)xm.get(this.sm.assignedsecond + "/cancellation/reason");
/* 217:212 */           String firstCancel = (String)firstM.get();
/* 218:213 */           String secondCancel = (String)secondM.get();
/* 219:215 */           if ((firstCancel != null) || (secondCancel != null))
/* 220:    */           {
/* 221:217 */             this.sm.currentState = "cancelled";
/* 222:218 */             Process.transition(p.pid);
/* 223:219 */             return;
/* 224:    */           }
/* 225:    */         }
/* 226:    */         catch (Exception e1)
/* 227:    */         {
/* 228:225 */           e1.printStackTrace();
/* 229:    */           
/* 230:227 */           String first = (String)((XModel)xm.get(this.sm.assignedfirst + "/icd")).get();
/* 231:228 */           this.logger.info(" PATH " + this.sm.assignedsecond + "/icd");
/* 232:229 */           String second = (String)((XModel)xm.get(this.sm.assignedsecond + "/icd")).get();
/* 233:230 */           this.logger.info(first + " " + second);
/* 234:232 */           if ((first.equals(second)) || (TestXUIDB.getInstance().checkEquivalence(first, second)))
/* 235:    */           {
/* 236:234 */             this.sm.matchingResult = true;
/* 237:235 */             Process.transition(p.pid);
/* 238:236 */             return;
/* 239:    */           }
/* 240:239 */           TestXUIDB.getInstance().saveKeyValue("keyvalue", "/cme/" + p.pid + "/stage", "Reconciliation");
/* 241:    */           
/* 242:241 */           String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 243:242 */           XTaskModel rootM = XTaskModel.getRoot(this.sm.assignedfirst, "cme", "6");
/* 244:243 */           XTaskModel cmeM = (XTaskModel)rootM.get("task0");
/* 245:244 */           cmeM.area = "1";cmeM.household = "1";cmeM.house = "1";
/* 246:245 */           cmeM.assignedTo = this.sm.assignedfirst;
/* 247:246 */           cmeM.set("@assignedto", this.sm.assignedfirst);
/* 248:247 */           XTaskModel codM1 = (XTaskModel)cmeM.get("task1-" + p.pid);
/* 249:248 */           codM1.assignedTo = this.sm.assignedfirst;
/* 250:249 */           codM1.set("@assignedto", this.sm.assignedfirst);
/* 251:250 */           codM1.set("@dateassigned", assignDate);
/* 252:251 */           codM1.set("@duedate", dueDate);
/* 253:252 */           cmeM.save();
/* 254:253 */           codM1.save();
/* 255:    */           
/* 256:255 */           TestXUIDB.getInstance().sendServerLogs("admin", this.sm.assignedfirst, bookmark, "999999");
/* 257:256 */           bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 258:257 */           rootM = XTaskModel.getRoot(this.sm.assignedsecond, "cme", "6");
/* 259:258 */           cmeM = (XTaskModel)rootM.get("task0");
/* 260:259 */           cmeM.set("@assignedto", this.sm.assignedsecond);
/* 261:260 */           cmeM.area = "1";cmeM.household = "1";cmeM.house = "1";
/* 262:261 */           cmeM.assignedTo = this.sm.assignedsecond;
/* 263:262 */           cmeM.save();
/* 264:263 */           codM1 = (XTaskModel)cmeM.get("task1-" + p.pid);
/* 265:264 */           codM1.assignedTo = this.sm.assignedsecond;
/* 266:265 */           codM1.set("@assignedto", this.sm.assignedsecond);
/* 267:266 */           codM1.set("@dateassigned", assignDate);
/* 268:267 */           codM1.set("@duedate", dueDate);
/* 269:    */           
/* 270:269 */           codM1.save();
/* 271:270 */           TestXUIDB.getInstance().sendServerLogs("admin", this.sm.assignedsecond, bookmark, "999999");
/* 272:271 */           Vector keys = new Vector();
/* 273:272 */           keys.add("key1");
/* 274:    */           
/* 275:274 */           bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 276:275 */           TestXUIDB.getInstance().createChangeLog("keyvalue", "key1 like '/cme/" + p.pid + "/%'", keys);
/* 277:276 */           TestXUIDB.getInstance().sendServerLogs("admin", this.sm.assignedfirst + "," + this.sm.assignedsecond, bookmark, "999999");
/* 278:277 */           Process.transition(p.pid);
/* 279:    */         }
/* 280:    */         return;
/* 281:    */       }
/* 282:    */       catch (Exception e)
/* 283:    */       {
/* 284:282 */         e.printStackTrace();
/* 285:    */       }
/* 286:    */     }
/* 287:    */   }
/* 288:    */   
/* 289:    */   public static void main(String[] args) {}
/* 290:    */   
/* 291:    */   public void setProcess(Process p)
/* 292:    */   {
/* 293:293 */     this.p = p;
/* 294:294 */     this.sm = ((CMEStateMachine)p.states);
/* 295:    */   }
/* 296:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.MatchingAgent
 * JD-Core Version:    0.7.0.1
 */