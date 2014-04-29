/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import java.util.Collection;
/*  5:   */ import org.springframework.cache.Cache;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ class CacheExpressionRootObject
/*  9:   */ {
/* 10:   */   private final Collection<Cache> caches;
/* 11:   */   private final Method method;
/* 12:   */   private final Object[] args;
/* 13:   */   private final Object target;
/* 14:   */   private final Class<?> targetClass;
/* 15:   */   
/* 16:   */   public CacheExpressionRootObject(Collection<Cache> caches, Method method, Object[] args, Object target, Class<?> targetClass)
/* 17:   */   {
/* 18:47 */     Assert.notNull(method, "Method is required");
/* 19:48 */     Assert.notNull(targetClass, "targetClass is required");
/* 20:49 */     this.method = method;
/* 21:50 */     this.target = target;
/* 22:51 */     this.targetClass = targetClass;
/* 23:52 */     this.args = args;
/* 24:53 */     this.caches = caches;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Collection<Cache> getCaches()
/* 28:   */   {
/* 29:58 */     return this.caches;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Method getMethod()
/* 33:   */   {
/* 34:62 */     return this.method;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getMethodName()
/* 38:   */   {
/* 39:66 */     return this.method.getName();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public Object[] getArgs()
/* 43:   */   {
/* 44:70 */     return this.args;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public Object getTarget()
/* 48:   */   {
/* 49:74 */     return this.target;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public Class<?> getTargetClass()
/* 53:   */   {
/* 54:78 */     return this.targetClass;
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CacheExpressionRootObject
 * JD-Core Version:    0.7.0.1
 */