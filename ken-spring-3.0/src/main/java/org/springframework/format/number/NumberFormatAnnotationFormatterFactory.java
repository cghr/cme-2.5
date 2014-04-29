/*  1:   */ package org.springframework.format.number;
/*  2:   */ 
/*  3:   */ import java.math.BigDecimal;
/*  4:   */ import java.math.BigInteger;
/*  5:   */ import java.util.Collections;
/*  6:   */ import java.util.HashSet;
/*  7:   */ import java.util.Set;
/*  8:   */ import org.springframework.context.EmbeddedValueResolverAware;
/*  9:   */ import org.springframework.format.AnnotationFormatterFactory;
/* 10:   */ import org.springframework.format.Formatter;
/* 11:   */ import org.springframework.format.Parser;
/* 12:   */ import org.springframework.format.Printer;
/* 13:   */ import org.springframework.format.annotation.NumberFormat;
/* 14:   */ import org.springframework.format.annotation.NumberFormat.Style;
/* 15:   */ import org.springframework.util.StringUtils;
/* 16:   */ import org.springframework.util.StringValueResolver;
/* 17:   */ 
/* 18:   */ public class NumberFormatAnnotationFormatterFactory
/* 19:   */   implements AnnotationFormatterFactory<NumberFormat>, EmbeddedValueResolverAware
/* 20:   */ {
/* 21:   */   private final Set<Class<?>> fieldTypes;
/* 22:   */   private StringValueResolver embeddedValueResolver;
/* 23:   */   
/* 24:   */   public NumberFormatAnnotationFormatterFactory()
/* 25:   */   {
/* 26:51 */     Set<Class<?>> rawFieldTypes = new HashSet(7);
/* 27:52 */     rawFieldTypes.add(Short.class);
/* 28:53 */     rawFieldTypes.add(Integer.class);
/* 29:54 */     rawFieldTypes.add(Long.class);
/* 30:55 */     rawFieldTypes.add(Float.class);
/* 31:56 */     rawFieldTypes.add(Double.class);
/* 32:57 */     rawFieldTypes.add(BigDecimal.class);
/* 33:58 */     rawFieldTypes.add(BigInteger.class);
/* 34:59 */     this.fieldTypes = Collections.unmodifiableSet(rawFieldTypes);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public final Set<Class<?>> getFieldTypes()
/* 38:   */   {
/* 39:63 */     return this.fieldTypes;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void setEmbeddedValueResolver(StringValueResolver resolver)
/* 43:   */   {
/* 44:68 */     this.embeddedValueResolver = resolver;
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected String resolveEmbeddedValue(String value)
/* 48:   */   {
/* 49:72 */     return this.embeddedValueResolver != null ? this.embeddedValueResolver.resolveStringValue(value) : value;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType)
/* 53:   */   {
/* 54:77 */     return configureFormatterFrom(annotation);
/* 55:   */   }
/* 56:   */   
/* 57:   */   public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType)
/* 58:   */   {
/* 59:81 */     return configureFormatterFrom(annotation);
/* 60:   */   }
/* 61:   */   
/* 62:   */   private Formatter<Number> configureFormatterFrom(NumberFormat annotation)
/* 63:   */   {
/* 64:86 */     if (StringUtils.hasLength(annotation.pattern())) {
/* 65:87 */       return new NumberFormatter(resolveEmbeddedValue(annotation.pattern()));
/* 66:   */     }
/* 67:90 */     NumberFormat.Style style = annotation.style();
/* 68:91 */     if (style == NumberFormat.Style.PERCENT) {
/* 69:92 */       return new PercentFormatter();
/* 70:   */     }
/* 71:94 */     if (style == NumberFormat.Style.CURRENCY) {
/* 72:95 */       return new CurrencyFormatter();
/* 73:   */     }
/* 74:98 */     return new NumberFormatter();
/* 75:   */   }
/* 76:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.number.NumberFormatAnnotationFormatterFactory
 * JD-Core Version:    0.7.0.1
 */