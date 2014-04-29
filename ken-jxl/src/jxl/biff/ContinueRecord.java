/*  1:   */ package jxl.biff;
/*  2:   */ 
/*  3:   */ import jxl.read.biff.Record;
/*  4:   */ 
/*  5:   */ public class ContinueRecord
/*  6:   */   extends WritableRecordData
/*  7:   */ {
/*  8:   */   private byte[] data;
/*  9:   */   
/* 10:   */   public ContinueRecord(Record t)
/* 11:   */   {
/* 12:42 */     super(t);
/* 13:43 */     this.data = t.getData();
/* 14:   */   }
/* 15:   */   
/* 16:   */   public ContinueRecord(byte[] d)
/* 17:   */   {
/* 18:53 */     super(Type.CONTINUE);
/* 19:54 */     this.data = d;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public byte[] getData()
/* 23:   */   {
/* 24:64 */     return this.data;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Record getRecord()
/* 28:   */   {
/* 29:76 */     return super.getRecord();
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.ContinueRecord
 * JD-Core Version:    0.7.0.1
 */