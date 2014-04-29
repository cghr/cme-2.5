/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.text.DateFormat;
/*   5:    */ import java.text.NumberFormat;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import jxl.common.Assert;
/*  10:    */ import jxl.common.Logger;
/*  11:    */ import jxl.format.Colour;
/*  12:    */ import jxl.format.RGB;
/*  13:    */ import jxl.write.biff.File;
/*  14:    */ 
/*  15:    */ public class FormattingRecords
/*  16:    */ {
/*  17: 44 */   private static Logger logger = Logger.getLogger(FormattingRecords.class);
/*  18:    */   private HashMap formats;
/*  19:    */   private ArrayList formatsList;
/*  20:    */   private ArrayList xfRecords;
/*  21:    */   private int nextCustomIndexNumber;
/*  22:    */   private Fonts fonts;
/*  23:    */   private PaletteRecord palette;
/*  24:    */   private static final int customFormatStartIndex = 164;
/*  25:    */   private static final int maxFormatRecordsIndex = 441;
/*  26:    */   private static final int minXFRecords = 21;
/*  27:    */   
/*  28:    */   public FormattingRecords(Fonts f)
/*  29:    */   {
/*  30:101 */     this.xfRecords = new ArrayList(10);
/*  31:102 */     this.formats = new HashMap(10);
/*  32:103 */     this.formatsList = new ArrayList(10);
/*  33:104 */     this.fonts = f;
/*  34:105 */     this.nextCustomIndexNumber = 164;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public final void addStyle(XFRecord xf)
/*  38:    */     throws NumFormatRecordsException
/*  39:    */   {
/*  40:120 */     if (!xf.isInitialized())
/*  41:    */     {
/*  42:122 */       int pos = this.xfRecords.size();
/*  43:123 */       xf.initialize(pos, this, this.fonts);
/*  44:124 */       this.xfRecords.add(xf);
/*  45:    */     }
/*  46:131 */     else if (xf.getXFIndex() >= this.xfRecords.size())
/*  47:    */     {
/*  48:133 */       this.xfRecords.add(xf);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public final void addFormat(DisplayFormat fr)
/*  53:    */     throws NumFormatRecordsException
/*  54:    */   {
/*  55:151 */     if ((fr.isInitialized()) && 
/*  56:152 */       (fr.getFormatIndex() >= 441))
/*  57:    */     {
/*  58:154 */       logger.warn("Format index exceeds Excel maximum - assigning custom number");
/*  59:    */       
/*  60:156 */       fr.initialize(this.nextCustomIndexNumber);
/*  61:157 */       this.nextCustomIndexNumber += 1;
/*  62:    */     }
/*  63:161 */     if (!fr.isInitialized())
/*  64:    */     {
/*  65:163 */       fr.initialize(this.nextCustomIndexNumber);
/*  66:164 */       this.nextCustomIndexNumber += 1;
/*  67:    */     }
/*  68:167 */     if (this.nextCustomIndexNumber > 441)
/*  69:    */     {
/*  70:169 */       this.nextCustomIndexNumber = 441;
/*  71:170 */       throw new NumFormatRecordsException();
/*  72:    */     }
/*  73:173 */     if (fr.getFormatIndex() >= this.nextCustomIndexNumber) {
/*  74:175 */       this.nextCustomIndexNumber = (fr.getFormatIndex() + 1);
/*  75:    */     }
/*  76:178 */     if (!fr.isBuiltIn())
/*  77:    */     {
/*  78:180 */       this.formatsList.add(fr);
/*  79:181 */       this.formats.put(new Integer(fr.getFormatIndex()), fr);
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   public final boolean isDate(int pos)
/*  84:    */   {
/*  85:195 */     XFRecord xfr = (XFRecord)this.xfRecords.get(pos);
/*  86:197 */     if (xfr.isDate()) {
/*  87:199 */       return true;
/*  88:    */     }
/*  89:202 */     FormatRecord fr = 
/*  90:203 */       (FormatRecord)this.formats.get(new Integer(xfr.getFormatRecord()));
/*  91:    */     
/*  92:205 */     return fr == null ? false : fr.isDate();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public final DateFormat getDateFormat(int pos)
/*  96:    */   {
/*  97:217 */     XFRecord xfr = (XFRecord)this.xfRecords.get(pos);
/*  98:219 */     if (xfr.isDate()) {
/*  99:221 */       return xfr.getDateFormat();
/* 100:    */     }
/* 101:224 */     FormatRecord fr = 
/* 102:225 */       (FormatRecord)this.formats.get(new Integer(xfr.getFormatRecord()));
/* 103:227 */     if (fr == null) {
/* 104:229 */       return null;
/* 105:    */     }
/* 106:232 */     return fr.isDate() ? fr.getDateFormat() : null;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public final NumberFormat getNumberFormat(int pos)
/* 110:    */   {
/* 111:244 */     XFRecord xfr = (XFRecord)this.xfRecords.get(pos);
/* 112:246 */     if (xfr.isNumber()) {
/* 113:248 */       return xfr.getNumberFormat();
/* 114:    */     }
/* 115:251 */     FormatRecord fr = 
/* 116:252 */       (FormatRecord)this.formats.get(new Integer(xfr.getFormatRecord()));
/* 117:254 */     if (fr == null) {
/* 118:256 */       return null;
/* 119:    */     }
/* 120:259 */     return fr.isNumber() ? fr.getNumberFormat() : null;
/* 121:    */   }
/* 122:    */   
/* 123:    */   FormatRecord getFormatRecord(int index)
/* 124:    */   {
/* 125:270 */     return 
/* 126:271 */       (FormatRecord)this.formats.get(new Integer(index));
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void write(File outputFile)
/* 130:    */     throws IOException
/* 131:    */   {
/* 132:282 */     Iterator i = this.formatsList.iterator();
/* 133:283 */     FormatRecord fr = null;
/* 134:284 */     while (i.hasNext())
/* 135:    */     {
/* 136:286 */       fr = (FormatRecord)i.next();
/* 137:287 */       outputFile.write(fr);
/* 138:    */     }
/* 139:291 */     i = this.xfRecords.iterator();
/* 140:292 */     XFRecord xfr = null;
/* 141:293 */     while (i.hasNext())
/* 142:    */     {
/* 143:295 */       xfr = (XFRecord)i.next();
/* 144:296 */       outputFile.write(xfr);
/* 145:    */     }
/* 146:300 */     BuiltInStyle style = new BuiltInStyle(16, 3);
/* 147:301 */     outputFile.write(style);
/* 148:    */     
/* 149:303 */     style = new BuiltInStyle(17, 6);
/* 150:304 */     outputFile.write(style);
/* 151:    */     
/* 152:306 */     style = new BuiltInStyle(18, 4);
/* 153:307 */     outputFile.write(style);
/* 154:    */     
/* 155:309 */     style = new BuiltInStyle(19, 7);
/* 156:310 */     outputFile.write(style);
/* 157:    */     
/* 158:312 */     style = new BuiltInStyle(0, 0);
/* 159:313 */     outputFile.write(style);
/* 160:    */     
/* 161:315 */     style = new BuiltInStyle(20, 5);
/* 162:316 */     outputFile.write(style);
/* 163:    */   }
/* 164:    */   
/* 165:    */   protected final Fonts getFonts()
/* 166:    */   {
/* 167:326 */     return this.fonts;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public final XFRecord getXFRecord(int index)
/* 171:    */   {
/* 172:338 */     return (XFRecord)this.xfRecords.get(index);
/* 173:    */   }
/* 174:    */   
/* 175:    */   protected final int getNumberOfFormatRecords()
/* 176:    */   {
/* 177:350 */     return this.formatsList.size();
/* 178:    */   }
/* 179:    */   
/* 180:    */   public IndexMapping rationalizeFonts()
/* 181:    */   {
/* 182:360 */     return this.fonts.rationalize();
/* 183:    */   }
/* 184:    */   
/* 185:    */   public IndexMapping rationalize(IndexMapping fontMapping, IndexMapping formatMapping)
/* 186:    */   {
/* 187:378 */     XFRecord xfr = null;
/* 188:379 */     for (Iterator it = this.xfRecords.iterator(); it.hasNext();)
/* 189:    */     {
/* 190:381 */       xfr = (XFRecord)it.next();
/* 191:383 */       if (xfr.getFormatRecord() >= 164) {
/* 192:385 */         xfr.setFormatIndex(formatMapping.getNewIndex(xfr.getFormatRecord()));
/* 193:    */       }
/* 194:388 */       xfr.setFontIndex(fontMapping.getNewIndex(xfr.getFontIndex()));
/* 195:    */     }
/* 196:391 */     ArrayList newrecords = new ArrayList(21);
/* 197:392 */     IndexMapping mapping = new IndexMapping(this.xfRecords.size());
/* 198:393 */     int numremoved = 0;
/* 199:    */     
/* 200:395 */     int numXFRecords = Math.min(21, this.xfRecords.size());
/* 201:397 */     for (int i = 0; i < numXFRecords; i++)
/* 202:    */     {
/* 203:399 */       newrecords.add(this.xfRecords.get(i));
/* 204:400 */       mapping.setMapping(i, i);
/* 205:    */     }
/* 206:403 */     if (numXFRecords < 21)
/* 207:    */     {
/* 208:405 */       logger.warn("There are less than the expected minimum number of XF records");
/* 209:    */       
/* 210:407 */       return mapping;
/* 211:    */     }
/* 212:411 */     for (int i = 21; i < this.xfRecords.size(); i++)
/* 213:    */     {
/* 214:413 */       XFRecord xf = (XFRecord)this.xfRecords.get(i);
/* 215:    */       
/* 216:    */ 
/* 217:416 */       boolean duplicate = false;
/* 218:417 */       Iterator it = newrecords.iterator();
/* 219:418 */       while ((it.hasNext()) && (!duplicate))
/* 220:    */       {
/* 221:420 */         XFRecord xf2 = (XFRecord)it.next();
/* 222:421 */         if (xf2.equals(xf))
/* 223:    */         {
/* 224:423 */           duplicate = true;
/* 225:424 */           mapping.setMapping(i, mapping.getNewIndex(xf2.getXFIndex()));
/* 226:425 */           numremoved++;
/* 227:    */         }
/* 228:    */       }
/* 229:430 */       if (!duplicate)
/* 230:    */       {
/* 231:432 */         newrecords.add(xf);
/* 232:433 */         mapping.setMapping(i, i - numremoved);
/* 233:    */       }
/* 234:    */     }
/* 235:440 */     for (Iterator i = this.xfRecords.iterator(); i.hasNext();)
/* 236:    */     {
/* 237:442 */       XFRecord xf = (XFRecord)i.next();
/* 238:443 */       xf.rationalize(mapping);
/* 239:    */     }
/* 240:447 */     this.xfRecords = newrecords;
/* 241:    */     
/* 242:449 */     return mapping;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public IndexMapping rationalizeDisplayFormats()
/* 246:    */   {
/* 247:462 */     ArrayList newformats = new ArrayList();
/* 248:463 */     int numremoved = 0;
/* 249:464 */     IndexMapping mapping = new IndexMapping(this.nextCustomIndexNumber);
/* 250:    */     
/* 251:    */ 
/* 252:467 */     Iterator i = this.formatsList.iterator();
/* 253:468 */     DisplayFormat df = null;
/* 254:469 */     DisplayFormat df2 = null;
/* 255:470 */     boolean duplicate = false;
/* 256:471 */     while (i.hasNext())
/* 257:    */     {
/* 258:473 */       df = (DisplayFormat)i.next();
/* 259:    */       
/* 260:475 */       Assert.verify(!df.isBuiltIn());
/* 261:    */       
/* 262:    */ 
/* 263:478 */       Iterator i2 = newformats.iterator();
/* 264:479 */       duplicate = false;
/* 265:480 */       while ((i2.hasNext()) && (!duplicate))
/* 266:    */       {
/* 267:482 */         df2 = (DisplayFormat)i2.next();
/* 268:483 */         if (df2.equals(df))
/* 269:    */         {
/* 270:485 */           duplicate = true;
/* 271:486 */           mapping.setMapping(df.getFormatIndex(), 
/* 272:487 */             mapping.getNewIndex(df2.getFormatIndex()));
/* 273:488 */           numremoved++;
/* 274:    */         }
/* 275:    */       }
/* 276:493 */       if (!duplicate)
/* 277:    */       {
/* 278:495 */         newformats.add(df);
/* 279:496 */         int indexnum = df.getFormatIndex() - numremoved;
/* 280:497 */         if (indexnum > 441)
/* 281:    */         {
/* 282:499 */           logger.warn("Too many number formats - using default format.");
/* 283:500 */           indexnum = 0;
/* 284:    */         }
/* 285:502 */         mapping.setMapping(df.getFormatIndex(), 
/* 286:503 */           df.getFormatIndex() - numremoved);
/* 287:    */       }
/* 288:    */     }
/* 289:508 */     this.formatsList = newformats;
/* 290:    */     
/* 291:    */ 
/* 292:511 */     i = this.formatsList.iterator();
/* 293:513 */     while (i.hasNext())
/* 294:    */     {
/* 295:515 */       df = (DisplayFormat)i.next();
/* 296:516 */       df.initialize(mapping.getNewIndex(df.getFormatIndex()));
/* 297:    */     }
/* 298:519 */     return mapping;
/* 299:    */   }
/* 300:    */   
/* 301:    */   public PaletteRecord getPalette()
/* 302:    */   {
/* 303:529 */     return this.palette;
/* 304:    */   }
/* 305:    */   
/* 306:    */   public void setPalette(PaletteRecord pr)
/* 307:    */   {
/* 308:539 */     this.palette = pr;
/* 309:    */   }
/* 310:    */   
/* 311:    */   public void setColourRGB(Colour c, int r, int g, int b)
/* 312:    */   {
/* 313:552 */     if (this.palette == null) {
/* 314:554 */       this.palette = new PaletteRecord();
/* 315:    */     }
/* 316:556 */     this.palette.setColourRGB(c, r, g, b);
/* 317:    */   }
/* 318:    */   
/* 319:    */   public RGB getColourRGB(Colour c)
/* 320:    */   {
/* 321:566 */     if (this.palette == null) {
/* 322:568 */       return c.getDefaultRGB();
/* 323:    */     }
/* 324:571 */     return this.palette.getColourRGB(c);
/* 325:    */   }
/* 326:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.FormattingRecords
 * JD-Core Version:    0.7.0.1
 */