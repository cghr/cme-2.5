/*  1:   */ package org.springframework.scripting.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.MutablePropertyValues;
/*  4:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  5:   */ import org.springframework.beans.factory.config.TypedStringValue;
/*  6:   */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*  7:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  8:   */ import org.springframework.util.StringUtils;
/*  9:   */ import org.w3c.dom.Element;
/* 10:   */ 
/* 11:   */ public class ScriptingDefaultsParser
/* 12:   */   implements BeanDefinitionParser
/* 13:   */ {
/* 14:   */   private static final String REFRESH_CHECK_DELAY_ATTRIBUTE = "refresh-check-delay";
/* 15:   */   private static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
/* 16:   */   
/* 17:   */   public BeanDefinition parse(Element element, ParserContext parserContext)
/* 18:   */   {
/* 19:39 */     BeanDefinition bd = 
/* 20:40 */       LangNamespaceUtils.registerScriptFactoryPostProcessorIfNecessary(parserContext.getRegistry());
/* 21:41 */     String refreshCheckDelay = element.getAttribute("refresh-check-delay");
/* 22:42 */     if (StringUtils.hasText(refreshCheckDelay)) {
/* 23:43 */       bd.getPropertyValues().add("defaultRefreshCheckDelay", new Long(refreshCheckDelay));
/* 24:   */     }
/* 25:45 */     String proxyTargetClass = element.getAttribute("proxy-target-class");
/* 26:46 */     if (StringUtils.hasText(proxyTargetClass)) {
/* 27:47 */       bd.getPropertyValues().add("defaultProxyTargetClass", new TypedStringValue(proxyTargetClass, Boolean.class));
/* 28:   */     }
/* 29:49 */     return null;
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.config.ScriptingDefaultsParser
 * JD-Core Version:    0.7.0.1
 */