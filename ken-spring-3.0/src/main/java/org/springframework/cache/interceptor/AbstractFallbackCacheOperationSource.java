/*   1:    */ package org.springframework.cache.interceptor;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.lang.reflect.Modifier;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.concurrent.ConcurrentHashMap;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.core.BridgeMethodResolver;
/*  10:    */ import org.springframework.util.ClassUtils;
/*  11:    */ import org.springframework.util.ObjectUtils;
/*  12:    */ 
/*  13:    */ public abstract class AbstractFallbackCacheOperationSource
/*  14:    */   implements CacheOperationSource
/*  15:    */ {
/*  16: 54 */   private static final CacheOperation NULL_CACHING_ATTRIBUTE = new CacheUpdateOperation();
/*  17: 61 */   protected final Log logger = LogFactory.getLog(getClass());
/*  18: 68 */   final Map<Object, CacheOperation> attributeCache = new ConcurrentHashMap();
/*  19:    */   
/*  20:    */   public CacheOperation getCacheOperation(Method method, Class<?> targetClass)
/*  21:    */   {
/*  22: 81 */     Object cacheKey = getCacheKey(method, targetClass);
/*  23: 82 */     CacheOperation cached = (CacheOperation)this.attributeCache.get(cacheKey);
/*  24: 83 */     if (cached != null)
/*  25:    */     {
/*  26: 84 */       if (cached == NULL_CACHING_ATTRIBUTE) {
/*  27: 85 */         return null;
/*  28:    */       }
/*  29: 89 */       return cached;
/*  30:    */     }
/*  31: 93 */     CacheOperation cacheDef = computeCacheOperationDefinition(method, targetClass);
/*  32: 95 */     if (cacheDef == null)
/*  33:    */     {
/*  34: 96 */       this.attributeCache.put(cacheKey, NULL_CACHING_ATTRIBUTE);
/*  35:    */     }
/*  36:    */     else
/*  37:    */     {
/*  38: 99 */       if (this.logger.isDebugEnabled()) {
/*  39:100 */         this.logger.debug("Adding cacheable method '" + method.getName() + "' with attribute: " + cacheDef);
/*  40:    */       }
/*  41:102 */       this.attributeCache.put(cacheKey, cacheDef);
/*  42:    */     }
/*  43:104 */     return cacheDef;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected Object getCacheKey(Method method, Class<?> targetClass)
/*  47:    */   {
/*  48:117 */     return new DefaultCacheKey(method, targetClass);
/*  49:    */   }
/*  50:    */   
/*  51:    */   private CacheOperation computeCacheOperationDefinition(Method method, Class<?> targetClass)
/*  52:    */   {
/*  53:122 */     if ((allowPublicMethodsOnly()) && (!Modifier.isPublic(method.getModifiers()))) {
/*  54:123 */       return null;
/*  55:    */     }
/*  56:128 */     Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/*  57:    */     
/*  58:130 */     specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/*  59:    */     
/*  60:    */ 
/*  61:133 */     CacheOperation opDef = findCacheOperation(specificMethod);
/*  62:134 */     if (opDef != null) {
/*  63:135 */       return opDef;
/*  64:    */     }
/*  65:139 */     opDef = findCacheOperation(specificMethod.getDeclaringClass());
/*  66:140 */     if (opDef != null) {
/*  67:141 */       return opDef;
/*  68:    */     }
/*  69:144 */     if (specificMethod != method)
/*  70:    */     {
/*  71:146 */       opDef = findCacheOperation(method);
/*  72:147 */       if (opDef != null) {
/*  73:148 */         return opDef;
/*  74:    */       }
/*  75:151 */       return findCacheOperation(method.getDeclaringClass());
/*  76:    */     }
/*  77:153 */     return null;
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected abstract CacheOperation findCacheOperation(Method paramMethod);
/*  81:    */   
/*  82:    */   protected abstract CacheOperation findCacheOperation(Class<?> paramClass);
/*  83:    */   
/*  84:    */   protected boolean allowPublicMethodsOnly()
/*  85:    */   {
/*  86:180 */     return false;
/*  87:    */   }
/*  88:    */   
/*  89:    */   private static class DefaultCacheKey
/*  90:    */   {
/*  91:    */     private final Method method;
/*  92:    */     private final Class<?> targetClass;
/*  93:    */     
/*  94:    */     public DefaultCacheKey(Method method, Class<?> targetClass)
/*  95:    */     {
/*  96:194 */       this.method = method;
/*  97:195 */       this.targetClass = targetClass;
/*  98:    */     }
/*  99:    */     
/* 100:    */     public boolean equals(Object other)
/* 101:    */     {
/* 102:200 */       if (this == other) {
/* 103:201 */         return true;
/* 104:    */       }
/* 105:203 */       if (!(other instanceof DefaultCacheKey)) {
/* 106:204 */         return false;
/* 107:    */       }
/* 108:206 */       DefaultCacheKey otherKey = (DefaultCacheKey)other;
/* 109:    */       
/* 110:208 */       return (this.method.equals(otherKey.method)) && (ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass));
/* 111:    */     }
/* 112:    */     
/* 113:    */     public int hashCode()
/* 114:    */     {
/* 115:213 */       return this.method.hashCode() * 29 + (this.targetClass != null ? this.targetClass.hashCode() : 0);
/* 116:    */     }
/* 117:    */   }
/* 118:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.AbstractFallbackCacheOperationSource
 * JD-Core Version:    0.7.0.1
 */