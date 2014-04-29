/*  1:   */ package org.springframework.context.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.MutablePropertyValues;
/*  4:   */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  5:   */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  6:   */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*  7:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  8:   */ import org.springframework.jmx.support.MBeanServerFactoryBean;
/*  9:   */ import org.springframework.jmx.support.WebSphereMBeanServerFactoryBean;
/* 10:   */ import org.springframework.jndi.JndiObjectFactoryBean;
/* 11:   */ import org.springframework.util.ClassUtils;
/* 12:   */ import org.springframework.util.StringUtils;
/* 13:   */ import org.w3c.dom.Element;
/* 14:   */ 
/* 15:   */ class MBeanServerBeanDefinitionParser
/* 16:   */   extends AbstractBeanDefinitionParser
/* 17:   */ {
/* 18:   */   private static final String MBEAN_SERVER_BEAN_NAME = "mbeanServer";
/* 19:   */   private static final String AGENT_ID_ATTRIBUTE = "agent-id";
/* 20:51 */   private static final boolean weblogicPresent = ClassUtils.isPresent(
/* 21:52 */     "weblogic.management.Helper", MBeanServerBeanDefinitionParser.class.getClassLoader());
/* 22:54 */   private static final boolean webspherePresent = ClassUtils.isPresent(
/* 23:55 */     "com.ibm.websphere.management.AdminServiceFactory", MBeanServerBeanDefinitionParser.class.getClassLoader());
/* 24:   */   
/* 25:   */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/* 26:   */   {
/* 27:60 */     String id = element.getAttribute("id");
/* 28:61 */     return StringUtils.hasText(id) ? id : "mbeanServer";
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext)
/* 32:   */   {
/* 33:66 */     String agentId = element.getAttribute("agent-id");
/* 34:67 */     if (StringUtils.hasText(agentId))
/* 35:   */     {
/* 36:68 */       RootBeanDefinition bd = new RootBeanDefinition(MBeanServerFactoryBean.class);
/* 37:69 */       bd.getPropertyValues().add("agentId", agentId);
/* 38:70 */       return bd;
/* 39:   */     }
/* 40:72 */     AbstractBeanDefinition specialServer = findServerForSpecialEnvironment();
/* 41:73 */     if (specialServer != null) {
/* 42:74 */       return specialServer;
/* 43:   */     }
/* 44:76 */     RootBeanDefinition bd = new RootBeanDefinition(MBeanServerFactoryBean.class);
/* 45:77 */     bd.getPropertyValues().add("locateExistingServerIfPossible", Boolean.TRUE);
/* 46:   */     
/* 47:   */ 
/* 48:80 */     bd.setRole(2);
/* 49:81 */     bd.setSource(parserContext.extractSource(element));
/* 50:82 */     return bd;
/* 51:   */   }
/* 52:   */   
/* 53:   */   static AbstractBeanDefinition findServerForSpecialEnvironment()
/* 54:   */   {
/* 55:86 */     if (weblogicPresent)
/* 56:   */     {
/* 57:87 */       RootBeanDefinition bd = new RootBeanDefinition(JndiObjectFactoryBean.class);
/* 58:88 */       bd.getPropertyValues().add("jndiName", "java:comp/env/jmx/runtime");
/* 59:89 */       return bd;
/* 60:   */     }
/* 61:91 */     if (webspherePresent) {
/* 62:92 */       return new RootBeanDefinition(WebSphereMBeanServerFactoryBean.class);
/* 63:   */     }
/* 64:95 */     return null;
/* 65:   */   }
/* 66:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.config.MBeanServerBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */