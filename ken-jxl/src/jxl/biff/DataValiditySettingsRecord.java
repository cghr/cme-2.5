/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.formula.ExternalSheet;
/*   5:    */ import jxl.biff.formula.FormulaException;
/*   6:    */ import jxl.common.Assert;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ import jxl.read.biff.Record;
/*   9:    */ 
/*  10:    */ public class DataValiditySettingsRecord
/*  11:    */   extends WritableRecordData
/*  12:    */ {
/*  13: 41 */   private static Logger logger = Logger.getLogger(DataValiditySettingsRecord.class);
/*  14:    */   private byte[] data;
/*  15:    */   private DVParser dvParser;
/*  16:    */   private WorkbookMethods workbook;
/*  17:    */   private ExternalSheet externalSheet;
/*  18:    */   private WorkbookSettings workbookSettings;
/*  19:    */   private DataValidation dataValidation;
/*  20:    */   
/*  21:    */   public DataValiditySettingsRecord(Record t, ExternalSheet es, WorkbookMethods wm, WorkbookSettings ws)
/*  22:    */   {
/*  23: 81 */     super(t);
/*  24:    */     
/*  25: 83 */     this.data = t.getData();
/*  26: 84 */     this.externalSheet = es;
/*  27: 85 */     this.workbook = wm;
/*  28: 86 */     this.workbookSettings = ws;
/*  29:    */   }
/*  30:    */   
/*  31:    */   DataValiditySettingsRecord(DataValiditySettingsRecord dvsr)
/*  32:    */   {
/*  33: 94 */     super(Type.DV);
/*  34:    */     
/*  35: 96 */     this.data = dvsr.getData();
/*  36:    */   }
/*  37:    */   
/*  38:    */   DataValiditySettingsRecord(DataValiditySettingsRecord dvsr, ExternalSheet es, WorkbookMethods w, WorkbookSettings ws)
/*  39:    */   {
/*  40:109 */     super(Type.DV);
/*  41:    */     
/*  42:111 */     this.workbook = w;
/*  43:112 */     this.externalSheet = es;
/*  44:113 */     this.workbookSettings = ws;
/*  45:    */     
/*  46:115 */     Assert.verify(w != null);
/*  47:116 */     Assert.verify(es != null);
/*  48:    */     
/*  49:118 */     this.data = new byte[dvsr.data.length];
/*  50:119 */     System.arraycopy(dvsr.data, 0, this.data, 0, this.data.length);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public DataValiditySettingsRecord(DVParser dvp)
/*  54:    */   {
/*  55:129 */     super(Type.DV);
/*  56:130 */     this.dvParser = dvp;
/*  57:    */   }
/*  58:    */   
/*  59:    */   private void initialize()
/*  60:    */   {
/*  61:138 */     if (this.dvParser == null) {
/*  62:140 */       this.dvParser = new DVParser(this.data, this.externalSheet, 
/*  63:141 */         this.workbook, this.workbookSettings);
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public byte[] getData()
/*  68:    */   {
/*  69:152 */     if (this.dvParser == null) {
/*  70:154 */       return this.data;
/*  71:    */     }
/*  72:157 */     return this.dvParser.getData();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void insertRow(int row)
/*  76:    */   {
/*  77:167 */     if (this.dvParser == null) {
/*  78:169 */       initialize();
/*  79:    */     }
/*  80:172 */     this.dvParser.insertRow(row);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void removeRow(int row)
/*  84:    */   {
/*  85:182 */     if (this.dvParser == null) {
/*  86:184 */       initialize();
/*  87:    */     }
/*  88:187 */     this.dvParser.removeRow(row);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void insertColumn(int col)
/*  92:    */   {
/*  93:197 */     if (this.dvParser == null) {
/*  94:199 */       initialize();
/*  95:    */     }
/*  96:202 */     this.dvParser.insertColumn(col);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void removeColumn(int col)
/* 100:    */   {
/* 101:212 */     if (this.dvParser == null) {
/* 102:214 */       initialize();
/* 103:    */     }
/* 104:217 */     this.dvParser.removeColumn(col);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public int getFirstColumn()
/* 108:    */   {
/* 109:227 */     if (this.dvParser == null) {
/* 110:229 */       initialize();
/* 111:    */     }
/* 112:232 */     return this.dvParser.getFirstColumn();
/* 113:    */   }
/* 114:    */   
/* 115:    */   public int getLastColumn()
/* 116:    */   {
/* 117:242 */     if (this.dvParser == null) {
/* 118:244 */       initialize();
/* 119:    */     }
/* 120:247 */     return this.dvParser.getLastColumn();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public int getFirstRow()
/* 124:    */   {
/* 125:257 */     if (this.dvParser == null) {
/* 126:259 */       initialize();
/* 127:    */     }
/* 128:262 */     return this.dvParser.getFirstRow();
/* 129:    */   }
/* 130:    */   
/* 131:    */   public int getLastRow()
/* 132:    */   {
/* 133:272 */     if (this.dvParser == null) {
/* 134:274 */       initialize();
/* 135:    */     }
/* 136:277 */     return this.dvParser.getLastRow();
/* 137:    */   }
/* 138:    */   
/* 139:    */   void setDataValidation(DataValidation dv)
/* 140:    */   {
/* 141:287 */     this.dataValidation = dv;
/* 142:    */   }
/* 143:    */   
/* 144:    */   DVParser getDVParser()
/* 145:    */   {
/* 146:296 */     return this.dvParser;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public String getValidationFormula()
/* 150:    */   {
/* 151:    */     try
/* 152:    */     {
/* 153:303 */       if (this.dvParser == null) {
/* 154:305 */         initialize();
/* 155:    */       }
/* 156:308 */       return this.dvParser.getValidationFormula();
/* 157:    */     }
/* 158:    */     catch (FormulaException e)
/* 159:    */     {
/* 160:312 */       logger.warn("Cannot read drop down range " + e.getMessage());
/* 161:    */     }
/* 162:313 */     return "";
/* 163:    */   }
/* 164:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.DataValiditySettingsRecord
 * JD-Core Version:    0.7.0.1
 */