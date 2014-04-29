/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ import java.util.Arrays;
/*  4:   */ 
/*  5:   */ class ChunkType
/*  6:   */ {
/*  7:   */   private byte[] id;
/*  8:   */   private String name;
/*  9:32 */   private static ChunkType[] chunkTypes = new ChunkType[0];
/* 10:   */   
/* 11:   */   private ChunkType(int d1, int d2, int d3, int d4, String n)
/* 12:   */   {
/* 13:36 */     this.id = new byte[] { (byte)d1, (byte)d2, (byte)d3, (byte)d4 };
/* 14:37 */     this.name = n;
/* 15:   */     
/* 16:39 */     ChunkType[] ct = new ChunkType[chunkTypes.length + 1];
/* 17:40 */     System.arraycopy(chunkTypes, 0, ct, 0, chunkTypes.length);
/* 18:41 */     ct[chunkTypes.length] = this;
/* 19:42 */     chunkTypes = ct;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String getName()
/* 23:   */   {
/* 24:47 */     return this.name;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public static ChunkType getChunkType(byte d1, byte d2, byte d3, byte d4)
/* 28:   */   {
/* 29:52 */     byte[] cmp = { d1, d2, d3, d4 };
/* 30:   */     
/* 31:54 */     boolean found = false;
/* 32:55 */     ChunkType chunk = UNKNOWN;
/* 33:57 */     for (int i = 0; (i < chunkTypes.length) && (!found); i++) {
/* 34:59 */       if (Arrays.equals(chunkTypes[i].id, cmp))
/* 35:   */       {
/* 36:61 */         chunk = chunkTypes[i];
/* 37:62 */         found = true;
/* 38:   */       }
/* 39:   */     }
/* 40:66 */     return chunk;
/* 41:   */   }
/* 42:   */   
/* 43:70 */   public static ChunkType IHDR = new ChunkType(73, 72, 68, 82, "IHDR");
/* 44:71 */   public static ChunkType IEND = new ChunkType(73, 69, 78, 68, "IEND");
/* 45:72 */   public static ChunkType PHYS = new ChunkType(112, 72, 89, 115, "pHYs");
/* 46:73 */   public static ChunkType UNKNOWN = new ChunkType(255, 255, 255, 255, "UNKNOWN");
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.ChunkType
 * JD-Core Version:    0.7.0.1
 */