/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class IterationRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private boolean iterate;
/* 10:   */   private byte[] data;
/* 11:   */   
/* 12:   */   public IterationRecord(boolean it)
/* 13:   */   {
/* 14:46 */     super(Type.ITERATION);
/* 15:   */     
/* 16:48 */     this.iterate = it;
/* 17:   */     
/* 18:50 */     this.data = new byte[2];
/* 19:52 */     if (this.iterate) {
/* 20:54 */       this.data[0] = 1;
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:   */   public byte[] getData()
/* 25:   */   {
/* 26:65 */     return this.data;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.IterationRecord
 * JD-Core Version:    0.7.0.1
 */