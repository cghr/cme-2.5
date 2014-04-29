/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import jxl.CellFeatures;
/*   6:    */ import jxl.CellType;
/*   7:    */ import jxl.NumberCell;
/*   8:    */ import jxl.biff.FormattingRecords;
/*   9:    */ import jxl.format.CellFormat;
/*  10:    */ 
/*  11:    */ class NumberValue
/*  12:    */   implements NumberCell, CellFeaturesAccessor
/*  13:    */ {
/*  14:    */   private int row;
/*  15:    */   private int column;
/*  16:    */   private double value;
/*  17:    */   private NumberFormat format;
/*  18:    */   private CellFormat cellFormat;
/*  19:    */   private CellFeatures features;
/*  20:    */   private int xfIndex;
/*  21:    */   private FormattingRecords formattingRecords;
/*  22:    */   private boolean initialized;
/*  23:    */   private SheetImpl sheet;
/*  24: 89 */   private static DecimalFormat defaultFormat = new DecimalFormat("#.###");
/*  25:    */   
/*  26:    */   public NumberValue(int r, int c, double val, int xfi, FormattingRecords fr, SheetImpl si)
/*  27:    */   {
/*  28:106 */     this.row = r;
/*  29:107 */     this.column = c;
/*  30:108 */     this.value = val;
/*  31:109 */     this.format = defaultFormat;
/*  32:110 */     this.xfIndex = xfi;
/*  33:111 */     this.formattingRecords = fr;
/*  34:112 */     this.sheet = si;
/*  35:113 */     this.initialized = false;
/*  36:    */   }
/*  37:    */   
/*  38:    */   final void setNumberFormat(NumberFormat f)
/*  39:    */   {
/*  40:125 */     if (f != null) {
/*  41:127 */       this.format = f;
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   public final int getRow()
/*  46:    */   {
/*  47:138 */     return this.row;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public final int getColumn()
/*  51:    */   {
/*  52:148 */     return this.column;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public double getValue()
/*  56:    */   {
/*  57:158 */     return this.value;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getContents()
/*  61:    */   {
/*  62:168 */     return this.format.format(this.value);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public CellType getType()
/*  66:    */   {
/*  67:178 */     return CellType.NUMBER;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public CellFormat getCellFormat()
/*  71:    */   {
/*  72:188 */     if (!this.initialized)
/*  73:    */     {
/*  74:190 */       this.cellFormat = this.formattingRecords.getXFRecord(this.xfIndex);
/*  75:191 */       this.initialized = true;
/*  76:    */     }
/*  77:194 */     return this.cellFormat;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean isHidden()
/*  81:    */   {
/*  82:204 */     ColumnInfoRecord cir = this.sheet.getColumnInfo(this.column);
/*  83:206 */     if ((cir != null) && (cir.getWidth() == 0)) {
/*  84:208 */       return true;
/*  85:    */     }
/*  86:211 */     RowRecord rr = this.sheet.getRowInfo(this.row);
/*  87:213 */     if ((rr != null) && ((rr.getRowHeight() == 0) || (rr.isCollapsed()))) {
/*  88:215 */       return true;
/*  89:    */     }
/*  90:218 */     return false;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public NumberFormat getNumberFormat()
/*  94:    */   {
/*  95:229 */     return this.format;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public CellFeatures getCellFeatures()
/*  99:    */   {
/* 100:239 */     return this.features;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setCellFeatures(CellFeatures cf)
/* 104:    */   {
/* 105:249 */     this.features = cf;
/* 106:    */   }
/* 107:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.NumberValue
 * JD-Core Version:    0.7.0.1
 */