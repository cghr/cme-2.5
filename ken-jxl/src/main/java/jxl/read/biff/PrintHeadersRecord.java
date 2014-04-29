/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.RecordData;
/*  4:   */ 
/*  5:   */ class PrintHeadersRecord
/*  6:   */   extends RecordData
/*  7:   */ {
/*  8:   */   private boolean printHeaders;
/*  9:   */   
/* 10:   */   public PrintHeadersRecord(Record ph)
/* 11:   */   {
/* 12:41 */     super(ph);
/* 13:42 */     byte[] data = ph.getData();
/* 14:   */     
/* 15:44 */     this.printHeaders = (data[0] == 1);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean getPrintHeaders()
/* 19:   */   {
/* 20:54 */     return this.printHeaders;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.PrintHeadersRecord
 * JD-Core Version:    0.7.0.1
 */