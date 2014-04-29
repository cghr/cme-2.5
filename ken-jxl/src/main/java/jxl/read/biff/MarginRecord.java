/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.DoubleHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.biff.Type;
/*  6:   */ 
/*  7:   */ abstract class MarginRecord
/*  8:   */   extends RecordData
/*  9:   */ {
/* 10:   */   private double margin;
/* 11:   */   
/* 12:   */   protected MarginRecord(Type t, Record r)
/* 13:   */   {
/* 14:45 */     super(t);
/* 15:   */     
/* 16:47 */     byte[] data = r.getData();
/* 17:   */     
/* 18:49 */     this.margin = DoubleHelper.getIEEEDouble(data, 0);
/* 19:   */   }
/* 20:   */   
/* 21:   */   double getMargin()
/* 22:   */   {
/* 23:59 */     return this.margin;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.MarginRecord
 * JD-Core Version:    0.7.0.1
 */