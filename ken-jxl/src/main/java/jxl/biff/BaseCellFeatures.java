/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import jxl.CellReferenceHelper;
/*   5:    */ import jxl.Range;
/*   6:    */ import jxl.biff.drawing.ComboBox;
/*   7:    */ import jxl.biff.drawing.Comment;
/*   8:    */ import jxl.common.Assert;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ import jxl.write.biff.CellValue;
/*  11:    */ 
/*  12:    */ public class BaseCellFeatures
/*  13:    */ {
/*  14: 41 */   public static Logger logger = Logger.getLogger(BaseCellFeatures.class);
/*  15:    */   private String comment;
/*  16:    */   private double commentWidth;
/*  17:    */   private double commentHeight;
/*  18:    */   private Comment commentDrawing;
/*  19:    */   private ComboBox comboBox;
/*  20:    */   private DataValiditySettingsRecord validationSettings;
/*  21:    */   private DVParser dvParser;
/*  22:    */   private boolean dropDown;
/*  23:    */   private boolean dataValidation;
/*  24:    */   private CellValue writableCell;
/*  25:    */   private static final double defaultCommentWidth = 3.0D;
/*  26:    */   private static final double defaultCommentHeight = 4.0D;
/*  27:    */   protected BaseCellFeatures() {}
/*  28:    */   
/*  29:    */   protected static class ValidationCondition
/*  30:    */   {
/*  31:    */     private DVParser.Condition condition;
/*  32:102 */     private static ValidationCondition[] types = new ValidationCondition[0];
/*  33:    */     
/*  34:    */     ValidationCondition(DVParser.Condition c)
/*  35:    */     {
/*  36:106 */       this.condition = c;
/*  37:107 */       ValidationCondition[] oldtypes = types;
/*  38:108 */       types = new ValidationCondition[oldtypes.length + 1];
/*  39:109 */       System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
/*  40:110 */       types[oldtypes.length] = this;
/*  41:    */     }
/*  42:    */     
/*  43:    */     public DVParser.Condition getCondition()
/*  44:    */     {
/*  45:115 */       return this.condition;
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:120 */   public static final ValidationCondition BETWEEN = new ValidationCondition(DVParser.BETWEEN);
/*  50:122 */   public static final ValidationCondition NOT_BETWEEN = new ValidationCondition(DVParser.NOT_BETWEEN);
/*  51:124 */   public static final ValidationCondition EQUAL = new ValidationCondition(DVParser.EQUAL);
/*  52:126 */   public static final ValidationCondition NOT_EQUAL = new ValidationCondition(DVParser.NOT_EQUAL);
/*  53:128 */   public static final ValidationCondition GREATER_THAN = new ValidationCondition(DVParser.GREATER_THAN);
/*  54:130 */   public static final ValidationCondition LESS_THAN = new ValidationCondition(DVParser.LESS_THAN);
/*  55:132 */   public static final ValidationCondition GREATER_EQUAL = new ValidationCondition(DVParser.GREATER_EQUAL);
/*  56:134 */   public static final ValidationCondition LESS_EQUAL = new ValidationCondition(DVParser.LESS_EQUAL);
/*  57:    */   
/*  58:    */   public BaseCellFeatures(BaseCellFeatures cf)
/*  59:    */   {
/*  60:151 */     this.comment = cf.comment;
/*  61:152 */     this.commentWidth = cf.commentWidth;
/*  62:153 */     this.commentHeight = cf.commentHeight;
/*  63:    */     
/*  64:    */ 
/*  65:156 */     this.dropDown = cf.dropDown;
/*  66:157 */     this.dataValidation = cf.dataValidation;
/*  67:    */     
/*  68:159 */     this.validationSettings = cf.validationSettings;
/*  69:161 */     if (cf.dvParser != null) {
/*  70:163 */       this.dvParser = new DVParser(cf.dvParser);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected String getComment()
/*  75:    */   {
/*  76:172 */     return this.comment;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public double getCommentWidth()
/*  80:    */   {
/*  81:180 */     return this.commentWidth;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public double getCommentHeight()
/*  85:    */   {
/*  86:188 */     return this.commentHeight;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public final void setWritableCell(CellValue wc)
/*  90:    */   {
/*  91:198 */     this.writableCell = wc;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setReadComment(String s, double w, double h)
/*  95:    */   {
/*  96:206 */     this.comment = s;
/*  97:207 */     this.commentWidth = w;
/*  98:208 */     this.commentHeight = h;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setValidationSettings(DataValiditySettingsRecord dvsr)
/* 102:    */   {
/* 103:216 */     Assert.verify(dvsr != null);
/* 104:    */     
/* 105:218 */     this.validationSettings = dvsr;
/* 106:219 */     this.dataValidation = true;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setComment(String s)
/* 110:    */   {
/* 111:229 */     setComment(s, 3.0D, 4.0D);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setComment(String s, double width, double height)
/* 115:    */   {
/* 116:241 */     this.comment = s;
/* 117:242 */     this.commentWidth = width;
/* 118:243 */     this.commentHeight = height;
/* 119:245 */     if (this.commentDrawing != null)
/* 120:    */     {
/* 121:247 */       this.commentDrawing.setCommentText(s);
/* 122:248 */       this.commentDrawing.setWidth(width);
/* 123:249 */       this.commentDrawing.setWidth(height);
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void removeComment()
/* 128:    */   {
/* 129:260 */     this.comment = null;
/* 130:263 */     if (this.commentDrawing != null)
/* 131:    */     {
/* 132:267 */       this.writableCell.removeComment(this.commentDrawing);
/* 133:268 */       this.commentDrawing = null;
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void removeDataValidation()
/* 138:    */   {
/* 139:277 */     if (!this.dataValidation) {
/* 140:279 */       return;
/* 141:    */     }
/* 142:283 */     DVParser dvp = getDVParser();
/* 143:284 */     if (dvp.extendedCellsValidation())
/* 144:    */     {
/* 145:286 */       logger.warn("Cannot remove data validation from " + 
/* 146:287 */         CellReferenceHelper.getCellReference(this.writableCell) + 
/* 147:288 */         " as it is part of the shared reference " + 
/* 148:289 */         CellReferenceHelper.getCellReference(dvp.getFirstColumn(), 
/* 149:290 */         dvp.getFirstRow()) + 
/* 150:291 */         "-" + 
/* 151:292 */         CellReferenceHelper.getCellReference(dvp.getLastColumn(), 
/* 152:293 */         dvp.getLastRow()));
/* 153:294 */       return;
/* 154:    */     }
/* 155:298 */     this.writableCell.removeDataValidation();
/* 156:299 */     clearValidationSettings();
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void removeSharedDataValidation()
/* 160:    */   {
/* 161:309 */     if (!this.dataValidation) {
/* 162:311 */       return;
/* 163:    */     }
/* 164:315 */     this.writableCell.removeDataValidation();
/* 165:316 */     clearValidationSettings();
/* 166:    */   }
/* 167:    */   
/* 168:    */   public final void setCommentDrawing(Comment c)
/* 169:    */   {
/* 170:324 */     this.commentDrawing = c;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public final Comment getCommentDrawing()
/* 174:    */   {
/* 175:332 */     return this.commentDrawing;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public String getDataValidationList()
/* 179:    */   {
/* 180:342 */     if (this.validationSettings == null) {
/* 181:344 */       return null;
/* 182:    */     }
/* 183:347 */     return this.validationSettings.getValidationFormula();
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setDataValidationList(Collection c)
/* 187:    */   {
/* 188:359 */     if ((this.dataValidation) && (getDVParser().extendedCellsValidation()))
/* 189:    */     {
/* 190:361 */       logger.warn("Cannot set data validation on " + 
/* 191:362 */         CellReferenceHelper.getCellReference(this.writableCell) + 
/* 192:363 */         " as it is part of a shared data validation");
/* 193:364 */       return;
/* 194:    */     }
/* 195:366 */     clearValidationSettings();
/* 196:367 */     this.dvParser = new DVParser(c);
/* 197:368 */     this.dropDown = true;
/* 198:369 */     this.dataValidation = true;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void setDataValidationRange(int col1, int r1, int col2, int r2)
/* 202:    */   {
/* 203:379 */     if ((this.dataValidation) && (getDVParser().extendedCellsValidation()))
/* 204:    */     {
/* 205:381 */       logger.warn("Cannot set data validation on " + 
/* 206:382 */         CellReferenceHelper.getCellReference(this.writableCell) + 
/* 207:383 */         " as it is part of a shared data validation");
/* 208:384 */       return;
/* 209:    */     }
/* 210:386 */     clearValidationSettings();
/* 211:387 */     this.dvParser = new DVParser(col1, r1, col2, r2);
/* 212:388 */     this.dropDown = true;
/* 213:389 */     this.dataValidation = true;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setDataValidationRange(String namedRange)
/* 217:    */   {
/* 218:397 */     if ((this.dataValidation) && (getDVParser().extendedCellsValidation()))
/* 219:    */     {
/* 220:399 */       logger.warn("Cannot set data validation on " + 
/* 221:400 */         CellReferenceHelper.getCellReference(this.writableCell) + 
/* 222:401 */         " as it is part of a shared data validation");
/* 223:402 */       return;
/* 224:    */     }
/* 225:404 */     clearValidationSettings();
/* 226:405 */     this.dvParser = new DVParser(namedRange);
/* 227:406 */     this.dropDown = true;
/* 228:407 */     this.dataValidation = true;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void setNumberValidation(double val, ValidationCondition c)
/* 232:    */   {
/* 233:415 */     if ((this.dataValidation) && (getDVParser().extendedCellsValidation()))
/* 234:    */     {
/* 235:417 */       logger.warn("Cannot set data validation on " + 
/* 236:418 */         CellReferenceHelper.getCellReference(this.writableCell) + 
/* 237:419 */         " as it is part of a shared data validation");
/* 238:420 */       return;
/* 239:    */     }
/* 240:422 */     clearValidationSettings();
/* 241:423 */     this.dvParser = new DVParser(val, (0.0D / 0.0D), c.getCondition());
/* 242:424 */     this.dropDown = false;
/* 243:425 */     this.dataValidation = true;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void setNumberValidation(double val1, double val2, ValidationCondition c)
/* 247:    */   {
/* 248:431 */     if ((this.dataValidation) && (getDVParser().extendedCellsValidation()))
/* 249:    */     {
/* 250:433 */       logger.warn("Cannot set data validation on " + 
/* 251:434 */         CellReferenceHelper.getCellReference(this.writableCell) + 
/* 252:435 */         " as it is part of a shared data validation");
/* 253:436 */       return;
/* 254:    */     }
/* 255:438 */     clearValidationSettings();
/* 256:439 */     this.dvParser = new DVParser(val1, val2, c.getCondition());
/* 257:440 */     this.dropDown = false;
/* 258:441 */     this.dataValidation = true;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public boolean hasDataValidation()
/* 262:    */   {
/* 263:452 */     return this.dataValidation;
/* 264:    */   }
/* 265:    */   
/* 266:    */   private void clearValidationSettings()
/* 267:    */   {
/* 268:460 */     this.validationSettings = null;
/* 269:461 */     this.dvParser = null;
/* 270:462 */     this.dropDown = false;
/* 271:463 */     this.comboBox = null;
/* 272:464 */     this.dataValidation = false;
/* 273:    */   }
/* 274:    */   
/* 275:    */   public boolean hasDropDown()
/* 276:    */   {
/* 277:474 */     return this.dropDown;
/* 278:    */   }
/* 279:    */   
/* 280:    */   public void setComboBox(ComboBox cb)
/* 281:    */   {
/* 282:484 */     this.comboBox = cb;
/* 283:    */   }
/* 284:    */   
/* 285:    */   public DVParser getDVParser()
/* 286:    */   {
/* 287:493 */     if (this.dvParser != null) {
/* 288:495 */       return this.dvParser;
/* 289:    */     }
/* 290:499 */     if (this.validationSettings != null)
/* 291:    */     {
/* 292:501 */       this.dvParser = new DVParser(this.validationSettings.getDVParser());
/* 293:502 */       return this.dvParser;
/* 294:    */     }
/* 295:505 */     return null;
/* 296:    */   }
/* 297:    */   
/* 298:    */   public void shareDataValidation(BaseCellFeatures source)
/* 299:    */   {
/* 300:515 */     if (this.dataValidation)
/* 301:    */     {
/* 302:517 */       logger.warn("Attempting to share a data validation on cell " + 
/* 303:518 */         CellReferenceHelper.getCellReference(this.writableCell) + 
/* 304:519 */         " which already has a data validation");
/* 305:520 */       return;
/* 306:    */     }
/* 307:522 */     clearValidationSettings();
/* 308:523 */     this.dvParser = source.getDVParser();
/* 309:524 */     this.validationSettings = null;
/* 310:525 */     this.dataValidation = true;
/* 311:526 */     this.dropDown = source.dropDown;
/* 312:527 */     this.comboBox = source.comboBox;
/* 313:    */   }
/* 314:    */   
/* 315:    */   public Range getSharedDataValidationRange()
/* 316:    */   {
/* 317:540 */     if (!this.dataValidation) {
/* 318:542 */       return null;
/* 319:    */     }
/* 320:545 */     DVParser dvp = getDVParser();
/* 321:    */     
/* 322:547 */     return new SheetRangeImpl(this.writableCell.getSheet(), 
/* 323:548 */       dvp.getFirstColumn(), 
/* 324:549 */       dvp.getFirstRow(), 
/* 325:550 */       dvp.getLastColumn(), 
/* 326:551 */       dvp.getLastRow());
/* 327:    */   }
/* 328:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.BaseCellFeatures
 * JD-Core Version:    0.7.0.1
 */