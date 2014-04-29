/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Type;
/*  4:   */ import jxl.biff.WritableRecordData;
/*  5:   */ 
/*  6:   */ class PLSRecord
/*  7:   */   extends WritableRecordData
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   
/* 11:   */   public PLSRecord(jxl.read.biff.PLSRecord hr)
/* 12:   */   {
/* 13:42 */     super(Type.PLS);
/* 14:   */     
/* 15:44 */     this.data = hr.getData();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public PLSRecord(PLSRecord hr)
/* 19:   */   {
/* 20:54 */     super(Type.PLS);
/* 21:   */     
/* 22:56 */     this.data = new byte[hr.data.length];
/* 23:57 */     System.arraycopy(hr.data, 0, this.data, 0, this.data.length);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public byte[] getData()
/* 27:   */   {
/* 28:67 */     return this.data;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.PLSRecord
 * JD-Core Version:    0.7.0.1
 */