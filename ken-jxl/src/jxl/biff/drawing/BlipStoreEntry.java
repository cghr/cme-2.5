/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.common.Assert;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ class BlipStoreEntry
/*   9:    */   extends EscherAtom
/*  10:    */ {
/*  11: 37 */   private static Logger logger = Logger.getLogger(BlipStoreEntry.class);
/*  12:    */   private BlipType type;
/*  13:    */   private byte[] data;
/*  14:    */   private int imageDataLength;
/*  15:    */   private int referenceCount;
/*  16:    */   private boolean write;
/*  17:    */   private static final int IMAGE_DATA_OFFSET = 61;
/*  18:    */   
/*  19:    */   public BlipStoreEntry(EscherRecordData erd)
/*  20:    */   {
/*  21: 77 */     super(erd);
/*  22: 78 */     this.type = BlipType.getType(getInstance());
/*  23: 79 */     this.write = false;
/*  24: 80 */     byte[] bytes = getBytes();
/*  25: 81 */     this.referenceCount = IntegerHelper.getInt(bytes[24], bytes[25], 
/*  26: 82 */       bytes[26], bytes[27]);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public BlipStoreEntry(Drawing d)
/*  30:    */     throws IOException
/*  31:    */   {
/*  32: 93 */     super(EscherRecordType.BSE);
/*  33: 94 */     this.type = BlipType.PNG;
/*  34: 95 */     setVersion(2);
/*  35: 96 */     setInstance(this.type.getValue());
/*  36:    */     
/*  37: 98 */     byte[] imageData = d.getImageBytes();
/*  38: 99 */     this.imageDataLength = imageData.length;
/*  39:100 */     this.data = new byte[this.imageDataLength + 61];
/*  40:101 */     System.arraycopy(imageData, 0, this.data, 61, this.imageDataLength);
/*  41:102 */     this.referenceCount = d.getReferenceCount();
/*  42:103 */     this.write = true;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public BlipType getBlipType()
/*  46:    */   {
/*  47:113 */     return this.type;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public byte[] getData()
/*  51:    */   {
/*  52:123 */     if (this.write)
/*  53:    */     {
/*  54:128 */       this.data[0] = ((byte)this.type.getValue());
/*  55:    */       
/*  56:    */ 
/*  57:131 */       this.data[1] = ((byte)this.type.getValue());
/*  58:    */       
/*  59:    */ 
/*  60:    */ 
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:140 */       IntegerHelper.getFourBytes(this.imageDataLength + 8 + 17, this.data, 20);
/*  67:    */       
/*  68:    */ 
/*  69:143 */       IntegerHelper.getFourBytes(this.referenceCount, this.data, 24);
/*  70:    */       
/*  71:    */ 
/*  72:146 */       IntegerHelper.getFourBytes(0, this.data, 28);
/*  73:    */       
/*  74:    */ 
/*  75:149 */       this.data[32] = 0;
/*  76:    */       
/*  77:    */ 
/*  78:152 */       this.data[33] = 0;
/*  79:    */       
/*  80:    */ 
/*  81:155 */       this.data[34] = 126;
/*  82:156 */       this.data[35] = 1;
/*  83:    */       
/*  84:    */ 
/*  85:159 */       this.data[36] = 0;
/*  86:160 */       this.data[37] = 110;
/*  87:    */       
/*  88:    */ 
/*  89:163 */       IntegerHelper.getTwoBytes(61470, this.data, 38);
/*  90:    */       
/*  91:    */ 
/*  92:    */ 
/*  93:167 */       IntegerHelper.getFourBytes(this.imageDataLength + 17, this.data, 40);
/*  94:    */     }
/*  95:    */     else
/*  96:    */     {
/*  97:175 */       this.data = getBytes();
/*  98:    */     }
/*  99:178 */     return setHeaderData(this.data);
/* 100:    */   }
/* 101:    */   
/* 102:    */   void dereference()
/* 103:    */   {
/* 104:187 */     this.referenceCount -= 1;
/* 105:188 */     Assert.verify(this.referenceCount >= 0);
/* 106:    */   }
/* 107:    */   
/* 108:    */   int getReferenceCount()
/* 109:    */   {
/* 110:198 */     return this.referenceCount;
/* 111:    */   }
/* 112:    */   
/* 113:    */   byte[] getImageData()
/* 114:    */   {
/* 115:208 */     byte[] allData = getBytes();
/* 116:209 */     byte[] imageData = new byte[allData.length - 61];
/* 117:210 */     System.arraycopy(allData, 61, 
/* 118:211 */       imageData, 0, imageData.length);
/* 119:212 */     return imageData;
/* 120:    */   }
/* 121:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.BlipStoreEntry
 * JD-Core Version:    0.7.0.1
 */