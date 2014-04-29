/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.CountryCode;
/*  4:   */ import jxl.biff.IntegerHelper;
/*  5:   */ import jxl.biff.Type;
/*  6:   */ import jxl.biff.WritableRecordData;
/*  7:   */ 
/*  8:   */ class CountryRecord
/*  9:   */   extends WritableRecordData
/* 10:   */ {
/* 11:   */   private int language;
/* 12:   */   private int regionalSettings;
/* 13:   */   
/* 14:   */   public CountryRecord(CountryCode lang, CountryCode r)
/* 15:   */   {
/* 16:48 */     super(Type.COUNTRY);
/* 17:   */     
/* 18:50 */     this.language = lang.getValue();
/* 19:51 */     this.regionalSettings = r.getValue();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public CountryRecord(jxl.read.biff.CountryRecord cr)
/* 23:   */   {
/* 24:56 */     super(Type.COUNTRY);
/* 25:   */     
/* 26:58 */     this.language = cr.getLanguageCode();
/* 27:59 */     this.regionalSettings = cr.getRegionalSettingsCode();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public byte[] getData()
/* 31:   */   {
/* 32:69 */     byte[] data = new byte[4];
/* 33:   */     
/* 34:71 */     IntegerHelper.getTwoBytes(this.language, data, 0);
/* 35:72 */     IntegerHelper.getTwoBytes(this.regionalSettings, data, 2);
/* 36:   */     
/* 37:74 */     return data;
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.CountryRecord
 * JD-Core Version:    0.7.0.1
 */