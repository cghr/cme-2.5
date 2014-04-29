/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.common.Logger;
/*  6:   */ 
/*  7:   */ class HideobjRecord
/*  8:   */   extends RecordData
/*  9:   */ {
/* 10:34 */   private static Logger logger = Logger.getLogger(HideobjRecord.class);
/* 11:   */   private int hidemode;
/* 12:   */   
/* 13:   */   public HideobjRecord(Record t)
/* 14:   */   {
/* 15:48 */     super(t);
/* 16:49 */     byte[] data = t.getData();
/* 17:50 */     this.hidemode = IntegerHelper.getInt(data[0], data[1]);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public int getHideMode()
/* 21:   */   {
/* 22:60 */     return this.hidemode;
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.HideobjRecord
 * JD-Core Version:    0.7.0.1
 */