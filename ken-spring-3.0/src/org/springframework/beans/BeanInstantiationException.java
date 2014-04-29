/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ public class BeanInstantiationException
/*  4:   */   extends FatalBeanException
/*  5:   */ {
/*  6:   */   private Class beanClass;
/*  7:   */   
/*  8:   */   public BeanInstantiationException(Class beanClass, String msg)
/*  9:   */   {
/* 10:37 */     this(beanClass, msg, null);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public BeanInstantiationException(Class beanClass, String msg, Throwable cause)
/* 14:   */   {
/* 15:47 */     super("Could not instantiate bean class [" + beanClass.getName() + "]: " + msg, cause);
/* 16:48 */     this.beanClass = beanClass;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public Class getBeanClass()
/* 20:   */   {
/* 21:55 */     return this.beanClass;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.BeanInstantiationException
 * JD-Core Version:    0.7.0.1
 */