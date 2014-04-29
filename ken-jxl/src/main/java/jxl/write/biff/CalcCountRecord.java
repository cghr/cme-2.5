/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class CalcCountRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private int calcCount;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public CalcCountRecord(int cnt)
/* 14:   */   {
/* 15:48 */     super(Type.CALCCOUNT);
/* 16:49 */     this.calcCount = cnt;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public byte[] getData()
/* 20:   */   {
/* 21:60 */     byte[] data = new byte[2];
/* 22:   */     
/* 23:62 */     IntegerHelper.getTwoBytes(this.calcCount, data, 0);
/* 24:   */     
/* 25:64 */     return data;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.CalcCountRecord
 * JD-Core Version:    0.7.0.1
 */