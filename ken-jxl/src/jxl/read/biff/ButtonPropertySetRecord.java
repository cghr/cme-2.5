/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.RecordData;
/*  4:   */ import jxl.common.Logger;
/*  5:   */ 
/*  6:   */ public class ButtonPropertySetRecord
/*  7:   */   extends RecordData
/*  8:   */ {
/*  9:35 */   private static Logger logger = Logger.getLogger(ButtonPropertySetRecord.class);
/* 10:   */   
/* 11:   */   ButtonPropertySetRecord(Record t)
/* 12:   */   {
/* 13:45 */     super(t);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public byte[] getData()
/* 17:   */   {
/* 18:55 */     return getRecord().getData();
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.ButtonPropertySetRecord
 * JD-Core Version:    0.7.0.1
 */