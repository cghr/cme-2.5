/*  1:   */ package jxl.common;
/*  2:   */ 
/*  3:   */ public class LengthUnit
/*  4:   */   extends BaseUnit
/*  5:   */ {
/*  6:28 */   private static int count = 0;
/*  7:   */   
/*  8:   */   private LengthUnit()
/*  9:   */   {
/* 10:32 */     super(count++);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public static int getCount()
/* 14:   */   {
/* 15:37 */     return count;
/* 16:   */   }
/* 17:   */   
/* 18:40 */   public static LengthUnit POINTS = new LengthUnit();
/* 19:41 */   public static LengthUnit METRES = new LengthUnit();
/* 20:42 */   public static LengthUnit CENTIMETRES = new LengthUnit();
/* 21:43 */   public static LengthUnit INCHES = new LengthUnit();
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.common.LengthUnit
 * JD-Core Version:    0.7.0.1
 */