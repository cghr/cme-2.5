/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.converter.Converter;
/*  4:   */ import org.springframework.core.convert.converter.ConverterFactory;
/*  5:   */ import org.springframework.util.NumberUtils;
/*  6:   */ 
/*  7:   */ final class NumberToNumberConverterFactory
/*  8:   */   implements ConverterFactory<Number, Number>
/*  9:   */ {
/* 10:   */   public <T extends Number> Converter<Number, T> getConverter(Class<T> targetType)
/* 11:   */   {
/* 12:44 */     return new NumberToNumber(targetType);
/* 13:   */   }
/* 14:   */   
/* 15:   */   private static final class NumberToNumber<T extends Number>
/* 16:   */     implements Converter<Number, T>
/* 17:   */   {
/* 18:   */     private final Class<T> targetType;
/* 19:   */     
/* 20:   */     public NumberToNumber(Class<T> targetType)
/* 21:   */     {
/* 22:52 */       this.targetType = targetType;
/* 23:   */     }
/* 24:   */     
/* 25:   */     public T convert(Number source)
/* 26:   */     {
/* 27:56 */       return NumberUtils.convertNumberToTargetClass(source, this.targetType);
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.NumberToNumberConverterFactory
 * JD-Core Version:    0.7.0.1
 */