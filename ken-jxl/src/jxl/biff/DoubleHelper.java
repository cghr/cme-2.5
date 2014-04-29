/*  1:   */ package jxl.biff;
/*  2:   */ 
/*  3:   */ public class DoubleHelper
/*  4:   */ {
/*  5:   */   public static double getIEEEDouble(byte[] data, int pos)
/*  6:   */   {
/*  7:43 */     int num1 = IntegerHelper.getInt(data[pos], data[(pos + 1)], 
/*  8:44 */       data[(pos + 2)], data[(pos + 3)]);
/*  9:45 */     int num2 = IntegerHelper.getInt(data[(pos + 4)], data[(pos + 5)], 
/* 10:46 */       data[(pos + 6)], data[(pos + 7)]);
/* 11:   */     
/* 12:   */ 
/* 13:   */ 
/* 14:   */ 
/* 15:51 */     boolean negative = (num2 & 0x80000000) != 0;
/* 16:   */     
/* 17:   */ 
/* 18:54 */     long val = (num2 & 0x7FFFFFFF) * 4294967296L + (
/* 19:55 */       num1 < 0 ? 4294967296L + num1 : num1);
/* 20:56 */     double value = Double.longBitsToDouble(val);
/* 21:58 */     if (negative) {
/* 22:60 */       value = -value;
/* 23:   */     }
/* 24:62 */     return value;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public static void getIEEEBytes(double d, byte[] target, int pos)
/* 28:   */   {
/* 29:76 */     long val = Double.doubleToLongBits(d);
/* 30:77 */     target[pos] = ((byte)(int)(val & 0xFF));
/* 31:78 */     target[(pos + 1)] = ((byte)(int)((val & 0xFF00) >> 8));
/* 32:79 */     target[(pos + 2)] = ((byte)(int)((val & 0xFF0000) >> 16));
/* 33:80 */     target[(pos + 3)] = ((byte)(int)((val & 0xFF000000) >> 24));
/* 34:81 */     target[(pos + 4)] = ((byte)(int)((val & 0x0) >> 32));
/* 35:82 */     target[(pos + 5)] = ((byte)(int)((val & 0x0) >> 40));
/* 36:83 */     target[(pos + 6)] = ((byte)(int)((val & 0x0) >> 48));
/* 37:84 */     target[(pos + 7)] = ((byte)(int)((val & 0x0) >> 56));
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.DoubleHelper
 * JD-Core Version:    0.7.0.1
 */