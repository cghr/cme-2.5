/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.StringHelper;
/*  4:   */ import jxl.biff.Type;
/*  5:   */ import jxl.biff.WritableRecordData;
/*  6:   */ import jxl.common.Logger;
/*  7:   */ 
/*  8:   */ class ExternalNameRecord
/*  9:   */   extends WritableRecordData
/* 10:   */ {
/* 11:37 */   Logger logger = Logger.getLogger(ExternalNameRecord.class);
/* 12:   */   private String name;
/* 13:   */   
/* 14:   */   public ExternalNameRecord(String n)
/* 15:   */   {
/* 16:49 */     super(Type.EXTERNNAME);
/* 17:50 */     this.name = n;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public byte[] getData()
/* 21:   */   {
/* 22:60 */     byte[] data = new byte[this.name.length() * 2 + 12];
/* 23:   */     
/* 24:62 */     data[6] = ((byte)this.name.length());
/* 25:63 */     data[7] = 1;
/* 26:64 */     StringHelper.getUnicodeBytes(this.name, data, 8);
/* 27:   */     
/* 28:66 */     int pos = 8 + this.name.length() * 2;
/* 29:67 */     data[pos] = 2;
/* 30:68 */     data[(pos + 1)] = 0;
/* 31:69 */     data[(pos + 2)] = 28;
/* 32:70 */     data[(pos + 3)] = 23;
/* 33:   */     
/* 34:72 */     return data;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ExternalNameRecord
 * JD-Core Version:    0.7.0.1
 */