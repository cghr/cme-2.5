/*  1:   */ package org.springframework.cache.concurrent;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.ConcurrentMap;
/*  4:   */ import org.springframework.beans.factory.BeanNameAware;
/*  5:   */ import org.springframework.beans.factory.FactoryBean;
/*  6:   */ import org.springframework.beans.factory.InitializingBean;
/*  7:   */ import org.springframework.util.StringUtils;
/*  8:   */ 
/*  9:   */ public class ConcurrentMapCacheFactoryBean
/* 10:   */   implements FactoryBean<ConcurrentMapCache>, BeanNameAware, InitializingBean
/* 11:   */ {
/* 12:42 */   private String name = "";
/* 13:   */   private ConcurrentMap<Object, Object> store;
/* 14:46 */   private boolean allowNullValues = true;
/* 15:   */   private ConcurrentMapCache cache;
/* 16:   */   
/* 17:   */   public void setName(String name)
/* 18:   */   {
/* 19:56 */     this.name = name;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setStore(ConcurrentMap<Object, Object> store)
/* 23:   */   {
/* 24:65 */     this.store = store;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setAllowNullValues(boolean allowNullValues)
/* 28:   */   {
/* 29:74 */     this.allowNullValues = allowNullValues;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setBeanName(String beanName)
/* 33:   */   {
/* 34:78 */     if (!StringUtils.hasLength(this.name)) {
/* 35:79 */       setName(beanName);
/* 36:   */     }
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void afterPropertiesSet()
/* 40:   */   {
/* 41:84 */     this.cache = (this.store != null ? new ConcurrentMapCache(this.name, this.store, this.allowNullValues) : 
/* 42:85 */       new ConcurrentMapCache(this.name, this.allowNullValues));
/* 43:   */   }
/* 44:   */   
/* 45:   */   public ConcurrentMapCache getObject()
/* 46:   */   {
/* 47:90 */     return this.cache;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public Class<?> getObjectType()
/* 51:   */   {
/* 52:94 */     return ConcurrentMapCache.class;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public boolean isSingleton()
/* 56:   */   {
/* 57:98 */     return true;
/* 58:   */   }
/* 59:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean
 * JD-Core Version:    0.7.0.1
 */