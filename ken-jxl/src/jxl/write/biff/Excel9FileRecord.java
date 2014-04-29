/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class Excel9FileRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   public Excel9FileRecord()
/* 10:   */   {
/* 11:38 */     super(Type.EXCEL9FILE);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public byte[] getData()
/* 15:   */   {
/* 16:48 */     return new byte[0];
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.Excel9FileRecord
 * JD-Core Version:    0.7.0.1
 */