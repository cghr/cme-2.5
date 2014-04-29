/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class PrintHeadersRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   private boolean printHeaders;
/* 11:   */   
/* 12:   */   public PrintHeadersRecord(boolean ph)
/* 13:   */   {
/* 14:46 */     super(Type.PRINTHEADERS);
/* 15:47 */     this.printHeaders = ph;
/* 16:   */     
/* 17:49 */     this.data = new byte[2];
/* 18:51 */     if (this.printHeaders) {
/* 19:53 */       this.data[0] = 1;
/* 20:   */     }
/* 21:   */   }
/* 22:   */   
/* 23:   */   public byte[] getData()
/* 24:   */   {
/* 25:64 */     return this.data;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.PrintHeadersRecord
 * JD-Core Version:    0.7.0.1
 */