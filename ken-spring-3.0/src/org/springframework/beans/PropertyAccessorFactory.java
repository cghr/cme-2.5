/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ public abstract class PropertyAccessorFactory
/*  4:   */ {
/*  5:   */   public static BeanWrapper forBeanPropertyAccess(Object target)
/*  6:   */   {
/*  7:37 */     return new BeanWrapperImpl(target);
/*  8:   */   }
/*  9:   */   
/* 10:   */   public static ConfigurablePropertyAccessor forDirectFieldAccess(Object target)
/* 11:   */   {
/* 12:48 */     return new DirectFieldAccessor(target);
/* 13:   */   }
/* 14:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyAccessorFactory
 * JD-Core Version:    0.7.0.1
 */