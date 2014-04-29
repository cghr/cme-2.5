/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.Cell;
/*   4:    */ import jxl.biff.CellReferenceHelper;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ class CellReference3d
/*   9:    */   extends Operand
/*  10:    */   implements ParsedThing
/*  11:    */ {
/*  12: 37 */   private static Logger logger = Logger.getLogger(CellReference3d.class);
/*  13:    */   private boolean columnRelative;
/*  14:    */   private boolean rowRelative;
/*  15:    */   private int column;
/*  16:    */   private int row;
/*  17:    */   private Cell relativeTo;
/*  18:    */   private int sheet;
/*  19:    */   private ExternalSheet workbook;
/*  20:    */   
/*  21:    */   public CellReference3d(Cell rt, ExternalSheet w)
/*  22:    */   {
/*  23: 83 */     this.relativeTo = rt;
/*  24: 84 */     this.workbook = w;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public CellReference3d(String s, ExternalSheet w)
/*  28:    */     throws FormulaException
/*  29:    */   {
/*  30: 96 */     this.workbook = w;
/*  31: 97 */     this.columnRelative = true;
/*  32: 98 */     this.rowRelative = true;
/*  33:    */     
/*  34:    */ 
/*  35:101 */     int sep = s.indexOf('!');
/*  36:102 */     String cellString = s.substring(sep + 1);
/*  37:103 */     this.column = CellReferenceHelper.getColumn(cellString);
/*  38:104 */     this.row = CellReferenceHelper.getRow(cellString);
/*  39:    */     
/*  40:    */ 
/*  41:107 */     String sheetName = s.substring(0, sep);
/*  42:110 */     if ((sheetName.charAt(0) == '\'') && 
/*  43:111 */       (sheetName.charAt(sheetName.length() - 1) == '\'')) {
/*  44:113 */       sheetName = sheetName.substring(1, sheetName.length() - 1);
/*  45:    */     }
/*  46:115 */     this.sheet = w.getExternalSheetIndex(sheetName);
/*  47:117 */     if (this.sheet < 0) {
/*  48:119 */       throw new FormulaException(FormulaException.SHEET_REF_NOT_FOUND, 
/*  49:120 */         sheetName);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int read(byte[] data, int pos)
/*  54:    */   {
/*  55:133 */     this.sheet = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  56:134 */     this.row = IntegerHelper.getInt(data[(pos + 2)], data[(pos + 3)]);
/*  57:135 */     int columnMask = IntegerHelper.getInt(data[(pos + 4)], data[(pos + 5)]);
/*  58:136 */     this.column = (columnMask & 0xFF);
/*  59:137 */     this.columnRelative = ((columnMask & 0x4000) != 0);
/*  60:138 */     this.rowRelative = ((columnMask & 0x8000) != 0);
/*  61:    */     
/*  62:140 */     return 6;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getColumn()
/*  66:    */   {
/*  67:150 */     return this.column;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int getRow()
/*  71:    */   {
/*  72:160 */     return this.row;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void getString(StringBuffer buf)
/*  76:    */   {
/*  77:170 */     CellReferenceHelper.getCellReference(this.sheet, this.column, !this.columnRelative, 
/*  78:171 */       this.row, !this.rowRelative, 
/*  79:172 */       this.workbook, buf);
/*  80:    */   }
/*  81:    */   
/*  82:    */   byte[] getBytes()
/*  83:    */   {
/*  84:182 */     byte[] data = new byte[7];
/*  85:183 */     data[0] = Token.REF3D.getCode();
/*  86:    */     
/*  87:185 */     IntegerHelper.getTwoBytes(this.sheet, data, 1);
/*  88:186 */     IntegerHelper.getTwoBytes(this.row, data, 3);
/*  89:    */     
/*  90:188 */     int grcol = this.column;
/*  91:191 */     if (this.rowRelative) {
/*  92:193 */       grcol |= 0x8000;
/*  93:    */     }
/*  94:196 */     if (this.columnRelative) {
/*  95:198 */       grcol |= 0x4000;
/*  96:    */     }
/*  97:201 */     IntegerHelper.getTwoBytes(grcol, data, 5);
/*  98:    */     
/*  99:203 */     return data;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/* 103:    */   {
/* 104:215 */     if (this.columnRelative) {
/* 105:217 */       this.column += colAdjust;
/* 106:    */     }
/* 107:220 */     if (this.rowRelative) {
/* 108:222 */       this.row += rowAdjust;
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void columnInserted(int sheetIndex, int col, boolean currentSheet)
/* 113:    */   {
/* 114:238 */     if (sheetIndex != this.sheet) {
/* 115:240 */       return;
/* 116:    */     }
/* 117:243 */     if (this.column >= col) {
/* 118:245 */       this.column += 1;
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/* 123:    */   {
/* 124:262 */     if (sheetIndex != this.sheet) {
/* 125:264 */       return;
/* 126:    */     }
/* 127:267 */     if (this.column >= col) {
/* 128:269 */       this.column -= 1;
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   void rowInserted(int sheetIndex, int r, boolean currentSheet)
/* 133:    */   {
/* 134:285 */     if (sheetIndex != this.sheet) {
/* 135:287 */       return;
/* 136:    */     }
/* 137:290 */     if (this.row >= r) {
/* 138:292 */       this.row += 1;
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   void rowRemoved(int sheetIndex, int r, boolean currentSheet)
/* 143:    */   {
/* 144:308 */     if (sheetIndex != this.sheet) {
/* 145:310 */       return;
/* 146:    */     }
/* 147:313 */     if (this.row >= r) {
/* 148:315 */       this.row -= 1;
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   void handleImportedCellReferences()
/* 153:    */   {
/* 154:326 */     setInvalid();
/* 155:    */   }
/* 156:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.CellReference3d
 * JD-Core Version:    0.7.0.1
 */