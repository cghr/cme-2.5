/*    1:     */ package jxl.read.biff;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.HashMap;
/*    5:     */ import java.util.Iterator;
/*    6:     */ import jxl.Cell;
/*    7:     */ import jxl.CellFeatures;
/*    8:     */ import jxl.CellReferenceHelper;
/*    9:     */ import jxl.CellType;
/*   10:     */ import jxl.DateCell;
/*   11:     */ import jxl.HeaderFooter;
/*   12:     */ import jxl.Range;
/*   13:     */ import jxl.SheetSettings;
/*   14:     */ import jxl.WorkbookSettings;
/*   15:     */ import jxl.biff.AutoFilter;
/*   16:     */ import jxl.biff.AutoFilterInfoRecord;
/*   17:     */ import jxl.biff.AutoFilterRecord;
/*   18:     */ import jxl.biff.ConditionalFormat;
/*   19:     */ import jxl.biff.ConditionalFormatRangeRecord;
/*   20:     */ import jxl.biff.ConditionalFormatRecord;
/*   21:     */ import jxl.biff.ContinueRecord;
/*   22:     */ import jxl.biff.DataValidation;
/*   23:     */ import jxl.biff.DataValidityListRecord;
/*   24:     */ import jxl.biff.DataValiditySettingsRecord;
/*   25:     */ import jxl.biff.FilterModeRecord;
/*   26:     */ import jxl.biff.FormattingRecords;
/*   27:     */ import jxl.biff.Type;
/*   28:     */ import jxl.biff.WorkspaceInformationRecord;
/*   29:     */ import jxl.biff.drawing.Button;
/*   30:     */ import jxl.biff.drawing.Chart;
/*   31:     */ import jxl.biff.drawing.CheckBox;
/*   32:     */ import jxl.biff.drawing.ComboBox;
/*   33:     */ import jxl.biff.drawing.Comment;
/*   34:     */ import jxl.biff.drawing.Drawing;
/*   35:     */ import jxl.biff.drawing.Drawing2;
/*   36:     */ import jxl.biff.drawing.DrawingData;
/*   37:     */ import jxl.biff.drawing.DrawingDataException;
/*   38:     */ import jxl.biff.drawing.DrawingGroup;
/*   39:     */ import jxl.biff.drawing.MsoDrawingRecord;
/*   40:     */ import jxl.biff.drawing.NoteRecord;
/*   41:     */ import jxl.biff.drawing.ObjRecord;
/*   42:     */ import jxl.biff.drawing.TextObjectRecord;
/*   43:     */ import jxl.biff.formula.FormulaException;
/*   44:     */ import jxl.common.Assert;
/*   45:     */ import jxl.common.Logger;
/*   46:     */ import jxl.format.PageOrder;
/*   47:     */ import jxl.format.PageOrientation;
/*   48:     */ import jxl.format.PaperSize;
/*   49:     */ 
/*   50:     */ final class SheetReader
/*   51:     */ {
/*   52:  80 */   private static Logger logger = Logger.getLogger(SheetReader.class);
/*   53:     */   private File excelFile;
/*   54:     */   private SSTRecord sharedStrings;
/*   55:     */   private BOFRecord sheetBof;
/*   56:     */   private BOFRecord workbookBof;
/*   57:     */   private FormattingRecords formattingRecords;
/*   58:     */   private int numRows;
/*   59:     */   private int numCols;
/*   60:     */   private Cell[][] cells;
/*   61:     */   private ArrayList outOfBoundsCells;
/*   62:     */   private int startPosition;
/*   63:     */   private ArrayList rowProperties;
/*   64:     */   private ArrayList columnInfosArray;
/*   65:     */   private ArrayList sharedFormulas;
/*   66:     */   private ArrayList hyperlinks;
/*   67:     */   private ArrayList conditionalFormats;
/*   68:     */   private AutoFilter autoFilter;
/*   69:     */   private Range[] mergedCells;
/*   70:     */   private DataValidation dataValidation;
/*   71:     */   private ArrayList charts;
/*   72:     */   private ArrayList drawings;
/*   73:     */   private DrawingData drawingData;
/*   74:     */   private boolean nineteenFour;
/*   75:     */   private PLSRecord plsRecord;
/*   76:     */   private ButtonPropertySetRecord buttonPropertySet;
/*   77:     */   private WorkspaceInformationRecord workspaceOptions;
/*   78:     */   private int[] rowBreaks;
/*   79:     */   private int[] columnBreaks;
/*   80:     */   private int maxRowOutlineLevel;
/*   81:     */   private int maxColumnOutlineLevel;
/*   82:     */   private SheetSettings settings;
/*   83:     */   private WorkbookSettings workbookSettings;
/*   84:     */   private WorkbookParser workbook;
/*   85:     */   private SheetImpl sheet;
/*   86:     */   
/*   87:     */   SheetReader(File f, SSTRecord sst, FormattingRecords fr, BOFRecord sb, BOFRecord wb, boolean nf, WorkbookParser wp, int sp, SheetImpl sh)
/*   88:     */   {
/*   89: 273 */     this.excelFile = f;
/*   90: 274 */     this.sharedStrings = sst;
/*   91: 275 */     this.formattingRecords = fr;
/*   92: 276 */     this.sheetBof = sb;
/*   93: 277 */     this.workbookBof = wb;
/*   94: 278 */     this.columnInfosArray = new ArrayList();
/*   95: 279 */     this.sharedFormulas = new ArrayList();
/*   96: 280 */     this.hyperlinks = new ArrayList();
/*   97: 281 */     this.conditionalFormats = new ArrayList();
/*   98: 282 */     this.rowProperties = new ArrayList(10);
/*   99: 283 */     this.charts = new ArrayList();
/*  100: 284 */     this.drawings = new ArrayList();
/*  101: 285 */     this.outOfBoundsCells = new ArrayList();
/*  102: 286 */     this.nineteenFour = nf;
/*  103: 287 */     this.workbook = wp;
/*  104: 288 */     this.startPosition = sp;
/*  105: 289 */     this.sheet = sh;
/*  106: 290 */     this.settings = new SheetSettings(sh);
/*  107: 291 */     this.workbookSettings = this.workbook.getSettings();
/*  108:     */   }
/*  109:     */   
/*  110:     */   private void addCell(Cell cell)
/*  111:     */   {
/*  112: 303 */     if ((cell.getRow() < this.numRows) && (cell.getColumn() < this.numCols))
/*  113:     */     {
/*  114: 305 */       if (this.cells[cell.getRow()][cell.getColumn()] != null)
/*  115:     */       {
/*  116: 307 */         StringBuffer sb = new StringBuffer();
/*  117: 308 */         CellReferenceHelper.getCellReference(
/*  118: 309 */           cell.getColumn(), cell.getRow(), sb);
/*  119: 310 */         logger.warn("Cell " + sb.toString() + 
/*  120: 311 */           " already contains data");
/*  121:     */       }
/*  122: 313 */       this.cells[cell.getRow()][cell.getColumn()] = cell;
/*  123:     */     }
/*  124:     */     else
/*  125:     */     {
/*  126: 317 */       this.outOfBoundsCells.add(cell);
/*  127:     */     }
/*  128:     */   }
/*  129:     */   
/*  130:     */   final void read()
/*  131:     */   {
/*  132: 333 */     Record r = null;
/*  133: 334 */     BaseSharedFormulaRecord sharedFormula = null;
/*  134: 335 */     boolean sharedFormulaAdded = false;
/*  135:     */     
/*  136: 337 */     boolean cont = true;
/*  137:     */     
/*  138:     */ 
/*  139: 340 */     this.excelFile.setPos(this.startPosition);
/*  140:     */     
/*  141:     */ 
/*  142: 343 */     MsoDrawingRecord msoRecord = null;
/*  143: 344 */     ObjRecord objRecord = null;
/*  144: 345 */     boolean firstMsoRecord = true;
/*  145:     */     
/*  146:     */ 
/*  147: 348 */     ConditionalFormat condFormat = null;
/*  148:     */     
/*  149:     */ 
/*  150: 351 */     FilterModeRecord filterMode = null;
/*  151: 352 */     AutoFilterInfoRecord autoFilterInfo = null;
/*  152:     */     
/*  153:     */ 
/*  154: 355 */     Window2Record window2Record = null;
/*  155:     */     
/*  156:     */ 
/*  157: 358 */     PrintGridLinesRecord printGridLinesRecord = null;
/*  158:     */     
/*  159:     */ 
/*  160: 361 */     PrintHeadersRecord printHeadersRecord = null;
/*  161:     */     
/*  162:     */ 
/*  163:     */ 
/*  164: 365 */     HashMap comments = new HashMap();
/*  165:     */     
/*  166:     */ 
/*  167: 368 */     ArrayList objectIds = new ArrayList();
/*  168:     */     
/*  169:     */ 
/*  170: 371 */     ContinueRecord continueRecord = null;
/*  171: 373 */     while (cont)
/*  172:     */     {
/*  173: 375 */       r = this.excelFile.next();
/*  174: 376 */       Type type = r.getType();
/*  175: 378 */       if ((type == Type.UNKNOWN) && (r.getCode() == 0))
/*  176:     */       {
/*  177: 380 */         logger.warn("Biff code zero found");
/*  178: 383 */         if (r.getLength() == 10)
/*  179:     */         {
/*  180: 385 */           logger.warn("Biff code zero found - trying a dimension record.");
/*  181: 386 */           r.setType(Type.DIMENSION);
/*  182:     */         }
/*  183:     */         else
/*  184:     */         {
/*  185: 390 */           logger.warn("Biff code zero found - Ignoring.");
/*  186:     */         }
/*  187:     */       }
/*  188: 394 */       if (type == Type.DIMENSION)
/*  189:     */       {
/*  190: 396 */         DimensionRecord dr = null;
/*  191: 398 */         if (this.workbookBof.isBiff8()) {
/*  192: 400 */           dr = new DimensionRecord(r);
/*  193:     */         } else {
/*  194: 404 */           dr = new DimensionRecord(r, DimensionRecord.biff7);
/*  195:     */         }
/*  196: 406 */         this.numRows = dr.getNumberOfRows();
/*  197: 407 */         this.numCols = dr.getNumberOfColumns();
/*  198: 408 */         this.cells = new Cell[this.numRows][this.numCols];
/*  199:     */       }
/*  200: 410 */       else if (type == Type.LABELSST)
/*  201:     */       {
/*  202: 412 */         LabelSSTRecord label = new LabelSSTRecord(r, 
/*  203: 413 */           this.sharedStrings, 
/*  204: 414 */           this.formattingRecords, 
/*  205: 415 */           this.sheet);
/*  206: 416 */         addCell(label);
/*  207:     */       }
/*  208: 418 */       else if ((type == Type.RK) || (type == Type.RK2))
/*  209:     */       {
/*  210: 420 */         RKRecord rkr = new RKRecord(r, this.formattingRecords, this.sheet);
/*  211: 422 */         if (this.formattingRecords.isDate(rkr.getXFIndex()))
/*  212:     */         {
/*  213: 424 */           DateCell dc = new DateRecord(
/*  214: 425 */             rkr, rkr.getXFIndex(), this.formattingRecords, this.nineteenFour, this.sheet);
/*  215: 426 */           addCell(dc);
/*  216:     */         }
/*  217:     */         else
/*  218:     */         {
/*  219: 430 */           addCell(rkr);
/*  220:     */         }
/*  221:     */       }
/*  222: 433 */       else if (type == Type.HLINK)
/*  223:     */       {
/*  224: 435 */         HyperlinkRecord hr = new HyperlinkRecord(r, this.sheet, this.workbookSettings);
/*  225: 436 */         this.hyperlinks.add(hr);
/*  226:     */       }
/*  227: 438 */       else if (type == Type.MERGEDCELLS)
/*  228:     */       {
/*  229: 440 */         MergedCellsRecord mc = new MergedCellsRecord(r, this.sheet);
/*  230: 441 */         if (this.mergedCells == null)
/*  231:     */         {
/*  232: 443 */           this.mergedCells = mc.getRanges();
/*  233:     */         }
/*  234:     */         else
/*  235:     */         {
/*  236: 447 */           Range[] newMergedCells = 
/*  237: 448 */             new Range[this.mergedCells.length + mc.getRanges().length];
/*  238: 449 */           System.arraycopy(this.mergedCells, 0, newMergedCells, 0, 
/*  239: 450 */             this.mergedCells.length);
/*  240: 451 */           System.arraycopy(mc.getRanges(), 
/*  241: 452 */             0, 
/*  242: 453 */             newMergedCells, this.mergedCells.length, 
/*  243: 454 */             mc.getRanges().length);
/*  244: 455 */           this.mergedCells = newMergedCells;
/*  245:     */         }
/*  246:     */       }
/*  247: 458 */       else if (type == Type.MULRK)
/*  248:     */       {
/*  249: 460 */         MulRKRecord mulrk = new MulRKRecord(r);
/*  250:     */         
/*  251:     */ 
/*  252: 463 */         int num = mulrk.getNumberOfColumns();
/*  253: 464 */         int ixf = 0;
/*  254: 465 */         for (int i = 0; i < num; i++)
/*  255:     */         {
/*  256: 467 */           ixf = mulrk.getXFIndex(i);
/*  257:     */           
/*  258: 469 */           NumberValue nv = new NumberValue(
/*  259: 470 */             mulrk.getRow(), 
/*  260: 471 */             mulrk.getFirstColumn() + i, 
/*  261: 472 */             RKHelper.getDouble(mulrk.getRKNumber(i)), 
/*  262: 473 */             ixf, 
/*  263: 474 */             this.formattingRecords, 
/*  264: 475 */             this.sheet);
/*  265: 478 */           if (this.formattingRecords.isDate(ixf))
/*  266:     */           {
/*  267: 480 */             DateCell dc = new DateRecord(nv, 
/*  268: 481 */               ixf, 
/*  269: 482 */               this.formattingRecords, 
/*  270: 483 */               this.nineteenFour, 
/*  271: 484 */               this.sheet);
/*  272: 485 */             addCell(dc);
/*  273:     */           }
/*  274:     */           else
/*  275:     */           {
/*  276: 489 */             nv.setNumberFormat(this.formattingRecords.getNumberFormat(ixf));
/*  277: 490 */             addCell(nv);
/*  278:     */           }
/*  279:     */         }
/*  280:     */       }
/*  281: 494 */       else if (type == Type.NUMBER)
/*  282:     */       {
/*  283: 496 */         NumberRecord nr = new NumberRecord(r, this.formattingRecords, this.sheet);
/*  284: 498 */         if (this.formattingRecords.isDate(nr.getXFIndex()))
/*  285:     */         {
/*  286: 500 */           DateCell dc = new DateRecord(nr, 
/*  287: 501 */             nr.getXFIndex(), 
/*  288: 502 */             this.formattingRecords, 
/*  289: 503 */             this.nineteenFour, this.sheet);
/*  290: 504 */           addCell(dc);
/*  291:     */         }
/*  292:     */         else
/*  293:     */         {
/*  294: 508 */           addCell(nr);
/*  295:     */         }
/*  296:     */       }
/*  297: 511 */       else if (type == Type.BOOLERR)
/*  298:     */       {
/*  299: 513 */         BooleanRecord br = new BooleanRecord(r, this.formattingRecords, this.sheet);
/*  300: 515 */         if (br.isError())
/*  301:     */         {
/*  302: 517 */           ErrorRecord er = new ErrorRecord(br.getRecord(), this.formattingRecords, 
/*  303: 518 */             this.sheet);
/*  304: 519 */           addCell(er);
/*  305:     */         }
/*  306:     */         else
/*  307:     */         {
/*  308: 523 */           addCell(br);
/*  309:     */         }
/*  310:     */       }
/*  311: 526 */       else if (type == Type.PRINTGRIDLINES)
/*  312:     */       {
/*  313: 528 */         printGridLinesRecord = new PrintGridLinesRecord(r);
/*  314: 529 */         this.settings.setPrintGridLines(printGridLinesRecord.getPrintGridLines());
/*  315:     */       }
/*  316: 531 */       else if (type == Type.PRINTHEADERS)
/*  317:     */       {
/*  318: 533 */         printHeadersRecord = new PrintHeadersRecord(r);
/*  319: 534 */         this.settings.setPrintHeaders(printHeadersRecord.getPrintHeaders());
/*  320:     */       }
/*  321: 536 */       else if (type == Type.WINDOW2)
/*  322:     */       {
/*  323: 538 */         window2Record = null;
/*  324: 540 */         if (this.workbookBof.isBiff8()) {
/*  325: 542 */           window2Record = new Window2Record(r);
/*  326:     */         } else {
/*  327: 546 */           window2Record = new Window2Record(r, Window2Record.biff7);
/*  328:     */         }
/*  329: 549 */         this.settings.setShowGridLines(window2Record.getShowGridLines());
/*  330: 550 */         this.settings.setDisplayZeroValues(window2Record.getDisplayZeroValues());
/*  331: 551 */         this.settings.setSelected(true);
/*  332: 552 */         this.settings.setPageBreakPreviewMode(window2Record.isPageBreakPreview());
/*  333:     */       }
/*  334: 554 */       else if (type == Type.PANE)
/*  335:     */       {
/*  336: 556 */         PaneRecord pr = new PaneRecord(r);
/*  337: 558 */         if ((window2Record != null) && 
/*  338: 559 */           (window2Record.getFrozen()))
/*  339:     */         {
/*  340: 561 */           this.settings.setVerticalFreeze(pr.getRowsVisible());
/*  341: 562 */           this.settings.setHorizontalFreeze(pr.getColumnsVisible());
/*  342:     */         }
/*  343:     */       }
/*  344: 565 */       else if (type == Type.CONTINUE)
/*  345:     */       {
/*  346: 568 */         continueRecord = new ContinueRecord(r);
/*  347:     */       }
/*  348: 570 */       else if (type == Type.NOTE)
/*  349:     */       {
/*  350: 572 */         if (!this.workbookSettings.getDrawingsDisabled())
/*  351:     */         {
/*  352: 574 */           NoteRecord nr = new NoteRecord(r);
/*  353:     */           
/*  354:     */ 
/*  355: 577 */           Comment comment = (Comment)comments.remove(
/*  356: 578 */             new Integer(nr.getObjectId()));
/*  357: 580 */           if (comment == null)
/*  358:     */           {
/*  359: 582 */             logger.warn(" cannot find comment for note id " + 
/*  360: 583 */               nr.getObjectId() + "...ignoring");
/*  361:     */           }
/*  362:     */           else
/*  363:     */           {
/*  364: 587 */             comment.setNote(nr);
/*  365:     */             
/*  366: 589 */             this.drawings.add(comment);
/*  367:     */             
/*  368: 591 */             addCellComment(comment.getColumn(), 
/*  369: 592 */               comment.getRow(), 
/*  370: 593 */               comment.getText(), 
/*  371: 594 */               comment.getWidth(), 
/*  372: 595 */               comment.getHeight());
/*  373:     */           }
/*  374:     */         }
/*  375:     */       }
/*  376: 599 */       else if (type != Type.ARRAY)
/*  377:     */       {
/*  378: 603 */         if (type == Type.PROTECT)
/*  379:     */         {
/*  380: 605 */           ProtectRecord pr = new ProtectRecord(r);
/*  381: 606 */           this.settings.setProtected(pr.isProtected());
/*  382:     */         }
/*  383: 608 */         else if (type == Type.SHAREDFORMULA)
/*  384:     */         {
/*  385: 610 */           if (sharedFormula == null)
/*  386:     */           {
/*  387: 612 */             logger.warn("Shared template formula is null - trying most recent formula template");
/*  388:     */             
/*  389: 614 */             SharedFormulaRecord lastSharedFormula = 
/*  390: 615 */               (SharedFormulaRecord)this.sharedFormulas.get(this.sharedFormulas.size() - 1);
/*  391: 617 */             if (lastSharedFormula != null) {
/*  392: 619 */               sharedFormula = lastSharedFormula.getTemplateFormula();
/*  393:     */             }
/*  394:     */           }
/*  395: 623 */           SharedFormulaRecord sfr = new SharedFormulaRecord(
/*  396: 624 */             r, sharedFormula, this.workbook, this.workbook, this.sheet);
/*  397: 625 */           this.sharedFormulas.add(sfr);
/*  398: 626 */           sharedFormula = null;
/*  399:     */         }
/*  400: 628 */         else if ((type == Type.FORMULA) || (type == Type.FORMULA2))
/*  401:     */         {
/*  402: 630 */           FormulaRecord fr = new FormulaRecord(r, 
/*  403: 631 */             this.excelFile, 
/*  404: 632 */             this.formattingRecords, 
/*  405: 633 */             this.workbook, 
/*  406: 634 */             this.workbook, 
/*  407: 635 */             this.sheet, 
/*  408: 636 */             this.workbookSettings);
/*  409: 638 */           if (fr.isShared())
/*  410:     */           {
/*  411: 640 */             BaseSharedFormulaRecord prevSharedFormula = sharedFormula;
/*  412: 641 */             sharedFormula = (BaseSharedFormulaRecord)fr.getFormula();
/*  413:     */             
/*  414:     */ 
/*  415: 644 */             sharedFormulaAdded = addToSharedFormulas(sharedFormula);
/*  416: 646 */             if (sharedFormulaAdded) {
/*  417: 648 */               sharedFormula = prevSharedFormula;
/*  418:     */             }
/*  419: 653 */             if ((!sharedFormulaAdded) && (prevSharedFormula != null)) {
/*  420: 660 */               addCell(revertSharedFormula(prevSharedFormula));
/*  421:     */             }
/*  422:     */           }
/*  423:     */           else
/*  424:     */           {
/*  425: 665 */             Cell cell = fr.getFormula();
/*  426:     */             try
/*  427:     */             {
/*  428: 669 */               if (fr.getFormula().getType() == CellType.NUMBER_FORMULA)
/*  429:     */               {
/*  430: 671 */                 NumberFormulaRecord nfr = (NumberFormulaRecord)fr.getFormula();
/*  431: 672 */                 if (this.formattingRecords.isDate(nfr.getXFIndex())) {
/*  432: 674 */                   cell = new DateFormulaRecord(nfr, 
/*  433: 675 */                     this.formattingRecords, 
/*  434: 676 */                     this.workbook, 
/*  435: 677 */                     this.workbook, 
/*  436: 678 */                     this.nineteenFour, 
/*  437: 679 */                     this.sheet);
/*  438:     */                 }
/*  439:     */               }
/*  440: 683 */               addCell(cell);
/*  441:     */             }
/*  442:     */             catch (FormulaException e)
/*  443:     */             {
/*  444: 689 */               logger.warn(
/*  445:     */               
/*  446: 691 */                 CellReferenceHelper.getCellReference(cell.getColumn(), cell.getRow()) + " " + e.getMessage());
/*  447:     */             }
/*  448:     */           }
/*  449:     */         }
/*  450: 695 */         else if (type == Type.LABEL)
/*  451:     */         {
/*  452: 697 */           LabelRecord lr = null;
/*  453: 699 */           if (this.workbookBof.isBiff8()) {
/*  454: 701 */             lr = new LabelRecord(r, this.formattingRecords, this.sheet, this.workbookSettings);
/*  455:     */           } else {
/*  456: 705 */             lr = new LabelRecord(r, this.formattingRecords, this.sheet, this.workbookSettings, 
/*  457: 706 */               LabelRecord.biff7);
/*  458:     */           }
/*  459: 708 */           addCell(lr);
/*  460:     */         }
/*  461: 710 */         else if (type == Type.RSTRING)
/*  462:     */         {
/*  463: 712 */           RStringRecord lr = null;
/*  464:     */           
/*  465:     */ 
/*  466: 715 */           Assert.verify(!this.workbookBof.isBiff8());
/*  467: 716 */           lr = new RStringRecord(r, this.formattingRecords, 
/*  468: 717 */             this.sheet, this.workbookSettings, 
/*  469: 718 */             RStringRecord.biff7);
/*  470: 719 */           addCell(lr);
/*  471:     */         }
/*  472: 721 */         else if (type != Type.NAME)
/*  473:     */         {
/*  474: 725 */           if (type == Type.PASSWORD)
/*  475:     */           {
/*  476: 727 */             PasswordRecord pr = new PasswordRecord(r);
/*  477: 728 */             this.settings.setPasswordHash(pr.getPasswordHash());
/*  478:     */           }
/*  479: 730 */           else if (type == Type.ROW)
/*  480:     */           {
/*  481: 732 */             RowRecord rr = new RowRecord(r);
/*  482: 735 */             if ((!rr.isDefaultHeight()) || 
/*  483: 736 */               (!rr.matchesDefaultFontHeight()) || 
/*  484: 737 */               (rr.isCollapsed()) || 
/*  485: 738 */               (rr.hasDefaultFormat()) || 
/*  486: 739 */               (rr.getOutlineLevel() != 0)) {
/*  487: 741 */               this.rowProperties.add(rr);
/*  488:     */             }
/*  489:     */           }
/*  490: 744 */           else if (type == Type.BLANK)
/*  491:     */           {
/*  492: 746 */             if (!this.workbookSettings.getIgnoreBlanks())
/*  493:     */             {
/*  494: 748 */               BlankCell bc = new BlankCell(r, this.formattingRecords, this.sheet);
/*  495: 749 */               addCell(bc);
/*  496:     */             }
/*  497:     */           }
/*  498: 752 */           else if (type == Type.MULBLANK)
/*  499:     */           {
/*  500: 754 */             if (!this.workbookSettings.getIgnoreBlanks())
/*  501:     */             {
/*  502: 756 */               MulBlankRecord mulblank = new MulBlankRecord(r);
/*  503:     */               
/*  504:     */ 
/*  505: 759 */               int num = mulblank.getNumberOfColumns();
/*  506: 761 */               for (int i = 0; i < num; i++)
/*  507:     */               {
/*  508: 763 */                 int ixf = mulblank.getXFIndex(i);
/*  509:     */                 
/*  510: 765 */                 MulBlankCell mbc = new MulBlankCell(
/*  511: 766 */                   mulblank.getRow(), 
/*  512: 767 */                   mulblank.getFirstColumn() + i, 
/*  513: 768 */                   ixf, 
/*  514: 769 */                   this.formattingRecords, 
/*  515: 770 */                   this.sheet);
/*  516:     */                 
/*  517: 772 */                 addCell(mbc);
/*  518:     */               }
/*  519:     */             }
/*  520:     */           }
/*  521: 776 */           else if (type == Type.SCL)
/*  522:     */           {
/*  523: 778 */             SCLRecord scl = new SCLRecord(r);
/*  524: 779 */             this.settings.setZoomFactor(scl.getZoomFactor());
/*  525:     */           }
/*  526: 781 */           else if (type == Type.COLINFO)
/*  527:     */           {
/*  528: 783 */             ColumnInfoRecord cir = new ColumnInfoRecord(r);
/*  529: 784 */             this.columnInfosArray.add(cir);
/*  530:     */           }
/*  531: 786 */           else if (type == Type.HEADER)
/*  532:     */           {
/*  533: 788 */             HeaderRecord hr = null;
/*  534: 789 */             if (this.workbookBof.isBiff8()) {
/*  535: 791 */               hr = new HeaderRecord(r, this.workbookSettings);
/*  536:     */             } else {
/*  537: 795 */               hr = new HeaderRecord(r, this.workbookSettings, HeaderRecord.biff7);
/*  538:     */             }
/*  539: 798 */             HeaderFooter header = new HeaderFooter(hr.getHeader());
/*  540: 799 */             this.settings.setHeader(header);
/*  541:     */           }
/*  542: 801 */           else if (type == Type.FOOTER)
/*  543:     */           {
/*  544: 803 */             FooterRecord fr = null;
/*  545: 804 */             if (this.workbookBof.isBiff8()) {
/*  546: 806 */               fr = new FooterRecord(r, this.workbookSettings);
/*  547:     */             } else {
/*  548: 810 */               fr = new FooterRecord(r, this.workbookSettings, FooterRecord.biff7);
/*  549:     */             }
/*  550: 813 */             HeaderFooter footer = new HeaderFooter(fr.getFooter());
/*  551: 814 */             this.settings.setFooter(footer);
/*  552:     */           }
/*  553: 816 */           else if (type == Type.SETUP)
/*  554:     */           {
/*  555: 818 */             SetupRecord sr = new SetupRecord(r);
/*  556: 822 */             if (sr.getInitialized())
/*  557:     */             {
/*  558: 824 */               if (sr.isPortrait()) {
/*  559: 826 */                 this.settings.setOrientation(PageOrientation.PORTRAIT);
/*  560:     */               } else {
/*  561: 830 */                 this.settings.setOrientation(PageOrientation.LANDSCAPE);
/*  562:     */               }
/*  563: 832 */               if (sr.isRightDown()) {
/*  564: 834 */                 this.settings.setPageOrder(PageOrder.RIGHT_THEN_DOWN);
/*  565:     */               } else {
/*  566: 838 */                 this.settings.setPageOrder(PageOrder.DOWN_THEN_RIGHT);
/*  567:     */               }
/*  568: 840 */               this.settings.setPaperSize(PaperSize.getPaperSize(sr.getPaperSize()));
/*  569: 841 */               this.settings.setHeaderMargin(sr.getHeaderMargin());
/*  570: 842 */               this.settings.setFooterMargin(sr.getFooterMargin());
/*  571: 843 */               this.settings.setScaleFactor(sr.getScaleFactor());
/*  572: 844 */               this.settings.setPageStart(sr.getPageStart());
/*  573: 845 */               this.settings.setFitWidth(sr.getFitWidth());
/*  574: 846 */               this.settings.setFitHeight(sr.getFitHeight());
/*  575: 847 */               this.settings.setHorizontalPrintResolution(
/*  576: 848 */                 sr.getHorizontalPrintResolution());
/*  577: 849 */               this.settings.setVerticalPrintResolution(sr.getVerticalPrintResolution());
/*  578: 850 */               this.settings.setCopies(sr.getCopies());
/*  579: 852 */               if (this.workspaceOptions != null) {
/*  580: 854 */                 this.settings.setFitToPages(this.workspaceOptions.getFitToPages());
/*  581:     */               }
/*  582:     */             }
/*  583:     */           }
/*  584: 858 */           else if (type == Type.WSBOOL)
/*  585:     */           {
/*  586: 860 */             this.workspaceOptions = new WorkspaceInformationRecord(r);
/*  587:     */           }
/*  588: 862 */           else if (type == Type.DEFCOLWIDTH)
/*  589:     */           {
/*  590: 864 */             DefaultColumnWidthRecord dcwr = new DefaultColumnWidthRecord(r);
/*  591: 865 */             this.settings.setDefaultColumnWidth(dcwr.getWidth());
/*  592:     */           }
/*  593: 867 */           else if (type == Type.DEFAULTROWHEIGHT)
/*  594:     */           {
/*  595: 869 */             DefaultRowHeightRecord drhr = new DefaultRowHeightRecord(r);
/*  596: 870 */             if (drhr.getHeight() != 0) {
/*  597: 872 */               this.settings.setDefaultRowHeight(drhr.getHeight());
/*  598:     */             }
/*  599:     */           }
/*  600: 875 */           else if (type == Type.CONDFMT)
/*  601:     */           {
/*  602: 877 */             ConditionalFormatRangeRecord cfrr = 
/*  603: 878 */               new ConditionalFormatRangeRecord(r);
/*  604: 879 */             condFormat = new ConditionalFormat(cfrr);
/*  605: 880 */             this.conditionalFormats.add(condFormat);
/*  606:     */           }
/*  607: 882 */           else if (type == Type.CF)
/*  608:     */           {
/*  609: 884 */             ConditionalFormatRecord cfr = new ConditionalFormatRecord(r);
/*  610: 885 */             condFormat.addCondition(cfr);
/*  611:     */           }
/*  612: 887 */           else if (type == Type.FILTERMODE)
/*  613:     */           {
/*  614: 889 */             filterMode = new FilterModeRecord(r);
/*  615:     */           }
/*  616: 891 */           else if (type == Type.AUTOFILTERINFO)
/*  617:     */           {
/*  618: 893 */             autoFilterInfo = new AutoFilterInfoRecord(r);
/*  619:     */           }
/*  620: 895 */           else if (type == Type.AUTOFILTER)
/*  621:     */           {
/*  622: 897 */             if (!this.workbookSettings.getAutoFilterDisabled())
/*  623:     */             {
/*  624: 899 */               AutoFilterRecord af = new AutoFilterRecord(r);
/*  625: 901 */               if (this.autoFilter == null)
/*  626:     */               {
/*  627: 903 */                 this.autoFilter = new AutoFilter(filterMode, autoFilterInfo);
/*  628: 904 */                 filterMode = null;
/*  629: 905 */                 autoFilterInfo = null;
/*  630:     */               }
/*  631: 908 */               this.autoFilter.add(af);
/*  632:     */             }
/*  633:     */           }
/*  634: 911 */           else if (type == Type.LEFTMARGIN)
/*  635:     */           {
/*  636: 913 */             MarginRecord m = new LeftMarginRecord(r);
/*  637: 914 */             this.settings.setLeftMargin(m.getMargin());
/*  638:     */           }
/*  639: 916 */           else if (type == Type.RIGHTMARGIN)
/*  640:     */           {
/*  641: 918 */             MarginRecord m = new RightMarginRecord(r);
/*  642: 919 */             this.settings.setRightMargin(m.getMargin());
/*  643:     */           }
/*  644: 921 */           else if (type == Type.TOPMARGIN)
/*  645:     */           {
/*  646: 923 */             MarginRecord m = new TopMarginRecord(r);
/*  647: 924 */             this.settings.setTopMargin(m.getMargin());
/*  648:     */           }
/*  649: 926 */           else if (type == Type.BOTTOMMARGIN)
/*  650:     */           {
/*  651: 928 */             MarginRecord m = new BottomMarginRecord(r);
/*  652: 929 */             this.settings.setBottomMargin(m.getMargin());
/*  653:     */           }
/*  654: 931 */           else if (type == Type.HORIZONTALPAGEBREAKS)
/*  655:     */           {
/*  656: 933 */             HorizontalPageBreaksRecord dr = null;
/*  657: 935 */             if (this.workbookBof.isBiff8()) {
/*  658: 937 */               dr = new HorizontalPageBreaksRecord(r);
/*  659:     */             } else {
/*  660: 941 */               dr = new HorizontalPageBreaksRecord(
/*  661: 942 */                 r, HorizontalPageBreaksRecord.biff7);
/*  662:     */             }
/*  663: 944 */             this.rowBreaks = dr.getRowBreaks();
/*  664:     */           }
/*  665: 946 */           else if (type == Type.VERTICALPAGEBREAKS)
/*  666:     */           {
/*  667: 948 */             VerticalPageBreaksRecord dr = null;
/*  668: 950 */             if (this.workbookBof.isBiff8()) {
/*  669: 952 */               dr = new VerticalPageBreaksRecord(r);
/*  670:     */             } else {
/*  671: 956 */               dr = new VerticalPageBreaksRecord(
/*  672: 957 */                 r, VerticalPageBreaksRecord.biff7);
/*  673:     */             }
/*  674: 959 */             this.columnBreaks = dr.getColumnBreaks();
/*  675:     */           }
/*  676: 961 */           else if (type == Type.PLS)
/*  677:     */           {
/*  678: 963 */             this.plsRecord = new PLSRecord(r);
/*  679: 966 */             while (this.excelFile.peek().getType() == Type.CONTINUE) {
/*  680: 968 */               r.addContinueRecord(this.excelFile.next());
/*  681:     */             }
/*  682:     */           }
/*  683: 971 */           else if (type == Type.DVAL)
/*  684:     */           {
/*  685: 973 */             if (!this.workbookSettings.getCellValidationDisabled())
/*  686:     */             {
/*  687: 975 */               DataValidityListRecord dvlr = new DataValidityListRecord(r);
/*  688: 976 */               if (dvlr.getObjectId() == -1)
/*  689:     */               {
/*  690: 978 */                 if ((msoRecord != null) && (objRecord == null))
/*  691:     */                 {
/*  692: 981 */                   if (this.drawingData == null) {
/*  693: 983 */                     this.drawingData = new DrawingData();
/*  694:     */                   }
/*  695: 986 */                   Drawing2 d2 = new Drawing2(msoRecord, this.drawingData, 
/*  696: 987 */                     this.workbook.getDrawingGroup());
/*  697: 988 */                   this.drawings.add(d2);
/*  698: 989 */                   msoRecord = null;
/*  699:     */                   
/*  700: 991 */                   this.dataValidation = new DataValidation(dvlr);
/*  701:     */                 }
/*  702:     */                 else
/*  703:     */                 {
/*  704: 996 */                   this.dataValidation = new DataValidation(dvlr);
/*  705:     */                 }
/*  706:     */               }
/*  707: 999 */               else if (objectIds.contains(new Integer(dvlr.getObjectId()))) {
/*  708:1001 */                 this.dataValidation = new DataValidation(dvlr);
/*  709:     */               } else {
/*  710:1005 */                 logger.warn("object id " + dvlr.getObjectId() + " referenced " + 
/*  711:1006 */                   " by data validity list record not found - ignoring");
/*  712:     */               }
/*  713:     */             }
/*  714:     */           }
/*  715:1010 */           else if (type == Type.HCENTER)
/*  716:     */           {
/*  717:1012 */             CentreRecord hr = new CentreRecord(r);
/*  718:1013 */             this.settings.setHorizontalCentre(hr.isCentre());
/*  719:     */           }
/*  720:1015 */           else if (type == Type.VCENTER)
/*  721:     */           {
/*  722:1017 */             CentreRecord vc = new CentreRecord(r);
/*  723:1018 */             this.settings.setVerticalCentre(vc.isCentre());
/*  724:     */           }
/*  725:1020 */           else if (type == Type.DV)
/*  726:     */           {
/*  727:1022 */             if (!this.workbookSettings.getCellValidationDisabled())
/*  728:     */             {
/*  729:1024 */               DataValiditySettingsRecord dvsr = 
/*  730:1025 */                 new DataValiditySettingsRecord(r, 
/*  731:1026 */                 this.workbook, 
/*  732:1027 */                 this.workbook, 
/*  733:1028 */                 this.workbook.getSettings());
/*  734:1029 */               if (this.dataValidation != null)
/*  735:     */               {
/*  736:1031 */                 this.dataValidation.add(dvsr);
/*  737:1032 */                 addCellValidation(dvsr.getFirstColumn(), 
/*  738:1033 */                   dvsr.getFirstRow(), 
/*  739:1034 */                   dvsr.getLastColumn(), 
/*  740:1035 */                   dvsr.getLastRow(), 
/*  741:1036 */                   dvsr);
/*  742:     */               }
/*  743:     */               else
/*  744:     */               {
/*  745:1040 */                 logger.warn("cannot add data validity settings");
/*  746:     */               }
/*  747:     */             }
/*  748:     */           }
/*  749:1044 */           else if (type == Type.OBJ)
/*  750:     */           {
/*  751:1046 */             objRecord = new ObjRecord(r);
/*  752:1048 */             if (!this.workbookSettings.getDrawingsDisabled())
/*  753:     */             {
/*  754:1053 */               if ((msoRecord == null) && (continueRecord != null))
/*  755:     */               {
/*  756:1055 */                 logger.warn("Cannot find drawing record - using continue record");
/*  757:1056 */                 msoRecord = new MsoDrawingRecord(continueRecord.getRecord());
/*  758:1057 */                 continueRecord = null;
/*  759:     */               }
/*  760:1059 */               handleObjectRecord(objRecord, msoRecord, comments);
/*  761:1060 */               objectIds.add(new Integer(objRecord.getObjectId()));
/*  762:     */             }
/*  763:1064 */             if (objRecord.getType() != ObjRecord.CHART)
/*  764:     */             {
/*  765:1066 */               objRecord = null;
/*  766:1067 */               msoRecord = null;
/*  767:     */             }
/*  768:     */           }
/*  769:1070 */           else if (type == Type.MSODRAWING)
/*  770:     */           {
/*  771:1072 */             if (!this.workbookSettings.getDrawingsDisabled())
/*  772:     */             {
/*  773:1074 */               if (msoRecord != null) {
/*  774:1078 */                 this.drawingData.addRawData(msoRecord.getData());
/*  775:     */               }
/*  776:1080 */               msoRecord = new MsoDrawingRecord(r);
/*  777:1082 */               if (firstMsoRecord)
/*  778:     */               {
/*  779:1084 */                 msoRecord.setFirst();
/*  780:1085 */                 firstMsoRecord = false;
/*  781:     */               }
/*  782:     */             }
/*  783:     */           }
/*  784:1089 */           else if (type == Type.BUTTONPROPERTYSET)
/*  785:     */           {
/*  786:1091 */             this.buttonPropertySet = new ButtonPropertySetRecord(r);
/*  787:     */           }
/*  788:1093 */           else if (type == Type.CALCMODE)
/*  789:     */           {
/*  790:1095 */             CalcModeRecord cmr = new CalcModeRecord(r);
/*  791:1096 */             this.settings.setAutomaticFormulaCalculation(cmr.isAutomatic());
/*  792:     */           }
/*  793:1098 */           else if (type == Type.SAVERECALC)
/*  794:     */           {
/*  795:1100 */             SaveRecalcRecord cmr = new SaveRecalcRecord(r);
/*  796:1101 */             this.settings.setRecalculateFormulasBeforeSave(cmr.getRecalculateOnSave());
/*  797:     */           }
/*  798:1103 */           else if (type == Type.GUTS)
/*  799:     */           {
/*  800:1105 */             GuttersRecord gr = new GuttersRecord(r);
/*  801:1106 */             this.maxRowOutlineLevel = 
/*  802:1107 */               (gr.getRowOutlineLevel() > 0 ? gr.getRowOutlineLevel() - 1 : 0);
/*  803:1108 */             this.maxColumnOutlineLevel = 
/*  804:1109 */               (gr.getColumnOutlineLevel() > 0 ? gr.getRowOutlineLevel() - 1 : 0);
/*  805:     */           }
/*  806:1111 */           else if (type == Type.BOF)
/*  807:     */           {
/*  808:1113 */             BOFRecord br = new BOFRecord(r);
/*  809:1114 */             Assert.verify(!br.isWorksheet());
/*  810:     */             
/*  811:1116 */             int startpos = this.excelFile.getPos() - r.getLength() - 4;
/*  812:     */             
/*  813:     */ 
/*  814:     */ 
/*  815:1120 */             Record r2 = this.excelFile.next();
/*  816:1121 */             while (r2.getCode() != Type.EOF.value) {
/*  817:1123 */               r2 = this.excelFile.next();
/*  818:     */             }
/*  819:1126 */             if (br.isChart())
/*  820:     */             {
/*  821:1128 */               if (!this.workbook.getWorkbookBof().isBiff8())
/*  822:     */               {
/*  823:1130 */                 logger.warn("only biff8 charts are supported");
/*  824:     */               }
/*  825:     */               else
/*  826:     */               {
/*  827:1134 */                 if (this.drawingData == null) {
/*  828:1136 */                   this.drawingData = new DrawingData();
/*  829:     */                 }
/*  830:1139 */                 if (!this.workbookSettings.getDrawingsDisabled())
/*  831:     */                 {
/*  832:1141 */                   Chart chart = new Chart(msoRecord, objRecord, this.drawingData, 
/*  833:1142 */                     startpos, this.excelFile.getPos(), 
/*  834:1143 */                     this.excelFile, this.workbookSettings);
/*  835:1144 */                   this.charts.add(chart);
/*  836:1146 */                   if (this.workbook.getDrawingGroup() != null) {
/*  837:1148 */                     this.workbook.getDrawingGroup().add(chart);
/*  838:     */                   }
/*  839:     */                 }
/*  840:     */               }
/*  841:1154 */               msoRecord = null;
/*  842:1155 */               objRecord = null;
/*  843:     */             }
/*  844:1160 */             if (this.sheetBof.isChart()) {
/*  845:1162 */               cont = false;
/*  846:     */             }
/*  847:     */           }
/*  848:1165 */           else if (type == Type.EOF)
/*  849:     */           {
/*  850:1167 */             cont = false;
/*  851:     */           }
/*  852:     */         }
/*  853:     */       }
/*  854:     */     }
/*  855:1172 */     this.excelFile.restorePos();
/*  856:1175 */     if (this.outOfBoundsCells.size() > 0) {
/*  857:1177 */       handleOutOfBoundsCells();
/*  858:     */     }
/*  859:1181 */     Iterator i = this.sharedFormulas.iterator();
/*  860:     */     Cell[] sfnr;
/*  861:     */     int sf;
/*  862:1183 */     for (; i.hasNext(); sf < sfnr.length)
/*  863:     */     {
/*  864:1185 */       SharedFormulaRecord sfr = (SharedFormulaRecord)i.next();
/*  865:     */       
/*  866:1187 */       sfnr = sfr.getFormulas(this.formattingRecords, this.nineteenFour);
/*  867:     */       
/*  868:1189 */       sf = 0; continue;
/*  869:     */       
/*  870:1191 */       addCell(sfnr[sf]);sf++;
/*  871:     */     }
/*  872:1197 */     if ((!sharedFormulaAdded) && (sharedFormula != null)) {
/*  873:1199 */       addCell(revertSharedFormula(sharedFormula));
/*  874:     */     }
/*  875:1204 */     if ((msoRecord != null) && (this.workbook.getDrawingGroup() != null)) {
/*  876:1206 */       this.workbook.getDrawingGroup().setDrawingsOmitted(msoRecord, objRecord);
/*  877:     */     }
/*  878:1210 */     if (!comments.isEmpty()) {
/*  879:1212 */       logger.warn("Not all comments have a corresponding Note record");
/*  880:     */     }
/*  881:     */   }
/*  882:     */   
/*  883:     */   private boolean addToSharedFormulas(BaseSharedFormulaRecord fr)
/*  884:     */   {
/*  885:1225 */     boolean added = false;
/*  886:1226 */     SharedFormulaRecord sfr = null;
/*  887:     */     
/*  888:1228 */     int i = 0;
/*  889:1228 */     for (int size = this.sharedFormulas.size(); (i < size) && (!added); i++)
/*  890:     */     {
/*  891:1230 */       sfr = (SharedFormulaRecord)this.sharedFormulas.get(i);
/*  892:1231 */       added = sfr.add(fr);
/*  893:     */     }
/*  894:1234 */     return added;
/*  895:     */   }
/*  896:     */   
/*  897:     */   private Cell revertSharedFormula(BaseSharedFormulaRecord f)
/*  898:     */   {
/*  899:1250 */     int pos = this.excelFile.getPos();
/*  900:1251 */     this.excelFile.setPos(f.getFilePos());
/*  901:     */     
/*  902:1253 */     FormulaRecord fr = new FormulaRecord(f.getRecord(), 
/*  903:1254 */       this.excelFile, 
/*  904:1255 */       this.formattingRecords, 
/*  905:1256 */       this.workbook, 
/*  906:1257 */       this.workbook, 
/*  907:1258 */       FormulaRecord.ignoreSharedFormula, 
/*  908:1259 */       this.sheet, 
/*  909:1260 */       this.workbookSettings);
/*  910:     */     try
/*  911:     */     {
/*  912:1264 */       Cell cell = fr.getFormula();
/*  913:1267 */       if (fr.getFormula().getType() == CellType.NUMBER_FORMULA)
/*  914:     */       {
/*  915:1269 */         NumberFormulaRecord nfr = (NumberFormulaRecord)fr.getFormula();
/*  916:1270 */         if (this.formattingRecords.isDate(fr.getXFIndex())) {
/*  917:1272 */           cell = new DateFormulaRecord(nfr, 
/*  918:1273 */             this.formattingRecords, 
/*  919:1274 */             this.workbook, 
/*  920:1275 */             this.workbook, 
/*  921:1276 */             this.nineteenFour, 
/*  922:1277 */             this.sheet);
/*  923:     */         }
/*  924:     */       }
/*  925:1281 */       this.excelFile.setPos(pos);
/*  926:1282 */       return cell;
/*  927:     */     }
/*  928:     */     catch (FormulaException e)
/*  929:     */     {
/*  930:1288 */       logger.warn(
/*  931:1289 */         CellReferenceHelper.getCellReference(fr.getColumn(), fr.getRow()) + 
/*  932:1290 */         " " + e.getMessage());
/*  933:     */     }
/*  934:1292 */     return null;
/*  935:     */   }
/*  936:     */   
/*  937:     */   final int getNumRows()
/*  938:     */   {
/*  939:1304 */     return this.numRows;
/*  940:     */   }
/*  941:     */   
/*  942:     */   final int getNumCols()
/*  943:     */   {
/*  944:1314 */     return this.numCols;
/*  945:     */   }
/*  946:     */   
/*  947:     */   final Cell[][] getCells()
/*  948:     */   {
/*  949:1324 */     return this.cells;
/*  950:     */   }
/*  951:     */   
/*  952:     */   final ArrayList getRowProperties()
/*  953:     */   {
/*  954:1334 */     return this.rowProperties;
/*  955:     */   }
/*  956:     */   
/*  957:     */   final ArrayList getColumnInfosArray()
/*  958:     */   {
/*  959:1344 */     return this.columnInfosArray;
/*  960:     */   }
/*  961:     */   
/*  962:     */   final ArrayList getHyperlinks()
/*  963:     */   {
/*  964:1354 */     return this.hyperlinks;
/*  965:     */   }
/*  966:     */   
/*  967:     */   final ArrayList getConditionalFormats()
/*  968:     */   {
/*  969:1364 */     return this.conditionalFormats;
/*  970:     */   }
/*  971:     */   
/*  972:     */   final AutoFilter getAutoFilter()
/*  973:     */   {
/*  974:1374 */     return this.autoFilter;
/*  975:     */   }
/*  976:     */   
/*  977:     */   final ArrayList getCharts()
/*  978:     */   {
/*  979:1384 */     return this.charts;
/*  980:     */   }
/*  981:     */   
/*  982:     */   final ArrayList getDrawings()
/*  983:     */   {
/*  984:1394 */     return this.drawings;
/*  985:     */   }
/*  986:     */   
/*  987:     */   final DataValidation getDataValidation()
/*  988:     */   {
/*  989:1404 */     return this.dataValidation;
/*  990:     */   }
/*  991:     */   
/*  992:     */   final Range[] getMergedCells()
/*  993:     */   {
/*  994:1414 */     return this.mergedCells;
/*  995:     */   }
/*  996:     */   
/*  997:     */   final SheetSettings getSettings()
/*  998:     */   {
/*  999:1424 */     return this.settings;
/* 1000:     */   }
/* 1001:     */   
/* 1002:     */   final int[] getRowBreaks()
/* 1003:     */   {
/* 1004:1434 */     return this.rowBreaks;
/* 1005:     */   }
/* 1006:     */   
/* 1007:     */   final int[] getColumnBreaks()
/* 1008:     */   {
/* 1009:1444 */     return this.columnBreaks;
/* 1010:     */   }
/* 1011:     */   
/* 1012:     */   final WorkspaceInformationRecord getWorkspaceOptions()
/* 1013:     */   {
/* 1014:1454 */     return this.workspaceOptions;
/* 1015:     */   }
/* 1016:     */   
/* 1017:     */   final PLSRecord getPLS()
/* 1018:     */   {
/* 1019:1464 */     return this.plsRecord;
/* 1020:     */   }
/* 1021:     */   
/* 1022:     */   final ButtonPropertySetRecord getButtonPropertySet()
/* 1023:     */   {
/* 1024:1474 */     return this.buttonPropertySet;
/* 1025:     */   }
/* 1026:     */   
/* 1027:     */   private void addCellComment(int col, int row, String text, double width, double height)
/* 1028:     */   {
/* 1029:1492 */     Cell c = this.cells[row][col];
/* 1030:1493 */     if (c == null)
/* 1031:     */     {
/* 1032:1495 */       logger.warn("Cell at " + CellReferenceHelper.getCellReference(col, row) + 
/* 1033:1496 */         " not present - adding a blank");
/* 1034:1497 */       MulBlankCell mbc = new MulBlankCell(row, 
/* 1035:1498 */         col, 
/* 1036:1499 */         0, 
/* 1037:1500 */         this.formattingRecords, 
/* 1038:1501 */         this.sheet);
/* 1039:1502 */       CellFeatures cf = new CellFeatures();
/* 1040:1503 */       cf.setReadComment(text, width, height);
/* 1041:1504 */       mbc.setCellFeatures(cf);
/* 1042:1505 */       addCell(mbc);
/* 1043:     */       
/* 1044:1507 */       return;
/* 1045:     */     }
/* 1046:1510 */     if ((c instanceof CellFeaturesAccessor))
/* 1047:     */     {
/* 1048:1512 */       CellFeaturesAccessor cv = (CellFeaturesAccessor)c;
/* 1049:1513 */       CellFeatures cf = cv.getCellFeatures();
/* 1050:1515 */       if (cf == null)
/* 1051:     */       {
/* 1052:1517 */         cf = new CellFeatures();
/* 1053:1518 */         cv.setCellFeatures(cf);
/* 1054:     */       }
/* 1055:1521 */       cf.setReadComment(text, width, height);
/* 1056:     */     }
/* 1057:     */     else
/* 1058:     */     {
/* 1059:1525 */       logger.warn("Not able to add comment to cell type " + 
/* 1060:1526 */         c.getClass().getName() + 
/* 1061:1527 */         " at " + CellReferenceHelper.getCellReference(col, row));
/* 1062:     */     }
/* 1063:     */   }
/* 1064:     */   
/* 1065:     */   private void addCellValidation(int col1, int row1, int col2, int row2, DataValiditySettingsRecord dvsr)
/* 1066:     */   {
/* 1067:1546 */     for (int row = row1; row <= row2; row++) {
/* 1068:1548 */       for (int col = col1; col <= col2; col++)
/* 1069:     */       {
/* 1070:1550 */         Cell c = null;
/* 1071:1552 */         if ((this.cells.length > row) && (this.cells[row].length > col)) {
/* 1072:1554 */           c = this.cells[row][col];
/* 1073:     */         }
/* 1074:1557 */         if (c == null)
/* 1075:     */         {
/* 1076:1559 */           MulBlankCell mbc = new MulBlankCell(row, 
/* 1077:1560 */             col, 
/* 1078:1561 */             0, 
/* 1079:1562 */             this.formattingRecords, 
/* 1080:1563 */             this.sheet);
/* 1081:1564 */           CellFeatures cf = new CellFeatures();
/* 1082:1565 */           cf.setValidationSettings(dvsr);
/* 1083:1566 */           mbc.setCellFeatures(cf);
/* 1084:1567 */           addCell(mbc);
/* 1085:     */         }
/* 1086:1569 */         else if ((c instanceof CellFeaturesAccessor))
/* 1087:     */         {
/* 1088:1571 */           CellFeaturesAccessor cv = (CellFeaturesAccessor)c;
/* 1089:1572 */           CellFeatures cf = cv.getCellFeatures();
/* 1090:1574 */           if (cf == null)
/* 1091:     */           {
/* 1092:1576 */             cf = new CellFeatures();
/* 1093:1577 */             cv.setCellFeatures(cf);
/* 1094:     */           }
/* 1095:1580 */           cf.setValidationSettings(dvsr);
/* 1096:     */         }
/* 1097:     */         else
/* 1098:     */         {
/* 1099:1584 */           logger.warn("Not able to add comment to cell type " + 
/* 1100:1585 */             c.getClass().getName() + 
/* 1101:1586 */             " at " + CellReferenceHelper.getCellReference(col, row));
/* 1102:     */         }
/* 1103:     */       }
/* 1104:     */     }
/* 1105:     */   }
/* 1106:     */   
/* 1107:     */   private void handleObjectRecord(ObjRecord objRecord, MsoDrawingRecord msoRecord, HashMap comments)
/* 1108:     */   {
/* 1109:1603 */     if (msoRecord == null)
/* 1110:     */     {
/* 1111:1605 */       logger.warn("Object record is not associated with a drawing  record - ignoring");
/* 1112:     */       
/* 1113:1607 */       return;
/* 1114:     */     }
/* 1115:     */     try
/* 1116:     */     {
/* 1117:1613 */       if (objRecord.getType() == ObjRecord.PICTURE)
/* 1118:     */       {
/* 1119:1615 */         if (this.drawingData == null) {
/* 1120:1617 */           this.drawingData = new DrawingData();
/* 1121:     */         }
/* 1122:1620 */         Drawing drawing = new Drawing(msoRecord, 
/* 1123:1621 */           objRecord, 
/* 1124:1622 */           this.drawingData, 
/* 1125:1623 */           this.workbook.getDrawingGroup(), 
/* 1126:1624 */           this.sheet);
/* 1127:1625 */         this.drawings.add(drawing);
/* 1128:1626 */         return;
/* 1129:     */       }
/* 1130:1630 */       if (objRecord.getType() == ObjRecord.EXCELNOTE)
/* 1131:     */       {
/* 1132:1632 */         if (this.drawingData == null) {
/* 1133:1634 */           this.drawingData = new DrawingData();
/* 1134:     */         }
/* 1135:1637 */         Comment comment = new Comment(msoRecord, 
/* 1136:1638 */           objRecord, 
/* 1137:1639 */           this.drawingData, 
/* 1138:1640 */           this.workbook.getDrawingGroup(), 
/* 1139:1641 */           this.workbookSettings);
/* 1140:     */         
/* 1141:     */ 
/* 1142:     */ 
/* 1143:1645 */         Record r2 = this.excelFile.next();
/* 1144:1646 */         if ((r2.getType() == Type.MSODRAWING) || (r2.getType() == Type.CONTINUE))
/* 1145:     */         {
/* 1146:1648 */           MsoDrawingRecord mso = new MsoDrawingRecord(r2);
/* 1147:1649 */           comment.addMso(mso);
/* 1148:1650 */           r2 = this.excelFile.next();
/* 1149:     */         }
/* 1150:1652 */         Assert.verify(r2.getType() == Type.TXO);
/* 1151:1653 */         TextObjectRecord txo = new TextObjectRecord(r2);
/* 1152:1654 */         comment.setTextObject(txo);
/* 1153:     */         
/* 1154:1656 */         r2 = this.excelFile.next();
/* 1155:1657 */         Assert.verify(r2.getType() == Type.CONTINUE);
/* 1156:1658 */         ContinueRecord text = new ContinueRecord(r2);
/* 1157:1659 */         comment.setText(text);
/* 1158:     */         
/* 1159:1661 */         r2 = this.excelFile.next();
/* 1160:1662 */         if (r2.getType() == Type.CONTINUE)
/* 1161:     */         {
/* 1162:1664 */           ContinueRecord formatting = new ContinueRecord(r2);
/* 1163:1665 */           comment.setFormatting(formatting);
/* 1164:     */         }
/* 1165:1668 */         comments.put(new Integer(comment.getObjectId()), comment);
/* 1166:1669 */         return;
/* 1167:     */       }
/* 1168:1673 */       if (objRecord.getType() == ObjRecord.COMBOBOX)
/* 1169:     */       {
/* 1170:1675 */         if (this.drawingData == null) {
/* 1171:1677 */           this.drawingData = new DrawingData();
/* 1172:     */         }
/* 1173:1680 */         ComboBox comboBox = new ComboBox(msoRecord, 
/* 1174:1681 */           objRecord, 
/* 1175:1682 */           this.drawingData, 
/* 1176:1683 */           this.workbook.getDrawingGroup(), 
/* 1177:1684 */           this.workbookSettings);
/* 1178:1685 */         this.drawings.add(comboBox);
/* 1179:1686 */         return;
/* 1180:     */       }
/* 1181:1690 */       if (objRecord.getType() == ObjRecord.CHECKBOX)
/* 1182:     */       {
/* 1183:1692 */         if (this.drawingData == null) {
/* 1184:1694 */           this.drawingData = new DrawingData();
/* 1185:     */         }
/* 1186:1697 */         CheckBox checkBox = new CheckBox(msoRecord, 
/* 1187:1698 */           objRecord, 
/* 1188:1699 */           this.drawingData, 
/* 1189:1700 */           this.workbook.getDrawingGroup(), 
/* 1190:1701 */           this.workbookSettings);
/* 1191:     */         
/* 1192:1703 */         Record r2 = this.excelFile.next();
/* 1193:1704 */         Assert.verify((r2.getType() == Type.MSODRAWING) || 
/* 1194:1705 */           (r2.getType() == Type.CONTINUE));
/* 1195:1706 */         if ((r2.getType() == Type.MSODRAWING) || (r2.getType() == Type.CONTINUE))
/* 1196:     */         {
/* 1197:1708 */           MsoDrawingRecord mso = new MsoDrawingRecord(r2);
/* 1198:1709 */           checkBox.addMso(mso);
/* 1199:1710 */           r2 = this.excelFile.next();
/* 1200:     */         }
/* 1201:1713 */         Assert.verify(r2.getType() == Type.TXO);
/* 1202:1714 */         TextObjectRecord txo = new TextObjectRecord(r2);
/* 1203:1715 */         checkBox.setTextObject(txo);
/* 1204:1717 */         if (txo.getTextLength() == 0) {
/* 1205:1719 */           return;
/* 1206:     */         }
/* 1207:1722 */         r2 = this.excelFile.next();
/* 1208:1723 */         Assert.verify(r2.getType() == Type.CONTINUE);
/* 1209:1724 */         ContinueRecord text = new ContinueRecord(r2);
/* 1210:1725 */         checkBox.setText(text);
/* 1211:     */         
/* 1212:1727 */         r2 = this.excelFile.next();
/* 1213:1728 */         if (r2.getType() == Type.CONTINUE)
/* 1214:     */         {
/* 1215:1730 */           ContinueRecord formatting = new ContinueRecord(r2);
/* 1216:1731 */           checkBox.setFormatting(formatting);
/* 1217:     */         }
/* 1218:1734 */         this.drawings.add(checkBox);
/* 1219:     */         
/* 1220:1736 */         return;
/* 1221:     */       }
/* 1222:1740 */       if (objRecord.getType() == ObjRecord.BUTTON)
/* 1223:     */       {
/* 1224:1742 */         if (this.drawingData == null) {
/* 1225:1744 */           this.drawingData = new DrawingData();
/* 1226:     */         }
/* 1227:1747 */         Button button = new Button(msoRecord, 
/* 1228:1748 */           objRecord, 
/* 1229:1749 */           this.drawingData, 
/* 1230:1750 */           this.workbook.getDrawingGroup(), 
/* 1231:1751 */           this.workbookSettings);
/* 1232:     */         
/* 1233:1753 */         Record r2 = this.excelFile.next();
/* 1234:1754 */         Assert.verify((r2.getType() == Type.MSODRAWING) || 
/* 1235:1755 */           (r2.getType() == Type.CONTINUE));
/* 1236:1756 */         if ((r2.getType() == Type.MSODRAWING) || 
/* 1237:1757 */           (r2.getType() == Type.CONTINUE))
/* 1238:     */         {
/* 1239:1759 */           MsoDrawingRecord mso = new MsoDrawingRecord(r2);
/* 1240:1760 */           button.addMso(mso);
/* 1241:1761 */           r2 = this.excelFile.next();
/* 1242:     */         }
/* 1243:1764 */         Assert.verify(r2.getType() == Type.TXO);
/* 1244:1765 */         TextObjectRecord txo = new TextObjectRecord(r2);
/* 1245:1766 */         button.setTextObject(txo);
/* 1246:     */         
/* 1247:1768 */         r2 = this.excelFile.next();
/* 1248:1769 */         Assert.verify(r2.getType() == Type.CONTINUE);
/* 1249:1770 */         ContinueRecord text = new ContinueRecord(r2);
/* 1250:1771 */         button.setText(text);
/* 1251:     */         
/* 1252:1773 */         r2 = this.excelFile.next();
/* 1253:1774 */         if (r2.getType() == Type.CONTINUE)
/* 1254:     */         {
/* 1255:1776 */           ContinueRecord formatting = new ContinueRecord(r2);
/* 1256:1777 */           button.setFormatting(formatting);
/* 1257:     */         }
/* 1258:1780 */         this.drawings.add(button);
/* 1259:     */         
/* 1260:1782 */         return;
/* 1261:     */       }
/* 1262:1786 */       if (objRecord.getType() == ObjRecord.TEXT)
/* 1263:     */       {
/* 1264:1788 */         logger.warn(objRecord.getType() + " Object on sheet \"" + 
/* 1265:1789 */           this.sheet.getName() + 
/* 1266:1790 */           "\" not supported - omitting");
/* 1267:1793 */         if (this.drawingData == null) {
/* 1268:1795 */           this.drawingData = new DrawingData();
/* 1269:     */         }
/* 1270:1798 */         this.drawingData.addData(msoRecord.getData());
/* 1271:     */         
/* 1272:1800 */         Record r2 = this.excelFile.next();
/* 1273:1801 */         Assert.verify((r2.getType() == Type.MSODRAWING) || 
/* 1274:1802 */           (r2.getType() == Type.CONTINUE));
/* 1275:1803 */         if ((r2.getType() == Type.MSODRAWING) || 
/* 1276:1804 */           (r2.getType() == Type.CONTINUE))
/* 1277:     */         {
/* 1278:1806 */           MsoDrawingRecord mso = new MsoDrawingRecord(r2);
/* 1279:1807 */           this.drawingData.addRawData(mso.getData());
/* 1280:1808 */           r2 = this.excelFile.next();
/* 1281:     */         }
/* 1282:1811 */         Assert.verify(r2.getType() == Type.TXO);
/* 1283:1813 */         if (this.workbook.getDrawingGroup() != null) {
/* 1284:1815 */           this.workbook.getDrawingGroup().setDrawingsOmitted(msoRecord, 
/* 1285:1816 */             objRecord);
/* 1286:     */         }
/* 1287:1819 */         return;
/* 1288:     */       }
/* 1289:1823 */       if (objRecord.getType() != ObjRecord.CHART)
/* 1290:     */       {
/* 1291:1825 */         logger.warn(objRecord.getType() + " Object on sheet \"" + 
/* 1292:1826 */           this.sheet.getName() + 
/* 1293:1827 */           "\" not supported - omitting");
/* 1294:1830 */         if (this.drawingData == null) {
/* 1295:1832 */           this.drawingData = new DrawingData();
/* 1296:     */         }
/* 1297:1835 */         this.drawingData.addData(msoRecord.getData());
/* 1298:1837 */         if (this.workbook.getDrawingGroup() != null) {
/* 1299:1839 */           this.workbook.getDrawingGroup().setDrawingsOmitted(msoRecord, 
/* 1300:1840 */             objRecord);
/* 1301:     */         }
/* 1302:1843 */         return;
/* 1303:     */       }
/* 1304:     */     }
/* 1305:     */     catch (DrawingDataException e)
/* 1306:     */     {
/* 1307:1848 */       logger.warn(e.getMessage() + 
/* 1308:1849 */         "...disabling drawings for the remainder of the workbook");
/* 1309:1850 */       this.workbookSettings.setDrawingsDisabled(true);
/* 1310:     */     }
/* 1311:     */   }
/* 1312:     */   
/* 1313:     */   DrawingData getDrawingData()
/* 1314:     */   {
/* 1315:1859 */     return this.drawingData;
/* 1316:     */   }
/* 1317:     */   
/* 1318:     */   private void handleOutOfBoundsCells()
/* 1319:     */   {
/* 1320:1868 */     int resizedRows = this.numRows;
/* 1321:1869 */     int resizedCols = this.numCols;
/* 1322:1872 */     for (Iterator i = this.outOfBoundsCells.iterator(); i.hasNext();)
/* 1323:     */     {
/* 1324:1874 */       Cell cell = (Cell)i.next();
/* 1325:1875 */       resizedRows = Math.max(resizedRows, cell.getRow() + 1);
/* 1326:1876 */       resizedCols = Math.max(resizedCols, cell.getColumn() + 1);
/* 1327:     */     }
/* 1328:1885 */     if (resizedCols > this.numCols) {
/* 1329:1887 */       for (int r = 0; r < this.numRows; r++)
/* 1330:     */       {
/* 1331:1889 */         Cell[] newRow = new Cell[resizedCols];
/* 1332:1890 */         Cell[] oldRow = this.cells[r];
/* 1333:1891 */         System.arraycopy(oldRow, 0, newRow, 0, oldRow.length);
/* 1334:1892 */         this.cells[r] = newRow;
/* 1335:     */       }
/* 1336:     */     }
/* 1337:1897 */     if (resizedRows > this.numRows)
/* 1338:     */     {
/* 1339:1899 */       Cell[][] newCells = new Cell[resizedRows][];
/* 1340:1900 */       System.arraycopy(this.cells, 0, newCells, 0, this.cells.length);
/* 1341:1901 */       this.cells = newCells;
/* 1342:1904 */       for (int i = this.numRows; i < resizedRows; i++) {
/* 1343:1906 */         newCells[i] = new Cell[resizedCols];
/* 1344:     */       }
/* 1345:     */     }
/* 1346:1910 */     this.numRows = resizedRows;
/* 1347:1911 */     this.numCols = resizedCols;
/* 1348:1914 */     for (Iterator i = this.outOfBoundsCells.iterator(); i.hasNext();)
/* 1349:     */     {
/* 1350:1916 */       Cell cell = (Cell)i.next();
/* 1351:1917 */       addCell(cell);
/* 1352:     */     }
/* 1353:1920 */     this.outOfBoundsCells.clear();
/* 1354:     */   }
/* 1355:     */   
/* 1356:     */   public int getMaxColumnOutlineLevel()
/* 1357:     */   {
/* 1358:1930 */     return this.maxColumnOutlineLevel;
/* 1359:     */   }
/* 1360:     */   
/* 1361:     */   public int getMaxRowOutlineLevel()
/* 1362:     */   {
/* 1363:1940 */     return this.maxRowOutlineLevel;
/* 1364:     */   }
/* 1365:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SheetReader
 * JD-Core Version:    0.7.0.1
 */