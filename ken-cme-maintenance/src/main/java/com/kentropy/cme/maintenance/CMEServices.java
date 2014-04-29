/*   1:    */ package com.kentropy.cme.maintenance;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.CMEController;
/*   4:    */ import com.kentropy.db.TestXUIDB;
/*   5:    */ import com.kentropy.util.DbUtil;
/*   6:    */ import com.kentropy.util.SpringApplicationContext;
/*   7:    */ import com.kentropy.util.SpringUtils;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Vector;
/*  12:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  13:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  14:    */ 
/*  15:    */ public class CMEServices
/*  16:    */ {
/*  17: 19 */   DbUtil db = new DbUtil();
/*  18: 20 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  19: 21 */   String[] args = new String[0];
/*  20: 22 */   String basepath = (String)SpringApplicationContext.getBean("basepath");
/*  21:    */   
/*  22:    */   public synchronized void resendTasks(String[] taskIds, boolean deleteOp)
/*  23:    */     throws Exception
/*  24:    */   {
/*  25: 28 */     for (String taskId : taskIds)
/*  26:    */     {
/*  27: 30 */       String stage = getTaskStage(taskId);
/*  28: 31 */       System.out.println(taskId + " " + stage);
/*  29:    */       
/*  30: 33 */       resendTask(taskId, stage, deleteOp);
/*  31:    */     }
/*  32: 40 */     resendImagesWithoutDuplicateEntriesInMqueue(taskIds);
/*  33:    */     
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37: 45 */     CMEController.sendOutboundMessages(this.args);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void resendImagesWithoutDuplicateEntriesInMqueue(String[] taskIds)
/*  41:    */     throws Exception
/*  42:    */   {
/*  43: 56 */     for (String taskId : taskIds)
/*  44:    */     {
/*  45: 58 */       Map<String, String> task = (Map)this.db.getDataAsListofMaps("tasks", "member,assignedto", "id=?", new Object[] { taskId }).get(0);
/*  46:    */       
/*  47: 60 */       String uniqno = (String)task.get("member");
/*  48: 61 */       String recepient = (String)task.get("assignedto");
/*  49:    */       
/*  50: 63 */       String page1 = uniqno + "_0_blank.png";
/*  51: 64 */       String page2 = uniqno + "_1_blank.png";
/*  52: 65 */       String cod = uniqno + "_cod.png";
/*  53:    */       
/*  54: 67 */       String[] images = { page1, page2, cod };
/*  55: 69 */       for (String image : images) {
/*  56: 71 */         if (checkIfExistsInMqueue(image + ".txt", recepient))
/*  57:    */         {
/*  58: 73 */           if (hasAlreadyDownloaded(image + ".txt", recepient)) {
/*  59: 74 */             setStatusNullToDownloadAgain(image + ".txt", recepient);
/*  60:    */           }
/*  61:    */         }
/*  62:    */         else {
/*  63: 80 */           createEntryForDownload(image, recepient);
/*  64:    */         }
/*  65:    */       }
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public synchronized void resendTask(String taskId, String stage, boolean deleteOp)
/*  70:    */     throws Exception
/*  71:    */   {
/*  72:104 */     String recepient = this.db.uniqueResult("tasks", "assignedto", "id=?", new Object[] { taskId });
/*  73:105 */     String uniqno = this.db.uniqueResult("tasks", "member", "id=?", new Object[] { taskId });
/*  74:106 */     String[] stateMachine = this.db.uniqueResult("process", "stateMachine", "pid=?", new Object[] { uniqno }).split(",");
/*  75:107 */     String phy1 = stateMachine[4];
/*  76:108 */     String phy2 = stateMachine[5];
/*  77:109 */     String adj = stateMachine[6];
/*  78:110 */     String[] assigned = { phy1, phy2, adj };
/*  79:    */     
/*  80:    */ 
/*  81:    */ 
/*  82:    */ 
/*  83:115 */     String[] taskKeyFields = { "id" };
/*  84:116 */     String[] keyvalueKeyFields = { "key1" };
/*  85:119 */     if (deleteOp)
/*  86:    */     {
/*  87:122 */       deleteChangelogs("tasks", "id='" + taskId + "'", recepient);
/*  88:123 */       deleteChangelogs("keyvalue", "key1 LIKE '%/" + uniqno + "/%'", recepient);
/*  89:    */     }
/*  90:127 */     createChangelogs("tasks", "id='" + taskId + "'", recepient, taskKeyFields);
/*  91:130 */     if (stage.equals("coding"))
/*  92:    */     {
/*  93:135 */       createChangelogs("keyvalue", "key1 like '/va/" + uniqno + "/%'", recepient, keyvalueKeyFields);
/*  94:    */     }
/*  95:140 */     else if (stage.equals("reconciliation"))
/*  96:    */     {
/*  97:144 */       createChangelogs("keyvalue", "key1 like '/va/" + uniqno + "/%'", recepient, keyvalueKeyFields);
/*  98:    */       
/*  99:146 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Coding/" + phy1 + "/%'", recepient, keyvalueKeyFields);
/* 100:147 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/stage'", recepient, keyvalueKeyFields);
/* 101:148 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Coding/Comments/" + phy1 + "/%'", recepient, keyvalueKeyFields);
/* 102:149 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Coding/" + phy2 + "/%'", recepient, keyvalueKeyFields);
/* 103:150 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Coding/Comments/" + phy2 + "/%'", recepient, keyvalueKeyFields);
/* 104:    */     }
/* 105:154 */     else if (stage.equals("adjudication"))
/* 106:    */     {
/* 107:159 */       createChangelogs("keyvalue", "key1 like '/va/" + uniqno + "%'", recepient, keyvalueKeyFields);
/* 108:    */       
/* 109:    */ 
/* 110:162 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Coding/" + phy1 + "/%'", recepient, keyvalueKeyFields);
/* 111:163 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/stage'", recepient, keyvalueKeyFields);
/* 112:164 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Coding/Comments/" + phy1 + "/%'", recepient, keyvalueKeyFields);
/* 113:165 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Coding/" + phy2 + "/%'", recepient, keyvalueKeyFields);
/* 114:166 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Coding/Comments/" + phy2 + "/%'", recepient, keyvalueKeyFields);
/* 115:    */       
/* 116:    */ 
/* 117:169 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Reconciliation/" + phy1 + "/%'", recepient, keyvalueKeyFields);
/* 118:170 */       createChangelogs("keyvalue", "key1 like '/cme/" + uniqno + "/Reconciliation/" + phy2 + "/%'", recepient, keyvalueKeyFields);
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void createEntryForDownload(String image, String recepient)
/* 123:    */     throws Exception
/* 124:    */   {
/* 125:185 */     TestXUIDB.getInstance().addToResourceOutboundQueue(recepient, this.basepath + "/data/split/" + image, image, "admin");
/* 126:    */   }
/* 127:    */   
/* 128:    */   public String getTaskStage(String taskId)
/* 129:    */   {
/* 130:196 */     return this.db.uniqueResult("tasks", "IF(task like '%task0/task0','coding',IF(task like '%task0/task1','reconciliation',IF(task like '%task0/task2','adjudication','duplicate')))", "id=?", new Object[] { taskId });
/* 131:    */   }
/* 132:    */   
/* 133:    */   public boolean checkIfExistsInMqueue(String image, String recepient)
/* 134:    */   {
/* 135:204 */     int count = this.jt.queryForInt("SELECT COUNT(*) FROM mqueue WHERE message=? AND recepient=?", new Object[] { image, recepient });
/* 136:206 */     if (count == 0) {
/* 137:207 */       return false;
/* 138:    */     }
/* 139:210 */     return true;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public boolean hasAlreadyDownloaded(String image, String recepient)
/* 143:    */   {
/* 144:216 */     int count = this.jt.queryForInt("SELECT COUNT(*) FROM mqueue WHERE message=? AND recepient=? AND status=1", new Object[] { image, recepient });
/* 145:218 */     if (count == 0) {
/* 146:219 */       return false;
/* 147:    */     }
/* 148:222 */     return true;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setStatusNullToDownloadAgain(String image, String recepient)
/* 152:    */   {
/* 153:229 */     this.jt.update("UPDATE mqueue set STATUS=NULL WHERE message=? AND recepient=? LIMIT 1", new Object[] { image, recepient });
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void deleteChangelogs(String table, String where, String recepient)
/* 157:    */     throws Exception
/* 158:    */   {
/* 159:253 */     String from = TestXUIDB.getInstance().getLastChangeLog();
/* 160:254 */     TestXUIDB.getInstance().createDeleteLog(table, where);
/* 161:255 */     String to = TestXUIDB.getInstance().getLastChangeLog();
/* 162:257 */     if ((from != null) && (to != null)) {
/* 163:259 */       if ((!from.equals("")) && (!to.equals("")) && (!from.equals(to))) {
/* 164:261 */         TestXUIDB.getInstance().addToChangeLogOutboundQueue(recepient, from, to);
/* 165:    */       }
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   public StringBuffer getDistinctYearsAsCommaSeparated()
/* 170:    */   {
/* 171:269 */     StringBuffer years = new StringBuffer();
/* 172:270 */     SqlRowSet rs = this.jt.queryForRowSet("SELECT * FROM (SELECT DISTINCT(DATE_FORMAT(time1,'%Y')) `year` FROM cme_report)a WHERE `year` IS NOT NULL");
/* 173:272 */     while (rs.next()) {
/* 174:274 */       years.append(rs.getString(1) + ",");
/* 175:    */     }
/* 176:277 */     years.deleteCharAt(years.length() - 1);
/* 177:278 */     return years;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public StringBuffer getDistinctMonthsAsCommaSeparated()
/* 181:    */   {
/* 182:282 */     StringBuffer months = new StringBuffer();
/* 183:283 */     SqlRowSet rs = this.jt.queryForRowSet("SELECT * FROM (SELECT DISTINCT(DATE_FORMAT(time1,'%m')) `month` FROM cme_report)a WHERE `month` IS NOT NULL");
/* 184:285 */     while (rs.next()) {
/* 185:287 */       months.append(rs.getString(1) + ",");
/* 186:    */     }
/* 187:290 */     months.deleteCharAt(months.length() - 1);
/* 188:291 */     return months;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public synchronized void createChangelogs(String table, String where, String recepient, String[] keyFields)
/* 192:    */     throws Exception
/* 193:    */   {
/* 194:295 */     Vector bookmarks = new Vector();
/* 195:296 */     Vector keyFieldsVector = new Vector();
/* 196:298 */     for (int i = 0; i < keyFields.length; i++) {
/* 197:300 */       keyFieldsVector.add(keyFields[i]);
/* 198:    */     }
/* 199:303 */     String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 200:    */     
/* 201:    */ 
/* 202:306 */     TestXUIDB.getInstance().createChangeLog(table, where, keyFieldsVector);
/* 203:307 */     String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 204:309 */     if ((frombookmark != null) && (tobookmark != null)) {
/* 205:311 */       if (!frombookmark.equals(tobookmark)) {
/* 206:312 */         TestXUIDB.getInstance().addToChangeLogOutboundQueue(recepient, frombookmark, tobookmark);
/* 207:    */       }
/* 208:    */     }
/* 209:314 */     System.out.println("from=" + frombookmark + " to=" + tobookmark);
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void validateXmlFiles(String whereCondition)
/* 213:    */   {
/* 214:342 */     String sql = "";
/* 215:    */     
/* 216:344 */     TestXUIDB xuidb = TestXUIDB.getInstance();
/* 217:    */     
/* 218:346 */     String basepath = (String)SpringApplicationContext.getBean("basepath");
/* 219:347 */     String fullPath = basepath + "/mbox/";
/* 220:351 */     if (whereCondition == null) {
/* 221:352 */       sql = "SELECT id,message FROM  mqueue WHERE message like 'admin%'";
/* 222:    */     } else {
/* 223:355 */       sql = "SELECT id,message FROM mqueue WHERE " + whereCondition;
/* 224:    */     }
/* 225:357 */     SqlRowSet rs = this.jt.queryForRowSet(sql);
/* 226:360 */     while (rs.next()) {
/* 227:    */       try
/* 228:    */       {
/* 229:366 */         xuidb.validateMessage(fullPath + rs.getString("message"));
/* 230:    */         
/* 231:368 */         this.jt.update("UPDATE mqueue SET isvalid=1 WHERE id=?", new Object[] { rs.getString("id") });
/* 232:    */       }
/* 233:    */       catch (Exception e)
/* 234:    */       {
/* 235:373 */         e.printStackTrace();
/* 236:374 */         this.jt.update("UPDATE mqueue SET isvalid=0 WHERE id=?", new Object[] { rs.getString("id") });
/* 237:375 */         System.out.println("Failure " + rs.getString("message"));
/* 238:    */       }
/* 239:    */     }
/* 240:    */   }
/* 241:    */   
/* 242:    */   public void updateFailedBillables()
/* 243:    */   {
/* 244:391 */     String sql = "SELECT a.pid,b.report_id FROM `process`  a LEFT JOIN billables b ON a.pid=b.report_id WHERE  a.stateMachine LIKE '%complete%' AND report_id IS NULL";
/* 245:    */     
/* 246:393 */     SqlRowSet rs = this.jt.queryForRowSet(sql);
/* 247:395 */     while (rs.next())
/* 248:    */     {
/* 249:397 */       String uniqno = rs.getString("pid");
/* 250:    */       
/* 251:399 */       getProcessData(uniqno);
/* 252:    */     }
/* 253:    */   }
/* 254:    */   
/* 255:    */   public void getProcessData(String uniqno)
/* 256:    */   {
/* 257:410 */     String[] stateMachine = this.db.uniqueResult("process", "stateMachine", "pid=?", new Object[] { uniqno }).split(",");
/* 258:411 */     String phy1 = stateMachine[4];
/* 259:412 */     String phy2 = stateMachine[5];
/* 260:413 */     String adj = stateMachine[6];
/* 261:418 */     if (adj.matches("\\d+")) {
/* 262:421 */       updateBillables(adj, uniqno, "adjudication");
/* 263:    */     }
/* 264:425 */     updateBillables(phy1, uniqno, "coding");
/* 265:426 */     updateBillables(phy2, uniqno, "coding");
/* 266:    */   }
/* 267:    */   
/* 268:    */   public void updateBillables(String phy, String uniqno, String stage)
/* 269:    */   {
/* 270:437 */     this.jt.update("insert into billables (physician_id,report_id,date_of_billable,role) VALUES(?,?,NOW(),?)", new Object[] { phy, uniqno, stage });
/* 271:    */   }
/* 272:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-maintenance\ken-cme-maintenance.jar
 * Qualified Name:     com.kentropy.cme.maintenance.CMEServices
 * JD-Core Version:    0.7.0.1
 */