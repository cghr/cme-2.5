/*  1:   */ package org.springframework.context.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  5:   */ import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
/*  6:   */ import org.springframework.util.StringUtils;
/*  7:   */ import org.w3c.dom.Element;
/*  8:   */ 
/*  9:   */ class PropertyPlaceholderBeanDefinitionParser
/* 10:   */   extends AbstractPropertyLoadingBeanDefinitionParser
/* 11:   */ {
/* 12:   */   protected Class<?> getBeanClass(Element element)
/* 13:   */   {
/* 14:38 */     if (element.hasAttribute("system-properties-mode")) {
/* 15:39 */       return PropertyPlaceholderConfigurer.class;
/* 16:   */     }
/* 17:42 */     return PropertySourcesPlaceholderConfigurer.class;
/* 18:   */   }
/* 19:   */   
/* 20:   */   protected void doParse(Element element, BeanDefinitionBuilder builder)
/* 21:   */   {
/* 22:47 */     super.doParse(element, builder);
/* 23:   */     
/* 24:49 */     builder.addPropertyValue("ignoreUnresolvablePlaceholders", 
/* 25:50 */       Boolean.valueOf(element.getAttribute("ignore-unresolvable")));
/* 26:   */     
/* 27:52 */     String systemPropertiesModeName = element.getAttribute("system-properties-mode");
/* 28:53 */     if (StringUtils.hasLength(systemPropertiesModeName)) {
/* 29:54 */       builder.addPropertyValue("systemPropertiesModeName", "SYSTEM_PROPERTIES_MODE_" + systemPropertiesModeName);
/* 30:   */     }
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.config.PropertyPlaceholderBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */