/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import jxl.biff.DisplayFormat;
/*   4:    */ 
/*   5:    */ public final class DateFormats
/*   6:    */ {
/*   7:    */   private static class BuiltInFormat
/*   8:    */     implements DisplayFormat
/*   9:    */   {
/*  10:    */     private int index;
/*  11:    */     private String formatString;
/*  12:    */     
/*  13:    */     public BuiltInFormat(int i, String s)
/*  14:    */     {
/*  15: 51 */       this.index = i;
/*  16: 52 */       this.formatString = s;
/*  17:    */     }
/*  18:    */     
/*  19:    */     public int getFormatIndex()
/*  20:    */     {
/*  21: 62 */       return this.index;
/*  22:    */     }
/*  23:    */     
/*  24:    */     public boolean isInitialized()
/*  25:    */     {
/*  26: 73 */       return true;
/*  27:    */     }
/*  28:    */     
/*  29:    */     public void initialize(int pos) {}
/*  30:    */     
/*  31:    */     public boolean isBuiltIn()
/*  32:    */     {
/*  33: 92 */       return true;
/*  34:    */     }
/*  35:    */     
/*  36:    */     public String getFormatString()
/*  37:    */     {
/*  38:103 */       return this.formatString;
/*  39:    */     }
/*  40:    */     
/*  41:    */     public boolean equals(Object o)
/*  42:    */     {
/*  43:114 */       if (o == this) {
/*  44:116 */         return true;
/*  45:    */       }
/*  46:119 */       if (!(o instanceof BuiltInFormat)) {
/*  47:121 */         return false;
/*  48:    */       }
/*  49:124 */       BuiltInFormat bif = (BuiltInFormat)o;
/*  50:    */       
/*  51:126 */       return this.index == bif.index;
/*  52:    */     }
/*  53:    */     
/*  54:    */     public int hashCode()
/*  55:    */     {
/*  56:136 */       return this.index;
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:146 */   public static final DisplayFormat FORMAT1 = new BuiltInFormat(14, "M/d/yy");
/*  61:150 */   public static final DisplayFormat DEFAULT = FORMAT1;
/*  62:156 */   public static final DisplayFormat FORMAT2 = new BuiltInFormat(15, "d-MMM-yy");
/*  63:162 */   public static final DisplayFormat FORMAT3 = new BuiltInFormat(16, "d-MMM");
/*  64:168 */   public static final DisplayFormat FORMAT4 = new BuiltInFormat(17, "MMM-yy");
/*  65:174 */   public static final DisplayFormat FORMAT5 = new BuiltInFormat(18, "h:mm a");
/*  66:180 */   public static final DisplayFormat FORMAT6 = new BuiltInFormat(19, "h:mm:ss a");
/*  67:186 */   public static final DisplayFormat FORMAT7 = new BuiltInFormat(20, "H:mm");
/*  68:192 */   public static final DisplayFormat FORMAT8 = new BuiltInFormat(21, "H:mm:ss");
/*  69:198 */   public static final DisplayFormat FORMAT9 = new BuiltInFormat(22, "M/d/yy H:mm");
/*  70:204 */   public static final DisplayFormat FORMAT10 = new BuiltInFormat(45, "mm:ss");
/*  71:210 */   public static final DisplayFormat FORMAT11 = new BuiltInFormat(46, "H:mm:ss");
/*  72:216 */   public static final DisplayFormat FORMAT12 = new BuiltInFormat(47, "H:mm:ss");
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.DateFormats
 * JD-Core Version:    0.7.0.1
 */