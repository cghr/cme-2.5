/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class UsesElfsRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   private boolean usesElfs;
/* 11:   */   
/* 12:   */   public UsesElfsRecord()
/* 13:   */   {
/* 14:45 */     super(Type.USESELFS);
/* 15:   */     
/* 16:47 */     this.usesElfs = true;
/* 17:   */     
/* 18:49 */     this.data = new byte[2];
/* 19:51 */     if (this.usesElfs) {
/* 20:53 */       this.data[0] = 1;
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:   */   public byte[] getData()
/* 25:   */   {
/* 26:64 */     return this.data;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.UsesElfsRecord
 * JD-Core Version:    0.7.0.1
 */