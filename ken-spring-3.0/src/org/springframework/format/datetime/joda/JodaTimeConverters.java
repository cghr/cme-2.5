/*   1:    */ package org.springframework.format.datetime.joda;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import java.util.Date;
/*   5:    */ import org.joda.time.DateMidnight;
/*   6:    */ import org.joda.time.DateTime;
/*   7:    */ import org.joda.time.Instant;
/*   8:    */ import org.joda.time.LocalDate;
/*   9:    */ import org.joda.time.LocalDateTime;
/*  10:    */ import org.joda.time.LocalTime;
/*  11:    */ import org.joda.time.MutableDateTime;
/*  12:    */ import org.joda.time.ReadableInstant;
/*  13:    */ import org.springframework.core.convert.converter.Converter;
/*  14:    */ import org.springframework.core.convert.converter.ConverterRegistry;
/*  15:    */ 
/*  16:    */ final class JodaTimeConverters
/*  17:    */ {
/*  18:    */   public static void registerConverters(ConverterRegistry registry)
/*  19:    */   {
/*  20: 46 */     registry.addConverter(new DateTimeToLocalDateConverter(null));
/*  21: 47 */     registry.addConverter(new DateTimeToLocalTimeConverter(null));
/*  22: 48 */     registry.addConverter(new DateTimeToLocalDateTimeConverter(null));
/*  23: 49 */     registry.addConverter(new DateTimeToDateMidnightConverter(null));
/*  24: 50 */     registry.addConverter(new DateTimeToInstantConverter(null));
/*  25: 51 */     registry.addConverter(new DateTimeToMutableDateTimeConverter(null));
/*  26: 52 */     registry.addConverter(new DateTimeToDateConverter(null));
/*  27: 53 */     registry.addConverter(new DateTimeToCalendarConverter(null));
/*  28: 54 */     registry.addConverter(new DateTimeToLongConverter(null));
/*  29: 55 */     registry.addConverter(new DateToLongConverter(null));
/*  30: 56 */     registry.addConverter(new CalendarToReadableInstantConverter(null));
/*  31:    */   }
/*  32:    */   
/*  33:    */   private static class DateTimeToLocalDateConverter
/*  34:    */     implements Converter<DateTime, LocalDate>
/*  35:    */   {
/*  36:    */     public LocalDate convert(DateTime source)
/*  37:    */     {
/*  38: 66 */       return source.toLocalDate();
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   private static class DateTimeToLocalTimeConverter
/*  43:    */     implements Converter<DateTime, LocalTime>
/*  44:    */   {
/*  45:    */     public LocalTime convert(DateTime source)
/*  46:    */     {
/*  47: 76 */       return source.toLocalTime();
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   private static class DateTimeToLocalDateTimeConverter
/*  52:    */     implements Converter<DateTime, LocalDateTime>
/*  53:    */   {
/*  54:    */     public LocalDateTime convert(DateTime source)
/*  55:    */     {
/*  56: 86 */       return source.toLocalDateTime();
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   private static class DateTimeToDateMidnightConverter
/*  61:    */     implements Converter<DateTime, DateMidnight>
/*  62:    */   {
/*  63:    */     public DateMidnight convert(DateTime source)
/*  64:    */     {
/*  65: 96 */       return source.toDateMidnight();
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   private static class DateTimeToInstantConverter
/*  70:    */     implements Converter<DateTime, Instant>
/*  71:    */   {
/*  72:    */     public Instant convert(DateTime source)
/*  73:    */     {
/*  74:106 */       return source.toInstant();
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   private static class DateTimeToMutableDateTimeConverter
/*  79:    */     implements Converter<DateTime, MutableDateTime>
/*  80:    */   {
/*  81:    */     public MutableDateTime convert(DateTime source)
/*  82:    */     {
/*  83:116 */       return source.toMutableDateTime();
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   private static class DateTimeToDateConverter
/*  88:    */     implements Converter<DateTime, Date>
/*  89:    */   {
/*  90:    */     public Date convert(DateTime source)
/*  91:    */     {
/*  92:126 */       return source.toDate();
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   private static class DateTimeToCalendarConverter
/*  97:    */     implements Converter<DateTime, Calendar>
/*  98:    */   {
/*  99:    */     public Calendar convert(DateTime source)
/* 100:    */     {
/* 101:136 */       return source.toGregorianCalendar();
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   private static class DateTimeToLongConverter
/* 106:    */     implements Converter<DateTime, Long>
/* 107:    */   {
/* 108:    */     public Long convert(DateTime source)
/* 109:    */     {
/* 110:146 */       return Long.valueOf(source.getMillis());
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   private static class DateToLongConverter
/* 115:    */     implements Converter<Date, Long>
/* 116:    */   {
/* 117:    */     public Long convert(Date source)
/* 118:    */     {
/* 119:157 */       return Long.valueOf(source.getTime());
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   private static class CalendarToReadableInstantConverter
/* 124:    */     implements Converter<Calendar, ReadableInstant>
/* 125:    */   {
/* 126:    */     public ReadableInstant convert(Calendar source)
/* 127:    */     {
/* 128:168 */       return new DateTime(source);
/* 129:    */     }
/* 130:    */   }
/* 131:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.joda.JodaTimeConverters
 * JD-Core Version:    0.7.0.1
 */