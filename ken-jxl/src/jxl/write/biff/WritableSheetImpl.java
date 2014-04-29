/*    1:     */ package jxl.write.biff;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.net.URL;
/*    5:     */ import java.util.ArrayList;
/*    6:     */ import java.util.Comparator;
/*    7:     */ import java.util.Iterator;
/*    8:     */ import java.util.TreeSet;
/*    9:     */ import java.util.regex.Pattern;
/*   10:     */ import jxl.Cell;
/*   11:     */ import jxl.CellFeatures;
/*   12:     */ import jxl.CellReferenceHelper;
/*   13:     */ import jxl.CellType;
/*   14:     */ import jxl.CellView;
/*   15:     */ import jxl.HeaderFooter;
/*   16:     */ import jxl.HeaderFooter.Contents;
/*   17:     */ import jxl.Hyperlink;
/*   18:     */ import jxl.Image;
/*   19:     */ import jxl.LabelCell;
/*   20:     */ import jxl.Range;
/*   21:     */ import jxl.Sheet;
/*   22:     */ import jxl.SheetSettings;
/*   23:     */ import jxl.WorkbookSettings;
/*   24:     */ import jxl.biff.AutoFilter;
/*   25:     */ import jxl.biff.CellFinder;
/*   26:     */ import jxl.biff.ConditionalFormat;
/*   27:     */ import jxl.biff.DVParser;
/*   28:     */ import jxl.biff.DataValidation;
/*   29:     */ import jxl.biff.EmptyCell;
/*   30:     */ import jxl.biff.FormattingRecords;
/*   31:     */ import jxl.biff.IndexMapping;
/*   32:     */ import jxl.biff.NumFormatRecordsException;
/*   33:     */ import jxl.biff.SheetRangeImpl;
/*   34:     */ import jxl.biff.WorkspaceInformationRecord;
/*   35:     */ import jxl.biff.XFRecord;
/*   36:     */ import jxl.biff.drawing.Chart;
/*   37:     */ import jxl.biff.drawing.ComboBox;
/*   38:     */ import jxl.biff.drawing.Drawing;
/*   39:     */ import jxl.biff.drawing.DrawingGroup;
/*   40:     */ import jxl.biff.drawing.DrawingGroupObject;
/*   41:     */ import jxl.common.Assert;
/*   42:     */ import jxl.common.Logger;
/*   43:     */ import jxl.format.CellFormat;
/*   44:     */ import jxl.format.Font;
/*   45:     */ import jxl.format.PageOrientation;
/*   46:     */ import jxl.format.PaperSize;
/*   47:     */ import jxl.write.Blank;
/*   48:     */ import jxl.write.Label;
/*   49:     */ import jxl.write.WritableCell;
/*   50:     */ import jxl.write.WritableCellFeatures;
/*   51:     */ import jxl.write.WritableCellFormat;
/*   52:     */ import jxl.write.WritableHyperlink;
/*   53:     */ import jxl.write.WritableImage;
/*   54:     */ import jxl.write.WritableSheet;
/*   55:     */ import jxl.write.WritableWorkbook;
/*   56:     */ import jxl.write.WriteException;
/*   57:     */ 
/*   58:     */ class WritableSheetImpl
/*   59:     */   implements WritableSheet
/*   60:     */ {
/*   61:  94 */   private static Logger logger = Logger.getLogger(WritableSheetImpl.class);
/*   62:     */   private String name;
/*   63:     */   private File outputFile;
/*   64:     */   private RowRecord[] rows;
/*   65:     */   private FormattingRecords formatRecords;
/*   66:     */   private SharedStrings sharedStrings;
/*   67:     */   private TreeSet columnFormats;
/*   68:     */   private TreeSet autosizedColumns;
/*   69:     */   private ArrayList hyperlinks;
/*   70:     */   private MergedCells mergedCells;
/*   71:     */   private int numRows;
/*   72:     */   private int numColumns;
/*   73:     */   private PLSRecord plsRecord;
/*   74:     */   private ButtonPropertySetRecord buttonPropertySet;
/*   75:     */   private boolean chartOnly;
/*   76:     */   private DataValidation dataValidation;
/*   77:     */   private ArrayList rowBreaks;
/*   78:     */   private ArrayList columnBreaks;
/*   79:     */   private ArrayList drawings;
/*   80:     */   private ArrayList images;
/*   81:     */   private ArrayList conditionalFormats;
/*   82:     */   private AutoFilter autoFilter;
/*   83:     */   private ArrayList validatedCells;
/*   84:     */   private ComboBox comboBox;
/*   85:     */   private boolean drawingsModified;
/*   86:     */   private int maxRowOutlineLevel;
/*   87:     */   private int maxColumnOutlineLevel;
/*   88:     */   private SheetSettings settings;
/*   89:     */   private SheetWriter sheetWriter;
/*   90:     */   private WorkbookSettings workbookSettings;
/*   91:     */   private WritableWorkbookImpl workbook;
/*   92:     */   private static final int rowGrowSize = 10;
/*   93:     */   private static final int numRowsPerSheet = 65536;
/*   94:     */   private static final int maxSheetNameLength = 31;
/*   95: 264 */   private static final char[] illegalSheetNameCharacters = { '*', ':', '?', '\\' };
/*   96: 269 */   private static final String[] imageTypes = { "png" };
/*   97:     */   
/*   98:     */   private static class ColumnInfoComparator
/*   99:     */     implements Comparator
/*  100:     */   {
/*  101:     */     public boolean equals(Object o)
/*  102:     */     {
/*  103: 284 */       return o == this;
/*  104:     */     }
/*  105:     */     
/*  106:     */     public int compare(Object o1, Object o2)
/*  107:     */     {
/*  108: 296 */       if (o1 == o2) {
/*  109: 298 */         return 0;
/*  110:     */       }
/*  111: 301 */       Assert.verify(o1 instanceof ColumnInfoRecord);
/*  112: 302 */       Assert.verify(o2 instanceof ColumnInfoRecord);
/*  113:     */       
/*  114: 304 */       ColumnInfoRecord ci1 = (ColumnInfoRecord)o1;
/*  115: 305 */       ColumnInfoRecord ci2 = (ColumnInfoRecord)o2;
/*  116:     */       
/*  117: 307 */       return ci1.getColumn() - ci2.getColumn();
/*  118:     */     }
/*  119:     */   }
/*  120:     */   
/*  121:     */   public WritableSheetImpl(String n, File of, FormattingRecords fr, SharedStrings ss, WorkbookSettings ws, WritableWorkbookImpl ww)
/*  122:     */   {
/*  123: 328 */     this.name = validateName(n);
/*  124: 329 */     this.outputFile = of;
/*  125: 330 */     this.rows = new RowRecord[0];
/*  126: 331 */     this.numRows = 0;
/*  127: 332 */     this.numColumns = 0;
/*  128: 333 */     this.chartOnly = false;
/*  129: 334 */     this.workbook = ww;
/*  130:     */     
/*  131: 336 */     this.formatRecords = fr;
/*  132: 337 */     this.sharedStrings = ss;
/*  133: 338 */     this.workbookSettings = ws;
/*  134: 339 */     this.drawingsModified = false;
/*  135: 340 */     this.columnFormats = new TreeSet(new ColumnInfoComparator(null));
/*  136: 341 */     this.autosizedColumns = new TreeSet();
/*  137: 342 */     this.hyperlinks = new ArrayList();
/*  138: 343 */     this.mergedCells = new MergedCells(this);
/*  139: 344 */     this.rowBreaks = new ArrayList();
/*  140: 345 */     this.columnBreaks = new ArrayList();
/*  141: 346 */     this.drawings = new ArrayList();
/*  142: 347 */     this.images = new ArrayList();
/*  143: 348 */     this.conditionalFormats = new ArrayList();
/*  144: 349 */     this.validatedCells = new ArrayList();
/*  145: 350 */     this.settings = new SheetSettings(this);
/*  146:     */     
/*  147:     */ 
/*  148: 353 */     this.sheetWriter = new SheetWriter(this.outputFile, 
/*  149: 354 */       this, 
/*  150: 355 */       this.workbookSettings);
/*  151:     */   }
/*  152:     */   
/*  153:     */   public Cell getCell(String loc)
/*  154:     */   {
/*  155: 367 */     return getCell(CellReferenceHelper.getColumn(loc), 
/*  156: 368 */       CellReferenceHelper.getRow(loc));
/*  157:     */   }
/*  158:     */   
/*  159:     */   public Cell getCell(int column, int row)
/*  160:     */   {
/*  161: 380 */     return getWritableCell(column, row);
/*  162:     */   }
/*  163:     */   
/*  164:     */   public WritableCell getWritableCell(String loc)
/*  165:     */   {
/*  166: 395 */     return getWritableCell(CellReferenceHelper.getColumn(loc), 
/*  167: 396 */       CellReferenceHelper.getRow(loc));
/*  168:     */   }
/*  169:     */   
/*  170:     */   public WritableCell getWritableCell(int column, int row)
/*  171:     */   {
/*  172: 408 */     WritableCell c = null;
/*  173: 410 */     if ((row < this.rows.length) && (this.rows[row] != null)) {
/*  174: 412 */       c = this.rows[row].getCell(column);
/*  175:     */     }
/*  176: 415 */     if (c == null) {
/*  177: 417 */       c = new EmptyCell(column, row);
/*  178:     */     }
/*  179: 420 */     return c;
/*  180:     */   }
/*  181:     */   
/*  182:     */   public int getRows()
/*  183:     */   {
/*  184: 430 */     return this.numRows;
/*  185:     */   }
/*  186:     */   
/*  187:     */   public int getColumns()
/*  188:     */   {
/*  189: 440 */     return this.numColumns;
/*  190:     */   }
/*  191:     */   
/*  192:     */   public Cell findCell(String contents)
/*  193:     */   {
/*  194: 454 */     CellFinder cellFinder = new CellFinder(this);
/*  195: 455 */     return cellFinder.findCell(contents);
/*  196:     */   }
/*  197:     */   
/*  198:     */   public Cell findCell(String contents, int firstCol, int firstRow, int lastCol, int lastRow, boolean reverse)
/*  199:     */   {
/*  200: 479 */     CellFinder cellFinder = new CellFinder(this);
/*  201: 480 */     return cellFinder.findCell(contents, 
/*  202: 481 */       firstCol, 
/*  203: 482 */       firstRow, 
/*  204: 483 */       lastCol, 
/*  205: 484 */       lastRow, 
/*  206: 485 */       reverse);
/*  207:     */   }
/*  208:     */   
/*  209:     */   public Cell findCell(Pattern pattern, int firstCol, int firstRow, int lastCol, int lastRow, boolean reverse)
/*  210:     */   {
/*  211: 509 */     CellFinder cellFinder = new CellFinder(this);
/*  212: 510 */     return cellFinder.findCell(pattern, 
/*  213: 511 */       firstCol, 
/*  214: 512 */       firstRow, 
/*  215: 513 */       lastCol, 
/*  216: 514 */       lastRow, 
/*  217: 515 */       reverse);
/*  218:     */   }
/*  219:     */   
/*  220:     */   public LabelCell findLabelCell(String contents)
/*  221:     */   {
/*  222: 532 */     CellFinder cellFinder = new CellFinder(this);
/*  223: 533 */     return cellFinder.findLabelCell(contents);
/*  224:     */   }
/*  225:     */   
/*  226:     */   public Cell[] getRow(int row)
/*  227:     */   {
/*  228: 545 */     boolean found = false;
/*  229: 546 */     int col = this.numColumns - 1;
/*  230: 547 */     while ((col >= 0) && (!found)) {
/*  231: 549 */       if (getCell(col, row).getType() != CellType.EMPTY) {
/*  232: 551 */         found = true;
/*  233:     */       } else {
/*  234: 555 */         col--;
/*  235:     */       }
/*  236:     */     }
/*  237: 560 */     Cell[] cells = new Cell[col + 1];
/*  238: 562 */     for (int i = 0; i <= col; i++) {
/*  239: 564 */       cells[i] = getCell(i, row);
/*  240:     */     }
/*  241: 566 */     return cells;
/*  242:     */   }
/*  243:     */   
/*  244:     */   public Cell[] getColumn(int col)
/*  245:     */   {
/*  246: 578 */     boolean found = false;
/*  247: 579 */     int row = this.numRows - 1;
/*  248: 581 */     while ((row >= 0) && (!found)) {
/*  249: 583 */       if (getCell(col, row).getType() != CellType.EMPTY) {
/*  250: 585 */         found = true;
/*  251:     */       } else {
/*  252: 589 */         row--;
/*  253:     */       }
/*  254:     */     }
/*  255: 594 */     Cell[] cells = new Cell[row + 1];
/*  256: 596 */     for (int i = 0; i <= row; i++) {
/*  257: 598 */       cells[i] = getCell(col, i);
/*  258:     */     }
/*  259: 600 */     return cells;
/*  260:     */   }
/*  261:     */   
/*  262:     */   public String getName()
/*  263:     */   {
/*  264: 610 */     return this.name;
/*  265:     */   }
/*  266:     */   
/*  267:     */   public void insertRow(int row)
/*  268:     */   {
/*  269: 621 */     if ((row < 0) || (row >= this.numRows)) {
/*  270: 623 */       return;
/*  271:     */     }
/*  272: 627 */     RowRecord[] oldRows = this.rows;
/*  273: 629 */     if (this.numRows == this.rows.length) {
/*  274: 631 */       this.rows = new RowRecord[oldRows.length + 10];
/*  275:     */     } else {
/*  276: 635 */       this.rows = new RowRecord[oldRows.length];
/*  277:     */     }
/*  278: 639 */     System.arraycopy(oldRows, 0, this.rows, 0, row);
/*  279:     */     
/*  280:     */ 
/*  281: 642 */     System.arraycopy(oldRows, row, this.rows, row + 1, this.numRows - row);
/*  282: 645 */     for (int i = row + 1; i <= this.numRows; i++) {
/*  283: 647 */       if (this.rows[i] != null) {
/*  284: 649 */         this.rows[i].incrementRow();
/*  285:     */       }
/*  286:     */     }
/*  287: 654 */     HyperlinkRecord hr = null;
/*  288: 655 */     Iterator i = this.hyperlinks.iterator();
/*  289: 656 */     while (i.hasNext())
/*  290:     */     {
/*  291: 658 */       hr = (HyperlinkRecord)i.next();
/*  292: 659 */       hr.insertRow(row);
/*  293:     */     }
/*  294: 663 */     if (this.dataValidation != null) {
/*  295: 665 */       this.dataValidation.insertRow(row);
/*  296:     */     }
/*  297: 668 */     if ((this.validatedCells != null) && (this.validatedCells.size() > 0)) {
/*  298: 670 */       for (Iterator vci = this.validatedCells.iterator(); vci.hasNext();)
/*  299:     */       {
/*  300: 672 */         CellValue cv = (CellValue)vci.next();
/*  301: 673 */         CellFeatures cf = cv.getCellFeatures();
/*  302: 674 */         if (cf.getDVParser() != null) {
/*  303: 676 */           cf.getDVParser().insertRow(row);
/*  304:     */         }
/*  305:     */       }
/*  306:     */     }
/*  307: 682 */     this.mergedCells.insertRow(row);
/*  308:     */     
/*  309:     */ 
/*  310: 685 */     ArrayList newRowBreaks = new ArrayList();
/*  311: 686 */     Iterator ri = this.rowBreaks.iterator();
/*  312: 687 */     while (ri.hasNext())
/*  313:     */     {
/*  314: 689 */       int val = ((Integer)ri.next()).intValue();
/*  315: 690 */       if (val >= row) {
/*  316: 692 */         val++;
/*  317:     */       }
/*  318: 695 */       newRowBreaks.add(new Integer(val));
/*  319:     */     }
/*  320: 697 */     this.rowBreaks = newRowBreaks;
/*  321: 700 */     for (Iterator cfit = this.conditionalFormats.iterator(); cfit.hasNext();)
/*  322:     */     {
/*  323: 702 */       ConditionalFormat cf = (ConditionalFormat)cfit.next();
/*  324: 703 */       cf.insertRow(row);
/*  325:     */     }
/*  326: 707 */     if (this.workbookSettings.getFormulaAdjust()) {
/*  327: 709 */       this.workbook.rowInserted(this, row);
/*  328:     */     }
/*  329: 713 */     this.numRows += 1;
/*  330:     */   }
/*  331:     */   
/*  332:     */   public void insertColumn(int col)
/*  333:     */   {
/*  334: 726 */     if ((col < 0) || (col >= this.numColumns)) {
/*  335: 728 */       return;
/*  336:     */     }
/*  337: 732 */     for (int i = 0; i < this.numRows; i++) {
/*  338: 734 */       if (this.rows[i] != null) {
/*  339: 736 */         this.rows[i].insertColumn(col);
/*  340:     */       }
/*  341:     */     }
/*  342: 741 */     HyperlinkRecord hr = null;
/*  343: 742 */     Iterator i = this.hyperlinks.iterator();
/*  344: 743 */     while (i.hasNext())
/*  345:     */     {
/*  346: 745 */       hr = (HyperlinkRecord)i.next();
/*  347: 746 */       hr.insertColumn(col);
/*  348:     */     }
/*  349: 750 */     i = this.columnFormats.iterator();
/*  350: 751 */     while (i.hasNext())
/*  351:     */     {
/*  352: 753 */       ColumnInfoRecord cir = (ColumnInfoRecord)i.next();
/*  353: 755 */       if (cir.getColumn() >= col) {
/*  354: 757 */         cir.incrementColumn();
/*  355:     */       }
/*  356:     */     }
/*  357: 762 */     if (this.autosizedColumns.size() > 0)
/*  358:     */     {
/*  359: 764 */       TreeSet newAutosized = new TreeSet();
/*  360: 765 */       i = this.autosizedColumns.iterator();
/*  361: 766 */       while (i.hasNext())
/*  362:     */       {
/*  363: 768 */         Integer colnumber = (Integer)i.next();
/*  364: 770 */         if (colnumber.intValue() >= col) {
/*  365: 772 */           newAutosized.add(new Integer(colnumber.intValue() + 1));
/*  366:     */         } else {
/*  367: 776 */           newAutosized.add(colnumber);
/*  368:     */         }
/*  369:     */       }
/*  370: 779 */       this.autosizedColumns = newAutosized;
/*  371:     */     }
/*  372: 783 */     if (this.dataValidation != null) {
/*  373: 785 */       this.dataValidation.insertColumn(col);
/*  374:     */     }
/*  375: 788 */     if ((this.validatedCells != null) && (this.validatedCells.size() > 0)) {
/*  376: 790 */       for (Iterator vci = this.validatedCells.iterator(); vci.hasNext();)
/*  377:     */       {
/*  378: 792 */         CellValue cv = (CellValue)vci.next();
/*  379: 793 */         CellFeatures cf = cv.getCellFeatures();
/*  380: 794 */         if (cf.getDVParser() != null) {
/*  381: 796 */           cf.getDVParser().insertColumn(col);
/*  382:     */         }
/*  383:     */       }
/*  384:     */     }
/*  385: 802 */     this.mergedCells.insertColumn(col);
/*  386:     */     
/*  387:     */ 
/*  388: 805 */     ArrayList newColumnBreaks = new ArrayList();
/*  389: 806 */     Iterator ri = this.columnBreaks.iterator();
/*  390: 807 */     while (ri.hasNext())
/*  391:     */     {
/*  392: 809 */       int val = ((Integer)ri.next()).intValue();
/*  393: 810 */       if (val >= col) {
/*  394: 812 */         val++;
/*  395:     */       }
/*  396: 815 */       newColumnBreaks.add(new Integer(val));
/*  397:     */     }
/*  398: 817 */     this.columnBreaks = newColumnBreaks;
/*  399: 820 */     for (Iterator cfit = this.conditionalFormats.iterator(); cfit.hasNext();)
/*  400:     */     {
/*  401: 822 */       ConditionalFormat cf = (ConditionalFormat)cfit.next();
/*  402: 823 */       cf.insertColumn(col);
/*  403:     */     }
/*  404: 827 */     if (this.workbookSettings.getFormulaAdjust()) {
/*  405: 829 */       this.workbook.columnInserted(this, col);
/*  406:     */     }
/*  407: 832 */     this.numColumns += 1;
/*  408:     */   }
/*  409:     */   
/*  410:     */   public void removeColumn(int col)
/*  411:     */   {
/*  412: 843 */     if ((col < 0) || (col >= this.numColumns)) {
/*  413: 845 */       return;
/*  414:     */     }
/*  415: 849 */     for (int i = 0; i < this.numRows; i++) {
/*  416: 851 */       if (this.rows[i] != null) {
/*  417: 853 */         this.rows[i].removeColumn(col);
/*  418:     */       }
/*  419:     */     }
/*  420: 858 */     HyperlinkRecord hr = null;
/*  421: 859 */     Iterator i = this.hyperlinks.iterator();
/*  422: 860 */     while (i.hasNext())
/*  423:     */     {
/*  424: 862 */       hr = (HyperlinkRecord)i.next();
/*  425: 864 */       if ((hr.getColumn() == col) && 
/*  426: 865 */         (hr.getLastColumn() == col)) {
/*  427: 869 */         i.remove();
/*  428:     */       } else {
/*  429: 873 */         hr.removeColumn(col);
/*  430:     */       }
/*  431:     */     }
/*  432: 878 */     if (this.dataValidation != null) {
/*  433: 880 */       this.dataValidation.removeColumn(col);
/*  434:     */     }
/*  435: 883 */     if ((this.validatedCells != null) && (this.validatedCells.size() > 0)) {
/*  436: 885 */       for (Iterator vci = this.validatedCells.iterator(); vci.hasNext();)
/*  437:     */       {
/*  438: 887 */         CellValue cv = (CellValue)vci.next();
/*  439: 888 */         CellFeatures cf = cv.getCellFeatures();
/*  440: 889 */         if (cf.getDVParser() != null) {
/*  441: 891 */           cf.getDVParser().removeColumn(col);
/*  442:     */         }
/*  443:     */       }
/*  444:     */     }
/*  445: 897 */     this.mergedCells.removeColumn(col);
/*  446:     */     
/*  447:     */ 
/*  448: 900 */     ArrayList newColumnBreaks = new ArrayList();
/*  449: 901 */     Iterator ri = this.columnBreaks.iterator();
/*  450: 902 */     while (ri.hasNext())
/*  451:     */     {
/*  452: 904 */       int val = ((Integer)ri.next()).intValue();
/*  453: 906 */       if (val != col)
/*  454:     */       {
/*  455: 908 */         if (val > col) {
/*  456: 910 */           val--;
/*  457:     */         }
/*  458: 913 */         newColumnBreaks.add(new Integer(val));
/*  459:     */       }
/*  460:     */     }
/*  461: 917 */     this.columnBreaks = newColumnBreaks;
/*  462:     */     
/*  463:     */ 
/*  464:     */ 
/*  465: 921 */     i = this.columnFormats.iterator();
/*  466: 922 */     ColumnInfoRecord removeColumn = null;
/*  467: 923 */     while (i.hasNext())
/*  468:     */     {
/*  469: 925 */       ColumnInfoRecord cir = (ColumnInfoRecord)i.next();
/*  470: 927 */       if (cir.getColumn() == col) {
/*  471: 929 */         removeColumn = cir;
/*  472: 931 */       } else if (cir.getColumn() > col) {
/*  473: 933 */         cir.decrementColumn();
/*  474:     */       }
/*  475:     */     }
/*  476: 937 */     if (removeColumn != null) {
/*  477: 939 */       this.columnFormats.remove(removeColumn);
/*  478:     */     }
/*  479: 943 */     if (this.autosizedColumns.size() > 0)
/*  480:     */     {
/*  481: 945 */       TreeSet newAutosized = new TreeSet();
/*  482: 946 */       i = this.autosizedColumns.iterator();
/*  483: 947 */       while (i.hasNext())
/*  484:     */       {
/*  485: 949 */         Integer colnumber = (Integer)i.next();
/*  486: 951 */         if (colnumber.intValue() != col) {
/*  487: 955 */           if (colnumber.intValue() > col) {
/*  488: 957 */             newAutosized.add(new Integer(colnumber.intValue() - 1));
/*  489:     */           } else {
/*  490: 961 */             newAutosized.add(colnumber);
/*  491:     */           }
/*  492:     */         }
/*  493:     */       }
/*  494: 964 */       this.autosizedColumns = newAutosized;
/*  495:     */     }
/*  496: 968 */     for (Iterator cfit = this.conditionalFormats.iterator(); cfit.hasNext();)
/*  497:     */     {
/*  498: 970 */       ConditionalFormat cf = (ConditionalFormat)cfit.next();
/*  499: 971 */       cf.removeColumn(col);
/*  500:     */     }
/*  501: 975 */     if (this.workbookSettings.getFormulaAdjust()) {
/*  502: 977 */       this.workbook.columnRemoved(this, col);
/*  503:     */     }
/*  504: 980 */     this.numColumns -= 1;
/*  505:     */   }
/*  506:     */   
/*  507:     */   public void removeRow(int row)
/*  508:     */   {
/*  509: 991 */     if ((row < 0) || (row >= this.numRows))
/*  510:     */     {
/*  511: 994 */       if (this.workbookSettings.getFormulaAdjust()) {
/*  512: 996 */         this.workbook.rowRemoved(this, row);
/*  513:     */       }
/*  514: 999 */       return;
/*  515:     */     }
/*  516:1003 */     RowRecord[] oldRows = this.rows;
/*  517:     */     
/*  518:1005 */     this.rows = new RowRecord[oldRows.length];
/*  519:     */     
/*  520:     */ 
/*  521:1008 */     System.arraycopy(oldRows, 0, this.rows, 0, row);
/*  522:     */     
/*  523:     */ 
/*  524:1011 */     System.arraycopy(oldRows, row + 1, this.rows, row, this.numRows - (row + 1));
/*  525:1014 */     for (int i = row; i < this.numRows; i++) {
/*  526:1016 */       if (this.rows[i] != null) {
/*  527:1018 */         this.rows[i].decrementRow();
/*  528:     */       }
/*  529:     */     }
/*  530:1023 */     HyperlinkRecord hr = null;
/*  531:1024 */     Iterator i = this.hyperlinks.iterator();
/*  532:1025 */     while (i.hasNext())
/*  533:     */     {
/*  534:1027 */       hr = (HyperlinkRecord)i.next();
/*  535:1029 */       if ((hr.getRow() == row) && 
/*  536:1030 */         (hr.getLastRow() == row)) {
/*  537:1034 */         i.remove();
/*  538:     */       } else {
/*  539:1038 */         hr.removeRow(row);
/*  540:     */       }
/*  541:     */     }
/*  542:1043 */     if (this.dataValidation != null) {
/*  543:1045 */       this.dataValidation.removeRow(row);
/*  544:     */     }
/*  545:1048 */     if ((this.validatedCells != null) && (this.validatedCells.size() > 0)) {
/*  546:1050 */       for (Iterator vci = this.validatedCells.iterator(); vci.hasNext();)
/*  547:     */       {
/*  548:1052 */         CellValue cv = (CellValue)vci.next();
/*  549:1053 */         CellFeatures cf = cv.getCellFeatures();
/*  550:1054 */         if (cf.getDVParser() != null) {
/*  551:1056 */           cf.getDVParser().removeRow(row);
/*  552:     */         }
/*  553:     */       }
/*  554:     */     }
/*  555:1062 */     this.mergedCells.removeRow(row);
/*  556:     */     
/*  557:     */ 
/*  558:1065 */     ArrayList newRowBreaks = new ArrayList();
/*  559:1066 */     Iterator ri = this.rowBreaks.iterator();
/*  560:1067 */     while (ri.hasNext())
/*  561:     */     {
/*  562:1069 */       int val = ((Integer)ri.next()).intValue();
/*  563:1071 */       if (val != row)
/*  564:     */       {
/*  565:1073 */         if (val > row) {
/*  566:1075 */           val--;
/*  567:     */         }
/*  568:1078 */         newRowBreaks.add(new Integer(val));
/*  569:     */       }
/*  570:     */     }
/*  571:1082 */     this.rowBreaks = newRowBreaks;
/*  572:1085 */     for (Iterator cfit = this.conditionalFormats.iterator(); cfit.hasNext();)
/*  573:     */     {
/*  574:1087 */       ConditionalFormat cf = (ConditionalFormat)cfit.next();
/*  575:1088 */       cf.removeRow(row);
/*  576:     */     }
/*  577:1092 */     if (this.workbookSettings.getFormulaAdjust()) {
/*  578:1094 */       this.workbook.rowRemoved(this, row);
/*  579:     */     }
/*  580:1110 */     this.numRows -= 1;
/*  581:     */   }
/*  582:     */   
/*  583:     */   public void addCell(WritableCell cell)
/*  584:     */     throws WriteException, RowsExceededException
/*  585:     */   {
/*  586:1133 */     if (cell.getType() == CellType.EMPTY) {
/*  587:1135 */       if ((cell != null) && (cell.getCellFormat() == null)) {
/*  588:1139 */         return;
/*  589:     */       }
/*  590:     */     }
/*  591:1143 */     CellValue cv = (CellValue)cell;
/*  592:1145 */     if (cv.isReferenced()) {
/*  593:1147 */       throw new JxlWriteException(JxlWriteException.cellReferenced);
/*  594:     */     }
/*  595:1150 */     int row = cell.getRow();
/*  596:1151 */     RowRecord rowrec = getRowRecord(row);
/*  597:     */     
/*  598:1153 */     CellValue curcell = rowrec.getCell(cv.getColumn());
/*  599:1154 */     boolean curSharedValidation = (curcell != null) && 
/*  600:1155 */       (curcell.getCellFeatures() != null) && 
/*  601:1156 */       (curcell.getCellFeatures().getDVParser() != null) && 
/*  602:1157 */       (curcell.getCellFeatures().getDVParser().extendedCellsValidation());
/*  603:1161 */     if ((cell.getCellFeatures() != null) && 
/*  604:1162 */       (cell.getCellFeatures().hasDataValidation()) && 
/*  605:1163 */       (curSharedValidation))
/*  606:     */     {
/*  607:1165 */       DVParser dvp = curcell.getCellFeatures().getDVParser();
/*  608:1166 */       logger.warn("Cannot add cell at " + 
/*  609:1167 */         CellReferenceHelper.getCellReference(cv) + 
/*  610:1168 */         " because it is part of the shared cell validation group " + 
/*  611:1169 */         CellReferenceHelper.getCellReference(dvp.getFirstColumn(), 
/*  612:1170 */         dvp.getFirstRow()) + 
/*  613:1171 */         "-" + 
/*  614:1172 */         CellReferenceHelper.getCellReference(dvp.getLastColumn(), 
/*  615:1173 */         dvp.getLastRow()));
/*  616:1174 */       return;
/*  617:     */     }
/*  618:1178 */     if (curSharedValidation)
/*  619:     */     {
/*  620:1180 */       WritableCellFeatures wcf = cell.getWritableCellFeatures();
/*  621:1182 */       if (wcf == null)
/*  622:     */       {
/*  623:1184 */         wcf = new WritableCellFeatures();
/*  624:1185 */         cell.setCellFeatures(wcf);
/*  625:     */       }
/*  626:1188 */       wcf.shareDataValidation(curcell.getCellFeatures());
/*  627:     */     }
/*  628:1191 */     rowrec.addCell(cv);
/*  629:     */     
/*  630:     */ 
/*  631:1194 */     this.numRows = Math.max(row + 1, this.numRows);
/*  632:1195 */     this.numColumns = Math.max(this.numColumns, rowrec.getMaxColumn());
/*  633:     */     
/*  634:     */ 
/*  635:     */ 
/*  636:1199 */     cv.setCellDetails(this.formatRecords, this.sharedStrings, this);
/*  637:     */   }
/*  638:     */   
/*  639:     */   RowRecord getRowRecord(int row)
/*  640:     */     throws RowsExceededException
/*  641:     */   {
/*  642:1212 */     if (row >= 65536) {
/*  643:1214 */       throw new RowsExceededException();
/*  644:     */     }
/*  645:1220 */     if (row >= this.rows.length)
/*  646:     */     {
/*  647:1222 */       RowRecord[] oldRows = this.rows;
/*  648:1223 */       this.rows = new RowRecord[Math.max(oldRows.length + 10, row + 1)];
/*  649:1224 */       System.arraycopy(oldRows, 0, this.rows, 0, oldRows.length);
/*  650:1225 */       oldRows = (RowRecord[])null;
/*  651:     */     }
/*  652:1228 */     RowRecord rowrec = this.rows[row];
/*  653:1230 */     if (rowrec == null)
/*  654:     */     {
/*  655:1232 */       rowrec = new RowRecord(row, this);
/*  656:1233 */       this.rows[row] = rowrec;
/*  657:     */     }
/*  658:1236 */     return rowrec;
/*  659:     */   }
/*  660:     */   
/*  661:     */   RowRecord getRowInfo(int r)
/*  662:     */   {
/*  663:1247 */     if ((r < 0) || (r > this.rows.length)) {
/*  664:1249 */       return null;
/*  665:     */     }
/*  666:1252 */     return this.rows[r];
/*  667:     */   }
/*  668:     */   
/*  669:     */   ColumnInfoRecord getColumnInfo(int c)
/*  670:     */   {
/*  671:1263 */     Iterator i = this.columnFormats.iterator();
/*  672:1264 */     ColumnInfoRecord cir = null;
/*  673:1265 */     boolean stop = false;
/*  674:1267 */     while ((i.hasNext()) && (!stop))
/*  675:     */     {
/*  676:1269 */       cir = (ColumnInfoRecord)i.next();
/*  677:1271 */       if (cir.getColumn() >= c) {
/*  678:1273 */         stop = true;
/*  679:     */       }
/*  680:     */     }
/*  681:1277 */     if (!stop) {
/*  682:1279 */       return null;
/*  683:     */     }
/*  684:1282 */     return cir.getColumn() == c ? cir : null;
/*  685:     */   }
/*  686:     */   
/*  687:     */   public void setName(String n)
/*  688:     */   {
/*  689:1292 */     this.name = n;
/*  690:     */   }
/*  691:     */   
/*  692:     */   /**
/*  693:     */    * @deprecated
/*  694:     */    */
/*  695:     */   public void setHidden(boolean h)
/*  696:     */   {
/*  697:1303 */     this.settings.setHidden(h);
/*  698:     */   }
/*  699:     */   
/*  700:     */   /**
/*  701:     */    * @deprecated
/*  702:     */    */
/*  703:     */   public void setProtected(boolean prot)
/*  704:     */   {
/*  705:1314 */     this.settings.setProtected(prot);
/*  706:     */   }
/*  707:     */   
/*  708:     */   /**
/*  709:     */    * @deprecated
/*  710:     */    */
/*  711:     */   public void setSelected()
/*  712:     */   {
/*  713:1323 */     this.settings.setSelected();
/*  714:     */   }
/*  715:     */   
/*  716:     */   /**
/*  717:     */    * @deprecated
/*  718:     */    */
/*  719:     */   public boolean isHidden()
/*  720:     */   {
/*  721:1334 */     return this.settings.isHidden();
/*  722:     */   }
/*  723:     */   
/*  724:     */   public void setColumnView(int col, int width)
/*  725:     */   {
/*  726:1345 */     CellView cv = new CellView();
/*  727:1346 */     cv.setSize(width * 256);
/*  728:1347 */     setColumnView(col, cv);
/*  729:     */   }
/*  730:     */   
/*  731:     */   public void setColumnView(int col, int width, CellFormat format)
/*  732:     */   {
/*  733:1360 */     CellView cv = new CellView();
/*  734:1361 */     cv.setSize(width * 256);
/*  735:1362 */     cv.setFormat(format);
/*  736:1363 */     setColumnView(col, cv);
/*  737:     */   }
/*  738:     */   
/*  739:     */   public void setColumnView(int col, CellView view)
/*  740:     */   {
/*  741:1374 */     XFRecord xfr = (XFRecord)view.getFormat();
/*  742:1375 */     if (xfr == null)
/*  743:     */     {
/*  744:1377 */       Styles styles = getWorkbook().getStyles();
/*  745:1378 */       xfr = styles.getNormalStyle();
/*  746:     */     }
/*  747:     */     try
/*  748:     */     {
/*  749:1383 */       if (!xfr.isInitialized()) {
/*  750:1385 */         this.formatRecords.addStyle(xfr);
/*  751:     */       }
/*  752:1388 */       int width = view.depUsed() ? view.getDimension() * 256 : view.getSize();
/*  753:1390 */       if (view.isAutosize()) {
/*  754:1392 */         this.autosizedColumns.add(new Integer(col));
/*  755:     */       }
/*  756:1395 */       ColumnInfoRecord cir = new ColumnInfoRecord(col, 
/*  757:1396 */         width, 
/*  758:1397 */         xfr);
/*  759:1399 */       if (view.isHidden()) {
/*  760:1401 */         cir.setHidden(true);
/*  761:     */       }
/*  762:1404 */       if (!this.columnFormats.contains(cir))
/*  763:     */       {
/*  764:1406 */         this.columnFormats.add(cir);
/*  765:     */       }
/*  766:     */       else
/*  767:     */       {
/*  768:1410 */         this.columnFormats.remove(cir);
/*  769:1411 */         this.columnFormats.add(cir);
/*  770:     */       }
/*  771:     */     }
/*  772:     */     catch (NumFormatRecordsException e)
/*  773:     */     {
/*  774:1416 */       logger.warn("Maximum number of format records exceeded.  Using default format.");
/*  775:     */       
/*  776:     */ 
/*  777:1419 */       ColumnInfoRecord cir = new ColumnInfoRecord(
/*  778:1420 */         col, view.getDimension() * 256, WritableWorkbook.NORMAL_STYLE);
/*  779:1421 */       if (!this.columnFormats.contains(cir)) {
/*  780:1423 */         this.columnFormats.add(cir);
/*  781:     */       }
/*  782:     */     }
/*  783:     */   }
/*  784:     */   
/*  785:     */   /**
/*  786:     */    * @deprecated
/*  787:     */    */
/*  788:     */   public void setRowView(int row, int height)
/*  789:     */     throws RowsExceededException
/*  790:     */   {
/*  791:1439 */     CellView cv = new CellView();
/*  792:1440 */     cv.setSize(height);
/*  793:1441 */     cv.setHidden(false);
/*  794:1442 */     setRowView(row, cv);
/*  795:     */   }
/*  796:     */   
/*  797:     */   /**
/*  798:     */    * @deprecated
/*  799:     */    */
/*  800:     */   public void setRowView(int row, boolean collapsed)
/*  801:     */     throws RowsExceededException
/*  802:     */   {
/*  803:1456 */     CellView cv = new CellView();
/*  804:1457 */     cv.setHidden(collapsed);
/*  805:1458 */     setRowView(row, cv);
/*  806:     */   }
/*  807:     */   
/*  808:     */   /**
/*  809:     */    * @deprecated
/*  810:     */    */
/*  811:     */   public void setRowView(int row, int height, boolean collapsed)
/*  812:     */     throws RowsExceededException
/*  813:     */   {
/*  814:1475 */     CellView cv = new CellView();
/*  815:1476 */     cv.setSize(height);
/*  816:1477 */     cv.setHidden(collapsed);
/*  817:1478 */     setRowView(row, cv);
/*  818:     */   }
/*  819:     */   
/*  820:     */   public void setRowView(int row, CellView view)
/*  821:     */     throws RowsExceededException
/*  822:     */   {
/*  823:1490 */     RowRecord rowrec = getRowRecord(row);
/*  824:     */     
/*  825:1492 */     XFRecord xfr = (XFRecord)view.getFormat();
/*  826:     */     try
/*  827:     */     {
/*  828:1496 */       if (xfr != null) {
/*  829:1498 */         if (!xfr.isInitialized()) {
/*  830:1500 */           this.formatRecords.addStyle(xfr);
/*  831:     */         }
/*  832:     */       }
/*  833:     */     }
/*  834:     */     catch (NumFormatRecordsException e)
/*  835:     */     {
/*  836:1506 */       logger.warn("Maximum number of format records exceeded.  Using default format.");
/*  837:     */       
/*  838:     */ 
/*  839:1509 */       xfr = null;
/*  840:     */     }
/*  841:1512 */     rowrec.setRowDetails(view.getSize(), 
/*  842:1513 */       false, 
/*  843:1514 */       view.isHidden(), 
/*  844:1515 */       0, 
/*  845:1516 */       false, 
/*  846:1517 */       xfr);
/*  847:1518 */     this.numRows = Math.max(this.numRows, row + 1);
/*  848:     */   }
/*  849:     */   
/*  850:     */   public void write()
/*  851:     */     throws IOException
/*  852:     */   {
/*  853:1530 */     boolean dmod = this.drawingsModified;
/*  854:1531 */     if (this.workbook.getDrawingGroup() != null) {
/*  855:1533 */       dmod |= this.workbook.getDrawingGroup().hasDrawingsOmitted();
/*  856:     */     }
/*  857:1536 */     if (this.autosizedColumns.size() > 0) {
/*  858:1538 */       autosizeColumns();
/*  859:     */     }
/*  860:1541 */     this.sheetWriter.setWriteData(this.rows, 
/*  861:1542 */       this.rowBreaks, 
/*  862:1543 */       this.columnBreaks, 
/*  863:1544 */       this.hyperlinks, 
/*  864:1545 */       this.mergedCells, 
/*  865:1546 */       this.columnFormats, 
/*  866:1547 */       this.maxRowOutlineLevel, 
/*  867:1548 */       this.maxColumnOutlineLevel);
/*  868:1549 */     this.sheetWriter.setDimensions(getRows(), getColumns());
/*  869:1550 */     this.sheetWriter.setSettings(this.settings);
/*  870:1551 */     this.sheetWriter.setPLS(this.plsRecord);
/*  871:1552 */     this.sheetWriter.setDrawings(this.drawings, dmod);
/*  872:1553 */     this.sheetWriter.setButtonPropertySet(this.buttonPropertySet);
/*  873:1554 */     this.sheetWriter.setDataValidation(this.dataValidation, this.validatedCells);
/*  874:1555 */     this.sheetWriter.setConditionalFormats(this.conditionalFormats);
/*  875:1556 */     this.sheetWriter.setAutoFilter(this.autoFilter);
/*  876:     */     
/*  877:1558 */     this.sheetWriter.write();
/*  878:     */   }
/*  879:     */   
/*  880:     */   void copy(Sheet s)
/*  881:     */   {
/*  882:1569 */     this.settings = new SheetSettings(s.getSettings(), this);
/*  883:     */     
/*  884:1571 */     SheetCopier si = new SheetCopier(s, this);
/*  885:1572 */     si.setColumnFormats(this.columnFormats);
/*  886:1573 */     si.setFormatRecords(this.formatRecords);
/*  887:1574 */     si.setHyperlinks(this.hyperlinks);
/*  888:1575 */     si.setMergedCells(this.mergedCells);
/*  889:1576 */     si.setRowBreaks(this.rowBreaks);
/*  890:1577 */     si.setColumnBreaks(this.columnBreaks);
/*  891:1578 */     si.setSheetWriter(this.sheetWriter);
/*  892:1579 */     si.setDrawings(this.drawings);
/*  893:1580 */     si.setImages(this.images);
/*  894:1581 */     si.setConditionalFormats(this.conditionalFormats);
/*  895:1582 */     si.setValidatedCells(this.validatedCells);
/*  896:     */     
/*  897:1584 */     si.copySheet();
/*  898:     */     
/*  899:1586 */     this.dataValidation = si.getDataValidation();
/*  900:1587 */     this.comboBox = si.getComboBox();
/*  901:1588 */     this.plsRecord = si.getPLSRecord();
/*  902:1589 */     this.chartOnly = si.isChartOnly();
/*  903:1590 */     this.buttonPropertySet = si.getButtonPropertySet();
/*  904:1591 */     this.numRows = si.getRows();
/*  905:1592 */     this.autoFilter = si.getAutoFilter();
/*  906:1593 */     this.maxRowOutlineLevel = si.getMaxRowOutlineLevel();
/*  907:1594 */     this.maxColumnOutlineLevel = si.getMaxColumnOutlineLevel();
/*  908:     */   }
/*  909:     */   
/*  910:     */   void copy(WritableSheet s)
/*  911:     */   {
/*  912:1604 */     this.settings = new SheetSettings(s.getSettings(), this);
/*  913:1605 */     WritableSheetImpl si = (WritableSheetImpl)s;
/*  914:     */     
/*  915:1607 */     WritableSheetCopier sc = new WritableSheetCopier(s, this);
/*  916:1608 */     sc.setColumnFormats(si.columnFormats, this.columnFormats);
/*  917:1609 */     sc.setMergedCells(si.mergedCells, this.mergedCells);
/*  918:1610 */     sc.setRows(si.rows);
/*  919:1611 */     sc.setRowBreaks(si.rowBreaks, this.rowBreaks);
/*  920:1612 */     sc.setColumnBreaks(si.columnBreaks, this.columnBreaks);
/*  921:1613 */     sc.setDataValidation(si.dataValidation);
/*  922:1614 */     sc.setSheetWriter(this.sheetWriter);
/*  923:1615 */     sc.setDrawings(si.drawings, this.drawings, this.images);
/*  924:1616 */     sc.setWorkspaceOptions(si.getWorkspaceOptions());
/*  925:1617 */     sc.setPLSRecord(si.plsRecord);
/*  926:1618 */     sc.setButtonPropertySetRecord(si.buttonPropertySet);
/*  927:1619 */     sc.setHyperlinks(si.hyperlinks, this.hyperlinks);
/*  928:1620 */     sc.setValidatedCells(this.validatedCells);
/*  929:     */     
/*  930:1622 */     sc.copySheet();
/*  931:     */     
/*  932:1624 */     this.dataValidation = sc.getDataValidation();
/*  933:1625 */     this.plsRecord = sc.getPLSRecord();
/*  934:1626 */     this.buttonPropertySet = sc.getButtonPropertySet();
/*  935:     */   }
/*  936:     */   
/*  937:     */   final HeaderRecord getHeader()
/*  938:     */   {
/*  939:1636 */     return this.sheetWriter.getHeader();
/*  940:     */   }
/*  941:     */   
/*  942:     */   final FooterRecord getFooter()
/*  943:     */   {
/*  944:1646 */     return this.sheetWriter.getFooter();
/*  945:     */   }
/*  946:     */   
/*  947:     */   /**
/*  948:     */    * @deprecated
/*  949:     */    */
/*  950:     */   public boolean isProtected()
/*  951:     */   {
/*  952:1656 */     return this.settings.isProtected();
/*  953:     */   }
/*  954:     */   
/*  955:     */   public Hyperlink[] getHyperlinks()
/*  956:     */   {
/*  957:1666 */     Hyperlink[] hl = new Hyperlink[this.hyperlinks.size()];
/*  958:1668 */     for (int i = 0; i < this.hyperlinks.size(); i++) {
/*  959:1670 */       hl[i] = ((Hyperlink)this.hyperlinks.get(i));
/*  960:     */     }
/*  961:1673 */     return hl;
/*  962:     */   }
/*  963:     */   
/*  964:     */   public Range[] getMergedCells()
/*  965:     */   {
/*  966:1683 */     return this.mergedCells.getMergedCells();
/*  967:     */   }
/*  968:     */   
/*  969:     */   public WritableHyperlink[] getWritableHyperlinks()
/*  970:     */   {
/*  971:1693 */     WritableHyperlink[] hl = new WritableHyperlink[this.hyperlinks.size()];
/*  972:1695 */     for (int i = 0; i < this.hyperlinks.size(); i++) {
/*  973:1697 */       hl[i] = ((WritableHyperlink)this.hyperlinks.get(i));
/*  974:     */     }
/*  975:1700 */     return hl;
/*  976:     */   }
/*  977:     */   
/*  978:     */   public void removeHyperlink(WritableHyperlink h)
/*  979:     */   {
/*  980:1717 */     removeHyperlink(h, false);
/*  981:     */   }
/*  982:     */   
/*  983:     */   public void removeHyperlink(WritableHyperlink h, boolean preserveLabel)
/*  984:     */   {
/*  985:1737 */     this.hyperlinks.remove(this.hyperlinks.indexOf(h));
/*  986:1739 */     if (!preserveLabel)
/*  987:     */     {
/*  988:1743 */       Assert.verify((this.rows.length > h.getRow()) && (this.rows[h.getRow()] != null));
/*  989:1744 */       this.rows[h.getRow()].removeCell(h.getColumn());
/*  990:     */     }
/*  991:     */   }
/*  992:     */   
/*  993:     */   public void addHyperlink(WritableHyperlink h)
/*  994:     */     throws WriteException, RowsExceededException
/*  995:     */   {
/*  996:1759 */     Cell c = getCell(h.getColumn(), h.getRow());
/*  997:     */     
/*  998:1761 */     String contents = null;
/*  999:1762 */     if ((h.isFile()) || (h.isUNC()))
/* 1000:     */     {
/* 1001:1764 */       String cnts = h.getContents();
/* 1002:1765 */       if (cnts == null) {
/* 1003:1767 */         contents = h.getFile().getPath();
/* 1004:     */       } else {
/* 1005:1771 */         contents = cnts;
/* 1006:     */       }
/* 1007:     */     }
/* 1008:1774 */     else if (h.isURL())
/* 1009:     */     {
/* 1010:1776 */       String cnts = h.getContents();
/* 1011:1777 */       if (cnts == null) {
/* 1012:1779 */         contents = h.getURL().toString();
/* 1013:     */       } else {
/* 1014:1783 */         contents = cnts;
/* 1015:     */       }
/* 1016:     */     }
/* 1017:1786 */     else if (h.isLocation())
/* 1018:     */     {
/* 1019:1788 */       contents = h.getContents();
/* 1020:     */     }
/* 1021:1795 */     if (c.getType() == CellType.LABEL)
/* 1022:     */     {
/* 1023:1797 */       Label l = (Label)c;
/* 1024:1798 */       l.setString(contents);
/* 1025:1799 */       WritableCellFormat wcf = new WritableCellFormat(l.getCellFormat());
/* 1026:1800 */       wcf.setFont(WritableWorkbook.HYPERLINK_FONT);
/* 1027:1801 */       l.setCellFormat(wcf);
/* 1028:     */     }
/* 1029:     */     else
/* 1030:     */     {
/* 1031:1805 */       Label l = new Label(h.getColumn(), h.getRow(), contents, 
/* 1032:1806 */         WritableWorkbook.HYPERLINK_STYLE);
/* 1033:1807 */       addCell(l);
/* 1034:     */     }
/* 1035:1811 */     for (int i = h.getRow(); i <= h.getLastRow(); i++) {
/* 1036:1813 */       for (int j = h.getColumn(); j <= h.getLastColumn(); j++) {
/* 1037:1815 */         if ((i != h.getRow()) && (j != h.getColumn())) {
/* 1038:1818 */           if ((this.rows.length < h.getLastColumn()) && (this.rows[i] != null)) {
/* 1039:1820 */             this.rows[i].removeCell(j);
/* 1040:     */           }
/* 1041:     */         }
/* 1042:     */       }
/* 1043:     */     }
/* 1044:1826 */     h.initialize(this);
/* 1045:1827 */     this.hyperlinks.add(h);
/* 1046:     */   }
/* 1047:     */   
/* 1048:     */   public Range mergeCells(int col1, int row1, int col2, int row2)
/* 1049:     */     throws WriteException, RowsExceededException
/* 1050:     */   {
/* 1051:1846 */     if ((col2 < col1) || (row2 < row1)) {
/* 1052:1848 */       logger.warn("Cannot merge cells - top left and bottom right incorrectly specified");
/* 1053:     */     }
/* 1054:1853 */     if ((col2 >= this.numColumns) || (row2 >= this.numRows)) {
/* 1055:1855 */       addCell(new Blank(col2, row2));
/* 1056:     */     }
/* 1057:1858 */     SheetRangeImpl range = new SheetRangeImpl(this, col1, row1, col2, row2);
/* 1058:1859 */     this.mergedCells.add(range);
/* 1059:     */     
/* 1060:1861 */     return range;
/* 1061:     */   }
/* 1062:     */   
/* 1063:     */   public void setRowGroup(int row1, int row2, boolean collapsed)
/* 1064:     */     throws WriteException, RowsExceededException
/* 1065:     */   {
/* 1066:1877 */     if (row2 < row1) {
/* 1067:1879 */       logger.warn("Cannot merge cells - top and bottom rows incorrectly specified");
/* 1068:     */     }
/* 1069:1883 */     for (int i = row1; i <= row2; i++)
/* 1070:     */     {
/* 1071:1885 */       RowRecord row = getRowRecord(i);
/* 1072:1886 */       this.numRows = Math.max(i + 1, this.numRows);
/* 1073:1887 */       row.incrementOutlineLevel();
/* 1074:1888 */       row.setCollapsed(collapsed);
/* 1075:1889 */       this.maxRowOutlineLevel = Math.max(this.maxRowOutlineLevel, 
/* 1076:1890 */         row.getOutlineLevel());
/* 1077:     */     }
/* 1078:     */   }
/* 1079:     */   
/* 1080:     */   public void unsetRowGroup(int row1, int row2)
/* 1081:     */     throws WriteException, RowsExceededException
/* 1082:     */   {
/* 1083:1905 */     if (row2 < row1) {
/* 1084:1907 */       logger.warn("Cannot merge cells - top and bottom rows incorrectly specified");
/* 1085:     */     }
/* 1086:1912 */     if (row2 >= this.numRows)
/* 1087:     */     {
/* 1088:1914 */       logger.warn(row2 + 
/* 1089:1915 */         " is greater than the sheet bounds");
/* 1090:1916 */       row2 = this.numRows - 1;
/* 1091:     */     }
/* 1092:1919 */     for (int i = row1; i <= row2; i++) {
/* 1093:1921 */       this.rows[i].decrementOutlineLevel();
/* 1094:     */     }
/* 1095:1925 */     this.maxRowOutlineLevel = 0;
/* 1096:1926 */     for (int i = this.rows.length; i-- > 0;) {
/* 1097:1928 */       this.maxRowOutlineLevel = Math.max(this.maxRowOutlineLevel, 
/* 1098:1929 */         this.rows[i].getOutlineLevel());
/* 1099:     */     }
/* 1100:     */   }
/* 1101:     */   
/* 1102:     */   public void setColumnGroup(int col1, int col2, boolean collapsed)
/* 1103:     */     throws WriteException, RowsExceededException
/* 1104:     */   {
/* 1105:1945 */     if (col2 < col1) {
/* 1106:1947 */       logger.warn("Cannot merge cells - top and bottom rows incorrectly specified");
/* 1107:     */     }
/* 1108:1951 */     for (int i = col1; i <= col2; i++)
/* 1109:     */     {
/* 1110:1953 */       ColumnInfoRecord cir = getColumnInfo(i);
/* 1111:1957 */       if (cir == null)
/* 1112:     */       {
/* 1113:1959 */         setColumnView(i, new CellView());
/* 1114:1960 */         cir = getColumnInfo(i);
/* 1115:     */       }
/* 1116:1963 */       cir.incrementOutlineLevel();
/* 1117:1964 */       cir.setCollapsed(collapsed);
/* 1118:1965 */       this.maxColumnOutlineLevel = Math.max(this.maxColumnOutlineLevel, 
/* 1119:1966 */         cir.getOutlineLevel());
/* 1120:     */     }
/* 1121:     */   }
/* 1122:     */   
/* 1123:     */   public void unsetColumnGroup(int col1, int col2)
/* 1124:     */     throws WriteException, RowsExceededException
/* 1125:     */   {
/* 1126:1981 */     if (col2 < col1) {
/* 1127:1983 */       logger.warn("Cannot merge cells - top and bottom rows incorrectly specified");
/* 1128:     */     }
/* 1129:1987 */     for (int i = col1; i <= col2; i++)
/* 1130:     */     {
/* 1131:1989 */       ColumnInfoRecord cir = getColumnInfo(i);
/* 1132:1990 */       cir.decrementOutlineLevel();
/* 1133:     */     }
/* 1134:1994 */     this.maxColumnOutlineLevel = 0;
/* 1135:1995 */     for (Iterator it = this.columnFormats.iterator(); it.hasNext();)
/* 1136:     */     {
/* 1137:1997 */       ColumnInfoRecord cir = (ColumnInfoRecord)it.next();
/* 1138:1998 */       this.maxColumnOutlineLevel = Math.max(this.maxColumnOutlineLevel, 
/* 1139:1999 */         cir.getOutlineLevel());
/* 1140:     */     }
/* 1141:     */   }
/* 1142:     */   
/* 1143:     */   public void unmergeCells(Range r)
/* 1144:     */   {
/* 1145:2011 */     this.mergedCells.unmergeCells(r);
/* 1146:     */   }
/* 1147:     */   
/* 1148:     */   /**
/* 1149:     */    * @deprecated
/* 1150:     */    */
/* 1151:     */   public void setHeader(String l, String c, String r)
/* 1152:     */   {
/* 1153:2024 */     HeaderFooter header = new HeaderFooter();
/* 1154:2025 */     header.getLeft().append(l);
/* 1155:2026 */     header.getCentre().append(c);
/* 1156:2027 */     header.getRight().append(r);
/* 1157:2028 */     this.settings.setHeader(header);
/* 1158:     */   }
/* 1159:     */   
/* 1160:     */   /**
/* 1161:     */    * @deprecated
/* 1162:     */    */
/* 1163:     */   public void setFooter(String l, String c, String r)
/* 1164:     */   {
/* 1165:2041 */     HeaderFooter footer = new HeaderFooter();
/* 1166:2042 */     footer.getLeft().append(l);
/* 1167:2043 */     footer.getCentre().append(c);
/* 1168:2044 */     footer.getRight().append(r);
/* 1169:2045 */     this.settings.setFooter(footer);
/* 1170:     */   }
/* 1171:     */   
/* 1172:     */   /**
/* 1173:     */    * @deprecated
/* 1174:     */    */
/* 1175:     */   public void setPageSetup(PageOrientation p)
/* 1176:     */   {
/* 1177:2056 */     this.settings.setOrientation(p);
/* 1178:     */   }
/* 1179:     */   
/* 1180:     */   /**
/* 1181:     */    * @deprecated
/* 1182:     */    */
/* 1183:     */   public void setPageSetup(PageOrientation p, double hm, double fm)
/* 1184:     */   {
/* 1185:2069 */     this.settings.setOrientation(p);
/* 1186:2070 */     this.settings.setHeaderMargin(hm);
/* 1187:2071 */     this.settings.setFooterMargin(fm);
/* 1188:     */   }
/* 1189:     */   
/* 1190:     */   /**
/* 1191:     */    * @deprecated
/* 1192:     */    */
/* 1193:     */   public void setPageSetup(PageOrientation p, PaperSize ps, double hm, double fm)
/* 1194:     */   {
/* 1195:2086 */     this.settings.setPaperSize(ps);
/* 1196:2087 */     this.settings.setOrientation(p);
/* 1197:2088 */     this.settings.setHeaderMargin(hm);
/* 1198:2089 */     this.settings.setFooterMargin(fm);
/* 1199:     */   }
/* 1200:     */   
/* 1201:     */   public SheetSettings getSettings()
/* 1202:     */   {
/* 1203:2099 */     return this.settings;
/* 1204:     */   }
/* 1205:     */   
/* 1206:     */   WorkbookSettings getWorkbookSettings()
/* 1207:     */   {
/* 1208:2107 */     return this.workbookSettings;
/* 1209:     */   }
/* 1210:     */   
/* 1211:     */   public void addRowPageBreak(int row)
/* 1212:     */   {
/* 1213:2118 */     Iterator i = this.rowBreaks.iterator();
/* 1214:2119 */     boolean found = false;
/* 1215:2121 */     while ((i.hasNext()) && (!found)) {
/* 1216:2123 */       if (((Integer)i.next()).intValue() == row) {
/* 1217:2125 */         found = true;
/* 1218:     */       }
/* 1219:     */     }
/* 1220:2129 */     if (!found) {
/* 1221:2131 */       this.rowBreaks.add(new Integer(row));
/* 1222:     */     }
/* 1223:     */   }
/* 1224:     */   
/* 1225:     */   public void addColumnPageBreak(int col)
/* 1226:     */   {
/* 1227:2143 */     Iterator i = this.columnBreaks.iterator();
/* 1228:2144 */     boolean found = false;
/* 1229:2146 */     while ((i.hasNext()) && (!found)) {
/* 1230:2148 */       if (((Integer)i.next()).intValue() == col) {
/* 1231:2150 */         found = true;
/* 1232:     */       }
/* 1233:     */     }
/* 1234:2154 */     if (!found) {
/* 1235:2156 */       this.columnBreaks.add(new Integer(col));
/* 1236:     */     }
/* 1237:     */   }
/* 1238:     */   
/* 1239:     */   Chart[] getCharts()
/* 1240:     */   {
/* 1241:2167 */     return this.sheetWriter.getCharts();
/* 1242:     */   }
/* 1243:     */   
/* 1244:     */   private DrawingGroupObject[] getDrawings()
/* 1245:     */   {
/* 1246:2177 */     DrawingGroupObject[] dr = new DrawingGroupObject[this.drawings.size()];
/* 1247:2178 */     dr = (DrawingGroupObject[])this.drawings.toArray(dr);
/* 1248:2179 */     return dr;
/* 1249:     */   }
/* 1250:     */   
/* 1251:     */   void checkMergedBorders()
/* 1252:     */   {
/* 1253:2190 */     this.sheetWriter.setWriteData(this.rows, 
/* 1254:2191 */       this.rowBreaks, 
/* 1255:2192 */       this.columnBreaks, 
/* 1256:2193 */       this.hyperlinks, 
/* 1257:2194 */       this.mergedCells, 
/* 1258:2195 */       this.columnFormats, 
/* 1259:2196 */       this.maxRowOutlineLevel, 
/* 1260:2197 */       this.maxColumnOutlineLevel);
/* 1261:2198 */     this.sheetWriter.setDimensions(getRows(), getColumns());
/* 1262:2199 */     this.sheetWriter.checkMergedBorders();
/* 1263:     */   }
/* 1264:     */   
/* 1265:     */   private WorkspaceInformationRecord getWorkspaceOptions()
/* 1266:     */   {
/* 1267:2209 */     return this.sheetWriter.getWorkspaceOptions();
/* 1268:     */   }
/* 1269:     */   
/* 1270:     */   void rationalize(IndexMapping xfMapping, IndexMapping fontMapping, IndexMapping formatMapping)
/* 1271:     */   {
/* 1272:2223 */     for (Iterator i = this.columnFormats.iterator(); i.hasNext();)
/* 1273:     */     {
/* 1274:2225 */       ColumnInfoRecord cir = (ColumnInfoRecord)i.next();
/* 1275:2226 */       cir.rationalize(xfMapping);
/* 1276:     */     }
/* 1277:2230 */     for (int i = 0; i < this.rows.length; i++) {
/* 1278:2232 */       if (this.rows[i] != null) {
/* 1279:2234 */         this.rows[i].rationalize(xfMapping);
/* 1280:     */       }
/* 1281:     */     }
/* 1282:2239 */     Chart[] charts = getCharts();
/* 1283:2240 */     for (int c = 0; c < charts.length; c++) {
/* 1284:2242 */       charts[c].rationalize(xfMapping, fontMapping, formatMapping);
/* 1285:     */     }
/* 1286:     */   }
/* 1287:     */   
/* 1288:     */   WritableWorkbookImpl getWorkbook()
/* 1289:     */   {
/* 1290:2252 */     return this.workbook;
/* 1291:     */   }
/* 1292:     */   
/* 1293:     */   /**
/* 1294:     */    * @deprecated
/* 1295:     */    */
/* 1296:     */   public CellFormat getColumnFormat(int col)
/* 1297:     */   {
/* 1298:2264 */     return getColumnView(col).getFormat();
/* 1299:     */   }
/* 1300:     */   
/* 1301:     */   /**
/* 1302:     */    * @deprecated
/* 1303:     */    */
/* 1304:     */   public int getColumnWidth(int col)
/* 1305:     */   {
/* 1306:2277 */     return getColumnView(col).getDimension();
/* 1307:     */   }
/* 1308:     */   
/* 1309:     */   /**
/* 1310:     */    * @deprecated
/* 1311:     */    */
/* 1312:     */   public int getRowHeight(int row)
/* 1313:     */   {
/* 1314:2290 */     return getRowView(row).getDimension();
/* 1315:     */   }
/* 1316:     */   
/* 1317:     */   boolean isChartOnly()
/* 1318:     */   {
/* 1319:2300 */     return this.chartOnly;
/* 1320:     */   }
/* 1321:     */   
/* 1322:     */   public CellView getRowView(int row)
/* 1323:     */   {
/* 1324:2312 */     CellView cv = new CellView();
/* 1325:     */     try
/* 1326:     */     {
/* 1327:2316 */       RowRecord rr = getRowRecord(row);
/* 1328:2318 */       if ((rr == null) || (rr.isDefaultHeight()))
/* 1329:     */       {
/* 1330:2320 */         cv.setDimension(this.settings.getDefaultRowHeight());
/* 1331:2321 */         cv.setSize(this.settings.getDefaultRowHeight());
/* 1332:     */       }
/* 1333:2323 */       else if (rr.isCollapsed())
/* 1334:     */       {
/* 1335:2325 */         cv.setHidden(true);
/* 1336:     */       }
/* 1337:     */       else
/* 1338:     */       {
/* 1339:2329 */         cv.setDimension(rr.getRowHeight());
/* 1340:2330 */         cv.setSize(rr.getRowHeight());
/* 1341:     */       }
/* 1342:2332 */       return cv;
/* 1343:     */     }
/* 1344:     */     catch (RowsExceededException e)
/* 1345:     */     {
/* 1346:2337 */       cv.setDimension(this.settings.getDefaultRowHeight());
/* 1347:2338 */       cv.setSize(this.settings.getDefaultRowHeight());
/* 1348:     */     }
/* 1349:2339 */     return cv;
/* 1350:     */   }
/* 1351:     */   
/* 1352:     */   public CellView getColumnView(int col)
/* 1353:     */   {
/* 1354:2352 */     ColumnInfoRecord cir = getColumnInfo(col);
/* 1355:2353 */     CellView cv = new CellView();
/* 1356:2355 */     if (cir != null)
/* 1357:     */     {
/* 1358:2357 */       cv.setDimension(cir.getWidth() / 256);
/* 1359:2358 */       cv.setSize(cir.getWidth());
/* 1360:2359 */       cv.setHidden(cir.getHidden());
/* 1361:2360 */       cv.setFormat(cir.getCellFormat());
/* 1362:     */     }
/* 1363:     */     else
/* 1364:     */     {
/* 1365:2364 */       cv.setDimension(this.settings.getDefaultColumnWidth() / 256);
/* 1366:2365 */       cv.setSize(this.settings.getDefaultColumnWidth() * 256);
/* 1367:     */     }
/* 1368:2368 */     return cv;
/* 1369:     */   }
/* 1370:     */   
/* 1371:     */   public void addImage(WritableImage image)
/* 1372:     */   {
/* 1373:2378 */     boolean supported = false;
/* 1374:2379 */     java.io.File imageFile = image.getImageFile();
/* 1375:2380 */     String fileType = "?";
/* 1376:2382 */     if (imageFile != null)
/* 1377:     */     {
/* 1378:2384 */       String fileName = imageFile.getName();
/* 1379:2385 */       int fileTypeIndex = fileName.lastIndexOf('.');
/* 1380:2386 */       fileType = fileTypeIndex != -1 ? 
/* 1381:2387 */         fileName.substring(fileTypeIndex + 1) : "";
/* 1382:     */       
/* 1383:2389 */       int i = 0;
/* 1384:     */       do
/* 1385:     */       {
/* 1386:2391 */         if (fileType.equalsIgnoreCase(imageTypes[i])) {
/* 1387:2393 */           supported = true;
/* 1388:     */         }
/* 1389:2389 */         i++;
/* 1390:2389 */         if (i >= imageTypes.length) {
/* 1391:     */           break;
/* 1392:     */         }
/* 1393:2389 */       } while (!supported);
/* 1394:     */     }
/* 1395:     */     else
/* 1396:     */     {
/* 1397:2399 */       supported = true;
/* 1398:     */     }
/* 1399:2402 */     if (supported)
/* 1400:     */     {
/* 1401:2404 */       this.workbook.addDrawing(image);
/* 1402:2405 */       this.drawings.add(image);
/* 1403:2406 */       this.images.add(image);
/* 1404:     */     }
/* 1405:     */     else
/* 1406:     */     {
/* 1407:2410 */       StringBuffer message = new StringBuffer("Image type ");
/* 1408:2411 */       message.append(fileType);
/* 1409:2412 */       message.append(" not supported.  Supported types are ");
/* 1410:2413 */       message.append(imageTypes[0]);
/* 1411:2414 */       for (int i = 1; i < imageTypes.length; i++)
/* 1412:     */       {
/* 1413:2416 */         message.append(", ");
/* 1414:2417 */         message.append(imageTypes[i]);
/* 1415:     */       }
/* 1416:2419 */       logger.warn(message.toString());
/* 1417:     */     }
/* 1418:     */   }
/* 1419:     */   
/* 1420:     */   public int getNumberOfImages()
/* 1421:     */   {
/* 1422:2430 */     return this.images.size();
/* 1423:     */   }
/* 1424:     */   
/* 1425:     */   public WritableImage getImage(int i)
/* 1426:     */   {
/* 1427:2441 */     return (WritableImage)this.images.get(i);
/* 1428:     */   }
/* 1429:     */   
/* 1430:     */   public Image getDrawing(int i)
/* 1431:     */   {
/* 1432:2452 */     return (Image)this.images.get(i);
/* 1433:     */   }
/* 1434:     */   
/* 1435:     */   public void removeImage(WritableImage wi)
/* 1436:     */   {
/* 1437:2463 */     this.drawings.remove(wi);
/* 1438:2464 */     this.images.remove(wi);
/* 1439:2465 */     this.drawingsModified = true;
/* 1440:2466 */     this.workbook.removeDrawing(wi);
/* 1441:     */   }
/* 1442:     */   
/* 1443:     */   private String validateName(String n)
/* 1444:     */   {
/* 1445:2474 */     if (n.length() > 31)
/* 1446:     */     {
/* 1447:2476 */       logger.warn("Sheet name " + n + " too long - truncating");
/* 1448:2477 */       n = n.substring(0, 31);
/* 1449:     */     }
/* 1450:2480 */     if (n.charAt(0) == '\'')
/* 1451:     */     {
/* 1452:2482 */       logger.warn("Sheet naming cannot start with ' - removing");
/* 1453:2483 */       n = n.substring(1);
/* 1454:     */     }
/* 1455:2486 */     for (int i = 0; i < illegalSheetNameCharacters.length; i++)
/* 1456:     */     {
/* 1457:2488 */       String newname = n.replace(illegalSheetNameCharacters[i], '@');
/* 1458:2489 */       if (n != newname) {
/* 1459:2491 */         logger.warn(illegalSheetNameCharacters[i] + 
/* 1460:2492 */           " is not a valid character within a sheet name - replacing");
/* 1461:     */       }
/* 1462:2494 */       n = newname;
/* 1463:     */     }
/* 1464:2497 */     return n;
/* 1465:     */   }
/* 1466:     */   
/* 1467:     */   void addDrawing(DrawingGroupObject o)
/* 1468:     */   {
/* 1469:2507 */     this.drawings.add(o);
/* 1470:2508 */     Assert.verify(!(o instanceof Drawing));
/* 1471:     */   }
/* 1472:     */   
/* 1473:     */   void removeDrawing(DrawingGroupObject o)
/* 1474:     */   {
/* 1475:2518 */     int origSize = this.drawings.size();
/* 1476:2519 */     this.drawings.remove(o);
/* 1477:2520 */     int newSize = this.drawings.size();
/* 1478:2521 */     this.drawingsModified = true;
/* 1479:2522 */     Assert.verify(newSize == origSize - 1);
/* 1480:     */   }
/* 1481:     */   
/* 1482:     */   void removeDataValidation(CellValue cv)
/* 1483:     */   {
/* 1484:2533 */     if (this.dataValidation != null) {
/* 1485:2535 */       this.dataValidation.removeDataValidation(cv.getColumn(), cv.getRow());
/* 1486:     */     }
/* 1487:2538 */     if (this.validatedCells != null)
/* 1488:     */     {
/* 1489:2540 */       boolean result = this.validatedCells.remove(cv);
/* 1490:2542 */       if (!result) {
/* 1491:2544 */         logger.warn("Could not remove validated cell " + 
/* 1492:2545 */           CellReferenceHelper.getCellReference(cv));
/* 1493:     */       }
/* 1494:     */     }
/* 1495:     */   }
/* 1496:     */   
/* 1497:     */   public int[] getRowPageBreaks()
/* 1498:     */   {
/* 1499:2557 */     int[] rb = new int[this.rowBreaks.size()];
/* 1500:2558 */     int pos = 0;
/* 1501:2559 */     for (Iterator i = this.rowBreaks.iterator(); i.hasNext(); pos++) {
/* 1502:2561 */       rb[pos] = ((Integer)i.next()).intValue();
/* 1503:     */     }
/* 1504:2563 */     return rb;
/* 1505:     */   }
/* 1506:     */   
/* 1507:     */   public int[] getColumnPageBreaks()
/* 1508:     */   {
/* 1509:2573 */     int[] rb = new int[this.columnBreaks.size()];
/* 1510:2574 */     int pos = 0;
/* 1511:2575 */     for (Iterator i = this.columnBreaks.iterator(); i.hasNext(); pos++) {
/* 1512:2577 */       rb[pos] = ((Integer)i.next()).intValue();
/* 1513:     */     }
/* 1514:2579 */     return rb;
/* 1515:     */   }
/* 1516:     */   
/* 1517:     */   void addValidationCell(CellValue cv)
/* 1518:     */   {
/* 1519:2589 */     this.validatedCells.add(cv);
/* 1520:     */   }
/* 1521:     */   
/* 1522:     */   ComboBox getComboBox()
/* 1523:     */   {
/* 1524:2600 */     return this.comboBox;
/* 1525:     */   }
/* 1526:     */   
/* 1527:     */   void setComboBox(ComboBox cb)
/* 1528:     */   {
/* 1529:2610 */     this.comboBox = cb;
/* 1530:     */   }
/* 1531:     */   
/* 1532:     */   public DataValidation getDataValidation()
/* 1533:     */   {
/* 1534:2618 */     return this.dataValidation;
/* 1535:     */   }
/* 1536:     */   
/* 1537:     */   private void autosizeColumns()
/* 1538:     */   {
/* 1539:2626 */     Iterator i = this.autosizedColumns.iterator();
/* 1540:2627 */     while (i.hasNext())
/* 1541:     */     {
/* 1542:2629 */       Integer col = (Integer)i.next();
/* 1543:2630 */       autosizeColumn(col.intValue());
/* 1544:     */     }
/* 1545:     */   }
/* 1546:     */   
/* 1547:     */   private void autosizeColumn(int col)
/* 1548:     */   {
/* 1549:2641 */     int maxWidth = 0;
/* 1550:2642 */     ColumnInfoRecord cir = getColumnInfo(col);
/* 1551:2643 */     Font columnFont = cir.getCellFormat().getFont();
/* 1552:2644 */     Font defaultFont = WritableWorkbook.NORMAL_STYLE.getFont();
/* 1553:2646 */     for (int i = 0; i < this.numRows; i++)
/* 1554:     */     {
/* 1555:2648 */       Cell cell = null;
/* 1556:2649 */       if (this.rows[i] != null) {
/* 1557:2651 */         cell = this.rows[i].getCell(col);
/* 1558:     */       }
/* 1559:2654 */       if (cell != null)
/* 1560:     */       {
/* 1561:2656 */         String contents = cell.getContents();
/* 1562:2657 */         Font font = cell.getCellFormat().getFont();
/* 1563:     */         
/* 1564:2659 */         Font activeFont = font.equals(defaultFont) ? columnFont : font;
/* 1565:     */         
/* 1566:2661 */         int pointSize = activeFont.getPointSize();
/* 1567:2662 */         int numChars = contents.length();
/* 1568:2664 */         if ((activeFont.isItalic()) || 
/* 1569:2665 */           (activeFont.getBoldWeight() > 400)) {
/* 1570:2667 */           numChars += 2;
/* 1571:     */         }
/* 1572:2670 */         int points = numChars * pointSize;
/* 1573:2671 */         maxWidth = Math.max(maxWidth, points * 256);
/* 1574:     */       }
/* 1575:     */     }
/* 1576:2674 */     cir.setWidth(maxWidth / defaultFont.getPointSize());
/* 1577:     */   }
/* 1578:     */   
/* 1579:     */   void importSheet(Sheet s)
/* 1580:     */   {
/* 1581:2685 */     this.settings = new SheetSettings(s.getSettings(), this);
/* 1582:     */     
/* 1583:2687 */     SheetCopier si = new SheetCopier(s, this);
/* 1584:2688 */     si.setColumnFormats(this.columnFormats);
/* 1585:2689 */     si.setFormatRecords(this.formatRecords);
/* 1586:2690 */     si.setHyperlinks(this.hyperlinks);
/* 1587:2691 */     si.setMergedCells(this.mergedCells);
/* 1588:2692 */     si.setRowBreaks(this.rowBreaks);
/* 1589:2693 */     si.setColumnBreaks(this.columnBreaks);
/* 1590:2694 */     si.setSheetWriter(this.sheetWriter);
/* 1591:2695 */     si.setDrawings(this.drawings);
/* 1592:2696 */     si.setImages(this.images);
/* 1593:2697 */     si.setValidatedCells(this.validatedCells);
/* 1594:     */     
/* 1595:2699 */     si.importSheet();
/* 1596:     */     
/* 1597:2701 */     this.dataValidation = si.getDataValidation();
/* 1598:2702 */     this.comboBox = si.getComboBox();
/* 1599:2703 */     this.plsRecord = si.getPLSRecord();
/* 1600:2704 */     this.chartOnly = si.isChartOnly();
/* 1601:2705 */     this.buttonPropertySet = si.getButtonPropertySet();
/* 1602:2706 */     this.numRows = si.getRows();
/* 1603:2707 */     this.maxRowOutlineLevel = si.getMaxRowOutlineLevel();
/* 1604:2708 */     this.maxColumnOutlineLevel = si.getMaxColumnOutlineLevel();
/* 1605:     */   }
/* 1606:     */   
/* 1607:     */   public void applySharedDataValidation(WritableCell c, int extraCols, int extraRows)
/* 1608:     */     throws WriteException
/* 1609:     */   {
/* 1610:2724 */     if ((c.getWritableCellFeatures() == null) || 
/* 1611:2725 */       (!c.getWritableCellFeatures().hasDataValidation()))
/* 1612:     */     {
/* 1613:2727 */       logger.warn("Cannot extend data validation for " + 
/* 1614:2728 */         CellReferenceHelper.getCellReference(c.getColumn(), 
/* 1615:2729 */         c.getRow()) + 
/* 1616:2730 */         " as it has no data validation");
/* 1617:2731 */       return;
/* 1618:     */     }
/* 1619:2736 */     int startColumn = c.getColumn();
/* 1620:2737 */     int startRow = c.getRow();
/* 1621:2738 */     int endRow = Math.min(this.numRows - 1, startRow + extraRows);
/* 1622:2739 */     for (int y = startRow; y <= endRow; y++) {
/* 1623:2741 */       if (this.rows[y] != null)
/* 1624:     */       {
/* 1625:2743 */         int endCol = Math.min(this.rows[y].getMaxColumn() - 1, 
/* 1626:2744 */           startColumn + extraCols);
/* 1627:2745 */         for (int x = startColumn; x <= endCol; x++) {
/* 1628:2748 */           if ((x != startColumn) || (y != startRow))
/* 1629:     */           {
/* 1630:2753 */             WritableCell c2 = this.rows[y].getCell(x);
/* 1631:2756 */             if ((c2 != null) && 
/* 1632:2757 */               (c2.getWritableCellFeatures() != null) && 
/* 1633:2758 */               (c2.getWritableCellFeatures().hasDataValidation()))
/* 1634:     */             {
/* 1635:2760 */               logger.warn("Cannot apply data validation from " + 
/* 1636:2761 */                 CellReferenceHelper.getCellReference(startColumn, 
/* 1637:2762 */                 startRow) + 
/* 1638:2763 */                 " to " + 
/* 1639:2764 */                 CellReferenceHelper.getCellReference(
/* 1640:2765 */                 startColumn + extraCols, 
/* 1641:2766 */                 startRow + extraRows) + 
/* 1642:2767 */                 " as cell " + 
/* 1643:2768 */                 CellReferenceHelper.getCellReference(x, y) + 
/* 1644:2769 */                 " already has a data validation");
/* 1645:2770 */               return;
/* 1646:     */             }
/* 1647:     */           }
/* 1648:     */         }
/* 1649:     */       }
/* 1650:     */     }
/* 1651:2777 */     WritableCellFeatures sourceDataValidation = c.getWritableCellFeatures();
/* 1652:2778 */     sourceDataValidation.getDVParser().extendCellValidation(extraCols, 
/* 1653:2779 */       extraRows);
/* 1654:2782 */     for (int y = startRow; y <= startRow + extraRows; y++)
/* 1655:     */     {
/* 1656:2784 */       RowRecord rowrec = getRowRecord(y);
/* 1657:2785 */       for (int x = startColumn; x <= startColumn + extraCols; x++) {
/* 1658:2788 */         if ((x != startColumn) || (y != startRow))
/* 1659:     */         {
/* 1660:2793 */           WritableCell c2 = rowrec.getCell(x);
/* 1661:2796 */           if (c2 == null)
/* 1662:     */           {
/* 1663:2798 */             Blank b = new Blank(x, y);
/* 1664:2799 */             WritableCellFeatures validation = new WritableCellFeatures();
/* 1665:2800 */             validation.shareDataValidation(sourceDataValidation);
/* 1666:2801 */             b.setCellFeatures(validation);
/* 1667:2802 */             addCell(b);
/* 1668:     */           }
/* 1669:     */           else
/* 1670:     */           {
/* 1671:2807 */             WritableCellFeatures validation = c2.getWritableCellFeatures();
/* 1672:2809 */             if (validation != null)
/* 1673:     */             {
/* 1674:2811 */               validation.shareDataValidation(sourceDataValidation);
/* 1675:     */             }
/* 1676:     */             else
/* 1677:     */             {
/* 1678:2815 */               validation = new WritableCellFeatures();
/* 1679:2816 */               validation.shareDataValidation(sourceDataValidation);
/* 1680:2817 */               c2.setCellFeatures(validation);
/* 1681:     */             }
/* 1682:     */           }
/* 1683:     */         }
/* 1684:     */       }
/* 1685:     */     }
/* 1686:     */   }
/* 1687:     */   
/* 1688:     */   public void removeSharedDataValidation(WritableCell cell)
/* 1689:     */     throws WriteException
/* 1690:     */   {
/* 1691:2834 */     WritableCellFeatures wcf = cell.getWritableCellFeatures();
/* 1692:2835 */     if ((wcf == null) || 
/* 1693:2836 */       (!wcf.hasDataValidation())) {
/* 1694:2838 */       return;
/* 1695:     */     }
/* 1696:2841 */     DVParser dvp = wcf.getDVParser();
/* 1697:2845 */     if (!dvp.extendedCellsValidation())
/* 1698:     */     {
/* 1699:2847 */       wcf.removeDataValidation();
/* 1700:2848 */       return;
/* 1701:     */     }
/* 1702:2853 */     if (dvp.extendedCellsValidation()) {
/* 1703:2855 */       if ((cell.getColumn() != dvp.getFirstColumn()) || 
/* 1704:2856 */         (cell.getRow() != dvp.getFirstRow()))
/* 1705:     */       {
/* 1706:2858 */         logger.warn("Cannot remove data validation from " + 
/* 1707:2859 */           CellReferenceHelper.getCellReference(dvp.getFirstColumn(), 
/* 1708:2860 */           dvp.getFirstRow()) + 
/* 1709:2861 */           "-" + 
/* 1710:2862 */           CellReferenceHelper.getCellReference(dvp.getLastColumn(), 
/* 1711:2863 */           dvp.getLastRow()) + 
/* 1712:2864 */           " because the selected cell " + 
/* 1713:2865 */           CellReferenceHelper.getCellReference(cell) + 
/* 1714:2866 */           " is not the top left cell in the range");
/* 1715:2867 */         return;
/* 1716:     */       }
/* 1717:     */     }
/* 1718:2871 */     for (int y = dvp.getFirstRow(); y <= dvp.getLastRow(); y++) {
/* 1719:2873 */       for (int x = dvp.getFirstColumn(); x <= dvp.getLastColumn(); x++)
/* 1720:     */       {
/* 1721:2875 */         CellValue c2 = this.rows[y].getCell(x);
/* 1722:2879 */         if (c2 != null)
/* 1723:     */         {
/* 1724:2881 */           c2.getWritableCellFeatures().removeSharedDataValidation();
/* 1725:2882 */           c2.removeCellFeatures();
/* 1726:     */         }
/* 1727:     */       }
/* 1728:     */     }
/* 1729:2889 */     if (this.dataValidation != null) {
/* 1730:2891 */       this.dataValidation.removeSharedDataValidation(dvp.getFirstColumn(), 
/* 1731:2892 */         dvp.getFirstRow(), 
/* 1732:2893 */         dvp.getLastColumn(), 
/* 1733:2894 */         dvp.getLastRow());
/* 1734:     */     }
/* 1735:     */   }
/* 1736:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.WritableSheetImpl
 * JD-Core Version:    0.7.0.1
 */