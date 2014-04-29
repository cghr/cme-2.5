/*  1:   */ package org.springframework.cache.support;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.LinkedHashSet;
/*  6:   */ import java.util.Set;
/*  7:   */ import java.util.concurrent.ConcurrentHashMap;
/*  8:   */ import java.util.concurrent.ConcurrentMap;
/*  9:   */ import org.springframework.beans.factory.InitializingBean;
/* 10:   */ import org.springframework.cache.Cache;
/* 11:   */ import org.springframework.cache.CacheManager;
/* 12:   */ import org.springframework.util.Assert;
/* 13:   */ 
/* 14:   */ public abstract class AbstractCacheManager
/* 15:   */   implements CacheManager, InitializingBean
/* 16:   */ {
/* 17:41 */   private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap();
/* 18:43 */   private Set<String> cacheNames = new LinkedHashSet();
/* 19:   */   
/* 20:   */   public void afterPropertiesSet()
/* 21:   */   {
/* 22:47 */     Collection<Cache> caches = loadCaches();
/* 23:48 */     Assert.notEmpty(caches, "loadCaches must not return an empty Collection");
/* 24:49 */     this.cacheMap.clear();
/* 25:52 */     for (Cache cache : caches)
/* 26:   */     {
/* 27:53 */       this.cacheMap.put(cache.getName(), cache);
/* 28:54 */       this.cacheNames.add(cache.getName());
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected final void addCache(Cache cache)
/* 33:   */   {
/* 34:59 */     this.cacheMap.put(cache.getName(), cache);
/* 35:60 */     this.cacheNames.add(cache.getName());
/* 36:   */   }
/* 37:   */   
/* 38:   */   public Cache getCache(String name)
/* 39:   */   {
/* 40:64 */     return (Cache)this.cacheMap.get(name);
/* 41:   */   }
/* 42:   */   
/* 43:   */   public Collection<String> getCacheNames()
/* 44:   */   {
/* 45:68 */     return (Collection)Collections.unmodifiableSet(this.cacheNames);
/* 46:   */   }
/* 47:   */   
/* 48:   */   protected abstract Collection<Cache> loadCaches();
/* 49:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.support.AbstractCacheManager
 * JD-Core Version:    0.7.0.1
 */