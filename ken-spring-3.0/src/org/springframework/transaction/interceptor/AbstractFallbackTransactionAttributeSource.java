/*   1:    */ package org.springframework.transaction.interceptor;
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
/*  13:    */ public abstract class AbstractFallbackTransactionAttributeSource
/*  14:    */   implements TransactionAttributeSource
/*  15:    */ {
/*  16: 57 */   private static final TransactionAttribute NULL_TRANSACTION_ATTRIBUTE = new DefaultTransactionAttribute();
/*  17: 65 */   protected final Log logger = LogFactory.getLog(getClass());
/*  18: 72 */   final Map<Object, TransactionAttribute> attributeCache = new ConcurrentHashMap();
/*  19:    */   
/*  20:    */   public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass)
/*  21:    */   {
/*  22: 85 */     Object cacheKey = getCacheKey(method, targetClass);
/*  23: 86 */     Object cached = this.attributeCache.get(cacheKey);
/*  24: 87 */     if (cached != null)
/*  25:    */     {
/*  26: 90 */       if (cached == NULL_TRANSACTION_ATTRIBUTE) {
/*  27: 91 */         return null;
/*  28:    */       }
/*  29: 94 */       return (TransactionAttribute)cached;
/*  30:    */     }
/*  31: 99 */     TransactionAttribute txAtt = computeTransactionAttribute(method, targetClass);
/*  32:101 */     if (txAtt == null)
/*  33:    */     {
/*  34:102 */       this.attributeCache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
/*  35:    */     }
/*  36:    */     else
/*  37:    */     {
/*  38:105 */       if (this.logger.isDebugEnabled()) {
/*  39:106 */         this.logger.debug("Adding transactional method '" + method.getName() + "' with attribute: " + txAtt);
/*  40:    */       }
/*  41:108 */       this.attributeCache.put(cacheKey, txAtt);
/*  42:    */     }
/*  43:110 */     return txAtt;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected Object getCacheKey(Method method, Class<?> targetClass)
/*  47:    */   {
/*  48:123 */     return new DefaultCacheKey(method, targetClass);
/*  49:    */   }
/*  50:    */   
/*  51:    */   private TransactionAttribute computeTransactionAttribute(Method method, Class<?> targetClass)
/*  52:    */   {
/*  53:133 */     if ((allowPublicMethodsOnly()) && (!Modifier.isPublic(method.getModifiers()))) {
/*  54:134 */       return null;
/*  55:    */     }
/*  56:138 */     Class<?> userClass = ClassUtils.getUserClass(targetClass);
/*  57:    */     
/*  58:    */ 
/*  59:141 */     Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
/*  60:    */     
/*  61:143 */     specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/*  62:    */     
/*  63:    */ 
/*  64:146 */     TransactionAttribute txAtt = findTransactionAttribute(specificMethod);
/*  65:147 */     if (txAtt != null) {
/*  66:148 */       return txAtt;
/*  67:    */     }
/*  68:152 */     txAtt = findTransactionAttribute(specificMethod.getDeclaringClass());
/*  69:153 */     if (txAtt != null) {
/*  70:154 */       return txAtt;
/*  71:    */     }
/*  72:157 */     if (specificMethod != method)
/*  73:    */     {
/*  74:159 */       txAtt = findTransactionAttribute(method);
/*  75:160 */       if (txAtt != null) {
/*  76:161 */         return txAtt;
/*  77:    */       }
/*  78:164 */       return findTransactionAttribute(method.getDeclaringClass());
/*  79:    */     }
/*  80:166 */     return null;
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected abstract TransactionAttribute findTransactionAttribute(Method paramMethod);
/*  84:    */   
/*  85:    */   protected abstract TransactionAttribute findTransactionAttribute(Class<?> paramClass);
/*  86:    */   
/*  87:    */   protected boolean allowPublicMethodsOnly()
/*  88:    */   {
/*  89:194 */     return false;
/*  90:    */   }
/*  91:    */   
/*  92:    */   private static class DefaultCacheKey
/*  93:    */   {
/*  94:    */     private final Method method;
/*  95:    */     private final Class targetClass;
/*  96:    */     
/*  97:    */     public DefaultCacheKey(Method method, Class targetClass)
/*  98:    */     {
/*  99:208 */       this.method = method;
/* 100:209 */       this.targetClass = targetClass;
/* 101:    */     }
/* 102:    */     
/* 103:    */     public boolean equals(Object other)
/* 104:    */     {
/* 105:214 */       if (this == other) {
/* 106:215 */         return true;
/* 107:    */       }
/* 108:217 */       if (!(other instanceof DefaultCacheKey)) {
/* 109:218 */         return false;
/* 110:    */       }
/* 111:220 */       DefaultCacheKey otherKey = (DefaultCacheKey)other;
/* 112:    */       
/* 113:222 */       return (this.method.equals(otherKey.method)) && (ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass));
/* 114:    */     }
/* 115:    */     
/* 116:    */     public int hashCode()
/* 117:    */     {
/* 118:227 */       return this.method.hashCode() * 29 + (this.targetClass != null ? this.targetClass.hashCode() : 0);
/* 119:    */     }
/* 120:    */   }
/* 121:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource
 * JD-Core Version:    0.7.0.1
 */