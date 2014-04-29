/*   1:    */ package com.kentropy.agents.cme;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.AgentTransformationSource;
/*   4:    */ import com.kentropy.agents.AgentUtils;
/*   5:    */ import com.kentropy.cme.qa.transformation.Transformation;
/*   6:    */ import com.kentropy.db.TestXUIDB;
/*   7:    */ import com.kentropy.db.XTaskModel;
/*   8:    */ import com.kentropy.process.Agent;
/*   9:    */ import com.kentropy.process.CMEStateMachine;
/*  10:    */ import com.kentropy.process.OfflineWorkListHandler;
/*  11:    */ import com.kentropy.process.Process;
/*  12:    */ import com.kentropy.process.StateMachine;
/*  13:    */ import com.kentropy.transfer.Client;
/*  14:    */ import java.text.SimpleDateFormat;
/*  15:    */ import java.util.Calendar;
/*  16:    */ import java.util.Date;
/*  17:    */ import java.util.GregorianCalendar;
/*  18:    */ import java.util.Hashtable;
/*  19:    */ import java.util.Vector;
/*  20:    */ import net.xoetrope.xui.data.XBaseModel;
/*  21:    */ import net.xoetrope.xui.data.XModel;
/*  22:    */ import org.apache.log4j.Logger;
/*  23:    */ 
/*  24:    */ public class ReMatchingAgent
/*  25:    */   implements Agent, AgentTransformationSource
/*  26:    */ {
/*  27: 27 */   Logger logger = Logger.getLogger(getClass().getName());
/*  28: 29 */   CMEStateMachine sm = null;
/*  29: 31 */   Process p = null;
/*  30: 33 */   public String ext = "png";
/*  31:    */   String[] resources;
/*  32:    */   String[] resources1;
/*  33:    */   String dueDate;
/*  34:    */   String assignDate;
/*  35:    */   
/*  36:    */   public void batchExecute() {}
/*  37:    */   
/*  38:    */   public void setProcess(Process p)
/*  39:    */   {
/*  40: 45 */     this.p = p;
/*  41: 46 */     this.sm = ((CMEStateMachine)p.states);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void checkConfirmed() {}
/*  45:    */   
/*  46:    */   public void checkAccepted() {}
/*  47:    */   
/*  48:    */   public void sendImage()
/*  49:    */     throws Exception
/*  50:    */   {
/*  51: 60 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/*  52: 61 */     String firstpage = this.p.pid + "_0_blank.png";
/*  53: 62 */     String secondpage = this.p.pid + "_1_blank.png";
/*  54: 63 */     String codcrop = this.p.pid + "_cod.png";
/*  55:    */     
/*  56: 65 */     Client cl = new Client();
/*  57: 66 */     cl.run(imagepath + firstpage, firstpage, this.sm.adjudicator);
/*  58: 67 */     cl.run(imagepath + secondpage, secondpage, this.sm.adjudicator);
/*  59: 68 */     cl.run(imagepath + codcrop, codcrop, this.sm.adjudicator);
/*  60:    */     
/*  61: 70 */     XModel dataModel = new XBaseModel();
/*  62: 71 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", "/va/" + this.p.pid);
/*  63: 72 */     String domain = dataModel.get("type/@value").toString();
/*  64: 73 */     this.logger.info("Domain: " + domain);
/*  65: 74 */     if (domain.toLowerCase().equals("maternal"))
/*  66:    */     {
/*  67: 75 */       String maternalImage = (String)dataModel.get("report/maternal_image/@value");
/*  68: 76 */       this.logger.info("Maternal Image:" + maternalImage);
/*  69:    */       
/*  70: 78 */       cl.run(imagepath + maternalImage, maternalImage, this.sm.adjudicator);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void getResources()
/*  75:    */   {
/*  76: 84 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/*  77: 85 */     String[] resourcesC = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext };
/*  78: 86 */     String[] resourcesC1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext };
/*  79: 87 */     XModel dataModel = new XBaseModel();
/*  80: 88 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", "/va/" + this.p.pid);
/*  81: 89 */     String domain = dataModel.get("type/@value").toString();
/*  82: 91 */     if (domain.toLowerCase().equals("maternal"))
/*  83:    */     {
/*  84: 92 */       String maternalImage = (String)dataModel.get("report/maternal_image/@value");
/*  85: 93 */       String[] resourcesM = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext, imagepath + maternalImage };
/*  86: 94 */       String[] resourcesM1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext, maternalImage };
/*  87: 95 */       this.resources = resourcesM;
/*  88: 96 */       this.resources1 = resourcesM1;
/*  89:    */     }
/*  90:    */     else
/*  91:    */     {
/*  92:100 */       this.resources = resourcesC;
/*  93:101 */       this.resources1 = resourcesC1;
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void match()
/*  98:    */     throws Exception
/*  99:    */   {
/* 100:108 */     XModel xm = new XBaseModel();
/* 101:109 */     String path = "/cme/" + this.p.pid + "/Reconciliation";
/* 102:110 */     TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", path);
/* 103:    */     
/* 104:112 */     transform("coderpayment");
/* 105:113 */     XModel firstM = (XModel)xm.get(this.sm.assignedfirst + "/icd");
/* 106:114 */     XModel secondM = (XModel)xm.get(this.sm.assignedsecond + "/icd");
/* 107:115 */     String first1 = (String)firstM.get();
/* 108:116 */     String second1 = (String)secondM.get();
/* 109:117 */     if ((first1 != null) && (second1 != null))
/* 110:    */     {
/* 111:119 */       if ((first1.equals(second1)) || (TestXUIDB.getInstance().checkEquivalence(first1, second1)))
/* 112:    */       {
/* 113:121 */         this.sm.reMatchingResult = true;
/* 114:    */         
/* 115:123 */         return;
/* 116:    */       }
/* 117:126 */       this.sm.reMatchingResult = false;
/* 118:    */       
/* 119:128 */       TestXUIDB.getInstance().saveKeyValue("keyvalue", "/cme/" + this.p.pid + "/stage", "Adjudication");
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void assign()
/* 124:    */     throws Exception
/* 125:    */   {
/* 126:135 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 127:136 */     Calendar cal = new GregorianCalendar();
/* 128:137 */     int adjtime = 5;
/* 129:138 */     String adjTime1 = TestXUIDB.getInstance().getProperty("adjtime");
/* 130:139 */     if (adjTime1 != null) {
/* 131:    */       try
/* 132:    */       {
/* 133:142 */         adjtime = Integer.parseInt(adjTime1);
/* 134:    */       }
/* 135:    */       catch (Exception e)
/* 136:    */       {
/* 137:146 */         e.printStackTrace();
/* 138:147 */         adjtime = 5;
/* 139:    */       }
/* 140:    */     }
/* 141:150 */     cal.add(5, adjtime);
/* 142:151 */     this.dueDate = sdf.format(cal.getTime());
/* 143:152 */     this.assignDate = sdf.format(new Date());
/* 144:153 */     Vector adjs = findPhysicians(this.p.pid);
/* 145:154 */     if (adjs.size() == 0) {
/* 146:155 */       return;
/* 147:    */     }
/* 148:156 */     String adj = adjs.get(0).toString();
/* 149:157 */     OfflineWorkListHandler ofwh = new OfflineWorkListHandler();
/* 150:158 */     synchronized (this)
/* 151:    */     {
/* 152:160 */       getResources();
/* 153:161 */       String[] dataPath = { "/va/" + this.p.pid, "/cme/" + this.p.pid };
/* 154:162 */       ofwh.assign("task0", adj, "cme", "6", "area:1/house:1/household:1", null, null, null, null, null);
/* 155:163 */       ofwh.assign("task0/task2-" + this.p.pid, adj, "cme", "6", "area:1/house:1/household:1", this.assignDate, this.dueDate, dataPath, this.resources, this.resources1);
/* 156:    */     }
/* 157:165 */     this.sm.adjudicator = adj;
/* 158:166 */     this.sm.adjUnassigned = " ";
/* 159:167 */     Process.transition(this.p.pid);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public Hashtable getHashtable()
/* 163:    */   {
/* 164:172 */     Hashtable ht = new Hashtable();
/* 165:173 */     ht.put("uniqno", this.p.pid);
/* 166:174 */     ht.put("phys1", this.sm.assignedfirst);
/* 167:175 */     ht.put("phys2", this.sm.assignedsecond);
/* 168:176 */     return ht;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public boolean transform(String type)
/* 172:    */     throws Exception
/* 173:    */   {
/* 174:179 */     XModel xm = new XBaseModel();
/* 175:180 */     TestXUIDB.getInstance()
/* 176:181 */       .getData("transformations", "*", "table1='" + type + "'", xm);
/* 177:182 */     boolean flag = false;
/* 178:    */     
/* 179:184 */     Hashtable ht = getHashtable();
/* 180:186 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 181:    */     {
/* 182:187 */       XModel row = xm.get(i);
/* 183:188 */       String field = ((XModel)row.get("field1")).get().toString();
/* 184:189 */       String transformationClass = ((XModel)row.get("transformation_class")).get().toString();
/* 185:190 */       String outTab = ((XModel)row.get("output_table")).get().toString();
/* 186:191 */       String outputFld = ((XModel)row.get("output_field")).get().toString();
/* 187:192 */       this.logger.info("Transformation::" + ((XModel)row.get("transformation_class")).get());
/* 188:193 */       AgentUtils.log(this.p, this, " class " + transformationClass);
/* 189:194 */       Transformation transformation = 
/* 190:    */       
/* 191:    */ 
/* 192:197 */         (Transformation)Class.forName(transformationClass).newInstance();
/* 193:    */       
/* 194:199 */       String retVal = (String)transformation.transform(ht, field, ht.get(field).toString(), null, 
/* 195:200 */         "keyvalue");
/* 196:201 */       flag = true;
/* 197:    */     }
/* 198:203 */     return flag;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void log(String message)
/* 202:    */   {
/* 203:208 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, message);
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void stateChange(Process p)
/* 207:    */   {
/* 208:212 */     this.p = p;
/* 209:213 */     if (p.states.getCurrentState().equals("rematching"))
/* 210:    */     {
/* 211:215 */       this.logger.info(" Doing matching for Process " + p.pid);
/* 212:    */       
/* 213:217 */       this.sm = ((CMEStateMachine)p.states);
/* 214:218 */       if (this.sm.adjCompleted.trim().length() > 0)
/* 215:    */       {
/* 216:220 */         this.logger.info(" At least one person has complete adj.Aborting " + p.pid);
/* 217:221 */         log(" At least one person has complete adj.Aborting ");
/* 218:222 */         Process.transition(p.pid);
/* 219:223 */         return;
/* 220:    */       }
/* 221:226 */       this.logger.info(" Doing matching for Process " + p.pid);
/* 222:    */       try
/* 223:    */       {
/* 224:229 */         match();
/* 225:230 */         Process.transition(p.pid);
/* 226:    */       }
/* 227:    */       catch (Exception e)
/* 228:    */       {
/* 229:234 */         this.sm.rollback();
/* 230:235 */         e.printStackTrace();
/* 231:236 */         TestXUIDB.getInstance().logAgent(p.pid, getClass().getName(), this.sm.currentState, "Error:" + e.getMessage());
/* 232:    */       }
/* 233:    */     }
/* 234:239 */     if (p.states.getCurrentState().equals("adjassignment"))
/* 235:    */     {
/* 236:241 */       this.logger.info(" Doing Adj assignment for Process " + p.pid);
/* 237:    */       
/* 238:243 */       this.sm = ((CMEStateMachine)p.states);
/* 239:    */       try
/* 240:    */       {
/* 241:246 */         String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 242:247 */         synchronized (this)
/* 243:    */         {
/* 244:249 */           assign();
/* 245:    */         }
/* 246:    */       }
/* 247:    */       catch (Exception e)
/* 248:    */       {
/* 249:254 */         e.printStackTrace();
/* 250:    */       }
/* 251:    */     }
/* 252:257 */     if (p.states.getCurrentState().equals("adjreassignment"))
/* 253:    */     {
/* 254:259 */       this.logger.info(" Doing Adj assignment for Process " + p.pid);
/* 255:    */       
/* 256:261 */       this.sm = ((CMEStateMachine)p.states);
/* 257:    */       try
/* 258:    */       {
/* 259:264 */         String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 260:265 */         synchronized (this)
/* 261:    */         {
/* 262:267 */           assign();
/* 263:    */         }
/* 264:    */       }
/* 265:    */       catch (Exception e)
/* 266:    */       {
/* 267:273 */         e.printStackTrace();
/* 268:    */       }
/* 269:    */     }
/* 270:    */   }
/* 271:    */   
/* 272:    */   public void stateChange1(Process p)
/* 273:    */   {
/* 274:280 */     this.p = p;
/* 275:281 */     if (p.states.getCurrentState().equals("rematching"))
/* 276:    */     {
/* 277:283 */       this.logger.info(" Doing matching for Process " + p.pid);
/* 278:    */       
/* 279:285 */       this.sm = ((CMEStateMachine)p.states);
/* 280:286 */       this.logger.info(" Doing matching for Process " + p.pid);
/* 281:287 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 282:288 */       Calendar cal = new GregorianCalendar();
/* 283:289 */       cal.add(5, 10);
/* 284:290 */       String dueDate = sdf.format(cal.getTime());
/* 285:291 */       String assignDate = sdf.format(new Date());
/* 286:    */       try
/* 287:    */       {
/* 288:294 */         XModel xm = new XBaseModel();
/* 289:295 */         String path = "/cme/" + p.pid + "/Reconciliation";
/* 290:296 */         TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", path);
/* 291:297 */         String first = (String)xm.get(this.sm.assignedfirst + "/icd/@value");
/* 292:298 */         String second = (String)xm.get(this.sm.assignedsecond + "/icd/@value");
/* 293:299 */         if ((first.equals(second)) || (TestXUIDB.getInstance().checkEquivalence(first, second)))
/* 294:    */         {
/* 295:301 */           this.sm.reMatchingResult = true;
/* 296:302 */           Process.transition(p.pid);
/* 297:303 */           return;
/* 298:    */         }
/* 299:306 */         TestXUIDB.getInstance().saveKeyValue("keyvalue", "/cme/" + p.pid + "/stage", "Adjudication");
/* 300:    */         
/* 301:308 */         String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 302:309 */         synchronized (this)
/* 303:    */         {
/* 304:311 */           Vector adjs = findPhysicians(p.pid);
/* 305:312 */           if (adjs.size() == 0) {
/* 306:313 */             return;
/* 307:    */           }
/* 308:314 */           String adj = adjs.get(0).toString();
/* 309:315 */           XTaskModel rootM = XTaskModel.getRoot(adj, "cme", "6");
/* 310:316 */           XTaskModel cmeM = (XTaskModel)rootM.get("task0");
/* 311:317 */           cmeM.area = "1";cmeM.household = "1";cmeM.house = "1";
/* 312:318 */           cmeM.assignedTo = adj;
/* 313:319 */           cmeM.set("@assignedto", adj);
/* 314:320 */           XTaskModel codM1 = (XTaskModel)cmeM.get("task2-" + p.pid);
/* 315:321 */           codM1.assignedTo = adj;
/* 316:322 */           codM1.set("@assignedto", adj);
/* 317:323 */           codM1.set("@dateassigned", assignDate);
/* 318:324 */           codM1.set("@duedate", dueDate);
/* 319:325 */           cmeM.save();
/* 320:326 */           codM1.save();
/* 321:327 */           this.sm.adjudicator = adj;
/* 322:    */           
/* 323:329 */           Vector keys = new Vector();
/* 324:330 */           keys.add("key1");
/* 325:    */           
/* 326:332 */           TestXUIDB.getInstance().createChangeLog("keyvalue", "key1 like '/cme/" + p.pid + "/%'", keys);
/* 327:333 */           TestXUIDB.getInstance().createChangeLog("keyvalue", 
/* 328:334 */             "key1 like '/va/" + p.pid + "%'", keys);
/* 329:335 */           TestXUIDB.getInstance().sendServerLogs("admin", adj, bookmark, "999999");
/* 330:336 */           sendImage();
/* 331:337 */           Process.transition(p.pid);
/* 332:    */         }
/* 333:    */         return;
/* 334:    */       }
/* 335:    */       catch (Exception e)
/* 336:    */       {
/* 337:343 */         TestXUIDB.getInstance().logAgent(p.pid, getClass().getName(), this.sm.currentState, "Error:" + e.getMessage());
/* 338:344 */         e.printStackTrace();
/* 339:    */       }
/* 340:    */     }
/* 341:    */   }
/* 342:    */   
/* 343:    */   public Vector findPhysicians(String vaId)
/* 344:    */   {
/* 345:351 */     XModel workloadModel = new XBaseModel();
/* 346:    */     
/* 347:353 */     String language = TestXUIDB.getInstance().getValue("keyvalue", 
/* 348:354 */       "/va/" + vaId + "/gi/language");
/* 349:    */     
/* 350:356 */     String where = "a.adjudicator=1 and a.status='active'";
/* 351:357 */     if (this.sm.currentState.equals("adjreassignment")) {
/* 352:358 */       where = where + " and a.id !='" + this.sm.adjUnassigned + "'";
/* 353:    */     }
/* 354:359 */     TestXUIDB.getInstance().getPhysiciansWithLessWorkload(language, where, workloadModel);
/* 355:    */     
/* 356:361 */     Vector ind = new Vector();
/* 357:362 */     this.logger.info("Going to call assign");
/* 358:363 */     if (workloadModel.getNumChildren() >= 1) {
/* 359:365 */       for (int i = 0; i < workloadModel.getNumChildren(); i++)
/* 360:    */       {
/* 361:366 */         String phy = ((XModel)workloadModel.get(i).get("id")).get().toString();
/* 362:367 */         TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, " ADjudicator " + phy);
/* 363:368 */         this.logger.info("physician::" + phy + " " + this.sm.assignedfirst + " " + this.sm.assignedsecond + ((!phy.equals(this.sm.assignedfirst)) && (!phy.equals(this.sm.assignedsecond))));
/* 364:370 */         if ((!phy.equals(this.sm.assignedfirst)) && (!phy.equals(this.sm.assignedsecond)))
/* 365:    */         {
/* 366:372 */           ind.add(phy);
/* 367:373 */           break;
/* 368:    */         }
/* 369:    */       }
/* 370:    */     }
/* 371:380 */     return ind;
/* 372:    */   }
/* 373:    */   
/* 374:    */   public static void main(String[] args) {}
/* 375:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.ReMatchingAgent
 * JD-Core Version:    0.7.0.1
 */