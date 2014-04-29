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
/*  19:    */ public class SharedStringFormulaRecord
/*  20:    */   extends BaseSharedFormulaRecord
/*  21:    */   implements LabelCell, FormulaData, StringFormulaCell
/*  22:    */ {
/*  23: 49 */   private static Logger logger = Logger.getLogger(
/*  24: 50 */     SharedStringFormulaRecord.class);
/*  25:    */   private String value;
/*  26: 60 */   protected static final EmptyString EMPTY_STRING = new EmptyString(null);
/*  27:    */   
/*  28:    */   public SharedStringFormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, WorkbookSettings ws)
/*  29:    */   {
/*  30: 81 */     super(t, fr, es, nt, si, excelFile.getPos());
/*  31: 82 */     int pos = excelFile.getPos();
/*  32:    */     
/*  33:    */ 
/*  34: 85 */     int filepos = excelFile.getPos();
/*  35:    */     
/*  36:    */ 
/*  37:    */ 
/*  38: 89 */     Record nextRecord = excelFile.next();
/*  39: 90 */     int count = 0;
/*  40: 91 */     while ((nextRecord.getType() != Type.STRING) && (count < 4))
/*  41:    */     {
/*  42: 93 */       nextRecord = excelFile.next();
/*  43: 94 */       count++;
/*  44:    */     }
/*  45: 96 */     Assert.verify(count < 4, " @ " + pos);
/*  46:    */     
/*  47: 98 */     byte[] stringData = nextRecord.getData();
/*  48:    */     
/*  49:    */ 
/*  50:101 */     nextRecord = excelFile.peek();
/*  51:102 */     while (nextRecord.getType() == Type.CONTINUE)
/*  52:    */     {
/*  53:104 */       nextRecord = excelFile.next();
/*  54:105 */       byte[] d = new byte[stringData.length + nextRecord.getLength() - 1];
/*  55:106 */       System.arraycopy(stringData, 0, d, 0, stringData.length);
/*  56:107 */       System.arraycopy(nextRecord.getData(), 1, d, 
/*  57:108 */         stringData.length, nextRecord.getLength() - 1);
/*  58:109 */       stringData = d;
/*  59:110 */       nextRecord = excelFile.peek();
/*  60:    */     }
/*  61:113 */     int chars = IntegerHelper.getInt(stringData[0], stringData[1]);
/*  62:    */     
/*  63:115 */     boolean unicode = false;
/*  64:116 */     int startpos = 3;
/*  65:117 */     if (stringData.length == chars + 2)
/*  66:    */     {
/*  67:121 */       startpos = 2;
/*  68:122 */       unicode = false;
/*  69:    */     }
/*  70:124 */     else if (stringData[2] == 1)
/*  71:    */     {
/*  72:127 */       startpos = 3;
/*  73:128 */       unicode = true;
/*  74:    */     }
/*  75:    */     else
/*  76:    */     {
/*  77:133 */       startpos = 3;
/*  78:134 */       unicode = false;
/*  79:    */     }
/*  80:137 */     if (!unicode) {
/*  81:139 */       this.value = StringHelper.getString(stringData, chars, startpos, ws);
/*  82:    */     } else {
/*  83:143 */       this.value = StringHelper.getUnicodeString(stringData, chars, startpos);
/*  84:    */     }
/*  85:148 */     excelFile.setPos(filepos);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public SharedStringFormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, EmptyString dummy)
/*  89:    */   {
/*  90:170 */     super(t, fr, es, nt, si, excelFile.getPos());
/*  91:    */     
/*  92:172 */     this.value = "";
/*  93:    */   }
/*  94:    */   
/*  95:    */   public String getString()
/*  96:    */   {
/*  97:182 */     return this.value;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String getContents()
/* 101:    */   {
/* 102:192 */     return this.value;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public CellType getType()
/* 106:    */   {
/* 107:202 */     return CellType.STRING_FORMULA;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public byte[] getFormulaData()
/* 111:    */     throws FormulaException
/* 112:    */   {
/* 113:214 */     if (!getSheet().getWorkbookBof().isBiff8()) {
/* 114:216 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/* 115:    */     }
/* 116:221 */     FormulaParser fp = new FormulaParser(
/* 117:222 */       getTokens(), this, 
/* 118:223 */       getExternalSheet(), getNameTable(), 
/* 119:224 */       getSheet().getWorkbook().getSettings());
/* 120:225 */     fp.parse();
/* 121:226 */     byte[] rpnTokens = fp.getBytes();
/* 122:    */     
/* 123:228 */     byte[] data = new byte[rpnTokens.length + 22];
/* 124:    */     
/* 125:    */ 
/* 126:231 */     IntegerHelper.getTwoBytes(getRow(), data, 0);
/* 127:232 */     IntegerHelper.getTwoBytes(getColumn(), data, 2);
/* 128:233 */     IntegerHelper.getTwoBytes(getXFIndex(), data, 4);
/* 129:    */     
/* 130:    */ 
/* 131:    */ 
/* 132:237 */     data[6] = 0;
/* 133:238 */     data[12] = -1;
/* 134:239 */     data[13] = -1;
/* 135:    */     
/* 136:    */ 
/* 137:242 */     System.arraycopy(rpnTokens, 0, data, 22, rpnTokens.length);
/* 138:243 */     IntegerHelper.getTwoBytes(rpnTokens.length, data, 20);
/* 139:    */     
/* 140:    */ 
/* 141:246 */     byte[] d = new byte[data.length - 6];
/* 142:247 */     System.arraycopy(data, 6, d, 0, data.length - 6);
/* 143:    */     
/* 144:249 */     return d;
/* 145:    */   }
/* 146:    */   
/* 147:    */   private static final class EmptyString {}
/* 148:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SharedStringFormulaRecord
 * JD-Core Version:    0.7.0.1
 */