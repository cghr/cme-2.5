/*   1:    */ package jxl.format;
/*   2:    */ 
/*   3:    */ public class VerticalAlignment
/*   4:    */ {
/*   5:    */   private int value;
/*   6:    */   private String string;
/*   7: 40 */   private static VerticalAlignment[] alignments = new VerticalAlignment[0];
/*   8:    */   
/*   9:    */   protected VerticalAlignment(int val, String s)
/*  10:    */   {
/*  11: 49 */     this.value = val;
/*  12: 50 */     this.string = s;
/*  13:    */     
/*  14: 52 */     VerticalAlignment[] oldaligns = alignments;
/*  15: 53 */     alignments = new VerticalAlignment[oldaligns.length + 1];
/*  16: 54 */     System.arraycopy(oldaligns, 0, alignments, 0, oldaligns.length);
/*  17: 55 */     alignments[oldaligns.length] = this;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public int getValue()
/*  21:    */   {
/*  22: 65 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getDescription()
/*  26:    */   {
/*  27: 73 */     return this.string;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static VerticalAlignment getAlignment(int val)
/*  31:    */   {
/*  32: 84 */     for (int i = 0; i < alignments.length; i++) {
/*  33: 86 */       if (alignments[i].getValue() == val) {
/*  34: 88 */         return alignments[i];
/*  35:    */       }
/*  36:    */     }
/*  37: 92 */     return BOTTOM;
/*  38:    */   }
/*  39:    */   
/*  40:100 */   public static VerticalAlignment TOP = new VerticalAlignment(0, "top");
/*  41:105 */   public static VerticalAlignment CENTRE = new VerticalAlignment(1, "centre");
/*  42:110 */   public static VerticalAlignment BOTTOM = new VerticalAlignment(2, "bottom");
/*  43:115 */   public static VerticalAlignment JUSTIFY = new VerticalAlignment(3, "Justify");
/*  44:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.VerticalAlignment
 * JD-Core Version:    0.7.0.1
 */