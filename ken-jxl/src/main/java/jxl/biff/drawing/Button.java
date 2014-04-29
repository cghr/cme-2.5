/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.biff.ContinueRecord;
/*   6:    */ import jxl.biff.IntegerHelper;
/*   7:    */ import jxl.biff.StringHelper;
/*   8:    */ import jxl.common.Assert;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ import jxl.write.biff.File;
/*  11:    */ 
/*  12:    */ public class Button
/*  13:    */   implements DrawingGroupObject
/*  14:    */ {
/*  15: 42 */   private static Logger logger = Logger.getLogger(Button.class);
/*  16:    */   private EscherContainer readSpContainer;
/*  17:    */   private EscherContainer spContainer;
/*  18:    */   private MsoDrawingRecord msoDrawingRecord;
/*  19:    */   private ObjRecord objRecord;
/*  20: 67 */   private boolean initialized = false;
/*  21:    */   private int objectId;
/*  22:    */   private int blipId;
/*  23:    */   private int shapeId;
/*  24:    */   private int column;
/*  25:    */   private int row;
/*  26:    */   private double width;
/*  27:    */   private double height;
/*  28:    */   private int referenceCount;
/*  29:    */   private EscherContainer escherData;
/*  30:    */   private Origin origin;
/*  31:    */   private DrawingGroup drawingGroup;
/*  32:    */   private DrawingData drawingData;
/*  33:    */   private ShapeType type;
/*  34:    */   private int drawingNumber;
/*  35:    */   private MsoDrawingRecord mso;
/*  36:    */   private TextObjectRecord txo;
/*  37:    */   private ContinueRecord text;
/*  38:    */   private ContinueRecord formatting;
/*  39:    */   private String commentText;
/*  40:    */   private WorkbookSettings workbookSettings;
/*  41:    */   
/*  42:    */   public Button(MsoDrawingRecord msodr, ObjRecord obj, DrawingData dd, DrawingGroup dg, WorkbookSettings ws)
/*  43:    */   {
/*  44:184 */     this.drawingGroup = dg;
/*  45:185 */     this.msoDrawingRecord = msodr;
/*  46:186 */     this.drawingData = dd;
/*  47:187 */     this.objRecord = obj;
/*  48:188 */     this.initialized = false;
/*  49:189 */     this.workbookSettings = ws;
/*  50:190 */     this.origin = Origin.READ;
/*  51:191 */     this.drawingData.addData(this.msoDrawingRecord.getData());
/*  52:192 */     this.drawingNumber = (this.drawingData.getNumDrawings() - 1);
/*  53:193 */     this.drawingGroup.addDrawing(this);
/*  54:    */     
/*  55:195 */     Assert.verify((this.msoDrawingRecord != null) && (this.objRecord != null));
/*  56:    */     
/*  57:197 */     initialize();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Button(DrawingGroupObject dgo, DrawingGroup dg, WorkbookSettings ws)
/*  61:    */   {
/*  62:211 */     Button d = (Button)dgo;
/*  63:212 */     Assert.verify(d.origin == Origin.READ);
/*  64:213 */     this.msoDrawingRecord = d.msoDrawingRecord;
/*  65:214 */     this.objRecord = d.objRecord;
/*  66:215 */     this.initialized = false;
/*  67:216 */     this.origin = Origin.READ;
/*  68:217 */     this.drawingData = d.drawingData;
/*  69:218 */     this.drawingGroup = dg;
/*  70:219 */     this.drawingNumber = d.drawingNumber;
/*  71:220 */     this.drawingGroup.addDrawing(this);
/*  72:221 */     this.mso = d.mso;
/*  73:222 */     this.txo = d.txo;
/*  74:223 */     this.text = d.text;
/*  75:224 */     this.formatting = d.formatting;
/*  76:225 */     this.workbookSettings = ws;
/*  77:    */   }
/*  78:    */   
/*  79:    */   private void initialize()
/*  80:    */   {
/*  81:233 */     this.readSpContainer = this.drawingData.getSpContainer(this.drawingNumber);
/*  82:234 */     Assert.verify(this.readSpContainer != null);
/*  83:    */     
/*  84:236 */     EscherRecord[] children = this.readSpContainer.getChildren();
/*  85:    */     
/*  86:238 */     Sp sp = (Sp)this.readSpContainer.getChildren()[0];
/*  87:239 */     this.objectId = this.objRecord.getObjectId();
/*  88:240 */     this.shapeId = sp.getShapeId();
/*  89:241 */     this.type = ShapeType.getType(sp.getShapeType());
/*  90:243 */     if (this.type == ShapeType.UNKNOWN) {
/*  91:245 */       logger.warn("Unknown shape type");
/*  92:    */     }
/*  93:248 */     ClientAnchor clientAnchor = null;
/*  94:249 */     for (int i = 0; (i < children.length) && (clientAnchor == null); i++) {
/*  95:251 */       if (children[i].getType() == EscherRecordType.CLIENT_ANCHOR) {
/*  96:253 */         clientAnchor = (ClientAnchor)children[i];
/*  97:    */       }
/*  98:    */     }
/*  99:257 */     if (clientAnchor == null)
/* 100:    */     {
/* 101:259 */       logger.warn("Client anchor not found");
/* 102:    */     }
/* 103:    */     else
/* 104:    */     {
/* 105:263 */       this.column = ((int)clientAnchor.getX1() - 1);
/* 106:264 */       this.row = ((int)clientAnchor.getY1() + 1);
/* 107:    */     }
/* 108:267 */     this.initialized = true;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public final void setObjectId(int objid, int bip, int sid)
/* 112:    */   {
/* 113:281 */     this.objectId = objid;
/* 114:282 */     this.blipId = bip;
/* 115:283 */     this.shapeId = sid;
/* 116:285 */     if (this.origin == Origin.READ) {
/* 117:287 */       this.origin = Origin.READ_WRITE;
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public final int getObjectId()
/* 122:    */   {
/* 123:298 */     if (!this.initialized) {
/* 124:300 */       initialize();
/* 125:    */     }
/* 126:303 */     return this.objectId;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public final int getShapeId()
/* 130:    */   {
/* 131:313 */     if (!this.initialized) {
/* 132:315 */       initialize();
/* 133:    */     }
/* 134:318 */     return this.shapeId;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public final int getBlipId()
/* 138:    */   {
/* 139:328 */     if (!this.initialized) {
/* 140:330 */       initialize();
/* 141:    */     }
/* 142:333 */     return this.blipId;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public MsoDrawingRecord getMsoDrawingRecord()
/* 146:    */   {
/* 147:343 */     return this.msoDrawingRecord;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public EscherContainer getSpContainer()
/* 151:    */   {
/* 152:353 */     if (!this.initialized) {
/* 153:355 */       initialize();
/* 154:    */     }
/* 155:358 */     if (this.origin == Origin.READ) {
/* 156:360 */       return getReadSpContainer();
/* 157:    */     }
/* 158:363 */     Assert.verify(false);
/* 159:    */     
/* 160:    */ 
/* 161:    */ 
/* 162:    */ 
/* 163:    */ 
/* 164:    */ 
/* 165:    */ 
/* 166:    */ 
/* 167:    */ 
/* 168:    */ 
/* 169:    */ 
/* 170:    */ 
/* 171:    */ 
/* 172:    */ 
/* 173:    */ 
/* 174:    */ 
/* 175:    */ 
/* 176:    */ 
/* 177:    */ 
/* 178:    */ 
/* 179:    */ 
/* 180:    */ 
/* 181:    */ 
/* 182:    */ 
/* 183:    */ 
/* 184:    */ 
/* 185:    */ 
/* 186:    */ 
/* 187:392 */     return this.spContainer;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void setDrawingGroup(DrawingGroup dg)
/* 191:    */   {
/* 192:403 */     this.drawingGroup = dg;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public DrawingGroup getDrawingGroup()
/* 196:    */   {
/* 197:413 */     return this.drawingGroup;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public Origin getOrigin()
/* 201:    */   {
/* 202:423 */     return this.origin;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public int getReferenceCount()
/* 206:    */   {
/* 207:433 */     return this.referenceCount;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public void setReferenceCount(int r)
/* 211:    */   {
/* 212:443 */     this.referenceCount = r;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public double getX()
/* 216:    */   {
/* 217:453 */     if (!this.initialized) {
/* 218:455 */       initialize();
/* 219:    */     }
/* 220:457 */     return this.column;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public void setX(double x)
/* 224:    */   {
/* 225:468 */     if (this.origin == Origin.READ)
/* 226:    */     {
/* 227:470 */       if (!this.initialized) {
/* 228:472 */         initialize();
/* 229:    */       }
/* 230:474 */       this.origin = Origin.READ_WRITE;
/* 231:    */     }
/* 232:477 */     this.column = ((int)x);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public double getY()
/* 236:    */   {
/* 237:487 */     if (!this.initialized) {
/* 238:489 */       initialize();
/* 239:    */     }
/* 240:492 */     return this.row;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void setY(double y)
/* 244:    */   {
/* 245:502 */     if (this.origin == Origin.READ)
/* 246:    */     {
/* 247:504 */       if (!this.initialized) {
/* 248:506 */         initialize();
/* 249:    */       }
/* 250:508 */       this.origin = Origin.READ_WRITE;
/* 251:    */     }
/* 252:511 */     this.row = ((int)y);
/* 253:    */   }
/* 254:    */   
/* 255:    */   public double getWidth()
/* 256:    */   {
/* 257:522 */     if (!this.initialized) {
/* 258:524 */       initialize();
/* 259:    */     }
/* 260:527 */     return this.width;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public void setWidth(double w)
/* 264:    */   {
/* 265:537 */     if (this.origin == Origin.READ)
/* 266:    */     {
/* 267:539 */       if (!this.initialized) {
/* 268:541 */         initialize();
/* 269:    */       }
/* 270:543 */       this.origin = Origin.READ_WRITE;
/* 271:    */     }
/* 272:546 */     this.width = w;
/* 273:    */   }
/* 274:    */   
/* 275:    */   public double getHeight()
/* 276:    */   {
/* 277:556 */     if (!this.initialized) {
/* 278:558 */       initialize();
/* 279:    */     }
/* 280:561 */     return this.height;
/* 281:    */   }
/* 282:    */   
/* 283:    */   public void setHeight(double h)
/* 284:    */   {
/* 285:571 */     if (this.origin == Origin.READ)
/* 286:    */     {
/* 287:573 */       if (!this.initialized) {
/* 288:575 */         initialize();
/* 289:    */       }
/* 290:577 */       this.origin = Origin.READ_WRITE;
/* 291:    */     }
/* 292:580 */     this.height = h;
/* 293:    */   }
/* 294:    */   
/* 295:    */   private EscherContainer getReadSpContainer()
/* 296:    */   {
/* 297:591 */     if (!this.initialized) {
/* 298:593 */       initialize();
/* 299:    */     }
/* 300:596 */     return this.readSpContainer;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public byte[] getImageData()
/* 304:    */   {
/* 305:606 */     Assert.verify((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE));
/* 306:608 */     if (!this.initialized) {
/* 307:610 */       initialize();
/* 308:    */     }
/* 309:613 */     return this.drawingGroup.getImageData(this.blipId);
/* 310:    */   }
/* 311:    */   
/* 312:    */   public ShapeType getType()
/* 313:    */   {
/* 314:623 */     return this.type;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public void setTextObject(TextObjectRecord t)
/* 318:    */   {
/* 319:633 */     this.txo = t;
/* 320:    */   }
/* 321:    */   
/* 322:    */   public void setText(ContinueRecord t)
/* 323:    */   {
/* 324:643 */     this.text = t;
/* 325:    */   }
/* 326:    */   
/* 327:    */   public void setFormatting(ContinueRecord t)
/* 328:    */   {
/* 329:653 */     this.formatting = t;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public byte[] getImageBytes()
/* 333:    */   {
/* 334:663 */     Assert.verify(false);
/* 335:664 */     return null;
/* 336:    */   }
/* 337:    */   
/* 338:    */   public String getImageFilePath()
/* 339:    */   {
/* 340:676 */     Assert.verify(false);
/* 341:677 */     return null;
/* 342:    */   }
/* 343:    */   
/* 344:    */   public void addMso(MsoDrawingRecord d)
/* 345:    */   {
/* 346:687 */     this.mso = d;
/* 347:688 */     this.drawingData.addRawData(this.mso.getData());
/* 348:    */   }
/* 349:    */   
/* 350:    */   public void writeAdditionalRecords(File outputFile)
/* 351:    */     throws IOException
/* 352:    */   {
/* 353:699 */     if (this.origin == Origin.READ)
/* 354:    */     {
/* 355:701 */       outputFile.write(this.objRecord);
/* 356:703 */       if (this.mso != null) {
/* 357:705 */         outputFile.write(this.mso);
/* 358:    */       }
/* 359:707 */       outputFile.write(this.txo);
/* 360:708 */       outputFile.write(this.text);
/* 361:709 */       if (this.formatting != null) {
/* 362:711 */         outputFile.write(this.formatting);
/* 363:    */       }
/* 364:713 */       return;
/* 365:    */     }
/* 366:716 */     Assert.verify(false);
/* 367:    */     
/* 368:    */ 
/* 369:719 */     ObjRecord objrec = new ObjRecord(this.objectId, 
/* 370:720 */       ObjRecord.EXCELNOTE);
/* 371:    */     
/* 372:722 */     outputFile.write(objrec);
/* 373:    */     
/* 374:    */ 
/* 375:    */ 
/* 376:726 */     ClientTextBox textBox = new ClientTextBox();
/* 377:727 */     MsoDrawingRecord msod = new MsoDrawingRecord(textBox.getData());
/* 378:728 */     outputFile.write(msod);
/* 379:    */     
/* 380:730 */     TextObjectRecord tor = new TextObjectRecord(getText());
/* 381:731 */     outputFile.write(tor);
/* 382:    */     
/* 383:    */ 
/* 384:734 */     byte[] textData = new byte[this.commentText.length() * 2 + 1];
/* 385:735 */     textData[0] = 1;
/* 386:736 */     StringHelper.getUnicodeBytes(this.commentText, textData, 1);
/* 387:    */     
/* 388:738 */     ContinueRecord textContinue = new ContinueRecord(textData);
/* 389:739 */     outputFile.write(textContinue);
/* 390:    */     
/* 391:    */ 
/* 392:    */ 
/* 393:743 */     byte[] frData = new byte[16];
/* 394:    */     
/* 395:    */ 
/* 396:746 */     IntegerHelper.getTwoBytes(0, frData, 0);
/* 397:747 */     IntegerHelper.getTwoBytes(0, frData, 2);
/* 398:    */     
/* 399:749 */     IntegerHelper.getTwoBytes(this.commentText.length(), frData, 8);
/* 400:750 */     IntegerHelper.getTwoBytes(0, frData, 10);
/* 401:    */     
/* 402:752 */     ContinueRecord frContinue = new ContinueRecord(frData);
/* 403:753 */     outputFile.write(frContinue);
/* 404:    */   }
/* 405:    */   
/* 406:    */   public void writeTailRecords(File outputFile) {}
/* 407:    */   
/* 408:    */   public int getRow()
/* 409:    */   {
/* 410:775 */     return 0;
/* 411:    */   }
/* 412:    */   
/* 413:    */   public int getColumn()
/* 414:    */   {
/* 415:786 */     return 0;
/* 416:    */   }
/* 417:    */   
/* 418:    */   public String getText()
/* 419:    */   {
/* 420:796 */     if (this.commentText == null)
/* 421:    */     {
/* 422:798 */       Assert.verify(this.text != null);
/* 423:    */       
/* 424:800 */       byte[] td = this.text.getData();
/* 425:801 */       if (td[0] == 0) {
/* 426:803 */         this.commentText = StringHelper.getString(
/* 427:804 */           td, td.length - 1, 1, this.workbookSettings);
/* 428:    */       } else {
/* 429:808 */         this.commentText = StringHelper.getUnicodeString(
/* 430:809 */           td, (td.length - 1) / 2, 1);
/* 431:    */       }
/* 432:    */     }
/* 433:813 */     return this.commentText;
/* 434:    */   }
/* 435:    */   
/* 436:    */   public int hashCode()
/* 437:    */   {
/* 438:823 */     return this.commentText.hashCode();
/* 439:    */   }
/* 440:    */   
/* 441:    */   public void setButtonText(String t)
/* 442:    */   {
/* 443:833 */     this.commentText = t;
/* 444:835 */     if (this.origin == Origin.READ) {
/* 445:837 */       this.origin = Origin.READ_WRITE;
/* 446:    */     }
/* 447:    */   }
/* 448:    */   
/* 449:    */   public boolean isFirst()
/* 450:    */   {
/* 451:850 */     return this.mso.isFirst();
/* 452:    */   }
/* 453:    */   
/* 454:    */   public boolean isFormObject()
/* 455:    */   {
/* 456:862 */     return true;
/* 457:    */   }
/* 458:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Button
 * JD-Core Version:    0.7.0.1
 */