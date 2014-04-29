/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.Cell;
/*   4:    */ import jxl.biff.CellReferenceHelper;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ class CellReference
/*   9:    */   extends Operand
/*  10:    */   implements ParsedThing
/*  11:    */ {
/*  12: 36 */   private static Logger logger = Logger.getLogger(CellReference.class);
/*  13:    */   private boolean columnRelative;
/*  14:    */   private boolean rowRelative;
/*  15:    */   private int column;
/*  16:    */   private int row;
/*  17:    */   private Cell relativeTo;
/*  18:    */   
/*  19:    */   public CellReference(Cell rt)
/*  20:    */   {
/*  21: 71 */     this.relativeTo = rt;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public CellReference() {}
/*  25:    */   
/*  26:    */   public CellReference(String s)
/*  27:    */   {
/*  28: 88 */     this.column = CellReferenceHelper.getColumn(s);
/*  29: 89 */     this.row = CellReferenceHelper.getRow(s);
/*  30: 90 */     this.columnRelative = CellReferenceHelper.isColumnRelative(s);
/*  31: 91 */     this.rowRelative = CellReferenceHelper.isRowRelative(s);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public int read(byte[] data, int pos)
/*  35:    */   {
/*  36:103 */     this.row = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  37:104 */     int columnMask = IntegerHelper.getInt(data[(pos + 2)], data[(pos + 3)]);
/*  38:105 */     this.column = (columnMask & 0xFF);
/*  39:106 */     this.columnRelative = ((columnMask & 0x4000) != 0);
/*  40:107 */     this.rowRelative = ((columnMask & 0x8000) != 0);
/*  41:    */     
/*  42:109 */     return 4;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public int getColumn()
/*  46:    */   {
/*  47:119 */     return this.column;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getRow()
/*  51:    */   {
/*  52:129 */     return this.row;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void getString(StringBuffer buf)
/*  56:    */   {
/*  57:139 */     CellReferenceHelper.getCellReference(this.column, !this.columnRelative, 
/*  58:140 */       this.row, !this.rowRelative, 
/*  59:141 */       buf);
/*  60:    */   }
/*  61:    */   
/*  62:    */   byte[] getBytes()
/*  63:    */   {
/*  64:151 */     byte[] data = new byte[5];
/*  65:152 */     data[0] = (!useAlternateCode() ? Token.REF.getCode() : 
/*  66:153 */       Token.REF.getCode2());
/*  67:    */     
/*  68:155 */     IntegerHelper.getTwoBytes(this.row, data, 1);
/*  69:    */     
/*  70:157 */     int grcol = this.column;
/*  71:160 */     if (this.rowRelative) {
/*  72:162 */       grcol |= 0x8000;
/*  73:    */     }
/*  74:165 */     if (this.columnRelative) {
/*  75:167 */       grcol |= 0x4000;
/*  76:    */     }
/*  77:170 */     IntegerHelper.getTwoBytes(grcol, data, 3);
/*  78:    */     
/*  79:172 */     return data;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/*  83:    */   {
/*  84:184 */     if (this.columnRelative) {
/*  85:186 */       this.column += colAdjust;
/*  86:    */     }
/*  87:189 */     if (this.rowRelative) {
/*  88:191 */       this.row += rowAdjust;
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void columnInserted(int sheetIndex, int col, boolean currentSheet)
/*  93:    */   {
/*  94:207 */     if (!currentSheet) {
/*  95:209 */       return;
/*  96:    */     }
/*  97:212 */     if (this.column >= col) {
/*  98:214 */       this.column += 1;
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/* 103:    */   {
/* 104:230 */     if (!currentSheet) {
/* 105:232 */       return;
/* 106:    */     }
/* 107:235 */     if (this.column >= col) {
/* 108:237 */       this.column -= 1;
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   void rowInserted(int sheetIndex, int r, boolean currentSheet)
/* 113:    */   {
/* 114:253 */     if (!currentSheet) {
/* 115:255 */       return;
/* 116:    */     }
/* 117:258 */     if (this.row >= r) {
/* 118:260 */       this.row += 1;
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   void rowRemoved(int sheetIndex, int r, boolean currentSheet)
/* 123:    */   {
/* 124:276 */     if (!currentSheet) {
/* 125:278 */       return;
/* 126:    */     }
/* 127:281 */     if (this.row >= r) {
/* 128:283 */       this.row -= 1;
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   void handleImportedCellReferences() {}
/* 133:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.CellReference
 * JD-Core Version:    0.7.0.1
 */