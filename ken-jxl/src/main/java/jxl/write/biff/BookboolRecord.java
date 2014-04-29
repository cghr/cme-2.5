/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class BookboolRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private boolean externalLink;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public BookboolRecord(boolean extlink)
/* 14:   */   {
/* 15:48 */     super(Type.BOOKBOOL);
/* 16:   */     
/* 17:50 */     this.externalLink = extlink;
/* 18:51 */     this.data = new byte[2];
/* 19:53 */     if (!this.externalLink) {
/* 20:55 */       IntegerHelper.getTwoBytes(1, this.data, 0);
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:   */   public byte[] getData()
/* 25:   */   {
/* 26:66 */     return this.data;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.BookboolRecord
 * JD-Core Version:    0.7.0.1
 */