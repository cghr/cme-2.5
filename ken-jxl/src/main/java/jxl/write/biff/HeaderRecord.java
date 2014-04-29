/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.StringHelper;
/*  5:   */ import jxl.biff.Type;
/*  6:   */ import jxl.biff.WritableRecordData;
/*  7:   */ 
/*  8:   */ class HeaderRecord
/*  9:   */   extends WritableRecordData
/* 10:   */ {
/* 11:   */   private byte[] data;
/* 12:   */   private String header;
/* 13:   */   
/* 14:   */   public HeaderRecord(String h)
/* 15:   */   {
/* 16:48 */     super(Type.HEADER);
/* 17:   */     
/* 18:50 */     this.header = h;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public HeaderRecord(HeaderRecord hr)
/* 22:   */   {
/* 23:60 */     super(Type.HEADER);
/* 24:   */     
/* 25:62 */     this.header = hr.header;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public byte[] getData()
/* 29:   */   {
/* 30:72 */     if ((this.header == null) || (this.header.length() == 0))
/* 31:   */     {
/* 32:74 */       this.data = new byte[0];
/* 33:75 */       return this.data;
/* 34:   */     }
/* 35:78 */     this.data = new byte[this.header.length() * 2 + 3];
/* 36:79 */     IntegerHelper.getTwoBytes(this.header.length(), this.data, 0);
/* 37:80 */     this.data[2] = 1;
/* 38:   */     
/* 39:82 */     StringHelper.getUnicodeBytes(this.header, this.data, 3);
/* 40:   */     
/* 41:84 */     return this.data;
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.HeaderRecord
 * JD-Core Version:    0.7.0.1
 */