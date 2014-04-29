/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ import java.util.Properties;
/*  5:   */ import org.springframework.core.convert.converter.Converter;
/*  6:   */ 
/*  7:   */ final class StringToPropertiesConverter
/*  8:   */   implements Converter<String, Properties>
/*  9:   */ {
/* 10:   */   public Properties convert(String source)
/* 11:   */   {
/* 12:   */     try
/* 13:   */     {
/* 14:35 */       Properties props = new Properties();
/* 15:   */       
/* 16:37 */       props.load(new ByteArrayInputStream(source.getBytes("ISO-8859-1")));
/* 17:38 */       return props;
/* 18:   */     }
/* 19:   */     catch (Exception ex)
/* 20:   */     {
/* 21:42 */       throw new IllegalArgumentException("Failed to parse [" + source + "] into Properties", ex);
/* 22:   */     }
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.StringToPropertiesConverter
 * JD-Core Version:    0.7.0.1
 */