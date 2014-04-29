/*  1:   */ package org.springframework.web.servlet.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.MutablePropertyValues;
/*  4:   */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*  5:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  6:   */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  7:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  8:   */ import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
/*  9:   */ import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
/* 10:   */ import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
/* 11:   */ 
/* 12:   */ abstract class MvcNamespaceUtils
/* 13:   */ {
/* 14:   */   private static final String BEAN_NAME_URL_HANDLER_MAPPING = "org.springframework.web.servlet.handler.beanNameUrlHandlerMapping";
/* 15:   */   private static final String VIEW_CONTROLLER_HANDLER_ADAPTER = "org.springframework.web.servlet.config.viewControllerHandlerAdapter";
/* 16:   */   private static final String HTTP_REQUEST_HANDLER_ADAPTER = "org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter";
/* 17:   */   
/* 18:   */   public static void registerDefaultHandlerAdapters(ParserContext parserContext, Object source)
/* 19:   */   {
/* 20:45 */     registerHttpRequestHandlerAdapter(parserContext, source);
/* 21:46 */     registerSimpleControllerHandlerAdapter(parserContext, source);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public static void registerBeanNameUrlHandlerMapping(ParserContext parserContext, Object source)
/* 25:   */   {
/* 26:50 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.web.servlet.handler.beanNameUrlHandlerMapping"))
/* 27:   */     {
/* 28:51 */       RootBeanDefinition beanNameMappingDef = new RootBeanDefinition(BeanNameUrlHandlerMapping.class);
/* 29:52 */       beanNameMappingDef.setSource(source);
/* 30:53 */       beanNameMappingDef.setRole(2);
/* 31:54 */       beanNameMappingDef.getPropertyValues().add("order", Integer.valueOf(2));
/* 32:55 */       parserContext.getRegistry().registerBeanDefinition("org.springframework.web.servlet.handler.beanNameUrlHandlerMapping", beanNameMappingDef);
/* 33:56 */       parserContext.registerComponent(new BeanComponentDefinition(beanNameMappingDef, "org.springframework.web.servlet.handler.beanNameUrlHandlerMapping"));
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   public static void registerHttpRequestHandlerAdapter(ParserContext parserContext, Object source)
/* 38:   */   {
/* 39:61 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter"))
/* 40:   */     {
/* 41:62 */       RootBeanDefinition handlerAdapterDef = new RootBeanDefinition(HttpRequestHandlerAdapter.class);
/* 42:63 */       handlerAdapterDef.setSource(source);
/* 43:64 */       handlerAdapterDef.setRole(2);
/* 44:65 */       parserContext.getRegistry().registerBeanDefinition("org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter", handlerAdapterDef);
/* 45:66 */       parserContext.registerComponent(new BeanComponentDefinition(handlerAdapterDef, "org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter"));
/* 46:   */     }
/* 47:   */   }
/* 48:   */   
/* 49:   */   public static void registerSimpleControllerHandlerAdapter(ParserContext parserContext, Object source)
/* 50:   */   {
/* 51:71 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerAdapter"))
/* 52:   */     {
/* 53:72 */       RootBeanDefinition handlerAdapterDef = new RootBeanDefinition(SimpleControllerHandlerAdapter.class);
/* 54:73 */       handlerAdapterDef.setSource(source);
/* 55:74 */       handlerAdapterDef.setRole(2);
/* 56:75 */       parserContext.getRegistry().registerBeanDefinition("org.springframework.web.servlet.config.viewControllerHandlerAdapter", handlerAdapterDef);
/* 57:76 */       parserContext.registerComponent(new BeanComponentDefinition(handlerAdapterDef, "org.springframework.web.servlet.config.viewControllerHandlerAdapter"));
/* 58:   */     }
/* 59:   */   }
/* 60:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.MvcNamespaceUtils
 * JD-Core Version:    0.7.0.1
 */