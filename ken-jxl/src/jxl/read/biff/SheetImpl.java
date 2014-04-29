/*    1:     */ package jxl.read.biff;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.Iterator;
/*    5:     */ import java.util.regex.Pattern;
/*    6:     */ import jxl.Cell;
/*    7:     */ import jxl.CellView;
/*    8:     */ import jxl.Hyperlink;
/*    9:     */ import jxl.Image;
/*   10:     */ import jxl.LabelCell;
/*   11:     */ import jxl.Range;
/*   12:     */ import jxl.Sheet;
/*   13:     */ import jxl.SheetSettings;
/*   14:     */ import jxl.WorkbookSettings;
/*   15:     */ import jxl.biff.AutoFilter;
/*   16:     */ import jxl.biff.BuiltInName;
/*   17:     */ import jxl.biff.CellFinder;
/*   18:     */ import jxl.biff.CellReferenceHelper;
/*   19:     */ import jxl.biff.ConditionalFormat;
/*   20:     */ import jxl.biff.DataValidation;
/*   21:     */ import jxl.biff.EmptyCell;
/*   22:     */ import jxl.biff.FormattingRecords;
/*   23:     */ import jxl.biff.Type;
/*   24:     */ import jxl.biff.WorkspaceInformationRecord;
/*   25:     */ import jxl.biff.drawing.Chart;
/*   26:     */ import jxl.biff.drawing.Drawing;
/*   27:     */ import jxl.biff.drawing.DrawingData;
/*   28:     */ import jxl.biff.drawing.DrawingGroupObject;
/*   29:     */ import jxl.common.Logger;
/*   30:     */ import jxl.format.CellFormat;
/*   31:     */ 
/*   32:     */ public class SheetImpl
/*   33:     */   implements Sheet
/*   34:     */ {
/*   35:  68 */   private static Logger logger = Logger.getLogger(SheetImpl.class);
/*   36:     */   private File excelFile;
/*   37:     */   private SSTRecord sharedStrings;
/*   38:     */   private BOFRecord sheetBof;
/*   39:     */   private BOFRecord workbookBof;
/*   40:     */   private FormattingRecords formattingRecords;
/*   41:     */   private String name;
/*   42:     */   private int numRows;
/*   43:     */   private int numCols;
/*   44:     */   private Cell[][] cells;
/*   45:     */   private int startPosition;
/*   46:     */   private ColumnInfoRecord[] columnInfos;
/*   47:     */   private RowRecord[] rowRecords;
/*   48:     */   private ArrayList rowProperties;
/*   49:     */   private ArrayList columnInfosArray;
/*   50:     */   private ArrayList sharedFormulas;
/*   51:     */   private ArrayList hyperlinks;
/*   52:     */   private ArrayList charts;
/*   53:     */   private ArrayList drawings;
/*   54:     */   private ArrayList images;
/*   55:     */   private DataValidation dataValidation;
/*   56:     */   private Range[] mergedCells;
/*   57:     */   private boolean columnInfosInitialized;
/*   58:     */   private boolean rowRecordsInitialized;
/*   59:     */   private boolean nineteenFour;
/*   60:     */   private WorkspaceInformationRecord workspaceOptions;
/*   61:     */   private boolean hidden;
/*   62:     */   private PLSRecord plsRecord;
/*   63:     */   private ButtonPropertySetRecord buttonPropertySet;
/*   64:     */   private SheetSettings settings;
/*   65:     */   private int[] rowBreaks;
/*   66:     */   private int[] columnBreaks;
/*   67:     */   private int maxRowOutlineLevel;
/*   68:     */   private int maxColumnOutlineLevel;
/*   69:     */   private ArrayList localNames;
/*   70:     */   private ArrayList conditionalFormats;
/*   71:     */   private AutoFilter autoFilter;
/*   72:     */   private WorkbookParser workbook;
/*   73:     */   private WorkbookSettings workbookSettings;
/*   74:     */   
/*   75:     */   SheetImpl(File f, SSTRecord sst, FormattingRecords fr, BOFRecord sb, BOFRecord wb, boolean nf, WorkbookParser wp)
/*   76:     */     throws BiffException
/*   77:     */   {
/*   78: 283 */     this.excelFile = f;
/*   79: 284 */     this.sharedStrings = sst;
/*   80: 285 */     this.formattingRecords = fr;
/*   81: 286 */     this.sheetBof = sb;
/*   82: 287 */     this.workbookBof = wb;
/*   83: 288 */     this.columnInfosArray = new ArrayList();
/*   84: 289 */     this.sharedFormulas = new ArrayList();
/*   85: 290 */     this.hyperlinks = new ArrayList();
/*   86: 291 */     this.rowProperties = new ArrayList(10);
/*   87: 292 */     this.columnInfosInitialized = false;
/*   88: 293 */     this.rowRecordsInitialized = false;
/*   89: 294 */     this.nineteenFour = nf;
/*   90: 295 */     this.workbook = wp;
/*   91: 296 */     this.workbookSettings = this.workbook.getSettings();
/*   92:     */     
/*   93:     */ 
/*   94: 299 */     this.startPosition = f.getPos();
/*   95: 301 */     if (this.sheetBof.isChart()) {
/*   96: 304 */       this.startPosition -= this.sheetBof.getLength() + 4;
/*   97:     */     }
/*   98: 307 */     Record r = null;
/*   99: 308 */     int bofs = 1;
/*  100: 310 */     while (bofs >= 1)
/*  101:     */     {
/*  102: 312 */       r = f.next();
/*  103: 315 */       if (r.getCode() == Type.EOF.value) {
/*  104: 317 */         bofs--;
/*  105:     */       }
/*  106: 320 */       if (r.getCode() == Type.BOF.value) {
/*  107: 322 */         bofs++;
/*  108:     */       }
/*  109:     */     }
/*  110:     */   }
/*  111:     */   
/*  112:     */   public Cell getCell(String loc)
/*  113:     */   {
/*  114: 336 */     return getCell(CellReferenceHelper.getColumn(loc), 
/*  115: 337 */       CellReferenceHelper.getRow(loc));
/*  116:     */   }
/*  117:     */   
/*  118:     */   public Cell getCell(int column, int row)
/*  119:     */   {
/*  120: 351 */     if (this.cells == null) {
/*  121: 353 */       readSheet();
/*  122:     */     }
/*  123: 356 */     Cell c = this.cells[row][column];
/*  124: 358 */     if (c == null)
/*  125:     */     {
/*  126: 360 */       c = new EmptyCell(column, row);
/*  127: 361 */       this.cells[row][column] = c;
/*  128:     */     }
/*  129: 364 */     return c;
/*  130:     */   }
/*  131:     */   
/*  132:     */   public Cell findCell(String contents)
/*  133:     */   {
/*  134: 378 */     CellFinder cellFinder = new CellFinder(this);
/*  135: 379 */     return cellFinder.findCell(contents);
/*  136:     */   }
/*  137:     */   
/*  138:     */   public Cell findCell(String contents, int firstCol, int firstRow, int lastCol, int lastRow, boolean reverse)
/*  139:     */   {
/*  140: 403 */     CellFinder cellFinder = new CellFinder(this);
/*  141: 404 */     return cellFinder.findCell(contents, 
/*  142: 405 */       firstCol, 
/*  143: 406 */       firstRow, 
/*  144: 407 */       lastCol, 
/*  145: 408 */       lastRow, 
/*  146: 409 */       reverse);
/*  147:     */   }
/*  148:     */   
/*  149:     */   public Cell findCell(Pattern pattern, int firstCol, int firstRow, int lastCol, int lastRow, boolean reverse)
/*  150:     */   {
/*  151: 433 */     CellFinder cellFinder = new CellFinder(this);
/*  152: 434 */     return cellFinder.findCell(pattern, 
/*  153: 435 */       firstCol, 
/*  154: 436 */       firstRow, 
/*  155: 437 */       lastCol, 
/*  156: 438 */       lastRow, 
/*  157: 439 */       reverse);
/*  158:     */   }
/*  159:     */   
/*  160:     */   public LabelCell findLabelCell(String contents)
/*  161:     */   {
/*  162: 456 */     CellFinder cellFinder = new CellFinder(this);
/*  163: 457 */     return cellFinder.findLabelCell(contents);
/*  164:     */   }
/*  165:     */   
/*  166:     */   public int getRows()
/*  167:     */   {
/*  168: 469 */     if (this.cells == null) {
/*  169: 471 */       readSheet();
/*  170:     */     }
/*  171: 474 */     return this.numRows;
/*  172:     */   }
/*  173:     */   
/*  174:     */   public int getColumns()
/*  175:     */   {
/*  176: 486 */     if (this.cells == null) {
/*  177: 488 */       readSheet();
/*  178:     */     }
/*  179: 491 */     return this.numCols;
/*  180:     */   }
/*  181:     */   
/*  182:     */   public Cell[] getRow(int row)
/*  183:     */   {
/*  184: 505 */     if (this.cells == null) {
/*  185: 507 */       readSheet();
/*  186:     */     }
/*  187: 511 */     boolean found = false;
/*  188: 512 */     int col = this.numCols - 1;
/*  189: 513 */     while ((col >= 0) && (!found)) {
/*  190: 515 */       if (this.cells[row][col] != null) {
/*  191: 517 */         found = true;
/*  192:     */       } else {
/*  193: 521 */         col--;
/*  194:     */       }
/*  195:     */     }
/*  196: 526 */     Cell[] c = new Cell[col + 1];
/*  197: 528 */     for (int i = 0; i <= col; i++) {
/*  198: 530 */       c[i] = getCell(i, row);
/*  199:     */     }
/*  200: 532 */     return c;
/*  201:     */   }
/*  202:     */   
/*  203:     */   public Cell[] getColumn(int col)
/*  204:     */   {
/*  205: 546 */     if (this.cells == null) {
/*  206: 548 */       readSheet();
/*  207:     */     }
/*  208: 552 */     boolean found = false;
/*  209: 553 */     int row = this.numRows - 1;
/*  210: 554 */     while ((row >= 0) && (!found)) {
/*  211: 556 */       if (this.cells[row][col] != null) {
/*  212: 558 */         found = true;
/*  213:     */       } else {
/*  214: 562 */         row--;
/*  215:     */       }
/*  216:     */     }
/*  217: 567 */     Cell[] c = new Cell[row + 1];
/*  218: 569 */     for (int i = 0; i <= row; i++) {
/*  219: 571 */       c[i] = getCell(col, i);
/*  220:     */     }
/*  221: 573 */     return c;
/*  222:     */   }
/*  223:     */   
/*  224:     */   public String getName()
/*  225:     */   {
/*  226: 583 */     return this.name;
/*  227:     */   }
/*  228:     */   
/*  229:     */   final void setName(String s)
/*  230:     */   {
/*  231: 593 */     this.name = s;
/*  232:     */   }
/*  233:     */   
/*  234:     */   /**
/*  235:     */    * @deprecated
/*  236:     */    */
/*  237:     */   public boolean isHidden()
/*  238:     */   {
/*  239: 604 */     return this.hidden;
/*  240:     */   }
/*  241:     */   
/*  242:     */   public ColumnInfoRecord getColumnInfo(int col)
/*  243:     */   {
/*  244: 616 */     if (!this.columnInfosInitialized)
/*  245:     */     {
/*  246: 619 */       Iterator i = this.columnInfosArray.iterator();
/*  247: 620 */       ColumnInfoRecord cir = null;
/*  248: 621 */       while (i.hasNext())
/*  249:     */       {
/*  250: 623 */         cir = (ColumnInfoRecord)i.next();
/*  251:     */         
/*  252: 625 */         int startcol = Math.max(0, cir.getStartColumn());
/*  253: 626 */         int endcol = Math.min(this.columnInfos.length - 1, cir.getEndColumn());
/*  254: 628 */         for (int c = startcol; c <= endcol; c++) {
/*  255: 630 */           this.columnInfos[c] = cir;
/*  256:     */         }
/*  257: 633 */         if (endcol < startcol) {
/*  258: 635 */           this.columnInfos[startcol] = cir;
/*  259:     */         }
/*  260:     */       }
/*  261: 639 */       this.columnInfosInitialized = true;
/*  262:     */     }
/*  263: 642 */     return col < this.columnInfos.length ? this.columnInfos[col] : null;
/*  264:     */   }
/*  265:     */   
/*  266:     */   public ColumnInfoRecord[] getColumnInfos()
/*  267:     */   {
/*  268: 653 */     ColumnInfoRecord[] infos = new ColumnInfoRecord[this.columnInfosArray.size()];
/*  269: 654 */     for (int i = 0; i < this.columnInfosArray.size(); i++) {
/*  270: 656 */       infos[i] = ((ColumnInfoRecord)this.columnInfosArray.get(i));
/*  271:     */     }
/*  272: 659 */     return infos;
/*  273:     */   }
/*  274:     */   
/*  275:     */   final void setHidden(boolean h)
/*  276:     */   {
/*  277: 669 */     this.hidden = h;
/*  278:     */   }
/*  279:     */   
/*  280:     */   final void clear()
/*  281:     */   {
/*  282: 678 */     this.cells = null;
/*  283: 679 */     this.mergedCells = null;
/*  284: 680 */     this.columnInfosArray.clear();
/*  285: 681 */     this.sharedFormulas.clear();
/*  286: 682 */     this.hyperlinks.clear();
/*  287: 683 */     this.columnInfosInitialized = false;
/*  288: 685 */     if (!this.workbookSettings.getGCDisabled()) {
/*  289: 687 */       System.gc();
/*  290:     */     }
/*  291:     */   }
/*  292:     */   
/*  293:     */   final void readSheet()
/*  294:     */   {
/*  295: 699 */     if (!this.sheetBof.isWorksheet())
/*  296:     */     {
/*  297: 701 */       this.numRows = 0;
/*  298: 702 */       this.numCols = 0;
/*  299: 703 */       this.cells = new Cell[0][0];
/*  300:     */     }
/*  301: 707 */     SheetReader reader = new SheetReader(this.excelFile, 
/*  302: 708 */       this.sharedStrings, 
/*  303: 709 */       this.formattingRecords, 
/*  304: 710 */       this.sheetBof, 
/*  305: 711 */       this.workbookBof, 
/*  306: 712 */       this.nineteenFour, 
/*  307: 713 */       this.workbook, 
/*  308: 714 */       this.startPosition, 
/*  309: 715 */       this);
/*  310: 716 */     reader.read();
/*  311:     */     
/*  312:     */ 
/*  313: 719 */     this.numRows = reader.getNumRows();
/*  314: 720 */     this.numCols = reader.getNumCols();
/*  315: 721 */     this.cells = reader.getCells();
/*  316: 722 */     this.rowProperties = reader.getRowProperties();
/*  317: 723 */     this.columnInfosArray = reader.getColumnInfosArray();
/*  318: 724 */     this.hyperlinks = reader.getHyperlinks();
/*  319: 725 */     this.conditionalFormats = reader.getConditionalFormats();
/*  320: 726 */     this.autoFilter = reader.getAutoFilter();
/*  321: 727 */     this.charts = reader.getCharts();
/*  322: 728 */     this.drawings = reader.getDrawings();
/*  323: 729 */     this.dataValidation = reader.getDataValidation();
/*  324: 730 */     this.mergedCells = reader.getMergedCells();
/*  325: 731 */     this.settings = reader.getSettings();
/*  326: 732 */     this.settings.setHidden(this.hidden);
/*  327: 733 */     this.rowBreaks = reader.getRowBreaks();
/*  328: 734 */     this.columnBreaks = reader.getColumnBreaks();
/*  329: 735 */     this.workspaceOptions = reader.getWorkspaceOptions();
/*  330: 736 */     this.plsRecord = reader.getPLS();
/*  331: 737 */     this.buttonPropertySet = reader.getButtonPropertySet();
/*  332: 738 */     this.maxRowOutlineLevel = reader.getMaxRowOutlineLevel();
/*  333: 739 */     this.maxColumnOutlineLevel = reader.getMaxColumnOutlineLevel();
/*  334:     */     
/*  335: 741 */     reader = null;
/*  336: 743 */     if (!this.workbookSettings.getGCDisabled()) {
/*  337: 745 */       System.gc();
/*  338:     */     }
/*  339: 748 */     if (this.columnInfosArray.size() > 0)
/*  340:     */     {
/*  341: 750 */       ColumnInfoRecord cir = 
/*  342: 751 */         (ColumnInfoRecord)this.columnInfosArray.get(this.columnInfosArray.size() - 1);
/*  343: 752 */       this.columnInfos = new ColumnInfoRecord[cir.getEndColumn() + 1];
/*  344:     */     }
/*  345:     */     else
/*  346:     */     {
/*  347: 756 */       this.columnInfos = new ColumnInfoRecord[0];
/*  348:     */     }
/*  349: 760 */     if (this.localNames != null) {
/*  350: 762 */       for (Iterator it = this.localNames.iterator(); it.hasNext();)
/*  351:     */       {
/*  352: 764 */         NameRecord nr = (NameRecord)it.next();
/*  353: 765 */         if (nr.getBuiltInName() == BuiltInName.PRINT_AREA)
/*  354:     */         {
/*  355: 767 */           if (nr.getRanges().length > 0)
/*  356:     */           {
/*  357: 769 */             NameRecord.NameRange rng = nr.getRanges()[0];
/*  358: 770 */             this.settings.setPrintArea(rng.getFirstColumn(), 
/*  359: 771 */               rng.getFirstRow(), 
/*  360: 772 */               rng.getLastColumn(), 
/*  361: 773 */               rng.getLastRow());
/*  362:     */           }
/*  363:     */         }
/*  364: 776 */         else if (nr.getBuiltInName() == BuiltInName.PRINT_TITLES) {
/*  365: 783 */           for (int i = 0; i < nr.getRanges().length; i++)
/*  366:     */           {
/*  367: 785 */             NameRecord.NameRange rng = nr.getRanges()[i];
/*  368: 786 */             if ((rng.getFirstColumn() == 0) && (rng.getLastColumn() == 255)) {
/*  369: 788 */               this.settings.setPrintTitlesRow(rng.getFirstRow(), 
/*  370: 789 */                 rng.getLastRow());
/*  371:     */             } else {
/*  372: 793 */               this.settings.setPrintTitlesCol(rng.getFirstColumn(), 
/*  373: 794 */                 rng.getLastColumn());
/*  374:     */             }
/*  375:     */           }
/*  376:     */         }
/*  377:     */       }
/*  378:     */     }
/*  379:     */   }
/*  380:     */   
/*  381:     */   public Hyperlink[] getHyperlinks()
/*  382:     */   {
/*  383: 809 */     Hyperlink[] hl = new Hyperlink[this.hyperlinks.size()];
/*  384: 811 */     for (int i = 0; i < this.hyperlinks.size(); i++) {
/*  385: 813 */       hl[i] = ((Hyperlink)this.hyperlinks.get(i));
/*  386:     */     }
/*  387: 816 */     return hl;
/*  388:     */   }
/*  389:     */   
/*  390:     */   public Range[] getMergedCells()
/*  391:     */   {
/*  392: 826 */     if (this.mergedCells == null) {
/*  393: 828 */       return new Range[0];
/*  394:     */     }
/*  395: 831 */     return this.mergedCells;
/*  396:     */   }
/*  397:     */   
/*  398:     */   public RowRecord[] getRowProperties()
/*  399:     */   {
/*  400: 841 */     RowRecord[] rp = new RowRecord[this.rowProperties.size()];
/*  401: 842 */     for (int i = 0; i < rp.length; i++) {
/*  402: 844 */       rp[i] = ((RowRecord)this.rowProperties.get(i));
/*  403:     */     }
/*  404: 847 */     return rp;
/*  405:     */   }
/*  406:     */   
/*  407:     */   public DataValidation getDataValidation()
/*  408:     */   {
/*  409: 857 */     return this.dataValidation;
/*  410:     */   }
/*  411:     */   
/*  412:     */   RowRecord getRowInfo(int r)
/*  413:     */   {
/*  414: 869 */     if (!this.rowRecordsInitialized)
/*  415:     */     {
/*  416: 871 */       this.rowRecords = new RowRecord[getRows()];
/*  417: 872 */       Iterator i = this.rowProperties.iterator();
/*  418:     */       
/*  419: 874 */       int rownum = 0;
/*  420: 875 */       RowRecord rr = null;
/*  421: 876 */       while (i.hasNext())
/*  422:     */       {
/*  423: 878 */         rr = (RowRecord)i.next();
/*  424: 879 */         rownum = rr.getRowNumber();
/*  425: 880 */         if (rownum < this.rowRecords.length) {
/*  426: 882 */           this.rowRecords[rownum] = rr;
/*  427:     */         }
/*  428:     */       }
/*  429: 886 */       this.rowRecordsInitialized = true;
/*  430:     */     }
/*  431: 889 */     return r < this.rowRecords.length ? this.rowRecords[r] : null;
/*  432:     */   }
/*  433:     */   
/*  434:     */   public final int[] getRowPageBreaks()
/*  435:     */   {
/*  436: 899 */     return this.rowBreaks;
/*  437:     */   }
/*  438:     */   
/*  439:     */   public final int[] getColumnPageBreaks()
/*  440:     */   {
/*  441: 909 */     return this.columnBreaks;
/*  442:     */   }
/*  443:     */   
/*  444:     */   public final Chart[] getCharts()
/*  445:     */   {
/*  446: 919 */     Chart[] ch = new Chart[this.charts.size()];
/*  447: 921 */     for (int i = 0; i < ch.length; i++) {
/*  448: 923 */       ch[i] = ((Chart)this.charts.get(i));
/*  449:     */     }
/*  450: 925 */     return ch;
/*  451:     */   }
/*  452:     */   
/*  453:     */   public final DrawingGroupObject[] getDrawings()
/*  454:     */   {
/*  455: 935 */     DrawingGroupObject[] dr = new DrawingGroupObject[this.drawings.size()];
/*  456: 936 */     dr = (DrawingGroupObject[])this.drawings.toArray(dr);
/*  457: 937 */     return dr;
/*  458:     */   }
/*  459:     */   
/*  460:     */   /**
/*  461:     */    * @deprecated
/*  462:     */    */
/*  463:     */   public boolean isProtected()
/*  464:     */   {
/*  465: 948 */     return this.settings.isProtected();
/*  466:     */   }
/*  467:     */   
/*  468:     */   public WorkspaceInformationRecord getWorkspaceOptions()
/*  469:     */   {
/*  470: 959 */     return this.workspaceOptions;
/*  471:     */   }
/*  472:     */   
/*  473:     */   public SheetSettings getSettings()
/*  474:     */   {
/*  475: 969 */     return this.settings;
/*  476:     */   }
/*  477:     */   
/*  478:     */   public WorkbookParser getWorkbook()
/*  479:     */   {
/*  480: 980 */     return this.workbook;
/*  481:     */   }
/*  482:     */   
/*  483:     */   /**
/*  484:     */    * @deprecated
/*  485:     */    */
/*  486:     */   public CellFormat getColumnFormat(int col)
/*  487:     */   {
/*  488: 992 */     CellView cv = getColumnView(col);
/*  489: 993 */     return cv.getFormat();
/*  490:     */   }
/*  491:     */   
/*  492:     */   public int getColumnWidth(int col)
/*  493:     */   {
/*  494:1005 */     return getColumnView(col).getSize() / 256;
/*  495:     */   }
/*  496:     */   
/*  497:     */   public CellView getColumnView(int col)
/*  498:     */   {
/*  499:1017 */     ColumnInfoRecord cir = getColumnInfo(col);
/*  500:1018 */     CellView cv = new CellView();
/*  501:1020 */     if (cir != null)
/*  502:     */     {
/*  503:1022 */       cv.setDimension(cir.getWidth() / 256);
/*  504:1023 */       cv.setSize(cir.getWidth());
/*  505:1024 */       cv.setHidden(cir.getHidden());
/*  506:1025 */       cv.setFormat(this.formattingRecords.getXFRecord(cir.getXFIndex()));
/*  507:     */     }
/*  508:     */     else
/*  509:     */     {
/*  510:1029 */       cv.setDimension(this.settings.getDefaultColumnWidth());
/*  511:1030 */       cv.setSize(this.settings.getDefaultColumnWidth() * 256);
/*  512:     */     }
/*  513:1033 */     return cv;
/*  514:     */   }
/*  515:     */   
/*  516:     */   /**
/*  517:     */    * @deprecated
/*  518:     */    */
/*  519:     */   public int getRowHeight(int row)
/*  520:     */   {
/*  521:1046 */     return getRowView(row).getDimension();
/*  522:     */   }
/*  523:     */   
/*  524:     */   public CellView getRowView(int row)
/*  525:     */   {
/*  526:1058 */     RowRecord rr = getRowInfo(row);
/*  527:     */     
/*  528:1060 */     CellView cv = new CellView();
/*  529:1062 */     if (rr != null)
/*  530:     */     {
/*  531:1064 */       cv.setDimension(rr.getRowHeight());
/*  532:1065 */       cv.setSize(rr.getRowHeight());
/*  533:1066 */       cv.setHidden(rr.isCollapsed());
/*  534:1067 */       if (rr.hasDefaultFormat()) {
/*  535:1069 */         cv.setFormat(this.formattingRecords.getXFRecord(rr.getXFIndex()));
/*  536:     */       }
/*  537:     */     }
/*  538:     */     else
/*  539:     */     {
/*  540:1074 */       cv.setDimension(this.settings.getDefaultRowHeight());
/*  541:1075 */       cv.setSize(this.settings.getDefaultRowHeight());
/*  542:     */     }
/*  543:1078 */     return cv;
/*  544:     */   }
/*  545:     */   
/*  546:     */   public BOFRecord getSheetBof()
/*  547:     */   {
/*  548:1089 */     return this.sheetBof;
/*  549:     */   }
/*  550:     */   
/*  551:     */   public BOFRecord getWorkbookBof()
/*  552:     */   {
/*  553:1100 */     return this.workbookBof;
/*  554:     */   }
/*  555:     */   
/*  556:     */   public PLSRecord getPLS()
/*  557:     */   {
/*  558:1111 */     return this.plsRecord;
/*  559:     */   }
/*  560:     */   
/*  561:     */   public ButtonPropertySetRecord getButtonPropertySet()
/*  562:     */   {
/*  563:1121 */     return this.buttonPropertySet;
/*  564:     */   }
/*  565:     */   
/*  566:     */   public int getNumberOfImages()
/*  567:     */   {
/*  568:1131 */     if (this.images == null) {
/*  569:1133 */       initializeImages();
/*  570:     */     }
/*  571:1136 */     return this.images.size();
/*  572:     */   }
/*  573:     */   
/*  574:     */   public Image getDrawing(int i)
/*  575:     */   {
/*  576:1147 */     if (this.images == null) {
/*  577:1149 */       initializeImages();
/*  578:     */     }
/*  579:1152 */     return (Image)this.images.get(i);
/*  580:     */   }
/*  581:     */   
/*  582:     */   private void initializeImages()
/*  583:     */   {
/*  584:1160 */     if (this.images != null) {
/*  585:1162 */       return;
/*  586:     */     }
/*  587:1165 */     this.images = new ArrayList();
/*  588:1166 */     DrawingGroupObject[] dgos = getDrawings();
/*  589:1168 */     for (int i = 0; i < dgos.length; i++) {
/*  590:1170 */       if ((dgos[i] instanceof Drawing)) {
/*  591:1172 */         this.images.add(dgos[i]);
/*  592:     */       }
/*  593:     */     }
/*  594:     */   }
/*  595:     */   
/*  596:     */   public DrawingData getDrawingData()
/*  597:     */   {
/*  598:1182 */     SheetReader reader = new SheetReader(this.excelFile, 
/*  599:1183 */       this.sharedStrings, 
/*  600:1184 */       this.formattingRecords, 
/*  601:1185 */       this.sheetBof, 
/*  602:1186 */       this.workbookBof, 
/*  603:1187 */       this.nineteenFour, 
/*  604:1188 */       this.workbook, 
/*  605:1189 */       this.startPosition, 
/*  606:1190 */       this);
/*  607:1191 */     reader.read();
/*  608:1192 */     return reader.getDrawingData();
/*  609:     */   }
/*  610:     */   
/*  611:     */   void addLocalName(NameRecord nr)
/*  612:     */   {
/*  613:1202 */     if (this.localNames == null) {
/*  614:1204 */       this.localNames = new ArrayList();
/*  615:     */     }
/*  616:1207 */     this.localNames.add(nr);
/*  617:     */   }
/*  618:     */   
/*  619:     */   public ConditionalFormat[] getConditionalFormats()
/*  620:     */   {
/*  621:1217 */     ConditionalFormat[] formats = 
/*  622:1218 */       new ConditionalFormat[this.conditionalFormats.size()];
/*  623:1219 */     formats = (ConditionalFormat[])this.conditionalFormats.toArray(formats);
/*  624:1220 */     return formats;
/*  625:     */   }
/*  626:     */   
/*  627:     */   public AutoFilter getAutoFilter()
/*  628:     */   {
/*  629:1230 */     return this.autoFilter;
/*  630:     */   }
/*  631:     */   
/*  632:     */   public int getMaxColumnOutlineLevel()
/*  633:     */   {
/*  634:1240 */     return this.maxColumnOutlineLevel;
/*  635:     */   }
/*  636:     */   
/*  637:     */   public int getMaxRowOutlineLevel()
/*  638:     */   {
/*  639:1250 */     return this.maxRowOutlineLevel;
/*  640:     */   }
/*  641:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SheetImpl
 * JD-Core Version:    0.7.0.1
 */