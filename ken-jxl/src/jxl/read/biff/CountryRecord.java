/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.IntegerHelper;
/*  4:   */ import jxl.biff.RecordData;
/*  5:   */ import jxl.common.Logger;
/*  6:   */ 
/*  7:   */ public class CountryRecord
/*  8:   */   extends RecordData
/*  9:   */ {
/* 10:35 */   private static Logger logger = Logger.getLogger(CountryRecord.class);
/* 11:   */   private int language;
/* 12:   */   private int regionalSettings;
/* 13:   */   
/* 14:   */   public CountryRecord(Record t)
/* 15:   */   {
/* 16:54 */     super(t);
/* 17:55 */     byte[] data = t.getData();
/* 18:   */     
/* 19:57 */     this.language = IntegerHelper.getInt(data[0], data[1]);
/* 20:58 */     this.regionalSettings = IntegerHelper.getInt(data[2], data[3]);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int getLanguageCode()
/* 24:   */   {
/* 25:68 */     return this.language;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public int getRegionalSettingsCode()
/* 29:   */   {
/* 30:78 */     return this.regionalSettings;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.CountryRecord
 * JD-Core Version:    0.7.0.1
 */