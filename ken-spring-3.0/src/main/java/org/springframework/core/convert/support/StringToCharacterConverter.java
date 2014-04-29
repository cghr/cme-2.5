/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.converter.Converter;
/*  4:   */ 
/*  5:   */ final class StringToCharacterConverter
/*  6:   */   implements Converter<String, Character>
/*  7:   */ {
/*  8:   */   public Character convert(String source)
/*  9:   */   {
/* 10:30 */     if (source.length() == 0) {
/* 11:31 */       return null;
/* 12:   */     }
/* 13:33 */     if (source.length() > 1) {
/* 14:34 */       throw new IllegalArgumentException(
/* 15:35 */         "Can only convert a [String] with length of 1 to a [Character]; string value '" + source + "'  has length of " + source.length());
/* 16:   */     }
/* 17:37 */     return Character.valueOf(source.charAt(0));
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.StringToCharacterConverter
 * JD-Core Version:    0.7.0.1
 */