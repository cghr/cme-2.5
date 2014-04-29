/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.common.Logger;
/*   4:    */ import jxl.read.biff.Record;
/*   5:    */ 
/*   6:    */ public class ConditionalFormatRangeRecord
/*   7:    */   extends WritableRecordData
/*   8:    */ {
/*   9: 33 */   private static Logger logger = Logger.getLogger(ConditionalFormatRangeRecord.class);
/*  10:    */   private Range enclosingRange;
/*  11:    */   private Range[] ranges;
/*  12:    */   private int numRanges;
/*  13:    */   private boolean initialized;
/*  14:    */   private boolean modified;
/*  15:    */   private byte[] data;
/*  16:    */   
/*  17:    */   private static class Range
/*  18:    */   {
/*  19:    */     public int firstRow;
/*  20:    */     public int firstColumn;
/*  21:    */     public int lastRow;
/*  22:    */     public int lastColumn;
/*  23:    */     public boolean modified;
/*  24:    */     
/*  25:    */     public Range()
/*  26:    */     {
/*  27: 75 */       this.modified = false;
/*  28:    */     }
/*  29:    */     
/*  30:    */     public void insertColumn(int col)
/*  31:    */     {
/*  32: 86 */       if (col > this.lastColumn) {
/*  33: 88 */         return;
/*  34:    */       }
/*  35: 91 */       if (col <= this.firstColumn)
/*  36:    */       {
/*  37: 93 */         this.firstColumn += 1;
/*  38: 94 */         this.modified = true;
/*  39:    */       }
/*  40: 97 */       if (col <= this.lastColumn)
/*  41:    */       {
/*  42: 99 */         this.lastColumn += 1;
/*  43:100 */         this.modified = true;
/*  44:    */       }
/*  45:    */     }
/*  46:    */     
/*  47:    */     public void removeColumn(int col)
/*  48:    */     {
/*  49:112 */       if (col > this.lastColumn) {
/*  50:114 */         return;
/*  51:    */       }
/*  52:117 */       if (col < this.firstColumn)
/*  53:    */       {
/*  54:119 */         this.firstColumn -= 1;
/*  55:120 */         this.modified = true;
/*  56:    */       }
/*  57:123 */       if (col <= this.lastColumn)
/*  58:    */       {
/*  59:125 */         this.lastColumn -= 1;
/*  60:126 */         this.modified = true;
/*  61:    */       }
/*  62:    */     }
/*  63:    */     
/*  64:    */     public void removeRow(int row)
/*  65:    */     {
/*  66:138 */       if (row > this.lastRow) {
/*  67:140 */         return;
/*  68:    */       }
/*  69:143 */       if (row < this.firstRow)
/*  70:    */       {
/*  71:145 */         this.firstRow -= 1;
/*  72:146 */         this.modified = true;
/*  73:    */       }
/*  74:149 */       if (row <= this.lastRow)
/*  75:    */       {
/*  76:151 */         this.lastRow -= 1;
/*  77:152 */         this.modified = true;
/*  78:    */       }
/*  79:    */     }
/*  80:    */     
/*  81:    */     public void insertRow(int row)
/*  82:    */     {
/*  83:164 */       if (row > this.lastRow) {
/*  84:166 */         return;
/*  85:    */       }
/*  86:169 */       if (row <= this.firstRow)
/*  87:    */       {
/*  88:171 */         this.firstRow += 1;
/*  89:172 */         this.modified = true;
/*  90:    */       }
/*  91:175 */       if (row <= this.lastRow)
/*  92:    */       {
/*  93:177 */         this.lastRow += 1;
/*  94:178 */         this.modified = true;
/*  95:    */       }
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public ConditionalFormatRangeRecord(Record t)
/* 100:    */   {
/* 101:189 */     super(t);
/* 102:    */     
/* 103:191 */     this.initialized = false;
/* 104:192 */     this.modified = false;
/* 105:193 */     this.data = getRecord().getData();
/* 106:    */   }
/* 107:    */   
/* 108:    */   private void initialize()
/* 109:    */   {
/* 110:201 */     this.enclosingRange = new Range();
/* 111:202 */     this.enclosingRange.firstRow = IntegerHelper.getInt(this.data[4], this.data[5]);
/* 112:203 */     this.enclosingRange.lastRow = IntegerHelper.getInt(this.data[6], this.data[7]);
/* 113:204 */     this.enclosingRange.firstColumn = IntegerHelper.getInt(this.data[8], this.data[9]);
/* 114:205 */     this.enclosingRange.lastColumn = IntegerHelper.getInt(this.data[10], this.data[11]);
/* 115:206 */     this.numRanges = IntegerHelper.getInt(this.data[12], this.data[13]);
/* 116:207 */     this.ranges = new Range[this.numRanges];
/* 117:    */     
/* 118:209 */     int pos = 14;
/* 119:211 */     for (int i = 0; i < this.numRanges; i++)
/* 120:    */     {
/* 121:213 */       this.ranges[i] = new Range();
/* 122:214 */       this.ranges[i].firstRow = IntegerHelper.getInt(this.data[pos], this.data[(pos + 1)]);
/* 123:215 */       this.ranges[i].lastRow = IntegerHelper.getInt(this.data[(pos + 2)], this.data[(pos + 3)]);
/* 124:216 */       this.ranges[i].firstColumn = IntegerHelper.getInt(this.data[(pos + 4)], this.data[(pos + 5)]);
/* 125:217 */       this.ranges[i].lastColumn = IntegerHelper.getInt(this.data[(pos + 6)], this.data[(pos + 7)]);
/* 126:218 */       pos += 8;
/* 127:    */     }
/* 128:221 */     this.initialized = true;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void insertColumn(int col)
/* 132:    */   {
/* 133:232 */     if (!this.initialized) {
/* 134:234 */       initialize();
/* 135:    */     }
/* 136:237 */     this.enclosingRange.insertColumn(col);
/* 137:238 */     if (this.enclosingRange.modified) {
/* 138:240 */       this.modified = true;
/* 139:    */     }
/* 140:243 */     for (int i = 0; i < this.ranges.length; i++)
/* 141:    */     {
/* 142:245 */       this.ranges[i].insertColumn(col);
/* 143:247 */       if (this.ranges[i].modified) {
/* 144:249 */         this.modified = true;
/* 145:    */       }
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void removeColumn(int col)
/* 150:    */   {
/* 151:264 */     if (!this.initialized) {
/* 152:266 */       initialize();
/* 153:    */     }
/* 154:269 */     this.enclosingRange.removeColumn(col);
/* 155:270 */     if (this.enclosingRange.modified) {
/* 156:272 */       this.modified = true;
/* 157:    */     }
/* 158:275 */     for (int i = 0; i < this.ranges.length; i++)
/* 159:    */     {
/* 160:277 */       this.ranges[i].removeColumn(col);
/* 161:279 */       if (this.ranges[i].modified) {
/* 162:281 */         this.modified = true;
/* 163:    */       }
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void removeRow(int row)
/* 168:    */   {
/* 169:296 */     if (!this.initialized) {
/* 170:298 */       initialize();
/* 171:    */     }
/* 172:301 */     this.enclosingRange.removeRow(row);
/* 173:302 */     if (this.enclosingRange.modified) {
/* 174:304 */       this.modified = true;
/* 175:    */     }
/* 176:307 */     for (int i = 0; i < this.ranges.length; i++)
/* 177:    */     {
/* 178:309 */       this.ranges[i].removeRow(row);
/* 179:311 */       if (this.ranges[i].modified) {
/* 180:313 */         this.modified = true;
/* 181:    */       }
/* 182:    */     }
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void insertRow(int row)
/* 186:    */   {
/* 187:328 */     if (!this.initialized) {
/* 188:330 */       initialize();
/* 189:    */     }
/* 190:333 */     this.enclosingRange.insertRow(row);
/* 191:334 */     if (this.enclosingRange.modified) {
/* 192:336 */       this.modified = true;
/* 193:    */     }
/* 194:339 */     for (int i = 0; i < this.ranges.length; i++)
/* 195:    */     {
/* 196:341 */       this.ranges[i].insertRow(row);
/* 197:343 */       if (this.ranges[i].modified) {
/* 198:345 */         this.modified = true;
/* 199:    */       }
/* 200:    */     }
/* 201:    */   }
/* 202:    */   
/* 203:    */   public byte[] getData()
/* 204:    */   {
/* 205:360 */     if (!this.modified) {
/* 206:362 */       return this.data;
/* 207:    */     }
/* 208:365 */     byte[] d = new byte[14 + this.ranges.length * 8];
/* 209:    */     
/* 210:    */ 
/* 211:368 */     System.arraycopy(this.data, 0, d, 0, 4);
/* 212:    */     
/* 213:    */ 
/* 214:371 */     IntegerHelper.getTwoBytes(this.enclosingRange.firstRow, d, 4);
/* 215:372 */     IntegerHelper.getTwoBytes(this.enclosingRange.lastRow, d, 6);
/* 216:373 */     IntegerHelper.getTwoBytes(this.enclosingRange.firstColumn, d, 8);
/* 217:374 */     IntegerHelper.getTwoBytes(this.enclosingRange.lastColumn, d, 10);
/* 218:    */     
/* 219:376 */     IntegerHelper.getTwoBytes(this.numRanges, d, 12);
/* 220:    */     
/* 221:378 */     int pos = 14;
/* 222:379 */     for (int i = 0; i < this.ranges.length; i++)
/* 223:    */     {
/* 224:381 */       IntegerHelper.getTwoBytes(this.ranges[i].firstRow, d, pos);
/* 225:382 */       IntegerHelper.getTwoBytes(this.ranges[i].lastRow, d, pos + 2);
/* 226:383 */       IntegerHelper.getTwoBytes(this.ranges[i].firstColumn, d, pos + 4);
/* 227:384 */       IntegerHelper.getTwoBytes(this.ranges[i].lastColumn, d, pos + 6);
/* 228:385 */       pos += 8;
/* 229:    */     }
/* 230:388 */     return d;
/* 231:    */   }
/* 232:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.ConditionalFormatRangeRecord
 * JD-Core Version:    0.7.0.1
 */