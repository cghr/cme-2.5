/*  1:   */ package org.springframework.cache.support;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Collection;
/*  5:   */ import java.util.Collections;
/*  6:   */ import java.util.List;
/*  7:   */ import org.springframework.beans.factory.InitializingBean;
/*  8:   */ import org.springframework.cache.Cache;
/*  9:   */ import org.springframework.cache.CacheManager;
/* 10:   */ import org.springframework.util.Assert;
/* 11:   */ 
/* 12:   */ public class CompositeCacheManager
/* 13:   */   implements InitializingBean, CacheManager
/* 14:   */ {
/* 15:   */   private List<CacheManager> cacheManagers;
/* 16:44 */   private boolean fallbackToNoOpCache = false;
/* 17:   */   
/* 18:   */   public void setCacheManagers(Collection<CacheManager> cacheManagers)
/* 19:   */   {
/* 20:48 */     Assert.notEmpty(cacheManagers, "cacheManagers Collection must not be empty");
/* 21:49 */     this.cacheManagers = new ArrayList();
/* 22:50 */     this.cacheManagers.addAll(cacheManagers);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setFallbackToNoOpCache(boolean fallbackToNoOpCache)
/* 26:   */   {
/* 27:59 */     this.fallbackToNoOpCache = fallbackToNoOpCache;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void afterPropertiesSet()
/* 31:   */   {
/* 32:63 */     if (this.fallbackToNoOpCache) {
/* 33:64 */       this.cacheManagers.add(new NoOpCacheManager());
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   public Cache getCache(String name)
/* 38:   */   {
/* 39:70 */     for (CacheManager cacheManager : this.cacheManagers)
/* 40:   */     {
/* 41:71 */       Cache cache = cacheManager.getCache(name);
/* 42:72 */       if (cache != null) {
/* 43:73 */         return cache;
/* 44:   */       }
/* 45:   */     }
/* 46:76 */     return null;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public Collection<String> getCacheNames()
/* 50:   */   {
/* 51:80 */     List<String> names = new ArrayList();
/* 52:81 */     for (CacheManager manager : this.cacheManagers) {
/* 53:82 */       names.addAll(manager.getCacheNames());
/* 54:   */     }
/* 55:84 */     return (Collection)Collections.unmodifiableList(names);
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.support.CompositeCacheManager
 * JD-Core Version:    0.7.0.1
 */