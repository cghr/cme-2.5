/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.scope.ScopedProxyUtils;
/*  4:   */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  5:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  6:   */ 
/*  7:   */ class ScopedProxyCreator
/*  8:   */ {
/*  9:   */   public static BeanDefinitionHolder createScopedProxy(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry, boolean proxyTargetClass)
/* 10:   */   {
/* 11:36 */     return ScopedProxyUtils.createScopedProxy(definitionHolder, registry, proxyTargetClass);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public static String getTargetBeanName(String originalBeanName)
/* 15:   */   {
/* 16:40 */     return ScopedProxyUtils.getTargetBeanName(originalBeanName);
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ScopedProxyCreator
 * JD-Core Version:    0.7.0.1
 */