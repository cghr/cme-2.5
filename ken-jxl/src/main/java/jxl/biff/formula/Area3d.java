/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.biff.CellReferenceHelper;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.common.Assert;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ class Area3d
/*   9:    */   extends Operand
/*  10:    */   implements ParsedThing
/*  11:    */ {
/*  12: 36 */   private static Logger logger = Logger.getLogger(Area3d.class);
/*  13:    */   private int sheet;
/*  14:    */   private int columnFirst;
/*  15:    */   private int rowFirst;
/*  16:    */   private int columnLast;
/*  17:    */   private int rowLast;
/*  18:    */   private boolean columnFirstRelative;
/*  19:    */   private boolean rowFirstRelative;
/*  20:    */   private boolean columnLastRelative;
/*  21:    */   private boolean rowLastRelative;
/*  22:    */   private ExternalSheet workbook;
/*  23:    */   
/*  24:    */   Area3d(ExternalSheet es)
/*  25:    */   {
/*  26: 95 */     this.workbook = es;
/*  27:    */   }
/*  28:    */   
/*  29:    */   Area3d(String s, ExternalSheet es)
/*  30:    */     throws FormulaException
/*  31:    */   {
/*  32:107 */     this.workbook = es;
/*  33:108 */     int seppos = s.lastIndexOf(":");
/*  34:109 */     Assert.verify(seppos != -1);
/*  35:110 */     String endcell = s.substring(seppos + 1);
/*  36:    */     
/*  37:    */ 
/*  38:113 */     int sep = s.indexOf('!');
/*  39:114 */     String cellString = s.substring(sep + 1, seppos);
/*  40:115 */     this.columnFirst = CellReferenceHelper.getColumn(cellString);
/*  41:116 */     this.rowFirst = CellReferenceHelper.getRow(cellString);
/*  42:    */     
/*  43:    */ 
/*  44:119 */     String sheetName = s.substring(0, sep);
/*  45:122 */     if ((sheetName.charAt(0) == '\'') && 
/*  46:123 */       (sheetName.charAt(sheetName.length() - 1) == '\'')) {
/*  47:125 */       sheetName = sheetName.substring(1, sheetName.length() - 1);
/*  48:    */     }
/*  49:128 */     this.sheet = es.getExternalSheetIndex(sheetName);
/*  50:130 */     if (this.sheet < 0) {
/*  51:132 */       throw new FormulaException(FormulaException.SHEET_REF_NOT_FOUND, 
/*  52:133 */         sheetName);
/*  53:    */     }
/*  54:137 */     this.columnLast = CellReferenceHelper.getColumn(endcell);
/*  55:138 */     this.rowLast = CellReferenceHelper.getRow(endcell);
/*  56:    */     
/*  57:140 */     this.columnFirstRelative = true;
/*  58:141 */     this.rowFirstRelative = true;
/*  59:142 */     this.columnLastRelative = true;
/*  60:143 */     this.rowLastRelative = true;
/*  61:    */   }
/*  62:    */   
/*  63:    */   int getFirstColumn()
/*  64:    */   {
/*  65:153 */     return this.columnFirst;
/*  66:    */   }
/*  67:    */   
/*  68:    */   int getFirstRow()
/*  69:    */   {
/*  70:163 */     return this.rowFirst;
/*  71:    */   }
/*  72:    */   
/*  73:    */   int getLastColumn()
/*  74:    */   {
/*  75:173 */     return this.columnLast;
/*  76:    */   }
/*  77:    */   
/*  78:    */   int getLastRow()
/*  79:    */   {
/*  80:183 */     return this.rowLast;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public int read(byte[] data, int pos)
/*  84:    */   {
/*  85:195 */     this.sheet = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  86:196 */     this.rowFirst = IntegerHelper.getInt(data[(pos + 2)], data[(pos + 3)]);
/*  87:197 */     this.rowLast = IntegerHelper.getInt(data[(pos + 4)], data[(pos + 5)]);
/*  88:198 */     int columnMask = IntegerHelper.getInt(data[(pos + 6)], data[(pos + 7)]);
/*  89:199 */     this.columnFirst = (columnMask & 0xFF);
/*  90:200 */     this.columnFirstRelative = ((columnMask & 0x4000) != 0);
/*  91:201 */     this.rowFirstRelative = ((columnMask & 0x8000) != 0);
/*  92:202 */     columnMask = IntegerHelper.getInt(data[(pos + 8)], data[(pos + 9)]);
/*  93:203 */     this.columnLast = (columnMask & 0xFF);
/*  94:204 */     this.columnLastRelative = ((columnMask & 0x4000) != 0);
/*  95:205 */     this.rowLastRelative = ((columnMask & 0x8000) != 0);
/*  96:    */     
/*  97:207 */     return 10;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void getString(StringBuffer buf)
/* 101:    */   {
/* 102:217 */     CellReferenceHelper.getCellReference(
/* 103:218 */       this.sheet, this.columnFirst, this.rowFirst, this.workbook, buf);
/* 104:219 */     buf.append(':');
/* 105:220 */     CellReferenceHelper.getCellReference(this.columnLast, this.rowLast, buf);
/* 106:    */   }
/* 107:    */   
/* 108:    */   byte[] getBytes()
/* 109:    */   {
/* 110:230 */     byte[] data = new byte[11];
/* 111:231 */     data[0] = Token.AREA3D.getCode();
/* 112:    */     
/* 113:233 */     IntegerHelper.getTwoBytes(this.sheet, data, 1);
/* 114:    */     
/* 115:235 */     IntegerHelper.getTwoBytes(this.rowFirst, data, 3);
/* 116:236 */     IntegerHelper.getTwoBytes(this.rowLast, data, 5);
/* 117:    */     
/* 118:238 */     int grcol = this.columnFirst;
/* 119:241 */     if (this.rowFirstRelative) {
/* 120:243 */       grcol |= 0x8000;
/* 121:    */     }
/* 122:246 */     if (this.columnFirstRelative) {
/* 123:248 */       grcol |= 0x4000;
/* 124:    */     }
/* 125:251 */     IntegerHelper.getTwoBytes(grcol, data, 7);
/* 126:    */     
/* 127:253 */     grcol = this.columnLast;
/* 128:256 */     if (this.rowLastRelative) {
/* 129:258 */       grcol |= 0x8000;
/* 130:    */     }
/* 131:261 */     if (this.columnLastRelative) {
/* 132:263 */       grcol |= 0x4000;
/* 133:    */     }
/* 134:266 */     IntegerHelper.getTwoBytes(grcol, data, 9);
/* 135:    */     
/* 136:268 */     return data;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/* 140:    */   {
/* 141:280 */     if (this.columnFirstRelative) {
/* 142:282 */       this.columnFirst += colAdjust;
/* 143:    */     }
/* 144:285 */     if (this.columnLastRelative) {
/* 145:287 */       this.columnLast += colAdjust;
/* 146:    */     }
/* 147:290 */     if (this.rowFirstRelative) {
/* 148:292 */       this.rowFirst += rowAdjust;
/* 149:    */     }
/* 150:295 */     if (this.rowLastRelative) {
/* 151:297 */       this.rowLast += rowAdjust;
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void columnInserted(int sheetIndex, int col, boolean currentSheet)
/* 156:    */   {
/* 157:313 */     if (sheetIndex != this.sheet) {
/* 158:315 */       return;
/* 159:    */     }
/* 160:318 */     if (this.columnFirst >= col) {
/* 161:320 */       this.columnFirst += 1;
/* 162:    */     }
/* 163:323 */     if (this.columnLast >= col) {
/* 164:325 */       this.columnLast += 1;
/* 165:    */     }
/* 166:    */   }
/* 167:    */   
/* 168:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/* 169:    */   {
/* 170:341 */     if (sheetIndex != this.sheet) {
/* 171:343 */       return;
/* 172:    */     }
/* 173:346 */     if (col < this.columnFirst) {
/* 174:348 */       this.columnFirst -= 1;
/* 175:    */     }
/* 176:351 */     if (col <= this.columnLast) {
/* 177:353 */       this.columnLast -= 1;
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   void rowInserted(int sheetIndex, int row, boolean currentSheet)
/* 182:    */   {
/* 183:369 */     if (sheetIndex != this.sheet) {
/* 184:371 */       return;
/* 185:    */     }
/* 186:374 */     if (this.rowLast == 65535) {
/* 187:377 */       return;
/* 188:    */     }
/* 189:380 */     if (row <= this.rowFirst) {
/* 190:382 */       this.rowFirst += 1;
/* 191:    */     }
/* 192:385 */     if (row <= this.rowLast) {
/* 193:387 */       this.rowLast += 1;
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/* 198:    */   {
/* 199:403 */     if (sheetIndex != this.sheet) {
/* 200:405 */       return;
/* 201:    */     }
/* 202:408 */     if (this.rowLast == 65535) {
/* 203:411 */       return;
/* 204:    */     }
/* 205:414 */     if (row < this.rowFirst) {
/* 206:416 */       this.rowFirst -= 1;
/* 207:    */     }
/* 208:419 */     if (row <= this.rowLast) {
/* 209:421 */       this.rowLast -= 1;
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   protected void setRangeData(int sht, int colFirst, int colLast, int rwFirst, int rwLast, boolean colFirstRel, boolean colLastRel, boolean rowFirstRel, boolean rowLastRel)
/* 214:    */   {
/* 215:448 */     this.sheet = sht;
/* 216:449 */     this.columnFirst = colFirst;
/* 217:450 */     this.columnLast = colLast;
/* 218:451 */     this.rowFirst = rwFirst;
/* 219:452 */     this.rowLast = rwLast;
/* 220:453 */     this.columnFirstRelative = colFirstRel;
/* 221:454 */     this.columnLastRelative = colLastRel;
/* 222:455 */     this.rowFirstRelative = rowFirstRel;
/* 223:456 */     this.rowLastRelative = rowLastRel;
/* 224:    */   }
/* 225:    */   
/* 226:    */   void handleImportedCellReferences()
/* 227:    */   {
/* 228:465 */     setInvalid();
/* 229:    */   }
/* 230:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Area3d
 * JD-Core Version:    0.7.0.1
 */