/*   1:    */ package jxl.format;
/*   2:    */ 
/*   3:    */ public class Pattern
/*   4:    */ {
/*   5:    */   private int value;
/*   6:    */   private String string;
/*   7: 42 */   private static Pattern[] patterns = new Pattern[0];
/*   8:    */   
/*   9:    */   protected Pattern(int val, String s)
/*  10:    */   {
/*  11: 52 */     this.value = val;
/*  12: 53 */     this.string = s;
/*  13:    */     
/*  14: 55 */     Pattern[] oldcols = patterns;
/*  15: 56 */     patterns = new Pattern[oldcols.length + 1];
/*  16: 57 */     System.arraycopy(oldcols, 0, patterns, 0, oldcols.length);
/*  17: 58 */     patterns[oldcols.length] = this;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public int getValue()
/*  21:    */   {
/*  22: 69 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getDescription()
/*  26:    */   {
/*  27: 79 */     return this.string;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static Pattern getPattern(int val)
/*  31:    */   {
/*  32: 90 */     for (int i = 0; i < patterns.length; i++) {
/*  33: 92 */       if (patterns[i].getValue() == val) {
/*  34: 94 */         return patterns[i];
/*  35:    */       }
/*  36:    */     }
/*  37: 98 */     return NONE;
/*  38:    */   }
/*  39:    */   
/*  40:101 */   public static final Pattern NONE = new Pattern(0, "None");
/*  41:102 */   public static final Pattern SOLID = new Pattern(1, "Solid");
/*  42:104 */   public static final Pattern GRAY_50 = new Pattern(2, "Gray 50%");
/*  43:105 */   public static final Pattern GRAY_75 = new Pattern(3, "Gray 75%");
/*  44:106 */   public static final Pattern GRAY_25 = new Pattern(4, "Gray 25%");
/*  45:108 */   public static final Pattern PATTERN1 = new Pattern(5, "Pattern 1");
/*  46:109 */   public static final Pattern PATTERN2 = new Pattern(6, "Pattern 2");
/*  47:110 */   public static final Pattern PATTERN3 = new Pattern(7, "Pattern 3");
/*  48:111 */   public static final Pattern PATTERN4 = new Pattern(8, "Pattern 4");
/*  49:112 */   public static final Pattern PATTERN5 = new Pattern(9, "Pattern 5");
/*  50:113 */   public static final Pattern PATTERN6 = new Pattern(10, "Pattern 6");
/*  51:114 */   public static final Pattern PATTERN7 = new Pattern(11, "Pattern 7");
/*  52:115 */   public static final Pattern PATTERN8 = new Pattern(12, "Pattern 8");
/*  53:116 */   public static final Pattern PATTERN9 = new Pattern(13, "Pattern 9");
/*  54:117 */   public static final Pattern PATTERN10 = new Pattern(14, "Pattern 10");
/*  55:118 */   public static final Pattern PATTERN11 = new Pattern(15, "Pattern 11");
/*  56:119 */   public static final Pattern PATTERN12 = new Pattern(16, "Pattern 12");
/*  57:120 */   public static final Pattern PATTERN13 = new Pattern(17, "Pattern 13");
/*  58:121 */   public static final Pattern PATTERN14 = new Pattern(18, "Pattern 14");
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.Pattern
 * JD-Core Version:    0.7.0.1
 */