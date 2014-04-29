/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.FormattingRecords;
/*   4:    */ import jxl.biff.IndexMapping;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.Type;
/*   7:    */ import jxl.biff.WritableRecordData;
/*   8:    */ import jxl.biff.XFRecord;
/*   9:    */ 
/*  10:    */ class ColumnInfoRecord
/*  11:    */   extends WritableRecordData
/*  12:    */ {
/*  13:    */   private byte[] data;
/*  14:    */   private int column;
/*  15:    */   private XFRecord style;
/*  16:    */   private int xfIndex;
/*  17:    */   private int width;
/*  18:    */   private boolean hidden;
/*  19:    */   private int outlineLevel;
/*  20:    */   private boolean collapsed;
/*  21:    */   
/*  22:    */   public ColumnInfoRecord(int col, int w, XFRecord xf)
/*  23:    */   {
/*  24: 83 */     super(Type.COLINFO);
/*  25:    */     
/*  26: 85 */     this.column = col;
/*  27: 86 */     this.width = w;
/*  28: 87 */     this.style = xf;
/*  29: 88 */     this.xfIndex = this.style.getXFIndex();
/*  30: 89 */     this.hidden = false;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ColumnInfoRecord(ColumnInfoRecord cir)
/*  34:    */   {
/*  35:100 */     super(Type.COLINFO);
/*  36:    */     
/*  37:102 */     this.column = cir.column;
/*  38:103 */     this.width = cir.width;
/*  39:104 */     this.style = cir.style;
/*  40:105 */     this.xfIndex = cir.xfIndex;
/*  41:106 */     this.hidden = cir.hidden;
/*  42:107 */     this.outlineLevel = cir.outlineLevel;
/*  43:108 */     this.collapsed = cir.collapsed;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public ColumnInfoRecord(jxl.read.biff.ColumnInfoRecord cir, int col, FormattingRecords fr)
/*  47:    */   {
/*  48:124 */     super(Type.COLINFO);
/*  49:    */     
/*  50:126 */     this.column = col;
/*  51:127 */     this.width = cir.getWidth();
/*  52:128 */     this.xfIndex = cir.getXFIndex();
/*  53:129 */     this.style = fr.getXFRecord(this.xfIndex);
/*  54:130 */     this.outlineLevel = cir.getOutlineLevel();
/*  55:131 */     this.collapsed = cir.getCollapsed();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public ColumnInfoRecord(jxl.read.biff.ColumnInfoRecord cir, int col)
/*  59:    */   {
/*  60:144 */     super(Type.COLINFO);
/*  61:    */     
/*  62:146 */     this.column = col;
/*  63:147 */     this.width = cir.getWidth();
/*  64:148 */     this.xfIndex = cir.getXFIndex();
/*  65:149 */     this.outlineLevel = cir.getOutlineLevel();
/*  66:150 */     this.collapsed = cir.getCollapsed();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getColumn()
/*  70:    */   {
/*  71:160 */     return this.column;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void incrementColumn()
/*  75:    */   {
/*  76:169 */     this.column += 1;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void decrementColumn()
/*  80:    */   {
/*  81:178 */     this.column -= 1;
/*  82:    */   }
/*  83:    */   
/*  84:    */   int getWidth()
/*  85:    */   {
/*  86:188 */     return this.width;
/*  87:    */   }
/*  88:    */   
/*  89:    */   void setWidth(int w)
/*  90:    */   {
/*  91:198 */     this.width = w;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public byte[] getData()
/*  95:    */   {
/*  96:208 */     this.data = new byte[12];
/*  97:    */     
/*  98:210 */     IntegerHelper.getTwoBytes(this.column, this.data, 0);
/*  99:211 */     IntegerHelper.getTwoBytes(this.column, this.data, 2);
/* 100:212 */     IntegerHelper.getTwoBytes(this.width, this.data, 4);
/* 101:213 */     IntegerHelper.getTwoBytes(this.xfIndex, this.data, 6);
/* 102:    */     
/* 103:    */ 
/* 104:216 */     int options = 0x6 | this.outlineLevel << 8;
/* 105:217 */     if (this.hidden) {
/* 106:219 */       options |= 0x1;
/* 107:    */     }
/* 108:222 */     this.outlineLevel = ((options & 0x700) / 256);
/* 109:224 */     if (this.collapsed) {
/* 110:226 */       options |= 0x1000;
/* 111:    */     }
/* 112:229 */     IntegerHelper.getTwoBytes(options, this.data, 8);
/* 113:    */     
/* 114:    */ 
/* 115:232 */     return this.data;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public XFRecord getCellFormat()
/* 119:    */   {
/* 120:242 */     return this.style;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setCellFormat(XFRecord xfr)
/* 124:    */   {
/* 125:252 */     this.style = xfr;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public int getXfIndex()
/* 129:    */   {
/* 130:262 */     return this.xfIndex;
/* 131:    */   }
/* 132:    */   
/* 133:    */   void rationalize(IndexMapping xfmapping)
/* 134:    */   {
/* 135:271 */     this.xfIndex = xfmapping.getNewIndex(this.xfIndex);
/* 136:    */   }
/* 137:    */   
/* 138:    */   void setHidden(boolean h)
/* 139:    */   {
/* 140:281 */     this.hidden = h;
/* 141:    */   }
/* 142:    */   
/* 143:    */   boolean getHidden()
/* 144:    */   {
/* 145:291 */     return this.hidden;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public boolean equals(Object o)
/* 149:    */   {
/* 150:301 */     if (o == this) {
/* 151:303 */       return true;
/* 152:    */     }
/* 153:306 */     if (!(o instanceof ColumnInfoRecord)) {
/* 154:308 */       return false;
/* 155:    */     }
/* 156:311 */     ColumnInfoRecord cir = (ColumnInfoRecord)o;
/* 157:313 */     if ((this.column != cir.column) || 
/* 158:314 */       (this.xfIndex != cir.xfIndex) || 
/* 159:315 */       (this.width != cir.width) || 
/* 160:316 */       (this.hidden != cir.hidden) || 
/* 161:317 */       (this.outlineLevel != cir.outlineLevel) || 
/* 162:318 */       (this.collapsed != cir.collapsed)) {
/* 163:320 */       return false;
/* 164:    */     }
/* 165:323 */     if (((this.style == null) && (cir.style != null)) || (
/* 166:324 */       (this.style != null) && (cir.style == null))) {
/* 167:326 */       return false;
/* 168:    */     }
/* 169:329 */     return this.style.equals(cir.style);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public int hashCode()
/* 173:    */   {
/* 174:339 */     int hashValue = 137;
/* 175:340 */     int oddPrimeNumber = 79;
/* 176:    */     
/* 177:342 */     hashValue = hashValue * oddPrimeNumber + this.column;
/* 178:343 */     hashValue = hashValue * oddPrimeNumber + this.xfIndex;
/* 179:344 */     hashValue = hashValue * oddPrimeNumber + this.width;
/* 180:345 */     hashValue = hashValue * oddPrimeNumber + (this.hidden ? 1 : 0);
/* 181:347 */     if (this.style != null) {
/* 182:349 */       hashValue ^= this.style.hashCode();
/* 183:    */     }
/* 184:352 */     return hashValue;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public int getOutlineLevel()
/* 188:    */   {
/* 189:362 */     return this.outlineLevel;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public boolean getCollapsed()
/* 193:    */   {
/* 194:372 */     return this.collapsed;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void incrementOutlineLevel()
/* 198:    */   {
/* 199:381 */     this.outlineLevel += 1;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public void decrementOutlineLevel()
/* 203:    */   {
/* 204:391 */     if (this.outlineLevel > 0) {
/* 205:393 */       this.outlineLevel -= 1;
/* 206:    */     }
/* 207:396 */     if (this.outlineLevel == 0) {
/* 208:398 */       this.collapsed = false;
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void setOutlineLevel(int level)
/* 213:    */   {
/* 214:409 */     this.outlineLevel = level;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public void setCollapsed(boolean value)
/* 218:    */   {
/* 219:419 */     this.collapsed = value;
/* 220:    */   }
/* 221:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ColumnInfoRecord
 * JD-Core Version:    0.7.0.1
 */