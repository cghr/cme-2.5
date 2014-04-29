/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.Cell;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.biff.WorkbookMethods;
/*   6:    */ import jxl.common.Assert;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ import jxl.read.biff.BOFRecord;
/*   9:    */ 
/*  10:    */ public class FormulaParser
/*  11:    */ {
/*  12: 38 */   private static final Logger logger = Logger.getLogger(FormulaParser.class);
/*  13:    */   private Parser parser;
/*  14:    */   
/*  15:    */   public FormulaParser(byte[] tokens, Cell rt, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws)
/*  16:    */     throws FormulaException
/*  17:    */   {
/*  18: 66 */     if ((es.getWorkbookBof() != null) && 
/*  19: 67 */       (!es.getWorkbookBof().isBiff8())) {
/*  20: 69 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  21:    */     }
/*  22: 71 */     Assert.verify(nt != null);
/*  23: 72 */     this.parser = new TokenFormulaParser(tokens, rt, es, nt, ws, 
/*  24: 73 */       ParseContext.DEFAULT);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public FormulaParser(byte[] tokens, Cell rt, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws, ParseContext pc)
/*  28:    */     throws FormulaException
/*  29:    */   {
/*  30: 97 */     if ((es.getWorkbookBof() != null) && 
/*  31: 98 */       (!es.getWorkbookBof().isBiff8())) {
/*  32:100 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  33:    */     }
/*  34:102 */     Assert.verify(nt != null);
/*  35:103 */     this.parser = new TokenFormulaParser(tokens, rt, es, nt, ws, pc);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public FormulaParser(String form, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws)
/*  39:    */   {
/*  40:119 */     this.parser = new StringFormulaParser(form, es, nt, ws, 
/*  41:120 */       ParseContext.DEFAULT);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public FormulaParser(String form, ExternalSheet es, WorkbookMethods nt, WorkbookSettings ws, ParseContext pc)
/*  45:    */   {
/*  46:138 */     this.parser = new StringFormulaParser(form, es, nt, ws, pc);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/*  50:    */   {
/*  51:151 */     this.parser.adjustRelativeCellReferences(colAdjust, rowAdjust);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void parse()
/*  55:    */     throws FormulaException
/*  56:    */   {
/*  57:161 */     this.parser.parse();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getFormula()
/*  61:    */     throws FormulaException
/*  62:    */   {
/*  63:172 */     return this.parser.getFormula();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public byte[] getBytes()
/*  67:    */   {
/*  68:183 */     return this.parser.getBytes();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void columnInserted(int sheetIndex, int col, boolean currentSheet)
/*  72:    */   {
/*  73:198 */     this.parser.columnInserted(sheetIndex, col, currentSheet);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/*  77:    */   {
/*  78:213 */     this.parser.columnRemoved(sheetIndex, col, currentSheet);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void rowInserted(int sheetIndex, int row, boolean currentSheet)
/*  82:    */   {
/*  83:228 */     this.parser.rowInserted(sheetIndex, row, currentSheet);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/*  87:    */   {
/*  88:243 */     this.parser.rowRemoved(sheetIndex, row, currentSheet);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean handleImportedCellReferences()
/*  92:    */   {
/*  93:254 */     return this.parser.handleImportedCellReferences();
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.FormulaParser
 * JD-Core Version:    0.7.0.1
 */