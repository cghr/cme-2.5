/*  1:   */ package org.springframework.format.datetime.joda;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.joda.time.ReadablePartial;
/*  5:   */ import org.joda.time.format.DateTimeFormatter;
/*  6:   */ import org.springframework.format.Printer;
/*  7:   */ 
/*  8:   */ public final class ReadablePartialPrinter
/*  9:   */   implements Printer<ReadablePartial>
/* 10:   */ {
/* 11:   */   private final DateTimeFormatter formatter;
/* 12:   */   
/* 13:   */   public ReadablePartialPrinter(DateTimeFormatter formatter)
/* 14:   */   {
/* 15:41 */     this.formatter = formatter;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String print(ReadablePartial partial, Locale locale)
/* 19:   */   {
/* 20:45 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(partial);
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.joda.ReadablePartialPrinter
 * JD-Core Version:    0.7.0.1
 */