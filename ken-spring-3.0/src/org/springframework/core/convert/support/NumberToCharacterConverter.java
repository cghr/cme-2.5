/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.converter.Converter;
/*  4:   */ 
/*  5:   */ final class NumberToCharacterConverter
/*  6:   */   implements Converter<Number, Character>
/*  7:   */ {
/*  8:   */   public Character convert(Number source)
/*  9:   */   {
/* 10:38 */     return Character.valueOf((char)source.shortValue());
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.NumberToCharacterConverter
 * JD-Core Version:    0.7.0.1
 */