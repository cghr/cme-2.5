/*    1:     */ package jxl.demo;
/*    2:     */ 
/*    3:     */ import java.io.File;
/*    4:     */ import java.io.IOException;
/*    5:     */ import java.io.PrintStream;
/*    6:     */ import java.net.MalformedURLException;
/*    7:     */ import java.net.URL;
/*    8:     */ import java.text.SimpleDateFormat;
/*    9:     */ import java.util.ArrayList;
/*   10:     */ import java.util.Calendar;
/*   11:     */ import java.util.Date;
/*   12:     */ import java.util.Locale;
/*   13:     */ import java.util.TimeZone;
/*   14:     */ import jxl.CellReferenceHelper;
/*   15:     */ import jxl.CellView;
/*   16:     */ import jxl.HeaderFooter;
/*   17:     */ import jxl.HeaderFooter.Contents;
/*   18:     */ import jxl.Range;
/*   19:     */ import jxl.SheetSettings;
/*   20:     */ import jxl.Workbook;
/*   21:     */ import jxl.WorkbookSettings;
/*   22:     */ import jxl.format.Alignment;
/*   23:     */ import jxl.format.Border;
/*   24:     */ import jxl.format.BorderLineStyle;
/*   25:     */ import jxl.format.Colour;
/*   26:     */ import jxl.format.Orientation;
/*   27:     */ import jxl.format.PageOrder;
/*   28:     */ import jxl.format.PageOrientation;
/*   29:     */ import jxl.format.PaperSize;
/*   30:     */ import jxl.format.ScriptStyle;
/*   31:     */ import jxl.format.UnderlineStyle;
/*   32:     */ import jxl.write.Blank;
/*   33:     */ import jxl.write.Boolean;
/*   34:     */ import jxl.write.DateFormat;
/*   35:     */ import jxl.write.DateFormats;
/*   36:     */ import jxl.write.DateTime;
/*   37:     */ import jxl.write.Formula;
/*   38:     */ import jxl.write.Label;
/*   39:     */ import jxl.write.Number;
/*   40:     */ import jxl.write.NumberFormat;
/*   41:     */ import jxl.write.NumberFormats;
/*   42:     */ import jxl.write.WritableCellFeatures;
/*   43:     */ import jxl.write.WritableCellFormat;
/*   44:     */ import jxl.write.WritableFont;
/*   45:     */ import jxl.write.WritableFont.FontName;
/*   46:     */ import jxl.write.WritableHyperlink;
/*   47:     */ import jxl.write.WritableImage;
/*   48:     */ import jxl.write.WritableSheet;
/*   49:     */ import jxl.write.WritableWorkbook;
/*   50:     */ import jxl.write.WriteException;
/*   51:     */ 
/*   52:     */ public class Write
/*   53:     */ {
/*   54:     */   private String filename;
/*   55:     */   private WritableWorkbook workbook;
/*   56:     */   
/*   57:     */   public Write(String fn)
/*   58:     */   {
/*   59:  93 */     this.filename = fn;
/*   60:     */   }
/*   61:     */   
/*   62:     */   public void write()
/*   63:     */     throws IOException, WriteException
/*   64:     */   {
/*   65: 104 */     WorkbookSettings ws = new WorkbookSettings();
/*   66: 105 */     ws.setLocale(new Locale("en", "EN"));
/*   67: 106 */     this.workbook = Workbook.createWorkbook(new File(this.filename), ws);
/*   68:     */     
/*   69:     */ 
/*   70: 109 */     WritableSheet s2 = this.workbook.createSheet("Number Formats", 0);
/*   71: 110 */     WritableSheet s3 = this.workbook.createSheet("Date Formats", 1);
/*   72: 111 */     WritableSheet s1 = this.workbook.createSheet("Label Formats", 2);
/*   73: 112 */     WritableSheet s4 = this.workbook.createSheet("Borders", 3);
/*   74: 113 */     WritableSheet s5 = this.workbook.createSheet("Labels", 4);
/*   75: 114 */     WritableSheet s6 = this.workbook.createSheet("Formulas", 5);
/*   76: 115 */     WritableSheet s7 = this.workbook.createSheet("Images", 6);
/*   77:     */     
/*   78:     */ 
/*   79:     */ 
/*   80:     */ 
/*   81: 120 */     this.workbook.setColourRGB(Colour.LIME, 255, 0, 0);
/*   82:     */     
/*   83:     */ 
/*   84: 123 */     this.workbook.addNameArea("namedrange", s4, 1, 11, 5, 14);
/*   85: 124 */     this.workbook.addNameArea("validation_range", s1, 4, 65, 9, 65);
/*   86: 125 */     this.workbook.addNameArea("formulavalue", s6, 1, 45, 1, 45);
/*   87:     */     
/*   88:     */ 
/*   89: 128 */     s5.getSettings().setPrintArea(4, 4, 15, 35);
/*   90:     */     
/*   91: 130 */     writeLabelFormatSheet(s1);
/*   92: 131 */     writeNumberFormatSheet(s2);
/*   93: 132 */     writeDateFormatSheet(s3);
/*   94: 133 */     writeBordersSheet(s4);
/*   95: 134 */     writeLabelsSheet(s5);
/*   96: 135 */     writeFormulaSheet(s6);
/*   97: 136 */     writeImageSheet(s7);
/*   98:     */     
/*   99: 138 */     this.workbook.write();
/*  100: 139 */     this.workbook.close();
/*  101:     */   }
/*  102:     */   
/*  103:     */   private void writeNumberFormatSheet(WritableSheet s)
/*  104:     */     throws WriteException
/*  105:     */   {
/*  106: 149 */     WritableCellFormat wrappedText = new WritableCellFormat(
/*  107: 150 */       WritableWorkbook.ARIAL_10_PT);
/*  108: 151 */     wrappedText.setWrap(true);
/*  109:     */     
/*  110: 153 */     s.setColumnView(0, 20);
/*  111: 154 */     s.setColumnView(4, 20);
/*  112: 155 */     s.setColumnView(5, 20);
/*  113: 156 */     s.setColumnView(6, 20);
/*  114:     */     
/*  115:     */ 
/*  116: 159 */     Label l = new Label(0, 0, "+/- Pi - default format", wrappedText);
/*  117: 160 */     s.addCell(l);
/*  118:     */     
/*  119: 162 */     Number n = new Number(1, 0, 3.1415926535D);
/*  120: 163 */     s.addCell(n);
/*  121:     */     
/*  122: 165 */     n = new Number(2, 0, -3.1415926535D);
/*  123: 166 */     s.addCell(n);
/*  124:     */     
/*  125: 168 */     l = new Label(0, 1, "+/- Pi - integer format", wrappedText);
/*  126: 169 */     s.addCell(l);
/*  127:     */     
/*  128: 171 */     WritableCellFormat cf1 = new WritableCellFormat(NumberFormats.INTEGER);
/*  129: 172 */     n = new Number(1, 1, 3.1415926535D, cf1);
/*  130: 173 */     s.addCell(n);
/*  131:     */     
/*  132: 175 */     n = new Number(2, 1, -3.1415926535D, cf1);
/*  133: 176 */     s.addCell(n);
/*  134:     */     
/*  135: 178 */     l = new Label(0, 2, "+/- Pi - float 2dps", wrappedText);
/*  136: 179 */     s.addCell(l);
/*  137:     */     
/*  138: 181 */     WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
/*  139: 182 */     n = new Number(1, 2, 3.1415926535D, cf2);
/*  140: 183 */     s.addCell(n);
/*  141:     */     
/*  142: 185 */     n = new Number(2, 2, -3.1415926535D, cf2);
/*  143: 186 */     s.addCell(n);
/*  144:     */     
/*  145: 188 */     l = new Label(0, 3, "+/- Pi - custom 3dps", 
/*  146: 189 */       wrappedText);
/*  147: 190 */     s.addCell(l);
/*  148:     */     
/*  149: 192 */     NumberFormat dp3 = new NumberFormat("#.###");
/*  150: 193 */     WritableCellFormat dp3cell = new WritableCellFormat(dp3);
/*  151: 194 */     n = new Number(1, 3, 3.1415926535D, dp3cell);
/*  152: 195 */     s.addCell(n);
/*  153:     */     
/*  154: 197 */     n = new Number(2, 3, -3.1415926535D, dp3cell);
/*  155: 198 */     s.addCell(n);
/*  156:     */     
/*  157: 200 */     l = new Label(0, 4, "+/- Pi - custom &3.14", 
/*  158: 201 */       wrappedText);
/*  159: 202 */     s.addCell(l);
/*  160:     */     
/*  161: 204 */     NumberFormat pounddp2 = new NumberFormat("&#.00");
/*  162: 205 */     WritableCellFormat pounddp2cell = new WritableCellFormat(pounddp2);
/*  163: 206 */     n = new Number(1, 4, 3.1415926535D, pounddp2cell);
/*  164: 207 */     s.addCell(n);
/*  165:     */     
/*  166: 209 */     n = new Number(2, 4, -3.1415926535D, pounddp2cell);
/*  167: 210 */     s.addCell(n);
/*  168:     */     
/*  169: 212 */     l = new Label(0, 5, "+/- Pi - custom Text #.### Text", 
/*  170: 213 */       wrappedText);
/*  171: 214 */     s.addCell(l);
/*  172:     */     
/*  173: 216 */     NumberFormat textdp4 = new NumberFormat("Text#.####Text");
/*  174: 217 */     WritableCellFormat textdp4cell = new WritableCellFormat(textdp4);
/*  175: 218 */     n = new Number(1, 5, 3.1415926535D, textdp4cell);
/*  176: 219 */     s.addCell(n);
/*  177:     */     
/*  178: 221 */     n = new Number(2, 5, -3.1415926535D, textdp4cell);
/*  179: 222 */     s.addCell(n);
/*  180:     */     
/*  181:     */ 
/*  182: 225 */     l = new Label(4, 0, "+/- Bilko default format");
/*  183: 226 */     s.addCell(l);
/*  184: 227 */     n = new Number(5, 0, 15042699.0D);
/*  185: 228 */     s.addCell(n);
/*  186: 229 */     n = new Number(6, 0, -15042699.0D);
/*  187: 230 */     s.addCell(n);
/*  188:     */     
/*  189: 232 */     l = new Label(4, 1, "+/- Bilko float format");
/*  190: 233 */     s.addCell(l);
/*  191: 234 */     WritableCellFormat cfi1 = new WritableCellFormat(NumberFormats.FLOAT);
/*  192: 235 */     n = new Number(5, 1, 15042699.0D, cfi1);
/*  193: 236 */     s.addCell(n);
/*  194: 237 */     n = new Number(6, 1, -15042699.0D, cfi1);
/*  195: 238 */     s.addCell(n);
/*  196:     */     
/*  197: 240 */     l = new Label(4, 2, "+/- Thousands separator");
/*  198: 241 */     s.addCell(l);
/*  199: 242 */     WritableCellFormat cfi2 = new WritableCellFormat(
/*  200: 243 */       NumberFormats.THOUSANDS_INTEGER);
/*  201: 244 */     n = new Number(5, 2, 15042699.0D, cfi2);
/*  202: 245 */     s.addCell(n);
/*  203: 246 */     n = new Number(6, 2, -15042699.0D, cfi2);
/*  204: 247 */     s.addCell(n);
/*  205:     */     
/*  206: 249 */     l = new Label(4, 3, "+/- Accounting red - added 0.01");
/*  207: 250 */     s.addCell(l);
/*  208: 251 */     WritableCellFormat cfi3 = new WritableCellFormat(
/*  209: 252 */       NumberFormats.ACCOUNTING_RED_FLOAT);
/*  210: 253 */     n = new Number(5, 3, 15042699.01D, cfi3);
/*  211: 254 */     s.addCell(n);
/*  212: 255 */     n = new Number(6, 3, -15042699.01D, cfi3);
/*  213: 256 */     s.addCell(n);
/*  214:     */     
/*  215: 258 */     l = new Label(4, 4, "+/- Percent");
/*  216: 259 */     s.addCell(l);
/*  217: 260 */     WritableCellFormat cfi4 = new WritableCellFormat(
/*  218: 261 */       NumberFormats.PERCENT_INTEGER);
/*  219: 262 */     n = new Number(5, 4, 15042699.0D, cfi4);
/*  220: 263 */     s.addCell(n);
/*  221: 264 */     n = new Number(6, 4, -15042699.0D, cfi4);
/*  222: 265 */     s.addCell(n);
/*  223:     */     
/*  224: 267 */     l = new Label(4, 5, "+/- Exponential - 2dps");
/*  225: 268 */     s.addCell(l);
/*  226: 269 */     WritableCellFormat cfi5 = new WritableCellFormat(
/*  227: 270 */       NumberFormats.EXPONENTIAL);
/*  228: 271 */     n = new Number(5, 5, 15042699.0D, cfi5);
/*  229: 272 */     s.addCell(n);
/*  230: 273 */     n = new Number(6, 5, -15042699.0D, cfi5);
/*  231: 274 */     s.addCell(n);
/*  232:     */     
/*  233: 276 */     l = new Label(4, 6, "+/- Custom exponentional - 3dps", wrappedText);
/*  234: 277 */     s.addCell(l);
/*  235: 278 */     NumberFormat edp3 = new NumberFormat("0.000E0");
/*  236: 279 */     WritableCellFormat edp3Cell = new WritableCellFormat(edp3);
/*  237: 280 */     n = new Number(5, 6, 15042699.0D, edp3Cell);
/*  238: 281 */     s.addCell(n);
/*  239: 282 */     n = new Number(6, 6, -15042699.0D, edp3Cell);
/*  240: 283 */     s.addCell(n);
/*  241:     */     
/*  242: 285 */     l = new Label(4, 7, "Custom neg brackets", wrappedText);
/*  243: 286 */     s.addCell(l);
/*  244: 287 */     NumberFormat negbracks = new NumberFormat("#,##0;(#,##0)");
/*  245: 288 */     WritableCellFormat negbrackscell = new WritableCellFormat(negbracks);
/*  246: 289 */     n = new Number(5, 7, 15042699.0D, negbrackscell);
/*  247: 290 */     s.addCell(n);
/*  248: 291 */     n = new Number(6, 7, -15042699.0D, negbrackscell);
/*  249: 292 */     s.addCell(n);
/*  250:     */     
/*  251: 294 */     l = new Label(4, 8, "Custom neg brackets 2", wrappedText);
/*  252: 295 */     s.addCell(l);
/*  253: 296 */     NumberFormat negbracks2 = new NumberFormat("#,##0;(#,##0)a");
/*  254: 297 */     WritableCellFormat negbrackscell2 = new WritableCellFormat(negbracks2);
/*  255: 298 */     n = new Number(5, 8, 15042699.0D, negbrackscell2);
/*  256: 299 */     s.addCell(n);
/*  257: 300 */     n = new Number(6, 8, -15042699.0D, negbrackscell2);
/*  258: 301 */     s.addCell(n);
/*  259:     */     
/*  260: 303 */     l = new Label(4, 9, "Custom percent", wrappedText);
/*  261: 304 */     s.addCell(l);
/*  262: 305 */     NumberFormat cuspercent = new NumberFormat("0.0%");
/*  263: 306 */     WritableCellFormat cuspercentf = new WritableCellFormat(cuspercent);
/*  264: 307 */     n = new Number(5, 9, 3.14159265D, cuspercentf);
/*  265: 308 */     s.addCell(n);
/*  266:     */     
/*  267:     */ 
/*  268:     */ 
/*  269: 312 */     l = new Label(0, 10, "Boolean - TRUE");
/*  270: 313 */     s.addCell(l);
/*  271: 314 */     Boolean b = new Boolean(1, 10, true);
/*  272: 315 */     s.addCell(b);
/*  273:     */     
/*  274: 317 */     l = new Label(0, 11, "Boolean - FALSE");
/*  275: 318 */     s.addCell(l);
/*  276: 319 */     b = new Boolean(1, 11, false);
/*  277: 320 */     s.addCell(b);
/*  278:     */     
/*  279: 322 */     l = new Label(0, 12, "A hidden cell->");
/*  280: 323 */     s.addCell(l);
/*  281: 324 */     n = new Number(1, 12, 17.0D, WritableWorkbook.HIDDEN_STYLE);
/*  282: 325 */     s.addCell(n);
/*  283:     */     
/*  284:     */ 
/*  285: 328 */     l = new Label(4, 19, "Currency formats");
/*  286: 329 */     s.addCell(l);
/*  287:     */     
/*  288: 331 */     l = new Label(4, 21, "UK Pound");
/*  289: 332 */     s.addCell(l);
/*  290: 333 */     NumberFormat poundCurrency = 
/*  291: 334 */       new NumberFormat("� #,###.00", 
/*  292: 335 */       NumberFormat.COMPLEX_FORMAT);
/*  293: 336 */     WritableCellFormat poundFormat = new WritableCellFormat(poundCurrency);
/*  294: 337 */     n = new Number(5, 21, 12345.0D, poundFormat);
/*  295: 338 */     s.addCell(n);
/*  296:     */     
/*  297: 340 */     l = new Label(4, 22, "Euro 1");
/*  298: 341 */     s.addCell(l);
/*  299: 342 */     NumberFormat euroPrefixCurrency = 
/*  300: 343 */       new NumberFormat("[$�-2] #,###.00", 
/*  301: 344 */       NumberFormat.COMPLEX_FORMAT);
/*  302: 345 */     WritableCellFormat euroPrefixFormat = 
/*  303: 346 */       new WritableCellFormat(euroPrefixCurrency);
/*  304: 347 */     n = new Number(5, 22, 12345.0D, euroPrefixFormat);
/*  305: 348 */     s.addCell(n);
/*  306:     */     
/*  307: 350 */     l = new Label(4, 23, "Euro 2");
/*  308: 351 */     s.addCell(l);
/*  309: 352 */     NumberFormat euroSuffixCurrency = 
/*  310: 353 */       new NumberFormat("#,###.00[$�-1]", 
/*  311: 354 */       NumberFormat.COMPLEX_FORMAT);
/*  312: 355 */     WritableCellFormat euroSuffixFormat = 
/*  313: 356 */       new WritableCellFormat(euroSuffixCurrency);
/*  314: 357 */     n = new Number(5, 23, 12345.0D, euroSuffixFormat);
/*  315: 358 */     s.addCell(n);
/*  316:     */     
/*  317: 360 */     l = new Label(4, 24, "Dollar");
/*  318: 361 */     s.addCell(l);
/*  319: 362 */     NumberFormat dollarCurrency = 
/*  320: 363 */       new NumberFormat("[$$-409] #,###.00", 
/*  321: 364 */       NumberFormat.COMPLEX_FORMAT);
/*  322: 365 */     WritableCellFormat dollarFormat = 
/*  323: 366 */       new WritableCellFormat(dollarCurrency);
/*  324: 367 */     n = new Number(5, 24, 12345.0D, dollarFormat);
/*  325: 368 */     s.addCell(n);
/*  326:     */     
/*  327: 370 */     l = new Label(4, 25, "Japanese Yen");
/*  328: 371 */     s.addCell(l);
/*  329: 372 */     NumberFormat japaneseYenCurrency = 
/*  330: 373 */       new NumberFormat("[$�-411] #,###.00", 
/*  331: 374 */       NumberFormat.COMPLEX_FORMAT);
/*  332: 375 */     WritableCellFormat japaneseYenFormat = 
/*  333: 376 */       new WritableCellFormat(japaneseYenCurrency);
/*  334: 377 */     n = new Number(5, 25, 12345.0D, japaneseYenFormat);
/*  335: 378 */     s.addCell(n);
/*  336:     */     
/*  337: 380 */     l = new Label(4, 30, "Fraction formats");
/*  338: 381 */     s.addCell(l);
/*  339:     */     
/*  340: 383 */     l = new Label(4, 32, "One digit fraction format", wrappedText);
/*  341: 384 */     s.addCell(l);
/*  342:     */     
/*  343: 386 */     WritableCellFormat fraction1digitformat = 
/*  344: 387 */       new WritableCellFormat(NumberFormats.FRACTION_ONE_DIGIT);
/*  345: 388 */     n = new Number(5, 32, 3.18279D, fraction1digitformat);
/*  346: 389 */     s.addCell(n);
/*  347:     */     
/*  348: 391 */     l = new Label(4, 33, "Two digit fraction format", wrappedText);
/*  349: 392 */     s.addCell(l);
/*  350:     */     
/*  351: 394 */     WritableCellFormat fraction2digitformat = 
/*  352: 395 */       new WritableCellFormat(NumberFormats.FRACTION_TWO_DIGITS);
/*  353: 396 */     n = new Number(5, 33, 3.18279D, fraction2digitformat);
/*  354: 397 */     s.addCell(n);
/*  355:     */     
/*  356: 399 */     l = new Label(4, 34, "Three digit fraction format (improper)", wrappedText);
/*  357: 400 */     s.addCell(l);
/*  358:     */     
/*  359: 402 */     NumberFormat fraction3digit1 = 
/*  360: 403 */       new NumberFormat("???/???", 
/*  361: 404 */       NumberFormat.COMPLEX_FORMAT);
/*  362: 405 */     WritableCellFormat fraction3digitformat1 = 
/*  363: 406 */       new WritableCellFormat(fraction3digit1);
/*  364: 407 */     n = new Number(5, 34, 3.18927D, fraction3digitformat1);
/*  365: 408 */     s.addCell(n);
/*  366:     */     
/*  367: 410 */     l = new Label(4, 35, "Three digit fraction format (proper)", wrappedText);
/*  368: 411 */     s.addCell(l);
/*  369:     */     
/*  370: 413 */     NumberFormat fraction3digit2 = 
/*  371: 414 */       new NumberFormat("# ???/???", 
/*  372: 415 */       NumberFormat.COMPLEX_FORMAT);
/*  373: 416 */     WritableCellFormat fraction3digitformat2 = 
/*  374: 417 */       new WritableCellFormat(fraction3digit2);
/*  375: 418 */     n = new Number(5, 35, 3.18927D, fraction3digitformat2);
/*  376: 419 */     s.addCell(n);
/*  377: 422 */     for (int row = 0; row < 100; row++) {
/*  378: 424 */       for (int col = 8; col < 108; col++)
/*  379:     */       {
/*  380: 426 */         n = new Number(col, row, col + row);
/*  381: 427 */         s.addCell(n);
/*  382:     */       }
/*  383:     */     }
/*  384: 432 */     for (int row = 101; row < 3000; row++) {
/*  385: 434 */       for (int col = 0; col < 25; col++)
/*  386:     */       {
/*  387: 436 */         n = new Number(col, row, col + row);
/*  388: 437 */         s.addCell(n);
/*  389:     */       }
/*  390:     */     }
/*  391:     */   }
/*  392:     */   
/*  393:     */   private void writeDateFormatSheet(WritableSheet s)
/*  394:     */     throws WriteException
/*  395:     */   {
/*  396: 449 */     WritableCellFormat wrappedText = new WritableCellFormat(
/*  397: 450 */       WritableWorkbook.ARIAL_10_PT);
/*  398: 451 */     wrappedText.setWrap(true);
/*  399:     */     
/*  400: 453 */     s.setColumnView(0, 20);
/*  401: 454 */     s.setColumnView(2, 20);
/*  402: 455 */     s.setColumnView(3, 20);
/*  403: 456 */     s.setColumnView(4, 20);
/*  404:     */     
/*  405: 458 */     s.getSettings().setFitWidth(2);
/*  406: 459 */     s.getSettings().setFitHeight(2);
/*  407:     */     
/*  408: 461 */     Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*  409: 462 */     c.set(1975, 4, 31, 15, 21, 45);
/*  410: 463 */     c.set(14, 660);
/*  411: 464 */     Date date = c.getTime();
/*  412: 465 */     c.set(1900, 0, 1, 0, 0, 0);
/*  413: 466 */     c.set(14, 0);
/*  414:     */     
/*  415: 468 */     Date date2 = c.getTime();
/*  416: 469 */     c.set(1970, 0, 1, 0, 0, 0);
/*  417: 470 */     Date date3 = c.getTime();
/*  418: 471 */     c.set(1918, 10, 11, 11, 0, 0);
/*  419: 472 */     Date date4 = c.getTime();
/*  420: 473 */     c.set(1900, 0, 2, 0, 0, 0);
/*  421: 474 */     Date date5 = c.getTime();
/*  422: 475 */     c.set(1901, 0, 1, 0, 0, 0);
/*  423: 476 */     Date date6 = c.getTime();
/*  424: 477 */     c.set(1900, 4, 31, 0, 0, 0);
/*  425: 478 */     Date date7 = c.getTime();
/*  426: 479 */     c.set(1900, 1, 1, 0, 0, 0);
/*  427: 480 */     Date date8 = c.getTime();
/*  428: 481 */     c.set(1900, 0, 31, 0, 0, 0);
/*  429: 482 */     Date date9 = c.getTime();
/*  430: 483 */     c.set(1900, 2, 1, 0, 0, 0);
/*  431: 484 */     Date date10 = c.getTime();
/*  432: 485 */     c.set(1900, 1, 27, 0, 0, 0);
/*  433: 486 */     Date date11 = c.getTime();
/*  434: 487 */     c.set(1900, 1, 28, 0, 0, 0);
/*  435: 488 */     Date date12 = c.getTime();
/*  436: 489 */     c.set(1980, 5, 31, 12, 0, 0);
/*  437: 490 */     Date date13 = c.getTime();
/*  438: 491 */     c.set(1066, 9, 14, 0, 0, 0);
/*  439: 492 */     Date date14 = c.getTime();
/*  440:     */     
/*  441:     */ 
/*  442: 495 */     SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");
/*  443: 496 */     sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
/*  444: 497 */     Label l = new Label(0, 0, "All dates are " + sdf.format(date), 
/*  445: 498 */       wrappedText);
/*  446: 499 */     s.addCell(l);
/*  447:     */     
/*  448: 501 */     l = new Label(0, 1, "Built in formats", 
/*  449: 502 */       wrappedText);
/*  450: 503 */     s.addCell(l);
/*  451:     */     
/*  452: 505 */     l = new Label(2, 1, "Custom formats");
/*  453: 506 */     s.addCell(l);
/*  454:     */     
/*  455: 508 */     WritableCellFormat cf1 = new WritableCellFormat(DateFormats.FORMAT1);
/*  456: 509 */     DateTime dt = new DateTime(0, 2, date, cf1, DateTime.GMT);
/*  457: 510 */     s.addCell(dt);
/*  458:     */     
/*  459: 512 */     cf1 = new WritableCellFormat(DateFormats.FORMAT2);
/*  460: 513 */     dt = new DateTime(0, 3, date, cf1, DateTime.GMT);
/*  461: 514 */     s.addCell(dt);
/*  462:     */     
/*  463: 516 */     cf1 = new WritableCellFormat(DateFormats.FORMAT3);
/*  464: 517 */     dt = new DateTime(0, 4, date, cf1);
/*  465: 518 */     s.addCell(dt);
/*  466:     */     
/*  467: 520 */     cf1 = new WritableCellFormat(DateFormats.FORMAT4);
/*  468: 521 */     dt = new DateTime(0, 5, date, cf1);
/*  469: 522 */     s.addCell(dt);
/*  470:     */     
/*  471: 524 */     cf1 = new WritableCellFormat(DateFormats.FORMAT5);
/*  472: 525 */     dt = new DateTime(0, 6, date, cf1);
/*  473: 526 */     s.addCell(dt);
/*  474:     */     
/*  475: 528 */     cf1 = new WritableCellFormat(DateFormats.FORMAT6);
/*  476: 529 */     dt = new DateTime(0, 7, date, cf1);
/*  477: 530 */     s.addCell(dt);
/*  478:     */     
/*  479: 532 */     cf1 = new WritableCellFormat(DateFormats.FORMAT7);
/*  480: 533 */     dt = new DateTime(0, 8, date, cf1, DateTime.GMT);
/*  481: 534 */     s.addCell(dt);
/*  482:     */     
/*  483: 536 */     cf1 = new WritableCellFormat(DateFormats.FORMAT8);
/*  484: 537 */     dt = new DateTime(0, 9, date, cf1, DateTime.GMT);
/*  485: 538 */     s.addCell(dt);
/*  486:     */     
/*  487: 540 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  488: 541 */     dt = new DateTime(0, 10, date, cf1, DateTime.GMT);
/*  489: 542 */     s.addCell(dt);
/*  490:     */     
/*  491: 544 */     cf1 = new WritableCellFormat(DateFormats.FORMAT10);
/*  492: 545 */     dt = new DateTime(0, 11, date, cf1, DateTime.GMT);
/*  493: 546 */     s.addCell(dt);
/*  494:     */     
/*  495: 548 */     cf1 = new WritableCellFormat(DateFormats.FORMAT11);
/*  496: 549 */     dt = new DateTime(0, 12, date, cf1, DateTime.GMT);
/*  497: 550 */     s.addCell(dt);
/*  498:     */     
/*  499: 552 */     cf1 = new WritableCellFormat(DateFormats.FORMAT12);
/*  500: 553 */     dt = new DateTime(0, 13, date, cf1, DateTime.GMT);
/*  501: 554 */     s.addCell(dt);
/*  502:     */     
/*  503:     */ 
/*  504: 557 */     DateFormat df = new DateFormat("dd MM yyyy");
/*  505: 558 */     cf1 = new WritableCellFormat(df);
/*  506: 559 */     l = new Label(2, 2, "dd MM yyyy");
/*  507: 560 */     s.addCell(l);
/*  508:     */     
/*  509: 562 */     dt = new DateTime(3, 2, date, cf1, DateTime.GMT);
/*  510: 563 */     s.addCell(dt);
/*  511:     */     
/*  512: 565 */     df = new DateFormat("dd MMM yyyy");
/*  513: 566 */     cf1 = new WritableCellFormat(df);
/*  514: 567 */     l = new Label(2, 3, "dd MMM yyyy");
/*  515: 568 */     s.addCell(l);
/*  516:     */     
/*  517: 570 */     dt = new DateTime(3, 3, date, cf1, DateTime.GMT);
/*  518: 571 */     s.addCell(dt);
/*  519:     */     
/*  520: 573 */     df = new DateFormat("hh:mm");
/*  521: 574 */     cf1 = new WritableCellFormat(df);
/*  522: 575 */     l = new Label(2, 4, "hh:mm");
/*  523: 576 */     s.addCell(l);
/*  524:     */     
/*  525: 578 */     dt = new DateTime(3, 4, date, cf1, DateTime.GMT);
/*  526: 579 */     s.addCell(dt);
/*  527:     */     
/*  528: 581 */     df = new DateFormat("hh:mm:ss");
/*  529: 582 */     cf1 = new WritableCellFormat(df);
/*  530: 583 */     l = new Label(2, 5, "hh:mm:ss");
/*  531: 584 */     s.addCell(l);
/*  532:     */     
/*  533: 586 */     dt = new DateTime(3, 5, date, cf1, DateTime.GMT);
/*  534: 587 */     s.addCell(dt);
/*  535:     */     
/*  536: 589 */     df = new DateFormat("H:mm:ss a");
/*  537: 590 */     cf1 = new WritableCellFormat(df);
/*  538: 591 */     l = new Label(2, 5, "H:mm:ss a");
/*  539: 592 */     s.addCell(l);
/*  540:     */     
/*  541: 594 */     dt = new DateTime(3, 5, date, cf1, DateTime.GMT);
/*  542: 595 */     s.addCell(dt);
/*  543: 596 */     dt = new DateTime(4, 5, date13, cf1, DateTime.GMT);
/*  544: 597 */     s.addCell(dt);
/*  545:     */     
/*  546: 599 */     df = new DateFormat("mm:ss.SSS");
/*  547: 600 */     cf1 = new WritableCellFormat(df);
/*  548: 601 */     l = new Label(2, 6, "mm:ss.SSS");
/*  549: 602 */     s.addCell(l);
/*  550:     */     
/*  551: 604 */     dt = new DateTime(3, 6, date, cf1, DateTime.GMT);
/*  552: 605 */     s.addCell(dt);
/*  553:     */     
/*  554: 607 */     df = new DateFormat("hh:mm:ss a");
/*  555: 608 */     cf1 = new WritableCellFormat(df);
/*  556: 609 */     l = new Label(2, 7, "hh:mm:ss a");
/*  557: 610 */     s.addCell(l);
/*  558:     */     
/*  559: 612 */     dt = new DateTime(4, 7, date13, cf1, DateTime.GMT);
/*  560: 613 */     s.addCell(dt);
/*  561:     */     
/*  562:     */ 
/*  563:     */ 
/*  564: 617 */     l = new Label(0, 16, "Zero date " + sdf.format(date2), 
/*  565: 618 */       wrappedText);
/*  566: 619 */     s.addCell(l);
/*  567:     */     
/*  568: 621 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  569: 622 */     dt = new DateTime(0, 17, date2, cf1, DateTime.GMT);
/*  570: 623 */     s.addCell(dt);
/*  571:     */     
/*  572:     */ 
/*  573: 626 */     l = new Label(3, 16, "Zero date + 1 " + sdf.format(date5), 
/*  574: 627 */       wrappedText);
/*  575: 628 */     s.addCell(l);
/*  576:     */     
/*  577: 630 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  578: 631 */     dt = new DateTime(3, 17, date5, cf1, DateTime.GMT);
/*  579: 632 */     s.addCell(dt);
/*  580:     */     
/*  581:     */ 
/*  582: 635 */     l = new Label(3, 19, sdf.format(date6), 
/*  583: 636 */       wrappedText);
/*  584: 637 */     s.addCell(l);
/*  585:     */     
/*  586: 639 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  587: 640 */     dt = new DateTime(3, 20, date6, cf1, DateTime.GMT);
/*  588: 641 */     s.addCell(dt);
/*  589:     */     
/*  590:     */ 
/*  591: 644 */     l = new Label(3, 22, sdf.format(date7), 
/*  592: 645 */       wrappedText);
/*  593: 646 */     s.addCell(l);
/*  594:     */     
/*  595: 648 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  596: 649 */     dt = new DateTime(3, 23, date7, cf1, DateTime.GMT);
/*  597: 650 */     s.addCell(dt);
/*  598:     */     
/*  599:     */ 
/*  600: 653 */     l = new Label(3, 25, sdf.format(date8), 
/*  601: 654 */       wrappedText);
/*  602: 655 */     s.addCell(l);
/*  603:     */     
/*  604: 657 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  605: 658 */     dt = new DateTime(3, 26, date8, cf1, DateTime.GMT);
/*  606: 659 */     s.addCell(dt);
/*  607:     */     
/*  608:     */ 
/*  609: 662 */     l = new Label(3, 28, sdf.format(date9), 
/*  610: 663 */       wrappedText);
/*  611: 664 */     s.addCell(l);
/*  612:     */     
/*  613: 666 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  614: 667 */     dt = new DateTime(3, 29, date9, cf1, DateTime.GMT);
/*  615: 668 */     s.addCell(dt);
/*  616:     */     
/*  617:     */ 
/*  618: 671 */     l = new Label(3, 28, sdf.format(date9), 
/*  619: 672 */       wrappedText);
/*  620: 673 */     s.addCell(l);
/*  621:     */     
/*  622: 675 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  623: 676 */     dt = new DateTime(3, 29, date9, cf1, DateTime.GMT);
/*  624: 677 */     s.addCell(dt);
/*  625:     */     
/*  626:     */ 
/*  627: 680 */     l = new Label(3, 31, sdf.format(date10), 
/*  628: 681 */       wrappedText);
/*  629: 682 */     s.addCell(l);
/*  630:     */     
/*  631: 684 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  632: 685 */     dt = new DateTime(3, 32, date10, cf1, DateTime.GMT);
/*  633: 686 */     s.addCell(dt);
/*  634:     */     
/*  635:     */ 
/*  636: 689 */     l = new Label(3, 34, sdf.format(date11), 
/*  637: 690 */       wrappedText);
/*  638: 691 */     s.addCell(l);
/*  639:     */     
/*  640: 693 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  641: 694 */     dt = new DateTime(3, 35, date11, cf1, DateTime.GMT);
/*  642: 695 */     s.addCell(dt);
/*  643:     */     
/*  644:     */ 
/*  645: 698 */     l = new Label(3, 37, sdf.format(date12), 
/*  646: 699 */       wrappedText);
/*  647: 700 */     s.addCell(l);
/*  648:     */     
/*  649: 702 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  650: 703 */     dt = new DateTime(3, 38, date12, cf1, DateTime.GMT);
/*  651: 704 */     s.addCell(dt);
/*  652:     */     
/*  653:     */ 
/*  654: 707 */     l = new Label(0, 19, "Zero UTC date " + sdf.format(date3), 
/*  655: 708 */       wrappedText);
/*  656: 709 */     s.addCell(l);
/*  657:     */     
/*  658: 711 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  659: 712 */     dt = new DateTime(0, 20, date3, cf1, DateTime.GMT);
/*  660: 713 */     s.addCell(dt);
/*  661:     */     
/*  662:     */ 
/*  663: 716 */     l = new Label(0, 22, "Armistice date " + sdf.format(date4), 
/*  664: 717 */       wrappedText);
/*  665: 718 */     s.addCell(l);
/*  666:     */     
/*  667: 720 */     cf1 = new WritableCellFormat(DateFormats.FORMAT9);
/*  668: 721 */     dt = new DateTime(0, 23, date4, cf1, DateTime.GMT);
/*  669: 722 */     s.addCell(dt);
/*  670:     */     
/*  671:     */ 
/*  672: 725 */     l = new Label(0, 25, "Battle of Hastings " + sdf.format(date14), 
/*  673: 726 */       wrappedText);
/*  674: 727 */     s.addCell(l);
/*  675:     */     
/*  676: 729 */     cf1 = new WritableCellFormat(DateFormats.FORMAT2);
/*  677: 730 */     dt = new DateTime(0, 26, date14, cf1, DateTime.GMT);
/*  678: 731 */     s.addCell(dt);
/*  679:     */   }
/*  680:     */   
/*  681:     */   private void writeLabelFormatSheet(WritableSheet s1)
/*  682:     */     throws WriteException
/*  683:     */   {
/*  684: 742 */     s1.setColumnView(0, 60);
/*  685:     */     
/*  686: 744 */     Label lr = new Label(0, 0, "Arial Fonts");
/*  687: 745 */     s1.addCell(lr);
/*  688:     */     
/*  689: 747 */     lr = new Label(1, 0, "10pt");
/*  690: 748 */     s1.addCell(lr);
/*  691:     */     
/*  692: 750 */     lr = new Label(2, 0, "Normal");
/*  693: 751 */     s1.addCell(lr);
/*  694:     */     
/*  695: 753 */     lr = new Label(3, 0, "12pt");
/*  696: 754 */     s1.addCell(lr);
/*  697:     */     
/*  698: 756 */     WritableFont arial12pt = new WritableFont(WritableFont.ARIAL, 12);
/*  699: 757 */     WritableCellFormat arial12format = new WritableCellFormat(arial12pt);
/*  700: 758 */     arial12format.setWrap(true);
/*  701: 759 */     lr = new Label(4, 0, "Normal", arial12format);
/*  702: 760 */     s1.addCell(lr);
/*  703:     */     
/*  704: 762 */     WritableFont arial10ptBold = new WritableFont(
/*  705: 763 */       WritableFont.ARIAL, 10, WritableFont.BOLD);
/*  706: 764 */     WritableCellFormat arial10BoldFormat = new WritableCellFormat(
/*  707: 765 */       arial10ptBold);
/*  708: 766 */     lr = new Label(2, 2, "BOLD", arial10BoldFormat);
/*  709: 767 */     s1.addCell(lr);
/*  710:     */     
/*  711: 769 */     WritableFont arial12ptBold = new WritableFont(
/*  712: 770 */       WritableFont.ARIAL, 12, WritableFont.BOLD);
/*  713: 771 */     WritableCellFormat arial12BoldFormat = new WritableCellFormat(
/*  714: 772 */       arial12ptBold);
/*  715: 773 */     lr = new Label(4, 2, "BOLD", arial12BoldFormat);
/*  716: 774 */     s1.addCell(lr);
/*  717:     */     
/*  718: 776 */     WritableFont arial10ptItalic = new WritableFont(
/*  719: 777 */       WritableFont.ARIAL, 10, WritableFont.NO_BOLD, true);
/*  720: 778 */     WritableCellFormat arial10ItalicFormat = new WritableCellFormat(
/*  721: 779 */       arial10ptItalic);
/*  722: 780 */     lr = new Label(2, 4, "Italic", arial10ItalicFormat);
/*  723: 781 */     s1.addCell(lr);
/*  724:     */     
/*  725: 783 */     WritableFont arial12ptItalic = new WritableFont(
/*  726: 784 */       WritableFont.ARIAL, 12, WritableFont.NO_BOLD, true);
/*  727: 785 */     WritableCellFormat arial12ptItalicFormat = new WritableCellFormat(
/*  728: 786 */       arial12ptItalic);
/*  729: 787 */     lr = new Label(4, 4, "Italic", arial12ptItalicFormat);
/*  730: 788 */     s1.addCell(lr);
/*  731:     */     
/*  732: 790 */     WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
/*  733: 791 */     WritableCellFormat times10format = new WritableCellFormat(times10pt);
/*  734: 792 */     lr = new Label(0, 7, "Times Fonts", times10format);
/*  735: 793 */     s1.addCell(lr);
/*  736:     */     
/*  737: 795 */     lr = new Label(1, 7, "10pt", times10format);
/*  738: 796 */     s1.addCell(lr);
/*  739:     */     
/*  740: 798 */     lr = new Label(2, 7, "Normal", times10format);
/*  741: 799 */     s1.addCell(lr);
/*  742:     */     
/*  743: 801 */     lr = new Label(3, 7, "12pt", times10format);
/*  744: 802 */     s1.addCell(lr);
/*  745:     */     
/*  746: 804 */     WritableFont times12pt = new WritableFont(WritableFont.TIMES, 12);
/*  747: 805 */     WritableCellFormat times12format = new WritableCellFormat(times12pt);
/*  748: 806 */     lr = new Label(4, 7, "Normal", times12format);
/*  749: 807 */     s1.addCell(lr);
/*  750:     */     
/*  751: 809 */     WritableFont times10ptBold = new WritableFont(
/*  752: 810 */       WritableFont.TIMES, 10, WritableFont.BOLD);
/*  753: 811 */     WritableCellFormat times10BoldFormat = new WritableCellFormat(
/*  754: 812 */       times10ptBold);
/*  755: 813 */     lr = new Label(2, 9, "BOLD", times10BoldFormat);
/*  756: 814 */     s1.addCell(lr);
/*  757:     */     
/*  758: 816 */     WritableFont times12ptBold = new WritableFont(
/*  759: 817 */       WritableFont.TIMES, 12, WritableFont.BOLD);
/*  760: 818 */     WritableCellFormat times12BoldFormat = new WritableCellFormat(
/*  761: 819 */       times12ptBold);
/*  762: 820 */     lr = new Label(4, 9, "BOLD", times12BoldFormat);
/*  763: 821 */     s1.addCell(lr);
/*  764:     */     
/*  765:     */ 
/*  766: 824 */     s1.setColumnView(6, 22);
/*  767: 825 */     s1.setColumnView(7, 22);
/*  768: 826 */     s1.setColumnView(8, 22);
/*  769: 827 */     s1.setColumnView(9, 22);
/*  770:     */     
/*  771: 829 */     lr = new Label(0, 11, "Underlining");
/*  772: 830 */     s1.addCell(lr);
/*  773:     */     
/*  774: 832 */     WritableFont arial10ptUnderline = new WritableFont(
/*  775: 833 */       WritableFont.ARIAL, 
/*  776: 834 */       10, 
/*  777: 835 */       WritableFont.NO_BOLD, 
/*  778: 836 */       false, 
/*  779: 837 */       UnderlineStyle.SINGLE);
/*  780: 838 */     WritableCellFormat arialUnderline = new WritableCellFormat(
/*  781: 839 */       arial10ptUnderline);
/*  782: 840 */     lr = new Label(6, 11, "Underline", arialUnderline);
/*  783: 841 */     s1.addCell(lr);
/*  784:     */     
/*  785: 843 */     WritableFont arial10ptDoubleUnderline = new WritableFont(
/*  786: 844 */       WritableFont.ARIAL, 
/*  787: 845 */       10, 
/*  788: 846 */       WritableFont.NO_BOLD, 
/*  789: 847 */       false, 
/*  790: 848 */       UnderlineStyle.DOUBLE);
/*  791: 849 */     WritableCellFormat arialDoubleUnderline = new WritableCellFormat(
/*  792: 850 */       arial10ptDoubleUnderline);
/*  793: 851 */     lr = new Label(7, 11, "Double Underline", arialDoubleUnderline);
/*  794: 852 */     s1.addCell(lr);
/*  795:     */     
/*  796: 854 */     WritableFont arial10ptSingleAcc = new WritableFont(
/*  797: 855 */       WritableFont.ARIAL, 
/*  798: 856 */       10, 
/*  799: 857 */       WritableFont.NO_BOLD, 
/*  800: 858 */       false, 
/*  801: 859 */       UnderlineStyle.SINGLE_ACCOUNTING);
/*  802: 860 */     WritableCellFormat arialSingleAcc = new WritableCellFormat(
/*  803: 861 */       arial10ptSingleAcc);
/*  804: 862 */     lr = new Label(8, 11, "Single Accounting Underline", arialSingleAcc);
/*  805: 863 */     s1.addCell(lr);
/*  806:     */     
/*  807: 865 */     WritableFont arial10ptDoubleAcc = new WritableFont(
/*  808: 866 */       WritableFont.ARIAL, 
/*  809: 867 */       10, 
/*  810: 868 */       WritableFont.NO_BOLD, 
/*  811: 869 */       false, 
/*  812: 870 */       UnderlineStyle.DOUBLE_ACCOUNTING);
/*  813: 871 */     WritableCellFormat arialDoubleAcc = new WritableCellFormat(
/*  814: 872 */       arial10ptDoubleAcc);
/*  815: 873 */     lr = new Label(9, 11, "Double Accounting Underline", arialDoubleAcc);
/*  816: 874 */     s1.addCell(lr);
/*  817:     */     
/*  818: 876 */     WritableFont times14ptBoldUnderline = new WritableFont(
/*  819: 877 */       WritableFont.TIMES, 
/*  820: 878 */       14, 
/*  821: 879 */       WritableFont.BOLD, 
/*  822: 880 */       false, 
/*  823: 881 */       UnderlineStyle.SINGLE);
/*  824: 882 */     WritableCellFormat timesBoldUnderline = new WritableCellFormat(
/*  825: 883 */       times14ptBoldUnderline);
/*  826: 884 */     lr = new Label(6, 12, "Times 14 Bold Underline", timesBoldUnderline);
/*  827: 885 */     s1.addCell(lr);
/*  828:     */     
/*  829: 887 */     WritableFont arial18ptBoldItalicUnderline = new WritableFont(
/*  830: 888 */       WritableFont.ARIAL, 
/*  831: 889 */       18, 
/*  832: 890 */       WritableFont.BOLD, 
/*  833: 891 */       true, 
/*  834: 892 */       UnderlineStyle.SINGLE);
/*  835: 893 */     WritableCellFormat arialBoldItalicUnderline = new WritableCellFormat(
/*  836: 894 */       arial18ptBoldItalicUnderline);
/*  837: 895 */     lr = new Label(6, 13, "Arial 18 Bold Italic Underline", 
/*  838: 896 */       arialBoldItalicUnderline);
/*  839: 897 */     s1.addCell(lr);
/*  840:     */     
/*  841: 899 */     lr = new Label(0, 15, "Script styles");
/*  842: 900 */     s1.addCell(lr);
/*  843:     */     
/*  844: 902 */     WritableFont superscript = new WritableFont(
/*  845: 903 */       WritableFont.ARIAL, 
/*  846: 904 */       10, 
/*  847: 905 */       WritableFont.NO_BOLD, 
/*  848: 906 */       false, 
/*  849: 907 */       UnderlineStyle.NO_UNDERLINE, 
/*  850: 908 */       Colour.BLACK, 
/*  851: 909 */       ScriptStyle.SUPERSCRIPT);
/*  852: 910 */     WritableCellFormat superscriptFormat = new WritableCellFormat(
/*  853: 911 */       superscript);
/*  854: 912 */     lr = new Label(1, 15, "superscript", superscriptFormat);
/*  855: 913 */     s1.addCell(lr);
/*  856:     */     
/*  857: 915 */     WritableFont subscript = new WritableFont(
/*  858: 916 */       WritableFont.ARIAL, 
/*  859: 917 */       10, 
/*  860: 918 */       WritableFont.NO_BOLD, 
/*  861: 919 */       false, 
/*  862: 920 */       UnderlineStyle.NO_UNDERLINE, 
/*  863: 921 */       Colour.BLACK, 
/*  864: 922 */       ScriptStyle.SUBSCRIPT);
/*  865: 923 */     WritableCellFormat subscriptFormat = new WritableCellFormat(
/*  866: 924 */       subscript);
/*  867: 925 */     lr = new Label(2, 15, "subscript", subscriptFormat);
/*  868: 926 */     s1.addCell(lr);
/*  869:     */     
/*  870: 928 */     lr = new Label(0, 17, "Colours");
/*  871: 929 */     s1.addCell(lr);
/*  872:     */     
/*  873: 931 */     WritableFont red = new WritableFont(WritableFont.ARIAL, 
/*  874: 932 */       10, 
/*  875: 933 */       WritableFont.NO_BOLD, 
/*  876: 934 */       false, 
/*  877: 935 */       UnderlineStyle.NO_UNDERLINE, 
/*  878: 936 */       Colour.RED);
/*  879: 937 */     WritableCellFormat redFormat = new WritableCellFormat(red);
/*  880: 938 */     lr = new Label(2, 17, "Red", redFormat);
/*  881: 939 */     s1.addCell(lr);
/*  882:     */     
/*  883: 941 */     WritableFont blue = new WritableFont(WritableFont.ARIAL, 
/*  884: 942 */       10, 
/*  885: 943 */       WritableFont.NO_BOLD, 
/*  886: 944 */       false, 
/*  887: 945 */       UnderlineStyle.NO_UNDERLINE, 
/*  888: 946 */       Colour.BLUE);
/*  889: 947 */     WritableCellFormat blueFormat = new WritableCellFormat(blue);
/*  890: 948 */     lr = new Label(2, 18, "Blue", blueFormat);
/*  891: 949 */     s1.addCell(lr);
/*  892:     */     
/*  893: 951 */     WritableFont lime = new WritableFont(WritableFont.ARIAL);
/*  894: 952 */     lime.setColour(Colour.LIME);
/*  895: 953 */     WritableCellFormat limeFormat = new WritableCellFormat(lime);
/*  896: 954 */     limeFormat.setWrap(true);
/*  897: 955 */     lr = new Label(4, 18, "Modified palette - was lime, now red", limeFormat);
/*  898: 956 */     s1.addCell(lr);
/*  899:     */     
/*  900: 958 */     WritableCellFormat greyBackground = new WritableCellFormat();
/*  901: 959 */     greyBackground.setWrap(true);
/*  902: 960 */     greyBackground.setBackground(Colour.GRAY_50);
/*  903: 961 */     lr = new Label(2, 19, "Grey background", greyBackground);
/*  904: 962 */     s1.addCell(lr);
/*  905:     */     
/*  906: 964 */     WritableFont yellow = new WritableFont(WritableFont.ARIAL, 
/*  907: 965 */       10, 
/*  908: 966 */       WritableFont.NO_BOLD, 
/*  909: 967 */       false, 
/*  910: 968 */       UnderlineStyle.NO_UNDERLINE, 
/*  911: 969 */       Colour.YELLOW);
/*  912: 970 */     WritableCellFormat yellowOnBlue = new WritableCellFormat(yellow);
/*  913: 971 */     yellowOnBlue.setWrap(true);
/*  914: 972 */     yellowOnBlue.setBackground(Colour.BLUE);
/*  915: 973 */     lr = new Label(2, 20, "Blue background, yellow foreground", yellowOnBlue);
/*  916: 974 */     s1.addCell(lr);
/*  917:     */     
/*  918: 976 */     WritableCellFormat yellowOnBlack = new WritableCellFormat(yellow);
/*  919: 977 */     yellowOnBlack.setWrap(true);
/*  920: 978 */     yellowOnBlack.setBackground(Colour.PALETTE_BLACK);
/*  921: 979 */     lr = new Label(3, 20, "Black background, yellow foreground", 
/*  922: 980 */       yellowOnBlack);
/*  923: 981 */     s1.addCell(lr);
/*  924:     */     
/*  925: 983 */     lr = new Label(0, 22, "Null label");
/*  926: 984 */     s1.addCell(lr);
/*  927:     */     
/*  928: 986 */     lr = new Label(2, 22, null);
/*  929: 987 */     s1.addCell(lr);
/*  930:     */     
/*  931: 989 */     lr = new Label(0, 24, 
/*  932: 990 */       "A very long label, more than 255 characters\nRejoice O shores\nSing O bells\nBut I with mournful tread\nWalk the deck my captain lies\nFallen cold and dead\nSummer surprised, coming over the Starnbergersee\nWith a shower of rain. We stopped in the Colonnade\nA very long label, more than 255 characters\nRejoice O shores\nSing O bells\nBut I with mournful tread\nWalk the deck my captain lies\nFallen cold and dead\nSummer surprised, coming over the Starnbergersee\nWith a shower of rain. We stopped in the Colonnade\nA very long label, more than 255 characters\nRejoice O shores\nSing O bells\nBut I with mournful tread\nWalk the deck my captain lies\nFallen cold and dead\nSummer surprised, coming over the Starnbergersee\nWith a shower of rain. We stopped in the Colonnade\nA very long label, more than 255 characters\nRejoice O shores\nSing O bells\nBut I with mournful tread\nWalk the deck my captain lies\nFallen cold and dead\nSummer surprised, coming over the Starnbergersee\nWith a shower of rain. We stopped in the Colonnade\nAnd sat and drank coffee an talked for an hour\n", 
/*  933:     */       
/*  934:     */ 
/*  935:     */ 
/*  936:     */ 
/*  937:     */ 
/*  938:     */ 
/*  939:     */ 
/*  940:     */ 
/*  941:     */ 
/*  942:     */ 
/*  943:     */ 
/*  944:     */ 
/*  945:     */ 
/*  946:     */ 
/*  947:     */ 
/*  948:     */ 
/*  949:     */ 
/*  950:     */ 
/*  951:     */ 
/*  952:     */ 
/*  953:     */ 
/*  954:     */ 
/*  955:     */ 
/*  956:     */ 
/*  957:     */ 
/*  958:     */ 
/*  959:     */ 
/*  960:     */ 
/*  961:     */ 
/*  962:     */ 
/*  963:1021 */       arial12format);
/*  964:1022 */     s1.addCell(lr);
/*  965:     */     
/*  966:1024 */     WritableCellFormat vertical = new WritableCellFormat();
/*  967:1025 */     vertical.setOrientation(Orientation.VERTICAL);
/*  968:1026 */     lr = new Label(0, 26, "Vertical orientation", vertical);
/*  969:1027 */     s1.addCell(lr);
/*  970:     */     
/*  971:     */ 
/*  972:1030 */     WritableCellFormat plus_90 = new WritableCellFormat();
/*  973:1031 */     plus_90.setOrientation(Orientation.PLUS_90);
/*  974:1032 */     lr = new Label(1, 26, "Plus 90", plus_90);
/*  975:1033 */     s1.addCell(lr);
/*  976:     */     
/*  977:     */ 
/*  978:1036 */     WritableCellFormat minus_90 = new WritableCellFormat();
/*  979:1037 */     minus_90.setOrientation(Orientation.MINUS_90);
/*  980:1038 */     lr = new Label(2, 26, "Minus 90", minus_90);
/*  981:1039 */     s1.addCell(lr);
/*  982:     */     
/*  983:1041 */     lr = new Label(0, 28, "Modified row height");
/*  984:1042 */     s1.addCell(lr);
/*  985:1043 */     s1.setRowView(28, 480);
/*  986:     */     
/*  987:1045 */     lr = new Label(0, 29, "Collapsed row");
/*  988:1046 */     s1.addCell(lr);
/*  989:1047 */     s1.setRowView(29, true);
/*  990:     */     try
/*  991:     */     {
/*  992:1052 */       Label l = new Label(0, 30, "Hyperlink to home page");
/*  993:1053 */       s1.addCell(l);
/*  994:     */       
/*  995:1055 */       URL url = new URL("http://www.andykhan.com/jexcelapi");
/*  996:1056 */       WritableHyperlink wh = new WritableHyperlink(0, 30, 8, 31, url);
/*  997:1057 */       s1.addHyperlink(wh);
/*  998:     */       
/*  999:     */ 
/* 1000:1060 */       WritableHyperlink wh2 = new WritableHyperlink(7, 30, 9, 31, url);
/* 1001:1061 */       s1.addHyperlink(wh2);
/* 1002:     */       
/* 1003:1063 */       l = new Label(4, 2, "File hyperlink to documentation");
/* 1004:1064 */       s1.addCell(l);
/* 1005:     */       
/* 1006:1066 */       File file = new File("../jexcelapi/docs/index.html");
/* 1007:1067 */       wh = new WritableHyperlink(0, 32, 8, 32, file, 
/* 1008:1068 */         "JExcelApi Documentation");
/* 1009:1069 */       s1.addHyperlink(wh);
/* 1010:     */       
/* 1011:     */ 
/* 1012:1072 */       wh = new WritableHyperlink(0, 34, 8, 34, 
/* 1013:1073 */         "Link to another cell", 
/* 1014:1074 */         s1, 
/* 1015:1075 */         0, 180, 1, 181);
/* 1016:1076 */       s1.addHyperlink(wh);
/* 1017:     */       
/* 1018:1078 */       file = new File("\\\\localhost\\file.txt");
/* 1019:1079 */       wh = new WritableHyperlink(0, 36, 8, 36, file);
/* 1020:1080 */       s1.addHyperlink(wh);
/* 1021:     */       
/* 1022:     */ 
/* 1023:1083 */       url = new URL("http://www.amazon.co.uk/exec/obidos/ASIN/0571058086/qid=1099836249/sr=1-3/ref=sr_1_11_3/202-6017285-1620664");
/* 1024:     */       
/* 1025:1085 */       wh = new WritableHyperlink(0, 38, 0, 38, url);
/* 1026:1086 */       s1.addHyperlink(wh);
/* 1027:     */     }
/* 1028:     */     catch (MalformedURLException e)
/* 1029:     */     {
/* 1030:1090 */       System.err.println(e.toString());
/* 1031:     */     }
/* 1032:1094 */     Label l = new Label(5, 35, "Merged cells", timesBoldUnderline);
/* 1033:1095 */     s1.mergeCells(5, 35, 8, 37);
/* 1034:1096 */     s1.addCell(l);
/* 1035:     */     
/* 1036:1098 */     l = new Label(5, 38, "More merged cells");
/* 1037:1099 */     s1.addCell(l);
/* 1038:1100 */     Range r = s1.mergeCells(5, 38, 8, 41);
/* 1039:1101 */     s1.insertRow(40);
/* 1040:1102 */     s1.removeRow(39);
/* 1041:1103 */     s1.unmergeCells(r);
/* 1042:     */     
/* 1043:     */ 
/* 1044:1106 */     WritableCellFormat wcf = new WritableCellFormat();
/* 1045:1107 */     wcf.setAlignment(Alignment.CENTRE);
/* 1046:1108 */     l = new Label(5, 42, "Centred across merged cells", wcf);
/* 1047:1109 */     s1.addCell(l);
/* 1048:1110 */     s1.mergeCells(5, 42, 10, 42);
/* 1049:     */     
/* 1050:1112 */     wcf = new WritableCellFormat();
/* 1051:1113 */     wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
/* 1052:1114 */     wcf.setBackground(Colour.GRAY_25);
/* 1053:1115 */     l = new Label(3, 44, "Merged with border", wcf);
/* 1054:1116 */     s1.addCell(l);
/* 1055:1117 */     s1.mergeCells(3, 44, 4, 46);
/* 1056:     */     
/* 1057:     */ 
/* 1058:     */ 
/* 1059:     */ 
/* 1060:     */ 
/* 1061:     */ 
/* 1062:     */ 
/* 1063:     */ 
/* 1064:     */ 
/* 1065:     */ 
/* 1066:     */ 
/* 1067:     */ 
/* 1068:     */ 
/* 1069:     */ 
/* 1070:     */ 
/* 1071:     */ 
/* 1072:1134 */     WritableFont courier10ptFont = new WritableFont(WritableFont.COURIER, 10);
/* 1073:1135 */     WritableCellFormat courier10pt = new WritableCellFormat(courier10ptFont);
/* 1074:1136 */     l = new Label(0, 49, "Courier fonts", courier10pt);
/* 1075:1137 */     s1.addCell(l);
/* 1076:     */     
/* 1077:1139 */     WritableFont tahoma12ptFont = new WritableFont(WritableFont.TAHOMA, 12);
/* 1078:1140 */     WritableCellFormat tahoma12pt = new WritableCellFormat(tahoma12ptFont);
/* 1079:1141 */     l = new Label(0, 50, "Tahoma fonts", tahoma12pt);
/* 1080:1142 */     s1.addCell(l);
/* 1081:     */     
/* 1082:1144 */     WritableFont.FontName wingdingsFont = 
/* 1083:1145 */       WritableFont.createFont("Wingdings 2");
/* 1084:1146 */     WritableFont wingdings210ptFont = new WritableFont(wingdingsFont, 10);
/* 1085:1147 */     WritableCellFormat wingdings210pt = new WritableCellFormat(
/* 1086:1148 */       wingdings210ptFont);
/* 1087:1149 */     l = new Label(0, 51, "Bespoke Windgdings 2", wingdings210pt);
/* 1088:1150 */     s1.addCell(l);
/* 1089:     */     
/* 1090:1152 */     WritableCellFormat shrinkToFit = new WritableCellFormat(times12pt);
/* 1091:1153 */     shrinkToFit.setShrinkToFit(true);
/* 1092:1154 */     l = new Label(3, 53, "Shrunk to fit", shrinkToFit);
/* 1093:1155 */     s1.addCell(l);
/* 1094:     */     
/* 1095:1157 */     l = new Label(3, 55, "Some long wrapped text in a merged cell", 
/* 1096:1158 */       arial12format);
/* 1097:1159 */     s1.addCell(l);
/* 1098:1160 */     s1.mergeCells(3, 55, 4, 55);
/* 1099:     */     
/* 1100:1162 */     l = new Label(0, 57, "A cell with a comment");
/* 1101:1163 */     WritableCellFeatures cellFeatures = new WritableCellFeatures();
/* 1102:1164 */     cellFeatures.setComment("the cell comment");
/* 1103:1165 */     l.setCellFeatures(cellFeatures);
/* 1104:1166 */     s1.addCell(l);
/* 1105:     */     
/* 1106:1168 */     l = new Label(0, 59, 
/* 1107:1169 */       "A cell with a long comment");
/* 1108:1170 */     cellFeatures = new WritableCellFeatures();
/* 1109:1171 */     cellFeatures.setComment("a very long cell comment indeed that won't fit inside a standard comment box, so a larger comment box is used instead", 
/* 1110:     */     
/* 1111:     */ 
/* 1112:1174 */       5.0D, 6.0D);
/* 1113:1175 */     l.setCellFeatures(cellFeatures);
/* 1114:1176 */     s1.addCell(l);
/* 1115:     */     
/* 1116:1178 */     WritableCellFormat indented = new WritableCellFormat(times12pt);
/* 1117:1179 */     indented.setIndentation(4);
/* 1118:1180 */     l = new Label(0, 61, "Some indented text", indented);
/* 1119:1181 */     s1.addCell(l);
/* 1120:     */     
/* 1121:1183 */     l = new Label(0, 63, "Data validation:  list");
/* 1122:1184 */     s1.addCell(l);
/* 1123:     */     
/* 1124:1186 */     Blank b = new Blank(1, 63);
/* 1125:1187 */     cellFeatures = new WritableCellFeatures();
/* 1126:1188 */     ArrayList al = new ArrayList();
/* 1127:1189 */     al.add("bagpuss");
/* 1128:1190 */     al.add("clangers");
/* 1129:1191 */     al.add("ivor the engine");
/* 1130:1192 */     al.add("noggin the nog");
/* 1131:1193 */     cellFeatures.setDataValidationList(al);
/* 1132:1194 */     b.setCellFeatures(cellFeatures);
/* 1133:1195 */     s1.addCell(b);
/* 1134:     */     
/* 1135:1197 */     l = new Label(0, 64, "Data validation:  number > 4.5");
/* 1136:1198 */     s1.addCell(l);
/* 1137:     */     
/* 1138:1200 */     b = new Blank(1, 64);
/* 1139:1201 */     cellFeatures = new WritableCellFeatures();
/* 1140:1202 */     cellFeatures.setNumberValidation(4.5D, WritableCellFeatures.GREATER_THAN);
/* 1141:1203 */     b.setCellFeatures(cellFeatures);
/* 1142:1204 */     s1.addCell(b);
/* 1143:     */     
/* 1144:1206 */     l = new Label(0, 65, "Data validation:  named range");
/* 1145:1207 */     s1.addCell(l);
/* 1146:     */     
/* 1147:1209 */     l = new Label(4, 65, "tiger");
/* 1148:1210 */     s1.addCell(l);
/* 1149:1211 */     l = new Label(5, 65, "sword");
/* 1150:1212 */     s1.addCell(l);
/* 1151:1213 */     l = new Label(6, 65, "honour");
/* 1152:1214 */     s1.addCell(l);
/* 1153:1215 */     l = new Label(7, 65, "company");
/* 1154:1216 */     s1.addCell(l);
/* 1155:1217 */     l = new Label(8, 65, "victory");
/* 1156:1218 */     s1.addCell(l);
/* 1157:1219 */     l = new Label(9, 65, "fortress");
/* 1158:1220 */     s1.addCell(l);
/* 1159:     */     
/* 1160:1222 */     b = new Blank(1, 65);
/* 1161:1223 */     cellFeatures = new WritableCellFeatures();
/* 1162:1224 */     cellFeatures.setDataValidationRange("validation_range");
/* 1163:1225 */     b.setCellFeatures(cellFeatures);
/* 1164:1226 */     s1.addCell(b);
/* 1165:     */     
/* 1166:     */ 
/* 1167:1229 */     s1.setRowGroup(39, 45, false);
/* 1168:     */     
/* 1169:     */ 
/* 1170:1232 */     l = new Label(0, 66, "Block of cells B67-F71 with data validation");
/* 1171:1233 */     s1.addCell(l);
/* 1172:     */     
/* 1173:1235 */     al = new ArrayList();
/* 1174:1236 */     al.add("Achilles");
/* 1175:1237 */     al.add("Agamemnon");
/* 1176:1238 */     al.add("Hector");
/* 1177:1239 */     al.add("Odysseus");
/* 1178:1240 */     al.add("Patroclus");
/* 1179:1241 */     al.add("Nestor");
/* 1180:     */     
/* 1181:1243 */     b = new Blank(1, 66);
/* 1182:1244 */     cellFeatures = new WritableCellFeatures();
/* 1183:1245 */     cellFeatures.setDataValidationList(al);
/* 1184:1246 */     b.setCellFeatures(cellFeatures);
/* 1185:1247 */     s1.addCell(b);
/* 1186:1248 */     s1.applySharedDataValidation(b, 4, 4);
/* 1187:     */     
/* 1188:1250 */     cellFeatures = new WritableCellFeatures();
/* 1189:1251 */     cellFeatures.setDataValidationRange("");
/* 1190:1252 */     l = new Label(0, 71, "Read only cell using empty data validation");
/* 1191:1253 */     l.setCellFeatures(cellFeatures);
/* 1192:1254 */     s1.addCell(l);
/* 1193:     */     
/* 1194:     */ 
/* 1195:1257 */     s1.setRowGroup(39, 45, false);
/* 1196:     */   }
/* 1197:     */   
/* 1198:     */   private void writeBordersSheet(WritableSheet s)
/* 1199:     */     throws WriteException
/* 1200:     */   {
/* 1201:1269 */     s.getSettings().setProtected(true);
/* 1202:     */     
/* 1203:1271 */     s.setColumnView(1, 15);
/* 1204:1272 */     s.setColumnView(2, 15);
/* 1205:1273 */     s.setColumnView(4, 15);
/* 1206:1274 */     WritableCellFormat thickLeft = new WritableCellFormat();
/* 1207:1275 */     thickLeft.setBorder(Border.LEFT, BorderLineStyle.THICK);
/* 1208:1276 */     Label lr = new Label(1, 0, "Thick left", thickLeft);
/* 1209:1277 */     s.addCell(lr);
/* 1210:     */     
/* 1211:1279 */     WritableCellFormat dashedRight = new WritableCellFormat();
/* 1212:1280 */     dashedRight.setBorder(Border.RIGHT, BorderLineStyle.DASHED);
/* 1213:1281 */     lr = new Label(2, 0, "Dashed right", dashedRight);
/* 1214:1282 */     s.addCell(lr);
/* 1215:     */     
/* 1216:1284 */     WritableCellFormat doubleTop = new WritableCellFormat();
/* 1217:1285 */     doubleTop.setBorder(Border.TOP, BorderLineStyle.DOUBLE);
/* 1218:1286 */     lr = new Label(1, 2, "Double top", doubleTop);
/* 1219:1287 */     s.addCell(lr);
/* 1220:     */     
/* 1221:1289 */     WritableCellFormat hairBottom = new WritableCellFormat();
/* 1222:1290 */     hairBottom.setBorder(Border.BOTTOM, BorderLineStyle.HAIR);
/* 1223:1291 */     lr = new Label(2, 2, "Hair bottom", hairBottom);
/* 1224:1292 */     s.addCell(lr);
/* 1225:     */     
/* 1226:1294 */     WritableCellFormat allThin = new WritableCellFormat();
/* 1227:1295 */     allThin.setBorder(Border.ALL, BorderLineStyle.THIN);
/* 1228:1296 */     lr = new Label(4, 2, "All thin", allThin);
/* 1229:1297 */     s.addCell(lr);
/* 1230:     */     
/* 1231:1299 */     WritableCellFormat twoBorders = new WritableCellFormat();
/* 1232:1300 */     twoBorders.setBorder(Border.TOP, BorderLineStyle.THICK);
/* 1233:1301 */     twoBorders.setBorder(Border.LEFT, BorderLineStyle.THICK);
/* 1234:1302 */     lr = new Label(6, 2, "Two borders", twoBorders);
/* 1235:1303 */     s.addCell(lr);
/* 1236:     */     
/* 1237:     */ 
/* 1238:1306 */     lr = new Label(20, 20, "Dislocated cell - after a page break");
/* 1239:1307 */     s.addCell(lr);
/* 1240:     */     
/* 1241:     */ 
/* 1242:1310 */     s.getSettings().setPaperSize(PaperSize.A3);
/* 1243:1311 */     s.getSettings().setOrientation(PageOrientation.LANDSCAPE);
/* 1244:1312 */     s.getSettings().setPageOrder(PageOrder.DOWN_THEN_RIGHT);
/* 1245:1313 */     s.getSettings().setHeaderMargin(2.0D);
/* 1246:1314 */     s.getSettings().setFooterMargin(2.0D);
/* 1247:     */     
/* 1248:1316 */     s.getSettings().setTopMargin(3.0D);
/* 1249:1317 */     s.getSettings().setBottomMargin(3.0D);
/* 1250:     */     
/* 1251:     */ 
/* 1252:1320 */     HeaderFooter header = new HeaderFooter();
/* 1253:1321 */     header.getCentre().append("Page Header");
/* 1254:1322 */     s.getSettings().setHeader(header);
/* 1255:     */     
/* 1256:1324 */     HeaderFooter footer = new HeaderFooter();
/* 1257:1325 */     footer.getRight().append("page ");
/* 1258:1326 */     footer.getRight().appendPageNumber();
/* 1259:1327 */     s.getSettings().setFooter(footer);
/* 1260:     */     
/* 1261:     */ 
/* 1262:1330 */     s.addRowPageBreak(18);
/* 1263:1331 */     s.insertRow(17);
/* 1264:1332 */     s.insertRow(17);
/* 1265:1333 */     s.removeRow(17);
/* 1266:     */     
/* 1267:     */ 
/* 1268:1336 */     s.addRowPageBreak(30);
/* 1269:     */     
/* 1270:     */ 
/* 1271:1339 */     lr = new Label(10, 1, "Hidden column");
/* 1272:1340 */     s.addCell(lr);
/* 1273:     */     
/* 1274:1342 */     lr = new Label(3, 8, "Hidden row");
/* 1275:1343 */     s.addCell(lr);
/* 1276:1344 */     s.setRowView(8, true);
/* 1277:     */     
/* 1278:1346 */     WritableCellFormat allThickRed = new WritableCellFormat();
/* 1279:1347 */     allThickRed.setBorder(Border.ALL, BorderLineStyle.THICK, Colour.RED);
/* 1280:1348 */     lr = new Label(1, 5, "All thick red", allThickRed);
/* 1281:1349 */     s.addCell(lr);
/* 1282:     */     
/* 1283:1351 */     WritableCellFormat topBottomBlue = new WritableCellFormat();
/* 1284:1352 */     topBottomBlue.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLUE);
/* 1285:1353 */     topBottomBlue.setBorder(Border.BOTTOM, BorderLineStyle.THIN, Colour.BLUE);
/* 1286:1354 */     lr = new Label(4, 5, "Top and bottom blue", topBottomBlue);
/* 1287:1355 */     s.addCell(lr);
/* 1288:     */   }
/* 1289:     */   
/* 1290:     */   private void writeLabelsSheet(WritableSheet ws)
/* 1291:     */     throws WriteException
/* 1292:     */   {
/* 1293:1363 */     ws.getSettings().setProtected(true);
/* 1294:1364 */     ws.getSettings().setPassword("jxl");
/* 1295:1365 */     ws.getSettings().setVerticalFreeze(5);
/* 1296:1366 */     ws.getSettings().setDefaultRowHeight(500);
/* 1297:     */     
/* 1298:1368 */     WritableFont wf = new WritableFont(WritableFont.ARIAL, 12);
/* 1299:1369 */     wf.setItalic(true);
/* 1300:     */     
/* 1301:1371 */     WritableCellFormat wcf = new WritableCellFormat(wf);
/* 1302:     */     
/* 1303:1373 */     CellView cv = new CellView();
/* 1304:1374 */     cv.setSize(6400);
/* 1305:1375 */     cv.setFormat(wcf);
/* 1306:1376 */     ws.setColumnView(0, cv);
/* 1307:1377 */     ws.setColumnView(1, 15);
/* 1308:1379 */     for (int i = 0; i < 61; i++)
/* 1309:     */     {
/* 1310:1381 */       Label l1 = new Label(0, i, "Common Label");
/* 1311:1382 */       Label l2 = new Label(1, i, "Distinct label number " + i);
/* 1312:1383 */       ws.addCell(l1);
/* 1313:1384 */       ws.addCell(l2);
/* 1314:     */     }
/* 1315:1390 */     Label l3 = new Label(0, 61, "Common Label", wcf);
/* 1316:1391 */     Label l4 = new Label(1, 61, "1-1234567890", wcf);
/* 1317:1392 */     Label l5 = new Label(2, 61, "2-1234567890", wcf);
/* 1318:1393 */     ws.addCell(l3);
/* 1319:1394 */     ws.addCell(l4);
/* 1320:1395 */     ws.addCell(l5);
/* 1321:1397 */     for (int i = 62; i < 200; i++)
/* 1322:     */     {
/* 1323:1399 */       Label l1 = new Label(0, i, "Common Label");
/* 1324:1400 */       Label l2 = new Label(1, i, "Distinct label number " + i);
/* 1325:1401 */       ws.addCell(l1);
/* 1326:1402 */       ws.addCell(l2);
/* 1327:     */     }
/* 1328:1406 */     wf = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
/* 1329:1407 */     wf.setColour(Colour.RED);
/* 1330:1408 */     WritableCellFormat wcf2 = new WritableCellFormat(wf);
/* 1331:1409 */     wcf2.setWrap(true);
/* 1332:1410 */     Label l = new Label(0, 205, "Different format", wcf2);
/* 1333:1411 */     ws.addCell(l);
/* 1334:     */     
/* 1335:     */ 
/* 1336:1414 */     Label l6 = new Label(5, 2, "A column for autosizing", wcf2);
/* 1337:1415 */     ws.addCell(l6);
/* 1338:1416 */     l6 = new Label(5, 4, "Another label, longer this time and in a different font");
/* 1339:     */     
/* 1340:1418 */     ws.addCell(l6);
/* 1341:     */     
/* 1342:1420 */     CellView cf = new CellView();
/* 1343:1421 */     cf.setAutosize(true);
/* 1344:1422 */     ws.setColumnView(5, cf);
/* 1345:     */   }
/* 1346:     */   
/* 1347:     */   private void writeFormulaSheet(WritableSheet ws)
/* 1348:     */     throws WriteException
/* 1349:     */   {
/* 1350:1431 */     Number nc = new Number(0, 0, 15.0D);
/* 1351:1432 */     ws.addCell(nc);
/* 1352:     */     
/* 1353:1434 */     nc = new Number(0, 1, 16.0D);
/* 1354:1435 */     ws.addCell(nc);
/* 1355:     */     
/* 1356:1437 */     nc = new Number(0, 2, 10.0D);
/* 1357:1438 */     ws.addCell(nc);
/* 1358:     */     
/* 1359:1440 */     nc = new Number(0, 3, 12.0D);
/* 1360:1441 */     ws.addCell(nc);
/* 1361:     */     
/* 1362:1443 */     ws.setColumnView(2, 20);
/* 1363:1444 */     WritableCellFormat wcf = new WritableCellFormat();
/* 1364:1445 */     wcf.setAlignment(Alignment.RIGHT);
/* 1365:1446 */     wcf.setWrap(true);
/* 1366:1447 */     CellView cv = new CellView();
/* 1367:1448 */     cv.setSize(6400);
/* 1368:1449 */     cv.setFormat(wcf);
/* 1369:1450 */     ws.setColumnView(3, cv);
/* 1370:     */     
/* 1371:     */ 
/* 1372:1453 */     Formula f = null;
/* 1373:1454 */     Label l = null;
/* 1374:     */     
/* 1375:1456 */     f = new Formula(2, 0, "A1+A2");
/* 1376:1457 */     ws.addCell(f);
/* 1377:1458 */     l = new Label(3, 0, "a1+a2");
/* 1378:1459 */     ws.addCell(l);
/* 1379:     */     
/* 1380:1461 */     f = new Formula(2, 1, "A2 * 3");
/* 1381:1462 */     ws.addCell(f);
/* 1382:1463 */     l = new Label(3, 1, "A2 * 3");
/* 1383:1464 */     ws.addCell(l);
/* 1384:     */     
/* 1385:1466 */     f = new Formula(2, 2, "A2+A1/2.5");
/* 1386:1467 */     ws.addCell(f);
/* 1387:1468 */     l = new Label(3, 2, "A2+A1/2.5");
/* 1388:1469 */     ws.addCell(l);
/* 1389:     */     
/* 1390:1471 */     f = new Formula(2, 3, "3+(a1+a2)/2.5");
/* 1391:1472 */     ws.addCell(f);
/* 1392:1473 */     l = new Label(3, 3, "3+(a1+a2)/2.5");
/* 1393:1474 */     ws.addCell(l);
/* 1394:     */     
/* 1395:1476 */     f = new Formula(2, 4, "(a1+a2)/2.5");
/* 1396:1477 */     ws.addCell(f);
/* 1397:1478 */     l = new Label(3, 4, "(a1+a2)/2.5");
/* 1398:1479 */     ws.addCell(l);
/* 1399:     */     
/* 1400:1481 */     f = new Formula(2, 5, "15+((a1+a2)/2.5)*17");
/* 1401:1482 */     ws.addCell(f);
/* 1402:1483 */     l = new Label(3, 5, "15+((a1+a2)/2.5)*17");
/* 1403:1484 */     ws.addCell(l);
/* 1404:     */     
/* 1405:1486 */     f = new Formula(2, 6, "SUM(a1:a4)");
/* 1406:1487 */     ws.addCell(f);
/* 1407:1488 */     l = new Label(3, 6, "SUM(a1:a4)");
/* 1408:1489 */     ws.addCell(l);
/* 1409:     */     
/* 1410:1491 */     f = new Formula(2, 7, "SUM(a1:a4)/4");
/* 1411:1492 */     ws.addCell(f);
/* 1412:1493 */     l = new Label(3, 7, "SUM(a1:a4)/4");
/* 1413:1494 */     ws.addCell(l);
/* 1414:     */     
/* 1415:1496 */     f = new Formula(2, 8, "AVERAGE(A1:A4)");
/* 1416:1497 */     ws.addCell(f);
/* 1417:1498 */     l = new Label(3, 8, "AVERAGE(a1:a4)");
/* 1418:1499 */     ws.addCell(l);
/* 1419:     */     
/* 1420:1501 */     f = new Formula(2, 9, "MIN(5,4,1,2,3)");
/* 1421:1502 */     ws.addCell(f);
/* 1422:1503 */     l = new Label(3, 9, "MIN(5,4,1,2,3)");
/* 1423:1504 */     ws.addCell(l);
/* 1424:     */     
/* 1425:1506 */     f = new Formula(2, 10, "ROUND(3.14159265, 3)");
/* 1426:1507 */     ws.addCell(f);
/* 1427:1508 */     l = new Label(3, 10, "ROUND(3.14159265, 3)");
/* 1428:1509 */     ws.addCell(l);
/* 1429:     */     
/* 1430:1511 */     f = new Formula(2, 11, "MAX(SUM(A1:A2), A1*A2, POWER(A1, 2))");
/* 1431:1512 */     ws.addCell(f);
/* 1432:1513 */     l = new Label(3, 11, "MAX(SUM(A1:A2), A1*A2, POWER(A1, 2))");
/* 1433:1514 */     ws.addCell(l);
/* 1434:     */     
/* 1435:1516 */     f = new Formula(2, 12, "IF(A2>A1, \"A2 bigger\", \"A1 bigger\")");
/* 1436:1517 */     ws.addCell(f);
/* 1437:1518 */     l = new Label(3, 12, "IF(A2>A1, \"A2 bigger\", \"A1 bigger\")");
/* 1438:1519 */     ws.addCell(l);
/* 1439:     */     
/* 1440:1521 */     f = new Formula(2, 13, "IF(A2<=A1, \"A2 smaller\", \"A1 smaller\")");
/* 1441:1522 */     ws.addCell(f);
/* 1442:1523 */     l = new Label(3, 13, "IF(A2<=A1, \"A2 smaller\", \"A1 smaller\")");
/* 1443:1524 */     ws.addCell(l);
/* 1444:     */     
/* 1445:1526 */     f = new Formula(2, 14, "IF(A3<=10, \"<= 10\")");
/* 1446:1527 */     ws.addCell(f);
/* 1447:1528 */     l = new Label(3, 14, "IF(A3<=10, \"<= 10\")");
/* 1448:1529 */     ws.addCell(l);
/* 1449:     */     
/* 1450:1531 */     f = new Formula(2, 15, "SUM(1,2,3,4,5)");
/* 1451:1532 */     ws.addCell(f);
/* 1452:1533 */     l = new Label(3, 15, "SUM(1,2,3,4,5)");
/* 1453:1534 */     ws.addCell(l);
/* 1454:     */     
/* 1455:1536 */     f = new Formula(2, 16, "HYPERLINK(\"http://www.andykhan.com/jexcelapi\", \"JExcelApi Home Page\")");
/* 1456:1537 */     ws.addCell(f);
/* 1457:1538 */     l = new Label(3, 16, "HYPERLINK(\"http://www.andykhan.com/jexcelapi\", \"JExcelApi Home Page\")");
/* 1458:1539 */     ws.addCell(l);
/* 1459:     */     
/* 1460:1541 */     f = new Formula(2, 17, "3*4+5");
/* 1461:1542 */     ws.addCell(f);
/* 1462:1543 */     l = new Label(3, 17, "3*4+5");
/* 1463:1544 */     ws.addCell(l);
/* 1464:     */     
/* 1465:1546 */     f = new Formula(2, 18, "\"Plain text formula\"");
/* 1466:1547 */     ws.addCell(f);
/* 1467:1548 */     l = new Label(3, 18, "Plain text formula");
/* 1468:1549 */     ws.addCell(l);
/* 1469:     */     
/* 1470:1551 */     f = new Formula(2, 19, "SUM(a1,a2,-a3,a4)");
/* 1471:1552 */     ws.addCell(f);
/* 1472:1553 */     l = new Label(3, 19, "SUM(a1,a2,-a3,a4)");
/* 1473:1554 */     ws.addCell(l);
/* 1474:     */     
/* 1475:1556 */     f = new Formula(2, 20, "2*-(a1+a2)");
/* 1476:1557 */     ws.addCell(f);
/* 1477:1558 */     l = new Label(3, 20, "2*-(a1+a2)");
/* 1478:1559 */     ws.addCell(l);
/* 1479:     */     
/* 1480:1561 */     f = new Formula(2, 21, "'Number Formats'!B1/2");
/* 1481:1562 */     ws.addCell(f);
/* 1482:1563 */     l = new Label(3, 21, "'Number Formats'!B1/2");
/* 1483:1564 */     ws.addCell(l);
/* 1484:     */     
/* 1485:1566 */     f = new Formula(2, 22, "IF(F22=0, 0, F21/F22)");
/* 1486:1567 */     ws.addCell(f);
/* 1487:1568 */     l = new Label(3, 22, "IF(F22=0, 0, F21/F22)");
/* 1488:1569 */     ws.addCell(l);
/* 1489:     */     
/* 1490:1571 */     f = new Formula(2, 23, "RAND()");
/* 1491:1572 */     ws.addCell(f);
/* 1492:1573 */     l = new Label(3, 23, "RAND()");
/* 1493:1574 */     ws.addCell(l);
/* 1494:     */     
/* 1495:1576 */     StringBuffer buf = new StringBuffer();
/* 1496:1577 */     buf.append("'");
/* 1497:1578 */     buf.append(this.workbook.getSheet(0).getName());
/* 1498:1579 */     buf.append("'!");
/* 1499:1580 */     buf.append(CellReferenceHelper.getCellReference(9, 18));
/* 1500:1581 */     buf.append("*25");
/* 1501:1582 */     f = new Formula(2, 24, buf.toString());
/* 1502:1583 */     ws.addCell(f);
/* 1503:1584 */     l = new Label(3, 24, buf.toString());
/* 1504:1585 */     ws.addCell(l);
/* 1505:     */     
/* 1506:1587 */     wcf = new WritableCellFormat(DateFormats.DEFAULT);
/* 1507:1588 */     f = new Formula(2, 25, "NOW()", wcf);
/* 1508:1589 */     ws.addCell(f);
/* 1509:1590 */     l = new Label(3, 25, "NOW()");
/* 1510:1591 */     ws.addCell(l);
/* 1511:     */     
/* 1512:1593 */     f = new Formula(2, 26, "$A$2+A3");
/* 1513:1594 */     ws.addCell(f);
/* 1514:1595 */     l = new Label(3, 26, "$A$2+A3");
/* 1515:1596 */     ws.addCell(l);
/* 1516:     */     
/* 1517:1598 */     f = new Formula(2, 27, "IF(COUNT(A1:A9,B1:B9)=0,\"\",COUNT(A1:A9,B1:B9))");
/* 1518:1599 */     ws.addCell(f);
/* 1519:1600 */     l = new Label(3, 27, "IF(COUNT(A1:A9,B1:B9)=0,\"\",COUNT(A1:A9,B1:B9))");
/* 1520:1601 */     ws.addCell(l);
/* 1521:     */     
/* 1522:1603 */     f = new Formula(2, 28, "SUM(A1,A2,A3,A4)");
/* 1523:1604 */     ws.addCell(f);
/* 1524:1605 */     l = new Label(3, 28, "SUM(A1,A2,A3,A4)");
/* 1525:1606 */     ws.addCell(l);
/* 1526:     */     
/* 1527:1608 */     l = new Label(1, 29, "a1");
/* 1528:1609 */     ws.addCell(l);
/* 1529:1610 */     f = new Formula(2, 29, "SUM(INDIRECT(ADDRESS(2,29)):A4)");
/* 1530:1611 */     ws.addCell(f);
/* 1531:1612 */     l = new Label(3, 29, "SUM(INDIRECT(ADDRESS(2,29):A4)");
/* 1532:1613 */     ws.addCell(l);
/* 1533:     */     
/* 1534:1615 */     f = new Formula(2, 30, "COUNTIF(A1:A4, \">=12\")");
/* 1535:1616 */     ws.addCell(f);
/* 1536:1617 */     l = new Label(3, 30, "COUNTIF(A1:A4, \">=12\")");
/* 1537:1618 */     ws.addCell(l);
/* 1538:     */     
/* 1539:1620 */     f = new Formula(2, 31, "MAX($A$1:$A$4)");
/* 1540:1621 */     ws.addCell(f);
/* 1541:1622 */     l = new Label(3, 31, "MAX($A$1:$A$4)");
/* 1542:1623 */     ws.addCell(l);
/* 1543:     */     
/* 1544:1625 */     f = new Formula(2, 32, "OR(A1,TRUE)");
/* 1545:1626 */     ws.addCell(f);
/* 1546:1627 */     l = new Label(3, 32, "OR(A1,TRUE)");
/* 1547:1628 */     ws.addCell(l);
/* 1548:     */     
/* 1549:1630 */     f = new Formula(2, 33, "ROWS(A1:C14)");
/* 1550:1631 */     ws.addCell(f);
/* 1551:1632 */     l = new Label(3, 33, "ROWS(A1:C14)");
/* 1552:1633 */     ws.addCell(l);
/* 1553:     */     
/* 1554:1635 */     f = new Formula(2, 34, "COUNTBLANK(A1:C14)");
/* 1555:1636 */     ws.addCell(f);
/* 1556:1637 */     l = new Label(3, 34, "COUNTBLANK(A1:C14)");
/* 1557:1638 */     ws.addCell(l);
/* 1558:     */     
/* 1559:1640 */     f = new Formula(2, 35, "IF(((F1=\"Not Found\")*(F2=\"Not Found\")*(F3=\"\")*(F4=\"\")*(F5=\"\")),1,0)");
/* 1560:1641 */     ws.addCell(f);
/* 1561:1642 */     l = new Label(3, 35, "IF(((F1=\"Not Found\")*(F2=\"Not Found\")*(F3=\"\")*(F4=\"\")*(F5=\"\")),1,0)");
/* 1562:1643 */     ws.addCell(l);
/* 1563:     */     
/* 1564:1645 */     f = new Formula(2, 36, 
/* 1565:1646 */       "HYPERLINK(\"http://www.amazon.co.uk/exec/obidos/ASIN/0571058086qid=1099836249/sr=1-3/ref=sr_1_11_3/202-6017285-1620664\",  \"Long hyperlink\")");
/* 1566:1647 */     ws.addCell(f);
/* 1567:     */     
/* 1568:1649 */     f = new Formula(2, 37, "1234567+2699");
/* 1569:1650 */     ws.addCell(f);
/* 1570:1651 */     l = new Label(3, 37, "1234567+2699");
/* 1571:1652 */     ws.addCell(l);
/* 1572:     */     
/* 1573:1654 */     f = new Formula(2, 38, "IF(ISERROR(G25/G29),0,-1)");
/* 1574:1655 */     ws.addCell(f);
/* 1575:1656 */     l = new Label(3, 38, "IF(ISERROR(G25/G29),0,-1)");
/* 1576:1657 */     ws.addCell(l);
/* 1577:     */     
/* 1578:1659 */     f = new Formula(2, 39, "SEARCH(\"C\",D40)");
/* 1579:1660 */     ws.addCell(f);
/* 1580:1661 */     l = new Label(3, 39, "SEARCH(\"C\",D40)");
/* 1581:1662 */     ws.addCell(l);
/* 1582:     */     
/* 1583:1664 */     f = new Formula(2, 40, "#REF!");
/* 1584:1665 */     ws.addCell(f);
/* 1585:1666 */     l = new Label(3, 40, "#REF!");
/* 1586:1667 */     ws.addCell(l);
/* 1587:     */     
/* 1588:1669 */     nc = new Number(1, 41, 79.0D);
/* 1589:1670 */     ws.addCell(nc);
/* 1590:1671 */     f = new Formula(2, 41, "--B42");
/* 1591:1672 */     ws.addCell(f);
/* 1592:1673 */     l = new Label(3, 41, "--B42");
/* 1593:1674 */     ws.addCell(l);
/* 1594:     */     
/* 1595:1676 */     f = new Formula(2, 42, "CHOOSE(3,A1,A2,A3,A4");
/* 1596:1677 */     ws.addCell(f);
/* 1597:1678 */     l = new Label(3, 42, "CHOOSE(3,A1,A2,A3,A4");
/* 1598:1679 */     ws.addCell(l);
/* 1599:     */     
/* 1600:1681 */     f = new Formula(2, 43, "A4-A3-A2");
/* 1601:1682 */     ws.addCell(f);
/* 1602:1683 */     l = new Label(3, 43, "A4-A3-A2");
/* 1603:1684 */     ws.addCell(l);
/* 1604:     */     
/* 1605:1686 */     f = new Formula(2, 44, "F29+F34+F41+F48+F55+F62+F69+F76+F83+F90+F97+F104+F111+F118+F125+F132+F139+F146+F153+F160+F167+F174+F181+F188+F195+F202+F209+F216+F223+F230+F237+F244+F251+F258+F265+F272+F279+F286+F293+F300+F305+F308");
/* 1606:1687 */     ws.addCell(f);
/* 1607:1688 */     l = new Label(3, 44, "F29+F34+F41+F48+F55+F62+F69+F76+F83+F90+F97+F104+F111+F118+F125+F132+F139+F146+F153+F160+F167+F174+F181+F188+F195+F202+F209+F216+F223+F230+F237+F244+F251+F258+F265+F272+F279+F286+F293+F300+F305+F308");
/* 1608:1689 */     ws.addCell(l);
/* 1609:     */     
/* 1610:1691 */     nc = new Number(1, 45, 17.0D);
/* 1611:1692 */     ws.addCell(nc);
/* 1612:1693 */     f = new Formula(2, 45, "formulavalue+5");
/* 1613:1694 */     ws.addCell(f);
/* 1614:1695 */     l = new Label(3, 45, "formulavalue+5");
/* 1615:1696 */     ws.addCell(l);
/* 1616:     */   }
/* 1617:     */   
/* 1618:     */   private void writeImageSheet(WritableSheet ws)
/* 1619:     */     throws WriteException
/* 1620:     */   {
/* 1621:1722 */     Label l = new Label(0, 0, "Weald & Downland Open Air Museum, Sussex");
/* 1622:1723 */     ws.addCell(l);
/* 1623:     */     
/* 1624:1725 */     WritableImage wi = new WritableImage(
/* 1625:1726 */       0.0D, 3.0D, 5.0D, 7.0D, new File("resources/wealdanddownland.png"));
/* 1626:1727 */     ws.addImage(wi);
/* 1627:     */     
/* 1628:1729 */     l = new Label(0, 12, "Merchant Adventurers Hall, York");
/* 1629:1730 */     ws.addCell(l);
/* 1630:     */     
/* 1631:1732 */     wi = new WritableImage(5.0D, 12.0D, 4.0D, 10.0D, 
/* 1632:1733 */       new File("resources/merchantadventurers.png"));
/* 1633:1734 */     ws.addImage(wi);
/* 1634:     */   }
/* 1635:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.Write
 * JD-Core Version:    0.7.0.1
 */