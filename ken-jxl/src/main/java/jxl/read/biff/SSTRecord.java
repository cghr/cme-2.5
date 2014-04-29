/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.RecordData;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ import jxl.common.Assert;
/*   8:    */ 
/*   9:    */ class SSTRecord
/*  10:    */   extends RecordData
/*  11:    */ {
/*  12:    */   private int totalStrings;
/*  13:    */   private int uniqueStrings;
/*  14:    */   private String[] strings;
/*  15:    */   private int[] continuationBreaks;
/*  16:    */   
/*  17:    */   public SSTRecord(Record t, Record[] continuations, WorkbookSettings ws)
/*  18:    */   {
/*  19: 82 */     super(t);
/*  20:    */     
/*  21:    */ 
/*  22:    */ 
/*  23:    */ 
/*  24:    */ 
/*  25: 88 */     int totalRecordLength = 0;
/*  26: 90 */     for (int i = 0; i < continuations.length; i++) {
/*  27: 92 */       totalRecordLength += continuations[i].getLength();
/*  28:    */     }
/*  29: 94 */     totalRecordLength += getRecord().getLength();
/*  30:    */     
/*  31: 96 */     byte[] data = new byte[totalRecordLength];
/*  32:    */     
/*  33:    */ 
/*  34: 99 */     int pos = 0;
/*  35:100 */     System.arraycopy(getRecord().getData(), 0, 
/*  36:101 */       data, 0, getRecord().getLength());
/*  37:102 */     pos += getRecord().getLength();
/*  38:    */     
/*  39:    */ 
/*  40:105 */     this.continuationBreaks = new int[continuations.length];
/*  41:106 */     Record r = null;
/*  42:107 */     for (int i = 0; i < continuations.length; i++)
/*  43:    */     {
/*  44:109 */       r = continuations[i];
/*  45:110 */       System.arraycopy(r.getData(), 0, 
/*  46:111 */         data, pos, 
/*  47:112 */         r.getLength());
/*  48:113 */       this.continuationBreaks[i] = pos;
/*  49:114 */       pos += r.getLength();
/*  50:    */     }
/*  51:117 */     this.totalStrings = IntegerHelper.getInt(data[0], data[1], 
/*  52:118 */       data[2], data[3]);
/*  53:119 */     this.uniqueStrings = IntegerHelper.getInt(data[4], data[5], 
/*  54:120 */       data[6], data[7]);
/*  55:    */     
/*  56:122 */     this.strings = new String[this.uniqueStrings];
/*  57:123 */     readStrings(data, 8, ws);
/*  58:    */   }
/*  59:    */   
/*  60:    */   private void readStrings(byte[] data, int offset, WorkbookSettings ws)
/*  61:    */   {
/*  62:135 */     int pos = offset;
/*  63:    */     
/*  64:    */ 
/*  65:138 */     String s = null;
/*  66:139 */     boolean asciiEncoding = false;
/*  67:140 */     boolean richString = false;
/*  68:141 */     boolean extendedString = false;
/*  69:142 */     int formattingRuns = 0;
/*  70:143 */     int extendedRunLength = 0;
/*  71:145 */     for (int i = 0; i < this.uniqueStrings; i++)
/*  72:    */     {
/*  73:148 */       int numChars = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  74:149 */       pos += 2;
/*  75:150 */       byte optionFlags = data[pos];
/*  76:151 */       pos++;
/*  77:    */       
/*  78:    */ 
/*  79:154 */       extendedString = (optionFlags & 0x4) != 0;
/*  80:    */       
/*  81:    */ 
/*  82:157 */       richString = (optionFlags & 0x8) != 0;
/*  83:159 */       if (richString)
/*  84:    */       {
/*  85:162 */         formattingRuns = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  86:163 */         pos += 2;
/*  87:    */       }
/*  88:166 */       if (extendedString)
/*  89:    */       {
/*  90:169 */         extendedRunLength = IntegerHelper.getInt(
/*  91:170 */           data[pos], data[(pos + 1)], data[(pos + 2)], data[(pos + 3)]);
/*  92:171 */         pos += 4;
/*  93:    */       }
/*  94:175 */       asciiEncoding = (optionFlags & 0x1) == 0;
/*  95:    */       
/*  96:177 */       ByteArrayHolder bah = new ByteArrayHolder(null);
/*  97:178 */       BooleanHolder bh = new BooleanHolder(null);
/*  98:179 */       bh.value = asciiEncoding;
/*  99:180 */       pos += getChars(data, bah, pos, bh, numChars);
/* 100:181 */       asciiEncoding = bh.value;
/* 101:183 */       if (asciiEncoding) {
/* 102:185 */         s = StringHelper.getString(bah.bytes, numChars, 0, ws);
/* 103:    */       } else {
/* 104:189 */         s = StringHelper.getUnicodeString(bah.bytes, numChars, 0);
/* 105:    */       }
/* 106:192 */       this.strings[i] = s;
/* 107:195 */       if (richString) {
/* 108:197 */         pos += 4 * formattingRuns;
/* 109:    */       }
/* 110:201 */       if (extendedString) {
/* 111:203 */         pos += extendedRunLength;
/* 112:    */       }
/* 113:206 */       if (pos > data.length) {
/* 114:208 */         Assert.verify(false, "pos exceeds record length");
/* 115:    */       }
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   private int getChars(byte[] source, ByteArrayHolder bah, int pos, BooleanHolder ascii, int numChars)
/* 120:    */   {
/* 121:230 */     int i = 0;
/* 122:231 */     boolean spansBreak = false;
/* 123:233 */     if (ascii.value) {
/* 124:235 */       bah.bytes = new byte[numChars];
/* 125:    */     } else {
/* 126:239 */       bah.bytes = new byte[numChars * 2];
/* 127:    */     }
/* 128:242 */     while ((i < this.continuationBreaks.length) && (!spansBreak))
/* 129:    */     {
/* 130:244 */       spansBreak = (pos <= this.continuationBreaks[i]) && 
/* 131:245 */         (pos + bah.bytes.length > this.continuationBreaks[i]);
/* 132:247 */       if (!spansBreak) {
/* 133:249 */         i++;
/* 134:    */       }
/* 135:    */     }
/* 136:255 */     if (!spansBreak)
/* 137:    */     {
/* 138:257 */       System.arraycopy(source, pos, bah.bytes, 0, bah.bytes.length);
/* 139:258 */       return bah.bytes.length;
/* 140:    */     }
/* 141:262 */     int breakpos = this.continuationBreaks[i];
/* 142:263 */     System.arraycopy(source, pos, bah.bytes, 0, breakpos - pos);
/* 143:    */     
/* 144:265 */     int bytesRead = breakpos - pos;
/* 145:    */     int charsRead;
/* 146:    */     int charsRead;
/* 147:267 */     if (ascii.value) {
/* 148:269 */       charsRead = bytesRead;
/* 149:    */     } else {
/* 150:273 */       charsRead = bytesRead / 2;
/* 151:    */     }
/* 152:281 */     bytesRead = bytesRead + getContinuedString(source, bah, bytesRead, i, ascii, numChars - charsRead);
/* 153:282 */     return bytesRead;
/* 154:    */   }
/* 155:    */   
/* 156:    */   private int getContinuedString(byte[] source, ByteArrayHolder bah, int destPos, int contBreakIndex, BooleanHolder ascii, int charsLeft)
/* 157:    */   {
/* 158:303 */     int breakpos = this.continuationBreaks[contBreakIndex];
/* 159:304 */     int bytesRead = 0;
/* 160:306 */     while (charsLeft > 0)
/* 161:    */     {
/* 162:308 */       Assert.verify(contBreakIndex < this.continuationBreaks.length, 
/* 163:309 */         "continuation break index");
/* 164:311 */       if ((ascii.value) && (source[breakpos] == 0))
/* 165:    */       {
/* 166:315 */         int length = contBreakIndex == this.continuationBreaks.length - 1 ? 
/* 167:316 */           charsLeft : 
/* 168:317 */           Math.min(
/* 169:318 */           charsLeft, 
/* 170:319 */           this.continuationBreaks[(contBreakIndex + 1)] - breakpos - 1);
/* 171:    */         
/* 172:321 */         System.arraycopy(source, 
/* 173:322 */           breakpos + 1, 
/* 174:323 */           bah.bytes, 
/* 175:324 */           destPos, 
/* 176:325 */           length);
/* 177:326 */         destPos += length;
/* 178:327 */         bytesRead += length + 1;
/* 179:328 */         charsLeft -= length;
/* 180:329 */         ascii.value = true;
/* 181:    */       }
/* 182:331 */       else if ((!ascii.value) && (source[breakpos] != 0))
/* 183:    */       {
/* 184:335 */         int length = contBreakIndex == this.continuationBreaks.length - 1 ? 
/* 185:336 */           charsLeft * 2 : 
/* 186:337 */           Math.min(
/* 187:338 */           charsLeft * 2, 
/* 188:339 */           this.continuationBreaks[(contBreakIndex + 1)] - breakpos - 1);
/* 189:    */         
/* 190:    */ 
/* 191:342 */         System.arraycopy(source, 
/* 192:343 */           breakpos + 1, 
/* 193:344 */           bah.bytes, 
/* 194:345 */           destPos, 
/* 195:346 */           length);
/* 196:    */         
/* 197:348 */         destPos += length;
/* 198:349 */         bytesRead += length + 1;
/* 199:350 */         charsLeft -= length / 2;
/* 200:351 */         ascii.value = false;
/* 201:    */       }
/* 202:353 */       else if ((!ascii.value) && (source[breakpos] == 0))
/* 203:    */       {
/* 204:357 */         int chars = contBreakIndex == this.continuationBreaks.length - 1 ? 
/* 205:358 */           charsLeft : 
/* 206:359 */           Math.min(
/* 207:360 */           charsLeft, 
/* 208:361 */           this.continuationBreaks[(contBreakIndex + 1)] - breakpos - 1);
/* 209:363 */         for (int j = 0; j < chars; j++)
/* 210:    */         {
/* 211:365 */           bah.bytes[destPos] = source[(breakpos + j + 1)];
/* 212:366 */           destPos += 2;
/* 213:    */         }
/* 214:369 */         bytesRead += chars + 1;
/* 215:370 */         charsLeft -= chars;
/* 216:371 */         ascii.value = false;
/* 217:    */       }
/* 218:    */       else
/* 219:    */       {
/* 220:380 */         byte[] oldBytes = bah.bytes;
/* 221:381 */         bah.bytes = new byte[destPos * 2 + charsLeft * 2];
/* 222:382 */         for (int j = 0; j < destPos; j++) {
/* 223:384 */           bah.bytes[(j * 2)] = oldBytes[j];
/* 224:    */         }
/* 225:387 */         destPos *= 2;
/* 226:    */         
/* 227:389 */         int length = contBreakIndex == this.continuationBreaks.length - 1 ? 
/* 228:390 */           charsLeft * 2 : 
/* 229:391 */           Math.min(
/* 230:392 */           charsLeft * 2, 
/* 231:393 */           this.continuationBreaks[(contBreakIndex + 1)] - breakpos - 1);
/* 232:    */         
/* 233:395 */         System.arraycopy(source, 
/* 234:396 */           breakpos + 1, 
/* 235:397 */           bah.bytes, 
/* 236:398 */           destPos, 
/* 237:399 */           length);
/* 238:    */         
/* 239:401 */         destPos += length;
/* 240:402 */         bytesRead += length + 1;
/* 241:403 */         charsLeft -= length / 2;
/* 242:404 */         ascii.value = false;
/* 243:    */       }
/* 244:407 */       contBreakIndex++;
/* 245:409 */       if (contBreakIndex < this.continuationBreaks.length) {
/* 246:411 */         breakpos = this.continuationBreaks[contBreakIndex];
/* 247:    */       }
/* 248:    */     }
/* 249:415 */     return bytesRead;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public String getString(int index)
/* 253:    */   {
/* 254:426 */     Assert.verify(index < this.uniqueStrings);
/* 255:427 */     return this.strings[index];
/* 256:    */   }
/* 257:    */   
/* 258:    */   private static class BooleanHolder
/* 259:    */   {
/* 260:    */     public boolean value;
/* 261:    */   }
/* 262:    */   
/* 263:    */   private static class ByteArrayHolder
/* 264:    */   {
/* 265:    */     public byte[] bytes;
/* 266:    */   }
/* 267:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SSTRecord
 * JD-Core Version:    0.7.0.1
 */