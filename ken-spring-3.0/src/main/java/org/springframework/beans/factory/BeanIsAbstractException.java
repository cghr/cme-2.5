/*  1:   */ package org.springframework.beans.factory;
/*  2:   */ 
/*  3:   */ public class BeanIsAbstractException
/*  4:   */   extends BeanCreationException
/*  5:   */ {
/*  6:   */   public BeanIsAbstractException(String beanName)
/*  7:   */   {
/*  8:34 */     super(beanName, "Bean definition is abstract");
/*  9:   */   }
/* 10:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.BeanIsAbstractException
 * JD-Core Version:    0.7.0.1
 */