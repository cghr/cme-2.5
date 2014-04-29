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
/*  12:    */ public class Comment
/*  13:    */   implements DrawingGroupObject
/*  14:    */ {
/*  15: 42 */   private static Logger logger = Logger.getLogger(Comment.class);
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
/*  37:    */   private NoteRecord note;
/*  38:    */   private ContinueRecord text;
/*  39:    */   private ContinueRecord formatting;
/*  40:    */   private String commentText;
/*  41:    */   private WorkbookSettings workbookSettings;
/*  42:    */   
/*  43:    */   public Comment(MsoDrawingRecord msorec, ObjRecord obj, DrawingData dd, DrawingGroup dg, WorkbookSettings ws)
/*  44:    */   {
/*  45:186 */     this.drawingGroup = dg;
/*  46:187 */     this.msoDrawingRecord = msorec;
/*  47:188 */     this.drawingData = dd;
/*  48:189 */     this.objRecord = obj;
/*  49:190 */     this.initialized = false;
/*  50:191 */     this.workbookSettings = ws;
/*  51:192 */     this.origin = Origin.READ;
/*  52:193 */     this.drawingData.addData(this.msoDrawingRecord.getData());
/*  53:194 */     this.drawingNumber = (this.drawingData.getNumDrawings() - 1);
/*  54:195 */     this.drawingGroup.addDrawing(this);
/*  55:    */     
/*  56:197 */     Assert.verify((this.msoDrawingRecord != null) && (this.objRecord != null));
/*  57:199 */     if (!this.initialized) {
/*  58:201 */       initialize();
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Comment(DrawingGroupObject dgo, DrawingGroup dg, WorkbookSettings ws)
/*  63:    */   {
/*  64:216 */     Comment d = (Comment)dgo;
/*  65:217 */     Assert.verify(d.origin == Origin.READ);
/*  66:218 */     this.msoDrawingRecord = d.msoDrawingRecord;
/*  67:219 */     this.objRecord = d.objRecord;
/*  68:220 */     this.initialized = false;
/*  69:221 */     this.origin = Origin.READ;
/*  70:222 */     this.drawingData = d.drawingData;
/*  71:223 */     this.drawingGroup = dg;
/*  72:224 */     this.drawingNumber = d.drawingNumber;
/*  73:225 */     this.drawingGroup.addDrawing(this);
/*  74:226 */     this.mso = d.mso;
/*  75:227 */     this.txo = d.txo;
/*  76:228 */     this.text = d.text;
/*  77:229 */     this.formatting = d.formatting;
/*  78:230 */     this.note = d.note;
/*  79:231 */     this.width = d.width;
/*  80:232 */     this.height = d.height;
/*  81:233 */     this.workbookSettings = ws;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public Comment(String txt, int c, int r)
/*  85:    */   {
/*  86:245 */     this.initialized = true;
/*  87:246 */     this.origin = Origin.WRITE;
/*  88:247 */     this.column = c;
/*  89:248 */     this.row = r;
/*  90:249 */     this.referenceCount = 1;
/*  91:250 */     this.type = ShapeType.TEXT_BOX;
/*  92:251 */     this.commentText = txt;
/*  93:252 */     this.width = 3.0D;
/*  94:253 */     this.height = 4.0D;
/*  95:    */   }
/*  96:    */   
/*  97:    */   private void initialize()
/*  98:    */   {
/*  99:261 */     this.readSpContainer = this.drawingData.getSpContainer(this.drawingNumber);
/* 100:262 */     Assert.verify(this.readSpContainer != null);
/* 101:    */     
/* 102:264 */     EscherRecord[] children = this.readSpContainer.getChildren();
/* 103:    */     
/* 104:266 */     Sp sp = (Sp)this.readSpContainer.getChildren()[0];
/* 105:267 */     this.objectId = this.objRecord.getObjectId();
/* 106:268 */     this.shapeId = sp.getShapeId();
/* 107:269 */     this.type = ShapeType.getType(sp.getShapeType());
/* 108:271 */     if (this.type == ShapeType.UNKNOWN) {
/* 109:273 */       logger.warn("Unknown shape type");
/* 110:    */     }
/* 111:276 */     ClientAnchor clientAnchor = null;
/* 112:277 */     for (int i = 0; (i < children.length) && (clientAnchor == null); i++) {
/* 113:279 */       if (children[i].getType() == EscherRecordType.CLIENT_ANCHOR) {
/* 114:281 */         clientAnchor = (ClientAnchor)children[i];
/* 115:    */       }
/* 116:    */     }
/* 117:285 */     if (clientAnchor == null)
/* 118:    */     {
/* 119:287 */       logger.warn("client anchor not found");
/* 120:    */     }
/* 121:    */     else
/* 122:    */     {
/* 123:291 */       this.column = ((int)clientAnchor.getX1() - 1);
/* 124:292 */       this.row = ((int)clientAnchor.getY1() + 1);
/* 125:293 */       this.width = (clientAnchor.getX2() - clientAnchor.getX1());
/* 126:294 */       this.height = (clientAnchor.getY2() - clientAnchor.getY1());
/* 127:    */     }
/* 128:297 */     this.initialized = true;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public final void setObjectId(int objid, int bip, int sid)
/* 132:    */   {
/* 133:311 */     this.objectId = objid;
/* 134:312 */     this.blipId = bip;
/* 135:313 */     this.shapeId = sid;
/* 136:315 */     if (this.origin == Origin.READ) {
/* 137:317 */       this.origin = Origin.READ_WRITE;
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public final int getObjectId()
/* 142:    */   {
/* 143:328 */     if (!this.initialized) {
/* 144:330 */       initialize();
/* 145:    */     }
/* 146:333 */     return this.objectId;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public final int getShapeId()
/* 150:    */   {
/* 151:343 */     if (!this.initialized) {
/* 152:345 */       initialize();
/* 153:    */     }
/* 154:348 */     return this.shapeId;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public final int getBlipId()
/* 158:    */   {
/* 159:358 */     if (!this.initialized) {
/* 160:360 */       initialize();
/* 161:    */     }
/* 162:363 */     return this.blipId;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public MsoDrawingRecord getMsoDrawingRecord()
/* 166:    */   {
/* 167:373 */     return this.msoDrawingRecord;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public EscherContainer getSpContainer()
/* 171:    */   {
/* 172:383 */     if (!this.initialized) {
/* 173:385 */       initialize();
/* 174:    */     }
/* 175:388 */     if (this.origin == Origin.READ) {
/* 176:390 */       return getReadSpContainer();
/* 177:    */     }
/* 178:393 */     if (this.spContainer == null)
/* 179:    */     {
/* 180:395 */       this.spContainer = new SpContainer();
/* 181:396 */       Sp sp = new Sp(this.type, this.shapeId, 2560);
/* 182:397 */       this.spContainer.add(sp);
/* 183:398 */       Opt opt = new Opt();
/* 184:399 */       opt.addProperty(344, false, false, 0);
/* 185:400 */       opt.addProperty(385, false, false, 134217808);
/* 186:401 */       opt.addProperty(387, false, false, 134217808);
/* 187:402 */       opt.addProperty(959, false, false, 131074);
/* 188:403 */       this.spContainer.add(opt);
/* 189:    */       
/* 190:405 */       ClientAnchor clientAnchor = new ClientAnchor(this.column + 1.3D, 
/* 191:406 */         Math.max(0.0D, this.row - 0.6D), 
/* 192:407 */         this.column + 1.3D + this.width, 
/* 193:408 */         this.row + this.height, 
/* 194:409 */         1);
/* 195:    */       
/* 196:411 */       this.spContainer.add(clientAnchor);
/* 197:    */       
/* 198:413 */       ClientData clientData = new ClientData();
/* 199:414 */       this.spContainer.add(clientData);
/* 200:    */       
/* 201:416 */       ClientTextBox clientTextBox = new ClientTextBox();
/* 202:417 */       this.spContainer.add(clientTextBox);
/* 203:    */     }
/* 204:420 */     return this.spContainer;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void setDrawingGroup(DrawingGroup dg)
/* 208:    */   {
/* 209:431 */     this.drawingGroup = dg;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public DrawingGroup getDrawingGroup()
/* 213:    */   {
/* 214:441 */     return this.drawingGroup;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public Origin getOrigin()
/* 218:    */   {
/* 219:451 */     return this.origin;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public int getReferenceCount()
/* 223:    */   {
/* 224:461 */     return this.referenceCount;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public void setReferenceCount(int r)
/* 228:    */   {
/* 229:471 */     this.referenceCount = r;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public double getX()
/* 233:    */   {
/* 234:481 */     if (!this.initialized) {
/* 235:483 */       initialize();
/* 236:    */     }
/* 237:485 */     return this.column;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public void setX(double x)
/* 241:    */   {
/* 242:496 */     if (this.origin == Origin.READ)
/* 243:    */     {
/* 244:498 */       if (!this.initialized) {
/* 245:500 */         initialize();
/* 246:    */       }
/* 247:502 */       this.origin = Origin.READ_WRITE;
/* 248:    */     }
/* 249:505 */     this.column = ((int)x);
/* 250:    */   }
/* 251:    */   
/* 252:    */   public double getY()
/* 253:    */   {
/* 254:515 */     if (!this.initialized) {
/* 255:517 */       initialize();
/* 256:    */     }
/* 257:520 */     return this.row;
/* 258:    */   }
/* 259:    */   
/* 260:    */   public void setY(double y)
/* 261:    */   {
/* 262:530 */     if (this.origin == Origin.READ)
/* 263:    */     {
/* 264:532 */       if (!this.initialized) {
/* 265:534 */         initialize();
/* 266:    */       }
/* 267:536 */       this.origin = Origin.READ_WRITE;
/* 268:    */     }
/* 269:539 */     this.row = ((int)y);
/* 270:    */   }
/* 271:    */   
/* 272:    */   public double getWidth()
/* 273:    */   {
/* 274:550 */     if (!this.initialized) {
/* 275:552 */       initialize();
/* 276:    */     }
/* 277:555 */     return this.width;
/* 278:    */   }
/* 279:    */   
/* 280:    */   public void setWidth(double w)
/* 281:    */   {
/* 282:565 */     if (this.origin == Origin.READ)
/* 283:    */     {
/* 284:567 */       if (!this.initialized) {
/* 285:569 */         initialize();
/* 286:    */       }
/* 287:571 */       this.origin = Origin.READ_WRITE;
/* 288:    */     }
/* 289:574 */     this.width = w;
/* 290:    */   }
/* 291:    */   
/* 292:    */   public double getHeight()
/* 293:    */   {
/* 294:584 */     if (!this.initialized) {
/* 295:586 */       initialize();
/* 296:    */     }
/* 297:589 */     return this.height;
/* 298:    */   }
/* 299:    */   
/* 300:    */   public void setHeight(double h)
/* 301:    */   {
/* 302:599 */     if (this.origin == Origin.READ)
/* 303:    */     {
/* 304:601 */       if (!this.initialized) {
/* 305:603 */         initialize();
/* 306:    */       }
/* 307:605 */       this.origin = Origin.READ_WRITE;
/* 308:    */     }
/* 309:608 */     this.height = h;
/* 310:    */   }
/* 311:    */   
/* 312:    */   private EscherContainer getReadSpContainer()
/* 313:    */   {
/* 314:619 */     if (!this.initialized) {
/* 315:621 */       initialize();
/* 316:    */     }
/* 317:624 */     return this.readSpContainer;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public byte[] getImageData()
/* 321:    */   {
/* 322:634 */     Assert.verify((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE));
/* 323:636 */     if (!this.initialized) {
/* 324:638 */       initialize();
/* 325:    */     }
/* 326:641 */     return this.drawingGroup.getImageData(this.blipId);
/* 327:    */   }
/* 328:    */   
/* 329:    */   public ShapeType getType()
/* 330:    */   {
/* 331:651 */     return this.type;
/* 332:    */   }
/* 333:    */   
/* 334:    */   public void setTextObject(TextObjectRecord t)
/* 335:    */   {
/* 336:661 */     this.txo = t;
/* 337:    */   }
/* 338:    */   
/* 339:    */   public void setNote(NoteRecord t)
/* 340:    */   {
/* 341:671 */     this.note = t;
/* 342:    */   }
/* 343:    */   
/* 344:    */   public void setText(ContinueRecord t)
/* 345:    */   {
/* 346:681 */     this.text = t;
/* 347:    */   }
/* 348:    */   
/* 349:    */   public void setFormatting(ContinueRecord t)
/* 350:    */   {
/* 351:691 */     this.formatting = t;
/* 352:    */   }
/* 353:    */   
/* 354:    */   public byte[] getImageBytes()
/* 355:    */   {
/* 356:701 */     Assert.verify(false);
/* 357:702 */     return null;
/* 358:    */   }
/* 359:    */   
/* 360:    */   public String getImageFilePath()
/* 361:    */   {
/* 362:714 */     Assert.verify(false);
/* 363:715 */     return null;
/* 364:    */   }
/* 365:    */   
/* 366:    */   public void addMso(MsoDrawingRecord d)
/* 367:    */   {
/* 368:725 */     this.mso = d;
/* 369:726 */     this.drawingData.addRawData(this.mso.getData());
/* 370:    */   }
/* 371:    */   
/* 372:    */   public void writeAdditionalRecords(File outputFile)
/* 373:    */     throws IOException
/* 374:    */   {
/* 375:737 */     if (this.origin == Origin.READ)
/* 376:    */     {
/* 377:739 */       outputFile.write(this.objRecord);
/* 378:741 */       if (this.mso != null) {
/* 379:743 */         outputFile.write(this.mso);
/* 380:    */       }
/* 381:745 */       outputFile.write(this.txo);
/* 382:746 */       outputFile.write(this.text);
/* 383:747 */       if (this.formatting != null) {
/* 384:749 */         outputFile.write(this.formatting);
/* 385:    */       }
/* 386:751 */       return;
/* 387:    */     }
/* 388:755 */     ObjRecord objrec = new ObjRecord(this.objectId, 
/* 389:756 */       ObjRecord.EXCELNOTE);
/* 390:    */     
/* 391:758 */     outputFile.write(objrec);
/* 392:    */     
/* 393:    */ 
/* 394:    */ 
/* 395:762 */     ClientTextBox textBox = new ClientTextBox();
/* 396:763 */     MsoDrawingRecord msod = new MsoDrawingRecord(textBox.getData());
/* 397:764 */     outputFile.write(msod);
/* 398:    */     
/* 399:766 */     TextObjectRecord txorec = new TextObjectRecord(getText());
/* 400:767 */     outputFile.write(txorec);
/* 401:    */     
/* 402:    */ 
/* 403:770 */     byte[] textData = new byte[this.commentText.length() * 2 + 1];
/* 404:771 */     textData[0] = 1;
/* 405:772 */     StringHelper.getUnicodeBytes(this.commentText, textData, 1);
/* 406:    */     
/* 407:774 */     ContinueRecord textContinue = new ContinueRecord(textData);
/* 408:775 */     outputFile.write(textContinue);
/* 409:    */     
/* 410:    */ 
/* 411:    */ 
/* 412:779 */     byte[] frData = new byte[16];
/* 413:    */     
/* 414:    */ 
/* 415:782 */     IntegerHelper.getTwoBytes(0, frData, 0);
/* 416:783 */     IntegerHelper.getTwoBytes(0, frData, 2);
/* 417:    */     
/* 418:785 */     IntegerHelper.getTwoBytes(this.commentText.length(), frData, 8);
/* 419:786 */     IntegerHelper.getTwoBytes(0, frData, 10);
/* 420:    */     
/* 421:788 */     ContinueRecord frContinue = new ContinueRecord(frData);
/* 422:789 */     outputFile.write(frContinue);
/* 423:    */   }
/* 424:    */   
/* 425:    */   public void writeTailRecords(File outputFile)
/* 426:    */     throws IOException
/* 427:    */   {
/* 428:802 */     if (this.origin == Origin.READ)
/* 429:    */     {
/* 430:804 */       outputFile.write(this.note);
/* 431:805 */       return;
/* 432:    */     }
/* 433:809 */     NoteRecord noteRecord = new NoteRecord(this.column, this.row, this.objectId);
/* 434:810 */     outputFile.write(noteRecord);
/* 435:    */   }
/* 436:    */   
/* 437:    */   public int getRow()
/* 438:    */   {
/* 439:820 */     return this.note.getRow();
/* 440:    */   }
/* 441:    */   
/* 442:    */   public int getColumn()
/* 443:    */   {
/* 444:830 */     return this.note.getColumn();
/* 445:    */   }
/* 446:    */   
/* 447:    */   public String getText()
/* 448:    */   {
/* 449:840 */     if (this.commentText == null)
/* 450:    */     {
/* 451:842 */       Assert.verify(this.text != null);
/* 452:    */       
/* 453:844 */       byte[] td = this.text.getData();
/* 454:845 */       if (td[0] == 0) {
/* 455:847 */         this.commentText = StringHelper.getString(
/* 456:848 */           td, td.length - 1, 1, this.workbookSettings);
/* 457:    */       } else {
/* 458:852 */         this.commentText = StringHelper.getUnicodeString(
/* 459:853 */           td, (td.length - 1) / 2, 1);
/* 460:    */       }
/* 461:    */     }
/* 462:857 */     return this.commentText;
/* 463:    */   }
/* 464:    */   
/* 465:    */   public int hashCode()
/* 466:    */   {
/* 467:867 */     return this.commentText.hashCode();
/* 468:    */   }
/* 469:    */   
/* 470:    */   public void setCommentText(String t)
/* 471:    */   {
/* 472:877 */     this.commentText = t;
/* 473:879 */     if (this.origin == Origin.READ) {
/* 474:881 */       this.origin = Origin.READ_WRITE;
/* 475:    */     }
/* 476:    */   }
/* 477:    */   
/* 478:    */   public boolean isFirst()
/* 479:    */   {
/* 480:894 */     return this.msoDrawingRecord.isFirst();
/* 481:    */   }
/* 482:    */   
/* 483:    */   public boolean isFormObject()
/* 484:    */   {
/* 485:905 */     return true;
/* 486:    */   }
/* 487:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Comment
 * JD-Core Version:    0.7.0.1
 */