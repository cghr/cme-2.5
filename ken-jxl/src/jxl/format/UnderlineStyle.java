/*   1:    */ package jxl.format;
/*   2:    */ 
/*   3:    */ public final class UnderlineStyle
/*   4:    */ {
/*   5:    */   private int value;
/*   6:    */   private String string;
/*   7: 43 */   private static UnderlineStyle[] styles = new UnderlineStyle[0];
/*   8:    */   
/*   9:    */   protected UnderlineStyle(int val, String s)
/*  10:    */   {
/*  11: 53 */     this.value = val;
/*  12: 54 */     this.string = s;
/*  13:    */     
/*  14: 56 */     UnderlineStyle[] oldstyles = styles;
/*  15: 57 */     styles = new UnderlineStyle[oldstyles.length + 1];
/*  16: 58 */     System.arraycopy(oldstyles, 0, styles, 0, oldstyles.length);
/*  17: 59 */     styles[oldstyles.length] = this;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public int getValue()
/*  21:    */   {
/*  22: 70 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getDescription()
/*  26:    */   {
/*  27: 80 */     return this.string;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static UnderlineStyle getStyle(int val)
/*  31:    */   {
/*  32: 91 */     for (int i = 0; i < styles.length; i++) {
/*  33: 93 */       if (styles[i].getValue() == val) {
/*  34: 95 */         return styles[i];
/*  35:    */       }
/*  36:    */     }
/*  37: 99 */     return NO_UNDERLINE;
/*  38:    */   }
/*  39:    */   
/*  40:104 */   public static final UnderlineStyle NO_UNDERLINE = new UnderlineStyle(0, "none");
/*  41:107 */   public static final UnderlineStyle SINGLE = new UnderlineStyle(1, "single");
/*  42:110 */   public static final UnderlineStyle DOUBLE = new UnderlineStyle(2, "double");
/*  43:113 */   public static final UnderlineStyle SINGLE_ACCOUNTING = new UnderlineStyle(33, "single accounting");
/*  44:116 */   public static final UnderlineStyle DOUBLE_ACCOUNTING = new UnderlineStyle(34, "double accounting");
/*  45:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.UnderlineStyle
 * JD-Core Version:    0.7.0.1
 */