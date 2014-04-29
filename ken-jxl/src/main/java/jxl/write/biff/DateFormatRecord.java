/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.FormatRecord;
/*  4:   */ 
/*  5:   */ public class DateFormatRecord
/*  6:   */   extends FormatRecord
/*  7:   */ {
/*  8:   */   protected DateFormatRecord(String fmt)
/*  9:   */   {
/* 10:39 */     String fs = fmt;
/* 11:   */     
/* 12:41 */     fs = replace(fs, "a", "AM/PM");
/* 13:42 */     fs = replace(fs, "S", "0");
/* 14:   */     
/* 15:44 */     setFormatString(fs);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.DateFormatRecord
 * JD-Core Version:    0.7.0.1
 */