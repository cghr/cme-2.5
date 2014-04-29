/*  1:   */ package org.springframework.cache.concurrent;
/*  2:   */ 
/*  3:   */ import java.util.Arrays;
/*  4:   */ import java.util.Collection;
/*  5:   */ import java.util.Collections;
/*  6:   */ import java.util.concurrent.ConcurrentHashMap;
/*  7:   */ import java.util.concurrent.ConcurrentMap;
/*  8:   */ import org.springframework.cache.Cache;
/*  9:   */ import org.springframework.cache.CacheManager;
/* 10:   */ 
/* 11:   */ public class ConcurrentMapCacheManager
/* 12:   */   implements CacheManager
/* 13:   */ {
/* 14:39 */   private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap();
/* 15:41 */   private boolean dynamic = true;
/* 16:   */   
/* 17:   */   public ConcurrentMapCacheManager() {}
/* 18:   */   
/* 19:   */   public ConcurrentMapCacheManager(String... cacheNames)
/* 20:   */   {
/* 21:56 */     setCacheNames((Collection)Arrays.asList(cacheNames));
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setCacheNames(Collection<String> cacheNames)
/* 25:   */   {
/* 26:66 */     if (cacheNames != null)
/* 27:   */     {
/* 28:67 */       for (String name : cacheNames) {
/* 29:68 */         this.cacheMap.put(name, createConcurrentMapCache(name));
/* 30:   */       }
/* 31:70 */       this.dynamic = false;
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Collection<String> getCacheNames()
/* 36:   */   {
/* 37:75 */     return (Collection)Collections.unmodifiableSet(this.cacheMap.keySet());
/* 38:   */   }
/* 39:   */   
/* 40:   */   public Cache getCache(String name)
/* 41:   */   {
/* 42:79 */     Cache cache = (Cache)this.cacheMap.get(name);
/* 43:80 */     if ((cache == null) && (this.dynamic)) {
/* 44:81 */       synchronized (this.cacheMap)
/* 45:   */       {
/* 46:82 */         cache = (Cache)this.cacheMap.get(name);
/* 47:83 */         if (cache == null)
/* 48:   */         {
/* 49:84 */           cache = createConcurrentMapCache(name);
/* 50:85 */           this.cacheMap.put(name, cache);
/* 51:   */         }
/* 52:   */       }
/* 53:   */     }
/* 54:89 */     return cache;
/* 55:   */   }
/* 56:   */   
/* 57:   */   protected Cache createConcurrentMapCache(String name)
/* 58:   */   {
/* 59:98 */     return new ConcurrentMapCache(name);
/* 60:   */   }
/* 61:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.concurrent.ConcurrentMapCacheManager
 * JD-Core Version:    0.7.0.1
 */