/*  1:   */ package org.springframework.beans.factory;
/*  2:   */ 
/*  3:   */ public class BeanCreationNotAllowedException
/*  4:   */   extends BeanCreationException
/*  5:   */ {
/*  6:   */   public BeanCreationNotAllowedException(String beanName, String msg)
/*  7:   */   {
/*  8:35 */     super(beanName, msg);
/*  9:   */   }
/* 10:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.BeanCreationNotAllowedException
 * JD-Core Version:    0.7.0.1
 */