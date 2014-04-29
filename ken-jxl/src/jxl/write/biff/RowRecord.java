/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import jxl.CellType;
/*   7:    */ import jxl.SheetSettings;
/*   8:    */ import jxl.biff.CellReferenceHelper;
/*   9:    */ import jxl.biff.DVParser;
/*  10:    */ import jxl.biff.IndexMapping;
/*  11:    */ import jxl.biff.IntegerHelper;
/*  12:    */ import jxl.biff.Type;
/*  13:    */ import jxl.biff.WritableRecordData;
/*  14:    */ import jxl.biff.XFRecord;
/*  15:    */ import jxl.common.Logger;
/*  16:    */ import jxl.write.Number;
/*  17:    */ import jxl.write.WritableCellFeatures;
/*  18:    */ import jxl.write.WritableSheet;
/*  19:    */ 
/*  20:    */ class RowRecord
/*  21:    */   extends WritableRecordData
/*  22:    */ {
/*  23: 49 */   private static final Logger logger = Logger.getLogger(RowRecord.class);
/*  24:    */   private byte[] data;
/*  25:    */   private CellValue[] cells;
/*  26:    */   private int rowHeight;
/*  27:    */   private boolean collapsed;
/*  28:    */   private int rowNumber;
/*  29:    */   private int numColumns;
/*  30:    */   private int xfIndex;
/*  31:    */   private XFRecord style;
/*  32:    */   private boolean defaultFormat;
/*  33:    */   private boolean matchesDefFontHeight;
/*  34:    */   private static final int growSize = 10;
/*  35:    */   private static final int maxRKValue = 536870911;
/*  36:    */   private static final int minRKValue = -536870912;
/*  37:109 */   private static int defaultHeightIndicator = 255;
/*  38:114 */   private static int maxColumns = 256;
/*  39:    */   private int outlineLevel;
/*  40:    */   private boolean groupStart;
/*  41:    */   private WritableSheet sheet;
/*  42:    */   
/*  43:    */   public RowRecord(int rn, WritableSheet ws)
/*  44:    */   {
/*  45:138 */     super(Type.ROW);
/*  46:139 */     this.rowNumber = rn;
/*  47:140 */     this.cells = new CellValue[0];
/*  48:141 */     this.numColumns = 0;
/*  49:142 */     this.rowHeight = defaultHeightIndicator;
/*  50:143 */     this.collapsed = false;
/*  51:144 */     this.matchesDefFontHeight = true;
/*  52:145 */     this.sheet = ws;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setRowHeight(int h)
/*  56:    */   {
/*  57:155 */     if (h == 0)
/*  58:    */     {
/*  59:157 */       setCollapsed(true);
/*  60:158 */       this.matchesDefFontHeight = false;
/*  61:    */     }
/*  62:    */     else
/*  63:    */     {
/*  64:162 */       this.rowHeight = h;
/*  65:163 */       this.matchesDefFontHeight = false;
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   void setRowDetails(int height, boolean mdfh, boolean col, int ol, boolean gs, XFRecord xfr)
/*  70:    */   {
/*  71:185 */     this.rowHeight = height;
/*  72:186 */     this.collapsed = col;
/*  73:187 */     this.matchesDefFontHeight = mdfh;
/*  74:188 */     this.outlineLevel = ol;
/*  75:189 */     this.groupStart = gs;
/*  76:191 */     if (xfr != null)
/*  77:    */     {
/*  78:193 */       this.defaultFormat = true;
/*  79:194 */       this.style = xfr;
/*  80:195 */       this.xfIndex = this.style.getXFIndex();
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setCollapsed(boolean c)
/*  85:    */   {
/*  86:206 */     this.collapsed = c;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getRowNumber()
/*  90:    */   {
/*  91:216 */     return this.rowNumber;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void addCell(CellValue cv)
/*  95:    */   {
/*  96:226 */     int col = cv.getColumn();
/*  97:228 */     if (col >= maxColumns)
/*  98:    */     {
/*  99:230 */       logger.warn("Could not add cell at " + 
/* 100:231 */         CellReferenceHelper.getCellReference(cv.getRow(), 
/* 101:232 */         cv.getColumn()) + 
/* 102:233 */         " because it exceeds the maximum column limit");
/* 103:234 */       return;
/* 104:    */     }
/* 105:238 */     if (col >= this.cells.length)
/* 106:    */     {
/* 107:240 */       CellValue[] oldCells = this.cells;
/* 108:241 */       this.cells = new CellValue[Math.max(oldCells.length + 10, col + 1)];
/* 109:242 */       System.arraycopy(oldCells, 0, this.cells, 0, oldCells.length);
/* 110:243 */       oldCells = (CellValue[])null;
/* 111:    */     }
/* 112:247 */     if (this.cells[col] != null)
/* 113:    */     {
/* 114:249 */       WritableCellFeatures wcf = this.cells[col].getWritableCellFeatures();
/* 115:250 */       if (wcf != null)
/* 116:    */       {
/* 117:252 */         wcf.removeComment();
/* 118:256 */         if ((wcf.getDVParser() != null) && 
/* 119:257 */           (!wcf.getDVParser().extendedCellsValidation())) {
/* 120:259 */           wcf.removeDataValidation();
/* 121:    */         }
/* 122:    */       }
/* 123:    */     }
/* 124:265 */     this.cells[col] = cv;
/* 125:    */     
/* 126:267 */     this.numColumns = Math.max(col + 1, this.numColumns);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void removeCell(int col)
/* 130:    */   {
/* 131:278 */     if (col >= this.numColumns) {
/* 132:280 */       return;
/* 133:    */     }
/* 134:283 */     this.cells[col] = null;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void write(File outputFile)
/* 138:    */     throws IOException
/* 139:    */   {
/* 140:294 */     outputFile.write(this);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void writeCells(File outputFile)
/* 144:    */     throws IOException
/* 145:    */   {
/* 146:309 */     ArrayList integerValues = new ArrayList();
/* 147:310 */     boolean integerValue = false;
/* 148:313 */     for (int i = 0; i < this.numColumns; i++)
/* 149:    */     {
/* 150:315 */       integerValue = false;
/* 151:316 */       if (this.cells[i] != null)
/* 152:    */       {
/* 153:320 */         if (this.cells[i].getType() == CellType.NUMBER)
/* 154:    */         {
/* 155:322 */           Number nc = (Number)this.cells[i];
/* 156:323 */           if ((nc.getValue() == (int)nc.getValue()) && 
/* 157:324 */             (nc.getValue() < 536870911.0D) && 
/* 158:325 */             (nc.getValue() > -536870912.0D) && 
/* 159:326 */             (nc.getCellFeatures() == null)) {
/* 160:328 */             integerValue = true;
/* 161:    */           }
/* 162:    */         }
/* 163:332 */         if (integerValue)
/* 164:    */         {
/* 165:335 */           integerValues.add(this.cells[i]);
/* 166:    */         }
/* 167:    */         else
/* 168:    */         {
/* 169:341 */           writeIntegerValues(integerValues, outputFile);
/* 170:342 */           outputFile.write(this.cells[i]);
/* 171:346 */           if (this.cells[i].getType() == CellType.STRING_FORMULA)
/* 172:    */           {
/* 173:348 */             StringRecord sr = new StringRecord(this.cells[i].getContents());
/* 174:349 */             outputFile.write(sr);
/* 175:    */           }
/* 176:    */         }
/* 177:    */       }
/* 178:    */       else
/* 179:    */       {
/* 180:357 */         writeIntegerValues(integerValues, outputFile);
/* 181:    */       }
/* 182:    */     }
/* 183:362 */     writeIntegerValues(integerValues, outputFile);
/* 184:    */   }
/* 185:    */   
/* 186:    */   private void writeIntegerValues(ArrayList integerValues, File outputFile)
/* 187:    */     throws IOException
/* 188:    */   {
/* 189:376 */     if (integerValues.size() == 0) {
/* 190:378 */       return;
/* 191:    */     }
/* 192:381 */     if (integerValues.size() >= 3)
/* 193:    */     {
/* 194:384 */       MulRKRecord mulrk = new MulRKRecord(integerValues);
/* 195:385 */       outputFile.write(mulrk);
/* 196:    */     }
/* 197:    */     else
/* 198:    */     {
/* 199:390 */       Iterator i = integerValues.iterator();
/* 200:391 */       while (i.hasNext()) {
/* 201:393 */         outputFile.write((CellValue)i.next());
/* 202:    */       }
/* 203:    */     }
/* 204:398 */     integerValues.clear();
/* 205:    */   }
/* 206:    */   
/* 207:    */   public byte[] getData()
/* 208:    */   {
/* 209:409 */     byte[] data = new byte[16];
/* 210:    */     
/* 211:    */ 
/* 212:    */ 
/* 213:    */ 
/* 214:414 */     int rh = this.rowHeight;
/* 215:415 */     if (this.sheet.getSettings().getDefaultRowHeight() != 
/* 216:416 */       255) {
/* 217:420 */       if (rh == defaultHeightIndicator) {
/* 218:422 */         rh = this.sheet.getSettings().getDefaultRowHeight();
/* 219:    */       }
/* 220:    */     }
/* 221:426 */     IntegerHelper.getTwoBytes(this.rowNumber, data, 0);
/* 222:427 */     IntegerHelper.getTwoBytes(this.numColumns, data, 4);
/* 223:428 */     IntegerHelper.getTwoBytes(rh, data, 6);
/* 224:    */     
/* 225:430 */     int options = 256 + this.outlineLevel;
/* 226:432 */     if (this.groupStart) {
/* 227:434 */       options |= 0x10;
/* 228:    */     }
/* 229:437 */     if (this.collapsed) {
/* 230:439 */       options |= 0x20;
/* 231:    */     }
/* 232:442 */     if (!this.matchesDefFontHeight) {
/* 233:444 */       options |= 0x40;
/* 234:    */     }
/* 235:447 */     if (this.defaultFormat)
/* 236:    */     {
/* 237:449 */       options |= 0x80;
/* 238:450 */       options |= this.xfIndex << 16;
/* 239:    */     }
/* 240:453 */     IntegerHelper.getFourBytes(options, data, 12);
/* 241:    */     
/* 242:455 */     return data;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public int getMaxColumn()
/* 246:    */   {
/* 247:465 */     return this.numColumns;
/* 248:    */   }
/* 249:    */   
/* 250:    */   public CellValue getCell(int col)
/* 251:    */   {
/* 252:477 */     return (col >= 0) && (col < this.numColumns) ? this.cells[col] : null;
/* 253:    */   }
/* 254:    */   
/* 255:    */   void incrementRow()
/* 256:    */   {
/* 257:486 */     this.rowNumber += 1;
/* 258:488 */     for (int i = 0; i < this.cells.length; i++) {
/* 259:490 */       if (this.cells[i] != null) {
/* 260:492 */         this.cells[i].incrementRow();
/* 261:    */       }
/* 262:    */     }
/* 263:    */   }
/* 264:    */   
/* 265:    */   void decrementRow()
/* 266:    */   {
/* 267:503 */     this.rowNumber -= 1;
/* 268:504 */     for (int i = 0; i < this.cells.length; i++) {
/* 269:506 */       if (this.cells[i] != null) {
/* 270:508 */         this.cells[i].decrementRow();
/* 271:    */       }
/* 272:    */     }
/* 273:    */   }
/* 274:    */   
/* 275:    */   void insertColumn(int col)
/* 276:    */   {
/* 277:523 */     if (col >= this.numColumns) {
/* 278:525 */       return;
/* 279:    */     }
/* 280:529 */     CellValue[] oldCells = this.cells;
/* 281:531 */     if (this.numColumns >= this.cells.length - 1) {
/* 282:533 */       this.cells = new CellValue[oldCells.length + 10];
/* 283:    */     } else {
/* 284:537 */       this.cells = new CellValue[oldCells.length];
/* 285:    */     }
/* 286:541 */     System.arraycopy(oldCells, 0, this.cells, 0, col);
/* 287:    */     
/* 288:    */ 
/* 289:544 */     System.arraycopy(oldCells, col, this.cells, col + 1, this.numColumns - col);
/* 290:547 */     for (int i = col + 1; i <= this.numColumns; i++) {
/* 291:549 */       if (this.cells[i] != null) {
/* 292:551 */         this.cells[i].incrementColumn();
/* 293:    */       }
/* 294:    */     }
/* 295:556 */     this.numColumns = Math.min(this.numColumns + 1, maxColumns);
/* 296:    */   }
/* 297:    */   
/* 298:    */   void removeColumn(int col)
/* 299:    */   {
/* 300:568 */     if (col >= this.numColumns) {
/* 301:570 */       return;
/* 302:    */     }
/* 303:574 */     CellValue[] oldCells = this.cells;
/* 304:    */     
/* 305:576 */     this.cells = new CellValue[oldCells.length];
/* 306:    */     
/* 307:    */ 
/* 308:579 */     System.arraycopy(oldCells, 0, this.cells, 0, col);
/* 309:    */     
/* 310:    */ 
/* 311:582 */     System.arraycopy(oldCells, col + 1, this.cells, col, this.numColumns - (col + 1));
/* 312:585 */     for (int i = col; i < this.numColumns; i++) {
/* 313:587 */       if (this.cells[i] != null) {
/* 314:589 */         this.cells[i].decrementColumn();
/* 315:    */       }
/* 316:    */     }
/* 317:594 */     this.numColumns -= 1;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public boolean isDefaultHeight()
/* 321:    */   {
/* 322:604 */     return this.rowHeight == defaultHeightIndicator;
/* 323:    */   }
/* 324:    */   
/* 325:    */   public int getRowHeight()
/* 326:    */   {
/* 327:614 */     return this.rowHeight;
/* 328:    */   }
/* 329:    */   
/* 330:    */   public boolean isCollapsed()
/* 331:    */   {
/* 332:624 */     return this.collapsed;
/* 333:    */   }
/* 334:    */   
/* 335:    */   void rationalize(IndexMapping xfmapping)
/* 336:    */   {
/* 337:633 */     if (this.defaultFormat) {
/* 338:635 */       this.xfIndex = xfmapping.getNewIndex(this.xfIndex);
/* 339:    */     }
/* 340:    */   }
/* 341:    */   
/* 342:    */   XFRecord getStyle()
/* 343:    */   {
/* 344:647 */     return this.style;
/* 345:    */   }
/* 346:    */   
/* 347:    */   boolean hasDefaultFormat()
/* 348:    */   {
/* 349:657 */     return this.defaultFormat;
/* 350:    */   }
/* 351:    */   
/* 352:    */   boolean matchesDefaultFontHeight()
/* 353:    */   {
/* 354:667 */     return this.matchesDefFontHeight;
/* 355:    */   }
/* 356:    */   
/* 357:    */   public int getOutlineLevel()
/* 358:    */   {
/* 359:677 */     return this.outlineLevel;
/* 360:    */   }
/* 361:    */   
/* 362:    */   public boolean getGroupStart()
/* 363:    */   {
/* 364:687 */     return this.groupStart;
/* 365:    */   }
/* 366:    */   
/* 367:    */   public void incrementOutlineLevel()
/* 368:    */   {
/* 369:695 */     this.outlineLevel += 1;
/* 370:    */   }
/* 371:    */   
/* 372:    */   public void decrementOutlineLevel()
/* 373:    */   {
/* 374:705 */     if (this.outlineLevel > 0) {
/* 375:707 */       this.outlineLevel -= 1;
/* 376:    */     }
/* 377:710 */     if (this.outlineLevel == 0) {
/* 378:712 */       this.collapsed = false;
/* 379:    */     }
/* 380:    */   }
/* 381:    */   
/* 382:    */   public void setOutlineLevel(int level)
/* 383:    */   {
/* 384:723 */     this.outlineLevel = level;
/* 385:    */   }
/* 386:    */   
/* 387:    */   public void setGroupStart(boolean value)
/* 388:    */   {
/* 389:733 */     this.groupStart = value;
/* 390:    */   }
/* 391:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.RowRecord
 * JD-Core Version:    0.7.0.1
 */