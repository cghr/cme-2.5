/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.format.Colour;
/*   4:    */ import jxl.format.RGB;
/*   5:    */ import jxl.read.biff.Record;
/*   6:    */ 
/*   7:    */ public class PaletteRecord
/*   8:    */   extends WritableRecordData
/*   9:    */ {
/*  10: 34 */   private RGB[] rgbColours = new RGB[56];
/*  11:    */   private boolean dirty;
/*  12:    */   private boolean read;
/*  13:    */   private boolean initialized;
/*  14:    */   private static final int numColours = 56;
/*  15:    */   
/*  16:    */   public PaletteRecord(Record t)
/*  17:    */   {
/*  18: 64 */     super(t);
/*  19:    */     
/*  20: 66 */     this.initialized = false;
/*  21: 67 */     this.dirty = false;
/*  22: 68 */     this.read = true;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public PaletteRecord()
/*  26:    */   {
/*  27: 76 */     super(Type.PALETTE);
/*  28:    */     
/*  29: 78 */     this.initialized = true;
/*  30: 79 */     this.dirty = false;
/*  31: 80 */     this.read = false;
/*  32:    */     
/*  33:    */ 
/*  34: 83 */     Colour[] colours = Colour.getAllColours();
/*  35: 85 */     for (int i = 0; i < colours.length; i++)
/*  36:    */     {
/*  37: 87 */       Colour c = colours[i];
/*  38: 88 */       setColourRGB(c, 
/*  39: 89 */         c.getDefaultRGB().getRed(), 
/*  40: 90 */         c.getDefaultRGB().getGreen(), 
/*  41: 91 */         c.getDefaultRGB().getBlue());
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   public byte[] getData()
/*  46:    */   {
/*  47:103 */     if ((this.read) && (!this.dirty)) {
/*  48:105 */       return getRecord().getData();
/*  49:    */     }
/*  50:108 */     byte[] data = new byte['â'];
/*  51:109 */     int pos = 0;
/*  52:    */     
/*  53:    */ 
/*  54:112 */     IntegerHelper.getTwoBytes(56, data, pos);
/*  55:115 */     for (int i = 0; i < 56; i++)
/*  56:    */     {
/*  57:117 */       pos = i * 4 + 2;
/*  58:118 */       data[pos] = ((byte)this.rgbColours[i].getRed());
/*  59:119 */       data[(pos + 1)] = ((byte)this.rgbColours[i].getGreen());
/*  60:120 */       data[(pos + 2)] = ((byte)this.rgbColours[i].getBlue());
/*  61:    */     }
/*  62:123 */     return data;
/*  63:    */   }
/*  64:    */   
/*  65:    */   private void initialize()
/*  66:    */   {
/*  67:131 */     byte[] data = getRecord().getData();
/*  68:    */     
/*  69:133 */     int numrecords = IntegerHelper.getInt(data[0], data[1]);
/*  70:135 */     for (int i = 0; i < numrecords; i++)
/*  71:    */     {
/*  72:137 */       int pos = i * 4 + 2;
/*  73:138 */       int red = IntegerHelper.getInt(data[pos], (byte)0);
/*  74:139 */       int green = IntegerHelper.getInt(data[(pos + 1)], (byte)0);
/*  75:140 */       int blue = IntegerHelper.getInt(data[(pos + 2)], (byte)0);
/*  76:141 */       this.rgbColours[i] = new RGB(red, green, blue);
/*  77:    */     }
/*  78:144 */     this.initialized = true;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean isDirty()
/*  82:    */   {
/*  83:155 */     return this.dirty;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setColourRGB(Colour c, int r, int g, int b)
/*  87:    */   {
/*  88:169 */     int pos = c.getValue() - 8;
/*  89:170 */     if ((pos < 0) || (pos >= 56)) {
/*  90:172 */       return;
/*  91:    */     }
/*  92:175 */     if (!this.initialized) {
/*  93:177 */       initialize();
/*  94:    */     }
/*  95:181 */     r = setValueRange(r, 0, 255);
/*  96:182 */     g = setValueRange(g, 0, 255);
/*  97:183 */     b = setValueRange(b, 0, 255);
/*  98:    */     
/*  99:185 */     this.rgbColours[pos] = new RGB(r, g, b);
/* 100:    */     
/* 101:    */ 
/* 102:188 */     this.dirty = true;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public RGB getColourRGB(Colour c)
/* 106:    */   {
/* 107:200 */     int pos = c.getValue() - 8;
/* 108:201 */     if ((pos < 0) || (pos >= 56)) {
/* 109:203 */       return c.getDefaultRGB();
/* 110:    */     }
/* 111:206 */     if (!this.initialized) {
/* 112:208 */       initialize();
/* 113:    */     }
/* 114:211 */     return this.rgbColours[pos];
/* 115:    */   }
/* 116:    */   
/* 117:    */   private int setValueRange(int val, int min, int max)
/* 118:    */   {
/* 119:224 */     val = Math.max(val, min);
/* 120:225 */     val = Math.min(val, max);
/* 121:226 */     return val;
/* 122:    */   }
/* 123:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.PaletteRecord
 * JD-Core Version:    0.7.0.1
 */