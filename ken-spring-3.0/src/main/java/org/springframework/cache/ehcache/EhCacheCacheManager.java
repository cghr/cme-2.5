/*  1:   */ package org.springframework.cache.ehcache;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.LinkedHashSet;
/*  5:   */ import net.sf.ehcache.CacheManager;
/*  6:   */ import net.sf.ehcache.Ehcache;
/*  7:   */ import net.sf.ehcache.Status;
/*  8:   */ import org.springframework.cache.Cache;
/*  9:   */ import org.springframework.cache.support.AbstractCacheManager;
/* 10:   */ import org.springframework.util.Assert;
/* 11:   */ 
/* 12:   */ public class EhCacheCacheManager
/* 13:   */   extends AbstractCacheManager
/* 14:   */ {
/* 15:   */   private CacheManager cacheManager;
/* 16:   */   
/* 17:   */   public void setCacheManager(CacheManager cacheManager)
/* 18:   */   {
/* 19:45 */     this.cacheManager = cacheManager;
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected Collection<Cache> loadCaches()
/* 23:   */   {
/* 24:51 */     Assert.notNull(this.cacheManager, "A backing EhCache CacheManager is required");
/* 25:52 */     Status status = this.cacheManager.getStatus();
/* 26:53 */     Assert.isTrue(Status.STATUS_ALIVE.equals(status), 
/* 27:54 */       "An 'alive' EhCache CacheManager is required - current cache is " + status.toString());
/* 28:   */     
/* 29:56 */     String[] names = this.cacheManager.getCacheNames();
/* 30:57 */     Collection<Cache> caches = new LinkedHashSet(names.length);
/* 31:58 */     for (String name : names) {
/* 32:59 */       caches.add(new EhCacheCache(this.cacheManager.getEhcache(name)));
/* 33:   */     }
/* 34:61 */     return caches;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public Cache getCache(String name)
/* 38:   */   {
/* 39:66 */     Cache cache = super.getCache(name);
/* 40:67 */     if (cache == null)
/* 41:   */     {
/* 42:70 */       Ehcache ehcache = this.cacheManager.getEhcache(name);
/* 43:71 */       if (ehcache != null)
/* 44:   */       {
/* 45:72 */         cache = new EhCacheCache(ehcache);
/* 46:73 */         addCache(cache);
/* 47:   */       }
/* 48:   */     }
/* 49:76 */     return cache;
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.ehcache.EhCacheCacheManager
 * JD-Core Version:    0.7.0.1
 */