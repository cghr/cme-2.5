/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.Cell;
/*   4:    */ import jxl.Range;
/*   5:    */ import jxl.Sheet;
/*   6:    */ 
/*   7:    */ public class SheetRangeImpl
/*   8:    */   implements Range
/*   9:    */ {
/*  10:    */   private Sheet sheet;
/*  11:    */   private int column1;
/*  12:    */   private int row1;
/*  13:    */   private int column2;
/*  14:    */   private int row2;
/*  15:    */   
/*  16:    */   public SheetRangeImpl(Sheet s, int c1, int r1, int c2, int r2)
/*  17:    */   {
/*  18: 70 */     this.sheet = s;
/*  19: 71 */     this.row1 = r1;
/*  20: 72 */     this.row2 = r2;
/*  21: 73 */     this.column1 = c1;
/*  22: 74 */     this.column2 = c2;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public SheetRangeImpl(SheetRangeImpl c, Sheet s)
/*  26:    */   {
/*  27: 85 */     this.sheet = s;
/*  28: 86 */     this.row1 = c.row1;
/*  29: 87 */     this.row2 = c.row2;
/*  30: 88 */     this.column1 = c.column1;
/*  31: 89 */     this.column2 = c.column2;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public Cell getTopLeft()
/*  35:    */   {
/*  36:101 */     if ((this.column1 >= this.sheet.getColumns()) || 
/*  37:102 */       (this.row1 >= this.sheet.getRows())) {
/*  38:104 */       return new EmptyCell(this.column1, this.row1);
/*  39:    */     }
/*  40:107 */     return this.sheet.getCell(this.column1, this.row1);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Cell getBottomRight()
/*  44:    */   {
/*  45:119 */     if ((this.column2 >= this.sheet.getColumns()) || 
/*  46:120 */       (this.row2 >= this.sheet.getRows())) {
/*  47:122 */       return new EmptyCell(this.column2, this.row2);
/*  48:    */     }
/*  49:125 */     return this.sheet.getCell(this.column2, this.row2);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getFirstSheetIndex()
/*  53:    */   {
/*  54:136 */     return -1;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int getLastSheetIndex()
/*  58:    */   {
/*  59:147 */     return -1;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public boolean intersects(SheetRangeImpl range)
/*  63:    */   {
/*  64:161 */     if (range == this) {
/*  65:163 */       return true;
/*  66:    */     }
/*  67:166 */     if ((this.row2 < range.row1) || 
/*  68:167 */       (this.row1 > range.row2) || 
/*  69:168 */       (this.column2 < range.column1) || 
/*  70:169 */       (this.column1 > range.column2)) {
/*  71:171 */       return false;
/*  72:    */     }
/*  73:174 */     return true;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String toString()
/*  77:    */   {
/*  78:184 */     StringBuffer sb = new StringBuffer();
/*  79:185 */     CellReferenceHelper.getCellReference(this.column1, this.row1, sb);
/*  80:186 */     sb.append('-');
/*  81:187 */     CellReferenceHelper.getCellReference(this.column2, this.row2, sb);
/*  82:188 */     return sb.toString();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void insertRow(int r)
/*  86:    */   {
/*  87:198 */     if (r > this.row2) {
/*  88:200 */       return;
/*  89:    */     }
/*  90:203 */     if (r <= this.row1) {
/*  91:205 */       this.row1 += 1;
/*  92:    */     }
/*  93:208 */     if (r <= this.row2) {
/*  94:210 */       this.row2 += 1;
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void insertColumn(int c)
/*  99:    */   {
/* 100:221 */     if (c > this.column2) {
/* 101:223 */       return;
/* 102:    */     }
/* 103:226 */     if (c <= this.column1) {
/* 104:228 */       this.column1 += 1;
/* 105:    */     }
/* 106:231 */     if (c <= this.column2) {
/* 107:233 */       this.column2 += 1;
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void removeRow(int r)
/* 112:    */   {
/* 113:244 */     if (r > this.row2) {
/* 114:246 */       return;
/* 115:    */     }
/* 116:249 */     if (r < this.row1) {
/* 117:251 */       this.row1 -= 1;
/* 118:    */     }
/* 119:254 */     if (r < this.row2) {
/* 120:256 */       this.row2 -= 1;
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void removeColumn(int c)
/* 125:    */   {
/* 126:267 */     if (c > this.column2) {
/* 127:269 */       return;
/* 128:    */     }
/* 129:272 */     if (c < this.column1) {
/* 130:274 */       this.column1 -= 1;
/* 131:    */     }
/* 132:277 */     if (c < this.column2) {
/* 133:279 */       this.column2 -= 1;
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   public int hashCode()
/* 138:    */   {
/* 139:290 */     return 0xFFFF ^ this.row1 ^ this.row2 ^ this.column1 ^ this.column2;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public boolean equals(Object o)
/* 143:    */   {
/* 144:301 */     if (o == this) {
/* 145:303 */       return true;
/* 146:    */     }
/* 147:306 */     if (!(o instanceof SheetRangeImpl)) {
/* 148:308 */       return false;
/* 149:    */     }
/* 150:311 */     SheetRangeImpl compare = (SheetRangeImpl)o;
/* 151:    */     
/* 152:    */ 
/* 153:    */ 
/* 154:    */ 
/* 155:316 */     return (this.column1 == compare.column1) && (this.column2 == compare.column2) && (this.row1 == compare.row1) && (this.row2 == compare.row2);
/* 156:    */   }
/* 157:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.SheetRangeImpl
 * JD-Core Version:    0.7.0.1
 */