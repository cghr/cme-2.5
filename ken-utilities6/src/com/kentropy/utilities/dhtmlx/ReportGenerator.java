/*   1:    */ package com.kentropy.utilities.dhtmlx;
/*   2:    */ 
/*   3:    */ import com.kentropy.tree.DBNode;
/*   4:    */ import com.kentropy.tree.DTMLTreeVisitor;
/*   5:    */ import com.kentropy.tree.Node;
/*   6:    */ import com.kentropy.util.SpringApplicationContext;
/*   7:    */ import com.kentropy.util.SpringUtils;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.sql.ResultSet;
/*  10:    */ import java.sql.SQLException;
/*  11:    */ import java.text.MessageFormat;
/*  12:    */ import java.util.Arrays;
/*  13:    */ import java.util.Hashtable;
/*  14:    */ import java.util.List;
/*  15:    */ import java.util.Vector;
/*  16:    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*  17:    */ import org.springframework.dao.DataAccessException;
/*  18:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  19:    */ import org.springframework.jdbc.core.ResultSetExtractor;
/*  20:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  21:    */ 
/*  22:    */ public class ReportGenerator
/*  23:    */ {
/*  24: 26 */   private int numFacts = 0;
/*  25:    */   private int numDims;
/*  26:    */   
/*  27:    */   public Vector<Vector<String>> getColVals(String table, String[] colDims, String where)
/*  28:    */   {
/*  29: 35 */     Vector<Vector<String>> v1 = new Vector();
/*  30: 36 */     for (int i = 0; i < colDims.length; i++)
/*  31:    */     {
/*  32: 40 */       String sql = "select distinct " + colDims[i] + " from " + table + " where " + where;
/*  33:    */       
/*  34: 42 */       JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  35: 43 */       SqlRowSet rs = jt.queryForRowSet(sql);
/*  36: 44 */       Vector<String> v = new Vector();
/*  37: 45 */       while (rs.next()) {
/*  38: 47 */         v.add(rs.getString(1));
/*  39:    */       }
/*  40: 49 */       v1.add(v);
/*  41:    */     }
/*  42: 51 */     return v1;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void getDHTMLConfig(int reportId, String[] params, Hashtable<String, String> ht)
/*  46:    */   {
/*  47: 61 */     getReportDefinition(reportId, ht);
/*  48:    */     
/*  49:    */ 
/*  50:    */ 
/*  51: 65 */     String table = (String)ht.get("table");
/*  52: 66 */     String headers = (String)ht.get("headings");
/*  53:    */     
/*  54: 68 */     String colDim = (String)ht.get("col_dim");
/*  55: 69 */     String aggregations = (String)ht.get("aggregations");
/*  56: 70 */     String dimFields = (String)ht.get("dim_fields");
/*  57: 71 */     String factFields = (String)ht.get("fact_fields");
/*  58: 72 */     String where = (String)ht.get("where");
/*  59:    */     
/*  60:    */ 
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64: 78 */     String initWidths = "300,100";
/*  65: 79 */     String colAlign = "left,right";
/*  66: 80 */     String colTypes = "tree,ro";
/*  67: 81 */     String sorting = "str,str";
/*  68: 82 */     String images = "folder.gif,leaf.gif";
/*  69: 83 */     MessageFormat mf = new MessageFormat(where);
/*  70: 84 */     String where1 = mf.format(params);
/*  71:    */     
/*  72:    */ 
/*  73:    */ 
/*  74: 88 */     String[] colDims1 = ("all1," + colDim).split(",");
/*  75: 89 */     String[] factIdxs = (String[])null;
/*  76: 90 */     String fff = "";
/*  77:    */     
/*  78: 92 */     String agg = "";
/*  79: 93 */     Vector<StringBuffer> h2 = null;
/*  80: 96 */     if ((colDim != null) && (!colDim.trim().equals("")))
/*  81:    */     {
/*  82: 99 */       factIdxs = ((String)ht.get("fact_idx")).split(",");
/*  83:    */       
/*  84:    */ 
/*  85:    */ 
/*  86:    */ 
/*  87:    */ 
/*  88:    */ 
/*  89:    */ 
/*  90:    */ 
/*  91:108 */       this.numDims = dimFields.split(",").length;
/*  92:109 */       String table1 = table + " , (select 'ALL' all1) b";
/*  93:110 */       DBNode d1 = new DBNode(DBNode.getDimTree(colDims1, table1, where1), "ALL", null);
/*  94:111 */       d1.distinct = true;
/*  95:112 */       d1.construct();
/*  96:113 */       StringBuffer sb = new StringBuffer();
/*  97:114 */       d1.printTree1(sb);
/*  98:115 */       System.out.println(sb);
/*  99:116 */       String[] test = sb.toString().split("\r\n");
/* 100:117 */       String[] factFields1 = factFields.split(",");
/* 101:118 */       String[] h1 = headers.split(",");
/* 102:119 */       DTMLTreeVisitor[] dtms = new DTMLTreeVisitor[factFields1.length];
/* 103:    */       String[] t;
/* 104:121 */       for (int k = 0; k < factFields1.length; k++)
/* 105:    */       {
/* 106:123 */         for (int i = 0; i < test.length; i++)
/* 107:    */         {
/* 108:125 */           String agg1 = aggregations.split(",")[0];
/* 109:126 */           t = test[i].split("and");
/* 110:127 */           String name1 = "";
/* 111:129 */           for (int j = 0; j < t.length; j++) {
/* 112:131 */             if (!t[j].trim().equals(""))
/* 113:    */             {
/* 114:133 */               initWidths = initWidths + ",50";
/* 115:134 */               colAlign = colAlign + ",right";
/* 116:135 */               colTypes = colTypes + ",ro";
/* 117:136 */               sorting = sorting + ",int";
/* 118:137 */               String[] t1 = t[j].split("=");
/* 119:138 */               name1 = name1 + t1[1];
/* 120:139 */               agg = agg + "," + agg1;
/* 121:140 */               this.numFacts += 1;
/* 122:    */             }
/* 123:    */           }
/* 124:    */         }
/* 125:150 */         dtms[k] = new DTMLTreeVisitor(h1[(h1.length - factFields1.length + k)]);
/* 126:151 */         d1.visit1(dtms[k]);
/* 127:152 */         if (k == 0)
/* 128:    */         {
/* 129:153 */           h2 = dtms[k].header;
/* 130:    */         }
/* 131:    */         else
/* 132:    */         {
/* 133:157 */           int count2 = 0;
/* 134:158 */           for (StringBuffer str : dtms[k].header)
/* 135:    */           {
/* 136:160 */             ((StringBuffer)h2.get(count2)).append("," + str);
/* 137:161 */             count2++;
/* 138:    */           }
/* 139:    */         }
/* 140:    */       }
/* 141:172 */       String factHeaders = "[";
/* 142:173 */       int count1 = 0;
/* 143:175 */       for (StringBuffer str : h2)
/* 144:    */       {
/* 145:178 */         factHeaders = factHeaders + (count1 == 0 ? "" : ",") + "'" + str + "'";
/* 146:179 */         count1++;
/* 147:    */       }
/* 148:184 */       factHeaders = factHeaders + "]";
/* 149:185 */       ht.put("factHeaders", factHeaders);
/* 150:186 */       aggregations = aggregations + agg;
/* 151:    */     }
/* 152:    */     else
/* 153:    */     {
/* 154:191 */       String[] h1 = headers.split(",");
/* 155:192 */       this.numDims = dimFields.split(",").length;
/* 156:193 */       this.numFacts = factFields.split(",").length;
/* 157:194 */       String factHeaders = "['";
/* 158:195 */       int count1 = 0;
/* 159:196 */       String head1 = "";
/* 160:197 */       for (int i = 0; i < this.numFacts; i++)
/* 161:    */       {
/* 162:199 */         if (i == 0) {
/* 163:200 */           head1 = head1 + h1[(this.numDims + i)];
/* 164:    */         } else {
/* 165:202 */           head1 = head1 + "," + h1[(this.numDims + i)];
/* 166:    */         }
/* 167:203 */         count1++;
/* 168:    */       }
/* 169:206 */       factHeaders = factHeaders + head1 + "']";
/* 170:207 */       ht.put("factHeaders", factHeaders);
/* 171:    */     }
/* 172:215 */     ht.put("headers", headers);
/* 173:216 */     ht.put("fact_fields", factFields + fff);
/* 174:217 */     ht.put("init_widths", initWidths);
/* 175:218 */     ht.put("col_align", colAlign);
/* 176:219 */     ht.put("col_types", colTypes);
/* 177:220 */     ht.put("sorting", sorting);
/* 178:    */     
/* 179:    */ 
/* 180:223 */     List<String> headers1 = Arrays.asList(headers.split(","));
/* 181:224 */     List<String> images1 = Arrays.asList(images.split(","));
/* 182:225 */     List<String> aggregations1 = Arrays.asList(aggregations.split(","));
/* 183:    */     
/* 184:227 */     Vector<String> headers2 = new Vector();
/* 185:228 */     headers2.addAll(headers1);
/* 186:229 */     Vector<String> images2 = new Vector();
/* 187:230 */     images2.addAll(images1);
/* 188:231 */     Vector<String> aggregations2 = new Vector();
/* 189:232 */     aggregations2.addAll(aggregations1);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public Vector<Vector<String>> queryFacts(String table, String colDim, String where1, String dimFields, String factFields, String aggregations, Vector<String> headers, Vector<String> aggr)
/* 193:    */   {
/* 194:238 */     String[] colDims = colDim.split(",");
/* 195:239 */     String[] colDims1 = ("all1," + colDim).split(",");
/* 196:    */     
/* 197:    */ 
/* 198:    */ 
/* 199:    */ 
/* 200:244 */     System.out.println(this.numDims + "--" + this.numFacts);
/* 201:    */     
/* 202:    */ 
/* 203:247 */     String table1 = table + " , (select 'ALL' all1) b";
/* 204:    */     
/* 205:    */ 
/* 206:    */ 
/* 207:251 */     DBNode d1 = new DBNode(DBNode.getDimTree(colDims1, table1, where1), "ALL", null);
/* 208:252 */     d1.distinct = true;
/* 209:253 */     d1.construct();
/* 210:254 */     StringBuffer sb = new StringBuffer();
/* 211:255 */     d1.printTree1(sb);
/* 212:256 */     System.out.println(sb);
/* 213:257 */     String[] test = sb.toString().split("\r\n");
/* 214:    */     
/* 215:259 */     String[] factFields1 = factFields.split(",");
/* 216:260 */     String fff = "";
/* 217:261 */     String hhh = "";
/* 218:262 */     String agg = "";
/* 219:264 */     for (int j = 0; j < factFields1.length; j++)
/* 220:    */     {
/* 221:266 */       String agg1 = aggregations.split(",")[j];
/* 222:267 */       fff = fff + "," + factFields1[j];
/* 223:268 */       for (int i = 0; i < test.length; i++)
/* 224:    */       {
/* 225:270 */         String sep = (j == 0) && (i == 0) ? "" : ",";
/* 226:    */         
/* 227:    */ 
/* 228:    */ 
/* 229:274 */         String qry = "if(" + test[i] + "," + factFields1[j] + ",0) ' test" + (i + j * test.length) + "'";
/* 230:    */         
/* 231:276 */         fff = fff + "," + qry;
/* 232:277 */         hhh = hhh + sep + "test" + (i + j * test.length);
/* 233:278 */         agg = agg + sep + agg1;
/* 234:279 */         this.numFacts += 1;
/* 235:280 */         System.out.println("hhh: " + hhh);
/* 236:    */       }
/* 237:    */     }
/* 238:285 */     List<String> aggregations1 = Arrays.asList(agg.split(","));
/* 239:286 */     aggr.addAll(aggregations1);
/* 240:    */     
/* 241:288 */     List<String> headers1 = Arrays.asList(hhh.split(","));
/* 242:289 */     headers.addAll(headers1);
/* 243:    */     
/* 244:291 */     System.out.println(headers);
/* 245:    */     
/* 246:    */ 
/* 247:    */ 
/* 248:    */ 
/* 249:    */ 
/* 250:297 */     System.out.println("after Where " + where1);
/* 251:    */     
/* 252:299 */     String sql = "select " + dimFields + fff + " from " + table + " where " + where1 + " group by  " + dimFields;
/* 253:300 */     System.out.println("sql:" + sql);
/* 254:301 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 255:    */     
/* 256:    */ 
/* 257:    */ 
/* 258:305 */     Vector<Vector<String>> values = new Vector();
/* 259:306 */     values = (Vector)jt.query(sql, new ResultSetExtractor()
/* 260:    */     {
/* 261:308 */       int numDims1 = ReportGenerator.this.numDims;
/* 262:309 */       int numFacts1 = ReportGenerator.this.numFacts;
/* 263:    */       
/* 264:    */       public Vector<Vector<String>> extractData(ResultSet rs)
/* 265:    */         throws SQLException, DataAccessException
/* 266:    */       {
/* 267:314 */         Vector<Vector<String>> values = new Vector();
/* 268:315 */         while (rs.next())
/* 269:    */         {
/* 270:317 */           System.out.println(this.numDims1 + "--" + this.numFacts1);
/* 271:318 */           Vector<String> row1 = new Vector();
/* 272:319 */           for (int i = 0; i < this.numDims1 + this.numFacts1; i++) {
/* 273:321 */             row1.add(rs.getString(i + 1));
/* 274:    */           }
/* 275:323 */           values.add(row1);
/* 276:    */         }
/* 277:325 */         return values;
/* 278:    */       }
/* 279:    */     });
/* 280:    */   }
/* 281:    */   
/* 282:    */   public String getReport(int reportId, String[] params)
/* 283:    */   {
/* 284:339 */     Hashtable<String, String> ht = new Hashtable();
/* 285:340 */     getReportDefinition(reportId, ht);
/* 286:341 */     Node n = new Node("row", "root", "");
/* 287:342 */     String table = (String)ht.get("table");
/* 288:343 */     String headers = (String)ht.get("headings");
/* 289:    */     
/* 290:345 */     String colDim = (String)ht.get("col_dim");
/* 291:346 */     String aggregations = (String)ht.get("aggregations");
/* 292:347 */     String dimFields = (String)ht.get("dim_fields");
/* 293:348 */     String factFields = (String)ht.get("fact_fields");
/* 294:349 */     String where = (String)ht.get("where");
/* 295:350 */     MessageFormat mf = new MessageFormat(where);
/* 296:351 */     String where1 = mf.format(params);
/* 297:352 */     String images = "folder.gif,leaf.gif";
/* 298:353 */     Vector<Vector<String>> colVals = new Vector();
/* 299:354 */     String[] colDims = (String[])null;
/* 300:355 */     String[] colDims1 = (String[])null;
/* 301:356 */     String[] factIdxs = (String[])null;
/* 302:    */     
/* 303:358 */     String fff = "";
/* 304:359 */     String hhh = "";
/* 305:360 */     String agg = "";
/* 306:361 */     this.numDims = dimFields.split(",").length;
/* 307:362 */     this.numFacts = factFields.split(",").length;
/* 308:    */     
/* 309:    */ 
/* 310:    */ 
/* 311:    */ 
/* 312:    */ 
/* 313:    */ 
/* 314:    */ 
/* 315:    */ 
/* 316:    */ 
/* 317:    */ 
/* 318:    */ 
/* 319:    */ 
/* 320:    */ 
/* 321:    */ 
/* 322:    */ 
/* 323:    */ 
/* 324:    */ 
/* 325:    */ 
/* 326:    */ 
/* 327:    */ 
/* 328:    */ 
/* 329:    */ 
/* 330:    */ 
/* 331:    */ 
/* 332:    */ 
/* 333:    */ 
/* 334:    */ 
/* 335:    */ 
/* 336:    */ 
/* 337:    */ 
/* 338:    */ 
/* 339:    */ 
/* 340:    */ 
/* 341:    */ 
/* 342:    */ 
/* 343:    */ 
/* 344:    */ 
/* 345:    */ 
/* 346:    */ 
/* 347:    */ 
/* 348:    */ 
/* 349:    */ 
/* 350:    */ 
/* 351:    */ 
/* 352:    */ 
/* 353:    */ 
/* 354:    */ 
/* 355:    */ 
/* 356:    */ 
/* 357:    */ 
/* 358:    */ 
/* 359:    */ 
/* 360:    */ 
/* 361:    */ 
/* 362:    */ 
/* 363:    */ 
/* 364:    */ 
/* 365:    */ 
/* 366:    */ 
/* 367:    */ 
/* 368:    */ 
/* 369:    */ 
/* 370:    */ 
/* 371:    */ 
/* 372:    */ 
/* 373:    */ 
/* 374:    */ 
/* 375:    */ 
/* 376:    */ 
/* 377:    */ 
/* 378:    */ 
/* 379:    */ 
/* 380:    */ 
/* 381:436 */     List<String> headers1 = Arrays.asList(headers.split(","));
/* 382:437 */     List<String> images1 = Arrays.asList(images.split(","));
/* 383:438 */     List<String> aggregations1 = Arrays.asList(aggregations.split(","));
/* 384:    */     
/* 385:440 */     Vector<String> headers2 = new Vector();
/* 386:441 */     headers2.addAll(headers1);
/* 387:442 */     Vector<String> images2 = new Vector();
/* 388:443 */     images2.addAll(images1);
/* 389:444 */     Vector<String> aggregations2 = new Vector();
/* 390:445 */     aggregations2.addAll(aggregations1);
/* 391:    */     
/* 392:    */ 
/* 393:    */ 
/* 394:    */ 
/* 395:    */ 
/* 396:    */ 
/* 397:    */ 
/* 398:    */ 
/* 399:    */ 
/* 400:    */ 
/* 401:456 */     Vector<Vector<String>> values2 = queryFacts(table, colDim, where1, dimFields, factFields, aggregations, headers2, aggregations2);
/* 402:    */     
/* 403:    */ 
/* 404:    */ 
/* 405:    */ 
/* 406:    */ 
/* 407:    */ 
/* 408:    */ 
/* 409:    */ 
/* 410:    */ 
/* 411:    */ 
/* 412:    */ 
/* 413:    */ 
/* 414:    */ 
/* 415:    */ 
/* 416:    */ 
/* 417:    */ 
/* 418:    */ 
/* 419:    */ 
/* 420:    */ 
/* 421:    */ 
/* 422:    */ 
/* 423:    */ 
/* 424:    */ 
/* 425:    */ 
/* 426:    */ 
/* 427:    */ 
/* 428:    */ 
/* 429:    */ 
/* 430:    */ 
/* 431:486 */     Node.testNode(headers2, images2, aggregations2, values2, this.numFacts, n);
/* 432:487 */     return n.toString();
/* 433:    */   }
/* 434:    */   
/* 435:    */   public void getReportDefinition(int reportId, Hashtable ht)
/* 436:    */   {
/* 437:493 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/* 438:494 */     SqlRowSet rs = jt.queryForRowSet("select `table`,dim_fields,fact_fields,`where`,headings,init_widths,col_align,col_types,sorting,images,aggregations,col_dim,fact_idx from reports where id=" + reportId);
/* 439:495 */     if (rs.next())
/* 440:    */     {
/* 441:497 */       ht.put("table", rs.getString(1));
/* 442:498 */       ht.put("dim_fields", rs.getString(2));
/* 443:499 */       ht.put("fact_fields", rs.getString(3));
/* 444:500 */       ht.put("where", rs.getString(4));
/* 445:501 */       ht.put("headings", rs.getString(5));
/* 446:    */       
/* 447:    */ 
/* 448:    */ 
/* 449:    */ 
/* 450:    */ 
/* 451:507 */       ht.put("aggregations", rs.getString(11));
/* 452:508 */       ht.put("col_dim", rs.getString(12) == null ? "" : rs.getString(12));
/* 453:509 */       ht.put("fact_idx", rs.getString(13) == null ? "" : rs.getString(13));
/* 454:    */     }
/* 455:    */   }
/* 456:    */   
/* 457:    */   public static void main(String[] args)
/* 458:    */   {
/* 459:514 */     new SpringApplicationContext().setApplicationContext(new ClassPathXmlApplicationContext("appContext.xml"));
/* 460:515 */     ReportGenerator r = new ReportGenerator();
/* 461:516 */     String[] params = { "2013-07-01", "2013-09-01" };
/* 462:    */   }
/* 463:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.utilities.dhtmlx.ReportGenerator
 * JD-Core Version:    0.7.0.1
 */