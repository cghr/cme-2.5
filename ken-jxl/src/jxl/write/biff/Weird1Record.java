/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class Weird1Record
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   public Weird1Record()
/* 10:   */   {
/* 11:35 */     super(Type.WEIRD1);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public byte[] getData()
/* 15:   */   {
/* 16:45 */     byte[] data = new byte[6];
/* 17:   */     
/* 18:47 */     data[2] = 55;
/* 19:   */     
/* 20:49 */     return data;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.Weird1Record
 * JD-Core Version:    0.7.0.1
 */