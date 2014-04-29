/*  1:   */ package org.springframework.cache.ehcache;
/*  2:   */ 
/*  3:   */ import net.sf.ehcache.Ehcache;
/*  4:   */ import net.sf.ehcache.Element;
/*  5:   */ import net.sf.ehcache.Status;
/*  6:   */ import org.springframework.cache.Cache;
/*  7:   */ import org.springframework.cache.Cache.ValueWrapper;
/*  8:   */ import org.springframework.cache.support.ValueWrapperImpl;
/*  9:   */ import org.springframework.util.Assert;
/* 10:   */ 
/* 11:   */ public class EhCacheCache
/* 12:   */   implements Cache
/* 13:   */ {
/* 14:   */   private final Ehcache cache;
/* 15:   */   
/* 16:   */   public EhCacheCache(Ehcache ehcache)
/* 17:   */   {
/* 18:44 */     Assert.notNull(ehcache, "Ehcache must not be null");
/* 19:45 */     Status status = ehcache.getStatus();
/* 20:46 */     Assert.isTrue(Status.STATUS_ALIVE.equals(status), 
/* 21:47 */       "An 'alive' Ehcache is required - current cache is " + status.toString());
/* 22:48 */     this.cache = ehcache;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getName()
/* 26:   */   {
/* 27:53 */     return this.cache.getName();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Ehcache getNativeCache()
/* 31:   */   {
/* 32:57 */     return this.cache;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void clear()
/* 36:   */   {
/* 37:61 */     this.cache.removeAll();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public Cache.ValueWrapper get(Object key)
/* 41:   */   {
/* 42:65 */     Element element = this.cache.get(key);
/* 43:66 */     return element != null ? new ValueWrapperImpl(element.getObjectValue()) : null;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void put(Object key, Object value)
/* 47:   */   {
/* 48:70 */     this.cache.put(new Element(key, value));
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void evict(Object key)
/* 52:   */   {
/* 53:74 */     this.cache.remove(key);
/* 54:   */   }
/* 55:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.ehcache.EhCacheCache
 * JD-Core Version:    0.7.0.1
 */