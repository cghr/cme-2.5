/*  1:   */ package org.springframework.format.datetime.joda;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.joda.time.format.DateTimeFormatter;
/*  5:   */ import org.springframework.format.Printer;
/*  6:   */ 
/*  7:   */ public final class MillisecondInstantPrinter
/*  8:   */   implements Printer<Long>
/*  9:   */ {
/* 10:   */   private final DateTimeFormatter formatter;
/* 11:   */   
/* 12:   */   public MillisecondInstantPrinter(DateTimeFormatter formatter)
/* 13:   */   {
/* 14:40 */     this.formatter = formatter;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String print(Long instant, Locale locale)
/* 18:   */   {
/* 19:44 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(instant.longValue());
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.joda.MillisecondInstantPrinter
 * JD-Core Version:    0.7.0.1
 */