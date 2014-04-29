/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.common.Logger;
/*  6:   */ 
/*  7:   */ class VerticalPageBreaksRecord
/*  8:   */   extends RecordData
/*  9:   */ {
/* 10:36 */   private final Logger logger = Logger.getLogger(VerticalPageBreaksRecord.class);
/* 11:   */   private int[] columnBreaks;
/* 12:47 */   public static Biff7 biff7 = new Biff7(null);
/* 13:   */   
/* 14:   */   public VerticalPageBreaksRecord(Record t)
/* 15:   */   {
/* 16:56 */     super(t);
/* 17:   */     
/* 18:58 */     byte[] data = t.getData();
/* 19:   */     
/* 20:60 */     int numbreaks = IntegerHelper.getInt(data[0], data[1]);
/* 21:61 */     int pos = 2;
/* 22:62 */     this.columnBreaks = new int[numbreaks];
/* 23:64 */     for (int i = 0; i < numbreaks; i++)
/* 24:   */     {
/* 25:66 */       this.columnBreaks[i] = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/* 26:67 */       pos += 6;
/* 27:   */     }
/* 28:   */   }
/* 29:   */   
/* 30:   */   public VerticalPageBreaksRecord(Record t, Biff7 biff7)
/* 31:   */   {
/* 32:79 */     super(t);
/* 33:   */     
/* 34:81 */     byte[] data = t.getData();
/* 35:82 */     int numbreaks = IntegerHelper.getInt(data[0], data[1]);
/* 36:83 */     int pos = 2;
/* 37:84 */     this.columnBreaks = new int[numbreaks];
/* 38:85 */     for (int i = 0; i < numbreaks; i++)
/* 39:   */     {
/* 40:87 */       this.columnBreaks[i] = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/* 41:88 */       pos += 2;
/* 42:   */     }
/* 43:   */   }
/* 44:   */   
/* 45:   */   public int[] getColumnBreaks()
/* 46:   */   {
/* 47:99 */     return this.columnBreaks;
/* 48:   */   }
/* 49:   */   
/* 50:   */   private static class Biff7 {}
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.VerticalPageBreaksRecord
 * JD-Core Version:    0.7.0.1
 */