/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.RecordData;
/*  4:   */ import jxl.common.Logger;
/*  5:   */ 
/*  6:   */ class Excel9FileRecord
/*  7:   */   extends RecordData
/*  8:   */ {
/*  9:33 */   private static Logger logger = Logger.getLogger(Excel9FileRecord.class);
/* 10:   */   private boolean excel9file;
/* 11:   */   
/* 12:   */   public Excel9FileRecord(Record t)
/* 13:   */   {
/* 14:47 */     super(t);
/* 15:48 */     this.excel9file = true;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean getExcel9File()
/* 19:   */   {
/* 20:58 */     return this.excel9file;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.Excel9FileRecord
 * JD-Core Version:    0.7.0.1
 */