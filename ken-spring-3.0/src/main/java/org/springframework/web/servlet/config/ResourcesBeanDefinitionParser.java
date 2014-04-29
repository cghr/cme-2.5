/*   1:    */ package org.springframework.web.servlet.config;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import org.springframework.beans.MutablePropertyValues;
/*   5:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   6:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*   7:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*   8:    */ import org.springframework.beans.factory.support.ManagedMap;
/*   9:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  10:    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*  11:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  12:    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*  15:    */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
/*  16:    */ import org.w3c.dom.Element;
/*  17:    */ 
/*  18:    */ class ResourcesBeanDefinitionParser
/*  19:    */   implements BeanDefinitionParser
/*  20:    */ {
/*  21:    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*  22:    */   {
/*  23: 48 */     Object source = parserContext.extractSource(element);
/*  24:    */     
/*  25: 50 */     String resourceHandlerName = registerResourceHandler(parserContext, element, source);
/*  26: 51 */     if (resourceHandlerName == null) {
/*  27: 52 */       return null;
/*  28:    */     }
/*  29: 55 */     Map<String, String> urlMap = new ManagedMap();
/*  30: 56 */     String resourceRequestPath = element.getAttribute("mapping");
/*  31: 57 */     if (!StringUtils.hasText(resourceRequestPath))
/*  32:    */     {
/*  33: 58 */       parserContext.getReaderContext().error("The 'mapping' attribute is required.", parserContext.extractSource(element));
/*  34: 59 */       return null;
/*  35:    */     }
/*  36: 61 */     urlMap.put(resourceRequestPath, resourceHandlerName);
/*  37:    */     
/*  38: 63 */     RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
/*  39: 64 */     handlerMappingDef.setSource(source);
/*  40: 65 */     handlerMappingDef.setRole(2);
/*  41: 66 */     handlerMappingDef.getPropertyValues().add("urlMap", urlMap);
/*  42:    */     
/*  43: 68 */     String order = element.getAttribute("order");
/*  44:    */     
/*  45: 70 */     handlerMappingDef.getPropertyValues().add("order", StringUtils.hasText(order) ? order : Integer.valueOf(2147483646));
/*  46:    */     
/*  47: 72 */     String beanName = parserContext.getReaderContext().generateBeanName(handlerMappingDef);
/*  48: 73 */     parserContext.getRegistry().registerBeanDefinition(beanName, handlerMappingDef);
/*  49: 74 */     parserContext.registerComponent(new BeanComponentDefinition(handlerMappingDef, beanName));
/*  50:    */     
/*  51:    */ 
/*  52: 77 */     MvcNamespaceUtils.registerBeanNameUrlHandlerMapping(parserContext, source);
/*  53:    */     
/*  54:    */ 
/*  55: 80 */     MvcNamespaceUtils.registerDefaultHandlerAdapters(parserContext, source);
/*  56:    */     
/*  57: 82 */     return null;
/*  58:    */   }
/*  59:    */   
/*  60:    */   private String registerResourceHandler(ParserContext parserContext, Element element, Object source)
/*  61:    */   {
/*  62: 86 */     String locationAttr = element.getAttribute("location");
/*  63: 87 */     if (!StringUtils.hasText(locationAttr))
/*  64:    */     {
/*  65: 88 */       parserContext.getReaderContext().error("The 'location' attribute is required.", parserContext.extractSource(element));
/*  66: 89 */       return null;
/*  67:    */     }
/*  68: 92 */     RootBeanDefinition resourceHandlerDef = new RootBeanDefinition(ResourceHttpRequestHandler.class);
/*  69: 93 */     resourceHandlerDef.setSource(source);
/*  70: 94 */     resourceHandlerDef.setRole(2);
/*  71: 95 */     resourceHandlerDef.getPropertyValues().add("locations", StringUtils.commaDelimitedListToStringArray(locationAttr));
/*  72:    */     
/*  73: 97 */     String cacheSeconds = element.getAttribute("cache-period");
/*  74: 98 */     if (StringUtils.hasText(cacheSeconds)) {
/*  75: 99 */       resourceHandlerDef.getPropertyValues().add("cacheSeconds", cacheSeconds);
/*  76:    */     }
/*  77:102 */     String beanName = parserContext.getReaderContext().generateBeanName(resourceHandlerDef);
/*  78:103 */     parserContext.getRegistry().registerBeanDefinition(beanName, resourceHandlerDef);
/*  79:104 */     parserContext.registerComponent(new BeanComponentDefinition(resourceHandlerDef, beanName));
/*  80:105 */     return beanName;
/*  81:    */   }
/*  82:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.ResourcesBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */