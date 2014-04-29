/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.RecordData;
/*  4:   */ 
/*  5:   */ class NineteenFourRecord
/*  6:   */   extends RecordData
/*  7:   */ {
/*  8:   */   private boolean nineteenFour;
/*  9:   */   
/* 10:   */   public NineteenFourRecord(Record t)
/* 11:   */   {
/* 12:41 */     super(t);
/* 13:   */     
/* 14:43 */     byte[] data = getRecord().getData();
/* 15:   */     
/* 16:45 */     this.nineteenFour = (data[0] == 1);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean is1904()
/* 20:   */   {
/* 21:58 */     return this.nineteenFour;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.NineteenFourRecord
 * JD-Core Version:    0.7.0.1
 */