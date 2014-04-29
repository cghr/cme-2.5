/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.DoubleHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ abstract class MarginRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private double margin;
/* 11:   */   
/* 12:   */   public MarginRecord(Type t, double v)
/* 13:   */   {
/* 14:41 */     super(t);
/* 15:42 */     this.margin = v;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public byte[] getData()
/* 19:   */   {
/* 20:51 */     byte[] data = new byte[8];
/* 21:   */     
/* 22:53 */     DoubleHelper.getIEEEBytes(this.margin, data, 0);
/* 23:   */     
/* 24:55 */     return data;
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.MarginRecord
 * JD-Core Version:    0.7.0.1
 */