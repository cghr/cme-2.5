/*   1:    */ package jxl.format;
/*   2:    */ 
/*   3:    */ public class Alignment
/*   4:    */ {
/*   5:    */   private int value;
/*   6:    */   private String string;
/*   7: 41 */   private static Alignment[] alignments = new Alignment[0];
/*   8:    */   
/*   9:    */   protected Alignment(int val, String s)
/*  10:    */   {
/*  11: 51 */     this.value = val;
/*  12: 52 */     this.string = s;
/*  13:    */     
/*  14: 54 */     Alignment[] oldaligns = alignments;
/*  15: 55 */     alignments = new Alignment[oldaligns.length + 1];
/*  16: 56 */     System.arraycopy(oldaligns, 0, alignments, 0, oldaligns.length);
/*  17: 57 */     alignments[oldaligns.length] = this;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public int getValue()
/*  21:    */   {
/*  22: 68 */     return this.value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getDescription()
/*  26:    */   {
/*  27: 78 */     return this.string;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static Alignment getAlignment(int val)
/*  31:    */   {
/*  32: 89 */     for (int i = 0; i < alignments.length; i++) {
/*  33: 91 */       if (alignments[i].getValue() == val) {
/*  34: 93 */         return alignments[i];
/*  35:    */       }
/*  36:    */     }
/*  37: 97 */     return GENERAL;
/*  38:    */   }
/*  39:    */   
/*  40:103 */   public static Alignment GENERAL = new Alignment(0, "general");
/*  41:108 */   public static Alignment LEFT = new Alignment(1, "left");
/*  42:112 */   public static Alignment CENTRE = new Alignment(2, "centre");
/*  43:116 */   public static Alignment RIGHT = new Alignment(3, "right");
/*  44:120 */   public static Alignment FILL = new Alignment(4, "fill");
/*  45:124 */   public static Alignment JUSTIFY = new Alignment(5, "justify");
/*  46:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.Alignment
 * JD-Core Version:    0.7.0.1
 */