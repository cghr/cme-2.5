/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.FormattingRecords;
/*   4:    */ import jxl.biff.FormulaData;
/*   5:    */ import jxl.biff.WorkbookMethods;
/*   6:    */ import jxl.biff.formula.ExternalSheet;
/*   7:    */ import jxl.biff.formula.FormulaException;
/*   8:    */ import jxl.biff.formula.FormulaParser;
/*   9:    */ 
/*  10:    */ public abstract class BaseSharedFormulaRecord
/*  11:    */   extends CellValue
/*  12:    */   implements FormulaData
/*  13:    */ {
/*  14:    */   private String formulaString;
/*  15:    */   private int filePos;
/*  16:    */   private byte[] tokens;
/*  17:    */   private ExternalSheet externalSheet;
/*  18:    */   private WorkbookMethods nameTable;
/*  19:    */   
/*  20:    */   public BaseSharedFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, int pos)
/*  21:    */   {
/*  22: 78 */     super(t, fr, si);
/*  23: 79 */     this.externalSheet = es;
/*  24: 80 */     this.nameTable = nt;
/*  25: 81 */     this.filePos = pos;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String getFormula()
/*  29:    */     throws FormulaException
/*  30:    */   {
/*  31: 92 */     if (this.formulaString == null)
/*  32:    */     {
/*  33: 94 */       FormulaParser fp = new FormulaParser(
/*  34: 95 */         this.tokens, this, this.externalSheet, this.nameTable, 
/*  35: 96 */         getSheet().getWorkbook().getSettings());
/*  36: 97 */       fp.parse();
/*  37: 98 */       this.formulaString = fp.getFormula();
/*  38:    */     }
/*  39:101 */     return this.formulaString;
/*  40:    */   }
/*  41:    */   
/*  42:    */   void setTokens(byte[] t)
/*  43:    */   {
/*  44:112 */     this.tokens = t;
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected final byte[] getTokens()
/*  48:    */   {
/*  49:122 */     return this.tokens;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected final ExternalSheet getExternalSheet()
/*  53:    */   {
/*  54:132 */     return this.externalSheet;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected final WorkbookMethods getNameTable()
/*  58:    */   {
/*  59:142 */     return this.nameTable;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Record getRecord()
/*  63:    */   {
/*  64:153 */     return super.getRecord();
/*  65:    */   }
/*  66:    */   
/*  67:    */   final int getFilePos()
/*  68:    */   {
/*  69:163 */     return this.filePos;
/*  70:    */   }
/*  71:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.BaseSharedFormulaRecord
 * JD-Core Version:    0.7.0.1
 */