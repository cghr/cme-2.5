/*  1:   */ package jxl.biff;
/*  2:   */ 
/*  3:   */ import jxl.read.biff.Record;
/*  4:   */ 
/*  5:   */ public class XCTRecord
/*  6:   */   extends WritableRecordData
/*  7:   */ {
/*  8:   */   public XCTRecord(Record t)
/*  9:   */   {
/* 10:37 */     super(t);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public byte[] getData()
/* 14:   */   {
/* 15:48 */     return getRecord().getData();
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.XCTRecord
 * JD-Core Version:    0.7.0.1
 */