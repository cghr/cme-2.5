/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ 
/*   7:    */ class IndexRecord
/*   8:    */   extends WritableRecordData
/*   9:    */ {
/*  10:    */   private byte[] data;
/*  11:    */   private int rows;
/*  12:    */   private int bofPosition;
/*  13:    */   private int blocks;
/*  14:    */   private int dataPos;
/*  15:    */   
/*  16:    */   public IndexRecord(int pos, int r, int bl)
/*  17:    */   {
/*  18: 62 */     super(Type.INDEX);
/*  19: 63 */     this.bofPosition = pos;
/*  20: 64 */     this.rows = r;
/*  21: 65 */     this.blocks = bl;
/*  22:    */     
/*  23:    */ 
/*  24: 68 */     this.data = new byte[16 + 4 * this.blocks];
/*  25: 69 */     this.dataPos = 16;
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected byte[] getData()
/*  29:    */   {
/*  30: 81 */     IntegerHelper.getFourBytes(this.rows, this.data, 8);
/*  31: 82 */     return this.data;
/*  32:    */   }
/*  33:    */   
/*  34:    */   void addBlockPosition(int pos)
/*  35:    */   {
/*  36: 92 */     IntegerHelper.getFourBytes(pos - this.bofPosition, this.data, this.dataPos);
/*  37: 93 */     this.dataPos += 4;
/*  38:    */   }
/*  39:    */   
/*  40:    */   void setDataStartPosition(int pos)
/*  41:    */   {
/*  42:102 */     IntegerHelper.getFourBytes(pos - this.bofPosition, this.data, 12);
/*  43:    */   }
/*  44:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.IndexRecord
 * JD-Core Version:    0.7.0.1
 */