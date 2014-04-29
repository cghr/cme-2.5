/*  1:   */ package org.springframework.format.datetime.joda;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.joda.time.ReadableInstant;
/*  5:   */ import org.joda.time.format.DateTimeFormatter;
/*  6:   */ import org.springframework.format.Printer;
/*  7:   */ 
/*  8:   */ public final class ReadableInstantPrinter
/*  9:   */   implements Printer<ReadableInstant>
/* 10:   */ {
/* 11:   */   private final DateTimeFormatter formatter;
/* 12:   */   
/* 13:   */   public ReadableInstantPrinter(DateTimeFormatter formatter)
/* 14:   */   {
/* 15:40 */     this.formatter = formatter;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String print(ReadableInstant instant, Locale locale)
/* 19:   */   {
/* 20:44 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(instant);
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.joda.ReadableInstantPrinter
 * JD-Core Version:    0.7.0.1
 */