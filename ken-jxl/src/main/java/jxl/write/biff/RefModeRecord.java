/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class RefModeRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   public RefModeRecord()
/* 10:   */   {
/* 11:35 */     super(Type.REFMODE);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public byte[] getData()
/* 15:   */   {
/* 16:45 */     byte[] data = new byte[2];
/* 17:   */     
/* 18:   */ 
/* 19:48 */     data[0] = 1;
/* 20:   */     
/* 21:50 */     return data;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.RefModeRecord
 * JD-Core Version:    0.7.0.1
 */