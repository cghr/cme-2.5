/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ final class EscherRecordData
/*   7:    */ {
/*   8: 36 */   private static Logger logger = Logger.getLogger(EscherRecordData.class);
/*   9:    */   private int pos;
/*  10:    */   private int instance;
/*  11:    */   private int version;
/*  12:    */   private int recordId;
/*  13:    */   private int length;
/*  14:    */   private int streamLength;
/*  15:    */   private boolean container;
/*  16:    */   private EscherRecordType type;
/*  17:    */   private EscherStream escherStream;
/*  18:    */   
/*  19:    */   public EscherRecordData(EscherStream dg, int p)
/*  20:    */   {
/*  21: 92 */     this.escherStream = dg;
/*  22: 93 */     this.pos = p;
/*  23: 94 */     byte[] data = this.escherStream.getData();
/*  24:    */     
/*  25: 96 */     this.streamLength = data.length;
/*  26:    */     
/*  27:    */ 
/*  28: 99 */     int value = IntegerHelper.getInt(data[this.pos], data[(this.pos + 1)]);
/*  29:    */     
/*  30:    */ 
/*  31:102 */     this.instance = ((value & 0xFFF0) >> 4);
/*  32:    */     
/*  33:    */ 
/*  34:105 */     this.version = (value & 0xF);
/*  35:    */     
/*  36:    */ 
/*  37:108 */     this.recordId = IntegerHelper.getInt(data[(this.pos + 2)], data[(this.pos + 3)]);
/*  38:    */     
/*  39:    */ 
/*  40:111 */     this.length = IntegerHelper.getInt(data[(this.pos + 4)], data[(this.pos + 5)], 
/*  41:112 */       data[(this.pos + 6)], data[(this.pos + 7)]);
/*  42:114 */     if (this.version == 15) {
/*  43:116 */       this.container = true;
/*  44:    */     } else {
/*  45:120 */       this.container = false;
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public EscherRecordData(EscherRecordType t)
/*  50:    */   {
/*  51:131 */     this.type = t;
/*  52:132 */     this.recordId = this.type.getValue();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public boolean isContainer()
/*  56:    */   {
/*  57:142 */     return this.container;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int getLength()
/*  61:    */   {
/*  62:152 */     return this.length;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getRecordId()
/*  66:    */   {
/*  67:162 */     return this.recordId;
/*  68:    */   }
/*  69:    */   
/*  70:    */   EscherStream getDrawingGroup()
/*  71:    */   {
/*  72:172 */     return this.escherStream;
/*  73:    */   }
/*  74:    */   
/*  75:    */   int getPos()
/*  76:    */   {
/*  77:182 */     return this.pos;
/*  78:    */   }
/*  79:    */   
/*  80:    */   EscherRecordType getType()
/*  81:    */   {
/*  82:192 */     if (this.type == null) {
/*  83:194 */       this.type = EscherRecordType.getType(this.recordId);
/*  84:    */     }
/*  85:197 */     return this.type;
/*  86:    */   }
/*  87:    */   
/*  88:    */   int getInstance()
/*  89:    */   {
/*  90:207 */     return this.instance;
/*  91:    */   }
/*  92:    */   
/*  93:    */   void setContainer(boolean c)
/*  94:    */   {
/*  95:218 */     this.container = c;
/*  96:    */   }
/*  97:    */   
/*  98:    */   void setInstance(int inst)
/*  99:    */   {
/* 100:228 */     this.instance = inst;
/* 101:    */   }
/* 102:    */   
/* 103:    */   void setLength(int l)
/* 104:    */   {
/* 105:238 */     this.length = l;
/* 106:    */   }
/* 107:    */   
/* 108:    */   void setVersion(int v)
/* 109:    */   {
/* 110:248 */     this.version = v;
/* 111:    */   }
/* 112:    */   
/* 113:    */   byte[] setHeaderData(byte[] d)
/* 114:    */   {
/* 115:260 */     byte[] data = new byte[d.length + 8];
/* 116:261 */     System.arraycopy(d, 0, data, 8, d.length);
/* 117:263 */     if (this.container) {
/* 118:265 */       this.version = 15;
/* 119:    */     }
/* 120:269 */     int value = this.instance << 4;
/* 121:270 */     value |= this.version;
/* 122:271 */     IntegerHelper.getTwoBytes(value, data, 0);
/* 123:    */     
/* 124:    */ 
/* 125:274 */     IntegerHelper.getTwoBytes(this.recordId, data, 2);
/* 126:    */     
/* 127:    */ 
/* 128:277 */     IntegerHelper.getFourBytes(d.length, data, 4);
/* 129:    */     
/* 130:279 */     return data;
/* 131:    */   }
/* 132:    */   
/* 133:    */   EscherStream getEscherStream()
/* 134:    */   {
/* 135:289 */     return this.escherStream;
/* 136:    */   }
/* 137:    */   
/* 138:    */   byte[] getBytes()
/* 139:    */   {
/* 140:299 */     byte[] d = new byte[this.length];
/* 141:300 */     System.arraycopy(this.escherStream.getData(), this.pos + 8, d, 0, this.length);
/* 142:301 */     return d;
/* 143:    */   }
/* 144:    */   
/* 145:    */   int getStreamLength()
/* 146:    */   {
/* 147:311 */     return this.streamLength;
/* 148:    */   }
/* 149:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.EscherRecordData
 * JD-Core Version:    0.7.0.1
 */