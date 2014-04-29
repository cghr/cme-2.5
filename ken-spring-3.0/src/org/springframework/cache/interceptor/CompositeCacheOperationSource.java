/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class CompositeCacheOperationSource
/*  8:   */   implements CacheOperationSource, Serializable
/*  9:   */ {
/* 10:   */   private final CacheOperationSource[] cacheOperationSources;
/* 11:   */   
/* 12:   */   public CompositeCacheOperationSource(CacheOperationSource... cacheOperationSources)
/* 13:   */   {
/* 14:42 */     Assert.notEmpty(cacheOperationSources, "cacheOperationSources array must not be empty");
/* 15:43 */     this.cacheOperationSources = cacheOperationSources;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public final CacheOperationSource[] getCacheOperationSources()
/* 19:   */   {
/* 20:51 */     return this.cacheOperationSources;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public CacheOperation getCacheOperation(Method method, Class<?> targetClass)
/* 24:   */   {
/* 25:56 */     for (CacheOperationSource source : this.cacheOperationSources)
/* 26:   */     {
/* 27:57 */       CacheOperation definition = source.getCacheOperation(method, targetClass);
/* 28:58 */       if (definition != null) {
/* 29:59 */         return definition;
/* 30:   */       }
/* 31:   */     }
/* 32:62 */     return null;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CompositeCacheOperationSource
 * JD-Core Version:    0.7.0.1
 */