/*   1:    */ package org.springframework.cache.config;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.springframework.beans.MutablePropertyValues;
/*   5:    */ import org.springframework.beans.factory.config.TypedStringValue;
/*   6:    */ import org.springframework.beans.factory.parsing.ReaderContext;
/*   7:    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*   8:    */ import org.springframework.beans.factory.support.ManagedList;
/*   9:    */ import org.springframework.beans.factory.support.ManagedMap;
/*  10:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  11:    */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*  12:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  13:    */ import org.springframework.cache.annotation.AnnotationCacheOperationSource;
/*  14:    */ import org.springframework.cache.interceptor.CacheEvictOperation;
/*  15:    */ import org.springframework.cache.interceptor.CacheInterceptor;
/*  16:    */ import org.springframework.cache.interceptor.CacheOperation;
/*  17:    */ import org.springframework.cache.interceptor.CacheUpdateOperation;
/*  18:    */ import org.springframework.cache.interceptor.NameMatchCacheOperationSource;
/*  19:    */ import org.springframework.util.StringUtils;
/*  20:    */ import org.springframework.util.xml.DomUtils;
/*  21:    */ import org.w3c.dom.Element;
/*  22:    */ 
/*  23:    */ class CacheAdviceParser
/*  24:    */   extends AbstractSingleBeanDefinitionParser
/*  25:    */ {
/*  26:    */   private static final String CACHEABLE_ELEMENT = "cacheable";
/*  27:    */   private static final String CACHE_EVICT_ELEMENT = "cache-evict";
/*  28:    */   private static final String METHOD_ATTRIBUTE = "method";
/*  29:    */   private static final String DEFS_ELEMENT = "definitions";
/*  30:    */   
/*  31:    */   private static class Props
/*  32:    */   {
/*  33:    */     private String key;
/*  34:    */     private String condition;
/*  35: 56 */     private String[] caches = null;
/*  36:    */     
/*  37:    */     Props(Element root)
/*  38:    */     {
/*  39: 59 */       String defaultCache = root.getAttribute("cache");
/*  40: 60 */       this.key = root.getAttribute("key");
/*  41: 61 */       this.condition = root.getAttribute("condition");
/*  42: 63 */       if (StringUtils.hasText(defaultCache)) {
/*  43: 64 */         this.caches = StringUtils.commaDelimitedListToStringArray(defaultCache.trim());
/*  44:    */       }
/*  45:    */     }
/*  46:    */     
/*  47:    */     CacheOperation merge(Element element, ReaderContext readerCtx, CacheOperation op)
/*  48:    */     {
/*  49: 69 */       String cache = element.getAttribute("cache");
/*  50: 70 */       String k = element.getAttribute("key");
/*  51: 71 */       String c = element.getAttribute("condition");
/*  52:    */       
/*  53: 73 */       String[] localCaches = this.caches;
/*  54: 74 */       String localKey = this.key;String localCondition = this.condition;
/*  55: 77 */       if (StringUtils.hasText(cache)) {
/*  56: 78 */         localCaches = StringUtils.commaDelimitedListToStringArray(cache.trim());
/*  57: 80 */       } else if (this.caches == null) {
/*  58: 81 */         readerCtx.error("No cache specified specified for " + element.getNodeName(), element);
/*  59:    */       }
/*  60: 85 */       if (StringUtils.hasText(k)) {
/*  61: 86 */         localKey = k.trim();
/*  62:    */       }
/*  63: 89 */       if (StringUtils.hasText(c)) {
/*  64: 90 */         localCondition = c.trim();
/*  65:    */       }
/*  66: 92 */       op.setCacheNames(localCaches);
/*  67: 93 */       op.setKey(localKey);
/*  68: 94 */       op.setCondition(localCondition);
/*  69:    */       
/*  70: 96 */       return op;
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected Class<?> getBeanClass(Element element)
/*  75:    */   {
/*  76:107 */     return CacheInterceptor.class;
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*  80:    */   {
/*  81:112 */     builder.addPropertyReference("cacheManager", CacheNamespaceHandler.extractCacheManager(element));
/*  82:113 */     CacheNamespaceHandler.parseKeyGenerator(element, builder.getBeanDefinition());
/*  83:    */     
/*  84:115 */     List<Element> cacheDefs = DomUtils.getChildElementsByTagName(element, "definitions");
/*  85:116 */     if (cacheDefs.size() >= 1)
/*  86:    */     {
/*  87:118 */       List<RootBeanDefinition> attributeSourceDefinitions = parseDefinitionsSources(cacheDefs, parserContext);
/*  88:119 */       builder.addPropertyValue("cacheOperationSources", attributeSourceDefinitions);
/*  89:    */     }
/*  90:    */     else
/*  91:    */     {
/*  92:122 */       builder.addPropertyValue("cacheOperationSources", new RootBeanDefinition(
/*  93:123 */         AnnotationCacheOperationSource.class));
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   private List<RootBeanDefinition> parseDefinitionsSources(List<Element> definitions, ParserContext parserContext)
/*  98:    */   {
/*  99:128 */     ManagedList<RootBeanDefinition> defs = new ManagedList(definitions.size());
/* 100:131 */     for (Element element : definitions) {
/* 101:132 */       defs.add(parseDefinitionSource(element, parserContext));
/* 102:    */     }
/* 103:135 */     return defs;
/* 104:    */   }
/* 105:    */   
/* 106:    */   private RootBeanDefinition parseDefinitionSource(Element definition, ParserContext parserContext)
/* 107:    */   {
/* 108:139 */     Props prop = new Props(definition);
/* 109:    */     
/* 110:    */ 
/* 111:142 */     ManagedMap<TypedStringValue, CacheOperation> cacheOpeMap = new ManagedMap();
/* 112:143 */     cacheOpeMap.setSource(parserContext.extractSource(definition));
/* 113:    */     
/* 114:145 */     List<Element> updateCacheMethods = DomUtils.getChildElementsByTagName(definition, "cacheable");
/* 115:    */     String name;
/* 116:147 */     for (Element opElement : updateCacheMethods)
/* 117:    */     {
/* 118:148 */       name = opElement.getAttribute("method");
/* 119:149 */       TypedStringValue nameHolder = new TypedStringValue(name);
/* 120:150 */       nameHolder.setSource(parserContext.extractSource(opElement));
/* 121:151 */       CacheOperation op = prop.merge(opElement, parserContext.getReaderContext(), new CacheUpdateOperation());
/* 122:    */       
/* 123:153 */       cacheOpeMap.put(nameHolder, op);
/* 124:    */     }
/* 125:156 */     List<Element> evictCacheMethods = DomUtils.getChildElementsByTagName(definition, "cache-evict");
/* 126:158 */     for (Element opElement : evictCacheMethods)
/* 127:    */     {
/* 128:159 */       String name = opElement.getAttribute("method");
/* 129:160 */       TypedStringValue nameHolder = new TypedStringValue(name);
/* 130:161 */       nameHolder.setSource(parserContext.extractSource(opElement));
/* 131:162 */       CacheOperation op = prop.merge(opElement, parserContext.getReaderContext(), new CacheEvictOperation());
/* 132:    */       
/* 133:164 */       cacheOpeMap.put(nameHolder, op);
/* 134:    */     }
/* 135:167 */     RootBeanDefinition attributeSourceDefinition = new RootBeanDefinition(NameMatchCacheOperationSource.class);
/* 136:168 */     attributeSourceDefinition.setSource(parserContext.extractSource(definition));
/* 137:169 */     attributeSourceDefinition.getPropertyValues().add("nameMap", cacheOpeMap);
/* 138:170 */     return attributeSourceDefinition;
/* 139:    */   }
/* 140:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.config.CacheAdviceParser
 * JD-Core Version:    0.7.0.1
 */