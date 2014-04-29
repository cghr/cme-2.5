/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.io.FileInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import jxl.common.Assert;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ public class Drawing2
/*   9:    */   implements DrawingGroupObject
/*  10:    */ {
/*  11: 41 */   private static Logger logger = Logger.getLogger(Drawing.class);
/*  12:    */   private EscherContainer readSpContainer;
/*  13:    */   private MsoDrawingRecord msoDrawingRecord;
/*  14: 56 */   private boolean initialized = false;
/*  15:    */   private java.io.File imageFile;
/*  16:    */   private byte[] imageData;
/*  17:    */   private int objectId;
/*  18:    */   private int blipId;
/*  19:    */   private double x;
/*  20:    */   private double y;
/*  21:    */   private double width;
/*  22:    */   private double height;
/*  23:    */   private int referenceCount;
/*  24:    */   private EscherContainer escherData;
/*  25:    */   private Origin origin;
/*  26:    */   private DrawingGroup drawingGroup;
/*  27:    */   private DrawingData drawingData;
/*  28:    */   private ShapeType type;
/*  29:    */   private int shapeId;
/*  30:    */   private int drawingNumber;
/*  31:    */   
/*  32:    */   public Drawing2(MsoDrawingRecord mso, DrawingData dd, DrawingGroup dg)
/*  33:    */   {
/*  34:150 */     this.drawingGroup = dg;
/*  35:151 */     this.msoDrawingRecord = mso;
/*  36:152 */     this.drawingData = dd;
/*  37:153 */     this.initialized = false;
/*  38:154 */     this.origin = Origin.READ;
/*  39:    */     
/*  40:156 */     this.drawingData.addRawData(this.msoDrawingRecord.getData());
/*  41:157 */     this.drawingGroup.addDrawing(this);
/*  42:    */     
/*  43:159 */     Assert.verify(mso != null);
/*  44:    */     
/*  45:161 */     initialize();
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected Drawing2(DrawingGroupObject dgo, DrawingGroup dg)
/*  49:    */   {
/*  50:172 */     Drawing2 d = (Drawing2)dgo;
/*  51:173 */     Assert.verify(d.origin == Origin.READ);
/*  52:174 */     this.msoDrawingRecord = d.msoDrawingRecord;
/*  53:175 */     this.initialized = false;
/*  54:176 */     this.origin = Origin.READ;
/*  55:177 */     this.drawingData = d.drawingData;
/*  56:178 */     this.drawingGroup = dg;
/*  57:179 */     this.drawingNumber = d.drawingNumber;
/*  58:180 */     this.drawingGroup.addDrawing(this);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Drawing2(double x, double y, double w, double h, java.io.File image)
/*  62:    */   {
/*  63:198 */     this.imageFile = image;
/*  64:199 */     this.initialized = true;
/*  65:200 */     this.origin = Origin.WRITE;
/*  66:201 */     this.x = x;
/*  67:202 */     this.y = y;
/*  68:203 */     this.width = w;
/*  69:204 */     this.height = h;
/*  70:205 */     this.referenceCount = 1;
/*  71:206 */     this.type = ShapeType.PICTURE_FRAME;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Drawing2(double x, double y, double w, double h, byte[] image)
/*  75:    */   {
/*  76:224 */     this.imageData = image;
/*  77:225 */     this.initialized = true;
/*  78:226 */     this.origin = Origin.WRITE;
/*  79:227 */     this.x = x;
/*  80:228 */     this.y = y;
/*  81:229 */     this.width = w;
/*  82:230 */     this.height = h;
/*  83:231 */     this.referenceCount = 1;
/*  84:232 */     this.type = ShapeType.PICTURE_FRAME;
/*  85:    */   }
/*  86:    */   
/*  87:    */   private void initialize()
/*  88:    */   {
/*  89:240 */     this.initialized = true;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public final void setObjectId(int objid, int bip, int sid)
/*  93:    */   {
/*  94:253 */     this.objectId = objid;
/*  95:254 */     this.blipId = bip;
/*  96:255 */     this.shapeId = sid;
/*  97:257 */     if (this.origin == Origin.READ) {
/*  98:259 */       this.origin = Origin.READ_WRITE;
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   public final int getObjectId()
/* 103:    */   {
/* 104:270 */     if (!this.initialized) {
/* 105:272 */       initialize();
/* 106:    */     }
/* 107:275 */     return this.objectId;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public int getShapeId()
/* 111:    */   {
/* 112:285 */     if (!this.initialized) {
/* 113:287 */       initialize();
/* 114:    */     }
/* 115:290 */     return this.shapeId;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public final int getBlipId()
/* 119:    */   {
/* 120:300 */     if (!this.initialized) {
/* 121:302 */       initialize();
/* 122:    */     }
/* 123:305 */     return this.blipId;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public MsoDrawingRecord getMsoDrawingRecord()
/* 127:    */   {
/* 128:315 */     return this.msoDrawingRecord;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public EscherContainer getSpContainer()
/* 132:    */   {
/* 133:325 */     if (!this.initialized) {
/* 134:327 */       initialize();
/* 135:    */     }
/* 136:330 */     Assert.verify(this.origin == Origin.READ);
/* 137:    */     
/* 138:332 */     return getReadSpContainer();
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setDrawingGroup(DrawingGroup dg)
/* 142:    */   {
/* 143:343 */     this.drawingGroup = dg;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public DrawingGroup getDrawingGroup()
/* 147:    */   {
/* 148:353 */     return this.drawingGroup;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public Origin getOrigin()
/* 152:    */   {
/* 153:363 */     return this.origin;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public int getReferenceCount()
/* 157:    */   {
/* 158:373 */     return this.referenceCount;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void setReferenceCount(int r)
/* 162:    */   {
/* 163:383 */     this.referenceCount = r;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public double getX()
/* 167:    */   {
/* 168:393 */     if (!this.initialized) {
/* 169:395 */       initialize();
/* 170:    */     }
/* 171:397 */     return this.x;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public void setX(double x)
/* 175:    */   {
/* 176:407 */     if (this.origin == Origin.READ)
/* 177:    */     {
/* 178:409 */       if (!this.initialized) {
/* 179:411 */         initialize();
/* 180:    */       }
/* 181:413 */       this.origin = Origin.READ_WRITE;
/* 182:    */     }
/* 183:416 */     this.x = x;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public double getY()
/* 187:    */   {
/* 188:426 */     if (!this.initialized) {
/* 189:428 */       initialize();
/* 190:    */     }
/* 191:431 */     return this.y;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public void setY(double y)
/* 195:    */   {
/* 196:441 */     if (this.origin == Origin.READ)
/* 197:    */     {
/* 198:443 */       if (!this.initialized) {
/* 199:445 */         initialize();
/* 200:    */       }
/* 201:447 */       this.origin = Origin.READ_WRITE;
/* 202:    */     }
/* 203:450 */     this.y = y;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public double getWidth()
/* 207:    */   {
/* 208:461 */     if (!this.initialized) {
/* 209:463 */       initialize();
/* 210:    */     }
/* 211:466 */     return this.width;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public void setWidth(double w)
/* 215:    */   {
/* 216:476 */     if (this.origin == Origin.READ)
/* 217:    */     {
/* 218:478 */       if (!this.initialized) {
/* 219:480 */         initialize();
/* 220:    */       }
/* 221:482 */       this.origin = Origin.READ_WRITE;
/* 222:    */     }
/* 223:485 */     this.width = w;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public double getHeight()
/* 227:    */   {
/* 228:495 */     if (!this.initialized) {
/* 229:497 */       initialize();
/* 230:    */     }
/* 231:500 */     return this.height;
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void setHeight(double h)
/* 235:    */   {
/* 236:510 */     if (this.origin == Origin.READ)
/* 237:    */     {
/* 238:512 */       if (!this.initialized) {
/* 239:514 */         initialize();
/* 240:    */       }
/* 241:516 */       this.origin = Origin.READ_WRITE;
/* 242:    */     }
/* 243:519 */     this.height = h;
/* 244:    */   }
/* 245:    */   
/* 246:    */   private EscherContainer getReadSpContainer()
/* 247:    */   {
/* 248:530 */     if (!this.initialized) {
/* 249:532 */       initialize();
/* 250:    */     }
/* 251:535 */     return this.readSpContainer;
/* 252:    */   }
/* 253:    */   
/* 254:    */   public byte[] getImageData()
/* 255:    */   {
/* 256:545 */     Assert.verify(false);
/* 257:546 */     Assert.verify((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE));
/* 258:548 */     if (!this.initialized) {
/* 259:550 */       initialize();
/* 260:    */     }
/* 261:553 */     return this.drawingGroup.getImageData(this.blipId);
/* 262:    */   }
/* 263:    */   
/* 264:    */   public byte[] getImageBytes()
/* 265:    */     throws IOException
/* 266:    */   {
/* 267:563 */     Assert.verify(false);
/* 268:564 */     if ((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE)) {
/* 269:566 */       return getImageData();
/* 270:    */     }
/* 271:569 */     Assert.verify(this.origin == Origin.WRITE);
/* 272:571 */     if (this.imageFile == null)
/* 273:    */     {
/* 274:573 */       Assert.verify(this.imageData != null);
/* 275:574 */       return this.imageData;
/* 276:    */     }
/* 277:577 */     byte[] data = new byte[(int)this.imageFile.length()];
/* 278:578 */     FileInputStream fis = new FileInputStream(this.imageFile);
/* 279:579 */     fis.read(data, 0, data.length);
/* 280:580 */     fis.close();
/* 281:581 */     return data;
/* 282:    */   }
/* 283:    */   
/* 284:    */   public ShapeType getType()
/* 285:    */   {
/* 286:591 */     return this.type;
/* 287:    */   }
/* 288:    */   
/* 289:    */   public void writeAdditionalRecords(jxl.write.biff.File outputFile)
/* 290:    */     throws IOException
/* 291:    */   {}
/* 292:    */   
/* 293:    */   public void writeTailRecords(jxl.write.biff.File outputFile)
/* 294:    */     throws IOException
/* 295:    */   {}
/* 296:    */   
/* 297:    */   public double getColumn()
/* 298:    */   {
/* 299:625 */     return getX();
/* 300:    */   }
/* 301:    */   
/* 302:    */   public double getRow()
/* 303:    */   {
/* 304:635 */     return getY();
/* 305:    */   }
/* 306:    */   
/* 307:    */   public boolean isFirst()
/* 308:    */   {
/* 309:647 */     return this.msoDrawingRecord.isFirst();
/* 310:    */   }
/* 311:    */   
/* 312:    */   public boolean isFormObject()
/* 313:    */   {
/* 314:659 */     return false;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public void removeRow(int r)
/* 318:    */   {
/* 319:669 */     if (this.y > r) {
/* 320:671 */       setY(r);
/* 321:    */     }
/* 322:    */   }
/* 323:    */   
/* 324:    */   public String getImageFilePath()
/* 325:    */   {
/* 326:684 */     Assert.verify(false);
/* 327:685 */     return null;
/* 328:    */   }
/* 329:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Drawing2
 * JD-Core Version:    0.7.0.1
 */