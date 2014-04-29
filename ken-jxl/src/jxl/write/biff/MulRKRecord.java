/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.Type;
/*   6:    */ import jxl.biff.WritableRecordData;
/*   7:    */ import jxl.write.Number;
/*   8:    */ 
/*   9:    */ class MulRKRecord
/*  10:    */   extends WritableRecordData
/*  11:    */ {
/*  12:    */   private int row;
/*  13:    */   private int colFirst;
/*  14:    */   private int colLast;
/*  15:    */   private int[] rknumbers;
/*  16:    */   private int[] xfIndices;
/*  17:    */   
/*  18:    */   public MulRKRecord(List numbers)
/*  19:    */   {
/*  20: 62 */     super(Type.MULRK);
/*  21: 63 */     this.row = ((Number)numbers.get(0)).getRow();
/*  22: 64 */     this.colFirst = ((Number)numbers.get(0)).getColumn();
/*  23: 65 */     this.colLast = (this.colFirst + numbers.size() - 1);
/*  24:    */     
/*  25: 67 */     this.rknumbers = new int[numbers.size()];
/*  26: 68 */     this.xfIndices = new int[numbers.size()];
/*  27: 70 */     for (int i = 0; i < numbers.size(); i++)
/*  28:    */     {
/*  29: 72 */       this.rknumbers[i] = ((int)((Number)numbers.get(i)).getValue());
/*  30: 73 */       this.xfIndices[i] = ((CellValue)numbers.get(i)).getXFIndex();
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public byte[] getData()
/*  35:    */   {
/*  36: 84 */     byte[] data = new byte[this.rknumbers.length * 6 + 6];
/*  37:    */     
/*  38:    */ 
/*  39: 87 */     IntegerHelper.getTwoBytes(this.row, data, 0);
/*  40: 88 */     IntegerHelper.getTwoBytes(this.colFirst, data, 2);
/*  41:    */     
/*  42:    */ 
/*  43: 91 */     int pos = 4;
/*  44: 92 */     int rkValue = 0;
/*  45: 93 */     byte[] rkBytes = new byte[4];
/*  46: 94 */     for (int i = 0; i < this.rknumbers.length; i++)
/*  47:    */     {
/*  48: 96 */       IntegerHelper.getTwoBytes(this.xfIndices[i], data, pos);
/*  49:    */       
/*  50:    */ 
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54:102 */       rkValue = this.rknumbers[i] << 2;
/*  55:    */       
/*  56:    */ 
/*  57:105 */       rkValue |= 0x2;
/*  58:106 */       IntegerHelper.getFourBytes(rkValue, data, pos + 2);
/*  59:    */       
/*  60:108 */       pos += 6;
/*  61:    */     }
/*  62:112 */     IntegerHelper.getTwoBytes(this.colLast, data, pos);
/*  63:    */     
/*  64:114 */     return data;
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.MulRKRecord
 * JD-Core Version:    0.7.0.1
 */