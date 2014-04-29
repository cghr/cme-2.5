/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.CellType;
/*   4:    */ import jxl.ErrorCell;
/*   5:    */ import jxl.ErrorFormulaCell;
/*   6:    */ import jxl.biff.FormattingRecords;
/*   7:    */ import jxl.biff.FormulaData;
/*   8:    */ import jxl.biff.IntegerHelper;
/*   9:    */ import jxl.biff.WorkbookMethods;
/*  10:    */ import jxl.biff.formula.ExternalSheet;
/*  11:    */ import jxl.biff.formula.FormulaErrorCode;
/*  12:    */ import jxl.biff.formula.FormulaException;
/*  13:    */ import jxl.biff.formula.FormulaParser;
/*  14:    */ import jxl.common.Logger;
/*  15:    */ 
/*  16:    */ public class SharedErrorFormulaRecord
/*  17:    */   extends BaseSharedFormulaRecord
/*  18:    */   implements ErrorCell, FormulaData, ErrorFormulaCell
/*  19:    */ {
/*  20: 47 */   private static Logger logger = Logger.getLogger(SharedErrorFormulaRecord.class);
/*  21:    */   private int errorCode;
/*  22:    */   private byte[] data;
/*  23:    */   private FormulaErrorCode error;
/*  24:    */   
/*  25:    */   public SharedErrorFormulaRecord(Record t, File excelFile, int ec, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si)
/*  26:    */   {
/*  27: 83 */     super(t, fr, es, nt, si, excelFile.getPos());
/*  28: 84 */     this.errorCode = ec;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int getErrorCode()
/*  32:    */   {
/*  33: 96 */     return this.errorCode;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String getContents()
/*  37:    */   {
/*  38:106 */     if (this.error == null) {
/*  39:108 */       this.error = FormulaErrorCode.getErrorCode(this.errorCode);
/*  40:    */     }
/*  41:111 */     return 
/*  42:112 */       "ERROR " + this.errorCode;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public CellType getType()
/*  46:    */   {
/*  47:122 */     return CellType.FORMULA_ERROR;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public byte[] getFormulaData()
/*  51:    */     throws FormulaException
/*  52:    */   {
/*  53:134 */     if (!getSheet().getWorkbookBof().isBiff8()) {
/*  54:136 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  55:    */     }
/*  56:141 */     FormulaParser fp = new FormulaParser(
/*  57:142 */       getTokens(), this, 
/*  58:143 */       getExternalSheet(), getNameTable(), 
/*  59:144 */       getSheet().getWorkbook().getSettings());
/*  60:145 */     fp.parse();
/*  61:146 */     byte[] rpnTokens = fp.getBytes();
/*  62:    */     
/*  63:148 */     byte[] data = new byte[rpnTokens.length + 22];
/*  64:    */     
/*  65:    */ 
/*  66:151 */     IntegerHelper.getTwoBytes(getRow(), data, 0);
/*  67:152 */     IntegerHelper.getTwoBytes(getColumn(), data, 2);
/*  68:153 */     IntegerHelper.getTwoBytes(getXFIndex(), data, 4);
/*  69:    */     
/*  70:155 */     data[6] = 2;
/*  71:156 */     data[8] = ((byte)this.errorCode);
/*  72:157 */     data[12] = -1;
/*  73:158 */     data[13] = -1;
/*  74:    */     
/*  75:    */ 
/*  76:161 */     System.arraycopy(rpnTokens, 0, data, 22, rpnTokens.length);
/*  77:162 */     IntegerHelper.getTwoBytes(rpnTokens.length, data, 20);
/*  78:    */     
/*  79:    */ 
/*  80:165 */     byte[] d = new byte[data.length - 6];
/*  81:166 */     System.arraycopy(data, 6, d, 0, data.length - 6);
/*  82:    */     
/*  83:168 */     return d;
/*  84:    */   }
/*  85:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SharedErrorFormulaRecord
 * JD-Core Version:    0.7.0.1
 */