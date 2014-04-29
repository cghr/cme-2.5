/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.text.SimpleDateFormat;
/*   5:    */ import java.util.Date;
/*   6:    */ 
/*   7:    */ public class NtpMessage
/*   8:    */ {
/*   9: 75 */   public byte leapIndicator = 0;
/*  10: 84 */   public byte version = 3;
/*  11:105 */   public byte mode = 0;
/*  12:119 */   public short stratum = 0;
/*  13:128 */   public byte pollInterval = 0;
/*  14:137 */   public byte precision = 0;
/*  15:148 */   public double rootDelay = 0.0D;
/*  16:156 */   public double rootDispersion = 0.0D;
/*  17:193 */   public byte[] referenceIdentifier = new byte[4];
/*  18:200 */   public double referenceTimestamp = 0.0D;
/*  19:207 */   public double originateTimestamp = 0.0D;
/*  20:214 */   public double receiveTimestamp = 0.0D;
/*  21:221 */   public double transmitTimestamp = 0.0D;
/*  22:    */   
/*  23:    */   public NtpMessage(byte[] array)
/*  24:    */   {
/*  25:231 */     this.leapIndicator = ((byte)(array[0] >> 6 & 0x3));
/*  26:232 */     this.version = ((byte)(array[0] >> 3 & 0x7));
/*  27:233 */     this.mode = ((byte)(array[0] & 0x7));
/*  28:234 */     this.stratum = unsignedByteToShort(array[1]);
/*  29:235 */     this.pollInterval = array[2];
/*  30:236 */     this.precision = array[3];
/*  31:    */     
/*  32:238 */     this.rootDelay = 
/*  33:    */     
/*  34:    */ 
/*  35:241 */       (array[4] * 256.0D + unsignedByteToShort(array[5]) + unsignedByteToShort(array[6]) / 256.0D + unsignedByteToShort(array[7]) / 65536.0D);
/*  36:    */     
/*  37:243 */     this.rootDispersion = 
/*  38:    */     
/*  39:    */ 
/*  40:246 */       (unsignedByteToShort(array[8]) * 256.0D + unsignedByteToShort(array[9]) + unsignedByteToShort(array[10]) / 256.0D + unsignedByteToShort(array[11]) / 65536.0D);
/*  41:    */     
/*  42:248 */     this.referenceIdentifier[0] = array[12];
/*  43:249 */     this.referenceIdentifier[1] = array[13];
/*  44:250 */     this.referenceIdentifier[2] = array[14];
/*  45:251 */     this.referenceIdentifier[3] = array[15];
/*  46:    */     
/*  47:253 */     this.referenceTimestamp = decodeTimestamp(array, 16);
/*  48:254 */     this.originateTimestamp = decodeTimestamp(array, 24);
/*  49:255 */     this.receiveTimestamp = decodeTimestamp(array, 32);
/*  50:256 */     this.transmitTimestamp = decodeTimestamp(array, 40);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public NtpMessage()
/*  54:    */   {
/*  55:269 */     this.mode = 3;
/*  56:270 */     this.transmitTimestamp = (System.currentTimeMillis() / 1000.0D + 2208988800.0D);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public byte[] toByteArray()
/*  60:    */   {
/*  61:281 */     byte[] p = new byte[48];
/*  62:    */     
/*  63:283 */     p[0] = ((byte)(this.leapIndicator << 6 | this.version << 3 | this.mode));
/*  64:284 */     p[1] = ((byte)this.stratum);
/*  65:285 */     p[2] = this.pollInterval;
/*  66:286 */     p[3] = this.precision;
/*  67:    */     
/*  68:    */ 
/*  69:289 */     int l = (int)(this.rootDelay * 65536.0D);
/*  70:290 */     p[4] = ((byte)(l >> 24 & 0xFF));
/*  71:291 */     p[5] = ((byte)(l >> 16 & 0xFF));
/*  72:292 */     p[6] = ((byte)(l >> 8 & 0xFF));
/*  73:293 */     p[7] = ((byte)(l & 0xFF));
/*  74:    */     
/*  75:    */ 
/*  76:    */ 
/*  77:297 */     long ul = (this.rootDispersion * 65536.0D);
/*  78:298 */     p[8] = ((byte)(int)(ul >> 24 & 0xFF));
/*  79:299 */     p[9] = ((byte)(int)(ul >> 16 & 0xFF));
/*  80:300 */     p[10] = ((byte)(int)(ul >> 8 & 0xFF));
/*  81:301 */     p[11] = ((byte)(int)(ul & 0xFF));
/*  82:    */     
/*  83:303 */     p[12] = this.referenceIdentifier[0];
/*  84:304 */     p[13] = this.referenceIdentifier[1];
/*  85:305 */     p[14] = this.referenceIdentifier[2];
/*  86:306 */     p[15] = this.referenceIdentifier[3];
/*  87:    */     
/*  88:308 */     encodeTimestamp(p, 16, this.referenceTimestamp);
/*  89:309 */     encodeTimestamp(p, 24, this.originateTimestamp);
/*  90:310 */     encodeTimestamp(p, 32, this.receiveTimestamp);
/*  91:311 */     encodeTimestamp(p, 40, this.transmitTimestamp);
/*  92:    */     
/*  93:313 */     return p;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String toString()
/*  97:    */   {
/*  98:323 */     String precisionStr = 
/*  99:324 */       new DecimalFormat("0.#E0").format(Math.pow(2.0D, this.precision));
/* 100:    */     
/* 101:326 */     return "Leap indicator: " + this.leapIndicator + "\n" + 
/* 102:327 */       "Version: " + this.version + "\n" + 
/* 103:328 */       "Mode: " + this.mode + "\n" + 
/* 104:329 */       "Stratum: " + this.stratum + "\n" + 
/* 105:330 */       "Poll: " + this.pollInterval + "\n" + 
/* 106:331 */       "Precision: " + this.precision + " (" + precisionStr + " seconds)\n" + 
/* 107:332 */       "Root delay: " + new DecimalFormat("0.00").format(this.rootDelay * 1000.0D) + " ms\n" + 
/* 108:333 */       "Root dispersion: " + new DecimalFormat("0.00").format(this.rootDispersion * 1000.0D) + " ms\n" + 
/* 109:334 */       "Reference identifier: " + referenceIdentifierToString(this.referenceIdentifier, this.stratum, this.version) + "\n" + 
/* 110:335 */       "Reference timestamp: " + timestampToString(this.referenceTimestamp) + "\n" + 
/* 111:336 */       "Originate timestamp: " + timestampToString(this.originateTimestamp) + "\n" + 
/* 112:337 */       "Receive timestamp:   " + timestampToString(this.receiveTimestamp) + "\n" + 
/* 113:338 */       "Transmit timestamp:  " + timestampToString(this.transmitTimestamp);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public static short unsignedByteToShort(byte b)
/* 117:    */   {
/* 118:349 */     if ((b & 0x80) == 128) {
/* 119:349 */       return (short)(128 + (b & 0x7F));
/* 120:    */     }
/* 121:350 */     return (short)b;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public static double decodeTimestamp(byte[] array, int pointer)
/* 125:    */   {
/* 126:362 */     double r = 0.0D;
/* 127:364 */     for (int i = 0; i < 8; i++) {
/* 128:366 */       r += unsignedByteToShort(array[(pointer + i)]) * Math.pow(2.0D, (3 - i) * 8);
/* 129:    */     }
/* 130:369 */     return r;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public static void encodeTimestamp(byte[] array, int pointer, double timestamp)
/* 134:    */   {
/* 135:380 */     for (int i = 0; i < 8; i++)
/* 136:    */     {
/* 137:383 */       double base = Math.pow(2.0D, (3 - i) * 8);
/* 138:    */       
/* 139:    */ 
/* 140:386 */       array[(pointer + i)] = ((byte)(int)(timestamp / base));
/* 141:    */       
/* 142:    */ 
/* 143:389 */       timestamp -= unsignedByteToShort(array[(pointer + i)]) * base;
/* 144:    */     }
/* 145:396 */     array[7] = ((byte)(int)(Math.random() * 255.0D));
/* 146:    */   }
/* 147:    */   
/* 148:    */   public static String timestampToString(double timestamp)
/* 149:    */   {
/* 150:407 */     if (timestamp == 0.0D) {
/* 151:407 */       return "0";
/* 152:    */     }
/* 153:411 */     double utc = timestamp - 2208988800.0D;
/* 154:    */     
/* 155:    */ 
/* 156:414 */     long ms = (utc * 1000.0D);
/* 157:    */     
/* 158:    */ 
/* 159:417 */     String date = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date(ms));
/* 160:    */     
/* 161:    */ 
/* 162:420 */     double fraction = timestamp - timestamp;
/* 163:421 */     String fractionSting = new DecimalFormat(".000000").format(fraction);
/* 164:    */     
/* 165:423 */     return date + fractionSting;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public static String referenceIdentifierToString(byte[] ref, short stratum, byte version)
/* 169:    */   {
/* 170:438 */     if ((stratum == 0) || (stratum == 1)) {
/* 171:440 */       return new String(ref);
/* 172:    */     }
/* 173:445 */     if (version == 3) {
/* 174:447 */       return 
/* 175:    */       
/* 176:    */ 
/* 177:450 */         unsignedByteToShort(ref[0]) + "." + unsignedByteToShort(ref[1]) + "." + unsignedByteToShort(ref[2]) + "." + unsignedByteToShort(ref[3]);
/* 178:    */     }
/* 179:455 */     if (version == 4) {
/* 180:457 */       return 
/* 181:    */       
/* 182:    */ 
/* 183:460 */         unsignedByteToShort(ref[0]) / 256.0D + unsignedByteToShort(ref[1]) / 65536.0D + unsignedByteToShort(ref[2]) / 16777216.0D + unsignedByteToShort(ref[3]) / 4294967296.0D;
/* 184:    */     }
/* 185:463 */     return "";
/* 186:    */   }
/* 187:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.NtpMessage
 * JD-Core Version:    0.7.0.1
 */