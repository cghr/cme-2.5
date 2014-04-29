/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import jxl.Cell;
/*  5:   */ import jxl.Range;
/*  6:   */ import jxl.biff.IntegerHelper;
/*  7:   */ import jxl.biff.Type;
/*  8:   */ import jxl.biff.WritableRecordData;
/*  9:   */ 
/* 10:   */ public class MergedCellsRecord
/* 11:   */   extends WritableRecordData
/* 12:   */ {
/* 13:   */   private ArrayList ranges;
/* 14:   */   
/* 15:   */   protected MergedCellsRecord(ArrayList mc)
/* 16:   */   {
/* 17:48 */     super(Type.MERGEDCELLS);
/* 18:   */     
/* 19:50 */     this.ranges = mc;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public byte[] getData()
/* 23:   */   {
/* 24:60 */     byte[] data = new byte[this.ranges.size() * 8 + 2];
/* 25:   */     
/* 26:   */ 
/* 27:63 */     IntegerHelper.getTwoBytes(this.ranges.size(), data, 0);
/* 28:   */     
/* 29:65 */     int pos = 2;
/* 30:66 */     Range range = null;
/* 31:67 */     for (int i = 0; i < this.ranges.size(); i++)
/* 32:   */     {
/* 33:69 */       range = (Range)this.ranges.get(i);
/* 34:   */       
/* 35:   */ 
/* 36:72 */       Cell tl = range.getTopLeft();
/* 37:73 */       Cell br = range.getBottomRight();
/* 38:   */       
/* 39:75 */       IntegerHelper.getTwoBytes(tl.getRow(), data, pos);
/* 40:76 */       IntegerHelper.getTwoBytes(br.getRow(), data, pos + 2);
/* 41:77 */       IntegerHelper.getTwoBytes(tl.getColumn(), data, pos + 4);
/* 42:78 */       IntegerHelper.getTwoBytes(br.getColumn(), data, pos + 6);
/* 43:   */       
/* 44:80 */       pos += 8;
/* 45:   */     }
/* 46:83 */     return data;
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.MergedCellsRecord
 * JD-Core Version:    0.7.0.1
 */