/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class NineteenFourRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private boolean nineteenFourDate;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public NineteenFourRecord(boolean oldDate)
/* 14:   */   {
/* 15:49 */     super(Type.NINETEENFOUR);
/* 16:   */     
/* 17:51 */     this.nineteenFourDate = oldDate;
/* 18:52 */     this.data = new byte[2];
/* 19:54 */     if (this.nineteenFourDate) {
/* 20:56 */       IntegerHelper.getTwoBytes(1, this.data, 0);
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:   */   public byte[] getData()
/* 25:   */   {
/* 26:66 */     return this.data;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.NineteenFourRecord
 * JD-Core Version:    0.7.0.1
 */