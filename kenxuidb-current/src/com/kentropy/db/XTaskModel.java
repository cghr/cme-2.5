/*   1:    */ package com.kentropy.db;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.io.StringWriter;
/*   6:    */ import java.text.DateFormat;
/*   7:    */ import java.util.Date;
/*   8:    */ import java.util.StringTokenizer;
/*   9:    */ import java.util.Vector;
/*  10:    */ import net.xoetrope.data.XDataSource;
/*  11:    */ import net.xoetrope.xui.data.XBaseModel;
/*  12:    */ import net.xoetrope.xui.data.XModel;
/*  13:    */ 
/*  14:    */ public class XTaskModel
/*  15:    */   extends XBaseModel
/*  16:    */ {
/*  17:    */   public String table;
/*  18:    */   public String where;
/*  19: 18 */   public String task = "";
/*  20: 19 */   public String assignedTo = "";
/*  21: 20 */   public String surveyType = "";
/*  22: 21 */   public String area = "";
/*  23: 22 */   public String house = "";
/*  24: 23 */   public String household = "";
/*  25: 24 */   public String member = "";
/*  26: 25 */   public boolean fetched = false;
/*  27: 27 */   String constraint = "";
/*  28: 29 */   Vector constraints = new Vector();
/*  29: 30 */   int idx = 0;
/*  30:    */   
/*  31:    */   public Vector getConstraints()
/*  32:    */   {
/*  33: 34 */     return this.constraints;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setConstraints(Vector constraints)
/*  37:    */   {
/*  38: 39 */     this.constraints = constraints;
/*  39: 40 */     if (constraints.size() > this.idx) {
/*  40: 41 */       this.constraint = constraints.get(this.idx).toString();
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setDataContext(String dataId)
/*  45:    */   {
/*  46: 46 */     if (this.area.equals("")) {
/*  47: 48 */       this.area = dataId;
/*  48: 50 */     } else if ((this.house == null) || (this.house.equals(""))) {
/*  49: 52 */       this.house = dataId;
/*  50: 54 */     } else if ((this.household == null) || (this.household.equals(""))) {
/*  51: 55 */       this.household = dataId;
/*  52: 57 */     } else if ((this.member == null) || (this.member.equals(""))) {
/*  53: 58 */       this.member = dataId;
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int getCount()
/*  58:    */   {
/*  59: 63 */     return TestXUIDB.getInstance().getTaskChildCount(this.task, this.assignedTo, this, this.area, this.house, this.household, this.member);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void readChildren()
/*  63:    */   {
/*  64: 68 */     if (!this.fetched)
/*  65:    */     {
/*  66: 70 */       TestXUIDB.getInstance().getTasks(this.task, this.assignedTo, this, this.surveyType, this.area, this.house, this.household, this.member);
/*  67:    */       
/*  68: 72 */       this.fetched = true;
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void readChild(String id)
/*  73:    */   {
/*  74: 77 */     if (super.getNumChildren() == 0)
/*  75:    */     {
/*  76: 79 */       StringTokenizer st = new StringTokenizer(id, "-");
/*  77: 80 */       String task = "";
/*  78: 81 */       if (st.hasMoreTokens()) {
/*  79: 82 */         task = st.nextToken();
/*  80:    */       } else {
/*  81: 86 */         task = id;
/*  82:    */       }
/*  83: 87 */       String path = task + "/" + task;
/*  84: 88 */       TestXUIDB.getInstance().getTask(path, this.assignedTo, this, this.surveyType, this.area, this.house, this.household, this.member);
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   public XTaskModel createProto()
/*  89:    */   {
/*  90: 94 */     return null;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void save()
/*  94:    */     throws Exception
/*  95:    */   {
/*  96: 99 */     TestXUIDB.getInstance().saveTask(this);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void updateStatus(String status)
/* 100:    */     throws Exception
/* 101:    */   {
/* 102:105 */     TestXUIDB.getInstance().updateTaskStatus(this, status);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void saveTree()
/* 106:    */     throws Exception
/* 107:    */   {
/* 108:111 */     save();
/* 109:112 */     for (int i = 0; i < getNumChildren(); i++) {
/* 110:114 */       ((XTaskModel)get(i)).saveTree();
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public Object get()
/* 115:    */   {
/* 116:120 */     System.out.println("Debug Get called");
/* 117:121 */     return super.get();
/* 118:    */   }
/* 119:    */   
/* 120:    */   public XModel get(int arg0)
/* 121:    */   {
/* 122:126 */     readChildren();
/* 123:    */     
/* 124:128 */     return super.get(arg0);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public Object getWithCreate(String arg0)
/* 128:    */   {
/* 129:133 */     System.out.println("Debug Get1 called " + arg0);
/* 130:134 */     if ((arg0 != null) && (!arg0.startsWith("@")))
/* 131:    */     {
/* 132:136 */       StringTokenizer st = new StringTokenizer(arg0, "/");
/* 133:137 */       String task = "";
/* 134:138 */       if (st.hasMoreTokens()) {
/* 135:139 */         task = st.nextToken();
/* 136:    */       } else {
/* 137:143 */         task = arg0;
/* 138:    */       }
/* 139:146 */       readChildren();
/* 140:    */     }
/* 141:149 */     return super.get(arg0);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public Object append(String arg0)
/* 145:    */   {
/* 146:154 */     System.out.println("Append Called" + arg0);
/* 147:155 */     StringTokenizer st1 = new StringTokenizer(arg0, "/");
/* 148:156 */     String currentId = arg0;
/* 149:157 */     if (st1.hasMoreTokens()) {
/* 150:158 */       currentId = st1.nextToken();
/* 151:    */     }
/* 152:160 */     XTaskModel taskM = new XTaskModel();
/* 153:    */     
/* 154:162 */     taskM.surveyType = this.surveyType;
/* 155:163 */     taskM.assignedTo = this.assignedTo;
/* 156:164 */     taskM.area = this.area;
/* 157:165 */     taskM.house = this.house;
/* 158:166 */     taskM.household = this.household;
/* 159:167 */     taskM.member = this.member;
/* 160:    */     
/* 161:169 */     StringTokenizer st = new StringTokenizer(currentId, "-");
/* 162:170 */     String taskId = currentId;
/* 163:171 */     String dataId = "";
/* 164:172 */     if (st.hasMoreTokens())
/* 165:    */     {
/* 166:174 */       taskId = st.nextToken();
/* 167:175 */       if (st.hasMoreTokens()) {
/* 168:176 */         dataId = st.nextToken();
/* 169:    */       }
/* 170:    */     }
/* 171:    */     else
/* 172:    */     {
/* 173:180 */       taskId = currentId;
/* 174:    */     }
/* 175:181 */     taskM.setId(currentId);
/* 176:182 */     taskM.task = (this.task + "/" + taskId);
/* 177:183 */     taskM.setDataContext(dataId);
/* 178:184 */     this.idx += 1;
/* 179:185 */     taskM.setConstraints(this.constraints);
/* 180:    */     
/* 181:187 */     String sttime = TestXUIDB.getInstance().getMysqlDateFormat().format(new Date());
/* 182:188 */     taskM.set("@starttime", sttime);
/* 183:189 */     super.append(taskM);
/* 184:190 */     return taskM;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void append(XModel arg0)
/* 188:    */   {
/* 189:195 */     System.out.println("Append1 Called" + arg0.getId());
/* 190:196 */     if ((arg0 instanceof XTaskModel))
/* 191:    */     {
/* 192:198 */       ((XTaskModel)arg0).idx = (this.idx + 1);
/* 193:199 */       ((XTaskModel)arg0).setConstraints(this.constraints);
/* 194:    */     }
/* 195:201 */     super.append(arg0);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public Object get(String arg0)
/* 199:    */   {
/* 200:206 */     System.out.println("Debug Get1 called " + arg0);
/* 201:207 */     if ((arg0 != null) && (!arg0.startsWith("@")))
/* 202:    */     {
/* 203:209 */       StringTokenizer st = new StringTokenizer(arg0, "/");
/* 204:210 */       String task = "";
/* 205:211 */       if (st.hasMoreTokens()) {
/* 206:212 */         task = st.nextToken();
/* 207:    */       } else {
/* 208:216 */         task = arg0;
/* 209:    */       }
/* 210:219 */       readChildren();
/* 211:    */     }
/* 212:222 */     return super.get(arg0);
/* 213:    */   }
/* 214:    */   
/* 215:    */   public String getId()
/* 216:    */   {
/* 217:227 */     return super.getId();
/* 218:    */   }
/* 219:    */   
/* 220:    */   public int getNumChildren()
/* 221:    */   {
/* 222:232 */     System.out.println(" Get Num called " + this.task);
/* 223:    */     
/* 224:234 */     readChildren();
/* 225:    */     
/* 226:236 */     return super.getNumChildren();
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void set(Object arg0)
/* 230:    */   {
/* 231:241 */     super.set(arg0);
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void set(String arg0, Object arg1)
/* 235:    */   {
/* 236:246 */     super.set(arg0, arg1);
/* 237:    */   }
/* 238:    */   
/* 239:    */   public void setId(String arg0)
/* 240:    */   {
/* 241:251 */     super.setId(arg0);
/* 242:    */   }
/* 243:    */   
/* 244:    */   protected Object clone()
/* 245:    */     throws CloneNotSupportedException
/* 246:    */   {
/* 247:257 */     return super.clone();
/* 248:    */   }
/* 249:    */   
/* 250:    */   public static XTaskModel getRoot(String assignedTo, String surveyType, String surveyTypeId)
/* 251:    */   {
/* 252:262 */     XTaskModel tM = new XTaskModel();
/* 253:263 */     System.out.println("assigned=" + assignedTo);
/* 254:264 */     tM.setId("tasks");
/* 255:265 */     tM.task = ("taskdefinitions/" + surveyType + "_taskdefinition");
/* 256:266 */     tM.surveyType = surveyTypeId;
/* 257:267 */     tM.assignedTo = assignedTo;
/* 258:268 */     return tM;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public static void iterate(XTaskModel xt)
/* 262:    */   {
/* 263:273 */     for (int i = 0; i < xt.getNumChildren(); i++)
/* 264:    */     {
/* 265:275 */       System.out.println(" iterator " + ((XTaskModel)xt.get(i)).task + " constraint " + ((XTaskModel)xt.get(i)).constraint + " idx " + ((XTaskModel)xt.get(i)).idx);
/* 266:276 */       iterate((XTaskModel)xt.get(i));
/* 267:    */     }
/* 268:    */   }
/* 269:    */   
/* 270:    */   public static void main(String[] args)
/* 271:    */     throws Exception
/* 272:    */   {
/* 273:282 */     XBaseModel xtm = new XBaseModel();
/* 274:    */     
/* 275:284 */     XTaskModel tM = getRoot("108", "cme", "6");
/* 276:285 */     XDataSource xd = new XDataSource();
/* 277:286 */     StringWriter sw = new StringWriter();
/* 278:287 */     XDataSource.outputModel(sw, tM);
/* 279:288 */     System.out.println(sw.toString());
/* 280:289 */     System.in.read();
/* 281:    */     
/* 282:291 */     Vector constraints = new Vector();
/* 283:292 */     constraints.add("task like '%task0'");
/* 284:293 */     constraints.add("task like '%task0'");
/* 285:294 */     constraints.add("task like '%task1'");
/* 286:295 */     constraints.add("task like '%task1'");
/* 287:    */     
/* 288:297 */     constraints.add("task like '%task4' and endtime > '2010-04-27'");
/* 289:298 */     constraints.add("task like '%task19' and endtime > '2010-04-27'");
/* 290:299 */     constraints.add("endtime > '2010-04-27'");
/* 291:300 */     constraints.add("endtime > '2010-04-27'");
/* 292:301 */     tM.setConstraints(new Vector());
/* 293:302 */     iterate(tM);
/* 294:    */     
/* 295:304 */     System.out.println("-----------------------------------");
/* 296:    */     
/* 297:306 */     System.out.println("-----------------------------------");
/* 298:    */   }
/* 299:    */   
/* 300:    */   public String getArea()
/* 301:    */   {
/* 302:311 */     return this.area;
/* 303:    */   }
/* 304:    */   
/* 305:    */   public void setArea(String area)
/* 306:    */   {
/* 307:316 */     this.area = area;
/* 308:    */   }
/* 309:    */   
/* 310:    */   public String getAssignedTo()
/* 311:    */   {
/* 312:321 */     return this.assignedTo;
/* 313:    */   }
/* 314:    */   
/* 315:    */   public void setAssignedTo(String assignedTo)
/* 316:    */   {
/* 317:326 */     this.assignedTo = assignedTo;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public String getHouse()
/* 321:    */   {
/* 322:331 */     return this.house;
/* 323:    */   }
/* 324:    */   
/* 325:    */   public void setHouse(String house)
/* 326:    */   {
/* 327:336 */     this.house = house;
/* 328:    */   }
/* 329:    */   
/* 330:    */   public String getHousehold()
/* 331:    */   {
/* 332:341 */     return this.household;
/* 333:    */   }
/* 334:    */   
/* 335:    */   public void setHousehold(String household)
/* 336:    */   {
/* 337:346 */     this.household = household;
/* 338:    */   }
/* 339:    */   
/* 340:    */   public String getMember()
/* 341:    */   {
/* 342:351 */     return this.member;
/* 343:    */   }
/* 344:    */   
/* 345:    */   public void setMember(String member)
/* 346:    */   {
/* 347:356 */     this.member = member;
/* 348:    */   }
/* 349:    */   
/* 350:    */   public String getTask()
/* 351:    */   {
/* 352:361 */     return this.task;
/* 353:    */   }
/* 354:    */   
/* 355:    */   public void setTask(String task)
/* 356:    */   {
/* 357:366 */     this.task = task;
/* 358:    */   }
/* 359:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.db.XTaskModel
 * JD-Core Version:    0.7.0.1
 */