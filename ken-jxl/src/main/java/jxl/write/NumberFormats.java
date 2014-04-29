/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import jxl.biff.DisplayFormat;
/*   4:    */ import jxl.format.Format;
/*   5:    */ 
/*   6:    */ public final class NumberFormats
/*   7:    */ {
/*   8:    */   private static class BuiltInFormat
/*   9:    */     implements DisplayFormat, Format
/*  10:    */   {
/*  11:    */     private int index;
/*  12:    */     private String formatString;
/*  13:    */     
/*  14:    */     public BuiltInFormat(int i, String s)
/*  15:    */     {
/*  16: 53 */       this.index = i;
/*  17: 54 */       this.formatString = s;
/*  18:    */     }
/*  19:    */     
/*  20:    */     public int getFormatIndex()
/*  21:    */     {
/*  22: 64 */       return this.index;
/*  23:    */     }
/*  24:    */     
/*  25:    */     public boolean isInitialized()
/*  26:    */     {
/*  27: 74 */       return true;
/*  28:    */     }
/*  29:    */     
/*  30:    */     public boolean isBuiltIn()
/*  31:    */     {
/*  32: 83 */       return true;
/*  33:    */     }
/*  34:    */     
/*  35:    */     public void initialize(int pos) {}
/*  36:    */     
/*  37:    */     public String getFormatString()
/*  38:    */     {
/*  39:104 */       return this.formatString;
/*  40:    */     }
/*  41:    */     
/*  42:    */     public boolean equals(Object o)
/*  43:    */     {
/*  44:115 */       if (o == this) {
/*  45:117 */         return true;
/*  46:    */       }
/*  47:120 */       if (!(o instanceof BuiltInFormat)) {
/*  48:122 */         return false;
/*  49:    */       }
/*  50:125 */       BuiltInFormat bif = (BuiltInFormat)o;
/*  51:    */       
/*  52:127 */       return this.index == bif.index;
/*  53:    */     }
/*  54:    */     
/*  55:    */     public int hashCode()
/*  56:    */     {
/*  57:137 */       return this.index;
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:148 */   public static final DisplayFormat DEFAULT = new BuiltInFormat(0, "#");
/*  62:153 */   public static final DisplayFormat INTEGER = new BuiltInFormat(1, "0");
/*  63:159 */   public static final DisplayFormat FLOAT = new BuiltInFormat(2, "0.00");
/*  64:166 */   public static final DisplayFormat THOUSANDS_INTEGER = new BuiltInFormat(3, "#,##0");
/*  65:173 */   public static final DisplayFormat THOUSANDS_FLOAT = new BuiltInFormat(4, "#,##0.00");
/*  66:181 */   public static final DisplayFormat ACCOUNTING_INTEGER = new BuiltInFormat(5, "$#,##0;($#,##0)");
/*  67:187 */   public static final DisplayFormat ACCOUNTING_RED_INTEGER = new BuiltInFormat(6, "$#,##0;($#,##0)");
/*  68:195 */   public static final DisplayFormat ACCOUNTING_FLOAT = new BuiltInFormat(7, "$#,##0;($#,##0)");
/*  69:201 */   public static final DisplayFormat ACCOUNTING_RED_FLOAT = new BuiltInFormat(8, "$#,##0;($#,##0)");
/*  70:208 */   public static final DisplayFormat PERCENT_INTEGER = new BuiltInFormat(9, "0%");
/*  71:215 */   public static final DisplayFormat PERCENT_FLOAT = new BuiltInFormat(10, "0.00%");
/*  72:222 */   public static final DisplayFormat EXPONENTIAL = new BuiltInFormat(11, "0.00E00");
/*  73:228 */   public static final DisplayFormat FRACTION_ONE_DIGIT = new BuiltInFormat(12, "?/?");
/*  74:234 */   public static final DisplayFormat FRACTION_TWO_DIGITS = new BuiltInFormat(13, "??/??");
/*  75:242 */   public static final DisplayFormat FORMAT1 = new BuiltInFormat(37, "#,##0;(#,##0)");
/*  76:248 */   public static final DisplayFormat FORMAT2 = new BuiltInFormat(38, "#,##0;(#,##0)");
/*  77:254 */   public static final DisplayFormat FORMAT3 = new BuiltInFormat(39, "#,##0.00;(#,##0.00)");
/*  78:260 */   public static final DisplayFormat FORMAT4 = new BuiltInFormat(40, "#,##0.00;(#,##0.00)");
/*  79:266 */   public static final DisplayFormat FORMAT5 = new BuiltInFormat(41, "#,##0;(#,##0)");
/*  80:272 */   public static final DisplayFormat FORMAT6 = new BuiltInFormat(42, "#,##0;(#,##0)");
/*  81:278 */   public static final DisplayFormat FORMAT7 = new BuiltInFormat(43, "#,##0.00;(#,##0.00)");
/*  82:284 */   public static final DisplayFormat FORMAT8 = new BuiltInFormat(44, "#,##0.00;(#,##0.00)");
/*  83:290 */   public static final DisplayFormat FORMAT9 = new BuiltInFormat(46, "#,##0.00;(#,##0.00)");
/*  84:296 */   public static final DisplayFormat FORMAT10 = new BuiltInFormat(48, "##0.0E0");
/*  85:301 */   public static final DisplayFormat TEXT = new BuiltInFormat(49, "@");
/*  86:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.NumberFormats
 * JD-Core Version:    0.7.0.1
 */