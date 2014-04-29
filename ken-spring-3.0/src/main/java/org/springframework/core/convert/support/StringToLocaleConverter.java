/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.springframework.core.convert.converter.Converter;
/*  5:   */ import org.springframework.util.StringUtils;
/*  6:   */ 
/*  7:   */ final class StringToLocaleConverter
/*  8:   */   implements Converter<String, Locale>
/*  9:   */ {
/* 10:   */   public Locale convert(String source)
/* 11:   */   {
/* 12:33 */     return StringUtils.parseLocaleString(source);
/* 13:   */   }
/* 14:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.StringToLocaleConverter
 * JD-Core Version:    0.7.0.1
 */