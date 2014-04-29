/*   1:    */ package jxl;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Locale;
/*   6:    */ import jxl.biff.CountryCode;
/*   7:    */ import jxl.biff.formula.FunctionNames;
/*   8:    */ import jxl.common.Logger;
/*   9:    */ 
/*  10:    */ public final class WorkbookSettings
/*  11:    */ {
/*  12: 42 */   private static Logger logger = Logger.getLogger(WorkbookSettings.class);
/*  13:    */   private int initialFileSize;
/*  14:    */   private int arrayGrowSize;
/*  15:    */   private boolean drawingsDisabled;
/*  16:    */   private boolean namesDisabled;
/*  17:    */   private boolean formulaReferenceAdjustDisabled;
/*  18:    */   private boolean gcDisabled;
/*  19:    */   private boolean rationalizationDisabled;
/*  20:    */   private boolean mergedCellCheckingDisabled;
/*  21:    */   private boolean propertySetsDisabled;
/*  22:    */   private boolean cellValidationDisabled;
/*  23:    */   private boolean ignoreBlankCells;
/*  24:    */   private boolean autoFilterDisabled;
/*  25:    */   private boolean useTemporaryFileDuringWrite;
/*  26:    */   private File temporaryFileDuringWriteDirectory;
/*  27:    */   private Locale locale;
/*  28:    */   private FunctionNames functionNames;
/*  29:    */   private String encoding;
/*  30:    */   private int characterSet;
/*  31:    */   private String excelDisplayLanguage;
/*  32:    */   private String excelRegionalSettings;
/*  33:    */   private HashMap localeFunctionNames;
/*  34:    */   private boolean refreshAll;
/*  35:    */   private boolean template;
/*  36:207 */   private boolean excel9file = false;
/*  37:    */   private boolean windowProtected;
/*  38:    */   private String writeAccess;
/*  39:    */   private int hideobj;
/*  40:    */   public static final int HIDEOBJ_HIDE_ALL = 2;
/*  41:    */   public static final int HIDEOBJ_SHOW_PLACEHOLDERS = 1;
/*  42:    */   public static final int HIDEOBJ_SHOW_ALL = 0;
/*  43:    */   private static final int DEFAULT_INITIAL_FILE_SIZE = 5242880;
/*  44:    */   private static final int DEFAULT_ARRAY_GROW_SIZE = 1048576;
/*  45:    */   
/*  46:    */   public WorkbookSettings()
/*  47:    */   {
/*  48:254 */     this.initialFileSize = 5242880;
/*  49:255 */     this.arrayGrowSize = 1048576;
/*  50:256 */     this.localeFunctionNames = new HashMap();
/*  51:257 */     this.excelDisplayLanguage = CountryCode.USA.getCode();
/*  52:258 */     this.excelRegionalSettings = CountryCode.UK.getCode();
/*  53:259 */     this.refreshAll = false;
/*  54:260 */     this.template = false;
/*  55:261 */     this.excel9file = false;
/*  56:262 */     this.windowProtected = false;
/*  57:263 */     this.hideobj = 0;
/*  58:    */     try
/*  59:    */     {
/*  60:268 */       boolean suppressWarnings = Boolean.getBoolean("jxl.nowarnings");
/*  61:269 */       setSuppressWarnings(suppressWarnings);
/*  62:270 */       this.drawingsDisabled = Boolean.getBoolean("jxl.nodrawings");
/*  63:271 */       this.namesDisabled = Boolean.getBoolean("jxl.nonames");
/*  64:272 */       this.gcDisabled = Boolean.getBoolean("jxl.nogc");
/*  65:273 */       this.rationalizationDisabled = Boolean.getBoolean("jxl.norat");
/*  66:274 */       this.mergedCellCheckingDisabled = 
/*  67:275 */         Boolean.getBoolean("jxl.nomergedcellchecks");
/*  68:276 */       this.formulaReferenceAdjustDisabled = 
/*  69:277 */         Boolean.getBoolean("jxl.noformulaadjust");
/*  70:278 */       this.propertySetsDisabled = Boolean.getBoolean("jxl.nopropertysets");
/*  71:279 */       this.ignoreBlankCells = Boolean.getBoolean("jxl.ignoreblanks");
/*  72:280 */       this.cellValidationDisabled = Boolean.getBoolean("jxl.nocellvalidation");
/*  73:281 */       this.autoFilterDisabled = (!Boolean.getBoolean("jxl.autofilter"));
/*  74:    */       
/*  75:283 */       this.useTemporaryFileDuringWrite = 
/*  76:284 */         Boolean.getBoolean("jxl.usetemporaryfileduringwrite");
/*  77:285 */       String tempdir = 
/*  78:286 */         System.getProperty("jxl.temporaryfileduringwritedirectory");
/*  79:288 */       if (tempdir != null) {
/*  80:290 */         this.temporaryFileDuringWriteDirectory = new File(tempdir);
/*  81:    */       }
/*  82:293 */       this.encoding = System.getProperty("file.encoding");
/*  83:    */     }
/*  84:    */     catch (SecurityException e)
/*  85:    */     {
/*  86:297 */       logger.warn("Error accessing system properties.", e);
/*  87:    */     }
/*  88:    */     try
/*  89:    */     {
/*  90:303 */       if ((System.getProperty("jxl.lang") == null) || 
/*  91:304 */         (System.getProperty("jxl.country") == null)) {
/*  92:306 */         this.locale = Locale.getDefault();
/*  93:    */       } else {
/*  94:310 */         this.locale = new Locale(System.getProperty("jxl.lang"), 
/*  95:311 */           System.getProperty("jxl.country"));
/*  96:    */       }
/*  97:314 */       if (System.getProperty("jxl.encoding") != null) {
/*  98:316 */         this.encoding = System.getProperty("jxl.encoding");
/*  99:    */       }
/* 100:    */     }
/* 101:    */     catch (SecurityException e)
/* 102:    */     {
/* 103:321 */       logger.warn("Error accessing system properties.", e);
/* 104:322 */       this.locale = Locale.getDefault();
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setArrayGrowSize(int sz)
/* 109:    */   {
/* 110:337 */     this.arrayGrowSize = sz;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public int getArrayGrowSize()
/* 114:    */   {
/* 115:347 */     return this.arrayGrowSize;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setInitialFileSize(int sz)
/* 119:    */   {
/* 120:360 */     this.initialFileSize = sz;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public int getInitialFileSize()
/* 124:    */   {
/* 125:370 */     return this.initialFileSize;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public boolean getDrawingsDisabled()
/* 129:    */   {
/* 130:380 */     return this.drawingsDisabled;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public boolean getGCDisabled()
/* 134:    */   {
/* 135:390 */     return this.gcDisabled;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public boolean getNamesDisabled()
/* 139:    */   {
/* 140:400 */     return this.namesDisabled;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void setNamesDisabled(boolean b)
/* 144:    */   {
/* 145:410 */     this.namesDisabled = b;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void setDrawingsDisabled(boolean b)
/* 149:    */   {
/* 150:420 */     this.drawingsDisabled = b;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setRationalization(boolean r)
/* 154:    */   {
/* 155:431 */     this.rationalizationDisabled = (!r);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public boolean getRationalizationDisabled()
/* 159:    */   {
/* 160:441 */     return this.rationalizationDisabled;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public boolean getMergedCellCheckingDisabled()
/* 164:    */   {
/* 165:451 */     return this.mergedCellCheckingDisabled;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setMergedCellChecking(boolean b)
/* 169:    */   {
/* 170:461 */     this.mergedCellCheckingDisabled = (!b);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setPropertySets(boolean r)
/* 174:    */   {
/* 175:474 */     this.propertySetsDisabled = (!r);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public boolean getPropertySetsDisabled()
/* 179:    */   {
/* 180:484 */     return this.propertySetsDisabled;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setSuppressWarnings(boolean w)
/* 184:    */   {
/* 185:496 */     logger.setSuppressWarnings(w);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public boolean getFormulaAdjust()
/* 189:    */   {
/* 190:507 */     return !this.formulaReferenceAdjustDisabled;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void setFormulaAdjust(boolean b)
/* 194:    */   {
/* 195:517 */     this.formulaReferenceAdjustDisabled = (!b);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public void setLocale(Locale l)
/* 199:    */   {
/* 200:529 */     this.locale = l;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public Locale getLocale()
/* 204:    */   {
/* 205:539 */     return this.locale;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public String getEncoding()
/* 209:    */   {
/* 210:549 */     return this.encoding;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void setEncoding(String enc)
/* 214:    */   {
/* 215:559 */     this.encoding = enc;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public FunctionNames getFunctionNames()
/* 219:    */   {
/* 220:571 */     if (this.functionNames == null)
/* 221:    */     {
/* 222:573 */       this.functionNames = ((FunctionNames)this.localeFunctionNames.get(this.locale));
/* 223:577 */       if (this.functionNames == null)
/* 224:    */       {
/* 225:579 */         this.functionNames = new FunctionNames(this.locale);
/* 226:580 */         this.localeFunctionNames.put(this.locale, this.functionNames);
/* 227:    */       }
/* 228:    */     }
/* 229:584 */     return this.functionNames;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public int getCharacterSet()
/* 233:    */   {
/* 234:595 */     return this.characterSet;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public void setCharacterSet(int cs)
/* 238:    */   {
/* 239:606 */     this.characterSet = cs;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public void setGCDisabled(boolean disabled)
/* 243:    */   {
/* 244:616 */     this.gcDisabled = disabled;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public void setIgnoreBlanks(boolean ignoreBlanks)
/* 248:    */   {
/* 249:626 */     this.ignoreBlankCells = ignoreBlanks;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public boolean getIgnoreBlanks()
/* 253:    */   {
/* 254:636 */     return this.ignoreBlankCells;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public void setCellValidationDisabled(boolean cv)
/* 258:    */   {
/* 259:646 */     this.cellValidationDisabled = cv;
/* 260:    */   }
/* 261:    */   
/* 262:    */   public boolean getCellValidationDisabled()
/* 263:    */   {
/* 264:656 */     return this.cellValidationDisabled;
/* 265:    */   }
/* 266:    */   
/* 267:    */   public String getExcelDisplayLanguage()
/* 268:    */   {
/* 269:666 */     return this.excelDisplayLanguage;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public String getExcelRegionalSettings()
/* 273:    */   {
/* 274:676 */     return this.excelRegionalSettings;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public void setExcelDisplayLanguage(String code)
/* 278:    */   {
/* 279:686 */     this.excelDisplayLanguage = code;
/* 280:    */   }
/* 281:    */   
/* 282:    */   public void setExcelRegionalSettings(String code)
/* 283:    */   {
/* 284:696 */     this.excelRegionalSettings = code;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public boolean getAutoFilterDisabled()
/* 288:    */   {
/* 289:706 */     return this.autoFilterDisabled;
/* 290:    */   }
/* 291:    */   
/* 292:    */   public void setAutoFilterDisabled(boolean disabled)
/* 293:    */   {
/* 294:716 */     this.autoFilterDisabled = disabled;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public boolean getUseTemporaryFileDuringWrite()
/* 298:    */   {
/* 299:731 */     return this.useTemporaryFileDuringWrite;
/* 300:    */   }
/* 301:    */   
/* 302:    */   public void setUseTemporaryFileDuringWrite(boolean temp)
/* 303:    */   {
/* 304:746 */     this.useTemporaryFileDuringWrite = temp;
/* 305:    */   }
/* 306:    */   
/* 307:    */   public void setTemporaryFileDuringWriteDirectory(File dir)
/* 308:    */   {
/* 309:760 */     this.temporaryFileDuringWriteDirectory = dir;
/* 310:    */   }
/* 311:    */   
/* 312:    */   public File getTemporaryFileDuringWriteDirectory()
/* 313:    */   {
/* 314:774 */     return this.temporaryFileDuringWriteDirectory;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public void setRefreshAll(boolean refreshAll)
/* 318:    */   {
/* 319:786 */     this.refreshAll = refreshAll;
/* 320:    */   }
/* 321:    */   
/* 322:    */   public boolean getRefreshAll()
/* 323:    */   {
/* 324:797 */     return this.refreshAll;
/* 325:    */   }
/* 326:    */   
/* 327:    */   public boolean getTemplate()
/* 328:    */   {
/* 329:806 */     return this.template;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public void setTemplate(boolean template)
/* 333:    */   {
/* 334:815 */     this.template = template;
/* 335:    */   }
/* 336:    */   
/* 337:    */   public boolean getExcel9File()
/* 338:    */   {
/* 339:825 */     return this.excel9file;
/* 340:    */   }
/* 341:    */   
/* 342:    */   public void setExcel9File(boolean excel9file)
/* 343:    */   {
/* 344:833 */     this.excel9file = excel9file;
/* 345:    */   }
/* 346:    */   
/* 347:    */   public boolean getWindowProtected()
/* 348:    */   {
/* 349:841 */     return this.windowProtected;
/* 350:    */   }
/* 351:    */   
/* 352:    */   public void setWindowProtected(boolean windowprotected)
/* 353:    */   {
/* 354:849 */     this.windowProtected = this.windowProtected;
/* 355:    */   }
/* 356:    */   
/* 357:    */   public int getHideobj()
/* 358:    */   {
/* 359:861 */     return this.hideobj;
/* 360:    */   }
/* 361:    */   
/* 362:    */   public void setHideobj(int hideobj)
/* 363:    */   {
/* 364:873 */     this.hideobj = hideobj;
/* 365:    */   }
/* 366:    */   
/* 367:    */   public String getWriteAccess()
/* 368:    */   {
/* 369:881 */     return this.writeAccess;
/* 370:    */   }
/* 371:    */   
/* 372:    */   public void setWriteAccess(String writeAccess)
/* 373:    */   {
/* 374:889 */     this.writeAccess = writeAccess;
/* 375:    */   }
/* 376:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.WorkbookSettings
 * JD-Core Version:    0.7.0.1
 */