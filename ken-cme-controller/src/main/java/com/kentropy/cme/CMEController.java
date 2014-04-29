/*   1:    */ package com.kentropy.cme;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.qa.transformation.Transformation;
/*   4:    */ import com.kentropy.cme.transformation.CMEImportTransformation;
/*   5:    */ import com.kentropy.cme.transformation.CMEImportTransformationFilter;
/*   6:    */ import com.kentropy.cme.transformation.CMEReportTransformation;
/*   7:    */ import com.kentropy.db.TestXUIDB;
/*   8:    */ import com.kentropy.process.Process;
/*   9:    */ import com.kentropy.util.SpringUtils;
/*  10:    */ import java.io.File;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.sql.SQLException;
/*  13:    */ import java.text.SimpleDateFormat;
/*  14:    */ import java.util.Date;
/*  15:    */ import java.util.Hashtable;
/*  16:    */ import java.util.Vector;
/*  17:    */ import net.xoetrope.xui.data.XBaseModel;
/*  18:    */ import net.xoetrope.xui.data.XModel;
/*  19:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  20:    */ 
/*  21:    */ public class CMEController
/*  22:    */ {
/*  23:    */   public void assignAll()
/*  24:    */   {
/*  25: 32 */     XModel xm = TestXUIDB.getInstance().getDataM1("cme_records2", null);
/*  26: 33 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  27:    */     {
/*  28: 34 */       XModel row = xm.get(i);
/*  29: 35 */       String uniqno = ((XModel)row.get("uniqno")).get().toString();
/*  30: 36 */       System.out.println("Uniqno::" + uniqno);
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean isPhysicianAvailable(String physician)
/*  35:    */   {
/*  36: 41 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  37: 42 */     String date = sdf.format(new Date());
/*  38:    */     
/*  39: 44 */     XModel xm = new XBaseModel();
/*  40: 45 */     TestXUIDB.getInstance().getData("physician_away", "*", "physician='" + physician + "' AND away_date='" + date + "'", xm);
/*  41:    */     
/*  42: 47 */     return xm.getNumChildren() <= 0;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static void createProcess(String[] args)
/*  46:    */     throws Exception
/*  47:    */   {
/*  48:    */     try
/*  49:    */     {
/*  50: 55 */       System.out.println("In CMEController.createProcess");
/*  51: 56 */       CMEController cmeController = new CMEController();
/*  52: 57 */       cmeController.createProcess();
/*  53:    */     }
/*  54:    */     catch (Exception e)
/*  55:    */     {
/*  56: 59 */       e.printStackTrace();
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public boolean imageExists(String uniqno, String domain)
/*  61:    */     throws SQLException
/*  62:    */   {
/*  63: 66 */     String absolutePath = TestXUIDB.getInstance().getImagePath();
/*  64: 67 */     System.out.println("imagePath::" + absolutePath);
/*  65: 68 */     System.out.println("imagePath1::" + absolutePath + uniqno + 
/*  66: 69 */       "_0_blank.png");
/*  67: 70 */     boolean imageExists = (new File(absolutePath + "/" + uniqno + "_0_blank.png").exists()) && 
/*  68: 71 */       (new File(absolutePath + "/" + uniqno + "_1_blank.png").exists()) && 
/*  69: 72 */       (new File(absolutePath + "/" + uniqno + "_cod.png").exists());
/*  70: 73 */     System.out.println("ImageExists:" + imageExists);
/*  71: 74 */     return imageExists;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public static void runProcessTransitions(String[] args)
/*  75:    */   {
/*  76:    */     try
/*  77:    */     {
/*  78: 79 */       XModel xm = new XBaseModel();
/*  79: 80 */       TestXUIDB.getInstance().getData("process", "*", "status!='Completed", xm);
/*  80: 81 */       for (int i = 0; i < xm.getNumChildren(); i++)
/*  81:    */       {
/*  82: 82 */         XModel row = xm.get(i);
/*  83: 83 */         String pid = ((XModel)row.get("pid")).get().toString();
/*  84: 84 */         Process.createProcess(pid);
/*  85: 85 */         Process.processTransitions();
/*  86:    */       }
/*  87:    */     }
/*  88:    */     catch (Exception e)
/*  89:    */     {
/*  90: 88 */       e.printStackTrace();
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static void sendOutboundMessages(String[] args)
/*  95:    */   {
/*  96:    */     try
/*  97:    */     {
/*  98: 94 */       System.out.println("In CMEController.sendOutboundMessages");
/*  99:    */       
/* 100:    */ 
/* 101:    */ 
/* 102:    */ 
/* 103:    */ 
/* 104:    */ 
/* 105:101 */       TestXUIDB.getInstance().sendOutboundDataPhysicians("default");
/* 106:    */       
/* 107:103 */       System.out.println("Done Send outboutnd logs");
/* 108:    */     }
/* 109:    */     catch (Exception e)
/* 110:    */     {
/* 111:117 */       e.printStackTrace();
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   public String createProcess()
/* 116:    */     throws Exception
/* 117:    */   {
/* 118:122 */     XModel xm = new XBaseModel();
/* 119:123 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 120:124 */     TestXUIDB.getInstance().getData("cme_records", "*", "status IS NULL or status='null'", xm);
/* 121:125 */     Vector v = new Vector();
/* 122:    */     
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:133 */     StringBuffer strBuf = new StringBuffer();
/* 130:135 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 131:    */     {
/* 132:136 */       XModel row = xm.get(i);
/* 133:137 */       Transformation transformation = new CMEImportTransformation();
/* 134:138 */       Transformation reportTransformation = new CMEReportTransformation();
/* 135:    */       
/* 136:    */ 
/* 137:141 */       Hashtable ht = new Hashtable();
/* 138:142 */       String uniqno = ((XModel)row.get("uniqno")).get().toString();
/* 139:143 */       String value1 = ((XModel)row.get("value1")).get().toString();
/* 140:    */       
/* 141:145 */       ht.put("uniqno", uniqno);
/* 142:146 */       ht.put("value1", value1);
/* 143:    */       
/* 144:148 */       CMEImportTransformationFilter filter = new CMEImportTransformationFilter();
/* 145:150 */       if (!filter.hasValidLanguage(uniqno, value1))
/* 146:    */       {
/* 147:153 */         jt.update("UPDATE cme_records SET status='ERROR INVALID LANGUAGE',comments='Invalid Language For State' WHERE uniqno=? ", new Object[] { uniqno });
/* 148:    */       }
/* 149:    */       else
/* 150:    */       {
/* 151:157 */         transformation.transform(ht, "uniqno", ht.get("uniqno").toString(), null, "adult");
/* 152:158 */         reportTransformation.transform(ht, "uniqno", ht.get("uniqno").toString(), null, null);
/* 153:    */         
/* 154:    */ 
/* 155:    */ 
/* 156:162 */         StringBuffer errBuf = new StringBuffer();
/* 157:163 */         System.out.println("Starting checkRecord");
/* 158:164 */         if (!checkRecord(uniqno, errBuf))
/* 159:    */         {
/* 160:165 */           row.set("status", "ERROR");
/* 161:166 */           row.set("comments", errBuf.toString());
/* 162:167 */           TestXUIDB.getInstance().saveDataM2("cme_records", "uniqno='" + uniqno + "'", row);
/* 163:    */         }
/* 164:    */         else
/* 165:    */         {
/* 166:170 */           System.out.println("Starting imageExists");
/* 167:171 */           if (!imageExists(uniqno, "adult"))
/* 168:    */           {
/* 169:172 */             row.set("status", "null");
/* 170:173 */             row.set("comments", "Image Not Present");
/* 171:174 */             TestXUIDB.getInstance().saveDataM2("cme_records", "uniqno='" + uniqno + "'", row);
/* 172:    */           }
/* 173:    */           else
/* 174:    */           {
/* 175:177 */             System.out.println("Creating Process");
/* 176:    */             try
/* 177:    */             {
/* 178:180 */               System.out.println("Processing: " + uniqno);
/* 179:181 */               Process p = Process.createProcess(uniqno, "com.kentropy.process.CMEStateMachine");
/* 180:182 */               System.out.println("After createProcess: " + uniqno);
/* 181:    */               
/* 182:184 */               System.out.println("After processTransitions: " + uniqno);
/* 183:185 */               row.set("status", "Process created");
/* 184:186 */               v.add(uniqno);
/* 185:187 */               TestXUIDB.getInstance().saveDataM2("cme_records", "uniqno='" + uniqno + "'", row);
/* 186:188 */               System.out.println("After saveDataM2: " + uniqno);
/* 187:    */             }
/* 188:    */             catch (Exception e)
/* 189:    */             {
/* 190:190 */               e.printStackTrace();
/* 191:    */             }
/* 192:    */           }
/* 193:    */         }
/* 194:    */       }
/* 195:    */     }
/* 196:195 */     Process.processTransitions();
/* 197:196 */     strBuf.append("Assigned: " + v.size() + " records<br />");
/* 198:198 */     for (int i = 0; i < v.size(); i++) {
/* 199:199 */       strBuf.append("Processed:" + v.get(i) + "<br />");
/* 200:    */     }
/* 201:202 */     return strBuf.toString();
/* 202:    */   }
/* 203:    */   
/* 204:    */   public static void assignTrainingRecords(String[] args)
/* 205:    */   {
/* 206:    */     try
/* 207:    */     {
/* 208:207 */       XModel xm = new XBaseModel();
/* 209:208 */       TestXUIDB.getInstance().getData("adult", "uniqno", null, xm);
/* 210:209 */       for (int i = 0; i < xm.getNumChildren(); i++)
/* 211:    */       {
/* 212:210 */         XModel row = xm.get(i);
/* 213:211 */         String uniqno = ((XModel)row.get("uniqno")).get().toString();
/* 214:212 */         Process process = Process.createProcess("2012-05/" + uniqno, "com.kentropy.process.CMEStateMachine2");
/* 215:213 */         Process.processTransitions();
/* 216:    */       }
/* 217:    */     }
/* 218:    */     catch (Exception e)
/* 219:    */     {
/* 220:216 */       e.printStackTrace();
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   public boolean checkRecord(String uniqno, StringBuffer strBuf)
/* 225:    */   {
/* 226:222 */     String keyvaluePath = "/va/" + uniqno + "/";
/* 227:223 */     String domain = TestXUIDB.getInstance().getValue("keyvalue", keyvaluePath + "type");
/* 228:224 */     if (domain == null)
/* 229:    */     {
/* 230:225 */       System.out.println("QA ERROR: Path '" + keyvaluePath + "type' not found");
/* 231:226 */       return false;
/* 232:    */     }
/* 233:228 */     System.out.println("CME QA Domain: " + domain);
/* 234:    */     
/* 235:230 */     XModel dataModel = new XBaseModel();
/* 236:231 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", keyvaluePath);
/* 237:    */     
/* 238:233 */     XModel mappingModel = new XBaseModel();
/* 239:234 */     TestXUIDB.getInstance().getData("cme_mapping", "*", "domain='" + domain + "'", mappingModel);
/* 240:235 */     for (int i = 0; i < mappingModel.getNumChildren(); i++)
/* 241:    */     {
/* 242:236 */       XModel row = mappingModel.get(i);
/* 243:237 */       String path = (String)((XModel)row.get("path")).get();
/* 244:238 */       String required = (String)((XModel)row.get("required")).get();
/* 245:239 */       if (required.equals("yes"))
/* 246:    */       {
/* 247:241 */         String value = TestXUIDB.getInstance().getValue("keyvalue", keyvaluePath + path);
/* 248:242 */         if ((value == null) || (value.trim().equals("")))
/* 249:    */         {
/* 250:243 */           System.out.println("CME QA ERROR: Path '" + keyvaluePath + path + "' not found");
/* 251:244 */           strBuf.append("Missing Path: \"" + keyvaluePath + path + "\"");
/* 252:245 */           return false;
/* 253:    */         }
/* 254:247 */         System.out.println("CME QA INFO: Path '" + keyvaluePath + path + "' found");
/* 255:    */       }
/* 256:    */     }
/* 257:250 */     System.out.println("CME QA: OK");
/* 258:251 */     return true;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public static void main(String[] args)
/* 262:    */     throws Exception
/* 263:    */   {}
/* 264:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-controller\ken-cme-controller.jar
 * Qualified Name:     com.kentropy.cme.CMEController
 * JD-Core Version:    0.7.0.1
 */