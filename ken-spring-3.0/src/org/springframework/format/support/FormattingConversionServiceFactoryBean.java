/*   1:    */ package org.springframework.format.support;
/*   2:    */ 
/*   3:    */ import java.util.Set;
/*   4:    */ import org.springframework.beans.factory.FactoryBean;
/*   5:    */ import org.springframework.beans.factory.InitializingBean;
/*   6:    */ import org.springframework.context.EmbeddedValueResolverAware;
/*   7:    */ import org.springframework.core.convert.support.ConversionServiceFactory;
/*   8:    */ import org.springframework.format.AnnotationFormatterFactory;
/*   9:    */ import org.springframework.format.Formatter;
/*  10:    */ import org.springframework.format.FormatterRegistrar;
/*  11:    */ import org.springframework.format.FormatterRegistry;
/*  12:    */ import org.springframework.util.StringValueResolver;
/*  13:    */ 
/*  14:    */ public class FormattingConversionServiceFactoryBean
/*  15:    */   implements FactoryBean<FormattingConversionService>, EmbeddedValueResolverAware, InitializingBean
/*  16:    */ {
/*  17:    */   private Set<?> converters;
/*  18:    */   private Set<?> formatters;
/*  19:    */   private Set<FormatterRegistrar> formatterRegistrars;
/*  20: 72 */   private boolean registerDefaultFormatters = true;
/*  21:    */   private StringValueResolver embeddedValueResolver;
/*  22:    */   private FormattingConversionService conversionService;
/*  23:    */   
/*  24:    */   public void setConverters(Set<?> converters)
/*  25:    */   {
/*  26: 87 */     this.converters = converters;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setFormatters(Set<?> formatters)
/*  30:    */   {
/*  31: 95 */     this.formatters = formatters;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setFormatterRegistrars(Set<FormatterRegistrar> formatterRegistrars)
/*  35:    */   {
/*  36:113 */     this.formatterRegistrars = formatterRegistrars;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setRegisterDefaultFormatters(boolean registerDefaultFormatters)
/*  40:    */   {
/*  41:124 */     this.registerDefaultFormatters = registerDefaultFormatters;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setEmbeddedValueResolver(StringValueResolver embeddedValueResolver)
/*  45:    */   {
/*  46:128 */     this.embeddedValueResolver = embeddedValueResolver;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void afterPropertiesSet()
/*  50:    */   {
/*  51:133 */     this.conversionService = new DefaultFormattingConversionService(this.embeddedValueResolver, this.registerDefaultFormatters);
/*  52:134 */     ConversionServiceFactory.registerConverters(this.converters, this.conversionService);
/*  53:135 */     registerFormatters();
/*  54:    */   }
/*  55:    */   
/*  56:    */   private void registerFormatters()
/*  57:    */   {
/*  58:139 */     if (this.formatters != null) {
/*  59:140 */       for (Object formatter : this.formatters) {
/*  60:141 */         if ((formatter instanceof Formatter)) {
/*  61:142 */           this.conversionService.addFormatter((Formatter)formatter);
/*  62:144 */         } else if ((formatter instanceof AnnotationFormatterFactory)) {
/*  63:145 */           this.conversionService.addFormatterForFieldAnnotation((AnnotationFormatterFactory)formatter);
/*  64:    */         } else {
/*  65:148 */           throw new IllegalArgumentException(
/*  66:149 */             "Custom formatters must be implementations of Formatter or AnnotationFormatterFactory");
/*  67:    */         }
/*  68:    */       }
/*  69:    */     }
/*  70:153 */     if (this.formatterRegistrars != null) {
/*  71:154 */       for (FormatterRegistrar registrar : this.formatterRegistrars) {
/*  72:155 */         registrar.registerFormatters(this.conversionService);
/*  73:    */       }
/*  74:    */     }
/*  75:158 */     installFormatters(this.conversionService);
/*  76:    */   }
/*  77:    */   
/*  78:    */   @Deprecated
/*  79:    */   protected void installFormatters(FormatterRegistry registry) {}
/*  80:    */   
/*  81:    */   public FormattingConversionService getObject()
/*  82:    */   {
/*  83:175 */     return this.conversionService;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public Class<? extends FormattingConversionService> getObjectType()
/*  87:    */   {
/*  88:179 */     return FormattingConversionService.class;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean isSingleton()
/*  92:    */   {
/*  93:183 */     return true;
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.support.FormattingConversionServiceFactoryBean
 * JD-Core Version:    0.7.0.1
 */