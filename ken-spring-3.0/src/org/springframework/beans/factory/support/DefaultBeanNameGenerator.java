/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  4:   */ 
/*  5:   */ public class DefaultBeanNameGenerator
/*  6:   */   implements BeanNameGenerator
/*  7:   */ {
/*  8:   */   public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry)
/*  9:   */   {
/* 10:31 */     return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.DefaultBeanNameGenerator
 * JD-Core Version:    0.7.0.1
 */