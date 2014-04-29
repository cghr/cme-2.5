/*  1:   */ package org.springframework.web.servlet.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*  4:   */ 
/*  5:   */ public class MvcNamespaceHandler
/*  6:   */   extends NamespaceHandlerSupport
/*  7:   */ {
/*  8:   */   public void init()
/*  9:   */   {
/* 10:32 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
/* 11:33 */     registerBeanDefinitionParser("default-servlet-handler", new DefaultServletHandlerBeanDefinitionParser());
/* 12:34 */     registerBeanDefinitionParser("interceptors", new InterceptorsBeanDefinitionParser());
/* 13:35 */     registerBeanDefinitionParser("resources", new ResourcesBeanDefinitionParser());
/* 14:36 */     registerBeanDefinitionParser("view-controller", new ViewControllerBeanDefinitionParser());
/* 15:   */   }
/* 16:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.MvcNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */