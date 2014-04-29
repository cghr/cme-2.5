/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class Prot4RevPassRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   
/* 11:   */   public Prot4RevPassRecord()
/* 12:   */   {
/* 13:40 */     super(Type.PROT4REVPASS);
/* 14:   */     
/* 15:   */ 
/* 16:43 */     this.data = new byte[2];
/* 17:   */   }
/* 18:   */   
/* 19:   */   public byte[] getData()
/* 20:   */   {
/* 21:53 */     return this.data;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.Prot4RevPassRecord
 * JD-Core Version:    0.7.0.1
 */