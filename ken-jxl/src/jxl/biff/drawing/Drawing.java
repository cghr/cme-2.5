/*    1:     */ package jxl.biff.drawing;
/*    2:     */ 
/*    3:     */ import java.io.FileInputStream;
/*    4:     */ import java.io.IOException;
/*    5:     */ import jxl.CellView;
/*    6:     */ import jxl.Image;
/*    7:     */ import jxl.Sheet;
/*    8:     */ import jxl.common.Assert;
/*    9:     */ import jxl.common.LengthConverter;
/*   10:     */ import jxl.common.LengthUnit;
/*   11:     */ import jxl.common.Logger;
/*   12:     */ import jxl.format.CellFormat;
/*   13:     */ import jxl.format.Font;
/*   14:     */ 
/*   15:     */ public class Drawing
/*   16:     */   implements DrawingGroupObject, Image
/*   17:     */ {
/*   18:  45 */   private static Logger logger = Logger.getLogger(Drawing.class);
/*   19:     */   private EscherContainer readSpContainer;
/*   20:     */   private MsoDrawingRecord msoDrawingRecord;
/*   21:     */   private ObjRecord objRecord;
/*   22:  65 */   private boolean initialized = false;
/*   23:     */   private java.io.File imageFile;
/*   24:     */   private byte[] imageData;
/*   25:     */   private int objectId;
/*   26:     */   private int blipId;
/*   27:     */   private double x;
/*   28:     */   private double y;
/*   29:     */   private double width;
/*   30:     */   private double height;
/*   31:     */   private int referenceCount;
/*   32:     */   private EscherContainer escherData;
/*   33:     */   private Origin origin;
/*   34:     */   private DrawingGroup drawingGroup;
/*   35:     */   private DrawingData drawingData;
/*   36:     */   private ShapeType type;
/*   37:     */   private int shapeId;
/*   38:     */   private int drawingNumber;
/*   39:     */   private Sheet sheet;
/*   40:     */   private PNGReader pngReader;
/*   41:     */   private ImageAnchorProperties imageAnchorProperties;
/*   42:     */   
/*   43:     */   protected static class ImageAnchorProperties
/*   44:     */   {
/*   45:     */     private int value;
/*   46: 167 */     private static ImageAnchorProperties[] o = new ImageAnchorProperties[0];
/*   47:     */     
/*   48:     */     ImageAnchorProperties(int v)
/*   49:     */     {
/*   50: 171 */       this.value = v;
/*   51:     */       
/*   52: 173 */       ImageAnchorProperties[] oldArray = o;
/*   53: 174 */       o = new ImageAnchorProperties[oldArray.length + 1];
/*   54: 175 */       System.arraycopy(oldArray, 0, o, 0, oldArray.length);
/*   55: 176 */       o[oldArray.length] = this;
/*   56:     */     }
/*   57:     */     
/*   58:     */     int getValue()
/*   59:     */     {
/*   60: 181 */       return this.value;
/*   61:     */     }
/*   62:     */     
/*   63:     */     static ImageAnchorProperties getImageAnchorProperties(int val)
/*   64:     */     {
/*   65: 186 */       ImageAnchorProperties iap = Drawing.MOVE_AND_SIZE_WITH_CELLS;
/*   66: 187 */       int pos = 0;
/*   67: 188 */       while (pos < o.length)
/*   68:     */       {
/*   69: 190 */         if (o[pos].getValue() == val)
/*   70:     */         {
/*   71: 192 */           iap = o[pos];
/*   72: 193 */           break;
/*   73:     */         }
/*   74: 197 */         pos++;
/*   75:     */       }
/*   76: 200 */       return iap;
/*   77:     */     }
/*   78:     */   }
/*   79:     */   
/*   80: 206 */   public static ImageAnchorProperties MOVE_AND_SIZE_WITH_CELLS = new ImageAnchorProperties(1);
/*   81: 208 */   public static ImageAnchorProperties MOVE_WITH_CELLS = new ImageAnchorProperties(2);
/*   82: 210 */   public static ImageAnchorProperties NO_MOVE_OR_SIZE_WITH_CELLS = new ImageAnchorProperties(3);
/*   83:     */   private static final double DEFAULT_FONT_SIZE = 10.0D;
/*   84:     */   
/*   85:     */   public Drawing(MsoDrawingRecord mso, ObjRecord obj, DrawingData dd, DrawingGroup dg, Sheet s)
/*   86:     */   {
/*   87: 231 */     this.drawingGroup = dg;
/*   88: 232 */     this.msoDrawingRecord = mso;
/*   89: 233 */     this.drawingData = dd;
/*   90: 234 */     this.objRecord = obj;
/*   91: 235 */     this.sheet = s;
/*   92: 236 */     this.initialized = false;
/*   93: 237 */     this.origin = Origin.READ;
/*   94: 238 */     this.drawingData.addData(this.msoDrawingRecord.getData());
/*   95: 239 */     this.drawingNumber = (this.drawingData.getNumDrawings() - 1);
/*   96: 240 */     this.drawingGroup.addDrawing(this);
/*   97:     */     
/*   98: 242 */     Assert.verify((mso != null) && (obj != null));
/*   99:     */     
/*  100: 244 */     initialize();
/*  101:     */   }
/*  102:     */   
/*  103:     */   protected Drawing(DrawingGroupObject dgo, DrawingGroup dg)
/*  104:     */   {
/*  105: 255 */     Drawing d = (Drawing)dgo;
/*  106: 256 */     Assert.verify(d.origin == Origin.READ);
/*  107: 257 */     this.msoDrawingRecord = d.msoDrawingRecord;
/*  108: 258 */     this.objRecord = d.objRecord;
/*  109: 259 */     this.initialized = false;
/*  110: 260 */     this.origin = Origin.READ;
/*  111: 261 */     this.drawingData = d.drawingData;
/*  112: 262 */     this.drawingGroup = dg;
/*  113: 263 */     this.drawingNumber = d.drawingNumber;
/*  114: 264 */     this.drawingGroup.addDrawing(this);
/*  115:     */   }
/*  116:     */   
/*  117:     */   public Drawing(double x, double y, double w, double h, java.io.File image)
/*  118:     */   {
/*  119: 282 */     this.imageFile = image;
/*  120: 283 */     this.initialized = true;
/*  121: 284 */     this.origin = Origin.WRITE;
/*  122: 285 */     this.x = x;
/*  123: 286 */     this.y = y;
/*  124: 287 */     this.width = w;
/*  125: 288 */     this.height = h;
/*  126: 289 */     this.referenceCount = 1;
/*  127: 290 */     this.imageAnchorProperties = MOVE_WITH_CELLS;
/*  128: 291 */     this.type = ShapeType.PICTURE_FRAME;
/*  129:     */   }
/*  130:     */   
/*  131:     */   public Drawing(double x, double y, double w, double h, byte[] image)
/*  132:     */   {
/*  133: 309 */     this.imageData = image;
/*  134: 310 */     this.initialized = true;
/*  135: 311 */     this.origin = Origin.WRITE;
/*  136: 312 */     this.x = x;
/*  137: 313 */     this.y = y;
/*  138: 314 */     this.width = w;
/*  139: 315 */     this.height = h;
/*  140: 316 */     this.referenceCount = 1;
/*  141: 317 */     this.imageAnchorProperties = MOVE_WITH_CELLS;
/*  142: 318 */     this.type = ShapeType.PICTURE_FRAME;
/*  143:     */   }
/*  144:     */   
/*  145:     */   private void initialize()
/*  146:     */   {
/*  147: 326 */     this.readSpContainer = this.drawingData.getSpContainer(this.drawingNumber);
/*  148: 327 */     Assert.verify(this.readSpContainer != null);
/*  149:     */     
/*  150: 329 */     EscherRecord[] children = this.readSpContainer.getChildren();
/*  151:     */     
/*  152: 331 */     Sp sp = (Sp)this.readSpContainer.getChildren()[0];
/*  153: 332 */     this.shapeId = sp.getShapeId();
/*  154: 333 */     this.objectId = this.objRecord.getObjectId();
/*  155: 334 */     this.type = ShapeType.getType(sp.getShapeType());
/*  156: 336 */     if (this.type == ShapeType.UNKNOWN) {
/*  157: 338 */       logger.warn("Unknown shape type");
/*  158:     */     }
/*  159: 341 */     Opt opt = (Opt)this.readSpContainer.getChildren()[1];
/*  160: 343 */     if (opt.getProperty(260) != null) {
/*  161: 345 */       this.blipId = opt.getProperty(260).value;
/*  162:     */     }
/*  163: 348 */     if (opt.getProperty(261) != null)
/*  164:     */     {
/*  165: 350 */       this.imageFile = new java.io.File(opt.getProperty(261).stringValue);
/*  166:     */     }
/*  167: 354 */     else if (this.type == ShapeType.PICTURE_FRAME)
/*  168:     */     {
/*  169: 356 */       logger.warn("no filename property for drawing");
/*  170: 357 */       this.imageFile = new java.io.File(Integer.toString(this.blipId));
/*  171:     */     }
/*  172: 361 */     ClientAnchor clientAnchor = null;
/*  173: 362 */     for (int i = 0; (i < children.length) && (clientAnchor == null); i++) {
/*  174: 364 */       if (children[i].getType() == EscherRecordType.CLIENT_ANCHOR) {
/*  175: 366 */         clientAnchor = (ClientAnchor)children[i];
/*  176:     */       }
/*  177:     */     }
/*  178: 370 */     if (clientAnchor == null)
/*  179:     */     {
/*  180: 372 */       logger.warn("client anchor not found");
/*  181:     */     }
/*  182:     */     else
/*  183:     */     {
/*  184: 376 */       this.x = clientAnchor.getX1();
/*  185: 377 */       this.y = clientAnchor.getY1();
/*  186: 378 */       this.width = (clientAnchor.getX2() - this.x);
/*  187: 379 */       this.height = (clientAnchor.getY2() - this.y);
/*  188: 380 */       this.imageAnchorProperties = ImageAnchorProperties.getImageAnchorProperties(
/*  189: 381 */         clientAnchor.getProperties());
/*  190:     */     }
/*  191: 384 */     if (this.blipId == 0) {
/*  192: 386 */       logger.warn("linked drawings are not supported");
/*  193:     */     }
/*  194: 389 */     this.initialized = true;
/*  195:     */   }
/*  196:     */   
/*  197:     */   public java.io.File getImageFile()
/*  198:     */   {
/*  199: 399 */     return this.imageFile;
/*  200:     */   }
/*  201:     */   
/*  202:     */   public String getImageFilePath()
/*  203:     */   {
/*  204: 411 */     if (this.imageFile == null) {
/*  205: 414 */       return this.blipId != 0 ? Integer.toString(this.blipId) : "__new__image__";
/*  206:     */     }
/*  207: 417 */     return this.imageFile.getPath();
/*  208:     */   }
/*  209:     */   
/*  210:     */   public final void setObjectId(int objid, int bip, int sid)
/*  211:     */   {
/*  212: 430 */     this.objectId = objid;
/*  213: 431 */     this.blipId = bip;
/*  214: 432 */     this.shapeId = sid;
/*  215: 434 */     if (this.origin == Origin.READ) {
/*  216: 436 */       this.origin = Origin.READ_WRITE;
/*  217:     */     }
/*  218:     */   }
/*  219:     */   
/*  220:     */   public final int getObjectId()
/*  221:     */   {
/*  222: 447 */     if (!this.initialized) {
/*  223: 449 */       initialize();
/*  224:     */     }
/*  225: 452 */     return this.objectId;
/*  226:     */   }
/*  227:     */   
/*  228:     */   public int getShapeId()
/*  229:     */   {
/*  230: 462 */     if (!this.initialized) {
/*  231: 464 */       initialize();
/*  232:     */     }
/*  233: 467 */     return this.shapeId;
/*  234:     */   }
/*  235:     */   
/*  236:     */   public final int getBlipId()
/*  237:     */   {
/*  238: 477 */     if (!this.initialized) {
/*  239: 479 */       initialize();
/*  240:     */     }
/*  241: 482 */     return this.blipId;
/*  242:     */   }
/*  243:     */   
/*  244:     */   public MsoDrawingRecord getMsoDrawingRecord()
/*  245:     */   {
/*  246: 492 */     return this.msoDrawingRecord;
/*  247:     */   }
/*  248:     */   
/*  249:     */   public EscherContainer getSpContainer()
/*  250:     */   {
/*  251: 502 */     if (!this.initialized) {
/*  252: 504 */       initialize();
/*  253:     */     }
/*  254: 507 */     if (this.origin == Origin.READ) {
/*  255: 509 */       return getReadSpContainer();
/*  256:     */     }
/*  257: 512 */     SpContainer spContainer = new SpContainer();
/*  258: 513 */     Sp sp = new Sp(this.type, this.shapeId, 2560);
/*  259: 514 */     spContainer.add(sp);
/*  260: 515 */     Opt opt = new Opt();
/*  261: 516 */     opt.addProperty(260, true, false, this.blipId);
/*  262: 518 */     if (this.type == ShapeType.PICTURE_FRAME)
/*  263:     */     {
/*  264: 520 */       String filePath = this.imageFile != null ? this.imageFile.getPath() : "";
/*  265: 521 */       opt.addProperty(261, true, true, filePath.length() * 2, filePath);
/*  266: 522 */       opt.addProperty(447, false, false, 65536);
/*  267: 523 */       opt.addProperty(959, false, false, 524288);
/*  268: 524 */       spContainer.add(opt);
/*  269:     */     }
/*  270: 527 */     ClientAnchor clientAnchor = new ClientAnchor(
/*  271: 528 */       this.x, this.y, this.x + this.width, this.y + this.height, 
/*  272: 529 */       this.imageAnchorProperties.getValue());
/*  273: 530 */     spContainer.add(clientAnchor);
/*  274: 531 */     ClientData clientData = new ClientData();
/*  275: 532 */     spContainer.add(clientData);
/*  276:     */     
/*  277: 534 */     return spContainer;
/*  278:     */   }
/*  279:     */   
/*  280:     */   public void setDrawingGroup(DrawingGroup dg)
/*  281:     */   {
/*  282: 545 */     this.drawingGroup = dg;
/*  283:     */   }
/*  284:     */   
/*  285:     */   public DrawingGroup getDrawingGroup()
/*  286:     */   {
/*  287: 555 */     return this.drawingGroup;
/*  288:     */   }
/*  289:     */   
/*  290:     */   public Origin getOrigin()
/*  291:     */   {
/*  292: 565 */     return this.origin;
/*  293:     */   }
/*  294:     */   
/*  295:     */   public int getReferenceCount()
/*  296:     */   {
/*  297: 575 */     return this.referenceCount;
/*  298:     */   }
/*  299:     */   
/*  300:     */   public void setReferenceCount(int r)
/*  301:     */   {
/*  302: 585 */     this.referenceCount = r;
/*  303:     */   }
/*  304:     */   
/*  305:     */   public double getX()
/*  306:     */   {
/*  307: 595 */     if (!this.initialized) {
/*  308: 597 */       initialize();
/*  309:     */     }
/*  310: 599 */     return this.x;
/*  311:     */   }
/*  312:     */   
/*  313:     */   public void setX(double x)
/*  314:     */   {
/*  315: 609 */     if (this.origin == Origin.READ)
/*  316:     */     {
/*  317: 611 */       if (!this.initialized) {
/*  318: 613 */         initialize();
/*  319:     */       }
/*  320: 615 */       this.origin = Origin.READ_WRITE;
/*  321:     */     }
/*  322: 618 */     this.x = x;
/*  323:     */   }
/*  324:     */   
/*  325:     */   public double getY()
/*  326:     */   {
/*  327: 628 */     if (!this.initialized) {
/*  328: 630 */       initialize();
/*  329:     */     }
/*  330: 633 */     return this.y;
/*  331:     */   }
/*  332:     */   
/*  333:     */   public void setY(double y)
/*  334:     */   {
/*  335: 643 */     if (this.origin == Origin.READ)
/*  336:     */     {
/*  337: 645 */       if (!this.initialized) {
/*  338: 647 */         initialize();
/*  339:     */       }
/*  340: 649 */       this.origin = Origin.READ_WRITE;
/*  341:     */     }
/*  342: 652 */     this.y = y;
/*  343:     */   }
/*  344:     */   
/*  345:     */   public double getWidth()
/*  346:     */   {
/*  347: 663 */     if (!this.initialized) {
/*  348: 665 */       initialize();
/*  349:     */     }
/*  350: 668 */     return this.width;
/*  351:     */   }
/*  352:     */   
/*  353:     */   public void setWidth(double w)
/*  354:     */   {
/*  355: 678 */     if (this.origin == Origin.READ)
/*  356:     */     {
/*  357: 680 */       if (!this.initialized) {
/*  358: 682 */         initialize();
/*  359:     */       }
/*  360: 684 */       this.origin = Origin.READ_WRITE;
/*  361:     */     }
/*  362: 687 */     this.width = w;
/*  363:     */   }
/*  364:     */   
/*  365:     */   public double getHeight()
/*  366:     */   {
/*  367: 697 */     if (!this.initialized) {
/*  368: 699 */       initialize();
/*  369:     */     }
/*  370: 702 */     return this.height;
/*  371:     */   }
/*  372:     */   
/*  373:     */   public void setHeight(double h)
/*  374:     */   {
/*  375: 712 */     if (this.origin == Origin.READ)
/*  376:     */     {
/*  377: 714 */       if (!this.initialized) {
/*  378: 716 */         initialize();
/*  379:     */       }
/*  380: 718 */       this.origin = Origin.READ_WRITE;
/*  381:     */     }
/*  382: 721 */     this.height = h;
/*  383:     */   }
/*  384:     */   
/*  385:     */   private EscherContainer getReadSpContainer()
/*  386:     */   {
/*  387: 732 */     if (!this.initialized) {
/*  388: 734 */       initialize();
/*  389:     */     }
/*  390: 737 */     return this.readSpContainer;
/*  391:     */   }
/*  392:     */   
/*  393:     */   public byte[] getImageData()
/*  394:     */   {
/*  395: 747 */     Assert.verify((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE));
/*  396: 749 */     if (!this.initialized) {
/*  397: 751 */       initialize();
/*  398:     */     }
/*  399: 754 */     return this.drawingGroup.getImageData(this.blipId);
/*  400:     */   }
/*  401:     */   
/*  402:     */   public byte[] getImageBytes()
/*  403:     */     throws IOException
/*  404:     */   {
/*  405: 764 */     if ((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE)) {
/*  406: 766 */       return getImageData();
/*  407:     */     }
/*  408: 769 */     Assert.verify(this.origin == Origin.WRITE);
/*  409: 771 */     if (this.imageFile == null)
/*  410:     */     {
/*  411: 773 */       Assert.verify(this.imageData != null);
/*  412: 774 */       return this.imageData;
/*  413:     */     }
/*  414: 777 */     byte[] data = new byte[(int)this.imageFile.length()];
/*  415: 778 */     FileInputStream fis = new FileInputStream(this.imageFile);
/*  416: 779 */     fis.read(data, 0, data.length);
/*  417: 780 */     fis.close();
/*  418: 781 */     return data;
/*  419:     */   }
/*  420:     */   
/*  421:     */   public ShapeType getType()
/*  422:     */   {
/*  423: 791 */     return this.type;
/*  424:     */   }
/*  425:     */   
/*  426:     */   public void writeAdditionalRecords(jxl.write.biff.File outputFile)
/*  427:     */     throws IOException
/*  428:     */   {
/*  429: 802 */     if (this.origin == Origin.READ)
/*  430:     */     {
/*  431: 804 */       outputFile.write(this.objRecord);
/*  432: 805 */       return;
/*  433:     */     }
/*  434: 809 */     ObjRecord objrec = new ObjRecord(this.objectId, 
/*  435: 810 */       ObjRecord.PICTURE);
/*  436: 811 */     outputFile.write(objrec);
/*  437:     */   }
/*  438:     */   
/*  439:     */   public void writeTailRecords(jxl.write.biff.File outputFile)
/*  440:     */     throws IOException
/*  441:     */   {}
/*  442:     */   
/*  443:     */   public double getColumn()
/*  444:     */   {
/*  445: 833 */     return getX();
/*  446:     */   }
/*  447:     */   
/*  448:     */   public double getRow()
/*  449:     */   {
/*  450: 843 */     return getY();
/*  451:     */   }
/*  452:     */   
/*  453:     */   public boolean isFirst()
/*  454:     */   {
/*  455: 855 */     return this.msoDrawingRecord.isFirst();
/*  456:     */   }
/*  457:     */   
/*  458:     */   public boolean isFormObject()
/*  459:     */   {
/*  460: 867 */     return false;
/*  461:     */   }
/*  462:     */   
/*  463:     */   public void removeRow(int r)
/*  464:     */   {
/*  465: 877 */     if (this.y > r) {
/*  466: 879 */       setY(r);
/*  467:     */     }
/*  468:     */   }
/*  469:     */   
/*  470:     */   private double getWidthInPoints()
/*  471:     */   {
/*  472: 891 */     if (this.sheet == null)
/*  473:     */     {
/*  474: 893 */       logger.warn("calculating image width:  sheet is null");
/*  475: 894 */       return 0.0D;
/*  476:     */     }
/*  477: 898 */     int firstCol = (int)this.x;
/*  478: 899 */     int lastCol = (int)Math.ceil(this.x + this.width) - 1;
/*  479:     */     
/*  480:     */ 
/*  481:     */ 
/*  482:     */ 
/*  483:     */ 
/*  484:     */ 
/*  485:     */ 
/*  486:     */ 
/*  487: 908 */     CellView cellView = this.sheet.getColumnView(firstCol);
/*  488: 909 */     int firstColWidth = cellView.getSize();
/*  489: 910 */     double firstColImageWidth = (1.0D - (this.x - firstCol)) * firstColWidth;
/*  490: 911 */     double pointSize = cellView.getFormat() != null ? 
/*  491: 912 */       cellView.getFormat().getFont().getPointSize() : 10.0D;
/*  492: 913 */     double firstColWidthInPoints = firstColImageWidth * 0.59D * pointSize / 256.0D;
/*  493:     */     
/*  494:     */ 
/*  495:     */ 
/*  496: 917 */     int lastColWidth = 0;
/*  497: 918 */     double lastColImageWidth = 0.0D;
/*  498: 919 */     double lastColWidthInPoints = 0.0D;
/*  499: 920 */     if (lastCol != firstCol)
/*  500:     */     {
/*  501: 922 */       cellView = this.sheet.getColumnView(lastCol);
/*  502: 923 */       lastColWidth = cellView.getSize();
/*  503: 924 */       lastColImageWidth = (this.x + this.width - lastCol) * lastColWidth;
/*  504: 925 */       pointSize = cellView.getFormat() != null ? 
/*  505: 926 */         cellView.getFormat().getFont().getPointSize() : 10.0D;
/*  506: 927 */       lastColWidthInPoints = lastColImageWidth * 0.59D * pointSize / 256.0D;
/*  507:     */     }
/*  508: 931 */     double width = 0.0D;
/*  509: 932 */     for (int i = 0; i < lastCol - firstCol - 1; i++)
/*  510:     */     {
/*  511: 934 */       cellView = this.sheet.getColumnView(firstCol + 1 + i);
/*  512: 935 */       pointSize = cellView.getFormat() != null ? 
/*  513: 936 */         cellView.getFormat().getFont().getPointSize() : 10.0D;
/*  514: 937 */       width += cellView.getSize() * 0.59D * pointSize / 256.0D;
/*  515:     */     }
/*  516: 941 */     double widthInPoints = width + 
/*  517: 942 */       firstColWidthInPoints + lastColWidthInPoints;
/*  518:     */     
/*  519: 944 */     return widthInPoints;
/*  520:     */   }
/*  521:     */   
/*  522:     */   private double getHeightInPoints()
/*  523:     */   {
/*  524: 955 */     if (this.sheet == null)
/*  525:     */     {
/*  526: 957 */       logger.warn("calculating image height:  sheet is null");
/*  527: 958 */       return 0.0D;
/*  528:     */     }
/*  529: 962 */     int firstRow = (int)this.y;
/*  530: 963 */     int lastRow = (int)Math.ceil(this.y + this.height) - 1;
/*  531:     */     
/*  532:     */ 
/*  533:     */ 
/*  534: 967 */     int firstRowHeight = this.sheet.getRowView(firstRow).getSize();
/*  535: 968 */     double firstRowImageHeight = (1.0D - (this.y - firstRow)) * firstRowHeight;
/*  536:     */     
/*  537:     */ 
/*  538:     */ 
/*  539: 972 */     int lastRowHeight = 0;
/*  540: 973 */     double lastRowImageHeight = 0.0D;
/*  541: 974 */     if (lastRow != firstRow)
/*  542:     */     {
/*  543: 976 */       lastRowHeight = this.sheet.getRowView(lastRow).getSize();
/*  544: 977 */       lastRowImageHeight = (this.y + this.height - lastRow) * lastRowHeight;
/*  545:     */     }
/*  546: 981 */     double height = 0.0D;
/*  547: 982 */     for (int i = 0; i < lastRow - firstRow - 1; i++) {
/*  548: 984 */       height += this.sheet.getRowView(firstRow + 1 + i).getSize();
/*  549:     */     }
/*  550: 988 */     double heightInTwips = height + firstRowHeight + lastRowHeight;
/*  551:     */     
/*  552:     */ 
/*  553:     */ 
/*  554: 992 */     double heightInPoints = heightInTwips / 20.0D;
/*  555:     */     
/*  556: 994 */     return heightInPoints;
/*  557:     */   }
/*  558:     */   
/*  559:     */   public double getWidth(LengthUnit unit)
/*  560:     */   {
/*  561:1005 */     double widthInPoints = getWidthInPoints();
/*  562:1006 */     return widthInPoints * LengthConverter.getConversionFactor(
/*  563:1007 */       LengthUnit.POINTS, unit);
/*  564:     */   }
/*  565:     */   
/*  566:     */   public double getHeight(LengthUnit unit)
/*  567:     */   {
/*  568:1018 */     double heightInPoints = getHeightInPoints();
/*  569:1019 */     return heightInPoints * LengthConverter.getConversionFactor(
/*  570:1020 */       LengthUnit.POINTS, unit);
/*  571:     */   }
/*  572:     */   
/*  573:     */   public int getImageWidth()
/*  574:     */   {
/*  575:1032 */     return getPngReader().getWidth();
/*  576:     */   }
/*  577:     */   
/*  578:     */   public int getImageHeight()
/*  579:     */   {
/*  580:1044 */     return getPngReader().getHeight();
/*  581:     */   }
/*  582:     */   
/*  583:     */   public double getHorizontalResolution(LengthUnit unit)
/*  584:     */   {
/*  585:1056 */     int res = getPngReader().getHorizontalResolution();
/*  586:1057 */     return res / LengthConverter.getConversionFactor(LengthUnit.METRES, unit);
/*  587:     */   }
/*  588:     */   
/*  589:     */   public double getVerticalResolution(LengthUnit unit)
/*  590:     */   {
/*  591:1068 */     int res = getPngReader().getVerticalResolution();
/*  592:1069 */     return res / LengthConverter.getConversionFactor(LengthUnit.METRES, unit);
/*  593:     */   }
/*  594:     */   
/*  595:     */   private PNGReader getPngReader()
/*  596:     */   {
/*  597:1074 */     if (this.pngReader != null) {
/*  598:1076 */       return this.pngReader;
/*  599:     */     }
/*  600:1079 */     byte[] imdata = (byte[])null;
/*  601:1080 */     if ((this.origin == Origin.READ) || (this.origin == Origin.READ_WRITE)) {
/*  602:1082 */       imdata = getImageData();
/*  603:     */     } else {
/*  604:     */       try
/*  605:     */       {
/*  606:1088 */         imdata = getImageBytes();
/*  607:     */       }
/*  608:     */       catch (IOException e)
/*  609:     */       {
/*  610:1092 */         logger.warn("Could not read image file");
/*  611:1093 */         imdata = new byte[0];
/*  612:     */       }
/*  613:     */     }
/*  614:1097 */     this.pngReader = new PNGReader(imdata);
/*  615:1098 */     this.pngReader.read();
/*  616:1099 */     return this.pngReader;
/*  617:     */   }
/*  618:     */   
/*  619:     */   protected void setImageAnchor(ImageAnchorProperties iap)
/*  620:     */   {
/*  621:1107 */     this.imageAnchorProperties = iap;
/*  622:1109 */     if (this.origin == Origin.READ) {
/*  623:1111 */       this.origin = Origin.READ_WRITE;
/*  624:     */     }
/*  625:     */   }
/*  626:     */   
/*  627:     */   protected ImageAnchorProperties getImageAnchor()
/*  628:     */   {
/*  629:1120 */     if (!this.initialized) {
/*  630:1122 */       initialize();
/*  631:     */     }
/*  632:1125 */     return this.imageAnchorProperties;
/*  633:     */   }
/*  634:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Drawing
 * JD-Core Version:    0.7.0.1
 */