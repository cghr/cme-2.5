/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.Cell;
/*   4:    */ import jxl.CellFeatures;
/*   5:    */ import jxl.CellType;
/*   6:    */ import jxl.biff.FormattingRecords;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ import jxl.format.CellFormat;
/*   9:    */ 
/*  10:    */ class MulBlankCell
/*  11:    */   implements Cell, CellFeaturesAccessor
/*  12:    */ {
/*  13: 39 */   private static Logger logger = Logger.getLogger(MulBlankCell.class);
/*  14:    */   private int row;
/*  15:    */   private int column;
/*  16:    */   private CellFormat cellFormat;
/*  17:    */   private int xfIndex;
/*  18:    */   private FormattingRecords formattingRecords;
/*  19:    */   private boolean initialized;
/*  20:    */   private SheetImpl sheet;
/*  21:    */   private CellFeatures features;
/*  22:    */   
/*  23:    */   public MulBlankCell(int r, int c, int xfi, FormattingRecords fr, SheetImpl si)
/*  24:    */   {
/*  25: 95 */     this.row = r;
/*  26: 96 */     this.column = c;
/*  27: 97 */     this.xfIndex = xfi;
/*  28: 98 */     this.formattingRecords = fr;
/*  29: 99 */     this.sheet = si;
/*  30:100 */     this.initialized = false;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public final int getRow()
/*  34:    */   {
/*  35:110 */     return this.row;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public final int getColumn()
/*  39:    */   {
/*  40:120 */     return this.column;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getContents()
/*  44:    */   {
/*  45:130 */     return "";
/*  46:    */   }
/*  47:    */   
/*  48:    */   public CellType getType()
/*  49:    */   {
/*  50:140 */     return CellType.EMPTY;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public CellFormat getCellFormat()
/*  54:    */   {
/*  55:150 */     if (!this.initialized)
/*  56:    */     {
/*  57:152 */       this.cellFormat = this.formattingRecords.getXFRecord(this.xfIndex);
/*  58:153 */       this.initialized = true;
/*  59:    */     }
/*  60:156 */     return this.cellFormat;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public boolean isHidden()
/*  64:    */   {
/*  65:166 */     ColumnInfoRecord cir = this.sheet.getColumnInfo(this.column);
/*  66:168 */     if ((cir != null) && (cir.getWidth() == 0)) {
/*  67:170 */       return true;
/*  68:    */     }
/*  69:173 */     RowRecord rr = this.sheet.getRowInfo(this.row);
/*  70:175 */     if ((rr != null) && ((rr.getRowHeight() == 0) || (rr.isCollapsed()))) {
/*  71:177 */       return true;
/*  72:    */     }
/*  73:180 */     return false;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public CellFeatures getCellFeatures()
/*  77:    */   {
/*  78:190 */     return this.features;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setCellFeatures(CellFeatures cf)
/*  82:    */   {
/*  83:200 */     if (this.features != null) {
/*  84:202 */       logger.warn("current cell features not null - overwriting");
/*  85:    */     }
/*  86:205 */     this.features = cf;
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.MulBlankCell
 * JD-Core Version:    0.7.0.1
 */