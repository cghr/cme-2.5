/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  4:   */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  5:   */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*  6:   */ 
/*  7:   */ public class SimpleAutowireCandidateResolver
/*  8:   */   implements AutowireCandidateResolver
/*  9:   */ {
/* 10:   */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor)
/* 11:   */   {
/* 12:41 */     return bdHolder.getBeanDefinition().isAutowireCandidate();
/* 13:   */   }
/* 14:   */   
/* 15:   */   public Object getSuggestedValue(DependencyDescriptor descriptor)
/* 16:   */   {
/* 17:45 */     return null;
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.SimpleAutowireCandidateResolver
 * JD-Core Version:    0.7.0.1
 */