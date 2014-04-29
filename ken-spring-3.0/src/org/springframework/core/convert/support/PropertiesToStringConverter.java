/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayOutputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.util.Properties;
/*  6:   */ import org.springframework.core.convert.converter.Converter;
/*  7:   */ 
/*  8:   */ final class PropertiesToStringConverter
/*  9:   */   implements Converter<Properties, String>
/* 10:   */ {
/* 11:   */   public String convert(Properties source)
/* 12:   */   {
/* 13:   */     try
/* 14:   */     {
/* 15:36 */       ByteArrayOutputStream os = new ByteArrayOutputStream();
/* 16:37 */       source.store(os, null);
/* 17:38 */       return os.toString("ISO-8859-1");
/* 18:   */     }
/* 19:   */     catch (IOException ex)
/* 20:   */     {
/* 21:42 */       throw new IllegalArgumentException("Failed to store [" + source + "] into String", ex);
/* 22:   */     }
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.PropertiesToStringConverter
 * JD-Core Version:    0.7.0.1
 */