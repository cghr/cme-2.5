/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.CellType;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.biff.DoubleHelper;
/*   6:    */ import jxl.biff.FormattingRecords;
/*   7:    */ import jxl.biff.IntegerHelper;
/*   8:    */ import jxl.biff.WorkbookMethods;
/*   9:    */ import jxl.biff.formula.ExternalSheet;
/*  10:    */ import jxl.common.Assert;
/*  11:    */ import jxl.common.Logger;
/*  12:    */ 
/*  13:    */ class FormulaRecord
/*  14:    */   extends CellValue
/*  15:    */ {
/*  16: 41 */   private static Logger logger = Logger.getLogger(FormulaRecord.class);
/*  17:    */   private CellValue formula;
/*  18:    */   private boolean shared;
/*  19: 59 */   public static final IgnoreSharedFormula ignoreSharedFormula = new IgnoreSharedFormula(null);
/*  20:    */   
/*  21:    */   public FormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, WorkbookSettings ws)
/*  22:    */   {
/*  23: 82 */     super(t, fr, si);
/*  24:    */     
/*  25: 84 */     byte[] data = getRecord().getData();
/*  26:    */     
/*  27: 86 */     this.shared = false;
/*  28:    */     
/*  29:    */ 
/*  30: 89 */     int grbit = IntegerHelper.getInt(data[14], data[15]);
/*  31: 90 */     if ((grbit & 0x8) != 0)
/*  32:    */     {
/*  33: 92 */       this.shared = true;
/*  34: 94 */       if ((data[6] == 0) && (data[12] == -1) && (data[13] == -1))
/*  35:    */       {
/*  36: 97 */         this.formula = new SharedStringFormulaRecord(
/*  37: 98 */           t, excelFile, fr, es, nt, si, ws);
/*  38:    */       }
/*  39:100 */       else if ((data[6] == 3) && (data[12] == -1) && (data[13] == -1))
/*  40:    */       {
/*  41:103 */         this.formula = new SharedStringFormulaRecord(
/*  42:104 */           t, excelFile, fr, es, nt, si, 
/*  43:105 */           SharedStringFormulaRecord.EMPTY_STRING);
/*  44:    */       }
/*  45:107 */       else if ((data[6] == 2) && 
/*  46:108 */         (data[12] == -1) && 
/*  47:109 */         (data[13] == -1))
/*  48:    */       {
/*  49:112 */         int errorCode = data[8];
/*  50:113 */         this.formula = new SharedErrorFormulaRecord(t, excelFile, errorCode, 
/*  51:114 */           fr, es, nt, si);
/*  52:    */       }
/*  53:116 */       else if ((data[6] == 1) && 
/*  54:117 */         (data[12] == -1) && 
/*  55:118 */         (data[13] == -1))
/*  56:    */       {
/*  57:120 */         boolean value = data[8] == 1;
/*  58:121 */         this.formula = new SharedBooleanFormulaRecord(
/*  59:122 */           t, excelFile, value, fr, es, nt, si);
/*  60:    */       }
/*  61:    */       else
/*  62:    */       {
/*  63:127 */         double value = DoubleHelper.getIEEEDouble(data, 6);
/*  64:128 */         SharedNumberFormulaRecord snfr = new SharedNumberFormulaRecord(
/*  65:129 */           t, excelFile, value, fr, es, nt, si);
/*  66:130 */         snfr.setNumberFormat(fr.getNumberFormat(getXFIndex()));
/*  67:131 */         this.formula = snfr;
/*  68:    */       }
/*  69:134 */       return;
/*  70:    */     }
/*  71:139 */     if ((data[6] == 0) && (data[12] == -1) && (data[13] == -1)) {
/*  72:142 */       this.formula = new StringFormulaRecord(t, excelFile, fr, es, nt, si, ws);
/*  73:144 */     } else if ((data[6] == 1) && 
/*  74:145 */       (data[12] == -1) && 
/*  75:146 */       (data[13] == -1)) {
/*  76:150 */       this.formula = new BooleanFormulaRecord(t, fr, es, nt, si);
/*  77:152 */     } else if ((data[6] == 2) && 
/*  78:153 */       (data[12] == -1) && 
/*  79:154 */       (data[13] == -1)) {
/*  80:157 */       this.formula = new ErrorFormulaRecord(t, fr, es, nt, si);
/*  81:159 */     } else if ((data[6] == 3) && (data[12] == -1) && (data[13] == -1)) {
/*  82:162 */       this.formula = new StringFormulaRecord(t, fr, es, nt, si);
/*  83:    */     } else {
/*  84:167 */       this.formula = new NumberFormulaRecord(t, fr, es, nt, si);
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   public FormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, IgnoreSharedFormula i, SheetImpl si, WorkbookSettings ws)
/*  89:    */   {
/*  90:195 */     super(t, fr, si);
/*  91:196 */     byte[] data = getRecord().getData();
/*  92:    */     
/*  93:198 */     this.shared = false;
/*  94:202 */     if ((data[6] == 0) && (data[12] == -1) && (data[13] == -1)) {
/*  95:205 */       this.formula = new StringFormulaRecord(t, excelFile, fr, es, nt, si, ws);
/*  96:207 */     } else if ((data[6] == 1) && 
/*  97:208 */       (data[12] == -1) && 
/*  98:209 */       (data[13] == -1)) {
/*  99:213 */       this.formula = new BooleanFormulaRecord(t, fr, es, nt, si);
/* 100:215 */     } else if ((data[6] == 2) && 
/* 101:216 */       (data[12] == -1) && 
/* 102:217 */       (data[13] == -1)) {
/* 103:220 */       this.formula = new ErrorFormulaRecord(t, fr, es, nt, si);
/* 104:    */     } else {
/* 105:225 */       this.formula = new NumberFormulaRecord(t, fr, es, nt, si);
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   public String getContents()
/* 110:    */   {
/* 111:236 */     Assert.verify(false);
/* 112:237 */     return "";
/* 113:    */   }
/* 114:    */   
/* 115:    */   public CellType getType()
/* 116:    */   {
/* 117:247 */     Assert.verify(false);
/* 118:248 */     return CellType.EMPTY;
/* 119:    */   }
/* 120:    */   
/* 121:    */   final CellValue getFormula()
/* 122:    */   {
/* 123:258 */     return this.formula;
/* 124:    */   }
/* 125:    */   
/* 126:    */   final boolean isShared()
/* 127:    */   {
/* 128:269 */     return this.shared;
/* 129:    */   }
/* 130:    */   
/* 131:    */   private static class IgnoreSharedFormula {}
/* 132:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.FormulaRecord
 * JD-Core Version:    0.7.0.1
 */