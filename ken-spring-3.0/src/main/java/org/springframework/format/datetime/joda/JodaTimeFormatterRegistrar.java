/*   1:    */ package org.springframework.format.datetime.joda;
/*   2:    */ 
/*   3:    */ import org.joda.time.DateTime;
/*   4:    */ import org.joda.time.LocalDate;
/*   5:    */ import org.joda.time.LocalDateTime;
/*   6:    */ import org.joda.time.LocalTime;
/*   7:    */ import org.joda.time.ReadableInstant;
/*   8:    */ import org.joda.time.format.DateTimeFormat;
/*   9:    */ import org.joda.time.format.DateTimeFormatter;
/*  10:    */ import org.joda.time.format.ISODateTimeFormat;
/*  11:    */ import org.springframework.format.FormatterRegistrar;
/*  12:    */ import org.springframework.format.FormatterRegistry;
/*  13:    */ import org.springframework.format.Parser;
/*  14:    */ import org.springframework.format.Printer;
/*  15:    */ 
/*  16:    */ public class JodaTimeFormatterRegistrar
/*  17:    */   implements FormatterRegistrar
/*  18:    */ {
/*  19:    */   private String dateStyle;
/*  20:    */   private String timeStyle;
/*  21:    */   private String dateTimeStyle;
/*  22:    */   private boolean useIsoFormat;
/*  23:    */   
/*  24:    */   public void setDateStyle(String dateStyle)
/*  25:    */   {
/*  26: 61 */     this.dateStyle = dateStyle;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setTimeStyle(String timeStyle)
/*  30:    */   {
/*  31: 69 */     this.timeStyle = timeStyle;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setDateTimeStyle(String dateTimeStyle)
/*  35:    */   {
/*  36: 78 */     this.dateTimeStyle = dateTimeStyle;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setUseIsoFormat(boolean useIsoFormat)
/*  40:    */   {
/*  41: 87 */     this.useIsoFormat = useIsoFormat;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void registerFormatters(FormatterRegistry registry)
/*  45:    */   {
/*  46: 91 */     JodaTimeConverters.registerConverters(registry);
/*  47:    */     
/*  48: 93 */     DateTimeFormatter jodaDateFormatter = getJodaDateFormatter();
/*  49: 94 */     registry.addFormatterForFieldType(LocalDate.class, new ReadablePartialPrinter(jodaDateFormatter), 
/*  50: 95 */       new DateTimeParser(jodaDateFormatter));
/*  51:    */     
/*  52: 97 */     DateTimeFormatter jodaTimeFormatter = getJodaTimeFormatter();
/*  53: 98 */     registry.addFormatterForFieldType(LocalTime.class, new ReadablePartialPrinter(jodaTimeFormatter), 
/*  54: 99 */       new DateTimeParser(jodaTimeFormatter));
/*  55:    */     
/*  56:101 */     DateTimeFormatter jodaDateTimeFormatter = getJodaDateTimeFormatter();
/*  57:102 */     Parser<DateTime> dateTimeParser = new DateTimeParser(jodaDateTimeFormatter);
/*  58:103 */     registry.addFormatterForFieldType(LocalDateTime.class, new ReadablePartialPrinter(jodaDateTimeFormatter), 
/*  59:104 */       dateTimeParser);
/*  60:    */     
/*  61:106 */     Printer<ReadableInstant> readableInstantPrinter = new ReadableInstantPrinter(jodaDateTimeFormatter);
/*  62:107 */     registry.addFormatterForFieldType(ReadableInstant.class, readableInstantPrinter, dateTimeParser);
/*  63:    */     
/*  64:109 */     registry.addFormatterForFieldAnnotation(new JodaDateTimeFormatAnnotationFormatterFactory());
/*  65:    */   }
/*  66:    */   
/*  67:    */   private DateTimeFormatter getJodaDateFormatter()
/*  68:    */   {
/*  69:115 */     if (this.useIsoFormat) {
/*  70:116 */       return ISODateTimeFormat.date();
/*  71:    */     }
/*  72:118 */     if (this.dateStyle != null) {
/*  73:119 */       return DateTimeFormat.forStyle(this.dateStyle + "-");
/*  74:    */     }
/*  75:121 */     return DateTimeFormat.shortDate();
/*  76:    */   }
/*  77:    */   
/*  78:    */   private DateTimeFormatter getJodaTimeFormatter()
/*  79:    */   {
/*  80:126 */     if (this.useIsoFormat) {
/*  81:127 */       return ISODateTimeFormat.time();
/*  82:    */     }
/*  83:129 */     if (this.timeStyle != null) {
/*  84:130 */       return DateTimeFormat.forStyle("-" + this.timeStyle);
/*  85:    */     }
/*  86:132 */     return DateTimeFormat.shortTime();
/*  87:    */   }
/*  88:    */   
/*  89:    */   private DateTimeFormatter getJodaDateTimeFormatter()
/*  90:    */   {
/*  91:137 */     if (this.useIsoFormat) {
/*  92:138 */       return ISODateTimeFormat.dateTime();
/*  93:    */     }
/*  94:140 */     if (this.dateTimeStyle != null) {
/*  95:141 */       return DateTimeFormat.forStyle(this.dateTimeStyle);
/*  96:    */     }
/*  97:143 */     return DateTimeFormat.shortDateTime();
/*  98:    */   }
/*  99:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar
 * JD-Core Version:    0.7.0.1
 */