/*   1:    */ package org.springframework.format.support;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Date;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.springframework.core.convert.support.DefaultConversionService;
/*   9:    */ import org.springframework.format.AnnotationFormatterFactory;
/*  10:    */ import org.springframework.format.FormatterRegistry;
/*  11:    */ import org.springframework.format.Parser;
/*  12:    */ import org.springframework.format.Printer;
/*  13:    */ import org.springframework.format.annotation.DateTimeFormat;
/*  14:    */ import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
/*  15:    */ import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
/*  16:    */ import org.springframework.util.ClassUtils;
/*  17:    */ import org.springframework.util.StringValueResolver;
/*  18:    */ 
/*  19:    */ public class DefaultFormattingConversionService
/*  20:    */   extends FormattingConversionService
/*  21:    */ {
/*  22: 50 */   private static final boolean jodaTimePresent = ClassUtils.isPresent(
/*  23: 51 */     "org.joda.time.LocalDate", DefaultFormattingConversionService.class.getClassLoader());
/*  24:    */   
/*  25:    */   public DefaultFormattingConversionService()
/*  26:    */   {
/*  27: 59 */     this(null, true);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public DefaultFormattingConversionService(boolean registerDefaultFormatters)
/*  31:    */   {
/*  32: 70 */     this(null, registerDefaultFormatters);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public DefaultFormattingConversionService(StringValueResolver embeddedValueResolver, boolean registerDefaultFormatters)
/*  36:    */   {
/*  37: 83 */     setEmbeddedValueResolver(embeddedValueResolver);
/*  38: 84 */     DefaultConversionService.addDefaultConverters(this);
/*  39: 85 */     if (registerDefaultFormatters) {
/*  40: 86 */       addDefaultFormatters(this);
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static void addDefaultFormatters(FormatterRegistry formatterRegistry)
/*  45:    */   {
/*  46: 96 */     formatterRegistry.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
/*  47: 97 */     if (jodaTimePresent) {
/*  48: 98 */       new JodaTimeFormatterRegistrar().registerFormatters(formatterRegistry);
/*  49:    */     } else {
/*  50:100 */       formatterRegistry.addFormatterForFieldAnnotation(new NoJodaDateTimeFormatAnnotationFormatterFactory());
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   private static final class NoJodaDateTimeFormatAnnotationFormatterFactory
/*  55:    */     implements AnnotationFormatterFactory<DateTimeFormat>
/*  56:    */   {
/*  57:    */     private final Set<Class<?>> fieldTypes;
/*  58:    */     
/*  59:    */     public NoJodaDateTimeFormatAnnotationFormatterFactory()
/*  60:    */     {
/*  61:115 */       Set<Class<?>> rawFieldTypes = new HashSet(4);
/*  62:116 */       rawFieldTypes.add(Date.class);
/*  63:117 */       rawFieldTypes.add(Calendar.class);
/*  64:118 */       rawFieldTypes.add(Long.class);
/*  65:119 */       this.fieldTypes = Collections.unmodifiableSet(rawFieldTypes);
/*  66:    */     }
/*  67:    */     
/*  68:    */     public Set<Class<?>> getFieldTypes()
/*  69:    */     {
/*  70:123 */       return this.fieldTypes;
/*  71:    */     }
/*  72:    */     
/*  73:    */     public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType)
/*  74:    */     {
/*  75:127 */       throw new IllegalStateException("JodaTime library not available - @DateTimeFormat not supported");
/*  76:    */     }
/*  77:    */     
/*  78:    */     public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType)
/*  79:    */     {
/*  80:131 */       throw new IllegalStateException("JodaTime library not available - @DateTimeFormat not supported");
/*  81:    */     }
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.support.DefaultFormattingConversionService
 * JD-Core Version:    0.7.0.1
 */