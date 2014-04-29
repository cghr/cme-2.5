/*  1:   */ package org.springframework.beans.factory;
/*  2:   */ 
/*  3:   */ public class BeanCurrentlyInCreationException
/*  4:   */   extends BeanCreationException
/*  5:   */ {
/*  6:   */   public BeanCurrentlyInCreationException(String beanName)
/*  7:   */   {
/*  8:35 */     super(beanName, "Requested bean is currently in creation: Is there an unresolvable circular reference?");
/*  9:   */   }
/* 10:   */   
/* 11:   */   public BeanCurrentlyInCreationException(String beanName, String msg)
/* 12:   */   {
/* 13:44 */     super(beanName, msg);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.BeanCurrentlyInCreationException
 * JD-Core Version:    0.7.0.1
 */