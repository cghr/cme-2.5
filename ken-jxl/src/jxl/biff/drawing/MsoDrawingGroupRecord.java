/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ import jxl.read.biff.Record;
/*  6:   */ 
/*  7:   */ public class MsoDrawingGroupRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private byte[] data;
/* 11:   */   
/* 12:   */   public MsoDrawingGroupRecord(Record t)
/* 13:   */   {
/* 14:44 */     super(t);
/* 15:45 */     this.data = t.getData();
/* 16:   */   }
/* 17:   */   
/* 18:   */   MsoDrawingGroupRecord(byte[] d)
/* 19:   */   {
/* 20:55 */     super(Type.MSODRAWINGGROUP);
/* 21:56 */     this.data = d;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public byte[] getData()
/* 25:   */   {
/* 26:66 */     return this.data;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.MsoDrawingGroupRecord
 * JD-Core Version:    0.7.0.1
 */