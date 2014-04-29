/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.CellReferenceHelper;
/*   4:    */ import jxl.CellType;
/*   5:    */ import jxl.Sheet;
/*   6:    */ import jxl.WorkbookSettings;
/*   7:    */ import jxl.biff.FormattingRecords;
/*   8:    */ import jxl.biff.FormulaData;
/*   9:    */ import jxl.biff.IntegerHelper;
/*  10:    */ import jxl.biff.Type;
/*  11:    */ import jxl.biff.WorkbookMethods;
/*  12:    */ import jxl.biff.formula.ExternalSheet;
/*  13:    */ import jxl.biff.formula.FormulaException;
/*  14:    */ import jxl.biff.formula.FormulaParser;
/*  15:    */ import jxl.common.Assert;
/*  16:    */ import jxl.common.Logger;
/*  17:    */ import jxl.format.CellFormat;
/*  18:    */ import jxl.write.WritableCell;
/*  19:    */ 
/*  20:    */ public class FormulaRecord
/*  21:    */   extends CellValue
/*  22:    */   implements FormulaData
/*  23:    */ {
/*  24: 49 */   private static Logger logger = Logger.getLogger(FormulaRecord.class);
/*  25:    */   private String formulaToParse;
/*  26:    */   private FormulaParser parser;
/*  27:    */   private String formulaString;
/*  28:    */   private byte[] formulaBytes;
/*  29:    */   private CellValue copiedFrom;
/*  30:    */   
/*  31:    */   public FormulaRecord(int c, int r, String f)
/*  32:    */   {
/*  33: 84 */     super(Type.FORMULA2, c, r);
/*  34: 85 */     this.formulaToParse = f;
/*  35: 86 */     this.copiedFrom = null;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public FormulaRecord(int c, int r, String f, CellFormat st)
/*  39:    */   {
/*  40: 96 */     super(Type.FORMULA, c, r, st);
/*  41: 97 */     this.formulaToParse = f;
/*  42: 98 */     this.copiedFrom = null;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected FormulaRecord(int c, int r, FormulaRecord fr)
/*  46:    */   {
/*  47:110 */     super(Type.FORMULA, c, r, fr);
/*  48:111 */     this.copiedFrom = fr;
/*  49:112 */     this.formulaBytes = new byte[fr.formulaBytes.length];
/*  50:113 */     System.arraycopy(fr.formulaBytes, 0, this.formulaBytes, 0, this.formulaBytes.length);
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected FormulaRecord(int c, int r, ReadFormulaRecord rfr)
/*  54:    */   {
/*  55:125 */     super(Type.FORMULA, c, r, rfr);
/*  56:    */     try
/*  57:    */     {
/*  58:128 */       this.copiedFrom = rfr;
/*  59:129 */       this.formulaBytes = rfr.getFormulaBytes();
/*  60:    */     }
/*  61:    */     catch (FormulaException e)
/*  62:    */     {
/*  63:134 */       logger.error("", e);
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   private void initialize(WorkbookSettings ws, ExternalSheet es, WorkbookMethods nt)
/*  68:    */   {
/*  69:150 */     if (this.copiedFrom != null)
/*  70:    */     {
/*  71:152 */       initializeCopiedFormula(ws, es, nt);
/*  72:153 */       return;
/*  73:    */     }
/*  74:156 */     this.parser = new FormulaParser(this.formulaToParse, es, nt, ws);
/*  75:    */     try
/*  76:    */     {
/*  77:160 */       this.parser.parse();
/*  78:161 */       this.formulaString = this.parser.getFormula();
/*  79:162 */       this.formulaBytes = this.parser.getBytes();
/*  80:    */     }
/*  81:    */     catch (FormulaException e)
/*  82:    */     {
/*  83:166 */       logger.warn(
/*  84:167 */         e.getMessage() + 
/*  85:168 */         " when parsing formula " + this.formulaToParse + " in cell " + 
/*  86:169 */         getSheet().getName() + "!" + 
/*  87:170 */         CellReferenceHelper.getCellReference(getColumn(), getRow()));
/*  88:    */       try
/*  89:    */       {
/*  90:175 */         this.formulaToParse = "ERROR(1)";
/*  91:176 */         this.parser = new FormulaParser(this.formulaToParse, es, nt, ws);
/*  92:177 */         this.parser.parse();
/*  93:178 */         this.formulaString = this.parser.getFormula();
/*  94:179 */         this.formulaBytes = this.parser.getBytes();
/*  95:    */       }
/*  96:    */       catch (FormulaException e2)
/*  97:    */       {
/*  98:184 */         logger.error("", e2);
/*  99:    */       }
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   private void initializeCopiedFormula(WorkbookSettings ws, ExternalSheet es, WorkbookMethods nt)
/* 104:    */   {
/* 105:    */     try
/* 106:    */     {
/* 107:202 */       this.parser = new FormulaParser(this.formulaBytes, this, es, nt, ws);
/* 108:203 */       this.parser.parse();
/* 109:204 */       this.parser.adjustRelativeCellReferences(
/* 110:205 */         getColumn() - this.copiedFrom.getColumn(), 
/* 111:206 */         getRow() - this.copiedFrom.getRow());
/* 112:207 */       this.formulaString = this.parser.getFormula();
/* 113:208 */       this.formulaBytes = this.parser.getBytes();
/* 114:    */     }
/* 115:    */     catch (FormulaException e)
/* 116:    */     {
/* 117:    */       try
/* 118:    */       {
/* 119:215 */         this.formulaToParse = "ERROR(1)";
/* 120:216 */         this.parser = new FormulaParser(this.formulaToParse, es, nt, ws);
/* 121:217 */         this.parser.parse();
/* 122:218 */         this.formulaString = this.parser.getFormula();
/* 123:219 */         this.formulaBytes = this.parser.getBytes();
/* 124:    */       }
/* 125:    */       catch (FormulaException e2)
/* 126:    */       {
/* 127:225 */         logger.error("", e2);
/* 128:    */       }
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   void setCellDetails(FormattingRecords fr, SharedStrings ss, WritableSheetImpl s)
/* 133:    */   {
/* 134:242 */     super.setCellDetails(fr, ss, s);
/* 135:243 */     initialize(s.getWorkbookSettings(), s.getWorkbook(), s.getWorkbook());
/* 136:244 */     s.getWorkbook().addRCIRCell(this);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public byte[] getData()
/* 140:    */   {
/* 141:254 */     byte[] celldata = super.getData();
/* 142:255 */     byte[] formulaData = getFormulaData();
/* 143:256 */     byte[] data = new byte[formulaData.length + celldata.length];
/* 144:257 */     System.arraycopy(celldata, 0, data, 0, celldata.length);
/* 145:258 */     System.arraycopy(formulaData, 0, data, celldata.length, 
/* 146:259 */       formulaData.length);
/* 147:260 */     return data;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public CellType getType()
/* 151:    */   {
/* 152:270 */     return CellType.ERROR;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public String getContents()
/* 156:    */   {
/* 157:282 */     return this.formulaString;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public byte[] getFormulaData()
/* 161:    */   {
/* 162:293 */     byte[] data = new byte[this.formulaBytes.length + 16];
/* 163:294 */     System.arraycopy(this.formulaBytes, 0, data, 16, this.formulaBytes.length);
/* 164:    */     
/* 165:296 */     data[6] = 16;
/* 166:297 */     data[7] = 64;
/* 167:298 */     data[12] = -32;
/* 168:299 */     data[13] = -4; byte[] 
/* 169:    */     
/* 170:301 */       tmp54_51 = data;tmp54_51[8] = ((byte)(tmp54_51[8] | 0x2));
/* 171:    */     
/* 172:    */ 
/* 173:304 */     IntegerHelper.getTwoBytes(this.formulaBytes.length, data, 14);
/* 174:    */     
/* 175:306 */     return data;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public WritableCell copyTo(int col, int row)
/* 179:    */   {
/* 180:319 */     Assert.verify(false);
/* 181:320 */     return null;
/* 182:    */   }
/* 183:    */   
/* 184:    */   void columnInserted(Sheet s, int sheetIndex, int col)
/* 185:    */   {
/* 186:333 */     this.parser.columnInserted(sheetIndex, col, s == getSheet());
/* 187:334 */     this.formulaBytes = this.parser.getBytes();
/* 188:    */   }
/* 189:    */   
/* 190:    */   void columnRemoved(Sheet s, int sheetIndex, int col)
/* 191:    */   {
/* 192:347 */     this.parser.columnRemoved(sheetIndex, col, s == getSheet());
/* 193:348 */     this.formulaBytes = this.parser.getBytes();
/* 194:    */   }
/* 195:    */   
/* 196:    */   void rowInserted(Sheet s, int sheetIndex, int row)
/* 197:    */   {
/* 198:361 */     this.parser.rowInserted(sheetIndex, row, s == getSheet());
/* 199:362 */     this.formulaBytes = this.parser.getBytes();
/* 200:    */   }
/* 201:    */   
/* 202:    */   void rowRemoved(Sheet s, int sheetIndex, int row)
/* 203:    */   {
/* 204:375 */     this.parser.rowRemoved(sheetIndex, row, s == getSheet());
/* 205:376 */     this.formulaBytes = this.parser.getBytes();
/* 206:    */   }
/* 207:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.FormulaRecord
 * JD-Core Version:    0.7.0.1
 */