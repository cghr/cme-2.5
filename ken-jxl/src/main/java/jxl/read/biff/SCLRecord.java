/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.biff.Type;
/*  6:   */ 
/*  7:   */ class SCLRecord
/*  8:   */   extends RecordData
/*  9:   */ {
/* 10:   */   private int numerator;
/* 11:   */   private int denominator;
/* 12:   */   
/* 13:   */   protected SCLRecord(Record r)
/* 14:   */   {
/* 15:47 */     super(Type.SCL);
/* 16:   */     
/* 17:49 */     byte[] data = r.getData();
/* 18:   */     
/* 19:51 */     this.numerator = IntegerHelper.getInt(data[0], data[1]);
/* 20:52 */     this.denominator = IntegerHelper.getInt(data[2], data[3]);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int getZoomFactor()
/* 24:   */   {
/* 25:62 */     return this.numerator * 100 / this.denominator;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SCLRecord
 * JD-Core Version:    0.7.0.1
 */