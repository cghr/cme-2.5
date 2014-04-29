/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ 
/*  6:   */ class DefaultColumnWidthRecord
/*  7:   */   extends RecordData
/*  8:   */ {
/*  9:   */   private int width;
/* 10:   */   
/* 11:   */   public DefaultColumnWidthRecord(Record t)
/* 12:   */   {
/* 13:42 */     super(t);
/* 14:43 */     byte[] data = t.getData();
/* 15:   */     
/* 16:45 */     this.width = IntegerHelper.getInt(data[0], data[1]);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public int getWidth()
/* 20:   */   {
/* 21:56 */     return this.width;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.DefaultColumnWidthRecord
 * JD-Core Version:    0.7.0.1
 */