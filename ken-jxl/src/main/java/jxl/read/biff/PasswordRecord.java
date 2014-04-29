/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.biff.Type;
/*  6:   */ 
/*  7:   */ class PasswordRecord
/*  8:   */   extends RecordData
/*  9:   */ {
/* 10:   */   private String password;
/* 11:   */   private int passwordHash;
/* 12:   */   
/* 13:   */   public PasswordRecord(Record t)
/* 14:   */   {
/* 15:47 */     super(Type.PASSWORD);
/* 16:   */     
/* 17:49 */     byte[] data = t.getData();
/* 18:50 */     this.passwordHash = IntegerHelper.getInt(data[0], data[1]);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public int getPasswordHash()
/* 22:   */   {
/* 23:60 */     return this.passwordHash;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.PasswordRecord
 * JD-Core Version:    0.7.0.1
 */