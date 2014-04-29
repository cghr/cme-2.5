/*   1:    */ package org.springframework.cache.interceptor;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.Enumeration;
/*   6:    */ import java.util.LinkedHashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Properties;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.util.ObjectUtils;
/*  13:    */ import org.springframework.util.PatternMatchUtils;
/*  14:    */ 
/*  15:    */ public class NameMatchCacheOperationSource
/*  16:    */   implements CacheOperationSource, Serializable
/*  17:    */ {
/*  18: 43 */   protected static final Log logger = LogFactory.getLog(NameMatchCacheOperationSource.class);
/*  19: 46 */   private Map<String, CacheOperation> nameMap = new LinkedHashMap();
/*  20:    */   
/*  21:    */   public void setNameMap(Map<String, CacheOperation> nameMap)
/*  22:    */   {
/*  23: 56 */     for (Map.Entry<String, CacheOperation> entry : nameMap.entrySet()) {
/*  24: 57 */       addCacheMethod((String)entry.getKey(), (CacheOperation)entry.getValue());
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setProperties(Properties cacheOperations)
/*  29:    */   {
/*  30: 69 */     CacheOperationEditor tae = new CacheOperationEditor();
/*  31: 70 */     Enumeration propNames = cacheOperations.propertyNames();
/*  32: 71 */     while (propNames.hasMoreElements())
/*  33:    */     {
/*  34: 72 */       String methodName = (String)propNames.nextElement();
/*  35: 73 */       String value = cacheOperations.getProperty(methodName);
/*  36: 74 */       tae.setAsText(value);
/*  37: 75 */       CacheOperation op = (CacheOperation)tae.getValue();
/*  38: 76 */       addCacheMethod(methodName, op);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void addCacheMethod(String methodName, CacheOperation operation)
/*  43:    */   {
/*  44: 88 */     if (logger.isDebugEnabled()) {
/*  45: 89 */       logger.debug("Adding method [" + methodName + "] with cache operation [" + operation + "]");
/*  46:    */     }
/*  47: 91 */     this.nameMap.put(methodName, operation);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public CacheOperation getCacheOperation(Method method, Class<?> targetClass)
/*  51:    */   {
/*  52: 96 */     String methodName = method.getName();
/*  53: 97 */     CacheOperation attr = (CacheOperation)this.nameMap.get(methodName);
/*  54: 99 */     if (attr == null)
/*  55:    */     {
/*  56:101 */       String bestNameMatch = null;
/*  57:102 */       for (String mappedName : this.nameMap.keySet()) {
/*  58:103 */         if ((isMatch(methodName, mappedName)) && (
/*  59:104 */           (bestNameMatch == null) || (bestNameMatch.length() <= mappedName.length())))
/*  60:    */         {
/*  61:105 */           attr = (CacheOperation)this.nameMap.get(mappedName);
/*  62:106 */           bestNameMatch = mappedName;
/*  63:    */         }
/*  64:    */       }
/*  65:    */     }
/*  66:111 */     return attr;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected boolean isMatch(String methodName, String mappedName)
/*  70:    */   {
/*  71:124 */     return PatternMatchUtils.simpleMatch(mappedName, methodName);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean equals(Object other)
/*  75:    */   {
/*  76:129 */     if (this == other) {
/*  77:130 */       return true;
/*  78:    */     }
/*  79:132 */     if (!(other instanceof NameMatchCacheOperationSource)) {
/*  80:133 */       return false;
/*  81:    */     }
/*  82:135 */     NameMatchCacheOperationSource otherTas = (NameMatchCacheOperationSource)other;
/*  83:136 */     return ObjectUtils.nullSafeEquals(this.nameMap, otherTas.nameMap);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int hashCode()
/*  87:    */   {
/*  88:141 */     return NameMatchCacheOperationSource.class.hashCode();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public String toString()
/*  92:    */   {
/*  93:146 */     return getClass().getName() + ": " + this.nameMap;
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.NameMatchCacheOperationSource
 * JD-Core Version:    0.7.0.1
 */