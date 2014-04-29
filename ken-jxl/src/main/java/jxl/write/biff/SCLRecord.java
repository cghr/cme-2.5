/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class SCLRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private int zoomFactor;
/* 11:   */   
/* 12:   */   public SCLRecord(int zf)
/* 13:   */   {
/* 14:43 */     super(Type.SCL);
/* 15:   */     
/* 16:45 */     this.zoomFactor = zf;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public byte[] getData()
/* 20:   */   {
/* 21:54 */     byte[] data = new byte[4];
/* 22:   */     
/* 23:56 */     int numerator = this.zoomFactor;
/* 24:57 */     int denominator = 100;
/* 25:   */     
/* 26:59 */     IntegerHelper.getTwoBytes(numerator, data, 0);
/* 27:60 */     IntegerHelper.getTwoBytes(denominator, data, 2);
/* 28:   */     
/* 29:62 */     return data;
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.SCLRecord
 * JD-Core Version:    0.7.0.1
 */