/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.util.Set;
/*  4:   */ import org.springframework.core.convert.converter.Converter;
/*  5:   */ import org.springframework.core.convert.converter.ConverterFactory;
/*  6:   */ import org.springframework.core.convert.converter.ConverterRegistry;
/*  7:   */ import org.springframework.core.convert.converter.GenericConverter;
/*  8:   */ 
/*  9:   */ public abstract class ConversionServiceFactory
/* 10:   */ {
/* 11:   */   public static void registerConverters(Set<?> converters, ConverterRegistry registry)
/* 12:   */   {
/* 13:44 */     if (converters != null) {
/* 14:45 */       for (Object converter : converters) {
/* 15:46 */         if ((converter instanceof GenericConverter)) {
/* 16:47 */           registry.addConverter((GenericConverter)converter);
/* 17:49 */         } else if ((converter instanceof Converter)) {
/* 18:50 */           registry.addConverter((Converter)converter);
/* 19:52 */         } else if ((converter instanceof ConverterFactory)) {
/* 20:53 */           registry.addConverterFactory((ConverterFactory)converter);
/* 21:   */         } else {
/* 22:56 */           throw new IllegalArgumentException("Each converter object must implement one of the Converter, ConverterFactory, or GenericConverter interfaces");
/* 23:   */         }
/* 24:   */       }
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   /**
/* 29:   */    * @deprecated
/* 30:   */    */
/* 31:   */   public static GenericConversionService createDefaultConversionService()
/* 32:   */   {
/* 33:68 */     return new DefaultConversionService();
/* 34:   */   }
/* 35:   */   
/* 36:   */   /**
/* 37:   */    * @deprecated
/* 38:   */    */
/* 39:   */   public static void addDefaultConverters(GenericConversionService conversionService)
/* 40:   */   {
/* 41:76 */     DefaultConversionService.addDefaultConverters(conversionService);
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.ConversionServiceFactory
 * JD-Core Version:    0.7.0.1
 */