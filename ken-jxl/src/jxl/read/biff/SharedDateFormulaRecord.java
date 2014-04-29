/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.text.DateFormat;
/*   4:    */ import java.util.Date;
/*   5:    */ import jxl.CellType;
/*   6:    */ import jxl.DateCell;
/*   7:    */ import jxl.DateFormulaCell;
/*   8:    */ import jxl.biff.DoubleHelper;
/*   9:    */ import jxl.biff.FormattingRecords;
/*  10:    */ import jxl.biff.FormulaData;
/*  11:    */ import jxl.biff.IntegerHelper;
/*  12:    */ import jxl.biff.formula.FormulaException;
/*  13:    */ import jxl.biff.formula.FormulaParser;
/*  14:    */ 
/*  15:    */ public class SharedDateFormulaRecord
/*  16:    */   extends BaseSharedFormulaRecord
/*  17:    */   implements DateCell, FormulaData, DateFormulaCell
/*  18:    */ {
/*  19:    */   private DateRecord dateRecord;
/*  20:    */   private double value;
/*  21:    */   
/*  22:    */   public SharedDateFormulaRecord(SharedNumberFormulaRecord nfr, FormattingRecords fr, boolean nf, SheetImpl si, int pos)
/*  23:    */   {
/*  24: 74 */     super(nfr.getRecord(), fr, nfr.getExternalSheet(), nfr.getNameTable(), si, pos);
/*  25: 75 */     this.dateRecord = new DateRecord(nfr, nfr.getXFIndex(), fr, nf, si);
/*  26: 76 */     this.value = nfr.getValue();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public double getValue()
/*  30:    */   {
/*  31: 86 */     return this.value;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String getContents()
/*  35:    */   {
/*  36: 96 */     return this.dateRecord.getContents();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public CellType getType()
/*  40:    */   {
/*  41:106 */     return CellType.DATE_FORMULA;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public byte[] getFormulaData()
/*  45:    */     throws FormulaException
/*  46:    */   {
/*  47:118 */     if (!getSheet().getWorkbookBof().isBiff8()) {
/*  48:120 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  49:    */     }
/*  50:125 */     FormulaParser fp = new FormulaParser(
/*  51:126 */       getTokens(), this, 
/*  52:127 */       getExternalSheet(), getNameTable(), 
/*  53:128 */       getSheet().getWorkbook().getSettings());
/*  54:129 */     fp.parse();
/*  55:130 */     byte[] rpnTokens = fp.getBytes();
/*  56:    */     
/*  57:132 */     byte[] data = new byte[rpnTokens.length + 22];
/*  58:    */     
/*  59:    */ 
/*  60:135 */     IntegerHelper.getTwoBytes(getRow(), data, 0);
/*  61:136 */     IntegerHelper.getTwoBytes(getColumn(), data, 2);
/*  62:137 */     IntegerHelper.getTwoBytes(getXFIndex(), data, 4);
/*  63:138 */     DoubleHelper.getIEEEBytes(this.value, data, 6);
/*  64:    */     
/*  65:    */ 
/*  66:141 */     System.arraycopy(rpnTokens, 0, data, 22, rpnTokens.length);
/*  67:142 */     IntegerHelper.getTwoBytes(rpnTokens.length, data, 20);
/*  68:    */     
/*  69:    */ 
/*  70:145 */     byte[] d = new byte[data.length - 6];
/*  71:146 */     System.arraycopy(data, 6, d, 0, data.length - 6);
/*  72:    */     
/*  73:148 */     return d;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public Date getDate()
/*  77:    */   {
/*  78:158 */     return this.dateRecord.getDate();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean isTime()
/*  82:    */   {
/*  83:169 */     return this.dateRecord.isTime();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public DateFormat getDateFormat()
/*  87:    */   {
/*  88:182 */     return this.dateRecord.getDateFormat();
/*  89:    */   }
/*  90:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SharedDateFormulaRecord
 * JD-Core Version:    0.7.0.1
 */