/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.StringFormulaCell;
/*   4:    */ import jxl.biff.FormulaData;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.formula.FormulaException;
/*   7:    */ import jxl.biff.formula.FormulaParser;
/*   8:    */ import jxl.common.Assert;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ 
/*  11:    */ class ReadStringFormulaRecord
/*  12:    */   extends ReadFormulaRecord
/*  13:    */   implements StringFormulaCell
/*  14:    */ {
/*  15: 38 */   private static Logger logger = Logger.getLogger(ReadFormulaRecord.class);
/*  16:    */   
/*  17:    */   public ReadStringFormulaRecord(FormulaData f)
/*  18:    */   {
/*  19: 47 */     super(f);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public String getString()
/*  23:    */   {
/*  24: 57 */     return ((StringFormulaCell)getReadFormula()).getString();
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected byte[] handleFormulaException()
/*  28:    */   {
/*  29: 69 */     byte[] expressiondata = (byte[])null;
/*  30: 70 */     byte[] celldata = super.getCellData();
/*  31:    */     
/*  32:    */ 
/*  33: 73 */     WritableWorkbookImpl w = getSheet().getWorkbook();
/*  34: 74 */     FormulaParser parser = new FormulaParser("\"" + getContents() + "\"", w, w, 
/*  35: 75 */       w.getSettings());
/*  36:    */     try
/*  37:    */     {
/*  38: 80 */       parser.parse();
/*  39:    */     }
/*  40:    */     catch (FormulaException e2)
/*  41:    */     {
/*  42: 84 */       logger.warn(e2.getMessage());
/*  43: 85 */       parser = new FormulaParser("\"ERROR\"", w, w, w.getSettings());
/*  44:    */       try
/*  45:    */       {
/*  46: 86 */         parser.parse();
/*  47:    */       }
/*  48:    */       catch (FormulaException e3)
/*  49:    */       {
/*  50: 87 */         Assert.verify(false);
/*  51:    */       }
/*  52:    */     }
/*  53: 89 */     byte[] formulaBytes = parser.getBytes();
/*  54: 90 */     expressiondata = new byte[formulaBytes.length + 16];
/*  55: 91 */     IntegerHelper.getTwoBytes(formulaBytes.length, expressiondata, 14);
/*  56: 92 */     System.arraycopy(formulaBytes, 0, expressiondata, 16, 
/*  57: 93 */       formulaBytes.length); byte[] 
/*  58:    */     
/*  59:    */ 
/*  60: 96 */       tmp149_146 = expressiondata;tmp149_146[8] = ((byte)(tmp149_146[8] | 0x2));
/*  61:    */     
/*  62: 98 */     byte[] data = new byte[celldata.length + 
/*  63: 99 */       expressiondata.length];
/*  64:100 */     System.arraycopy(celldata, 0, data, 0, celldata.length);
/*  65:101 */     System.arraycopy(expressiondata, 0, data, 
/*  66:102 */       celldata.length, expressiondata.length);
/*  67:    */     
/*  68:    */ 
/*  69:105 */     data[6] = 0;
/*  70:106 */     data[12] = -1;
/*  71:107 */     data[13] = -1;
/*  72:    */     
/*  73:109 */     return data;
/*  74:    */   }
/*  75:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ReadStringFormulaRecord
 * JD-Core Version:    0.7.0.1
 */