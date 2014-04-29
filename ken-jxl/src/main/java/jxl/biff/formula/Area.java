/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.biff.CellReferenceHelper;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.common.Assert;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ class Area
/*   9:    */   extends Operand
/*  10:    */   implements ParsedThing
/*  11:    */ {
/*  12: 36 */   private static Logger logger = Logger.getLogger(Area.class);
/*  13:    */   private int columnFirst;
/*  14:    */   private int rowFirst;
/*  15:    */   private int columnLast;
/*  16:    */   private int rowLast;
/*  17:    */   private boolean columnFirstRelative;
/*  18:    */   private boolean rowFirstRelative;
/*  19:    */   private boolean columnLastRelative;
/*  20:    */   private boolean rowLastRelative;
/*  21:    */   
/*  22:    */   Area() {}
/*  23:    */   
/*  24:    */   Area(String s)
/*  25:    */   {
/*  26: 92 */     int seppos = s.indexOf(":");
/*  27: 93 */     Assert.verify(seppos != -1);
/*  28: 94 */     String startcell = s.substring(0, seppos);
/*  29: 95 */     String endcell = s.substring(seppos + 1);
/*  30:    */     
/*  31: 97 */     this.columnFirst = CellReferenceHelper.getColumn(startcell);
/*  32: 98 */     this.rowFirst = CellReferenceHelper.getRow(startcell);
/*  33: 99 */     this.columnLast = CellReferenceHelper.getColumn(endcell);
/*  34:100 */     this.rowLast = CellReferenceHelper.getRow(endcell);
/*  35:    */     
/*  36:102 */     this.columnFirstRelative = CellReferenceHelper.isColumnRelative(startcell);
/*  37:103 */     this.rowFirstRelative = CellReferenceHelper.isRowRelative(startcell);
/*  38:104 */     this.columnLastRelative = CellReferenceHelper.isColumnRelative(endcell);
/*  39:105 */     this.rowLastRelative = CellReferenceHelper.isRowRelative(endcell);
/*  40:    */   }
/*  41:    */   
/*  42:    */   int getFirstColumn()
/*  43:    */   {
/*  44:115 */     return this.columnFirst;
/*  45:    */   }
/*  46:    */   
/*  47:    */   int getFirstRow()
/*  48:    */   {
/*  49:125 */     return this.rowFirst;
/*  50:    */   }
/*  51:    */   
/*  52:    */   int getLastColumn()
/*  53:    */   {
/*  54:135 */     return this.columnLast;
/*  55:    */   }
/*  56:    */   
/*  57:    */   int getLastRow()
/*  58:    */   {
/*  59:145 */     return this.rowLast;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int read(byte[] data, int pos)
/*  63:    */   {
/*  64:157 */     this.rowFirst = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  65:158 */     this.rowLast = IntegerHelper.getInt(data[(pos + 2)], data[(pos + 3)]);
/*  66:159 */     int columnMask = IntegerHelper.getInt(data[(pos + 4)], data[(pos + 5)]);
/*  67:160 */     this.columnFirst = (columnMask & 0xFF);
/*  68:161 */     this.columnFirstRelative = ((columnMask & 0x4000) != 0);
/*  69:162 */     this.rowFirstRelative = ((columnMask & 0x8000) != 0);
/*  70:163 */     columnMask = IntegerHelper.getInt(data[(pos + 6)], data[(pos + 7)]);
/*  71:164 */     this.columnLast = (columnMask & 0xFF);
/*  72:165 */     this.columnLastRelative = ((columnMask & 0x4000) != 0);
/*  73:166 */     this.rowLastRelative = ((columnMask & 0x8000) != 0);
/*  74:    */     
/*  75:168 */     return 8;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void getString(StringBuffer buf)
/*  79:    */   {
/*  80:178 */     CellReferenceHelper.getCellReference(this.columnFirst, this.rowFirst, buf);
/*  81:179 */     buf.append(':');
/*  82:180 */     CellReferenceHelper.getCellReference(this.columnLast, this.rowLast, buf);
/*  83:    */   }
/*  84:    */   
/*  85:    */   byte[] getBytes()
/*  86:    */   {
/*  87:190 */     byte[] data = new byte[9];
/*  88:191 */     data[0] = (!useAlternateCode() ? Token.AREA.getCode() : 
/*  89:192 */       Token.AREA.getCode2());
/*  90:    */     
/*  91:194 */     IntegerHelper.getTwoBytes(this.rowFirst, data, 1);
/*  92:195 */     IntegerHelper.getTwoBytes(this.rowLast, data, 3);
/*  93:    */     
/*  94:197 */     int grcol = this.columnFirst;
/*  95:200 */     if (this.rowFirstRelative) {
/*  96:202 */       grcol |= 0x8000;
/*  97:    */     }
/*  98:205 */     if (this.columnFirstRelative) {
/*  99:207 */       grcol |= 0x4000;
/* 100:    */     }
/* 101:210 */     IntegerHelper.getTwoBytes(grcol, data, 5);
/* 102:    */     
/* 103:212 */     grcol = this.columnLast;
/* 104:215 */     if (this.rowLastRelative) {
/* 105:217 */       grcol |= 0x8000;
/* 106:    */     }
/* 107:220 */     if (this.columnLastRelative) {
/* 108:222 */       grcol |= 0x4000;
/* 109:    */     }
/* 110:225 */     IntegerHelper.getTwoBytes(grcol, data, 7);
/* 111:    */     
/* 112:227 */     return data;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/* 116:    */   {
/* 117:239 */     if (this.columnFirstRelative) {
/* 118:241 */       this.columnFirst += colAdjust;
/* 119:    */     }
/* 120:244 */     if (this.columnLastRelative) {
/* 121:246 */       this.columnLast += colAdjust;
/* 122:    */     }
/* 123:249 */     if (this.rowFirstRelative) {
/* 124:251 */       this.rowFirst += rowAdjust;
/* 125:    */     }
/* 126:254 */     if (this.rowLastRelative) {
/* 127:256 */       this.rowLast += rowAdjust;
/* 128:    */     }
/* 129:    */   }
/* 130:    */   
/* 131:    */   void columnInserted(int sheetIndex, int col, boolean currentSheet)
/* 132:    */   {
/* 133:272 */     if (!currentSheet) {
/* 134:274 */       return;
/* 135:    */     }
/* 136:277 */     if (col <= this.columnFirst) {
/* 137:279 */       this.columnFirst += 1;
/* 138:    */     }
/* 139:282 */     if (col <= this.columnLast) {
/* 140:284 */       this.columnLast += 1;
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/* 145:    */   {
/* 146:300 */     if (!currentSheet) {
/* 147:302 */       return;
/* 148:    */     }
/* 149:305 */     if (col < this.columnFirst) {
/* 150:307 */       this.columnFirst -= 1;
/* 151:    */     }
/* 152:310 */     if (col <= this.columnLast) {
/* 153:312 */       this.columnLast -= 1;
/* 154:    */     }
/* 155:    */   }
/* 156:    */   
/* 157:    */   void rowInserted(int sheetIndex, int row, boolean currentSheet)
/* 158:    */   {
/* 159:328 */     if (!currentSheet) {
/* 160:330 */       return;
/* 161:    */     }
/* 162:333 */     if (this.rowLast == 65535) {
/* 163:336 */       return;
/* 164:    */     }
/* 165:339 */     if (row <= this.rowFirst) {
/* 166:341 */       this.rowFirst += 1;
/* 167:    */     }
/* 168:344 */     if (row <= this.rowLast) {
/* 169:346 */       this.rowLast += 1;
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/* 174:    */   {
/* 175:362 */     if (!currentSheet) {
/* 176:364 */       return;
/* 177:    */     }
/* 178:367 */     if (this.rowLast == 65535) {
/* 179:370 */       return;
/* 180:    */     }
/* 181:373 */     if (row < this.rowFirst) {
/* 182:375 */       this.rowFirst -= 1;
/* 183:    */     }
/* 184:378 */     if (row <= this.rowLast) {
/* 185:380 */       this.rowLast -= 1;
/* 186:    */     }
/* 187:    */   }
/* 188:    */   
/* 189:    */   protected void setRangeData(int colFirst, int colLast, int rwFirst, int rwLast, boolean colFirstRel, boolean colLastRel, boolean rowFirstRel, boolean rowLastRel)
/* 190:    */   {
/* 191:405 */     this.columnFirst = colFirst;
/* 192:406 */     this.columnLast = colLast;
/* 193:407 */     this.rowFirst = rwFirst;
/* 194:408 */     this.rowLast = rwLast;
/* 195:409 */     this.columnFirstRelative = colFirstRel;
/* 196:410 */     this.columnLastRelative = colLastRel;
/* 197:411 */     this.rowFirstRelative = rowFirstRel;
/* 198:412 */     this.rowLastRelative = rowLastRel;
/* 199:    */   }
/* 200:    */   
/* 201:    */   void handleImportedCellReferences() {}
/* 202:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Area
 * JD-Core Version:    0.7.0.1
 */