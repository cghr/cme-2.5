/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.BooleanCell;
/*   4:    */ import jxl.CellType;
/*   5:    */ import jxl.biff.FormattingRecords;
/*   6:    */ import jxl.common.Assert;
/*   7:    */ 
/*   8:    */ class BooleanRecord
/*   9:    */   extends CellValue
/*  10:    */   implements BooleanCell
/*  11:    */ {
/*  12:    */   private boolean error;
/*  13:    */   private boolean value;
/*  14:    */   
/*  15:    */   public BooleanRecord(Record t, FormattingRecords fr, SheetImpl si)
/*  16:    */   {
/*  17: 53 */     super(t, fr, si);
/*  18: 54 */     this.error = false;
/*  19: 55 */     this.value = false;
/*  20:    */     
/*  21: 57 */     byte[] data = getRecord().getData();
/*  22:    */     
/*  23: 59 */     this.error = (data[7] == 1);
/*  24: 61 */     if (!this.error) {
/*  25: 63 */       this.value = (data[6] == 1);
/*  26:    */     }
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isError()
/*  30:    */   {
/*  31: 75 */     return this.error;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean getValue()
/*  35:    */   {
/*  36: 88 */     return this.value;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getContents()
/*  40:    */   {
/*  41: 98 */     Assert.verify(!isError());
/*  42:    */     
/*  43:    */ 
/*  44:101 */     return new Boolean(this.value).toString();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public CellType getType()
/*  48:    */   {
/*  49:111 */     return CellType.BOOLEAN;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Record getRecord()
/*  53:    */   {
/*  54:122 */     return super.getRecord();
/*  55:    */   }
/*  56:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.BooleanRecord
 * JD-Core Version:    0.7.0.1
 */