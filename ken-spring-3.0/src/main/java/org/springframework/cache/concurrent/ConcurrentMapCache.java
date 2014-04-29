/*   1:    */ package org.springframework.cache.concurrent;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.concurrent.ConcurrentHashMap;
/*   5:    */ import java.util.concurrent.ConcurrentMap;
/*   6:    */ import org.springframework.cache.Cache;
/*   7:    */ import org.springframework.cache.Cache.ValueWrapper;
/*   8:    */ import org.springframework.cache.support.ValueWrapperImpl;
/*   9:    */ 
/*  10:    */ public class ConcurrentMapCache
/*  11:    */   implements Cache
/*  12:    */ {
/*  13: 45 */   private static final Object NULL_HOLDER = new NullHolder(null);
/*  14:    */   private final String name;
/*  15:    */   private final ConcurrentMap<Object, Object> store;
/*  16:    */   private final boolean allowNullValues;
/*  17:    */   
/*  18:    */   public ConcurrentMapCache(String name)
/*  19:    */   {
/*  20: 59 */     this(name, new ConcurrentHashMap(), true);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public ConcurrentMapCache(String name, boolean allowNullValues)
/*  24:    */   {
/*  25: 67 */     this(name, new ConcurrentHashMap(), allowNullValues);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues)
/*  29:    */   {
/*  30: 79 */     this.name = name;
/*  31: 80 */     this.store = store;
/*  32: 81 */     this.allowNullValues = allowNullValues;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public String getName()
/*  36:    */   {
/*  37: 86 */     return this.name;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public ConcurrentMap getNativeCache()
/*  41:    */   {
/*  42: 90 */     return this.store;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public boolean isAllowNullValues()
/*  46:    */   {
/*  47: 94 */     return this.allowNullValues;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Cache.ValueWrapper get(Object key)
/*  51:    */   {
/*  52: 98 */     Object value = this.store.get(key);
/*  53: 99 */     return value != null ? new ValueWrapperImpl(fromStoreValue(value)) : null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void put(Object key, Object value)
/*  57:    */   {
/*  58:103 */     this.store.put(key, toStoreValue(value));
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void evict(Object key)
/*  62:    */   {
/*  63:107 */     this.store.remove(key);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void clear()
/*  67:    */   {
/*  68:111 */     this.store.clear();
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected Object fromStoreValue(Object storeValue)
/*  72:    */   {
/*  73:122 */     if ((this.allowNullValues) && (storeValue == NULL_HOLDER)) {
/*  74:123 */       return null;
/*  75:    */     }
/*  76:125 */     return storeValue;
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected Object toStoreValue(Object userValue)
/*  80:    */   {
/*  81:135 */     if ((this.allowNullValues) && (userValue == null)) {
/*  82:136 */       return NULL_HOLDER;
/*  83:    */     }
/*  84:138 */     return userValue;
/*  85:    */   }
/*  86:    */   
/*  87:    */   private static class NullHolder
/*  88:    */     implements Serializable
/*  89:    */   {}
/*  90:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.concurrent.ConcurrentMapCache
 * JD-Core Version:    0.7.0.1
 */