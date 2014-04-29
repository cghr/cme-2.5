/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.CellReferenceHelper;
/*   4:    */ import jxl.CellType;
/*   5:    */ import jxl.FormulaCell;
/*   6:    */ import jxl.Sheet;
/*   7:    */ import jxl.WorkbookSettings;
/*   8:    */ import jxl.biff.FormattingRecords;
/*   9:    */ import jxl.biff.FormulaData;
/*  10:    */ import jxl.biff.IntegerHelper;
/*  11:    */ import jxl.biff.Type;
/*  12:    */ import jxl.biff.WorkbookMethods;
/*  13:    */ import jxl.biff.formula.ExternalSheet;
/*  14:    */ import jxl.biff.formula.FormulaException;
/*  15:    */ import jxl.biff.formula.FormulaParser;
/*  16:    */ import jxl.common.Assert;
/*  17:    */ import jxl.common.Logger;
/*  18:    */ import jxl.write.WritableCell;
/*  19:    */ 
/*  20:    */ class ReadFormulaRecord
/*  21:    */   extends CellValue
/*  22:    */   implements FormulaData
/*  23:    */ {
/*  24: 51 */   private static Logger logger = Logger.getLogger(ReadFormulaRecord.class);
/*  25:    */   private FormulaData formula;
/*  26:    */   private FormulaParser parser;
/*  27:    */   
/*  28:    */   protected ReadFormulaRecord(FormulaData f)
/*  29:    */   {
/*  30: 70 */     super(Type.FORMULA, f);
/*  31: 71 */     this.formula = f;
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected final byte[] getCellData()
/*  35:    */   {
/*  36: 76 */     return super.getData();
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected byte[] handleFormulaException()
/*  40:    */   {
/*  41: 88 */     byte[] expressiondata = (byte[])null;
/*  42: 89 */     byte[] celldata = super.getData();
/*  43:    */     
/*  44:    */ 
/*  45: 92 */     WritableWorkbookImpl w = getSheet().getWorkbook();
/*  46: 93 */     this.parser = new FormulaParser(getContents(), w, w, 
/*  47: 94 */       w.getSettings());
/*  48:    */     try
/*  49:    */     {
/*  50: 99 */       this.parser.parse();
/*  51:    */     }
/*  52:    */     catch (FormulaException e2)
/*  53:    */     {
/*  54:103 */       logger.warn(e2.getMessage());
/*  55:104 */       this.parser = new FormulaParser("\"ERROR\"", w, w, w.getSettings());
/*  56:    */       try
/*  57:    */       {
/*  58:105 */         this.parser.parse();
/*  59:    */       }
/*  60:    */       catch (FormulaException e3)
/*  61:    */       {
/*  62:106 */         Assert.verify(false);
/*  63:    */       }
/*  64:    */     }
/*  65:108 */     byte[] formulaBytes = this.parser.getBytes();
/*  66:109 */     expressiondata = new byte[formulaBytes.length + 16];
/*  67:110 */     IntegerHelper.getTwoBytes(formulaBytes.length, expressiondata, 14);
/*  68:111 */     System.arraycopy(formulaBytes, 0, expressiondata, 16, 
/*  69:112 */       formulaBytes.length); byte[] 
/*  70:    */     
/*  71:    */ 
/*  72:115 */       tmp139_136 = expressiondata;tmp139_136[8] = ((byte)(tmp139_136[8] | 0x2));
/*  73:    */     
/*  74:117 */     byte[] data = new byte[celldata.length + 
/*  75:118 */       expressiondata.length];
/*  76:119 */     System.arraycopy(celldata, 0, data, 0, celldata.length);
/*  77:120 */     System.arraycopy(expressiondata, 0, data, 
/*  78:121 */       celldata.length, expressiondata.length);
/*  79:122 */     return data;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public byte[] getData()
/*  83:    */   {
/*  84:134 */     byte[] celldata = super.getData();
/*  85:135 */     byte[] expressiondata = (byte[])null;
/*  86:    */     try
/*  87:    */     {
/*  88:139 */       if (this.parser == null)
/*  89:    */       {
/*  90:141 */         expressiondata = this.formula.getFormulaData();
/*  91:    */       }
/*  92:    */       else
/*  93:    */       {
/*  94:145 */         byte[] formulaBytes = this.parser.getBytes();
/*  95:146 */         expressiondata = new byte[formulaBytes.length + 16];
/*  96:147 */         IntegerHelper.getTwoBytes(formulaBytes.length, expressiondata, 14);
/*  97:148 */         System.arraycopy(formulaBytes, 0, expressiondata, 16, 
/*  98:149 */           formulaBytes.length);
/*  99:    */       }
/* 100:153 */       byte[] tmp67_64 = expressiondata;tmp67_64[8] = ((byte)(tmp67_64[8] | 0x2));
/* 101:    */       
/* 102:155 */       byte[] data = new byte[celldata.length + 
/* 103:156 */         expressiondata.length];
/* 104:157 */       System.arraycopy(celldata, 0, data, 0, celldata.length);
/* 105:158 */       System.arraycopy(expressiondata, 0, data, 
/* 106:159 */         celldata.length, expressiondata.length);
/* 107:160 */       return data;
/* 108:    */     }
/* 109:    */     catch (FormulaException e)
/* 110:    */     {
/* 111:166 */       logger.warn(
/* 112:167 */         CellReferenceHelper.getCellReference(getColumn(), getRow()) + 
/* 113:168 */         " " + e.getMessage());
/* 114:    */     }
/* 115:169 */     return handleFormulaException();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public CellType getType()
/* 119:    */   {
/* 120:180 */     return this.formula.getType();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public String getContents()
/* 124:    */   {
/* 125:190 */     return this.formula.getContents();
/* 126:    */   }
/* 127:    */   
/* 128:    */   public byte[] getFormulaData()
/* 129:    */     throws FormulaException
/* 130:    */   {
/* 131:201 */     byte[] d = this.formula.getFormulaData();
/* 132:202 */     byte[] data = new byte[d.length];
/* 133:    */     
/* 134:204 */     System.arraycopy(d, 0, data, 0, d.length); byte[] 
/* 135:    */     
/* 136:    */ 
/* 137:207 */       tmp27_24 = data;tmp27_24[8] = ((byte)(tmp27_24[8] | 0x2));
/* 138:    */     
/* 139:209 */     return data;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public byte[] getFormulaBytes()
/* 143:    */     throws FormulaException
/* 144:    */   {
/* 145:220 */     if (this.parser != null) {
/* 146:222 */       return this.parser.getBytes();
/* 147:    */     }
/* 148:226 */     byte[] readFormulaData = getFormulaData();
/* 149:227 */     byte[] formulaBytes = new byte[readFormulaData.length - 16];
/* 150:228 */     System.arraycopy(readFormulaData, 16, formulaBytes, 0, 
/* 151:229 */       formulaBytes.length);
/* 152:230 */     return formulaBytes;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public WritableCell copyTo(int col, int row)
/* 156:    */   {
/* 157:242 */     return new FormulaRecord(col, row, this);
/* 158:    */   }
/* 159:    */   
/* 160:    */   void setCellDetails(FormattingRecords fr, SharedStrings ss, WritableSheetImpl s)
/* 161:    */   {
/* 162:256 */     super.setCellDetails(fr, ss, s);
/* 163:257 */     s.getWorkbook().addRCIRCell(this);
/* 164:    */   }
/* 165:    */   
/* 166:    */   void columnInserted(Sheet s, int sheetIndex, int col)
/* 167:    */   {
/* 168:    */     try
/* 169:    */     {
/* 170:272 */       if (this.parser == null)
/* 171:    */       {
/* 172:274 */         byte[] formulaData = this.formula.getFormulaData();
/* 173:275 */         byte[] formulaBytes = new byte[formulaData.length - 16];
/* 174:276 */         System.arraycopy(formulaData, 16, 
/* 175:277 */           formulaBytes, 0, formulaBytes.length);
/* 176:278 */         this.parser = new FormulaParser(formulaBytes, 
/* 177:279 */           this, 
/* 178:280 */           getSheet().getWorkbook(), 
/* 179:281 */           getSheet().getWorkbook(), 
/* 180:282 */           getSheet().getWorkbookSettings());
/* 181:283 */         this.parser.parse();
/* 182:    */       }
/* 183:286 */       this.parser.columnInserted(sheetIndex, col, s == getSheet());
/* 184:    */     }
/* 185:    */     catch (FormulaException e)
/* 186:    */     {
/* 187:290 */       logger.warn("cannot insert column within formula:  " + e.getMessage());
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   void columnRemoved(Sheet s, int sheetIndex, int col)
/* 192:    */   {
/* 193:    */     try
/* 194:    */     {
/* 195:306 */       if (this.parser == null)
/* 196:    */       {
/* 197:308 */         byte[] formulaData = this.formula.getFormulaData();
/* 198:309 */         byte[] formulaBytes = new byte[formulaData.length - 16];
/* 199:310 */         System.arraycopy(formulaData, 16, 
/* 200:311 */           formulaBytes, 0, formulaBytes.length);
/* 201:312 */         this.parser = new FormulaParser(formulaBytes, 
/* 202:313 */           this, 
/* 203:314 */           getSheet().getWorkbook(), 
/* 204:315 */           getSheet().getWorkbook(), 
/* 205:316 */           getSheet().getWorkbookSettings());
/* 206:317 */         this.parser.parse();
/* 207:    */       }
/* 208:320 */       this.parser.columnRemoved(sheetIndex, col, s == getSheet());
/* 209:    */     }
/* 210:    */     catch (FormulaException e)
/* 211:    */     {
/* 212:324 */       logger.warn("cannot remove column within formula:  " + e.getMessage());
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   void rowInserted(Sheet s, int sheetIndex, int row)
/* 217:    */   {
/* 218:    */     try
/* 219:    */     {
/* 220:340 */       if (this.parser == null)
/* 221:    */       {
/* 222:342 */         byte[] formulaData = this.formula.getFormulaData();
/* 223:343 */         byte[] formulaBytes = new byte[formulaData.length - 16];
/* 224:344 */         System.arraycopy(formulaData, 16, 
/* 225:345 */           formulaBytes, 0, formulaBytes.length);
/* 226:346 */         this.parser = new FormulaParser(formulaBytes, 
/* 227:347 */           this, 
/* 228:348 */           getSheet().getWorkbook(), 
/* 229:349 */           getSheet().getWorkbook(), 
/* 230:350 */           getSheet().getWorkbookSettings());
/* 231:351 */         this.parser.parse();
/* 232:    */       }
/* 233:354 */       this.parser.rowInserted(sheetIndex, row, s == getSheet());
/* 234:    */     }
/* 235:    */     catch (FormulaException e)
/* 236:    */     {
/* 237:358 */       logger.warn("cannot insert row within formula:  " + e.getMessage());
/* 238:    */     }
/* 239:    */   }
/* 240:    */   
/* 241:    */   void rowRemoved(Sheet s, int sheetIndex, int row)
/* 242:    */   {
/* 243:    */     try
/* 244:    */     {
/* 245:374 */       if (this.parser == null)
/* 246:    */       {
/* 247:376 */         byte[] formulaData = this.formula.getFormulaData();
/* 248:377 */         byte[] formulaBytes = new byte[formulaData.length - 16];
/* 249:378 */         System.arraycopy(formulaData, 16, 
/* 250:379 */           formulaBytes, 0, formulaBytes.length);
/* 251:380 */         this.parser = new FormulaParser(formulaBytes, 
/* 252:381 */           this, 
/* 253:382 */           getSheet().getWorkbook(), 
/* 254:383 */           getSheet().getWorkbook(), 
/* 255:384 */           getSheet().getWorkbookSettings());
/* 256:385 */         this.parser.parse();
/* 257:    */       }
/* 258:388 */       this.parser.rowRemoved(sheetIndex, row, s == getSheet());
/* 259:    */     }
/* 260:    */     catch (FormulaException e)
/* 261:    */     {
/* 262:392 */       logger.warn("cannot remove row within formula:  " + e.getMessage());
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   protected FormulaData getReadFormula()
/* 267:    */   {
/* 268:403 */     return this.formula;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public String getFormula()
/* 272:    */     throws FormulaException
/* 273:    */   {
/* 274:413 */     return ((FormulaCell)this.formula).getFormula();
/* 275:    */   }
/* 276:    */   
/* 277:    */   public boolean handleImportedCellReferences(ExternalSheet es, WorkbookMethods mt, WorkbookSettings ws)
/* 278:    */   {
/* 279:    */     try
/* 280:    */     {
/* 281:428 */       if (this.parser == null)
/* 282:    */       {
/* 283:430 */         byte[] formulaData = this.formula.getFormulaData();
/* 284:431 */         byte[] formulaBytes = new byte[formulaData.length - 16];
/* 285:432 */         System.arraycopy(formulaData, 16, 
/* 286:433 */           formulaBytes, 0, formulaBytes.length);
/* 287:434 */         this.parser = new FormulaParser(formulaBytes, 
/* 288:435 */           this, 
/* 289:436 */           es, mt, ws);
/* 290:437 */         this.parser.parse();
/* 291:    */       }
/* 292:440 */       return this.parser.handleImportedCellReferences();
/* 293:    */     }
/* 294:    */     catch (FormulaException e)
/* 295:    */     {
/* 296:444 */       logger.warn("cannot import formula:  " + e.getMessage());
/* 297:    */     }
/* 298:445 */     return false;
/* 299:    */   }
/* 300:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ReadFormulaRecord
 * JD-Core Version:    0.7.0.1
 */