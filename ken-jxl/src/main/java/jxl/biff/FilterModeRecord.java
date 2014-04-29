/*  1:   */ package jxl.biff;
/*  2:   */ 
/*  3:   */ import jxl.common.Logger;
/*  4:   */ import jxl.read.biff.Record;
/*  5:   */ 
/*  6:   */ public class FilterModeRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:32 */   private static Logger logger = Logger.getLogger(FilterModeRecord.class);
/* 10:   */   private byte[] data;
/* 11:   */   
/* 12:   */   public FilterModeRecord(Record t)
/* 13:   */   {
/* 14:45 */     super(t);
/* 15:   */     
/* 16:47 */     this.data = getRecord().getData();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public byte[] getData()
/* 20:   */   {
/* 21:57 */     return this.data;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.FilterModeRecord
 * JD-Core Version:    0.7.0.1
 */