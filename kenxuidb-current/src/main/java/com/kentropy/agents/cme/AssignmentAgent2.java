/*   1:    */ package com.kentropy.agents.cme;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.db.XTaskModel;
/*   5:    */ import com.kentropy.process.Agent;
/*   6:    */ import com.kentropy.process.CMEStateMachine2;
/*   7:    */ import com.kentropy.process.OfflineWorkListHandler;
/*   8:    */ import com.kentropy.process.Process;
/*   9:    */ import com.kentropy.process.StateMachine;
/*  10:    */ import com.kentropy.transfer.Client;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.text.SimpleDateFormat;
/*  13:    */ import java.util.Calendar;
/*  14:    */ import java.util.Date;
/*  15:    */ import java.util.GregorianCalendar;
/*  16:    */ import java.util.Vector;
/*  17:    */ import net.xoetrope.xui.data.XBaseModel;
/*  18:    */ import net.xoetrope.xui.data.XModel;
/*  19:    */ 
/*  20:    */ public class AssignmentAgent2
/*  21:    */   implements Agent
/*  22:    */ {
/*  23: 23 */   CMEStateMachine2 sm = null;
/*  24: 25 */   Process p = null;
/*  25: 27 */   public String ext = "png";
/*  26: 29 */   int i = 0;
/*  27:    */   String[] resources;
/*  28:    */   String[] resources1;
/*  29: 32 */   int count = 0;
/*  30:    */   
/*  31:    */   public void sendImage(String phy)
/*  32:    */     throws Exception
/*  33:    */   {
/*  34: 37 */     String imagepath = new TestXUIDB().getProperty("imagePath");
/*  35: 38 */     String firstpage = this.p.pid + "_0_blank." + this.ext;
/*  36: 39 */     String secondpage = this.p.pid + "_1_blank." + this.ext;
/*  37: 40 */     String codcrop = this.p.pid + "_cod." + this.ext;
/*  38:    */     
/*  39: 42 */     Client cl = new Client();
/*  40:    */     
/*  41: 44 */     cl.run(imagepath + firstpage, firstpage, phy);
/*  42: 45 */     cl.run(imagepath + secondpage, secondpage, phy);
/*  43: 46 */     cl.run(imagepath + codcrop, codcrop, phy);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void updateWorkload(String physician)
/*  47:    */   {
/*  48: 51 */     String sql = "update physician_workload set workload=workload+1 where physician='" + 
/*  49: 52 */       physician + "'";
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void assign(String phy)
/*  53:    */     throws Exception
/*  54:    */   {
/*  55: 58 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  56: 59 */     Calendar cal = new GregorianCalendar();
/*  57: 60 */     cal.add(5, 10);
/*  58: 61 */     String dueDate = sdf.format(cal.getTime());
/*  59: 62 */     String assignDate = sdf.format(new Date());
/*  60: 63 */     String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/*  61: 64 */     XTaskModel rootM = XTaskModel.getRoot(phy, 
/*  62: 65 */       "cme", "6");
/*  63: 66 */     XTaskModel cmeM = (XTaskModel)rootM.get("task0");
/*  64: 67 */     cmeM.area = "1";
/*  65: 68 */     cmeM.household = "1";
/*  66: 69 */     cmeM.house = "1";
/*  67: 70 */     cmeM.assignedTo = phy;
/*  68: 71 */     cmeM.set("@assignedto", phy);
/*  69: 72 */     XTaskModel codM1 = (XTaskModel)cmeM.get("task0-" + this.p.pid);
/*  70: 73 */     codM1.assignedTo = phy;
/*  71: 74 */     codM1.set("@assignedto", phy);
/*  72: 75 */     codM1.set("@dateassigned", assignDate);
/*  73: 76 */     codM1.set("@duedate", dueDate);
/*  74: 77 */     cmeM.save();
/*  75: 78 */     codM1.save();
/*  76:    */     
/*  77: 80 */     Vector keys = new Vector();
/*  78: 81 */     keys.add("key1");
/*  79: 82 */     keys.add("value1");
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void getResources()
/*  83:    */   {
/*  84: 87 */     String uniqno = this.p.pid.split("/")[1];
/*  85: 88 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/*  86: 89 */     String[] resourcesC = { imagepath + uniqno + "_0_blank." + this.ext, imagepath + uniqno + "_1_blank." + this.ext, imagepath + uniqno + "_cod." + this.ext };
/*  87: 90 */     String[] resourcesC1 = { uniqno + "_0_blank." + this.ext, uniqno + "_1_blank." + this.ext, uniqno + "_cod." + this.ext };
/*  88: 91 */     XModel dataModel = new XBaseModel();
/*  89: 92 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", "/va/" + this.p.pid);
/*  90: 93 */     String domain = dataModel.get("type/@value").toString();
/*  91:    */     
/*  92: 95 */     this.resources = resourcesC;
/*  93: 96 */     this.resources1 = resourcesC1;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void stateChange(Process p)
/*  97:    */   {
/*  98:101 */     this.p = p;
/*  99:102 */     if (p.states.getCurrentState().equals("assignment"))
/* 100:    */     {
/* 101:103 */       this.sm = ((CMEStateMachine2)p.states);
/* 102:104 */       System.out.println(" Doing assignment for Process " + p.pid);
/* 103:    */       
/* 104:106 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 105:    */       
/* 106:108 */       Vector v = findPhysicians(p.pid);
/* 107:109 */       System.out.println(" Assignment physicians " + v.size() + " pid=" + p.pid);
/* 108:111 */       if (v.size() < 1) {
/* 109:112 */         return;
/* 110:    */       }
/* 111:    */       try
/* 112:    */       {
/* 113:116 */         Calendar cal = new GregorianCalendar();
/* 114:117 */         cal.add(5, 10);
/* 115:118 */         String dueDate = sdf.format(cal.getTime());
/* 116:119 */         String assignDate = sdf.format(new Date());
/* 117:120 */         String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 118:    */         
/* 119:122 */         getResources();
/* 120:    */         
/* 121:124 */         assign1();
/* 122:    */       }
/* 123:    */       catch (Exception e)
/* 124:    */       {
/* 125:128 */         e.printStackTrace();
/* 126:    */       }
/* 127:131 */       Process.transition(p.pid);
/* 128:    */     }
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void assign1()
/* 132:    */   {
/* 133:137 */     OfflineWorkListHandler ofwh = new OfflineWorkListHandler();
/* 134:    */     
/* 135:139 */     System.out.println(" Doing assignment for Process " + this.p.pid);
/* 136:    */     
/* 137:141 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 138:    */     
/* 139:143 */     Vector v = findPhysicians(this.p.pid);
/* 140:144 */     if (v.size() < 1) {
/* 141:145 */       return;
/* 142:    */     }
/* 143:    */     try
/* 144:    */     {
/* 145:149 */       Calendar cal = new GregorianCalendar();
/* 146:150 */       cal.add(5, 10);
/* 147:151 */       String dueDate = sdf.format(cal.getTime());
/* 148:152 */       String assignDate = sdf.format(new Date());
/* 149:153 */       String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 150:155 */       synchronized (this)
/* 151:    */       {
/* 152:157 */         getResources();
/* 153:    */         
/* 154:159 */         String[] dataPath = { "/va/" + this.p.pid.split("/")[1] };
/* 155:161 */         for (int i = 0; i < v.size(); i++)
/* 156:    */         {
/* 157:163 */           String parentPath = "area:1/house:1/household:1";
/* 158:164 */           ofwh.assign("task0", v.get(i).toString(), "cme", "6", parentPath, null, null, null, null, null);
/* 159:165 */           ofwh.assign("task0/task0-" + this.p.pid.split("/")[1], v.get(i).toString(), "cme", "6", parentPath, assignDate, dueDate, dataPath, null, null);
/* 160:166 */           this.sm.assigned.add(v.get(i).toString());
/* 161:    */         }
/* 162:    */       }
/* 163:    */     }
/* 164:    */     catch (Exception e)
/* 165:    */     {
/* 166:173 */       e.printStackTrace();
/* 167:    */     }
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void batchExecute() {}
/* 171:    */   
/* 172:    */   public Vector findPhysicians(String vaId)
/* 173:    */   {
/* 174:183 */     XModel workloadModel = new XBaseModel();
/* 175:    */     
/* 176:185 */     String language = TestXUIDB.getInstance().getValue("keyvalue", 
/* 177:186 */       "/va/" + vaId.split("/")[1] + "/gi/language");
/* 178:187 */     XModel xm = new XBaseModel();
/* 179:    */     
/* 180:189 */     TestXUIDB.getInstance().getData("batch_physician", "count(*)", " batch='" + vaId.split("/")[0] + "'", xm);
/* 181:190 */     XModel xm1 = new XBaseModel();
/* 182:    */     
/* 183:192 */     TestXUIDB.getInstance().getData("adult", "count(*)", "", xm1);
/* 184:    */     
/* 185:194 */     int totphy = Integer.parseInt(xm.get(0).get(0).get().toString());
/* 186:195 */     int totrec = Integer.parseInt(xm1.get(0).get(0).get().toString());
/* 187:    */     
/* 188:197 */     TestXUIDB.getInstance().getPhysiciansWithLessWorkload(language, "a.coder=1 and current_tr_batch='" + vaId.split("/")[0] + "'", workloadModel);
/* 189:    */     
/* 190:199 */     Vector ind = new Vector();
/* 191:200 */     System.out.println("Going to call assign");
/* 192:202 */     for (int i = 0; i < Math.min(workloadModel.getNumChildren(), totphy * 50 / totrec); i++)
/* 193:    */     {
/* 194:203 */       String phy = ((XModel)workloadModel.get(i).get("id")).get().toString();
/* 195:204 */       System.out.println("physician::" + phy);
/* 196:205 */       ind.add(phy);
/* 197:    */     }
/* 198:208 */     return ind;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public static void main(String[] args)
/* 202:    */   {
/* 203:213 */     AssignmentAgent2 a = new AssignmentAgent2();
/* 204:214 */     a.findPhysicians("09100249_01_04");
/* 205:    */   }
/* 206:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.AssignmentAgent2
 * JD-Core Version:    0.7.0.1
 */