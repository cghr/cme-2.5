/*  1:   */ package org.springframework.cache.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.MutablePropertyValues;
/*  4:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  5:   */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*  6:   */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*  7:   */ import org.springframework.util.StringUtils;
/*  8:   */ import org.w3c.dom.Element;
/*  9:   */ 
/* 10:   */ public class CacheNamespaceHandler
/* 11:   */   extends NamespaceHandlerSupport
/* 12:   */ {
/* 13:   */   static final String CACHE_MANAGER_ATTRIBUTE = "cache-manager";
/* 14:   */   static final String DEFAULT_CACHE_MANAGER_BEAN_NAME = "cacheManager";
/* 15:   */   
/* 16:   */   static String extractCacheManager(Element element)
/* 17:   */   {
/* 18:41 */     return element.hasAttribute("cache-manager") ? 
/* 19:42 */       element.getAttribute("cache-manager") : 
/* 20:43 */       "cacheManager";
/* 21:   */   }
/* 22:   */   
/* 23:   */   static BeanDefinition parseKeyGenerator(Element element, BeanDefinition def)
/* 24:   */   {
/* 25:47 */     String name = element.getAttribute("key-generator");
/* 26:48 */     if (StringUtils.hasText(name)) {
/* 27:49 */       def.getPropertyValues().add("keyGenerator", new RuntimeBeanReference(name.trim()));
/* 28:   */     }
/* 29:51 */     return def;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void init()
/* 33:   */   {
/* 34:55 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenCacheBeanDefinitionParser());
/* 35:56 */     registerBeanDefinitionParser("advice", new CacheAdviceParser());
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.config.CacheNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */