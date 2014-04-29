/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.CellType;
/*   4:    */ import jxl.ErrorCell;
/*   5:    */ import jxl.ErrorFormulaCell;
/*   6:    */ import jxl.biff.FormattingRecords;
/*   7:    */ import jxl.biff.FormulaData;
/*   8:    */ import jxl.biff.WorkbookMethods;
/*   9:    */ import jxl.biff.formula.ExternalSheet;
/*  10:    */ import jxl.biff.formula.FormulaErrorCode;
/*  11:    */ import jxl.biff.formula.FormulaException;
/*  12:    */ import jxl.biff.formula.FormulaParser;
/*  13:    */ import jxl.common.Assert;
/*  14:    */ 
/*  15:    */ class ErrorFormulaRecord
/*  16:    */   extends CellValue
/*  17:    */   implements ErrorCell, FormulaData, ErrorFormulaCell
/*  18:    */ {
/*  19:    */   private int errorCode;
/*  20:    */   private ExternalSheet externalSheet;
/*  21:    */   private WorkbookMethods nameTable;
/*  22:    */   private String formulaString;
/*  23:    */   private byte[] data;
/*  24:    */   private FormulaErrorCode error;
/*  25:    */   
/*  26:    */   public ErrorFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si)
/*  27:    */   {
/*  28: 84 */     super(t, fr, si);
/*  29:    */     
/*  30: 86 */     this.externalSheet = es;
/*  31: 87 */     this.nameTable = nt;
/*  32: 88 */     this.data = getRecord().getData();
/*  33:    */     
/*  34: 90 */     Assert.verify(this.data[6] == 2);
/*  35:    */     
/*  36: 92 */     this.errorCode = this.data[8];
/*  37:    */   }
/*  38:    */   
/*  39:    */   public int getErrorCode()
/*  40:    */   {
/*  41:104 */     return this.errorCode;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getContents()
/*  45:    */   {
/*  46:114 */     if (this.error == null) {
/*  47:116 */       this.error = FormulaErrorCode.getErrorCode(this.errorCode);
/*  48:    */     }
/*  49:119 */     return 
/*  50:120 */       "ERROR " + this.errorCode;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public CellType getType()
/*  54:    */   {
/*  55:130 */     return CellType.FORMULA_ERROR;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public byte[] getFormulaData()
/*  59:    */     throws FormulaException
/*  60:    */   {
/*  61:141 */     if (!getSheet().getWorkbookBof().isBiff8()) {
/*  62:143 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  63:    */     }
/*  64:147 */     byte[] d = new byte[this.data.length - 6];
/*  65:148 */     System.arraycopy(this.data, 6, d, 0, this.data.length - 6);
/*  66:    */     
/*  67:150 */     return d;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String getFormula()
/*  71:    */     throws FormulaException
/*  72:    */   {
/*  73:161 */     if (this.formulaString == null)
/*  74:    */     {
/*  75:163 */       byte[] tokens = new byte[this.data.length - 22];
/*  76:164 */       System.arraycopy(this.data, 22, tokens, 0, tokens.length);
/*  77:165 */       FormulaParser fp = new FormulaParser(
/*  78:166 */         tokens, this, this.externalSheet, this.nameTable, 
/*  79:167 */         getSheet().getWorkbook().getSettings());
/*  80:168 */       fp.parse();
/*  81:169 */       this.formulaString = fp.getFormula();
/*  82:    */     }
/*  83:172 */     return this.formulaString;
/*  84:    */   }
/*  85:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.ErrorFormulaRecord
 * JD-Core Version:    0.7.0.1
 */