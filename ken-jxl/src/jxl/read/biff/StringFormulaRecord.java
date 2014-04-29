/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.CellType;
/*   4:    */ import jxl.LabelCell;
/*   5:    */ import jxl.StringFormulaCell;
/*   6:    */ import jxl.WorkbookSettings;
/*   7:    */ import jxl.biff.FormattingRecords;
/*   8:    */ import jxl.biff.FormulaData;
/*   9:    */ import jxl.biff.IntegerHelper;
/*  10:    */ import jxl.biff.StringHelper;
/*  11:    */ import jxl.biff.Type;
/*  12:    */ import jxl.biff.WorkbookMethods;
/*  13:    */ import jxl.biff.formula.ExternalSheet;
/*  14:    */ import jxl.biff.formula.FormulaException;
/*  15:    */ import jxl.biff.formula.FormulaParser;
/*  16:    */ import jxl.common.Assert;
/*  17:    */ import jxl.common.Logger;
/*  18:    */ 
/*  19:    */ class StringFormulaRecord
/*  20:    */   extends CellValue
/*  21:    */   implements LabelCell, FormulaData, StringFormulaCell
/*  22:    */ {
/*  23: 49 */   private static Logger logger = Logger.getLogger(StringFormulaRecord.class);
/*  24:    */   private String value;
/*  25:    */   private ExternalSheet externalSheet;
/*  26:    */   private WorkbookMethods nameTable;
/*  27:    */   private String formulaString;
/*  28:    */   private byte[] data;
/*  29:    */   
/*  30:    */   public StringFormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, WorkbookSettings ws)
/*  31:    */   {
/*  32: 95 */     super(t, fr, si);
/*  33:    */     
/*  34: 97 */     this.externalSheet = es;
/*  35: 98 */     this.nameTable = nt;
/*  36:    */     
/*  37:100 */     this.data = getRecord().getData();
/*  38:    */     
/*  39:102 */     int pos = excelFile.getPos();
/*  40:    */     
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44:107 */     Record nextRecord = excelFile.next();
/*  45:108 */     int count = 0;
/*  46:109 */     while ((nextRecord.getType() != Type.STRING) && (count < 4))
/*  47:    */     {
/*  48:111 */       nextRecord = excelFile.next();
/*  49:112 */       count++;
/*  50:    */     }
/*  51:114 */     Assert.verify(count < 4, " @ " + pos);
/*  52:115 */     byte[] stringData = nextRecord.getData();
/*  53:    */     
/*  54:    */ 
/*  55:118 */     nextRecord = excelFile.peek();
/*  56:119 */     while (nextRecord.getType() == Type.CONTINUE)
/*  57:    */     {
/*  58:121 */       nextRecord = excelFile.next();
/*  59:122 */       byte[] d = new byte[stringData.length + nextRecord.getLength() - 1];
/*  60:123 */       System.arraycopy(stringData, 0, d, 0, stringData.length);
/*  61:124 */       System.arraycopy(nextRecord.getData(), 1, d, 
/*  62:125 */         stringData.length, nextRecord.getLength() - 1);
/*  63:126 */       stringData = d;
/*  64:127 */       nextRecord = excelFile.peek();
/*  65:    */     }
/*  66:129 */     readString(stringData, ws);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public StringFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si)
/*  70:    */   {
/*  71:149 */     super(t, fr, si);
/*  72:    */     
/*  73:151 */     this.externalSheet = es;
/*  74:152 */     this.nameTable = nt;
/*  75:    */     
/*  76:154 */     this.data = getRecord().getData();
/*  77:155 */     this.value = "";
/*  78:    */   }
/*  79:    */   
/*  80:    */   private void readString(byte[] d, WorkbookSettings ws)
/*  81:    */   {
/*  82:167 */     int pos = 0;
/*  83:168 */     int chars = IntegerHelper.getInt(d[0], d[1]);
/*  84:170 */     if (chars == 0)
/*  85:    */     {
/*  86:172 */       this.value = "";
/*  87:173 */       return;
/*  88:    */     }
/*  89:175 */     pos += 2;
/*  90:176 */     int optionFlags = d[pos];
/*  91:177 */     pos++;
/*  92:179 */     if ((optionFlags & 0xF) != optionFlags)
/*  93:    */     {
/*  94:183 */       pos = 0;
/*  95:184 */       chars = IntegerHelper.getInt(d[0], (byte)0);
/*  96:185 */       optionFlags = d[1];
/*  97:186 */       pos = 2;
/*  98:    */     }
/*  99:190 */     boolean extendedString = (optionFlags & 0x4) != 0;
/* 100:    */     
/* 101:    */ 
/* 102:193 */     boolean richString = (optionFlags & 0x8) != 0;
/* 103:195 */     if (richString) {
/* 104:197 */       pos += 2;
/* 105:    */     }
/* 106:200 */     if (extendedString) {
/* 107:202 */       pos += 4;
/* 108:    */     }
/* 109:206 */     boolean asciiEncoding = (optionFlags & 0x1) == 0;
/* 110:208 */     if (asciiEncoding) {
/* 111:210 */       this.value = StringHelper.getString(d, chars, pos, ws);
/* 112:    */     } else {
/* 113:214 */       this.value = StringHelper.getUnicodeString(d, chars, pos);
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public String getContents()
/* 118:    */   {
/* 119:225 */     return this.value;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public String getString()
/* 123:    */   {
/* 124:235 */     return this.value;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public CellType getType()
/* 128:    */   {
/* 129:245 */     return CellType.STRING_FORMULA;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public byte[] getFormulaData()
/* 133:    */     throws FormulaException
/* 134:    */   {
/* 135:256 */     if (!getSheet().getWorkbook().getWorkbookBof().isBiff8()) {
/* 136:258 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/* 137:    */     }
/* 138:262 */     byte[] d = new byte[this.data.length - 6];
/* 139:263 */     System.arraycopy(this.data, 6, d, 0, this.data.length - 6);
/* 140:    */     
/* 141:265 */     return d;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public String getFormula()
/* 145:    */     throws FormulaException
/* 146:    */   {
/* 147:276 */     if (this.formulaString == null)
/* 148:    */     {
/* 149:278 */       byte[] tokens = new byte[this.data.length - 22];
/* 150:279 */       System.arraycopy(this.data, 22, tokens, 0, tokens.length);
/* 151:280 */       FormulaParser fp = new FormulaParser(
/* 152:281 */         tokens, this, this.externalSheet, this.nameTable, 
/* 153:282 */         getSheet().getWorkbook().getSettings());
/* 154:283 */       fp.parse();
/* 155:284 */       this.formulaString = fp.getFormula();
/* 156:    */     }
/* 157:287 */     return this.formulaString;
/* 158:    */   }
/* 159:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.StringFormulaRecord
 * JD-Core Version:    0.7.0.1
 */