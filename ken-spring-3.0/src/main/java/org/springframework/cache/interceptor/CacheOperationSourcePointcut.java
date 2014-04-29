/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import org.springframework.aop.support.StaticMethodMatcherPointcut;
/*  6:   */ import org.springframework.util.ObjectUtils;
/*  7:   */ 
/*  8:   */ abstract class CacheOperationSourcePointcut
/*  9:   */   extends StaticMethodMatcherPointcut
/* 10:   */   implements Serializable
/* 11:   */ {
/* 12:   */   public boolean matches(Method method, Class<?> targetClass)
/* 13:   */   {
/* 14:36 */     CacheOperationSource cas = getCacheOperationSource();
/* 15:37 */     return (cas == null) || (cas.getCacheOperation(method, targetClass) != null);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean equals(Object other)
/* 19:   */   {
/* 20:42 */     if (this == other) {
/* 21:43 */       return true;
/* 22:   */     }
/* 23:45 */     if (!(other instanceof CacheOperationSourcePointcut)) {
/* 24:46 */       return false;
/* 25:   */     }
/* 26:48 */     CacheOperationSourcePointcut otherPc = (CacheOperationSourcePointcut)other;
/* 27:49 */     return ObjectUtils.nullSafeEquals(getCacheOperationSource(), 
/* 28:50 */       otherPc.getCacheOperationSource());
/* 29:   */   }
/* 30:   */   
/* 31:   */   public int hashCode()
/* 32:   */   {
/* 33:55 */     return CacheOperationSourcePointcut.class.hashCode();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String toString()
/* 37:   */   {
/* 38:60 */     return getClass().getName() + ": " + getCacheOperationSource();
/* 39:   */   }
/* 40:   */   
/* 41:   */   protected abstract CacheOperationSource getCacheOperationSource();
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CacheOperationSourcePointcut
 * JD-Core Version:    0.7.0.1
 */