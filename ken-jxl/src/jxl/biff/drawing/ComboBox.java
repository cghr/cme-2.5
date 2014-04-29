/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.common.Assert;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ import jxl.write.biff.File;
/*   8:    */ 
/*   9:    */ public class ComboBox
/*  10:    */   implements DrawingGroupObject
/*  11:    */ {
/*  12: 39 */   private static Logger logger = Logger.getLogger(ComboBox.class);
/*  13:    */   private EscherContainer readSpContainer;
/*  14:    */   private EscherContainer spContainer;
/*  15:    */   private MsoDrawingRecord msoDrawingRecord;
/*  16:    */   private ObjRecord objRecord;
/*  17: 64 */   private boolean initialized = false;
/*  18:    */   private int objectId;
/*  19:    */   private int blipId;
/*  20:    */   private int shapeId;
/*  21:    */   private int column;
/*  22:    */   private int row;
/*  23:    */   private double width;
/*  24:    */   private double height;
/*  25:    */   private int referenceCount;
/*  26:    */   private EscherContainer escherData;
/*  27:    */   private Origin origin;
/*  28:    */   private DrawingGroup drawingGroup;
/*  29:    */   private DrawingData drawingData;
/*  30:    */   private ShapeType type;
/*  31:    */   private int drawingNumber;
/*  32:    */   private WorkbookSettings workbookSettings;
/*  33:    */   
/*  34:    */   public ComboBox(MsoDrawingRecord mso, ObjRecord obj, DrawingData dd, DrawingGroup dg, WorkbookSettings ws)
/*  35:    */   {
/*  36:153 */     this.drawingGroup = dg;
/*  37:154 */     this.msoDrawingRecord = mso;
/*  38:155 */     this.drawingData = dd;
/*  39:156 */     this.objRecord = obj;
/*  40:157 */     this.initialized = false;
/*  41:158 */     this.workbookSettings = ws;
/*  42:159 */     this.origin = Origin.READ;
/*  43:160 */     this.drawingData.addData(this.msoDrawingRecord.getData());
/*  44:161 */     this.drawingNumber = (this.drawingData.getNumDrawings() - 1);
/*  45:162 */     this.drawingGroup.addDrawing(this);
/*  46:    */     
/*  47:164 */     Assert.verify((mso != null) && (obj != null));
/*  48:    */     
/*  49:166 */     initialize();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public ComboBox(DrawingGroupObject dgo, DrawingGroup dg, WorkbookSettings ws)
/*  53:    */   {
/*  54:180 */     ComboBox d = (ComboBox)dgo;
/*  55:181 */     Assert.verify(d.origin == Origin.READ);
/*  56:182 */     this.msoDrawingRecord = d.msoDrawingRecord;
/*  57:183 */     this.objRecord = d.objRecord;
/*  58:184 */     this.initialized = false;
/*  59:185 */     this.origin = Origin.READ;
/*  60:186 */     this.drawingData = d.drawingData;
/*  61:187 */     this.drawingGroup = dg;
/*  62:188 */     this.drawingNumber = d.drawingNumber;
/*  63:189 */     this.drawingGroup.addDrawing(this);
/*  64:190 */     this.workbookSettings = ws;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public ComboBox()
/*  68:    */   {
/*  69:198 */     this.initialized = true;
/*  70:199 */     this.origin = Origin.WRITE;
/*  71:200 */     this.referenceCount = 1;
/*  72:201 */     this.type = ShapeType.HOST_CONTROL;
/*  73:    */   }
/*  74:    */   
/*  75:    */   private void initialize()
/*  76:    */   {
/*  77:209 */     this.readSpContainer = this.drawingData.getSpContainer(this.drawingNumber);
/*  78:210 */     Assert.verify(this.readSpContainer != null);
/*  79:    */     
/*  80:212 */     EscherRecord[] children = this.readSpContainer.getChildren();
/*  81:    */     
/*  82:214 */     Sp sp = (Sp)this.readSpContainer.getChildren()[0];
/*  83:215 */     this.objectId = this.objRecord.getObjectId();
/*  84:216 */     this.shapeId = sp.getShapeId();
/*  85:217 */     this.type = ShapeType.getType(sp.getShapeType());
/*  86:219 */     if (this.type == ShapeType.UNKNOWN) {
/*  87:221 */       logger.warn("Unknown shape type");
/*  88:    */     }
/*  89:224 */     ClientAnchor clientAnchor = null;
/*  90:225 */     for (int i = 0; (i < children.length) && (clientAnchor == null); i++) {
/*  91:227 */       if (children[i].getType() == EscherRecordType.CLIENT_ANCHOR) {
/*  92:229 */         clientAnchor = (ClientAnchor)children[i];
/*  93:    */       }
/*  94:    */     }
/*  95:233 */     if (clientAnchor == null)
/*  96:    */     {
/*  97:235 */       logger.warn("Client anchor not found");
/*  98:    */     }
/*  99:    */     else
/* 100:    */     {
/* 101:239 */       this.column = ((int)clientAnchor.getX1());
/* 102:240 */       this.row = ((int)clientAnchor.getY1());
/* 103:    */     }
/* 104:243 */     this.initialized = true;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public final void setObjectId(int objid, int bip, int sid)
/* 108:    */   {
/* 109:257 */     this.objectId = objid;
/* 110:258 */     this.blipId = bip;
/* 111:259 */     this.shapeId = sid;
/* 112:261 */     if (this.origin == Origin.READ) {
/* 113:263 */       this.origin = Origin.READ_WRITE;
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public final int getObjectId()
/* 118:    */   {
/* 119:274 */     if (!this.initialized) {
/* 120:276 */       initialize();
/* 121:    */     }
/* 122:279 */     return this.objectId;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public final int getShapeId()
/* 126:    */   {
/* 127:289 */     if (!this.initialized) {
/* 128:291 */       initialize();
/* 129:    */     }
/* 130:294 */     return this.shapeId;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public final int getBlipId()
/* 134:    */   {
/* 135:304 */     if (!this.initialized) {
/* 136:306 */       initialize();
/* 137:    */     }
/* 138:309 */     return this.blipId;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public MsoDrawingRecord getMsoDrawingRecord()
/* 142:    */   {
/* 143:319 */     return this.msoDrawingRecord;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public EscherContainer getSpContainer()
/* 147:    */   {
/* 148:329 */     if (!this.initialized) {
/* 149:331 */       initialize();
/* 150:    */     }
/* 151:334 */     if (this.origin == Origin.READ) {
/* 152:336 */       return getReadSpContainer();
/* 153:    */     }
/* 154:339 */     SpContainer spc = new SpContainer();
/* 155:340 */     Sp sp = new Sp(this.type, this.shapeId, 2560);
/* 156:341 */     spc.add(sp);
/* 157:342 */     Opt opt = new Opt();
/* 158:343 */     opt.addProperty(127, false, false, 17039620);
/* 159:344 */     opt.addProperty(191, false, false, 524296);
/* 160:345 */     opt.addProperty(511, false, false, 524288);
/* 161:346 */     opt.addProperty(959, false, false, 131072);
/* 162:    */     
/* 163:    */ 
/* 164:349 */     spc.add(opt);
/* 165:    */     
/* 166:351 */     ClientAnchor clientAnchor = new ClientAnchor(this.column, 
/* 167:352 */       this.row, 
/* 168:353 */       this.column + 1, 
/* 169:354 */       this.row + 1, 
/* 170:355 */       1);
/* 171:356 */     spc.add(clientAnchor);
/* 172:357 */     ClientData clientData = new ClientData();
/* 173:358 */     spc.add(clientData);
/* 174:    */     
/* 175:360 */     return spc;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void setDrawingGroup(DrawingGroup dg)
/* 179:    */   {
/* 180:371 */     this.drawingGroup = dg;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public DrawingGroup getDrawingGroup()
/* 184:    */   {
/* 185:381 */     return this.drawingGroup;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public Origin getOrigin()
/* 189:    */   {
/* 190:391 */     return this.origin;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public int getReferenceCount()
/* 194:    */   {
/* 195:401 */     return this.referenceCount;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public void setReferenceCount(int r)
/* 199:    */   {
/* 200:411 */     this.referenceCount = r;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public double getX()
/* 204:    */   {
/* 205:421 */     if (!this.initialized) {
/* 206:423 */       initialize();
/* 207:    */     }
/* 208:425 */     return this.column;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void setX(double x)
/* 212:    */   {
/* 213:436 */     if (this.origin == Origin.READ)
/* 214:    */     {
/* 215:438 */       if (!this.initialized) {
/* 216:440 */         initialize();
/* 217:    */       }
/* 218:442 */       this.origin = Origin.READ_WRITE;
/* 219:    */     }
/* 220:445 */     this.column = ((int)x);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public double getY()
/* 224:    */   {
/* 225:455 */     if (!this.initialized) {
/* 226:457 */       initialize();
/* 227:    */     }
/* 228:460 */     return this.row;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void setY(double y)
/* 232:    */   {
/* 233:470 */     if (this.origin == Origin.READ)
/* 234:    */     {
/* 235:472 */       if (!this.initialized) {
/* 236:474 */         initialize();
/* 237:    */       }
/* 238:476 */       this.origin = Origin.READ_WRITE;
/* 239:    */     }
/* 240:479 */     this.row = ((int)y);
/* 241:    */   }
/* 242:    */   
/* 243:    */   public double getWidth()
/* 244:    */   {
/* 245:490 */     if (!this.initialized) {
/* 246:492 */       initialize();
/* 247:    */     }
/* 248:495 */     return this.width;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public void setWidth(double w)
/* 252:    */   {
/* 253:505 */     if (this.origin == Origin.READ)
/* 254:    */     {
/* 255:507 */       if (!this.initialized) {
/* 256:509 */         initialize();
/* 257:    */       }
/* 258:511 */       this.origin = Origin.READ_WRITE;
/* 259:    */     }
/* 260:514 */     this.width = w;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public double getHeight()
/* 264:    */   {
/* 265:524 */     if (!this.initialized) {
/* 266:526 */       initialize();
/* 267:    */     }
/* 268:529 */     return this.height;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public void setHeight(double h)
/* 272:    */   {
/* 273:539 */     if (this.origin == Origin.READ)
/* 274:    */     {
/* 275:541 */       if (!this.initialized) {
/* 276:543 */         initialize();
/* 277:    */       }
/* 278:545 */       this.origin = Origin.READ_WRITE;
/* 279:    */     }
/* 280:548 */     this.height = h;
/* 281:    */   }
/* 282:    */   
/* 283:    */   private EscherContainer getReadSpContainer()
/* 284:    */   {
/* 285:559 */     if (!this.initialized) {
/* 286:561 */       initialize();
/* 287:    */     }
/* 288:564 */     return this.readSpContainer;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public byte[] getImageData()
/* 292:    */   {
/* 293:574 */     Assert.verify((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE));
/* 294:576 */     if (!this.initialized) {
/* 295:578 */       initialize();
/* 296:    */     }
/* 297:581 */     return this.drawingGroup.getImageData(this.blipId);
/* 298:    */   }
/* 299:    */   
/* 300:    */   public ShapeType getType()
/* 301:    */   {
/* 302:591 */     return this.type;
/* 303:    */   }
/* 304:    */   
/* 305:    */   public byte[] getImageBytes()
/* 306:    */   {
/* 307:601 */     Assert.verify(false);
/* 308:602 */     return null;
/* 309:    */   }
/* 310:    */   
/* 311:    */   public String getImageFilePath()
/* 312:    */   {
/* 313:614 */     Assert.verify(false);
/* 314:615 */     return null;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public void writeAdditionalRecords(File outputFile)
/* 318:    */     throws IOException
/* 319:    */   {
/* 320:626 */     if (this.origin == Origin.READ)
/* 321:    */     {
/* 322:628 */       outputFile.write(this.objRecord);
/* 323:629 */       return;
/* 324:    */     }
/* 325:633 */     ObjRecord objrec = new ObjRecord(this.objectId, 
/* 326:634 */       ObjRecord.COMBOBOX);
/* 327:    */     
/* 328:636 */     outputFile.write(objrec);
/* 329:    */   }
/* 330:    */   
/* 331:    */   public void writeTailRecords(File outputFile) {}
/* 332:    */   
/* 333:    */   public int getRow()
/* 334:    */   {
/* 335:657 */     return 0;
/* 336:    */   }
/* 337:    */   
/* 338:    */   public int getColumn()
/* 339:    */   {
/* 340:667 */     return 0;
/* 341:    */   }
/* 342:    */   
/* 343:    */   public int hashCode()
/* 344:    */   {
/* 345:677 */     return getClass().getName().hashCode();
/* 346:    */   }
/* 347:    */   
/* 348:    */   public boolean isFirst()
/* 349:    */   {
/* 350:689 */     return this.msoDrawingRecord.isFirst();
/* 351:    */   }
/* 352:    */   
/* 353:    */   public boolean isFormObject()
/* 354:    */   {
/* 355:701 */     return false;
/* 356:    */   }
/* 357:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.ComboBox
 * JD-Core Version:    0.7.0.1
 */