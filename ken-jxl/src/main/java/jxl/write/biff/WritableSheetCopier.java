/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.TreeSet;
/*   7:    */ import jxl.BooleanCell;
/*   8:    */ import jxl.Cell;
/*   9:    */ import jxl.CellFeatures;
/*  10:    */ import jxl.CellType;
/*  11:    */ import jxl.DateCell;
/*  12:    */ import jxl.LabelCell;
/*  13:    */ import jxl.NumberCell;
/*  14:    */ import jxl.Range;
/*  15:    */ import jxl.WorkbookSettings;
/*  16:    */ import jxl.biff.CellReferenceHelper;
/*  17:    */ import jxl.biff.DataValidation;
/*  18:    */ import jxl.biff.FormattingRecords;
/*  19:    */ import jxl.biff.FormulaData;
/*  20:    */ import jxl.biff.NumFormatRecordsException;
/*  21:    */ import jxl.biff.SheetRangeImpl;
/*  22:    */ import jxl.biff.WorkspaceInformationRecord;
/*  23:    */ import jxl.biff.XFRecord;
/*  24:    */ import jxl.biff.drawing.Drawing;
/*  25:    */ import jxl.biff.formula.FormulaException;
/*  26:    */ import jxl.common.Assert;
/*  27:    */ import jxl.common.Logger;
/*  28:    */ import jxl.format.CellFormat;
/*  29:    */ import jxl.write.Blank;
/*  30:    */ import jxl.write.Boolean;
/*  31:    */ import jxl.write.DateTime;
/*  32:    */ import jxl.write.Formula;
/*  33:    */ import jxl.write.Label;
/*  34:    */ import jxl.write.Number;
/*  35:    */ import jxl.write.WritableCell;
/*  36:    */ import jxl.write.WritableCellFormat;
/*  37:    */ import jxl.write.WritableHyperlink;
/*  38:    */ import jxl.write.WritableImage;
/*  39:    */ import jxl.write.WritableSheet;
/*  40:    */ import jxl.write.WritableWorkbook;
/*  41:    */ import jxl.write.WriteException;
/*  42:    */ 
/*  43:    */ class WritableSheetCopier
/*  44:    */ {
/*  45: 87 */   private static Logger logger = Logger.getLogger(SheetCopier.class);
/*  46:    */   private WritableSheetImpl fromSheet;
/*  47:    */   private WritableSheetImpl toSheet;
/*  48:    */   private WorkbookSettings workbookSettings;
/*  49:    */   private TreeSet fromColumnFormats;
/*  50:    */   private TreeSet toColumnFormats;
/*  51:    */   private MergedCells fromMergedCells;
/*  52:    */   private MergedCells toMergedCells;
/*  53:    */   private RowRecord[] fromRows;
/*  54:    */   private ArrayList fromRowBreaks;
/*  55:    */   private ArrayList fromColumnBreaks;
/*  56:    */   private ArrayList toRowBreaks;
/*  57:    */   private ArrayList toColumnBreaks;
/*  58:    */   private DataValidation fromDataValidation;
/*  59:    */   private DataValidation toDataValidation;
/*  60:    */   private SheetWriter sheetWriter;
/*  61:    */   private ArrayList fromDrawings;
/*  62:    */   private ArrayList toDrawings;
/*  63:    */   private ArrayList toImages;
/*  64:    */   private WorkspaceInformationRecord fromWorkspaceOptions;
/*  65:    */   private PLSRecord fromPLSRecord;
/*  66:    */   private PLSRecord toPLSRecord;
/*  67:    */   private ButtonPropertySetRecord fromButtonPropertySet;
/*  68:    */   private ButtonPropertySetRecord toButtonPropertySet;
/*  69:    */   private ArrayList fromHyperlinks;
/*  70:    */   private ArrayList toHyperlinks;
/*  71:    */   private ArrayList validatedCells;
/*  72:    */   private int numRows;
/*  73:    */   private int maxRowOutlineLevel;
/*  74:    */   private int maxColumnOutlineLevel;
/*  75:    */   private boolean chartOnly;
/*  76:    */   private FormattingRecords formatRecords;
/*  77:    */   private HashMap xfRecords;
/*  78:    */   private HashMap fonts;
/*  79:    */   private HashMap formats;
/*  80:    */   
/*  81:    */   public WritableSheetCopier(WritableSheet f, WritableSheet t)
/*  82:    */   {
/*  83:134 */     this.fromSheet = ((WritableSheetImpl)f);
/*  84:135 */     this.toSheet = ((WritableSheetImpl)t);
/*  85:136 */     this.workbookSettings = this.toSheet.getWorkbook().getSettings();
/*  86:137 */     this.chartOnly = false;
/*  87:    */   }
/*  88:    */   
/*  89:    */   void setColumnFormats(TreeSet fcf, TreeSet tcf)
/*  90:    */   {
/*  91:142 */     this.fromColumnFormats = fcf;
/*  92:143 */     this.toColumnFormats = tcf;
/*  93:    */   }
/*  94:    */   
/*  95:    */   void setMergedCells(MergedCells fmc, MergedCells tmc)
/*  96:    */   {
/*  97:148 */     this.fromMergedCells = fmc;
/*  98:149 */     this.toMergedCells = tmc;
/*  99:    */   }
/* 100:    */   
/* 101:    */   void setRows(RowRecord[] r)
/* 102:    */   {
/* 103:154 */     this.fromRows = r;
/* 104:    */   }
/* 105:    */   
/* 106:    */   void setValidatedCells(ArrayList vc)
/* 107:    */   {
/* 108:159 */     this.validatedCells = vc;
/* 109:    */   }
/* 110:    */   
/* 111:    */   void setRowBreaks(ArrayList frb, ArrayList trb)
/* 112:    */   {
/* 113:164 */     this.fromRowBreaks = frb;
/* 114:165 */     this.toRowBreaks = trb;
/* 115:    */   }
/* 116:    */   
/* 117:    */   void setColumnBreaks(ArrayList fcb, ArrayList tcb)
/* 118:    */   {
/* 119:170 */     this.fromColumnBreaks = fcb;
/* 120:171 */     this.toColumnBreaks = tcb;
/* 121:    */   }
/* 122:    */   
/* 123:    */   void setDrawings(ArrayList fd, ArrayList td, ArrayList ti)
/* 124:    */   {
/* 125:176 */     this.fromDrawings = fd;
/* 126:177 */     this.toDrawings = td;
/* 127:178 */     this.toImages = ti;
/* 128:    */   }
/* 129:    */   
/* 130:    */   void setHyperlinks(ArrayList fh, ArrayList th)
/* 131:    */   {
/* 132:183 */     this.fromHyperlinks = fh;
/* 133:184 */     this.toHyperlinks = th;
/* 134:    */   }
/* 135:    */   
/* 136:    */   void setWorkspaceOptions(WorkspaceInformationRecord wir)
/* 137:    */   {
/* 138:189 */     this.fromWorkspaceOptions = wir;
/* 139:    */   }
/* 140:    */   
/* 141:    */   void setDataValidation(DataValidation dv)
/* 142:    */   {
/* 143:194 */     this.fromDataValidation = dv;
/* 144:    */   }
/* 145:    */   
/* 146:    */   void setPLSRecord(PLSRecord plsr)
/* 147:    */   {
/* 148:199 */     this.fromPLSRecord = plsr;
/* 149:    */   }
/* 150:    */   
/* 151:    */   void setButtonPropertySetRecord(ButtonPropertySetRecord bpsr)
/* 152:    */   {
/* 153:204 */     this.fromButtonPropertySet = bpsr;
/* 154:    */   }
/* 155:    */   
/* 156:    */   void setSheetWriter(SheetWriter sw)
/* 157:    */   {
/* 158:209 */     this.sheetWriter = sw;
/* 159:    */   }
/* 160:    */   
/* 161:    */   DataValidation getDataValidation()
/* 162:    */   {
/* 163:215 */     return this.toDataValidation;
/* 164:    */   }
/* 165:    */   
/* 166:    */   PLSRecord getPLSRecord()
/* 167:    */   {
/* 168:220 */     return this.toPLSRecord;
/* 169:    */   }
/* 170:    */   
/* 171:    */   boolean isChartOnly()
/* 172:    */   {
/* 173:225 */     return this.chartOnly;
/* 174:    */   }
/* 175:    */   
/* 176:    */   ButtonPropertySetRecord getButtonPropertySet()
/* 177:    */   {
/* 178:230 */     return this.toButtonPropertySet;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public void copySheet()
/* 182:    */   {
/* 183:239 */     shallowCopyCells();
/* 184:    */     
/* 185:    */ 
/* 186:242 */     Iterator cfit = this.fromColumnFormats.iterator();
/* 187:243 */     while (cfit.hasNext())
/* 188:    */     {
/* 189:245 */       ColumnInfoRecord cv = new ColumnInfoRecord(
/* 190:246 */         (ColumnInfoRecord)cfit.next());
/* 191:247 */       this.toColumnFormats.add(cv);
/* 192:    */     }
/* 193:251 */     Range[] merged = this.fromMergedCells.getMergedCells();
/* 194:253 */     for (int i = 0; i < merged.length; i++) {
/* 195:255 */       this.toMergedCells.add(new SheetRangeImpl((SheetRangeImpl)merged[i], 
/* 196:256 */         this.toSheet));
/* 197:    */     }
/* 198:    */     try
/* 199:    */     {
/* 200:261 */       RowRecord row = null;
/* 201:262 */       RowRecord newRow = null;
/* 202:263 */       for (int i = 0; i < this.fromRows.length; i++)
/* 203:    */       {
/* 204:265 */         row = this.fromRows[i];
/* 205:267 */         if ((row != null) && (
/* 206:268 */           (!row.isDefaultHeight()) || 
/* 207:269 */           (row.isCollapsed())))
/* 208:    */         {
/* 209:271 */           newRow = this.toSheet.getRowRecord(i);
/* 210:272 */           newRow.setRowDetails(row.getRowHeight(), 
/* 211:273 */             row.matchesDefaultFontHeight(), 
/* 212:274 */             row.isCollapsed(), 
/* 213:275 */             row.getOutlineLevel(), 
/* 214:276 */             row.getGroupStart(), 
/* 215:277 */             row.getStyle());
/* 216:    */         }
/* 217:    */       }
/* 218:    */     }
/* 219:    */     catch (RowsExceededException e)
/* 220:    */     {
/* 221:285 */       Assert.verify(false);
/* 222:    */     }
/* 223:289 */     this.toRowBreaks = new ArrayList(this.fromRowBreaks);
/* 224:    */     
/* 225:    */ 
/* 226:292 */     this.toColumnBreaks = new ArrayList(this.fromColumnBreaks);
/* 227:295 */     if (this.fromDataValidation != null) {
/* 228:297 */       this.toDataValidation = new DataValidation(
/* 229:298 */         this.fromDataValidation, 
/* 230:299 */         this.toSheet.getWorkbook(), 
/* 231:300 */         this.toSheet.getWorkbook(), 
/* 232:301 */         this.toSheet.getWorkbook().getSettings());
/* 233:    */     }
/* 234:305 */     this.sheetWriter.setCharts(this.fromSheet.getCharts());
/* 235:308 */     for (Iterator i = this.fromDrawings.iterator(); i.hasNext();)
/* 236:    */     {
/* 237:310 */       Object o = i.next();
/* 238:311 */       if ((o instanceof Drawing))
/* 239:    */       {
/* 240:313 */         WritableImage wi = new WritableImage(
/* 241:314 */           (Drawing)o, 
/* 242:315 */           this.toSheet.getWorkbook().getDrawingGroup());
/* 243:316 */         this.toDrawings.add(wi);
/* 244:317 */         this.toImages.add(wi);
/* 245:    */       }
/* 246:    */     }
/* 247:325 */     this.sheetWriter.setWorkspaceOptions(this.fromWorkspaceOptions);
/* 248:328 */     if (this.fromPLSRecord != null) {
/* 249:330 */       this.toPLSRecord = new PLSRecord(this.fromPLSRecord);
/* 250:    */     }
/* 251:334 */     if (this.fromButtonPropertySet != null) {
/* 252:336 */       this.toButtonPropertySet = new ButtonPropertySetRecord(this.fromButtonPropertySet);
/* 253:    */     }
/* 254:340 */     for (Iterator i = this.fromHyperlinks.iterator(); i.hasNext();)
/* 255:    */     {
/* 256:342 */       WritableHyperlink hr = new WritableHyperlink(
/* 257:343 */         (WritableHyperlink)i.next(), this.toSheet);
/* 258:344 */       this.toHyperlinks.add(hr);
/* 259:    */     }
/* 260:    */   }
/* 261:    */   
/* 262:    */   private WritableCell shallowCopyCell(Cell cell)
/* 263:    */   {
/* 264:353 */     CellType ct = cell.getType();
/* 265:354 */     WritableCell newCell = null;
/* 266:356 */     if (ct == CellType.LABEL) {
/* 267:358 */       newCell = new Label((LabelCell)cell);
/* 268:360 */     } else if (ct == CellType.NUMBER) {
/* 269:362 */       newCell = new Number((NumberCell)cell);
/* 270:364 */     } else if (ct == CellType.DATE) {
/* 271:366 */       newCell = new DateTime((DateCell)cell);
/* 272:368 */     } else if (ct == CellType.BOOLEAN) {
/* 273:370 */       newCell = new Boolean((BooleanCell)cell);
/* 274:372 */     } else if (ct == CellType.NUMBER_FORMULA) {
/* 275:374 */       newCell = new ReadNumberFormulaRecord((FormulaData)cell);
/* 276:376 */     } else if (ct == CellType.STRING_FORMULA) {
/* 277:378 */       newCell = new ReadStringFormulaRecord((FormulaData)cell);
/* 278:380 */     } else if (ct == CellType.BOOLEAN_FORMULA) {
/* 279:382 */       newCell = new ReadBooleanFormulaRecord((FormulaData)cell);
/* 280:384 */     } else if (ct == CellType.DATE_FORMULA) {
/* 281:386 */       newCell = new ReadDateFormulaRecord((FormulaData)cell);
/* 282:388 */     } else if (ct == CellType.FORMULA_ERROR) {
/* 283:390 */       newCell = new ReadErrorFormulaRecord((FormulaData)cell);
/* 284:392 */     } else if (ct == CellType.EMPTY) {
/* 285:394 */       if (cell.getCellFormat() != null) {
/* 286:399 */         newCell = new Blank(cell);
/* 287:    */       }
/* 288:    */     }
/* 289:403 */     return newCell;
/* 290:    */   }
/* 291:    */   
/* 292:    */   private WritableCell deepCopyCell(Cell cell)
/* 293:    */   {
/* 294:413 */     WritableCell c = shallowCopyCell(cell);
/* 295:415 */     if (c == null) {
/* 296:417 */       return c;
/* 297:    */     }
/* 298:420 */     if ((c instanceof ReadFormulaRecord))
/* 299:    */     {
/* 300:422 */       ReadFormulaRecord rfr = (ReadFormulaRecord)c;
/* 301:423 */       boolean crossSheetReference = !rfr.handleImportedCellReferences(
/* 302:424 */         this.fromSheet.getWorkbook(), 
/* 303:425 */         this.fromSheet.getWorkbook(), 
/* 304:426 */         this.workbookSettings);
/* 305:428 */       if (crossSheetReference)
/* 306:    */       {
/* 307:    */         try
/* 308:    */         {
/* 309:432 */           logger.warn("Formula " + rfr.getFormula() + 
/* 310:433 */             " in cell " + 
/* 311:434 */             CellReferenceHelper.getCellReference(cell.getColumn(), 
/* 312:435 */             cell.getRow()) + 
/* 313:436 */             " cannot be imported because it references another " + 
/* 314:437 */             " sheet from the source workbook");
/* 315:    */         }
/* 316:    */         catch (FormulaException e)
/* 317:    */         {
/* 318:441 */           logger.warn("Formula  in cell " + 
/* 319:442 */             CellReferenceHelper.getCellReference(cell.getColumn(), 
/* 320:443 */             cell.getRow()) + 
/* 321:444 */             " cannot be imported:  " + e.getMessage());
/* 322:    */         }
/* 323:448 */         c = new Formula(cell.getColumn(), cell.getRow(), "\"ERROR\"");
/* 324:    */       }
/* 325:    */     }
/* 326:453 */     CellFormat cf = c.getCellFormat();
/* 327:454 */     int index = ((XFRecord)cf).getXFIndex();
/* 328:455 */     WritableCellFormat wcf = 
/* 329:456 */       (WritableCellFormat)this.xfRecords.get(new Integer(index));
/* 330:458 */     if (wcf == null) {
/* 331:460 */       wcf = copyCellFormat(cf);
/* 332:    */     }
/* 333:463 */     c.setCellFormat(wcf);
/* 334:    */     
/* 335:465 */     return c;
/* 336:    */   }
/* 337:    */   
/* 338:    */   void shallowCopyCells()
/* 339:    */   {
/* 340:474 */     int cells = this.fromSheet.getRows();
/* 341:475 */     Cell[] row = (Cell[])null;
/* 342:476 */     Cell cell = null;
/* 343:477 */     for (int i = 0; i < cells; i++)
/* 344:    */     {
/* 345:479 */       row = this.fromSheet.getRow(i);
/* 346:481 */       for (int j = 0; j < row.length; j++)
/* 347:    */       {
/* 348:483 */         cell = row[j];
/* 349:484 */         WritableCell c = shallowCopyCell(cell);
/* 350:    */         try
/* 351:    */         {
/* 352:493 */           if (c != null)
/* 353:    */           {
/* 354:495 */             this.toSheet.addCell(c);
/* 355:500 */             if ((c.getCellFeatures() != null & c.getCellFeatures().hasDataValidation())) {
/* 356:502 */               this.validatedCells.add(c);
/* 357:    */             }
/* 358:    */           }
/* 359:    */         }
/* 360:    */         catch (WriteException e)
/* 361:    */         {
/* 362:508 */           Assert.verify(false);
/* 363:    */         }
/* 364:    */       }
/* 365:    */     }
/* 366:512 */     this.numRows = this.toSheet.getRows();
/* 367:    */   }
/* 368:    */   
/* 369:    */   void deepCopyCells()
/* 370:    */   {
/* 371:521 */     int cells = this.fromSheet.getRows();
/* 372:522 */     Cell[] row = (Cell[])null;
/* 373:523 */     Cell cell = null;
/* 374:524 */     for (int i = 0; i < cells; i++)
/* 375:    */     {
/* 376:526 */       row = this.fromSheet.getRow(i);
/* 377:528 */       for (int j = 0; j < row.length; j++)
/* 378:    */       {
/* 379:530 */         cell = row[j];
/* 380:531 */         WritableCell c = deepCopyCell(cell);
/* 381:    */         try
/* 382:    */         {
/* 383:540 */           if (c != null)
/* 384:    */           {
/* 385:542 */             this.toSheet.addCell(c);
/* 386:547 */             if ((c.getCellFeatures() != null & c.getCellFeatures().hasDataValidation())) {
/* 387:549 */               this.validatedCells.add(c);
/* 388:    */             }
/* 389:    */           }
/* 390:    */         }
/* 391:    */         catch (WriteException e)
/* 392:    */         {
/* 393:555 */           Assert.verify(false);
/* 394:    */         }
/* 395:    */       }
/* 396:    */     }
/* 397:    */   }
/* 398:    */   
/* 399:    */   private WritableCellFormat copyCellFormat(CellFormat cf)
/* 400:    */   {
/* 401:    */     try
/* 402:    */     {
/* 403:574 */       XFRecord xfr = (XFRecord)cf;
/* 404:575 */       WritableCellFormat f = new WritableCellFormat(xfr);
/* 405:576 */       this.formatRecords.addStyle(f);
/* 406:    */       
/* 407:    */ 
/* 408:579 */       int xfIndex = xfr.getXFIndex();
/* 409:580 */       this.xfRecords.put(new Integer(xfIndex), f);
/* 410:    */       
/* 411:582 */       int fontIndex = xfr.getFontIndex();
/* 412:583 */       this.fonts.put(new Integer(fontIndex), new Integer(f.getFontIndex()));
/* 413:    */       
/* 414:585 */       int formatIndex = xfr.getFormatRecord();
/* 415:586 */       this.formats.put(new Integer(formatIndex), new Integer(f.getFormatRecord()));
/* 416:    */       
/* 417:588 */       return f;
/* 418:    */     }
/* 419:    */     catch (NumFormatRecordsException e)
/* 420:    */     {
/* 421:592 */       logger.warn("Maximum number of format records exceeded.  Using default format.");
/* 422:    */     }
/* 423:595 */     return WritableWorkbook.NORMAL_STYLE;
/* 424:    */   }
/* 425:    */   
/* 426:    */   public int getMaxColumnOutlineLevel()
/* 427:    */   {
/* 428:607 */     return this.maxColumnOutlineLevel;
/* 429:    */   }
/* 430:    */   
/* 431:    */   public int getMaxRowOutlineLevel()
/* 432:    */   {
/* 433:617 */     return this.maxRowOutlineLevel;
/* 434:    */   }
/* 435:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.WritableSheetCopier
 * JD-Core Version:    0.7.0.1
 */