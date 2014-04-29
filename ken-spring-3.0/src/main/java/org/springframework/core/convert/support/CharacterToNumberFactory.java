/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.converter.Converter;
/*  4:   */ import org.springframework.core.convert.converter.ConverterFactory;
/*  5:   */ import org.springframework.util.NumberUtils;
/*  6:   */ 
/*  7:   */ final class CharacterToNumberFactory
/*  8:   */   implements ConverterFactory<Character, Number>
/*  9:   */ {
/* 10:   */   public <T extends Number> Converter<Character, T> getConverter(Class<T> targetType)
/* 11:   */   {
/* 12:44 */     return new CharacterToNumber(targetType);
/* 13:   */   }
/* 14:   */   
/* 15:   */   private static final class CharacterToNumber<T extends Number>
/* 16:   */     implements Converter<Character, T>
/* 17:   */   {
/* 18:   */     private final Class<T> targetType;
/* 19:   */     
/* 20:   */     public CharacterToNumber(Class<T> targetType)
/* 21:   */     {
/* 22:52 */       this.targetType = targetType;
/* 23:   */     }
/* 24:   */     
/* 25:   */     public T convert(Character source)
/* 26:   */     {
/* 27:56 */       return NumberUtils.convertNumberToTargetClass(Short.valueOf((short)source.charValue()), this.targetType);
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.CharacterToNumberFactory
 * JD-Core Version:    0.7.0.1
 */