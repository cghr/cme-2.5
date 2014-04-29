/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ import java.util.Random;
/*   5:    */ 
/*   6:    */ public class NtpMessageJ2ME
/*   7:    */ {
/*   8: 72 */   public byte leapIndicator = 0;
/*   9: 81 */   public byte version = 3;
/*  10:102 */   public byte mode = 0;
/*  11:116 */   public short stratum = 0;
/*  12:125 */   public byte pollInterval = 0;
/*  13:134 */   public byte precision = 0;
/*  14:145 */   public double rootDelay = 0.0D;
/*  15:153 */   public double rootDispersion = 0.0D;
/*  16:190 */   public byte[] referenceIdentifier = new byte[4];
/*  17:197 */   public double referenceTimestamp = 0.0D;
/*  18:204 */   public double originateTimestamp = 0.0D;
/*  19:211 */   public double receiveTimestamp = 0.0D;
/*  20:218 */   public double transmitTimestamp = 0.0D;
/*  21:    */   
/*  22:    */   public NtpMessageJ2ME(byte[] array)
/*  23:    */   {
/*  24:228 */     this.leapIndicator = ((byte)(array[0] >> 6 & 0x3));
/*  25:229 */     this.version = ((byte)(array[0] >> 3 & 0x7));
/*  26:230 */     this.mode = ((byte)(array[0] & 0x7));
/*  27:231 */     this.stratum = unsignedByteToShort(array[1]);
/*  28:232 */     this.pollInterval = array[2];
/*  29:233 */     this.precision = array[3];
/*  30:    */     
/*  31:235 */     this.rootDelay = 
/*  32:    */     
/*  33:    */ 
/*  34:238 */       (array[4] * 256.0D + unsignedByteToShort(array[5]) + unsignedByteToShort(array[6]) / 256.0D + unsignedByteToShort(array[7]) / 65536.0D);
/*  35:    */     
/*  36:240 */     this.rootDispersion = 
/*  37:    */     
/*  38:    */ 
/*  39:243 */       (unsignedByteToShort(array[8]) * 256.0D + unsignedByteToShort(array[9]) + unsignedByteToShort(array[10]) / 256.0D + unsignedByteToShort(array[11]) / 65536.0D);
/*  40:    */     
/*  41:245 */     this.referenceIdentifier[0] = array[12];
/*  42:246 */     this.referenceIdentifier[1] = array[13];
/*  43:247 */     this.referenceIdentifier[2] = array[14];
/*  44:248 */     this.referenceIdentifier[3] = array[15];
/*  45:    */     
/*  46:250 */     this.referenceTimestamp = decodeTimestamp(array, 16);
/*  47:251 */     this.originateTimestamp = decodeTimestamp(array, 24);
/*  48:252 */     this.receiveTimestamp = decodeTimestamp(array, 32);
/*  49:253 */     this.transmitTimestamp = decodeTimestamp(array, 40);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public NtpMessageJ2ME()
/*  53:    */   {
/*  54:266 */     this.mode = 3;
/*  55:267 */     this.transmitTimestamp = (System.currentTimeMillis() / 1000.0D + 2208988800.0D);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public byte[] toByteArray()
/*  59:    */   {
/*  60:278 */     byte[] p = new byte[48];
/*  61:    */     
/*  62:280 */     p[0] = ((byte)(this.leapIndicator << 6 | this.version << 3 | this.mode));
/*  63:281 */     p[1] = ((byte)this.stratum);
/*  64:282 */     p[2] = this.pollInterval;
/*  65:283 */     p[3] = this.precision;
/*  66:    */     
/*  67:    */ 
/*  68:286 */     int l = (int)(this.rootDelay * 65536.0D);
/*  69:287 */     p[4] = ((byte)(l >> 24 & 0xFF));
/*  70:288 */     p[5] = ((byte)(l >> 16 & 0xFF));
/*  71:289 */     p[6] = ((byte)(l >> 8 & 0xFF));
/*  72:290 */     p[7] = ((byte)(l & 0xFF));
/*  73:    */     
/*  74:    */ 
/*  75:    */ 
/*  76:294 */     long ul = (this.rootDispersion * 65536.0D);
/*  77:295 */     p[8] = ((byte)(int)(ul >> 24 & 0xFF));
/*  78:296 */     p[9] = ((byte)(int)(ul >> 16 & 0xFF));
/*  79:297 */     p[10] = ((byte)(int)(ul >> 8 & 0xFF));
/*  80:298 */     p[11] = ((byte)(int)(ul & 0xFF));
/*  81:    */     
/*  82:300 */     p[12] = this.referenceIdentifier[0];
/*  83:301 */     p[13] = this.referenceIdentifier[1];
/*  84:302 */     p[14] = this.referenceIdentifier[2];
/*  85:303 */     p[15] = this.referenceIdentifier[3];
/*  86:    */     
/*  87:305 */     encodeTimestamp(p, 16, this.referenceTimestamp);
/*  88:306 */     encodeTimestamp(p, 24, this.originateTimestamp);
/*  89:307 */     encodeTimestamp(p, 32, this.receiveTimestamp);
/*  90:308 */     encodeTimestamp(p, 40, this.transmitTimestamp);
/*  91:    */     
/*  92:310 */     return p;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public String toString()
/*  96:    */   {
/*  97:320 */     String strAux = "Leap indicator: " + this.leapIndicator + "\r\n" + 
/*  98:321 */       "Version: " + this.version + "\r\n" + 
/*  99:322 */       "Mode: " + this.mode + "\r\n" + 
/* 100:323 */       "Stratum: " + this.stratum + "\r\n" + 
/* 101:324 */       "Poll: " + this.pollInterval + "\r\n" + 
/* 102:325 */       "Precision: " + this.precision + "\r\n" + 
/* 103:326 */       "Root delay: " + this.rootDelay * 1000.0D + " ms\r\n" + 
/* 104:327 */       "Root dispersion: " + this.rootDispersion * 1000.0D + " ms\r\n" + 
/* 105:328 */       "Reference identifier: " + referenceIdentifierToString(this.referenceIdentifier, this.stratum, this.version) + "\n" + 
/* 106:329 */       "Reference timestamp: " + timestampToString(this.referenceTimestamp) + "\r\n" + 
/* 107:330 */       "Originate timestamp: " + timestampToString(this.originateTimestamp) + "\r\n" + 
/* 108:331 */       "Receive timestamp:   " + timestampToString(this.receiveTimestamp) + "\r\n" + 
/* 109:332 */       "Transmit timestamp:  " + timestampToString(this.transmitTimestamp);
/* 110:    */     
/* 111:334 */     return strAux;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static short unsignedByteToShort(byte b)
/* 115:    */   {
/* 116:345 */     if ((b & 0x80) == 128) {
/* 117:345 */       return (short)(128 + (b & 0x7F));
/* 118:    */     }
/* 119:346 */     return (short)b;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static double decodeTimestamp(byte[] array, int pointer)
/* 123:    */   {
/* 124:358 */     double r = 0.0D;
/* 125:359 */     double p = 0.0D;
/* 126:360 */     short s = 0;
/* 127:361 */     for (int i = 0; i < 8; i++)
/* 128:    */     {
/* 129:363 */       p = _pow(2.0D, (3 - i) * 8);
/* 130:364 */       s = unsignedByteToShort(array[(pointer + i)]);
/* 131:365 */       r += s * p;
/* 132:    */     }
/* 133:368 */     return r;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public static void encodeTimestamp(byte[] array, int pointer, double timestamp)
/* 137:    */   {
/* 138:378 */     Random rd = new Random();
/* 139:381 */     for (int i = 0; i < 8; i++)
/* 140:    */     {
/* 141:384 */       double base = _pow(2.0D, (3 - i) * 8);
/* 142:    */       
/* 143:    */ 
/* 144:387 */       array[(pointer + i)] = ((byte)(int)(timestamp / base));
/* 145:    */       
/* 146:    */ 
/* 147:390 */       timestamp -= unsignedByteToShort(array[(pointer + i)]) * base;
/* 148:    */     }
/* 149:397 */     array[7] = ((byte)(int)(rd.nextDouble() * 255.0D));
/* 150:    */   }
/* 151:    */   
/* 152:    */   public static String timestampToString(double timestamp)
/* 153:    */   {
/* 154:408 */     if (timestamp == 0.0D) {
/* 155:408 */       return "0";
/* 156:    */     }
/* 157:412 */     double utc = timestamp - 2208988800.0D;
/* 158:    */     
/* 159:    */ 
/* 160:415 */     long ms = (utc * 1000.0D);
/* 161:    */     
/* 162:    */ 
/* 163:418 */     String date = new Date(ms).toString();
/* 164:    */     
/* 165:    */ 
/* 166:421 */     return date;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static String referenceIdentifierToString(byte[] ref, short stratum, byte version)
/* 170:    */   {
/* 171:436 */     if ((stratum == 0) || (stratum == 1)) {
/* 172:438 */       return new String(ref);
/* 173:    */     }
/* 174:443 */     if (version == 3) {
/* 175:445 */       return 
/* 176:    */       
/* 177:    */ 
/* 178:448 */         unsignedByteToShort(ref[0]) + "." + unsignedByteToShort(ref[1]) + "." + unsignedByteToShort(ref[2]) + "." + unsignedByteToShort(ref[3]);
/* 179:    */     }
/* 180:453 */     if (version == 4) {
/* 181:455 */       return 
/* 182:    */       
/* 183:    */ 
/* 184:458 */         unsignedByteToShort(ref[0]) / 256.0D + unsignedByteToShort(ref[1]) / 65536.0D + unsignedByteToShort(ref[2]) / 16777216.0D + unsignedByteToShort(ref[3]) / 4294967296.0D;
/* 185:    */     }
/* 186:461 */     return "";
/* 187:    */   }
/* 188:    */   
/* 189:    */   public static double _pow(double a, double b)
/* 190:    */   {
/* 191:472 */     double dAux = a;
/* 192:474 */     if (b == 0.0D) {
/* 193:476 */       return 1.0D;
/* 194:    */     }
/* 195:    */     int count;
/* 196:    */     int count;
/* 197:479 */     if (b < 0.0D) {
/* 198:481 */       count = -(int)b;
/* 199:    */     } else {
/* 200:485 */       count = (int)b;
/* 201:    */     }
/* 202:488 */     for (int i = 1; i < count; i++) {
/* 203:490 */       dAux *= a;
/* 204:    */     }
/* 205:493 */     if (b < 0.0D) {
/* 206:495 */       dAux = 1.0D / dAux;
/* 207:    */     }
/* 208:498 */     return dAux;
/* 209:    */   }
/* 210:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.NtpMessageJ2ME
 * JD-Core Version:    0.7.0.1
 */