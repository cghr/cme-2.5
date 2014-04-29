/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.RecordData;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ class MulRKRecord
/*   8:    */   extends RecordData
/*   9:    */ {
/*  10: 35 */   private static Logger logger = Logger.getLogger(MulRKRecord.class);
/*  11:    */   private int row;
/*  12:    */   private int colFirst;
/*  13:    */   private int colLast;
/*  14:    */   private int numrks;
/*  15:    */   private int[] rknumbers;
/*  16:    */   private int[] xfIndices;
/*  17:    */   
/*  18:    */   public MulRKRecord(Record t)
/*  19:    */   {
/*  20: 69 */     super(t);
/*  21: 70 */     byte[] data = getRecord().getData();
/*  22: 71 */     int length = getRecord().getLength();
/*  23: 72 */     this.row = IntegerHelper.getInt(data[0], data[1]);
/*  24: 73 */     this.colFirst = IntegerHelper.getInt(data[2], data[3]);
/*  25: 74 */     this.colLast = IntegerHelper.getInt(data[(length - 2)], data[(length - 1)]);
/*  26: 75 */     this.numrks = (this.colLast - this.colFirst + 1);
/*  27: 76 */     this.rknumbers = new int[this.numrks];
/*  28: 77 */     this.xfIndices = new int[this.numrks];
/*  29:    */     
/*  30: 79 */     readRks(data);
/*  31:    */   }
/*  32:    */   
/*  33:    */   private void readRks(byte[] data)
/*  34:    */   {
/*  35: 89 */     int pos = 4;
/*  36: 91 */     for (int i = 0; i < this.numrks; i++)
/*  37:    */     {
/*  38: 93 */       this.xfIndices[i] = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  39: 94 */       int rk = IntegerHelper.getInt(
/*  40: 95 */         data[(pos + 2)], data[(pos + 3)], data[(pos + 4)], data[(pos + 5)]);
/*  41: 96 */       this.rknumbers[i] = rk;
/*  42: 97 */       pos += 6;
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getRow()
/*  47:    */   {
/*  48:108 */     return this.row;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getFirstColumn()
/*  52:    */   {
/*  53:118 */     return this.colFirst;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getNumberOfColumns()
/*  57:    */   {
/*  58:128 */     return this.numrks;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int getRKNumber(int index)
/*  62:    */   {
/*  63:139 */     return this.rknumbers[index];
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int getXFIndex(int index)
/*  67:    */   {
/*  68:150 */     return this.xfIndices[index];
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.MulRKRecord
 * JD-Core Version:    0.7.0.1
 */