/*   1:    */ package com.kentropy.cme.transformation;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.qa.transformation.Transformation;
/*   4:    */ import com.kentropy.db.TestXUIDB;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.text.SimpleDateFormat;
/*   8:    */ import java.util.Date;
/*   9:    */ import java.util.Hashtable;
/*  10:    */ import net.xoetrope.xui.data.XBaseModel;
/*  11:    */ import net.xoetrope.xui.data.XModel;
/*  12:    */ 
/*  13:    */ public class CompletionTransformation
/*  14:    */   implements Transformation
/*  15:    */ {
/*  16:    */   public Object transform(ResultSet rs, String name, String value, StringBuffer errorMsg, String domain)
/*  17:    */     throws Exception
/*  18:    */   {
/*  19: 19 */     return null;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Object transform(Hashtable rs, String name, String value, StringBuffer errorMsg, String domain)
/*  23:    */   {
/*  24: 24 */     String phys1 = (String)rs.get("phys1");
/*  25: 25 */     String phys2 = (String)rs.get("phys2");
/*  26: 26 */     String adjudicator = (String)rs.get("adjudicator");
/*  27: 27 */     String uniqno = (String)rs.get("uniqno");
/*  28: 28 */     String cancelled = CMETransformation.getCancellation(phys1, phys2, uniqno);
/*  29:    */     
/*  30: 30 */     String role = "coder";
/*  31: 31 */     if (name.equals("adjudicator")) {
/*  32: 32 */       role = "adjudicator";
/*  33:    */     }
/*  34:    */     try
/*  35:    */     {
/*  36: 36 */       if (!cancelled.equals("true")) {
/*  37: 39 */         updateCODReport1(value, role, uniqno);
/*  38:    */       }
/*  39:    */     }
/*  40:    */     catch (Exception e)
/*  41:    */     {
/*  42: 42 */       e.printStackTrace();
/*  43:    */     }
/*  44: 45 */     return null;
/*  45:    */   }
/*  46:    */   
/*  47:    */   private void updateCancellationReport(String phys1, String phys2, String uniqno)
/*  48:    */     throws Exception
/*  49:    */   {
/*  50: 51 */     String path = "/cme/" + uniqno;
/*  51:    */     
/*  52: 53 */     String reason1Path = path + "/Coding/" + phys1 + "/cancellation/reason";
/*  53: 54 */     String comment1Path = path + "/Coding/" + phys1 + "/cancellation/comment";
/*  54: 55 */     String reason2Path = path + "/Coding/" + phys2 + "/cancellation/reason";
/*  55: 56 */     String comment2Path = path + "/Coding/" + phys2 + "/cancellation/comment";
/*  56: 57 */     System.out.println("reason1Path" + reason1Path);
/*  57:    */     
/*  58: 59 */     String reason1 = TestXUIDB.getInstance().getValue("keyvalue", reason1Path);
/*  59: 60 */     String comment1 = TestXUIDB.getInstance().getValue("keyvalue", comment1Path);
/*  60: 61 */     String reason2 = TestXUIDB.getInstance().getValue("keyvalue", reason2Path);
/*  61: 62 */     String comment2 = TestXUIDB.getInstance().getValue("keyvalue", comment2Path);
/*  62:    */     
/*  63: 64 */     XModel cancellationModel = new XBaseModel();
/*  64: 65 */     cancellationModel.setId(uniqno);
/*  65: 66 */     cancellationModel.set("uniqno", uniqno);
/*  66: 67 */     cancellationModel.set("physician1", phys1);
/*  67: 68 */     cancellationModel.set("physician2", phys2);
/*  68: 69 */     cancellationModel.set("reason1", reason1);
/*  69: 70 */     cancellationModel.set("comments1", comment1);
/*  70: 71 */     cancellationModel.set("reason2", reason2);
/*  71: 72 */     cancellationModel.set("comments2", comment2);
/*  72:    */     
/*  73: 74 */     TestXUIDB.getInstance().saveDataM2("cancellation", "uniqno='" + uniqno + "' and physician1='" + phys1 + "' and physician2='" + phys2 + "'", cancellationModel);
/*  74:    */   }
/*  75:    */   
/*  76:    */   private void updateCancellationReport1(String phys1, String role, String uniqno)
/*  77:    */     throws Exception
/*  78:    */   {
/*  79: 81 */     String path = "/cme/" + uniqno;
/*  80:    */     
/*  81: 83 */     String reason1Path = path + "/Coding/" + phys1 + "/cancellation/reason";
/*  82: 84 */     String comment1Path = path + "/Coding/" + phys1 + "/cancellation/comment";
/*  83: 85 */     System.out.println("reason1Path" + reason1Path);
/*  84:    */     
/*  85: 87 */     String reason1 = TestXUIDB.getInstance().getValue("keyvalue", reason1Path);
/*  86: 88 */     String comment1 = TestXUIDB.getInstance().getValue("keyvalue", comment1Path);
/*  87: 90 */     if ((reason1 != null) || (comment1 != null))
/*  88:    */     {
/*  89: 91 */       XModel cancellationModel = new XBaseModel();
/*  90: 92 */       cancellationModel.setId(uniqno);
/*  91: 93 */       cancellationModel.set("uniqno", uniqno);
/*  92: 94 */       cancellationModel.set("physician1", phys1);
/*  93: 95 */       cancellationModel.set("reason1", reason1);
/*  94: 96 */       cancellationModel.set("comments1", comment1);
/*  95: 97 */       cancellationModel.set("role", role);
/*  96:    */       
/*  97: 99 */       TestXUIDB.getInstance().saveDataM2("cancellation", "uniqno='" + uniqno + "' and physician1='" + phys1 + "'", cancellationModel);
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void updateCODReport(String phys1, String phys2, String adjudicator, String report)
/* 102:    */     throws Exception
/* 103:    */   {
/* 104:136 */     System.out.println("PHYS1:" + phys1);
/* 105:137 */     System.out.println("PHYS2:" + phys2);
/* 106:138 */     System.out.println("ADJUDICATOR:" + adjudicator);
/* 107:139 */     System.out.println("REPORT:" + report);
/* 108:140 */     String finalIcd = "";
/* 109:141 */     XModel xm = new XBaseModel();
/* 110:142 */     String path = "/cme/" + report;
/* 111:    */     
/* 112:144 */     String qualityPath1 = path + "/Coding/" + phys1 + "/quality";
/* 113:145 */     String qualityPath2 = path + "/Coding/" + phys2 + "/quality";
/* 114:146 */     String certaintyPath1 = path + "/Coding/" + phys1 + "/certainity";
/* 115:147 */     String certaintyPath2 = path + "/Coding/" + phys2 + "/certainity";
/* 116:    */     
/* 117:149 */     String codPath1 = path + "/Coding/" + phys1 + "/icd";
/* 118:150 */     String reconPath1 = path + "/Reconciliation/" + phys1 + "/icd";
/* 119:151 */     String codPath2 = path + "/Coding/" + phys2 + "/icd";
/* 120:152 */     String reconPath2 = path + "/Reconciliation/" + phys2 + "/icd";
/* 121:153 */     String adjPath = path + "/Adjudication/" + adjudicator + "/icd";
/* 122:    */     
/* 123:155 */     String keywordPath1 = path + "/Coding/Comments/" + phys1;
/* 124:156 */     String keywordPath2 = path + "/Coding/Comments/" + phys2;
/* 125:157 */     String keywordPath3 = path + "/Coding/Comments/" + adjudicator;
/* 126:158 */     System.out.println("KEYWORDPATH3" + keywordPath3);
/* 127:    */     
/* 128:160 */     String cod1 = TestXUIDB.getInstance().getValue("keyvalue", codPath1);
/* 129:161 */     String recon1 = TestXUIDB.getInstance().getValue("keyvalue", reconPath1);
/* 130:162 */     String cod2 = TestXUIDB.getInstance().getValue("keyvalue", codPath2);
/* 131:163 */     String recon2 = TestXUIDB.getInstance().getValue("keyvalue", reconPath2);
/* 132:164 */     String adj = TestXUIDB.getInstance().getValue("keyvalue", adjPath);
/* 133:    */     
/* 134:166 */     String quality1 = TestXUIDB.getInstance().getValue("keyvalue", qualityPath1);
/* 135:167 */     String quality2 = TestXUIDB.getInstance().getValue("keyvalue", qualityPath2);
/* 136:168 */     String certainty1 = TestXUIDB.getInstance().getValue("keyvalue", certaintyPath1);
/* 137:169 */     String certainty2 = TestXUIDB.getInstance().getValue("keyvalue", certaintyPath2);
/* 138:    */     
/* 139:171 */     StringBuffer codingKeyword1 = new StringBuffer();
/* 140:172 */     StringBuffer codingKeyword2 = new StringBuffer();
/* 141:173 */     StringBuffer reconKeyword1 = new StringBuffer();
/* 142:174 */     StringBuffer reconKeyword2 = new StringBuffer();
/* 143:175 */     StringBuffer adjudicationKeyword = new StringBuffer();
/* 144:    */     
/* 145:177 */     getKeywords(codingKeyword1, keywordPath1, "coding");
/* 146:178 */     getKeywords(reconKeyword1, keywordPath1, "reconciliation");
/* 147:179 */     getKeywords(codingKeyword2, keywordPath2, "coding");
/* 148:180 */     getKeywords(reconKeyword2, keywordPath2, "reconciliation");
/* 149:181 */     getKeywords(adjudicationKeyword, keywordPath3, "adjudication");
/* 150:    */     
/* 151:183 */     XModel dataM = new XBaseModel();
/* 152:184 */     dataM.setId(report);
/* 153:185 */     ((XModel)dataM.get("physician1")).set(phys1);
/* 154:186 */     ((XModel)dataM.get("physician2")).set(phys2);
/* 155:187 */     if ((adjudicator != null) && (!adjudicator.trim().equals(""))) {
/* 156:188 */       ((XModel)dataM.get("adjudicator")).set(adjudicator);
/* 157:    */     }
/* 158:190 */     ((XModel)dataM.get("coding_icd1")).set(cod1);
/* 159:191 */     ((XModel)dataM.get("coding_icd2")).set(cod2);
/* 160:192 */     finalIcd = cod2;
/* 161:193 */     if ((recon1 != null) && (!recon1.trim().equals(""))) {
/* 162:194 */       ((XModel)dataM.get("reconciliation_icd1")).set(recon1);
/* 163:    */     }
/* 164:196 */     if ((recon2 != null) && (!recon2.trim().equals("")))
/* 165:    */     {
/* 166:197 */       ((XModel)dataM.get("reconciliation_icd2")).set(recon2);
/* 167:198 */       finalIcd = recon2;
/* 168:    */     }
/* 169:200 */     if ((adj != null) && (!adj.trim().equals("")))
/* 170:    */     {
/* 171:201 */       ((XModel)dataM.get("adjudicator_icd")).set(adj);
/* 172:202 */       finalIcd = adj;
/* 173:    */     }
/* 174:204 */     ((XModel)dataM.get("uniqno")).set(report);
/* 175:    */     
/* 176:    */ 
/* 177:207 */     ((XModel)dataM.get("coding_keyword1")).set(codingKeyword1);
/* 178:208 */     ((XModel)dataM.get("coding_keyword2")).set(codingKeyword2);
/* 179:209 */     ((XModel)dataM.get("reconciliation_keyword1")).set(reconKeyword1);
/* 180:210 */     ((XModel)dataM.get("reconciliation_keyword2")).set(reconKeyword2);
/* 181:211 */     ((XModel)dataM.get("adjudicator_keyword")).set(adjudicationKeyword);
/* 182:    */     
/* 183:213 */     ((XModel)dataM.get("quality1")).set(quality1);
/* 184:214 */     ((XModel)dataM.get("certainty1")).set(certainty1);
/* 185:215 */     ((XModel)dataM.get("quality2")).set(quality2);
/* 186:216 */     ((XModel)dataM.get("certainty2")).set(certainty2);
/* 187:    */     
/* 188:    */ 
/* 189:219 */     TestXUIDB.getInstance().saveDataM2("cme_report", "uniqno='" + report + "' and physician1='" + phys1 + "' and physician2='" + phys2 + "'", dataM);
/* 190:    */   }
/* 191:    */   
/* 192:    */   private void getKeywords(StringBuffer keywordBuffer, String keywordPath, String stage)
/* 193:    */   {
/* 194:231 */     XModel keywordModel = new XBaseModel();
/* 195:232 */     TestXUIDB.getInstance().getKeyValues(keywordModel, "keyvalue", keywordPath);
/* 196:233 */     for (int i = 0; i < keywordModel.getNumChildren(); i++)
/* 197:    */     {
/* 198:234 */       XModel xm = keywordModel.get(i);
/* 199:235 */       String keywordStage = xm.getValueAsString("stage");
/* 200:236 */       String text = xm.getValueAsString("text");
/* 201:237 */       System.out.println("stage:" + keywordStage);
/* 202:238 */       if ((keywordStage != null) && (keywordStage.toLowerCase().equals(stage))) {
/* 203:239 */         keywordBuffer.append(text + ",\n");
/* 204:    */       }
/* 205:    */     }
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void updateCODReport1(String phys1, String role, String report)
/* 209:    */     throws Exception
/* 210:    */   {
/* 211:246 */     if ((phys1 == null) || (phys1.trim().equals(""))) {
/* 212:247 */       return;
/* 213:    */     }
/* 214:249 */     System.out.println("PHYS1:" + phys1);
/* 215:250 */     System.out.println("REPORT:" + report);
/* 216:251 */     XModel xm = new XBaseModel();
/* 217:252 */     String path = "/cme/" + report;
/* 218:    */     
/* 219:254 */     String qualityPath1 = path + "/Coding/" + phys1 + "/quality";
/* 220:255 */     String certaintyPath1 = path + "/Coding/" + phys1 + "/certainity";
/* 221:    */     
/* 222:257 */     String codPath1 = path + "/Coding/" + phys1 + "/icd";
/* 223:258 */     if ((role != null) && (role.equals("adjudicator"))) {
/* 224:259 */       codPath1 = path + "/Adjudication/" + phys1 + "/icd";
/* 225:    */     }
/* 226:261 */     String reconPath1 = path + "/Reconciliation/" + phys1 + "/icd";
/* 227:    */     
/* 228:263 */     String keywordPath1 = path + "/Coding/Comments/" + phys1;
/* 229:264 */     System.out.println("KeywordPath1::" + keywordPath1);
/* 230:    */     
/* 231:266 */     String cod1 = TestXUIDB.getInstance().getValue("keyvalue", codPath1);
/* 232:267 */     String recon1 = TestXUIDB.getInstance().getValue("keyvalue", reconPath1);
/* 233:    */     
/* 234:269 */     String quality1 = TestXUIDB.getInstance().getValue("keyvalue", qualityPath1);
/* 235:270 */     String certainty1 = TestXUIDB.getInstance().getValue("keyvalue", certaintyPath1);
/* 236:    */     
/* 237:272 */     StringBuffer codingKeyword1 = new StringBuffer();
/* 238:273 */     StringBuffer codingKeyword2 = new StringBuffer();
/* 239:274 */     StringBuffer reconKeyword1 = new StringBuffer();
/* 240:275 */     StringBuffer adjudicationKeyword = new StringBuffer();
/* 241:277 */     if (role.equals("adjudicator")) {
/* 242:278 */       getKeywords(codingKeyword1, keywordPath1, "adjudication");
/* 243:    */     } else {
/* 244:280 */       getKeywords(codingKeyword1, keywordPath1, "coding");
/* 245:    */     }
/* 246:282 */     getKeywords(reconKeyword1, keywordPath1, "reconciliation");
/* 247:    */     
/* 248:284 */     String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
/* 249:    */     
/* 250:286 */     XModel dataM = new XBaseModel();
/* 251:287 */     dataM.setId(report);
/* 252:288 */     ((XModel)dataM.get("uniqno")).set(report);
/* 253:289 */     ((XModel)dataM.get("physician")).set(phys1);
/* 254:290 */     ((XModel)dataM.get("role")).set(role);
/* 255:291 */     ((XModel)dataM.get("icd")).set(cod1);
/* 256:292 */     ((XModel)dataM.get("quality")).set(quality1);
/* 257:293 */     ((XModel)dataM.get("certainty")).set(certainty1);
/* 258:294 */     ((XModel)dataM.get("keywords")).set(codingKeyword1.toString());
/* 259:295 */     if ((recon1 != null) && (!recon1.trim().equals(""))) {
/* 260:296 */       ((XModel)dataM.get("reconciliation_icd")).set(recon1);
/* 261:    */     }
/* 262:298 */     ((XModel)dataM.get("reconciliation_keyword")).set(reconKeyword1.toString());
/* 263:299 */     ((XModel)dataM.get("time1")).set(time);
/* 264:    */     
/* 265:    */ 
/* 266:    */ 
/* 267:    */ 
/* 268:304 */     String fromBookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 269:    */     
/* 270:306 */     TestXUIDB.getInstance().saveDataM1("cme_report", "uniqno='" + report + "' and physician='" + phys1 + "'", dataM);
/* 271:307 */     String toBookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 272:308 */     TestXUIDB.getInstance().addToChangeLogOutboundQueue("cmebilling", fromBookmark, toBookmark);
/* 273:309 */     System.out.println("FromBookmark:" + fromBookmark);
/* 274:310 */     System.out.println("ToBookmark:" + toBookmark);
/* 275:    */   }
/* 276:    */   
/* 277:    */   public static void main(String[] args)
/* 278:    */     throws Exception
/* 279:    */   {
/* 280:319 */     XModel xm = new XBaseModel();
/* 281:320 */     TestXUIDB.getInstance()
/* 282:321 */       .getData("transformations", "*", "table1='completion'", xm);
/* 283:    */     
/* 284:323 */     Hashtable ht = new Hashtable();
/* 285:324 */     ht.put("uniqno", "14300107_01_01");
/* 286:    */     
/* 287:326 */     ht.put("phys1", "20");
/* 288:327 */     ht.put("phys2", "21");
/* 289:328 */     ht.put("adjudicator", "25");
/* 290:329 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 291:    */     {
/* 292:330 */       XModel row = xm.get(i);
/* 293:331 */       String field = ((XModel)row.get("field1")).get().toString();
/* 294:332 */       String transformationClass = ((XModel)row.get("transformation_class")).get().toString();
/* 295:333 */       System.out.println("Transformation::" + ((XModel)row.get("transformation_class")).get());
/* 296:334 */       Transformation transformation = 
/* 297:    */       
/* 298:336 */         (Transformation)Class.forName(transformationClass).newInstance();
/* 299:    */       
/* 300:338 */       String str1 = (String)transformation.transform(ht, field, (String)ht.get(field), null, 
/* 301:339 */         "keyvalue");
/* 302:    */     }
/* 303:    */   }
/* 304:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa-transformations\ken-qa-transformations.jar
 * Qualified Name:     com.kentropy.cme.transformation.CompletionTransformation
 * JD-Core Version:    0.7.0.1
 */