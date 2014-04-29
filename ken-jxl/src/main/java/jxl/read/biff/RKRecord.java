/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import jxl.CellType;
/*   6:    */ import jxl.NumberCell;
/*   7:    */ import jxl.biff.FormattingRecords;
/*   8:    */ import jxl.biff.IntegerHelper;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ 
/*  11:    */ class RKRecord
/*  12:    */   extends CellValue
/*  13:    */   implements NumberCell
/*  14:    */ {
/*  15: 40 */   private static Logger logger = Logger.getLogger(RKRecord.class);
/*  16:    */   private double value;
/*  17:    */   private NumberFormat format;
/*  18: 55 */   private static DecimalFormat defaultFormat = new DecimalFormat("#.###");
/*  19:    */   
/*  20:    */   public RKRecord(Record t, FormattingRecords fr, SheetImpl si)
/*  21:    */   {
/*  22: 66 */     super(t, fr, si);
/*  23: 67 */     byte[] data = getRecord().getData();
/*  24: 68 */     int rknum = IntegerHelper.getInt(data[6], data[7], data[8], data[9]);
/*  25: 69 */     this.value = RKHelper.getDouble(rknum);
/*  26:    */     
/*  27:    */ 
/*  28: 72 */     this.format = fr.getNumberFormat(getXFIndex());
/*  29: 73 */     if (this.format == null) {
/*  30: 75 */       this.format = defaultFormat;
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double getValue()
/*  35:    */   {
/*  36: 86 */     return this.value;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getContents()
/*  40:    */   {
/*  41: 96 */     return this.format.format(this.value);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public CellType getType()
/*  45:    */   {
/*  46:106 */     return CellType.NUMBER;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public NumberFormat getNumberFormat()
/*  50:    */   {
/*  51:117 */     return this.format;
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.RKRecord
 * JD-Core Version:    0.7.0.1
 */