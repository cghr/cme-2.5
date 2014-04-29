/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.WorkbookSettings;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.biff.StringHelper;
/*  6:   */ import jxl.biff.Type;
/*  7:   */ 
/*  8:   */ class WriteAccessRecord
/*  9:   */   extends RecordData
/* 10:   */ {
/* 11:   */   private String wauser;
/* 12:   */   
/* 13:   */   public WriteAccessRecord(Record t, boolean isBiff8, WorkbookSettings ws)
/* 14:   */   {
/* 15:45 */     super(Type.WRITEACCESS);
/* 16:   */     
/* 17:47 */     byte[] data = t.getData();
/* 18:48 */     if (isBiff8)
/* 19:   */     {
/* 20:50 */       this.wauser = StringHelper.getUnicodeString(data, 56, 0);
/* 21:   */     }
/* 22:   */     else
/* 23:   */     {
/* 24:55 */       int length = data[1];
/* 25:56 */       this.wauser = StringHelper.getString(data, length, 1, ws);
/* 26:   */     }
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String getWriteAccess()
/* 30:   */   {
/* 31:67 */     return this.wauser;
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.WriteAccessRecord
 * JD-Core Version:    0.7.0.1
 */