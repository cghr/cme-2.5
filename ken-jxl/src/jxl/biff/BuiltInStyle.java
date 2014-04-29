/*  1:   */ package jxl.biff;
/*  2:   */ 
/*  3:   */ class BuiltInStyle
/*  4:   */   extends WritableRecordData
/*  5:   */ {
/*  6:   */   private int xfIndex;
/*  7:   */   private int styleNumber;
/*  8:   */   
/*  9:   */   public BuiltInStyle(int xfind, int sn)
/* 10:   */   {
/* 11:46 */     super(Type.STYLE);
/* 12:   */     
/* 13:48 */     this.xfIndex = xfind;
/* 14:49 */     this.styleNumber = sn;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public byte[] getData()
/* 18:   */   {
/* 19:59 */     byte[] data = new byte[4];
/* 20:   */     
/* 21:61 */     IntegerHelper.getTwoBytes(this.xfIndex, data, 0); int 
/* 22:   */     
/* 23:   */ 
/* 24:64 */       tmp15_14 = 1; byte[] tmp15_13 = data;tmp15_13[tmp15_14] = ((byte)(tmp15_13[tmp15_14] | 0x80));
/* 25:   */     
/* 26:66 */     data[2] = ((byte)this.styleNumber);
/* 27:   */     
/* 28:   */ 
/* 29:69 */     data[3] = -1;
/* 30:   */     
/* 31:71 */     return data;
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.BuiltInStyle
 * JD-Core Version:    0.7.0.1
 */