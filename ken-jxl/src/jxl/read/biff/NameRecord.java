/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.biff.BuiltInName;
/*   6:    */ import jxl.biff.IntegerHelper;
/*   7:    */ import jxl.biff.RecordData;
/*   8:    */ import jxl.biff.StringHelper;
/*   9:    */ import jxl.common.Assert;
/*  10:    */ import jxl.common.Logger;
/*  11:    */ 
/*  12:    */ public class NameRecord
/*  13:    */   extends RecordData
/*  14:    */ {
/*  15: 42 */   private static Logger logger = Logger.getLogger(NameRecord.class);
/*  16:    */   private String name;
/*  17:    */   private BuiltInName builtInName;
/*  18:    */   private int index;
/*  19: 63 */   private int sheetRef = 0;
/*  20:    */   private boolean isbiff8;
/*  21: 74 */   public static Biff7 biff7 = new Biff7(null);
/*  22:    */   private static final int commandMacro = 12;
/*  23:    */   private static final int builtIn = 32;
/*  24:    */   private static final int cellReference = 58;
/*  25:    */   private static final int areaReference = 59;
/*  26:    */   private static final int subExpression = 41;
/*  27:    */   private static final int union = 16;
/*  28:    */   private ArrayList ranges;
/*  29:    */   
/*  30:    */   private static class Biff7 {}
/*  31:    */   
/*  32:    */   public class NameRange
/*  33:    */   {
/*  34:    */     private int columnFirst;
/*  35:    */     private int rowFirst;
/*  36:    */     private int columnLast;
/*  37:    */     private int rowLast;
/*  38:    */     private int externalSheet;
/*  39:    */     
/*  40:    */     NameRange(int s1, int c1, int r1, int c2, int r2)
/*  41:    */     {
/*  42:127 */       this.columnFirst = c1;
/*  43:128 */       this.rowFirst = r1;
/*  44:129 */       this.columnLast = c2;
/*  45:130 */       this.rowLast = r2;
/*  46:131 */       this.externalSheet = s1;
/*  47:    */     }
/*  48:    */     
/*  49:    */     public int getFirstColumn()
/*  50:    */     {
/*  51:141 */       return this.columnFirst;
/*  52:    */     }
/*  53:    */     
/*  54:    */     public int getFirstRow()
/*  55:    */     {
/*  56:151 */       return this.rowFirst;
/*  57:    */     }
/*  58:    */     
/*  59:    */     public int getLastColumn()
/*  60:    */     {
/*  61:161 */       return this.columnLast;
/*  62:    */     }
/*  63:    */     
/*  64:    */     public int getLastRow()
/*  65:    */     {
/*  66:171 */       return this.rowLast;
/*  67:    */     }
/*  68:    */     
/*  69:    */     public int getExternalSheet()
/*  70:    */     {
/*  71:181 */       return this.externalSheet;
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   NameRecord(Record t, WorkbookSettings ws, int ind)
/*  76:    */   {
/*  77:199 */     super(t);
/*  78:200 */     this.index = ind;
/*  79:201 */     this.isbiff8 = true;
/*  80:    */     try
/*  81:    */     {
/*  82:205 */       this.ranges = new ArrayList();
/*  83:    */       
/*  84:207 */       byte[] data = getRecord().getData();
/*  85:208 */       int option = IntegerHelper.getInt(data[0], data[1]);
/*  86:209 */       int length = data[3];
/*  87:210 */       this.sheetRef = IntegerHelper.getInt(data[8], data[9]);
/*  88:212 */       if ((option & 0x20) != 0) {
/*  89:214 */         this.builtInName = BuiltInName.getBuiltInName(data[15]);
/*  90:    */       } else {
/*  91:218 */         this.name = StringHelper.getString(data, length, 15, ws);
/*  92:    */       }
/*  93:221 */       if ((option & 0xC) != 0) {
/*  94:224 */         return;
/*  95:    */       }
/*  96:227 */       int pos = length + 15;
/*  97:229 */       if (data[pos] == 58)
/*  98:    */       {
/*  99:231 */         int sheet = IntegerHelper.getInt(data[(pos + 1)], data[(pos + 2)]);
/* 100:232 */         int row = IntegerHelper.getInt(data[(pos + 3)], data[(pos + 4)]);
/* 101:233 */         int columnMask = IntegerHelper.getInt(data[(pos + 5)], data[(pos + 6)]);
/* 102:234 */         int column = columnMask & 0xFF;
/* 103:    */         
/* 104:    */ 
/* 105:237 */         Assert.verify((columnMask & 0xC0000) == 0);
/* 106:    */         
/* 107:239 */         NameRange r = new NameRange(sheet, column, row, column, row);
/* 108:240 */         this.ranges.add(r);
/* 109:    */       }
/* 110:242 */       else if (data[pos] == 59)
/* 111:    */       {
/* 112:244 */         int sheet1 = 0;
/* 113:245 */         int r1 = 0;
/* 114:246 */         int columnMask = 0;
/* 115:247 */         int c1 = 0;
/* 116:248 */         int r2 = 0;
/* 117:249 */         int c2 = 0;
/* 118:250 */         NameRange range = null;
/* 119:252 */         while (pos < data.length)
/* 120:    */         {
/* 121:254 */           sheet1 = IntegerHelper.getInt(data[(pos + 1)], data[(pos + 2)]);
/* 122:255 */           r1 = IntegerHelper.getInt(data[(pos + 3)], data[(pos + 4)]);
/* 123:256 */           r2 = IntegerHelper.getInt(data[(pos + 5)], data[(pos + 6)]);
/* 124:    */           
/* 125:258 */           columnMask = IntegerHelper.getInt(data[(pos + 7)], data[(pos + 8)]);
/* 126:259 */           c1 = columnMask & 0xFF;
/* 127:    */           
/* 128:    */ 
/* 129:262 */           Assert.verify((columnMask & 0xC0000) == 0);
/* 130:    */           
/* 131:264 */           columnMask = IntegerHelper.getInt(data[(pos + 9)], data[(pos + 10)]);
/* 132:265 */           c2 = columnMask & 0xFF;
/* 133:    */           
/* 134:    */ 
/* 135:268 */           Assert.verify((columnMask & 0xC0000) == 0);
/* 136:    */           
/* 137:270 */           range = new NameRange(sheet1, c1, r1, c2, r2);
/* 138:271 */           this.ranges.add(range);
/* 139:    */           
/* 140:273 */           pos += 11;
/* 141:    */         }
/* 142:    */       }
/* 143:276 */       else if (data[pos] == 41)
/* 144:    */       {
/* 145:278 */         int sheet1 = 0;
/* 146:279 */         int r1 = 0;
/* 147:280 */         int columnMask = 0;
/* 148:281 */         int c1 = 0;
/* 149:282 */         int r2 = 0;
/* 150:283 */         int c2 = 0;
/* 151:284 */         NameRange range = null;
/* 152:287 */         if ((pos < data.length) && 
/* 153:288 */           (data[pos] != 58) && 
/* 154:289 */           (data[pos] != 59)) {
/* 155:291 */           if (data[pos] == 41) {
/* 156:293 */             pos += 3;
/* 157:295 */           } else if (data[pos] == 16) {
/* 158:297 */             pos++;
/* 159:    */           }
/* 160:    */         }
/* 161:301 */         while (pos < data.length)
/* 162:    */         {
/* 163:303 */           sheet1 = IntegerHelper.getInt(data[(pos + 1)], data[(pos + 2)]);
/* 164:304 */           r1 = IntegerHelper.getInt(data[(pos + 3)], data[(pos + 4)]);
/* 165:305 */           r2 = IntegerHelper.getInt(data[(pos + 5)], data[(pos + 6)]);
/* 166:    */           
/* 167:307 */           columnMask = IntegerHelper.getInt(data[(pos + 7)], data[(pos + 8)]);
/* 168:308 */           c1 = columnMask & 0xFF;
/* 169:    */           
/* 170:    */ 
/* 171:311 */           Assert.verify((columnMask & 0xC0000) == 0);
/* 172:    */           
/* 173:313 */           columnMask = IntegerHelper.getInt(data[(pos + 9)], data[(pos + 10)]);
/* 174:314 */           c2 = columnMask & 0xFF;
/* 175:    */           
/* 176:    */ 
/* 177:317 */           Assert.verify((columnMask & 0xC0000) == 0);
/* 178:    */           
/* 179:319 */           range = new NameRange(sheet1, c1, r1, c2, r2);
/* 180:320 */           this.ranges.add(range);
/* 181:    */           
/* 182:322 */           pos += 11;
/* 183:325 */           if ((pos < data.length) && 
/* 184:326 */             (data[pos] != 58) && 
/* 185:327 */             (data[pos] != 59)) {
/* 186:329 */             if (data[pos] == 41) {
/* 187:331 */               pos += 3;
/* 188:333 */             } else if (data[pos] == 16) {
/* 189:335 */               pos++;
/* 190:    */             }
/* 191:    */           }
/* 192:    */         }
/* 193:    */       }
/* 194:    */       else
/* 195:    */       {
/* 196:342 */         String n = this.name != null ? this.name : this.builtInName.getName();
/* 197:343 */         logger.warn("Cannot read name ranges for " + n + 
/* 198:344 */           " - setting to empty");
/* 199:345 */         NameRange range = new NameRange(0, 0, 0, 0, 0);
/* 200:346 */         this.ranges.add(range);
/* 201:    */       }
/* 202:    */     }
/* 203:    */     catch (Throwable t1)
/* 204:    */     {
/* 205:354 */       logger.warn("Cannot read name");
/* 206:355 */       this.name = "ERROR";
/* 207:    */     }
/* 208:    */   }
/* 209:    */   
/* 210:    */   NameRecord(Record t, WorkbookSettings ws, int ind, Biff7 dummy)
/* 211:    */   {
/* 212:369 */     super(t);
/* 213:370 */     this.index = ind;
/* 214:371 */     this.isbiff8 = false;
/* 215:    */     try
/* 216:    */     {
/* 217:375 */       this.ranges = new ArrayList();
/* 218:376 */       byte[] data = getRecord().getData();
/* 219:377 */       int length = data[3];
/* 220:378 */       this.sheetRef = IntegerHelper.getInt(data[8], data[9]);
/* 221:379 */       this.name = StringHelper.getString(data, length, 14, ws);
/* 222:    */       
/* 223:381 */       int pos = length + 14;
/* 224:383 */       if (pos >= data.length) {
/* 225:386 */         return;
/* 226:    */       }
/* 227:389 */       if (data[pos] == 58)
/* 228:    */       {
/* 229:391 */         int sheet = IntegerHelper.getInt(data[(pos + 11)], data[(pos + 12)]);
/* 230:392 */         int row = IntegerHelper.getInt(data[(pos + 15)], data[(pos + 16)]);
/* 231:393 */         int column = data[(pos + 17)];
/* 232:    */         
/* 233:395 */         NameRange r = new NameRange(sheet, column, row, column, row);
/* 234:396 */         this.ranges.add(r);
/* 235:    */       }
/* 236:398 */       else if (data[pos] == 59)
/* 237:    */       {
/* 238:400 */         int sheet1 = 0;
/* 239:401 */         int r1 = 0;
/* 240:402 */         int c1 = 0;
/* 241:403 */         int r2 = 0;
/* 242:404 */         int c2 = 0;
/* 243:405 */         NameRange range = null;
/* 244:407 */         while (pos < data.length)
/* 245:    */         {
/* 246:409 */           sheet1 = IntegerHelper.getInt(data[(pos + 11)], data[(pos + 12)]);
/* 247:410 */           r1 = IntegerHelper.getInt(data[(pos + 15)], data[(pos + 16)]);
/* 248:411 */           r2 = IntegerHelper.getInt(data[(pos + 17)], data[(pos + 18)]);
/* 249:    */           
/* 250:413 */           c1 = data[(pos + 19)];
/* 251:414 */           c2 = data[(pos + 20)];
/* 252:    */           
/* 253:416 */           range = new NameRange(sheet1, c1, r1, c2, r2);
/* 254:417 */           this.ranges.add(range);
/* 255:    */           
/* 256:419 */           pos += 21;
/* 257:    */         }
/* 258:    */       }
/* 259:422 */       else if (data[pos] == 41)
/* 260:    */       {
/* 261:424 */         int sheet1 = 0;
/* 262:425 */         int sheet2 = 0;
/* 263:426 */         int r1 = 0;
/* 264:427 */         int c1 = 0;
/* 265:428 */         int r2 = 0;
/* 266:429 */         int c2 = 0;
/* 267:430 */         NameRange range = null;
/* 268:433 */         if ((pos < data.length) && 
/* 269:434 */           (data[pos] != 58) && 
/* 270:435 */           (data[pos] != 59)) {
/* 271:437 */           if (data[pos] == 41) {
/* 272:439 */             pos += 3;
/* 273:441 */           } else if (data[pos] == 16) {
/* 274:443 */             pos++;
/* 275:    */           }
/* 276:    */         }
/* 277:447 */         while (pos < data.length)
/* 278:    */         {
/* 279:449 */           sheet1 = IntegerHelper.getInt(data[(pos + 11)], data[(pos + 12)]);
/* 280:450 */           r1 = IntegerHelper.getInt(data[(pos + 15)], data[(pos + 16)]);
/* 281:451 */           r2 = IntegerHelper.getInt(data[(pos + 17)], data[(pos + 18)]);
/* 282:    */           
/* 283:453 */           c1 = data[(pos + 19)];
/* 284:454 */           c2 = data[(pos + 20)];
/* 285:    */           
/* 286:456 */           range = new NameRange(sheet1, c1, r1, c2, r2);
/* 287:457 */           this.ranges.add(range);
/* 288:    */           
/* 289:459 */           pos += 21;
/* 290:462 */           if ((pos < data.length) && 
/* 291:463 */             (data[pos] != 58) && 
/* 292:464 */             (data[pos] != 59)) {
/* 293:466 */             if (data[pos] == 41) {
/* 294:468 */               pos += 3;
/* 295:470 */             } else if (data[pos] == 16) {
/* 296:472 */               pos++;
/* 297:    */             }
/* 298:    */           }
/* 299:    */         }
/* 300:    */       }
/* 301:    */     }
/* 302:    */     catch (Throwable t1)
/* 303:    */     {
/* 304:483 */       logger.warn("Cannot read name.");
/* 305:484 */       this.name = "ERROR";
/* 306:    */     }
/* 307:    */   }
/* 308:    */   
/* 309:    */   public String getName()
/* 310:    */   {
/* 311:495 */     return this.name;
/* 312:    */   }
/* 313:    */   
/* 314:    */   public BuiltInName getBuiltInName()
/* 315:    */   {
/* 316:505 */     return this.builtInName;
/* 317:    */   }
/* 318:    */   
/* 319:    */   public NameRange[] getRanges()
/* 320:    */   {
/* 321:516 */     NameRange[] nr = new NameRange[this.ranges.size()];
/* 322:517 */     return (NameRange[])this.ranges.toArray(nr);
/* 323:    */   }
/* 324:    */   
/* 325:    */   int getIndex()
/* 326:    */   {
/* 327:527 */     return this.index;
/* 328:    */   }
/* 329:    */   
/* 330:    */   public int getSheetRef()
/* 331:    */   {
/* 332:538 */     return this.sheetRef;
/* 333:    */   }
/* 334:    */   
/* 335:    */   public void setSheetRef(int i)
/* 336:    */   {
/* 337:547 */     this.sheetRef = i;
/* 338:    */   }
/* 339:    */   
/* 340:    */   public byte[] getData()
/* 341:    */   {
/* 342:557 */     return getRecord().getData();
/* 343:    */   }
/* 344:    */   
/* 345:    */   public boolean isBiff8()
/* 346:    */   {
/* 347:567 */     return this.isbiff8;
/* 348:    */   }
/* 349:    */   
/* 350:    */   public boolean isGlobal()
/* 351:    */   {
/* 352:577 */     return this.sheetRef == 0;
/* 353:    */   }
/* 354:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.NameRecord
 * JD-Core Version:    0.7.0.1
 */