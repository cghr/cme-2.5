/*  1:   */ package org.springframework.format.datetime.joda;
/*  2:   */ 
/*  3:   */ import java.text.ParseException;
/*  4:   */ import java.util.Locale;
/*  5:   */ import org.joda.time.DateTime;
/*  6:   */ import org.joda.time.format.DateTimeFormatter;
/*  7:   */ import org.springframework.format.Parser;
/*  8:   */ 
/*  9:   */ public final class DateTimeParser
/* 10:   */   implements Parser<DateTime>
/* 11:   */ {
/* 12:   */   private final DateTimeFormatter formatter;
/* 13:   */   
/* 14:   */   public DateTimeParser(DateTimeFormatter formatter)
/* 15:   */   {
/* 16:42 */     this.formatter = formatter;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public DateTime parse(String text, Locale locale)
/* 20:   */     throws ParseException
/* 21:   */   {
/* 22:46 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).parseDateTime(text);
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.joda.DateTimeParser
 * JD-Core Version:    0.7.0.1
 */