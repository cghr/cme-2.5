/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ class Chunk
/*  4:   */ {
/*  5:   */   private int pos;
/*  6:   */   private int length;
/*  7:   */   private ChunkType type;
/*  8:   */   private byte[] data;
/*  9:   */   
/* 10:   */   public Chunk(int p, int l, ChunkType ct, byte[] d)
/* 11:   */   {
/* 12:31 */     this.pos = p;
/* 13:32 */     this.length = l;
/* 14:33 */     this.type = ct;
/* 15:34 */     this.data = new byte[this.length];
/* 16:35 */     System.arraycopy(d, this.pos, this.data, 0, this.length);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public byte[] getData()
/* 20:   */   {
/* 21:41 */     return this.data;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Chunk
 * JD-Core Version:    0.7.0.1
 */