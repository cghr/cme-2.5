/*  1:   */ package org.springframework.context.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  5:   */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*  6:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  7:   */ import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
/*  8:   */ import org.springframework.util.StringUtils;
/*  9:   */ import org.w3c.dom.Element;
/* 10:   */ 
/* 11:   */ class MBeanExportBeanDefinitionParser
/* 12:   */   extends AbstractBeanDefinitionParser
/* 13:   */ {
/* 14:   */   private static final String MBEAN_EXPORTER_BEAN_NAME = "mbeanExporter";
/* 15:   */   private static final String DEFAULT_DOMAIN_ATTRIBUTE = "default-domain";
/* 16:   */   private static final String SERVER_ATTRIBUTE = "server";
/* 17:   */   private static final String REGISTRATION_ATTRIBUTE = "registration";
/* 18:   */   private static final String REGISTRATION_IGNORE_EXISTING = "ignoreExisting";
/* 19:   */   private static final String REGISTRATION_REPLACE_EXISTING = "replaceExisting";
/* 20:   */   
/* 21:   */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/* 22:   */   {
/* 23:59 */     return "mbeanExporter";
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext)
/* 27:   */   {
/* 28:64 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AnnotationMBeanExporter.class);
/* 29:   */     
/* 30:   */ 
/* 31:67 */     builder.setRole(2);
/* 32:68 */     builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
/* 33:   */     
/* 34:70 */     String defaultDomain = element.getAttribute("default-domain");
/* 35:71 */     if (StringUtils.hasText(defaultDomain)) {
/* 36:72 */       builder.addPropertyValue("defaultDomain", defaultDomain);
/* 37:   */     }
/* 38:75 */     String serverBeanName = element.getAttribute("server");
/* 39:76 */     if (StringUtils.hasText(serverBeanName))
/* 40:   */     {
/* 41:77 */       builder.addPropertyReference("server", serverBeanName);
/* 42:   */     }
/* 43:   */     else
/* 44:   */     {
/* 45:80 */       AbstractBeanDefinition specialServer = MBeanServerBeanDefinitionParser.findServerForSpecialEnvironment();
/* 46:81 */       if (specialServer != null) {
/* 47:82 */         builder.addPropertyValue("server", specialServer);
/* 48:   */       }
/* 49:   */     }
/* 50:86 */     String registration = element.getAttribute("registration");
/* 51:87 */     int registrationBehavior = 0;
/* 52:88 */     if ("ignoreExisting".equals(registration)) {
/* 53:89 */       registrationBehavior = 1;
/* 54:91 */     } else if ("replaceExisting".equals(registration)) {
/* 55:92 */       registrationBehavior = 2;
/* 56:   */     }
/* 57:94 */     builder.addPropertyValue("registrationBehavior", Integer.valueOf(registrationBehavior));
/* 58:   */     
/* 59:96 */     return builder.getBeanDefinition();
/* 60:   */   }
/* 61:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.config.MBeanExportBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */