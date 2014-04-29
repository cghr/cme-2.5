/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ public final class IntegerHelper
/*   4:    */ {
/*   5:    */   public static int getInt(byte b1, byte b2)
/*   6:    */   {
/*   7: 43 */     int i1 = b1 & 0xFF;
/*   8: 44 */     int i2 = b2 & 0xFF;
/*   9: 45 */     int val = i2 << 8 | i1;
/*  10: 46 */     return val;
/*  11:    */   }
/*  12:    */   
/*  13:    */   public static short getShort(byte b1, byte b2)
/*  14:    */   {
/*  15: 58 */     short i1 = (short)(b1 & 0xFF);
/*  16: 59 */     short i2 = (short)(b2 & 0xFF);
/*  17: 60 */     short val = (short)(i2 << 8 | i1);
/*  18: 61 */     return val;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static int getInt(byte b1, byte b2, byte b3, byte b4)
/*  22:    */   {
/*  23: 76 */     int i1 = getInt(b1, b2);
/*  24: 77 */     int i2 = getInt(b3, b4);
/*  25:    */     
/*  26: 79 */     int val = i2 << 16 | i1;
/*  27: 80 */     return val;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static byte[] getTwoBytes(int i)
/*  31:    */   {
/*  32: 91 */     byte[] bytes = new byte[2];
/*  33:    */     
/*  34: 93 */     bytes[0] = ((byte)(i & 0xFF));
/*  35: 94 */     bytes[1] = ((byte)((i & 0xFF00) >> 8));
/*  36:    */     
/*  37: 96 */     return bytes;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static byte[] getFourBytes(int i)
/*  41:    */   {
/*  42:107 */     byte[] bytes = new byte[4];
/*  43:    */     
/*  44:109 */     int i1 = i & 0xFFFF;
/*  45:110 */     int i2 = (i & 0xFFFF0000) >> 16;
/*  46:    */     
/*  47:112 */     getTwoBytes(i1, bytes, 0);
/*  48:113 */     getTwoBytes(i2, bytes, 2);
/*  49:    */     
/*  50:115 */     return bytes;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static void getTwoBytes(int i, byte[] target, int pos)
/*  54:    */   {
/*  55:129 */     target[pos] = ((byte)(i & 0xFF));
/*  56:130 */     target[(pos + 1)] = ((byte)((i & 0xFF00) >> 8));
/*  57:    */   }
/*  58:    */   
/*  59:    */   public static void getFourBytes(int i, byte[] target, int pos)
/*  60:    */   {
/*  61:143 */     byte[] bytes = getFourBytes(i);
/*  62:144 */     target[pos] = bytes[0];
/*  63:145 */     target[(pos + 1)] = bytes[1];
/*  64:146 */     target[(pos + 2)] = bytes[2];
/*  65:147 */     target[(pos + 3)] = bytes[3];
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.IntegerHelper
 * JD-Core Version:    0.7.0.1
 */