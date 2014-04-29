/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.BooleanCell;
/*   4:    */ import jxl.BooleanFormulaCell;
/*   5:    */ import jxl.CellType;
/*   6:    */ import jxl.biff.FormattingRecords;
/*   7:    */ import jxl.biff.FormulaData;
/*   8:    */ import jxl.biff.IntegerHelper;
/*   9:    */ import jxl.biff.WorkbookMethods;
/*  10:    */ import jxl.biff.formula.ExternalSheet;
/*  11:    */ import jxl.biff.formula.FormulaException;
/*  12:    */ import jxl.biff.formula.FormulaParser;
/*  13:    */ import jxl.common.Logger;
/*  14:    */ 
/*  15:    */ public class SharedBooleanFormulaRecord
/*  16:    */   extends BaseSharedFormulaRecord
/*  17:    */   implements BooleanCell, FormulaData, BooleanFormulaCell
/*  18:    */ {
/*  19: 47 */   private static Logger logger = Logger.getLogger(SharedBooleanFormulaRecord.class);
/*  20:    */   private boolean value;
/*  21:    */   
/*  22:    */   public SharedBooleanFormulaRecord(Record t, File excelFile, boolean v, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si)
/*  23:    */   {
/*  24: 74 */     super(t, fr, es, nt, si, excelFile.getPos());
/*  25: 75 */     this.value = v;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean getValue()
/*  29:    */   {
/*  30: 88 */     return this.value;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getContents()
/*  34:    */   {
/*  35: 99 */     return new Boolean(this.value).toString();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public CellType getType()
/*  39:    */   {
/*  40:109 */     return CellType.BOOLEAN_FORMULA;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public byte[] getFormulaData()
/*  44:    */     throws FormulaException
/*  45:    */   {
/*  46:121 */     if (!getSheet().getWorkbookBof().isBiff8()) {
/*  47:123 */       throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
/*  48:    */     }
/*  49:128 */     FormulaParser fp = new FormulaParser(
/*  50:129 */       getTokens(), this, 
/*  51:130 */       getExternalSheet(), getNameTable(), 
/*  52:131 */       getSheet().getWorkbook().getSettings());
/*  53:132 */     fp.parse();
/*  54:133 */     byte[] rpnTokens = fp.getBytes();
/*  55:    */     
/*  56:135 */     byte[] data = new byte[rpnTokens.length + 22];
/*  57:    */     
/*  58:    */ 
/*  59:138 */     IntegerHelper.getTwoBytes(getRow(), data, 0);
/*  60:139 */     IntegerHelper.getTwoBytes(getColumn(), data, 2);
/*  61:140 */     IntegerHelper.getTwoBytes(getXFIndex(), data, 4);
/*  62:141 */     data[6] = 1;
/*  63:142 */     data[8] = ((byte)(this.value ? 1 : 0));
/*  64:143 */     data[12] = -1;
/*  65:144 */     data[13] = -1;
/*  66:    */     
/*  67:    */ 
/*  68:147 */     System.arraycopy(rpnTokens, 0, data, 22, rpnTokens.length);
/*  69:148 */     IntegerHelper.getTwoBytes(rpnTokens.length, data, 20);
/*  70:    */     
/*  71:    */ 
/*  72:151 */     byte[] d = new byte[data.length - 6];
/*  73:152 */     System.arraycopy(data, 6, d, 0, data.length - 6);
/*  74:    */     
/*  75:154 */     return d;
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SharedBooleanFormulaRecord
 * JD-Core Version:    0.7.0.1
 */