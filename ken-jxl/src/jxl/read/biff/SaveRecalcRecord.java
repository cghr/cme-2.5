/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.common.Logger;
/*  6:   */ 
/*  7:   */ class SaveRecalcRecord
/*  8:   */   extends RecordData
/*  9:   */ {
/* 10:35 */   private static Logger logger = Logger.getLogger(SaveRecalcRecord.class);
/* 11:   */   private boolean recalculateOnSave;
/* 12:   */   
/* 13:   */   public SaveRecalcRecord(Record t)
/* 14:   */   {
/* 15:49 */     super(t);
/* 16:50 */     byte[] data = t.getData();
/* 17:51 */     int mode = IntegerHelper.getInt(data[0], data[1]);
/* 18:52 */     this.recalculateOnSave = (mode == 1);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public boolean getRecalculateOnSave()
/* 22:   */   {
/* 23:62 */     return this.recalculateOnSave;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SaveRecalcRecord
 * JD-Core Version:    0.7.0.1
 */