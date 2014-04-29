/*  1:   */ package org.springframework.context.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*  4:   */ import org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser;
/*  5:   */ import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
/*  6:   */ 
/*  7:   */ public class ContextNamespaceHandler
/*  8:   */   extends NamespaceHandlerSupport
/*  9:   */ {
/* 10:   */   public void init()
/* 11:   */   {
/* 12:34 */     registerBeanDefinitionParser("property-placeholder", new PropertyPlaceholderBeanDefinitionParser());
/* 13:35 */     registerBeanDefinitionParser("property-override", new PropertyOverrideBeanDefinitionParser());
/* 14:36 */     registerBeanDefinitionParser("annotation-config", new AnnotationConfigBeanDefinitionParser());
/* 15:37 */     registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
/* 16:38 */     registerBeanDefinitionParser("load-time-weaver", new LoadTimeWeaverBeanDefinitionParser());
/* 17:39 */     registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
/* 18:40 */     registerBeanDefinitionParser("mbean-export", new MBeanExportBeanDefinitionParser());
/* 19:41 */     registerBeanDefinitionParser("mbean-server", new MBeanServerBeanDefinitionParser());
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.config.ContextNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */