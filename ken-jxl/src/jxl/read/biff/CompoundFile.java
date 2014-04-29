/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import jxl.WorkbookSettings;
/*   6:    */ import jxl.biff.BaseCompoundFile;
/*   7:    */ import jxl.biff.BaseCompoundFile.PropertyStorage;
/*   8:    */ import jxl.biff.IntegerHelper;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ 
/*  11:    */ public final class CompoundFile
/*  12:    */   extends BaseCompoundFile
/*  13:    */ {
/*  14: 40 */   private static Logger logger = Logger.getLogger(CompoundFile.class);
/*  15:    */   private byte[] data;
/*  16:    */   private int numBigBlockDepotBlocks;
/*  17:    */   private int sbdStartBlock;
/*  18:    */   private int rootStartBlock;
/*  19:    */   private int extensionBlock;
/*  20:    */   private int numExtensionBlocks;
/*  21:    */   private byte[] rootEntry;
/*  22:    */   private int[] bigBlockChain;
/*  23:    */   private int[] smallBlockChain;
/*  24:    */   private int[] bigBlockDepotBlocks;
/*  25:    */   private ArrayList propertySets;
/*  26:    */   private WorkbookSettings settings;
/*  27:    */   private BaseCompoundFile.PropertyStorage rootEntryPropertyStorage;
/*  28:    */   
/*  29:    */   public CompoundFile(byte[] d, WorkbookSettings ws)
/*  30:    */     throws BiffException
/*  31:    */   {
/*  32:108 */     this.data = d;
/*  33:109 */     this.settings = ws;
/*  34:112 */     for (int i = 0; i < IDENTIFIER.length; i++) {
/*  35:114 */       if (this.data[i] != IDENTIFIER[i]) {
/*  36:116 */         throw new BiffException(BiffException.unrecognizedOLEFile);
/*  37:    */       }
/*  38:    */     }
/*  39:120 */     this.propertySets = new ArrayList();
/*  40:121 */     this.numBigBlockDepotBlocks = IntegerHelper.getInt(
/*  41:122 */       this.data[44], 
/*  42:123 */       this.data[45], 
/*  43:124 */       this.data[46], 
/*  44:125 */       this.data[47]);
/*  45:126 */     this.sbdStartBlock = IntegerHelper.getInt(
/*  46:127 */       this.data[60], 
/*  47:128 */       this.data[61], 
/*  48:129 */       this.data[62], 
/*  49:130 */       this.data[63]);
/*  50:131 */     this.rootStartBlock = IntegerHelper.getInt(
/*  51:132 */       this.data[48], 
/*  52:133 */       this.data[49], 
/*  53:134 */       this.data[50], 
/*  54:135 */       this.data[51]);
/*  55:136 */     this.extensionBlock = IntegerHelper.getInt(
/*  56:137 */       this.data[68], 
/*  57:138 */       this.data[69], 
/*  58:139 */       this.data[70], 
/*  59:140 */       this.data[71]);
/*  60:141 */     this.numExtensionBlocks = IntegerHelper.getInt(
/*  61:142 */       this.data[72], 
/*  62:143 */       this.data[73], 
/*  63:144 */       this.data[74], 
/*  64:145 */       this.data[75]);
/*  65:    */     
/*  66:147 */     this.bigBlockDepotBlocks = new int[this.numBigBlockDepotBlocks];
/*  67:    */     
/*  68:149 */     int pos = 76;
/*  69:    */     
/*  70:151 */     int bbdBlocks = this.numBigBlockDepotBlocks;
/*  71:153 */     if (this.numExtensionBlocks != 0) {
/*  72:155 */       bbdBlocks = 109;
/*  73:    */     }
/*  74:158 */     for (int i = 0; i < bbdBlocks; i++)
/*  75:    */     {
/*  76:160 */       this.bigBlockDepotBlocks[i] = IntegerHelper.getInt(
/*  77:161 */         d[pos], d[(pos + 1)], d[(pos + 2)], d[(pos + 3)]);
/*  78:162 */       pos += 4;
/*  79:    */     }
/*  80:165 */     for (int j = 0; j < this.numExtensionBlocks; j++)
/*  81:    */     {
/*  82:167 */       pos = (this.extensionBlock + 1) * 512;
/*  83:168 */       int blocksToRead = Math.min(this.numBigBlockDepotBlocks - bbdBlocks, 
/*  84:169 */         127);
/*  85:171 */       for (int i = bbdBlocks; i < bbdBlocks + blocksToRead; i++)
/*  86:    */       {
/*  87:173 */         this.bigBlockDepotBlocks[i] = IntegerHelper.getInt(
/*  88:174 */           d[pos], d[(pos + 1)], d[(pos + 2)], d[(pos + 3)]);
/*  89:175 */         pos += 4;
/*  90:    */       }
/*  91:178 */       bbdBlocks += blocksToRead;
/*  92:179 */       if (bbdBlocks < this.numBigBlockDepotBlocks) {
/*  93:181 */         this.extensionBlock = IntegerHelper.getInt(
/*  94:182 */           d[pos], d[(pos + 1)], d[(pos + 2)], d[(pos + 3)]);
/*  95:    */       }
/*  96:    */     }
/*  97:186 */     readBigBlockDepot();
/*  98:187 */     readSmallBlockDepot();
/*  99:    */     
/* 100:189 */     this.rootEntry = readData(this.rootStartBlock);
/* 101:190 */     readPropertySets();
/* 102:    */   }
/* 103:    */   
/* 104:    */   private void readBigBlockDepot()
/* 105:    */   {
/* 106:198 */     int pos = 0;
/* 107:199 */     int index = 0;
/* 108:200 */     this.bigBlockChain = new int[this.numBigBlockDepotBlocks * 512 / 4];
/* 109:202 */     for (int i = 0; i < this.numBigBlockDepotBlocks; i++)
/* 110:    */     {
/* 111:204 */       pos = (this.bigBlockDepotBlocks[i] + 1) * 512;
/* 112:206 */       for (int j = 0; j < 128; j++)
/* 113:    */       {
/* 114:208 */         this.bigBlockChain[index] = IntegerHelper.getInt(
/* 115:209 */           this.data[pos], this.data[(pos + 1)], this.data[(pos + 2)], this.data[(pos + 3)]);
/* 116:210 */         pos += 4;
/* 117:211 */         index++;
/* 118:    */       }
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   private void readSmallBlockDepot()
/* 123:    */     throws BiffException
/* 124:    */   {
/* 125:221 */     int pos = 0;
/* 126:222 */     int index = 0;
/* 127:223 */     int sbdBlock = this.sbdStartBlock;
/* 128:224 */     this.smallBlockChain = new int[0];
/* 129:228 */     if (sbdBlock == -1)
/* 130:    */     {
/* 131:230 */       logger.warn("invalid small block depot number");
/* 132:231 */       return;
/* 133:    */     }
/* 134:234 */     for (int blockCount = 0; (blockCount <= this.bigBlockChain.length) && (sbdBlock != -2); blockCount++)
/* 135:    */     {
/* 136:238 */       int[] oldChain = this.smallBlockChain;
/* 137:239 */       this.smallBlockChain = new int[this.smallBlockChain.length + 128];
/* 138:240 */       System.arraycopy(oldChain, 0, this.smallBlockChain, 0, oldChain.length);
/* 139:    */       
/* 140:242 */       pos = (sbdBlock + 1) * 512;
/* 141:244 */       for (int j = 0; j < 128; j++)
/* 142:    */       {
/* 143:246 */         this.smallBlockChain[index] = IntegerHelper.getInt(
/* 144:247 */           this.data[pos], this.data[(pos + 1)], this.data[(pos + 2)], this.data[(pos + 3)]);
/* 145:248 */         pos += 4;
/* 146:249 */         index++;
/* 147:    */       }
/* 148:252 */       sbdBlock = this.bigBlockChain[sbdBlock];
/* 149:    */     }
/* 150:255 */     if (blockCount > this.bigBlockChain.length) {
/* 151:259 */       throw new BiffException(BiffException.corruptFileFormat);
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   private void readPropertySets()
/* 156:    */   {
/* 157:268 */     int offset = 0;
/* 158:269 */     byte[] d = (byte[])null;
/* 159:271 */     while (offset < this.rootEntry.length)
/* 160:    */     {
/* 161:273 */       d = new byte[''];
/* 162:274 */       System.arraycopy(this.rootEntry, offset, d, 0, d.length);
/* 163:275 */       BaseCompoundFile.PropertyStorage ps = new BaseCompoundFile.PropertyStorage(this, d);
/* 164:279 */       if ((ps.name == null) || (ps.name.length() == 0)) {
/* 165:281 */         if (ps.type == 5)
/* 166:    */         {
/* 167:283 */           ps.name = "Root Entry";
/* 168:284 */           logger.warn("Property storage name for " + ps.type + 
/* 169:285 */             " is empty - setting to " + "Root Entry");
/* 170:    */         }
/* 171:289 */         else if (ps.size != 0)
/* 172:    */         {
/* 173:291 */           logger.warn("Property storage type " + ps.type + 
/* 174:292 */             " is non-empty and has no associated name");
/* 175:    */         }
/* 176:    */       }
/* 177:296 */       this.propertySets.add(ps);
/* 178:297 */       if (ps.name.equalsIgnoreCase("Root Entry")) {
/* 179:299 */         this.rootEntryPropertyStorage = ps;
/* 180:    */       }
/* 181:301 */       offset += 128;
/* 182:    */     }
/* 183:304 */     if (this.rootEntryPropertyStorage == null) {
/* 184:306 */       this.rootEntryPropertyStorage = ((BaseCompoundFile.PropertyStorage)this.propertySets.get(0));
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */   public byte[] getStream(String streamName)
/* 189:    */     throws BiffException
/* 190:    */   {
/* 191:319 */     BaseCompoundFile.PropertyStorage ps = findPropertyStorage(streamName, 
/* 192:320 */       this.rootEntryPropertyStorage);
/* 193:324 */     if (ps == null) {
/* 194:326 */       ps = getPropertyStorage(streamName);
/* 195:    */     }
/* 196:329 */     if ((ps.size >= 4096) || 
/* 197:330 */       (streamName.equalsIgnoreCase("Root Entry"))) {
/* 198:332 */       return getBigBlockStream(ps);
/* 199:    */     }
/* 200:336 */     return getSmallBlockStream(ps);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public byte[] getStream(int psIndex)
/* 204:    */     throws BiffException
/* 205:    */   {
/* 206:350 */     BaseCompoundFile.PropertyStorage ps = getPropertyStorage(psIndex);
/* 207:352 */     if ((ps.size >= 4096) || 
/* 208:353 */       (ps.name.equalsIgnoreCase("Root Entry"))) {
/* 209:355 */       return getBigBlockStream(ps);
/* 210:    */     }
/* 211:359 */     return getSmallBlockStream(ps);
/* 212:    */   }
/* 213:    */   
/* 214:    */   public BaseCompoundFile.PropertyStorage findPropertyStorage(String name)
/* 215:    */   {
/* 216:371 */     return findPropertyStorage(name, this.rootEntryPropertyStorage);
/* 217:    */   }
/* 218:    */   
/* 219:    */   private BaseCompoundFile.PropertyStorage findPropertyStorage(String name, BaseCompoundFile.PropertyStorage base)
/* 220:    */   {
/* 221:381 */     if (base.child == -1) {
/* 222:383 */       return null;
/* 223:    */     }
/* 224:387 */     BaseCompoundFile.PropertyStorage child = getPropertyStorage(base.child);
/* 225:388 */     if (child.name.equalsIgnoreCase(name)) {
/* 226:390 */       return child;
/* 227:    */     }
/* 228:394 */     BaseCompoundFile.PropertyStorage prev = child;
/* 229:395 */     while (prev.previous != -1)
/* 230:    */     {
/* 231:397 */       prev = getPropertyStorage(prev.previous);
/* 232:398 */       if (prev.name.equalsIgnoreCase(name)) {
/* 233:400 */         return prev;
/* 234:    */       }
/* 235:    */     }
/* 236:405 */     BaseCompoundFile.PropertyStorage next = child;
/* 237:406 */     while (next.next != -1)
/* 238:    */     {
/* 239:408 */       next = getPropertyStorage(next.next);
/* 240:409 */       if (next.name.equalsIgnoreCase(name)) {
/* 241:411 */         return next;
/* 242:    */       }
/* 243:    */     }
/* 244:415 */     return findPropertyStorage(name, child);
/* 245:    */   }
/* 246:    */   
/* 247:    */   /**
/* 248:    */    * @deprecated
/* 249:    */    */
/* 250:    */   private BaseCompoundFile.PropertyStorage getPropertyStorage(String name)
/* 251:    */     throws BiffException
/* 252:    */   {
/* 253:429 */     Iterator i = this.propertySets.iterator();
/* 254:430 */     boolean found = false;
/* 255:431 */     boolean multiple = false;
/* 256:432 */     BaseCompoundFile.PropertyStorage ps = null;
/* 257:433 */     while (i.hasNext())
/* 258:    */     {
/* 259:435 */       BaseCompoundFile.PropertyStorage ps2 = (BaseCompoundFile.PropertyStorage)i.next();
/* 260:436 */       if (ps2.name.equalsIgnoreCase(name))
/* 261:    */       {
/* 262:438 */         multiple = found;
/* 263:439 */         found = true;
/* 264:440 */         ps = ps2;
/* 265:    */       }
/* 266:    */     }
/* 267:444 */     if (multiple) {
/* 268:446 */       logger.warn("found multiple copies of property set " + name);
/* 269:    */     }
/* 270:449 */     if (!found) {
/* 271:451 */       throw new BiffException(BiffException.streamNotFound);
/* 272:    */     }
/* 273:454 */     return ps;
/* 274:    */   }
/* 275:    */   
/* 276:    */   private BaseCompoundFile.PropertyStorage getPropertyStorage(int index)
/* 277:    */   {
/* 278:464 */     return (BaseCompoundFile.PropertyStorage)this.propertySets.get(index);
/* 279:    */   }
/* 280:    */   
/* 281:    */   private byte[] getBigBlockStream(BaseCompoundFile.PropertyStorage ps)
/* 282:    */   {
/* 283:475 */     int numBlocks = ps.size / 512;
/* 284:476 */     if (ps.size % 512 != 0) {
/* 285:478 */       numBlocks++;
/* 286:    */     }
/* 287:481 */     byte[] streamData = new byte[numBlocks * 512];
/* 288:    */     
/* 289:483 */     int block = ps.startBlock;
/* 290:    */     
/* 291:485 */     int count = 0;
/* 292:486 */     int pos = 0;
/* 293:487 */     while ((block != -2) && (count < numBlocks))
/* 294:    */     {
/* 295:489 */       pos = (block + 1) * 512;
/* 296:490 */       System.arraycopy(this.data, pos, streamData, 
/* 297:491 */         count * 512, 512);
/* 298:492 */       count++;
/* 299:493 */       block = this.bigBlockChain[block];
/* 300:    */     }
/* 301:496 */     if ((block != -2) && (count == numBlocks)) {
/* 302:498 */       logger.warn("Property storage size inconsistent with block chain.");
/* 303:    */     }
/* 304:501 */     return streamData;
/* 305:    */   }
/* 306:    */   
/* 307:    */   private byte[] getSmallBlockStream(BaseCompoundFile.PropertyStorage ps)
/* 308:    */     throws BiffException
/* 309:    */   {
/* 310:513 */     byte[] rootdata = readData(this.rootEntryPropertyStorage.startBlock);
/* 311:514 */     byte[] sbdata = new byte[0];
/* 312:    */     
/* 313:516 */     int block = ps.startBlock;
/* 314:517 */     int pos = 0;
/* 315:519 */     for (int blockCount = 0; (blockCount <= this.smallBlockChain.length) && (block != -2); blockCount++)
/* 316:    */     {
/* 317:523 */       byte[] olddata = sbdata;
/* 318:524 */       sbdata = new byte[olddata.length + 64];
/* 319:525 */       System.arraycopy(olddata, 0, sbdata, 0, olddata.length);
/* 320:    */       
/* 321:    */ 
/* 322:528 */       pos = block * 64;
/* 323:529 */       System.arraycopy(rootdata, pos, sbdata, 
/* 324:530 */         olddata.length, 64);
/* 325:531 */       block = this.smallBlockChain[block];
/* 326:533 */       if (block == -1)
/* 327:    */       {
/* 328:535 */         logger.warn("Incorrect terminator for small block stream " + ps.name);
/* 329:536 */         block = -2;
/* 330:    */       }
/* 331:    */     }
/* 332:540 */     if (blockCount > this.smallBlockChain.length) {
/* 333:544 */       throw new BiffException(BiffException.corruptFileFormat);
/* 334:    */     }
/* 335:547 */     return sbdata;
/* 336:    */   }
/* 337:    */   
/* 338:    */   private byte[] readData(int bl)
/* 339:    */     throws BiffException
/* 340:    */   {
/* 341:559 */     int block = bl;
/* 342:560 */     int pos = 0;
/* 343:561 */     byte[] entry = new byte[0];
/* 344:563 */     for (int blockCount = 0; (blockCount <= this.bigBlockChain.length) && (block != -2); blockCount++)
/* 345:    */     {
/* 346:567 */       byte[] oldEntry = entry;
/* 347:568 */       entry = new byte[oldEntry.length + 512];
/* 348:569 */       System.arraycopy(oldEntry, 0, entry, 0, oldEntry.length);
/* 349:570 */       pos = (block + 1) * 512;
/* 350:571 */       System.arraycopy(this.data, pos, entry, 
/* 351:572 */         oldEntry.length, 512);
/* 352:573 */       if (this.bigBlockChain[block] == block) {
/* 353:575 */         throw new BiffException(BiffException.corruptFileFormat);
/* 354:    */       }
/* 355:577 */       block = this.bigBlockChain[block];
/* 356:    */     }
/* 357:580 */     if (blockCount > this.bigBlockChain.length) {
/* 358:584 */       throw new BiffException(BiffException.corruptFileFormat);
/* 359:    */     }
/* 360:587 */     return entry;
/* 361:    */   }
/* 362:    */   
/* 363:    */   public int getNumberOfPropertySets()
/* 364:    */   {
/* 365:596 */     return this.propertySets.size();
/* 366:    */   }
/* 367:    */   
/* 368:    */   public BaseCompoundFile.PropertyStorage getPropertySet(int index)
/* 369:    */   {
/* 370:608 */     return getPropertyStorage(index);
/* 371:    */   }
/* 372:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.CompoundFile
 * JD-Core Version:    0.7.0.1
 */