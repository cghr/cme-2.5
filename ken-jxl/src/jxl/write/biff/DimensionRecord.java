/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class DimensionRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private int numRows;
/* 11:   */   private int numCols;
/* 12:   */   private byte[] data;
/* 13:   */   
/* 14:   */   public DimensionRecord(int r, int c)
/* 15:   */   {
/* 16:53 */     super(Type.DIMENSION);
/* 17:54 */     this.numRows = r;
/* 18:55 */     this.numCols = c;
/* 19:   */     
/* 20:57 */     this.data = new byte[14];
/* 21:   */     
/* 22:59 */     IntegerHelper.getFourBytes(this.numRows, this.data, 4);
/* 23:60 */     IntegerHelper.getTwoBytes(this.numCols, this.data, 10);
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected byte[] getData()
/* 27:   */   {
/* 28:70 */     return this.data;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.DimensionRecord
 * JD-Core Version:    0.7.0.1
 */