/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.Pointcut;
/*  4:   */ import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
/*  5:   */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*  6:   */ 
/*  7:   */ public class CacheProxyFactoryBean
/*  8:   */   extends AbstractSingletonProxyFactoryBean
/*  9:   */ {
/* 10:44 */   private final CacheInterceptor cachingInterceptor = new CacheInterceptor();
/* 11:   */   private Pointcut pointcut;
/* 12:   */   
/* 13:   */   public void setPointcut(Pointcut pointcut)
/* 14:   */   {
/* 15:55 */     this.pointcut = pointcut;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected Object createMainInterceptor()
/* 19:   */   {
/* 20:60 */     this.cachingInterceptor.afterPropertiesSet();
/* 21:61 */     if (this.pointcut != null) {
/* 22:62 */       return new DefaultPointcutAdvisor(this.pointcut, this.cachingInterceptor);
/* 23:   */     }
/* 24:65 */     throw new UnsupportedOperationException();
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setCacheDefinitionSources(CacheOperationSource... cacheDefinitionSources)
/* 28:   */   {
/* 29:76 */     this.cachingInterceptor.setCacheOperationSources(cacheDefinitionSources);
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CacheProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */