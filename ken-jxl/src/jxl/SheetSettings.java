/*    1:     */ package jxl;
/*    2:     */ 
/*    3:     */ import jxl.biff.SheetRangeImpl;
/*    4:     */ import jxl.common.Assert;
/*    5:     */ import jxl.format.PageOrder;
/*    6:     */ import jxl.format.PageOrientation;
/*    7:     */ import jxl.format.PaperSize;
/*    8:     */ 
/*    9:     */ public final class SheetSettings
/*   10:     */ {
/*   11:     */   private PageOrientation orientation;
/*   12:     */   private PageOrder pageOrder;
/*   13:     */   private PaperSize paperSize;
/*   14:     */   private boolean sheetProtected;
/*   15:     */   private boolean hidden;
/*   16:     */   private boolean selected;
/*   17:     */   private HeaderFooter header;
/*   18:     */   private double headerMargin;
/*   19:     */   private HeaderFooter footer;
/*   20:     */   private double footerMargin;
/*   21:     */   private int scaleFactor;
/*   22:     */   private int zoomFactor;
/*   23:     */   private int pageStart;
/*   24:     */   private int fitWidth;
/*   25:     */   private int fitHeight;
/*   26:     */   private int horizontalPrintResolution;
/*   27:     */   private int verticalPrintResolution;
/*   28:     */   private double leftMargin;
/*   29:     */   private double rightMargin;
/*   30:     */   private double topMargin;
/*   31:     */   private double bottomMargin;
/*   32:     */   private boolean fitToPages;
/*   33:     */   private boolean showGridLines;
/*   34:     */   private boolean printGridLines;
/*   35:     */   private boolean printHeaders;
/*   36:     */   private boolean pageBreakPreviewMode;
/*   37:     */   private boolean displayZeroValues;
/*   38:     */   private String password;
/*   39:     */   private int passwordHash;
/*   40:     */   private int defaultColumnWidth;
/*   41:     */   private int defaultRowHeight;
/*   42:     */   private int horizontalFreeze;
/*   43:     */   private int verticalFreeze;
/*   44:     */   private boolean verticalCentre;
/*   45:     */   private boolean horizontalCentre;
/*   46:     */   private int copies;
/*   47:     */   private boolean automaticFormulaCalculation;
/*   48:     */   private boolean recalculateFormulasBeforeSave;
/*   49:     */   private int pageBreakPreviewMagnification;
/*   50:     */   private int normalMagnification;
/*   51:     */   private Range printArea;
/*   52:     */   private Range printTitlesRow;
/*   53:     */   private Range printTitlesCol;
/*   54:     */   private Sheet sheet;
/*   55: 265 */   private static final PageOrientation DEFAULT_ORIENTATION = PageOrientation.PORTRAIT;
/*   56: 267 */   private static final PageOrder DEFAULT_ORDER = PageOrder.RIGHT_THEN_DOWN;
/*   57: 268 */   private static final PaperSize DEFAULT_PAPER_SIZE = PaperSize.A4;
/*   58:     */   private static final double DEFAULT_HEADER_MARGIN = 0.5D;
/*   59:     */   private static final double DEFAULT_FOOTER_MARGIN = 0.5D;
/*   60:     */   private static final int DEFAULT_PRINT_RESOLUTION = 300;
/*   61:     */   private static final double DEFAULT_WIDTH_MARGIN = 0.75D;
/*   62:     */   private static final double DEFAULT_HEIGHT_MARGIN = 1.0D;
/*   63:     */   private static final int DEFAULT_DEFAULT_COLUMN_WIDTH = 8;
/*   64:     */   private static final int DEFAULT_ZOOM_FACTOR = 100;
/*   65:     */   private static final int DEFAULT_NORMAL_MAGNIFICATION = 100;
/*   66:     */   private static final int DEFAULT_PAGE_BREAK_PREVIEW_MAGNIFICATION = 60;
/*   67:     */   public static final int DEFAULT_DEFAULT_ROW_HEIGHT = 255;
/*   68:     */   
/*   69:     */   public SheetSettings(Sheet s)
/*   70:     */   {
/*   71: 291 */     this.sheet = s;
/*   72: 292 */     this.orientation = DEFAULT_ORIENTATION;
/*   73: 293 */     this.pageOrder = DEFAULT_ORDER;
/*   74: 294 */     this.paperSize = DEFAULT_PAPER_SIZE;
/*   75: 295 */     this.sheetProtected = false;
/*   76: 296 */     this.hidden = false;
/*   77: 297 */     this.selected = false;
/*   78: 298 */     this.headerMargin = 0.5D;
/*   79: 299 */     this.footerMargin = 0.5D;
/*   80: 300 */     this.horizontalPrintResolution = 300;
/*   81: 301 */     this.verticalPrintResolution = 300;
/*   82: 302 */     this.leftMargin = 0.75D;
/*   83: 303 */     this.rightMargin = 0.75D;
/*   84: 304 */     this.topMargin = 1.0D;
/*   85: 305 */     this.bottomMargin = 1.0D;
/*   86: 306 */     this.fitToPages = false;
/*   87: 307 */     this.showGridLines = true;
/*   88: 308 */     this.printGridLines = false;
/*   89: 309 */     this.printHeaders = false;
/*   90: 310 */     this.pageBreakPreviewMode = false;
/*   91: 311 */     this.displayZeroValues = true;
/*   92: 312 */     this.defaultColumnWidth = 8;
/*   93: 313 */     this.defaultRowHeight = 255;
/*   94: 314 */     this.zoomFactor = 100;
/*   95: 315 */     this.pageBreakPreviewMagnification = 60;
/*   96: 316 */     this.normalMagnification = 100;
/*   97: 317 */     this.horizontalFreeze = 0;
/*   98: 318 */     this.verticalFreeze = 0;
/*   99: 319 */     this.copies = 1;
/*  100: 320 */     this.header = new HeaderFooter();
/*  101: 321 */     this.footer = new HeaderFooter();
/*  102: 322 */     this.automaticFormulaCalculation = true;
/*  103: 323 */     this.recalculateFormulasBeforeSave = true;
/*  104:     */   }
/*  105:     */   
/*  106:     */   public SheetSettings(SheetSettings copy, Sheet s)
/*  107:     */   {
/*  108: 332 */     Assert.verify(copy != null);
/*  109:     */     
/*  110: 334 */     this.sheet = s;
/*  111: 335 */     this.orientation = copy.orientation;
/*  112: 336 */     this.pageOrder = copy.pageOrder;
/*  113: 337 */     this.paperSize = copy.paperSize;
/*  114: 338 */     this.sheetProtected = copy.sheetProtected;
/*  115: 339 */     this.hidden = copy.hidden;
/*  116: 340 */     this.selected = false;
/*  117: 341 */     this.headerMargin = copy.headerMargin;
/*  118: 342 */     this.footerMargin = copy.footerMargin;
/*  119: 343 */     this.scaleFactor = copy.scaleFactor;
/*  120: 344 */     this.pageStart = copy.pageStart;
/*  121: 345 */     this.fitWidth = copy.fitWidth;
/*  122: 346 */     this.fitHeight = copy.fitHeight;
/*  123: 347 */     this.horizontalPrintResolution = copy.horizontalPrintResolution;
/*  124: 348 */     this.verticalPrintResolution = copy.verticalPrintResolution;
/*  125: 349 */     this.leftMargin = copy.leftMargin;
/*  126: 350 */     this.rightMargin = copy.rightMargin;
/*  127: 351 */     this.topMargin = copy.topMargin;
/*  128: 352 */     this.bottomMargin = copy.bottomMargin;
/*  129: 353 */     this.fitToPages = copy.fitToPages;
/*  130: 354 */     this.password = copy.password;
/*  131: 355 */     this.passwordHash = copy.passwordHash;
/*  132: 356 */     this.defaultColumnWidth = copy.defaultColumnWidth;
/*  133: 357 */     this.defaultRowHeight = copy.defaultRowHeight;
/*  134: 358 */     this.zoomFactor = copy.zoomFactor;
/*  135: 359 */     this.pageBreakPreviewMagnification = copy.pageBreakPreviewMagnification;
/*  136: 360 */     this.normalMagnification = copy.normalMagnification;
/*  137: 361 */     this.showGridLines = copy.showGridLines;
/*  138: 362 */     this.displayZeroValues = copy.displayZeroValues;
/*  139: 363 */     this.pageBreakPreviewMode = copy.pageBreakPreviewMode;
/*  140: 364 */     this.horizontalFreeze = copy.horizontalFreeze;
/*  141: 365 */     this.verticalFreeze = copy.verticalFreeze;
/*  142: 366 */     this.horizontalCentre = copy.horizontalCentre;
/*  143: 367 */     this.verticalCentre = copy.verticalCentre;
/*  144: 368 */     this.copies = copy.copies;
/*  145: 369 */     this.header = new HeaderFooter(copy.header);
/*  146: 370 */     this.footer = new HeaderFooter(copy.footer);
/*  147: 371 */     this.automaticFormulaCalculation = copy.automaticFormulaCalculation;
/*  148: 372 */     this.recalculateFormulasBeforeSave = copy.recalculateFormulasBeforeSave;
/*  149: 374 */     if (copy.printArea != null) {
/*  150: 376 */       this.printArea = new SheetRangeImpl(
/*  151: 377 */         this.sheet, 
/*  152: 378 */         copy.getPrintArea().getTopLeft().getColumn(), 
/*  153: 379 */         copy.getPrintArea().getTopLeft().getRow(), 
/*  154: 380 */         copy.getPrintArea().getBottomRight().getColumn(), 
/*  155: 381 */         copy.getPrintArea().getBottomRight().getRow());
/*  156:     */     }
/*  157: 384 */     if (copy.printTitlesRow != null) {
/*  158: 386 */       this.printTitlesRow = new SheetRangeImpl(
/*  159: 387 */         this.sheet, 
/*  160: 388 */         copy.getPrintTitlesRow().getTopLeft().getColumn(), 
/*  161: 389 */         copy.getPrintTitlesRow().getTopLeft().getRow(), 
/*  162: 390 */         copy.getPrintTitlesRow().getBottomRight().getColumn(), 
/*  163: 391 */         copy.getPrintTitlesRow().getBottomRight().getRow());
/*  164:     */     }
/*  165: 394 */     if (copy.printTitlesCol != null) {
/*  166: 396 */       this.printTitlesCol = new SheetRangeImpl(
/*  167: 397 */         this.sheet, 
/*  168: 398 */         copy.getPrintTitlesCol().getTopLeft().getColumn(), 
/*  169: 399 */         copy.getPrintTitlesCol().getTopLeft().getRow(), 
/*  170: 400 */         copy.getPrintTitlesCol().getBottomRight().getColumn(), 
/*  171: 401 */         copy.getPrintTitlesCol().getBottomRight().getRow());
/*  172:     */     }
/*  173:     */   }
/*  174:     */   
/*  175:     */   public void setOrientation(PageOrientation po)
/*  176:     */   {
/*  177: 412 */     this.orientation = po;
/*  178:     */   }
/*  179:     */   
/*  180:     */   public PageOrientation getOrientation()
/*  181:     */   {
/*  182: 422 */     return this.orientation;
/*  183:     */   }
/*  184:     */   
/*  185:     */   public PageOrder getPageOrder()
/*  186:     */   {
/*  187: 432 */     return this.pageOrder;
/*  188:     */   }
/*  189:     */   
/*  190:     */   public void setPageOrder(PageOrder order)
/*  191:     */   {
/*  192: 442 */     this.pageOrder = order;
/*  193:     */   }
/*  194:     */   
/*  195:     */   public void setPaperSize(PaperSize ps)
/*  196:     */   {
/*  197: 452 */     this.paperSize = ps;
/*  198:     */   }
/*  199:     */   
/*  200:     */   public PaperSize getPaperSize()
/*  201:     */   {
/*  202: 462 */     return this.paperSize;
/*  203:     */   }
/*  204:     */   
/*  205:     */   public boolean isProtected()
/*  206:     */   {
/*  207: 472 */     return this.sheetProtected;
/*  208:     */   }
/*  209:     */   
/*  210:     */   public void setProtected(boolean p)
/*  211:     */   {
/*  212: 482 */     this.sheetProtected = p;
/*  213:     */   }
/*  214:     */   
/*  215:     */   public void setHeaderMargin(double d)
/*  216:     */   {
/*  217: 492 */     this.headerMargin = d;
/*  218:     */   }
/*  219:     */   
/*  220:     */   public double getHeaderMargin()
/*  221:     */   {
/*  222: 502 */     return this.headerMargin;
/*  223:     */   }
/*  224:     */   
/*  225:     */   public void setFooterMargin(double d)
/*  226:     */   {
/*  227: 512 */     this.footerMargin = d;
/*  228:     */   }
/*  229:     */   
/*  230:     */   public double getFooterMargin()
/*  231:     */   {
/*  232: 522 */     return this.footerMargin;
/*  233:     */   }
/*  234:     */   
/*  235:     */   public void setHidden(boolean h)
/*  236:     */   {
/*  237: 532 */     this.hidden = h;
/*  238:     */   }
/*  239:     */   
/*  240:     */   public boolean isHidden()
/*  241:     */   {
/*  242: 542 */     return this.hidden;
/*  243:     */   }
/*  244:     */   
/*  245:     */   /**
/*  246:     */    * @deprecated
/*  247:     */    */
/*  248:     */   public void setSelected()
/*  249:     */   {
/*  250: 552 */     setSelected(true);
/*  251:     */   }
/*  252:     */   
/*  253:     */   public void setSelected(boolean s)
/*  254:     */   {
/*  255: 562 */     this.selected = s;
/*  256:     */   }
/*  257:     */   
/*  258:     */   public boolean isSelected()
/*  259:     */   {
/*  260: 572 */     return this.selected;
/*  261:     */   }
/*  262:     */   
/*  263:     */   public void setScaleFactor(int sf)
/*  264:     */   {
/*  265: 584 */     this.scaleFactor = sf;
/*  266: 585 */     this.fitToPages = false;
/*  267:     */   }
/*  268:     */   
/*  269:     */   public int getScaleFactor()
/*  270:     */   {
/*  271: 595 */     return this.scaleFactor;
/*  272:     */   }
/*  273:     */   
/*  274:     */   public void setPageStart(int ps)
/*  275:     */   {
/*  276: 605 */     this.pageStart = ps;
/*  277:     */   }
/*  278:     */   
/*  279:     */   public int getPageStart()
/*  280:     */   {
/*  281: 615 */     return this.pageStart;
/*  282:     */   }
/*  283:     */   
/*  284:     */   public void setFitWidth(int fw)
/*  285:     */   {
/*  286: 626 */     this.fitWidth = fw;
/*  287: 627 */     this.fitToPages = true;
/*  288:     */   }
/*  289:     */   
/*  290:     */   public int getFitWidth()
/*  291:     */   {
/*  292: 637 */     return this.fitWidth;
/*  293:     */   }
/*  294:     */   
/*  295:     */   public void setFitHeight(int fh)
/*  296:     */   {
/*  297: 647 */     this.fitHeight = fh;
/*  298: 648 */     this.fitToPages = true;
/*  299:     */   }
/*  300:     */   
/*  301:     */   public int getFitHeight()
/*  302:     */   {
/*  303: 658 */     return this.fitHeight;
/*  304:     */   }
/*  305:     */   
/*  306:     */   public void setHorizontalPrintResolution(int hpw)
/*  307:     */   {
/*  308: 668 */     this.horizontalPrintResolution = hpw;
/*  309:     */   }
/*  310:     */   
/*  311:     */   public int getHorizontalPrintResolution()
/*  312:     */   {
/*  313: 678 */     return this.horizontalPrintResolution;
/*  314:     */   }
/*  315:     */   
/*  316:     */   public void setVerticalPrintResolution(int vpw)
/*  317:     */   {
/*  318: 688 */     this.verticalPrintResolution = vpw;
/*  319:     */   }
/*  320:     */   
/*  321:     */   public int getVerticalPrintResolution()
/*  322:     */   {
/*  323: 698 */     return this.verticalPrintResolution;
/*  324:     */   }
/*  325:     */   
/*  326:     */   public void setRightMargin(double m)
/*  327:     */   {
/*  328: 708 */     this.rightMargin = m;
/*  329:     */   }
/*  330:     */   
/*  331:     */   public double getRightMargin()
/*  332:     */   {
/*  333: 718 */     return this.rightMargin;
/*  334:     */   }
/*  335:     */   
/*  336:     */   public void setLeftMargin(double m)
/*  337:     */   {
/*  338: 728 */     this.leftMargin = m;
/*  339:     */   }
/*  340:     */   
/*  341:     */   public double getLeftMargin()
/*  342:     */   {
/*  343: 738 */     return this.leftMargin;
/*  344:     */   }
/*  345:     */   
/*  346:     */   public void setTopMargin(double m)
/*  347:     */   {
/*  348: 748 */     this.topMargin = m;
/*  349:     */   }
/*  350:     */   
/*  351:     */   public double getTopMargin()
/*  352:     */   {
/*  353: 758 */     return this.topMargin;
/*  354:     */   }
/*  355:     */   
/*  356:     */   public void setBottomMargin(double m)
/*  357:     */   {
/*  358: 768 */     this.bottomMargin = m;
/*  359:     */   }
/*  360:     */   
/*  361:     */   public double getBottomMargin()
/*  362:     */   {
/*  363: 778 */     return this.bottomMargin;
/*  364:     */   }
/*  365:     */   
/*  366:     */   public double getDefaultWidthMargin()
/*  367:     */   {
/*  368: 788 */     return 0.75D;
/*  369:     */   }
/*  370:     */   
/*  371:     */   public double getDefaultHeightMargin()
/*  372:     */   {
/*  373: 798 */     return 1.0D;
/*  374:     */   }
/*  375:     */   
/*  376:     */   public boolean getFitToPages()
/*  377:     */   {
/*  378: 807 */     return this.fitToPages;
/*  379:     */   }
/*  380:     */   
/*  381:     */   public void setFitToPages(boolean b)
/*  382:     */   {
/*  383: 816 */     this.fitToPages = b;
/*  384:     */   }
/*  385:     */   
/*  386:     */   public String getPassword()
/*  387:     */   {
/*  388: 826 */     return this.password;
/*  389:     */   }
/*  390:     */   
/*  391:     */   public void setPassword(String s)
/*  392:     */   {
/*  393: 836 */     this.password = s;
/*  394:     */   }
/*  395:     */   
/*  396:     */   public int getPasswordHash()
/*  397:     */   {
/*  398: 846 */     return this.passwordHash;
/*  399:     */   }
/*  400:     */   
/*  401:     */   public void setPasswordHash(int ph)
/*  402:     */   {
/*  403: 856 */     this.passwordHash = ph;
/*  404:     */   }
/*  405:     */   
/*  406:     */   public int getDefaultColumnWidth()
/*  407:     */   {
/*  408: 866 */     return this.defaultColumnWidth;
/*  409:     */   }
/*  410:     */   
/*  411:     */   public void setDefaultColumnWidth(int w)
/*  412:     */   {
/*  413: 876 */     this.defaultColumnWidth = w;
/*  414:     */   }
/*  415:     */   
/*  416:     */   public int getDefaultRowHeight()
/*  417:     */   {
/*  418: 886 */     return this.defaultRowHeight;
/*  419:     */   }
/*  420:     */   
/*  421:     */   public void setDefaultRowHeight(int h)
/*  422:     */   {
/*  423: 896 */     this.defaultRowHeight = h;
/*  424:     */   }
/*  425:     */   
/*  426:     */   public int getZoomFactor()
/*  427:     */   {
/*  428: 908 */     return this.zoomFactor;
/*  429:     */   }
/*  430:     */   
/*  431:     */   public void setZoomFactor(int zf)
/*  432:     */   {
/*  433: 920 */     this.zoomFactor = zf;
/*  434:     */   }
/*  435:     */   
/*  436:     */   public int getPageBreakPreviewMagnification()
/*  437:     */   {
/*  438: 931 */     return this.pageBreakPreviewMagnification;
/*  439:     */   }
/*  440:     */   
/*  441:     */   public void setPageBreakPreviewMagnification(int f)
/*  442:     */   {
/*  443: 942 */     this.pageBreakPreviewMagnification = f;
/*  444:     */   }
/*  445:     */   
/*  446:     */   public int getNormalMagnification()
/*  447:     */   {
/*  448: 953 */     return this.normalMagnification;
/*  449:     */   }
/*  450:     */   
/*  451:     */   public void setNormalMagnification(int f)
/*  452:     */   {
/*  453: 964 */     this.normalMagnification = f;
/*  454:     */   }
/*  455:     */   
/*  456:     */   public boolean getDisplayZeroValues()
/*  457:     */   {
/*  458: 975 */     return this.displayZeroValues;
/*  459:     */   }
/*  460:     */   
/*  461:     */   public void setDisplayZeroValues(boolean b)
/*  462:     */   {
/*  463: 985 */     this.displayZeroValues = b;
/*  464:     */   }
/*  465:     */   
/*  466:     */   public boolean getShowGridLines()
/*  467:     */   {
/*  468: 995 */     return this.showGridLines;
/*  469:     */   }
/*  470:     */   
/*  471:     */   public void setShowGridLines(boolean b)
/*  472:     */   {
/*  473:1005 */     this.showGridLines = b;
/*  474:     */   }
/*  475:     */   
/*  476:     */   public boolean getPageBreakPreviewMode()
/*  477:     */   {
/*  478:1015 */     return this.pageBreakPreviewMode;
/*  479:     */   }
/*  480:     */   
/*  481:     */   public void setPageBreakPreviewMode(boolean b)
/*  482:     */   {
/*  483:1025 */     this.pageBreakPreviewMode = b;
/*  484:     */   }
/*  485:     */   
/*  486:     */   public boolean getPrintGridLines()
/*  487:     */   {
/*  488:1035 */     return this.printGridLines;
/*  489:     */   }
/*  490:     */   
/*  491:     */   public void setPrintGridLines(boolean b)
/*  492:     */   {
/*  493:1045 */     this.printGridLines = b;
/*  494:     */   }
/*  495:     */   
/*  496:     */   public boolean getPrintHeaders()
/*  497:     */   {
/*  498:1055 */     return this.printHeaders;
/*  499:     */   }
/*  500:     */   
/*  501:     */   public void setPrintHeaders(boolean b)
/*  502:     */   {
/*  503:1065 */     this.printHeaders = b;
/*  504:     */   }
/*  505:     */   
/*  506:     */   public int getHorizontalFreeze()
/*  507:     */   {
/*  508:1076 */     return this.horizontalFreeze;
/*  509:     */   }
/*  510:     */   
/*  511:     */   public void setHorizontalFreeze(int row)
/*  512:     */   {
/*  513:1086 */     this.horizontalFreeze = Math.max(row, 0);
/*  514:     */   }
/*  515:     */   
/*  516:     */   public int getVerticalFreeze()
/*  517:     */   {
/*  518:1097 */     return this.verticalFreeze;
/*  519:     */   }
/*  520:     */   
/*  521:     */   public void setVerticalFreeze(int col)
/*  522:     */   {
/*  523:1107 */     this.verticalFreeze = Math.max(col, 0);
/*  524:     */   }
/*  525:     */   
/*  526:     */   public void setCopies(int c)
/*  527:     */   {
/*  528:1117 */     this.copies = c;
/*  529:     */   }
/*  530:     */   
/*  531:     */   public int getCopies()
/*  532:     */   {
/*  533:1127 */     return this.copies;
/*  534:     */   }
/*  535:     */   
/*  536:     */   public HeaderFooter getHeader()
/*  537:     */   {
/*  538:1137 */     return this.header;
/*  539:     */   }
/*  540:     */   
/*  541:     */   public void setHeader(HeaderFooter h)
/*  542:     */   {
/*  543:1147 */     this.header = h;
/*  544:     */   }
/*  545:     */   
/*  546:     */   public void setFooter(HeaderFooter f)
/*  547:     */   {
/*  548:1157 */     this.footer = f;
/*  549:     */   }
/*  550:     */   
/*  551:     */   public HeaderFooter getFooter()
/*  552:     */   {
/*  553:1167 */     return this.footer;
/*  554:     */   }
/*  555:     */   
/*  556:     */   public boolean isHorizontalCentre()
/*  557:     */   {
/*  558:1177 */     return this.horizontalCentre;
/*  559:     */   }
/*  560:     */   
/*  561:     */   public void setHorizontalCentre(boolean horizCentre)
/*  562:     */   {
/*  563:1187 */     this.horizontalCentre = horizCentre;
/*  564:     */   }
/*  565:     */   
/*  566:     */   public boolean isVerticalCentre()
/*  567:     */   {
/*  568:1197 */     return this.verticalCentre;
/*  569:     */   }
/*  570:     */   
/*  571:     */   public void setVerticalCentre(boolean vertCentre)
/*  572:     */   {
/*  573:1207 */     this.verticalCentre = vertCentre;
/*  574:     */   }
/*  575:     */   
/*  576:     */   public void setAutomaticFormulaCalculation(boolean auto)
/*  577:     */   {
/*  578:1218 */     this.automaticFormulaCalculation = auto;
/*  579:     */   }
/*  580:     */   
/*  581:     */   public boolean getAutomaticFormulaCalculation()
/*  582:     */   {
/*  583:1229 */     return this.automaticFormulaCalculation;
/*  584:     */   }
/*  585:     */   
/*  586:     */   public void setRecalculateFormulasBeforeSave(boolean recalc)
/*  587:     */   {
/*  588:1240 */     this.recalculateFormulasBeforeSave = recalc;
/*  589:     */   }
/*  590:     */   
/*  591:     */   public boolean getRecalculateFormulasBeforeSave()
/*  592:     */   {
/*  593:1251 */     return this.recalculateFormulasBeforeSave;
/*  594:     */   }
/*  595:     */   
/*  596:     */   public void setPrintArea(int firstCol, int firstRow, int lastCol, int lastRow)
/*  597:     */   {
/*  598:1267 */     this.printArea = new SheetRangeImpl(this.sheet, firstCol, firstRow, 
/*  599:1268 */       lastCol, lastRow);
/*  600:     */   }
/*  601:     */   
/*  602:     */   public Range getPrintArea()
/*  603:     */   {
/*  604:1278 */     return this.printArea;
/*  605:     */   }
/*  606:     */   
/*  607:     */   public void setPrintTitles(int firstRow, int lastRow, int firstCol, int lastCol)
/*  608:     */   {
/*  609:1294 */     setPrintTitlesRow(firstRow, lastRow);
/*  610:1295 */     setPrintTitlesCol(firstCol, lastCol);
/*  611:     */   }
/*  612:     */   
/*  613:     */   public void setPrintTitlesRow(int firstRow, int lastRow)
/*  614:     */   {
/*  615:1307 */     this.printTitlesRow = new SheetRangeImpl(this.sheet, 0, firstRow, 
/*  616:1308 */       255, lastRow);
/*  617:     */   }
/*  618:     */   
/*  619:     */   public void setPrintTitlesCol(int firstCol, int lastCol)
/*  620:     */   {
/*  621:1320 */     this.printTitlesCol = new SheetRangeImpl(this.sheet, firstCol, 0, 
/*  622:1321 */       lastCol, 65535);
/*  623:     */   }
/*  624:     */   
/*  625:     */   public Range getPrintTitlesRow()
/*  626:     */   {
/*  627:1331 */     return this.printTitlesRow;
/*  628:     */   }
/*  629:     */   
/*  630:     */   public Range getPrintTitlesCol()
/*  631:     */   {
/*  632:1342 */     return this.printTitlesCol;
/*  633:     */   }
/*  634:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.SheetSettings
 * JD-Core Version:    0.7.0.1
 */