/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.converter.Converter;
/*  4:   */ import org.springframework.core.convert.converter.ConverterFactory;
/*  5:   */ 
/*  6:   */ final class StringToEnumConverterFactory
/*  7:   */   implements ConverterFactory<String, Enum>
/*  8:   */ {
/*  9:   */   public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType)
/* 10:   */   {
/* 11:32 */     return new StringToEnum(targetType);
/* 12:   */   }
/* 13:   */   
/* 14:   */   private class StringToEnum<T extends Enum>
/* 15:   */     implements Converter<String, T>
/* 16:   */   {
/* 17:   */     private final Class<T> enumType;
/* 18:   */     
/* 19:   */     public StringToEnum()
/* 20:   */     {
/* 21:40 */       this.enumType = enumType;
/* 22:   */     }
/* 23:   */     
/* 24:   */     public T convert(String source)
/* 25:   */     {
/* 26:44 */       if (source.length() == 0) {
/* 27:46 */         return null;
/* 28:   */       }
/* 29:48 */       return Enum.valueOf(this.enumType, source.trim());
/* 30:   */     }
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.StringToEnumConverterFactory
 * JD-Core Version:    0.7.0.1
 */