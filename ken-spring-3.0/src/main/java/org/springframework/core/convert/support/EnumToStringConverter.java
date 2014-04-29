/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.converter.Converter;
/*  4:   */ 
/*  5:   */ final class EnumToStringConverter
/*  6:   */   implements Converter<Enum<?>, String>
/*  7:   */ {
/*  8:   */   public String convert(Enum<?> source)
/*  9:   */   {
/* 10:29 */     return source.name();
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.EnumToStringConverter
 * JD-Core Version:    0.7.0.1
 */