/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ 
/*  6:   */ class ProtectRecord
/*  7:   */   extends RecordData
/*  8:   */ {
/*  9:   */   private boolean prot;
/* 10:   */   
/* 11:   */   ProtectRecord(Record t)
/* 12:   */   {
/* 13:42 */     super(t);
/* 14:43 */     byte[] data = getRecord().getData();
/* 15:   */     
/* 16:45 */     int protflag = IntegerHelper.getInt(data[0], data[1]);
/* 17:   */     
/* 18:47 */     this.prot = (protflag == 1);
/* 19:   */   }
/* 20:   */   
/* 21:   */   boolean isProtected()
/* 22:   */   {
/* 23:57 */     return this.prot;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.ProtectRecord
 * JD-Core Version:    0.7.0.1
 */