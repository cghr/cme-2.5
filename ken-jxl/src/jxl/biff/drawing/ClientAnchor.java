/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ class ClientAnchor
/*   7:    */   extends EscherAtom
/*   8:    */ {
/*   9: 34 */   private static final Logger logger = Logger.getLogger(ClientAnchor.class);
/*  10:    */   private byte[] data;
/*  11:    */   private int properties;
/*  12:    */   private double x1;
/*  13:    */   private double y1;
/*  14:    */   private double x2;
/*  15:    */   private double y2;
/*  16:    */   
/*  17:    */   public ClientAnchor(EscherRecordData erd)
/*  18:    */   {
/*  19: 73 */     super(erd);
/*  20: 74 */     byte[] bytes = getBytes();
/*  21:    */     
/*  22:    */ 
/*  23: 77 */     this.properties = IntegerHelper.getInt(bytes[0], bytes[1]);
/*  24:    */     
/*  25:    */ 
/*  26: 80 */     int x1Cell = IntegerHelper.getInt(bytes[2], bytes[3]);
/*  27: 81 */     int x1Fraction = IntegerHelper.getInt(bytes[4], bytes[5]);
/*  28:    */     
/*  29: 83 */     this.x1 = (x1Cell + x1Fraction / 1024.0D);
/*  30:    */     
/*  31:    */ 
/*  32: 86 */     int y1Cell = IntegerHelper.getInt(bytes[6], bytes[7]);
/*  33: 87 */     int y1Fraction = IntegerHelper.getInt(bytes[8], bytes[9]);
/*  34:    */     
/*  35: 89 */     this.y1 = (y1Cell + y1Fraction / 256.0D);
/*  36:    */     
/*  37:    */ 
/*  38: 92 */     int x2Cell = IntegerHelper.getInt(bytes[10], bytes[11]);
/*  39: 93 */     int x2Fraction = IntegerHelper.getInt(bytes[12], bytes[13]);
/*  40:    */     
/*  41: 95 */     this.x2 = (x2Cell + x2Fraction / 1024.0D);
/*  42:    */     
/*  43:    */ 
/*  44: 98 */     int y2Cell = IntegerHelper.getInt(bytes[14], bytes[15]);
/*  45: 99 */     int y2Fraction = IntegerHelper.getInt(bytes[16], bytes[17]);
/*  46:    */     
/*  47:101 */     this.y2 = (y2Cell + y2Fraction / 256.0D);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public ClientAnchor(double x1, double y1, double x2, double y2, int props)
/*  51:    */   {
/*  52:115 */     super(EscherRecordType.CLIENT_ANCHOR);
/*  53:116 */     this.x1 = x1;
/*  54:117 */     this.y1 = y1;
/*  55:118 */     this.x2 = x2;
/*  56:119 */     this.y2 = y2;
/*  57:120 */     this.properties = props;
/*  58:    */   }
/*  59:    */   
/*  60:    */   byte[] getData()
/*  61:    */   {
/*  62:130 */     this.data = new byte[18];
/*  63:131 */     IntegerHelper.getTwoBytes(this.properties, this.data, 0);
/*  64:    */     
/*  65:    */ 
/*  66:134 */     IntegerHelper.getTwoBytes((int)this.x1, this.data, 2);
/*  67:    */     
/*  68:    */ 
/*  69:137 */     int x1fraction = (int)((this.x1 - (int)this.x1) * 1024.0D);
/*  70:138 */     IntegerHelper.getTwoBytes(x1fraction, this.data, 4);
/*  71:    */     
/*  72:    */ 
/*  73:141 */     IntegerHelper.getTwoBytes((int)this.y1, this.data, 6);
/*  74:    */     
/*  75:    */ 
/*  76:144 */     int y1fraction = (int)((this.y1 - (int)this.y1) * 256.0D);
/*  77:145 */     IntegerHelper.getTwoBytes(y1fraction, this.data, 8);
/*  78:    */     
/*  79:    */ 
/*  80:148 */     IntegerHelper.getTwoBytes((int)this.x2, this.data, 10);
/*  81:    */     
/*  82:    */ 
/*  83:151 */     int x2fraction = (int)((this.x2 - (int)this.x2) * 1024.0D);
/*  84:152 */     IntegerHelper.getTwoBytes(x2fraction, this.data, 12);
/*  85:    */     
/*  86:    */ 
/*  87:155 */     IntegerHelper.getTwoBytes((int)this.y2, this.data, 14);
/*  88:    */     
/*  89:    */ 
/*  90:158 */     int y2fraction = (int)((this.y2 - (int)this.y2) * 256.0D);
/*  91:159 */     IntegerHelper.getTwoBytes(y2fraction, this.data, 16);
/*  92:    */     
/*  93:161 */     return setHeaderData(this.data);
/*  94:    */   }
/*  95:    */   
/*  96:    */   double getX1()
/*  97:    */   {
/*  98:171 */     return this.x1;
/*  99:    */   }
/* 100:    */   
/* 101:    */   double getY1()
/* 102:    */   {
/* 103:181 */     return this.y1;
/* 104:    */   }
/* 105:    */   
/* 106:    */   double getX2()
/* 107:    */   {
/* 108:191 */     return this.x2;
/* 109:    */   }
/* 110:    */   
/* 111:    */   double getY2()
/* 112:    */   {
/* 113:201 */     return this.y2;
/* 114:    */   }
/* 115:    */   
/* 116:    */   int getProperties()
/* 117:    */   {
/* 118:209 */     return this.properties;
/* 119:    */   }
/* 120:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.ClientAnchor
 * JD-Core Version:    0.7.0.1
 */