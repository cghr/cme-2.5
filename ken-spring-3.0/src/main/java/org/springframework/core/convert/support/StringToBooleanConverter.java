/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.util.HashSet;
/*  4:   */ import java.util.Set;
/*  5:   */ import org.springframework.core.convert.converter.Converter;
/*  6:   */ 
/*  7:   */ final class StringToBooleanConverter
/*  8:   */   implements Converter<String, Boolean>
/*  9:   */ {
/* 10:33 */   private static final Set<String> trueValues = new HashSet(4);
/* 11:35 */   private static final Set<String> falseValues = new HashSet(4);
/* 12:   */   
/* 13:   */   static
/* 14:   */   {
/* 15:38 */     trueValues.add("true");
/* 16:39 */     trueValues.add("on");
/* 17:40 */     trueValues.add("yes");
/* 18:41 */     trueValues.add("1");
/* 19:   */     
/* 20:43 */     falseValues.add("false");
/* 21:44 */     falseValues.add("off");
/* 22:45 */     falseValues.add("no");
/* 23:46 */     falseValues.add("0");
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Boolean convert(String source)
/* 27:   */   {
/* 28:50 */     String value = source.trim();
/* 29:51 */     if ("".equals(value)) {
/* 30:52 */       return null;
/* 31:   */     }
/* 32:54 */     value = value.toLowerCase();
/* 33:55 */     if (trueValues.contains(value)) {
/* 34:56 */       return Boolean.TRUE;
/* 35:   */     }
/* 36:58 */     if (falseValues.contains(value)) {
/* 37:59 */       return Boolean.FALSE;
/* 38:   */     }
/* 39:62 */     throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.StringToBooleanConverter
 * JD-Core Version:    0.7.0.1
 */