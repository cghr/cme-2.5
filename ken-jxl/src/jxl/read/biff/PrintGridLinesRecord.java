/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.RecordData;
/*  4:   */ 
/*  5:   */ class PrintGridLinesRecord
/*  6:   */   extends RecordData
/*  7:   */ {
/*  8:   */   private boolean printGridLines;
/*  9:   */   
/* 10:   */   public PrintGridLinesRecord(Record pgl)
/* 11:   */   {
/* 12:41 */     super(pgl);
/* 13:42 */     byte[] data = pgl.getData();
/* 14:   */     
/* 15:44 */     this.printGridLines = (data[0] == 1);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean getPrintGridLines()
/* 19:   */   {
/* 20:54 */     return this.printGridLines;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.PrintGridLinesRecord
 * JD-Core Version:    0.7.0.1
 */