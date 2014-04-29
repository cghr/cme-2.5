/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.Cell;
/*   4:    */ import jxl.CellFeatures;
/*   5:    */ import jxl.biff.FormattingRecords;
/*   6:    */ import jxl.biff.IntegerHelper;
/*   7:    */ import jxl.biff.RecordData;
/*   8:    */ import jxl.biff.XFRecord;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ import jxl.format.CellFormat;
/*  11:    */ 
/*  12:    */ public abstract class CellValue
/*  13:    */   extends RecordData
/*  14:    */   implements Cell, CellFeaturesAccessor
/*  15:    */ {
/*  16: 41 */   private static Logger logger = Logger.getLogger(CellValue.class);
/*  17:    */   private int row;
/*  18:    */   private int column;
/*  19:    */   private int xfIndex;
/*  20:    */   private FormattingRecords formattingRecords;
/*  21:    */   private boolean initialized;
/*  22:    */   private XFRecord format;
/*  23:    */   private SheetImpl sheet;
/*  24:    */   private CellFeatures features;
/*  25:    */   
/*  26:    */   protected CellValue(Record t, FormattingRecords fr, SheetImpl si)
/*  27:    */   {
/*  28: 93 */     super(t);
/*  29: 94 */     byte[] data = getRecord().getData();
/*  30: 95 */     this.row = IntegerHelper.getInt(data[0], data[1]);
/*  31: 96 */     this.column = IntegerHelper.getInt(data[2], data[3]);
/*  32: 97 */     this.xfIndex = IntegerHelper.getInt(data[4], data[5]);
/*  33: 98 */     this.sheet = si;
/*  34: 99 */     this.formattingRecords = fr;
/*  35:100 */     this.initialized = false;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public final int getRow()
/*  39:    */   {
/*  40:110 */     return this.row;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public final int getColumn()
/*  44:    */   {
/*  45:120 */     return this.column;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public final int getXFIndex()
/*  49:    */   {
/*  50:131 */     return this.xfIndex;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public CellFormat getCellFormat()
/*  54:    */   {
/*  55:142 */     if (!this.initialized)
/*  56:    */     {
/*  57:144 */       this.format = this.formattingRecords.getXFRecord(this.xfIndex);
/*  58:145 */       this.initialized = true;
/*  59:    */     }
/*  60:148 */     return this.format;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public boolean isHidden()
/*  64:    */   {
/*  65:158 */     ColumnInfoRecord cir = this.sheet.getColumnInfo(this.column);
/*  66:160 */     if ((cir != null) && ((cir.getWidth() == 0) || (cir.getHidden()))) {
/*  67:162 */       return true;
/*  68:    */     }
/*  69:165 */     RowRecord rr = this.sheet.getRowInfo(this.row);
/*  70:167 */     if ((rr != null) && ((rr.getRowHeight() == 0) || (rr.isCollapsed()))) {
/*  71:169 */       return true;
/*  72:    */     }
/*  73:172 */     return false;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected SheetImpl getSheet()
/*  77:    */   {
/*  78:182 */     return this.sheet;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public CellFeatures getCellFeatures()
/*  82:    */   {
/*  83:192 */     return this.features;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setCellFeatures(CellFeatures cf)
/*  87:    */   {
/*  88:202 */     if (this.features != null) {
/*  89:204 */       logger.warn("current cell features not null - overwriting");
/*  90:    */     }
/*  91:207 */     this.features = cf;
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.CellValue
 * JD-Core Version:    0.7.0.1
 */