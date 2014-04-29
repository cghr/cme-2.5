/*  1:   */ package org.springframework.beans.factory;
/*  2:   */ 
/*  3:   */ public class BeanIsNotAFactoryException
/*  4:   */   extends BeanNotOfRequiredTypeException
/*  5:   */ {
/*  6:   */   public BeanIsNotAFactoryException(String name, Class actualType)
/*  7:   */   {
/*  8:37 */     super(name, FactoryBean.class, actualType);
/*  9:   */   }
/* 10:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.BeanIsNotAFactoryException
 * JD-Core Version:    0.7.0.1
 */