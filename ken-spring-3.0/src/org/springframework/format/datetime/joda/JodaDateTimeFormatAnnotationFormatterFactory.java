/*   1:    */ package org.springframework.format.datetime.joda;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Date;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.joda.time.DateTime;
/*   9:    */ import org.joda.time.LocalDate;
/*  10:    */ import org.joda.time.LocalDateTime;
/*  11:    */ import org.joda.time.LocalTime;
/*  12:    */ import org.joda.time.ReadableInstant;
/*  13:    */ import org.joda.time.ReadablePartial;
/*  14:    */ import org.joda.time.format.DateTimeFormatter;
/*  15:    */ import org.joda.time.format.ISODateTimeFormat;
/*  16:    */ import org.springframework.context.EmbeddedValueResolverAware;
/*  17:    */ import org.springframework.format.AnnotationFormatterFactory;
/*  18:    */ import org.springframework.format.Parser;
/*  19:    */ import org.springframework.format.Printer;
/*  20:    */ import org.springframework.format.annotation.DateTimeFormat.ISO;
/*  21:    */ import org.springframework.util.StringUtils;
/*  22:    */ import org.springframework.util.StringValueResolver;
/*  23:    */ 
/*  24:    */ public class JodaDateTimeFormatAnnotationFormatterFactory
/*  25:    */   implements AnnotationFormatterFactory<org.springframework.format.annotation.DateTimeFormat>, EmbeddedValueResolverAware
/*  26:    */ {
/*  27:    */   private final Set<Class<?>> fieldTypes;
/*  28:    */   private StringValueResolver embeddedValueResolver;
/*  29:    */   
/*  30:    */   public JodaDateTimeFormatAnnotationFormatterFactory()
/*  31:    */   {
/*  32: 58 */     this.fieldTypes = createFieldTypes();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public final Set<Class<?>> getFieldTypes()
/*  36:    */   {
/*  37: 62 */     return this.fieldTypes;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setEmbeddedValueResolver(StringValueResolver resolver)
/*  41:    */   {
/*  42: 67 */     this.embeddedValueResolver = resolver;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected String resolveEmbeddedValue(String value)
/*  46:    */   {
/*  47: 71 */     return this.embeddedValueResolver != null ? this.embeddedValueResolver.resolveStringValue(value) : value;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Printer<?> getPrinter(org.springframework.format.annotation.DateTimeFormat annotation, Class<?> fieldType)
/*  51:    */   {
/*  52: 76 */     DateTimeFormatter formatter = configureDateTimeFormatterFrom(annotation);
/*  53: 77 */     if (ReadableInstant.class.isAssignableFrom(fieldType)) {
/*  54: 78 */       return new ReadableInstantPrinter(formatter);
/*  55:    */     }
/*  56: 80 */     if (ReadablePartial.class.isAssignableFrom(fieldType)) {
/*  57: 81 */       return new ReadablePartialPrinter(formatter);
/*  58:    */     }
/*  59: 83 */     if (Calendar.class.isAssignableFrom(fieldType)) {
/*  60: 85 */       return new ReadableInstantPrinter(formatter);
/*  61:    */     }
/*  62: 89 */     return new MillisecondInstantPrinter(formatter);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Parser<DateTime> getParser(org.springframework.format.annotation.DateTimeFormat annotation, Class<?> fieldType)
/*  66:    */   {
/*  67: 94 */     return new DateTimeParser(configureDateTimeFormatterFrom(annotation));
/*  68:    */   }
/*  69:    */   
/*  70:    */   private Set<Class<?>> createFieldTypes()
/*  71:    */   {
/*  72:106 */     Set<Class<?>> rawFieldTypes = new HashSet(7);
/*  73:107 */     rawFieldTypes.add(ReadableInstant.class);
/*  74:108 */     rawFieldTypes.add(LocalDate.class);
/*  75:109 */     rawFieldTypes.add(LocalTime.class);
/*  76:110 */     rawFieldTypes.add(LocalDateTime.class);
/*  77:111 */     rawFieldTypes.add(Date.class);
/*  78:112 */     rawFieldTypes.add(Calendar.class);
/*  79:113 */     rawFieldTypes.add(Long.class);
/*  80:114 */     return Collections.unmodifiableSet(rawFieldTypes);
/*  81:    */   }
/*  82:    */   
/*  83:    */   private DateTimeFormatter configureDateTimeFormatterFrom(org.springframework.format.annotation.DateTimeFormat annotation)
/*  84:    */   {
/*  85:118 */     if (StringUtils.hasLength(annotation.pattern())) {
/*  86:119 */       return forPattern(resolveEmbeddedValue(annotation.pattern()));
/*  87:    */     }
/*  88:121 */     if (annotation.iso() != DateTimeFormat.ISO.NONE) {
/*  89:122 */       return forIso(annotation.iso());
/*  90:    */     }
/*  91:125 */     return forStyle(resolveEmbeddedValue(annotation.style()));
/*  92:    */   }
/*  93:    */   
/*  94:    */   private DateTimeFormatter forPattern(String pattern)
/*  95:    */   {
/*  96:130 */     return org.joda.time.format.DateTimeFormat.forPattern(pattern);
/*  97:    */   }
/*  98:    */   
/*  99:    */   private DateTimeFormatter forStyle(String style)
/* 100:    */   {
/* 101:134 */     return org.joda.time.format.DateTimeFormat.forStyle(style);
/* 102:    */   }
/* 103:    */   
/* 104:    */   private DateTimeFormatter forIso(DateTimeFormat.ISO iso)
/* 105:    */   {
/* 106:138 */     if (iso == DateTimeFormat.ISO.DATE) {
/* 107:139 */       return ISODateTimeFormat.date();
/* 108:    */     }
/* 109:141 */     if (iso == DateTimeFormat.ISO.TIME) {
/* 110:142 */       return ISODateTimeFormat.time();
/* 111:    */     }
/* 112:145 */     return ISODateTimeFormat.dateTime();
/* 113:    */   }
/* 114:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.joda.JodaDateTimeFormatAnnotationFormatterFactory
 * JD-Core Version:    0.7.0.1
 */