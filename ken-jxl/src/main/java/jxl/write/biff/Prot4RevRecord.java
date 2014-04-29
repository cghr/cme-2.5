/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class Prot4RevRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private boolean protection;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public Prot4RevRecord(boolean prot)
/* 14:   */   {
/* 15:47 */     super(Type.PROT4REV);
/* 16:   */     
/* 17:49 */     this.protection = prot;
/* 18:   */     
/* 19:   */ 
/* 20:52 */     this.data = new byte[2];
/* 21:54 */     if (this.protection) {
/* 22:56 */       IntegerHelper.getTwoBytes(1, this.data, 0);
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   public byte[] getData()
/* 27:   */   {
/* 28:67 */     return this.data;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.Prot4RevRecord
 * JD-Core Version:    0.7.0.1
 */