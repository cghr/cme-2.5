/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.biff.ContinueRecord;
/*   6:    */ import jxl.common.Assert;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ import jxl.write.biff.File;
/*   9:    */ 
/*  10:    */ public class CheckBox
/*  11:    */   implements DrawingGroupObject
/*  12:    */ {
/*  13: 40 */   private static Logger logger = Logger.getLogger(CheckBox.class);
/*  14:    */   private EscherContainer readSpContainer;
/*  15:    */   private EscherContainer spContainer;
/*  16:    */   private MsoDrawingRecord msoDrawingRecord;
/*  17:    */   private ObjRecord objRecord;
/*  18: 65 */   private boolean initialized = false;
/*  19:    */   private int objectId;
/*  20:    */   private int blipId;
/*  21:    */   private int shapeId;
/*  22:    */   private int column;
/*  23:    */   private int row;
/*  24:    */   private double width;
/*  25:    */   private double height;
/*  26:    */   private int referenceCount;
/*  27:    */   private EscherContainer escherData;
/*  28:    */   private Origin origin;
/*  29:    */   private DrawingGroup drawingGroup;
/*  30:    */   private DrawingData drawingData;
/*  31:    */   private ShapeType type;
/*  32:    */   private int drawingNumber;
/*  33:    */   private MsoDrawingRecord mso;
/*  34:    */   private TextObjectRecord txo;
/*  35:    */   private ContinueRecord text;
/*  36:    */   private ContinueRecord formatting;
/*  37:    */   private WorkbookSettings workbookSettings;
/*  38:    */   
/*  39:    */   public CheckBox(MsoDrawingRecord mso, ObjRecord obj, DrawingData dd, DrawingGroup dg, WorkbookSettings ws)
/*  40:    */   {
/*  41:174 */     this.drawingGroup = dg;
/*  42:175 */     this.msoDrawingRecord = mso;
/*  43:176 */     this.drawingData = dd;
/*  44:177 */     this.objRecord = obj;
/*  45:178 */     this.initialized = false;
/*  46:179 */     this.workbookSettings = ws;
/*  47:180 */     this.origin = Origin.READ;
/*  48:181 */     this.drawingData.addData(this.msoDrawingRecord.getData());
/*  49:182 */     this.drawingNumber = (this.drawingData.getNumDrawings() - 1);
/*  50:183 */     this.drawingGroup.addDrawing(this);
/*  51:    */     
/*  52:185 */     Assert.verify((mso != null) && (obj != null));
/*  53:    */     
/*  54:187 */     initialize();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public CheckBox(DrawingGroupObject dgo, DrawingGroup dg, WorkbookSettings ws)
/*  58:    */   {
/*  59:201 */     CheckBox d = (CheckBox)dgo;
/*  60:202 */     Assert.verify(d.origin == Origin.READ);
/*  61:203 */     this.msoDrawingRecord = d.msoDrawingRecord;
/*  62:204 */     this.objRecord = d.objRecord;
/*  63:205 */     this.initialized = false;
/*  64:206 */     this.origin = Origin.READ;
/*  65:207 */     this.drawingData = d.drawingData;
/*  66:208 */     this.drawingGroup = dg;
/*  67:209 */     this.drawingNumber = d.drawingNumber;
/*  68:210 */     this.drawingGroup.addDrawing(this);
/*  69:211 */     this.mso = d.mso;
/*  70:212 */     this.txo = d.txo;
/*  71:213 */     this.text = d.text;
/*  72:214 */     this.formatting = d.formatting;
/*  73:215 */     this.workbookSettings = ws;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public CheckBox()
/*  77:    */   {
/*  78:223 */     this.initialized = true;
/*  79:224 */     this.origin = Origin.WRITE;
/*  80:225 */     this.referenceCount = 1;
/*  81:226 */     this.type = ShapeType.HOST_CONTROL;
/*  82:    */   }
/*  83:    */   
/*  84:    */   private void initialize()
/*  85:    */   {
/*  86:234 */     this.readSpContainer = this.drawingData.getSpContainer(this.drawingNumber);
/*  87:235 */     Assert.verify(this.readSpContainer != null);
/*  88:    */     
/*  89:237 */     EscherRecord[] children = this.readSpContainer.getChildren();
/*  90:    */     
/*  91:239 */     Sp sp = (Sp)this.readSpContainer.getChildren()[0];
/*  92:240 */     this.objectId = this.objRecord.getObjectId();
/*  93:241 */     this.shapeId = sp.getShapeId();
/*  94:242 */     this.type = ShapeType.getType(sp.getShapeType());
/*  95:244 */     if (this.type == ShapeType.UNKNOWN) {
/*  96:246 */       logger.warn("Unknown shape type");
/*  97:    */     }
/*  98:249 */     ClientAnchor clientAnchor = null;
/*  99:250 */     for (int i = 0; (i < children.length) && (clientAnchor == null); i++) {
/* 100:252 */       if (children[i].getType() == EscherRecordType.CLIENT_ANCHOR) {
/* 101:254 */         clientAnchor = (ClientAnchor)children[i];
/* 102:    */       }
/* 103:    */     }
/* 104:258 */     if (clientAnchor == null)
/* 105:    */     {
/* 106:260 */       logger.warn("Client anchor not found");
/* 107:    */     }
/* 108:    */     else
/* 109:    */     {
/* 110:264 */       this.column = ((int)clientAnchor.getX1());
/* 111:265 */       this.row = ((int)clientAnchor.getY1());
/* 112:    */     }
/* 113:268 */     this.initialized = true;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public final void setObjectId(int objid, int bip, int sid)
/* 117:    */   {
/* 118:282 */     this.objectId = objid;
/* 119:283 */     this.blipId = bip;
/* 120:284 */     this.shapeId = sid;
/* 121:286 */     if (this.origin == Origin.READ) {
/* 122:288 */       this.origin = Origin.READ_WRITE;
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   public final int getObjectId()
/* 127:    */   {
/* 128:299 */     if (!this.initialized) {
/* 129:301 */       initialize();
/* 130:    */     }
/* 131:304 */     return this.objectId;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public final int getShapeId()
/* 135:    */   {
/* 136:314 */     if (!this.initialized) {
/* 137:316 */       initialize();
/* 138:    */     }
/* 139:319 */     return this.shapeId;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public final int getBlipId()
/* 143:    */   {
/* 144:329 */     if (!this.initialized) {
/* 145:331 */       initialize();
/* 146:    */     }
/* 147:334 */     return this.blipId;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public MsoDrawingRecord getMsoDrawingRecord()
/* 151:    */   {
/* 152:344 */     return this.msoDrawingRecord;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public EscherContainer getSpContainer()
/* 156:    */   {
/* 157:354 */     if (!this.initialized) {
/* 158:356 */       initialize();
/* 159:    */     }
/* 160:359 */     if (this.origin == Origin.READ) {
/* 161:361 */       return getReadSpContainer();
/* 162:    */     }
/* 163:364 */     SpContainer spc = new SpContainer();
/* 164:365 */     Sp sp = new Sp(this.type, this.shapeId, 2560);
/* 165:366 */     spc.add(sp);
/* 166:367 */     Opt opt = new Opt();
/* 167:368 */     opt.addProperty(127, false, false, 17039620);
/* 168:369 */     opt.addProperty(191, false, false, 524296);
/* 169:370 */     opt.addProperty(511, false, false, 524288);
/* 170:371 */     opt.addProperty(959, false, false, 131072);
/* 171:    */     
/* 172:    */ 
/* 173:374 */     spc.add(opt);
/* 174:    */     
/* 175:376 */     ClientAnchor clientAnchor = new ClientAnchor(this.column, 
/* 176:377 */       this.row, 
/* 177:378 */       this.column + 1, 
/* 178:379 */       this.row + 1, 
/* 179:380 */       1);
/* 180:381 */     spc.add(clientAnchor);
/* 181:382 */     ClientData clientData = new ClientData();
/* 182:383 */     spc.add(clientData);
/* 183:    */     
/* 184:385 */     return spc;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void setDrawingGroup(DrawingGroup dg)
/* 188:    */   {
/* 189:396 */     this.drawingGroup = dg;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public DrawingGroup getDrawingGroup()
/* 193:    */   {
/* 194:406 */     return this.drawingGroup;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public Origin getOrigin()
/* 198:    */   {
/* 199:416 */     return this.origin;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public int getReferenceCount()
/* 203:    */   {
/* 204:426 */     return this.referenceCount;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void setReferenceCount(int r)
/* 208:    */   {
/* 209:436 */     this.referenceCount = r;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public double getX()
/* 213:    */   {
/* 214:446 */     if (!this.initialized) {
/* 215:448 */       initialize();
/* 216:    */     }
/* 217:450 */     return this.column;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public void setX(double x)
/* 221:    */   {
/* 222:461 */     if (this.origin == Origin.READ)
/* 223:    */     {
/* 224:463 */       if (!this.initialized) {
/* 225:465 */         initialize();
/* 226:    */       }
/* 227:467 */       this.origin = Origin.READ_WRITE;
/* 228:    */     }
/* 229:470 */     this.column = ((int)x);
/* 230:    */   }
/* 231:    */   
/* 232:    */   public double getY()
/* 233:    */   {
/* 234:480 */     if (!this.initialized) {
/* 235:482 */       initialize();
/* 236:    */     }
/* 237:485 */     return this.row;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public void setY(double y)
/* 241:    */   {
/* 242:495 */     if (this.origin == Origin.READ)
/* 243:    */     {
/* 244:497 */       if (!this.initialized) {
/* 245:499 */         initialize();
/* 246:    */       }
/* 247:501 */       this.origin = Origin.READ_WRITE;
/* 248:    */     }
/* 249:504 */     this.row = ((int)y);
/* 250:    */   }
/* 251:    */   
/* 252:    */   public double getWidth()
/* 253:    */   {
/* 254:515 */     if (!this.initialized) {
/* 255:517 */       initialize();
/* 256:    */     }
/* 257:520 */     return this.width;
/* 258:    */   }
/* 259:    */   
/* 260:    */   public void setWidth(double w)
/* 261:    */   {
/* 262:530 */     if (this.origin == Origin.READ)
/* 263:    */     {
/* 264:532 */       if (!this.initialized) {
/* 265:534 */         initialize();
/* 266:    */       }
/* 267:536 */       this.origin = Origin.READ_WRITE;
/* 268:    */     }
/* 269:539 */     this.width = w;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public double getHeight()
/* 273:    */   {
/* 274:549 */     if (!this.initialized) {
/* 275:551 */       initialize();
/* 276:    */     }
/* 277:554 */     return this.height;
/* 278:    */   }
/* 279:    */   
/* 280:    */   public void setHeight(double h)
/* 281:    */   {
/* 282:564 */     if (this.origin == Origin.READ)
/* 283:    */     {
/* 284:566 */       if (!this.initialized) {
/* 285:568 */         initialize();
/* 286:    */       }
/* 287:570 */       this.origin = Origin.READ_WRITE;
/* 288:    */     }
/* 289:573 */     this.height = h;
/* 290:    */   }
/* 291:    */   
/* 292:    */   private EscherContainer getReadSpContainer()
/* 293:    */   {
/* 294:584 */     if (!this.initialized) {
/* 295:586 */       initialize();
/* 296:    */     }
/* 297:589 */     return this.readSpContainer;
/* 298:    */   }
/* 299:    */   
/* 300:    */   public byte[] getImageData()
/* 301:    */   {
/* 302:599 */     Assert.verify((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE));
/* 303:601 */     if (!this.initialized) {
/* 304:603 */       initialize();
/* 305:    */     }
/* 306:606 */     return this.drawingGroup.getImageData(this.blipId);
/* 307:    */   }
/* 308:    */   
/* 309:    */   public ShapeType getType()
/* 310:    */   {
/* 311:616 */     return this.type;
/* 312:    */   }
/* 313:    */   
/* 314:    */   public byte[] getImageBytes()
/* 315:    */   {
/* 316:626 */     Assert.verify(false);
/* 317:627 */     return null;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public String getImageFilePath()
/* 321:    */   {
/* 322:639 */     Assert.verify(false);
/* 323:640 */     return null;
/* 324:    */   }
/* 325:    */   
/* 326:    */   public void writeAdditionalRecords(File outputFile)
/* 327:    */     throws IOException
/* 328:    */   {
/* 329:651 */     if (this.origin == Origin.READ)
/* 330:    */     {
/* 331:653 */       outputFile.write(this.objRecord);
/* 332:655 */       if (this.mso != null) {
/* 333:657 */         outputFile.write(this.mso);
/* 334:    */       }
/* 335:659 */       outputFile.write(this.txo);
/* 336:660 */       outputFile.write(this.text);
/* 337:661 */       if (this.formatting != null) {
/* 338:663 */         outputFile.write(this.formatting);
/* 339:    */       }
/* 340:665 */       return;
/* 341:    */     }
/* 342:669 */     ObjRecord objrec = new ObjRecord(this.objectId, 
/* 343:670 */       ObjRecord.CHECKBOX);
/* 344:    */     
/* 345:672 */     outputFile.write(objrec);
/* 346:    */     
/* 347:674 */     logger.warn("Writing of additional records for checkboxes not implemented");
/* 348:    */   }
/* 349:    */   
/* 350:    */   public void writeTailRecords(File outputFile) {}
/* 351:    */   
/* 352:    */   public int getRow()
/* 353:    */   {
/* 354:696 */     return 0;
/* 355:    */   }
/* 356:    */   
/* 357:    */   public int getColumn()
/* 358:    */   {
/* 359:706 */     return 0;
/* 360:    */   }
/* 361:    */   
/* 362:    */   public int hashCode()
/* 363:    */   {
/* 364:716 */     return getClass().getName().hashCode();
/* 365:    */   }
/* 366:    */   
/* 367:    */   public boolean isFirst()
/* 368:    */   {
/* 369:728 */     return this.msoDrawingRecord.isFirst();
/* 370:    */   }
/* 371:    */   
/* 372:    */   public boolean isFormObject()
/* 373:    */   {
/* 374:740 */     return false;
/* 375:    */   }
/* 376:    */   
/* 377:    */   public void setTextObject(TextObjectRecord t)
/* 378:    */   {
/* 379:750 */     this.txo = t;
/* 380:    */   }
/* 381:    */   
/* 382:    */   public void setText(ContinueRecord t)
/* 383:    */   {
/* 384:760 */     this.text = t;
/* 385:    */   }
/* 386:    */   
/* 387:    */   public void setFormatting(ContinueRecord t)
/* 388:    */   {
/* 389:770 */     this.formatting = t;
/* 390:    */   }
/* 391:    */   
/* 392:    */   public void addMso(MsoDrawingRecord d)
/* 393:    */   {
/* 394:780 */     this.mso = d;
/* 395:781 */     this.drawingData.addRawData(this.mso.getData());
/* 396:    */   }
/* 397:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.CheckBox
 * JD-Core Version:    0.7.0.1
 */