/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class DefaultRowHeightRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private byte[] data;
/* 11:   */   private int rowHeight;
/* 12:   */   private boolean changed;
/* 13:   */   
/* 14:   */   public DefaultRowHeightRecord(int h, boolean ch)
/* 15:   */   {
/* 16:54 */     super(Type.DEFAULTROWHEIGHT);
/* 17:55 */     this.data = new byte[4];
/* 18:56 */     this.rowHeight = h;
/* 19:57 */     this.changed = ch;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public byte[] getData()
/* 23:   */   {
/* 24:67 */     if (this.changed)
/* 25:   */     {
/* 26:69 */       int tmp12_11 = 0; byte[] tmp12_8 = this.data;tmp12_8[tmp12_11] = ((byte)(tmp12_8[tmp12_11] | 0x1));
/* 27:   */     }
/* 28:72 */     IntegerHelper.getTwoBytes(this.rowHeight, this.data, 2);
/* 29:73 */     return this.data;
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.DefaultRowHeightRecord
 * JD-Core Version:    0.7.0.1
 */