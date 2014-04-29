/*    1:     */ package jxl.read.biff;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.HashMap;
/*    5:     */ import java.util.Iterator;
/*    6:     */ import java.util.Set;
/*    7:     */ import jxl.Cell;
/*    8:     */ import jxl.Range;
/*    9:     */ import jxl.Sheet;
/*   10:     */ import jxl.Workbook;
/*   11:     */ import jxl.WorkbookSettings;
/*   12:     */ import jxl.biff.BuiltInName;
/*   13:     */ import jxl.biff.CellReferenceHelper;
/*   14:     */ import jxl.biff.EmptyCell;
/*   15:     */ import jxl.biff.FontRecord;
/*   16:     */ import jxl.biff.Fonts;
/*   17:     */ import jxl.biff.FormatRecord;
/*   18:     */ import jxl.biff.FormattingRecords;
/*   19:     */ import jxl.biff.NameRangeException;
/*   20:     */ import jxl.biff.NumFormatRecordsException;
/*   21:     */ import jxl.biff.PaletteRecord;
/*   22:     */ import jxl.biff.RangeImpl;
/*   23:     */ import jxl.biff.StringHelper;
/*   24:     */ import jxl.biff.Type;
/*   25:     */ import jxl.biff.WorkbookMethods;
/*   26:     */ import jxl.biff.XCTRecord;
/*   27:     */ import jxl.biff.XFRecord;
/*   28:     */ import jxl.biff.drawing.DrawingGroup;
/*   29:     */ import jxl.biff.drawing.MsoDrawingGroupRecord;
/*   30:     */ import jxl.biff.drawing.Origin;
/*   31:     */ import jxl.biff.formula.ExternalSheet;
/*   32:     */ import jxl.common.Assert;
/*   33:     */ import jxl.common.Logger;
/*   34:     */ 
/*   35:     */ public class WorkbookParser
/*   36:     */   extends Workbook
/*   37:     */   implements ExternalSheet, WorkbookMethods
/*   38:     */ {
/*   39:  65 */   private static Logger logger = Logger.getLogger(WorkbookParser.class);
/*   40:     */   private File excelFile;
/*   41:     */   private int bofs;
/*   42:     */   private boolean nineteenFour;
/*   43:     */   private SSTRecord sharedStrings;
/*   44:     */   private ArrayList boundsheets;
/*   45:     */   private FormattingRecords formattingRecords;
/*   46:     */   private Fonts fonts;
/*   47:     */   private ArrayList sheets;
/*   48:     */   private SheetImpl lastSheet;
/*   49:     */   private int lastSheetIndex;
/*   50:     */   private HashMap namedRecords;
/*   51:     */   private ArrayList nameTable;
/*   52:     */   private ArrayList addInFunctions;
/*   53:     */   private ExternalSheetRecord externSheet;
/*   54:     */   private ArrayList supbooks;
/*   55:     */   private BOFRecord workbookBof;
/*   56:     */   private MsoDrawingGroupRecord msoDrawingGroup;
/*   57:     */   private ButtonPropertySetRecord buttonPropertySet;
/*   58:     */   private boolean wbProtected;
/*   59:     */   private boolean containsMacros;
/*   60:     */   private WorkbookSettings settings;
/*   61:     */   private DrawingGroup drawingGroup;
/*   62:     */   private CountryRecord countryRecord;
/*   63:     */   private ArrayList xctRecords;
/*   64:     */   
/*   65:     */   public WorkbookParser(File f, WorkbookSettings s)
/*   66:     */   {
/*   67: 188 */     this.excelFile = f;
/*   68: 189 */     this.boundsheets = new ArrayList(10);
/*   69: 190 */     this.fonts = new Fonts();
/*   70: 191 */     this.formattingRecords = new FormattingRecords(this.fonts);
/*   71: 192 */     this.sheets = new ArrayList(10);
/*   72: 193 */     this.supbooks = new ArrayList(10);
/*   73: 194 */     this.namedRecords = new HashMap();
/*   74: 195 */     this.lastSheetIndex = -1;
/*   75: 196 */     this.wbProtected = false;
/*   76: 197 */     this.containsMacros = false;
/*   77: 198 */     this.settings = s;
/*   78: 199 */     this.xctRecords = new ArrayList(10);
/*   79:     */   }
/*   80:     */   
/*   81:     */   public Sheet[] getSheets()
/*   82:     */   {
/*   83: 212 */     Sheet[] sheetArray = new Sheet[getNumberOfSheets()];
/*   84: 213 */     return (Sheet[])this.sheets.toArray(sheetArray);
/*   85:     */   }
/*   86:     */   
/*   87:     */   public Sheet getReadSheet(int index)
/*   88:     */   {
/*   89: 225 */     return getSheet(index);
/*   90:     */   }
/*   91:     */   
/*   92:     */   public Sheet getSheet(int index)
/*   93:     */   {
/*   94: 239 */     if ((this.lastSheet != null) && (this.lastSheetIndex == index)) {
/*   95: 241 */       return this.lastSheet;
/*   96:     */     }
/*   97: 245 */     if (this.lastSheet != null)
/*   98:     */     {
/*   99: 247 */       this.lastSheet.clear();
/*  100: 249 */       if (!this.settings.getGCDisabled()) {
/*  101: 251 */         System.gc();
/*  102:     */       }
/*  103:     */     }
/*  104: 255 */     this.lastSheet = ((SheetImpl)this.sheets.get(index));
/*  105: 256 */     this.lastSheetIndex = index;
/*  106: 257 */     this.lastSheet.readSheet();
/*  107:     */     
/*  108: 259 */     return this.lastSheet;
/*  109:     */   }
/*  110:     */   
/*  111:     */   public Sheet getSheet(String name)
/*  112:     */   {
/*  113: 271 */     int pos = 0;
/*  114: 272 */     boolean found = false;
/*  115: 273 */     Iterator i = this.boundsheets.iterator();
/*  116: 274 */     BoundsheetRecord br = null;
/*  117: 276 */     while ((i.hasNext()) && (!found))
/*  118:     */     {
/*  119: 278 */       br = (BoundsheetRecord)i.next();
/*  120: 280 */       if (br.getName().equals(name)) {
/*  121: 282 */         found = true;
/*  122:     */       } else {
/*  123: 286 */         pos++;
/*  124:     */       }
/*  125:     */     }
/*  126: 290 */     return found ? getSheet(pos) : null;
/*  127:     */   }
/*  128:     */   
/*  129:     */   public String[] getSheetNames()
/*  130:     */   {
/*  131: 300 */     String[] names = new String[this.boundsheets.size()];
/*  132:     */     
/*  133: 302 */     BoundsheetRecord br = null;
/*  134: 303 */     for (int i = 0; i < names.length; i++)
/*  135:     */     {
/*  136: 305 */       br = (BoundsheetRecord)this.boundsheets.get(i);
/*  137: 306 */       names[i] = br.getName();
/*  138:     */     }
/*  139: 309 */     return names;
/*  140:     */   }
/*  141:     */   
/*  142:     */   public int getExternalSheetIndex(int index)
/*  143:     */   {
/*  144: 325 */     if (this.workbookBof.isBiff7()) {
/*  145: 327 */       return index;
/*  146:     */     }
/*  147: 330 */     Assert.verify(this.externSheet != null);
/*  148:     */     
/*  149: 332 */     int firstTab = this.externSheet.getFirstTabIndex(index);
/*  150:     */     
/*  151: 334 */     return firstTab;
/*  152:     */   }
/*  153:     */   
/*  154:     */   public int getLastExternalSheetIndex(int index)
/*  155:     */   {
/*  156: 349 */     if (this.workbookBof.isBiff7()) {
/*  157: 351 */       return index;
/*  158:     */     }
/*  159: 354 */     Assert.verify(this.externSheet != null);
/*  160:     */     
/*  161: 356 */     int lastTab = this.externSheet.getLastTabIndex(index);
/*  162:     */     
/*  163: 358 */     return lastTab;
/*  164:     */   }
/*  165:     */   
/*  166:     */   public String getExternalSheetName(int index)
/*  167:     */   {
/*  168: 371 */     if (this.workbookBof.isBiff7())
/*  169:     */     {
/*  170: 373 */       BoundsheetRecord br = (BoundsheetRecord)this.boundsheets.get(index);
/*  171:     */       
/*  172: 375 */       return br.getName();
/*  173:     */     }
/*  174: 378 */     int supbookIndex = this.externSheet.getSupbookIndex(index);
/*  175: 379 */     SupbookRecord sr = (SupbookRecord)this.supbooks.get(supbookIndex);
/*  176:     */     
/*  177: 381 */     int firstTab = this.externSheet.getFirstTabIndex(index);
/*  178: 382 */     int lastTab = this.externSheet.getLastTabIndex(index);
/*  179: 383 */     String firstTabName = "";
/*  180: 384 */     String lastTabName = "";
/*  181: 386 */     if (sr.getType() == SupbookRecord.INTERNAL)
/*  182:     */     {
/*  183: 389 */       if (firstTab == 65535)
/*  184:     */       {
/*  185: 391 */         firstTabName = "#REF";
/*  186:     */       }
/*  187:     */       else
/*  188:     */       {
/*  189: 395 */         BoundsheetRecord br = (BoundsheetRecord)this.boundsheets.get(firstTab);
/*  190: 396 */         firstTabName = br.getName();
/*  191:     */       }
/*  192: 399 */       if (lastTab == 65535)
/*  193:     */       {
/*  194: 401 */         lastTabName = "#REF";
/*  195:     */       }
/*  196:     */       else
/*  197:     */       {
/*  198: 405 */         BoundsheetRecord br = (BoundsheetRecord)this.boundsheets.get(lastTab);
/*  199: 406 */         lastTabName = br.getName();
/*  200:     */       }
/*  201: 409 */       String sheetName = 
/*  202: 410 */         firstTabName + ':' + lastTabName;
/*  203:     */       
/*  204:     */ 
/*  205: 413 */       sheetName = sheetName.indexOf('\'') == -1 ? sheetName : 
/*  206: 414 */         StringHelper.replace(sheetName, "'", "''");
/*  207:     */       
/*  208:     */ 
/*  209:     */ 
/*  210: 418 */       return 
/*  211: 419 */         '\'' + sheetName + '\'';
/*  212:     */     }
/*  213: 421 */     if (sr.getType() == SupbookRecord.EXTERNAL)
/*  214:     */     {
/*  215: 424 */       StringBuffer sb = new StringBuffer();
/*  216: 425 */       java.io.File fl = new java.io.File(sr.getFileName());
/*  217: 426 */       sb.append("'");
/*  218: 427 */       sb.append(fl.getAbsolutePath());
/*  219: 428 */       sb.append("[");
/*  220: 429 */       sb.append(fl.getName());
/*  221: 430 */       sb.append("]");
/*  222: 431 */       sb.append(firstTab == 65535 ? "#REF" : sr.getSheetName(firstTab));
/*  223: 432 */       if (lastTab != firstTab) {
/*  224: 434 */         sb.append(sr.getSheetName(lastTab));
/*  225:     */       }
/*  226: 436 */       sb.append("'");
/*  227: 437 */       return sb.toString();
/*  228:     */     }
/*  229: 441 */     logger.warn("Unknown Supbook 3");
/*  230: 442 */     return "[UNKNOWN]";
/*  231:     */   }
/*  232:     */   
/*  233:     */   public String getLastExternalSheetName(int index)
/*  234:     */   {
/*  235: 455 */     if (this.workbookBof.isBiff7())
/*  236:     */     {
/*  237: 457 */       BoundsheetRecord br = (BoundsheetRecord)this.boundsheets.get(index);
/*  238:     */       
/*  239: 459 */       return br.getName();
/*  240:     */     }
/*  241: 462 */     int supbookIndex = this.externSheet.getSupbookIndex(index);
/*  242: 463 */     SupbookRecord sr = (SupbookRecord)this.supbooks.get(supbookIndex);
/*  243:     */     
/*  244: 465 */     int lastTab = this.externSheet.getLastTabIndex(index);
/*  245: 467 */     if (sr.getType() == SupbookRecord.INTERNAL)
/*  246:     */     {
/*  247: 470 */       if (lastTab == 65535) {
/*  248: 472 */         return "#REF";
/*  249:     */       }
/*  250: 476 */       BoundsheetRecord br = (BoundsheetRecord)this.boundsheets.get(lastTab);
/*  251: 477 */       return br.getName();
/*  252:     */     }
/*  253: 480 */     if (sr.getType() == SupbookRecord.EXTERNAL)
/*  254:     */     {
/*  255: 483 */       StringBuffer sb = new StringBuffer();
/*  256: 484 */       java.io.File fl = new java.io.File(sr.getFileName());
/*  257: 485 */       sb.append("'");
/*  258: 486 */       sb.append(fl.getAbsolutePath());
/*  259: 487 */       sb.append("[");
/*  260: 488 */       sb.append(fl.getName());
/*  261: 489 */       sb.append("]");
/*  262: 490 */       sb.append(lastTab == 65535 ? "#REF" : sr.getSheetName(lastTab));
/*  263: 491 */       sb.append("'");
/*  264: 492 */       return sb.toString();
/*  265:     */     }
/*  266: 496 */     logger.warn("Unknown Supbook 4");
/*  267: 497 */     return "[UNKNOWN]";
/*  268:     */   }
/*  269:     */   
/*  270:     */   public int getNumberOfSheets()
/*  271:     */   {
/*  272: 507 */     return this.sheets.size();
/*  273:     */   }
/*  274:     */   
/*  275:     */   public void close()
/*  276:     */   {
/*  277: 516 */     if (this.lastSheet != null) {
/*  278: 518 */       this.lastSheet.clear();
/*  279:     */     }
/*  280: 520 */     this.excelFile.clear();
/*  281: 522 */     if (!this.settings.getGCDisabled()) {
/*  282: 524 */       System.gc();
/*  283:     */     }
/*  284:     */   }
/*  285:     */   
/*  286:     */   final void addSheet(Sheet s)
/*  287:     */   {
/*  288: 535 */     this.sheets.add(s);
/*  289:     */   }
/*  290:     */   
/*  291:     */   protected void parse()
/*  292:     */     throws BiffException, PasswordException
/*  293:     */   {
/*  294: 546 */     Record r = null;
/*  295:     */     
/*  296: 548 */     BOFRecord bof = new BOFRecord(this.excelFile.next());
/*  297: 549 */     this.workbookBof = bof;
/*  298: 550 */     this.bofs += 1;
/*  299: 552 */     if ((!bof.isBiff8()) && (!bof.isBiff7())) {
/*  300: 554 */       throw new BiffException(BiffException.unrecognizedBiffVersion);
/*  301:     */     }
/*  302: 557 */     if (!bof.isWorkbookGlobals()) {
/*  303: 559 */       throw new BiffException(BiffException.expectedGlobals);
/*  304:     */     }
/*  305: 561 */     ArrayList continueRecords = new ArrayList();
/*  306: 562 */     ArrayList localNames = new ArrayList();
/*  307: 563 */     this.nameTable = new ArrayList();
/*  308: 564 */     this.addInFunctions = new ArrayList();
/*  309: 567 */     while (this.bofs == 1)
/*  310:     */     {
/*  311: 569 */       r = this.excelFile.next();
/*  312: 571 */       if (r.getType() == Type.SST)
/*  313:     */       {
/*  314: 573 */         continueRecords.clear();
/*  315: 574 */         Record nextrec = this.excelFile.peek();
/*  316: 575 */         while (nextrec.getType() == Type.CONTINUE)
/*  317:     */         {
/*  318: 577 */           continueRecords.add(this.excelFile.next());
/*  319: 578 */           nextrec = this.excelFile.peek();
/*  320:     */         }
/*  321: 582 */         Record[] records = new Record[continueRecords.size()];
/*  322: 583 */         records = (Record[])continueRecords.toArray(records);
/*  323:     */         
/*  324: 585 */         this.sharedStrings = new SSTRecord(r, records, this.settings);
/*  325:     */       }
/*  326:     */       else
/*  327:     */       {
/*  328: 587 */         if (r.getType() == Type.FILEPASS) {
/*  329: 589 */           throw new PasswordException();
/*  330:     */         }
/*  331: 591 */         if (r.getType() == Type.NAME)
/*  332:     */         {
/*  333: 593 */           NameRecord nr = null;
/*  334: 595 */           if (bof.isBiff8()) {
/*  335: 597 */             nr = new NameRecord(r, this.settings, this.nameTable.size());
/*  336:     */           } else {
/*  337: 602 */             nr = new NameRecord(r, this.settings, this.nameTable.size(), 
/*  338: 603 */               NameRecord.biff7);
/*  339:     */           }
/*  340: 608 */           this.nameTable.add(nr);
/*  341: 610 */           if (nr.isGlobal()) {
/*  342: 612 */             this.namedRecords.put(nr.getName(), nr);
/*  343:     */           } else {
/*  344: 616 */             localNames.add(nr);
/*  345:     */           }
/*  346:     */         }
/*  347: 619 */         else if (r.getType() == Type.FONT)
/*  348:     */         {
/*  349: 621 */           FontRecord fr = null;
/*  350: 623 */           if (bof.isBiff8()) {
/*  351: 625 */             fr = new FontRecord(r, this.settings);
/*  352:     */           } else {
/*  353: 629 */             fr = new FontRecord(r, this.settings, FontRecord.biff7);
/*  354:     */           }
/*  355: 631 */           this.fonts.addFont(fr);
/*  356:     */         }
/*  357: 633 */         else if (r.getType() == Type.PALETTE)
/*  358:     */         {
/*  359: 635 */           PaletteRecord palette = new PaletteRecord(r);
/*  360: 636 */           this.formattingRecords.setPalette(palette);
/*  361:     */         }
/*  362: 638 */         else if (r.getType() == Type.NINETEENFOUR)
/*  363:     */         {
/*  364: 640 */           NineteenFourRecord nr = new NineteenFourRecord(r);
/*  365: 641 */           this.nineteenFour = nr.is1904();
/*  366:     */         }
/*  367: 643 */         else if (r.getType() == Type.FORMAT)
/*  368:     */         {
/*  369: 645 */           FormatRecord fr = null;
/*  370: 646 */           if (bof.isBiff8()) {
/*  371: 648 */             fr = new FormatRecord(r, this.settings, FormatRecord.biff8);
/*  372:     */           } else {
/*  373: 652 */             fr = new FormatRecord(r, this.settings, FormatRecord.biff7);
/*  374:     */           }
/*  375:     */           try
/*  376:     */           {
/*  377: 656 */             this.formattingRecords.addFormat(fr);
/*  378:     */           }
/*  379:     */           catch (NumFormatRecordsException e)
/*  380:     */           {
/*  381: 661 */             Assert.verify(false, e.getMessage());
/*  382:     */           }
/*  383:     */         }
/*  384: 664 */         else if (r.getType() == Type.XF)
/*  385:     */         {
/*  386: 666 */           XFRecord xfr = null;
/*  387: 667 */           if (bof.isBiff8()) {
/*  388: 669 */             xfr = new XFRecord(r, this.settings, XFRecord.biff8);
/*  389:     */           } else {
/*  390: 673 */             xfr = new XFRecord(r, this.settings, XFRecord.biff7);
/*  391:     */           }
/*  392:     */           try
/*  393:     */           {
/*  394: 678 */             this.formattingRecords.addStyle(xfr);
/*  395:     */           }
/*  396:     */           catch (NumFormatRecordsException e)
/*  397:     */           {
/*  398: 683 */             Assert.verify(false, e.getMessage());
/*  399:     */           }
/*  400:     */         }
/*  401: 686 */         else if (r.getType() == Type.BOUNDSHEET)
/*  402:     */         {
/*  403: 688 */           BoundsheetRecord br = null;
/*  404: 690 */           if (bof.isBiff8()) {
/*  405: 692 */             br = new BoundsheetRecord(r, this.settings);
/*  406:     */           } else {
/*  407: 696 */             br = new BoundsheetRecord(r, BoundsheetRecord.biff7);
/*  408:     */           }
/*  409: 699 */           if (br.isSheet()) {
/*  410: 701 */             this.boundsheets.add(br);
/*  411: 703 */           } else if ((br.isChart()) && (!this.settings.getDrawingsDisabled())) {
/*  412: 705 */             this.boundsheets.add(br);
/*  413:     */           }
/*  414:     */         }
/*  415: 708 */         else if (r.getType() == Type.EXTERNSHEET)
/*  416:     */         {
/*  417: 710 */           if (bof.isBiff8()) {
/*  418: 712 */             this.externSheet = new ExternalSheetRecord(r, this.settings);
/*  419:     */           } else {
/*  420: 716 */             this.externSheet = new ExternalSheetRecord(r, this.settings, 
/*  421: 717 */               ExternalSheetRecord.biff7);
/*  422:     */           }
/*  423:     */         }
/*  424: 720 */         else if (r.getType() == Type.XCT)
/*  425:     */         {
/*  426: 722 */           XCTRecord xctr = new XCTRecord(r);
/*  427: 723 */           this.xctRecords.add(xctr);
/*  428:     */         }
/*  429: 725 */         else if (r.getType() == Type.CODEPAGE)
/*  430:     */         {
/*  431: 727 */           CodepageRecord cr = new CodepageRecord(r);
/*  432: 728 */           this.settings.setCharacterSet(cr.getCharacterSet());
/*  433:     */         }
/*  434: 730 */         else if (r.getType() == Type.SUPBOOK)
/*  435:     */         {
/*  436: 732 */           Record nextrec = this.excelFile.peek();
/*  437: 733 */           while (nextrec.getType() == Type.CONTINUE)
/*  438:     */           {
/*  439: 735 */             r.addContinueRecord(this.excelFile.next());
/*  440: 736 */             nextrec = this.excelFile.peek();
/*  441:     */           }
/*  442: 739 */           SupbookRecord sr = new SupbookRecord(r, this.settings);
/*  443: 740 */           this.supbooks.add(sr);
/*  444:     */         }
/*  445: 742 */         else if (r.getType() == Type.EXTERNNAME)
/*  446:     */         {
/*  447: 744 */           ExternalNameRecord enr = new ExternalNameRecord(r, this.settings);
/*  448: 746 */           if (enr.isAddInFunction()) {
/*  449: 748 */             this.addInFunctions.add(enr.getName());
/*  450:     */           }
/*  451:     */         }
/*  452: 751 */         else if (r.getType() == Type.PROTECT)
/*  453:     */         {
/*  454: 753 */           ProtectRecord pr = new ProtectRecord(r);
/*  455: 754 */           this.wbProtected = pr.isProtected();
/*  456:     */         }
/*  457: 756 */         else if (r.getType() == Type.OBJPROJ)
/*  458:     */         {
/*  459: 758 */           this.containsMacros = true;
/*  460:     */         }
/*  461: 760 */         else if (r.getType() == Type.COUNTRY)
/*  462:     */         {
/*  463: 762 */           this.countryRecord = new CountryRecord(r);
/*  464:     */         }
/*  465: 764 */         else if (r.getType() == Type.MSODRAWINGGROUP)
/*  466:     */         {
/*  467: 766 */           if (!this.settings.getDrawingsDisabled())
/*  468:     */           {
/*  469: 768 */             this.msoDrawingGroup = new MsoDrawingGroupRecord(r);
/*  470: 770 */             if (this.drawingGroup == null) {
/*  471: 772 */               this.drawingGroup = new DrawingGroup(Origin.READ);
/*  472:     */             }
/*  473: 775 */             this.drawingGroup.add(this.msoDrawingGroup);
/*  474:     */             
/*  475: 777 */             Record nextrec = this.excelFile.peek();
/*  476: 778 */             while (nextrec.getType() == Type.CONTINUE)
/*  477:     */             {
/*  478: 780 */               this.drawingGroup.add(this.excelFile.next());
/*  479: 781 */               nextrec = this.excelFile.peek();
/*  480:     */             }
/*  481:     */           }
/*  482:     */         }
/*  483: 785 */         else if (r.getType() == Type.BUTTONPROPERTYSET)
/*  484:     */         {
/*  485: 787 */           this.buttonPropertySet = new ButtonPropertySetRecord(r);
/*  486:     */         }
/*  487: 789 */         else if (r.getType() == Type.EOF)
/*  488:     */         {
/*  489: 791 */           this.bofs -= 1;
/*  490:     */         }
/*  491: 793 */         else if (r.getType() == Type.REFRESHALL)
/*  492:     */         {
/*  493: 795 */           RefreshAllRecord rfm = new RefreshAllRecord(r);
/*  494: 796 */           this.settings.setRefreshAll(rfm.getRefreshAll());
/*  495:     */         }
/*  496: 798 */         else if (r.getType() == Type.TEMPLATE)
/*  497:     */         {
/*  498: 800 */           TemplateRecord rfm = new TemplateRecord(r);
/*  499: 801 */           this.settings.setTemplate(rfm.getTemplate());
/*  500:     */         }
/*  501: 803 */         else if (r.getType() == Type.EXCEL9FILE)
/*  502:     */         {
/*  503: 805 */           Excel9FileRecord e9f = new Excel9FileRecord(r);
/*  504: 806 */           this.settings.setExcel9File(e9f.getExcel9File());
/*  505:     */         }
/*  506: 808 */         else if (r.getType() == Type.WINDOWPROTECT)
/*  507:     */         {
/*  508: 810 */           WindowProtectedRecord winp = new WindowProtectedRecord(r);
/*  509: 811 */           this.settings.setWindowProtected(winp.getWindowProtected());
/*  510:     */         }
/*  511: 813 */         else if (r.getType() == Type.HIDEOBJ)
/*  512:     */         {
/*  513: 815 */           HideobjRecord hobj = new HideobjRecord(r);
/*  514: 816 */           this.settings.setHideobj(hobj.getHideMode());
/*  515:     */         }
/*  516: 818 */         else if (r.getType() == Type.WRITEACCESS)
/*  517:     */         {
/*  518: 820 */           WriteAccessRecord war = new WriteAccessRecord(r, bof.isBiff8(), 
/*  519: 821 */             this.settings);
/*  520: 822 */           this.settings.setWriteAccess(war.getWriteAccess());
/*  521:     */         }
/*  522:     */       }
/*  523:     */     }
/*  524: 831 */     bof = null;
/*  525: 832 */     if (this.excelFile.hasNext())
/*  526:     */     {
/*  527: 834 */       r = this.excelFile.next();
/*  528: 836 */       if (r.getType() == Type.BOF) {
/*  529: 838 */         bof = new BOFRecord(r);
/*  530:     */       }
/*  531:     */     }
/*  532: 843 */     while ((bof != null) && (getNumberOfSheets() < this.boundsheets.size()))
/*  533:     */     {
/*  534: 845 */       if ((!bof.isBiff8()) && (!bof.isBiff7())) {
/*  535: 847 */         throw new BiffException(BiffException.unrecognizedBiffVersion);
/*  536:     */       }
/*  537: 850 */       if (bof.isWorksheet())
/*  538:     */       {
/*  539: 853 */         SheetImpl s = new SheetImpl(this.excelFile, 
/*  540: 854 */           this.sharedStrings, 
/*  541: 855 */           this.formattingRecords, 
/*  542: 856 */           bof, 
/*  543: 857 */           this.workbookBof, 
/*  544: 858 */           this.nineteenFour, 
/*  545: 859 */           this);
/*  546:     */         
/*  547: 861 */         BoundsheetRecord br = (BoundsheetRecord)this.boundsheets.get(
/*  548: 862 */           getNumberOfSheets());
/*  549: 863 */         s.setName(br.getName());
/*  550: 864 */         s.setHidden(br.isHidden());
/*  551: 865 */         addSheet(s);
/*  552:     */       }
/*  553: 867 */       else if (bof.isChart())
/*  554:     */       {
/*  555: 870 */         SheetImpl s = new SheetImpl(this.excelFile, 
/*  556: 871 */           this.sharedStrings, 
/*  557: 872 */           this.formattingRecords, 
/*  558: 873 */           bof, 
/*  559: 874 */           this.workbookBof, 
/*  560: 875 */           this.nineteenFour, 
/*  561: 876 */           this);
/*  562:     */         
/*  563: 878 */         BoundsheetRecord br = (BoundsheetRecord)this.boundsheets.get(
/*  564: 879 */           getNumberOfSheets());
/*  565: 880 */         s.setName(br.getName());
/*  566: 881 */         s.setHidden(br.isHidden());
/*  567: 882 */         addSheet(s);
/*  568:     */       }
/*  569:     */       else
/*  570:     */       {
/*  571: 886 */         logger.warn("BOF is unrecognized");
/*  572: 889 */         while ((this.excelFile.hasNext()) && (r.getType() != Type.EOF)) {
/*  573: 891 */           r = this.excelFile.next();
/*  574:     */         }
/*  575:     */       }
/*  576: 900 */       bof = null;
/*  577: 901 */       if (this.excelFile.hasNext())
/*  578:     */       {
/*  579: 903 */         r = this.excelFile.next();
/*  580: 905 */         if (r.getType() == Type.BOF) {
/*  581: 907 */           bof = new BOFRecord(r);
/*  582:     */         }
/*  583:     */       }
/*  584:     */     }
/*  585: 913 */     for (Iterator it = localNames.iterator(); it.hasNext();)
/*  586:     */     {
/*  587: 915 */       NameRecord nr = (NameRecord)it.next();
/*  588: 917 */       if (nr.getBuiltInName() == null)
/*  589:     */       {
/*  590: 919 */         logger.warn("Usage of a local non-builtin name");
/*  591:     */       }
/*  592: 921 */       else if ((nr.getBuiltInName() == BuiltInName.PRINT_AREA) || 
/*  593: 922 */         (nr.getBuiltInName() == BuiltInName.PRINT_TITLES))
/*  594:     */       {
/*  595: 926 */         SheetImpl s = (SheetImpl)this.sheets.get(nr.getSheetRef() - 1);
/*  596: 927 */         s.addLocalName(nr);
/*  597:     */       }
/*  598:     */     }
/*  599:     */   }
/*  600:     */   
/*  601:     */   public FormattingRecords getFormattingRecords()
/*  602:     */   {
/*  603: 940 */     return this.formattingRecords;
/*  604:     */   }
/*  605:     */   
/*  606:     */   public ExternalSheetRecord getExternalSheetRecord()
/*  607:     */   {
/*  608: 951 */     return this.externSheet;
/*  609:     */   }
/*  610:     */   
/*  611:     */   public MsoDrawingGroupRecord getMsoDrawingGroupRecord()
/*  612:     */   {
/*  613: 962 */     return this.msoDrawingGroup;
/*  614:     */   }
/*  615:     */   
/*  616:     */   public SupbookRecord[] getSupbookRecords()
/*  617:     */   {
/*  618: 973 */     SupbookRecord[] sr = new SupbookRecord[this.supbooks.size()];
/*  619: 974 */     return (SupbookRecord[])this.supbooks.toArray(sr);
/*  620:     */   }
/*  621:     */   
/*  622:     */   public NameRecord[] getNameRecords()
/*  623:     */   {
/*  624: 985 */     NameRecord[] na = new NameRecord[this.nameTable.size()];
/*  625: 986 */     return (NameRecord[])this.nameTable.toArray(na);
/*  626:     */   }
/*  627:     */   
/*  628:     */   public Fonts getFonts()
/*  629:     */   {
/*  630: 996 */     return this.fonts;
/*  631:     */   }
/*  632:     */   
/*  633:     */   public Cell getCell(String loc)
/*  634:     */   {
/*  635:1010 */     Sheet s = getSheet(CellReferenceHelper.getSheet(loc));
/*  636:1011 */     return s.getCell(loc);
/*  637:     */   }
/*  638:     */   
/*  639:     */   public Cell findCellByName(String name)
/*  640:     */   {
/*  641:1025 */     NameRecord nr = (NameRecord)this.namedRecords.get(name);
/*  642:1027 */     if (nr == null) {
/*  643:1029 */       return null;
/*  644:     */     }
/*  645:1032 */     NameRecord.NameRange[] ranges = nr.getRanges();
/*  646:     */     
/*  647:     */ 
/*  648:1035 */     Sheet s = getSheet(getExternalSheetIndex(ranges[0].getExternalSheet()));
/*  649:1036 */     int col = ranges[0].getFirstColumn();
/*  650:1037 */     int row = ranges[0].getFirstRow();
/*  651:1041 */     if ((col > s.getColumns()) || (row > s.getRows())) {
/*  652:1043 */       return new EmptyCell(col, row);
/*  653:     */     }
/*  654:1046 */     Cell cell = s.getCell(col, row);
/*  655:     */     
/*  656:1048 */     return cell;
/*  657:     */   }
/*  658:     */   
/*  659:     */   public Range[] findByName(String name)
/*  660:     */   {
/*  661:1067 */     NameRecord nr = (NameRecord)this.namedRecords.get(name);
/*  662:1069 */     if (nr == null) {
/*  663:1071 */       return null;
/*  664:     */     }
/*  665:1074 */     NameRecord.NameRange[] ranges = nr.getRanges();
/*  666:     */     
/*  667:1076 */     Range[] cellRanges = new Range[ranges.length];
/*  668:1078 */     for (int i = 0; i < ranges.length; i++) {
/*  669:1080 */       cellRanges[i] = new RangeImpl(
/*  670:1081 */         this, 
/*  671:1082 */         getExternalSheetIndex(ranges[i].getExternalSheet()), 
/*  672:1083 */         ranges[i].getFirstColumn(), 
/*  673:1084 */         ranges[i].getFirstRow(), 
/*  674:1085 */         getLastExternalSheetIndex(ranges[i].getExternalSheet()), 
/*  675:1086 */         ranges[i].getLastColumn(), 
/*  676:1087 */         ranges[i].getLastRow());
/*  677:     */     }
/*  678:1090 */     return cellRanges;
/*  679:     */   }
/*  680:     */   
/*  681:     */   public String[] getRangeNames()
/*  682:     */   {
/*  683:1100 */     Object[] keys = this.namedRecords.keySet().toArray();
/*  684:1101 */     String[] names = new String[keys.length];
/*  685:1102 */     System.arraycopy(keys, 0, names, 0, keys.length);
/*  686:     */     
/*  687:1104 */     return names;
/*  688:     */   }
/*  689:     */   
/*  690:     */   public BOFRecord getWorkbookBof()
/*  691:     */   {
/*  692:1115 */     return this.workbookBof;
/*  693:     */   }
/*  694:     */   
/*  695:     */   public boolean isProtected()
/*  696:     */   {
/*  697:1125 */     return this.wbProtected;
/*  698:     */   }
/*  699:     */   
/*  700:     */   public WorkbookSettings getSettings()
/*  701:     */   {
/*  702:1135 */     return this.settings;
/*  703:     */   }
/*  704:     */   
/*  705:     */   public int getExternalSheetIndex(String sheetName)
/*  706:     */   {
/*  707:1146 */     return 0;
/*  708:     */   }
/*  709:     */   
/*  710:     */   public int getLastExternalSheetIndex(String sheetName)
/*  711:     */   {
/*  712:1157 */     return 0;
/*  713:     */   }
/*  714:     */   
/*  715:     */   public String getName(int index)
/*  716:     */     throws NameRangeException
/*  717:     */   {
/*  718:1170 */     if ((index < 0) || (index >= this.nameTable.size())) {
/*  719:1172 */       throw new NameRangeException();
/*  720:     */     }
/*  721:1174 */     return ((NameRecord)this.nameTable.get(index)).getName();
/*  722:     */   }
/*  723:     */   
/*  724:     */   public int getNameIndex(String name)
/*  725:     */   {
/*  726:1185 */     NameRecord nr = (NameRecord)this.namedRecords.get(name);
/*  727:     */     
/*  728:1187 */     return nr != null ? nr.getIndex() : 0;
/*  729:     */   }
/*  730:     */   
/*  731:     */   public DrawingGroup getDrawingGroup()
/*  732:     */   {
/*  733:1197 */     return this.drawingGroup;
/*  734:     */   }
/*  735:     */   
/*  736:     */   public CompoundFile getCompoundFile()
/*  737:     */   {
/*  738:1211 */     return this.excelFile.getCompoundFile();
/*  739:     */   }
/*  740:     */   
/*  741:     */   public boolean containsMacros()
/*  742:     */   {
/*  743:1221 */     return this.containsMacros;
/*  744:     */   }
/*  745:     */   
/*  746:     */   public ButtonPropertySetRecord getButtonPropertySet()
/*  747:     */   {
/*  748:1231 */     return this.buttonPropertySet;
/*  749:     */   }
/*  750:     */   
/*  751:     */   public CountryRecord getCountryRecord()
/*  752:     */   {
/*  753:1241 */     return this.countryRecord;
/*  754:     */   }
/*  755:     */   
/*  756:     */   public String[] getAddInFunctionNames()
/*  757:     */   {
/*  758:1251 */     String[] addins = new String[0];
/*  759:1252 */     return (String[])this.addInFunctions.toArray(addins);
/*  760:     */   }
/*  761:     */   
/*  762:     */   public int getIndex(Sheet sheet)
/*  763:     */   {
/*  764:1263 */     String name = sheet.getName();
/*  765:1264 */     int index = -1;
/*  766:1265 */     int pos = 0;
/*  767:1267 */     for (Iterator i = this.boundsheets.iterator(); (i.hasNext()) && (index == -1);)
/*  768:     */     {
/*  769:1269 */       BoundsheetRecord br = (BoundsheetRecord)i.next();
/*  770:1271 */       if (br.getName().equals(name)) {
/*  771:1273 */         index = pos;
/*  772:     */       } else {
/*  773:1277 */         pos++;
/*  774:     */       }
/*  775:     */     }
/*  776:1281 */     return index;
/*  777:     */   }
/*  778:     */   
/*  779:     */   public XCTRecord[] getXCTRecords()
/*  780:     */   {
/*  781:1286 */     XCTRecord[] xctr = new XCTRecord[0];
/*  782:1287 */     return (XCTRecord[])this.xctRecords.toArray(xctr);
/*  783:     */   }
/*  784:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.WorkbookParser
 * JD-Core Version:    0.7.0.1
 */