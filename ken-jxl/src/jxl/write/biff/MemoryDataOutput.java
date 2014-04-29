/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ class MemoryDataOutput
/*   8:    */   implements ExcelDataOutput
/*   9:    */ {
/*  10: 34 */   private static Logger logger = Logger.getLogger(MemoryDataOutput.class);
/*  11:    */   private byte[] data;
/*  12:    */   private int growSize;
/*  13:    */   private int pos;
/*  14:    */   
/*  15:    */   public MemoryDataOutput(int initialSize, int gs)
/*  16:    */   {
/*  17: 56 */     this.data = new byte[initialSize];
/*  18: 57 */     this.growSize = gs;
/*  19: 58 */     this.pos = 0;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void write(byte[] bytes)
/*  23:    */   {
/*  24: 69 */     while (this.pos + bytes.length > this.data.length)
/*  25:    */     {
/*  26: 72 */       byte[] newdata = new byte[this.data.length + this.growSize];
/*  27: 73 */       System.arraycopy(this.data, 0, newdata, 0, this.pos);
/*  28: 74 */       this.data = newdata;
/*  29:    */     }
/*  30: 77 */     System.arraycopy(bytes, 0, this.data, this.pos, bytes.length);
/*  31: 78 */     this.pos += bytes.length;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public int getPosition()
/*  35:    */   {
/*  36: 88 */     return this.pos;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setData(byte[] newdata, int pos)
/*  40:    */   {
/*  41: 99 */     System.arraycopy(newdata, 0, this.data, pos, newdata.length);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void writeData(OutputStream out)
/*  45:    */     throws IOException
/*  46:    */   {
/*  47:107 */     out.write(this.data, 0, this.pos);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void close()
/*  51:    */     throws IOException
/*  52:    */   {}
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.MemoryDataOutput
 * JD-Core Version:    0.7.0.1
 */