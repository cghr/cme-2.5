/*  1:   */ package org.springframework.beans.factory.wiring;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ import org.springframework.util.ClassUtils;
/*  5:   */ 
/*  6:   */ public class ClassNameBeanWiringInfoResolver
/*  7:   */   implements BeanWiringInfoResolver
/*  8:   */ {
/*  9:   */   public BeanWiringInfo resolveWiringInfo(Object beanInstance)
/* 10:   */   {
/* 11:35 */     Assert.notNull(beanInstance, "Bean instance must not be null");
/* 12:36 */     return new BeanWiringInfo(ClassUtils.getUserClass(beanInstance).getName(), true);
/* 13:   */   }
/* 14:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.wiring.ClassNameBeanWiringInfoResolver
 * JD-Core Version:    0.7.0.1
 */