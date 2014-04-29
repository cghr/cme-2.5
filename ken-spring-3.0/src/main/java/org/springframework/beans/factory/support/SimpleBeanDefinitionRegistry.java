/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Map;
/*  5:   */ import java.util.concurrent.ConcurrentHashMap;
/*  6:   */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  7:   */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*  8:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  9:   */ import org.springframework.core.SimpleAliasRegistry;
/* 10:   */ import org.springframework.util.Assert;
/* 11:   */ import org.springframework.util.StringUtils;
/* 12:   */ 
/* 13:   */ public class SimpleBeanDefinitionRegistry
/* 14:   */   extends SimpleAliasRegistry
/* 15:   */   implements BeanDefinitionRegistry
/* 16:   */ {
/* 17:40 */   private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap();
/* 18:   */   
/* 19:   */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
/* 20:   */     throws BeanDefinitionStoreException
/* 21:   */   {
/* 22:46 */     Assert.hasText(beanName, "'beanName' must not be empty");
/* 23:47 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/* 24:48 */     this.beanDefinitionMap.put(beanName, beanDefinition);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void removeBeanDefinition(String beanName)
/* 28:   */     throws NoSuchBeanDefinitionException
/* 29:   */   {
/* 30:52 */     if (this.beanDefinitionMap.remove(beanName) == null) {
/* 31:53 */       throw new NoSuchBeanDefinitionException(beanName);
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   public BeanDefinition getBeanDefinition(String beanName)
/* 36:   */     throws NoSuchBeanDefinitionException
/* 37:   */   {
/* 38:58 */     BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.get(beanName);
/* 39:59 */     if (bd == null) {
/* 40:60 */       throw new NoSuchBeanDefinitionException(beanName);
/* 41:   */     }
/* 42:62 */     return bd;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public boolean containsBeanDefinition(String beanName)
/* 46:   */   {
/* 47:66 */     return this.beanDefinitionMap.containsKey(beanName);
/* 48:   */   }
/* 49:   */   
/* 50:   */   public String[] getBeanDefinitionNames()
/* 51:   */   {
/* 52:70 */     return StringUtils.toStringArray((Collection)this.beanDefinitionMap.keySet());
/* 53:   */   }
/* 54:   */   
/* 55:   */   public int getBeanDefinitionCount()
/* 56:   */   {
/* 57:74 */     return this.beanDefinitionMap.size();
/* 58:   */   }
/* 59:   */   
/* 60:   */   public boolean isBeanNameInUse(String beanName)
/* 61:   */   {
/* 62:78 */     return (isAlias(beanName)) || (containsBeanDefinition(beanName));
/* 63:   */   }
/* 64:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry
 * JD-Core Version:    0.7.0.1
 */