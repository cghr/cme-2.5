/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ 
/*  6:   */ class DefaultRowHeightRecord
/*  7:   */   extends RecordData
/*  8:   */ {
/*  9:   */   private int height;
/* 10:   */   
/* 11:   */   public DefaultRowHeightRecord(Record t)
/* 12:   */   {
/* 13:42 */     super(t);
/* 14:43 */     byte[] data = t.getData();
/* 15:45 */     if (data.length > 2) {
/* 16:47 */       this.height = IntegerHelper.getInt(data[2], data[3]);
/* 17:   */     }
/* 18:   */   }
/* 19:   */   
/* 20:   */   public int getHeight()
/* 21:   */   {
/* 22:58 */     return this.height;
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.DefaultRowHeightRecord
 * JD-Core Version:    0.7.0.1
 */