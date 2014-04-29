/*  1:   */ package org.springframework.cache.support;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.LinkedHashSet;
/*  6:   */ import java.util.Set;
/*  7:   */ import java.util.concurrent.ConcurrentHashMap;
/*  8:   */ import java.util.concurrent.ConcurrentMap;
/*  9:   */ import org.springframework.cache.Cache;
/* 10:   */ import org.springframework.cache.Cache.ValueWrapper;
/* 11:   */ import org.springframework.cache.CacheManager;
/* 12:   */ 
/* 13:   */ public class NoOpCacheManager
/* 14:   */   implements CacheManager
/* 15:   */ {
/* 16:41 */   private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap();
/* 17:42 */   private Set<String> names = new LinkedHashSet();
/* 18:   */   
/* 19:   */   private static class NoOpCache
/* 20:   */     implements Cache
/* 21:   */   {
/* 22:   */     private final String name;
/* 23:   */     
/* 24:   */     public NoOpCache(String name)
/* 25:   */     {
/* 26:49 */       this.name = name;
/* 27:   */     }
/* 28:   */     
/* 29:   */     public void clear() {}
/* 30:   */     
/* 31:   */     public void evict(Object key) {}
/* 32:   */     
/* 33:   */     public Cache.ValueWrapper get(Object key)
/* 34:   */     {
/* 35:59 */       return null;
/* 36:   */     }
/* 37:   */     
/* 38:   */     public String getName()
/* 39:   */     {
/* 40:63 */       return this.name;
/* 41:   */     }
/* 42:   */     
/* 43:   */     public Object getNativeCache()
/* 44:   */     {
/* 45:67 */       return null;
/* 46:   */     }
/* 47:   */     
/* 48:   */     public void put(Object key, Object value) {}
/* 49:   */   }
/* 50:   */   
/* 51:   */   public Cache getCache(String name)
/* 52:   */   {
/* 53:81 */     Cache cache = (Cache)this.caches.get(name);
/* 54:82 */     if (cache == null)
/* 55:   */     {
/* 56:83 */       this.caches.putIfAbsent(name, new NoOpCache(name));
/* 57:84 */       synchronized (this.names)
/* 58:   */       {
/* 59:85 */         this.names.add(name);
/* 60:   */       }
/* 61:   */     }
/* 62:89 */     return (Cache)this.caches.get(name);
/* 63:   */   }
/* 64:   */   
/* 65:   */   public Collection<String> getCacheNames()
/* 66:   */   {
/* 67:98 */     return (Collection)Collections.unmodifiableSet(this.names);
/* 68:   */   }
/* 69:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.support.NoOpCacheManager
 * JD-Core Version:    0.7.0.1
 */