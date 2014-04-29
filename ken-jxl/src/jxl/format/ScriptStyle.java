/*   1:    */ package jxl.format;
/*   2:    */ 
/*   3:    */ public final class ScriptStyle
/*   4:    */ {
/*   5:    */   private int value;
/*   6:    */   private String string;
/*   7: 43 */   private static ScriptStyle[] styles = new ScriptStyle[0];
/*   8:    */   
/*   9:    */   protected ScriptStyle(int val, String s)
/*  10:    */   {
/*  11: 54 */     this.value = val;
/*  12: 55 */     this.string = s;
/*  13:    */     
/*  14: 57 */     ScriptStyle[] oldstyles = styles;
/*  15: 58 */     styles = new ScriptStyle[oldstyles.length + 1];
/*  16: 59 */     System.arraycopy(oldstyles, 0, styles, 0, oldstyles.length);
/*  17: 60 */     styles[oldstyles.length] = this;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public int getValue()
/*  21:    */   {
/*  22: 71 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getDescription()
/*  26:    */   {
/*  27: 81 */     return this.string;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static ScriptStyle getStyle(int val)
/*  31:    */   {
/*  32: 92 */     for (int i = 0; i < styles.length; i++) {
/*  33: 94 */       if (styles[i].getValue() == val) {
/*  34: 96 */         return styles[i];
/*  35:    */       }
/*  36:    */     }
/*  37:100 */     return NORMAL_SCRIPT;
/*  38:    */   }
/*  39:    */   
/*  40:104 */   public static final ScriptStyle NORMAL_SCRIPT = new ScriptStyle(0, "normal");
/*  41:105 */   public static final ScriptStyle SUPERSCRIPT = new ScriptStyle(1, "super");
/*  42:106 */   public static final ScriptStyle SUBSCRIPT = new ScriptStyle(2, "sub");
/*  43:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.ScriptStyle
 * JD-Core Version:    0.7.0.1
 */