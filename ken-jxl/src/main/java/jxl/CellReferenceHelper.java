/*   1:    */ package jxl;
/*   2:    */ 
/*   3:    */ import jxl.biff.formula.ExternalSheet;
/*   4:    */ import jxl.write.WritableWorkbook;
/*   5:    */ 
/*   6:    */ public final class CellReferenceHelper
/*   7:    */ {
/*   8:    */   public static void getCellReference(int column, int row, StringBuffer buf)
/*   9:    */   {
/*  10: 46 */     jxl.biff.CellReferenceHelper.getCellReference(column, row, buf);
/*  11:    */   }
/*  12:    */   
/*  13:    */   public static void getCellReference(int column, boolean colabs, int row, boolean rowabs, StringBuffer buf)
/*  14:    */   {
/*  15: 64 */     jxl.biff.CellReferenceHelper.getCellReference(column, colabs, 
/*  16: 65 */       row, rowabs, 
/*  17: 66 */       buf);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static String getCellReference(int column, int row)
/*  21:    */   {
/*  22: 79 */     return jxl.biff.CellReferenceHelper.getCellReference(column, row);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static int getColumn(String s)
/*  26:    */   {
/*  27: 90 */     return jxl.biff.CellReferenceHelper.getColumn(s);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static String getColumnReference(int c)
/*  31:    */   {
/*  32:101 */     return jxl.biff.CellReferenceHelper.getColumnReference(c);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static int getRow(String s)
/*  36:    */   {
/*  37:111 */     return jxl.biff.CellReferenceHelper.getRow(s);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static boolean isColumnRelative(String s)
/*  41:    */   {
/*  42:122 */     return jxl.biff.CellReferenceHelper.isColumnRelative(s);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static boolean isRowRelative(String s)
/*  46:    */   {
/*  47:133 */     return jxl.biff.CellReferenceHelper.isRowRelative(s);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static void getCellReference(int sheet, int column, int row, Workbook workbook, StringBuffer buf)
/*  51:    */   {
/*  52:150 */     jxl.biff.CellReferenceHelper.getCellReference(
/*  53:151 */       sheet, column, row, (ExternalSheet)workbook, buf);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static void getCellReference(int sheet, int column, int row, WritableWorkbook workbook, StringBuffer buf)
/*  57:    */   {
/*  58:170 */     jxl.biff.CellReferenceHelper.getCellReference(
/*  59:171 */       sheet, column, row, (ExternalSheet)workbook, buf);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static void getCellReference(int sheet, int column, boolean colabs, int row, boolean rowabs, Workbook workbook, StringBuffer buf)
/*  63:    */   {
/*  64:194 */     jxl.biff.CellReferenceHelper.getCellReference(
/*  65:195 */       sheet, column, colabs, row, rowabs, 
/*  66:196 */       (ExternalSheet)workbook, buf);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static String getCellReference(int sheet, int column, int row, Workbook workbook)
/*  70:    */   {
/*  71:214 */     return jxl.biff.CellReferenceHelper.getCellReference(
/*  72:215 */       sheet, column, row, (ExternalSheet)workbook);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static String getCellReference(int sheet, int column, int row, WritableWorkbook workbook)
/*  76:    */   {
/*  77:233 */     return jxl.biff.CellReferenceHelper.getCellReference(
/*  78:234 */       sheet, column, row, (ExternalSheet)workbook);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static String getSheet(String ref)
/*  82:    */   {
/*  83:246 */     return jxl.biff.CellReferenceHelper.getSheet(ref);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static String getCellReference(Cell c)
/*  87:    */   {
/*  88:256 */     return getCellReference(c.getColumn(), c.getRow());
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static void getCellReference(Cell c, StringBuffer sb)
/*  92:    */   {
/*  93:267 */     getCellReference(c.getColumn(), c.getRow(), sb);
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.CellReferenceHelper
 * JD-Core Version:    0.7.0.1
 */