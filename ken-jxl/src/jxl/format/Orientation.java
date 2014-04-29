/*   1:    */ package jxl.format;
/*   2:    */ 
/*   3:    */ public final class Orientation
/*   4:    */ {
/*   5:    */   private int value;
/*   6:    */   private String string;
/*   7: 40 */   private static Orientation[] orientations = new Orientation[0];
/*   8:    */   
/*   9:    */   protected Orientation(int val, String s)
/*  10:    */   {
/*  11: 49 */     this.value = val;this.string = s;
/*  12:    */     
/*  13: 51 */     Orientation[] oldorients = orientations;
/*  14: 52 */     orientations = new Orientation[oldorients.length + 1];
/*  15: 53 */     System.arraycopy(oldorients, 0, orientations, 0, oldorients.length);
/*  16: 54 */     orientations[oldorients.length] = this;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public int getValue()
/*  20:    */   {
/*  21: 64 */     return this.value;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public String getDescription()
/*  25:    */   {
/*  26: 72 */     return this.string;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static Orientation getOrientation(int val)
/*  30:    */   {
/*  31: 83 */     for (int i = 0; i < orientations.length; i++) {
/*  32: 85 */       if (orientations[i].getValue() == val) {
/*  33: 87 */         return orientations[i];
/*  34:    */       }
/*  35:    */     }
/*  36: 91 */     return HORIZONTAL;
/*  37:    */   }
/*  38:    */   
/*  39: 98 */   public static Orientation HORIZONTAL = new Orientation(0, "horizontal");
/*  40:103 */   public static Orientation VERTICAL = new Orientation(255, "vertical");
/*  41:108 */   public static Orientation PLUS_90 = new Orientation(90, "up 90");
/*  42:113 */   public static Orientation MINUS_90 = new Orientation(180, "down 90");
/*  43:118 */   public static Orientation PLUS_45 = new Orientation(45, "up 45");
/*  44:123 */   public static Orientation MINUS_45 = new Orientation(135, "down 45");
/*  45:128 */   public static Orientation STACKED = new Orientation(255, "stacked");
/*  46:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.Orientation
 * JD-Core Version:    0.7.0.1
 */