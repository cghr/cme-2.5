/*  1:   */ package jxl.format;
/*  2:   */ 
/*  3:   */ public class Border
/*  4:   */ {
/*  5:   */   private String string;
/*  6:   */   
/*  7:   */   protected Border(String s)
/*  8:   */   {
/*  9:37 */     this.string = s;
/* 10:   */   }
/* 11:   */   
/* 12:   */   public String getDescription()
/* 13:   */   {
/* 14:45 */     return this.string;
/* 15:   */   }
/* 16:   */   
/* 17:48 */   public static final Border NONE = new Border("none");
/* 18:49 */   public static final Border ALL = new Border("all");
/* 19:50 */   public static final Border TOP = new Border("top");
/* 20:51 */   public static final Border BOTTOM = new Border("bottom");
/* 21:52 */   public static final Border LEFT = new Border("left");
/* 22:53 */   public static final Border RIGHT = new Border("right");
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.Border
 * JD-Core Version:    0.7.0.1
 */