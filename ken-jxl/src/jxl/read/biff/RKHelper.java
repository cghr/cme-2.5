/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ final class RKHelper
/*  4:   */ {
/*  5:   */   public static double getDouble(int rk)
/*  6:   */   {
/*  7:42 */     if ((rk & 0x2) != 0)
/*  8:   */     {
/*  9:44 */       int intval = rk >> 2;
/* 10:   */       
/* 11:46 */       double value = intval;
/* 12:47 */       if ((rk & 0x1) != 0) {
/* 13:49 */         value /= 100.0D;
/* 14:   */       }
/* 15:52 */       return value;
/* 16:   */     }
/* 17:56 */     long valbits = rk & 0xFFFFFFFC;
/* 18:57 */     valbits <<= 32;
/* 19:58 */     double value = Double.longBitsToDouble(valbits);
/* 20:60 */     if ((rk & 0x1) != 0) {
/* 21:62 */       value /= 100.0D;
/* 22:   */     }
/* 23:65 */     return value;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.RKHelper
 * JD-Core Version:    0.7.0.1
 */