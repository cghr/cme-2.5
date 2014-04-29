/*   1:    */ package jxl.demo;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.net.MalformedURLException;
/*   6:    */ import java.net.URL;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Calendar;
/*   9:    */ import java.util.Date;
/*  10:    */ import jxl.Cell;
/*  11:    */ import jxl.CellType;
/*  12:    */ import jxl.Range;
/*  13:    */ import jxl.Workbook;
/*  14:    */ import jxl.common.Logger;
/*  15:    */ import jxl.format.CellFormat;
/*  16:    */ import jxl.format.Colour;
/*  17:    */ import jxl.format.UnderlineStyle;
/*  18:    */ import jxl.read.biff.BiffException;
/*  19:    */ import jxl.write.Blank;
/*  20:    */ import jxl.write.DateFormat;
/*  21:    */ import jxl.write.DateFormats;
/*  22:    */ import jxl.write.DateTime;
/*  23:    */ import jxl.write.Formula;
/*  24:    */ import jxl.write.Label;
/*  25:    */ import jxl.write.Number;
/*  26:    */ import jxl.write.NumberFormat;
/*  27:    */ import jxl.write.WritableCell;
/*  28:    */ import jxl.write.WritableCellFeatures;
/*  29:    */ import jxl.write.WritableCellFormat;
/*  30:    */ import jxl.write.WritableFont;
/*  31:    */ import jxl.write.WritableHyperlink;
/*  32:    */ import jxl.write.WritableImage;
/*  33:    */ import jxl.write.WritableSheet;
/*  34:    */ import jxl.write.WritableWorkbook;
/*  35:    */ import jxl.write.WriteException;
/*  36:    */ 
/*  37:    */ public class ReadWrite
/*  38:    */ {
/*  39: 73 */   private static Logger logger = Logger.getLogger(ReadWrite.class);
/*  40:    */   private File inputWorkbook;
/*  41:    */   private File outputWorkbook;
/*  42:    */   
/*  43:    */   public ReadWrite(String input, String output)
/*  44:    */   {
/*  45: 92 */     this.inputWorkbook = new File(input);
/*  46: 93 */     this.outputWorkbook = new File(output);
/*  47: 94 */     logger.setSuppressWarnings(Boolean.getBoolean("jxl.nowarnings"));
/*  48: 95 */     logger.info("Input file:  " + input);
/*  49: 96 */     logger.info("Output file:  " + output);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void readWrite()
/*  53:    */     throws IOException, BiffException, WriteException
/*  54:    */   {
/*  55:107 */     logger.info("Reading...");
/*  56:108 */     Workbook w1 = Workbook.getWorkbook(this.inputWorkbook);
/*  57:    */     
/*  58:110 */     logger.info("Copying...");
/*  59:111 */     WritableWorkbook w2 = Workbook.createWorkbook(this.outputWorkbook, w1);
/*  60:113 */     if (this.inputWorkbook.getName().equals("jxlrwtest.xls")) {
/*  61:115 */       modify(w2);
/*  62:    */     }
/*  63:118 */     w2.write();
/*  64:119 */     w2.close();
/*  65:120 */     logger.info("Done");
/*  66:    */   }
/*  67:    */   
/*  68:    */   private void modify(WritableWorkbook w)
/*  69:    */     throws WriteException
/*  70:    */   {
/*  71:131 */     logger.info("Modifying...");
/*  72:    */     
/*  73:133 */     WritableSheet sheet = w.getSheet("modified");
/*  74:    */     
/*  75:135 */     WritableCell cell = null;
/*  76:136 */     CellFormat cf = null;
/*  77:137 */     Label l = null;
/*  78:138 */     WritableCellFeatures wcf = null;
/*  79:    */     
/*  80:    */ 
/*  81:141 */     cell = sheet.getWritableCell(1, 3);
/*  82:142 */     WritableFont bold = new WritableFont(WritableFont.ARIAL, 
/*  83:143 */       10, 
/*  84:144 */       WritableFont.BOLD);
/*  85:145 */     cf = new WritableCellFormat(bold);
/*  86:146 */     cell.setCellFormat(cf);
/*  87:    */     
/*  88:    */ 
/*  89:149 */     cell = sheet.getWritableCell(1, 4);
/*  90:150 */     WritableFont underline = new WritableFont(WritableFont.ARIAL, 
/*  91:151 */       10, 
/*  92:152 */       WritableFont.NO_BOLD, 
/*  93:153 */       false, 
/*  94:154 */       UnderlineStyle.SINGLE);
/*  95:155 */     cf = new WritableCellFormat(underline);
/*  96:156 */     cell.setCellFormat(cf);
/*  97:    */     
/*  98:    */ 
/*  99:159 */     cell = sheet.getWritableCell(1, 5);
/* 100:160 */     WritableFont tenpoint = new WritableFont(WritableFont.ARIAL, 10);
/* 101:161 */     cf = new WritableCellFormat(tenpoint);
/* 102:162 */     cell.setCellFormat(cf);
/* 103:    */     
/* 104:    */ 
/* 105:165 */     cell = sheet.getWritableCell(1, 6);
/* 106:166 */     if (cell.getType() == CellType.LABEL)
/* 107:    */     {
/* 108:168 */       Label lc = (Label)cell;
/* 109:169 */       lc.setString(lc.getString() + " - mod");
/* 110:    */     }
/* 111:173 */     cell = sheet.getWritableCell(1, 9);
/* 112:174 */     NumberFormat sevendps = new NumberFormat("#.0000000");
/* 113:175 */     cf = new WritableCellFormat(sevendps);
/* 114:176 */     cell.setCellFormat(cf);
/* 115:    */     
/* 116:    */ 
/* 117:    */ 
/* 118:180 */     cell = sheet.getWritableCell(1, 10);
/* 119:181 */     NumberFormat exp4 = new NumberFormat("0.####E0");
/* 120:182 */     cf = new WritableCellFormat(exp4);
/* 121:183 */     cell.setCellFormat(cf);
/* 122:    */     
/* 123:    */ 
/* 124:186 */     cell = sheet.getWritableCell(1, 11);
/* 125:187 */     cell.setCellFormat(WritableWorkbook.NORMAL_STYLE);
/* 126:    */     
/* 127:    */ 
/* 128:190 */     cell = sheet.getWritableCell(1, 12);
/* 129:191 */     if (cell.getType() == CellType.NUMBER)
/* 130:    */     {
/* 131:193 */       Number n = (Number)cell;
/* 132:194 */       n.setValue(42.0D);
/* 133:    */     }
/* 134:198 */     cell = sheet.getWritableCell(1, 13);
/* 135:199 */     if (cell.getType() == CellType.NUMBER)
/* 136:    */     {
/* 137:201 */       Number n = (Number)cell;
/* 138:202 */       n.setValue(n.getValue() + 0.1D);
/* 139:    */     }
/* 140:206 */     cell = sheet.getWritableCell(1, 16);
/* 141:207 */     DateFormat df = new DateFormat("dd MMM yyyy HH:mm:ss");
/* 142:208 */     cf = new WritableCellFormat(df);
/* 143:209 */     cell.setCellFormat(cf);
/* 144:    */     
/* 145:    */ 
/* 146:212 */     cell = sheet.getWritableCell(1, 17);
/* 147:213 */     cf = new WritableCellFormat(DateFormats.FORMAT9);
/* 148:214 */     cell.setCellFormat(cf);
/* 149:    */     
/* 150:    */ 
/* 151:217 */     cell = sheet.getWritableCell(1, 18);
/* 152:218 */     if (cell.getType() == CellType.DATE)
/* 153:    */     {
/* 154:220 */       DateTime dt = (DateTime)cell;
/* 155:221 */       Calendar cal = Calendar.getInstance();
/* 156:222 */       cal.set(1998, 1, 18, 11, 23, 28);
/* 157:223 */       Date d = cal.getTime();
/* 158:224 */       dt.setDate(d);
/* 159:    */     }
/* 160:229 */     cell = sheet.getWritableCell(1, 22);
/* 161:230 */     if (cell.getType() == CellType.NUMBER)
/* 162:    */     {
/* 163:232 */       Number n = (Number)cell;
/* 164:233 */       n.setValue(6.8D);
/* 165:    */     }
/* 166:238 */     cell = sheet.getWritableCell(1, 29);
/* 167:239 */     if (cell.getType() == CellType.LABEL)
/* 168:    */     {
/* 169:241 */       l = (Label)cell;
/* 170:242 */       l.setString("Modified string contents");
/* 171:    */     }
/* 172:245 */     sheet.insertRow(34);
/* 173:    */     
/* 174:    */ 
/* 175:248 */     sheet.removeRow(38);
/* 176:    */     
/* 177:    */ 
/* 178:251 */     sheet.insertColumn(9);
/* 179:    */     
/* 180:    */ 
/* 181:254 */     sheet.removeColumn(11);
/* 182:    */     
/* 183:    */ 
/* 184:    */ 
/* 185:258 */     sheet.removeRow(43);
/* 186:259 */     sheet.insertRow(43);
/* 187:    */     
/* 188:    */ 
/* 189:262 */     WritableHyperlink[] hyperlinks = sheet.getWritableHyperlinks();
/* 190:264 */     for (int i = 0; i < hyperlinks.length; i++)
/* 191:    */     {
/* 192:266 */       WritableHyperlink wh = hyperlinks[i];
/* 193:267 */       if ((wh.getColumn() == 1) && (wh.getRow() == 39)) {
/* 194:    */         try
/* 195:    */         {
/* 196:272 */           wh.setURL(new URL("http://www.andykhan.com/jexcelapi/index.html"));
/* 197:    */         }
/* 198:    */         catch (MalformedURLException e)
/* 199:    */         {
/* 200:276 */           logger.warn(e.toString());
/* 201:    */         }
/* 202:279 */       } else if ((wh.getColumn() == 1) && (wh.getRow() == 40)) {
/* 203:281 */         wh.setFile(new File("../jexcelapi/docs/overview-summary.html"));
/* 204:283 */       } else if ((wh.getColumn() == 1) && (wh.getRow() == 41)) {
/* 205:285 */         wh.setFile(new File("d:/home/jexcelapi/docs/jxl/package-summary.html"));
/* 206:287 */       } else if ((wh.getColumn() == 1) && (wh.getRow() == 44)) {
/* 207:290 */         sheet.removeHyperlink(wh);
/* 208:    */       }
/* 209:    */     }
/* 210:295 */     WritableCell c = sheet.getWritableCell(5, 30);
/* 211:296 */     WritableCellFormat newFormat = new WritableCellFormat(c.getCellFormat());
/* 212:297 */     newFormat.setBackground(Colour.RED);
/* 213:298 */     c.setCellFormat(newFormat);
/* 214:    */     
/* 215:    */ 
/* 216:301 */     l = new Label(0, 49, "Modified merged cells");
/* 217:302 */     sheet.addCell(l);
/* 218:    */     
/* 219:    */ 
/* 220:305 */     Number n = (Number)sheet.getWritableCell(0, 70);
/* 221:306 */     n.setValue(9.0D);
/* 222:    */     
/* 223:308 */     n = (Number)sheet.getWritableCell(0, 71);
/* 224:309 */     n.setValue(10.0D);
/* 225:    */     
/* 226:311 */     n = (Number)sheet.getWritableCell(0, 73);
/* 227:312 */     n.setValue(4.0D);
/* 228:    */     
/* 229:    */ 
/* 230:315 */     Formula f = new Formula(1, 80, "ROUND(COS(original!B10),2)");
/* 231:316 */     sheet.addCell(f);
/* 232:    */     
/* 233:    */ 
/* 234:319 */     f = new Formula(1, 83, "value1+value2");
/* 235:320 */     sheet.addCell(f);
/* 236:    */     
/* 237:    */ 
/* 238:323 */     f = new Formula(1, 84, "AVERAGE(value1,value1*4,value2)");
/* 239:324 */     sheet.addCell(f);
/* 240:    */     
/* 241:    */ 
/* 242:    */ 
/* 243:    */ 
/* 244:    */ 
/* 245:330 */     Label label = new Label(0, 88, "Some copied cells", cf);
/* 246:331 */     sheet.addCell(label);
/* 247:    */     
/* 248:333 */     label = new Label(0, 89, "Number from B9");
/* 249:334 */     sheet.addCell(label);
/* 250:    */     
/* 251:336 */     WritableCell wc = sheet.getWritableCell(1, 9).copyTo(1, 89);
/* 252:337 */     sheet.addCell(wc);
/* 253:    */     
/* 254:339 */     label = new Label(0, 90, "Label from B4 (modified format)");
/* 255:340 */     sheet.addCell(label);
/* 256:    */     
/* 257:342 */     wc = sheet.getWritableCell(1, 3).copyTo(1, 90);
/* 258:343 */     sheet.addCell(wc);
/* 259:    */     
/* 260:345 */     label = new Label(0, 91, "Date from B17");
/* 261:346 */     sheet.addCell(label);
/* 262:    */     
/* 263:348 */     wc = sheet.getWritableCell(1, 16).copyTo(1, 91);
/* 264:349 */     sheet.addCell(wc);
/* 265:    */     
/* 266:351 */     label = new Label(0, 92, "Boolean from E16");
/* 267:352 */     sheet.addCell(label);
/* 268:    */     
/* 269:354 */     wc = sheet.getWritableCell(4, 15).copyTo(1, 92);
/* 270:355 */     sheet.addCell(wc);
/* 271:    */     
/* 272:357 */     label = new Label(0, 93, "URL from B40");
/* 273:358 */     sheet.addCell(label);
/* 274:    */     
/* 275:360 */     wc = sheet.getWritableCell(1, 39).copyTo(1, 93);
/* 276:361 */     sheet.addCell(wc);
/* 277:364 */     for (int i = 0; i < 6; i++)
/* 278:    */     {
/* 279:366 */       Number number = new Number(1, 94 + i, i + 1 + i / 8.0D);
/* 280:367 */       sheet.addCell(number);
/* 281:    */     }
/* 282:370 */     label = new Label(0, 100, "Formula from B27");
/* 283:371 */     sheet.addCell(label);
/* 284:    */     
/* 285:373 */     wc = sheet.getWritableCell(1, 26).copyTo(1, 100);
/* 286:374 */     sheet.addCell(wc);
/* 287:    */     
/* 288:376 */     label = new Label(0, 101, "A brand new formula");
/* 289:377 */     sheet.addCell(label);
/* 290:    */     
/* 291:379 */     Formula formula = new Formula(1, 101, "SUM(B94:B96)");
/* 292:380 */     sheet.addCell(formula);
/* 293:    */     
/* 294:382 */     label = new Label(0, 102, "A copy of it");
/* 295:383 */     sheet.addCell(label);
/* 296:    */     
/* 297:385 */     wc = sheet.getWritableCell(1, 101).copyTo(1, 102);
/* 298:386 */     sheet.addCell(wc);
/* 299:    */     
/* 300:    */ 
/* 301:389 */     WritableImage wi = sheet.getImage(1);
/* 302:390 */     sheet.removeImage(wi);
/* 303:    */     
/* 304:392 */     wi = new WritableImage(1.0D, 116.0D, 2.0D, 9.0D, 
/* 305:393 */       new File("resources/littlemoretonhall.png"));
/* 306:394 */     sheet.addImage(wi);
/* 307:    */     
/* 308:    */ 
/* 309:397 */     label = new Label(0, 151, "Added drop down validation");
/* 310:398 */     sheet.addCell(label);
/* 311:    */     
/* 312:400 */     Blank b = new Blank(1, 151);
/* 313:401 */     wcf = new WritableCellFeatures();
/* 314:402 */     ArrayList al = new ArrayList();
/* 315:403 */     al.add("The Fellowship of the Ring");
/* 316:404 */     al.add("The Two Towers");
/* 317:405 */     al.add("The Return of the King");
/* 318:406 */     wcf.setDataValidationList(al);
/* 319:407 */     b.setCellFeatures(wcf);
/* 320:408 */     sheet.addCell(b);
/* 321:    */     
/* 322:    */ 
/* 323:411 */     label = new Label(0, 152, "Added number validation 2.718 < x < 3.142");
/* 324:412 */     sheet.addCell(label);
/* 325:413 */     b = new Blank(1, 152);
/* 326:414 */     wcf = new WritableCellFeatures();
/* 327:415 */     wcf.setNumberValidation(2.718D, 3.142D, WritableCellFeatures.BETWEEN);
/* 328:416 */     b.setCellFeatures(wcf);
/* 329:417 */     sheet.addCell(b);
/* 330:    */     
/* 331:    */ 
/* 332:420 */     cell = sheet.getWritableCell(0, 156);
/* 333:421 */     l = (Label)cell;
/* 334:422 */     l.setString("Label text modified");
/* 335:    */     
/* 336:424 */     cell = sheet.getWritableCell(0, 157);
/* 337:425 */     wcf = cell.getWritableCellFeatures();
/* 338:426 */     wcf.setComment("modified comment text");
/* 339:    */     
/* 340:428 */     cell = sheet.getWritableCell(0, 158);
/* 341:429 */     wcf = cell.getWritableCellFeatures();
/* 342:430 */     wcf.removeComment();
/* 343:    */     
/* 344:    */ 
/* 345:433 */     cell = sheet.getWritableCell(0, 172);
/* 346:434 */     wcf = cell.getWritableCellFeatures();
/* 347:435 */     Range r = wcf.getSharedDataValidationRange();
/* 348:436 */     Cell botright = r.getBottomRight();
/* 349:437 */     sheet.removeSharedDataValidation(cell);
/* 350:438 */     al = new ArrayList();
/* 351:439 */     al.add("Stanley Featherstonehaugh Ukridge");
/* 352:440 */     al.add("Major Plank");
/* 353:441 */     al.add("Earl of Ickenham");
/* 354:442 */     al.add("Sir Gregory Parsloe-Parsloe");
/* 355:443 */     al.add("Honoria Glossop");
/* 356:444 */     al.add("Stiffy Byng");
/* 357:445 */     al.add("Bingo Little");
/* 358:446 */     wcf.setDataValidationList(al);
/* 359:447 */     cell.setCellFeatures(wcf);
/* 360:448 */     sheet.applySharedDataValidation(cell, 
/* 361:449 */       botright.getColumn() - cell.getColumn(), 
/* 362:450 */       1);
/* 363:    */   }
/* 364:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.ReadWrite
 * JD-Core Version:    0.7.0.1
 */