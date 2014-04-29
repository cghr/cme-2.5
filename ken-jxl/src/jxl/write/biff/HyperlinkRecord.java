/*    1:     */ package jxl.write.biff;
/*    2:     */ 
/*    3:     */ import java.io.File;
/*    4:     */ import java.net.MalformedURLException;
/*    5:     */ import java.net.URL;
/*    6:     */ import java.util.ArrayList;
/*    7:     */ import jxl.CellType;
/*    8:     */ import jxl.Hyperlink;
/*    9:     */ import jxl.Range;
/*   10:     */ import jxl.biff.CellReferenceHelper;
/*   11:     */ import jxl.biff.IntegerHelper;
/*   12:     */ import jxl.biff.SheetRangeImpl;
/*   13:     */ import jxl.biff.StringHelper;
/*   14:     */ import jxl.biff.Type;
/*   15:     */ import jxl.biff.WritableRecordData;
/*   16:     */ import jxl.common.Assert;
/*   17:     */ import jxl.common.Logger;
/*   18:     */ import jxl.read.biff.Record;
/*   19:     */ import jxl.write.Label;
/*   20:     */ import jxl.write.WritableCell;
/*   21:     */ import jxl.write.WritableSheet;
/*   22:     */ 
/*   23:     */ public class HyperlinkRecord
/*   24:     */   extends WritableRecordData
/*   25:     */ {
/*   26:  54 */   private static Logger logger = Logger.getLogger(HyperlinkRecord.class);
/*   27:     */   private int firstRow;
/*   28:     */   private int lastRow;
/*   29:     */   private int firstColumn;
/*   30:     */   private int lastColumn;
/*   31:     */   private URL url;
/*   32:     */   private File file;
/*   33:     */   private String location;
/*   34:     */   private String contents;
/*   35:     */   private LinkType linkType;
/*   36:     */   private byte[] data;
/*   37:     */   private Range range;
/*   38:     */   private WritableSheet sheet;
/*   39:     */   private boolean modified;
/*   40: 124 */   private static final LinkType urlLink = new LinkType(null);
/*   41: 125 */   private static final LinkType fileLink = new LinkType(null);
/*   42: 126 */   private static final LinkType uncLink = new LinkType(null);
/*   43: 127 */   private static final LinkType workbookLink = new LinkType(null);
/*   44: 128 */   private static final LinkType unknown = new LinkType(null);
/*   45:     */   
/*   46:     */   protected HyperlinkRecord(Hyperlink h, WritableSheet s)
/*   47:     */   {
/*   48: 137 */     super(Type.HLINK);
/*   49: 139 */     if ((h instanceof jxl.read.biff.HyperlinkRecord)) {
/*   50: 141 */       copyReadHyperlink(h, s);
/*   51:     */     } else {
/*   52: 145 */       copyWritableHyperlink(h, s);
/*   53:     */     }
/*   54:     */   }
/*   55:     */   
/*   56:     */   private void copyReadHyperlink(Hyperlink h, WritableSheet s)
/*   57:     */   {
/*   58: 154 */     jxl.read.biff.HyperlinkRecord hl = (jxl.read.biff.HyperlinkRecord)h;
/*   59:     */     
/*   60: 156 */     this.data = hl.getRecord().getData();
/*   61: 157 */     this.sheet = s;
/*   62:     */     
/*   63:     */ 
/*   64: 160 */     this.firstRow = hl.getRow();
/*   65: 161 */     this.firstColumn = hl.getColumn();
/*   66: 162 */     this.lastRow = hl.getLastRow();
/*   67: 163 */     this.lastColumn = hl.getLastColumn();
/*   68: 164 */     this.range = new SheetRangeImpl(s, 
/*   69: 165 */       this.firstColumn, this.firstRow, 
/*   70: 166 */       this.lastColumn, this.lastRow);
/*   71:     */     
/*   72: 168 */     this.linkType = unknown;
/*   73: 170 */     if (hl.isFile())
/*   74:     */     {
/*   75: 172 */       this.linkType = fileLink;
/*   76: 173 */       this.file = hl.getFile();
/*   77:     */     }
/*   78: 175 */     else if (hl.isURL())
/*   79:     */     {
/*   80: 177 */       this.linkType = urlLink;
/*   81: 178 */       this.url = hl.getURL();
/*   82:     */     }
/*   83: 180 */     else if (hl.isLocation())
/*   84:     */     {
/*   85: 182 */       this.linkType = workbookLink;
/*   86: 183 */       this.location = hl.getLocation();
/*   87:     */     }
/*   88: 186 */     this.modified = false;
/*   89:     */   }
/*   90:     */   
/*   91:     */   private void copyWritableHyperlink(Hyperlink hl, WritableSheet s)
/*   92:     */   {
/*   93: 197 */     HyperlinkRecord h = (HyperlinkRecord)hl;
/*   94:     */     
/*   95: 199 */     this.firstRow = h.firstRow;
/*   96: 200 */     this.lastRow = h.lastRow;
/*   97: 201 */     this.firstColumn = h.firstColumn;
/*   98: 202 */     this.lastColumn = h.lastColumn;
/*   99: 204 */     if (h.url != null) {
/*  100:     */       try
/*  101:     */       {
/*  102: 208 */         this.url = new URL(h.url.toString());
/*  103:     */       }
/*  104:     */       catch (MalformedURLException e)
/*  105:     */       {
/*  106: 213 */         Assert.verify(false);
/*  107:     */       }
/*  108:     */     }
/*  109: 217 */     if (h.file != null) {
/*  110: 219 */       this.file = new File(h.file.getPath());
/*  111:     */     }
/*  112: 222 */     this.location = h.location;
/*  113: 223 */     this.contents = h.contents;
/*  114: 224 */     this.linkType = h.linkType;
/*  115: 225 */     this.modified = true;
/*  116:     */     
/*  117: 227 */     this.sheet = s;
/*  118: 228 */     this.range = new SheetRangeImpl(s, 
/*  119: 229 */       this.firstColumn, this.firstRow, 
/*  120: 230 */       this.lastColumn, this.lastRow);
/*  121:     */   }
/*  122:     */   
/*  123:     */   protected HyperlinkRecord(int col, int row, int lastcol, int lastrow, URL url, String desc)
/*  124:     */   {
/*  125: 248 */     super(Type.HLINK);
/*  126:     */     
/*  127: 250 */     this.firstColumn = col;
/*  128: 251 */     this.firstRow = row;
/*  129:     */     
/*  130: 253 */     this.lastColumn = Math.max(this.firstColumn, lastcol);
/*  131: 254 */     this.lastRow = Math.max(this.firstRow, lastrow);
/*  132:     */     
/*  133: 256 */     this.url = url;
/*  134: 257 */     this.contents = desc;
/*  135:     */     
/*  136: 259 */     this.linkType = urlLink;
/*  137:     */     
/*  138: 261 */     this.modified = true;
/*  139:     */   }
/*  140:     */   
/*  141:     */   protected HyperlinkRecord(int col, int row, int lastcol, int lastrow, File file, String desc)
/*  142:     */   {
/*  143: 277 */     super(Type.HLINK);
/*  144:     */     
/*  145: 279 */     this.firstColumn = col;
/*  146: 280 */     this.firstRow = row;
/*  147:     */     
/*  148: 282 */     this.lastColumn = Math.max(this.firstColumn, lastcol);
/*  149: 283 */     this.lastRow = Math.max(this.firstRow, lastrow);
/*  150: 284 */     this.contents = desc;
/*  151:     */     
/*  152: 286 */     this.file = file;
/*  153: 288 */     if (file.getPath().startsWith("\\\\")) {
/*  154: 290 */       this.linkType = uncLink;
/*  155:     */     } else {
/*  156: 294 */       this.linkType = fileLink;
/*  157:     */     }
/*  158: 297 */     this.modified = true;
/*  159:     */   }
/*  160:     */   
/*  161:     */   protected HyperlinkRecord(int col, int row, int lastcol, int lastrow, String desc, WritableSheet s, int destcol, int destrow, int lastdestcol, int lastdestrow)
/*  162:     */   {
/*  163: 321 */     super(Type.HLINK);
/*  164:     */     
/*  165: 323 */     this.firstColumn = col;
/*  166: 324 */     this.firstRow = row;
/*  167:     */     
/*  168: 326 */     this.lastColumn = Math.max(this.firstColumn, lastcol);
/*  169: 327 */     this.lastRow = Math.max(this.firstRow, lastrow);
/*  170:     */     
/*  171: 329 */     setLocation(s, destcol, destrow, lastdestcol, lastdestrow);
/*  172: 330 */     this.contents = desc;
/*  173:     */     
/*  174: 332 */     this.linkType = workbookLink;
/*  175:     */     
/*  176: 334 */     this.modified = true;
/*  177:     */   }
/*  178:     */   
/*  179:     */   public boolean isFile()
/*  180:     */   {
/*  181: 344 */     return this.linkType == fileLink;
/*  182:     */   }
/*  183:     */   
/*  184:     */   public boolean isUNC()
/*  185:     */   {
/*  186: 354 */     return this.linkType == uncLink;
/*  187:     */   }
/*  188:     */   
/*  189:     */   public boolean isURL()
/*  190:     */   {
/*  191: 364 */     return this.linkType == urlLink;
/*  192:     */   }
/*  193:     */   
/*  194:     */   public boolean isLocation()
/*  195:     */   {
/*  196: 374 */     return this.linkType == workbookLink;
/*  197:     */   }
/*  198:     */   
/*  199:     */   public int getRow()
/*  200:     */   {
/*  201: 384 */     return this.firstRow;
/*  202:     */   }
/*  203:     */   
/*  204:     */   public int getColumn()
/*  205:     */   {
/*  206: 394 */     return this.firstColumn;
/*  207:     */   }
/*  208:     */   
/*  209:     */   public int getLastRow()
/*  210:     */   {
/*  211: 404 */     return this.lastRow;
/*  212:     */   }
/*  213:     */   
/*  214:     */   public int getLastColumn()
/*  215:     */   {
/*  216: 414 */     return this.lastColumn;
/*  217:     */   }
/*  218:     */   
/*  219:     */   public URL getURL()
/*  220:     */   {
/*  221: 424 */     return this.url;
/*  222:     */   }
/*  223:     */   
/*  224:     */   public File getFile()
/*  225:     */   {
/*  226: 434 */     return this.file;
/*  227:     */   }
/*  228:     */   
/*  229:     */   public byte[] getData()
/*  230:     */   {
/*  231: 444 */     if (!this.modified) {
/*  232: 446 */       return this.data;
/*  233:     */     }
/*  234: 450 */     byte[] commonData = new byte[32];
/*  235:     */     
/*  236:     */ 
/*  237: 453 */     IntegerHelper.getTwoBytes(this.firstRow, commonData, 0);
/*  238: 454 */     IntegerHelper.getTwoBytes(this.lastRow, commonData, 2);
/*  239: 455 */     IntegerHelper.getTwoBytes(this.firstColumn, commonData, 4);
/*  240: 456 */     IntegerHelper.getTwoBytes(this.lastColumn, commonData, 6);
/*  241:     */     
/*  242:     */ 
/*  243: 459 */     commonData[8] = -48;
/*  244: 460 */     commonData[9] = -55;
/*  245: 461 */     commonData[10] = -22;
/*  246: 462 */     commonData[11] = 121;
/*  247: 463 */     commonData[12] = -7;
/*  248: 464 */     commonData[13] = -70;
/*  249: 465 */     commonData[14] = -50;
/*  250: 466 */     commonData[15] = 17;
/*  251: 467 */     commonData[16] = -116;
/*  252: 468 */     commonData[17] = -126;
/*  253: 469 */     commonData[18] = 0;
/*  254: 470 */     commonData[19] = -86;
/*  255: 471 */     commonData[20] = 0;
/*  256: 472 */     commonData[21] = 75;
/*  257: 473 */     commonData[22] = -87;
/*  258: 474 */     commonData[23] = 11;
/*  259: 475 */     commonData[24] = 2;
/*  260: 476 */     commonData[25] = 0;
/*  261: 477 */     commonData[26] = 0;
/*  262: 478 */     commonData[27] = 0;
/*  263:     */     
/*  264:     */ 
/*  265:     */ 
/*  266: 482 */     int optionFlags = 0;
/*  267: 483 */     if (isURL())
/*  268:     */     {
/*  269: 485 */       optionFlags = 3;
/*  270: 487 */       if (this.contents != null) {
/*  271: 489 */         optionFlags |= 0x14;
/*  272:     */       }
/*  273:     */     }
/*  274: 492 */     else if (isFile())
/*  275:     */     {
/*  276: 494 */       optionFlags = 1;
/*  277: 496 */       if (this.contents != null) {
/*  278: 498 */         optionFlags |= 0x14;
/*  279:     */       }
/*  280:     */     }
/*  281: 501 */     else if (isLocation())
/*  282:     */     {
/*  283: 503 */       optionFlags = 8;
/*  284:     */     }
/*  285: 505 */     else if (isUNC())
/*  286:     */     {
/*  287: 507 */       optionFlags = 259;
/*  288:     */     }
/*  289: 510 */     IntegerHelper.getFourBytes(optionFlags, commonData, 28);
/*  290: 512 */     if (isURL()) {
/*  291: 514 */       this.data = getURLData(commonData);
/*  292: 516 */     } else if (isFile()) {
/*  293: 518 */       this.data = getFileData(commonData);
/*  294: 520 */     } else if (isLocation()) {
/*  295: 522 */       this.data = getLocationData(commonData);
/*  296: 524 */     } else if (isUNC()) {
/*  297: 526 */       this.data = getUNCData(commonData);
/*  298:     */     }
/*  299: 529 */     return this.data;
/*  300:     */   }
/*  301:     */   
/*  302:     */   public String toString()
/*  303:     */   {
/*  304: 539 */     if (isFile()) {
/*  305: 541 */       return this.file.toString();
/*  306:     */     }
/*  307: 543 */     if (isURL()) {
/*  308: 545 */       return this.url.toString();
/*  309:     */     }
/*  310: 547 */     if (isUNC()) {
/*  311: 549 */       return this.file.toString();
/*  312:     */     }
/*  313: 553 */     return "";
/*  314:     */   }
/*  315:     */   
/*  316:     */   public Range getRange()
/*  317:     */   {
/*  318: 567 */     return this.range;
/*  319:     */   }
/*  320:     */   
/*  321:     */   public void setURL(URL url)
/*  322:     */   {
/*  323: 577 */     URL prevurl = this.url;
/*  324: 578 */     this.linkType = urlLink;
/*  325: 579 */     this.file = null;
/*  326: 580 */     this.location = null;
/*  327: 581 */     this.contents = null;
/*  328: 582 */     this.url = url;
/*  329: 583 */     this.modified = true;
/*  330: 585 */     if (this.sheet == null) {
/*  331: 588 */       return;
/*  332:     */     }
/*  333: 593 */     WritableCell wc = this.sheet.getWritableCell(this.firstColumn, this.firstRow);
/*  334: 595 */     if (wc.getType() == CellType.LABEL)
/*  335:     */     {
/*  336: 597 */       Label l = (Label)wc;
/*  337: 598 */       String prevurlString = prevurl.toString();
/*  338: 599 */       String prevurlString2 = "";
/*  339: 600 */       if ((prevurlString.charAt(prevurlString.length() - 1) == '/') || 
/*  340: 601 */         (prevurlString.charAt(prevurlString.length() - 1) == '\\')) {
/*  341: 603 */         prevurlString2 = prevurlString.substring(0, 
/*  342: 604 */           prevurlString.length() - 1);
/*  343:     */       }
/*  344: 607 */       if ((l.getString().equals(prevurlString)) || 
/*  345: 608 */         (l.getString().equals(prevurlString2))) {
/*  346: 610 */         l.setString(url.toString());
/*  347:     */       }
/*  348:     */     }
/*  349:     */   }
/*  350:     */   
/*  351:     */   public void setFile(File file)
/*  352:     */   {
/*  353: 622 */     this.linkType = fileLink;
/*  354: 623 */     this.url = null;
/*  355: 624 */     this.location = null;
/*  356: 625 */     this.contents = null;
/*  357: 626 */     this.file = file;
/*  358: 627 */     this.modified = true;
/*  359: 629 */     if (this.sheet == null) {
/*  360: 632 */       return;
/*  361:     */     }
/*  362: 636 */     WritableCell wc = this.sheet.getWritableCell(this.firstColumn, this.firstRow);
/*  363:     */     
/*  364: 638 */     Assert.verify(wc.getType() == CellType.LABEL);
/*  365:     */     
/*  366: 640 */     Label l = (Label)wc;
/*  367: 641 */     l.setString(file.toString());
/*  368:     */   }
/*  369:     */   
/*  370:     */   protected void setLocation(String desc, WritableSheet sheet, int destcol, int destrow, int lastdestcol, int lastdestrow)
/*  371:     */   {
/*  372: 659 */     this.linkType = workbookLink;
/*  373: 660 */     this.url = null;
/*  374: 661 */     this.file = null;
/*  375: 662 */     this.modified = true;
/*  376: 663 */     this.contents = desc;
/*  377:     */     
/*  378: 665 */     setLocation(sheet, destcol, destrow, lastdestcol, lastdestrow);
/*  379: 667 */     if (sheet == null) {
/*  380: 670 */       return;
/*  381:     */     }
/*  382: 674 */     WritableCell wc = sheet.getWritableCell(this.firstColumn, this.firstRow);
/*  383:     */     
/*  384: 676 */     Assert.verify(wc.getType() == CellType.LABEL);
/*  385:     */     
/*  386: 678 */     Label l = (Label)wc;
/*  387: 679 */     l.setString(desc);
/*  388:     */   }
/*  389:     */   
/*  390:     */   private void setLocation(WritableSheet sheet, int destcol, int destrow, int lastdestcol, int lastdestrow)
/*  391:     */   {
/*  392: 695 */     StringBuffer sb = new StringBuffer();
/*  393: 696 */     sb.append('\'');
/*  394: 698 */     if (sheet.getName().indexOf('\'') == -1)
/*  395:     */     {
/*  396: 700 */       sb.append(sheet.getName());
/*  397:     */     }
/*  398:     */     else
/*  399:     */     {
/*  400: 708 */       String sheetName = sheet.getName();
/*  401: 709 */       int pos = 0;
/*  402: 710 */       int nextPos = sheetName.indexOf('\'', pos);
/*  403: 712 */       while ((nextPos != -1) && (pos < sheetName.length()))
/*  404:     */       {
/*  405: 714 */         sb.append(sheetName.substring(pos, nextPos));
/*  406: 715 */         sb.append("''");
/*  407: 716 */         pos = nextPos + 1;
/*  408: 717 */         nextPos = sheetName.indexOf('\'', pos);
/*  409:     */       }
/*  410: 719 */       sb.append(sheetName.substring(pos));
/*  411:     */     }
/*  412: 722 */     sb.append('\'');
/*  413: 723 */     sb.append('!');
/*  414:     */     
/*  415: 725 */     lastdestcol = Math.max(destcol, lastdestcol);
/*  416: 726 */     lastdestrow = Math.max(destrow, lastdestrow);
/*  417:     */     
/*  418: 728 */     CellReferenceHelper.getCellReference(destcol, destrow, sb);
/*  419: 729 */     sb.append(':');
/*  420: 730 */     CellReferenceHelper.getCellReference(lastdestcol, lastdestrow, sb);
/*  421:     */     
/*  422: 732 */     this.location = sb.toString();
/*  423:     */   }
/*  424:     */   
/*  425:     */   void insertRow(int r)
/*  426:     */   {
/*  427: 744 */     Assert.verify((this.sheet != null) && (this.range != null));
/*  428: 746 */     if (r > this.lastRow) {
/*  429: 748 */       return;
/*  430:     */     }
/*  431: 751 */     if (r <= this.firstRow)
/*  432:     */     {
/*  433: 753 */       this.firstRow += 1;
/*  434: 754 */       this.modified = true;
/*  435:     */     }
/*  436: 757 */     if (r <= this.lastRow)
/*  437:     */     {
/*  438: 759 */       this.lastRow += 1;
/*  439: 760 */       this.modified = true;
/*  440:     */     }
/*  441: 763 */     if (this.modified) {
/*  442: 765 */       this.range = new SheetRangeImpl(this.sheet, 
/*  443: 766 */         this.firstColumn, this.firstRow, 
/*  444: 767 */         this.lastColumn, this.lastRow);
/*  445:     */     }
/*  446:     */   }
/*  447:     */   
/*  448:     */   void insertColumn(int c)
/*  449:     */   {
/*  450: 780 */     Assert.verify((this.sheet != null) && (this.range != null));
/*  451: 782 */     if (c > this.lastColumn) {
/*  452: 784 */       return;
/*  453:     */     }
/*  454: 787 */     if (c <= this.firstColumn)
/*  455:     */     {
/*  456: 789 */       this.firstColumn += 1;
/*  457: 790 */       this.modified = true;
/*  458:     */     }
/*  459: 793 */     if (c <= this.lastColumn)
/*  460:     */     {
/*  461: 795 */       this.lastColumn += 1;
/*  462: 796 */       this.modified = true;
/*  463:     */     }
/*  464: 799 */     if (this.modified) {
/*  465: 801 */       this.range = new SheetRangeImpl(this.sheet, 
/*  466: 802 */         this.firstColumn, this.firstRow, 
/*  467: 803 */         this.lastColumn, this.lastRow);
/*  468:     */     }
/*  469:     */   }
/*  470:     */   
/*  471:     */   void removeRow(int r)
/*  472:     */   {
/*  473: 816 */     Assert.verify((this.sheet != null) && (this.range != null));
/*  474: 818 */     if (r > this.lastRow) {
/*  475: 820 */       return;
/*  476:     */     }
/*  477: 823 */     if (r < this.firstRow)
/*  478:     */     {
/*  479: 825 */       this.firstRow -= 1;
/*  480: 826 */       this.modified = true;
/*  481:     */     }
/*  482: 829 */     if (r < this.lastRow)
/*  483:     */     {
/*  484: 831 */       this.lastRow -= 1;
/*  485: 832 */       this.modified = true;
/*  486:     */     }
/*  487: 835 */     if (this.modified)
/*  488:     */     {
/*  489: 837 */       Assert.verify(this.range != null);
/*  490: 838 */       this.range = new SheetRangeImpl(this.sheet, 
/*  491: 839 */         this.firstColumn, this.firstRow, 
/*  492: 840 */         this.lastColumn, this.lastRow);
/*  493:     */     }
/*  494:     */   }
/*  495:     */   
/*  496:     */   void removeColumn(int c)
/*  497:     */   {
/*  498: 853 */     Assert.verify((this.sheet != null) && (this.range != null));
/*  499: 855 */     if (c > this.lastColumn) {
/*  500: 857 */       return;
/*  501:     */     }
/*  502: 860 */     if (c < this.firstColumn)
/*  503:     */     {
/*  504: 862 */       this.firstColumn -= 1;
/*  505: 863 */       this.modified = true;
/*  506:     */     }
/*  507: 866 */     if (c < this.lastColumn)
/*  508:     */     {
/*  509: 868 */       this.lastColumn -= 1;
/*  510: 869 */       this.modified = true;
/*  511:     */     }
/*  512: 872 */     if (this.modified)
/*  513:     */     {
/*  514: 874 */       Assert.verify(this.range != null);
/*  515: 875 */       this.range = new SheetRangeImpl(this.sheet, 
/*  516: 876 */         this.firstColumn, this.firstRow, 
/*  517: 877 */         this.lastColumn, this.lastRow);
/*  518:     */     }
/*  519:     */   }
/*  520:     */   
/*  521:     */   private byte[] getURLData(byte[] cd)
/*  522:     */   {
/*  523: 889 */     String urlString = this.url.toString();
/*  524:     */     
/*  525: 891 */     int dataLength = cd.length + 20 + (urlString.length() + 1) * 2;
/*  526: 893 */     if (this.contents != null) {
/*  527: 895 */       dataLength += 4 + (this.contents.length() + 1) * 2;
/*  528:     */     }
/*  529: 898 */     byte[] d = new byte[dataLength];
/*  530:     */     
/*  531: 900 */     System.arraycopy(cd, 0, d, 0, cd.length);
/*  532:     */     
/*  533: 902 */     int urlPos = cd.length;
/*  534: 904 */     if (this.contents != null)
/*  535:     */     {
/*  536: 906 */       IntegerHelper.getFourBytes(this.contents.length() + 1, d, urlPos);
/*  537: 907 */       StringHelper.getUnicodeBytes(this.contents, d, urlPos + 4);
/*  538: 908 */       urlPos += (this.contents.length() + 1) * 2 + 4;
/*  539:     */     }
/*  540: 912 */     d[urlPos] = -32;
/*  541: 913 */     d[(urlPos + 1)] = -55;
/*  542: 914 */     d[(urlPos + 2)] = -22;
/*  543: 915 */     d[(urlPos + 3)] = 121;
/*  544: 916 */     d[(urlPos + 4)] = -7;
/*  545: 917 */     d[(urlPos + 5)] = -70;
/*  546: 918 */     d[(urlPos + 6)] = -50;
/*  547: 919 */     d[(urlPos + 7)] = 17;
/*  548: 920 */     d[(urlPos + 8)] = -116;
/*  549: 921 */     d[(urlPos + 9)] = -126;
/*  550: 922 */     d[(urlPos + 10)] = 0;
/*  551: 923 */     d[(urlPos + 11)] = -86;
/*  552: 924 */     d[(urlPos + 12)] = 0;
/*  553: 925 */     d[(urlPos + 13)] = 75;
/*  554: 926 */     d[(urlPos + 14)] = -87;
/*  555: 927 */     d[(urlPos + 15)] = 11;
/*  556:     */     
/*  557:     */ 
/*  558: 930 */     IntegerHelper.getFourBytes((urlString.length() + 1) * 2, d, urlPos + 16);
/*  559:     */     
/*  560:     */ 
/*  561: 933 */     StringHelper.getUnicodeBytes(urlString, d, urlPos + 20);
/*  562:     */     
/*  563: 935 */     return d;
/*  564:     */   }
/*  565:     */   
/*  566:     */   private byte[] getUNCData(byte[] cd)
/*  567:     */   {
/*  568: 946 */     String uncString = this.file.getPath();
/*  569:     */     
/*  570: 948 */     byte[] d = new byte[cd.length + uncString.length() * 2 + 2 + 4];
/*  571: 949 */     System.arraycopy(cd, 0, d, 0, cd.length);
/*  572:     */     
/*  573: 951 */     int urlPos = cd.length;
/*  574:     */     
/*  575:     */ 
/*  576: 954 */     int length = uncString.length() + 1;
/*  577: 955 */     IntegerHelper.getFourBytes(length, d, urlPos);
/*  578:     */     
/*  579:     */ 
/*  580: 958 */     StringHelper.getUnicodeBytes(uncString, d, urlPos + 4);
/*  581:     */     
/*  582: 960 */     return d;
/*  583:     */   }
/*  584:     */   
/*  585:     */   private byte[] getFileData(byte[] cd)
/*  586:     */   {
/*  587: 972 */     ArrayList path = new ArrayList();
/*  588: 973 */     ArrayList shortFileName = new ArrayList();
/*  589: 974 */     path.add(this.file.getName());
/*  590: 975 */     shortFileName.add(getShortName(this.file.getName()));
/*  591:     */     
/*  592: 977 */     File parent = this.file.getParentFile();
/*  593: 978 */     while (parent != null)
/*  594:     */     {
/*  595: 980 */       path.add(parent.getName());
/*  596: 981 */       shortFileName.add(getShortName(parent.getName()));
/*  597: 982 */       parent = parent.getParentFile();
/*  598:     */     }
/*  599: 987 */     int upLevelCount = 0;
/*  600: 988 */     int pos = path.size() - 1;
/*  601: 989 */     boolean upDir = true;
/*  602: 991 */     while (upDir)
/*  603:     */     {
/*  604: 993 */       String s = (String)path.get(pos);
/*  605: 994 */       if (s.equals(".."))
/*  606:     */       {
/*  607: 996 */         upLevelCount++;
/*  608: 997 */         path.remove(pos);
/*  609: 998 */         shortFileName.remove(pos);
/*  610:     */       }
/*  611:     */       else
/*  612:     */       {
/*  613:1002 */         upDir = false;
/*  614:     */       }
/*  615:1005 */       pos--;
/*  616:     */     }
/*  617:1008 */     StringBuffer filePathSB = new StringBuffer();
/*  618:1009 */     StringBuffer shortFilePathSB = new StringBuffer();
/*  619:1011 */     if (this.file.getPath().charAt(1) == ':')
/*  620:     */     {
/*  621:1013 */       char driveLetter = this.file.getPath().charAt(0);
/*  622:1014 */       if ((driveLetter != 'C') && (driveLetter != 'c'))
/*  623:     */       {
/*  624:1016 */         filePathSB.append(driveLetter);
/*  625:1017 */         filePathSB.append(':');
/*  626:1018 */         shortFilePathSB.append(driveLetter);
/*  627:1019 */         shortFilePathSB.append(':');
/*  628:     */       }
/*  629:     */     }
/*  630:1023 */     for (int i = path.size() - 1; i >= 0; i--)
/*  631:     */     {
/*  632:1025 */       filePathSB.append((String)path.get(i));
/*  633:1026 */       shortFilePathSB.append((String)shortFileName.get(i));
/*  634:1028 */       if (i != 0)
/*  635:     */       {
/*  636:1030 */         filePathSB.append("\\");
/*  637:1031 */         shortFilePathSB.append("\\");
/*  638:     */       }
/*  639:     */     }
/*  640:1036 */     String filePath = filePathSB.toString();
/*  641:1037 */     String shortFilePath = shortFilePathSB.toString();
/*  642:     */     
/*  643:1039 */     int dataLength = cd.length + 
/*  644:1040 */       4 + (shortFilePath.length() + 1) + 
/*  645:1041 */       16 + 
/*  646:1042 */       2 + 
/*  647:1043 */       8 + (filePath.length() + 1) * 2 + 
/*  648:1044 */       24;
/*  649:1047 */     if (this.contents != null) {
/*  650:1049 */       dataLength += 4 + (this.contents.length() + 1) * 2;
/*  651:     */     }
/*  652:1053 */     byte[] d = new byte[dataLength];
/*  653:     */     
/*  654:1055 */     System.arraycopy(cd, 0, d, 0, cd.length);
/*  655:     */     
/*  656:1057 */     int filePos = cd.length;
/*  657:1060 */     if (this.contents != null)
/*  658:     */     {
/*  659:1062 */       IntegerHelper.getFourBytes(this.contents.length() + 1, d, filePos);
/*  660:1063 */       StringHelper.getUnicodeBytes(this.contents, d, filePos + 4);
/*  661:1064 */       filePos += (this.contents.length() + 1) * 2 + 4;
/*  662:     */     }
/*  663:1067 */     int curPos = filePos;
/*  664:     */     
/*  665:     */ 
/*  666:1070 */     d[curPos] = 3;
/*  667:1071 */     d[(curPos + 1)] = 3;
/*  668:1072 */     d[(curPos + 2)] = 0;
/*  669:1073 */     d[(curPos + 3)] = 0;
/*  670:1074 */     d[(curPos + 4)] = 0;
/*  671:1075 */     d[(curPos + 5)] = 0;
/*  672:1076 */     d[(curPos + 6)] = 0;
/*  673:1077 */     d[(curPos + 7)] = 0;
/*  674:1078 */     d[(curPos + 8)] = -64;
/*  675:1079 */     d[(curPos + 9)] = 0;
/*  676:1080 */     d[(curPos + 10)] = 0;
/*  677:1081 */     d[(curPos + 11)] = 0;
/*  678:1082 */     d[(curPos + 12)] = 0;
/*  679:1083 */     d[(curPos + 13)] = 0;
/*  680:1084 */     d[(curPos + 14)] = 0;
/*  681:1085 */     d[(curPos + 15)] = 70;
/*  682:     */     
/*  683:1087 */     curPos += 16;
/*  684:     */     
/*  685:     */ 
/*  686:1090 */     IntegerHelper.getTwoBytes(upLevelCount, d, curPos);
/*  687:1091 */     curPos += 2;
/*  688:     */     
/*  689:     */ 
/*  690:1094 */     IntegerHelper.getFourBytes(shortFilePath.length() + 1, d, curPos);
/*  691:     */     
/*  692:     */ 
/*  693:1097 */     StringHelper.getBytes(shortFilePath, d, curPos + 4);
/*  694:     */     
/*  695:1099 */     curPos += 4 + (shortFilePath.length() + 1);
/*  696:     */     
/*  697:     */ 
/*  698:1102 */     d[curPos] = -1;
/*  699:1103 */     d[(curPos + 1)] = -1;
/*  700:1104 */     d[(curPos + 2)] = -83;
/*  701:1105 */     d[(curPos + 3)] = -34;
/*  702:1106 */     d[(curPos + 4)] = 0;
/*  703:1107 */     d[(curPos + 5)] = 0;
/*  704:1108 */     d[(curPos + 6)] = 0;
/*  705:1109 */     d[(curPos + 7)] = 0;
/*  706:1110 */     d[(curPos + 8)] = 0;
/*  707:1111 */     d[(curPos + 9)] = 0;
/*  708:1112 */     d[(curPos + 10)] = 0;
/*  709:1113 */     d[(curPos + 11)] = 0;
/*  710:1114 */     d[(curPos + 12)] = 0;
/*  711:1115 */     d[(curPos + 13)] = 0;
/*  712:1116 */     d[(curPos + 14)] = 0;
/*  713:1117 */     d[(curPos + 15)] = 0;
/*  714:1118 */     d[(curPos + 16)] = 0;
/*  715:1119 */     d[(curPos + 17)] = 0;
/*  716:1120 */     d[(curPos + 18)] = 0;
/*  717:1121 */     d[(curPos + 19)] = 0;
/*  718:1122 */     d[(curPos + 20)] = 0;
/*  719:1123 */     d[(curPos + 21)] = 0;
/*  720:1124 */     d[(curPos + 22)] = 0;
/*  721:1125 */     d[(curPos + 23)] = 0;
/*  722:     */     
/*  723:1127 */     curPos += 24;
/*  724:     */     
/*  725:     */ 
/*  726:     */ 
/*  727:1131 */     int size = 6 + filePath.length() * 2;
/*  728:1132 */     IntegerHelper.getFourBytes(size, d, curPos);
/*  729:1133 */     curPos += 4;
/*  730:     */     
/*  731:     */ 
/*  732:     */ 
/*  733:1137 */     IntegerHelper.getFourBytes(filePath.length() * 2, d, curPos);
/*  734:1138 */     curPos += 4;
/*  735:     */     
/*  736:     */ 
/*  737:1141 */     d[curPos] = 3;
/*  738:1142 */     d[(curPos + 1)] = 0;
/*  739:     */     
/*  740:1144 */     curPos += 2;
/*  741:     */     
/*  742:     */ 
/*  743:1147 */     StringHelper.getUnicodeBytes(filePath, d, curPos);
/*  744:1148 */     curPos += (filePath.length() + 1) * 2;
/*  745:     */     
/*  746:     */ 
/*  747:     */ 
/*  748:     */ 
/*  749:     */ 
/*  750:     */ 
/*  751:     */ 
/*  752:     */ 
/*  753:     */ 
/*  754:     */ 
/*  755:     */ 
/*  756:     */ 
/*  757:     */ 
/*  758:     */ 
/*  759:     */ 
/*  760:     */ 
/*  761:     */ 
/*  762:     */ 
/*  763:     */ 
/*  764:1168 */     return d;
/*  765:     */   }
/*  766:     */   
/*  767:     */   private String getShortName(String s)
/*  768:     */   {
/*  769:1179 */     int sep = s.indexOf('.');
/*  770:     */     
/*  771:1181 */     String prefix = null;
/*  772:1182 */     String suffix = null;
/*  773:1184 */     if (sep == -1)
/*  774:     */     {
/*  775:1186 */       prefix = s;
/*  776:1187 */       suffix = "";
/*  777:     */     }
/*  778:     */     else
/*  779:     */     {
/*  780:1191 */       prefix = s.substring(0, sep);
/*  781:1192 */       suffix = s.substring(sep + 1);
/*  782:     */     }
/*  783:1195 */     if (prefix.length() > 8)
/*  784:     */     {
/*  785:1197 */       prefix = prefix.substring(0, 6) + "~" + (prefix.length() - 8);
/*  786:1198 */       prefix = prefix.substring(0, 8);
/*  787:     */     }
/*  788:1201 */     suffix = suffix.substring(0, Math.min(3, suffix.length()));
/*  789:1203 */     if (suffix.length() > 0) {
/*  790:1205 */       return prefix + '.' + suffix;
/*  791:     */     }
/*  792:1209 */     return prefix;
/*  793:     */   }
/*  794:     */   
/*  795:     */   private byte[] getLocationData(byte[] cd)
/*  796:     */   {
/*  797:1221 */     byte[] d = new byte[cd.length + 4 + (this.location.length() + 1) * 2];
/*  798:1222 */     System.arraycopy(cd, 0, d, 0, cd.length);
/*  799:     */     
/*  800:1224 */     int locPos = cd.length;
/*  801:     */     
/*  802:     */ 
/*  803:1227 */     IntegerHelper.getFourBytes(this.location.length() + 1, d, locPos);
/*  804:     */     
/*  805:     */ 
/*  806:1230 */     StringHelper.getUnicodeBytes(this.location, d, locPos + 4);
/*  807:     */     
/*  808:1232 */     return d;
/*  809:     */   }
/*  810:     */   
/*  811:     */   void initialize(WritableSheet s)
/*  812:     */   {
/*  813:1243 */     this.sheet = s;
/*  814:1244 */     this.range = new SheetRangeImpl(s, 
/*  815:1245 */       this.firstColumn, this.firstRow, 
/*  816:1246 */       this.lastColumn, this.lastRow);
/*  817:     */   }
/*  818:     */   
/*  819:     */   String getContents()
/*  820:     */   {
/*  821:1257 */     return this.contents;
/*  822:     */   }
/*  823:     */   
/*  824:     */   protected void setContents(String desc)
/*  825:     */   {
/*  826:1267 */     this.contents = desc;
/*  827:1268 */     this.modified = true;
/*  828:     */   }
/*  829:     */   
/*  830:     */   private static class LinkType {}
/*  831:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.HyperlinkRecord
 * JD-Core Version:    0.7.0.1
 */