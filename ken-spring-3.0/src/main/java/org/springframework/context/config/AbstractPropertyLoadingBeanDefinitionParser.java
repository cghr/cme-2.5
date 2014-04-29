/*  1:   */ package org.springframework.context.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  4:   */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*  5:   */ import org.springframework.util.StringUtils;
/*  6:   */ import org.w3c.dom.Element;
/*  7:   */ 
/*  8:   */ abstract class AbstractPropertyLoadingBeanDefinitionParser
/*  9:   */   extends AbstractSingleBeanDefinitionParser
/* 10:   */ {
/* 11:   */   protected boolean shouldGenerateId()
/* 12:   */   {
/* 13:38 */     return true;
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected void doParse(Element element, BeanDefinitionBuilder builder)
/* 17:   */   {
/* 18:43 */     String location = element.getAttribute("location");
/* 19:44 */     if (StringUtils.hasLength(location))
/* 20:   */     {
/* 21:45 */       String[] locations = StringUtils.commaDelimitedListToStringArray(location);
/* 22:46 */       builder.addPropertyValue("locations", locations);
/* 23:   */     }
/* 24:49 */     String propertiesRef = element.getAttribute("properties-ref");
/* 25:50 */     if (StringUtils.hasLength(propertiesRef)) {
/* 26:51 */       builder.addPropertyReference("properties", propertiesRef);
/* 27:   */     }
/* 28:54 */     String fileEncoding = element.getAttribute("file-encoding");
/* 29:55 */     if (StringUtils.hasLength(fileEncoding)) {
/* 30:56 */       builder.addPropertyReference("fileEncoding", fileEncoding);
/* 31:   */     }
/* 32:59 */     String order = element.getAttribute("order");
/* 33:60 */     if (StringUtils.hasLength(order)) {
/* 34:61 */       builder.addPropertyValue("order", Integer.valueOf(order));
/* 35:   */     }
/* 36:64 */     builder.addPropertyValue("ignoreResourceNotFound", 
/* 37:65 */       Boolean.valueOf(element.getAttribute("ignore-resource-not-found")));
/* 38:   */     
/* 39:67 */     builder.addPropertyValue("localOverride", 
/* 40:68 */       Boolean.valueOf(element.getAttribute("local-override")));
/* 41:   */     
/* 42:70 */     builder.setRole(2);
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.config.AbstractPropertyLoadingBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */