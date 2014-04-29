/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.RecordData;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ class DimensionRecord
/*   8:    */   extends RecordData
/*   9:    */ {
/*  10: 35 */   private static Logger logger = Logger.getLogger(DimensionRecord.class);
/*  11:    */   private int numRows;
/*  12:    */   private int numCols;
/*  13: 50 */   public static Biff7 biff7 = new Biff7(null);
/*  14:    */   
/*  15:    */   public DimensionRecord(Record t)
/*  16:    */   {
/*  17: 59 */     super(t);
/*  18: 60 */     byte[] data = t.getData();
/*  19: 66 */     if (data.length == 10) {
/*  20: 68 */       read10ByteData(data);
/*  21:    */     } else {
/*  22: 72 */       read14ByteData(data);
/*  23:    */     }
/*  24:    */   }
/*  25:    */   
/*  26:    */   public DimensionRecord(Record t, Biff7 biff7)
/*  27:    */   {
/*  28: 84 */     super(t);
/*  29: 85 */     byte[] data = t.getData();
/*  30: 86 */     read10ByteData(data);
/*  31:    */   }
/*  32:    */   
/*  33:    */   private void read10ByteData(byte[] data)
/*  34:    */   {
/*  35: 95 */     this.numRows = IntegerHelper.getInt(data[2], data[3]);
/*  36: 96 */     this.numCols = IntegerHelper.getInt(data[6], data[7]);
/*  37:    */   }
/*  38:    */   
/*  39:    */   private void read14ByteData(byte[] data)
/*  40:    */   {
/*  41:105 */     this.numRows = IntegerHelper.getInt(data[4], data[5], data[6], data[7]);
/*  42:106 */     this.numCols = IntegerHelper.getInt(data[10], data[11]);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public int getNumberOfRows()
/*  46:    */   {
/*  47:116 */     return this.numRows;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getNumberOfColumns()
/*  51:    */   {
/*  52:126 */     return this.numCols;
/*  53:    */   }
/*  54:    */   
/*  55:    */   private static class Biff7 {}
/*  56:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.DimensionRecord
 * JD-Core Version:    0.7.0.1
 */