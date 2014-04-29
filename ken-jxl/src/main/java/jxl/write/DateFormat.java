/*  1:   */ package jxl.write;
/*  2:   */ 
/*  3:   */ import java.text.SimpleDateFormat;
/*  4:   */ import jxl.biff.DisplayFormat;
/*  5:   */ import jxl.write.biff.DateFormatRecord;
/*  6:   */ 
/*  7:   */ public class DateFormat
/*  8:   */   extends DateFormatRecord
/*  9:   */   implements DisplayFormat
/* 10:   */ {
/* 11:   */   public DateFormat(String format)
/* 12:   */   {
/* 13:47 */     super(format);
/* 14:   */     
/* 15:   */ 
/* 16:50 */     SimpleDateFormat df = new SimpleDateFormat(format);
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.DateFormat
 * JD-Core Version:    0.7.0.1
 */