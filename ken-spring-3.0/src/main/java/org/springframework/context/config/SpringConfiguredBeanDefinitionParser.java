/*  1:   */ package org.springframework.context.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  4:   */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*  5:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  6:   */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  7:   */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*  8:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  9:   */ import org.w3c.dom.Element;
/* 10:   */ 
/* 11:   */ class SpringConfiguredBeanDefinitionParser
/* 12:   */   implements BeanDefinitionParser
/* 13:   */ {
/* 14:   */   public static final String BEAN_CONFIGURER_ASPECT_BEAN_NAME = "org.springframework.context.config.internalBeanConfigurerAspect";
/* 15:   */   static final String BEAN_CONFIGURER_ASPECT_CLASS_NAME = "org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect";
/* 16:   */   
/* 17:   */   public BeanDefinition parse(Element element, ParserContext parserContext)
/* 18:   */   {
/* 19:47 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.context.config.internalBeanConfigurerAspect"))
/* 20:   */     {
/* 21:48 */       RootBeanDefinition def = new RootBeanDefinition();
/* 22:49 */       def.setBeanClassName("org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect");
/* 23:50 */       def.setFactoryMethodName("aspectOf");
/* 24:   */       
/* 25:   */ 
/* 26:53 */       def.setRole(2);
/* 27:54 */       def.setSource(parserContext.extractSource(element));
/* 28:55 */       parserContext.registerBeanComponent(new BeanComponentDefinition(def, "org.springframework.context.config.internalBeanConfigurerAspect"));
/* 29:   */     }
/* 30:57 */     return null;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.config.SpringConfiguredBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */