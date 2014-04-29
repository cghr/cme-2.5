/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import jxl.NumberFormulaCell;
/*   5:    */ import jxl.biff.DoubleHelper;
/*   6:    */ import jxl.biff.FormulaData;
/*   7:    */ import jxl.biff.IntegerHelper;
/*   8:    */ import jxl.biff.formula.FormulaException;
/*   9:    */ import jxl.biff.formula.FormulaParser;
/*  10:    */ import jxl.common.Logger;
/*  11:    */ 
/*  12:    */ class ReadNumberFormulaRecord
/*  13:    */   extends ReadFormulaRecord
/*  14:    */   implements NumberFormulaCell
/*  15:    */ {
/*  16: 40 */   private static Logger logger = Logger.getLogger(ReadNumberFormulaRecord.class);
/*  17:    */   
/*  18:    */   public ReadNumberFormulaRecord(FormulaData f)
/*  19:    */   {
/*  20: 49 */     super(f);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public double getValue()
/*  24:    */   {
/*  25: 59 */     return ((NumberFormulaCell)getReadFormula()).getValue();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public NumberFormat getNumberFormat()
/*  29:    */   {
/*  30: 70 */     return ((NumberFormulaCell)getReadFormula()).getNumberFormat();
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected byte[] handleFormulaException()
/*  34:    */   {
/*  35: 82 */     byte[] expressiondata = (byte[])null;
/*  36: 83 */     byte[] celldata = super.getCellData();
/*  37:    */     
/*  38:    */ 
/*  39: 86 */     WritableWorkbookImpl w = getSheet().getWorkbook();
/*  40: 87 */     FormulaParser parser = new FormulaParser(Double.toString(getValue()), w, w, 
/*  41: 88 */       w.getSettings());
/*  42:    */     try
/*  43:    */     {
/*  44: 93 */       parser.parse();
/*  45:    */     }
/*  46:    */     catch (FormulaException e2)
/*  47:    */     {
/*  48: 97 */       logger.warn(e2.getMessage());
/*  49:    */     }
/*  50: 99 */     byte[] formulaBytes = parser.getBytes();
/*  51:100 */     expressiondata = new byte[formulaBytes.length + 16];
/*  52:101 */     IntegerHelper.getTwoBytes(formulaBytes.length, expressiondata, 14);
/*  53:102 */     System.arraycopy(formulaBytes, 0, expressiondata, 16, 
/*  54:103 */       formulaBytes.length); byte[] 
/*  55:    */     
/*  56:    */ 
/*  57:106 */       tmp101_98 = expressiondata;tmp101_98[8] = ((byte)(tmp101_98[8] | 0x2));
/*  58:    */     
/*  59:108 */     byte[] data = new byte[celldata.length + 
/*  60:109 */       expressiondata.length];
/*  61:110 */     System.arraycopy(celldata, 0, data, 0, celldata.length);
/*  62:111 */     System.arraycopy(expressiondata, 0, data, 
/*  63:112 */       celldata.length, expressiondata.length);
/*  64:    */     
/*  65:    */ 
/*  66:115 */     DoubleHelper.getIEEEBytes(getValue(), data, 6);
/*  67:    */     
/*  68:117 */     return data;
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ReadNumberFormulaRecord
 * JD-Core Version:    0.7.0.1
 */