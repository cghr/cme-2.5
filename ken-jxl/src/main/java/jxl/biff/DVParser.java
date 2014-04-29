/*    1:     */ package jxl.biff;
/*    2:     */ 
/*    3:     */ import java.text.DecimalFormat;
/*    4:     */ import java.text.MessageFormat;
/*    5:     */ import java.util.Collection;
/*    6:     */ import java.util.Iterator;
/*    7:     */ import jxl.WorkbookSettings;
/*    8:     */ import jxl.biff.formula.ExternalSheet;
/*    9:     */ import jxl.biff.formula.FormulaException;
/*   10:     */ import jxl.biff.formula.FormulaParser;
/*   11:     */ import jxl.biff.formula.ParseContext;
/*   12:     */ import jxl.common.Assert;
/*   13:     */ import jxl.common.Logger;
/*   14:     */ 
/*   15:     */ public class DVParser
/*   16:     */ {
/*   17:  45 */   private static Logger logger = Logger.getLogger(DVParser.class);
/*   18:     */   
/*   19:     */   public static class DVType
/*   20:     */   {
/*   21:     */     private int value;
/*   22:     */     private String desc;
/*   23:  53 */     private static DVType[] types = new DVType[0];
/*   24:     */     
/*   25:     */     DVType(int v, String d)
/*   26:     */     {
/*   27:  57 */       this.value = v;
/*   28:  58 */       this.desc = d;
/*   29:  59 */       DVType[] oldtypes = types;
/*   30:  60 */       types = new DVType[oldtypes.length + 1];
/*   31:  61 */       System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
/*   32:  62 */       types[oldtypes.length] = this;
/*   33:     */     }
/*   34:     */     
/*   35:     */     static DVType getType(int v)
/*   36:     */     {
/*   37:  67 */       DVType found = null;
/*   38:  68 */       for (int i = 0; (i < types.length) && (found == null); i++) {
/*   39:  70 */         if (types[i].value == v) {
/*   40:  72 */           found = types[i];
/*   41:     */         }
/*   42:     */       }
/*   43:  75 */       return found;
/*   44:     */     }
/*   45:     */     
/*   46:     */     public int getValue()
/*   47:     */     {
/*   48:  80 */       return this.value;
/*   49:     */     }
/*   50:     */     
/*   51:     */     public String getDescription()
/*   52:     */     {
/*   53:  85 */       return this.desc;
/*   54:     */     }
/*   55:     */   }
/*   56:     */   
/*   57:     */   public static class ErrorStyle
/*   58:     */   {
/*   59:     */     private int value;
/*   60:  94 */     private static ErrorStyle[] types = new ErrorStyle[0];
/*   61:     */     
/*   62:     */     ErrorStyle(int v)
/*   63:     */     {
/*   64:  98 */       this.value = v;
/*   65:  99 */       ErrorStyle[] oldtypes = types;
/*   66: 100 */       types = new ErrorStyle[oldtypes.length + 1];
/*   67: 101 */       System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
/*   68: 102 */       types[oldtypes.length] = this;
/*   69:     */     }
/*   70:     */     
/*   71:     */     static ErrorStyle getErrorStyle(int v)
/*   72:     */     {
/*   73: 107 */       ErrorStyle found = null;
/*   74: 108 */       for (int i = 0; (i < types.length) && (found == null); i++) {
/*   75: 110 */         if (types[i].value == v) {
/*   76: 112 */           found = types[i];
/*   77:     */         }
/*   78:     */       }
/*   79: 115 */       return found;
/*   80:     */     }
/*   81:     */     
/*   82:     */     public int getValue()
/*   83:     */     {
/*   84: 120 */       return this.value;
/*   85:     */     }
/*   86:     */   }
/*   87:     */   
/*   88:     */   public static class Condition
/*   89:     */   {
/*   90:     */     private int value;
/*   91:     */     private MessageFormat format;
/*   92: 130 */     private static Condition[] types = new Condition[0];
/*   93:     */     
/*   94:     */     Condition(int v, String pattern)
/*   95:     */     {
/*   96: 134 */       this.value = v;
/*   97: 135 */       this.format = new MessageFormat(pattern);
/*   98: 136 */       Condition[] oldtypes = types;
/*   99: 137 */       types = new Condition[oldtypes.length + 1];
/*  100: 138 */       System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
/*  101: 139 */       types[oldtypes.length] = this;
/*  102:     */     }
/*  103:     */     
/*  104:     */     static Condition getCondition(int v)
/*  105:     */     {
/*  106: 144 */       Condition found = null;
/*  107: 145 */       for (int i = 0; (i < types.length) && (found == null); i++) {
/*  108: 147 */         if (types[i].value == v) {
/*  109: 149 */           found = types[i];
/*  110:     */         }
/*  111:     */       }
/*  112: 152 */       return found;
/*  113:     */     }
/*  114:     */     
/*  115:     */     public int getValue()
/*  116:     */     {
/*  117: 157 */       return this.value;
/*  118:     */     }
/*  119:     */     
/*  120:     */     public String getConditionString(String s1, String s2)
/*  121:     */     {
/*  122: 162 */       return this.format.format(new String[] { s1, s2 });
/*  123:     */     }
/*  124:     */   }
/*  125:     */   
/*  126: 167 */   public static final DVType ANY = new DVType(0, "any");
/*  127: 168 */   public static final DVType INTEGER = new DVType(1, "int");
/*  128: 169 */   public static final DVType DECIMAL = new DVType(2, "dec");
/*  129: 170 */   public static final DVType LIST = new DVType(3, "list");
/*  130: 171 */   public static final DVType DATE = new DVType(4, "date");
/*  131: 172 */   public static final DVType TIME = new DVType(5, "time");
/*  132: 173 */   public static final DVType TEXT_LENGTH = new DVType(6, "strlen");
/*  133: 174 */   public static final DVType FORMULA = new DVType(7, "form");
/*  134: 177 */   public static final ErrorStyle STOP = new ErrorStyle(0);
/*  135: 178 */   public static final ErrorStyle WARNING = new ErrorStyle(1);
/*  136: 179 */   public static final ErrorStyle INFO = new ErrorStyle(2);
/*  137: 182 */   public static final Condition BETWEEN = new Condition(0, "{0} <= x <= {1}");
/*  138: 184 */   public static final Condition NOT_BETWEEN = new Condition(1, "!({0} <= x <= {1}");
/*  139: 185 */   public static final Condition EQUAL = new Condition(2, "x == {0}");
/*  140: 186 */   public static final Condition NOT_EQUAL = new Condition(3, "x != {0}");
/*  141: 187 */   public static final Condition GREATER_THAN = new Condition(4, "x > {0}");
/*  142: 188 */   public static final Condition LESS_THAN = new Condition(5, "x < {0}");
/*  143: 189 */   public static final Condition GREATER_EQUAL = new Condition(6, "x >= {0}");
/*  144: 190 */   public static final Condition LESS_EQUAL = new Condition(7, "x <= {0}");
/*  145:     */   private static final int STRING_LIST_GIVEN_MASK = 128;
/*  146:     */   private static final int EMPTY_CELLS_ALLOWED_MASK = 256;
/*  147:     */   private static final int SUPPRESS_ARROW_MASK = 512;
/*  148:     */   private static final int SHOW_PROMPT_MASK = 262144;
/*  149:     */   private static final int SHOW_ERROR_MASK = 524288;
/*  150: 200 */   private static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
/*  151:     */   private static final int MAX_VALIDATION_LIST_LENGTH = 254;
/*  152:     */   private static final int MAX_ROWS = 65535;
/*  153:     */   private static final int MAX_COLUMNS = 255;
/*  154:     */   private DVType type;
/*  155:     */   private ErrorStyle errorStyle;
/*  156:     */   private Condition condition;
/*  157:     */   private boolean stringListGiven;
/*  158:     */   private boolean emptyCellsAllowed;
/*  159:     */   private boolean suppressArrow;
/*  160:     */   private boolean showPrompt;
/*  161:     */   private boolean showError;
/*  162:     */   private String promptTitle;
/*  163:     */   private String errorTitle;
/*  164:     */   private String promptText;
/*  165:     */   private String errorText;
/*  166:     */   private FormulaParser formula1;
/*  167:     */   private String formula1String;
/*  168:     */   private FormulaParser formula2;
/*  169:     */   private String formula2String;
/*  170:     */   private int column1;
/*  171:     */   private int row1;
/*  172:     */   private int column2;
/*  173:     */   private int row2;
/*  174:     */   private boolean extendedCellsValidation;
/*  175:     */   private boolean copied;
/*  176:     */   
/*  177:     */   public DVParser(byte[] data, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws)
/*  178:     */   {
/*  179: 328 */     Assert.verify(nt != null);
/*  180:     */     
/*  181: 330 */     this.copied = false;
/*  182: 331 */     int options = IntegerHelper.getInt(data[0], data[1], data[2], data[3]);
/*  183:     */     
/*  184: 333 */     int typeVal = options & 0xF;
/*  185: 334 */     this.type = DVType.getType(typeVal);
/*  186:     */     
/*  187: 336 */     int errorStyleVal = (options & 0x70) >> 4;
/*  188: 337 */     this.errorStyle = ErrorStyle.getErrorStyle(errorStyleVal);
/*  189:     */     
/*  190: 339 */     int conditionVal = (options & 0xF00000) >> 20;
/*  191: 340 */     this.condition = Condition.getCondition(conditionVal);
/*  192:     */     
/*  193: 342 */     this.stringListGiven = ((options & 0x80) != 0);
/*  194: 343 */     this.emptyCellsAllowed = ((options & 0x100) != 0);
/*  195: 344 */     this.suppressArrow = ((options & 0x200) != 0);
/*  196: 345 */     this.showPrompt = ((options & 0x40000) != 0);
/*  197: 346 */     this.showError = ((options & 0x80000) != 0);
/*  198:     */     
/*  199: 348 */     int pos = 4;
/*  200: 349 */     int length = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  201: 350 */     if ((length > 0) && (data[(pos + 2)] == 0))
/*  202:     */     {
/*  203: 352 */       this.promptTitle = StringHelper.getString(data, length, pos + 3, ws);
/*  204: 353 */       pos += length + 3;
/*  205:     */     }
/*  206: 355 */     else if (length > 0)
/*  207:     */     {
/*  208: 357 */       this.promptTitle = StringHelper.getUnicodeString(data, length, pos + 3);
/*  209: 358 */       pos += length * 2 + 3;
/*  210:     */     }
/*  211:     */     else
/*  212:     */     {
/*  213: 362 */       pos += 3;
/*  214:     */     }
/*  215: 365 */     length = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  216: 366 */     if ((length > 0) && (data[(pos + 2)] == 0))
/*  217:     */     {
/*  218: 368 */       this.errorTitle = StringHelper.getString(data, length, pos + 3, ws);
/*  219: 369 */       pos += length + 3;
/*  220:     */     }
/*  221: 371 */     else if (length > 0)
/*  222:     */     {
/*  223: 373 */       this.errorTitle = StringHelper.getUnicodeString(data, length, pos + 3);
/*  224: 374 */       pos += length * 2 + 3;
/*  225:     */     }
/*  226:     */     else
/*  227:     */     {
/*  228: 378 */       pos += 3;
/*  229:     */     }
/*  230: 381 */     length = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  231: 382 */     if ((length > 0) && (data[(pos + 2)] == 0))
/*  232:     */     {
/*  233: 384 */       this.promptText = StringHelper.getString(data, length, pos + 3, ws);
/*  234: 385 */       pos += length + 3;
/*  235:     */     }
/*  236: 387 */     else if (length > 0)
/*  237:     */     {
/*  238: 389 */       this.promptText = StringHelper.getUnicodeString(data, length, pos + 3);
/*  239: 390 */       pos += length * 2 + 3;
/*  240:     */     }
/*  241:     */     else
/*  242:     */     {
/*  243: 394 */       pos += 3;
/*  244:     */     }
/*  245: 397 */     length = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  246: 398 */     if ((length > 0) && (data[(pos + 2)] == 0))
/*  247:     */     {
/*  248: 400 */       this.errorText = StringHelper.getString(data, length, pos + 3, ws);
/*  249: 401 */       pos += length + 3;
/*  250:     */     }
/*  251: 403 */     else if (length > 0)
/*  252:     */     {
/*  253: 405 */       this.errorText = StringHelper.getUnicodeString(data, length, pos + 3);
/*  254: 406 */       pos += length * 2 + 3;
/*  255:     */     }
/*  256:     */     else
/*  257:     */     {
/*  258: 410 */       pos += 3;
/*  259:     */     }
/*  260: 413 */     int formula1Length = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  261: 414 */     pos += 4;
/*  262: 415 */     int formula1Pos = pos;
/*  263: 416 */     pos += formula1Length;
/*  264:     */     
/*  265: 418 */     int formula2Length = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  266: 419 */     pos += 4;
/*  267: 420 */     int formula2Pos = pos;
/*  268: 421 */     pos += formula2Length;
/*  269:     */     
/*  270: 423 */     pos += 2;
/*  271:     */     
/*  272: 425 */     this.row1 = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  273: 426 */     pos += 2;
/*  274:     */     
/*  275: 428 */     this.row2 = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  276: 429 */     pos += 2;
/*  277:     */     
/*  278: 431 */     this.column1 = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  279: 432 */     pos += 2;
/*  280:     */     
/*  281: 434 */     this.column2 = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  282: 435 */     pos += 2;
/*  283:     */     
/*  284: 437 */     this.extendedCellsValidation = ((this.row1 != this.row2) || (this.column1 != this.column2));
/*  285:     */     try
/*  286:     */     {
/*  287: 445 */       EmptyCell tmprt = new EmptyCell(this.column1, this.row1);
/*  288: 447 */       if (formula1Length != 0)
/*  289:     */       {
/*  290: 449 */         byte[] tokens = new byte[formula1Length];
/*  291: 450 */         System.arraycopy(data, formula1Pos, tokens, 0, formula1Length);
/*  292: 451 */         this.formula1 = new FormulaParser(tokens, tmprt, es, nt, ws, 
/*  293: 452 */           ParseContext.DATA_VALIDATION);
/*  294: 453 */         this.formula1.parse();
/*  295:     */       }
/*  296: 456 */       if (formula2Length != 0)
/*  297:     */       {
/*  298: 458 */         byte[] tokens = new byte[formula2Length];
/*  299: 459 */         System.arraycopy(data, formula2Pos, tokens, 0, formula2Length);
/*  300: 460 */         this.formula2 = new FormulaParser(tokens, tmprt, es, nt, ws, 
/*  301: 461 */           ParseContext.DATA_VALIDATION);
/*  302: 462 */         this.formula2.parse();
/*  303:     */       }
/*  304:     */     }
/*  305:     */     catch (FormulaException e)
/*  306:     */     {
/*  307: 467 */       logger.warn(e.getMessage() + " for cells " + 
/*  308: 468 */         CellReferenceHelper.getCellReference(this.column1, this.row1) + "-" + 
/*  309: 469 */         CellReferenceHelper.getCellReference(this.column2, this.row2));
/*  310:     */     }
/*  311:     */   }
/*  312:     */   
/*  313:     */   public DVParser(Collection strings)
/*  314:     */   {
/*  315: 478 */     this.copied = false;
/*  316: 479 */     this.type = LIST;
/*  317: 480 */     this.errorStyle = STOP;
/*  318: 481 */     this.condition = BETWEEN;
/*  319: 482 */     this.extendedCellsValidation = false;
/*  320:     */     
/*  321:     */ 
/*  322: 485 */     this.stringListGiven = true;
/*  323: 486 */     this.emptyCellsAllowed = true;
/*  324: 487 */     this.suppressArrow = false;
/*  325: 488 */     this.showPrompt = true;
/*  326: 489 */     this.showError = true;
/*  327:     */     
/*  328: 491 */     this.promptTitle = "";
/*  329: 492 */     this.errorTitle = "";
/*  330: 493 */     this.promptText = "";
/*  331: 494 */     this.errorText = "";
/*  332: 495 */     if (strings.size() == 0) {
/*  333: 497 */       logger.warn("no validation strings - ignoring");
/*  334:     */     }
/*  335: 500 */     Iterator i = strings.iterator();
/*  336: 501 */     StringBuffer formulaString = new StringBuffer();
/*  337:     */     
/*  338: 503 */     formulaString.append(i.next().toString());
/*  339: 504 */     while (i.hasNext())
/*  340:     */     {
/*  341: 506 */       formulaString.append('\000');
/*  342: 507 */       formulaString.append(' ');
/*  343: 508 */       formulaString.append(i.next().toString());
/*  344:     */     }
/*  345: 513 */     if (formulaString.length() > 254)
/*  346:     */     {
/*  347: 515 */       logger.warn("Validation list exceeds maximum number of characters - truncating");
/*  348:     */       
/*  349: 517 */       formulaString.delete(254, 
/*  350: 518 */         formulaString.length());
/*  351:     */     }
/*  352: 522 */     formulaString.insert(0, '"');
/*  353: 523 */     formulaString.append('"');
/*  354: 524 */     this.formula1String = formulaString.toString();
/*  355:     */   }
/*  356:     */   
/*  357:     */   public DVParser(String namedRange)
/*  358:     */   {
/*  359: 533 */     if (namedRange.length() == 0)
/*  360:     */     {
/*  361: 535 */       this.copied = false;
/*  362: 536 */       this.type = FORMULA;
/*  363: 537 */       this.errorStyle = STOP;
/*  364: 538 */       this.condition = EQUAL;
/*  365: 539 */       this.extendedCellsValidation = false;
/*  366:     */       
/*  367: 541 */       this.stringListGiven = false;
/*  368: 542 */       this.emptyCellsAllowed = false;
/*  369: 543 */       this.suppressArrow = false;
/*  370: 544 */       this.showPrompt = true;
/*  371: 545 */       this.showError = true;
/*  372:     */       
/*  373: 547 */       this.promptTitle = "";
/*  374: 548 */       this.errorTitle = "";
/*  375: 549 */       this.promptText = "";
/*  376: 550 */       this.errorText = "";
/*  377: 551 */       this.formula1String = "\"\"";
/*  378: 552 */       return;
/*  379:     */     }
/*  380: 555 */     this.copied = false;
/*  381: 556 */     this.type = LIST;
/*  382: 557 */     this.errorStyle = STOP;
/*  383: 558 */     this.condition = BETWEEN;
/*  384: 559 */     this.extendedCellsValidation = false;
/*  385:     */     
/*  386:     */ 
/*  387: 562 */     this.stringListGiven = false;
/*  388: 563 */     this.emptyCellsAllowed = true;
/*  389: 564 */     this.suppressArrow = false;
/*  390: 565 */     this.showPrompt = true;
/*  391: 566 */     this.showError = true;
/*  392:     */     
/*  393: 568 */     this.promptTitle = "";
/*  394: 569 */     this.errorTitle = "";
/*  395: 570 */     this.promptText = "";
/*  396: 571 */     this.errorText = "";
/*  397: 572 */     this.formula1String = namedRange;
/*  398:     */   }
/*  399:     */   
/*  400:     */   public DVParser(int c1, int r1, int c2, int r2)
/*  401:     */   {
/*  402: 580 */     this.copied = false;
/*  403: 581 */     this.type = LIST;
/*  404: 582 */     this.errorStyle = STOP;
/*  405: 583 */     this.condition = BETWEEN;
/*  406: 584 */     this.extendedCellsValidation = false;
/*  407:     */     
/*  408:     */ 
/*  409: 587 */     this.stringListGiven = false;
/*  410: 588 */     this.emptyCellsAllowed = true;
/*  411: 589 */     this.suppressArrow = false;
/*  412: 590 */     this.showPrompt = true;
/*  413: 591 */     this.showError = true;
/*  414:     */     
/*  415: 593 */     this.promptTitle = "";
/*  416: 594 */     this.errorTitle = "";
/*  417: 595 */     this.promptText = "";
/*  418: 596 */     this.errorText = "";
/*  419: 597 */     StringBuffer formulaString = new StringBuffer();
/*  420: 598 */     CellReferenceHelper.getCellReference(c1, r1, formulaString);
/*  421: 599 */     formulaString.append(':');
/*  422: 600 */     CellReferenceHelper.getCellReference(c2, r2, formulaString);
/*  423: 601 */     this.formula1String = formulaString.toString();
/*  424:     */   }
/*  425:     */   
/*  426:     */   public DVParser(double val1, double val2, Condition c)
/*  427:     */   {
/*  428: 609 */     this.copied = false;
/*  429: 610 */     this.type = DECIMAL;
/*  430: 611 */     this.errorStyle = STOP;
/*  431: 612 */     this.condition = c;
/*  432: 613 */     this.extendedCellsValidation = false;
/*  433:     */     
/*  434:     */ 
/*  435: 616 */     this.stringListGiven = false;
/*  436: 617 */     this.emptyCellsAllowed = true;
/*  437: 618 */     this.suppressArrow = false;
/*  438: 619 */     this.showPrompt = true;
/*  439: 620 */     this.showError = true;
/*  440:     */     
/*  441: 622 */     this.promptTitle = "";
/*  442: 623 */     this.errorTitle = "";
/*  443: 624 */     this.promptText = "";
/*  444: 625 */     this.errorText = "";
/*  445: 626 */     this.formula1String = DECIMAL_FORMAT.format(val1);
/*  446: 628 */     if (!Double.isNaN(val2)) {
/*  447: 630 */       this.formula2String = DECIMAL_FORMAT.format(val2);
/*  448:     */     }
/*  449:     */   }
/*  450:     */   
/*  451:     */   public DVParser(DVParser copy)
/*  452:     */   {
/*  453: 639 */     this.copied = true;
/*  454: 640 */     this.type = copy.type;
/*  455: 641 */     this.errorStyle = copy.errorStyle;
/*  456: 642 */     this.condition = copy.condition;
/*  457: 643 */     this.stringListGiven = copy.stringListGiven;
/*  458: 644 */     this.emptyCellsAllowed = copy.emptyCellsAllowed;
/*  459: 645 */     this.suppressArrow = copy.suppressArrow;
/*  460: 646 */     this.showPrompt = copy.showPrompt;
/*  461: 647 */     this.showError = copy.showError;
/*  462: 648 */     this.promptTitle = copy.promptTitle;
/*  463: 649 */     this.promptText = copy.promptText;
/*  464: 650 */     this.errorTitle = copy.errorTitle;
/*  465: 651 */     this.errorText = copy.errorText;
/*  466: 652 */     this.extendedCellsValidation = copy.extendedCellsValidation;
/*  467:     */     
/*  468: 654 */     this.row1 = copy.row1;
/*  469: 655 */     this.row2 = copy.row2;
/*  470: 656 */     this.column1 = copy.column1;
/*  471: 657 */     this.column2 = copy.column2;
/*  472: 660 */     if (copy.formula1String != null)
/*  473:     */     {
/*  474: 662 */       this.formula1String = copy.formula1String;
/*  475: 663 */       this.formula2String = copy.formula2String;
/*  476:     */     }
/*  477:     */     else
/*  478:     */     {
/*  479:     */       try
/*  480:     */       {
/*  481: 669 */         this.formula1String = copy.formula1.getFormula();
/*  482: 670 */         this.formula2String = (copy.formula2 != null ? 
/*  483: 671 */           copy.formula2.getFormula() : null);
/*  484:     */       }
/*  485:     */       catch (FormulaException e)
/*  486:     */       {
/*  487: 675 */         logger.warn("Cannot parse validation formula:  " + e.getMessage());
/*  488:     */       }
/*  489:     */     }
/*  490:     */   }
/*  491:     */   
/*  492:     */   public byte[] getData()
/*  493:     */   {
/*  494: 687 */     byte[] f1Bytes = this.formula1 != null ? this.formula1.getBytes() : new byte[0];
/*  495: 688 */     byte[] f2Bytes = this.formula2 != null ? this.formula2.getBytes() : new byte[0];
/*  496: 689 */     int dataLength = 
/*  497: 690 */       4 + 
/*  498: 691 */       this.promptTitle.length() * 2 + 3 + 
/*  499: 692 */       this.errorTitle.length() * 2 + 3 + 
/*  500: 693 */       this.promptText.length() * 2 + 3 + 
/*  501: 694 */       this.errorText.length() * 2 + 3 + 
/*  502: 695 */       f1Bytes.length + 2 + 
/*  503: 696 */       f2Bytes.length + 2 + 
/*  504: 697 */       4 + 
/*  505: 698 */       10;
/*  506:     */     
/*  507: 700 */     byte[] data = new byte[dataLength];
/*  508:     */     
/*  509:     */ 
/*  510: 703 */     int pos = 0;
/*  511:     */     
/*  512:     */ 
/*  513: 706 */     int options = 0;
/*  514: 707 */     options |= this.type.getValue();
/*  515: 708 */     options |= this.errorStyle.getValue() << 4;
/*  516: 709 */     options |= this.condition.getValue() << 20;
/*  517: 711 */     if (this.stringListGiven) {
/*  518: 713 */       options |= 0x80;
/*  519:     */     }
/*  520: 716 */     if (this.emptyCellsAllowed) {
/*  521: 718 */       options |= 0x100;
/*  522:     */     }
/*  523: 721 */     if (this.suppressArrow) {
/*  524: 723 */       options |= 0x200;
/*  525:     */     }
/*  526: 726 */     if (this.showPrompt) {
/*  527: 728 */       options |= 0x40000;
/*  528:     */     }
/*  529: 731 */     if (this.showError) {
/*  530: 733 */       options |= 0x80000;
/*  531:     */     }
/*  532: 737 */     IntegerHelper.getFourBytes(options, data, pos);
/*  533: 738 */     pos += 4;
/*  534:     */     
/*  535: 740 */     IntegerHelper.getTwoBytes(this.promptTitle.length(), data, pos);
/*  536: 741 */     pos += 2;
/*  537:     */     
/*  538: 743 */     data[pos] = 1;
/*  539: 744 */     pos++;
/*  540:     */     
/*  541: 746 */     StringHelper.getUnicodeBytes(this.promptTitle, data, pos);
/*  542: 747 */     pos += this.promptTitle.length() * 2;
/*  543:     */     
/*  544: 749 */     IntegerHelper.getTwoBytes(this.errorTitle.length(), data, pos);
/*  545: 750 */     pos += 2;
/*  546:     */     
/*  547: 752 */     data[pos] = 1;
/*  548: 753 */     pos++;
/*  549:     */     
/*  550: 755 */     StringHelper.getUnicodeBytes(this.errorTitle, data, pos);
/*  551: 756 */     pos += this.errorTitle.length() * 2;
/*  552:     */     
/*  553: 758 */     IntegerHelper.getTwoBytes(this.promptText.length(), data, pos);
/*  554: 759 */     pos += 2;
/*  555:     */     
/*  556: 761 */     data[pos] = 1;
/*  557: 762 */     pos++;
/*  558:     */     
/*  559: 764 */     StringHelper.getUnicodeBytes(this.promptText, data, pos);
/*  560: 765 */     pos += this.promptText.length() * 2;
/*  561:     */     
/*  562: 767 */     IntegerHelper.getTwoBytes(this.errorText.length(), data, pos);
/*  563: 768 */     pos += 2;
/*  564:     */     
/*  565: 770 */     data[pos] = 1;
/*  566: 771 */     pos++;
/*  567:     */     
/*  568: 773 */     StringHelper.getUnicodeBytes(this.errorText, data, pos);
/*  569: 774 */     pos += this.errorText.length() * 2;
/*  570:     */     
/*  571:     */ 
/*  572: 777 */     IntegerHelper.getTwoBytes(f1Bytes.length, data, pos);
/*  573: 778 */     pos += 4;
/*  574:     */     
/*  575: 780 */     System.arraycopy(f1Bytes, 0, data, pos, f1Bytes.length);
/*  576: 781 */     pos += f1Bytes.length;
/*  577:     */     
/*  578:     */ 
/*  579: 784 */     IntegerHelper.getTwoBytes(f2Bytes.length, data, pos);
/*  580: 785 */     pos += 4;
/*  581:     */     
/*  582: 787 */     System.arraycopy(f2Bytes, 0, data, pos, f2Bytes.length);
/*  583: 788 */     pos += f2Bytes.length;
/*  584:     */     
/*  585:     */ 
/*  586: 791 */     IntegerHelper.getTwoBytes(1, data, pos);
/*  587: 792 */     pos += 2;
/*  588:     */     
/*  589: 794 */     IntegerHelper.getTwoBytes(this.row1, data, pos);
/*  590: 795 */     pos += 2;
/*  591:     */     
/*  592: 797 */     IntegerHelper.getTwoBytes(this.row2, data, pos);
/*  593: 798 */     pos += 2;
/*  594:     */     
/*  595: 800 */     IntegerHelper.getTwoBytes(this.column1, data, pos);
/*  596: 801 */     pos += 2;
/*  597:     */     
/*  598: 803 */     IntegerHelper.getTwoBytes(this.column2, data, pos);
/*  599: 804 */     pos += 2;
/*  600:     */     
/*  601: 806 */     return data;
/*  602:     */   }
/*  603:     */   
/*  604:     */   public void insertRow(int row)
/*  605:     */   {
/*  606: 816 */     if (this.formula1 != null) {
/*  607: 818 */       this.formula1.rowInserted(0, row, true);
/*  608:     */     }
/*  609: 821 */     if (this.formula2 != null) {
/*  610: 823 */       this.formula2.rowInserted(0, row, true);
/*  611:     */     }
/*  612: 826 */     if (this.row1 >= row) {
/*  613: 828 */       this.row1 += 1;
/*  614:     */     }
/*  615: 831 */     if ((this.row2 >= row) && (this.row2 != 65535)) {
/*  616: 833 */       this.row2 += 1;
/*  617:     */     }
/*  618:     */   }
/*  619:     */   
/*  620:     */   public void insertColumn(int col)
/*  621:     */   {
/*  622: 844 */     if (this.formula1 != null) {
/*  623: 846 */       this.formula1.columnInserted(0, col, true);
/*  624:     */     }
/*  625: 849 */     if (this.formula2 != null) {
/*  626: 851 */       this.formula2.columnInserted(0, col, true);
/*  627:     */     }
/*  628: 854 */     if (this.column1 >= col) {
/*  629: 856 */       this.column1 += 1;
/*  630:     */     }
/*  631: 859 */     if ((this.column2 >= col) && (this.column2 != 255)) {
/*  632: 861 */       this.column2 += 1;
/*  633:     */     }
/*  634:     */   }
/*  635:     */   
/*  636:     */   public void removeRow(int row)
/*  637:     */   {
/*  638: 872 */     if (this.formula1 != null) {
/*  639: 874 */       this.formula1.rowRemoved(0, row, true);
/*  640:     */     }
/*  641: 877 */     if (this.formula2 != null) {
/*  642: 879 */       this.formula2.rowRemoved(0, row, true);
/*  643:     */     }
/*  644: 882 */     if (this.row1 > row) {
/*  645: 884 */       this.row1 -= 1;
/*  646:     */     }
/*  647: 887 */     if (this.row2 >= row) {
/*  648: 889 */       this.row2 -= 1;
/*  649:     */     }
/*  650:     */   }
/*  651:     */   
/*  652:     */   public void removeColumn(int col)
/*  653:     */   {
/*  654: 900 */     if (this.formula1 != null) {
/*  655: 902 */       this.formula1.columnRemoved(0, col, true);
/*  656:     */     }
/*  657: 905 */     if (this.formula2 != null) {
/*  658: 907 */       this.formula2.columnRemoved(0, col, true);
/*  659:     */     }
/*  660: 910 */     if (this.column1 > col) {
/*  661: 912 */       this.column1 -= 1;
/*  662:     */     }
/*  663: 915 */     if ((this.column2 >= col) && (this.column2 != 255)) {
/*  664: 917 */       this.column2 -= 1;
/*  665:     */     }
/*  666:     */   }
/*  667:     */   
/*  668:     */   public int getFirstColumn()
/*  669:     */   {
/*  670: 928 */     return this.column1;
/*  671:     */   }
/*  672:     */   
/*  673:     */   public int getLastColumn()
/*  674:     */   {
/*  675: 938 */     return this.column2;
/*  676:     */   }
/*  677:     */   
/*  678:     */   public int getFirstRow()
/*  679:     */   {
/*  680: 948 */     return this.row1;
/*  681:     */   }
/*  682:     */   
/*  683:     */   public int getLastRow()
/*  684:     */   {
/*  685: 958 */     return this.row2;
/*  686:     */   }
/*  687:     */   
/*  688:     */   String getValidationFormula()
/*  689:     */     throws FormulaException
/*  690:     */   {
/*  691: 969 */     if (this.type == LIST) {
/*  692: 971 */       return this.formula1.getFormula();
/*  693:     */     }
/*  694: 974 */     String s1 = this.formula1.getFormula();
/*  695: 975 */     String s2 = this.formula2 != null ? this.formula2.getFormula() : null;
/*  696: 976 */     return this.condition.getConditionString(s1, s2) + 
/*  697: 977 */       "; x " + this.type.getDescription();
/*  698:     */   }
/*  699:     */   
/*  700:     */   public void setCell(int col, int row, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws)
/*  701:     */     throws FormulaException
/*  702:     */   {
/*  703: 992 */     if (this.extendedCellsValidation) {
/*  704: 994 */       return;
/*  705:     */     }
/*  706: 997 */     this.row1 = row;
/*  707: 998 */     this.row2 = row;
/*  708: 999 */     this.column1 = col;
/*  709:1000 */     this.column2 = col;
/*  710:     */     
/*  711:1002 */     this.formula1 = new FormulaParser(this.formula1String, 
/*  712:1003 */       es, nt, ws, 
/*  713:1004 */       ParseContext.DATA_VALIDATION);
/*  714:1005 */     this.formula1.parse();
/*  715:1007 */     if (this.formula2String != null)
/*  716:     */     {
/*  717:1009 */       this.formula2 = new FormulaParser(this.formula2String, 
/*  718:1010 */         es, nt, ws, 
/*  719:1011 */         ParseContext.DATA_VALIDATION);
/*  720:1012 */       this.formula2.parse();
/*  721:     */     }
/*  722:     */   }
/*  723:     */   
/*  724:     */   public void extendCellValidation(int cols, int rows)
/*  725:     */   {
/*  726:1024 */     this.row2 = (this.row1 + rows);
/*  727:1025 */     this.column2 = (this.column1 + cols);
/*  728:1026 */     this.extendedCellsValidation = true;
/*  729:     */   }
/*  730:     */   
/*  731:     */   public boolean extendedCellsValidation()
/*  732:     */   {
/*  733:1035 */     return this.extendedCellsValidation;
/*  734:     */   }
/*  735:     */   
/*  736:     */   public boolean copied()
/*  737:     */   {
/*  738:1040 */     return this.copied;
/*  739:     */   }
/*  740:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.DVParser
 * JD-Core Version:    0.7.0.1
 */