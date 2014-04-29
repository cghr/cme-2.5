/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.biff.CellReferenceHelper;
/*   4:    */ import jxl.common.Assert;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ class ColumnRange3d
/*   8:    */   extends Area3d
/*   9:    */ {
/*  10: 35 */   private static Logger logger = Logger.getLogger(ColumnRange3d.class);
/*  11:    */   private ExternalSheet workbook;
/*  12:    */   private int sheet;
/*  13:    */   
/*  14:    */   ColumnRange3d(ExternalSheet es)
/*  15:    */   {
/*  16: 54 */     super(es);
/*  17: 55 */     this.workbook = es;
/*  18:    */   }
/*  19:    */   
/*  20:    */   ColumnRange3d(String s, ExternalSheet es)
/*  21:    */     throws FormulaException
/*  22:    */   {
/*  23: 67 */     super(es);
/*  24: 68 */     this.workbook = es;
/*  25: 69 */     int seppos = s.lastIndexOf(":");
/*  26: 70 */     Assert.verify(seppos != -1);
/*  27: 71 */     String startcell = s.substring(0, seppos);
/*  28: 72 */     String endcell = s.substring(seppos + 1);
/*  29:    */     
/*  30:    */ 
/*  31: 75 */     int sep = s.indexOf('!');
/*  32: 76 */     String cellString = s.substring(sep + 1, seppos);
/*  33: 77 */     int columnFirst = CellReferenceHelper.getColumn(cellString);
/*  34: 78 */     int rowFirst = 0;
/*  35:    */     
/*  36:    */ 
/*  37: 81 */     String sheetName = s.substring(0, sep);
/*  38: 82 */     int sheetNamePos = sheetName.lastIndexOf(']');
/*  39: 85 */     if ((sheetName.charAt(0) == '\'') && 
/*  40: 86 */       (sheetName.charAt(sheetName.length() - 1) == '\'')) {
/*  41: 88 */       sheetName = sheetName.substring(1, sheetName.length() - 1);
/*  42:    */     }
/*  43: 91 */     this.sheet = es.getExternalSheetIndex(sheetName);
/*  44: 93 */     if (this.sheet < 0) {
/*  45: 95 */       throw new FormulaException(FormulaException.SHEET_REF_NOT_FOUND, 
/*  46: 96 */         sheetName);
/*  47:    */     }
/*  48:100 */     int columnLast = CellReferenceHelper.getColumn(endcell);
/*  49:101 */     int rowLast = 65535;
/*  50:    */     
/*  51:103 */     boolean columnFirstRelative = true;
/*  52:104 */     boolean rowFirstRelative = true;
/*  53:105 */     boolean columnLastRelative = true;
/*  54:106 */     boolean rowLastRelative = true;
/*  55:    */     
/*  56:108 */     setRangeData(this.sheet, columnFirst, columnLast, rowFirst, rowLast, 
/*  57:109 */       columnFirstRelative, rowFirstRelative, 
/*  58:110 */       columnLastRelative, rowLastRelative);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void getString(StringBuffer buf)
/*  62:    */   {
/*  63:120 */     buf.append('\'');
/*  64:121 */     buf.append(this.workbook.getExternalSheetName(this.sheet));
/*  65:122 */     buf.append('\'');
/*  66:123 */     buf.append('!');
/*  67:    */     
/*  68:125 */     CellReferenceHelper.getColumnReference(getFirstColumn(), buf);
/*  69:126 */     buf.append(':');
/*  70:127 */     CellReferenceHelper.getColumnReference(getLastColumn(), buf);
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.ColumnRange3d
 * JD-Core Version:    0.7.0.1
 */