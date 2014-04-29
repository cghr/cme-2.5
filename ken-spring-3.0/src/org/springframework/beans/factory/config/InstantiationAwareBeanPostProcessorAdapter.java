/*  1:   */ package org.springframework.beans.factory.config;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyDescriptor;
/*  4:   */ import java.lang.reflect.Constructor;
/*  5:   */ import org.springframework.beans.BeansException;
/*  6:   */ import org.springframework.beans.PropertyValues;
/*  7:   */ 
/*  8:   */ public abstract class InstantiationAwareBeanPostProcessorAdapter
/*  9:   */   implements SmartInstantiationAwareBeanPostProcessor
/* 10:   */ {
/* 11:   */   public Class<?> predictBeanType(Class<?> beanClass, String beanName)
/* 12:   */   {
/* 13:43 */     return null;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName)
/* 17:   */     throws BeansException
/* 18:   */   {
/* 19:47 */     return null;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Object getEarlyBeanReference(Object bean, String beanName)
/* 23:   */     throws BeansException
/* 24:   */   {
/* 25:51 */     return bean;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName)
/* 29:   */     throws BeansException
/* 30:   */   {
/* 31:55 */     return null;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public boolean postProcessAfterInstantiation(Object bean, String beanName)
/* 35:   */     throws BeansException
/* 36:   */   {
/* 37:59 */     return true;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
/* 41:   */     throws BeansException
/* 42:   */   {
/* 43:66 */     return pvs;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/* 47:   */     throws BeansException
/* 48:   */   {
/* 49:70 */     return bean;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public Object postProcessAfterInitialization(Object bean, String beanName)
/* 53:   */     throws BeansException
/* 54:   */   {
/* 55:74 */     return bean;
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
 * JD-Core Version:    0.7.0.1
 */