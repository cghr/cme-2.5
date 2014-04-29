/*  1:   */ package org.springframework.http.converter.xml;
/*  2:   */ 
/*  3:   */ import org.springframework.http.converter.FormHttpMessageConverter;
/*  4:   */ 
/*  5:   */ public class XmlAwareFormHttpMessageConverter
/*  6:   */   extends FormHttpMessageConverter
/*  7:   */ {
/*  8:   */   public XmlAwareFormHttpMessageConverter()
/*  9:   */   {
/* 10:32 */     addPartConverter(new SourceHttpMessageConverter());
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */