/*  1:   */ package org.springframework.web.servlet.config;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.beans.MutablePropertyValues;
/*  5:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  6:   */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*  7:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  8:   */ import org.springframework.beans.factory.support.ManagedMap;
/*  9:   */ import org.springframework.beans.factory.support.RootBeanDefinition;
/* 10:   */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/* 11:   */ import org.springframework.beans.factory.xml.ParserContext;
/* 12:   */ import org.springframework.beans.factory.xml.XmlReaderContext;
/* 13:   */ import org.springframework.util.StringUtils;
/* 14:   */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/* 15:   */ import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
/* 16:   */ import org.w3c.dom.Element;
/* 17:   */ 
/* 18:   */ class DefaultServletHandlerBeanDefinitionParser
/* 19:   */   implements BeanDefinitionParser
/* 20:   */ {
/* 21:   */   public BeanDefinition parse(Element element, ParserContext parserContext)
/* 22:   */   {
/* 23:47 */     Object source = parserContext.extractSource(element);
/* 24:   */     
/* 25:49 */     String defaultServletName = element.getAttribute("default-servlet-name");
/* 26:50 */     RootBeanDefinition defaultServletHandlerDef = new RootBeanDefinition(DefaultServletHttpRequestHandler.class);
/* 27:51 */     defaultServletHandlerDef.setSource(source);
/* 28:52 */     defaultServletHandlerDef.setRole(2);
/* 29:53 */     if (StringUtils.hasText(defaultServletName)) {
/* 30:54 */       defaultServletHandlerDef.getPropertyValues().add("defaultServletName", defaultServletName);
/* 31:   */     }
/* 32:56 */     String defaultServletHandlerName = parserContext.getReaderContext().generateBeanName(defaultServletHandlerDef);
/* 33:57 */     parserContext.getRegistry().registerBeanDefinition(defaultServletHandlerName, defaultServletHandlerDef);
/* 34:58 */     parserContext.registerComponent(new BeanComponentDefinition(defaultServletHandlerDef, defaultServletHandlerName));
/* 35:   */     
/* 36:60 */     Map<String, String> urlMap = new ManagedMap();
/* 37:61 */     urlMap.put("/**", defaultServletHandlerName);
/* 38:   */     
/* 39:63 */     RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
/* 40:64 */     handlerMappingDef.setSource(source);
/* 41:65 */     handlerMappingDef.setRole(2);
/* 42:66 */     handlerMappingDef.getPropertyValues().add("urlMap", urlMap);
/* 43:   */     
/* 44:68 */     String handlerMappingBeanName = parserContext.getReaderContext().generateBeanName(handlerMappingDef);
/* 45:69 */     parserContext.getRegistry().registerBeanDefinition(handlerMappingBeanName, handlerMappingDef);
/* 46:70 */     parserContext.registerComponent(new BeanComponentDefinition(handlerMappingDef, handlerMappingBeanName));
/* 47:   */     
/* 48:   */ 
/* 49:73 */     MvcNamespaceUtils.registerBeanNameUrlHandlerMapping(parserContext, source);
/* 50:   */     
/* 51:   */ 
/* 52:76 */     MvcNamespaceUtils.registerDefaultHandlerAdapters(parserContext, source);
/* 53:   */     
/* 54:78 */     return null;
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.DefaultServletHandlerBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */