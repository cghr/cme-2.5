/*  1:   */ package org.springframework.context.expression;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.BeansException;
/*  4:   */ import org.springframework.beans.factory.BeanFactory;
/*  5:   */ import org.springframework.expression.AccessException;
/*  6:   */ import org.springframework.expression.BeanResolver;
/*  7:   */ import org.springframework.expression.EvaluationContext;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ 
/* 10:   */ public class BeanFactoryResolver
/* 11:   */   implements BeanResolver
/* 12:   */ {
/* 13:   */   private final BeanFactory beanFactory;
/* 14:   */   
/* 15:   */   public BeanFactoryResolver(BeanFactory beanFactory)
/* 16:   */   {
/* 17:38 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 18:39 */     this.beanFactory = beanFactory;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Object resolve(EvaluationContext context, String beanName)
/* 22:   */     throws AccessException
/* 23:   */   {
/* 24:   */     try
/* 25:   */     {
/* 26:44 */       return this.beanFactory.getBean(beanName);
/* 27:   */     }
/* 28:   */     catch (BeansException ex)
/* 29:   */     {
/* 30:47 */       throw new AccessException("Could not resolve bean reference against BeanFactory", ex);
/* 31:   */     }
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.expression.BeanFactoryResolver
 * JD-Core Version:    0.7.0.1
 */