/*  1:   */ package org.springframework.beans.factory.access.el;
/*  2:   */ 
/*  3:   */ import javax.el.ELContext;
/*  4:   */ import org.springframework.beans.factory.BeanFactory;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class SimpleSpringBeanELResolver
/*  8:   */   extends SpringBeanELResolver
/*  9:   */ {
/* 10:   */   private final BeanFactory beanFactory;
/* 11:   */   
/* 12:   */   public SimpleSpringBeanELResolver(BeanFactory beanFactory)
/* 13:   */   {
/* 14:41 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 15:42 */     this.beanFactory = beanFactory;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected BeanFactory getBeanFactory(ELContext elContext)
/* 19:   */   {
/* 20:47 */     return this.beanFactory;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.access.el.SimpleSpringBeanELResolver
 * JD-Core Version:    0.7.0.1
 */