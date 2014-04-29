/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.Workbook;
/*  4:   */ import jxl.biff.StringHelper;
/*  5:   */ import jxl.biff.Type;
/*  6:   */ import jxl.biff.WritableRecordData;
/*  7:   */ 
/*  8:   */ class WriteAccessRecord
/*  9:   */   extends WritableRecordData
/* 10:   */ {
/* 11:   */   private byte[] data;
/* 12:   */   private static final String authorString = "Java Excel API";
/* 13:   */   private String userName;
/* 14:   */   
/* 15:   */   public WriteAccessRecord(String userName)
/* 16:   */   {
/* 17:52 */     super(Type.WRITEACCESS);
/* 18:   */     
/* 19:54 */     this.data = new byte[112];
/* 20:55 */     String astring = 
/* 21:   */     
/* 22:57 */       "Java Excel API v" + Workbook.getVersion();
/* 23:   */     
/* 24:59 */     StringHelper.getBytes(astring, this.data, 0);
/* 25:62 */     for (int i = astring.length(); i < this.data.length; i++) {
/* 26:64 */       this.data[i] = 32;
/* 27:   */     }
/* 28:   */   }
/* 29:   */   
/* 30:   */   public byte[] getData()
/* 31:   */   {
/* 32:75 */     return this.data;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.WriteAccessRecord
 * JD-Core Version:    0.7.0.1
 */