/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ 
/*  7:   */ class BackupRecord
/*  8:   */   extends WritableRecordData
/*  9:   */ {
/* 10:   */   private boolean backup;
/* 11:   */   private byte[] data;
/* 12:   */   
/* 13:   */   public BackupRecord(boolean bu)
/* 14:   */   {
/* 15:48 */     super(Type.BACKUP);
/* 16:   */     
/* 17:50 */     this.backup = bu;
/* 18:   */     
/* 19:   */ 
/* 20:53 */     this.data = new byte[2];
/* 21:55 */     if (this.backup) {
/* 22:57 */       IntegerHelper.getTwoBytes(1, this.data, 0);
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   public byte[] getData()
/* 27:   */   {
/* 28:68 */     return this.data;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.BackupRecord
 * JD-Core Version:    0.7.0.1
 */