/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.BooleanCell;
/*   4:    */ import jxl.BooleanFormulaCell;
/*   5:    */ import jxl.CellType;
/*   6:    */ import jxl.biff.FormattingRecords;
/*   7:    */ import jxl.biff.FormulaData;
/*   8:    */ import jxl.biff.WorkbookMethods;
/*   9:    */ import jxl.biff.formula.ExternalSheet;
/*  10:    */ import jxl.biff.formula.FormulaException;
/*  11:    */ import jxl.biff.formula.FormulaParser;
/*  12:    */ import jxl.common.Assert;
/*  13:    */ 
/*  14:    */ class BooleanFormulaRecord
/*  15:    */   extends CellValue
/*  16:    */   implements BooleanCell, FormulaData, BooleanFormulaCell
/*  17:    */ {
/*  18:    */   private boolean value;
/*  19:    */   private ExternalSheet externalSheet;
/*  20:    */   private WorkbookMethods nameTable;
/*  21:    */   private String formulaString;
/*  22:    */   private byte[] data;
/*  23:    */   
/*  24:    */   public BooleanFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si)
/*  25:    */   {
/*  26: 79 */     super(t, fr, si);
/*  27: 80 */     this.externalSheet = es;
/*  28: 81 */     this.nameTable = nt;
/*  29: 82 */     this.value = false;
/*  30:    */     
/*  31: 84 */     this.data = getRecord().getData();
/*  32:    */     
/*  33: 86 */     Assert.verify(this.data[6] != 2);
/*  34:    */     
/*  35: 88 */     this.value = (this.data[8] == 1);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean getValue()
/*  39:    */   {
/*  40:101 */     return this.value;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getContents()
/*  44:    */   {
/*  45:112 */     return new Boolean(this.value).toString();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public CellType getType()
/*  49:    */   {
/*  50:122 */     return CellType.BOOLEAN_FORMULA;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public byte[] getFormulaData()
/*  54:    */     throws FormulaException
/*  55:    */   {
/*  56:133 */     if (!getSheet().getWorkbookBof().isBiff8()) {
/*  57:135 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  58:    */     }
/*  59:139 */     byte[] d = new byte[this.data.length - 6];
/*  60:140 */     System.arraycopy(this.data, 6, d, 0, this.data.length - 6);
/*  61:    */     
/*  62:142 */     return d;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String getFormula()
/*  66:    */     throws FormulaException
/*  67:    */   {
/*  68:153 */     if (this.formulaString == null)
/*  69:    */     {
/*  70:155 */       byte[] tokens = new byte[this.data.length - 22];
/*  71:156 */       System.arraycopy(this.data, 22, tokens, 0, tokens.length);
/*  72:157 */       FormulaParser fp = new FormulaParser(
/*  73:158 */         tokens, this, this.externalSheet, this.nameTable, 
/*  74:159 */         getSheet().getWorkbook().getSettings());
/*  75:160 */       fp.parse();
/*  76:161 */       this.formulaString = fp.getFormula();
/*  77:    */     }
/*  78:164 */     return this.formulaString;
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.BooleanFormulaRecord
 * JD-Core Version:    0.7.0.1
 */