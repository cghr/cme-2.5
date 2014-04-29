/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.Range;
/*  4:   */ import jxl.Sheet;
/*  5:   */ import jxl.biff.IntegerHelper;
/*  6:   */ import jxl.biff.RecordData;
/*  7:   */ import jxl.biff.SheetRangeImpl;
/*  8:   */ 
/*  9:   */ public class MergedCellsRecord
/* 10:   */   extends RecordData
/* 11:   */ {
/* 12:   */   private Range[] ranges;
/* 13:   */   
/* 14:   */   MergedCellsRecord(Record t, Sheet s)
/* 15:   */   {
/* 16:46 */     super(t);
/* 17:   */     
/* 18:48 */     byte[] data = getRecord().getData();
/* 19:   */     
/* 20:50 */     int numRanges = IntegerHelper.getInt(data[0], data[1]);
/* 21:   */     
/* 22:52 */     this.ranges = new Range[numRanges];
/* 23:   */     
/* 24:54 */     int pos = 2;
/* 25:55 */     int firstRow = 0;
/* 26:56 */     int lastRow = 0;
/* 27:57 */     int firstCol = 0;
/* 28:58 */     int lastCol = 0;
/* 29:60 */     for (int i = 0; i < numRanges; i++)
/* 30:   */     {
/* 31:62 */       firstRow = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/* 32:63 */       lastRow = IntegerHelper.getInt(data[(pos + 2)], data[(pos + 3)]);
/* 33:64 */       firstCol = IntegerHelper.getInt(data[(pos + 4)], data[(pos + 5)]);
/* 34:65 */       lastCol = IntegerHelper.getInt(data[(pos + 6)], data[(pos + 7)]);
/* 35:   */       
/* 36:67 */       this.ranges[i] = new SheetRangeImpl(s, firstCol, firstRow, 
/* 37:68 */         lastCol, lastRow);
/* 38:   */       
/* 39:70 */       pos += 8;
/* 40:   */     }
/* 41:   */   }
/* 42:   */   
/* 43:   */   public Range[] getRanges()
/* 44:   */   {
/* 45:81 */     return this.ranges;
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.MergedCellsRecord
 * JD-Core Version:    0.7.0.1
 */