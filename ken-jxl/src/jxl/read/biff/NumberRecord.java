/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import jxl.CellType;
/*   6:    */ import jxl.NumberCell;
/*   7:    */ import jxl.biff.DoubleHelper;
/*   8:    */ import jxl.biff.FormattingRecords;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ 
/*  11:    */ class NumberRecord
/*  12:    */   extends CellValue
/*  13:    */   implements NumberCell
/*  14:    */ {
/*  15: 41 */   private static Logger logger = Logger.getLogger(NumberRecord.class);
/*  16:    */   private double value;
/*  17:    */   private NumberFormat format;
/*  18: 56 */   private static DecimalFormat defaultFormat = new DecimalFormat("#.###");
/*  19:    */   
/*  20:    */   public NumberRecord(Record t, FormattingRecords fr, SheetImpl si)
/*  21:    */   {
/*  22: 67 */     super(t, fr, si);
/*  23: 68 */     byte[] data = getRecord().getData();
/*  24:    */     
/*  25: 70 */     this.value = DoubleHelper.getIEEEDouble(data, 6);
/*  26:    */     
/*  27:    */ 
/*  28: 73 */     this.format = fr.getNumberFormat(getXFIndex());
/*  29: 74 */     if (this.format == null) {
/*  30: 76 */       this.format = defaultFormat;
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double getValue()
/*  35:    */   {
/*  36: 87 */     return this.value;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getContents()
/*  40:    */   {
/*  41: 97 */     return this.format.format(this.value);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public CellType getType()
/*  45:    */   {
/*  46:107 */     return CellType.NUMBER;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public NumberFormat getNumberFormat()
/*  50:    */   {
/*  51:118 */     return this.format;
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.NumberRecord
 * JD-Core Version:    0.7.0.1
 */