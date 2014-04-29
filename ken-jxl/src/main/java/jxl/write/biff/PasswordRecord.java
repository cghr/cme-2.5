/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ 
/*   7:    */ class PasswordRecord
/*   8:    */   extends WritableRecordData
/*   9:    */ {
/*  10:    */   private String password;
/*  11:    */   private byte[] data;
/*  12:    */   
/*  13:    */   public PasswordRecord(String pw)
/*  14:    */   {
/*  15: 48 */     super(Type.PASSWORD);
/*  16:    */     
/*  17: 50 */     this.password = pw;
/*  18: 52 */     if (pw == null)
/*  19:    */     {
/*  20: 54 */       this.data = new byte[2];
/*  21: 55 */       IntegerHelper.getTwoBytes(0, this.data, 0);
/*  22:    */     }
/*  23:    */     else
/*  24:    */     {
/*  25: 59 */       byte[] passwordBytes = pw.getBytes();
/*  26: 60 */       int passwordHash = 0;
/*  27: 61 */       for (int a = 0; a < passwordBytes.length; a++)
/*  28:    */       {
/*  29: 63 */         int shifted = rotLeft15Bit(passwordBytes[a], a + 1);
/*  30: 64 */         passwordHash ^= shifted;
/*  31:    */       }
/*  32: 66 */       passwordHash ^= passwordBytes.length;
/*  33: 67 */       passwordHash ^= 0xCE4B;
/*  34:    */       
/*  35: 69 */       this.data = new byte[2];
/*  36: 70 */       IntegerHelper.getTwoBytes(passwordHash, this.data, 0);
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public PasswordRecord(int ph)
/*  41:    */   {
/*  42: 81 */     super(Type.PASSWORD);
/*  43:    */     
/*  44: 83 */     this.data = new byte[2];
/*  45: 84 */     IntegerHelper.getTwoBytes(ph, this.data, 0);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public byte[] getData()
/*  49:    */   {
/*  50: 94 */     return this.data;
/*  51:    */   }
/*  52:    */   
/*  53:    */   private int rotLeft15Bit(int val, int rotate)
/*  54:    */   {
/*  55:106 */     val &= 0x7FFF;
/*  56:108 */     for (; rotate > 0; rotate--) {
/*  57:110 */       if ((val & 0x4000) != 0) {
/*  58:112 */         val = (val << 1 & 0x7FFF) + 1;
/*  59:    */       } else {
/*  60:116 */         val = val << 1 & 0x7FFF;
/*  61:    */       }
/*  62:    */     }
/*  63:120 */     return val;
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.PasswordRecord
 * JD-Core Version:    0.7.0.1
 */