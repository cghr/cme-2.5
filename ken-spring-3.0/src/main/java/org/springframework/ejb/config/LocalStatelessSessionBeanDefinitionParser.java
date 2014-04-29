/*  1:   */ package org.springframework.ejb.config;
/*  2:   */ 
/*  3:   */ import org.w3c.dom.Element;
/*  4:   */ 
/*  5:   */ class LocalStatelessSessionBeanDefinitionParser
/*  6:   */   extends AbstractJndiLocatingBeanDefinitionParser
/*  7:   */ {
/*  8:   */   protected String getBeanClassName(Element element)
/*  9:   */   {
/* 10:36 */     return "org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean";
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.config.LocalStatelessSessionBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */