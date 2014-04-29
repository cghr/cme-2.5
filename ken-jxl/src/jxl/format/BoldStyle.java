/*  1:   */ package jxl.format;
/*  2:   */ 
/*  3:   */ public class BoldStyle
/*  4:   */ {
/*  5:   */   private int value;
/*  6:   */   private String string;
/*  7:   */   
/*  8:   */   protected BoldStyle(int val, String s)
/*  9:   */   {
/* 10:44 */     this.value = val;
/* 11:45 */     this.string = s;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int getValue()
/* 15:   */   {
/* 16:56 */     return this.value;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String getDescription()
/* 20:   */   {
/* 21:64 */     return this.string;
/* 22:   */   }
/* 23:   */   
/* 24:70 */   public static final BoldStyle NORMAL = new BoldStyle(400, "Normal");
/* 25:74 */   public static final BoldStyle BOLD = new BoldStyle(700, "Bold");
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.BoldStyle
 * JD-Core Version:    0.7.0.1
 */