/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import jxl.CellType;
/*   5:    */ import jxl.DateCell;
/*   6:    */ import jxl.DateFormulaCell;
/*   7:    */ import jxl.biff.FormattingRecords;
/*   8:    */ import jxl.biff.FormulaData;
/*   9:    */ import jxl.biff.WorkbookMethods;
/*  10:    */ import jxl.biff.formula.ExternalSheet;
/*  11:    */ import jxl.biff.formula.FormulaException;
/*  12:    */ import jxl.biff.formula.FormulaParser;
/*  13:    */ 
/*  14:    */ class DateFormulaRecord
/*  15:    */   extends DateRecord
/*  16:    */   implements DateCell, FormulaData, DateFormulaCell
/*  17:    */ {
/*  18:    */   private String formulaString;
/*  19:    */   private ExternalSheet externalSheet;
/*  20:    */   private WorkbookMethods nameTable;
/*  21:    */   private byte[] data;
/*  22:    */   
/*  23:    */   public DateFormulaRecord(NumberFormulaRecord t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, boolean nf, SheetImpl si)
/*  24:    */     throws FormulaException
/*  25:    */   {
/*  26: 74 */     super(t, t.getXFIndex(), fr, nf, si);
/*  27:    */     
/*  28: 76 */     this.externalSheet = es;
/*  29: 77 */     this.nameTable = nt;
/*  30: 78 */     this.data = t.getFormulaData();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public CellType getType()
/*  34:    */   {
/*  35: 88 */     return CellType.DATE_FORMULA;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public byte[] getFormulaData()
/*  39:    */     throws FormulaException
/*  40:    */   {
/*  41: 99 */     if (!getSheet().getWorkbookBof().isBiff8()) {
/*  42:101 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  43:    */     }
/*  44:105 */     return this.data;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getFormula()
/*  48:    */     throws FormulaException
/*  49:    */   {
/*  50:118 */     if (this.formulaString == null)
/*  51:    */     {
/*  52:120 */       byte[] tokens = new byte[this.data.length - 16];
/*  53:121 */       System.arraycopy(this.data, 16, tokens, 0, tokens.length);
/*  54:122 */       FormulaParser fp = new FormulaParser(
/*  55:123 */         tokens, this, this.externalSheet, this.nameTable, 
/*  56:124 */         getSheet().getWorkbook().getSettings());
/*  57:125 */       fp.parse();
/*  58:126 */       this.formulaString = fp.getFormula();
/*  59:    */     }
/*  60:129 */     return this.formulaString;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public double getValue()
/*  64:    */   {
/*  65:139 */     return 0.0D;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public NumberFormat getNumberFormat()
/*  69:    */   {
/*  70:149 */     return null;
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.DateFormulaRecord
 * JD-Core Version:    0.7.0.1
 */