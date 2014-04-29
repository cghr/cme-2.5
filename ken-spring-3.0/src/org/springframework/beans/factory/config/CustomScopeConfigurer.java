/*  1:   */ package org.springframework.beans.factory.config;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import java.util.Map.Entry;
/*  5:   */ import org.springframework.beans.BeanUtils;
/*  6:   */ import org.springframework.beans.BeansException;
/*  7:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  8:   */ import org.springframework.core.Ordered;
/*  9:   */ import org.springframework.util.Assert;
/* 10:   */ import org.springframework.util.ClassUtils;
/* 11:   */ 
/* 12:   */ public class CustomScopeConfigurer
/* 13:   */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, Ordered
/* 14:   */ {
/* 15:   */   private Map<String, Object> scopes;
/* 16:49 */   private int order = 2147483647;
/* 17:51 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/* 18:   */   
/* 19:   */   public void setScopes(Map<String, Object> scopes)
/* 20:   */   {
/* 21:61 */     this.scopes = scopes;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setOrder(int order)
/* 25:   */   {
/* 26:65 */     this.order = order;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public int getOrder()
/* 30:   */   {
/* 31:69 */     return this.order;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/* 35:   */   {
/* 36:73 */     this.beanClassLoader = beanClassLoader;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/* 40:   */     throws BeansException
/* 41:   */   {
/* 42:79 */     if (this.scopes != null) {
/* 43:80 */       for (Map.Entry<String, Object> entry : this.scopes.entrySet())
/* 44:   */       {
/* 45:81 */         String scopeKey = (String)entry.getKey();
/* 46:82 */         Object value = entry.getValue();
/* 47:83 */         if ((value instanceof Scope))
/* 48:   */         {
/* 49:84 */           beanFactory.registerScope(scopeKey, (Scope)value);
/* 50:   */         }
/* 51:86 */         else if ((value instanceof Class))
/* 52:   */         {
/* 53:87 */           Class scopeClass = (Class)value;
/* 54:88 */           Assert.isAssignable(Scope.class, scopeClass);
/* 55:89 */           beanFactory.registerScope(scopeKey, (Scope)BeanUtils.instantiateClass(scopeClass));
/* 56:   */         }
/* 57:91 */         else if ((value instanceof String))
/* 58:   */         {
/* 59:92 */           Class scopeClass = ClassUtils.resolveClassName((String)value, this.beanClassLoader);
/* 60:93 */           Assert.isAssignable(Scope.class, scopeClass);
/* 61:94 */           beanFactory.registerScope(scopeKey, (Scope)BeanUtils.instantiateClass(scopeClass));
/* 62:   */         }
/* 63:   */         else
/* 64:   */         {
/* 65:97 */           throw new IllegalArgumentException("Mapped value [" + value + "] for scope key [" + 
/* 66:98 */             scopeKey + "] is not an instance of required type [" + Scope.class.getName() + 
/* 67:99 */             "] or a corresponding Class or String value indicating a Scope implementation");
/* 68:   */         }
/* 69:   */       }
/* 70:   */     }
/* 71:   */   }
/* 72:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.CustomScopeConfigurer
 * JD-Core Version:    0.7.0.1
 */