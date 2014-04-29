/*   1:    */ package org.springframework.cache.config;
/*   2:    */ 
/*   3:    */ import org.springframework.aop.config.AopNamespaceUtils;
/*   4:    */ import org.springframework.beans.MutablePropertyValues;
/*   5:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   6:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*   7:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*   8:    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*   9:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  10:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  11:    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*  12:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  13:    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  14:    */ import org.springframework.cache.annotation.AnnotationCacheOperationSource;
/*  15:    */ import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
/*  16:    */ import org.springframework.cache.interceptor.CacheInterceptor;
/*  17:    */ import org.w3c.dom.Element;
/*  18:    */ 
/*  19:    */ class AnnotationDrivenCacheBeanDefinitionParser
/*  20:    */   implements BeanDefinitionParser
/*  21:    */ {
/*  22:    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*  23:    */   {
/*  24: 56 */     String mode = element.getAttribute("mode");
/*  25: 57 */     if ("aspectj".equals(mode)) {
/*  26: 59 */       registerCacheAspect(element, parserContext);
/*  27:    */     } else {
/*  28: 63 */       AopAutoProxyConfigurer.configureAutoProxyCreator(element, parserContext);
/*  29:    */     }
/*  30: 66 */     return null;
/*  31:    */   }
/*  32:    */   
/*  33:    */   private static void parseCacheManagerProperty(Element element, BeanDefinition def)
/*  34:    */   {
/*  35: 70 */     def.getPropertyValues().add("cacheManager", 
/*  36: 71 */       new RuntimeBeanReference(CacheNamespaceHandler.extractCacheManager(element)));
/*  37:    */   }
/*  38:    */   
/*  39:    */   private void registerCacheAspect(Element element, ParserContext parserContext)
/*  40:    */   {
/*  41: 86 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalCacheAspect"))
/*  42:    */     {
/*  43: 87 */       RootBeanDefinition def = new RootBeanDefinition();
/*  44: 88 */       def.setBeanClassName("org.springframework.cache.aspectj.AnnotationCacheAspect");
/*  45: 89 */       def.setFactoryMethodName("aspectOf");
/*  46: 90 */       parseCacheManagerProperty(element, def);
/*  47: 91 */       parserContext.registerBeanComponent(new BeanComponentDefinition(def, "org.springframework.cache.config.internalCacheAspect"));
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   private static class AopAutoProxyConfigurer
/*  52:    */   {
/*  53:    */     public static void configureAutoProxyCreator(Element element, ParserContext parserContext)
/*  54:    */     {
/*  55:102 */       AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);
/*  56:104 */       if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.cache.config.internalCacheAdvisor"))
/*  57:    */       {
/*  58:105 */         Object eleSource = parserContext.extractSource(element);
/*  59:    */         
/*  60:    */ 
/*  61:108 */         RootBeanDefinition sourceDef = new RootBeanDefinition(AnnotationCacheOperationSource.class);
/*  62:109 */         sourceDef.setSource(eleSource);
/*  63:110 */         sourceDef.setRole(2);
/*  64:111 */         String sourceName = parserContext.getReaderContext().registerWithGeneratedName(sourceDef);
/*  65:    */         
/*  66:    */ 
/*  67:114 */         RootBeanDefinition interceptorDef = new RootBeanDefinition(CacheInterceptor.class);
/*  68:115 */         interceptorDef.setSource(eleSource);
/*  69:116 */         interceptorDef.setRole(2);
/*  70:117 */         AnnotationDrivenCacheBeanDefinitionParser.parseCacheManagerProperty(element, interceptorDef);
/*  71:118 */         CacheNamespaceHandler.parseKeyGenerator(element, interceptorDef);
/*  72:119 */         interceptorDef.getPropertyValues().add("cacheOperationSources", new RuntimeBeanReference(sourceName));
/*  73:120 */         String interceptorName = parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);
/*  74:    */         
/*  75:    */ 
/*  76:123 */         RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryCacheOperationSourceAdvisor.class);
/*  77:124 */         advisorDef.setSource(eleSource);
/*  78:125 */         advisorDef.setRole(2);
/*  79:126 */         advisorDef.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
/*  80:127 */         advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
/*  81:128 */         if (element.hasAttribute("order")) {
/*  82:129 */           advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
/*  83:    */         }
/*  84:131 */         parserContext.getRegistry().registerBeanDefinition("org.springframework.cache.config.internalCacheAdvisor", advisorDef);
/*  85:    */         
/*  86:133 */         CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), 
/*  87:134 */           eleSource);
/*  88:135 */         compositeDef.addNestedComponent(new BeanComponentDefinition(sourceDef, sourceName));
/*  89:136 */         compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
/*  90:137 */         compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, "org.springframework.cache.config.internalCacheAdvisor"));
/*  91:138 */         parserContext.registerComponent(compositeDef);
/*  92:    */       }
/*  93:    */     }
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.config.AnnotationDrivenCacheBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */