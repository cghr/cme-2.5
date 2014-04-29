/*  1:   */ package org.springframework.cache.support;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import org.springframework.cache.Cache;
/*  5:   */ 
/*  6:   */ public class SimpleCacheManager
/*  7:   */   extends AbstractCacheManager
/*  8:   */ {
/*  9:   */   private Collection<Cache> caches;
/* 10:   */   
/* 11:   */   public void setCaches(Collection<Cache> caches)
/* 12:   */   {
/* 13:38 */     this.caches = caches;
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected Collection<Cache> loadCaches()
/* 17:   */   {
/* 18:43 */     return this.caches;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.support.SimpleCacheManager
 * JD-Core Version:    0.7.0.1
 */