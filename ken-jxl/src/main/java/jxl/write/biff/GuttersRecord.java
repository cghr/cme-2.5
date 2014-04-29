/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ 
/*   7:    */ class GuttersRecord
/*   8:    */   extends WritableRecordData
/*   9:    */ {
/*  10:    */   private byte[] data;
/*  11:    */   private int rowGutter;
/*  12:    */   private int colGutter;
/*  13:    */   private int maxRowOutline;
/*  14:    */   private int maxColumnOutline;
/*  15:    */   
/*  16:    */   public GuttersRecord()
/*  17:    */   {
/*  18: 58 */     super(Type.GUTS);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public byte[] getData()
/*  22:    */   {
/*  23: 68 */     this.data = new byte[8];
/*  24: 69 */     IntegerHelper.getTwoBytes(this.rowGutter, this.data, 0);
/*  25: 70 */     IntegerHelper.getTwoBytes(this.colGutter, this.data, 2);
/*  26: 71 */     IntegerHelper.getTwoBytes(this.maxRowOutline, this.data, 4);
/*  27: 72 */     IntegerHelper.getTwoBytes(this.maxColumnOutline, this.data, 6);
/*  28: 73 */     return this.data;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int getMaxRowOutline()
/*  32:    */   {
/*  33: 83 */     return this.maxRowOutline;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setMaxRowOutline(int value)
/*  37:    */   {
/*  38: 93 */     this.maxRowOutline = value;
/*  39: 94 */     this.rowGutter = (1 + 14 * value);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getMaxColumnOutline()
/*  43:    */   {
/*  44:104 */     return this.maxColumnOutline;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setMaxColumnOutline(int value)
/*  48:    */   {
/*  49:114 */     this.maxColumnOutline = value;
/*  50:115 */     this.colGutter = (1 + 14 * value);
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.GuttersRecord
 * JD-Core Version:    0.7.0.1
 */