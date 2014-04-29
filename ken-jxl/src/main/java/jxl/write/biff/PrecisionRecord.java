/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class PrecisionRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private boolean asDisplayed;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public PrecisionRecord(boolean disp)
/* 14:   */   {
/* 15:47 */     super(Type.PRECISION);
/* 16:   */     
/* 17:49 */     this.asDisplayed = disp;
/* 18:50 */     this.data = new byte[2];
/* 19:52 */     if (!this.asDisplayed) {
/* 20:54 */       IntegerHelper.getTwoBytes(1, this.data, 0);
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:   */   public byte[] getData()
/* 25:   */   {
/* 26:65 */     return this.data;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.PrecisionRecord
 * JD-Core Version:    0.7.0.1
 */