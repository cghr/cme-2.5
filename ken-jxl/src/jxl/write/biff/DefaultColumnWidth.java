/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class DefaultColumnWidth
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private int width;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public DefaultColumnWidth(int w)
/* 14:   */   {
/* 15:47 */     super(Type.DEFCOLWIDTH);
/* 16:48 */     this.width = w;
/* 17:49 */     this.data = new byte[2];
/* 18:50 */     IntegerHelper.getTwoBytes(this.width, this.data, 0);
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected byte[] getData()
/* 22:   */   {
/* 23:60 */     return this.data;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.DefaultColumnWidth
 * JD-Core Version:    0.7.0.1
 */