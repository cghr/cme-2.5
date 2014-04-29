/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.converter.Converter;
/*  4:   */ import org.springframework.core.convert.converter.ConverterFactory;
/*  5:   */ import org.springframework.util.NumberUtils;
/*  6:   */ 
/*  7:   */ final class StringToNumberConverterFactory
/*  8:   */   implements ConverterFactory<String, Number>
/*  9:   */ {
/* 10:   */   public <T extends Number> Converter<String, T> getConverter(Class<T> targetType)
/* 11:   */   {
/* 12:44 */     return new StringToNumber(targetType);
/* 13:   */   }
/* 14:   */   
/* 15:   */   private static final class StringToNumber<T extends Number>
/* 16:   */     implements Converter<String, T>
/* 17:   */   {
/* 18:   */     private final Class<T> targetType;
/* 19:   */     
/* 20:   */     public StringToNumber(Class<T> targetType)
/* 21:   */     {
/* 22:52 */       this.targetType = targetType;
/* 23:   */     }
/* 24:   */     
/* 25:   */     public T convert(String source)
/* 26:   */     {
/* 27:56 */       if (source.length() == 0) {
/* 28:57 */         return null;
/* 29:   */       }
/* 30:59 */       return NumberUtils.parseNumber(source, this.targetType);
/* 31:   */     }
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.StringToNumberConverterFactory
 * JD-Core Version:    0.7.0.1
 */