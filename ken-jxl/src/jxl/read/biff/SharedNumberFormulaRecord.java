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
/*  11:    */ import jxl.biff.IntegerHelper;
/*  12:    */ import jxl.biff.WorkbookMethods;
/*  13:    */ import jxl.biff.formula.ExternalSheet;
/*  14:    */ import jxl.biff.formula.FormulaException;
/*  15:    */ import jxl.biff.formula.FormulaParser;
/*  16:    */ import jxl.common.Logger;
/*  17:    */ 
/*  18:    */ public class SharedNumberFormulaRecord
/*  19:    */   extends BaseSharedFormulaRecord
/*  20:    */   implements NumberCell, FormulaData, NumberFormulaCell
/*  21:    */ {
/*  22: 51 */   private static Logger logger = Logger.getLogger(SharedNumberFormulaRecord.class);
/*  23:    */   private double value;
/*  24:    */   private NumberFormat format;
/*  25:    */   private FormattingRecords formattingRecords;
/*  26: 68 */   private static DecimalFormat defaultFormat = new DecimalFormat("#.###");
/*  27:    */   
/*  28:    */   public SharedNumberFormulaRecord(Record t, File excelFile, double v, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si)
/*  29:    */   {
/*  30: 89 */     super(t, fr, es, nt, si, excelFile.getPos());
/*  31: 90 */     this.value = v;
/*  32: 91 */     this.format = defaultFormat;
/*  33:    */   }
/*  34:    */   
/*  35:    */   final void setNumberFormat(NumberFormat f)
/*  36:    */   {
/*  37:104 */     if (f != null) {
/*  38:106 */       this.format = f;
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public double getValue()
/*  43:    */   {
/*  44:117 */     return this.value;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getContents()
/*  48:    */   {
/*  49:127 */     return !Double.isNaN(this.value) ? this.format.format(this.value) : "";
/*  50:    */   }
/*  51:    */   
/*  52:    */   public CellType getType()
/*  53:    */   {
/*  54:137 */     return CellType.NUMBER_FORMULA;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public byte[] getFormulaData()
/*  58:    */     throws FormulaException
/*  59:    */   {
/*  60:149 */     if (!getSheet().getWorkbookBof().isBiff8()) {
/*  61:151 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  62:    */     }
/*  63:156 */     FormulaParser fp = new FormulaParser(
/*  64:157 */       getTokens(), this, 
/*  65:158 */       getExternalSheet(), getNameTable(), 
/*  66:159 */       getSheet().getWorkbook().getSettings());
/*  67:160 */     fp.parse();
/*  68:161 */     byte[] rpnTokens = fp.getBytes();
/*  69:    */     
/*  70:163 */     byte[] data = new byte[rpnTokens.length + 22];
/*  71:    */     
/*  72:    */ 
/*  73:166 */     IntegerHelper.getTwoBytes(getRow(), data, 0);
/*  74:167 */     IntegerHelper.getTwoBytes(getColumn(), data, 2);
/*  75:168 */     IntegerHelper.getTwoBytes(getXFIndex(), data, 4);
/*  76:169 */     DoubleHelper.getIEEEBytes(this.value, data, 6);
/*  77:    */     
/*  78:    */ 
/*  79:172 */     System.arraycopy(rpnTokens, 0, data, 22, rpnTokens.length);
/*  80:173 */     IntegerHelper.getTwoBytes(rpnTokens.length, data, 20);
/*  81:    */     
/*  82:    */ 
/*  83:176 */     byte[] d = new byte[data.length - 6];
/*  84:177 */     System.arraycopy(data, 6, d, 0, data.length - 6);
/*  85:    */     
/*  86:179 */     return d;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public NumberFormat getNumberFormat()
/*  90:    */   {
/*  91:190 */     return this.format;
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SharedNumberFormulaRecord
 * JD-Core Version:    0.7.0.1
 */