/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.util.Arrays;
/*   6:    */ 
/*   7:    */ public class PNGReader
/*   8:    */ {
/*   9:    */   private byte[] pngData;
/*  10:    */   private Chunk ihdr;
/*  11:    */   private Chunk phys;
/*  12:    */   private int pixelWidth;
/*  13:    */   private int pixelHeight;
/*  14:    */   private int verticalResolution;
/*  15:    */   private int horizontalResolution;
/*  16:    */   private int resolutionUnit;
/*  17: 40 */   private static byte[] PNG_MAGIC_NUMBER = { -119, 80, 78, 71, 
/*  18: 41 */     13, 10, 26, 10 };
/*  19:    */   
/*  20:    */   public PNGReader(byte[] data)
/*  21:    */   {
/*  22: 45 */     this.pngData = data;
/*  23:    */   }
/*  24:    */   
/*  25:    */   void read()
/*  26:    */   {
/*  27: 51 */     byte[] header = new byte[PNG_MAGIC_NUMBER.length];
/*  28: 52 */     System.arraycopy(this.pngData, 0, header, 0, header.length);
/*  29: 53 */     boolean pngFile = Arrays.equals(PNG_MAGIC_NUMBER, header);
/*  30: 54 */     if (!pngFile) {
/*  31: 56 */       return;
/*  32:    */     }
/*  33: 59 */     int pos = 8;
/*  34: 60 */     while (pos < this.pngData.length)
/*  35:    */     {
/*  36: 62 */       int length = getInt(this.pngData[pos], 
/*  37: 63 */         this.pngData[(pos + 1)], 
/*  38: 64 */         this.pngData[(pos + 2)], 
/*  39: 65 */         this.pngData[(pos + 3)]);
/*  40: 66 */       ChunkType chunkType = ChunkType.getChunkType(this.pngData[(pos + 4)], 
/*  41: 67 */         this.pngData[(pos + 5)], 
/*  42: 68 */         this.pngData[(pos + 6)], 
/*  43: 69 */         this.pngData[(pos + 7)]);
/*  44: 71 */       if (chunkType == ChunkType.IHDR) {
/*  45: 73 */         this.ihdr = new Chunk(pos + 8, length, chunkType, this.pngData);
/*  46: 75 */       } else if (chunkType == ChunkType.PHYS) {
/*  47: 77 */         this.phys = new Chunk(pos + 8, length, chunkType, this.pngData);
/*  48:    */       }
/*  49: 80 */       pos += length + 12;
/*  50:    */     }
/*  51: 84 */     byte[] ihdrData = this.ihdr.getData();
/*  52: 85 */     this.pixelWidth = getInt(ihdrData[0], ihdrData[1], ihdrData[2], ihdrData[3]);
/*  53: 86 */     this.pixelHeight = getInt(ihdrData[4], ihdrData[5], ihdrData[6], ihdrData[7]);
/*  54: 88 */     if (this.phys != null)
/*  55:    */     {
/*  56: 90 */       byte[] physData = this.phys.getData();
/*  57: 91 */       this.resolutionUnit = physData[8];
/*  58: 92 */       this.horizontalResolution = getInt(physData[0], physData[1], 
/*  59: 93 */         physData[2], physData[3]);
/*  60: 94 */       this.verticalResolution = getInt(physData[4], physData[5], 
/*  61: 95 */         physData[6], physData[7]);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   private int getInt(byte d1, byte d2, byte d3, byte d4)
/*  66:    */   {
/*  67:102 */     int i1 = d1 & 0xFF;
/*  68:103 */     int i2 = d2 & 0xFF;
/*  69:104 */     int i3 = d3 & 0xFF;
/*  70:105 */     int i4 = d4 & 0xFF;
/*  71:    */     
/*  72:107 */     int val = i1 << 24 | 
/*  73:108 */       i2 << 16 | 
/*  74:109 */       i3 << 8 | 
/*  75:110 */       i4;
/*  76:    */     
/*  77:112 */     return val;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public int getHeight()
/*  81:    */   {
/*  82:117 */     return this.pixelHeight;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int getWidth()
/*  86:    */   {
/*  87:122 */     return this.pixelWidth;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int getHorizontalResolution()
/*  91:    */   {
/*  92:128 */     return this.resolutionUnit == 1 ? this.horizontalResolution : 0;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public int getVerticalResolution()
/*  96:    */   {
/*  97:134 */     return this.resolutionUnit == 1 ? this.verticalResolution : 0;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static void main(String[] args)
/* 101:    */   {
/* 102:    */     try
/* 103:    */     {
/* 104:141 */       File f = new File(args[0]);
/* 105:142 */       int size = (int)f.length();
/* 106:    */       
/* 107:144 */       byte[] data = new byte[size];
/* 108:    */       
/* 109:146 */       FileInputStream fis = new FileInputStream(f);
/* 110:147 */       fis.read(data);
/* 111:148 */       fis.close();
/* 112:149 */       PNGReader reader = new PNGReader(data);
/* 113:150 */       reader.read();
/* 114:    */     }
/* 115:    */     catch (Throwable t)
/* 116:    */     {
/* 117:154 */       t.printStackTrace();
/* 118:    */     }
/* 119:    */   }
/* 120:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.PNGReader
 * JD-Core Version:    0.7.0.1
 */