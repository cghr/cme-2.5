/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ 
/*  6:   */ class CentreRecord
/*  7:   */   extends RecordData
/*  8:   */ {
/*  9:   */   private boolean centre;
/* 10:   */   
/* 11:   */   public CentreRecord(Record t)
/* 12:   */   {
/* 13:42 */     super(t);
/* 14:43 */     byte[] data = getRecord().getData();
/* 15:44 */     this.centre = (IntegerHelper.getInt(data[0], data[1]) != 0);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean isCentre()
/* 19:   */   {
/* 20:54 */     return this.centre;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.CentreRecord
 * JD-Core Version:    0.7.0.1
 */