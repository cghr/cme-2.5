/*  1:   */ package org.springframework.expression.spel.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.ConversionException;
/*  4:   */ import org.springframework.core.convert.ConversionService;
/*  5:   */ import org.springframework.core.convert.ConverterNotFoundException;
/*  6:   */ import org.springframework.core.convert.TypeDescriptor;
/*  7:   */ import org.springframework.core.convert.support.DefaultConversionService;
/*  8:   */ import org.springframework.expression.TypeConverter;
/*  9:   */ import org.springframework.expression.spel.SpelEvaluationException;
/* 10:   */ import org.springframework.expression.spel.SpelMessage;
/* 11:   */ import org.springframework.util.Assert;
/* 12:   */ 
/* 13:   */ public class StandardTypeConverter
/* 14:   */   implements TypeConverter
/* 15:   */ {
/* 16:   */   private static ConversionService defaultConversionService;
/* 17:   */   private final ConversionService conversionService;
/* 18:   */   
/* 19:   */   public StandardTypeConverter()
/* 20:   */   {
/* 21:46 */     synchronized (this)
/* 22:   */     {
/* 23:47 */       if (defaultConversionService == null) {
/* 24:48 */         defaultConversionService = new DefaultConversionService();
/* 25:   */       }
/* 26:   */     }
/* 27:51 */     this.conversionService = defaultConversionService;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public StandardTypeConverter(ConversionService conversionService)
/* 31:   */   {
/* 32:55 */     Assert.notNull(conversionService, "ConversionService must not be null");
/* 33:56 */     this.conversionService = conversionService;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 37:   */   {
/* 38:61 */     return this.conversionService.canConvert(sourceType, targetType);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public Object convertValue(Object value, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 42:   */   {
/* 43:   */     try
/* 44:   */     {
/* 45:66 */       return this.conversionService.convert(value, sourceType, targetType);
/* 46:   */     }
/* 47:   */     catch (ConverterNotFoundException cenfe)
/* 48:   */     {
/* 49:69 */       throw new SpelEvaluationException(cenfe, SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { sourceType.toString(), targetType.toString() });
/* 50:   */     }
/* 51:   */     catch (ConversionException ce)
/* 52:   */     {
/* 53:72 */       throw new SpelEvaluationException(ce, SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { sourceType.toString(), targetType.toString() });
/* 54:   */     }
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.StandardTypeConverter
 * JD-Core Version:    0.7.0.1
 */