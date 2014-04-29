/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class RefreshAllRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private boolean refreshall;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public RefreshAllRecord(boolean refresh)
/* 14:   */   {
/* 15:48 */     super(Type.REFRESHALL);
/* 16:   */     
/* 17:50 */     this.refreshall = refresh;
/* 18:   */     
/* 19:   */ 
/* 20:53 */     this.data = new byte[2];
/* 21:55 */     if (this.refreshall) {
/* 22:57 */       IntegerHelper.getTwoBytes(1, this.data, 0);
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   public byte[] getData()
/* 27:   */   {
/* 28:68 */     return this.data;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.RefreshAllRecord
 * JD-Core Version:    0.7.0.1
 */