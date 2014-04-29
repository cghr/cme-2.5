/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class VerticalPageBreaksRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private int[] columnBreaks;
/* 11:   */   
/* 12:   */   public VerticalPageBreaksRecord(int[] breaks)
/* 13:   */   {
/* 14:43 */     super(Type.VERTICALPAGEBREAKS);
/* 15:   */     
/* 16:45 */     this.columnBreaks = breaks;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public byte[] getData()
/* 20:   */   {
/* 21:55 */     byte[] data = new byte[this.columnBreaks.length * 6 + 2];
/* 22:   */     
/* 23:   */ 
/* 24:58 */     IntegerHelper.getTwoBytes(this.columnBreaks.length, data, 0);
/* 25:59 */     int pos = 2;
/* 26:61 */     for (int i = 0; i < this.columnBreaks.length; i++)
/* 27:   */     {
/* 28:63 */       IntegerHelper.getTwoBytes(this.columnBreaks[i], data, pos);
/* 29:64 */       IntegerHelper.getTwoBytes(255, data, pos + 4);
/* 30:65 */       pos += 6;
/* 31:   */     }
/* 32:68 */     return data;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.VerticalPageBreaksRecord
 * JD-Core Version:    0.7.0.1
 */