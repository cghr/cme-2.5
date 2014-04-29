/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.StringHelper;
/*  5:   */ import jxl.biff.Type;
/*  6:   */ import jxl.biff.WritableRecordData;
/*  7:   */ 
/*  8:   */ class FooterRecord
/*  9:   */   extends WritableRecordData
/* 10:   */ {
/* 11:   */   private byte[] data;
/* 12:   */   private String footer;
/* 13:   */   
/* 14:   */   public FooterRecord(String s)
/* 15:   */   {
/* 16:49 */     super(Type.FOOTER);
/* 17:   */     
/* 18:51 */     this.footer = s;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public FooterRecord(FooterRecord fr)
/* 22:   */   {
/* 23:61 */     super(Type.FOOTER);
/* 24:   */     
/* 25:63 */     this.footer = fr.footer;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public byte[] getData()
/* 29:   */   {
/* 30:73 */     if ((this.footer == null) || (this.footer.length() == 0))
/* 31:   */     {
/* 32:75 */       this.data = new byte[0];
/* 33:76 */       return this.data;
/* 34:   */     }
/* 35:79 */     this.data = new byte[this.footer.length() * 2 + 3];
/* 36:80 */     IntegerHelper.getTwoBytes(this.footer.length(), this.data, 0);
/* 37:81 */     this.data[2] = 1;
/* 38:   */     
/* 39:83 */     StringHelper.getUnicodeBytes(this.footer, this.data, 3);
/* 40:   */     
/* 41:85 */     return this.data;
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.FooterRecord
 * JD-Core Version:    0.7.0.1
 */