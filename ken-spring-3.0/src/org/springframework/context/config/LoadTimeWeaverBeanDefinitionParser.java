/*  1:   */ package org.springframework.context.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  5:   */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  6:   */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*  7:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  8:   */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  9:   */ import org.springframework.core.io.ResourceLoader;
/* 10:   */ import org.springframework.util.ClassUtils;
/* 11:   */ import org.w3c.dom.Element;
/* 12:   */ 
/* 13:   */ class LoadTimeWeaverBeanDefinitionParser
/* 14:   */   extends AbstractSingleBeanDefinitionParser
/* 15:   */ {
/* 16:   */   private static final String WEAVER_CLASS_ATTRIBUTE = "weaver-class";
/* 17:   */   private static final String ASPECTJ_WEAVING_ATTRIBUTE = "aspectj-weaving";
/* 18:   */   private static final String DEFAULT_LOAD_TIME_WEAVER_CLASS_NAME = "org.springframework.context.weaving.DefaultContextLoadTimeWeaver";
/* 19:   */   private static final String ASPECTJ_WEAVING_ENABLER_CLASS_NAME = "org.springframework.context.weaving.AspectJWeavingEnabler";
/* 20:   */   
/* 21:   */   protected String getBeanClassName(Element element)
/* 22:   */   {
/* 23:52 */     if (element.hasAttribute("weaver-class")) {
/* 24:53 */       return element.getAttribute("weaver-class");
/* 25:   */     }
/* 26:55 */     return "org.springframework.context.weaving.DefaultContextLoadTimeWeaver";
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/* 30:   */   {
/* 31:60 */     return "loadTimeWeaver";
/* 32:   */   }
/* 33:   */   
/* 34:   */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/* 35:   */   {
/* 36:65 */     builder.setRole(2);
/* 37:67 */     if (isAspectJWeavingEnabled(element.getAttribute("aspectj-weaving"), parserContext))
/* 38:   */     {
/* 39:68 */       RootBeanDefinition weavingEnablerDef = new RootBeanDefinition();
/* 40:69 */       weavingEnablerDef.setBeanClassName("org.springframework.context.weaving.AspectJWeavingEnabler");
/* 41:70 */       parserContext.getReaderContext().registerWithGeneratedName(weavingEnablerDef);
/* 42:72 */       if (isBeanConfigurerAspectEnabled(parserContext.getReaderContext().getBeanClassLoader())) {
/* 43:73 */         new SpringConfiguredBeanDefinitionParser().parse(element, parserContext);
/* 44:   */       }
/* 45:   */     }
/* 46:   */   }
/* 47:   */   
/* 48:   */   protected boolean isAspectJWeavingEnabled(String value, ParserContext parserContext)
/* 49:   */   {
/* 50:79 */     if ("on".equals(value)) {
/* 51:80 */       return true;
/* 52:   */     }
/* 53:82 */     if ("off".equals(value)) {
/* 54:83 */       return false;
/* 55:   */     }
/* 56:87 */     ClassLoader cl = parserContext.getReaderContext().getResourceLoader().getClassLoader();
/* 57:88 */     return cl.getResource("META-INF/aop.xml") != null;
/* 58:   */   }
/* 59:   */   
/* 60:   */   protected boolean isBeanConfigurerAspectEnabled(ClassLoader beanClassLoader)
/* 61:   */   {
/* 62:93 */     return ClassUtils.isPresent("org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect", 
/* 63:94 */       beanClassLoader);
/* 64:   */   }
/* 65:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.config.LoadTimeWeaverBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */