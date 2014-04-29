/*  1:   */ package org.springframework.web.context;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletConfig;
/*  4:   */ import javax.servlet.ServletContext;
/*  5:   */ import org.springframework.context.ConfigurableApplicationContext;
/*  6:   */ 
/*  7:   */ public abstract interface ConfigurableWebApplicationContext
/*  8:   */   extends WebApplicationContext, ConfigurableApplicationContext
/*  9:   */ {
/* 10:45 */   public static final String APPLICATION_CONTEXT_ID_PREFIX = WebApplicationContext.class.getName() + ":";
/* 11:   */   public static final String SERVLET_CONFIG_BEAN_NAME = "servletConfig";
/* 12:   */   
/* 13:   */   public abstract void setServletContext(ServletContext paramServletContext);
/* 14:   */   
/* 15:   */   public abstract void setServletConfig(ServletConfig paramServletConfig);
/* 16:   */   
/* 17:   */   public abstract ServletConfig getServletConfig();
/* 18:   */   
/* 19:   */   public abstract void setNamespace(String paramString);
/* 20:   */   
/* 21:   */   public abstract String getNamespace();
/* 22:   */   
/* 23:   */   public abstract void setConfigLocation(String paramString);
/* 24:   */   
/* 25:   */   public abstract void setConfigLocations(String[] paramArrayOfString);
/* 26:   */   
/* 27:   */   public abstract String[] getConfigLocations();
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.ConfigurableWebApplicationContext
 * JD-Core Version:    0.7.0.1
 */