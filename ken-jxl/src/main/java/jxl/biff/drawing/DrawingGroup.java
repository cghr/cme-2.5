/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import jxl.common.Assert;
/*   8:    */ import jxl.common.Logger;
/*   9:    */ import jxl.read.biff.Record;
/*  10:    */ import jxl.write.biff.File;
/*  11:    */ 
/*  12:    */ public class DrawingGroup
/*  13:    */   implements EscherStream
/*  14:    */ {
/*  15: 42 */   private static Logger logger = Logger.getLogger(DrawingGroup.class);
/*  16:    */   private byte[] drawingData;
/*  17:    */   private EscherContainer escherData;
/*  18:    */   private BStoreContainer bstoreContainer;
/*  19:    */   private boolean initialized;
/*  20:    */   private ArrayList drawings;
/*  21:    */   private int numBlips;
/*  22:    */   private int numCharts;
/*  23:    */   private int drawingGroupId;
/*  24:    */   private boolean drawingsOmitted;
/*  25:    */   private Origin origin;
/*  26:    */   private HashMap imageFiles;
/*  27:    */   private int maxObjectId;
/*  28:    */   private int maxShapeId;
/*  29:    */   
/*  30:    */   public DrawingGroup(Origin o)
/*  31:    */   {
/*  32:118 */     this.origin = o;
/*  33:119 */     this.initialized = (o == Origin.WRITE);
/*  34:120 */     this.drawings = new ArrayList();
/*  35:121 */     this.imageFiles = new HashMap();
/*  36:122 */     this.drawingsOmitted = false;
/*  37:123 */     this.maxObjectId = 1;
/*  38:124 */     this.maxShapeId = 1024;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public DrawingGroup(DrawingGroup dg)
/*  42:    */   {
/*  43:137 */     this.drawingData = dg.drawingData;
/*  44:138 */     this.escherData = dg.escherData;
/*  45:139 */     this.bstoreContainer = dg.bstoreContainer;
/*  46:140 */     this.initialized = dg.initialized;
/*  47:141 */     this.drawingData = dg.drawingData;
/*  48:142 */     this.escherData = dg.escherData;
/*  49:143 */     this.bstoreContainer = dg.bstoreContainer;
/*  50:144 */     this.numBlips = dg.numBlips;
/*  51:145 */     this.numCharts = dg.numCharts;
/*  52:146 */     this.drawingGroupId = dg.drawingGroupId;
/*  53:147 */     this.drawingsOmitted = dg.drawingsOmitted;
/*  54:148 */     this.origin = dg.origin;
/*  55:149 */     this.imageFiles = ((HashMap)dg.imageFiles.clone());
/*  56:150 */     this.maxObjectId = dg.maxObjectId;
/*  57:151 */     this.maxShapeId = dg.maxShapeId;
/*  58:    */     
/*  59:    */ 
/*  60:    */ 
/*  61:155 */     this.drawings = new ArrayList();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void add(MsoDrawingGroupRecord mso)
/*  65:    */   {
/*  66:169 */     addData(mso.getData());
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void add(Record cont)
/*  70:    */   {
/*  71:180 */     addData(cont.getData());
/*  72:    */   }
/*  73:    */   
/*  74:    */   private void addData(byte[] msodata)
/*  75:    */   {
/*  76:190 */     if (this.drawingData == null)
/*  77:    */     {
/*  78:192 */       this.drawingData = new byte[msodata.length];
/*  79:193 */       System.arraycopy(msodata, 0, this.drawingData, 0, msodata.length);
/*  80:194 */       return;
/*  81:    */     }
/*  82:198 */     byte[] newdata = new byte[this.drawingData.length + msodata.length];
/*  83:199 */     System.arraycopy(this.drawingData, 0, newdata, 0, this.drawingData.length);
/*  84:200 */     System.arraycopy(msodata, 0, newdata, this.drawingData.length, msodata.length);
/*  85:201 */     this.drawingData = newdata;
/*  86:    */   }
/*  87:    */   
/*  88:    */   final void addDrawing(DrawingGroupObject d)
/*  89:    */   {
/*  90:211 */     this.drawings.add(d);
/*  91:212 */     this.maxObjectId = Math.max(this.maxObjectId, d.getObjectId());
/*  92:213 */     this.maxShapeId = Math.max(this.maxShapeId, d.getShapeId());
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void add(Chart c)
/*  96:    */   {
/*  97:223 */     this.numCharts += 1;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void add(DrawingGroupObject d)
/* 101:    */   {
/* 102:233 */     if (this.origin == Origin.READ)
/* 103:    */     {
/* 104:235 */       this.origin = Origin.READ_WRITE;
/* 105:236 */       BStoreContainer bsc = getBStoreContainer();
/* 106:237 */       Dgg dgg = (Dgg)this.escherData.getChildren()[0];
/* 107:238 */       this.drawingGroupId = (dgg.getCluster(1).drawingGroupId - this.numBlips - 1);
/* 108:239 */       this.numBlips = (bsc != null ? bsc.getNumBlips() : 0);
/* 109:241 */       if (bsc != null) {
/* 110:243 */         Assert.verify(this.numBlips == bsc.getNumBlips());
/* 111:    */       }
/* 112:    */     }
/* 113:247 */     if (!(d instanceof Drawing))
/* 114:    */     {
/* 115:251 */       this.maxObjectId += 1;
/* 116:252 */       this.maxShapeId += 1;
/* 117:253 */       d.setDrawingGroup(this);
/* 118:254 */       d.setObjectId(this.maxObjectId, this.numBlips + 1, this.maxShapeId);
/* 119:255 */       if (this.drawings.size() > this.maxObjectId) {
/* 120:257 */         logger.warn("drawings length " + this.drawings.size() + 
/* 121:258 */           " exceeds the max object id " + this.maxObjectId);
/* 122:    */       }
/* 123:261 */       return;
/* 124:    */     }
/* 125:264 */     Drawing drawing = (Drawing)d;
/* 126:    */     
/* 127:    */ 
/* 128:267 */     Drawing refImage = 
/* 129:268 */       (Drawing)this.imageFiles.get(d.getImageFilePath());
/* 130:270 */     if (refImage == null)
/* 131:    */     {
/* 132:274 */       this.maxObjectId += 1;
/* 133:275 */       this.maxShapeId += 1;
/* 134:276 */       this.drawings.add(drawing);
/* 135:277 */       drawing.setDrawingGroup(this);
/* 136:278 */       drawing.setObjectId(this.maxObjectId, this.numBlips + 1, this.maxShapeId);
/* 137:279 */       this.numBlips += 1;
/* 138:280 */       this.imageFiles.put(drawing.getImageFilePath(), drawing);
/* 139:    */     }
/* 140:    */     else
/* 141:    */     {
/* 142:287 */       refImage.setReferenceCount(refImage.getReferenceCount() + 1);
/* 143:288 */       drawing.setDrawingGroup(this);
/* 144:289 */       drawing.setObjectId(refImage.getObjectId(), 
/* 145:290 */         refImage.getBlipId(), 
/* 146:291 */         refImage.getShapeId());
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void remove(DrawingGroupObject d)
/* 151:    */   {
/* 152:304 */     if (getBStoreContainer() == null) {
/* 153:306 */       return;
/* 154:    */     }
/* 155:309 */     if (this.origin == Origin.READ)
/* 156:    */     {
/* 157:311 */       this.origin = Origin.READ_WRITE;
/* 158:312 */       this.numBlips = getBStoreContainer().getNumBlips();
/* 159:313 */       Dgg dgg = (Dgg)this.escherData.getChildren()[0];
/* 160:314 */       this.drawingGroupId = (dgg.getCluster(1).drawingGroupId - this.numBlips - 1);
/* 161:    */     }
/* 162:318 */     EscherRecord[] children = getBStoreContainer().getChildren();
/* 163:319 */     BlipStoreEntry bse = (BlipStoreEntry)children[(d.getBlipId() - 1)];
/* 164:    */     
/* 165:321 */     bse.dereference();
/* 166:323 */     if (bse.getReferenceCount() == 0)
/* 167:    */     {
/* 168:326 */       getBStoreContainer().remove(bse);
/* 169:329 */       for (Iterator i = this.drawings.iterator(); i.hasNext();)
/* 170:    */       {
/* 171:331 */         DrawingGroupObject drawing = (DrawingGroupObject)i.next();
/* 172:333 */         if (drawing.getBlipId() > d.getBlipId()) {
/* 173:335 */           drawing.setObjectId(drawing.getObjectId(), 
/* 174:336 */             drawing.getBlipId() - 1, 
/* 175:337 */             drawing.getShapeId());
/* 176:    */         }
/* 177:    */       }
/* 178:341 */       this.numBlips -= 1;
/* 179:    */     }
/* 180:    */   }
/* 181:    */   
/* 182:    */   private void initialize()
/* 183:    */   {
/* 184:351 */     EscherRecordData er = new EscherRecordData(this, 0);
/* 185:    */     
/* 186:353 */     Assert.verify(er.isContainer());
/* 187:    */     
/* 188:355 */     this.escherData = new EscherContainer(er);
/* 189:    */     
/* 190:357 */     Assert.verify(this.escherData.getLength() == this.drawingData.length);
/* 191:358 */     Assert.verify(this.escherData.getType() == EscherRecordType.DGG_CONTAINER);
/* 192:    */     
/* 193:360 */     this.initialized = true;
/* 194:    */   }
/* 195:    */   
/* 196:    */   private BStoreContainer getBStoreContainer()
/* 197:    */   {
/* 198:370 */     if (this.bstoreContainer == null)
/* 199:    */     {
/* 200:372 */       if (!this.initialized) {
/* 201:374 */         initialize();
/* 202:    */       }
/* 203:377 */       EscherRecord[] children = this.escherData.getChildren();
/* 204:378 */       if ((children.length > 1) && 
/* 205:379 */         (children[1].getType() == EscherRecordType.BSTORE_CONTAINER)) {
/* 206:381 */         this.bstoreContainer = ((BStoreContainer)children[1]);
/* 207:    */       }
/* 208:    */     }
/* 209:385 */     return this.bstoreContainer;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public byte[] getData()
/* 213:    */   {
/* 214:395 */     return this.drawingData;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public void write(File outputFile)
/* 218:    */     throws IOException
/* 219:    */   {
/* 220:406 */     if (this.origin == Origin.WRITE)
/* 221:    */     {
/* 222:408 */       DggContainer dggContainer = new DggContainer();
/* 223:    */       
/* 224:410 */       Dgg dgg = new Dgg(this.numBlips + this.numCharts + 1, this.numBlips);
/* 225:    */       
/* 226:412 */       dgg.addCluster(1, 0);
/* 227:413 */       dgg.addCluster(this.numBlips + 1, 0);
/* 228:    */       
/* 229:415 */       dggContainer.add(dgg);
/* 230:    */       
/* 231:417 */       int drawingsAdded = 0;
/* 232:418 */       BStoreContainer bstoreCont = new BStoreContainer();
/* 233:421 */       for (Iterator i = this.drawings.iterator(); i.hasNext();)
/* 234:    */       {
/* 235:423 */         Object o = i.next();
/* 236:424 */         if ((o instanceof Drawing))
/* 237:    */         {
/* 238:426 */           Drawing d = (Drawing)o;
/* 239:427 */           BlipStoreEntry bse = new BlipStoreEntry(d);
/* 240:    */           
/* 241:429 */           bstoreCont.add(bse);
/* 242:430 */           drawingsAdded++;
/* 243:    */         }
/* 244:    */       }
/* 245:433 */       if (drawingsAdded > 0)
/* 246:    */       {
/* 247:435 */         bstoreCont.setNumBlips(drawingsAdded);
/* 248:436 */         dggContainer.add(bstoreCont);
/* 249:    */       }
/* 250:439 */       Opt opt = new Opt();
/* 251:    */       
/* 252:441 */       dggContainer.add(opt);
/* 253:    */       
/* 254:443 */       SplitMenuColors splitMenuColors = new SplitMenuColors();
/* 255:444 */       dggContainer.add(splitMenuColors);
/* 256:    */       
/* 257:446 */       this.drawingData = dggContainer.getData();
/* 258:    */     }
/* 259:448 */     else if (this.origin == Origin.READ_WRITE)
/* 260:    */     {
/* 261:450 */       DggContainer dggContainer = new DggContainer();
/* 262:    */       
/* 263:452 */       Dgg dgg = new Dgg(this.numBlips + this.numCharts + 1, this.numBlips);
/* 264:    */       
/* 265:454 */       dgg.addCluster(1, 0);
/* 266:455 */       dgg.addCluster(this.drawingGroupId + this.numBlips + 1, 0);
/* 267:    */       
/* 268:457 */       dggContainer.add(dgg);
/* 269:    */       
/* 270:459 */       BStoreContainer bstoreCont = new BStoreContainer();
/* 271:460 */       bstoreCont.setNumBlips(this.numBlips);
/* 272:    */       
/* 273:    */ 
/* 274:463 */       BStoreContainer readBStoreContainer = getBStoreContainer();
/* 275:465 */       if (readBStoreContainer != null)
/* 276:    */       {
/* 277:467 */         EscherRecord[] children = readBStoreContainer.getChildren();
/* 278:468 */         for (int i = 0; i < children.length; i++)
/* 279:    */         {
/* 280:470 */           BlipStoreEntry bse = (BlipStoreEntry)children[i];
/* 281:471 */           bstoreCont.add(bse);
/* 282:    */         }
/* 283:    */       }
/* 284:476 */       for (Iterator i = this.drawings.iterator(); i.hasNext();)
/* 285:    */       {
/* 286:478 */         DrawingGroupObject dgo = (DrawingGroupObject)i.next();
/* 287:479 */         if ((dgo instanceof Drawing))
/* 288:    */         {
/* 289:481 */           Drawing d = (Drawing)dgo;
/* 290:482 */           if (d.getOrigin() == Origin.WRITE)
/* 291:    */           {
/* 292:484 */             BlipStoreEntry bse = new BlipStoreEntry(d);
/* 293:485 */             bstoreCont.add(bse);
/* 294:    */           }
/* 295:    */         }
/* 296:    */       }
/* 297:490 */       dggContainer.add(bstoreCont);
/* 298:    */       
/* 299:492 */       Opt opt = new Opt();
/* 300:    */       
/* 301:494 */       opt.addProperty(191, false, false, 524296);
/* 302:495 */       opt.addProperty(385, false, false, 134217737);
/* 303:496 */       opt.addProperty(448, false, false, 134217792);
/* 304:    */       
/* 305:498 */       dggContainer.add(opt);
/* 306:    */       
/* 307:500 */       SplitMenuColors splitMenuColors = new SplitMenuColors();
/* 308:501 */       dggContainer.add(splitMenuColors);
/* 309:    */       
/* 310:503 */       this.drawingData = dggContainer.getData();
/* 311:    */     }
/* 312:506 */     MsoDrawingGroupRecord msodg = new MsoDrawingGroupRecord(this.drawingData);
/* 313:507 */     outputFile.write(msodg);
/* 314:    */   }
/* 315:    */   
/* 316:    */   final int getNumberOfBlips()
/* 317:    */   {
/* 318:517 */     return this.numBlips;
/* 319:    */   }
/* 320:    */   
/* 321:    */   byte[] getImageData(int blipId)
/* 322:    */   {
/* 323:529 */     this.numBlips = getBStoreContainer().getNumBlips();
/* 324:    */     
/* 325:531 */     Assert.verify(blipId <= this.numBlips);
/* 326:532 */     Assert.verify((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE));
/* 327:    */     
/* 328:    */ 
/* 329:535 */     EscherRecord[] children = getBStoreContainer().getChildren();
/* 330:536 */     BlipStoreEntry bse = (BlipStoreEntry)children[(blipId - 1)];
/* 331:    */     
/* 332:538 */     return bse.getImageData();
/* 333:    */   }
/* 334:    */   
/* 335:    */   public void setDrawingsOmitted(MsoDrawingRecord mso, ObjRecord obj)
/* 336:    */   {
/* 337:550 */     this.drawingsOmitted = true;
/* 338:552 */     if (obj != null) {
/* 339:554 */       this.maxObjectId = Math.max(this.maxObjectId, obj.getObjectId());
/* 340:    */     }
/* 341:    */   }
/* 342:    */   
/* 343:    */   public boolean hasDrawingsOmitted()
/* 344:    */   {
/* 345:565 */     return this.drawingsOmitted;
/* 346:    */   }
/* 347:    */   
/* 348:    */   public void updateData(DrawingGroup dg)
/* 349:    */   {
/* 350:580 */     this.drawingsOmitted = dg.drawingsOmitted;
/* 351:581 */     this.maxObjectId = dg.maxObjectId;
/* 352:582 */     this.maxShapeId = dg.maxShapeId;
/* 353:    */   }
/* 354:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.DrawingGroup
 * JD-Core Version:    0.7.0.1
 */