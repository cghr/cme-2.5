/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.env.MutablePropertySources;
/*  4:   */ import org.springframework.core.env.PropertySource.StubPropertySource;
/*  5:   */ import org.springframework.core.env.StandardEnvironment;
/*  6:   */ import org.springframework.jndi.JndiPropertySource;
/*  7:   */ 
/*  8:   */ public class StandardServletEnvironment
/*  9:   */   extends StandardEnvironment
/* 10:   */ {
/* 11:   */   public static final String SERVLET_CONTEXT_PROPERTY_SOURCE_NAME = "servletContextInitParams";
/* 12:   */   public static final String SERVLET_CONFIG_PROPERTY_SOURCE_NAME = "servletConfigInitParams";
/* 13:   */   public static final String JNDI_PROPERTY_SOURCE_NAME = "jndiProperties";
/* 14:   */   
/* 15:   */   protected void customizePropertySources(MutablePropertySources propertySources)
/* 16:   */   {
/* 17:82 */     propertySources.addLast(new PropertySource.StubPropertySource("servletConfigInitParams"));
/* 18:83 */     propertySources.addLast(new PropertySource.StubPropertySource("servletContextInitParams"));
/* 19:84 */     propertySources.addLast(new JndiPropertySource("jndiProperties"));
/* 20:85 */     super.customizePropertySources(propertySources);
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.StandardServletEnvironment
 * JD-Core Version:    0.7.0.1
 */