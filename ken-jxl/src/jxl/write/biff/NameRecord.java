/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.BuiltInName;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.StringHelper;
/*   6:    */ import jxl.biff.Type;
/*   7:    */ import jxl.biff.WritableRecordData;
/*   8:    */ import jxl.common.Logger;
/*   9:    */ 
/*  10:    */ class NameRecord
/*  11:    */   extends WritableRecordData
/*  12:    */ {
/*  13: 37 */   private static Logger logger = Logger.getLogger(NameRecord.class);
/*  14:    */   private byte[] data;
/*  15:    */   private String name;
/*  16:    */   private BuiltInName builtInName;
/*  17:    */   private int index;
/*  18: 62 */   private int sheetRef = 0;
/*  19:    */   private boolean modified;
/*  20:    */   private NameRange[] ranges;
/*  21:    */   private static final int cellReference = 58;
/*  22:    */   private static final int areaReference = 59;
/*  23:    */   private static final int subExpression = 41;
/*  24:    */   private static final int union = 16;
/*  25:    */   
/*  26:    */   static class NameRange
/*  27:    */   {
/*  28:    */     private int columnFirst;
/*  29:    */     private int rowFirst;
/*  30:    */     private int columnLast;
/*  31:    */     private int rowLast;
/*  32:    */     private int externalSheet;
/*  33:    */     
/*  34:    */     NameRange(jxl.read.biff.NameRecord.NameRange nr)
/*  35:    */     {
/*  36: 82 */       this.columnFirst = nr.getFirstColumn();
/*  37: 83 */       this.rowFirst = nr.getFirstRow();
/*  38: 84 */       this.columnLast = nr.getLastColumn();
/*  39: 85 */       this.rowLast = nr.getLastRow();
/*  40: 86 */       this.externalSheet = nr.getExternalSheet();
/*  41:    */     }
/*  42:    */     
/*  43:    */     NameRange(int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol)
/*  44:    */     {
/*  45: 98 */       this.columnFirst = theStartCol;
/*  46: 99 */       this.rowFirst = theStartRow;
/*  47:100 */       this.columnLast = theEndCol;
/*  48:101 */       this.rowLast = theEndRow;
/*  49:102 */       this.externalSheet = extSheet;
/*  50:    */     }
/*  51:    */     
/*  52:    */     int getFirstColumn()
/*  53:    */     {
/*  54:105 */       return this.columnFirst;
/*  55:    */     }
/*  56:    */     
/*  57:    */     int getFirstRow()
/*  58:    */     {
/*  59:106 */       return this.rowFirst;
/*  60:    */     }
/*  61:    */     
/*  62:    */     int getLastColumn()
/*  63:    */     {
/*  64:107 */       return this.columnLast;
/*  65:    */     }
/*  66:    */     
/*  67:    */     int getLastRow()
/*  68:    */     {
/*  69:108 */       return this.rowLast;
/*  70:    */     }
/*  71:    */     
/*  72:    */     int getExternalSheet()
/*  73:    */     {
/*  74:109 */       return this.externalSheet;
/*  75:    */     }
/*  76:    */     
/*  77:    */     void incrementFirstRow()
/*  78:    */     {
/*  79:111 */       this.rowFirst += 1;
/*  80:    */     }
/*  81:    */     
/*  82:    */     void incrementLastRow()
/*  83:    */     {
/*  84:112 */       this.rowLast += 1;
/*  85:    */     }
/*  86:    */     
/*  87:    */     void decrementFirstRow()
/*  88:    */     {
/*  89:113 */       this.rowFirst -= 1;
/*  90:    */     }
/*  91:    */     
/*  92:    */     void decrementLastRow()
/*  93:    */     {
/*  94:114 */       this.rowLast -= 1;
/*  95:    */     }
/*  96:    */     
/*  97:    */     void incrementFirstColumn()
/*  98:    */     {
/*  99:115 */       this.columnFirst += 1;
/* 100:    */     }
/* 101:    */     
/* 102:    */     void incrementLastColumn()
/* 103:    */     {
/* 104:116 */       this.columnLast += 1;
/* 105:    */     }
/* 106:    */     
/* 107:    */     void decrementFirstColumn()
/* 108:    */     {
/* 109:117 */       this.columnFirst -= 1;
/* 110:    */     }
/* 111:    */     
/* 112:    */     void decrementLastColumn()
/* 113:    */     {
/* 114:118 */       this.columnLast -= 1;
/* 115:    */     }
/* 116:    */     
/* 117:    */     byte[] getData()
/* 118:    */     {
/* 119:122 */       byte[] d = new byte[10];
/* 120:    */       
/* 121:    */ 
/* 122:125 */       IntegerHelper.getTwoBytes(this.externalSheet, d, 0);
/* 123:    */       
/* 124:    */ 
/* 125:128 */       IntegerHelper.getTwoBytes(this.rowFirst, d, 2);
/* 126:    */       
/* 127:    */ 
/* 128:131 */       IntegerHelper.getTwoBytes(this.rowLast, d, 4);
/* 129:    */       
/* 130:    */ 
/* 131:134 */       IntegerHelper.getTwoBytes(this.columnFirst & 0xFF, d, 6);
/* 132:    */       
/* 133:    */ 
/* 134:137 */       IntegerHelper.getTwoBytes(this.columnLast & 0xFF, d, 8);
/* 135:    */       
/* 136:139 */       return d;
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:155 */   private static final NameRange EMPTY_RANGE = new NameRange(0, 0, 0, 0, 0);
/* 141:    */   
/* 142:    */   public NameRecord(jxl.read.biff.NameRecord sr, int ind)
/* 143:    */   {
/* 144:164 */     super(Type.NAME);
/* 145:    */     
/* 146:166 */     this.data = sr.getData();
/* 147:167 */     this.name = sr.getName();
/* 148:168 */     this.sheetRef = sr.getSheetRef();
/* 149:169 */     this.index = ind;
/* 150:170 */     this.modified = false;
/* 151:    */     
/* 152:    */ 
/* 153:173 */     jxl.read.biff.NameRecord.NameRange[] r = sr.getRanges();
/* 154:174 */     this.ranges = new NameRange[r.length];
/* 155:175 */     for (int i = 0; i < this.ranges.length; i++) {
/* 156:177 */       this.ranges[i] = new NameRange(r[i]);
/* 157:    */     }
/* 158:    */   }
/* 159:    */   
/* 160:    */   NameRecord(String theName, int theIndex, int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol, boolean global)
/* 161:    */   {
/* 162:202 */     super(Type.NAME);
/* 163:    */     
/* 164:204 */     this.name = theName;
/* 165:205 */     this.index = theIndex;
/* 166:206 */     this.sheetRef = (global ? 0 : this.index + 1);
/* 167:    */     
/* 168:    */ 
/* 169:209 */     this.ranges = new NameRange[1];
/* 170:210 */     this.ranges[0] = new NameRange(extSheet, 
/* 171:211 */       theStartRow, 
/* 172:212 */       theEndRow, 
/* 173:213 */       theStartCol, 
/* 174:214 */       theEndCol);
/* 175:215 */     this.modified = true;
/* 176:    */   }
/* 177:    */   
/* 178:    */   NameRecord(BuiltInName theName, int theIndex, int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol, boolean global)
/* 179:    */   {
/* 180:239 */     super(Type.NAME);
/* 181:    */     
/* 182:241 */     this.builtInName = theName;
/* 183:242 */     this.index = theIndex;
/* 184:243 */     this.sheetRef = (global ? 0 : this.index + 1);
/* 185:    */     
/* 186:    */ 
/* 187:246 */     this.ranges = new NameRange[1];
/* 188:247 */     this.ranges[0] = new NameRange(extSheet, 
/* 189:248 */       theStartRow, 
/* 190:249 */       theEndRow, 
/* 191:250 */       theStartCol, 
/* 192:251 */       theEndCol);
/* 193:    */   }
/* 194:    */   
/* 195:    */   NameRecord(BuiltInName theName, int theIndex, int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol, int theStartRow2, int theEndRow2, int theStartCol2, int theEndCol2, boolean global)
/* 196:    */   {
/* 197:283 */     super(Type.NAME);
/* 198:    */     
/* 199:285 */     this.builtInName = theName;
/* 200:286 */     this.index = theIndex;
/* 201:287 */     this.sheetRef = (global ? 0 : this.index + 1);
/* 202:    */     
/* 203:    */ 
/* 204:290 */     this.ranges = new NameRange[2];
/* 205:291 */     this.ranges[0] = new NameRange(extSheet, 
/* 206:292 */       theStartRow, 
/* 207:293 */       theEndRow, 
/* 208:294 */       theStartCol, 
/* 209:295 */       theEndCol);
/* 210:296 */     this.ranges[1] = new NameRange(extSheet, 
/* 211:297 */       theStartRow2, 
/* 212:298 */       theEndRow2, 
/* 213:299 */       theStartCol2, 
/* 214:300 */       theEndCol2);
/* 215:    */   }
/* 216:    */   
/* 217:    */   public byte[] getData()
/* 218:    */   {
/* 219:311 */     if ((this.data != null) && (!this.modified)) {
/* 220:314 */       return this.data;
/* 221:    */     }
/* 222:317 */     int NAME_HEADER_LENGTH = 15;
/* 223:318 */     byte AREA_RANGE_LENGTH = 11;
/* 224:319 */     byte AREA_REFERENCE = 59;
/* 225:    */     int detailLength;
/* 226:    */     int detailLength;
/* 227:323 */     if (this.ranges.length > 1) {
/* 228:325 */       detailLength = this.ranges.length * 11 + 4;
/* 229:    */     } else {
/* 230:329 */       detailLength = 11;
/* 231:    */     }
/* 232:332 */     int length = 15 + detailLength;
/* 233:333 */     length += (this.builtInName != null ? 1 : this.name.length());
/* 234:334 */     this.data = new byte[length];
/* 235:    */     
/* 236:    */ 
/* 237:337 */     int options = 0;
/* 238:339 */     if (this.builtInName != null) {
/* 239:341 */       options |= 0x20;
/* 240:    */     }
/* 241:343 */     IntegerHelper.getTwoBytes(options, this.data, 0);
/* 242:    */     
/* 243:    */ 
/* 244:346 */     this.data[2] = 0;
/* 245:349 */     if (this.builtInName != null) {
/* 246:351 */       this.data[3] = 1;
/* 247:    */     } else {
/* 248:355 */       this.data[3] = ((byte)this.name.length());
/* 249:    */     }
/* 250:359 */     IntegerHelper.getTwoBytes(detailLength, this.data, 4);
/* 251:    */     
/* 252:    */ 
/* 253:362 */     IntegerHelper.getTwoBytes(this.sheetRef, this.data, 6);
/* 254:363 */     IntegerHelper.getTwoBytes(this.sheetRef, this.data, 8);
/* 255:369 */     if (this.builtInName != null) {
/* 256:371 */       this.data[15] = ((byte)this.builtInName.getValue());
/* 257:    */     } else {
/* 258:375 */       StringHelper.getBytes(this.name, this.data, 15);
/* 259:    */     }
/* 260:379 */     int pos = this.builtInName != null ? 16 : this.name.length() + 15;
/* 261:384 */     if (this.ranges.length > 1)
/* 262:    */     {
/* 263:386 */       this.data[(pos++)] = 41;
/* 264:    */       
/* 265:388 */       IntegerHelper.getTwoBytes(detailLength - 3, this.data, pos);
/* 266:389 */       pos += 2;
/* 267:391 */       for (int i = 0; i < this.ranges.length; i++)
/* 268:    */       {
/* 269:393 */         this.data[(pos++)] = 59;
/* 270:394 */         byte[] rd = this.ranges[i].getData();
/* 271:395 */         System.arraycopy(rd, 0, this.data, pos, rd.length);
/* 272:396 */         pos += rd.length;
/* 273:    */       }
/* 274:398 */       this.data[pos] = 16;
/* 275:    */     }
/* 276:    */     else
/* 277:    */     {
/* 278:403 */       this.data[pos] = 59;
/* 279:    */       
/* 280:    */ 
/* 281:406 */       byte[] rd = this.ranges[0].getData();
/* 282:407 */       System.arraycopy(rd, 0, this.data, pos + 1, rd.length);
/* 283:    */     }
/* 284:410 */     return this.data;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public String getName()
/* 288:    */   {
/* 289:420 */     return this.name;
/* 290:    */   }
/* 291:    */   
/* 292:    */   public int getIndex()
/* 293:    */   {
/* 294:430 */     return this.index;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public int getSheetRef()
/* 298:    */   {
/* 299:441 */     return this.sheetRef;
/* 300:    */   }
/* 301:    */   
/* 302:    */   public void setSheetRef(int i)
/* 303:    */   {
/* 304:451 */     this.sheetRef = i;
/* 305:452 */     IntegerHelper.getTwoBytes(this.sheetRef, this.data, 8);
/* 306:    */   }
/* 307:    */   
/* 308:    */   public NameRange[] getRanges()
/* 309:    */   {
/* 310:461 */     return this.ranges;
/* 311:    */   }
/* 312:    */   
/* 313:    */   void rowInserted(int sheetIndex, int row)
/* 314:    */   {
/* 315:472 */     for (int i = 0; i < this.ranges.length; i++) {
/* 316:474 */       if (sheetIndex == this.ranges[i].getExternalSheet())
/* 317:    */       {
/* 318:479 */         if (row <= this.ranges[i].getFirstRow())
/* 319:    */         {
/* 320:481 */           this.ranges[i].incrementFirstRow();
/* 321:482 */           this.modified = true;
/* 322:    */         }
/* 323:485 */         if (row <= this.ranges[i].getLastRow())
/* 324:    */         {
/* 325:487 */           this.ranges[i].incrementLastRow();
/* 326:488 */           this.modified = true;
/* 327:    */         }
/* 328:    */       }
/* 329:    */     }
/* 330:    */   }
/* 331:    */   
/* 332:    */   boolean rowRemoved(int sheetIndex, int row)
/* 333:    */   {
/* 334:502 */     for (int i = 0; i < this.ranges.length; i++) {
/* 335:504 */       if (sheetIndex == this.ranges[i].getExternalSheet())
/* 336:    */       {
/* 337:509 */         if ((row == this.ranges[i].getFirstRow()) && (row == this.ranges[i].getLastRow())) {
/* 338:512 */           this.ranges[i] = EMPTY_RANGE;
/* 339:    */         }
/* 340:515 */         if ((row < this.ranges[i].getFirstRow()) && (row > 0))
/* 341:    */         {
/* 342:517 */           this.ranges[i].decrementFirstRow();
/* 343:518 */           this.modified = true;
/* 344:    */         }
/* 345:521 */         if (row <= this.ranges[i].getLastRow())
/* 346:    */         {
/* 347:523 */           this.ranges[i].decrementLastRow();
/* 348:524 */           this.modified = true;
/* 349:    */         }
/* 350:    */       }
/* 351:    */     }
/* 352:529 */     int emptyRanges = 0;
/* 353:530 */     for (int i = 0; i < this.ranges.length; i++) {
/* 354:532 */       if (this.ranges[i] == EMPTY_RANGE) {
/* 355:534 */         emptyRanges++;
/* 356:    */       }
/* 357:    */     }
/* 358:538 */     if (emptyRanges == this.ranges.length) {
/* 359:540 */       return true;
/* 360:    */     }
/* 361:544 */     NameRange[] newRanges = new NameRange[this.ranges.length - emptyRanges];
/* 362:545 */     for (int i = 0; i < this.ranges.length; i++) {
/* 363:547 */       if (this.ranges[i] != EMPTY_RANGE) {
/* 364:549 */         newRanges[i] = this.ranges[i];
/* 365:    */       }
/* 366:    */     }
/* 367:553 */     this.ranges = newRanges;
/* 368:    */     
/* 369:555 */     return false;
/* 370:    */   }
/* 371:    */   
/* 372:    */   boolean columnRemoved(int sheetIndex, int col)
/* 373:    */   {
/* 374:567 */     for (int i = 0; i < this.ranges.length; i++) {
/* 375:569 */       if (sheetIndex == this.ranges[i].getExternalSheet())
/* 376:    */       {
/* 377:574 */         if ((col == this.ranges[i].getFirstColumn()) && 
/* 378:575 */           (col == this.ranges[i].getLastColumn())) {
/* 379:578 */           this.ranges[i] = EMPTY_RANGE;
/* 380:    */         }
/* 381:581 */         if ((col < this.ranges[i].getFirstColumn()) && (col > 0))
/* 382:    */         {
/* 383:583 */           this.ranges[i].decrementFirstColumn();
/* 384:584 */           this.modified = true;
/* 385:    */         }
/* 386:587 */         if (col <= this.ranges[i].getLastColumn())
/* 387:    */         {
/* 388:589 */           this.ranges[i].decrementLastColumn();
/* 389:590 */           this.modified = true;
/* 390:    */         }
/* 391:    */       }
/* 392:    */     }
/* 393:595 */     int emptyRanges = 0;
/* 394:596 */     for (int i = 0; i < this.ranges.length; i++) {
/* 395:598 */       if (this.ranges[i] == EMPTY_RANGE) {
/* 396:600 */         emptyRanges++;
/* 397:    */       }
/* 398:    */     }
/* 399:604 */     if (emptyRanges == this.ranges.length) {
/* 400:606 */       return true;
/* 401:    */     }
/* 402:610 */     NameRange[] newRanges = new NameRange[this.ranges.length - emptyRanges];
/* 403:611 */     for (int i = 0; i < this.ranges.length; i++) {
/* 404:613 */       if (this.ranges[i] != EMPTY_RANGE) {
/* 405:615 */         newRanges[i] = this.ranges[i];
/* 406:    */       }
/* 407:    */     }
/* 408:619 */     this.ranges = newRanges;
/* 409:    */     
/* 410:621 */     return false;
/* 411:    */   }
/* 412:    */   
/* 413:    */   void columnInserted(int sheetIndex, int col)
/* 414:    */   {
/* 415:633 */     for (int i = 0; i < this.ranges.length; i++) {
/* 416:635 */       if (sheetIndex == this.ranges[i].getExternalSheet())
/* 417:    */       {
/* 418:640 */         if (col <= this.ranges[i].getFirstColumn())
/* 419:    */         {
/* 420:642 */           this.ranges[i].incrementFirstColumn();
/* 421:643 */           this.modified = true;
/* 422:    */         }
/* 423:646 */         if (col <= this.ranges[i].getLastColumn())
/* 424:    */         {
/* 425:648 */           this.ranges[i].incrementLastColumn();
/* 426:649 */           this.modified = true;
/* 427:    */         }
/* 428:    */       }
/* 429:    */     }
/* 430:    */   }
/* 431:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.NameRecord
 * JD-Core Version:    0.7.0.1
 */