/*   1:    */ package com.kentropy.model;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.io.PrintWriter;
/*   7:    */ import net.xoetrope.data.XDataSource;
/*   8:    */ import net.xoetrope.xui.data.XBaseModel;
/*   9:    */ import net.xoetrope.xui.data.XModel;
/*  10:    */ import org.apache.log4j.Logger;
/*  11:    */ 
/*  12:    */ public class TaskTableAdapter
/*  13:    */ {
/*  14:    */   public static XModel createTableModel(XModel header, XModel data)
/*  15:    */   {
/*  16: 16 */     XModel retM = new XBaseModel();
/*  17: 17 */     retM.append(header);
/*  18: 19 */     for (int i = 0; i < data.getNumChildren(); i++)
/*  19:    */     {
/*  20: 21 */       XModel rowM = (XModel)retM.get(data.get(i).getId());
/*  21: 22 */       for (int j = 0; j < header.getNumChildren(); j++)
/*  22:    */       {
/*  23: 24 */         String id = header.get(j).getId();
/*  24: 25 */         XModel item = (XModel)data.get(i).get(id);
/*  25:    */         
/*  26: 27 */         rowM.append(item);
/*  27:    */       }
/*  28: 29 */       retM.append(rowM);
/*  29:    */     }
/*  30: 32 */     return retM;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static XModel getCurrentTasks(String team, String surveyType, KenList parentPath)
/*  34:    */     throws Exception
/*  35:    */   {
/*  36: 38 */     KenList taskPath = parentPath.left("-");
/*  37: 39 */     KenList dataPath = parentPath.right("-").subset(0, 3);
/*  38:    */     
/*  39: 41 */     XModel dataM = new XBaseModel();
/*  40: 42 */     dataM = TestXUIDB.getInstance().getTasks(surveyType, team, taskPath.toString(), dataPath);
/*  41: 43 */     XModel tm = new XBaseModel();
/*  42: 44 */     if (dataPath.size() == 0) {
/*  43: 46 */       for (int i = 0; i < dataM.getNumChildren(); i++)
/*  44:    */       {
/*  45: 48 */         String area = (String)dataM.get(i).get("@area");
/*  46: 49 */         if (area != null)
/*  47:    */         {
/*  48: 51 */           XModel rowm = TestXUIDB.getInstance().getAreadetails(area);
/*  49: 52 */           rowm.setId(area);
/*  50: 53 */           tm.append(rowm);
/*  51:    */         }
/*  52:    */       }
/*  53:    */     }
/*  54: 59 */     if (dataPath.size() == 1) {
/*  55: 61 */       for (int i = 0; i < dataM.getNumChildren(); i++)
/*  56:    */       {
/*  57: 63 */         String area = (String)dataM.get(i).get("@area");
/*  58: 65 */         if (area != null)
/*  59:    */         {
/*  60: 67 */           String house = (String)dataM.get(i).get("@house");
/*  61: 68 */           XModel rowm = TestXUIDB.getInstance().getHousedetails(house, area);
/*  62: 69 */           rowm.append(TestXUIDB.getInstance().getDataM("data", "gpscheck", area, house, null, null));
/*  63: 70 */           rowm.setId(house);
/*  64: 71 */           tm.append(rowm);
/*  65:    */         }
/*  66:    */       }
/*  67:    */     }
/*  68: 75 */     if (dataPath.size() == 2) {
/*  69: 77 */       for (int i = 0; i < dataM.getNumChildren(); i++)
/*  70:    */       {
/*  71: 79 */         String area = (String)dataM.get(i).get("@area");
/*  72: 80 */         if (area != null)
/*  73:    */         {
/*  74: 82 */           String house = dataM.get(i).get("@house").toString();
/*  75: 83 */           String household = dataM.get(i).get("@household").toString();
/*  76: 84 */           XModel rowm = TestXUIDB.getInstance().getHouseholddetails(household, house, area);
/*  77: 85 */           rowm.setId(household);
/*  78: 86 */           tm.append(rowm);
/*  79:    */         }
/*  80:    */       }
/*  81:    */     }
/*  82: 90 */     if (dataPath.size() > 2) {
/*  83: 92 */       for (int i = 0; i < dataM.getNumChildren(); i++)
/*  84:    */       {
/*  85: 94 */         String area = (String)dataM.get(i).get("@area");
/*  86: 95 */         if (area != null)
/*  87:    */         {
/*  88: 97 */           String house = dataM.get(i).get("@house").toString();
/*  89: 98 */           String household = dataM.get(i).get("@household").toString();
/*  90: 99 */           String individual = dataM.get(i).get("@member").toString();
/*  91:100 */           XModel rowm = TestXUIDB.getInstance().getIndividualdetails(individual, household, house, area);
/*  92:101 */           rowm.setId(individual);
/*  93:102 */           tm.append(rowm);
/*  94:    */         }
/*  95:    */       }
/*  96:    */     }
/*  97:108 */     return tm;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static XModel getCMECurrentTasks(String team, String surveyType, KenList parentPath)
/* 101:    */     throws Exception
/* 102:    */   {
/* 103:114 */     KenList taskPath = parentPath.left("-");
/* 104:115 */     KenList dataPath = parentPath.right("-").subset(0, 3);
/* 105:    */     
/* 106:117 */     XModel dataM = new XBaseModel();
/* 107:118 */     Logger logger = Logger.getLogger(TaskTableAdapter.class);
/* 108:119 */     dataM = TestXUIDB.getInstance().getTasks1(surveyType, team, taskPath.toString(), dataPath);
/* 109:120 */     XModel tm = new XBaseModel();
/* 110:121 */     logger.info(" No of tasks " + dataM.getNumChildren());
/* 111:123 */     for (int i = 0; i < dataM.getNumChildren(); i++)
/* 112:    */     {
/* 113:125 */       String report = (String)dataM.get(i).get("@member");
/* 114:126 */       String dateAssigned = (String)dataM.get(i).get("@starttime");
/* 115:127 */       String dueDate = (String)dataM.get(i).get("@duedate");
/* 116:128 */       String status = (String)dataM.get(i).get("@status");
/* 117:129 */       System.out.println(report);
/* 118:130 */       logger.info(" REport:" + report + "Status :" + status);
/* 119:131 */       if (report != null)
/* 120:    */       {
/* 121:133 */         logger.info(" Processing REport:" + report + "Status :" + status);
/* 122:134 */         XModel rowm = new XBaseModel();
/* 123:135 */         rowm.setId(report);
/* 124:136 */         String stat = "Not Started ";
/* 125:138 */         if (status != null) {
/* 126:139 */           stat = status.equals("1") ? "Completed" : "In Process";
/* 127:    */         }
/* 128:141 */         rowm.set("status", stat);
/* 129:142 */         rowm.set("dateassigned", dateAssigned.substring(0, 10));
/* 130:143 */         rowm.set("duedate", dueDate.substring(0, 10));
/* 131:144 */         TestXUIDB.getInstance().getKeyValues(rowm, "keyvalue", "/va/" + report + "/gi");
/* 132:145 */         logger.info(" Before Stage ");
/* 133:146 */         String stage = TestXUIDB.getInstance().getValue("keyvalue", "/cme/" + report + "/stage");
/* 134:147 */         logger.info(" After Stage " + null);
/* 135:148 */         stage = stage == null ? "Coding" : stage;
/* 136:149 */         String type = TestXUIDB.getInstance().getValue("keyvalue", "/va/" + report + "/type");
/* 137:150 */         rowm.set("stage", stage);
/* 138:151 */         rowm.set("type", type);
/* 139:152 */         rowm.set("idc", report);
/* 140:153 */         XDataSource.outputModel(new PrintWriter(System.out), rowm);
/* 141:154 */         tm.append(rowm);
/* 142:    */       }
/* 143:    */     }
/* 144:159 */     return tm;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public static void main(String[] args)
/* 148:    */     throws Exception
/* 149:    */   {
/* 150:165 */     XModel tt1 = getCMECurrentTasks("9", "6", new KenList("task0"));
/* 151:166 */     XDataSource.outputModel(new PrintWriter(System.out), tt1);
/* 152:167 */     XModel xm = new XBaseModel();
/* 153:    */     
/* 154:169 */     System.in.read();
/* 155:170 */     XModel header = new XBaseModel();
/* 156:171 */     header.setId("header");
/* 157:    */     
/* 158:173 */     header.get("houseno");
/* 159:174 */     header.get("slNo");
/* 160:175 */     header.get("latitude");
/* 161:176 */     header.get("doorNo");
/* 162:177 */     header.get("address1");
/* 163:178 */     XModel tt = getCurrentTasks("12", "3", new KenList("task0/task0-13/task0"));
/* 164:179 */     new KenRandomGPSOrderer().order(tt);
/* 165:180 */     tt = createTableModel(header, tt);
/* 166:    */     
/* 167:182 */     XDataSource.outputModel(new PrintWriter(System.out), tt);
/* 168:183 */     System.out.println(tt.get(1).getNumChildren());
/* 169:    */   }
/* 170:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.model.TaskTableAdapter
 * JD-Core Version:    0.7.0.1
 */