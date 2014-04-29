/*    1:     */ package jxl.biff;
/*    2:     */ 
/*    3:     */ import java.text.DateFormat;
/*    4:     */ import java.text.DecimalFormat;
/*    5:     */ import java.text.DecimalFormatSymbols;
/*    6:     */ import java.text.NumberFormat;
/*    7:     */ import java.text.SimpleDateFormat;
/*    8:     */ import jxl.WorkbookSettings;
/*    9:     */ import jxl.common.Assert;
/*   10:     */ import jxl.common.Logger;
/*   11:     */ import jxl.format.Alignment;
/*   12:     */ import jxl.format.Border;
/*   13:     */ import jxl.format.BorderLineStyle;
/*   14:     */ import jxl.format.CellFormat;
/*   15:     */ import jxl.format.Colour;
/*   16:     */ import jxl.format.Font;
/*   17:     */ import jxl.format.Format;
/*   18:     */ import jxl.format.Orientation;
/*   19:     */ import jxl.format.Pattern;
/*   20:     */ import jxl.format.VerticalAlignment;
/*   21:     */ import jxl.read.biff.Record;
/*   22:     */ 
/*   23:     */ public class XFRecord
/*   24:     */   extends WritableRecordData
/*   25:     */   implements CellFormat
/*   26:     */ {
/*   27:  53 */   private static Logger logger = Logger.getLogger(XFRecord.class);
/*   28:     */   public int formatIndex;
/*   29:     */   private int parentFormat;
/*   30:     */   private XFType xfFormatType;
/*   31:     */   private boolean date;
/*   32:     */   private boolean number;
/*   33:     */   private DateFormat dateFormat;
/*   34:     */   private NumberFormat numberFormat;
/*   35:     */   private byte usedAttributes;
/*   36:     */   private int fontIndex;
/*   37:     */   private boolean locked;
/*   38:     */   private boolean hidden;
/*   39:     */   private Alignment align;
/*   40:     */   private VerticalAlignment valign;
/*   41:     */   private Orientation orientation;
/*   42:     */   private boolean wrap;
/*   43:     */   private int indentation;
/*   44:     */   private boolean shrinkToFit;
/*   45:     */   private BorderLineStyle leftBorder;
/*   46:     */   private BorderLineStyle rightBorder;
/*   47:     */   private BorderLineStyle topBorder;
/*   48:     */   private BorderLineStyle bottomBorder;
/*   49:     */   private Colour leftBorderColour;
/*   50:     */   private Colour rightBorderColour;
/*   51:     */   private Colour topBorderColour;
/*   52:     */   private Colour bottomBorderColour;
/*   53:     */   private Colour backgroundColour;
/*   54:     */   private Pattern pattern;
/*   55:     */   private int options;
/*   56:     */   private int xfIndex;
/*   57:     */   private FontRecord font;
/*   58:     */   private DisplayFormat format;
/*   59:     */   private boolean initialized;
/*   60:     */   private boolean read;
/*   61:     */   private Format excelFormat;
/*   62:     */   private boolean formatInfoInitialized;
/*   63:     */   private boolean copied;
/*   64:     */   private FormattingRecords formattingRecords;
/*   65:     */   private static final int USE_FONT = 4;
/*   66:     */   private static final int USE_FORMAT = 8;
/*   67:     */   private static final int USE_ALIGNMENT = 16;
/*   68:     */   private static final int USE_BORDER = 32;
/*   69:     */   private static final int USE_BACKGROUND = 64;
/*   70:     */   private static final int USE_PROTECTION = 128;
/*   71:     */   private static final int USE_DEFAULT_VALUE = 248;
/*   72: 251 */   private static final int[] dateFormats = { 14, 
/*   73: 252 */     15, 
/*   74: 253 */     16, 
/*   75: 254 */     17, 
/*   76: 255 */     18, 
/*   77: 256 */     19, 
/*   78: 257 */     20, 
/*   79: 258 */     21, 
/*   80: 259 */     22, 
/*   81: 260 */     45, 
/*   82: 261 */     46, 
/*   83: 262 */     47 };
/*   84: 268 */   private static final DateFormat[] javaDateFormats = { SimpleDateFormat.getDateInstance(3), 
/*   85: 269 */     SimpleDateFormat.getDateInstance(2), 
/*   86: 270 */     new SimpleDateFormat("d-MMM"), 
/*   87: 271 */     new SimpleDateFormat("MMM-yy"), 
/*   88: 272 */     new SimpleDateFormat("h:mm a"), 
/*   89: 273 */     new SimpleDateFormat("h:mm:ss a"), 
/*   90: 274 */     new SimpleDateFormat("H:mm"), 
/*   91: 275 */     new SimpleDateFormat("H:mm:ss"), 
/*   92: 276 */     new SimpleDateFormat("M/d/yy H:mm"), 
/*   93: 277 */     new SimpleDateFormat("mm:ss"), 
/*   94: 278 */     new SimpleDateFormat("H:mm:ss"), 
/*   95: 279 */     new SimpleDateFormat("mm:ss.S") };
/*   96: 285 */   private static int[] numberFormats = { 1, 
/*   97: 286 */     2, 
/*   98: 287 */     3, 
/*   99: 288 */     4, 
/*  100: 289 */     5, 
/*  101: 290 */     6, 
/*  102: 291 */     7, 
/*  103: 292 */     8, 
/*  104: 293 */     9, 
/*  105: 294 */     10, 
/*  106: 295 */     11, 
/*  107: 296 */     37, 
/*  108: 297 */     38, 
/*  109: 298 */     39, 
/*  110: 299 */     40, 
/*  111: 300 */     41, 
/*  112: 301 */     42, 
/*  113: 302 */     43, 
/*  114: 303 */     44, 
/*  115: 304 */     48 };
/*  116: 310 */   private static NumberFormat[] javaNumberFormats = { new DecimalFormat("0"), 
/*  117: 311 */     new DecimalFormat("0.00"), 
/*  118: 312 */     new DecimalFormat("#,##0"), 
/*  119: 313 */     new DecimalFormat("#,##0.00"), 
/*  120: 314 */     new DecimalFormat("$#,##0;($#,##0)"), 
/*  121: 315 */     new DecimalFormat("$#,##0;($#,##0)"), 
/*  122: 316 */     new DecimalFormat("$#,##0.00;($#,##0.00)"), 
/*  123: 317 */     new DecimalFormat("$#,##0.00;($#,##0.00)"), 
/*  124: 318 */     new DecimalFormat("0%"), 
/*  125: 319 */     new DecimalFormat("0.00%"), 
/*  126: 320 */     new DecimalFormat("0.00E00"), 
/*  127: 321 */     new DecimalFormat("#,##0;(#,##0)"), 
/*  128: 322 */     new DecimalFormat("#,##0;(#,##0)"), 
/*  129: 323 */     new DecimalFormat("#,##0.00;(#,##0.00)"), 
/*  130: 324 */     new DecimalFormat("#,##0.00;(#,##0.00)"), 
/*  131: 325 */     new DecimalFormat("#,##0;(#,##0)"), 
/*  132: 326 */     new DecimalFormat("$#,##0;($#,##0)"), 
/*  133: 327 */     new DecimalFormat("#,##0.00;(#,##0.00)"), 
/*  134: 328 */     new DecimalFormat("$#,##0.00;($#,##0.00)"), 
/*  135: 329 */     new DecimalFormat("##0.0E0") };
/*  136: 334 */   public static final BiffType biff8 = new BiffType(null);
/*  137: 335 */   public static final BiffType biff7 = new BiffType(null);
/*  138:     */   private BiffType biffType;
/*  139: 346 */   protected static final XFType cell = new XFType(null);
/*  140: 347 */   protected static final XFType style = new XFType(null);
/*  141:     */   
/*  142:     */   public XFRecord(Record t, WorkbookSettings ws, BiffType bt)
/*  143:     */   {
/*  144: 357 */     super(t);
/*  145:     */     
/*  146: 359 */     this.biffType = bt;
/*  147:     */     
/*  148: 361 */     byte[] data = getRecord().getData();
/*  149:     */     
/*  150: 363 */     this.fontIndex = IntegerHelper.getInt(data[0], data[1]);
/*  151: 364 */     this.formatIndex = IntegerHelper.getInt(data[2], data[3]);
/*  152: 365 */     this.date = false;
/*  153: 366 */     this.number = false;
/*  154: 370 */     for (int i = 0; (i < dateFormats.length) && (!this.date); i++) {
/*  155: 372 */       if (this.formatIndex == dateFormats[i])
/*  156:     */       {
/*  157: 374 */         this.date = true;
/*  158: 375 */         this.dateFormat = javaDateFormats[i];
/*  159:     */       }
/*  160:     */     }
/*  161: 380 */     for (int i = 0; (i < numberFormats.length) && (!this.number); i++) {
/*  162: 382 */       if (this.formatIndex == numberFormats[i])
/*  163:     */       {
/*  164: 384 */         this.number = true;
/*  165: 385 */         DecimalFormat df = (DecimalFormat)javaNumberFormats[i].clone();
/*  166: 386 */         DecimalFormatSymbols symbols = 
/*  167: 387 */           new DecimalFormatSymbols(ws.getLocale());
/*  168: 388 */         df.setDecimalFormatSymbols(symbols);
/*  169: 389 */         this.numberFormat = df;
/*  170:     */       }
/*  171:     */     }
/*  172: 395 */     int cellAttributes = IntegerHelper.getInt(data[4], data[5]);
/*  173: 396 */     this.parentFormat = ((cellAttributes & 0xFFF0) >> 4);
/*  174:     */     
/*  175: 398 */     int formatType = cellAttributes & 0x4;
/*  176: 399 */     this.xfFormatType = (formatType == 0 ? cell : style);
/*  177: 400 */     this.locked = ((cellAttributes & 0x1) != 0);
/*  178: 401 */     this.hidden = ((cellAttributes & 0x2) != 0);
/*  179: 403 */     if ((this.xfFormatType == cell) && 
/*  180: 404 */       ((this.parentFormat & 0xFFF) == 4095))
/*  181:     */     {
/*  182: 407 */       this.parentFormat = 0;
/*  183: 408 */       logger.warn("Invalid parent format found - ignoring");
/*  184:     */     }
/*  185: 411 */     this.initialized = false;
/*  186: 412 */     this.read = true;
/*  187: 413 */     this.formatInfoInitialized = false;
/*  188: 414 */     this.copied = false;
/*  189:     */   }
/*  190:     */   
/*  191:     */   public XFRecord(FontRecord fnt, DisplayFormat form)
/*  192:     */   {
/*  193: 425 */     super(Type.XF);
/*  194:     */     
/*  195: 427 */     this.initialized = false;
/*  196: 428 */     this.locked = true;
/*  197: 429 */     this.hidden = false;
/*  198: 430 */     this.align = Alignment.GENERAL;
/*  199: 431 */     this.valign = VerticalAlignment.BOTTOM;
/*  200: 432 */     this.orientation = Orientation.HORIZONTAL;
/*  201: 433 */     this.wrap = false;
/*  202: 434 */     this.leftBorder = BorderLineStyle.NONE;
/*  203: 435 */     this.rightBorder = BorderLineStyle.NONE;
/*  204: 436 */     this.topBorder = BorderLineStyle.NONE;
/*  205: 437 */     this.bottomBorder = BorderLineStyle.NONE;
/*  206: 438 */     this.leftBorderColour = Colour.AUTOMATIC;
/*  207: 439 */     this.rightBorderColour = Colour.AUTOMATIC;
/*  208: 440 */     this.topBorderColour = Colour.AUTOMATIC;
/*  209: 441 */     this.bottomBorderColour = Colour.AUTOMATIC;
/*  210: 442 */     this.pattern = Pattern.NONE;
/*  211: 443 */     this.backgroundColour = Colour.DEFAULT_BACKGROUND;
/*  212: 444 */     this.indentation = 0;
/*  213: 445 */     this.shrinkToFit = false;
/*  214: 446 */     this.usedAttributes = 124;
/*  215:     */     
/*  216:     */ 
/*  217:     */ 
/*  218: 450 */     this.parentFormat = 0;
/*  219: 451 */     this.xfFormatType = null;
/*  220:     */     
/*  221: 453 */     this.font = fnt;
/*  222: 454 */     this.format = form;
/*  223: 455 */     this.biffType = biff8;
/*  224: 456 */     this.read = false;
/*  225: 457 */     this.copied = false;
/*  226: 458 */     this.formatInfoInitialized = true;
/*  227:     */     
/*  228: 460 */     Assert.verify(this.font != null);
/*  229: 461 */     Assert.verify(this.format != null);
/*  230:     */   }
/*  231:     */   
/*  232:     */   protected XFRecord(XFRecord fmt)
/*  233:     */   {
/*  234: 472 */     super(Type.XF);
/*  235:     */     
/*  236: 474 */     this.initialized = false;
/*  237: 475 */     this.locked = fmt.locked;
/*  238: 476 */     this.hidden = fmt.hidden;
/*  239: 477 */     this.align = fmt.align;
/*  240: 478 */     this.valign = fmt.valign;
/*  241: 479 */     this.orientation = fmt.orientation;
/*  242: 480 */     this.wrap = fmt.wrap;
/*  243: 481 */     this.leftBorder = fmt.leftBorder;
/*  244: 482 */     this.rightBorder = fmt.rightBorder;
/*  245: 483 */     this.topBorder = fmt.topBorder;
/*  246: 484 */     this.bottomBorder = fmt.bottomBorder;
/*  247: 485 */     this.leftBorderColour = fmt.leftBorderColour;
/*  248: 486 */     this.rightBorderColour = fmt.rightBorderColour;
/*  249: 487 */     this.topBorderColour = fmt.topBorderColour;
/*  250: 488 */     this.bottomBorderColour = fmt.bottomBorderColour;
/*  251: 489 */     this.pattern = fmt.pattern;
/*  252: 490 */     this.xfFormatType = fmt.xfFormatType;
/*  253: 491 */     this.indentation = fmt.indentation;
/*  254: 492 */     this.shrinkToFit = fmt.shrinkToFit;
/*  255: 493 */     this.parentFormat = fmt.parentFormat;
/*  256: 494 */     this.backgroundColour = fmt.backgroundColour;
/*  257:     */     
/*  258:     */ 
/*  259: 497 */     this.font = fmt.font;
/*  260: 498 */     this.format = fmt.format;
/*  261:     */     
/*  262: 500 */     this.fontIndex = fmt.fontIndex;
/*  263: 501 */     this.formatIndex = fmt.formatIndex;
/*  264:     */     
/*  265: 503 */     this.formatInfoInitialized = fmt.formatInfoInitialized;
/*  266:     */     
/*  267: 505 */     this.biffType = biff8;
/*  268: 506 */     this.read = false;
/*  269: 507 */     this.copied = true;
/*  270:     */   }
/*  271:     */   
/*  272:     */   protected XFRecord(CellFormat cellFormat)
/*  273:     */   {
/*  274: 519 */     super(Type.XF);
/*  275:     */     
/*  276: 521 */     Assert.verify(cellFormat != null);
/*  277: 522 */     Assert.verify(cellFormat instanceof XFRecord);
/*  278: 523 */     XFRecord fmt = (XFRecord)cellFormat;
/*  279: 525 */     if (!fmt.formatInfoInitialized) {
/*  280: 527 */       fmt.initializeFormatInformation();
/*  281:     */     }
/*  282: 530 */     this.locked = fmt.locked;
/*  283: 531 */     this.hidden = fmt.hidden;
/*  284: 532 */     this.align = fmt.align;
/*  285: 533 */     this.valign = fmt.valign;
/*  286: 534 */     this.orientation = fmt.orientation;
/*  287: 535 */     this.wrap = fmt.wrap;
/*  288: 536 */     this.leftBorder = fmt.leftBorder;
/*  289: 537 */     this.rightBorder = fmt.rightBorder;
/*  290: 538 */     this.topBorder = fmt.topBorder;
/*  291: 539 */     this.bottomBorder = fmt.bottomBorder;
/*  292: 540 */     this.leftBorderColour = fmt.leftBorderColour;
/*  293: 541 */     this.rightBorderColour = fmt.rightBorderColour;
/*  294: 542 */     this.topBorderColour = fmt.topBorderColour;
/*  295: 543 */     this.bottomBorderColour = fmt.bottomBorderColour;
/*  296: 544 */     this.pattern = fmt.pattern;
/*  297: 545 */     this.xfFormatType = fmt.xfFormatType;
/*  298: 546 */     this.parentFormat = fmt.parentFormat;
/*  299: 547 */     this.indentation = fmt.indentation;
/*  300: 548 */     this.shrinkToFit = fmt.shrinkToFit;
/*  301: 549 */     this.backgroundColour = fmt.backgroundColour;
/*  302:     */     
/*  303:     */ 
/*  304: 552 */     this.font = new FontRecord(fmt.getFont());
/*  305: 555 */     if (fmt.getFormat() == null)
/*  306:     */     {
/*  307: 558 */       if (fmt.format.isBuiltIn()) {
/*  308: 560 */         this.format = fmt.format;
/*  309:     */       } else {
/*  310: 565 */         this.format = new FormatRecord((FormatRecord)fmt.format);
/*  311:     */       }
/*  312:     */     }
/*  313: 568 */     else if ((fmt.getFormat() instanceof BuiltInFormat))
/*  314:     */     {
/*  315: 571 */       this.excelFormat = ((BuiltInFormat)fmt.excelFormat);
/*  316: 572 */       this.format = ((BuiltInFormat)fmt.excelFormat);
/*  317:     */     }
/*  318:     */     else
/*  319:     */     {
/*  320: 577 */       Assert.verify(fmt.formatInfoInitialized);
/*  321:     */       
/*  322:     */ 
/*  323:     */ 
/*  324: 581 */       Assert.verify(fmt.excelFormat instanceof FormatRecord);
/*  325:     */       
/*  326:     */ 
/*  327: 584 */       FormatRecord fr = new FormatRecord((FormatRecord)fmt.excelFormat);
/*  328:     */       
/*  329:     */ 
/*  330:     */ 
/*  331: 588 */       this.excelFormat = fr;
/*  332: 589 */       this.format = fr;
/*  333:     */     }
/*  334: 592 */     this.biffType = biff8;
/*  335:     */     
/*  336:     */ 
/*  337: 595 */     this.formatInfoInitialized = true;
/*  338:     */     
/*  339:     */ 
/*  340: 598 */     this.read = false;
/*  341:     */     
/*  342:     */ 
/*  343: 601 */     this.copied = false;
/*  344:     */     
/*  345:     */ 
/*  346: 604 */     this.initialized = false;
/*  347:     */   }
/*  348:     */   
/*  349:     */   public DateFormat getDateFormat()
/*  350:     */   {
/*  351: 614 */     return this.dateFormat;
/*  352:     */   }
/*  353:     */   
/*  354:     */   public NumberFormat getNumberFormat()
/*  355:     */   {
/*  356: 624 */     return this.numberFormat;
/*  357:     */   }
/*  358:     */   
/*  359:     */   public int getFormatRecord()
/*  360:     */   {
/*  361: 634 */     return this.formatIndex;
/*  362:     */   }
/*  363:     */   
/*  364:     */   public boolean isDate()
/*  365:     */   {
/*  366: 644 */     return this.date;
/*  367:     */   }
/*  368:     */   
/*  369:     */   public boolean isNumber()
/*  370:     */   {
/*  371: 654 */     return this.number;
/*  372:     */   }
/*  373:     */   
/*  374:     */   public byte[] getData()
/*  375:     */   {
/*  376: 670 */     if (!this.formatInfoInitialized) {
/*  377: 672 */       initializeFormatInformation();
/*  378:     */     }
/*  379: 675 */     byte[] data = new byte[20];
/*  380:     */     
/*  381: 677 */     IntegerHelper.getTwoBytes(this.fontIndex, data, 0);
/*  382: 678 */     IntegerHelper.getTwoBytes(this.formatIndex, data, 2);
/*  383:     */     
/*  384:     */ 
/*  385: 681 */     int cellAttributes = 0;
/*  386: 683 */     if (getLocked()) {
/*  387: 685 */       cellAttributes |= 0x1;
/*  388:     */     }
/*  389: 688 */     if (getHidden()) {
/*  390: 690 */       cellAttributes |= 0x2;
/*  391:     */     }
/*  392: 693 */     if (this.xfFormatType == style)
/*  393:     */     {
/*  394: 695 */       cellAttributes |= 0x4;
/*  395: 696 */       this.parentFormat = 65535;
/*  396:     */     }
/*  397: 699 */     cellAttributes |= this.parentFormat << 4;
/*  398:     */     
/*  399: 701 */     IntegerHelper.getTwoBytes(cellAttributes, data, 4);
/*  400:     */     
/*  401: 703 */     int alignMask = this.align.getValue();
/*  402: 705 */     if (this.wrap) {
/*  403: 707 */       alignMask |= 0x8;
/*  404:     */     }
/*  405: 710 */     alignMask |= this.valign.getValue() << 4;
/*  406:     */     
/*  407: 712 */     alignMask |= this.orientation.getValue() << 8;
/*  408:     */     
/*  409: 714 */     IntegerHelper.getTwoBytes(alignMask, data, 6);
/*  410:     */     
/*  411: 716 */     data[9] = 16;
/*  412:     */     
/*  413:     */ 
/*  414: 719 */     int borderMask = this.leftBorder.getValue();
/*  415: 720 */     borderMask |= this.rightBorder.getValue() << 4;
/*  416: 721 */     borderMask |= this.topBorder.getValue() << 8;
/*  417: 722 */     borderMask |= this.bottomBorder.getValue() << 12;
/*  418:     */     
/*  419: 724 */     IntegerHelper.getTwoBytes(borderMask, data, 10);
/*  420: 728 */     if (borderMask != 0)
/*  421:     */     {
/*  422: 730 */       byte lc = (byte)this.leftBorderColour.getValue();
/*  423: 731 */       byte rc = (byte)this.rightBorderColour.getValue();
/*  424: 732 */       byte tc = (byte)this.topBorderColour.getValue();
/*  425: 733 */       byte bc = (byte)this.bottomBorderColour.getValue();
/*  426:     */       
/*  427: 735 */       int sideColourMask = lc & 0x7F | (rc & 0x7F) << 7;
/*  428: 736 */       int topColourMask = tc & 0x7F | (bc & 0x7F) << 7;
/*  429:     */       
/*  430: 738 */       IntegerHelper.getTwoBytes(sideColourMask, data, 12);
/*  431: 739 */       IntegerHelper.getTwoBytes(topColourMask, data, 14);
/*  432:     */     }
/*  433: 743 */     int patternVal = this.pattern.getValue() << 10;
/*  434: 744 */     IntegerHelper.getTwoBytes(patternVal, data, 16);
/*  435:     */     
/*  436:     */ 
/*  437: 747 */     int colourPaletteMask = this.backgroundColour.getValue();
/*  438: 748 */     colourPaletteMask |= 0x2000;
/*  439: 749 */     IntegerHelper.getTwoBytes(colourPaletteMask, data, 18);
/*  440:     */     
/*  441:     */ 
/*  442: 752 */     this.options |= this.indentation & 0xF;
/*  443: 754 */     if (this.shrinkToFit) {
/*  444: 756 */       this.options |= 0x10;
/*  445:     */     } else {
/*  446: 760 */       this.options &= 0xEF;
/*  447:     */     }
/*  448: 763 */     data[8] = ((byte)this.options);
/*  449: 765 */     if (this.biffType == biff8) {
/*  450: 767 */       data[9] = this.usedAttributes;
/*  451:     */     }
/*  452: 770 */     return data;
/*  453:     */   }
/*  454:     */   
/*  455:     */   protected final boolean getLocked()
/*  456:     */   {
/*  457: 780 */     return this.locked;
/*  458:     */   }
/*  459:     */   
/*  460:     */   protected final boolean getHidden()
/*  461:     */   {
/*  462: 790 */     return this.hidden;
/*  463:     */   }
/*  464:     */   
/*  465:     */   protected final void setXFLocked(boolean l)
/*  466:     */   {
/*  467: 800 */     this.locked = l;
/*  468: 801 */     this.usedAttributes = ((byte)(this.usedAttributes | 0x80));
/*  469:     */   }
/*  470:     */   
/*  471:     */   protected final void setXFCellOptions(int opt)
/*  472:     */   {
/*  473: 811 */     this.options |= opt;
/*  474:     */   }
/*  475:     */   
/*  476:     */   protected void setXFAlignment(Alignment a)
/*  477:     */   {
/*  478: 823 */     Assert.verify(!this.initialized);
/*  479: 824 */     this.align = a;
/*  480: 825 */     this.usedAttributes = ((byte)(this.usedAttributes | 0x10));
/*  481:     */   }
/*  482:     */   
/*  483:     */   protected void setXFIndentation(int i)
/*  484:     */   {
/*  485: 835 */     Assert.verify(!this.initialized);
/*  486: 836 */     this.indentation = i;
/*  487: 837 */     this.usedAttributes = ((byte)(this.usedAttributes | 0x10));
/*  488:     */   }
/*  489:     */   
/*  490:     */   protected void setXFShrinkToFit(boolean s)
/*  491:     */   {
/*  492: 847 */     Assert.verify(!this.initialized);
/*  493: 848 */     this.shrinkToFit = s;
/*  494: 849 */     this.usedAttributes = ((byte)(this.usedAttributes | 0x10));
/*  495:     */   }
/*  496:     */   
/*  497:     */   public Alignment getAlignment()
/*  498:     */   {
/*  499: 859 */     if (!this.formatInfoInitialized) {
/*  500: 861 */       initializeFormatInformation();
/*  501:     */     }
/*  502: 864 */     return this.align;
/*  503:     */   }
/*  504:     */   
/*  505:     */   public int getIndentation()
/*  506:     */   {
/*  507: 874 */     if (!this.formatInfoInitialized) {
/*  508: 876 */       initializeFormatInformation();
/*  509:     */     }
/*  510: 879 */     return this.indentation;
/*  511:     */   }
/*  512:     */   
/*  513:     */   public boolean isShrinkToFit()
/*  514:     */   {
/*  515: 889 */     if (!this.formatInfoInitialized) {
/*  516: 891 */       initializeFormatInformation();
/*  517:     */     }
/*  518: 894 */     return this.shrinkToFit;
/*  519:     */   }
/*  520:     */   
/*  521:     */   public boolean isLocked()
/*  522:     */   {
/*  523: 904 */     if (!this.formatInfoInitialized) {
/*  524: 906 */       initializeFormatInformation();
/*  525:     */     }
/*  526: 909 */     return this.locked;
/*  527:     */   }
/*  528:     */   
/*  529:     */   public VerticalAlignment getVerticalAlignment()
/*  530:     */   {
/*  531: 920 */     if (!this.formatInfoInitialized) {
/*  532: 922 */       initializeFormatInformation();
/*  533:     */     }
/*  534: 925 */     return this.valign;
/*  535:     */   }
/*  536:     */   
/*  537:     */   public Orientation getOrientation()
/*  538:     */   {
/*  539: 935 */     if (!this.formatInfoInitialized) {
/*  540: 937 */       initializeFormatInformation();
/*  541:     */     }
/*  542: 940 */     return this.orientation;
/*  543:     */   }
/*  544:     */   
/*  545:     */   protected void setXFBackground(Colour c, Pattern p)
/*  546:     */   {
/*  547: 953 */     Assert.verify(!this.initialized);
/*  548: 954 */     this.backgroundColour = c;
/*  549: 955 */     this.pattern = p;
/*  550: 956 */     this.usedAttributes = ((byte)(this.usedAttributes | 0x40));
/*  551:     */   }
/*  552:     */   
/*  553:     */   public Colour getBackgroundColour()
/*  554:     */   {
/*  555: 966 */     if (!this.formatInfoInitialized) {
/*  556: 968 */       initializeFormatInformation();
/*  557:     */     }
/*  558: 971 */     return this.backgroundColour;
/*  559:     */   }
/*  560:     */   
/*  561:     */   public Pattern getPattern()
/*  562:     */   {
/*  563: 981 */     if (!this.formatInfoInitialized) {
/*  564: 983 */       initializeFormatInformation();
/*  565:     */     }
/*  566: 986 */     return this.pattern;
/*  567:     */   }
/*  568:     */   
/*  569:     */   protected void setXFVerticalAlignment(VerticalAlignment va)
/*  570:     */   {
/*  571: 999 */     Assert.verify(!this.initialized);
/*  572:1000 */     this.valign = va;
/*  573:1001 */     this.usedAttributes = ((byte)(this.usedAttributes | 0x10));
/*  574:     */   }
/*  575:     */   
/*  576:     */   protected void setXFOrientation(Orientation o)
/*  577:     */   {
/*  578:1014 */     Assert.verify(!this.initialized);
/*  579:1015 */     this.orientation = o;
/*  580:1016 */     this.usedAttributes = ((byte)(this.usedAttributes | 0x10));
/*  581:     */   }
/*  582:     */   
/*  583:     */   protected void setXFWrap(boolean w)
/*  584:     */   {
/*  585:1028 */     Assert.verify(!this.initialized);
/*  586:1029 */     this.wrap = w;
/*  587:1030 */     this.usedAttributes = ((byte)(this.usedAttributes | 0x10));
/*  588:     */   }
/*  589:     */   
/*  590:     */   public boolean getWrap()
/*  591:     */   {
/*  592:1040 */     if (!this.formatInfoInitialized) {
/*  593:1042 */       initializeFormatInformation();
/*  594:     */     }
/*  595:1045 */     return this.wrap;
/*  596:     */   }
/*  597:     */   
/*  598:     */   protected void setXFBorder(Border b, BorderLineStyle ls, Colour c)
/*  599:     */   {
/*  600:1058 */     Assert.verify(!this.initialized);
/*  601:1060 */     if ((c == Colour.BLACK) || (c == Colour.UNKNOWN)) {
/*  602:1062 */       c = Colour.PALETTE_BLACK;
/*  603:     */     }
/*  604:1065 */     if (b == Border.LEFT)
/*  605:     */     {
/*  606:1067 */       this.leftBorder = ls;
/*  607:1068 */       this.leftBorderColour = c;
/*  608:     */     }
/*  609:1070 */     else if (b == Border.RIGHT)
/*  610:     */     {
/*  611:1072 */       this.rightBorder = ls;
/*  612:1073 */       this.rightBorderColour = c;
/*  613:     */     }
/*  614:1075 */     else if (b == Border.TOP)
/*  615:     */     {
/*  616:1077 */       this.topBorder = ls;
/*  617:1078 */       this.topBorderColour = c;
/*  618:     */     }
/*  619:1080 */     else if (b == Border.BOTTOM)
/*  620:     */     {
/*  621:1082 */       this.bottomBorder = ls;
/*  622:1083 */       this.bottomBorderColour = c;
/*  623:     */     }
/*  624:1086 */     this.usedAttributes = ((byte)(this.usedAttributes | 0x20));
/*  625:     */   }
/*  626:     */   
/*  627:     */   public BorderLineStyle getBorder(Border border)
/*  628:     */   {
/*  629:1102 */     return getBorderLine(border);
/*  630:     */   }
/*  631:     */   
/*  632:     */   public BorderLineStyle getBorderLine(Border border)
/*  633:     */   {
/*  634:1116 */     if ((border == Border.NONE) || 
/*  635:1117 */       (border == Border.ALL)) {
/*  636:1119 */       return BorderLineStyle.NONE;
/*  637:     */     }
/*  638:1122 */     if (!this.formatInfoInitialized) {
/*  639:1124 */       initializeFormatInformation();
/*  640:     */     }
/*  641:1127 */     if (border == Border.LEFT) {
/*  642:1129 */       return this.leftBorder;
/*  643:     */     }
/*  644:1131 */     if (border == Border.RIGHT) {
/*  645:1133 */       return this.rightBorder;
/*  646:     */     }
/*  647:1135 */     if (border == Border.TOP) {
/*  648:1137 */       return this.topBorder;
/*  649:     */     }
/*  650:1139 */     if (border == Border.BOTTOM) {
/*  651:1141 */       return this.bottomBorder;
/*  652:     */     }
/*  653:1144 */     return BorderLineStyle.NONE;
/*  654:     */   }
/*  655:     */   
/*  656:     */   public Colour getBorderColour(Border border)
/*  657:     */   {
/*  658:1158 */     if ((border == Border.NONE) || 
/*  659:1159 */       (border == Border.ALL)) {
/*  660:1161 */       return Colour.PALETTE_BLACK;
/*  661:     */     }
/*  662:1164 */     if (!this.formatInfoInitialized) {
/*  663:1166 */       initializeFormatInformation();
/*  664:     */     }
/*  665:1169 */     if (border == Border.LEFT) {
/*  666:1171 */       return this.leftBorderColour;
/*  667:     */     }
/*  668:1173 */     if (border == Border.RIGHT) {
/*  669:1175 */       return this.rightBorderColour;
/*  670:     */     }
/*  671:1177 */     if (border == Border.TOP) {
/*  672:1179 */       return this.topBorderColour;
/*  673:     */     }
/*  674:1181 */     if (border == Border.BOTTOM) {
/*  675:1183 */       return this.bottomBorderColour;
/*  676:     */     }
/*  677:1186 */     return Colour.BLACK;
/*  678:     */   }
/*  679:     */   
/*  680:     */   public final boolean hasBorders()
/*  681:     */   {
/*  682:1198 */     if (!this.formatInfoInitialized) {
/*  683:1200 */       initializeFormatInformation();
/*  684:     */     }
/*  685:1203 */     if ((this.leftBorder == BorderLineStyle.NONE) && 
/*  686:1204 */       (this.rightBorder == BorderLineStyle.NONE) && 
/*  687:1205 */       (this.topBorder == BorderLineStyle.NONE) && 
/*  688:1206 */       (this.bottomBorder == BorderLineStyle.NONE)) {
/*  689:1208 */       return false;
/*  690:     */     }
/*  691:1211 */     return true;
/*  692:     */   }
/*  693:     */   
/*  694:     */   public final void initialize(int pos, FormattingRecords fr, Fonts fonts)
/*  695:     */     throws NumFormatRecordsException
/*  696:     */   {
/*  697:1227 */     this.xfIndex = pos;
/*  698:1228 */     this.formattingRecords = fr;
/*  699:1234 */     if ((this.read) || (this.copied))
/*  700:     */     {
/*  701:1236 */       this.initialized = true;
/*  702:1237 */       return;
/*  703:     */     }
/*  704:1240 */     if (!this.font.isInitialized()) {
/*  705:1242 */       fonts.addFont(this.font);
/*  706:     */     }
/*  707:1245 */     if (!this.format.isInitialized()) {
/*  708:1247 */       fr.addFormat(this.format);
/*  709:     */     }
/*  710:1250 */     this.fontIndex = this.font.getFontIndex();
/*  711:1251 */     this.formatIndex = this.format.getFormatIndex();
/*  712:     */     
/*  713:1253 */     this.initialized = true;
/*  714:     */   }
/*  715:     */   
/*  716:     */   public final void uninitialize()
/*  717:     */   {
/*  718:1264 */     if (this.initialized) {
/*  719:1266 */       logger.warn("A default format has been initialized");
/*  720:     */     }
/*  721:1268 */     this.initialized = false;
/*  722:     */   }
/*  723:     */   
/*  724:     */   final void setXFIndex(int xfi)
/*  725:     */   {
/*  726:1279 */     this.xfIndex = xfi;
/*  727:     */   }
/*  728:     */   
/*  729:     */   public final int getXFIndex()
/*  730:     */   {
/*  731:1289 */     return this.xfIndex;
/*  732:     */   }
/*  733:     */   
/*  734:     */   public final boolean isInitialized()
/*  735:     */   {
/*  736:1299 */     return this.initialized;
/*  737:     */   }
/*  738:     */   
/*  739:     */   public final boolean isRead()
/*  740:     */   {
/*  741:1311 */     return this.read;
/*  742:     */   }
/*  743:     */   
/*  744:     */   public Format getFormat()
/*  745:     */   {
/*  746:1321 */     if (!this.formatInfoInitialized) {
/*  747:1323 */       initializeFormatInformation();
/*  748:     */     }
/*  749:1325 */     return this.excelFormat;
/*  750:     */   }
/*  751:     */   
/*  752:     */   public Font getFont()
/*  753:     */   {
/*  754:1335 */     if (!this.formatInfoInitialized) {
/*  755:1337 */       initializeFormatInformation();
/*  756:     */     }
/*  757:1339 */     return this.font;
/*  758:     */   }
/*  759:     */   
/*  760:     */   private void initializeFormatInformation()
/*  761:     */   {
/*  762:1348 */     if ((this.formatIndex < BuiltInFormat.builtIns.length) && 
/*  763:1349 */       (BuiltInFormat.builtIns[this.formatIndex] != null)) {
/*  764:1351 */       this.excelFormat = BuiltInFormat.builtIns[this.formatIndex];
/*  765:     */     } else {
/*  766:1355 */       this.excelFormat = this.formattingRecords.getFormatRecord(this.formatIndex);
/*  767:     */     }
/*  768:1359 */     this.font = this.formattingRecords.getFonts().getFont(this.fontIndex);
/*  769:     */     
/*  770:     */ 
/*  771:1362 */     byte[] data = getRecord().getData();
/*  772:     */     
/*  773:     */ 
/*  774:1365 */     int cellAttributes = IntegerHelper.getInt(data[4], data[5]);
/*  775:1366 */     this.parentFormat = ((cellAttributes & 0xFFF0) >> 4);
/*  776:1367 */     int formatType = cellAttributes & 0x4;
/*  777:1368 */     this.xfFormatType = (formatType == 0 ? cell : style);
/*  778:1369 */     this.locked = ((cellAttributes & 0x1) != 0);
/*  779:1370 */     this.hidden = ((cellAttributes & 0x2) != 0);
/*  780:1372 */     if ((this.xfFormatType == cell) && 
/*  781:1373 */       ((this.parentFormat & 0xFFF) == 4095))
/*  782:     */     {
/*  783:1376 */       this.parentFormat = 0;
/*  784:1377 */       logger.warn("Invalid parent format found - ignoring");
/*  785:     */     }
/*  786:1381 */     int alignMask = IntegerHelper.getInt(data[6], data[7]);
/*  787:1384 */     if ((alignMask & 0x8) != 0) {
/*  788:1386 */       this.wrap = true;
/*  789:     */     }
/*  790:1390 */     this.align = Alignment.getAlignment(alignMask & 0x7);
/*  791:     */     
/*  792:     */ 
/*  793:1393 */     this.valign = VerticalAlignment.getAlignment(alignMask >> 4 & 0x7);
/*  794:     */     
/*  795:     */ 
/*  796:1396 */     this.orientation = Orientation.getOrientation(alignMask >> 8 & 0xFF);
/*  797:     */     
/*  798:1398 */     int attr = IntegerHelper.getInt(data[8], data[9]);
/*  799:     */     
/*  800:     */ 
/*  801:1401 */     this.indentation = (attr & 0xF);
/*  802:     */     
/*  803:     */ 
/*  804:1404 */     this.shrinkToFit = ((attr & 0x10) != 0);
/*  805:1407 */     if (this.biffType == biff8) {
/*  806:1409 */       this.usedAttributes = data[9];
/*  807:     */     }
/*  808:1413 */     int borderMask = IntegerHelper.getInt(data[10], data[11]);
/*  809:     */     
/*  810:1415 */     this.leftBorder = BorderLineStyle.getStyle(borderMask & 0x7);
/*  811:1416 */     this.rightBorder = BorderLineStyle.getStyle(borderMask >> 4 & 0x7);
/*  812:1417 */     this.topBorder = BorderLineStyle.getStyle(borderMask >> 8 & 0x7);
/*  813:1418 */     this.bottomBorder = BorderLineStyle.getStyle(borderMask >> 12 & 0x7);
/*  814:     */     
/*  815:1420 */     int borderColourMask = IntegerHelper.getInt(data[12], data[13]);
/*  816:     */     
/*  817:1422 */     this.leftBorderColour = Colour.getInternalColour(borderColourMask & 0x7F);
/*  818:1423 */     this.rightBorderColour = Colour.getInternalColour(
/*  819:1424 */       (borderColourMask & 0x3F80) >> 7);
/*  820:     */     
/*  821:1426 */     borderColourMask = IntegerHelper.getInt(data[14], data[15]);
/*  822:1427 */     this.topBorderColour = Colour.getInternalColour(borderColourMask & 0x7F);
/*  823:1428 */     this.bottomBorderColour = Colour.getInternalColour(
/*  824:1429 */       (borderColourMask & 0x3F80) >> 7);
/*  825:1431 */     if (this.biffType == biff8)
/*  826:     */     {
/*  827:1434 */       int patternVal = IntegerHelper.getInt(data[16], data[17]);
/*  828:1435 */       patternVal &= 0xFC00;
/*  829:1436 */       patternVal >>= 10;
/*  830:1437 */       this.pattern = Pattern.getPattern(patternVal);
/*  831:     */       
/*  832:     */ 
/*  833:1440 */       int colourPaletteMask = IntegerHelper.getInt(data[18], data[19]);
/*  834:1441 */       this.backgroundColour = Colour.getInternalColour(colourPaletteMask & 0x3F);
/*  835:1443 */       if ((this.backgroundColour == Colour.UNKNOWN) || 
/*  836:1444 */         (this.backgroundColour == Colour.DEFAULT_BACKGROUND1)) {
/*  837:1446 */         this.backgroundColour = Colour.DEFAULT_BACKGROUND;
/*  838:     */       }
/*  839:     */     }
/*  840:     */     else
/*  841:     */     {
/*  842:1451 */       this.pattern = Pattern.NONE;
/*  843:1452 */       this.backgroundColour = Colour.DEFAULT_BACKGROUND;
/*  844:     */     }
/*  845:1456 */     this.formatInfoInitialized = true;
/*  846:     */   }
/*  847:     */   
/*  848:     */   public int hashCode()
/*  849:     */   {
/*  850:1466 */     if (!this.formatInfoInitialized) {
/*  851:1468 */       initializeFormatInformation();
/*  852:     */     }
/*  853:1471 */     int hashValue = 17;
/*  854:1472 */     int oddPrimeNumber = 37;
/*  855:     */     
/*  856:     */ 
/*  857:1475 */     hashValue = oddPrimeNumber * hashValue + (this.hidden ? 1 : 0);
/*  858:1476 */     hashValue = oddPrimeNumber * hashValue + (this.locked ? 1 : 0);
/*  859:1477 */     hashValue = oddPrimeNumber * hashValue + (this.wrap ? 1 : 0);
/*  860:1478 */     hashValue = oddPrimeNumber * hashValue + (this.shrinkToFit ? 1 : 0);
/*  861:1481 */     if (this.xfFormatType == cell) {
/*  862:1483 */       hashValue = oddPrimeNumber * hashValue + 1;
/*  863:1485 */     } else if (this.xfFormatType == style) {
/*  864:1487 */       hashValue = oddPrimeNumber * hashValue + 2;
/*  865:     */     }
/*  866:1490 */     hashValue = oddPrimeNumber * hashValue + (this.align.getValue() + 1);
/*  867:1491 */     hashValue = oddPrimeNumber * hashValue + (this.valign.getValue() + 1);
/*  868:1492 */     hashValue = oddPrimeNumber * hashValue + this.orientation.getValue();
/*  869:     */     
/*  870:1494 */     hashValue ^= this.leftBorder.getDescription().hashCode();
/*  871:1495 */     hashValue ^= this.rightBorder.getDescription().hashCode();
/*  872:1496 */     hashValue ^= this.topBorder.getDescription().hashCode();
/*  873:1497 */     hashValue ^= this.bottomBorder.getDescription().hashCode();
/*  874:     */     
/*  875:1499 */     hashValue = oddPrimeNumber * hashValue + this.leftBorderColour.getValue();
/*  876:1500 */     hashValue = oddPrimeNumber * hashValue + this.rightBorderColour.getValue();
/*  877:1501 */     hashValue = oddPrimeNumber * hashValue + this.topBorderColour.getValue();
/*  878:1502 */     hashValue = oddPrimeNumber * hashValue + this.bottomBorderColour.getValue();
/*  879:1503 */     hashValue = oddPrimeNumber * hashValue + this.backgroundColour.getValue();
/*  880:1504 */     hashValue = oddPrimeNumber * hashValue + (this.pattern.getValue() + 1);
/*  881:     */     
/*  882:     */ 
/*  883:1507 */     hashValue = oddPrimeNumber * hashValue + this.usedAttributes;
/*  884:1508 */     hashValue = oddPrimeNumber * hashValue + this.parentFormat;
/*  885:1509 */     hashValue = oddPrimeNumber * hashValue + this.fontIndex;
/*  886:1510 */     hashValue = oddPrimeNumber * hashValue + this.formatIndex;
/*  887:1511 */     hashValue = oddPrimeNumber * hashValue + this.indentation;
/*  888:     */     
/*  889:1513 */     return hashValue;
/*  890:     */   }
/*  891:     */   
/*  892:     */   public boolean equals(Object o)
/*  893:     */   {
/*  894:1525 */     if (o == this) {
/*  895:1527 */       return true;
/*  896:     */     }
/*  897:1530 */     if (!(o instanceof XFRecord)) {
/*  898:1532 */       return false;
/*  899:     */     }
/*  900:1535 */     XFRecord xfr = (XFRecord)o;
/*  901:1538 */     if (!this.formatInfoInitialized) {
/*  902:1540 */       initializeFormatInformation();
/*  903:     */     }
/*  904:1543 */     if (!xfr.formatInfoInitialized) {
/*  905:1545 */       xfr.initializeFormatInformation();
/*  906:     */     }
/*  907:1548 */     if ((this.xfFormatType != xfr.xfFormatType) || 
/*  908:1549 */       (this.parentFormat != xfr.parentFormat) || 
/*  909:1550 */       (this.locked != xfr.locked) || 
/*  910:1551 */       (this.hidden != xfr.hidden) || 
/*  911:1552 */       (this.usedAttributes != xfr.usedAttributes)) {
/*  912:1554 */       return false;
/*  913:     */     }
/*  914:1557 */     if ((this.align != xfr.align) || 
/*  915:1558 */       (this.valign != xfr.valign) || 
/*  916:1559 */       (this.orientation != xfr.orientation) || 
/*  917:1560 */       (this.wrap != xfr.wrap) || 
/*  918:1561 */       (this.shrinkToFit != xfr.shrinkToFit) || 
/*  919:1562 */       (this.indentation != xfr.indentation)) {
/*  920:1564 */       return false;
/*  921:     */     }
/*  922:1567 */     if ((this.leftBorder != xfr.leftBorder) || 
/*  923:1568 */       (this.rightBorder != xfr.rightBorder) || 
/*  924:1569 */       (this.topBorder != xfr.topBorder) || 
/*  925:1570 */       (this.bottomBorder != xfr.bottomBorder)) {
/*  926:1572 */       return false;
/*  927:     */     }
/*  928:1575 */     if ((this.leftBorderColour != xfr.leftBorderColour) || 
/*  929:1576 */       (this.rightBorderColour != xfr.rightBorderColour) || 
/*  930:1577 */       (this.topBorderColour != xfr.topBorderColour) || 
/*  931:1578 */       (this.bottomBorderColour != xfr.bottomBorderColour)) {
/*  932:1580 */       return false;
/*  933:     */     }
/*  934:1583 */     if ((this.backgroundColour != xfr.backgroundColour) || 
/*  935:1584 */       (this.pattern != xfr.pattern)) {
/*  936:1586 */       return false;
/*  937:     */     }
/*  938:1589 */     if ((this.initialized) && (xfr.initialized))
/*  939:     */     {
/*  940:1596 */       if ((this.fontIndex != xfr.fontIndex) || 
/*  941:1597 */         (this.formatIndex != xfr.formatIndex)) {
/*  942:1599 */         return false;
/*  943:     */       }
/*  944:     */     }
/*  945:1605 */     else if ((!this.font.equals(xfr.font)) || 
/*  946:1606 */       (!this.format.equals(xfr.format))) {
/*  947:1608 */       return false;
/*  948:     */     }
/*  949:1612 */     return true;
/*  950:     */   }
/*  951:     */   
/*  952:     */   void setFormatIndex(int newindex)
/*  953:     */   {
/*  954:1622 */     this.formatIndex = newindex;
/*  955:     */   }
/*  956:     */   
/*  957:     */   public int getFontIndex()
/*  958:     */   {
/*  959:1632 */     return this.fontIndex;
/*  960:     */   }
/*  961:     */   
/*  962:     */   void setFontIndex(int newindex)
/*  963:     */   {
/*  964:1643 */     this.fontIndex = newindex;
/*  965:     */   }
/*  966:     */   
/*  967:     */   protected void setXFDetails(XFType t, int pf)
/*  968:     */   {
/*  969:1653 */     this.xfFormatType = t;
/*  970:1654 */     this.parentFormat = pf;
/*  971:     */   }
/*  972:     */   
/*  973:     */   void rationalize(IndexMapping xfMapping)
/*  974:     */   {
/*  975:1663 */     this.xfIndex = xfMapping.getNewIndex(this.xfIndex);
/*  976:1665 */     if (this.xfFormatType == cell) {
/*  977:1667 */       this.parentFormat = xfMapping.getNewIndex(this.parentFormat);
/*  978:     */     }
/*  979:     */   }
/*  980:     */   
/*  981:     */   public void setFont(FontRecord f)
/*  982:     */   {
/*  983:1684 */     this.font = f;
/*  984:     */   }
/*  985:     */   
/*  986:     */   private static class BiffType {}
/*  987:     */   
/*  988:     */   private static class XFType {}
/*  989:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.XFRecord
 * JD-Core Version:    0.7.0.1
 */