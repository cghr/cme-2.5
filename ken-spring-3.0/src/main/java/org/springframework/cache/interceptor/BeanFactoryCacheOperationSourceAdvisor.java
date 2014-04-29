/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.ClassFilter;
/*  4:   */ import org.springframework.aop.Pointcut;
/*  5:   */ import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
/*  6:   */ 
/*  7:   */ public class BeanFactoryCacheOperationSourceAdvisor
/*  8:   */   extends AbstractBeanFactoryPointcutAdvisor
/*  9:   */ {
/* 10:   */   private CacheOperationSource cacheOperationSource;
/* 11:35 */   private final CacheOperationSourcePointcut pointcut = new CacheOperationSourcePointcut()
/* 12:   */   {
/* 13:   */     protected CacheOperationSource getCacheOperationSource()
/* 14:   */     {
/* 15:38 */       return BeanFactoryCacheOperationSourceAdvisor.this.cacheOperationSource;
/* 16:   */     }
/* 17:   */   };
/* 18:   */   
/* 19:   */   public void setCacheOperationSource(CacheOperationSource cacheOperationSource)
/* 20:   */   {
/* 21:50 */     this.cacheOperationSource = cacheOperationSource;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setClassFilter(ClassFilter classFilter)
/* 25:   */   {
/* 26:58 */     this.pointcut.setClassFilter(classFilter);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public Pointcut getPointcut()
/* 30:   */   {
/* 31:62 */     return this.pointcut;
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor
 * JD-Core Version:    0.7.0.1
 */