/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ public class ByteArray
/*   4:    */ {
/*   5:    */   private int growSize;
/*   6:    */   private byte[] bytes;
/*   7:    */   private int pos;
/*   8:    */   private static final int defaultGrowSize = 1024;
/*   9:    */   
/*  10:    */   public ByteArray()
/*  11:    */   {
/*  12: 50 */     this(1024);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public ByteArray(int gs)
/*  16:    */   {
/*  17: 60 */     this.growSize = gs;
/*  18: 61 */     this.bytes = new byte[1024];
/*  19: 62 */     this.pos = 0;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void add(byte b)
/*  23:    */   {
/*  24: 72 */     checkSize(1);
/*  25: 73 */     this.bytes[this.pos] = b;
/*  26: 74 */     this.pos += 1;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void add(byte[] b)
/*  30:    */   {
/*  31: 84 */     checkSize(b.length);
/*  32: 85 */     System.arraycopy(b, 0, this.bytes, this.pos, b.length);
/*  33: 86 */     this.pos += b.length;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public byte[] getBytes()
/*  37:    */   {
/*  38: 96 */     byte[] returnArray = new byte[this.pos];
/*  39: 97 */     System.arraycopy(this.bytes, 0, returnArray, 0, this.pos);
/*  40: 98 */     return returnArray;
/*  41:    */   }
/*  42:    */   
/*  43:    */   private void checkSize(int sz)
/*  44:    */   {
/*  45:109 */     while (this.pos + sz >= this.bytes.length)
/*  46:    */     {
/*  47:112 */       byte[] newArray = new byte[this.bytes.length + this.growSize];
/*  48:113 */       System.arraycopy(this.bytes, 0, newArray, 0, this.pos);
/*  49:114 */       this.bytes = newArray;
/*  50:    */     }
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.ByteArray
 * JD-Core Version:    0.7.0.1
 */