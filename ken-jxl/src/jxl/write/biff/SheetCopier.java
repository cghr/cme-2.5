/*    1:     */ package jxl.write.biff;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.Arrays;
/*    5:     */ import java.util.HashMap;
/*    6:     */ import java.util.TreeSet;
/*    7:     */ import jxl.BooleanCell;
/*    8:     */ import jxl.Cell;
/*    9:     */ import jxl.CellFeatures;
/*   10:     */ import jxl.CellType;
/*   11:     */ import jxl.CellView;
/*   12:     */ import jxl.DateCell;
/*   13:     */ import jxl.Hyperlink;
/*   14:     */ import jxl.LabelCell;
/*   15:     */ import jxl.NumberCell;
/*   16:     */ import jxl.Range;
/*   17:     */ import jxl.Sheet;
/*   18:     */ import jxl.WorkbookSettings;
/*   19:     */ import jxl.biff.AutoFilter;
/*   20:     */ import jxl.biff.CellReferenceHelper;
/*   21:     */ import jxl.biff.ConditionalFormat;
/*   22:     */ import jxl.biff.DataValidation;
/*   23:     */ import jxl.biff.FormattingRecords;
/*   24:     */ import jxl.biff.FormulaData;
/*   25:     */ import jxl.biff.NumFormatRecordsException;
/*   26:     */ import jxl.biff.SheetRangeImpl;
/*   27:     */ import jxl.biff.XFRecord;
/*   28:     */ import jxl.biff.drawing.Button;
/*   29:     */ import jxl.biff.drawing.Chart;
/*   30:     */ import jxl.biff.drawing.CheckBox;
/*   31:     */ import jxl.biff.drawing.ComboBox;
/*   32:     */ import jxl.biff.drawing.Comment;
/*   33:     */ import jxl.biff.drawing.Drawing;
/*   34:     */ import jxl.biff.drawing.DrawingGroupObject;
/*   35:     */ import jxl.biff.formula.FormulaException;
/*   36:     */ import jxl.common.Assert;
/*   37:     */ import jxl.common.Logger;
/*   38:     */ import jxl.format.CellFormat;
/*   39:     */ import jxl.read.biff.BOFRecord;
/*   40:     */ import jxl.read.biff.NameRecord;
/*   41:     */ import jxl.read.biff.NameRecord.NameRange;
/*   42:     */ import jxl.read.biff.SheetImpl;
/*   43:     */ import jxl.read.biff.WorkbookParser;
/*   44:     */ import jxl.write.Blank;
/*   45:     */ import jxl.write.Boolean;
/*   46:     */ import jxl.write.DateTime;
/*   47:     */ import jxl.write.Formula;
/*   48:     */ import jxl.write.Label;
/*   49:     */ import jxl.write.Number;
/*   50:     */ import jxl.write.WritableCell;
/*   51:     */ import jxl.write.WritableCellFeatures;
/*   52:     */ import jxl.write.WritableCellFormat;
/*   53:     */ import jxl.write.WritableHyperlink;
/*   54:     */ import jxl.write.WritableImage;
/*   55:     */ import jxl.write.WritableSheet;
/*   56:     */ import jxl.write.WritableWorkbook;
/*   57:     */ import jxl.write.WriteException;
/*   58:     */ 
/*   59:     */ class SheetCopier
/*   60:     */ {
/*   61:  87 */   private static Logger logger = Logger.getLogger(SheetCopier.class);
/*   62:     */   private SheetImpl fromSheet;
/*   63:     */   private WritableSheetImpl toSheet;
/*   64:     */   private WorkbookSettings workbookSettings;
/*   65:     */   private TreeSet columnFormats;
/*   66:     */   private FormattingRecords formatRecords;
/*   67:     */   private ArrayList hyperlinks;
/*   68:     */   private MergedCells mergedCells;
/*   69:     */   private ArrayList rowBreaks;
/*   70:     */   private ArrayList columnBreaks;
/*   71:     */   private SheetWriter sheetWriter;
/*   72:     */   private ArrayList drawings;
/*   73:     */   private ArrayList images;
/*   74:     */   private ArrayList conditionalFormats;
/*   75:     */   private ArrayList validatedCells;
/*   76:     */   private AutoFilter autoFilter;
/*   77:     */   private DataValidation dataValidation;
/*   78:     */   private ComboBox comboBox;
/*   79:     */   private PLSRecord plsRecord;
/*   80:     */   private boolean chartOnly;
/*   81:     */   private ButtonPropertySetRecord buttonPropertySet;
/*   82:     */   private int numRows;
/*   83:     */   private int maxRowOutlineLevel;
/*   84:     */   private int maxColumnOutlineLevel;
/*   85:     */   private HashMap xfRecords;
/*   86:     */   private HashMap fonts;
/*   87:     */   private HashMap formats;
/*   88:     */   
/*   89:     */   public SheetCopier(Sheet f, WritableSheet t)
/*   90:     */   {
/*   91: 122 */     this.fromSheet = ((SheetImpl)f);
/*   92: 123 */     this.toSheet = ((WritableSheetImpl)t);
/*   93: 124 */     this.workbookSettings = this.toSheet.getWorkbook().getSettings();
/*   94: 125 */     this.chartOnly = false;
/*   95:     */   }
/*   96:     */   
/*   97:     */   void setColumnFormats(TreeSet cf)
/*   98:     */   {
/*   99: 130 */     this.columnFormats = cf;
/*  100:     */   }
/*  101:     */   
/*  102:     */   void setFormatRecords(FormattingRecords fr)
/*  103:     */   {
/*  104: 135 */     this.formatRecords = fr;
/*  105:     */   }
/*  106:     */   
/*  107:     */   void setHyperlinks(ArrayList h)
/*  108:     */   {
/*  109: 140 */     this.hyperlinks = h;
/*  110:     */   }
/*  111:     */   
/*  112:     */   void setMergedCells(MergedCells mc)
/*  113:     */   {
/*  114: 145 */     this.mergedCells = mc;
/*  115:     */   }
/*  116:     */   
/*  117:     */   void setRowBreaks(ArrayList rb)
/*  118:     */   {
/*  119: 150 */     this.rowBreaks = rb;
/*  120:     */   }
/*  121:     */   
/*  122:     */   void setColumnBreaks(ArrayList cb)
/*  123:     */   {
/*  124: 155 */     this.columnBreaks = cb;
/*  125:     */   }
/*  126:     */   
/*  127:     */   void setSheetWriter(SheetWriter sw)
/*  128:     */   {
/*  129: 160 */     this.sheetWriter = sw;
/*  130:     */   }
/*  131:     */   
/*  132:     */   void setDrawings(ArrayList d)
/*  133:     */   {
/*  134: 165 */     this.drawings = d;
/*  135:     */   }
/*  136:     */   
/*  137:     */   void setImages(ArrayList i)
/*  138:     */   {
/*  139: 170 */     this.images = i;
/*  140:     */   }
/*  141:     */   
/*  142:     */   void setConditionalFormats(ArrayList cf)
/*  143:     */   {
/*  144: 175 */     this.conditionalFormats = cf;
/*  145:     */   }
/*  146:     */   
/*  147:     */   void setValidatedCells(ArrayList vc)
/*  148:     */   {
/*  149: 180 */     this.validatedCells = vc;
/*  150:     */   }
/*  151:     */   
/*  152:     */   AutoFilter getAutoFilter()
/*  153:     */   {
/*  154: 185 */     return this.autoFilter;
/*  155:     */   }
/*  156:     */   
/*  157:     */   DataValidation getDataValidation()
/*  158:     */   {
/*  159: 190 */     return this.dataValidation;
/*  160:     */   }
/*  161:     */   
/*  162:     */   ComboBox getComboBox()
/*  163:     */   {
/*  164: 195 */     return this.comboBox;
/*  165:     */   }
/*  166:     */   
/*  167:     */   PLSRecord getPLSRecord()
/*  168:     */   {
/*  169: 200 */     return this.plsRecord;
/*  170:     */   }
/*  171:     */   
/*  172:     */   boolean isChartOnly()
/*  173:     */   {
/*  174: 205 */     return this.chartOnly;
/*  175:     */   }
/*  176:     */   
/*  177:     */   ButtonPropertySetRecord getButtonPropertySet()
/*  178:     */   {
/*  179: 210 */     return this.buttonPropertySet;
/*  180:     */   }
/*  181:     */   
/*  182:     */   public void copySheet()
/*  183:     */   {
/*  184: 219 */     shallowCopyCells();
/*  185:     */     
/*  186:     */ 
/*  187: 222 */     jxl.read.biff.ColumnInfoRecord[] readCirs = this.fromSheet.getColumnInfos();
/*  188: 224 */     for (int i = 0; i < readCirs.length; i++)
/*  189:     */     {
/*  190: 226 */       jxl.read.biff.ColumnInfoRecord rcir = readCirs[i];
/*  191: 227 */       for (int j = rcir.getStartColumn(); j <= rcir.getEndColumn(); j++)
/*  192:     */       {
/*  193: 229 */         ColumnInfoRecord cir = new ColumnInfoRecord(rcir, j, 
/*  194: 230 */           this.formatRecords);
/*  195: 231 */         cir.setHidden(rcir.getHidden());
/*  196: 232 */         this.columnFormats.add(cir);
/*  197:     */       }
/*  198:     */     }
/*  199: 237 */     Hyperlink[] hls = this.fromSheet.getHyperlinks();
/*  200: 238 */     for (int i = 0; i < hls.length; i++)
/*  201:     */     {
/*  202: 240 */       WritableHyperlink hr = new WritableHyperlink(
/*  203: 241 */         hls[i], this.toSheet);
/*  204: 242 */       this.hyperlinks.add(hr);
/*  205:     */     }
/*  206: 246 */     Range[] merged = this.fromSheet.getMergedCells();
/*  207: 248 */     for (int i = 0; i < merged.length; i++) {
/*  208: 250 */       this.mergedCells.add(new SheetRangeImpl((SheetRangeImpl)merged[i], this.toSheet));
/*  209:     */     }
/*  210:     */     try
/*  211:     */     {
/*  212: 256 */       jxl.read.biff.RowRecord[] rowprops = this.fromSheet.getRowProperties();
/*  213: 258 */       for (int i = 0; i < rowprops.length; i++)
/*  214:     */       {
/*  215: 260 */         RowRecord rr = this.toSheet.getRowRecord(rowprops[i].getRowNumber());
/*  216: 261 */         XFRecord format = rowprops[i].hasDefaultFormat() ? 
/*  217: 262 */           this.formatRecords.getXFRecord(rowprops[i].getXFIndex()) : null;
/*  218: 263 */         rr.setRowDetails(rowprops[i].getRowHeight(), 
/*  219: 264 */           rowprops[i].matchesDefaultFontHeight(), 
/*  220: 265 */           rowprops[i].isCollapsed(), 
/*  221: 266 */           rowprops[i].getOutlineLevel(), 
/*  222: 267 */           rowprops[i].getGroupStart(), 
/*  223: 268 */           format);
/*  224: 269 */         this.numRows = Math.max(this.numRows, rowprops[i].getRowNumber() + 1);
/*  225:     */       }
/*  226:     */     }
/*  227:     */     catch (RowsExceededException e)
/*  228:     */     {
/*  229: 276 */       Assert.verify(false);
/*  230:     */     }
/*  231: 284 */     int[] rowbreaks = this.fromSheet.getRowPageBreaks();
/*  232: 286 */     if (rowbreaks != null) {
/*  233: 288 */       for (int i = 0; i < rowbreaks.length; i++) {
/*  234: 290 */         this.rowBreaks.add(new Integer(rowbreaks[i]));
/*  235:     */       }
/*  236:     */     }
/*  237: 294 */     int[] columnbreaks = this.fromSheet.getColumnPageBreaks();
/*  238: 296 */     if (columnbreaks != null) {
/*  239: 298 */       for (int i = 0; i < columnbreaks.length; i++) {
/*  240: 300 */         this.columnBreaks.add(new Integer(columnbreaks[i]));
/*  241:     */       }
/*  242:     */     }
/*  243: 305 */     this.sheetWriter.setCharts(this.fromSheet.getCharts());
/*  244:     */     
/*  245:     */ 
/*  246: 308 */     DrawingGroupObject[] dr = this.fromSheet.getDrawings();
/*  247: 309 */     for (int i = 0; i < dr.length; i++) {
/*  248: 311 */       if ((dr[i] instanceof Drawing))
/*  249:     */       {
/*  250: 313 */         WritableImage wi = new WritableImage(
/*  251: 314 */           dr[i], this.toSheet.getWorkbook().getDrawingGroup());
/*  252: 315 */         this.drawings.add(wi);
/*  253: 316 */         this.images.add(wi);
/*  254:     */       }
/*  255: 318 */       else if ((dr[i] instanceof Comment))
/*  256:     */       {
/*  257: 320 */         Comment c = 
/*  258: 321 */           new Comment(dr[i], 
/*  259: 322 */           this.toSheet.getWorkbook().getDrawingGroup(), 
/*  260: 323 */           this.workbookSettings);
/*  261: 324 */         this.drawings.add(c);
/*  262:     */         
/*  263:     */ 
/*  264: 327 */         CellValue cv = (CellValue)this.toSheet.getWritableCell(c.getColumn(), 
/*  265: 328 */           c.getRow());
/*  266: 329 */         Assert.verify(cv.getCellFeatures() != null);
/*  267: 330 */         cv.getWritableCellFeatures().setCommentDrawing(c);
/*  268:     */       }
/*  269: 332 */       else if ((dr[i] instanceof Button))
/*  270:     */       {
/*  271: 334 */         Button b = 
/*  272: 335 */           new Button(
/*  273: 336 */           dr[i], 
/*  274: 337 */           this.toSheet.getWorkbook().getDrawingGroup(), 
/*  275: 338 */           this.workbookSettings);
/*  276: 339 */         this.drawings.add(b);
/*  277:     */       }
/*  278: 341 */       else if ((dr[i] instanceof ComboBox))
/*  279:     */       {
/*  280: 343 */         ComboBox cb = 
/*  281: 344 */           new ComboBox(
/*  282: 345 */           dr[i], 
/*  283: 346 */           this.toSheet.getWorkbook().getDrawingGroup(), 
/*  284: 347 */           this.workbookSettings);
/*  285: 348 */         this.drawings.add(cb);
/*  286:     */       }
/*  287: 350 */       else if ((dr[i] instanceof CheckBox))
/*  288:     */       {
/*  289: 352 */         CheckBox cb = 
/*  290: 353 */           new CheckBox(
/*  291: 354 */           dr[i], 
/*  292: 355 */           this.toSheet.getWorkbook().getDrawingGroup(), 
/*  293: 356 */           this.workbookSettings);
/*  294: 357 */         this.drawings.add(cb);
/*  295:     */       }
/*  296:     */     }
/*  297: 363 */     DataValidation rdv = this.fromSheet.getDataValidation();
/*  298: 364 */     if (rdv != null)
/*  299:     */     {
/*  300: 366 */       this.dataValidation = new DataValidation(rdv, 
/*  301: 367 */         this.toSheet.getWorkbook(), 
/*  302: 368 */         this.toSheet.getWorkbook(), 
/*  303: 369 */         this.workbookSettings);
/*  304: 370 */       int objid = this.dataValidation.getComboBoxObjectId();
/*  305: 372 */       if (objid != 0) {
/*  306: 374 */         this.comboBox = ((ComboBox)this.drawings.get(objid));
/*  307:     */       }
/*  308:     */     }
/*  309: 379 */     ConditionalFormat[] cf = this.fromSheet.getConditionalFormats();
/*  310: 380 */     if (cf.length > 0) {
/*  311: 382 */       for (int i = 0; i < cf.length; i++) {
/*  312: 384 */         this.conditionalFormats.add(cf[i]);
/*  313:     */       }
/*  314:     */     }
/*  315: 389 */     this.autoFilter = this.fromSheet.getAutoFilter();
/*  316:     */     
/*  317:     */ 
/*  318: 392 */     this.sheetWriter.setWorkspaceOptions(this.fromSheet.getWorkspaceOptions());
/*  319: 395 */     if (this.fromSheet.getSheetBof().isChart())
/*  320:     */     {
/*  321: 397 */       this.chartOnly = true;
/*  322: 398 */       this.sheetWriter.setChartOnly();
/*  323:     */     }
/*  324: 402 */     if (this.fromSheet.getPLS() != null) {
/*  325: 404 */       if (this.fromSheet.getWorkbookBof().isBiff7()) {
/*  326: 406 */         logger.warn("Cannot copy Biff7 print settings record - ignoring");
/*  327:     */       } else {
/*  328: 410 */         this.plsRecord = new PLSRecord(this.fromSheet.getPLS());
/*  329:     */       }
/*  330:     */     }
/*  331: 415 */     if (this.fromSheet.getButtonPropertySet() != null) {
/*  332: 417 */       this.buttonPropertySet = new ButtonPropertySetRecord(
/*  333: 418 */         this.fromSheet.getButtonPropertySet());
/*  334:     */     }
/*  335: 422 */     this.maxRowOutlineLevel = this.fromSheet.getMaxRowOutlineLevel();
/*  336: 423 */     this.maxColumnOutlineLevel = this.fromSheet.getMaxColumnOutlineLevel();
/*  337:     */   }
/*  338:     */   
/*  339:     */   public void copyWritableSheet()
/*  340:     */   {
/*  341: 432 */     shallowCopyCells();
/*  342:     */   }
/*  343:     */   
/*  344:     */   public void importSheet()
/*  345:     */   {
/*  346: 538 */     this.xfRecords = new HashMap();
/*  347: 539 */     this.fonts = new HashMap();
/*  348: 540 */     this.formats = new HashMap();
/*  349:     */     
/*  350: 542 */     deepCopyCells();
/*  351:     */     
/*  352:     */ 
/*  353: 545 */     jxl.read.biff.ColumnInfoRecord[] readCirs = this.fromSheet.getColumnInfos();
/*  354: 547 */     for (int i = 0; i < readCirs.length; i++)
/*  355:     */     {
/*  356: 549 */       jxl.read.biff.ColumnInfoRecord rcir = readCirs[i];
/*  357: 550 */       for (int j = rcir.getStartColumn(); j <= rcir.getEndColumn(); j++)
/*  358:     */       {
/*  359: 552 */         ColumnInfoRecord cir = new ColumnInfoRecord(rcir, j);
/*  360: 553 */         int xfIndex = cir.getXfIndex();
/*  361: 554 */         XFRecord cf = (WritableCellFormat)this.xfRecords.get(new Integer(xfIndex));
/*  362: 556 */         if (cf == null)
/*  363:     */         {
/*  364: 558 */           CellFormat readFormat = this.fromSheet.getColumnView(j).getFormat();
/*  365: 559 */           WritableCellFormat localWritableCellFormat1 = copyCellFormat(readFormat);
/*  366:     */         }
/*  367: 562 */         cir.setCellFormat(cf);
/*  368: 563 */         cir.setHidden(rcir.getHidden());
/*  369: 564 */         this.columnFormats.add(cir);
/*  370:     */       }
/*  371:     */     }
/*  372: 569 */     Hyperlink[] hls = this.fromSheet.getHyperlinks();
/*  373: 570 */     for (int i = 0; i < hls.length; i++)
/*  374:     */     {
/*  375: 572 */       WritableHyperlink hr = new WritableHyperlink(
/*  376: 573 */         hls[i], this.toSheet);
/*  377: 574 */       this.hyperlinks.add(hr);
/*  378:     */     }
/*  379: 578 */     Range[] merged = this.fromSheet.getMergedCells();
/*  380: 580 */     for (int i = 0; i < merged.length; i++) {
/*  381: 582 */       this.mergedCells.add(new SheetRangeImpl((SheetRangeImpl)merged[i], this.toSheet));
/*  382:     */     }
/*  383:     */     try
/*  384:     */     {
/*  385: 588 */       jxl.read.biff.RowRecord[] rowprops = this.fromSheet.getRowProperties();
/*  386: 590 */       for (int i = 0; i < rowprops.length; i++)
/*  387:     */       {
/*  388: 592 */         RowRecord rr = this.toSheet.getRowRecord(rowprops[i].getRowNumber());
/*  389: 593 */         XFRecord format = null;
/*  390: 594 */         jxl.read.biff.RowRecord rowrec = rowprops[i];
/*  391: 595 */         if (rowrec.hasDefaultFormat())
/*  392:     */         {
/*  393: 597 */           format = 
/*  394: 598 */             (WritableCellFormat)this.xfRecords.get(new Integer(rowrec.getXFIndex()));
/*  395: 600 */           if (format == null)
/*  396:     */           {
/*  397: 602 */             int rownum = rowrec.getRowNumber();
/*  398: 603 */             CellFormat readFormat = this.fromSheet.getRowView(rownum).getFormat();
/*  399: 604 */             WritableCellFormat localWritableCellFormat2 = copyCellFormat(readFormat);
/*  400:     */           }
/*  401:     */         }
/*  402: 608 */         rr.setRowDetails(rowrec.getRowHeight(), 
/*  403: 609 */           rowrec.matchesDefaultFontHeight(), 
/*  404: 610 */           rowrec.isCollapsed(), 
/*  405: 611 */           rowrec.getOutlineLevel(), 
/*  406: 612 */           rowrec.getGroupStart(), 
/*  407: 613 */           format);
/*  408: 614 */         this.numRows = Math.max(this.numRows, rowprops[i].getRowNumber() + 1);
/*  409:     */       }
/*  410:     */     }
/*  411:     */     catch (RowsExceededException e)
/*  412:     */     {
/*  413: 621 */       Assert.verify(false);
/*  414:     */     }
/*  415: 629 */     int[] rowbreaks = this.fromSheet.getRowPageBreaks();
/*  416: 631 */     if (rowbreaks != null) {
/*  417: 633 */       for (int i = 0; i < rowbreaks.length; i++) {
/*  418: 635 */         this.rowBreaks.add(new Integer(rowbreaks[i]));
/*  419:     */       }
/*  420:     */     }
/*  421: 639 */     int[] columnbreaks = this.fromSheet.getColumnPageBreaks();
/*  422: 641 */     if (columnbreaks != null) {
/*  423: 643 */       for (int i = 0; i < columnbreaks.length; i++) {
/*  424: 645 */         this.columnBreaks.add(new Integer(columnbreaks[i]));
/*  425:     */       }
/*  426:     */     }
/*  427: 650 */     Chart[] fromCharts = this.fromSheet.getCharts();
/*  428: 651 */     if ((fromCharts != null) && (fromCharts.length > 0)) {
/*  429: 653 */       logger.warn("Importing of charts is not supported");
/*  430:     */     }
/*  431: 690 */     DrawingGroupObject[] dr = this.fromSheet.getDrawings();
/*  432: 694 */     if ((dr.length > 0) && 
/*  433: 695 */       (this.toSheet.getWorkbook().getDrawingGroup() == null)) {
/*  434: 697 */       this.toSheet.getWorkbook().createDrawingGroup();
/*  435:     */     }
/*  436: 700 */     for (int i = 0; i < dr.length; i++) {
/*  437: 702 */       if ((dr[i] instanceof Drawing))
/*  438:     */       {
/*  439: 704 */         WritableImage wi = new WritableImage(
/*  440: 705 */           dr[i].getX(), dr[i].getY(), 
/*  441: 706 */           dr[i].getWidth(), dr[i].getHeight(), 
/*  442: 707 */           dr[i].getImageData());
/*  443: 708 */         this.toSheet.getWorkbook().addDrawing(wi);
/*  444: 709 */         this.drawings.add(wi);
/*  445: 710 */         this.images.add(wi);
/*  446:     */       }
/*  447: 712 */       else if ((dr[i] instanceof Comment))
/*  448:     */       {
/*  449: 714 */         Comment c = 
/*  450: 715 */           new Comment(dr[i], 
/*  451: 716 */           this.toSheet.getWorkbook().getDrawingGroup(), 
/*  452: 717 */           this.workbookSettings);
/*  453: 718 */         this.drawings.add(c);
/*  454:     */         
/*  455:     */ 
/*  456: 721 */         CellValue cv = (CellValue)this.toSheet.getWritableCell(c.getColumn(), 
/*  457: 722 */           c.getRow());
/*  458: 723 */         Assert.verify(cv.getCellFeatures() != null);
/*  459: 724 */         cv.getWritableCellFeatures().setCommentDrawing(c);
/*  460:     */       }
/*  461: 726 */       else if ((dr[i] instanceof Button))
/*  462:     */       {
/*  463: 728 */         Button b = 
/*  464: 729 */           new Button(
/*  465: 730 */           dr[i], 
/*  466: 731 */           this.toSheet.getWorkbook().getDrawingGroup(), 
/*  467: 732 */           this.workbookSettings);
/*  468: 733 */         this.drawings.add(b);
/*  469:     */       }
/*  470: 735 */       else if ((dr[i] instanceof ComboBox))
/*  471:     */       {
/*  472: 737 */         ComboBox cb = 
/*  473: 738 */           new ComboBox(
/*  474: 739 */           dr[i], 
/*  475: 740 */           this.toSheet.getWorkbook().getDrawingGroup(), 
/*  476: 741 */           this.workbookSettings);
/*  477: 742 */         this.drawings.add(cb);
/*  478:     */       }
/*  479:     */     }
/*  480: 747 */     DataValidation rdv = this.fromSheet.getDataValidation();
/*  481: 748 */     if (rdv != null)
/*  482:     */     {
/*  483: 750 */       this.dataValidation = new DataValidation(rdv, 
/*  484: 751 */         this.toSheet.getWorkbook(), 
/*  485: 752 */         this.toSheet.getWorkbook(), 
/*  486: 753 */         this.workbookSettings);
/*  487: 754 */       int objid = this.dataValidation.getComboBoxObjectId();
/*  488: 755 */       if (objid != 0) {
/*  489: 757 */         this.comboBox = ((ComboBox)this.drawings.get(objid));
/*  490:     */       }
/*  491:     */     }
/*  492: 762 */     this.sheetWriter.setWorkspaceOptions(this.fromSheet.getWorkspaceOptions());
/*  493: 765 */     if (this.fromSheet.getSheetBof().isChart())
/*  494:     */     {
/*  495: 767 */       this.chartOnly = true;
/*  496: 768 */       this.sheetWriter.setChartOnly();
/*  497:     */     }
/*  498: 772 */     if (this.fromSheet.getPLS() != null) {
/*  499: 774 */       if (this.fromSheet.getWorkbookBof().isBiff7()) {
/*  500: 776 */         logger.warn("Cannot copy Biff7 print settings record - ignoring");
/*  501:     */       } else {
/*  502: 780 */         this.plsRecord = new PLSRecord(this.fromSheet.getPLS());
/*  503:     */       }
/*  504:     */     }
/*  505: 785 */     if (this.fromSheet.getButtonPropertySet() != null) {
/*  506: 787 */       this.buttonPropertySet = new ButtonPropertySetRecord(
/*  507: 788 */         this.fromSheet.getButtonPropertySet());
/*  508:     */     }
/*  509: 791 */     importNames();
/*  510:     */     
/*  511:     */ 
/*  512: 794 */     this.maxRowOutlineLevel = this.fromSheet.getMaxRowOutlineLevel();
/*  513: 795 */     this.maxColumnOutlineLevel = this.fromSheet.getMaxColumnOutlineLevel();
/*  514:     */   }
/*  515:     */   
/*  516:     */   private WritableCell shallowCopyCell(Cell cell)
/*  517:     */   {
/*  518: 803 */     CellType ct = cell.getType();
/*  519: 804 */     WritableCell newCell = null;
/*  520: 806 */     if (ct == CellType.LABEL) {
/*  521: 808 */       newCell = new Label((LabelCell)cell);
/*  522: 810 */     } else if (ct == CellType.NUMBER) {
/*  523: 812 */       newCell = new Number((NumberCell)cell);
/*  524: 814 */     } else if (ct == CellType.DATE) {
/*  525: 816 */       newCell = new DateTime((DateCell)cell);
/*  526: 818 */     } else if (ct == CellType.BOOLEAN) {
/*  527: 820 */       newCell = new Boolean((BooleanCell)cell);
/*  528: 822 */     } else if (ct == CellType.NUMBER_FORMULA) {
/*  529: 824 */       newCell = new ReadNumberFormulaRecord((FormulaData)cell);
/*  530: 826 */     } else if (ct == CellType.STRING_FORMULA) {
/*  531: 828 */       newCell = new ReadStringFormulaRecord((FormulaData)cell);
/*  532: 830 */     } else if (ct == CellType.BOOLEAN_FORMULA) {
/*  533: 832 */       newCell = new ReadBooleanFormulaRecord((FormulaData)cell);
/*  534: 834 */     } else if (ct == CellType.DATE_FORMULA) {
/*  535: 836 */       newCell = new ReadDateFormulaRecord((FormulaData)cell);
/*  536: 838 */     } else if (ct == CellType.FORMULA_ERROR) {
/*  537: 840 */       newCell = new ReadErrorFormulaRecord((FormulaData)cell);
/*  538: 842 */     } else if (ct == CellType.EMPTY) {
/*  539: 844 */       if (cell.getCellFormat() != null) {
/*  540: 849 */         newCell = new Blank(cell);
/*  541:     */       }
/*  542:     */     }
/*  543: 853 */     return newCell;
/*  544:     */   }
/*  545:     */   
/*  546:     */   private WritableCell deepCopyCell(Cell cell)
/*  547:     */   {
/*  548: 863 */     WritableCell c = shallowCopyCell(cell);
/*  549: 865 */     if (c == null) {
/*  550: 867 */       return c;
/*  551:     */     }
/*  552: 870 */     if ((c instanceof ReadFormulaRecord))
/*  553:     */     {
/*  554: 872 */       ReadFormulaRecord rfr = (ReadFormulaRecord)c;
/*  555: 873 */       boolean crossSheetReference = !rfr.handleImportedCellReferences(
/*  556: 874 */         this.fromSheet.getWorkbook(), 
/*  557: 875 */         this.fromSheet.getWorkbook(), 
/*  558: 876 */         this.workbookSettings);
/*  559: 878 */       if (crossSheetReference)
/*  560:     */       {
/*  561:     */         try
/*  562:     */         {
/*  563: 882 */           logger.warn("Formula " + rfr.getFormula() + 
/*  564: 883 */             " in cell " + 
/*  565: 884 */             CellReferenceHelper.getCellReference(cell.getColumn(), 
/*  566: 885 */             cell.getRow()) + 
/*  567: 886 */             " cannot be imported because it references another " + 
/*  568: 887 */             " sheet from the source workbook");
/*  569:     */         }
/*  570:     */         catch (FormulaException e)
/*  571:     */         {
/*  572: 891 */           logger.warn("Formula  in cell " + 
/*  573: 892 */             CellReferenceHelper.getCellReference(cell.getColumn(), 
/*  574: 893 */             cell.getRow()) + 
/*  575: 894 */             " cannot be imported:  " + e.getMessage());
/*  576:     */         }
/*  577: 898 */         c = new Formula(cell.getColumn(), cell.getRow(), "\"ERROR\"");
/*  578:     */       }
/*  579:     */     }
/*  580: 903 */     CellFormat cf = c.getCellFormat();
/*  581: 904 */     int index = ((XFRecord)cf).getXFIndex();
/*  582: 905 */     WritableCellFormat wcf = 
/*  583: 906 */       (WritableCellFormat)this.xfRecords.get(new Integer(index));
/*  584: 908 */     if (wcf == null) {
/*  585: 910 */       wcf = copyCellFormat(cf);
/*  586:     */     }
/*  587: 913 */     c.setCellFormat(wcf);
/*  588:     */     
/*  589: 915 */     return c;
/*  590:     */   }
/*  591:     */   
/*  592:     */   void shallowCopyCells()
/*  593:     */   {
/*  594: 924 */     int cells = this.fromSheet.getRows();
/*  595: 925 */     Cell[] row = (Cell[])null;
/*  596: 926 */     Cell cell = null;
/*  597: 927 */     for (int i = 0; i < cells; i++)
/*  598:     */     {
/*  599: 929 */       row = this.fromSheet.getRow(i);
/*  600: 931 */       for (int j = 0; j < row.length; j++)
/*  601:     */       {
/*  602: 933 */         cell = row[j];
/*  603: 934 */         WritableCell c = shallowCopyCell(cell);
/*  604:     */         try
/*  605:     */         {
/*  606: 943 */           if (c != null)
/*  607:     */           {
/*  608: 945 */             this.toSheet.addCell(c);
/*  609: 949 */             if ((c.getCellFeatures() != null) && 
/*  610: 950 */               (c.getCellFeatures().hasDataValidation())) {
/*  611: 952 */               this.validatedCells.add(c);
/*  612:     */             }
/*  613:     */           }
/*  614:     */         }
/*  615:     */         catch (WriteException e)
/*  616:     */         {
/*  617: 958 */           Assert.verify(false);
/*  618:     */         }
/*  619:     */       }
/*  620:     */     }
/*  621: 962 */     this.numRows = this.toSheet.getRows();
/*  622:     */   }
/*  623:     */   
/*  624:     */   void deepCopyCells()
/*  625:     */   {
/*  626: 971 */     int cells = this.fromSheet.getRows();
/*  627: 972 */     Cell[] row = (Cell[])null;
/*  628: 973 */     Cell cell = null;
/*  629: 974 */     for (int i = 0; i < cells; i++)
/*  630:     */     {
/*  631: 976 */       row = this.fromSheet.getRow(i);
/*  632: 978 */       for (int j = 0; j < row.length; j++)
/*  633:     */       {
/*  634: 980 */         cell = row[j];
/*  635: 981 */         WritableCell c = deepCopyCell(cell);
/*  636:     */         try
/*  637:     */         {
/*  638: 990 */           if (c != null)
/*  639:     */           {
/*  640: 992 */             this.toSheet.addCell(c);
/*  641: 997 */             if ((c.getCellFeatures() != null & c.getCellFeatures().hasDataValidation())) {
/*  642: 999 */               this.validatedCells.add(c);
/*  643:     */             }
/*  644:     */           }
/*  645:     */         }
/*  646:     */         catch (WriteException e)
/*  647:     */         {
/*  648:1005 */           Assert.verify(false);
/*  649:     */         }
/*  650:     */       }
/*  651:     */     }
/*  652:     */   }
/*  653:     */   
/*  654:     */   private WritableCellFormat copyCellFormat(CellFormat cf)
/*  655:     */   {
/*  656:     */     try
/*  657:     */     {
/*  658:1024 */       XFRecord xfr = (XFRecord)cf;
/*  659:1025 */       WritableCellFormat f = new WritableCellFormat(xfr);
/*  660:1026 */       this.formatRecords.addStyle(f);
/*  661:     */       
/*  662:     */ 
/*  663:1029 */       int xfIndex = xfr.getXFIndex();
/*  664:1030 */       this.xfRecords.put(new Integer(xfIndex), f);
/*  665:     */       
/*  666:1032 */       int fontIndex = xfr.getFontIndex();
/*  667:1033 */       this.fonts.put(new Integer(fontIndex), new Integer(f.getFontIndex()));
/*  668:     */       
/*  669:1035 */       int formatIndex = xfr.getFormatRecord();
/*  670:1036 */       this.formats.put(new Integer(formatIndex), new Integer(f.getFormatRecord()));
/*  671:     */       
/*  672:1038 */       return f;
/*  673:     */     }
/*  674:     */     catch (NumFormatRecordsException e)
/*  675:     */     {
/*  676:1042 */       logger.warn("Maximum number of format records exceeded.  Using default format.");
/*  677:     */     }
/*  678:1045 */     return WritableWorkbook.NORMAL_STYLE;
/*  679:     */   }
/*  680:     */   
/*  681:     */   private void importNames()
/*  682:     */   {
/*  683:1054 */     WorkbookParser fromWorkbook = this.fromSheet.getWorkbook();
/*  684:1055 */     WritableWorkbook toWorkbook = this.toSheet.getWorkbook();
/*  685:1056 */     int fromSheetIndex = fromWorkbook.getIndex(this.fromSheet);
/*  686:1057 */     NameRecord[] nameRecords = fromWorkbook.getNameRecords();
/*  687:1058 */     String[] names = toWorkbook.getRangeNames();
/*  688:1060 */     for (int i = 0; i < nameRecords.length; i++)
/*  689:     */     {
/*  690:1062 */       NameRecord.NameRange[] nameRanges = nameRecords[i].getRanges();
/*  691:1064 */       for (int j = 0; j < nameRanges.length; j++)
/*  692:     */       {
/*  693:1066 */         int nameSheetIndex = fromWorkbook.getExternalSheetIndex(
/*  694:1067 */           nameRanges[j].getExternalSheet());
/*  695:1069 */         if (fromSheetIndex == nameSheetIndex)
/*  696:     */         {
/*  697:1071 */           String name = nameRecords[i].getName();
/*  698:1072 */           if (Arrays.binarySearch(names, name) < 0) {
/*  699:1074 */             toWorkbook.addNameArea(name, 
/*  700:1075 */               this.toSheet, 
/*  701:1076 */               nameRanges[j].getFirstColumn(), 
/*  702:1077 */               nameRanges[j].getFirstRow(), 
/*  703:1078 */               nameRanges[j].getLastColumn(), 
/*  704:1079 */               nameRanges[j].getLastRow());
/*  705:     */           } else {
/*  706:1083 */             logger.warn("Named range " + name + 
/*  707:1084 */               " is already present in the destination workbook");
/*  708:     */           }
/*  709:     */         }
/*  710:     */       }
/*  711:     */     }
/*  712:     */   }
/*  713:     */   
/*  714:     */   int getRows()
/*  715:     */   {
/*  716:1100 */     return this.numRows;
/*  717:     */   }
/*  718:     */   
/*  719:     */   public int getMaxColumnOutlineLevel()
/*  720:     */   {
/*  721:1110 */     return this.maxColumnOutlineLevel;
/*  722:     */   }
/*  723:     */   
/*  724:     */   public int getMaxRowOutlineLevel()
/*  725:     */   {
/*  726:1120 */     return this.maxRowOutlineLevel;
/*  727:     */   }
/*  728:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.SheetCopier
 * JD-Core Version:    0.7.0.1
 */