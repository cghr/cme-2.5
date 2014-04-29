/*  1:   */ package jxl.common;
/*  2:   */ 
/*  3:   */ public class LengthConverter
/*  4:   */ {
/*  5:25 */   private static double[][] factors = new double[LengthUnit.getCount()][LengthUnit.getCount()];
/*  6:   */   
/*  7:   */   static
/*  8:   */   {
/*  9:30 */     factors[LengthUnit.POINTS.getIndex()][LengthUnit.POINTS.getIndex()] = 1.0D;
/* 10:31 */     factors[LengthUnit.METRES.getIndex()][LengthUnit.METRES.getIndex()] = 1.0D;
/* 11:32 */     factors[LengthUnit.CENTIMETRES.getIndex()][LengthUnit.CENTIMETRES.getIndex()] = 1.0D;
/* 12:33 */     factors[LengthUnit.INCHES.getIndex()][LengthUnit.INCHES.getIndex()] = 1.0D;
/* 13:   */     
/* 14:   */ 
/* 15:36 */     factors[LengthUnit.POINTS.getIndex()][LengthUnit.METRES.getIndex()] = 0.00035277777778D;
/* 16:37 */     factors[LengthUnit.POINTS.getIndex()][LengthUnit.CENTIMETRES.getIndex()] = 0.035277777778D;
/* 17:38 */     factors[LengthUnit.POINTS.getIndex()][LengthUnit.INCHES.getIndex()] = 0.013888888889D;
/* 18:   */     
/* 19:   */ 
/* 20:41 */     factors[LengthUnit.METRES.getIndex()][LengthUnit.POINTS.getIndex()] = 2877.8400000000001D;
/* 21:42 */     factors[LengthUnit.METRES.getIndex()][LengthUnit.CENTIMETRES.getIndex()] = 100.0D;
/* 22:43 */     factors[LengthUnit.METRES.getIndex()][LengthUnit.INCHES.getIndex()] = 39.369999999999997D;
/* 23:   */     
/* 24:   */ 
/* 25:46 */     factors[LengthUnit.CENTIMETRES.getIndex()][LengthUnit.POINTS.getIndex()] = 28.346430000000002D;
/* 26:47 */     factors[LengthUnit.CENTIMETRES.getIndex()][LengthUnit.METRES.getIndex()] = 0.01D;
/* 27:48 */     factors[LengthUnit.CENTIMETRES.getIndex()][LengthUnit.INCHES.getIndex()] = 0.3937D;
/* 28:   */     
/* 29:   */ 
/* 30:51 */     factors[LengthUnit.INCHES.getIndex()][LengthUnit.POINTS.getIndex()] = 72.0D;
/* 31:52 */     factors[LengthUnit.INCHES.getIndex()][LengthUnit.METRES.getIndex()] = 0.0254D;
/* 32:53 */     factors[LengthUnit.INCHES.getIndex()][LengthUnit.CENTIMETRES.getIndex()] = 2.54D;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public static double getConversionFactor(LengthUnit from, LengthUnit to)
/* 36:   */   {
/* 37:58 */     return factors[from.getIndex()][to.getIndex()];
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.common.LengthConverter
 * JD-Core Version:    0.7.0.1
 */