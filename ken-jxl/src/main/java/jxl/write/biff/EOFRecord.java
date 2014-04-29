/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class EOFRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   public EOFRecord()
/* 10:   */   {
/* 11:35 */     super(Type.EOF);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public byte[] getData()
/* 15:   */   {
/* 16:45 */     return new byte[0];
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.EOFRecord
 * JD-Core Version:    0.7.0.1
 */