/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.DoubleHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class DeltaRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private byte[] data;
/* 11:   */   private double iterationValue;
/* 12:   */   
/* 13:   */   public DeltaRecord(double itval)
/* 14:   */   {
/* 15:48 */     super(Type.DELTA);
/* 16:49 */     this.iterationValue = itval;
/* 17:   */     
/* 18:51 */     this.data = new byte[8];
/* 19:   */   }
/* 20:   */   
/* 21:   */   public byte[] getData()
/* 22:   */   {
/* 23:62 */     DoubleHelper.getIEEEBytes(this.iterationValue, this.data, 0);
/* 24:   */     
/* 25:   */ 
/* 26:   */ 
/* 27:   */ 
/* 28:   */ 
/* 29:   */ 
/* 30:   */ 
/* 31:   */ 
/* 32:   */ 
/* 33:   */ 
/* 34:   */ 
/* 35:   */ 
/* 36:75 */     return this.data;
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.DeltaRecord
 * JD-Core Version:    0.7.0.1
 */