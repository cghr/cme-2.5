/*  1:   */ package org.springframework.ejb.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  5:   */ import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
/*  6:   */ import org.springframework.util.StringUtils;
/*  7:   */ import org.springframework.util.xml.DomUtils;
/*  8:   */ import org.w3c.dom.Element;
/*  9:   */ 
/* 10:   */ abstract class AbstractJndiLocatingBeanDefinitionParser
/* 11:   */   extends AbstractSimpleBeanDefinitionParser
/* 12:   */ {
/* 13:   */   public static final String ENVIRONMENT = "environment";
/* 14:   */   public static final String ENVIRONMENT_REF = "environment-ref";
/* 15:   */   public static final String JNDI_ENVIRONMENT = "jndiEnvironment";
/* 16:   */   
/* 17:   */   protected boolean isEligibleAttribute(String attributeName)
/* 18:   */   {
/* 19:50 */     return (super.isEligibleAttribute(attributeName)) && (!"environment-ref".equals(attributeName)) && (!"lazy-init".equals(attributeName));
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected void postProcess(BeanDefinitionBuilder definitionBuilder, Element element)
/* 23:   */   {
/* 24:55 */     Object envValue = DomUtils.getChildElementValueByTagName(element, "environment");
/* 25:56 */     if (envValue != null)
/* 26:   */     {
/* 27:58 */       definitionBuilder.addPropertyValue("jndiEnvironment", envValue);
/* 28:   */     }
/* 29:   */     else
/* 30:   */     {
/* 31:62 */       String envRef = element.getAttribute("environment-ref");
/* 32:63 */       if (StringUtils.hasLength(envRef)) {
/* 33:64 */         definitionBuilder.addPropertyValue("jndiEnvironment", new RuntimeBeanReference(envRef));
/* 34:   */       }
/* 35:   */     }
/* 36:68 */     String lazyInit = element.getAttribute("lazy-init");
/* 37:69 */     if ((StringUtils.hasText(lazyInit)) && (!"default".equals(lazyInit))) {
/* 38:70 */       definitionBuilder.setLazyInit("true".equals(lazyInit));
/* 39:   */     }
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.config.AbstractJndiLocatingBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */