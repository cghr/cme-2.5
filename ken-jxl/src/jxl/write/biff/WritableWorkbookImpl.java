/*    1:     */ package jxl.write.biff;
/*    2:     */ 
/*    3:     */ import java.io.FileOutputStream;
/*    4:     */ import java.io.IOException;
/*    5:     */ import java.io.OutputStream;
/*    6:     */ import java.util.ArrayList;
/*    7:     */ import java.util.HashMap;
/*    8:     */ import java.util.Iterator;
/*    9:     */ import jxl.Cell;
/*   10:     */ import jxl.Range;
/*   11:     */ import jxl.Sheet;
/*   12:     */ import jxl.SheetSettings;
/*   13:     */ import jxl.Workbook;
/*   14:     */ import jxl.WorkbookSettings;
/*   15:     */ import jxl.biff.BuiltInName;
/*   16:     */ import jxl.biff.CellReferenceHelper;
/*   17:     */ import jxl.biff.CountryCode;
/*   18:     */ import jxl.biff.Fonts;
/*   19:     */ import jxl.biff.FormattingRecords;
/*   20:     */ import jxl.biff.IndexMapping;
/*   21:     */ import jxl.biff.IntegerHelper;
/*   22:     */ import jxl.biff.RangeImpl;
/*   23:     */ import jxl.biff.WorkbookMethods;
/*   24:     */ import jxl.biff.XCTRecord;
/*   25:     */ import jxl.biff.drawing.Drawing;
/*   26:     */ import jxl.biff.drawing.DrawingGroup;
/*   27:     */ import jxl.biff.drawing.DrawingGroupObject;
/*   28:     */ import jxl.biff.drawing.Origin;
/*   29:     */ import jxl.biff.formula.ExternalSheet;
/*   30:     */ import jxl.common.Assert;
/*   31:     */ import jxl.common.Logger;
/*   32:     */ import jxl.format.Colour;
/*   33:     */ import jxl.format.RGB;
/*   34:     */ import jxl.read.biff.WorkbookParser;
/*   35:     */ import jxl.write.WritableCell;
/*   36:     */ import jxl.write.WritableCellFormat;
/*   37:     */ import jxl.write.WritableFont;
/*   38:     */ import jxl.write.WritableSheet;
/*   39:     */ import jxl.write.WritableWorkbook;
/*   40:     */ 
/*   41:     */ public class WritableWorkbookImpl
/*   42:     */   extends WritableWorkbook
/*   43:     */   implements ExternalSheet, WorkbookMethods
/*   44:     */ {
/*   45:  68 */   private static Logger logger = Logger.getLogger(WritableWorkbookImpl.class);
/*   46:     */   private FormattingRecords formatRecords;
/*   47:     */   private File outputFile;
/*   48:     */   private ArrayList sheets;
/*   49:     */   private Fonts fonts;
/*   50:     */   private ExternalSheetRecord externSheet;
/*   51:     */   private ArrayList supbooks;
/*   52:     */   private ArrayList names;
/*   53:     */   private HashMap nameRecords;
/*   54:     */   private SharedStrings sharedStrings;
/*   55:     */   private boolean closeStream;
/*   56:     */   private boolean wbProtected;
/*   57:     */   private WorkbookSettings settings;
/*   58:     */   private ArrayList rcirCells;
/*   59:     */   private DrawingGroup drawingGroup;
/*   60:     */   private Styles styles;
/*   61:     */   private boolean containsMacros;
/*   62:     */   private ButtonPropertySetRecord buttonPropertySet;
/*   63:     */   private CountryRecord countryRecord;
/*   64: 159 */   private static Object SYNCHRONIZER = new Object();
/*   65:     */   private String[] addInFunctionNames;
/*   66:     */   private XCTRecord[] xctRecords;
/*   67:     */   
/*   68:     */   public WritableWorkbookImpl(OutputStream os, boolean cs, WorkbookSettings ws)
/*   69:     */     throws IOException
/*   70:     */   {
/*   71: 183 */     this.outputFile = new File(os, ws, null);
/*   72: 184 */     this.sheets = new ArrayList();
/*   73: 185 */     this.sharedStrings = new SharedStrings();
/*   74: 186 */     this.nameRecords = new HashMap();
/*   75: 187 */     this.closeStream = cs;
/*   76: 188 */     this.wbProtected = false;
/*   77: 189 */     this.containsMacros = false;
/*   78: 190 */     this.settings = ws;
/*   79: 191 */     this.rcirCells = new ArrayList();
/*   80: 192 */     this.styles = new Styles();
/*   81: 199 */     synchronized (SYNCHRONIZER)
/*   82:     */     {
/*   83: 201 */       WritableWorkbook.ARIAL_10_PT.uninitialize();
/*   84: 202 */       WritableWorkbook.HYPERLINK_FONT.uninitialize();
/*   85: 203 */       WritableWorkbook.NORMAL_STYLE.uninitialize();
/*   86: 204 */       WritableWorkbook.HYPERLINK_STYLE.uninitialize();
/*   87: 205 */       WritableWorkbook.HIDDEN_STYLE.uninitialize();
/*   88: 206 */       DateRecord.defaultDateFormat.uninitialize();
/*   89:     */     }
/*   90: 209 */     WritableFonts wf = new WritableFonts(this);
/*   91: 210 */     this.fonts = wf;
/*   92:     */     
/*   93: 212 */     WritableFormattingRecords wfr = new WritableFormattingRecords(this.fonts, 
/*   94: 213 */       this.styles);
/*   95: 214 */     this.formatRecords = wfr;
/*   96:     */   }
/*   97:     */   
/*   98:     */   public WritableWorkbookImpl(OutputStream os, Workbook w, boolean cs, WorkbookSettings ws)
/*   99:     */     throws IOException
/*  100:     */   {
/*  101: 233 */     WorkbookParser wp = (WorkbookParser)w;
/*  102: 240 */     synchronized (SYNCHRONIZER)
/*  103:     */     {
/*  104: 242 */       WritableWorkbook.ARIAL_10_PT.uninitialize();
/*  105: 243 */       WritableWorkbook.HYPERLINK_FONT.uninitialize();
/*  106: 244 */       WritableWorkbook.NORMAL_STYLE.uninitialize();
/*  107: 245 */       WritableWorkbook.HYPERLINK_STYLE.uninitialize();
/*  108: 246 */       WritableWorkbook.HIDDEN_STYLE.uninitialize();
/*  109: 247 */       DateRecord.defaultDateFormat.uninitialize();
/*  110:     */     }
/*  111: 250 */     this.closeStream = cs;
/*  112: 251 */     this.sheets = new ArrayList();
/*  113: 252 */     this.sharedStrings = new SharedStrings();
/*  114: 253 */     this.nameRecords = new HashMap();
/*  115: 254 */     this.fonts = wp.getFonts();
/*  116: 255 */     this.formatRecords = wp.getFormattingRecords();
/*  117: 256 */     this.wbProtected = false;
/*  118: 257 */     this.settings = ws;
/*  119: 258 */     this.rcirCells = new ArrayList();
/*  120: 259 */     this.styles = new Styles();
/*  121: 260 */     this.outputFile = new File(os, ws, wp.getCompoundFile());
/*  122:     */     
/*  123: 262 */     this.containsMacros = false;
/*  124: 263 */     if (!ws.getPropertySetsDisabled()) {
/*  125: 265 */       this.containsMacros = wp.containsMacros();
/*  126:     */     }
/*  127: 269 */     if (wp.getCountryRecord() != null) {
/*  128: 271 */       this.countryRecord = new CountryRecord(wp.getCountryRecord());
/*  129:     */     }
/*  130: 275 */     this.addInFunctionNames = wp.getAddInFunctionNames();
/*  131:     */     
/*  132:     */ 
/*  133: 278 */     this.xctRecords = wp.getXCTRecords();
/*  134: 281 */     if (wp.getExternalSheetRecord() != null)
/*  135:     */     {
/*  136: 283 */       this.externSheet = new ExternalSheetRecord(wp.getExternalSheetRecord());
/*  137:     */       
/*  138:     */ 
/*  139: 286 */       jxl.read.biff.SupbookRecord[] readsr = wp.getSupbookRecords();
/*  140: 287 */       this.supbooks = new ArrayList(readsr.length);
/*  141: 289 */       for (int i = 0; i < readsr.length; i++)
/*  142:     */       {
/*  143: 291 */         jxl.read.biff.SupbookRecord readSupbook = readsr[i];
/*  144: 292 */         if ((readSupbook.getType() == jxl.read.biff.SupbookRecord.INTERNAL) || 
/*  145: 293 */           (readSupbook.getType() == jxl.read.biff.SupbookRecord.EXTERNAL)) {
/*  146: 295 */           this.supbooks.add(new SupbookRecord(readSupbook, this.settings));
/*  147: 299 */         } else if (readSupbook.getType() != jxl.read.biff.SupbookRecord.ADDIN) {
/*  148: 301 */           logger.warn("unsupported supbook type - ignoring");
/*  149:     */         }
/*  150:     */       }
/*  151:     */     }
/*  152: 309 */     if (wp.getDrawingGroup() != null) {
/*  153: 311 */       this.drawingGroup = new DrawingGroup(wp.getDrawingGroup());
/*  154:     */     }
/*  155: 315 */     if ((this.containsMacros) && (wp.getButtonPropertySet() != null)) {
/*  156: 317 */       this.buttonPropertySet = new ButtonPropertySetRecord(
/*  157: 318 */         wp.getButtonPropertySet());
/*  158:     */     }
/*  159: 322 */     if (!this.settings.getNamesDisabled())
/*  160:     */     {
/*  161: 324 */       jxl.read.biff.NameRecord[] na = wp.getNameRecords();
/*  162: 325 */       this.names = new ArrayList(na.length);
/*  163: 327 */       for (int i = 0; i < na.length; i++) {
/*  164: 329 */         if (na[i].isBiff8())
/*  165:     */         {
/*  166: 331 */           NameRecord n = new NameRecord(na[i], i);
/*  167: 332 */           this.names.add(n);
/*  168: 333 */           String name = n.getName();
/*  169: 334 */           this.nameRecords.put(name, n);
/*  170:     */         }
/*  171:     */         else
/*  172:     */         {
/*  173: 338 */           logger.warn("Cannot copy Biff7 name records - ignoring");
/*  174:     */         }
/*  175:     */       }
/*  176:     */     }
/*  177: 343 */     copyWorkbook(w);
/*  178: 348 */     if (this.drawingGroup != null) {
/*  179: 350 */       this.drawingGroup.updateData(wp.getDrawingGroup());
/*  180:     */     }
/*  181:     */   }
/*  182:     */   
/*  183:     */   public WritableSheet[] getSheets()
/*  184:     */   {
/*  185: 362 */     WritableSheet[] sheetArray = new WritableSheet[getNumberOfSheets()];
/*  186: 364 */     for (int i = 0; i < getNumberOfSheets(); i++) {
/*  187: 366 */       sheetArray[i] = getSheet(i);
/*  188:     */     }
/*  189: 368 */     return sheetArray;
/*  190:     */   }
/*  191:     */   
/*  192:     */   public String[] getSheetNames()
/*  193:     */   {
/*  194: 378 */     String[] sheetNames = new String[getNumberOfSheets()];
/*  195: 380 */     for (int i = 0; i < sheetNames.length; i++) {
/*  196: 382 */       sheetNames[i] = getSheet(i).getName();
/*  197:     */     }
/*  198: 385 */     return sheetNames;
/*  199:     */   }
/*  200:     */   
/*  201:     */   public Sheet getReadSheet(int index)
/*  202:     */   {
/*  203: 397 */     return getSheet(index);
/*  204:     */   }
/*  205:     */   
/*  206:     */   public WritableSheet getSheet(int index)
/*  207:     */   {
/*  208: 408 */     return (WritableSheet)this.sheets.get(index);
/*  209:     */   }
/*  210:     */   
/*  211:     */   public WritableSheet getSheet(String name)
/*  212:     */   {
/*  213: 420 */     boolean found = false;
/*  214: 421 */     Iterator i = this.sheets.iterator();
/*  215: 422 */     WritableSheet s = null;
/*  216: 424 */     while ((i.hasNext()) && (!found))
/*  217:     */     {
/*  218: 426 */       s = (WritableSheet)i.next();
/*  219: 428 */       if (s.getName().equals(name)) {
/*  220: 430 */         found = true;
/*  221:     */       }
/*  222:     */     }
/*  223: 434 */     return found ? s : null;
/*  224:     */   }
/*  225:     */   
/*  226:     */   public int getNumberOfSheets()
/*  227:     */   {
/*  228: 444 */     return this.sheets.size();
/*  229:     */   }
/*  230:     */   
/*  231:     */   public void close()
/*  232:     */     throws IOException, JxlWriteException
/*  233:     */   {
/*  234: 456 */     this.outputFile.close(this.closeStream);
/*  235:     */   }
/*  236:     */   
/*  237:     */   public void setOutputFile(java.io.File fileName)
/*  238:     */     throws IOException
/*  239:     */   {
/*  240: 469 */     FileOutputStream fos = new FileOutputStream(fileName);
/*  241: 470 */     this.outputFile.setOutputFile(fos);
/*  242:     */   }
/*  243:     */   
/*  244:     */   private WritableSheet createSheet(String name, int index, boolean handleRefs)
/*  245:     */   {
/*  246: 486 */     WritableSheet w = new WritableSheetImpl(name, 
/*  247: 487 */       this.outputFile, 
/*  248: 488 */       this.formatRecords, 
/*  249: 489 */       this.sharedStrings, 
/*  250: 490 */       this.settings, 
/*  251: 491 */       this);
/*  252:     */     
/*  253: 493 */     int pos = index;
/*  254: 495 */     if (index <= 0)
/*  255:     */     {
/*  256: 497 */       pos = 0;
/*  257: 498 */       this.sheets.add(0, w);
/*  258:     */     }
/*  259: 500 */     else if (index > this.sheets.size())
/*  260:     */     {
/*  261: 502 */       pos = this.sheets.size();
/*  262: 503 */       this.sheets.add(w);
/*  263:     */     }
/*  264:     */     else
/*  265:     */     {
/*  266: 507 */       this.sheets.add(index, w);
/*  267:     */     }
/*  268: 510 */     if ((handleRefs) && (this.externSheet != null)) {
/*  269: 512 */       this.externSheet.sheetInserted(pos);
/*  270:     */     }
/*  271: 515 */     if ((this.supbooks != null) && (this.supbooks.size() > 0))
/*  272:     */     {
/*  273: 517 */       SupbookRecord supbook = (SupbookRecord)this.supbooks.get(0);
/*  274: 518 */       if (supbook.getType() == SupbookRecord.INTERNAL) {
/*  275: 520 */         supbook.adjustInternal(this.sheets.size());
/*  276:     */       }
/*  277:     */     }
/*  278: 524 */     return w;
/*  279:     */   }
/*  280:     */   
/*  281:     */   public WritableSheet createSheet(String name, int index)
/*  282:     */   {
/*  283: 538 */     return createSheet(name, index, true);
/*  284:     */   }
/*  285:     */   
/*  286:     */   public void removeSheet(int index)
/*  287:     */   {
/*  288: 550 */     int pos = index;
/*  289: 551 */     if (index <= 0)
/*  290:     */     {
/*  291: 553 */       pos = 0;
/*  292: 554 */       this.sheets.remove(0);
/*  293:     */     }
/*  294: 556 */     else if (index >= this.sheets.size())
/*  295:     */     {
/*  296: 558 */       pos = this.sheets.size() - 1;
/*  297: 559 */       this.sheets.remove(this.sheets.size() - 1);
/*  298:     */     }
/*  299:     */     else
/*  300:     */     {
/*  301: 563 */       this.sheets.remove(index);
/*  302:     */     }
/*  303: 566 */     if (this.externSheet != null) {
/*  304: 568 */       this.externSheet.sheetRemoved(pos);
/*  305:     */     }
/*  306: 571 */     if ((this.supbooks != null) && (this.supbooks.size() > 0))
/*  307:     */     {
/*  308: 573 */       SupbookRecord supbook = (SupbookRecord)this.supbooks.get(0);
/*  309: 574 */       if (supbook.getType() == SupbookRecord.INTERNAL) {
/*  310: 576 */         supbook.adjustInternal(this.sheets.size());
/*  311:     */       }
/*  312:     */     }
/*  313: 580 */     if ((this.names != null) && (this.names.size() > 0)) {
/*  314: 582 */       for (int i = 0; i < this.names.size(); i++)
/*  315:     */       {
/*  316: 584 */         NameRecord n = (NameRecord)this.names.get(i);
/*  317: 585 */         int oldRef = n.getSheetRef();
/*  318: 586 */         if (oldRef == pos + 1)
/*  319:     */         {
/*  320: 588 */           n.setSheetRef(0);
/*  321:     */         }
/*  322: 590 */         else if (oldRef > pos + 1)
/*  323:     */         {
/*  324: 592 */           if (oldRef < 1) {
/*  325: 594 */             oldRef = 1;
/*  326:     */           }
/*  327: 596 */           n.setSheetRef(oldRef - 1);
/*  328:     */         }
/*  329:     */       }
/*  330:     */     }
/*  331:     */   }
/*  332:     */   
/*  333:     */   public WritableSheet moveSheet(int fromIndex, int toIndex)
/*  334:     */   {
/*  335: 613 */     fromIndex = Math.max(fromIndex, 0);
/*  336: 614 */     fromIndex = Math.min(fromIndex, this.sheets.size() - 1);
/*  337: 615 */     toIndex = Math.max(toIndex, 0);
/*  338: 616 */     toIndex = Math.min(toIndex, this.sheets.size() - 1);
/*  339:     */     
/*  340: 618 */     WritableSheet sheet = (WritableSheet)this.sheets.remove(fromIndex);
/*  341: 619 */     this.sheets.add(toIndex, sheet);
/*  342:     */     
/*  343: 621 */     return sheet;
/*  344:     */   }
/*  345:     */   
/*  346:     */   public void write()
/*  347:     */     throws IOException
/*  348:     */   {
/*  349: 635 */     WritableSheetImpl wsi = null;
/*  350: 636 */     for (int i = 0; i < getNumberOfSheets(); i++)
/*  351:     */     {
/*  352: 638 */       wsi = (WritableSheetImpl)getSheet(i);
/*  353:     */       
/*  354:     */ 
/*  355:     */ 
/*  356: 642 */       wsi.checkMergedBorders();
/*  357:     */       
/*  358:     */ 
/*  359: 645 */       Range range = wsi.getSettings().getPrintArea();
/*  360: 646 */       if (range != null) {
/*  361: 648 */         addNameArea(BuiltInName.PRINT_AREA, 
/*  362: 649 */           wsi, 
/*  363: 650 */           range.getTopLeft().getColumn(), 
/*  364: 651 */           range.getTopLeft().getRow(), 
/*  365: 652 */           range.getBottomRight().getColumn(), 
/*  366: 653 */           range.getBottomRight().getRow(), 
/*  367: 654 */           false);
/*  368:     */       }
/*  369: 658 */       Range rangeR = wsi.getSettings().getPrintTitlesRow();
/*  370: 659 */       Range rangeC = wsi.getSettings().getPrintTitlesCol();
/*  371: 660 */       if ((rangeR != null) && (rangeC != null)) {
/*  372: 662 */         addNameArea(BuiltInName.PRINT_TITLES, 
/*  373: 663 */           wsi, 
/*  374: 664 */           rangeR.getTopLeft().getColumn(), 
/*  375: 665 */           rangeR.getTopLeft().getRow(), 
/*  376: 666 */           rangeR.getBottomRight().getColumn(), 
/*  377: 667 */           rangeR.getBottomRight().getRow(), 
/*  378: 668 */           rangeC.getTopLeft().getColumn(), 
/*  379: 669 */           rangeC.getTopLeft().getRow(), 
/*  380: 670 */           rangeC.getBottomRight().getColumn(), 
/*  381: 671 */           rangeC.getBottomRight().getRow(), 
/*  382: 672 */           false);
/*  383: 675 */       } else if (rangeR != null) {
/*  384: 677 */         addNameArea(BuiltInName.PRINT_TITLES, 
/*  385: 678 */           wsi, 
/*  386: 679 */           rangeR.getTopLeft().getColumn(), 
/*  387: 680 */           rangeR.getTopLeft().getRow(), 
/*  388: 681 */           rangeR.getBottomRight().getColumn(), 
/*  389: 682 */           rangeR.getBottomRight().getRow(), 
/*  390: 683 */           false);
/*  391: 686 */       } else if (rangeC != null) {
/*  392: 688 */         addNameArea(BuiltInName.PRINT_TITLES, 
/*  393: 689 */           wsi, 
/*  394: 690 */           rangeC.getTopLeft().getColumn(), 
/*  395: 691 */           rangeC.getTopLeft().getRow(), 
/*  396: 692 */           rangeC.getBottomRight().getColumn(), 
/*  397: 693 */           rangeC.getBottomRight().getRow(), 
/*  398: 694 */           false);
/*  399:     */       }
/*  400:     */     }
/*  401: 699 */     if (!this.settings.getRationalizationDisabled()) {
/*  402: 701 */       rationalize();
/*  403:     */     }
/*  404: 705 */     BOFRecord bof = new BOFRecord(BOFRecord.workbookGlobals);
/*  405: 706 */     this.outputFile.write(bof);
/*  406: 709 */     if (this.settings.getTemplate())
/*  407:     */     {
/*  408: 712 */       TemplateRecord trec = new TemplateRecord();
/*  409: 713 */       this.outputFile.write(trec);
/*  410:     */     }
/*  411: 717 */     InterfaceHeaderRecord ihr = new InterfaceHeaderRecord();
/*  412: 718 */     this.outputFile.write(ihr);
/*  413:     */     
/*  414: 720 */     MMSRecord mms = new MMSRecord(0, 0);
/*  415: 721 */     this.outputFile.write(mms);
/*  416:     */     
/*  417: 723 */     InterfaceEndRecord ier = new InterfaceEndRecord();
/*  418: 724 */     this.outputFile.write(ier);
/*  419:     */     
/*  420: 726 */     WriteAccessRecord wr = new WriteAccessRecord(this.settings.getWriteAccess());
/*  421: 727 */     this.outputFile.write(wr);
/*  422:     */     
/*  423: 729 */     CodepageRecord cp = new CodepageRecord();
/*  424: 730 */     this.outputFile.write(cp);
/*  425:     */     
/*  426: 732 */     DSFRecord dsf = new DSFRecord();
/*  427: 733 */     this.outputFile.write(dsf);
/*  428: 735 */     if (this.settings.getExcel9File())
/*  429:     */     {
/*  430: 739 */       Excel9FileRecord e9rec = new Excel9FileRecord();
/*  431: 740 */       this.outputFile.write(e9rec);
/*  432:     */     }
/*  433: 743 */     TabIdRecord tabid = new TabIdRecord(getNumberOfSheets());
/*  434: 744 */     this.outputFile.write(tabid);
/*  435: 746 */     if (this.containsMacros)
/*  436:     */     {
/*  437: 748 */       ObjProjRecord objproj = new ObjProjRecord();
/*  438: 749 */       this.outputFile.write(objproj);
/*  439:     */     }
/*  440: 752 */     if (this.buttonPropertySet != null) {
/*  441: 754 */       this.outputFile.write(this.buttonPropertySet);
/*  442:     */     }
/*  443: 757 */     FunctionGroupCountRecord fgcr = new FunctionGroupCountRecord();
/*  444: 758 */     this.outputFile.write(fgcr);
/*  445:     */     
/*  446:     */ 
/*  447: 761 */     WindowProtectRecord wpr = new WindowProtectRecord(
/*  448: 762 */       this.settings.getWindowProtected());
/*  449: 763 */     this.outputFile.write(wpr);
/*  450:     */     
/*  451: 765 */     ProtectRecord pr = new ProtectRecord(this.wbProtected);
/*  452: 766 */     this.outputFile.write(pr);
/*  453:     */     
/*  454: 768 */     PasswordRecord pw = new PasswordRecord(null);
/*  455: 769 */     this.outputFile.write(pw);
/*  456:     */     
/*  457: 771 */     Prot4RevRecord p4r = new Prot4RevRecord(false);
/*  458: 772 */     this.outputFile.write(p4r);
/*  459:     */     
/*  460: 774 */     Prot4RevPassRecord p4rp = new Prot4RevPassRecord();
/*  461: 775 */     this.outputFile.write(p4rp);
/*  462:     */     
/*  463:     */ 
/*  464:     */ 
/*  465: 779 */     boolean sheetSelected = false;
/*  466: 780 */     WritableSheetImpl wsheet = null;
/*  467: 781 */     int selectedSheetIndex = 0;
/*  468: 782 */     for (int i = 0; (i < getNumberOfSheets()) && (!sheetSelected); i++)
/*  469:     */     {
/*  470: 784 */       wsheet = (WritableSheetImpl)getSheet(i);
/*  471: 785 */       if (wsheet.getSettings().isSelected())
/*  472:     */       {
/*  473: 787 */         sheetSelected = true;
/*  474: 788 */         selectedSheetIndex = i;
/*  475:     */       }
/*  476:     */     }
/*  477: 792 */     if (!sheetSelected)
/*  478:     */     {
/*  479: 794 */       wsheet = (WritableSheetImpl)getSheet(0);
/*  480: 795 */       wsheet.getSettings().setSelected(true);
/*  481: 796 */       selectedSheetIndex = 0;
/*  482:     */     }
/*  483: 799 */     Window1Record w1r = new Window1Record(selectedSheetIndex);
/*  484: 800 */     this.outputFile.write(w1r);
/*  485:     */     
/*  486: 802 */     BackupRecord bkr = new BackupRecord(false);
/*  487: 803 */     this.outputFile.write(bkr);
/*  488:     */     
/*  489: 805 */     HideobjRecord ho = new HideobjRecord(this.settings.getHideobj());
/*  490: 806 */     this.outputFile.write(ho);
/*  491:     */     
/*  492: 808 */     NineteenFourRecord nf = new NineteenFourRecord(false);
/*  493: 809 */     this.outputFile.write(nf);
/*  494:     */     
/*  495: 811 */     PrecisionRecord pc = new PrecisionRecord(false);
/*  496: 812 */     this.outputFile.write(pc);
/*  497:     */     
/*  498: 814 */     RefreshAllRecord rar = new RefreshAllRecord(this.settings.getRefreshAll());
/*  499: 815 */     this.outputFile.write(rar);
/*  500:     */     
/*  501: 817 */     BookboolRecord bb = new BookboolRecord(true);
/*  502: 818 */     this.outputFile.write(bb);
/*  503:     */     
/*  504:     */ 
/*  505: 821 */     this.fonts.write(this.outputFile);
/*  506:     */     
/*  507:     */ 
/*  508: 824 */     this.formatRecords.write(this.outputFile);
/*  509: 827 */     if (this.formatRecords.getPalette() != null) {
/*  510: 829 */       this.outputFile.write(this.formatRecords.getPalette());
/*  511:     */     }
/*  512: 833 */     UsesElfsRecord uer = new UsesElfsRecord();
/*  513: 834 */     this.outputFile.write(uer);
/*  514:     */     
/*  515:     */ 
/*  516:     */ 
/*  517: 838 */     int[] boundsheetPos = new int[getNumberOfSheets()];
/*  518: 839 */     Sheet sheet = null;
/*  519: 841 */     for (int i = 0; i < getNumberOfSheets(); i++)
/*  520:     */     {
/*  521: 843 */       boundsheetPos[i] = this.outputFile.getPos();
/*  522: 844 */       sheet = getSheet(i);
/*  523: 845 */       BoundsheetRecord br = new BoundsheetRecord(sheet.getName());
/*  524: 846 */       if (sheet.getSettings().isHidden()) {
/*  525: 848 */         br.setHidden();
/*  526:     */       }
/*  527: 851 */       if (((WritableSheetImpl)this.sheets.get(i)).isChartOnly()) {
/*  528: 853 */         br.setChartOnly();
/*  529:     */       }
/*  530: 856 */       this.outputFile.write(br);
/*  531:     */     }
/*  532: 859 */     if (this.countryRecord == null)
/*  533:     */     {
/*  534: 861 */       CountryCode lang = 
/*  535: 862 */         CountryCode.getCountryCode(this.settings.getExcelDisplayLanguage());
/*  536: 863 */       if (lang == CountryCode.UNKNOWN)
/*  537:     */       {
/*  538: 865 */         logger.warn("Unknown country code " + 
/*  539: 866 */           this.settings.getExcelDisplayLanguage() + 
/*  540: 867 */           " using " + CountryCode.USA.getCode());
/*  541: 868 */         lang = CountryCode.USA;
/*  542:     */       }
/*  543: 870 */       CountryCode region = 
/*  544: 871 */         CountryCode.getCountryCode(this.settings.getExcelRegionalSettings());
/*  545: 872 */       this.countryRecord = new CountryRecord(lang, region);
/*  546: 873 */       if (region == CountryCode.UNKNOWN)
/*  547:     */       {
/*  548: 875 */         logger.warn("Unknown country code " + 
/*  549: 876 */           this.settings.getExcelDisplayLanguage() + 
/*  550: 877 */           " using " + CountryCode.UK.getCode());
/*  551: 878 */         region = CountryCode.UK;
/*  552:     */       }
/*  553:     */     }
/*  554: 882 */     this.outputFile.write(this.countryRecord);
/*  555: 885 */     if ((this.addInFunctionNames != null) && (this.addInFunctionNames.length > 0)) {
/*  556: 891 */       for (int i = 0; i < this.addInFunctionNames.length; i++)
/*  557:     */       {
/*  558: 893 */         ExternalNameRecord enr = new ExternalNameRecord(this.addInFunctionNames[i]);
/*  559: 894 */         this.outputFile.write(enr);
/*  560:     */       }
/*  561:     */     }
/*  562: 898 */     if (this.xctRecords != null) {
/*  563: 900 */       for (int i = 0; i < this.xctRecords.length; i++) {
/*  564: 902 */         this.outputFile.write(this.xctRecords[i]);
/*  565:     */       }
/*  566:     */     }
/*  567: 907 */     if (this.externSheet != null)
/*  568:     */     {
/*  569: 910 */       for (int i = 0; i < this.supbooks.size(); i++)
/*  570:     */       {
/*  571: 912 */         SupbookRecord supbook = (SupbookRecord)this.supbooks.get(i);
/*  572: 913 */         this.outputFile.write(supbook);
/*  573:     */       }
/*  574: 915 */       this.outputFile.write(this.externSheet);
/*  575:     */     }
/*  576: 919 */     if (this.names != null) {
/*  577: 921 */       for (int i = 0; i < this.names.size(); i++)
/*  578:     */       {
/*  579: 923 */         NameRecord n = (NameRecord)this.names.get(i);
/*  580: 924 */         this.outputFile.write(n);
/*  581:     */       }
/*  582:     */     }
/*  583: 929 */     if (this.drawingGroup != null) {
/*  584: 931 */       this.drawingGroup.write(this.outputFile);
/*  585:     */     }
/*  586: 934 */     this.sharedStrings.write(this.outputFile);
/*  587:     */     
/*  588: 936 */     EOFRecord eof = new EOFRecord();
/*  589: 937 */     this.outputFile.write(eof);
/*  590: 941 */     for (int i = 0; i < getNumberOfSheets(); i++)
/*  591:     */     {
/*  592: 945 */       this.outputFile.setData(
/*  593: 946 */         IntegerHelper.getFourBytes(this.outputFile.getPos()), 
/*  594: 947 */         boundsheetPos[i] + 4);
/*  595:     */       
/*  596: 949 */       wsheet = (WritableSheetImpl)getSheet(i);
/*  597: 950 */       wsheet.write();
/*  598:     */     }
/*  599:     */   }
/*  600:     */   
/*  601:     */   private void copyWorkbook(Workbook w)
/*  602:     */   {
/*  603: 963 */     int numSheets = w.getNumberOfSheets();
/*  604: 964 */     this.wbProtected = w.isProtected();
/*  605: 965 */     Sheet s = null;
/*  606: 966 */     WritableSheetImpl ws = null;
/*  607: 967 */     for (int i = 0; i < numSheets; i++)
/*  608:     */     {
/*  609: 969 */       s = w.getSheet(i);
/*  610: 970 */       ws = (WritableSheetImpl)createSheet(s.getName(), i, false);
/*  611: 971 */       ws.copy(s);
/*  612:     */     }
/*  613:     */   }
/*  614:     */   
/*  615:     */   public void copySheet(int s, String name, int index)
/*  616:     */   {
/*  617: 985 */     WritableSheet sheet = getSheet(s);
/*  618: 986 */     WritableSheetImpl ws = (WritableSheetImpl)createSheet(name, index);
/*  619: 987 */     ws.copy(sheet);
/*  620:     */   }
/*  621:     */   
/*  622:     */   public void copySheet(String s, String name, int index)
/*  623:     */   {
/*  624:1000 */     WritableSheet sheet = getSheet(s);
/*  625:1001 */     WritableSheetImpl ws = (WritableSheetImpl)createSheet(name, index);
/*  626:1002 */     ws.copy(sheet);
/*  627:     */   }
/*  628:     */   
/*  629:     */   public void setProtected(boolean prot)
/*  630:     */   {
/*  631:1012 */     this.wbProtected = prot;
/*  632:     */   }
/*  633:     */   
/*  634:     */   private void rationalize()
/*  635:     */   {
/*  636:1021 */     IndexMapping fontMapping = this.formatRecords.rationalizeFonts();
/*  637:1022 */     IndexMapping formatMapping = this.formatRecords.rationalizeDisplayFormats();
/*  638:1023 */     IndexMapping xfMapping = this.formatRecords.rationalize(fontMapping, 
/*  639:1024 */       formatMapping);
/*  640:     */     
/*  641:1026 */     WritableSheetImpl wsi = null;
/*  642:1027 */     for (int i = 0; i < this.sheets.size(); i++)
/*  643:     */     {
/*  644:1029 */       wsi = (WritableSheetImpl)this.sheets.get(i);
/*  645:1030 */       wsi.rationalize(xfMapping, fontMapping, formatMapping);
/*  646:     */     }
/*  647:     */   }
/*  648:     */   
/*  649:     */   private int getInternalSheetIndex(String name)
/*  650:     */   {
/*  651:1042 */     int index = -1;
/*  652:1043 */     String[] names = getSheetNames();
/*  653:1044 */     for (int i = 0; i < names.length; i++) {
/*  654:1046 */       if (name.equals(names[i]))
/*  655:     */       {
/*  656:1048 */         index = i;
/*  657:1049 */         break;
/*  658:     */       }
/*  659:     */     }
/*  660:1053 */     return index;
/*  661:     */   }
/*  662:     */   
/*  663:     */   public String getExternalSheetName(int index)
/*  664:     */   {
/*  665:1064 */     int supbookIndex = this.externSheet.getSupbookIndex(index);
/*  666:1065 */     SupbookRecord sr = (SupbookRecord)this.supbooks.get(supbookIndex);
/*  667:     */     
/*  668:1067 */     int firstTab = this.externSheet.getFirstTabIndex(index);
/*  669:1069 */     if (sr.getType() == SupbookRecord.INTERNAL)
/*  670:     */     {
/*  671:1072 */       WritableSheet ws = getSheet(firstTab);
/*  672:     */       
/*  673:1074 */       return ws.getName();
/*  674:     */     }
/*  675:1076 */     if (sr.getType() == SupbookRecord.EXTERNAL)
/*  676:     */     {
/*  677:1078 */       String name = sr.getFileName() + sr.getSheetName(firstTab);
/*  678:1079 */       return name;
/*  679:     */     }
/*  680:1083 */     logger.warn("Unknown Supbook 1");
/*  681:1084 */     return "[UNKNOWN]";
/*  682:     */   }
/*  683:     */   
/*  684:     */   public String getLastExternalSheetName(int index)
/*  685:     */   {
/*  686:1095 */     int supbookIndex = this.externSheet.getSupbookIndex(index);
/*  687:1096 */     SupbookRecord sr = (SupbookRecord)this.supbooks.get(supbookIndex);
/*  688:     */     
/*  689:1098 */     int lastTab = this.externSheet.getLastTabIndex(index);
/*  690:1100 */     if (sr.getType() == SupbookRecord.INTERNAL)
/*  691:     */     {
/*  692:1103 */       WritableSheet ws = getSheet(lastTab);
/*  693:     */       
/*  694:1105 */       return ws.getName();
/*  695:     */     }
/*  696:1107 */     if (sr.getType() == SupbookRecord.EXTERNAL) {
/*  697:1109 */       Assert.verify(false);
/*  698:     */     }
/*  699:1113 */     logger.warn("Unknown Supbook 2");
/*  700:1114 */     return "[UNKNOWN]";
/*  701:     */   }
/*  702:     */   
/*  703:     */   public jxl.read.biff.BOFRecord getWorkbookBof()
/*  704:     */   {
/*  705:1125 */     return null;
/*  706:     */   }
/*  707:     */   
/*  708:     */   public int getExternalSheetIndex(int index)
/*  709:     */   {
/*  710:1137 */     if (this.externSheet == null) {
/*  711:1139 */       return index;
/*  712:     */     }
/*  713:1142 */     Assert.verify(this.externSheet != null);
/*  714:     */     
/*  715:1144 */     int firstTab = this.externSheet.getFirstTabIndex(index);
/*  716:     */     
/*  717:1146 */     return firstTab;
/*  718:     */   }
/*  719:     */   
/*  720:     */   public int getLastExternalSheetIndex(int index)
/*  721:     */   {
/*  722:1157 */     if (this.externSheet == null) {
/*  723:1159 */       return index;
/*  724:     */     }
/*  725:1162 */     Assert.verify(this.externSheet != null);
/*  726:     */     
/*  727:1164 */     int lastTab = this.externSheet.getLastTabIndex(index);
/*  728:     */     
/*  729:1166 */     return lastTab;
/*  730:     */   }
/*  731:     */   
/*  732:     */   public int getExternalSheetIndex(String sheetName)
/*  733:     */   {
/*  734:1177 */     if (this.externSheet == null)
/*  735:     */     {
/*  736:1179 */       this.externSheet = new ExternalSheetRecord();
/*  737:1180 */       this.supbooks = new ArrayList();
/*  738:1181 */       this.supbooks.add(new SupbookRecord(getNumberOfSheets(), this.settings));
/*  739:     */     }
/*  740:1185 */     boolean found = false;
/*  741:1186 */     Iterator i = this.sheets.iterator();
/*  742:1187 */     int sheetpos = 0;
/*  743:1188 */     WritableSheetImpl s = null;
/*  744:1190 */     while ((i.hasNext()) && (!found))
/*  745:     */     {
/*  746:1192 */       s = (WritableSheetImpl)i.next();
/*  747:1194 */       if (s.getName().equals(sheetName)) {
/*  748:1196 */         found = true;
/*  749:     */       } else {
/*  750:1200 */         sheetpos++;
/*  751:     */       }
/*  752:     */     }
/*  753:1204 */     if (found)
/*  754:     */     {
/*  755:1208 */       SupbookRecord supbook = (SupbookRecord)this.supbooks.get(0);
/*  756:1209 */       if ((supbook.getType() != SupbookRecord.INTERNAL) || 
/*  757:1210 */         (supbook.getNumberOfSheets() != getNumberOfSheets())) {
/*  758:1212 */         logger.warn("Cannot find sheet " + sheetName + " in supbook record");
/*  759:     */       }
/*  760:1215 */       return this.externSheet.getIndex(0, sheetpos);
/*  761:     */     }
/*  762:1219 */     int closeSquareBracketsIndex = sheetName.lastIndexOf(']');
/*  763:1220 */     int openSquareBracketsIndex = sheetName.lastIndexOf('[');
/*  764:1222 */     if ((closeSquareBracketsIndex == -1) || 
/*  765:1223 */       (openSquareBracketsIndex == -1))
/*  766:     */     {
/*  767:1225 */       logger.warn("Square brackets");
/*  768:1226 */       return -1;
/*  769:     */     }
/*  770:1229 */     String worksheetName = sheetName.substring(closeSquareBracketsIndex + 1);
/*  771:1230 */     String workbookName = sheetName.substring(openSquareBracketsIndex + 1, 
/*  772:1231 */       closeSquareBracketsIndex);
/*  773:1232 */     String path = sheetName.substring(0, openSquareBracketsIndex);
/*  774:1233 */     String fileName = path + workbookName;
/*  775:     */     
/*  776:1235 */     boolean supbookFound = false;
/*  777:1236 */     SupbookRecord externalSupbook = null;
/*  778:1237 */     int supbookIndex = -1;
/*  779:1238 */     for (int ind = 0; (ind < this.supbooks.size()) && (!supbookFound); ind++)
/*  780:     */     {
/*  781:1240 */       externalSupbook = (SupbookRecord)this.supbooks.get(ind);
/*  782:1241 */       if ((externalSupbook.getType() == SupbookRecord.EXTERNAL) && 
/*  783:1242 */         (externalSupbook.getFileName().equals(fileName)))
/*  784:     */       {
/*  785:1244 */         supbookFound = true;
/*  786:1245 */         supbookIndex = ind;
/*  787:     */       }
/*  788:     */     }
/*  789:1249 */     if (!supbookFound)
/*  790:     */     {
/*  791:1251 */       externalSupbook = new SupbookRecord(fileName, this.settings);
/*  792:1252 */       supbookIndex = this.supbooks.size();
/*  793:1253 */       this.supbooks.add(externalSupbook);
/*  794:     */     }
/*  795:1256 */     int sheetIndex = externalSupbook.getSheetIndex(worksheetName);
/*  796:     */     
/*  797:1258 */     return this.externSheet.getIndex(supbookIndex, sheetIndex);
/*  798:     */   }
/*  799:     */   
/*  800:     */   public int getLastExternalSheetIndex(String sheetName)
/*  801:     */   {
/*  802:1268 */     if (this.externSheet == null)
/*  803:     */     {
/*  804:1270 */       this.externSheet = new ExternalSheetRecord();
/*  805:1271 */       this.supbooks = new ArrayList();
/*  806:1272 */       this.supbooks.add(new SupbookRecord(getNumberOfSheets(), this.settings));
/*  807:     */     }
/*  808:1276 */     boolean found = false;
/*  809:1277 */     Iterator i = this.sheets.iterator();
/*  810:1278 */     int sheetpos = 0;
/*  811:1279 */     WritableSheetImpl s = null;
/*  812:1281 */     while ((i.hasNext()) && (!found))
/*  813:     */     {
/*  814:1283 */       s = (WritableSheetImpl)i.next();
/*  815:1285 */       if (s.getName().equals(sheetName)) {
/*  816:1287 */         found = true;
/*  817:     */       } else {
/*  818:1291 */         sheetpos++;
/*  819:     */       }
/*  820:     */     }
/*  821:1295 */     if (!found) {
/*  822:1297 */       return -1;
/*  823:     */     }
/*  824:1302 */     SupbookRecord supbook = (SupbookRecord)this.supbooks.get(0);
/*  825:1303 */     Assert.verify((supbook.getType() == SupbookRecord.INTERNAL) && 
/*  826:1304 */       (supbook.getNumberOfSheets() == getNumberOfSheets()));
/*  827:     */     
/*  828:1306 */     return this.externSheet.getIndex(0, sheetpos);
/*  829:     */   }
/*  830:     */   
/*  831:     */   public void setColourRGB(Colour c, int r, int g, int b)
/*  832:     */   {
/*  833:1319 */     this.formatRecords.setColourRGB(c, r, g, b);
/*  834:     */   }
/*  835:     */   
/*  836:     */   public RGB getColourRGB(Colour c)
/*  837:     */   {
/*  838:1329 */     return this.formatRecords.getColourRGB(c);
/*  839:     */   }
/*  840:     */   
/*  841:     */   public String getName(int index)
/*  842:     */   {
/*  843:1340 */     Assert.verify((index >= 0) && (index < this.names.size()));
/*  844:1341 */     NameRecord n = (NameRecord)this.names.get(index);
/*  845:1342 */     return n.getName();
/*  846:     */   }
/*  847:     */   
/*  848:     */   public int getNameIndex(String name)
/*  849:     */   {
/*  850:1353 */     NameRecord nr = (NameRecord)this.nameRecords.get(name);
/*  851:1354 */     return nr != null ? nr.getIndex() : -1;
/*  852:     */   }
/*  853:     */   
/*  854:     */   void addRCIRCell(CellValue cv)
/*  855:     */   {
/*  856:1365 */     this.rcirCells.add(cv);
/*  857:     */   }
/*  858:     */   
/*  859:     */   void columnInserted(WritableSheetImpl s, int col)
/*  860:     */   {
/*  861:1377 */     int externalSheetIndex = getExternalSheetIndex(s.getName());
/*  862:1378 */     for (Iterator i = this.rcirCells.iterator(); i.hasNext();)
/*  863:     */     {
/*  864:1380 */       CellValue cv = (CellValue)i.next();
/*  865:1381 */       cv.columnInserted(s, externalSheetIndex, col);
/*  866:     */     }
/*  867:1385 */     if (this.names != null) {
/*  868:1387 */       for (Iterator i = this.names.iterator(); i.hasNext();)
/*  869:     */       {
/*  870:1389 */         NameRecord nameRecord = (NameRecord)i.next();
/*  871:1390 */         nameRecord.columnInserted(externalSheetIndex, col);
/*  872:     */       }
/*  873:     */     }
/*  874:     */   }
/*  875:     */   
/*  876:     */   void columnRemoved(WritableSheetImpl s, int col)
/*  877:     */   {
/*  878:1404 */     int externalSheetIndex = getExternalSheetIndex(s.getName());
/*  879:1405 */     for (Iterator i = this.rcirCells.iterator(); i.hasNext();)
/*  880:     */     {
/*  881:1407 */       CellValue cv = (CellValue)i.next();
/*  882:1408 */       cv.columnRemoved(s, externalSheetIndex, col);
/*  883:     */     }
/*  884:1412 */     ArrayList removedNames = new ArrayList();
/*  885:1413 */     if (this.names != null)
/*  886:     */     {
/*  887:1415 */       for (Iterator i = this.names.iterator(); i.hasNext();)
/*  888:     */       {
/*  889:1417 */         NameRecord nameRecord = (NameRecord)i.next();
/*  890:1418 */         boolean removeName = nameRecord.columnRemoved(externalSheetIndex, 
/*  891:1419 */           col);
/*  892:1421 */         if (removeName) {
/*  893:1423 */           removedNames.add(nameRecord);
/*  894:     */         }
/*  895:     */       }
/*  896:1428 */       for (Iterator i = removedNames.iterator(); i.hasNext();)
/*  897:     */       {
/*  898:1430 */         NameRecord nameRecord = (NameRecord)i.next();
/*  899:1431 */         boolean removed = this.names.remove(nameRecord);
/*  900:1432 */         Assert.verify(removed, "Could not remove name " + 
/*  901:1433 */           nameRecord.getName());
/*  902:     */       }
/*  903:     */     }
/*  904:     */   }
/*  905:     */   
/*  906:     */   void rowInserted(WritableSheetImpl s, int row)
/*  907:     */   {
/*  908:1447 */     int externalSheetIndex = getExternalSheetIndex(s.getName());
/*  909:1450 */     for (Iterator i = this.rcirCells.iterator(); i.hasNext();)
/*  910:     */     {
/*  911:1452 */       CellValue cv = (CellValue)i.next();
/*  912:1453 */       cv.rowInserted(s, externalSheetIndex, row);
/*  913:     */     }
/*  914:1457 */     if (this.names != null) {
/*  915:1459 */       for (Iterator i = this.names.iterator(); i.hasNext();)
/*  916:     */       {
/*  917:1461 */         NameRecord nameRecord = (NameRecord)i.next();
/*  918:1462 */         nameRecord.rowInserted(externalSheetIndex, row);
/*  919:     */       }
/*  920:     */     }
/*  921:     */   }
/*  922:     */   
/*  923:     */   void rowRemoved(WritableSheetImpl s, int row)
/*  924:     */   {
/*  925:1476 */     int externalSheetIndex = getExternalSheetIndex(s.getName());
/*  926:1477 */     for (Iterator i = this.rcirCells.iterator(); i.hasNext();)
/*  927:     */     {
/*  928:1479 */       CellValue cv = (CellValue)i.next();
/*  929:1480 */       cv.rowRemoved(s, externalSheetIndex, row);
/*  930:     */     }
/*  931:1484 */     ArrayList removedNames = new ArrayList();
/*  932:1485 */     if (this.names != null)
/*  933:     */     {
/*  934:1487 */       for (Iterator i = this.names.iterator(); i.hasNext();)
/*  935:     */       {
/*  936:1489 */         NameRecord nameRecord = (NameRecord)i.next();
/*  937:1490 */         boolean removeName = nameRecord.rowRemoved(externalSheetIndex, row);
/*  938:1492 */         if (removeName) {
/*  939:1494 */           removedNames.add(nameRecord);
/*  940:     */         }
/*  941:     */       }
/*  942:1499 */       for (Iterator i = removedNames.iterator(); i.hasNext();)
/*  943:     */       {
/*  944:1501 */         NameRecord nameRecord = (NameRecord)i.next();
/*  945:1502 */         boolean removed = this.names.remove(nameRecord);
/*  946:1503 */         Assert.verify(removed, "Could not remove name " + 
/*  947:1504 */           nameRecord.getName());
/*  948:     */       }
/*  949:     */     }
/*  950:     */   }
/*  951:     */   
/*  952:     */   public WritableCell findCellByName(String name)
/*  953:     */   {
/*  954:1520 */     NameRecord nr = (NameRecord)this.nameRecords.get(name);
/*  955:1522 */     if (nr == null) {
/*  956:1524 */       return null;
/*  957:     */     }
/*  958:1527 */     NameRecord.NameRange[] ranges = nr.getRanges();
/*  959:     */     
/*  960:     */ 
/*  961:1530 */     int sheetIndex = getExternalSheetIndex(ranges[0].getExternalSheet());
/*  962:1531 */     WritableSheet s = getSheet(sheetIndex);
/*  963:1532 */     WritableCell cell = s.getWritableCell(ranges[0].getFirstColumn(), 
/*  964:1533 */       ranges[0].getFirstRow());
/*  965:     */     
/*  966:1535 */     return cell;
/*  967:     */   }
/*  968:     */   
/*  969:     */   public Range[] findByName(String name)
/*  970:     */   {
/*  971:1554 */     NameRecord nr = (NameRecord)this.nameRecords.get(name);
/*  972:1556 */     if (nr == null) {
/*  973:1558 */       return null;
/*  974:     */     }
/*  975:1561 */     NameRecord.NameRange[] ranges = nr.getRanges();
/*  976:     */     
/*  977:1563 */     Range[] cellRanges = new Range[ranges.length];
/*  978:1565 */     for (int i = 0; i < ranges.length; i++) {
/*  979:1567 */       cellRanges[i] = new RangeImpl(
/*  980:1568 */         this, 
/*  981:1569 */         getExternalSheetIndex(ranges[i].getExternalSheet()), 
/*  982:1570 */         ranges[i].getFirstColumn(), 
/*  983:1571 */         ranges[i].getFirstRow(), 
/*  984:1572 */         getLastExternalSheetIndex(ranges[i].getExternalSheet()), 
/*  985:1573 */         ranges[i].getLastColumn(), 
/*  986:1574 */         ranges[i].getLastRow());
/*  987:     */     }
/*  988:1577 */     return cellRanges;
/*  989:     */   }
/*  990:     */   
/*  991:     */   void addDrawing(DrawingGroupObject d)
/*  992:     */   {
/*  993:1587 */     if (this.drawingGroup == null) {
/*  994:1589 */       this.drawingGroup = new DrawingGroup(Origin.WRITE);
/*  995:     */     }
/*  996:1592 */     this.drawingGroup.add(d);
/*  997:     */   }
/*  998:     */   
/*  999:     */   void removeDrawing(Drawing d)
/* 1000:     */   {
/* 1001:1602 */     Assert.verify(this.drawingGroup != null);
/* 1002:     */     
/* 1003:1604 */     this.drawingGroup.remove(d);
/* 1004:     */   }
/* 1005:     */   
/* 1006:     */   DrawingGroup getDrawingGroup()
/* 1007:     */   {
/* 1008:1614 */     return this.drawingGroup;
/* 1009:     */   }
/* 1010:     */   
/* 1011:     */   DrawingGroup createDrawingGroup()
/* 1012:     */   {
/* 1013:1626 */     if (this.drawingGroup == null) {
/* 1014:1628 */       this.drawingGroup = new DrawingGroup(Origin.WRITE);
/* 1015:     */     }
/* 1016:1631 */     return this.drawingGroup;
/* 1017:     */   }
/* 1018:     */   
/* 1019:     */   public String[] getRangeNames()
/* 1020:     */   {
/* 1021:1641 */     if (this.names == null) {
/* 1022:1643 */       return new String[0];
/* 1023:     */     }
/* 1024:1646 */     String[] n = new String[this.names.size()];
/* 1025:1647 */     for (int i = 0; i < this.names.size(); i++)
/* 1026:     */     {
/* 1027:1649 */       NameRecord nr = (NameRecord)this.names.get(i);
/* 1028:1650 */       n[i] = nr.getName();
/* 1029:     */     }
/* 1030:1653 */     return n;
/* 1031:     */   }
/* 1032:     */   
/* 1033:     */   public void removeRangeName(String name)
/* 1034:     */   {
/* 1035:1663 */     int pos = 0;
/* 1036:1664 */     boolean found = false;
/* 1037:1665 */     for (Iterator i = this.names.iterator(); (i.hasNext()) && (!found);)
/* 1038:     */     {
/* 1039:1667 */       NameRecord nr = (NameRecord)i.next();
/* 1040:1668 */       if (nr.getName().equals(name)) {
/* 1041:1670 */         found = true;
/* 1042:     */       } else {
/* 1043:1674 */         pos++;
/* 1044:     */       }
/* 1045:     */     }
/* 1046:1681 */     if (found)
/* 1047:     */     {
/* 1048:1683 */       this.names.remove(pos);
/* 1049:1684 */       if (this.nameRecords.remove(name) == null) {
/* 1050:1686 */         logger.warn("Could not remove " + name + " from index lookups");
/* 1051:     */       }
/* 1052:     */     }
/* 1053:     */   }
/* 1054:     */   
/* 1055:     */   Styles getStyles()
/* 1056:     */   {
/* 1057:1698 */     return this.styles;
/* 1058:     */   }
/* 1059:     */   
/* 1060:     */   public void addNameArea(String name, WritableSheet sheet, int firstCol, int firstRow, int lastCol, int lastRow)
/* 1061:     */   {
/* 1062:1718 */     addNameArea(name, sheet, firstCol, firstRow, lastCol, lastRow, true);
/* 1063:     */   }
/* 1064:     */   
/* 1065:     */   void addNameArea(String name, WritableSheet sheet, int firstCol, int firstRow, int lastCol, int lastRow, boolean global)
/* 1066:     */   {
/* 1067:1741 */     if (this.names == null) {
/* 1068:1743 */       this.names = new ArrayList();
/* 1069:     */     }
/* 1070:1746 */     int externalSheetIndex = getExternalSheetIndex(sheet.getName());
/* 1071:     */     
/* 1072:     */ 
/* 1073:1749 */     NameRecord nr = 
/* 1074:1750 */       new NameRecord(name, 
/* 1075:1751 */       this.names.size(), 
/* 1076:1752 */       externalSheetIndex, 
/* 1077:1753 */       firstRow, lastRow, 
/* 1078:1754 */       firstCol, lastCol, 
/* 1079:1755 */       global);
/* 1080:     */     
/* 1081:     */ 
/* 1082:1758 */     this.names.add(nr);
/* 1083:     */     
/* 1084:     */ 
/* 1085:1761 */     this.nameRecords.put(name, nr);
/* 1086:     */   }
/* 1087:     */   
/* 1088:     */   void addNameArea(BuiltInName name, WritableSheet sheet, int firstCol, int firstRow, int lastCol, int lastRow, boolean global)
/* 1089:     */   {
/* 1090:1784 */     if (this.names == null) {
/* 1091:1786 */       this.names = new ArrayList();
/* 1092:     */     }
/* 1093:1789 */     int index = getInternalSheetIndex(sheet.getName());
/* 1094:1790 */     int externalSheetIndex = getExternalSheetIndex(sheet.getName());
/* 1095:     */     
/* 1096:     */ 
/* 1097:1793 */     NameRecord nr = 
/* 1098:1794 */       new NameRecord(name, 
/* 1099:1795 */       index, 
/* 1100:1796 */       externalSheetIndex, 
/* 1101:1797 */       firstRow, lastRow, 
/* 1102:1798 */       firstCol, lastCol, 
/* 1103:1799 */       global);
/* 1104:     */     
/* 1105:     */ 
/* 1106:1802 */     this.names.add(nr);
/* 1107:     */     
/* 1108:     */ 
/* 1109:1805 */     this.nameRecords.put(name, nr);
/* 1110:     */   }
/* 1111:     */   
/* 1112:     */   void addNameArea(BuiltInName name, WritableSheet sheet, int firstCol, int firstRow, int lastCol, int lastRow, int firstCol2, int firstRow2, int lastCol2, int lastRow2, boolean global)
/* 1113:     */   {
/* 1114:1836 */     if (this.names == null) {
/* 1115:1838 */       this.names = new ArrayList();
/* 1116:     */     }
/* 1117:1841 */     int index = getInternalSheetIndex(sheet.getName());
/* 1118:1842 */     int externalSheetIndex = getExternalSheetIndex(sheet.getName());
/* 1119:     */     
/* 1120:     */ 
/* 1121:1845 */     NameRecord nr = 
/* 1122:1846 */       new NameRecord(name, 
/* 1123:1847 */       index, 
/* 1124:1848 */       externalSheetIndex, 
/* 1125:1849 */       firstRow2, lastRow2, 
/* 1126:1850 */       firstCol2, lastCol2, 
/* 1127:1851 */       firstRow, lastRow, 
/* 1128:1852 */       firstCol, lastCol, 
/* 1129:1853 */       global);
/* 1130:     */     
/* 1131:     */ 
/* 1132:1856 */     this.names.add(nr);
/* 1133:     */     
/* 1134:     */ 
/* 1135:1859 */     this.nameRecords.put(name, nr);
/* 1136:     */   }
/* 1137:     */   
/* 1138:     */   WorkbookSettings getSettings()
/* 1139:     */   {
/* 1140:1867 */     return this.settings;
/* 1141:     */   }
/* 1142:     */   
/* 1143:     */   public WritableCell getWritableCell(String loc)
/* 1144:     */   {
/* 1145:1881 */     WritableSheet s = getSheet(CellReferenceHelper.getSheet(loc));
/* 1146:1882 */     return s.getWritableCell(loc);
/* 1147:     */   }
/* 1148:     */   
/* 1149:     */   public WritableSheet importSheet(String name, int index, Sheet sheet)
/* 1150:     */   {
/* 1151:1896 */     WritableSheet ws = createSheet(name, index);
/* 1152:1897 */     ((WritableSheetImpl)ws).importSheet(sheet);
/* 1153:     */     
/* 1154:1899 */     return ws;
/* 1155:     */   }
/* 1156:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.WritableWorkbookImpl
 * JD-Core Version:    0.7.0.1
 */