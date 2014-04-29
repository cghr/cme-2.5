/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class HideobjRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private int hidemode;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public HideobjRecord(int newHideMode)
/* 14:   */   {
/* 15:53 */     super(Type.HIDEOBJ);
/* 16:   */     
/* 17:55 */     this.hidemode = newHideMode;
/* 18:56 */     this.data = new byte[2];
/* 19:   */     
/* 20:58 */     IntegerHelper.getTwoBytes(this.hidemode, this.data, 0);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public byte[] getData()
/* 24:   */   {
/* 25:68 */     return this.data;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.HideobjRecord
 * JD-Core Version:    0.7.0.1
 */