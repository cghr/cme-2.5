/*  1:   */ package org.springframework.beans.factory.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.wiring.BeanWiringInfo;
/*  4:   */ import org.springframework.beans.factory.wiring.BeanWiringInfoResolver;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ import org.springframework.util.ClassUtils;
/*  7:   */ 
/*  8:   */ public class AnnotationBeanWiringInfoResolver
/*  9:   */   implements BeanWiringInfoResolver
/* 10:   */ {
/* 11:   */   public BeanWiringInfo resolveWiringInfo(Object beanInstance)
/* 12:   */   {
/* 13:40 */     Assert.notNull(beanInstance, "Bean instance must not be null");
/* 14:41 */     Configurable annotation = (Configurable)beanInstance.getClass().getAnnotation(Configurable.class);
/* 15:42 */     return annotation != null ? buildWiringInfo(beanInstance, annotation) : null;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected BeanWiringInfo buildWiringInfo(Object beanInstance, Configurable annotation)
/* 19:   */   {
/* 20:52 */     if (!Autowire.NO.equals(annotation.autowire())) {
/* 21:53 */       return new BeanWiringInfo(annotation.autowire().value(), annotation.dependencyCheck());
/* 22:   */     }
/* 23:56 */     if (!"".equals(annotation.value())) {
/* 24:58 */       return new BeanWiringInfo(annotation.value(), false);
/* 25:   */     }
/* 26:62 */     return new BeanWiringInfo(getDefaultBeanName(beanInstance), true);
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected String getDefaultBeanName(Object beanInstance)
/* 30:   */   {
/* 31:76 */     return ClassUtils.getUserClass(beanInstance).getName();
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.AnnotationBeanWiringInfoResolver
 * JD-Core Version:    0.7.0.1
 */