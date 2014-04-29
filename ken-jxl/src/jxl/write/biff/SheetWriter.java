/*    1:     */ package jxl.write.biff;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.util.ArrayList;
/*    5:     */ import java.util.Iterator;
/*    6:     */ import java.util.TreeSet;
/*    7:     */ import jxl.Cell;
/*    8:     */ import jxl.CellFeatures;
/*    9:     */ import jxl.HeaderFooter;
/*   10:     */ import jxl.Range;
/*   11:     */ import jxl.SheetSettings;
/*   12:     */ import jxl.WorkbookSettings;
/*   13:     */ import jxl.biff.AutoFilter;
/*   14:     */ import jxl.biff.ConditionalFormat;
/*   15:     */ import jxl.biff.DVParser;
/*   16:     */ import jxl.biff.DataValidation;
/*   17:     */ import jxl.biff.DataValiditySettingsRecord;
/*   18:     */ import jxl.biff.WorkspaceInformationRecord;
/*   19:     */ import jxl.biff.XFRecord;
/*   20:     */ import jxl.biff.drawing.Chart;
/*   21:     */ import jxl.biff.drawing.ComboBox;
/*   22:     */ import jxl.biff.drawing.SheetDrawingWriter;
/*   23:     */ import jxl.common.Assert;
/*   24:     */ import jxl.common.Logger;
/*   25:     */ import jxl.format.Border;
/*   26:     */ import jxl.format.BorderLineStyle;
/*   27:     */ import jxl.format.Colour;
/*   28:     */ import jxl.write.Blank;
/*   29:     */ import jxl.write.WritableCell;
/*   30:     */ import jxl.write.WritableCellFormat;
/*   31:     */ import jxl.write.WritableHyperlink;
/*   32:     */ import jxl.write.WriteException;
/*   33:     */ 
/*   34:     */ final class SheetWriter
/*   35:     */ {
/*   36:  66 */   private static Logger logger = Logger.getLogger(SheetWriter.class);
/*   37:     */   private File outputFile;
/*   38:     */   private RowRecord[] rows;
/*   39:     */   private int numRows;
/*   40:     */   private int numCols;
/*   41:     */   private HeaderRecord header;
/*   42:     */   private FooterRecord footer;
/*   43:     */   private SheetSettings settings;
/*   44:     */   private WorkbookSettings workbookSettings;
/*   45:     */   private ArrayList rowBreaks;
/*   46:     */   private ArrayList columnBreaks;
/*   47:     */   private ArrayList hyperlinks;
/*   48:     */   private ArrayList conditionalFormats;
/*   49:     */   private AutoFilter autoFilter;
/*   50:     */   private ArrayList validatedCells;
/*   51:     */   private DataValidation dataValidation;
/*   52:     */   private MergedCells mergedCells;
/*   53:     */   private PLSRecord plsRecord;
/*   54:     */   private ButtonPropertySetRecord buttonPropertySet;
/*   55:     */   private WorkspaceInformationRecord workspaceOptions;
/*   56:     */   private TreeSet columnFormats;
/*   57:     */   private SheetDrawingWriter drawingWriter;
/*   58:     */   private boolean chartOnly;
/*   59:     */   private int maxRowOutlineLevel;
/*   60:     */   private int maxColumnOutlineLevel;
/*   61:     */   private WritableSheetImpl sheet;
/*   62:     */   
/*   63:     */   public SheetWriter(File of, WritableSheetImpl wsi, WorkbookSettings ws)
/*   64:     */   {
/*   65: 194 */     this.outputFile = of;
/*   66: 195 */     this.sheet = wsi;
/*   67: 196 */     this.workspaceOptions = new WorkspaceInformationRecord();
/*   68: 197 */     this.workbookSettings = ws;
/*   69: 198 */     this.chartOnly = false;
/*   70: 199 */     this.drawingWriter = new SheetDrawingWriter(ws);
/*   71:     */   }
/*   72:     */   
/*   73:     */   public void write()
/*   74:     */     throws IOException
/*   75:     */   {
/*   76: 212 */     Assert.verify(this.rows != null);
/*   77: 215 */     if (this.chartOnly)
/*   78:     */     {
/*   79: 217 */       this.drawingWriter.write(this.outputFile);
/*   80: 218 */       return;
/*   81:     */     }
/*   82: 221 */     BOFRecord bof = new BOFRecord(BOFRecord.sheet);
/*   83: 222 */     this.outputFile.write(bof);
/*   84:     */     
/*   85:     */ 
/*   86: 225 */     int numBlocks = this.numRows / 32;
/*   87: 226 */     if (this.numRows - numBlocks * 32 != 0) {
/*   88: 228 */       numBlocks++;
/*   89:     */     }
/*   90: 231 */     int indexPos = this.outputFile.getPos();
/*   91:     */     
/*   92:     */ 
/*   93:     */ 
/*   94: 235 */     IndexRecord indexRecord = new IndexRecord(0, this.numRows, numBlocks);
/*   95: 236 */     this.outputFile.write(indexRecord);
/*   96: 238 */     if (this.settings.getAutomaticFormulaCalculation())
/*   97:     */     {
/*   98: 240 */       CalcModeRecord cmr = new CalcModeRecord(CalcModeRecord.automatic);
/*   99: 241 */       this.outputFile.write(cmr);
/*  100:     */     }
/*  101:     */     else
/*  102:     */     {
/*  103: 245 */       CalcModeRecord cmr = new CalcModeRecord(CalcModeRecord.manual);
/*  104: 246 */       this.outputFile.write(cmr);
/*  105:     */     }
/*  106: 249 */     CalcCountRecord ccr = new CalcCountRecord(100);
/*  107: 250 */     this.outputFile.write(ccr);
/*  108:     */     
/*  109: 252 */     RefModeRecord rmr = new RefModeRecord();
/*  110: 253 */     this.outputFile.write(rmr);
/*  111:     */     
/*  112: 255 */     IterationRecord itr = new IterationRecord(false);
/*  113: 256 */     this.outputFile.write(itr);
/*  114:     */     
/*  115: 258 */     DeltaRecord dtr = new DeltaRecord(0.001D);
/*  116: 259 */     this.outputFile.write(dtr);
/*  117:     */     
/*  118: 261 */     SaveRecalcRecord srr = new SaveRecalcRecord(
/*  119: 262 */       this.settings.getRecalculateFormulasBeforeSave());
/*  120: 263 */     this.outputFile.write(srr);
/*  121:     */     
/*  122: 265 */     PrintHeadersRecord phr = new PrintHeadersRecord(
/*  123: 266 */       this.settings.getPrintHeaders());
/*  124: 267 */     this.outputFile.write(phr);
/*  125:     */     
/*  126: 269 */     PrintGridLinesRecord pglr = new PrintGridLinesRecord(
/*  127: 270 */       this.settings.getPrintGridLines());
/*  128: 271 */     this.outputFile.write(pglr);
/*  129:     */     
/*  130: 273 */     GridSetRecord gsr = new GridSetRecord(true);
/*  131: 274 */     this.outputFile.write(gsr);
/*  132:     */     
/*  133: 276 */     GuttersRecord gutr = new GuttersRecord();
/*  134: 277 */     gutr.setMaxColumnOutline(this.maxColumnOutlineLevel + 1);
/*  135: 278 */     gutr.setMaxRowOutline(this.maxRowOutlineLevel + 1);
/*  136:     */     
/*  137: 280 */     this.outputFile.write(gutr);
/*  138:     */     
/*  139: 282 */     DefaultRowHeightRecord drhr = new DefaultRowHeightRecord(
/*  140: 283 */       this.settings.getDefaultRowHeight(), 
/*  141: 284 */       this.settings.getDefaultRowHeight() != 
/*  142: 285 */       255);
/*  143: 286 */     this.outputFile.write(drhr);
/*  144: 288 */     if (this.maxRowOutlineLevel > 0) {
/*  145: 290 */       this.workspaceOptions.setRowOutlines(true);
/*  146:     */     }
/*  147: 293 */     if (this.maxColumnOutlineLevel > 0) {
/*  148: 295 */       this.workspaceOptions.setColumnOutlines(true);
/*  149:     */     }
/*  150: 298 */     this.workspaceOptions.setFitToPages(this.settings.getFitToPages());
/*  151: 299 */     this.outputFile.write(this.workspaceOptions);
/*  152: 301 */     if (this.rowBreaks.size() > 0)
/*  153:     */     {
/*  154: 303 */       int[] rb = new int[this.rowBreaks.size()];
/*  155: 305 */       for (int i = 0; i < rb.length; i++) {
/*  156: 307 */         rb[i] = ((Integer)this.rowBreaks.get(i)).intValue();
/*  157:     */       }
/*  158: 310 */       HorizontalPageBreaksRecord hpbr = new HorizontalPageBreaksRecord(rb);
/*  159: 311 */       this.outputFile.write(hpbr);
/*  160:     */     }
/*  161: 314 */     if (this.columnBreaks.size() > 0)
/*  162:     */     {
/*  163: 316 */       int[] rb = new int[this.columnBreaks.size()];
/*  164: 318 */       for (int i = 0; i < rb.length; i++) {
/*  165: 320 */         rb[i] = ((Integer)this.columnBreaks.get(i)).intValue();
/*  166:     */       }
/*  167: 323 */       VerticalPageBreaksRecord hpbr = new VerticalPageBreaksRecord(rb);
/*  168: 324 */       this.outputFile.write(hpbr);
/*  169:     */     }
/*  170: 327 */     HeaderRecord header = new HeaderRecord(this.settings.getHeader().toString());
/*  171: 328 */     this.outputFile.write(header);
/*  172:     */     
/*  173: 330 */     FooterRecord footer = new FooterRecord(this.settings.getFooter().toString());
/*  174: 331 */     this.outputFile.write(footer);
/*  175:     */     
/*  176: 333 */     HorizontalCentreRecord hcr = new HorizontalCentreRecord(
/*  177: 334 */       this.settings.isHorizontalCentre());
/*  178: 335 */     this.outputFile.write(hcr);
/*  179:     */     
/*  180: 337 */     VerticalCentreRecord vcr = new VerticalCentreRecord(
/*  181: 338 */       this.settings.isVerticalCentre());
/*  182: 339 */     this.outputFile.write(vcr);
/*  183: 342 */     if (this.settings.getLeftMargin() != this.settings.getDefaultWidthMargin())
/*  184:     */     {
/*  185: 344 */       MarginRecord mr = new LeftMarginRecord(this.settings.getLeftMargin());
/*  186: 345 */       this.outputFile.write(mr);
/*  187:     */     }
/*  188: 348 */     if (this.settings.getRightMargin() != this.settings.getDefaultWidthMargin())
/*  189:     */     {
/*  190: 350 */       MarginRecord mr = new RightMarginRecord(this.settings.getRightMargin());
/*  191: 351 */       this.outputFile.write(mr);
/*  192:     */     }
/*  193: 354 */     if (this.settings.getTopMargin() != this.settings.getDefaultHeightMargin())
/*  194:     */     {
/*  195: 356 */       MarginRecord mr = new TopMarginRecord(this.settings.getTopMargin());
/*  196: 357 */       this.outputFile.write(mr);
/*  197:     */     }
/*  198: 360 */     if (this.settings.getBottomMargin() != this.settings.getDefaultHeightMargin())
/*  199:     */     {
/*  200: 362 */       MarginRecord mr = new BottomMarginRecord(this.settings.getBottomMargin());
/*  201: 363 */       this.outputFile.write(mr);
/*  202:     */     }
/*  203: 366 */     if (this.plsRecord != null) {
/*  204: 368 */       this.outputFile.write(this.plsRecord);
/*  205:     */     }
/*  206: 371 */     SetupRecord setup = new SetupRecord(this.settings);
/*  207: 372 */     this.outputFile.write(setup);
/*  208: 374 */     if (this.settings.isProtected())
/*  209:     */     {
/*  210: 376 */       ProtectRecord pr = new ProtectRecord(this.settings.isProtected());
/*  211: 377 */       this.outputFile.write(pr);
/*  212:     */       
/*  213: 379 */       ScenarioProtectRecord spr = new ScenarioProtectRecord(
/*  214: 380 */         this.settings.isProtected());
/*  215: 381 */       this.outputFile.write(spr);
/*  216:     */       
/*  217: 383 */       ObjectProtectRecord opr = new ObjectProtectRecord(
/*  218: 384 */         this.settings.isProtected());
/*  219: 385 */       this.outputFile.write(opr);
/*  220: 387 */       if (this.settings.getPassword() != null)
/*  221:     */       {
/*  222: 389 */         PasswordRecord pw = new PasswordRecord(this.settings.getPassword());
/*  223: 390 */         this.outputFile.write(pw);
/*  224:     */       }
/*  225: 392 */       else if (this.settings.getPasswordHash() != 0)
/*  226:     */       {
/*  227: 394 */         PasswordRecord pw = new PasswordRecord(this.settings.getPasswordHash());
/*  228: 395 */         this.outputFile.write(pw);
/*  229:     */       }
/*  230:     */     }
/*  231: 399 */     indexRecord.setDataStartPosition(this.outputFile.getPos());
/*  232: 400 */     DefaultColumnWidth dcw = 
/*  233: 401 */       new DefaultColumnWidth(this.settings.getDefaultColumnWidth());
/*  234: 402 */     this.outputFile.write(dcw);
/*  235:     */     
/*  236:     */ 
/*  237: 405 */     WritableCellFormat normalStyle = 
/*  238: 406 */       this.sheet.getWorkbook().getStyles().getNormalStyle();
/*  239: 407 */     WritableCellFormat defaultDateFormat = 
/*  240: 408 */       this.sheet.getWorkbook().getStyles().getDefaultDateFormat();
/*  241:     */     
/*  242:     */ 
/*  243: 411 */     ColumnInfoRecord cir = null;
/*  244:     */     Cell[] cells;
/*  245:     */     int i;
/*  246:     */     label1325:
/*  247: 412 */     for (Iterator colit = this.columnFormats.iterator(); colit.hasNext(); i < cells.length)
/*  248:     */     {
/*  249: 414 */       cir = (ColumnInfoRecord)colit.next();
/*  250: 417 */       if (cir.getColumn() < 256) {
/*  251: 419 */         this.outputFile.write(cir);
/*  252:     */       }
/*  253: 422 */       XFRecord xfr = cir.getCellFormat();
/*  254: 424 */       if ((xfr == normalStyle) || (cir.getColumn() >= 256)) {
/*  255:     */         break label1325;
/*  256:     */       }
/*  257: 427 */       cells = getColumn(cir.getColumn());
/*  258:     */       
/*  259: 429 */       i = 0; continue;
/*  260: 431 */       if ((cells[i] != null) && (
/*  261: 432 */         (cells[i].getCellFormat() == normalStyle) || 
/*  262: 433 */         (cells[i].getCellFormat() == defaultDateFormat))) {
/*  263: 437 */         ((WritableCell)cells[i]).setCellFormat(xfr);
/*  264:     */       }
/*  265: 429 */       i++;
/*  266:     */     }
/*  267: 444 */     if (this.autoFilter != null) {
/*  268: 446 */       this.autoFilter.write(this.outputFile);
/*  269:     */     }
/*  270: 449 */     DimensionRecord dr = new DimensionRecord(this.numRows, this.numCols);
/*  271: 450 */     this.outputFile.write(dr);
/*  272: 453 */     for (int block = 0; block < numBlocks; block++)
/*  273:     */     {
/*  274: 455 */       DBCellRecord dbcell = new DBCellRecord(this.outputFile.getPos());
/*  275:     */       
/*  276: 457 */       int blockRows = Math.min(32, this.numRows - block * 32);
/*  277: 458 */       boolean firstRow = true;
/*  278: 461 */       for (int i = block * 32; i < block * 32 + blockRows; i++) {
/*  279: 463 */         if (this.rows[i] != null)
/*  280:     */         {
/*  281: 465 */           this.rows[i].write(this.outputFile);
/*  282: 466 */           if (firstRow)
/*  283:     */           {
/*  284: 468 */             dbcell.setCellOffset(this.outputFile.getPos());
/*  285: 469 */             firstRow = false;
/*  286:     */           }
/*  287:     */         }
/*  288:     */       }
/*  289: 475 */       for (int i = block * 32; i < block * 32 + blockRows; i++) {
/*  290: 477 */         if (this.rows[i] != null)
/*  291:     */         {
/*  292: 479 */           dbcell.addCellRowPosition(this.outputFile.getPos());
/*  293: 480 */           this.rows[i].writeCells(this.outputFile);
/*  294:     */         }
/*  295:     */       }
/*  296: 485 */       indexRecord.addBlockPosition(this.outputFile.getPos());
/*  297:     */       
/*  298:     */ 
/*  299:     */ 
/*  300: 489 */       dbcell.setPosition(this.outputFile.getPos());
/*  301: 490 */       this.outputFile.write(dbcell);
/*  302:     */     }
/*  303: 494 */     if (!this.workbookSettings.getDrawingsDisabled()) {
/*  304: 496 */       this.drawingWriter.write(this.outputFile);
/*  305:     */     }
/*  306: 499 */     Window2Record w2r = new Window2Record(this.settings);
/*  307: 500 */     this.outputFile.write(w2r);
/*  308: 503 */     if ((this.settings.getHorizontalFreeze() != 0) || 
/*  309: 504 */       (this.settings.getVerticalFreeze() != 0))
/*  310:     */     {
/*  311: 506 */       PaneRecord pr = new PaneRecord(this.settings.getHorizontalFreeze(), 
/*  312: 507 */         this.settings.getVerticalFreeze());
/*  313: 508 */       this.outputFile.write(pr);
/*  314:     */       
/*  315:     */ 
/*  316: 511 */       SelectionRecord sr = new SelectionRecord(
/*  317: 512 */         SelectionRecord.upperLeft, 0, 0);
/*  318: 513 */       this.outputFile.write(sr);
/*  319: 516 */       if (this.settings.getHorizontalFreeze() != 0)
/*  320:     */       {
/*  321: 518 */         sr = new SelectionRecord(
/*  322: 519 */           SelectionRecord.upperRight, this.settings.getHorizontalFreeze(), 0);
/*  323: 520 */         this.outputFile.write(sr);
/*  324:     */       }
/*  325: 524 */       if (this.settings.getVerticalFreeze() != 0)
/*  326:     */       {
/*  327: 526 */         sr = new SelectionRecord(
/*  328: 527 */           SelectionRecord.lowerLeft, 0, this.settings.getVerticalFreeze());
/*  329: 528 */         this.outputFile.write(sr);
/*  330:     */       }
/*  331: 532 */       if ((this.settings.getHorizontalFreeze() != 0) && 
/*  332: 533 */         (this.settings.getVerticalFreeze() != 0))
/*  333:     */       {
/*  334: 535 */         sr = new SelectionRecord(
/*  335: 536 */           SelectionRecord.lowerRight, 
/*  336: 537 */           this.settings.getHorizontalFreeze(), 
/*  337: 538 */           this.settings.getVerticalFreeze());
/*  338: 539 */         this.outputFile.write(sr);
/*  339:     */       }
/*  340: 542 */       Weird1Record w1r = new Weird1Record();
/*  341: 543 */       this.outputFile.write(w1r);
/*  342:     */     }
/*  343:     */     else
/*  344:     */     {
/*  345: 549 */       SelectionRecord sr = new SelectionRecord(
/*  346: 550 */         SelectionRecord.upperLeft, 0, 0);
/*  347: 551 */       this.outputFile.write(sr);
/*  348:     */     }
/*  349: 555 */     if (this.settings.getZoomFactor() != 100)
/*  350:     */     {
/*  351: 557 */       SCLRecord sclr = new SCLRecord(this.settings.getZoomFactor());
/*  352: 558 */       this.outputFile.write(sclr);
/*  353:     */     }
/*  354: 562 */     this.mergedCells.write(this.outputFile);
/*  355:     */     
/*  356:     */ 
/*  357: 565 */     Iterator hi = this.hyperlinks.iterator();
/*  358: 566 */     WritableHyperlink hlr = null;
/*  359: 567 */     while (hi.hasNext())
/*  360:     */     {
/*  361: 569 */       hlr = (WritableHyperlink)hi.next();
/*  362: 570 */       this.outputFile.write(hlr);
/*  363:     */     }
/*  364: 573 */     if (this.buttonPropertySet != null) {
/*  365: 575 */       this.outputFile.write(this.buttonPropertySet);
/*  366:     */     }
/*  367: 579 */     if ((this.dataValidation != null) || (this.validatedCells.size() > 0)) {
/*  368: 581 */       writeDataValidation();
/*  369:     */     }
/*  370: 585 */     if ((this.conditionalFormats != null) && (this.conditionalFormats.size() > 0)) {
/*  371: 587 */       for (Iterator i = this.conditionalFormats.iterator(); i.hasNext();)
/*  372:     */       {
/*  373: 589 */         ConditionalFormat cf = (ConditionalFormat)i.next();
/*  374: 590 */         cf.write(this.outputFile);
/*  375:     */       }
/*  376:     */     }
/*  377: 594 */     EOFRecord eof = new EOFRecord();
/*  378: 595 */     this.outputFile.write(eof);
/*  379:     */     
/*  380:     */ 
/*  381:     */ 
/*  382: 599 */     this.outputFile.setData(indexRecord.getData(), indexPos + 4);
/*  383:     */   }
/*  384:     */   
/*  385:     */   final HeaderRecord getHeader()
/*  386:     */   {
/*  387: 609 */     return this.header;
/*  388:     */   }
/*  389:     */   
/*  390:     */   final FooterRecord getFooter()
/*  391:     */   {
/*  392: 619 */     return this.footer;
/*  393:     */   }
/*  394:     */   
/*  395:     */   void setWriteData(RowRecord[] rws, ArrayList rb, ArrayList cb, ArrayList hl, MergedCells mc, TreeSet cf, int mrol, int mcol)
/*  396:     */   {
/*  397: 637 */     this.rows = rws;
/*  398: 638 */     this.rowBreaks = rb;
/*  399: 639 */     this.columnBreaks = cb;
/*  400: 640 */     this.hyperlinks = hl;
/*  401: 641 */     this.mergedCells = mc;
/*  402: 642 */     this.columnFormats = cf;
/*  403: 643 */     this.maxRowOutlineLevel = mrol;
/*  404: 644 */     this.maxColumnOutlineLevel = mcol;
/*  405:     */   }
/*  406:     */   
/*  407:     */   void setDimensions(int rws, int cls)
/*  408:     */   {
/*  409: 656 */     this.numRows = rws;
/*  410: 657 */     this.numCols = cls;
/*  411:     */   }
/*  412:     */   
/*  413:     */   void setSettings(SheetSettings sr)
/*  414:     */   {
/*  415: 668 */     this.settings = sr;
/*  416:     */   }
/*  417:     */   
/*  418:     */   WorkspaceInformationRecord getWorkspaceOptions()
/*  419:     */   {
/*  420: 678 */     return this.workspaceOptions;
/*  421:     */   }
/*  422:     */   
/*  423:     */   void setWorkspaceOptions(WorkspaceInformationRecord wo)
/*  424:     */   {
/*  425: 688 */     if (wo != null) {
/*  426: 690 */       this.workspaceOptions = wo;
/*  427:     */     }
/*  428:     */   }
/*  429:     */   
/*  430:     */   void setCharts(Chart[] ch)
/*  431:     */   {
/*  432: 702 */     this.drawingWriter.setCharts(ch);
/*  433:     */   }
/*  434:     */   
/*  435:     */   void setDrawings(ArrayList dr, boolean mod)
/*  436:     */   {
/*  437: 713 */     this.drawingWriter.setDrawings(dr, mod);
/*  438:     */   }
/*  439:     */   
/*  440:     */   Chart[] getCharts()
/*  441:     */   {
/*  442: 723 */     return this.drawingWriter.getCharts();
/*  443:     */   }
/*  444:     */   
/*  445:     */   void checkMergedBorders()
/*  446:     */   {
/*  447: 734 */     Range[] mcells = this.mergedCells.getMergedCells();
/*  448: 735 */     ArrayList borderFormats = new ArrayList();
/*  449: 736 */     for (int mci = 0; mci < mcells.length; mci++)
/*  450:     */     {
/*  451: 738 */       Range range = mcells[mci];
/*  452: 739 */       Cell topLeft = range.getTopLeft();
/*  453: 740 */       XFRecord tlformat = (XFRecord)topLeft.getCellFormat();
/*  454: 742 */       if ((tlformat != null) && 
/*  455: 743 */         (tlformat.hasBorders()) && 
/*  456: 744 */         (!tlformat.isRead())) {
/*  457:     */         try
/*  458:     */         {
/*  459: 748 */           CellXFRecord cf1 = new CellXFRecord(tlformat);
/*  460: 749 */           Cell bottomRight = range.getBottomRight();
/*  461:     */           
/*  462: 751 */           cf1.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
/*  463: 752 */           cf1.setBorder(Border.LEFT, 
/*  464: 753 */             tlformat.getBorderLine(Border.LEFT), 
/*  465: 754 */             tlformat.getBorderColour(Border.LEFT));
/*  466: 755 */           cf1.setBorder(Border.TOP, 
/*  467: 756 */             tlformat.getBorderLine(Border.TOP), 
/*  468: 757 */             tlformat.getBorderColour(Border.TOP));
/*  469: 759 */           if (topLeft.getRow() == bottomRight.getRow()) {
/*  470: 761 */             cf1.setBorder(Border.BOTTOM, 
/*  471: 762 */               tlformat.getBorderLine(Border.BOTTOM), 
/*  472: 763 */               tlformat.getBorderColour(Border.BOTTOM));
/*  473:     */           }
/*  474: 766 */           if (topLeft.getColumn() == bottomRight.getColumn()) {
/*  475: 768 */             cf1.setBorder(Border.RIGHT, 
/*  476: 769 */               tlformat.getBorderLine(Border.RIGHT), 
/*  477: 770 */               tlformat.getBorderColour(Border.RIGHT));
/*  478:     */           }
/*  479: 773 */           int index = borderFormats.indexOf(cf1);
/*  480: 774 */           if (index != -1) {
/*  481: 776 */             cf1 = (CellXFRecord)borderFormats.get(index);
/*  482:     */           } else {
/*  483: 780 */             borderFormats.add(cf1);
/*  484:     */           }
/*  485: 782 */           ((WritableCell)topLeft).setCellFormat(cf1);
/*  486: 785 */           if (bottomRight.getRow() > topLeft.getRow())
/*  487:     */           {
/*  488: 788 */             if (bottomRight.getColumn() != topLeft.getColumn())
/*  489:     */             {
/*  490: 790 */               CellXFRecord cf2 = new CellXFRecord(tlformat);
/*  491: 791 */               cf2.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
/*  492: 792 */               cf2.setBorder(Border.LEFT, 
/*  493: 793 */                 tlformat.getBorderLine(Border.LEFT), 
/*  494: 794 */                 tlformat.getBorderColour(Border.LEFT));
/*  495: 795 */               cf2.setBorder(Border.BOTTOM, 
/*  496: 796 */                 tlformat.getBorderLine(Border.BOTTOM), 
/*  497: 797 */                 tlformat.getBorderColour(Border.BOTTOM));
/*  498:     */               
/*  499: 799 */               index = borderFormats.indexOf(cf2);
/*  500: 800 */               if (index != -1) {
/*  501: 802 */                 cf2 = (CellXFRecord)borderFormats.get(index);
/*  502:     */               } else {
/*  503: 806 */                 borderFormats.add(cf2);
/*  504:     */               }
/*  505: 809 */               this.sheet.addCell(new Blank(topLeft.getColumn(), 
/*  506: 810 */                 bottomRight.getRow(), cf2));
/*  507:     */             }
/*  508: 815 */             for (int i = topLeft.getRow() + 1; i < bottomRight.getRow(); i++)
/*  509:     */             {
/*  510: 817 */               CellXFRecord cf3 = new CellXFRecord(tlformat);
/*  511: 818 */               cf3.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
/*  512: 819 */               cf3.setBorder(Border.LEFT, 
/*  513: 820 */                 tlformat.getBorderLine(Border.LEFT), 
/*  514: 821 */                 tlformat.getBorderColour(Border.LEFT));
/*  515: 823 */               if (topLeft.getColumn() == bottomRight.getColumn()) {
/*  516: 825 */                 cf3.setBorder(Border.RIGHT, 
/*  517: 826 */                   tlformat.getBorderLine(Border.RIGHT), 
/*  518: 827 */                   tlformat.getBorderColour(Border.RIGHT));
/*  519:     */               }
/*  520: 830 */               index = borderFormats.indexOf(cf3);
/*  521: 831 */               if (index != -1) {
/*  522: 833 */                 cf3 = (CellXFRecord)borderFormats.get(index);
/*  523:     */               } else {
/*  524: 837 */                 borderFormats.add(cf3);
/*  525:     */               }
/*  526: 840 */               this.sheet.addCell(new Blank(topLeft.getColumn(), i, cf3));
/*  527:     */             }
/*  528:     */           }
/*  529: 845 */           if (bottomRight.getColumn() > topLeft.getColumn())
/*  530:     */           {
/*  531: 847 */             if (bottomRight.getRow() != topLeft.getRow())
/*  532:     */             {
/*  533: 850 */               CellXFRecord cf6 = new CellXFRecord(tlformat);
/*  534: 851 */               cf6.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
/*  535: 852 */               cf6.setBorder(Border.RIGHT, 
/*  536: 853 */                 tlformat.getBorderLine(Border.RIGHT), 
/*  537: 854 */                 tlformat.getBorderColour(Border.RIGHT));
/*  538: 855 */               cf6.setBorder(Border.TOP, 
/*  539: 856 */                 tlformat.getBorderLine(Border.TOP), 
/*  540: 857 */                 tlformat.getBorderColour(Border.TOP));
/*  541: 858 */               index = borderFormats.indexOf(cf6);
/*  542: 859 */               if (index != -1) {
/*  543: 861 */                 cf6 = (CellXFRecord)borderFormats.get(index);
/*  544:     */               } else {
/*  545: 865 */                 borderFormats.add(cf6);
/*  546:     */               }
/*  547: 868 */               this.sheet.addCell(new Blank(bottomRight.getColumn(), 
/*  548: 869 */                 topLeft.getRow(), cf6));
/*  549:     */             }
/*  550: 873 */             for (int i = topLeft.getRow() + 1; i < bottomRight.getRow(); i++)
/*  551:     */             {
/*  552: 876 */               CellXFRecord cf7 = new CellXFRecord(tlformat);
/*  553: 877 */               cf7.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
/*  554: 878 */               cf7.setBorder(Border.RIGHT, 
/*  555: 879 */                 tlformat.getBorderLine(Border.RIGHT), 
/*  556: 880 */                 tlformat.getBorderColour(Border.RIGHT));
/*  557:     */               
/*  558: 882 */               index = borderFormats.indexOf(cf7);
/*  559: 883 */               if (index != -1) {
/*  560: 885 */                 cf7 = (CellXFRecord)borderFormats.get(index);
/*  561:     */               } else {
/*  562: 889 */                 borderFormats.add(cf7);
/*  563:     */               }
/*  564: 892 */               this.sheet.addCell(new Blank(bottomRight.getColumn(), i, cf7));
/*  565:     */             }
/*  566: 896 */             for (int i = topLeft.getColumn() + 1; i < bottomRight.getColumn(); i++)
/*  567:     */             {
/*  568: 899 */               CellXFRecord cf8 = new CellXFRecord(tlformat);
/*  569: 900 */               cf8.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
/*  570: 901 */               cf8.setBorder(Border.TOP, 
/*  571: 902 */                 tlformat.getBorderLine(Border.TOP), 
/*  572: 903 */                 tlformat.getBorderColour(Border.TOP));
/*  573: 905 */               if (topLeft.getRow() == bottomRight.getRow()) {
/*  574: 907 */                 cf8.setBorder(Border.BOTTOM, 
/*  575: 908 */                   tlformat.getBorderLine(Border.BOTTOM), 
/*  576: 909 */                   tlformat.getBorderColour(Border.BOTTOM));
/*  577:     */               }
/*  578: 912 */               index = borderFormats.indexOf(cf8);
/*  579: 913 */               if (index != -1) {
/*  580: 915 */                 cf8 = (CellXFRecord)borderFormats.get(index);
/*  581:     */               } else {
/*  582: 919 */                 borderFormats.add(cf8);
/*  583:     */               }
/*  584: 922 */               this.sheet.addCell(new Blank(i, topLeft.getRow(), cf8));
/*  585:     */             }
/*  586:     */           }
/*  587: 927 */           if ((bottomRight.getColumn() > topLeft.getColumn()) || 
/*  588: 928 */             (bottomRight.getRow() > topLeft.getRow()))
/*  589:     */           {
/*  590: 931 */             CellXFRecord cf4 = new CellXFRecord(tlformat);
/*  591: 932 */             cf4.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
/*  592: 933 */             cf4.setBorder(Border.RIGHT, 
/*  593: 934 */               tlformat.getBorderLine(Border.RIGHT), 
/*  594: 935 */               tlformat.getBorderColour(Border.RIGHT));
/*  595: 936 */             cf4.setBorder(Border.BOTTOM, 
/*  596: 937 */               tlformat.getBorderLine(Border.BOTTOM), 
/*  597: 938 */               tlformat.getBorderColour(Border.BOTTOM));
/*  598: 940 */             if (bottomRight.getRow() == topLeft.getRow()) {
/*  599: 942 */               cf4.setBorder(Border.TOP, 
/*  600: 943 */                 tlformat.getBorderLine(Border.TOP), 
/*  601: 944 */                 tlformat.getBorderColour(Border.TOP));
/*  602:     */             }
/*  603: 947 */             if (bottomRight.getColumn() == topLeft.getColumn()) {
/*  604: 949 */               cf4.setBorder(Border.LEFT, 
/*  605: 950 */                 tlformat.getBorderLine(Border.LEFT), 
/*  606: 951 */                 tlformat.getBorderColour(Border.LEFT));
/*  607:     */             }
/*  608: 954 */             index = borderFormats.indexOf(cf4);
/*  609: 955 */             if (index != -1) {
/*  610: 957 */               cf4 = (CellXFRecord)borderFormats.get(index);
/*  611:     */             } else {
/*  612: 961 */               borderFormats.add(cf4);
/*  613:     */             }
/*  614: 964 */             this.sheet.addCell(new Blank(bottomRight.getColumn(), 
/*  615: 965 */               bottomRight.getRow(), cf4));
/*  616: 969 */             for (int i = topLeft.getColumn() + 1; i < bottomRight.getColumn(); i++)
/*  617:     */             {
/*  618: 972 */               CellXFRecord cf5 = new CellXFRecord(tlformat);
/*  619: 973 */               cf5.setBorder(Border.ALL, BorderLineStyle.NONE, Colour.BLACK);
/*  620: 974 */               cf5.setBorder(Border.BOTTOM, 
/*  621: 975 */                 tlformat.getBorderLine(Border.BOTTOM), 
/*  622: 976 */                 tlformat.getBorderColour(Border.BOTTOM));
/*  623: 978 */               if (topLeft.getRow() == bottomRight.getRow()) {
/*  624: 980 */                 cf5.setBorder(Border.TOP, 
/*  625: 981 */                   tlformat.getBorderLine(Border.TOP), 
/*  626: 982 */                   tlformat.getBorderColour(Border.TOP));
/*  627:     */               }
/*  628: 985 */               index = borderFormats.indexOf(cf5);
/*  629: 986 */               if (index != -1) {
/*  630: 988 */                 cf5 = (CellXFRecord)borderFormats.get(index);
/*  631:     */               } else {
/*  632: 992 */                 borderFormats.add(cf5);
/*  633:     */               }
/*  634: 995 */               this.sheet.addCell(new Blank(i, bottomRight.getRow(), cf5));
/*  635:     */             }
/*  636:     */           }
/*  637:     */         }
/*  638:     */         catch (WriteException e)
/*  639:     */         {
/*  640:1002 */           logger.warn(e.toString());
/*  641:     */         }
/*  642:     */       }
/*  643:     */     }
/*  644:     */   }
/*  645:     */   
/*  646:     */   private Cell[] getColumn(int col)
/*  647:     */   {
/*  648:1016 */     boolean found = false;
/*  649:1017 */     int row = this.numRows - 1;
/*  650:1019 */     while ((row >= 0) && (!found)) {
/*  651:1021 */       if ((this.rows[row] != null) && 
/*  652:1022 */         (this.rows[row].getCell(col) != null)) {
/*  653:1024 */         found = true;
/*  654:     */       } else {
/*  655:1028 */         row--;
/*  656:     */       }
/*  657:     */     }
/*  658:1033 */     Cell[] cells = new Cell[row + 1];
/*  659:1035 */     for (int i = 0; i <= row; i++) {
/*  660:1037 */       cells[i] = (this.rows[i] != null ? this.rows[i].getCell(col) : null);
/*  661:     */     }
/*  662:1040 */     return cells;
/*  663:     */   }
/*  664:     */   
/*  665:     */   void setChartOnly()
/*  666:     */   {
/*  667:1048 */     this.chartOnly = true;
/*  668:     */   }
/*  669:     */   
/*  670:     */   void setPLS(PLSRecord pls)
/*  671:     */   {
/*  672:1058 */     this.plsRecord = pls;
/*  673:     */   }
/*  674:     */   
/*  675:     */   void setButtonPropertySet(ButtonPropertySetRecord bps)
/*  676:     */   {
/*  677:1068 */     this.buttonPropertySet = bps;
/*  678:     */   }
/*  679:     */   
/*  680:     */   void setDataValidation(DataValidation dv, ArrayList vc)
/*  681:     */   {
/*  682:1079 */     this.dataValidation = dv;
/*  683:1080 */     this.validatedCells = vc;
/*  684:     */   }
/*  685:     */   
/*  686:     */   void setConditionalFormats(ArrayList cf)
/*  687:     */   {
/*  688:1090 */     this.conditionalFormats = cf;
/*  689:     */   }
/*  690:     */   
/*  691:     */   void setAutoFilter(AutoFilter af)
/*  692:     */   {
/*  693:1100 */     this.autoFilter = af;
/*  694:     */   }
/*  695:     */   
/*  696:     */   private void writeDataValidation()
/*  697:     */     throws IOException
/*  698:     */   {
/*  699:1108 */     if ((this.dataValidation != null) && (this.validatedCells.size() == 0))
/*  700:     */     {
/*  701:1113 */       this.dataValidation.write(this.outputFile);
/*  702:1114 */       return;
/*  703:     */     }
/*  704:1117 */     if ((this.dataValidation == null) && (this.validatedCells.size() > 0))
/*  705:     */     {
/*  706:1121 */       int comboBoxId = this.sheet.getComboBox() != null ? 
/*  707:1122 */         this.sheet.getComboBox().getObjectId() : -1;
/*  708:1123 */       this.dataValidation = new DataValidation(comboBoxId, 
/*  709:1124 */         this.sheet.getWorkbook(), 
/*  710:1125 */         this.sheet.getWorkbook(), 
/*  711:1126 */         this.workbookSettings);
/*  712:     */     }
/*  713:1129 */     for (Iterator i = this.validatedCells.iterator(); i.hasNext();)
/*  714:     */     {
/*  715:1131 */       CellValue cv = (CellValue)i.next();
/*  716:1132 */       CellFeatures cf = cv.getCellFeatures();
/*  717:1137 */       if (!cf.getDVParser().copied()) {
/*  718:1139 */         if (!cf.getDVParser().extendedCellsValidation())
/*  719:     */         {
/*  720:1142 */           DataValiditySettingsRecord dvsr = 
/*  721:1143 */             new DataValiditySettingsRecord(cf.getDVParser());
/*  722:1144 */           this.dataValidation.add(dvsr);
/*  723:     */         }
/*  724:1150 */         else if ((cv.getColumn() == cf.getDVParser().getFirstColumn()) && 
/*  725:1151 */           (cv.getRow() == cf.getDVParser().getFirstRow()))
/*  726:     */         {
/*  727:1153 */           DataValiditySettingsRecord dvsr = 
/*  728:1154 */             new DataValiditySettingsRecord(cf.getDVParser());
/*  729:1155 */           this.dataValidation.add(dvsr);
/*  730:     */         }
/*  731:     */       }
/*  732:     */     }
/*  733:1160 */     this.dataValidation.write(this.outputFile);
/*  734:     */   }
/*  735:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.SheetWriter
 * JD-Core Version:    0.7.0.1
 */