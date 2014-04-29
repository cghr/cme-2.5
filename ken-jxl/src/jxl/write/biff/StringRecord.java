/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.StringHelper;
/*  5:   */ import jxl.biff.Type;
/*  6:   */ import jxl.biff.WritableRecordData;
/*  7:   */ 
/*  8:   */ class StringRecord
/*  9:   */   extends WritableRecordData
/* 10:   */ {
/* 11:   */   private String value;
/* 12:   */   
/* 13:   */   public StringRecord(String val)
/* 14:   */   {
/* 15:43 */     super(Type.STRING);
/* 16:   */     
/* 17:45 */     this.value = val;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public byte[] getData()
/* 21:   */   {
/* 22:55 */     byte[] data = new byte[this.value.length() * 2 + 3];
/* 23:56 */     IntegerHelper.getTwoBytes(this.value.length(), data, 0);
/* 24:57 */     data[2] = 1;
/* 25:58 */     StringHelper.getUnicodeBytes(this.value, data, 3);
/* 26:   */     
/* 27:60 */     return data;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.StringRecord
 * JD-Core Version:    0.7.0.1
 */