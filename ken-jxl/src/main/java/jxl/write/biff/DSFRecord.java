/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class DSFRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   
/* 11:   */   public DSFRecord()
/* 12:   */   {
/* 13:41 */     super(Type.DSF);
/* 14:   */     
/* 15:   */ 
/* 16:   */ 
/* 17:45 */     this.data = new byte[2];
/* 18:   */   }
/* 19:   */   
/* 20:   */   public byte[] getData()
/* 21:   */   {
/* 22:55 */     return this.data;
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.DSFRecord
 * JD-Core Version:    0.7.0.1
 */