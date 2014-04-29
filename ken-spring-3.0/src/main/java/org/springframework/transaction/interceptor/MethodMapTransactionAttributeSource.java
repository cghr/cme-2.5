/*   1:    */ package org.springframework.transaction.interceptor;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  12:    */ import org.springframework.beans.factory.InitializingBean;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.ClassUtils;
/*  15:    */ import org.springframework.util.ObjectUtils;
/*  16:    */ import org.springframework.util.PatternMatchUtils;
/*  17:    */ 
/*  18:    */ public class MethodMapTransactionAttributeSource
/*  19:    */   implements TransactionAttributeSource, BeanClassLoaderAware, InitializingBean
/*  20:    */ {
/*  21: 49 */   protected final Log logger = LogFactory.getLog(getClass());
/*  22:    */   private Map<String, TransactionAttribute> methodMap;
/*  23: 54 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  24: 56 */   private boolean eagerlyInitialized = false;
/*  25: 58 */   private boolean initialized = false;
/*  26: 62 */   private final Map<Method, TransactionAttribute> transactionAttributeMap = new HashMap();
/*  27: 65 */   private final Map<Method, String> methodNameMap = new HashMap();
/*  28:    */   
/*  29:    */   public void setMethodMap(Map<String, TransactionAttribute> methodMap)
/*  30:    */   {
/*  31: 81 */     this.methodMap = methodMap;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*  35:    */   {
/*  36: 85 */     this.beanClassLoader = beanClassLoader;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void afterPropertiesSet()
/*  40:    */   {
/*  41: 95 */     initMethodMap(this.methodMap);
/*  42: 96 */     this.eagerlyInitialized = true;
/*  43: 97 */     this.initialized = true;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected void initMethodMap(Map<String, TransactionAttribute> methodMap)
/*  47:    */   {
/*  48:106 */     if (methodMap != null) {
/*  49:107 */       for (Map.Entry<String, TransactionAttribute> entry : methodMap.entrySet()) {
/*  50:108 */         addTransactionalMethod((String)entry.getKey(), (TransactionAttribute)entry.getValue());
/*  51:    */       }
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void addTransactionalMethod(String name, TransactionAttribute attr)
/*  56:    */   {
/*  57:122 */     Assert.notNull(name, "Name must not be null");
/*  58:123 */     int lastDotIndex = name.lastIndexOf(".");
/*  59:124 */     if (lastDotIndex == -1) {
/*  60:125 */       throw new IllegalArgumentException("'" + name + "' is not a valid method name: format is FQN.methodName");
/*  61:    */     }
/*  62:127 */     String className = name.substring(0, lastDotIndex);
/*  63:128 */     String methodName = name.substring(lastDotIndex + 1);
/*  64:129 */     Class clazz = ClassUtils.resolveClassName(className, this.beanClassLoader);
/*  65:130 */     addTransactionalMethod(clazz, methodName, attr);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void addTransactionalMethod(Class<?> clazz, String mappedName, TransactionAttribute attr)
/*  69:    */   {
/*  70:141 */     Assert.notNull(clazz, "Class must not be null");
/*  71:142 */     Assert.notNull(mappedName, "Mapped name must not be null");
/*  72:143 */     String name = clazz.getName() + '.' + mappedName;
/*  73:    */     
/*  74:145 */     Method[] methods = clazz.getDeclaredMethods();
/*  75:146 */     List<Method> matchingMethods = new ArrayList();
/*  76:147 */     for (Method method : methods) {
/*  77:148 */       if (isMatch(method.getName(), mappedName)) {
/*  78:149 */         matchingMethods.add(method);
/*  79:    */       }
/*  80:    */     }
/*  81:152 */     if (matchingMethods.isEmpty()) {
/*  82:153 */       throw new IllegalArgumentException(
/*  83:154 */         "Couldn't find method '" + mappedName + "' on class [" + clazz.getName() + "]");
/*  84:    */     }
/*  85:158 */     for (Method method : matchingMethods)
/*  86:    */     {
/*  87:159 */       String regMethodName = (String)this.methodNameMap.get(method);
/*  88:160 */       if ((regMethodName == null) || ((!regMethodName.equals(name)) && (regMethodName.length() <= name.length())))
/*  89:    */       {
/*  90:163 */         if ((this.logger.isDebugEnabled()) && (regMethodName != null)) {
/*  91:164 */           this.logger.debug("Replacing attribute for transactional method [" + method + "]: current name '" + 
/*  92:165 */             name + "' is more specific than '" + regMethodName + "'");
/*  93:    */         }
/*  94:167 */         this.methodNameMap.put(method, name);
/*  95:168 */         addTransactionalMethod(method, attr);
/*  96:    */       }
/*  97:171 */       else if (this.logger.isDebugEnabled())
/*  98:    */       {
/*  99:172 */         this.logger.debug("Keeping attribute for transactional method [" + method + "]: current name '" + 
/* 100:173 */           name + "' is not more specific than '" + regMethodName + "'");
/* 101:    */       }
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void addTransactionalMethod(Method method, TransactionAttribute attr)
/* 106:    */   {
/* 107:185 */     Assert.notNull(method, "Method must not be null");
/* 108:186 */     Assert.notNull(attr, "TransactionAttribute must not be null");
/* 109:187 */     if (this.logger.isDebugEnabled()) {
/* 110:188 */       this.logger.debug("Adding transactional method [" + method + "] with attribute [" + attr + "]");
/* 111:    */     }
/* 112:190 */     this.transactionAttributeMap.put(method, attr);
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected boolean isMatch(String methodName, String mappedName)
/* 116:    */   {
/* 117:203 */     return PatternMatchUtils.simpleMatch(mappedName, methodName);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass)
/* 121:    */   {
/* 122:208 */     if (this.eagerlyInitialized) {
/* 123:209 */       return (TransactionAttribute)this.transactionAttributeMap.get(method);
/* 124:    */     }
/* 125:212 */     synchronized (this.transactionAttributeMap)
/* 126:    */     {
/* 127:213 */       if (!this.initialized)
/* 128:    */       {
/* 129:214 */         initMethodMap(this.methodMap);
/* 130:215 */         this.initialized = true;
/* 131:    */       }
/* 132:217 */       return (TransactionAttribute)this.transactionAttributeMap.get(method);
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   public boolean equals(Object other)
/* 137:    */   {
/* 138:225 */     if (this == other) {
/* 139:226 */       return true;
/* 140:    */     }
/* 141:228 */     if (!(other instanceof MethodMapTransactionAttributeSource)) {
/* 142:229 */       return false;
/* 143:    */     }
/* 144:231 */     MethodMapTransactionAttributeSource otherTas = (MethodMapTransactionAttributeSource)other;
/* 145:232 */     return ObjectUtils.nullSafeEquals(this.methodMap, otherTas.methodMap);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int hashCode()
/* 149:    */   {
/* 150:237 */     return MethodMapTransactionAttributeSource.class.hashCode();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public String toString()
/* 154:    */   {
/* 155:242 */     return getClass().getName() + ": " + this.methodMap;
/* 156:    */   }
/* 157:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.MethodMapTransactionAttributeSource
 * JD-Core Version:    0.7.0.1
 */