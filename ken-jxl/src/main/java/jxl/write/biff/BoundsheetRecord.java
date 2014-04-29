/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.StringHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ 
/*   7:    */ class BoundsheetRecord
/*   8:    */   extends WritableRecordData
/*   9:    */ {
/*  10:    */   private boolean hidden;
/*  11:    */   private boolean chartOnly;
/*  12:    */   private String name;
/*  13:    */   private byte[] data;
/*  14:    */   
/*  15:    */   public BoundsheetRecord(String n)
/*  16:    */   {
/*  17: 59 */     super(Type.BOUNDSHEET);
/*  18: 60 */     this.name = n;
/*  19: 61 */     this.hidden = false;
/*  20: 62 */     this.chartOnly = false;
/*  21:    */   }
/*  22:    */   
/*  23:    */   void setHidden()
/*  24:    */   {
/*  25: 70 */     this.hidden = true;
/*  26:    */   }
/*  27:    */   
/*  28:    */   void setChartOnly()
/*  29:    */   {
/*  30: 78 */     this.chartOnly = true;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public byte[] getData()
/*  34:    */   {
/*  35: 88 */     this.data = new byte[this.name.length() * 2 + 8];
/*  36: 90 */     if (this.chartOnly) {
/*  37: 92 */       this.data[5] = 2;
/*  38:    */     } else {
/*  39: 96 */       this.data[5] = 0;
/*  40:    */     }
/*  41: 99 */     if (this.hidden)
/*  42:    */     {
/*  43:101 */       this.data[4] = 1;
/*  44:102 */       this.data[5] = 0;
/*  45:    */     }
/*  46:105 */     this.data[6] = ((byte)this.name.length());
/*  47:106 */     this.data[7] = 1;
/*  48:107 */     StringHelper.getUnicodeBytes(this.name, this.data, 8);
/*  49:    */     
/*  50:109 */     return this.data;
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.BoundsheetRecord
 * JD-Core Version:    0.7.0.1
 */