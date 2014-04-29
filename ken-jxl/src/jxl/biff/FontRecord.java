/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.common.Assert;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ import jxl.format.Colour;
/*   7:    */ import jxl.format.Font;
/*   8:    */ import jxl.format.ScriptStyle;
/*   9:    */ import jxl.format.UnderlineStyle;
/*  10:    */ import jxl.read.biff.Record;
/*  11:    */ 
/*  12:    */ public class FontRecord
/*  13:    */   extends WritableRecordData
/*  14:    */   implements Font
/*  15:    */ {
/*  16: 40 */   private static Logger logger = Logger.getLogger(FontRecord.class);
/*  17:    */   private int pointHeight;
/*  18:    */   private int colourIndex;
/*  19:    */   private int boldWeight;
/*  20:    */   private int scriptStyle;
/*  21:    */   private int underlineStyle;
/*  22:    */   private byte fontFamily;
/*  23:    */   private byte characterSet;
/*  24:    */   private boolean italic;
/*  25:    */   private boolean struckout;
/*  26:    */   private String name;
/*  27:    */   private boolean initialized;
/*  28:    */   private int fontIndex;
/*  29: 98 */   public static final Biff7 biff7 = new Biff7(null);
/*  30:    */   private static final int EXCEL_UNITS_PER_POINT = 20;
/*  31:    */   
/*  32:    */   protected FontRecord(String fn, int ps, int bold, boolean it, int us, int ci, int ss)
/*  33:    */   {
/*  34:119 */     super(Type.FONT);
/*  35:120 */     this.boldWeight = bold;
/*  36:121 */     this.underlineStyle = us;
/*  37:122 */     this.name = fn;
/*  38:123 */     this.pointHeight = ps;
/*  39:124 */     this.italic = it;
/*  40:125 */     this.scriptStyle = ss;
/*  41:126 */     this.colourIndex = ci;
/*  42:127 */     this.initialized = false;
/*  43:128 */     this.struckout = false;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public FontRecord(Record t, WorkbookSettings ws)
/*  47:    */   {
/*  48:140 */     super(t);
/*  49:    */     
/*  50:142 */     byte[] data = getRecord().getData();
/*  51:    */     
/*  52:144 */     this.pointHeight = 
/*  53:145 */       (IntegerHelper.getInt(data[0], data[1]) / 20);
/*  54:146 */     this.colourIndex = IntegerHelper.getInt(data[4], data[5]);
/*  55:147 */     this.boldWeight = IntegerHelper.getInt(data[6], data[7]);
/*  56:148 */     this.scriptStyle = IntegerHelper.getInt(data[8], data[9]);
/*  57:149 */     this.underlineStyle = data[10];
/*  58:150 */     this.fontFamily = data[11];
/*  59:151 */     this.characterSet = data[12];
/*  60:152 */     this.initialized = false;
/*  61:154 */     if ((data[2] & 0x2) != 0) {
/*  62:156 */       this.italic = true;
/*  63:    */     }
/*  64:159 */     if ((data[2] & 0x8) != 0) {
/*  65:161 */       this.struckout = true;
/*  66:    */     }
/*  67:164 */     int numChars = data[14];
/*  68:165 */     if (data[15] == 0) {
/*  69:167 */       this.name = StringHelper.getString(data, numChars, 16, ws);
/*  70:169 */     } else if (data[15] == 1) {
/*  71:171 */       this.name = StringHelper.getUnicodeString(data, numChars, 16);
/*  72:    */     } else {
/*  73:176 */       this.name = StringHelper.getString(data, numChars, 15, ws);
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public FontRecord(Record t, WorkbookSettings ws, Biff7 dummy)
/*  78:    */   {
/*  79:190 */     super(t);
/*  80:    */     
/*  81:192 */     byte[] data = getRecord().getData();
/*  82:    */     
/*  83:194 */     this.pointHeight = 
/*  84:195 */       (IntegerHelper.getInt(data[0], data[1]) / 20);
/*  85:196 */     this.colourIndex = IntegerHelper.getInt(data[4], data[5]);
/*  86:197 */     this.boldWeight = IntegerHelper.getInt(data[6], data[7]);
/*  87:198 */     this.scriptStyle = IntegerHelper.getInt(data[8], data[9]);
/*  88:199 */     this.underlineStyle = data[10];
/*  89:200 */     this.fontFamily = data[11];
/*  90:201 */     this.initialized = false;
/*  91:203 */     if ((data[2] & 0x2) != 0) {
/*  92:205 */       this.italic = true;
/*  93:    */     }
/*  94:208 */     if ((data[2] & 0x8) != 0) {
/*  95:210 */       this.struckout = true;
/*  96:    */     }
/*  97:213 */     int numChars = data[14];
/*  98:214 */     this.name = StringHelper.getString(data, numChars, 15, ws);
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected FontRecord(Font f)
/* 102:    */   {
/* 103:224 */     super(Type.FONT);
/* 104:    */     
/* 105:226 */     Assert.verify(f != null);
/* 106:    */     
/* 107:228 */     this.pointHeight = f.getPointSize();
/* 108:229 */     this.colourIndex = f.getColour().getValue();
/* 109:230 */     this.boldWeight = f.getBoldWeight();
/* 110:231 */     this.scriptStyle = f.getScriptStyle().getValue();
/* 111:232 */     this.underlineStyle = f.getUnderlineStyle().getValue();
/* 112:233 */     this.italic = f.isItalic();
/* 113:234 */     this.name = f.getName();
/* 114:235 */     this.struckout = f.isStruckout();
/* 115:236 */     this.initialized = false;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public byte[] getData()
/* 119:    */   {
/* 120:246 */     byte[] data = new byte[16 + this.name.length() * 2];
/* 121:    */     
/* 122:    */ 
/* 123:249 */     IntegerHelper.getTwoBytes(this.pointHeight * 20, data, 0);
/* 124:252 */     if (this.italic)
/* 125:    */     {
/* 126:254 */       int tmp36_35 = 2; byte[] tmp36_34 = data;tmp36_34[tmp36_35] = ((byte)(tmp36_34[tmp36_35] | 0x2));
/* 127:    */     }
/* 128:257 */     if (this.struckout)
/* 129:    */     {
/* 130:259 */       int tmp51_50 = 2; byte[] tmp51_49 = data;tmp51_49[tmp51_50] = ((byte)(tmp51_49[tmp51_50] | 0x8));
/* 131:    */     }
/* 132:263 */     IntegerHelper.getTwoBytes(this.colourIndex, data, 4);
/* 133:    */     
/* 134:    */ 
/* 135:266 */     IntegerHelper.getTwoBytes(this.boldWeight, data, 6);
/* 136:    */     
/* 137:    */ 
/* 138:269 */     IntegerHelper.getTwoBytes(this.scriptStyle, data, 8);
/* 139:    */     
/* 140:    */ 
/* 141:272 */     data[10] = ((byte)this.underlineStyle);
/* 142:    */     
/* 143:    */ 
/* 144:275 */     data[11] = this.fontFamily;
/* 145:    */     
/* 146:    */ 
/* 147:278 */     data[12] = this.characterSet;
/* 148:    */     
/* 149:    */ 
/* 150:281 */     data[13] = 0;
/* 151:    */     
/* 152:    */ 
/* 153:284 */     data[14] = ((byte)this.name.length());
/* 154:    */     
/* 155:286 */     data[15] = 1;
/* 156:    */     
/* 157:    */ 
/* 158:289 */     StringHelper.getUnicodeBytes(this.name, data, 16);
/* 159:    */     
/* 160:291 */     return data;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public final boolean isInitialized()
/* 164:    */   {
/* 165:301 */     return this.initialized;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public final void initialize(int pos)
/* 169:    */   {
/* 170:312 */     this.fontIndex = pos;
/* 171:313 */     this.initialized = true;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public final void uninitialize()
/* 175:    */   {
/* 176:322 */     this.initialized = false;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public final int getFontIndex()
/* 180:    */   {
/* 181:332 */     return this.fontIndex;
/* 182:    */   }
/* 183:    */   
/* 184:    */   protected void setFontPointSize(int ps)
/* 185:    */   {
/* 186:342 */     Assert.verify(!this.initialized);
/* 187:    */     
/* 188:344 */     this.pointHeight = ps;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public int getPointSize()
/* 192:    */   {
/* 193:354 */     return this.pointHeight;
/* 194:    */   }
/* 195:    */   
/* 196:    */   protected void setFontBoldStyle(int bs)
/* 197:    */   {
/* 198:364 */     Assert.verify(!this.initialized);
/* 199:    */     
/* 200:366 */     this.boldWeight = bs;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public int getBoldWeight()
/* 204:    */   {
/* 205:376 */     return this.boldWeight;
/* 206:    */   }
/* 207:    */   
/* 208:    */   protected void setFontItalic(boolean i)
/* 209:    */   {
/* 210:387 */     Assert.verify(!this.initialized);
/* 211:    */     
/* 212:389 */     this.italic = i;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public boolean isItalic()
/* 216:    */   {
/* 217:399 */     return this.italic;
/* 218:    */   }
/* 219:    */   
/* 220:    */   protected void setFontUnderlineStyle(int us)
/* 221:    */   {
/* 222:410 */     Assert.verify(!this.initialized);
/* 223:    */     
/* 224:412 */     this.underlineStyle = us;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public UnderlineStyle getUnderlineStyle()
/* 228:    */   {
/* 229:422 */     return UnderlineStyle.getStyle(this.underlineStyle);
/* 230:    */   }
/* 231:    */   
/* 232:    */   protected void setFontColour(int c)
/* 233:    */   {
/* 234:433 */     Assert.verify(!this.initialized);
/* 235:    */     
/* 236:435 */     this.colourIndex = c;
/* 237:    */   }
/* 238:    */   
/* 239:    */   public Colour getColour()
/* 240:    */   {
/* 241:445 */     return Colour.getInternalColour(this.colourIndex);
/* 242:    */   }
/* 243:    */   
/* 244:    */   protected void setFontScriptStyle(int ss)
/* 245:    */   {
/* 246:456 */     Assert.verify(!this.initialized);
/* 247:    */     
/* 248:458 */     this.scriptStyle = ss;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public ScriptStyle getScriptStyle()
/* 252:    */   {
/* 253:468 */     return ScriptStyle.getStyle(this.scriptStyle);
/* 254:    */   }
/* 255:    */   
/* 256:    */   public String getName()
/* 257:    */   {
/* 258:478 */     return this.name;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public int hashCode()
/* 262:    */   {
/* 263:487 */     return this.name.hashCode();
/* 264:    */   }
/* 265:    */   
/* 266:    */   public boolean equals(Object o)
/* 267:    */   {
/* 268:497 */     if (o == this) {
/* 269:499 */       return true;
/* 270:    */     }
/* 271:502 */     if (!(o instanceof FontRecord)) {
/* 272:504 */       return false;
/* 273:    */     }
/* 274:507 */     FontRecord font = (FontRecord)o;
/* 275:509 */     if ((this.pointHeight == font.pointHeight) && 
/* 276:510 */       (this.colourIndex == font.colourIndex) && 
/* 277:511 */       (this.boldWeight == font.boldWeight) && 
/* 278:512 */       (this.scriptStyle == font.scriptStyle) && 
/* 279:513 */       (this.underlineStyle == font.underlineStyle) && 
/* 280:514 */       (this.italic == font.italic) && 
/* 281:515 */       (this.struckout == font.struckout) && 
/* 282:516 */       (this.fontFamily == font.fontFamily) && 
/* 283:517 */       (this.characterSet == font.characterSet) && 
/* 284:518 */       (this.name.equals(font.name))) {
/* 285:520 */       return true;
/* 286:    */     }
/* 287:523 */     return false;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public boolean isStruckout()
/* 291:    */   {
/* 292:533 */     return this.struckout;
/* 293:    */   }
/* 294:    */   
/* 295:    */   protected void setFontStruckout(boolean os)
/* 296:    */   {
/* 297:543 */     this.struckout = os;
/* 298:    */   }
/* 299:    */   
/* 300:    */   private static class Biff7 {}
/* 301:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.FontRecord
 * JD-Core Version:    0.7.0.1
 */