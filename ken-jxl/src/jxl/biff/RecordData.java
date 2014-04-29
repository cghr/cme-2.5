/*  1:   */ package jxl.biff;
/*  2:   */ 
/*  3:   */ import jxl.read.biff.Record;
/*  4:   */ 
/*  5:   */ public abstract class RecordData
/*  6:   */ {
/*  7:   */   private Record record;
/*  8:   */   private int code;
/*  9:   */   
/* 10:   */   protected RecordData(Record r)
/* 11:   */   {
/* 12:47 */     this.record = r;
/* 13:48 */     this.code = r.getCode();
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected RecordData(Type t)
/* 17:   */   {
/* 18:58 */     this.code = t.value;
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected Record getRecord()
/* 22:   */   {
/* 23:68 */     return this.record;
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected final int getCode()
/* 27:   */   {
/* 28:78 */     return this.code;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.RecordData
 * JD-Core Version:    0.7.0.1
 */