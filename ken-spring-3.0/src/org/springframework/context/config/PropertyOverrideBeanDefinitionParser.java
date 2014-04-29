/*  1:   */ package org.springframework.context.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  5:   */ import org.w3c.dom.Element;
/*  6:   */ 
/*  7:   */ class PropertyOverrideBeanDefinitionParser
/*  8:   */   extends AbstractPropertyLoadingBeanDefinitionParser
/*  9:   */ {
/* 10:   */   protected Class getBeanClass(Element element)
/* 11:   */   {
/* 12:35 */     return PropertyOverrideConfigurer.class;
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected void doParse(Element element, BeanDefinitionBuilder builder)
/* 16:   */   {
/* 17:41 */     super.doParse(element, builder);
/* 18:42 */     builder.addPropertyValue("ignoreInvalidKeys", 
/* 19:43 */       Boolean.valueOf(element.getAttribute("ignore-unresolvable")));
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.config.PropertyOverrideBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */