/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import java.text.DateFormat;
/*   4:    */ import java.text.DecimalFormat;
/*   5:    */ import java.text.NumberFormat;
/*   6:    */ import java.text.SimpleDateFormat;
/*   7:    */ import jxl.WorkbookSettings;
/*   8:    */ import jxl.common.Logger;
/*   9:    */ import jxl.read.biff.Record;
/*  10:    */ 
/*  11:    */ public class FormatRecord
/*  12:    */   extends WritableRecordData
/*  13:    */   implements DisplayFormat, jxl.format.Format
/*  14:    */ {
/*  15: 42 */   public static Logger logger = Logger.getLogger(FormatRecord.class);
/*  16:    */   private boolean initialized;
/*  17:    */   private byte[] data;
/*  18:    */   private int indexCode;
/*  19:    */   private String formatString;
/*  20:    */   private boolean date;
/*  21:    */   private boolean number;
/*  22:    */   private java.text.Format format;
/*  23: 83 */   private static String[] dateStrings = {
/*  24: 84 */     "dd", 
/*  25: 85 */     "mm", 
/*  26: 86 */     "yy", 
/*  27: 87 */     "hh", 
/*  28: 88 */     "ss", 
/*  29: 89 */     "m/", 
/*  30: 90 */     "/d" };
/*  31: 98 */   public static final BiffType biff8 = new BiffType(null);
/*  32: 99 */   public static final BiffType biff7 = new BiffType(null);
/*  33:    */   
/*  34:    */   FormatRecord(String fmt, int refno)
/*  35:    */   {
/*  36:109 */     super(Type.FORMAT);
/*  37:110 */     this.formatString = fmt;
/*  38:111 */     this.indexCode = refno;
/*  39:112 */     this.initialized = true;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected FormatRecord()
/*  43:    */   {
/*  44:120 */     super(Type.FORMAT);
/*  45:121 */     this.initialized = false;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected FormatRecord(FormatRecord fr)
/*  49:    */   {
/*  50:131 */     super(Type.FORMAT);
/*  51:132 */     this.initialized = false;
/*  52:    */     
/*  53:134 */     this.formatString = fr.formatString;
/*  54:135 */     this.date = fr.date;
/*  55:136 */     this.number = fr.number;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public FormatRecord(Record t, WorkbookSettings ws, BiffType biffType)
/*  59:    */   {
/*  60:150 */     super(t);
/*  61:    */     
/*  62:152 */     byte[] data = getRecord().getData();
/*  63:153 */     this.indexCode = IntegerHelper.getInt(data[0], data[1]);
/*  64:154 */     this.initialized = true;
/*  65:156 */     if (biffType == biff8)
/*  66:    */     {
/*  67:158 */       int numchars = IntegerHelper.getInt(data[2], data[3]);
/*  68:159 */       if (data[4] == 0) {
/*  69:161 */         this.formatString = StringHelper.getString(data, numchars, 5, ws);
/*  70:    */       } else {
/*  71:165 */         this.formatString = StringHelper.getUnicodeString(data, numchars, 5);
/*  72:    */       }
/*  73:    */     }
/*  74:    */     else
/*  75:    */     {
/*  76:170 */       int numchars = data[2];
/*  77:171 */       byte[] chars = new byte[numchars];
/*  78:172 */       System.arraycopy(data, 3, chars, 0, chars.length);
/*  79:173 */       this.formatString = new String(chars);
/*  80:    */     }
/*  81:176 */     this.date = false;
/*  82:177 */     this.number = false;
/*  83:180 */     for (int i = 0; i < dateStrings.length; i++)
/*  84:    */     {
/*  85:182 */       String dateString = dateStrings[i];
/*  86:183 */       if ((this.formatString.indexOf(dateString) != -1) || 
/*  87:184 */         (this.formatString.indexOf(dateString.toUpperCase()) != -1))
/*  88:    */       {
/*  89:186 */         this.date = true;
/*  90:187 */         break;
/*  91:    */       }
/*  92:    */     }
/*  93:192 */     if (!this.date) {
/*  94:194 */       if ((this.formatString.indexOf('#') != -1) || 
/*  95:195 */         (this.formatString.indexOf('0') != -1)) {
/*  96:197 */         this.number = true;
/*  97:    */       }
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   public byte[] getData()
/* 102:    */   {
/* 103:209 */     this.data = new byte[this.formatString.length() * 2 + 3 + 2];
/* 104:    */     
/* 105:211 */     IntegerHelper.getTwoBytes(this.indexCode, this.data, 0);
/* 106:212 */     IntegerHelper.getTwoBytes(this.formatString.length(), this.data, 2);
/* 107:213 */     this.data[4] = 1;
/* 108:214 */     StringHelper.getUnicodeBytes(this.formatString, this.data, 5);
/* 109:    */     
/* 110:216 */     return this.data;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public int getFormatIndex()
/* 114:    */   {
/* 115:226 */     return this.indexCode;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public boolean isInitialized()
/* 119:    */   {
/* 120:236 */     return this.initialized;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void initialize(int pos)
/* 124:    */   {
/* 125:248 */     this.indexCode = pos;
/* 126:249 */     this.initialized = true;
/* 127:    */   }
/* 128:    */   
/* 129:    */   protected final String replace(String input, String search, String replace)
/* 130:    */   {
/* 131:263 */     String fmtstr = input;
/* 132:264 */     int pos = fmtstr.indexOf(search);
/* 133:265 */     while (pos != -1)
/* 134:    */     {
/* 135:267 */       StringBuffer tmp = new StringBuffer(fmtstr.substring(0, pos));
/* 136:268 */       tmp.append(replace);
/* 137:269 */       tmp.append(fmtstr.substring(pos + search.length()));
/* 138:270 */       fmtstr = tmp.toString();
/* 139:271 */       pos = fmtstr.indexOf(search);
/* 140:    */     }
/* 141:273 */     return fmtstr;
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected final void setFormatString(String s)
/* 145:    */   {
/* 146:284 */     this.formatString = s;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public final boolean isDate()
/* 150:    */   {
/* 151:294 */     return this.date;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public final boolean isNumber()
/* 155:    */   {
/* 156:304 */     return this.number;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public final NumberFormat getNumberFormat()
/* 160:    */   {
/* 161:314 */     if ((this.format != null) && ((this.format instanceof NumberFormat))) {
/* 162:316 */       return (NumberFormat)this.format;
/* 163:    */     }
/* 164:    */     try
/* 165:    */     {
/* 166:321 */       String fs = this.formatString;
/* 167:    */       
/* 168:    */ 
/* 169:324 */       fs = replace(fs, "E+", "E");
/* 170:325 */       fs = replace(fs, "_)", "");
/* 171:326 */       fs = replace(fs, "_", "");
/* 172:327 */       fs = replace(fs, "[Red]", "");
/* 173:328 */       fs = replace(fs, "\\", "");
/* 174:    */       
/* 175:330 */       this.format = new DecimalFormat(fs);
/* 176:    */     }
/* 177:    */     catch (IllegalArgumentException e)
/* 178:    */     {
/* 179:336 */       this.format = new DecimalFormat("#.###");
/* 180:    */     }
/* 181:339 */     return (NumberFormat)this.format;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public final DateFormat getDateFormat()
/* 185:    */   {
/* 186:349 */     if ((this.format != null) && ((this.format instanceof DateFormat))) {
/* 187:351 */       return (DateFormat)this.format;
/* 188:    */     }
/* 189:354 */     String fmt = this.formatString;
/* 190:    */     
/* 191:    */ 
/* 192:357 */     int pos = fmt.indexOf("AM/PM");
/* 193:358 */     while (pos != -1)
/* 194:    */     {
/* 195:360 */       StringBuffer sb = new StringBuffer(fmt.substring(0, pos));
/* 196:361 */       sb.append('a');
/* 197:362 */       sb.append(fmt.substring(pos + 5));
/* 198:363 */       fmt = sb.toString();
/* 199:364 */       pos = fmt.indexOf("AM/PM");
/* 200:    */     }
/* 201:369 */     pos = fmt.indexOf("ss.0");
/* 202:370 */     while (pos != -1)
/* 203:    */     {
/* 204:372 */       StringBuffer sb = new StringBuffer(fmt.substring(0, pos));
/* 205:373 */       sb.append("ss.SSS");
/* 206:    */       
/* 207:    */ 
/* 208:376 */       pos += 4;
/* 209:377 */       while ((pos < fmt.length()) && (fmt.charAt(pos) == '0')) {
/* 210:379 */         pos++;
/* 211:    */       }
/* 212:382 */       sb.append(fmt.substring(pos));
/* 213:383 */       fmt = sb.toString();
/* 214:384 */       pos = fmt.indexOf("ss.0");
/* 215:    */     }
/* 216:389 */     StringBuffer sb = new StringBuffer();
/* 217:390 */     for (int i = 0; i < fmt.length(); i++) {
/* 218:392 */       if (fmt.charAt(i) != '\\') {
/* 219:394 */         sb.append(fmt.charAt(i));
/* 220:    */       }
/* 221:    */     }
/* 222:398 */     fmt = sb.toString();
/* 223:402 */     if (fmt.charAt(0) == '[')
/* 224:    */     {
/* 225:404 */       int end = fmt.indexOf(']');
/* 226:405 */       if (end != -1) {
/* 227:407 */         fmt = fmt.substring(end + 1);
/* 228:    */       }
/* 229:    */     }
/* 230:412 */     fmt = replace(fmt, ";@", "");
/* 231:    */     
/* 232:    */ 
/* 233:    */ 
/* 234:416 */     char[] formatBytes = fmt.toCharArray();
/* 235:418 */     for (int i = 0; i < formatBytes.length; i++) {
/* 236:420 */       if (formatBytes[i] == 'm') {
/* 237:424 */         if ((i > 0) && ((formatBytes[(i - 1)] == 'm') || (formatBytes[(i - 1)] == 'M')))
/* 238:    */         {
/* 239:426 */           formatBytes[i] = formatBytes[(i - 1)];
/* 240:    */         }
/* 241:    */         else
/* 242:    */         {
/* 243:434 */           int minuteDist = 2147483647;
/* 244:435 */           for (int j = i - 1; j > 0; j--) {
/* 245:437 */             if (formatBytes[j] == 'h')
/* 246:    */             {
/* 247:439 */               minuteDist = i - j;
/* 248:440 */               break;
/* 249:    */             }
/* 250:    */           }
/* 251:444 */           for (int j = i + 1; j < formatBytes.length; j++) {
/* 252:446 */             if (formatBytes[j] == 'h')
/* 253:    */             {
/* 254:448 */               minuteDist = Math.min(minuteDist, j - i);
/* 255:449 */               break;
/* 256:    */             }
/* 257:    */           }
/* 258:453 */           for (int j = i - 1; j > 0; j--) {
/* 259:455 */             if (formatBytes[j] == 'H')
/* 260:    */             {
/* 261:457 */               minuteDist = i - j;
/* 262:458 */               break;
/* 263:    */             }
/* 264:    */           }
/* 265:462 */           for (int j = i + 1; j < formatBytes.length; j++) {
/* 266:464 */             if (formatBytes[j] == 'H')
/* 267:    */             {
/* 268:466 */               minuteDist = Math.min(minuteDist, j - i);
/* 269:467 */               break;
/* 270:    */             }
/* 271:    */           }
/* 272:472 */           for (int j = i - 1; j > 0; j--) {
/* 273:474 */             if (formatBytes[j] == 's')
/* 274:    */             {
/* 275:476 */               minuteDist = Math.min(minuteDist, i - j);
/* 276:477 */               break;
/* 277:    */             }
/* 278:    */           }
/* 279:480 */           for (int j = i + 1; j < formatBytes.length; j++) {
/* 280:482 */             if (formatBytes[j] == 's')
/* 281:    */             {
/* 282:484 */               minuteDist = Math.min(minuteDist, j - i);
/* 283:485 */               break;
/* 284:    */             }
/* 285:    */           }
/* 286:491 */           int monthDist = 2147483647;
/* 287:492 */           for (int j = i - 1; j > 0; j--) {
/* 288:494 */             if (formatBytes[j] == 'd')
/* 289:    */             {
/* 290:496 */               monthDist = i - j;
/* 291:497 */               break;
/* 292:    */             }
/* 293:    */           }
/* 294:501 */           for (int j = i + 1; j < formatBytes.length; j++) {
/* 295:503 */             if (formatBytes[j] == 'd')
/* 296:    */             {
/* 297:505 */               monthDist = Math.min(monthDist, j - i);
/* 298:506 */               break;
/* 299:    */             }
/* 300:    */           }
/* 301:510 */           for (int j = i - 1; j > 0; j--) {
/* 302:512 */             if (formatBytes[j] == 'y')
/* 303:    */             {
/* 304:514 */               monthDist = Math.min(monthDist, i - j);
/* 305:515 */               break;
/* 306:    */             }
/* 307:    */           }
/* 308:518 */           for (int j = i + 1; j < formatBytes.length; j++) {
/* 309:520 */             if (formatBytes[j] == 'y')
/* 310:    */             {
/* 311:522 */               monthDist = Math.min(monthDist, j - i);
/* 312:523 */               break;
/* 313:    */             }
/* 314:    */           }
/* 315:527 */           if (monthDist < minuteDist)
/* 316:    */           {
/* 317:530 */             formatBytes[i] = Character.toUpperCase(formatBytes[i]);
/* 318:    */           }
/* 319:532 */           else if ((monthDist == minuteDist) && 
/* 320:533 */             (monthDist != 2147483647))
/* 321:    */           {
/* 322:537 */             char ind = formatBytes[(i - monthDist)];
/* 323:538 */             if ((ind == 'y') || (ind == 'd')) {
/* 324:541 */               formatBytes[i] = Character.toUpperCase(formatBytes[i]);
/* 325:    */             }
/* 326:    */           }
/* 327:    */         }
/* 328:    */       }
/* 329:    */     }
/* 330:    */     try
/* 331:    */     {
/* 332:550 */       this.format = new SimpleDateFormat(new String(formatBytes));
/* 333:    */     }
/* 334:    */     catch (IllegalArgumentException e)
/* 335:    */     {
/* 336:555 */       this.format = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
/* 337:    */     }
/* 338:557 */     return (DateFormat)this.format;
/* 339:    */   }
/* 340:    */   
/* 341:    */   public int getIndexCode()
/* 342:    */   {
/* 343:567 */     return this.indexCode;
/* 344:    */   }
/* 345:    */   
/* 346:    */   public String getFormatString()
/* 347:    */   {
/* 348:577 */     return this.formatString;
/* 349:    */   }
/* 350:    */   
/* 351:    */   public boolean isBuiltIn()
/* 352:    */   {
/* 353:587 */     return false;
/* 354:    */   }
/* 355:    */   
/* 356:    */   public int hashCode()
/* 357:    */   {
/* 358:596 */     return this.formatString.hashCode();
/* 359:    */   }
/* 360:    */   
/* 361:    */   public boolean equals(Object o)
/* 362:    */   {
/* 363:608 */     if (o == this) {
/* 364:610 */       return true;
/* 365:    */     }
/* 366:613 */     if (!(o instanceof FormatRecord)) {
/* 367:615 */       return false;
/* 368:    */     }
/* 369:618 */     FormatRecord fr = (FormatRecord)o;
/* 370:621 */     if ((this.initialized) && (fr.initialized))
/* 371:    */     {
/* 372:624 */       if ((this.date != fr.date) || 
/* 373:625 */         (this.number != fr.number)) {
/* 374:627 */         return false;
/* 375:    */       }
/* 376:630 */       return this.formatString.equals(fr.formatString);
/* 377:    */     }
/* 378:634 */     return this.formatString.equals(fr.formatString);
/* 379:    */   }
/* 380:    */   
/* 381:    */   private static class BiffType {}
/* 382:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.FormatRecord
 * JD-Core Version:    0.7.0.1
 */