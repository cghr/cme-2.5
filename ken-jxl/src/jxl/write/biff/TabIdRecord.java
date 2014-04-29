/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class TabIdRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private byte[] data;
/* 11:   */   
/* 12:   */   public TabIdRecord(int sheets)
/* 13:   */   {
/* 14:43 */     super(Type.TABID);
/* 15:   */     
/* 16:45 */     this.data = new byte[sheets * 2];
/* 17:47 */     for (int i = 0; i < sheets; i++) {
/* 18:49 */       IntegerHelper.getTwoBytes(i + 1, this.data, i * 2);
/* 19:   */     }
/* 20:   */   }
/* 21:   */   
/* 22:   */   public byte[] getData()
/* 23:   */   {
/* 24:60 */     return this.data;
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.TabIdRecord
 * JD-Core Version:    0.7.0.1
 */