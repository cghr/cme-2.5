/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.ErrorFormulaCell;
/*   4:    */ import jxl.biff.FormulaData;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.formula.FormulaErrorCode;
/*   7:    */ import jxl.biff.formula.FormulaException;
/*   8:    */ import jxl.biff.formula.FormulaParser;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ 
/*  11:    */ class ReadErrorFormulaRecord
/*  12:    */   extends ReadFormulaRecord
/*  13:    */   implements ErrorFormulaCell
/*  14:    */ {
/*  15: 39 */   private static Logger logger = Logger.getLogger(ReadErrorFormulaRecord.class);
/*  16:    */   
/*  17:    */   public ReadErrorFormulaRecord(FormulaData f)
/*  18:    */   {
/*  19: 48 */     super(f);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public int getErrorCode()
/*  23:    */   {
/*  24: 58 */     return ((ErrorFormulaCell)getReadFormula()).getErrorCode();
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected byte[] handleFormulaException()
/*  28:    */   {
/*  29: 70 */     byte[] expressiondata = (byte[])null;
/*  30: 71 */     byte[] celldata = super.getCellData();
/*  31:    */     
/*  32: 73 */     int errorCode = getErrorCode();
/*  33: 74 */     String formulaString = null;
/*  34: 76 */     if (errorCode == FormulaErrorCode.DIV0.getCode()) {
/*  35: 78 */       formulaString = "1/0";
/*  36: 80 */     } else if (errorCode == FormulaErrorCode.VALUE.getCode()) {
/*  37: 82 */       formulaString = "\"\"/0";
/*  38: 84 */     } else if (errorCode == FormulaErrorCode.REF.getCode()) {
/*  39: 86 */       formulaString = "\"#REF!\"";
/*  40:    */     } else {
/*  41: 90 */       formulaString = "\"ERROR\"";
/*  42:    */     }
/*  43: 94 */     WritableWorkbookImpl w = getSheet().getWorkbook();
/*  44: 95 */     FormulaParser parser = new FormulaParser(formulaString, w, w, 
/*  45: 96 */       w.getSettings());
/*  46:    */     try
/*  47:    */     {
/*  48:101 */       parser.parse();
/*  49:    */     }
/*  50:    */     catch (FormulaException e2)
/*  51:    */     {
/*  52:105 */       logger.warn(e2.getMessage());
/*  53:    */     }
/*  54:107 */     byte[] formulaBytes = parser.getBytes();
/*  55:108 */     expressiondata = new byte[formulaBytes.length + 16];
/*  56:109 */     IntegerHelper.getTwoBytes(formulaBytes.length, expressiondata, 14);
/*  57:110 */     System.arraycopy(formulaBytes, 0, expressiondata, 16, 
/*  58:111 */       formulaBytes.length); byte[] 
/*  59:    */     
/*  60:    */ 
/*  61:114 */       tmp163_160 = expressiondata;tmp163_160[8] = ((byte)(tmp163_160[8] | 0x2));
/*  62:    */     
/*  63:116 */     byte[] data = new byte[celldata.length + 
/*  64:117 */       expressiondata.length];
/*  65:118 */     System.arraycopy(celldata, 0, data, 0, celldata.length);
/*  66:119 */     System.arraycopy(expressiondata, 0, data, 
/*  67:120 */       celldata.length, expressiondata.length);
/*  68:    */     
/*  69:    */ 
/*  70:123 */     data[6] = 2;
/*  71:124 */     data[12] = -1;
/*  72:125 */     data[13] = -1;
/*  73:    */     
/*  74:    */ 
/*  75:128 */     data[8] = ((byte)errorCode);
/*  76:    */     
/*  77:130 */     return data;
/*  78:    */   }
/*  79:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ReadErrorFormulaRecord
 * JD-Core Version:    0.7.0.1
 */