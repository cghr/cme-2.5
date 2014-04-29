/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import jxl.CellType;
/*   6:    */ import jxl.NumberCell;
/*   7:    */ import jxl.NumberFormulaCell;
/*   8:    */ import jxl.biff.DoubleHelper;
/*   9:    */ import jxl.biff.FormattingRecords;
/*  10:    */ import jxl.biff.FormulaData;
/*  11:    */ import jxl.biff.WorkbookMethods;
/*  12:    */ import jxl.biff.formula.ExternalSheet;
/*  13:    */ import jxl.biff.formula.FormulaException;
/*  14:    */ import jxl.biff.formula.FormulaParser;
/*  15:    */ import jxl.common.Logger;
/*  16:    */ 
/*  17:    */ class NumberFormulaRecord
/*  18:    */   extends CellValue
/*  19:    */   implements NumberCell, FormulaData, NumberFormulaCell
/*  20:    */ {
/*  21: 47 */   private static Logger logger = Logger.getLogger(NumberFormulaRecord.class);
/*  22:    */   private double value;
/*  23:    */   private NumberFormat format;
/*  24: 63 */   private static final DecimalFormat defaultFormat = new DecimalFormat("#.###");
/*  25:    */   private String formulaString;
/*  26:    */   private ExternalSheet externalSheet;
/*  27:    */   private WorkbookMethods nameTable;
/*  28:    */   private byte[] data;
/*  29:    */   
/*  30:    */   public NumberFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si)
/*  31:    */   {
/*  32: 98 */     super(t, fr, si);
/*  33:    */     
/*  34:100 */     this.externalSheet = es;
/*  35:101 */     this.nameTable = nt;
/*  36:102 */     this.data = getRecord().getData();
/*  37:    */     
/*  38:104 */     this.format = fr.getNumberFormat(getXFIndex());
/*  39:106 */     if (this.format == null) {
/*  40:108 */       this.format = defaultFormat;
/*  41:    */     }
/*  42:111 */     this.value = DoubleHelper.getIEEEDouble(this.data, 6);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public double getValue()
/*  46:    */   {
/*  47:121 */     return this.value;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String getContents()
/*  51:    */   {
/*  52:131 */     return !Double.isNaN(this.value) ? this.format.format(this.value) : "";
/*  53:    */   }
/*  54:    */   
/*  55:    */   public CellType getType()
/*  56:    */   {
/*  57:141 */     return CellType.NUMBER_FORMULA;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public byte[] getFormulaData()
/*  61:    */     throws FormulaException
/*  62:    */   {
/*  63:152 */     if (!getSheet().getWorkbookBof().isBiff8()) {
/*  64:154 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  65:    */     }
/*  66:158 */     byte[] d = new byte[this.data.length - 6];
/*  67:159 */     System.arraycopy(this.data, 6, d, 0, this.data.length - 6);
/*  68:    */     
/*  69:161 */     return d;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String getFormula()
/*  73:    */     throws FormulaException
/*  74:    */   {
/*  75:172 */     if (this.formulaString == null)
/*  76:    */     {
/*  77:174 */       byte[] tokens = new byte[this.data.length - 22];
/*  78:175 */       System.arraycopy(this.data, 22, tokens, 0, tokens.length);
/*  79:176 */       FormulaParser fp = new FormulaParser(
/*  80:177 */         tokens, this, this.externalSheet, this.nameTable, 
/*  81:178 */         getSheet().getWorkbook().getSettings());
/*  82:179 */       fp.parse();
/*  83:180 */       this.formulaString = fp.getFormula();
/*  84:    */     }
/*  85:183 */     return this.formulaString;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public NumberFormat getNumberFormat()
/*  89:    */   {
/*  90:194 */     return this.format;
/*  91:    */   }
/*  92:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.NumberFormulaRecord
 * JD-Core Version:    0.7.0.1
 */