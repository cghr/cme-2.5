/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.RecordData;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ class MulBlankRecord
/*   8:    */   extends RecordData
/*   9:    */ {
/*  10: 35 */   private static Logger logger = Logger.getLogger(MulBlankRecord.class);
/*  11:    */   private int row;
/*  12:    */   private int colFirst;
/*  13:    */   private int colLast;
/*  14:    */   private int numblanks;
/*  15:    */   private int[] xfIndices;
/*  16:    */   
/*  17:    */   public MulBlankRecord(Record t)
/*  18:    */   {
/*  19: 65 */     super(t);
/*  20: 66 */     byte[] data = getRecord().getData();
/*  21: 67 */     int length = getRecord().getLength();
/*  22: 68 */     this.row = IntegerHelper.getInt(data[0], data[1]);
/*  23: 69 */     this.colFirst = IntegerHelper.getInt(data[2], data[3]);
/*  24: 70 */     this.colLast = IntegerHelper.getInt(data[(length - 2)], data[(length - 1)]);
/*  25: 71 */     this.numblanks = (this.colLast - this.colFirst + 1);
/*  26: 72 */     this.xfIndices = new int[this.numblanks];
/*  27:    */     
/*  28: 74 */     readBlanks(data);
/*  29:    */   }
/*  30:    */   
/*  31:    */   private void readBlanks(byte[] data)
/*  32:    */   {
/*  33: 84 */     int pos = 4;
/*  34: 85 */     for (int i = 0; i < this.numblanks; i++)
/*  35:    */     {
/*  36: 87 */       this.xfIndices[i] = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  37: 88 */       pos += 2;
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getRow()
/*  42:    */   {
/*  43: 99 */     return this.row;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getFirstColumn()
/*  47:    */   {
/*  48:109 */     return this.colFirst;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getNumberOfColumns()
/*  52:    */   {
/*  53:119 */     return this.numblanks;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getXFIndex(int index)
/*  57:    */   {
/*  58:129 */     return this.xfIndices[index];
/*  59:    */   }
/*  60:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.MulBlankRecord
 * JD-Core Version:    0.7.0.1
 */