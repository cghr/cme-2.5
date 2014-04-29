/*  1:   */ package org.springframework.web.servlet.config;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.beans.MutablePropertyValues;
/*  5:   */ import org.springframework.beans.PropertyValue;
/*  6:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  7:   */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*  8:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  9:   */ import org.springframework.beans.factory.support.ManagedMap;
/* 10:   */ import org.springframework.beans.factory.support.RootBeanDefinition;
/* 11:   */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/* 12:   */ import org.springframework.beans.factory.xml.ParserContext;
/* 13:   */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/* 14:   */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
/* 15:   */ import org.w3c.dom.Element;
/* 16:   */ 
/* 17:   */ class ViewControllerBeanDefinitionParser
/* 18:   */   implements BeanDefinitionParser
/* 19:   */ {
/* 20:   */   private static final String HANDLER_MAPPING_BEAN_NAME = "org.springframework.web.servlet.config.viewControllerHandlerMapping";
/* 21:   */   
/* 22:   */   public BeanDefinition parse(Element element, ParserContext parserContext)
/* 23:   */   {
/* 24:47 */     Object source = parserContext.extractSource(element);
/* 25:   */     
/* 26:   */ 
/* 27:50 */     BeanDefinition handlerMappingDef = registerHandlerMapping(parserContext, source);
/* 28:   */     
/* 29:   */ 
/* 30:53 */     MvcNamespaceUtils.registerBeanNameUrlHandlerMapping(parserContext, source);
/* 31:   */     
/* 32:   */ 
/* 33:56 */     MvcNamespaceUtils.registerDefaultHandlerAdapters(parserContext, source);
/* 34:   */     
/* 35:   */ 
/* 36:59 */     RootBeanDefinition viewControllerDef = new RootBeanDefinition(ParameterizableViewController.class);
/* 37:60 */     viewControllerDef.setSource(source);
/* 38:61 */     if (element.hasAttribute("view-name")) {
/* 39:62 */       viewControllerDef.getPropertyValues().add("viewName", element.getAttribute("view-name"));
/* 40:   */     }
/* 41:   */     Map<String, BeanDefinition> urlMap;
/* 42:   */     Map<String, BeanDefinition> urlMap;
/* 43:65 */     if (handlerMappingDef.getPropertyValues().contains("urlMap"))
/* 44:   */     {
/* 45:66 */       urlMap = (Map)handlerMappingDef.getPropertyValues().getPropertyValue("urlMap").getValue();
/* 46:   */     }
/* 47:   */     else
/* 48:   */     {
/* 49:69 */       urlMap = new ManagedMap();
/* 50:70 */       handlerMappingDef.getPropertyValues().add("urlMap", urlMap);
/* 51:   */     }
/* 52:72 */     urlMap.put(element.getAttribute("path"), viewControllerDef);
/* 53:   */     
/* 54:74 */     return null;
/* 55:   */   }
/* 56:   */   
/* 57:   */   private BeanDefinition registerHandlerMapping(ParserContext parserContext, Object source)
/* 58:   */   {
/* 59:78 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerMapping"))
/* 60:   */     {
/* 61:79 */       RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
/* 62:80 */       handlerMappingDef.setSource(source);
/* 63:81 */       handlerMappingDef.getPropertyValues().add("order", "1");
/* 64:82 */       handlerMappingDef.setRole(2);
/* 65:83 */       parserContext.getRegistry().registerBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerMapping", handlerMappingDef);
/* 66:84 */       parserContext.registerComponent(new BeanComponentDefinition(handlerMappingDef, "org.springframework.web.servlet.config.viewControllerHandlerMapping"));
/* 67:85 */       return handlerMappingDef;
/* 68:   */     }
/* 69:88 */     return parserContext.getRegistry().getBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerMapping");
/* 70:   */   }
/* 71:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.ViewControllerBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */