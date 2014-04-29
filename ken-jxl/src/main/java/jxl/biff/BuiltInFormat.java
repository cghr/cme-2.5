/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.format.Format;
/*   4:    */ 
/*   5:    */ final class BuiltInFormat
/*   6:    */   implements Format, DisplayFormat
/*   7:    */ {
/*   8:    */   private String formatString;
/*   9:    */   private int formatIndex;
/*  10:    */   
/*  11:    */   private BuiltInFormat(String s, int i)
/*  12:    */   {
/*  13: 52 */     this.formatIndex = i;
/*  14: 53 */     this.formatString = s;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public String getFormatString()
/*  18:    */   {
/*  19: 65 */     return this.formatString;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public int getFormatIndex()
/*  23:    */   {
/*  24: 75 */     return this.formatIndex;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean isInitialized()
/*  28:    */   {
/*  29: 84 */     return true;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public boolean isBuiltIn()
/*  33:    */   {
/*  34:102 */     return true;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public boolean equals(Object o)
/*  38:    */   {
/*  39:112 */     if (o == this) {
/*  40:114 */       return true;
/*  41:    */     }
/*  42:117 */     if (!(o instanceof BuiltInFormat)) {
/*  43:119 */       return false;
/*  44:    */     }
/*  45:122 */     BuiltInFormat bif = (BuiltInFormat)o;
/*  46:123 */     return this.formatIndex == bif.formatIndex;
/*  47:    */   }
/*  48:    */   
/*  49:129 */   public static BuiltInFormat[] builtIns = new BuiltInFormat[50];
/*  50:    */   
/*  51:    */   static
/*  52:    */   {
/*  53:134 */     builtIns[0] = new BuiltInFormat("", 0);
/*  54:135 */     builtIns[1] = new BuiltInFormat("0", 1);
/*  55:136 */     builtIns[2] = new BuiltInFormat("0.00", 2);
/*  56:137 */     builtIns[3] = new BuiltInFormat("#,##0", 3);
/*  57:138 */     builtIns[4] = new BuiltInFormat("#,##0.00", 4);
/*  58:139 */     builtIns[5] = new BuiltInFormat("($#,##0_);($#,##0)", 5);
/*  59:140 */     builtIns[6] = new BuiltInFormat("($#,##0_);[Red]($#,##0)", 6);
/*  60:141 */     builtIns[7] = new BuiltInFormat("($#,##0_);[Red]($#,##0)", 7);
/*  61:142 */     builtIns[8] = new BuiltInFormat("($#,##0.00_);[Red]($#,##0.00)", 8);
/*  62:143 */     builtIns[9] = new BuiltInFormat("0%", 9);
/*  63:144 */     builtIns[10] = new BuiltInFormat("0.00%", 10);
/*  64:145 */     builtIns[11] = new BuiltInFormat("0.00E+00", 11);
/*  65:146 */     builtIns[12] = new BuiltInFormat("# ?/?", 12);
/*  66:147 */     builtIns[13] = new BuiltInFormat("# ??/??", 13);
/*  67:148 */     builtIns[14] = new BuiltInFormat("dd/mm/yyyy", 14);
/*  68:149 */     builtIns[15] = new BuiltInFormat("d-mmm-yy", 15);
/*  69:150 */     builtIns[16] = new BuiltInFormat("d-mmm", 16);
/*  70:151 */     builtIns[17] = new BuiltInFormat("mmm-yy", 17);
/*  71:152 */     builtIns[18] = new BuiltInFormat("h:mm AM/PM", 18);
/*  72:153 */     builtIns[19] = new BuiltInFormat("h:mm:ss AM/PM", 19);
/*  73:154 */     builtIns[20] = new BuiltInFormat("h:mm", 20);
/*  74:155 */     builtIns[21] = new BuiltInFormat("h:mm:ss", 21);
/*  75:156 */     builtIns[22] = new BuiltInFormat("m/d/yy h:mm", 22);
/*  76:157 */     builtIns[37] = new BuiltInFormat("(#,##0_);(#,##0)", 37);
/*  77:158 */     builtIns[38] = new BuiltInFormat("(#,##0_);[Red](#,##0)", 38);
/*  78:159 */     builtIns[39] = new BuiltInFormat("(#,##0.00_);(#,##0.00)", 39);
/*  79:160 */     builtIns[40] = new BuiltInFormat("(#,##0.00_);[Red](#,##0.00)", 40);
/*  80:161 */     builtIns[41] = new BuiltInFormat(
/*  81:162 */       "_(*#,##0_);_(*(#,##0);_(*\"-\"_);(@_)", 41);
/*  82:163 */     builtIns[42] = new BuiltInFormat(
/*  83:164 */       "_($*#,##0_);_($*(#,##0);_($*\"-\"_);(@_)", 42);
/*  84:165 */     builtIns[43] = new BuiltInFormat(
/*  85:166 */       "_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);(@_)", 43);
/*  86:167 */     builtIns[44] = new BuiltInFormat(
/*  87:168 */       "_($* #,##0.00_);_($* (#,##0.00);_($* \"-\"??_);(@_)", 44);
/*  88:169 */     builtIns[45] = new BuiltInFormat("mm:ss", 45);
/*  89:170 */     builtIns[46] = new BuiltInFormat("[h]mm:ss", 46);
/*  90:171 */     builtIns[47] = new BuiltInFormat("mm:ss.0", 47);
/*  91:172 */     builtIns[48] = new BuiltInFormat("##0.0E+0", 48);
/*  92:173 */     builtIns[49] = new BuiltInFormat("@", 49);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void initialize(int pos) {}
/*  96:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.BuiltInFormat
 * JD-Core Version:    0.7.0.1
 */